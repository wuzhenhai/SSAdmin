/**
 * 初始化普通用户详情对话框
 */
var NormalUserInfoDlg = {
    normalUserInfoData : {},
    citySel: {
        validators: {
            notEmpty: {
                message: '部门不能为空'
            }
        }
    },
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
    .set('avatar')
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

/**
 * 显示部门选择的树
 *
 * @returns
 */
NormalUserInfoDlg.showDeptSelectTree = function () {
    var cityObj = $("#citySel");
    var cityOffset = $("#citySel").offset();
    $("#menuContent").css({
        left: cityOffset.left + "px",
        top: cityOffset.top + cityObj.outerHeight() + "px"
    }).slideDown("fast");

    $("body").bind("mousedown", onBodyDown);
};

function onBodyDown(event) {
    if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(
            event.target).parents("#menuContent").length > 0)) {
        NormalUserInfoDlg.hideDeptSelectTree();
    }
}


/**
 * 隐藏部门选择的树
 */
NormalUserInfoDlg.hideDeptSelectTree = function () {
    $("#menuContent").fadeOut("fast");
    $("body").unbind("mousedown", onBodyDown);// mousedown当鼠标按下就可以触发，不用弹起
};

NormalUserInfoDlg.onClickDept = function (e, treeId, treeNode) {
    $("#citySel").attr("value", instance.getSelectedVal());
    $("#deptid").attr("value", treeNode.id);
};

$(function() {
    var ztree = new $ZTree("treeDemo", "/dept/treeByTopId?topTreeId=28");
    ztree.bindOnClick(NormalUserInfoDlg.onClickDept);
    ztree.init();
    instance = ztree;

    // 初始化头像上传
    var avatarUp = new $WebUpload("avatar");
    // avatarUp.setUploadBarId("progressBar");
    avatarUp.init();
});
