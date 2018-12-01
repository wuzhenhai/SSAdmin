package cn.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author wzh
 * @since 2018-12-01
 */
@TableName("sys_lesson_info")
public class LessonInfo extends Model<LessonInfo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField("lesson_name")
    private String lessonName;
    @TableField("lesson_begin_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date lessonBeginTime;
    @TableField("lesson_period")
    private Integer lessonPeriod;
    @TableField("lesson_class")
    private String lessonClass;
    @TableField("teacher_info")
    private String teacherInfo;
    /**
     * 前置课程
     */
    @TableField("pre_lesson_id")
    private Integer preLessonId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public Date getLessonBeginTime() {
        return lessonBeginTime;
    }

    public void setLessonBeginTime(Date lessonBeginTime) {
        this.lessonBeginTime = lessonBeginTime;
    }

    public Integer getLessonPeriod() {
        return lessonPeriod;
    }

    public void setLessonPeriod(Integer lessonPeriod) {
        this.lessonPeriod = lessonPeriod;
    }

    public String getLessonClass() {
        return lessonClass;
    }

    public void setLessonClass(String lessonClass) {
        this.lessonClass = lessonClass;
    }

    public String getTeacherInfo() {
        return teacherInfo;
    }

    public void setTeacherInfo(String teacherInfo) {
        this.teacherInfo = teacherInfo;
    }

    public Integer getPreLessonId() {
        return preLessonId;
    }

    public void setPreLessonId(Integer preLessonId) {
        this.preLessonId = preLessonId;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "LessonInfo{" +
        ", id=" + id +
        ", lessonName=" + lessonName +
        ", lessonBeginTime=" + lessonBeginTime +
        ", lessonPeriod=" + lessonPeriod +
        ", lessonClass=" + lessonClass +
        ", teacherInfo=" + teacherInfo +
        ", preLessonId=" + preLessonId +
        "}";
    }
}
