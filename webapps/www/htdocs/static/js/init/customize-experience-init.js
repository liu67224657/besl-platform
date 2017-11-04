define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var common = require('../common/common');
    var customize = require('../page/customize');
    var header = require('../page/header');
    require('../common/tips');

    $(document).ready(function() {
        //添加学校
        $("#addschoolbtn a").live("click", function() {
            customize.addSchool();
        });
        //添加学校项
        $("#addSchoolItem").live("click", function() {
            customize.addSchoolItem('');
        });
        //remove item
        $("a[name=removeSchoolItem]").live("click", function() {
            customize.removeSchoolItem($(this));
        });
        //修改学校
        $("a[name=updateSchool]").live("click", function() {
            customize.updateSchool($(this).attr("id"))
        });
        //保存学校
        $("a[id=saveSchool]").live("click", function() {
            customize.saveSchool();
        });
        //取消
        $("a[id=cancelSchool]").live("click", function() {
//            customize.cancelSchool($(this).attr("flag"));
            window.location.href = "/profile/customize/experience";
        });

        //添加公司
        $("#addCompanyBtn a").live("click", function() {
            customize.addCompany();
        });
        //添加company项
        $("#addCompanyItem").live("click", function() {
            customize.addCompanyItem('');
        });
        //remove item
        $("a[name=removeCompanyItem]").live("click", function() {
            customize.removeCompanyItem($(this));
        });
        //修改学校
        $("a[name=updateCompany]").live("click", function() {
            customize.updateCompany($(this).attr("id"))
        });
        //保存学校
        $("a[id=saveCompany]").live("click", function() {
            customize.saveCompany();
        });
        //取消
        $("a[id=cancelCompany]").live("click", function() {
//            customize.cancelCompany($(this).attr("flag"));
            window.location.href = "/profile/customize/experience";
        });
        header.noticeSearchReTopInit();
    });
    require.async('../common/google-statistics');
    require.async('../common/bdhm');
});