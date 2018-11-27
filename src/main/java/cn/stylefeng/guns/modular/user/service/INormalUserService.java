package cn.stylefeng.guns.modular.user.service;

import cn.stylefeng.guns.modular.system.model.NormalUser;
import cn.stylefeng.roses.core.datascope.DataScope;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 普通用户表 服务类
 * </p>
 *
 * @author wzh
 * @since 2018-11-27
 */
public interface INormalUserService extends IService<NormalUser> {
    public NormalUser getNormalUser(String username);

    int setStatus(int userid,int status);

    /**
     * 根据条件查询用户列表
     */
    List<Map<String, Object>> selectUsers(DataScope dataScope, String name, String beginTime, String endTime,Integer deptid);

}
