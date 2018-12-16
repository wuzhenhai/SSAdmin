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
        {
            title: '操作',
            field: 'id',
            align: 'center',
            valign: 'middle',
            width: '6%',
            formatter: operateFormatter //自定义方法，添加操作按钮
        }

            // {title: '受洗时间', field: 'baptizedtime', visible: true, align: 'center', valign: 'middle'},
    ];
};

function operateFormatter (value, row, index) {
    var id = value;
    var result = "";
    result += "<a onclick=\"ViewUserById(" + id + ")\" title='查看'>详情</a>";

    return result;


}

function ViewUserById(id) {
    var index = layer.open({
        type: 2,
        title: '用户详情',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/normalUser/view/' + id
    });
    this.layerIndex = index;
}

NormalUser.downloadTemplateForUserList = function () {
  location.href = templateUrl + "";
};

NormalUser.uploadExcel = function () {
    $("#i_file_upload").click();
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

NormalUser.saveToExcel = function () {
    // if (this.check()) {
        if(confirm("是否确定导出?")){
            var ajax = new $ax(Feng.ctxPath + "/normalUser/saveToExcel", function (data) {
                Feng.success("导出成功!");
                var downloadUrl = data.data;
                location.href = downloadUrl;
            }, function (data) {
                // Feng.error("导出失败!" + data.responseJSON.message + "!");
                Feng.error("导出失败!");
            });
            ajax.set("name", $("#s_name").val());
            ajax.set("deptid",NormalUser.deptid);
            ajax.start();
        }
    // }
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

//文件上传事件
function prod_data_change(){
    var formData = new FormData();
    var name = $("#i_file_upload").val();
    var file = $("#i_file_upload").prop("files")[0];
    formData.append("file",file);
    formData.append("name",name);
    $.ajax({
        url : Feng.ctxPath  + '/normalUser/uploadExcel',
        type : 'POST',
        async : false,
        dataType:"json",
        data : formData,
        // 告诉jQuery不要去处理发送的数据
        processData : false,
        // 告诉jQuery不要去设置Content-Type请求头
        contentType : false,
        beforeSend:function(){
            console.log("正在进行，请稍候");
        },
        success : function(ret) {
            if(ret.code != -1){
                Feng.success("导入成功!");
                NormalUser.table.refresh();
            }else{
                Feng.error("导入失败!");
            }
        }
    });
}

$(function () {
    var defaultColunms = NormalUser.initColumn();
    var table = new BSTable(NormalUser.id, "/normalUser/list", defaultColunms);
    table.setPaginationType("client");
    NormalUser.table = table.init();

    var ztree = new $ZTree("deptTree", "/dept/treeByTopId?topTreeId=28");
    ztree.bindOnClick(NormalUser.onClickDept);
    ztree.init();

});
