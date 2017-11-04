define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var common = require('../common/common');
    var customize = require('../page/customize');
    var header = require('../page/header');
    require.async('../common/tips');
    $(document).ready(function() {
        $("input[name^=hint]").click(function() {
            var id = $(this).attr('name');
            if (id == 'hintmynotice') {
                if ($(this).attr('checked') == true) {
                    $('#p_' + id).html('1条新通知，<b>查看收件箱</b>');
                } else {
                    $('#p_' + id).html('');
                }

            } else if (id == 'hintmyfeedback') {
                if ($(this).attr('checked') == true) {
                    $('#p_' + id).html('1条新评论，<b>查看评论</b>');
                } else {
                    $('#p_' + id).html('');
                }
            } else if (id == 'hintmyfans') {
                if ($(this).attr('checked') == true) {
                    $('#p_' + id).html('1位新粉丝，<b>查看我的粉丝</b>');
                } else {
                    $('#p_' + id).html('');
                }
            } else if (id == 'hintmyletter') {
                if ($(this).attr('checked') == true) {
                    $('#p_' + id).html('1条新私信，<b>查看收件箱</b>');
                } else {
                    $('#p_' + id).html('');
                }
            } else if (id == 'hintat') {
                if ($(this).attr('checked') == true) {
                    $('#p_' + id).html('1条@提到我，<b>查看@到我的</b>');
                    $("#atEm").removeClass("aithover").addClass("ait");
                    $("#ait_text").slideDown();
                } else {
                    $("#atEm").removeClass("ait").addClass("aithover");
                    $("#ait_text").slideUp();
                    $('#p_' + id).html('');
                }
            }
            var flag = false;
            $("#checkbox_select input[type=checkbox]").each(function(i, val) {
                if (val.checked) {
                    flag = true;
                }
            });
            if (!flag) {
                $("#notice_tips").css("display", "none");
            } else {
                $("#notice_tips").css("display", "block");
            }

        });

        $(".submitbtn").bind("click", function() {
            customize.setUserDefine();
        });

        header.noticeSearchReTopInit();

    });
    require.async('../common/google-statistics');
    require.async('../common/bdhm');
});