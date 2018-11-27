/**
 * userlist管理初始化
 */
var NormalUser = {
    id: "NormalUserTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
NormalUser.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
            {title: '账号', field: 'account', visible: true, align: 'center', valign: 'middle'},
            {title: '名字', field: 'name', visible: true, align: 'center', valign: 'middle'},
            // {title: '生日', field: 'birthday', visible: true, align: 'center', valign: 'middle'},
            {title: '性别', field: 'sexName', visible: true, align: 'center', valign: 'middle'},
            {title: '部门', field: 'deptName', visible: true, align: 'center', valign: 'middle'},
            {title: '电话', field: 'phone', visible: true, align: 'center', valign: 'middle'},
            {title: '状态', field: 'statusName', visible: true, align: 'center', valign: 'middle'},
            {title: '职位', field: 'deptstr', visible: true, align: 'center', valign: 'middle'},
            {title: '学历', field: 'eduName', visible: true, align: 'center', valign: 'middle'},
            // {title: '受洗时间', field: 'baptizedtime', visible: true, align: 'center', valign: 'middle'},
    ];
};

/**
 * 检查是否选中
 */
NormalUser.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        NormalUser.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加普通用户
 */
NormalUser.openAddNormalUser = function () {
    var index = layer.open({
        type: 2,
        title: '添加普通用户',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/normalUser/normalUser_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看普通用户详情
 */
NormalUser.openNormalUserDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '普通用户详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/normalUser/normalUser_update/' + NormalUser.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除普通用户
 */
NormalUser.delete = function () {
    if (this.check()) {
        if(confirm("是否确定删除?")){
            var ajax = new $ax(Feng.ctxPath + "/normalUser/delete", function (data) {
                Feng.success("删除成功!");
                NormalUser.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("normalUserId",this.seItem.id);
            ajax.start();
        }
    }
};

/**
 * 查询普通用户列表
 */
NormalUser.search = function () {
    var queryData = {};
    queryData['name'] = $("#s_name").val();
    queryData['deptid'] = NormalUser.deptid;
    NormalUser.table.refresh({query: queryData});
};

NormalUser.onClickDept = function (e, treeId, treeNode) {
    NormalUser.deptid = treeNode.id;
    NormalUser.search();
};

$(function () {
    var defaultColunms = NormalUser.initColumn();
    var table = new BSTable(NormalUser.id, "/normalUser/list", defaultColunms);
    table.setPaginationType("client");
    NormalUser.table = table.init();

    var ztree = new $ZTree("deptTree", "/dept/treeByTopId?topTreeId=28");
    ztree.bindOnClick(NormalUser.onClickDept);
    ztree.init();
});
