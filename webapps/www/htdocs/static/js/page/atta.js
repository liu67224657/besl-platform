define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var loginBiz = require('../biz/login-biz');
    var attaBiz = require('../biz/atta-biz');
    var pop = require('../common/jmpopup');
    var common = require('../common/common');
    var atta = {
        attaBind:function() {
            $("a[name=atTa]").live("click", function() {
                if (loginBiz.checkLogin($(this))) {
                    initAtTaMask($(this).attr("data-nick"));
                }
            });
        }

    }

    function initAtTaMask(nickname) {
        var config = {
            pointerFlag : false,//是否有指针
            tipLayer : true,//是否遮罩
            containTitle : true,//包含title
            containFoot : false,//包含footer
            addClass:true,
            className:"atpop",
            forclosed:true,
            popwidth:700,
            allowmultiple:true,
            addClass:true,
            hideCallback:function() {
                $("#div_post_text").hide();
                $("#div_post_chat").show();
            },showFunction:function() {
                setTimeout(function() {
                    $("#chat_content").val('对@' + nickname + ' 说：');
                    var te = new TextareaEditor(document.getElementById('chat_content'));
                    te.setSelectionRange($("#chat_content").val().length, $("#chat_content").val.length);
                }, 200)
            }
        };
        var htmlObj = new Object();
        htmlObj['id'] = 'post_area'; //设置层ID
        if ($("#post_area").length > 0) {

            window.blogContent = {content:'',audio:null,video:null,image:new Array()};//设置全局对象
            window.imgLocaL = 0;


            $("body").append("<div id='blackDom'></div>");
            $("#blackDom").css({opacity:"0","background":"#000",width:$(window).width(),height:$(document).height(),position: "absolute",top:0, left:0,'z-index':"9998"}).animate({opacity:"0.3"}, function() {
                $("#post_area").show();
                $("#post_area #div_post_text").hide();
                $("#chat_content").val('对@' + nickname + ' 说：');
                var te = new TextareaEditor(document.getElementById('chat_content'));
                te.setSelectionRange($("#chat_content").val().length, $("#chat_content").val.length);
            });


        } else {
            htmlObj['html'] = attaBiz.ajaxPostAtHtml();
            htmlObj['title'] = '<em>有什么话想对@' + nickname + '说：</em>';//设置标题内容
            pop.popupInit(config, htmlObj);
            var postChat = require('./post-chat');
            var postText = require('./post-text');
            var postOption = require('./post-option');
            require.async('../../third/swfupload/swfupload');
            require.async('../../third/swfupload/swfupload.queue');
            require.async('../../third/swfupload/fileprogress');
            window.blogContent = {content:'',audio:null,video:null,image:new Array()};//设置全局对象
            window.imgLocaL = 0;
            $(document).ready(function() {
                postChat.postChatInit(postOption.atTaOption);
                postText.posttextinit(postOption.atTaOption);
            });


        }
    }

    return atta;
});