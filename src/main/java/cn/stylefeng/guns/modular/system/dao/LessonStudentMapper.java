package cn.stylefeng.guns.modular.system.dao;

import cn.stylefeng.guns.modular.system.model.LessonStudent;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wzh
 * @since 2018-12-03
 */
public interface LessonStudentMapper extends BaseMapper<LessonStudent> {
    //根据课程id 获取选课学生详情列表
    List<Map<String,Object>> selectStudentList(@Param("lessonId") Integer lessonId);
}
