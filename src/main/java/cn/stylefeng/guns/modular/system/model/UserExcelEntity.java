package cn.stylefeng.guns.modular.system.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

@Data
public class UserExcelEntity {
    @Excel(name="账号")
    private String account;
    @Excel(name="姓名")
    private String name;
    @Excel(name="生日",exportFormat = "yyyy-MM-dd")
    private Date birthday;
    @Excel(name="性别")
    private Integer sex;
    @Excel(name="邮箱")
    private String email;
    @Excel(name="手机号码")
    private String phone;
    @Excel(name="部门编号")
    private Integer deptid;
    @Excel(name="岗位")
    private String deptstr;
    @Excel(name="是否已婚")
    private Integer married;
    @Excel(name="学历")
    private Integer edu;
    @Excel(name="家庭住址")
    private Integer address;
    @Excel(name = "信主时间",exportFormat = "yyyy-MM-dd")
    private Date believetime;
    @Excel(name = "受洗时间",exportFormat = "yyyy-MM-dd")
    private Date baptizedtime;

}
