/**
 * userlist管理初始化
 */
var NormalUser = {
    id: "NormalUserTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1,
    params:{}
};

/**
 * 初始化表格的列
 */
NormalUser.initColumn = function () {
    return [
        {field: 'selectItem', checkbox: true},
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

/**
 * 检查是否选中
 */
NormalUser.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        NormalUser.seItem = selected;
        return true;
    }
};

function getStudentListLi(id,name){
    return "<li class='studentName'>" + name + "<button class='delBtn' onclick='NormalUser.delStudent(" + id + ",\"" + name +"\")' >删除</button></li>"
}


NormalUser.delStudent = function(id,name){
    if(confirm("确定删除[" + name + "]的选课？")){
        this.params["id"] = id;
        this.params["lessonId"] = lessonId;
        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/lessonInfo/delStudent", function(data){

            Feng.success("添加成功!");
            $("#selectStudentList").find("ul").html("");
            //动态添加选课客户
            for(var i=0;i<data.data.length;i++){
                var obj = data.data[i];
                $("#selectStudentList").find("ul").append(getStudentListLi(obj['userid'],obj['username']))
            }
        },function(data){
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set(this.params);
        ajax.start();
    }
};


NormalUser.addToLesson = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        var arrays = new Array();// 声明一个数组
        $(selected).each(function () {// 通过获得别选中的来进行遍历
            arrays.push(this.id);// cid为获得到的整条数据中的一列
        });
        var ids = arrays.join(','); // 获得要删除的id
        console.log(ids);
        this.params["ids"] = ids;
        this.params["lessonId"] = lessonId;

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/lessonInfo/addToLessonByIds", function(data){

            Feng.success("添加成功!");
            $("input[name='btSelectItem']").prop("checked",false);
            $("#selectStudentList").find("ul").html("");
            //动态添加选课客户
            for(var i=0;i<data.data.length;i++){
                var obj = data.data[i];
                $("#selectStudentList").find("ul").append(getStudentListLi(obj['userid'],obj['username']))
            }
            // location.reload();
            // window.parent.NormalUser.table.refresh();
            // NormalUserInfoDlg.close();
        },function(data){
            Feng.error("添加失败!" + data.responseJSON.message + "!");
        });
        ajax.set(this.params);
        ajax.start();
        return true;
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
