package cn.stylefeng.guns.modular.user.service.impl;

import cn.stylefeng.guns.modular.system.model.NormalUser;
import cn.stylefeng.guns.modular.system.dao.NormalUserMapper;
import cn.stylefeng.guns.modular.user.service.INormalUserService;
import cn.stylefeng.roses.core.datascope.DataScope;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 普通用户表 服务实现类
 * </p>
 *
 * @author wzh
 * @since 2018-11-27
 */
@Service
public class NormalUserServiceImpl extends ServiceImpl<NormalUserMapper, NormalUser> implements INormalUserService {

    @Override
    public NormalUser getNormalUser(String account) {
            return this.baseMapper.getByAccount(account);
    }

    @Override
    public int setStatus(int userid, int status) {
        return this.baseMapper.setStatus(userid,status);
    }

    @Override
    public List<Map<String, Object>> selectUsers(DataScope dataScope, String name, String beginTime, String endTime) {
        return this.baseMapper.selectUsers(dataScope, name, beginTime, endTime);
    }


}
