package cn.stylefeng.guns.modular.user.service.impl;

import cn.stylefeng.guns.modular.system.model.LessonStudent;
import cn.stylefeng.guns.modular.system.dao.LessonStudentMapper;
import cn.stylefeng.guns.modular.user.service.ILessonStudentService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wzh
 * @since 2018-12-03
 */
@Service
public class LessonStudentServiceImpl extends ServiceImpl<LessonStudentMapper, LessonStudent> implements ILessonStudentService {

    @Autowired
    LessonStudentMapper lessonStudentMapper;
    @Override
    public List<Map<String, Object>> getSelectStudentList(Integer lessonId) {
        return lessonStudentMapper.selectStudentList(lessonId);
    }
}
