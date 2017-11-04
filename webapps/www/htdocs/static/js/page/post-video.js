/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-7
 * Time: 下午3:01
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var postvideo = {
        praseChatVideo:function (ctx, url) {
            praseVideo(url, ctx, callbackPreaseChatVideo, 'video_error', praseVideoCallback);
        },
        praseTextVideo: function (ctx, oEditorVideoDialog) {
            var url = $("#video_ptext_url").val();
            praseVideo(url, ctx, callbackPreaseTextVideo, 'video_text_error', "", oEditorVideoDialog);
        },
        resetChatVideo:function() {
            resetChatVideo();
        },
        resetTextVideo:function() {
            resetTextVideo();
        }
    }
    var praseVideo = function (url, ctx, callback, infoId, callback2, oEditorVideoDialog) {
        if (url == null || url.length == 0) {
            $("#" + infoId).text('视频地址不能为空');
            return;
        }

        $("#" + infoId).text('正在读取视频信息');
        var postbiz = require('../biz/post-biz');
        postbiz.searchVideo(url, ctx, callback, infoId, callback2, oEditorVideoDialog);
    }
    var praseVideoCallback = function(url, ctx, callback, infoId, msg) {
        if (msg.status_code == '0' || msg.result[0] == null) {
            $("#" + infoId).text('视频格式错误');
            return;
        }
        var video = msg.result[0];
        if (video == null) {
            $("#" + infoId).text('视频格式错误');
        }
        callback(video, infoId);
    }
    var callbackPreaseChatVideo = function (video, infoId) {
        displayPreviewDiv();
        var discription = video.description;
        var title = (discription != null && discription.length != 0) ? (discription.length > 5) ? discription.substr(0, 5) + '...' : discription : '视频';
        var content = (discription != null && discription.length != 0) ? (discription.length > 20) ? discription.substr(0, 20) + '...' : discription : '';
        $("#ul_preview").append('<li id="li_video_preview"><a href="javascript:void(0)" class="videoreview" title="' + discription + '">' + title + '</a><a href="javascript:void(0)" title="取消" class="close"></a>' +
                '<input type="hidden" name="videoUrl" id="videoUrl" value="' + video.flash + '" />' +
                '<input type="hidden" name="videoAlbum" id="videoAlbum" value="' + video.pic + '" />' +
                '<input type="hidden" name="videoTitle" id="videoTitle" value="' + title + '" />' +
                '<input type="hidden" name="viorgurl" id="viorgurl" value="' + video.orgUrl + '" />' +
                '<input type="hidden" name="vtime" id="vtime" value="' + video.time + '" />' +
                '</li>');
        blogContent.video = {album:video.pic,flash:video.flash,title:discription,orgurl:video.orgUrl,vtime:video.time};
        resetChatVideo();
        if ($("#ul_preview li").size() > 0) {
            $("#post_chat_submit").attr('class','publishon')
            $("#edit_chat_submit").attr('class','publishon')
        }
        $(".pop").hide();
    };
    var displayPreviewDiv = function () {
        $("#rel_preview").show();
    }
    var hidePreviewDiv = function() {
        if ($("#ul_preview li").length == 0) {
            $("#rel_preview").hide();
        }
    }
    var resetChatVideo = function () {
        $("#video_error").html('');
        $("#video_url").val('');
    }
    var callbackPreaseTextVideo = function (video, oEditorVideoDialog) {
        $("#video_ptext_url").val("请输入视频链接地址").css('color', '#ccc');
        $("#video_text_error").text("");
        var offset = $("#editor_video").offset();
        var videoId = "div_text_video_post_content";
        var pop = require('../common/jmpopup');
        var config = {
            pointerFlag : true,//是否有指针
            pointdir : 'up',//指针方向
            tipLayer : false,//是否遮罩
            offset:"Custom",
            containTitle : true,//包含title
            containFoot : true,//包含footer
            offsetlocation:[offset.top + 31,offset.left],
            className:"",
            forclosed:true,
            popwidth:445 ,
            allowmultiple:false,
            isfocus:true,
            hideCallback:function() {
                $("#div_text_video_post_content").hide().remove();
                hideEditorFocusBtn();
            }
        };
        if ($("#" + videoId).length > 0) {
            pop.resetOffset(config, videoId);
        } else {
            var htmlObj = new Object();
            htmlObj['id'] = videoId;
            htmlObj['html'] = '<div class="longvideo"><div class="longvideol" id="text_video_img">' +
                    '<img width="165" height="123" src="' + video.pic + '">' +
                    '</div><div class="longvideor">' +
                    '<input type="text" id="video_title" value="' + video.description + '" class="musictxt">' +
                    '<textarea cols="" rows="" class="musictextarea" id="video_description" style="font-family:Tahoma, \'宋体\';"></textarea></div></div>';
            htmlObj['title'] = '插入视频'
            htmlObj['input'] = '<a class="submitbtn" id="link_insert_text_video"><span>加入文章</span></a>';
            pop.popupInit(config, htmlObj);
        }
        $('#link_insert_text_video').die().live('click', function() {
            var str = createVideoImage(video.pic, video.flash, $("#video_title").val(), $("#video_description").val(),video.orgUrl,video.time) +
                    '<br/>' + $("#video_title").val() +
                    '<br/>' + $("#video_description").val();

            CKEDITOR.instances['text_content'].insertHtml(str);
            hideEditorFocusBtn();
            $("#div_text_video_post_content").hide().remove();
        });
    }
    var resetTextVideo = function() {
        $("#video_text_error").text('');
        $("#video_ptext_url").val('');
        $("#text_video_img").html('');
        $("#video_title").val('');
        $("#video_description").val('');
        $('#video_flash').val('');
    }

    var createVideoImage = function (album, falsh, title, desc,orgUrl,vtime) {
        var str = '</br><img joymed="' + desc + '" joymef="' + falsh + '" joymeo="'+orgUrl+'" joymevtime="'+vtime+'" joymet="video" src="' + album + '" title="' + title + '" width="188" height="141"/>';
        return str;
    }
    var hideEditorFocusBtn = function() {
        $(".edit_hd a").each(function() {
            var editclass = $(this).attr('class');
            if (editclass.indexOf('_on') != -1) {
                $(this).removeClass().addClass(editclass.substr(0, editclass.length - 3));
            }
        });
    }
    return postvideo;
});