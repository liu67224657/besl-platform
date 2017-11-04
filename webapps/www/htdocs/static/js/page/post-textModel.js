/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-11
 * Time: 上午11:37
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');

    var textModel = {
        textModel:function (isFocusText, homeOption) {
            $(".pop").hide();
            $(".release").css("display", 'none');
            $(".text_edit").hide();
            swfuflag = true;
            swfu = null;
            var str = '';
            if ($.trim($('#chat_content').val()).length > 0) {
                str += $('#chat_content').val().toString().replace(/(\r)*\n/g, "<br />").replace(/\s/g, " ");
            }
            if ($("#blogSubject").val() == "" || $("#blogSubject").val() == "给你的文章加个标题吧") {
                $("#blogSubject").css("color", "#CCCCCC").val("给你的文章加个标题吧");
            }

            if (blogContent.content != null) {
                str += blogContent.content;
            }

            //pic
            if (blogContent.image != null && blogContent.image.length > 0) {
                str += createPhoto();
            }
            //video
            if (blogContent.video != null) {
                str += createVideoImage(blogContent.video.album, blogContent.video.flash, blogContent.video.title, '', blogContent.video.orgUrl, blogContent.video.vtime);
            }

            if (blogContent.audio != null) {
                str += createAudioImage(blogContent.audio.album, blogContent.audio.flash, blogContent.audio.title, '');
            }
            //标签
            $("#postchat").find("input[name='tags']").each(function() {
                $(this).clone().appendTo("#posttext");
                $(this).remove();
            });
//            $("div[id$='Dialog']").remove();


            setTimeout(function() {
                CKEDITOR.instances['text_content'].focus();
                CKEDITOR.instances['text_content'].insertHtml(str);
            }, 400);

            $("#chat_content").val("");
            $("#ul_preview li").remove();
            $("#rel_preview").hide();
            if (typeof(homeOption.changeTextCallBack) == 'function') {
                homeOption.changeTextCallBack();
            }
        }
    }

    var createPhoto = function () {
        var str = ""
        $.each(blogContent.image, function(i, val) {
            str += '<img src="' + val.value.src + '" joymet="img" joymed="" joymeu="' + val.value.url + '" joymew="' + val.value.w + '" joymeh="' + val.value.h + '"/><br/>';
        });
        return str;
    }

    var createVideoImage = function (album, falsh, title, desc, orgUrl, vtime) {
        var str = '</br><img joymed="' + desc + '" joymef="' + falsh + '" joymeo="' + orgUrl + '" joymevtime="' + vtime + '" joymet="video" src="' + album + '" title="' + title + '" />';
        return str;
    }
    var createAudioImage = function (album, flashUrl, title, desc) {
        var str = '<br /><img src="' + album + '" joymet="audio"  title="' + title + '"  joymed="' + desc + '" joymef="' + flashUrl + '"/>';
        return str;
    }

    return textModel;
});