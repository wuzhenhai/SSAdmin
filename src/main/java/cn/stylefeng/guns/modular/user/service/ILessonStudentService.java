package cn.stylefeng.guns.modular.user.service;

import cn.stylefeng.guns.modular.system.model.LessonStudent;
import com.baomidou.mybatisplus.service.IService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wzh
 * @since 2018-12-03
 */
public interface ILessonStudentService extends IService<LessonStudent> {
    List<Map<String,Object>> getSelectStudentList(Integer lessonId);
}
