package cn.stylefeng.guns.modular.user.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.stylefeng.guns.core.common.annotion.BussinessLog;
import cn.stylefeng.guns.core.common.constant.cache.CacheKey;
import cn.stylefeng.guns.core.common.constant.factory.ConstantFactory;
import cn.stylefeng.guns.core.common.constant.state.ManagerStatus;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.core.util.PrintUtil;
import cn.stylefeng.guns.modular.system.model.LessonStudent;
import cn.stylefeng.guns.modular.system.model.User;
import cn.stylefeng.guns.modular.system.model.UserExcelEntity;
import cn.stylefeng.guns.modular.system.warpper.NormalUserWarpper;
import cn.stylefeng.guns.modular.system.warpper.UserWarpper;
import cn.stylefeng.guns.modular.user.service.ILessonStudentService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import cn.stylefeng.guns.modular.system.model.NormalUser;
import cn.stylefeng.guns.modular.user.service.INormalUserService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 普通用户控制器
 *
 * @author fengshuonan
 * @Date 2018-11-27 10:29:55
 */
@Controller
@RequestMapping("/normalUser")
public class NormalUserController extends BaseController {

    private String PREFIX = "/user/normalUser/";

    @Autowired
    private INormalUserService normalUserService;
    @Autowired
    private ILessonStudentService lessonStudentService;
    @Autowired
    private CacheManager cacheManager;
    //读取配置文件 excelTemplatePath.userlist 参数值
    @Value("${excelTemplatePath.userlist}")
    private String userListTempPath;
    @Value("${savePath}")
    private String savePath;
    @Value("${downloadHost}")
    private String downloadHost;
    /**
     * 跳转到普通用户首页
     */
    @RequestMapping("")
    public String index(ModelMap modelMap) {
        modelMap.addAttribute("templateUrl",downloadHost + "userInputTemplate.xls");
        return PREFIX + "normalUser.html";
    }

    /**
     * 跳转到添加选课用户首页
     */
    @RequestMapping("select/{lessonId}")
    public String select(@PathVariable Integer lessonId,ModelMap modelMap) {
        List<LessonStudent> lessonStudentList = lessonStudentService.selectList(new EntityWrapper<LessonStudent>().eq("lessonid",lessonId));
        if(lessonStudentList == null){
            lessonStudentList = new ArrayList<>();
        }else {
            //插入学生名称
            for(LessonStudent lessonStudent : lessonStudentList){
                lessonStudent.setUsername(ConstantFactory.me().getNormalUserNameById(lessonStudent.getUserid()));
            }
        }
        modelMap.addAttribute("lessonId",lessonId);
        modelMap.addAttribute("lessonStudentList",lessonStudentList);
        return PREFIX + "normalUserSelect.html";
    }

    /**
     * 转excel导出
     */
    @RequestMapping("/saveToExcel")
    @ResponseBody
    public Object saveToExcel(@RequestParam(required = false) String name,@RequestParam(required = false) Integer deptid) throws IOException {
        List<Map<String, Object>> users = normalUserService.selectUsers(null,name,null,null,deptid);
        List<Map<String, Object>> retUsers = new NormalUserWarpper(users).wrap();

        TemplateExportParams params = new TemplateExportParams(
                userListTempPath);
        Map<String, Object> map = new HashMap<String, Object>();
        SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd");
        String time = st.format(new Date());
        map.put("time", time);
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        for (int i = 0; i < retUsers.size(); i++) {
            Map<String ,Object> old = retUsers.get(i);
            Map<String, String> lm = new HashMap<String, String>();
            lm.put("id", i + 1 + "");
            lm.put("name",old.get("name").toString());
            lm.put("sexName",old.get("sexName").toString());
            lm.put("eduName",old.get("eduName").toString());
            lm.put("marriedName",old.get("marriedName").toString());
            lm.put("address",old.get("address").toString());
            lm.put("deptstr",old.get("deptstr").toString());
            lm.put("birthday",old.get("birthday").toString().split(" ")[0]);
            lm.put("no",old.get("id").toString());
            listMap.add(lm);
        }
        map.put("maplist", listMap);
        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        String fileName = new Date().getTime() + "";
        String filePath = PrintUtil.saveToExcel(workbook,savePath,fileName);
        String downloadUrl = downloadHost + fileName + ".xls";
        SUCCESS_TIP.setData(downloadUrl);
        return SUCCESS_TIP;
    }

