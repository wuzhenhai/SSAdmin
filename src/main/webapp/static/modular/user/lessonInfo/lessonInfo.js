/**
 * 课程管理管理初始化
 */
var LessonInfo = {
    id: "LessonInfoTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
LessonInfo.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '课程编号', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '课程名称', field: 'lessonName', visible: true, align: 'center', valign: 'middle'},
            {title: '开课时间', field: 'lessonBeginTime', visible: true, align: 'center', valign: 'middle'},
            {title: '课时', field: 'lessonPeriod', visible: true, align: 'center', valign: 'middle'},
            {title: '教室', field: 'lessonClass', visible: true, align: 'center', valign: 'middle'},
            {title: '老师信息', field: 'teacherInfo', visible: true, align: 'center', valign: 'middle'},
            {title: '前置课程', field: 'preLessonName', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
LessonInfo.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        LessonInfo.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加课程管理
 */
LessonInfo.openAddLessonInfo = function () {
    var index = layer.open({
        type: 2,
        title: '添加课程管理',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/lessonInfo/lessonInfo_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看课程管理详情
 */
LessonInfo.openLessonInfoDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '课程管理详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/lessonInfo/lessonInfo_update/' + LessonInfo.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除课程管理
 */
LessonInfo.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/lessonInfo/delete", function (data) {
            Feng.success("删除成功!");
            LessonInfo.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("lessonInfoId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询课程管理列表
 */
LessonInfo.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    LessonInfo.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = LessonInfo.initColumn();
    var table = new BSTable(LessonInfo.id, "/lessonInfo/list", defaultColunms);
    table.setPaginationType("client");
    LessonInfo.table = table.init();
});
