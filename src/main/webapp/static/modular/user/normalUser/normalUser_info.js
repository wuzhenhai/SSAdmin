/**
 * 初始化普通用户详情对话框
 */
var NormalUserInfoDlg = {
    normalUserInfoData : {}
};

/**
 * 清除数据
 */
NormalUserInfoDlg.clearData = function() {
    this.normalUserInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
NormalUserInfoDlg.set = function(key, val) {
    this.normalUserInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
NormalUserInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
NormalUserInfoDlg.close = function() {
    parent.layer.close(window.parent.NormalUser.layerIndex);
}

/**
 * 收集数据
 */
NormalUserInfoDlg.collectData = function() {
    this.set('id')
    .set('account')
    .set('password')
    .set('salt')
    .set('name')
    .set('birthday')
    .set('sex')
    .set('email')
    .set('phone')
    .set('roleid')
    .set('deptid')
    .set('status')
    .set('createtime')
    .set('version')
    .set('deptstr')
    .set('idcard')
    .set('married')
    .set('edu')
    .set('address')
    .set('believetime')
    .set('baptizedtime')
    .set('family');
}

/**
 * 提交添加
 */
NormalUserInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/normalUser/add", function(data){
        Feng.success("添加成功!");
        window.parent.NormalUser.table.refresh();
        NormalUserInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.normalUserInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
NormalUserInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/normalUser/update", function(data){
        Feng.success("修改成功!");
        window.parent.NormalUser.table.refresh();
        NormalUserInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.normalUserInfoData);
    ajax.start();
}

$(function() {

});
