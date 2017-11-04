/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-11
 * Time: 下午12:46
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var contentGenerator = require('./post-contentgenerator');
    var common = require('../common/common');
    var login = require('../biz/login-biz');
    var tag = require('./tag');
    var joymealert = require('../common/joymealert');
    var textsubmit = require('./post-text-submit');
    var jmpopup = require('../common/jmpopup');
    var option = {
        homeOption:{
            postTextCallBack:function (resultMsg, homeOption) {
                if ($("#post_area").length > 0) {
                    $("#closePop_post_area").click();
//                    //mini-post
                    $('#send_box_small').css('display', '');
                    $('#div_post_area').css('display', 'none');
                }
                var alertFunction = function() {
                    //删除标签隐藏层内容
                    $("#posttext").children('input').remove();
                    $("#tags_input_box_text").children('span').remove();
                    //再次点击上传图片组件时允许重新创建
                    swfEdirorFlag = true;
                    //销毁当前swfupload实例
                    swftext = null;
                    $("#blogSubject").css("color", "#ccc").val("给你的文章加个标题吧");
                    var contentObj = contentGenerator.generator(resultMsg.result[0]);
                    $('#memo_content').after(contentObj.contentHtml);
                    $(".lazy").scrollLoading();
                    $('#' + contentObj.htmlId).fadeIn(function() {
                        window.scrollTo(0, document.body.scrollTop + document.documentElement.scrollTop + 1)
                        window.scrollTo(0, document.body.scrollTop + document.documentElement.scrollTop - 1)
                    });
                    //修改右边计数
                    common.increaseCount('r_blog_num', 1);
                    //初始化图片上传全局变量
                    blogContent = {content:'',audio:null,video:null,image:new Array()};
                    setTimeout(function() {
                        CKEDITOR.instances['text_content'].setData('');
                        setTimeout(function() {
                            $("#link_chatmodel").click();
                        }, 100)
                    }, 200)
                    if ($("#edit_text_height").children('span').attr('class') == 'unfold_btn01') {
                        $("#edit_text_height").click();
                    }

                    if ($('#div_post_area').length > 0) {
                        $(window).scrollTop($('#div_post_area').offset().top - 20);
                    }
                }

                alertPostSuccess(resultMsg, alertFunction);
            },
            postChatCallBack:function (resultMsg) {
//                //mini-post
                $('#send_box_small').css('display', '');
                $('#div_post_area').css('display', 'none');
                //是否登录
                CKEDITOR.instances['text_content'].setData(''); //内容

                //隐藏全部弹出层
                $(".pop").hide().remove();
                //清空内容
                $("#chat_content").val("");
                //删除上传内容
                $("#ul_preview li").remove();
                //隐藏长传内容层
                $("#rel_preview").hide();
                //删除标签隐藏层内容
                $("#postchat").children('input').remove();
                //再次点击上传图片组件时允许重新创建
                swfuflag = true;
                //销毁当前swfupload实例
                swfu = null;
                //拼装上传返回字符串

                blogContent = {content:'',audio:null,video:null,image:new Array()};

                var contentObj = contentGenerator.generator(resultMsg.result[0]);
                $('#memo_content').after(contentObj.contentHtml);
                $(".lazy").scrollLoading();
                $('#' + contentObj.htmlId).fadeIn(function() {
                    window.scrollTo(0, document.body.scrollTop + document.documentElement.scrollTop + 1)
                    window.scrollTo(0, document.body.scrollTop + document.documentElement.scrollTop - 1)
                });
                //修改右边计数
                common.increaseCount('r_blog_num', 1);
                alertPostSuccess(resultMsg);
            },
            changeChatCallBack:function () {

            },
            changeTextCallBack:function () {

            },
            afterCallBack:true
        },
        guideOption:{
            postTextCallBack:function (resultMsg) {
//                //mini-post
                $('#send_box_small').css('display', '');
                $('#div_post_con').css('display', 'none');
                var alertFunction = function() {
                    //删除标签隐藏层内容
                    $("#posttext").children('input').remove();
                    $("#tags_input_box_text").children('span').remove();
                    //再次点击上传图片组件时允许重新创建
                    exports.swfuflag = true;
                    //销毁当前swfupload实例
                    exports.swfu = null;
                    $("#blogSubject").css("color", "#ccc").val("给你的文章加个标题吧");
                    var contentObj = contentGenerator.generator(resultMsg.result[0]);
                    $('#div_post_con').after(contentObj.contentHtml);
                    $(".lazy").scrollLoading();
                    $('#' + contentObj.htmlId).fadeIn(function() {
                        window.scrollTo(0, document.body.scrollTop + document.documentElement.scrollTop + 1)
                        window.scrollTo(0, document.body.scrollTop + document.documentElement.scrollTop - 1)
                    });

                    setTimeout(function() {
                        CKEDITOR.instances['text_content'].setData('');
                        setTimeout(function() {
                            $("#link_chatmodel").click();
                        }, 100)
                    }, 200)
                    if ($("#edit_text_height").children('span').attr('class') == 'unfold_btn01') {
                        $("#edit_text_height").click();
                    }
                    //修改右边计数
                    common.increaseCount('r_blog_num', 1);
                    //再次点击上传图片组件时允许重新创建
                    swfuflag = true;
                    //销毁当前swfupload实例
                    swfu = null;
                    //拼装上传返回字符串
                    blogContent = {content:'',audio:null,video:null,image:new Array()};
                    $("#link_chatmodel").click();
                }
                alertPostSuccess(resultMsg, alertFunction);
            },
            postChatCallBack:function (resultMsg) {
//                //mini-post
                $('#send_box_small').css('display', '');
                $('#div_post_con').css('display', 'none');
                //是否登录
                CKEDITOR.instances['text_content'].setData(''); //内容
                blogContent = {audio:null,video:null,image:new Array()};
                //隐藏全部弹出层
                $(".pop").hide().remove();
                //再次点击上传图片组件时允许重新创建
                exports.swfuflag = true;
                //销毁当前swfupload实例
                exports.swfu = null;
                //清空内容
                $("#chat_content").val("");
                //删除上传内容
                $("#ul_preview li").remove();
                //隐藏长传内容层
                $("#rel_preview").hide();
                //删除标签隐藏层内容
                $("#postchat").children('input').remove();
                //拼装上传返回字符串
                var contentObj = contentGenerator.generator(resultMsg.result[0]);
                $('#div_post_con').after(contentObj.contentHtml);
                $(".lazy").scrollLoading();
                $('#' + contentObj.htmlId).fadeIn(function() {
                    window.scrollTo(0, document.body.scrollTop + document.documentElement.scrollTop + 1)
                    window.scrollTo(0, document.body.scrollTop + document.documentElement.scrollTop - 1)
                });
                //修改右边计数
                common.increaseCount('r_blog_num', 1);
                //再次点击上传图片组件时允许重新创建
                swfuflag = true;
                //销毁当前swfupload实例
                swfu = null;
                //拼装上传返回字符串
                blogContent = {content:'',audio:null,video:null,image:new Array()};

                alertPostSuccess(resultMsg);
            },
            changeChatCallBack:function () {

            },
            changeTextCallBack:function () {

            },
            afterCallBack:true
        },
        editOption:{
            initTag:[]
        },
        searchOption:{
            postTextCallBack:function (resultMsg) {
                var alertFunction = function() {
                    var contentCateGory = $('#content_categroy>a[class=brightbtn]').text();
                    if (contentCateGory == '全部') {
                        if ($('#content_empty').size() > 0) {
                            $('#content_empty').remove();
                        }
                        var contentObj = contentGenerator.generator(resultMsg.result[0]);
                        $('#content_categroy').parent().after(contentObj.contentHtml);
                        $('#' + contentObj.htmlId).fadeIn(function() {
                            $('#post_area').slideUp(function() {
                                $('#show_post').css('display', '');
                            });
                        });
                    }
                    //初始化图片上传全局变量
                    resetChatForm();
                    resetTextForm();
                }
                alertPostSuccess(resultMsg, alertFunction);
            },
            postChatCallBack:function (resultMsg) {
                var alertFunction = function() {
                    //是否登录
                    var contentCateGory = $('#content_categroy>a[class=brightbtn]').text();
                    if (contentCateGory == '全部') {
                        if ($('#content_empty').size() > 0) {
                            $('#content_empty').remove();
                        }

                        var contentObj = contentGenerator.generator(resultMsg.result[0]);
                        $('#content_categroy').parent().after(contentObj.contentHtml);
                        $('#' + contentObj.htmlId).fadeIn(function() {
                            $('#post_area').slideUp(function() {
                                $('#show_post').css('display', '');
                            });
                        });
                    }

                    this.initTag = [];
                    resetChatForm();
                    resetTextForm();
                }
                alertPostSuccess(resultMsg, alertFunction);
            },
            initTag:[]
        },
        searchTopicOption:{
            postTextCallBack:function (resultMsg) {
                var alertFunction = function() {
                    login.locationLoginByJsonObj(resultMsg);
                    var contentCateGory = $('#content_categroy>a[class=brightbtn]').text();
                    if (contentCateGory == '全部') {
                        if ($('#content_empty').size() > 0) {
                            $('#content_empty').remove();
                        }

                        var contentObj = contentGenerator.generator(resultMsg.result[0]);
                        $('#content_categroy').parent().after(contentObj.contentHtml);
                        $('#' + contentObj.htmlId).fadeIn(function() {
                            $('#post_area').slideUp(function() {
                                $('#show_post').parent().css('display', '');
                            });
                        });
                    }
                    //初始化图片上传全局变量
                    resetChatForm();
                    resetTextForm();
                }
                alertPostSuccess(resultMsg, alertFunction);
            },
            postChatCallBack:function (resultMsg) {
                var alertFunction = function() {
                    login.locationLoginByJsonObj(resultMsg);
                    var contentCateGory = $('#content_categroy>a[class=brightbtn]').text();
                    if (contentCateGory == '全部') {
                        if ($('#content_empty').size() > 0) {
                            $('#content_empty').remove();
                        }

                        var contentObj = contentGenerator.generator(resultMsg.result[0]);
                        $('#content_categroy').parent().after(contentObj.contentHtml);
                        $('#' + contentObj.htmlId).fadeIn(function() {
                            $('#post_area').slideUp(function() {
                                $('#show_post').parent().css('display', '');
                            });
                        });
                    }

                    this.initTag = [];
                    resetChatForm();
                    resetTextForm();
                }
                alertPostSuccess(resultMsg, alertFunction);
            },
            initTag:[]
        },
        //searchGame
        searchGameOption:{
            postTextCallBack:function (resultMsg) {
                var alertFunction = function() {
                    login.locationLoginByJsonObj(resultMsg);
                    var currentTheme = $('.entry_gametalk>li>a[class=hover]').attr('title');
                    var contentTheme = $('#content_theme>a[class=hover]').attr('title');
                    var contentCateGory = $('#content_categroy>a[class=brightbtn]').text();
                    //最新或者当前主题或者
                    if ((contentTheme == '最新' || currentTheme == '' || contentTheme == currentTheme) && (contentCateGory == '全部' || contentCateGory == '')) {
                        if ($('#content_empty').size() > 0) {
                            $('#content_empty').remove();
                        }

                        var contentObj = contentGenerator.generator(resultMsg.result[0]);
                        $('#content_categroy').parent().after(contentObj.contentHtml);
                        $('.entry_gametalk>li>a[class=hover]').removeClass('hover');
                        $('#' + contentObj.htmlId).fadeIn(function() {
                            $('#post_area').slideUp();
                        });
                    }

                    resetChatForm();
                    resetTextForm();
                    tag.clearTagItem('chat', 'postchat');
                    this.initTag = [];
                }
                alertPostSuccess(resultMsg, alertFunction);
            },
            postChatCallBack:function (resultMsg) {
                var alertFunction = function() {
                    //是否登录
                    login.locationLoginByJsonObj(resultMsg);
                    var currentTheme = $('.entry_gametalk>li>a[class=hover]').attr('title');
                    var contentTheme = $('#content_theme>a[class=hover]').attr('title');
                    var contentCateGory = $('#content_categroy>a[class=brightbtn]').text();
                    //最新或者当前主题或者
                    if ((contentTheme == '最新' || currentTheme == '' || contentTheme == currentTheme) && contentCateGory == '全部') {
                        if ($('#content_empty').size() > 0) {
                            $('#content_empty').remove();
                        }

                        var contentObj = contentGenerator.generator(resultMsg.result[0]);
                        $('#content_categroy').parent().after(contentObj.contentHtml);
                        $('.entry_gametalk>li>a[class=hover]').removeClass('hover');
                        $('#' + contentObj.htmlId).fadeIn(function() {
                            $('#post_area').slideUp();
                        });
                    }

                    resetChatForm();
                    resetTextForm();
                    this.initTag = [];
                }
                alertPostSuccess(resultMsg, alertFunction);
            },
            initTag:[]
        },
        atTaOption:{
            postTextCallBack:function (resultMsg) {
                CKEDITOR.instances['text_content'].setData(''); //内容
                $("#post_area").hide();
                $("#blackDom").fadeOut().remove();

                var text = '';
                if (resultMsg.status_code == '1') {
                    text = '发布成功';
//                    if (resultMsg.billingStatus) {
//                        text += resultMsg.billingMsg;
//                    }
                    var alertOption = {text:text,tipLayer:true};
                    joymealert.alert(alertOption);
                } else {
                    text = '发布失败';
                    var alertOption = {text:text,tipLayer:true,textClass:"tipstext"};
                    joymealert.alert(alertOption);
                }


            },
            postChatCallBack:function (resultMsg) {
                $("#post_area").hide();
                $("#blackDom").fadeOut().remove();

                var text = '';
                if (resultMsg.status_code == '1') {
                    text = '发布成功';
//                    if (resultMsg.billingStatus) {
//                        text += resultMsg.billingMsg;
//                    }
                    var alertOption = {text:text,tipLayer:true};
                    joymealert.alert(alertOption);
                } else {
                    text = '发布失败';
                    var alertOption = {text:text,tipLayer:true,textClass:"tipstext"};
                    joymealert.alert(alertOption);
                }

            },
            changeChatCallBack:function () {

            },
            changeTextCallBack:function () {

            },
            afterCallBack:true
        },
        talkOption:{
            postTextCallBack:function (resultMsg, homeOption) {
                if ($("#post_area").length > 0) {
                    $("#closePop_post_area").click();
                }
                var alertFunction = function() {
                    var location = window.location.href;
                    if (location.indexOf('?') != -1) {
                        var intPos = location.indexOf("?");
                        location = location.substr(0, intPos);
                    }
                    resetTextForm();
                    window.location.href = location;
                }

                alertPostSuccess(resultMsg, alertFunction);
            },
            afterCallBack:true
        },
        handbookOption:{
            postTextCallBack:function(resultMsg, homeOption) {
                if ($("#post_area").length > 0) {
                    $("#closePop_post_area").click();
                }

                var gameName = $('#hid_gamename').val();
                var gameCode = $('#hid_gamecode').val();
                var groupName = $('#hid_groupname').val();
                var blogContent = resultMsg.result[0];

                var gamePageLink = joyconfig.URL_WWW + '/game/' + gameCode;

                var alertFunction = function() {
                    //删除标签隐藏层内容
                    window.location.href = gamePageLink;
                }

                var i = 10;
                var intervalSec = null;
                var jmpopupConfig = {
                    tipLayer : true,
                    containTitle : true,
                    containFoot : false,
                    forclosed: true,
                    allowmultiple:false,
                    isremovepop:true,
                    isfocus:false,
                    offset:'Custom',
                    hideCallback:alertFunction,
                    showFunction:function() {
                        if (intervalSec != null) {
                            clearInterval(intervalSec);
                        }
                        intervalSec = setInterval(function() {
                            $('#mask_hide_sec').text(--i);
                        }, 1000);
                    },
                    addClass:true,
                    className:"add-success-pop",
                    showTime:10000
                }
                var htmlObj = new Object();
                htmlObj['id'] = 'phb_success_mask';
                htmlObj['title'] = '<em><strong>提示信息</strong></em>';
                htmlObj['html'] = '<div class="add-baike-successed">' +
                        '<p>感谢您的投稿，我们会根据您的内容考虑入选到条目页。</p>' +
                        '<span><em id="mask_hide_sec">10</em>秒钟后自动返回条目页…</span>' +
                        '<div><a href="' + joyconfig.URL_WWW + '/note/' + blogContent.content.contentId + '">去看看发好的文章>></a></div>' +
                        '<div><a href="' + gamePageLink + '">返回' + gameName + '条目页>></a></div>' +
                        '</div>';
                jmpopup.popupInit(jmpopupConfig, htmlObj);

                resetTextForm();
            },
            initTag:[],
            formula:[]
        },
        editorOption:{
            postTextCallBack:function (resultMsg, homeOption) {
                if ($("#post_area").length > 0) {
                    $("#closePop_post_area").click();
                }
                var alertFunction = function() {
                    window.location.href = window.location.href;
                }

                alertPostSuccess(resultMsg, alertFunction);
            },
            postChatCallBack:function (resultMsg) {
                if ($("#post_area").length > 0) {
                    $("#closePop_post_area").click();
                }
                var alertFunction = function() {
                    window.location.href = window.location.href;
                }

                alertPostSuccess(resultMsg, alertFunction);
            },
            afterCallBack:true
        }
    }
    return option;

    function alertPostSuccess(resultMsg, alertFunction) {
        var text = '发布成功';
//        if (resultMsg.billingStatus) {
//            text += resultMsg.billingMsg;
//        }
        var alertOption = {text:text,tipLayer:true,callbackFunction:alertFunction};
        joymealert.alert(alertOption);
    }

    function resetTextForm() {
        //删除标签隐藏层内容
        $("#posttext").children('input').remove();
        //再次点击上传图片组件时允许重新创建
        swfEdirorFlag = true;
        //销毁当前swfupload实例
        swftext = null;
//        swfuflag = true;
//        //销毁当前swfupload实例
//        swfu = null;

        $("#blogSubject").css("color", "#ccc").val("给你的文章加个标题吧");
        CKEDITOR.instances['text_content'].setData('');
        //初始化图片上传全局变量
        blogContent = {content:'',audio:null,video:null,image:new Array()};
    }

    function resetChatForm() {
        //是否登录
        CKEDITOR.instances['text_content'].setData(''); //内容

        //隐藏全部弹出层
        $(".pop").hide().remove();
        //清空内容
        $("#chat_content").val("");
        //删除上传内容
        $("#ul_preview li").remove();
        //隐藏长传内容层
        $("#rel_preview").hide();
        //删除标签隐藏层内容
        $("#postchat").children('input').remove();
        //再次点击上传图片组件时允许重新创建
        swfuflag = true;
        //销毁当前swfupload实例
        swfu = null;
        //拼装上传返回字符串
        blogContent = {content:'',audio:null,video:null,image:new Array()};
    }


});