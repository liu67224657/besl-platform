/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-5
 * Time: 下午12:57
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var chatsubmit = require('./post-chat-submit');
    var moodbiz = require('../biz/mood-biz');
    var pop = require('../common/jmpopup');
    var postaudio = require('./post-audio');
    var postvideo = require('./post-video');
    var postTag = require('./tag');
    var postat = require('./post-at');
    var postimagelink = require('./post-imagelink');
    var posttextmodel = require('./post-textModel');
    var common = require('../common/common');
    var removeprevObj = require('./removepreview');
    var joymealert = require('../common/joymealert')
    var syncObj = require('../page/sync')
    window.swfu;
    window.swfuflag = true;
    var cleartimeoutpreview;//预览层离开的timeout
    var outprevfocusTimeout;//预览触发层离开的timeout
    var config = {
        pointerFlag : true,//是否有指针
        pointdir : 'up',//指针方向
        tipLayer : false,//是否遮罩
        offset:"Custom",
        containTitle : true,//包含title
        containFoot : false,//包含footer
        offsetlocation:[0,0],
        className:"",
        forclosed:true,
        popwidth:0 ,
        allowmultiple:false,
        isfocus:true

    };
    var postConfig = {};
    var atSearch = '';

    require('../common/atme')
    var postChat = {
        postChatInit:function(homeOption) {
            postConfig = homeOption;
            window.autoAt({id:'chat_content',ohterStyle:'line-height:22px;padding:5px;font-size:14px;font-family:monospace'});

            if (homeOption.initTag != null && homeOption.initTag.length > 0) {
                $.each(homeOption.initTag, function(i, val) {
                    postTag.initTagItem('chat', val, val, 'postchat');
                });
            }

            //短文输入框验证
            $("#chat_content").die().live('cut keydown keyup  focusin blur focusout', function(event) {
                if (event.type == "keydown" || event.type == "keyup") {
                    checkModel(140, 'chat_content', postConfig);
                    //ctrl+enter
                    if ($("#edit_chat_submit").length > 0) {
                        common.joymeCtrlEnter(event, $("#edit_chat_submit"));
                    } else {
                        common.joymeCtrlEnter(event, $("#post_chat_submit"));
                    }
                } else if (event.type == "focusin") {
                    var dom=$("#post_chat_submit");
                    if(!dom.hasClass('publishloadbtn')){
                         dom.attr('class','publishon');
                    }
                    var editDom=$("#edit_chat_submit");
                    if(!editDom.hasClass('publishloadbtn')){
                         editDom.attr('class','publishon');
                    }
                } else if (event.type == "focusout") {
                    if ($(this).val() == "" && $("#rel_preview ul li").size() == 0) {
                        $("#post_chat_submit").removeClass().addClass('publish_btn');
                        $("#edit_chat_submit").removeClass().addClass('publish_btn');
                    }
                }
            });
            $("#chat_content").unbind().bind('paste', function() {
                setTimeout(function() {
                    checkModel(140, 'chat_content', postConfig);
                }, 100);
            });
            //图片层折叠
            $(".talk_pic").mouseenter(function() {
                $("#relPhoto").css('background-position', '-58px -207px');
                $(".pic_more").show();
            });
            $(".pic_more").mouseleave(function() {
                $("#relPhoto").css('background-position', '-58px -179px');
                $(".pic_more").hide();
            });
            //同步显示
            $('.install').die().live('click',function(){
                 syncObj.showSyncArea($(this));
            });

            //同步隐藏
            $("#close_sync_info").live("click", function() {
                $('#sync_info').slideUp();
            });

            //切换图片与链接--图片链接
            $("#piclinkbtn").live('click', function() {
                $("#photoUploadBtn").hide();
                $("#photoLinkBox").show();
                $("#piclinkbtn").addClass("tabon");
                $("#picuploadbtn").removeClass("tabon");
            })
            //切换图片与链接--图片上传
            $("#picuploadbtn").live('click', function() {
                $("#photoLinkBox").hide();
                $("#photoUploadBtn").show();
                $("#picuploadbtn").addClass("tabon");
                $("#piclinkbtn").removeClass("tabon")
            });
            //删除图片上传内容
            $("#ul_preview>li>a.close").live('click', function() {
                var previd = $(this).parent().attr('id');
                if (previd.indexOf('photo') != -1) {
                    removeprevObj.cancelPhoto(previd.substr(previd.length - 1, previd.length))
                } else if (previd.indexOf('audio') != -1) {
                    removeprevObj.cancelAudio()
                } else if (previd.indexOf('video') != -1) {
                    removeprevObj.cancelVideo();
                }
                $(this).parent().remove();
                if ($("#ul_preview li").size() == 0) {
                    $("#post_chat_submit").removeClass().addClass('publish_btn');
                    $("#edit_chat_submit").removeClass().addClass('publish_btn');
                    $("#relPhotoDialog").css({"top": $("#pic_more").offset().top + 26 + "px"});
                    $("div[id^=prev_preview_photo_]").hide();
                }
                previewLiSize();
            });

            //音乐
            $("#audio_code").live('keyup', function(event) {
                postaudio.searchChatAudio(event, joyconfig.ctx, 1);
            });
            $("#audio_code").live('focus',
                    function() {
                        if ($(this).val() == "请输入歌曲名专辑名演唱者") {
                            $(this).css("color", "#5b5b5b").val("");
                        }
                    }).live('blur',
                    function() {
                        if ($(this).val() == "") {
                            $(this).css("color", "#ccc").val("请输入歌曲名专辑名演唱者")
                        }
                    }).live('keyup', function(event) {
                        switch (event.which) {
                            case 38:
                                if (!$("#audio_search").is(':hidden')) {
                                    var p_up = $("#audio_search .on");
                                    if (p_up.length != 0) {
                                        p_up.removeClass("on");
                                    }
                                    if (p_up.prev().length != 0) {
                                        p_up.prev().addClass("on");
                                    } else {
                                        var last = $("#audio_search ul li").last();
                                        last.addClass("on");
                                    }
                                }
                                break;
                            case 40:
                                if (!$("#audio_search").is(':hidden')) {
                                    var p_up = $("#audio_search .on");
                                    if (p_up.length != 0) {
                                        p_up.removeClass("on");
                                    }
                                    if (p_up.next().length != 0) {
                                        p_up.next().addClass("on");
                                    } else {
                                        var first = $("#audio_search ul li").first()
                                        first.addClass("on");
                                    }
                                }
                                break;
                            case 13:
                                var songlistarray = $("#songlist .on a").attr("name").split('||');
                                postaudio.selectChatAudio(songlistarray[0], songlistarray[1], songlistarray[2]);
                                $("#audio_search").hide();
                                break;
                            default:
                                break;
                        }
                    });
            $("#songlist li").live('click', function() {
                var songlistarray = $(this).find("a").attr("name").split('||');
                postaudio.selectChatAudio(songlistarray[0], songlistarray[1], songlistarray[2]);
                $("#audio_search").hide();
            })
            //视频
            $("#video_url").live('focus',
                    function() {
                        if ($(this).val() == "请输入视频链接地址") {
                            $(this).css("color", "#5b5b5b").val("");
                        }
                    }).live('blur', function() {
                        if ($(this).val() == "") {
                            $(this).css("color", "#ccc").val("请输入视频链接地址");
                        }
                    })
            $("#link_post_video").live('click', function() {
                var url = $("#video_url").val();
                postvideo.praseChatVideo(joyconfig.ctx, url);
            })
            //标签
            $("#tags_input_chat").live('paste cut keydown keyup', function(event) {
                var tagText = $(this).val();
                if (tagText.length > 14) {
                    $(this).val(tagText.substr(0, 14));
                } else {
                    postTag.checkTagssplit('chat', event, 'postchat');
                }
            })
            $("#tags_input_chat").live("focusin focusout", function(event) {
                if (event.type == "focusout") {
                    postTag.hideTips('chat')
                    if ($(this).val() == "") {
                        $(this).css("color", "#ccc").val("请输入标签");
                    }
                } else if (event.type == 'focusin') {
                    postTag.getTagFocus('chat');
                    if ($(this).val() == "请输入标签") {
                        $(this).css("color", "#5b5b5b").val("");
                    }
                }

            });
            $("#tags_tips_chat div:eq(1) ul li").die().live('click', function() {
                postTag.setCommonTags($('form[id=postchat] input[name=tags]').size(), $(this).text(), 'chat', 'postchat');
            })
            //朋友
            $("#at_input_Chat").live('focusin keyup keydown', function(event) {
                if (event.type == "keyup") {
                    if ($(this).val() != atSearch) {
                        atSearch = $(this).val();
                        postat.searchAtFocus($(this), 'Chat');
                    }

                } else if (event.type == "focusin") {
                    var val = "";
                    $(this).css("color", "#5b5b5b");
                    if ($(this).val() != "") {
                        val = $(this).val();
                    }
                    postat.ajaxInitAtDiv(val, 'Chat');
                } else if (event.type == "keydown") {
                    //上下键选择
                    switch (event.keyCode) {
                        case 38:
                            if (!$("#ChatAtul").is(":hidden")) {
                                var libg = $("#ChatAtul .hover");
                                var id = 0;
                                if (libg.length != 0) {
                                    libg.removeClass("hover");
                                }
                                if (libg.prev().length != 0) {
                                    libg.prev().addClass("hover");
                                    id = libg.prev().attr("id") - 9;
                                } else {
                                    var hoverObj = $("#ChatAtul ul li]").last();
                                    hoverObj.addClass("hover");
                                    id = hoverObj.attr("id") - 9;
                                }

                                if (id > 0) {
                                    $("#ChatAtul").scrollTop(22 * id);
                                } else {
                                    $("#ChatAtul").scrollTop(0);
                                }
                            }
                            break;
                        case 40:
                            if (!$("#ChatAtul").is(":hidden")) {
                                var libg = $("#ChatAtul .hover");
                                var id = 0;
                                //清除.libg
                                if (libg.length != 0) {
                                    libg.removeClass("hover");
                                }
                                if (libg.next().length != 0) {
                                    libg.next().addClass("hover");
                                    id = libg.next().attr("id") - 9;
                                } else {
                                    var hoverObj = $("#ChatAtul ul li").first();
                                    hoverObj.addClass("hover");
                                    id = hoverObj.attr("id") - 9;
                                }

                                if (id > 0) {
                                    $("#ChatAtul").scrollTop(22 * id);
                                } else {
                                    $("#ChatAtul").scrollTop(0);
                                }
                            }
                            break;
                        case 13:
                            if ($("#ChatAtul .hover").size() > 0) {
                                postat.fillAtFocusToInput('chat_content', $("#ChatAtul .hover").children().attr("title"));
                                $(this).focus();
                            }

                            break;
                        default:
                            break;
                    }
                }
            });
            $("#at_input_remove_Chat").live("click", function() {
                $("#at_input_Chat").val('');
                $(this).hide();
                $("#at_input_Chat").focus();
            });
            $("a[name=atFocusChat]").live("click", function() {
                postat.fillAtFocusToInput('chat_content', $(this).attr("title"));
            });
            livephotoUpload();
            liveSubClick();
        }
    };

    //预览层注册事件
    var livephotoUpload = function() {
        //图片预览层
        $("#ul_preview").delegate('li', 'mouseenter',
                function() {
                    if ($(this).find("a").size() > 0) {
                        clearTimeout(outprevfocusTimeout);
                        var prevId = $(this).attr("id");
                        if (prevId.indexOf("li_audio") != -1) {
                            config.popwidth = 385;
                            previewAudio(config, $(this).find("a").first(), prevId);
                        } else if (prevId.indexOf("li_video") != -1) {
                            config.popwidth = 185;
                            previewVideo(config, $(this).find("a").first(), prevId);
                        } else if (prevId.indexOf("preview_photo_") != -1) {
                            previewPhoto($(this).find("a").first(), prevId);
                        }
                    }
                }).delegate('li', 'mouseleave', function() {
                    var prevId = $(this).attr("id");
                    if ($(this).find("a").size() > 0) {
                        outprevfocusTimeout = setTimeout(function() {
                            $("#prev_" + prevId).fadeOut();
                        }, 1000)
                    }
                });
    };
    //点击各个短文发布按钮事件
    var liveSubClick = function() {
        $(".edittool a").each(function() {
            $(this).bind('click', function() {
                hideFocusBtn();
                var editclass = $(this).attr('class');
                if (editclass.indexOf('_on') == -1) {
                    $(this).removeClass().addClass(editclass + '_on');
                }
            })
        });
        //焦点失去隐藏判断
        $("body").die().live('mousedown', function() {
            if (window.isOut) {
                $(".pop").hide();
                $("#mood").remove();
                hideFocusBtn();
                isOut = true;
            }
        });
        //表情
        $("#faceShow").die().live('click', function() {
            var te = new TextareaEditor(document.getElementById('chat_content'));
            var faceConfig = {
                hideCallback:function() {
                    hideFocusBtn();
                }
            }
            moodbiz.docFaceBind($(this), 'chat_content', faceConfig, te);
        });
        //短文提交按钮
        $("#post_chat_submit").die().live('click', function() {
            chatsubmit.ajaxPostChat(postConfig);
        })


        //图片 上传
        $("#relPhoto").die().live('click', function() {
            if ($("li[id^=preview_photo_]").length > 40) {
                joymealert.alert({text:'只能上传40张图片',textClass:"tipstext"})
            } else {
                $("#pic_more").removeClass().addClass("talk_pic_on")
                loadPhotoDialog($("#pic_more"), false);
            }
        })
        //图片 链接
        $("#relLinkphoto").die().live('click', function() {
            loadPhotoDialog($("#pic_more"), true);
        });
        $("a[name=chat_upload_image_loading]").die().live("click", function() {
            window.swfu.cancelQueue();
            $(this).parent().remove();
            if ($("#ul_preview li").size() == 0) {
                $("#relPhotoDialog").css({"top": $("#pic_more").offset().top + 26 + "px"});
                $("#rel_preview").hide();
            }
        });
        $("#link_post_image_url").die().live('click', function() {
            postimagelink.uploadChatImageLink($("#txt_image_url").val());
        });
        $("#txt_image_url").die().live('focus',
                function() {
                    if ($(this).val() == "请输入图片链接地址") {
                        $(this).css("color", "#5b5b5b").val("");
                    }
                }).live('blur', function() {
                    if ($(this).val() == "") {
                        $(this).css("color", "#ccc").val("请输入图片链接地址");
                    }
                });
        //切换到长文模式
        $("#link_textmodel").die().live('click', function() {
            $("#div_post_chat").hide();
            $("#div_post_text").show();
            posttextmodel.textModel(true, postConfig);
        })
        //增加APP
        $("#relApp").die().live('click', function() {
            loadAppDialog($(this));
        });
        //朋友
        $("#relFriend").die().live('click', function() {
            loadFriendDialog($(this));
        });
        //标签
        $("#relTag").die().live('click', function() {
            loadTagDialog($(this), postConfig);
        });
        //视频
        $("#relVideo").die().live('click', function() {
            if ($("#li_video_preview").size() > 0) {
                joymealert.alert({text:'只能输入一个视频',textClass:"tipstext"})
            } else {
                loadVideoDialog($(this));
            }
        });
        //音乐
        $("#relMusic").die().live('click', function() {
            if ($("#li_audio_preview").size() > 0) {
                joymealert.alert({text:'只能上传一个音乐',textClass:"tipstext"})
            } else {
                loadMusicDialog($(this));
            }
        });
    }

    //加载图片层
    var loadPhotoDialog = function(photoDom, flag) {
        var offset = photoDom.offset();
        var photoId = "relPhotoDialog";
        var imageConfig = {forclosed:true,
            containTitle:true,
            isfocus:false,
            popwidth:355,
            offset:"Custom",
            offsetlocation:[offset.top + 31,offset.left],
            hideCallback:function() {
                hideFocusBtn();
                postimagelink.resetChatImageLink();
            }
        };
        imageConfig = $.extend(config, imageConfig);

        if ($("#" + photoId).length > 0) {
            if (flag) {
                $("#photoUploadBtn").hide();
                $("#photoLinkBox").show();
                $("#piclinkbtn").addClass("tabon");
                $("#picuploadbtn").removeClass("tabon");
            } else {
                $("#photoLinkBox").hide();
                $("#photoUploadBtn").show();
                $("#picuploadbtn").addClass("tabon");
                $("#piclinkbtn").removeClass("tabon")
            }
            pop.resetOffset(imageConfig, photoId, true);
        } else {
            var htmlObj = new Object();
            htmlObj['id'] = photoId;
            htmlObj['title'] = '<div class="pichd"><ul><li id="picuploadbtn"><a href="javascript:void(0)">上传图片</a></li> <li id="piclinkbtn"><a href="javascript:void(0)">图片链接</a></li></ul></div>'
            htmlObj['html'] = '<div class="picbd" id="photoUploadBtn"><div class="piccontext"><p><a href="javascript:void(0);" id="spanButtonPlaceholder">从电脑选择图片</a></p><p id="uploadErrorText" style="display:none;color:red;"></p><p class="pt10">仅支持JPG、GIF、PNG图片文件，且单个文件小于8M</p></div></div><div id="photoLinkBox" class="picbd"><div><p>粘贴网上图片地址（可在博客中直接显示图片）</p>' +
                    '<p><input type="text" class="linktext" id="txt_image_url" name="" value="请输入图片链接地址" style="color:#ccc"><a class="submitbtn" id="link_post_image_url"><span>确 定</span></a></p>' +
                    '<p class="tipstext" id="error_image_link" si></p></div></div>';
            if (flag) {
                pop.popupInit(imageConfig, htmlObj);
                $("#" + photoId + " .hd").css({padding:0});
                $("#photoUploadBtn").hide();
                $("#piclinkbtn").addClass("tabon");
            } else {
                pop.popupInit(imageConfig, htmlObj);
                $("#" + photoId + " .hd").css({padding:0});
                $("#photoLinkBox").hide();
                $("#picuploadbtn").addClass("tabon");
            }
        }
        if (window.swfuflag) {
            swfuUploadInit();
        }

    }

    //加载音乐层
    var loadMusicDialog = function(musicDom) {
        var offset = musicDom.offset();
        var musicId = "relMusicDialog";

        var audioConfig = {
            forclosed :true,
            containTitle : true,
            popwidth : 360,
            offset : "Custom",
            offsetlocation : [offset.top + 31,offset.left],
            hideCallback:function() {
                hideFocusBtn()
            }
        }
        audioConfig = $.extend(config, audioConfig);
        if ($("#" + musicId).length > 0) {
            pop.resetOffset(audioConfig, musicId);
        } else {
            var htmlObj = new Object();
            htmlObj['id'] = musicId;
            htmlObj['html'] = '<div class="audiocon"><p><input type="text" name="" id="audio_code" class="linktext" value="请输入歌曲名\专辑名\演唱者" style="color:#ccc">' +
                    '<a class="submitbtn" id="link_upload_audio"><span>确 定</span></a></p><p class="tipstext"></p>' +
                    '<div class="search_music" id="audio_search" style="display:none"><ul id="songlist"></ul><div class="search_music_info" id="songinfo"></div></div></div>';
            htmlObj['title'] = '从在线音乐库中搜索( 支持歌曲名、专辑名、演唱者 )'
            pop.popupInit(audioConfig, htmlObj);
        }
    }
    //加载视频层
    var loadVideoDialog = function(videoDom) {
        var offset = videoDom.offset();
        var videoId = "relVideoDialog";
        var videoConfig = {forclosed:true,
            containTitle:true,
            popwidth:395,
            offset:"Custom",
            offsetlocation:[offset.top + 31,offset.left],
            hideCallback:function() {
                hideFocusBtn();
                postvideo.resetChatVideo()
            }
        };
        videoConfig = $.extend(config, videoConfig);

        if ($("#" + videoId).length > 0) {
            pop.resetOffset(videoConfig, videoId);
        } else {
            var htmlObj = new Object();
            htmlObj['id'] = videoId;
            htmlObj['html'] = '<div class="audiocon"><p><input class="audiotext" type="text" name="" id="video_url" value="请输入视频链接地址" style="color:#ccc">' +
                    '<a class="submitbtn" id="link_post_video"><span>确 定</span></a>' +
                    '</p><p class="tipstext" id="video_error"></p></div>';
            htmlObj['title'] = '支持优酷/土豆/新浪/搜狐/酷6/56/bilibili的视频播放页链接地址'
            pop.popupInit(videoConfig, htmlObj);
        }
    }
    //加载标签层
    var loadTagDialog = function(tagDom, homeOption) {
        var offset = tagDom.offset();
        var tagId = "relTagDialog";
        var tagConfig = {
            forclosed : true,
            containTitle : true,
            popwidth : 360,
            offsetlocation : [offset.top + 31,offset.left],
            hideCallback:function() {
                hideFocusBtn();
                $("#" + tagId).remove();
            }
        }
        tagConfig = $.extend(config, tagConfig);
        if ($("#" + tagId).length > 0) {
            pop.resetOffsetById(tagConfig, tagId);
        } else {
            var htmlObj = new Object();
            htmlObj['id'] = tagId;
            htmlObj['html'] = '<div id="tags_editor_chat"><div class="tagscon clearfix"><div id="tags_input_box_chat" class="linktext">' +
                    '<input name="tags" id="tags_input_chat" type="text" name="" style="color:#CCCCCC;" value="请输入标签" AUTOCOMPLETE="OFF"></div>' +
                    '<a class="submitbtn" id="tagSure"><span>确 定</span></a></p><p class="gray">多个标签可用逗号或回车分隔</p>' +
                    '<div class="searchtags" id="tags_tips_chat" style="display: none;">' +
                    '<div class="serhd">常用标签<a class="close" href="javascript:void(0)" id="close_tagBox_chat"></a></div>' +
                    '<div class="serbd clearfix">' +
                    '<ul>' +
                    '</ul></div></div></div>';
            htmlObj['title'] = '标签（帮助你把文章在网站上分类，例：小游戏）';
            pop.popupInit(tagConfig, htmlObj);
            $("#tagSure").bind("click", function() {
                if ($("#tags_input_chat").val() != "" && $("#tags_input_chat").val() != '请输入标签') {
                    postTag.setCommonTags($('form[id=postchat] input[name=tags]').size(), $("#tags_input_chat").val(), 'chat', 'postchat');
                    $("#tags_input_chat").val("请输入标签").css("color", "#ccc");
                }
                $("#" + tagId).remove();
                $("#relTagDialog").hide();
                hideFocusBtn();
            });
            $("#tags_input_box_chat").bind('click', function() {
                $("#tags_input_chat").focus();
            })
            $("#close_tagBox_chat").bind('click', function() {
                postTag.hideTips('chat');
//                $("#tags_tips_chat").remove();
                $("#tags_input_chat").die('focusin focusout').live('focusin focusout', function(event) {
                    if (event.type == "focusout") {
                        if ($(this).val() == "") {
                            $(this).css("color", "#ccc").val("请输入标签");
                        }
                    } else if (event.type == 'focusin') {
                        if ($(this).val() == "请输入标签") {
                            $(this).css("color", "#5b5b5b").val("");
                        }
                    }
                })
            });
        }
        $("form[id=postchat] input[name=tags]").each(function(i, val) {
            var id = $(this).attr('id').replace("tag_", "");
            postTag.initTagItem('chat', id, $(this).val(), 'postchat');
        })
    }
    //朋友层
    var loadFriendDialog = function(FriendDom) {
        var offset = FriendDom.offset();
        var friendId = "relFriendDialog";
        var friendConfig = {
            forclosed : true ,
            containTitle:true,
            popwidth:290,
            offsetlocation :[offset.top + 31,offset.left],
            hideCallback:function() {
                hideFocusBtn();
            }
        }
        friendConfig = $.extend(config, friendConfig);
        if ($("#" + friendId).length > 0) {
            pop.resetOffset(config, friendId);
        } else {
            var htmlObj = new Object();
            htmlObj['id'] = friendId;
            htmlObj['html'] = '<div class="friendcon" id="atfriend_area_Chat"><div class="friendload"><p>读取中，请稍候...</p></div></div>';
            htmlObj['title'] = '@朋友帐号，他就能在[提到我的]页收到 '
            pop.popupInit(config, htmlObj);
            postat.getAtFriendList("Chat")
        }
    }
    //App应用层
    var loadAppDialog = function(AppDom) {
        $("#link_textmodel").click();
        var appId = "relAppDialog";
        var postapp = require('./post-app');
        hideFocusBtn();
        setTimeout(function() {
            postapp.appinit($("#editor_app"), config, appId, hideFocusBtn);
            var appclass = $("#editor_app").attr('class');
            $("#editor_app").removeClass().addClass(appclass + '_on');
        }, 600);
        setTimeout(function() {
            $("#text_ios_url").focus();
        }, 800);
    }

    var swfuUploadInit = function() {
        var handler = require('../../third/swfupload/chathandler');
        var url = joyconfig.urlUpload + "/json/upload/single";
            if ("undefined" != typeof(SWFUpload)) {
                var settings = {
                    upload_url :url,
                    post_params : {
                        "at" : joyconfig.token
                    },

                    // File Upload Settings
                    file_size_limit : "8 MB",    // 2MB
                    file_types : "*.jpg;*.png;*.gif",
                    file_types_description : "请选择图片",
                    file_upload_limit : 0,
                    file_queue_limit : 40,

                    file_dialog_complete_handler : handler.fileDialogComplete,
                    upload_start_handler:  handler.uploadStart,
                    upload_success_handler : handler.uploadSuccess,
                    upload_complete_handler : handler.uploadComplete,
                    file_queue_error_handler : handler.fileQueueError,
                    // Button Settings
                    button_image_url : joyconfig.URL_LIB + "/static/theme/default/img/choose.jpg",
                    button_placeholder_id : "spanButtonPlaceholder",
                    button_width: 148,
                    button_height: 26,
                    button_text : '',
                    button_text_style : '',
                    button_text_top_padding: 0,
                    button_text_left_padding: 12,
                    button_window_mode: SWFUpload.WINDOW_MODE.OPAQUE,
                    button_cursor: SWFUpload.CURSOR.HAND,
                    // Flash Settings
                    flash_url : joyconfig.URL_LIB + "/static/third/swfupload/swfupload.swf",
                    flash9_url : joyconfig.URL_LIB + "/static/third/js/swfupload/swfupload_fp9.swf",

                    custom_settings : {
                        cancelButtonId : "btnCancel",
                        uploadCallback: function() {
                            livephotoUpload();
                            liveSubClick();
                        },
                        resource_path :  joyconfig.DOMAIN
                    },

                    // Debug Settings
                    debug: false}
                window.swfu = new SWFUpload(settings);
                window.swfuflag = false;
            }
    }
    if (window.navigator.userAgent.indexOf('MSIE 6.0') > 0) {
        loadPhotoDialog($("#pic_more"), true);
        $("#relPhotoDialog").hide();
    }
    //判断预览层是否没有内容...是则隐藏
    var previewLiSize = function() {
        if ($("#ul_preview li").size() == 0) {
            $("#rel_preview").hide();
        }
    }
    //预览层
    var previewPhoto = function(prevDom, prevId) {
        var prevPhotoId = "prev_" + prevId;
        if ($("#" + prevId).find('span.upload').size() != 0) {
            return;
        }
        if ($("#" + prevPhotoId).length > 0) {
            $("#" + prevPhotoId).remove();
        }
        $(".previewlist").hide();
        $("body").append('<div style="position: absolute; z-index: 9999; width: 230px; display: none;" id="' + prevPhotoId + '" class="pop previewlist">' +
                '<div class="corner"></div>' +
                '<div class="bd">' +
                '<div class="uploadpic">' +
                '<div class="viewpicload"><img width="208" height="156" src="' + prevDom.parent().find("input[name=picurl]").val() + '"></div>' +
                '</div></div></div>');
        $("#" + prevPhotoId).css({top:$("#" + prevId).offset().top + 26 + "px",left:$("#" + prevId).offset().left + "px"}).show();

        $("#" + prevPhotoId).live("mouseenter mouseleave", function(event) {
            showpreviewInit($(this), event);
        });

    }
    var previewAudio = function(popconfig, prevDom, prevId) {
        var offset = prevDom.offset();
        var prevPhotoId = "prev_" + prevId;


        var audioConfig = {forclosed : false,
            containTitle : false,
            containFoot : false,
            offsetlocation : [offset.top + 26,offset.left],
            isremovepop:true,
            addClass:true,
            className:"pop previewlist"
        };
        audioConfig = $.extend(popconfig, audioConfig);

        if ($("#" + prevPhotoId).length > 0) {
            pop.hidepopById(prevPhotoId, true, true);
        }

        var htmlObj = new Object();
        htmlObj['id'] = prevPhotoId;
        var audioTitle = $(prevDom).parent().find("input[name=audioTitle]").val();
        var audioAlbum = $(prevDom).parent().find("input[name=audioAlbum]").val();
        var audioUrl = $(prevDom).parent().find("input[name=audioUrl]").val();
        htmlObj['html'] = '<div class="uploadpic"><div class="musicl"><img src="' + audioAlbum + '" width="92" height="95"></div>' +
                '<div class="musicr"><h3>' + audioTitle + '</h3><span class="mv">' +
                '<object width="258" height="35" align="middle" codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,0,0" classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000">' +
                '<param value="sameDomain" name="allowScriptAccess">' +
                '<param value="' + audioUrl + '" name="movie">' +
                '<param value="Transparent" name="wmode">' +
                '<embed width="258" height="35" src="' + audioUrl + '" type="application/x-shockwave-flash" wmode="Transparent">' +
                '</object>' +
                '</span></div></div>';
        pop.popupInit(audioConfig, htmlObj);
        $("#" + prevPhotoId).live("mouseenter mouseleave", function(event) {
            showpreviewInit($(this), event);
        });

    }
    var previewVideo = function(popconfig, prevDom, prevId) {
        var offset = prevDom.offset();
        var prevVideoId = "prev_" + prevId;


        var videoConfig = {forclosed : false,
            containTitle : false,
            containFoot : false,
            offsetlocation : [offset.top + 26,offset.left],
            isremovepop:true,
            addClass:true,
            className:"pop previewlist"
        };
        videoConfig = $.extend(popconfig, videoConfig);

        if ($("#" + prevVideoId).length > 0) {
            pop.hidepopById(prevVideoId, true, true);
        }

        var htmlObj = new Object();
        htmlObj['id'] = prevVideoId;
        var imgSrc = $(prevDom).parent().find("input[name=videoAlbum]").val();
        var content = $(prevDom).parent().find("a:eq(0)").attr("title");
        htmlObj['html'] = '<div class="uploadpic"><a class="video_btn" title="播放" href="javascript:void(0)"></a><img src="' + imgSrc + '" width="165" height="132"></div>';
        pop.popupInit(videoConfig, htmlObj);
        $("#" + prevVideoId).live("mouseenter mouseleave", function(event) {
            showpreviewInit($(this), event);
        });

    }

    var showpreviewInit = function(dom, event) {
        if (event.type == "mouseenter") {
            clearTimeout(outprevfocusTimeout);
            clearTimeout(cleartimeoutpreview);
        } else if (event.type == "mouseleave") {
            var _this = dom;
            cleartimeoutpreview = setTimeout(function() {
                _this.fadeOut();
            }, 500)
        }
    }

    var checkModel = function (num, contentid, option) {
        var str = $("#" + contentid).val();
        var common = require('../common/common');
        var len = common.getInputLength(str);
        if (len > num) {
            //进放文章发布模式
            $("#link_textmodel").click()
        }
    }
    var hideFocusBtn = function() {
        $(".edittool a").each(function() {
            var editclass = $(this).attr('class');
            if (editclass.indexOf('_on') != -1) {
                $(this).removeClass().addClass(editclass.substr(0, editclass.length - 3));
            }
        });
        $(".edit_hd a").each(function() {
            var editclass = $(this).attr('class');
            if (editclass.indexOf('_on') != -1) {
                $(this).removeClass().addClass(editclass.substr(0, editclass.length - 3));
            }
        });
    }
    return postChat;
}
)
