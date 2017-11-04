define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var header = require('../page/header');

    $().ready(function () {
        header.noticeSearchReTopInit();

        $('#form_edit').bind('submit', function(){
            return verify();
        });
        $('#txt_name').bind('blur', function(){
            return verify();
        });
    });

    function verify() {
        var name = $('#txt_name').val();
        if (name == null || name.length == 0) {
            $('#name_msg').attr('style', 'color:#fd5d51').html('栏目名称不能为空！');
            return false;
        }

        if (/[^\u4e00-\u9fa5A-Za-z0-9\-_]+/g.test(name)) {
            $('#name_msg').attr('style', 'color:#fd5d51').html('只允许使用汉字、数字、“-”、“_”最长不超过10个汉字!');
            return false;
        }else{
           $('#name_msg').attr('style', '').html('只允许使用汉字、数字、“-”、“_”最长不超过10个汉字!');
        }
        return true;
    }

    require.async('../common/google-statistics');
    require.async('../common/bdhm')
});