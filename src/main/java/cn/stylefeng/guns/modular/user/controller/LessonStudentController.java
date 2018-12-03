package cn.stylefeng.guns.modular.user.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import cn.stylefeng.guns.modular.system.model.LessonStudent;
import cn.stylefeng.guns.modular.user.service.ILessonStudentService;

/**
 * 选课学生控制器
 *
 * @author fengshuonan
 * @Date 2018-12-03 22:37:32
 */
@Controller
@RequestMapping("/lessonStudent")
public class LessonStudentController extends BaseController {

    private String PREFIX = "/user/lessonStudent/";

    @Autowired
    private ILessonStudentService lessonStudentService;

    /**
     * 跳转到选课学生首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "lessonStudent.html";
    }

    /**
     * 跳转到添加选课学生
     */
    @RequestMapping("/lessonStudent_add")
    public String lessonStudentAdd() {
        return PREFIX + "lessonStudent_add.html";
    }

    /**
     * 跳转到修改选课学生
     */
    @RequestMapping("/lessonStudent_update/{lessonStudentId}")
    public String lessonStudentUpdate(@PathVariable Integer lessonStudentId, Model model) {
        LessonStudent lessonStudent = lessonStudentService.selectById(lessonStudentId);
        model.addAttribute("item",lessonStudent);
        LogObjectHolder.me().set(lessonStudent);
        return PREFIX + "lessonStudent_edit.html";
    }

    /**
     * 获取选课学生列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return lessonStudentService.selectList(null);
    }

    /**
     * 新增选课学生
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(LessonStudent lessonStudent) {
        lessonStudentService.insert(lessonStudent);
        return SUCCESS_TIP;
    }

    /**
     * 删除选课学生
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer lessonStudentId) {
        lessonStudentService.deleteById(lessonStudentId);
        return SUCCESS_TIP;
    }

    /**
     * 修改选课学生
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(LessonStudent lessonStudent) {
        lessonStudentService.updateById(lessonStudent);
        return SUCCESS_TIP;
    }

    /**
     * 选课学生详情
     */
    @RequestMapping(value = "/detail/{lessonStudentId}")
    @ResponseBody
    public Object detail(@PathVariable("lessonStudentId") Integer lessonStudentId) {
        return lessonStudentService.selectById(lessonStudentId);
    }
}