    /**
     * 从excel导入用户信息
     */
    @RequestMapping("/uploadExcel")
    @ResponseBody
    public Object uploadExcel(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
        ImportParams params = new ImportParams();
        params.setHeadRows(1);
        List<UserExcelEntity> list = ExcelImportUtil.importExcel(
                file.getInputStream(),UserExcelEntity.class, params);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");

        List<NormalUser> normalUserList = new ArrayList<>();

        for (UserExcelEntity user : list){
            if(user.getAccount() !=null && user.getName() != null){
                NormalUser normalUser = new NormalUser();
                normalUser.setAccount(user.getAccount());
                normalUser.setCreatetime(new Date());
                normalUser.setName(user.getName());
                normalUser.setBaptizedtime(user.getBaptizedtime());
                normalUser.setBelievetime(user.getBelievetime());
                normalUser.setSex(user.getSex());
                normalUser.setDeptid(user.getDeptid());
                normalUser.setAddress(user.getAccount());
                normalUser.setPhone(user.getPhone());
                //设置默认密码111111
                normalUser.setPassword("527220889cdfb7bf45788388f5eb5102");
                normalUser.setSalt("p3k21");
                //设置状态为启用
                normalUser.setStatus(1);
                //String dateStr = sd.format(user.getBirthday());
                normalUserList.add(normalUser);
            }
        }
        Map<String,Object> res = new HashMap<>();


        boolean ret = normalUserService.insertBatch(normalUserList);
        if (ret){
            res.put("code",200);
            res.put("msg","导入成功");
        }else{
            res.put("code",-1);
            res.put("msg","导入失败");
        }
        return res;
    }


    /**
     * 跳转到添加普通用户
     */
    @RequestMapping("/normalUser_add")
    public String normalUserAdd() {
        return PREFIX + "normalUser_add.html";
    }

    /**
     * 跳转到修改普通用户
     */
    @RequestMapping("/normalUser_update/{normalUserId}")
    public String normalUserUpdate(@PathVariable Integer normalUserId, Model model) {
        NormalUser normalUser = normalUserService.selectById(normalUserId);
        model.addAttribute("item",normalUser);
        model.addAttribute("deptName", ConstantFactory.me().getDeptName(normalUser.getDeptid()));
        LogObjectHolder.me().set(normalUser);
        return PREFIX + "normalUser_edit.html";
    }

    /**
     * 获取普通用户列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String name, @RequestParam(required = false) String beginTime, @RequestParam(required = false) String endTime,@RequestParam(required = false) Integer deptid) {
        List<Map<String, Object>> users = normalUserService.selectUsers(null,name,beginTime,endTime,deptid);
        return new NormalUserWarpper(users).wrap();
    }

    /**
     * 新增普通用户
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(NormalUser normalUser) {

        // 判断账号是否重复
        NormalUser theUser = normalUserService.getNormalUser(normalUser.getAccount());
        if(theUser != null){
            throw new ServiceException(BizExceptionEnum.USER_ALREADY_REG);
        }

        // 完善账号信息
        normalUser.setSalt(ShiroKit.getRandomSalt(5));
        normalUser.setPassword(ShiroKit.md5(normalUser.getPassword(), normalUser.getSalt()));
        normalUser.setStatus(ManagerStatus.OK.getCode());
        normalUser.setCreatetime(new Date());

        normalUserService.insert(normalUser);


        return SUCCESS_TIP;
    }

    /**
     * 删除普通用户
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer normalUserId) {
        normalUserService.setStatus(normalUserId,ManagerStatus.DELETED.getCode());
        return SUCCESS_TIP;
    }

    /**
     * 修改普通用户
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(NormalUser normalUser) {
        Cache cache = cacheManager.getCache("CONSTANT");
        String key = CacheKey.NORMALUSER_NAME + normalUser.getId();
        Element element= cache.get( key );
        //如果已经存在的话，移除缓存，新加新数据
        if(element != null){
            cache.remove(element);
            cache.put(new Element(key,normalUser.getName()));
        }
        normalUserService.updateById(normalUser);
        return SUCCESS_TIP;
    }

    /**
     * 普通用户详情
     */
    @RequestMapping(value = "/detail/{normalUserId}")
    @ResponseBody
    public Object detail(@PathVariable("normalUserId") Integer normalUserId) {
        return normalUserService.selectById(normalUserId);
    }

    /**
     * 普通用户详情
     */
    @RequestMapping(value = "/view/{normalUserId}")
    public String view(@PathVariable("normalUserId") Integer normalUserId, ModelMap model) {
        NormalUser normalUser = normalUserService.selectById(normalUserId);
        model.addAttribute("deptName", ConstantFactory.me().getDeptName(normalUser.getDeptid()));
        model.addAttribute("statusName", ConstantFactory.me().getStatusName(normalUser.getStatus()));
        model.addAttribute("sexName", ConstantFactory.me().getSexName(normalUser.getSex()));
        model.addAttribute("eduName", ConstantFactory.me().getDictsByName("学历",Integer.parseInt(normalUser.getEdu())));
        model.addAttribute("marriedName", ConstantFactory.me().getDictsByName("婚姻状态",normalUser.getMarried()));
        model.addAttribute("item",normalUser);
        LogObjectHolder.me().set(normalUser);
        return PREFIX + "normalUser_view.html";
    }


}
