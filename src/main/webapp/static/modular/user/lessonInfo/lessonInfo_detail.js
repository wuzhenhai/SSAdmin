

var LessonSelectTable= {
    id: "selectStudentList",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1,
    params : {}
};

LessonSelectTable.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: '用户id', field: 'userid', visible: false, align: 'center', valign: 'middle'},
        {title: '姓名', field: 'name', visible: true, align: 'center', valign: 'middle'},
        {title: '电话', field: 'phone', visible: true, align: 'center', valign: 'middle'},
        {title: '地址', field: 'address', visible: true, align: 'center', valign: 'middle'},
        {title: '部门', field: 'deptName', visible: true, align: 'center', valign: 'middle'},
        // {title: '出席天数', field: 'days', visible: true, align: 'center', valign: 'middle'},
        {
            title: '出席天数',
            field: 'days',
            align: 'center',
            valign: 'middle',
            width: '16%',
            formatter: operateFormatter //自定义方法，添加操作按钮
        }
    ];
};


function operateFormatter (value, row, index) {
    var result = "";
    result += "<div class='changeDiv'>";
    result += "<span class='btn-minus' onclick='LessonSelectTable.changeDaysByBtn(this,-1," + row.id + " )'>-</span>";
    result += "<input class='days_input' onchange =\"LessonSelectTable.changeDays(this," + row.id + ")\" value='" + value + "'/>";
    result += "<span class='btn-plus' onclick='LessonSelectTable.changeDaysByBtn(this,1," + row.id + " )'>+</span>";
    result += "</div>";

    return result;
}

//导出课程详情
function exportLessonDetail() {
    if(confirm("是否确定导出?")){
        var ajax = new $ax(Feng.ctxPath + "/lessonInfo/exportLessonDetail", function (data) {
            Feng.success("导出成功!");
            var downloadUrl = data.data;
            location.href = downloadUrl;
        }, function (data) {
            // Feng.error("导出失败!" + data.responseJSON.message + "!");
            Feng.error("导出失败!");
        });

        ajax.set("lessonId",lessonId);
        ajax.start();
    }
}

//动态设置学生出席课时
LessonSelectTable.changeDays = function(obj,id) {
    this.params['id'] = id;
    //不能超出课时上限
    var days = $(obj).val() >= parseInt(lesson_period)?parseInt(lesson_period):$(obj).val();
    this.params['days'] = days;
    $(obj).val(days);
    // alert($(obj).val() + "--" + userid)
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/lessonStudent/setStudentDays", function(data){
        // Feng.success("修改成功!");
        // window.parent.LessonInfo.table.refresh();
        // LessonInfoInfoDlg.close();
        console.log("修改成功")
    },function(data){
        // Feng.error("修改失败!" + data.responseJSON.message + "!");
        console.log("修改失败")
    });
    ajax.set(this.params);
    ajax.start();
}


//动态设置学生出席课时
LessonSelectTable.changeDaysByBtn = function(obj,num,id) {

    this.params['id'] = id;
    //不能超出课时上限
    var days = parseInt($(obj).siblings("[class='days_input']").val()) + num;
    //超出上限，小于0，都是不合理的，直接返回
    if(days > parseInt(lesson_period) || days < 0){
        return;
    }
    var final_days = days >= parseInt(lesson_period)?parseInt(lesson_period):days;

    if(final_days < 0){
        final_days = 0;
    }

    this.params['days'] = final_days;
    $(obj).siblings(".days_input").val(final_days);
    // alert($(obj).val() + "--" + userid)
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/lessonStudent/setStudentDays", function(data){
        // Feng.success("修改成功!");
        // window.parent.LessonInfo.table.refresh();
        // LessonInfoInfoDlg.close();
        console.log("修改成功")
    },function(data){
        // Feng.error("修改失败!" + data.responseJSON.message + "!");
        console.log("修改失败")
    });
    ajax.set(this.params);
    ajax.start();
}

$(function() {
    var defaultColunms = LessonSelectTable.initColumn();
    var table = new BSTable(LessonSelectTable.id, "/lessonInfo/selectList", defaultColunms);
    table.setQueryParams({"lessonId":lessonId});
    table.setPaginationType("client");
    LessonSelectTable.table = table.init();
});
