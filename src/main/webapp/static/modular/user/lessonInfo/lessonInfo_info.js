/**
 * 初始化课程管理详情对话框
 */
var LessonInfoInfoDlg = {
    lessonInfoInfoData : {}
};

/**
 * 清除数据
 */
LessonInfoInfoDlg.clearData = function() {
    this.lessonInfoInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
LessonInfoInfoDlg.set = function(key, val) {
    this.lessonInfoInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
LessonInfoInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
LessonInfoInfoDlg.close = function() {
    parent.layer.close(window.parent.LessonInfo.layerIndex);
}

/**
 * 收集数据
 */
LessonInfoInfoDlg.collectData = function() {
    this
    .set('id')
    .set('lessonName')
    .set('lessonBeginTime')
    .set('lessonPeriod')
    .set('lessonClass')
    .set('teacherInfo')
    .set('preLessonId');
}

/**
 * 提交添加
 */
LessonInfoInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/lessonInfo/add", function(data){
        Feng.success("添加成功!");
        window.parent.LessonInfo.table.refresh();
        LessonInfoInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.lessonInfoInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
LessonInfoInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/lessonInfo/update", function(data){
        Feng.success("修改成功!");
        window.parent.LessonInfo.table.refresh();
        LessonInfoInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.lessonInfoInfoData);
    ajax.start();
}

$(function() {

});
