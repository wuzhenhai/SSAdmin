package cn.stylefeng.guns.modular.user.controller;

import cn.stylefeng.guns.core.common.constant.factory.ConstantFactory;
import cn.stylefeng.guns.modular.system.model.LessonStudent;
import cn.stylefeng.guns.modular.system.warpper.LessonStudentWarpper;
import cn.stylefeng.guns.modular.system.warpper.LessonWarpper;
import cn.stylefeng.guns.modular.user.service.ILessonStudentService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.beust.jcommander.Parameter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import cn.stylefeng.guns.modular.system.model.LessonInfo;
import cn.stylefeng.guns.modular.user.service.ILessonInfoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 课程管理控制器
 *
 * @author fengshuonan
 * @Date 2018-12-01 20:26:32
 */
@Controller
@RequestMapping("/lessonInfo")
public class LessonInfoController extends BaseController {

    private String PREFIX = "/user/lessonInfo/";

    @Autowired
    private ILessonInfoService lessonInfoService;
    @Autowired
    private ILessonStudentService lessonStudentService;

    /**
     * 跳转到课程管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "lessonInfo.html";
    }

    /**
     * 跳转到添加课程管理
     */
    @RequestMapping("/lessonInfo_add")
    public String lessonInfoAdd(ModelMap modelMap) {
        List<LessonInfo> list = lessonInfoService.selectList(null);
        modelMap.addAttribute("lessonList",list);
        return PREFIX + "lessonInfo_add.html";
    }

    /**
     * 跳转到修改课程管理
     */
    @RequestMapping("/lessonInfo_update/{lessonInfoId}")
    public String lessonInfoUpdate(@PathVariable Integer lessonInfoId, Model model) {
        LessonInfo lessonInfo = lessonInfoService.selectById(lessonInfoId);
        //获取课程列表
        List<LessonInfo> list = lessonInfoService.selectList(null);
        model.addAttribute("item",lessonInfo);
        model.addAttribute("lessonList",list);
//        model.addAttribute("preLessonName", ConstantFactory.me().getLessonName(Integer.parseInt(lessonInfo.getPreLessonId())));
        LogObjectHolder.me().set(lessonInfo);
        return PREFIX + "lessonInfo_edit.html";
    }

    /**
     * 获取课程管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        List<Map<String, Object>> lessonMapList = lessonInfoService.selectMaps(null);
        return new LessonWarpper(lessonMapList).wrap();
    }

    /**
     * 新增课程管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(LessonInfo lessonInfo) {
        lessonInfoService.insert(lessonInfo);
        return SUCCESS_TIP;
    }

    /**
     * 删除选课学生
     */
    @RequestMapping(value = "/delStudent")
    @ResponseBody
    public Object delStudent(@RequestParam Integer id,@RequestParam Integer lessonId) {
        //删除选课学生
        boolean ret = lessonStudentService.delete(new EntityWrapper<LessonStudent>()
                .eq("lessonid",lessonId)
                .and()
                .eq("userid",id));

        if(ret){

            List<LessonStudent> lessonStudentList = lessonStudentService.selectList(new EntityWrapper<LessonStudent>().eq("lessonid",lessonId));
            if(lessonStudentList == null){
                lessonStudentList = new ArrayList<>();
            }else {
                //插入学生名称
                for(LessonStudent lessonStudent : lessonStudentList){
                    lessonStudent.setUsername(ConstantFactory.me().getNormalUserNameById(lessonStudent.getUserid()));
                }
            }

            SUCCESS_TIP.setData(lessonStudentList);
            return SUCCESS_TIP;
        }else{
            throw new ServiceException(500,"删除错误");
        }
    }

    /**
     * 新增选课学生
     */
    @RequestMapping(value = "/addToLessonByIds")
    @ResponseBody
    public Object addToLessonByIds(@RequestParam String ids,@RequestParam Integer lessonId) {

        //获取选课学生id
        String[] idsArr = ids.split(",");
        List<LessonStudent> lessonStudents = new ArrayList<>();
        for(String idStr:idsArr){
            int userid = Integer.parseInt(idStr);
            LessonStudent ls = lessonStudentService.selectOne(new EntityWrapper<LessonStudent>().eq("userid",userid).and().eq("lessonid",lessonId));
            //没有找到相同的用户添加，反之跳过
            if(ls == null){
                ls = new LessonStudent();
                ls.setUserid(userid);
                ls.setLessonid(lessonId);
                //加入带插入列表
                lessonStudents.add(ls);
            }

        }

        boolean ret = true;
        //插入到数据库
        if(lessonStudents.size()>0) {
            ret = lessonStudentService.insertBatch(lessonStudents);
        }

        List<LessonStudent> lessonStudentList = lessonStudentService.selectList(new EntityWrapper<LessonStudent>().eq("lessonid",lessonId));
        if(lessonStudentList == null){
            lessonStudentList = new ArrayList<>();
        }else {
            //插入学生名称
            for(LessonStudent lessonStudent : lessonStudentList){
                lessonStudent.setUsername(ConstantFactory.me().getNormalUserNameById(lessonStudent.getUserid()));
            }
        }



        if(ret){
            SUCCESS_TIP.setData(lessonStudentList);
            return SUCCESS_TIP;
        }else{
            throw new ServiceException(500,"添加错误");
        }

    }

    /**
     * 删除课程管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer lessonInfoId) {
        lessonInfoService.deleteById(lessonInfoId);
        return SUCCESS_TIP;
    }

    /**
     * 修改课程管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(LessonInfo lessonInfo) {
        lessonInfoService.updateById(lessonInfo);
        return SUCCESS_TIP;
    }

    /**
     * 课程管理详情
     */
//    @RequestMapping(value = "/detail/{lessonInfoId}")
//    @ResponseBody
//    public Object detail(@PathVariable("lessonInfoId") Integer lessonInfoId) {
//        return lessonInfoService.selectById(lessonInfoId);
//    }
    /**
     * 课程管理详情
     */
    @RequestMapping(value = "/detail/{lessonId}")
    public String detail(@PathVariable("lessonId") Integer lessonId,ModelMap modelMap) {
        List<LessonStudent> lessonStudentList = lessonStudentService.selectList(new EntityWrapper<LessonStudent>().eq("lessonid",lessonId));
        if(lessonStudentList == null){
            lessonStudentList = new ArrayList<>();
        }else {
            //插入学生名称
            for(LessonStudent lessonStudent : lessonStudentList){
                lessonStudent.setUsername(ConstantFactory.me().getNormalUserNameById(lessonStudent.getUserid()));
            }
        }

        LessonInfo lessonInfo = lessonInfoService.selectById(lessonId);

        //获取前置课程
        List<LessonInfo> list = lessonInfoService.selectList(null);
        modelMap.addAttribute("lessonList",list);

        modelMap.addAttribute("lessonId",lessonId);
        modelMap.addAttribute("item",lessonInfo);
        modelMap.addAttribute("lessonStudentList",lessonStudentList);
        return PREFIX + "lessonInfo_detail.html";
    }

    /**
     * 课程管理详情
     */
    @RequestMapping(value = "/selectList")
    @ResponseBody
    public Object selectList(@RequestParam Integer lessonId) {
//        List<Map<String, Object>> lessonStudentList = lessonStudentService.selectMaps(new EntityWrapper<LessonStudent>().eq("lessonid",lessonId));
        List<Map<String,Object>> lessonStudentList = lessonStudentService.getSelectStudentList(lessonId);
        return new LessonStudentWarpper(lessonStudentList).wrap();
    }
}
