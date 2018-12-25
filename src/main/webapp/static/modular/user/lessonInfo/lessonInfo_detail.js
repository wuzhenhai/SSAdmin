

var LessonSelectTable= {
    id: "selectStudentList",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

LessonSelectTable.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: '姓名', field: 'username', visible: true, align: 'center', valign: 'middle'},
    ];
};

$(function() {
    var defaultColunms = LessonSelectTable.initColumn();
    var table = new BSTable(LessonSelectTable.id, "/lessonInfo/selectList", defaultColunms);
    table.setQueryParams({"lessonId":lessonId});
    table.setPaginationType("client");
    LessonSelectTable.table = table.init();
});
