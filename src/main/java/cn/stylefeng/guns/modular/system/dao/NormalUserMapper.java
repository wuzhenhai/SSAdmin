package cn.stylefeng.guns.modular.system.dao;

import cn.stylefeng.guns.modular.system.model.NormalUser;
import cn.stylefeng.roses.core.datascope.DataScope;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 普通用户表 Mapper 接口
 * </p>
 *
 * @author wzh
 * @since 2018-11-27
 */
public interface NormalUserMapper extends BaseMapper<NormalUser> {

    /**
     * 通过账号获取用户
     */
    NormalUser getByAccount(@Param("account") String account);

    int setStatus(@Param("userid") int userid,@Param("status") int status);

    /**
     * 根据条件查询用户列表
     */
    List<Map<String, Object>> selectUsers(@Param("dataScope") DataScope dataScope, @Param("name") String name, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

}
