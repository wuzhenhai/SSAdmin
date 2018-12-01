package cn.stylefeng.guns.modular.user.controller;

import cn.stylefeng.guns.core.common.constant.factory.ConstantFactory;
import cn.stylefeng.guns.modular.system.warpper.LessonWarpper;
import cn.stylefeng.roses.core.base.controller.BaseController;
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
    @RequestMapping(value = "/detail/{lessonInfoId}")
    @ResponseBody
    public Object detail(@PathVariable("lessonInfoId") Integer lessonInfoId) {
        return lessonInfoService.selectById(lessonInfoId);
    }
}
