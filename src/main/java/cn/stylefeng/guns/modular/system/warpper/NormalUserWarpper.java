/**
 * Copyright 2018-2020 stylefeng & fengshuonan (https://gitee.com/stylefeng)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.stylefeng.guns.modular.system.warpper;

import cn.stylefeng.guns.core.common.constant.factory.ConstantFactory;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.plugins.Page;

import java.util.List;
import java.util.Map;

/**
 * 用户管理的包装类
 *
 * @author fengshuonan
 * @date 2017年2月13日 下午10:47:03
 */
public class NormalUserWarpper extends BaseControllerWrapper {

    public NormalUserWarpper(Map<String, Object> single) {
        super(single);
    }

    public NormalUserWarpper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public NormalUserWarpper(Page<Map<String, Object>> page) {
        super(page);
    }

    public NormalUserWarpper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        map.put("sexName", ConstantFactory.me().getSexName((Integer) map.get("sex")));
        map.put("statusName", ConstantFactory.me().getStatusName((Integer) map.get("status")));
//        map.put("deptName", ConstantFactory.me().getDeptName((Integer) map.get("deptid")));
        map.put("marriedName", ConstantFactory.me().getDictsByName("婚姻状态",(Integer) map.get("married")));
        map.put("eduName", ConstantFactory.me().getDictsByName("学历",(Integer) map.get("edu")));
        try{
            map.put("deptName", ConstantFactory.me().getDeptName((Integer) map.get("deptid")));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
