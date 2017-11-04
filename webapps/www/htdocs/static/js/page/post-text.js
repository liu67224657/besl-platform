/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-5
 * Time: 下午4:21
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var oEditor;
    var moodbiz = require('../biz/mood-biz');
    var pop = require('../common/jmpopup');
    var postaudio = require('./post-audio');
    var postimagelink = require('./post-imagelink');
    var postvideo = require('./post-video');
    var postTag = require('./tag');
    var postat = require('./post-at');
    var textsubmit = require('./post-text-submit');
    var postchatModel = require('./post-chatModel');
    var joymealert = require('../common/joymealert');
    var syncObj = require('../page/sync');
    var texthandler = require('../../third/swfupload/texthandler');
    var atTextSearch = '';
    var common = require('../common/common');
    window.swftext;
    window.swfEdirorFlag = true;
    var config = {
        pointerFlag : true,//是否有指针
        pointdir : 'up',//指针方向
        tipLayer : false,//是否遮罩
        offset:"Custom",
        containTitle : true,//包含title
        containFoot : true,//包含footer
        offsetlocation:[0,0],
        className:"",
        forclosed:true,
        popwidth:0 ,
        allowmultiple:false,
        isfocus:true
    };
    var posttext = {
        posttextinit : function(homeOption) {
            if (homeOption.initTag != null && homeOption.initTag.length > 0) {
                $.each(homeOption.initTag, function(i, val) {
                    postTag.initTagItem('text', val, val, 'posttext');
                });
            }
            var loadEditorTimes = setInterval(function() {
                if ("undefined" != typeof(CKEDITOR)) {
                    loadEditor();
                    clearInterval(loadEditorTimes);
                }
            }, 500);
            $(".edit_hd a:not(:eq(0),:eq(1))").each(function() {
                $(this).bind('click', function() {
                    hideEditorFocusBtn();
                    var editclass = $(this).attr('class');
                    if (editclass.indexOf('_on') == -1) {
                        $(this).removeClass().addClass(editclass + '_on');
                    }
                })
            })
            //加粗
            $("#editor_b").die().live('click', function() {
                var selected = oEditor.getSelection();
                if (selected) {
                    oEditor.document.$.execCommand('bold', false, '');//简单的文字加粗效果 easy
                } else {
                    joymealert.alert({text:'请选择内容',textClass:"tipstext"});
                    oEditor.focus();
                }
            });
            $('#editor_hide').die().live('click', function() {
                //去掉标签
                var selection = oEditor.getSelection(),
                        range = selection && selection.getRanges(true)[0];
                if (!range) {
                    return;
                }
                var bookmarks = selection.createBookmarks();
                var htmlData = oEditor.getData();
                var dataReg = /<span\s+style="display\s?:\s?none;?"\s*>[^<]*<\/span>((?:.|\r|\n)*)<span\s+style="display\s?:\s?none;?"\s*>[^<]*<\/span>/igm;

                htmlData=htmlData.replace(dataReg,'[hide]$1[/hide]')
                htmlData = htmlData.replace(/<span[^>]+style="display\s?:\s?none;?"[^<]*?<\/span>/ig, '');
                oEditor.setData(htmlData);
//                 selection.selectBookmarks(bookmarks);
            })

            //斜体
            $("#editor_italic").die().live('click', function() {
                var selected = oEditor.getSelection();
                if (selected) {
                    oEditor.document.$.execCommand('italic', false, '');
                } else {
                    joymealert.alert({text:'请选择内容',textClass:"tipstext"});
                    oEditor.focus();
                }
            });
            //字体
            $("#editor_font").die().live('change', function() {
                var selected = oEditor.getSelection();
                var fontname = $(this).find('option:selected').val();
                if (selected) {
                    oEditor.document.$.execCommand('fontname', false, fontname);
                } else {
                    joymealert.alert({text:'请选择内容',textClass:"tipstext"});
                    oEditor.focus();
                }
            });
            //撤销
            $("#editor_undo").die().live('click', function() {
                oEditor.document.$.execCommand('undo');
            })
            //分割线
            $("#editor_split").live('click', function() {
                oEditor.insertHtml('<br /><br />---------------------华丽的分割线----------------------------<br /><br />');
            });
            //表情
            $("#editor_mood").die().live('click', function() {
                var moodConfig = {
                    hideCallback:hideEditorFocusBtn
                }
                moodbiz.editorFace($(this), oEditor, moodConfig);
            });
            //同步显示
            $('.install').die().live('click', function() {
                syncObj.showSyncArea($(this));
            });

            //同步隐藏
            $("#close_sync_info").die().live("click", function() {
                $('#sync_info').slideUp();
            });
            //图片
            $("#editor_pic").die().live('click', function() {
                loadoEditorDialog($(this));
            });
            //删除本地上传图片
            $("a[id^=delPhotoLi_]").die().live('click', function() {
                var fileid = $(this).attr("id");
                texthandler.delPhotoLi(fileid.substr('delPhotoLi_'.length, fileid.length))
            });
            $("#closePop_oEditorPhotoDialog").die().live('click', function() {
                $("#photo_upload_progress li").each(function() {
                    var fileid = $(this).attr("id");
                    texthandler.delPhotoLi(fileid.substr('upload_txt_img_'.length, fileid.length));
                });
                $("#oEditorPhotoDialog").hide();
            })
            //图片链接
            $("#editor_link").die().live('click', function() {
                loadoEditorLinkDialog($(this));
            });
            $("#link_text_img_url").die().live('click', function() {
                postimagelink.uploadTxtImageLink($('#input_text_img_link').val());
            });
            $("#input_text_img_link").die().live('focus',
                    function() {
                        if ($(this).val() == "请输入图片链接地址") {
                            $(this).css("color", "#5b5b5b").val("");
                        }
                    }).live('blur', function() {
                        if ($(this).val() == "") {
                            $(this).css("color", "#ccc").val("请输入图片链接地址");
                        }
                    });
            //音乐
            $("#editor_audio").die().live('click', function() {
                loadoEditorMusicDialog($(this));
            });

            $("#audio_oEditor_code").die().live('focus',
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
                                if (!$("#audio_text_search").is(':hidden')) {
                                    var p_up = $("#audio_text_search .on");
                                    if (p_up.length != 0) {
                                        p_up.removeClass("on");
                                    }
                                    if (p_up.prev().length != 0) {
                                        p_up.prev().addClass("on");
                                    } else {
                                        var last = $("#audio_text_search ul li").last();
                                        last.addClass("on");
                                    }
                                }
                                break;
                            case 40:
                                if (!$("#audio_text_search").is(':hidden')) {
                                    var p_up = $("#audio_text_search .on");
                                    if (p_up.length != 0) {
                                        p_up.removeClass("on");
                                    }
                                    if (p_up.next().length != 0) {
                                        p_up.next().addClass("on");
                                    } else {
                                        var first = $("#audio_text_search ul li").first()
                                        first.addClass("on");
                                    }
                                }
                                break;
                            case 13:
//                                var p_up = $("#songlist").find('.on');
                                var songlistarray = $("#songlist .on a").attr("name").split('||');
                                postaudio.selectTextAudio(songlistarray[0], songlistarray[1], songlistarray[2]);
                                $("#audio_text_search").hide();
                                break;
                            default:
                                postaudio.searchTextAudio(event, joyconfig.ctx, 1);
                                break;
                        }
                    });
            $("#t_songlist li").die().live('click', function() {
                var songlistarray = $(this).find("a").attr("name").split('||');
                postaudio.selectTextAudio(songlistarray[0], songlistarray[1], songlistarray[2]);
                $("#audio_text_search").hide();
            });
            //视频
            $("#editor_video").die().live('click', function() {
                loadoEditorVideoDialog($(this));
            });
            $("#link_post_t_video").live('click', function() {
                postvideo.praseTextVideo(joyconfig.ctx, 'oEditorVideoDialog');
            });
            $('#video_ptext_url').die().live('focus',
                    function() {
                        if ($(this).val() == "请输入视频链接地址") {
                            $(this).css("color", "#5b5b5b").val("");
                        }
                    }).live('blur', function() {
                        if ($(this).val() == "") {
                            $(this).css("color", "#ccc").val("请输入视频链接地址");
                        }
                    });
            //朋友
            $("#editor_friend").die().live('click', function() {
                loadoEditorfriendDialog($(this));
            });
            $("#at_input_Text").die().live('focusin keyup keydown', function(event) {
                if (event.type == "keyup") {
                    if ($(this).val() != atTextSearch) {
                        atTextSearch = $(this).val();
                        postat.searchAtFocus($(this), 'Text')
                    }
                } else if (event.type == "focusin") {
                    var val = ""
                    $(this).css("color", "#5b5b5b")
                    if ($(this).val() != "") {
                        val = $(this).val();
                    }
                    postat.ajaxInitAtDiv(val, 'Text');
                } else if (event.type == "keydown") {
                    //上下键选择
                    switch (event.keyCode) {
                        case 38:
                            if (!$("#TextAtul").is(":hidden")) {
                                var libg = $("#TextAtul .hover");
                                var id = 0;
                                if (libg.length != 0) {
                                    libg.removeClass("hover");
                                }
                                if (libg.prev().length != 0) {
                                    libg.prev().addClass("hover");
                                    id = libg.prev().attr("id") - 9;
                                } else {
                                    var hoverObj = $("#TextAtul ul li]").last();
                                    hoverObj.addClass("hover");
                                    id = hoverObj.attr("id") - 9;
                                }

                                if (id > 0) {
                                    $("#TextAtul").scrollTop(22 * id);
                                } else {
                                    $("#TextAtul").scrollTop(0);
                                }
                            }
                            break;
                        case 40:
                            if (!$("#TextAtul").is(":hidden")) {
                                var libg = $("#TextAtul .hover");
                                var id = 0;
                                //清除.libg
                                if (libg.length != 0) {
                                    libg.removeClass("hover");
                                }
                                if (libg.next().length != 0) {
                                    libg.next().addClass("hover");
                                    id = libg.next().attr("id") - 9;
                                } else {
                                    var hoverObj = $("#TextAtul ul li").first();
                                    hoverObj.addClass("hover");
                                    id = hoverObj.attr("id") - 9;
                                }

                                if (id > 0) {
                                    $("#TextAtul").scrollTop(22 * id);
                                } else {
                                    $("#TextAtul").scrollTop(0);
                                }
                            }
                            break;
                        case 13:
                            if ($("#TextAtul .hover").size() > 0) {
                                setTimeout(function() {
                                    postat.fillAtFocusToEditor(CKEDITOR.instances['text_content'], $("#TextAtul .hover").children().attr("title"));
                                }, 200)
                                setTimeout(function() {
                                    $("#at_input_Text").focus();
                                }, 400)
                            }
                            break;
                        default:
                            break;
                    }
                }
            });

            $("#at_input_remove_Text").live("click", function() {
                $("#at_input_Text").val('');
                $(this).hide();
                $("#at_input_Text").focus();
            });

            $("a[name=atFocusText]").die().live("click", function() {
                postat.fillAtFocusToEditor(CKEDITOR.instances['text_content'], $(this).attr("title"));
                $("#oEditorFriendDialog").hide();
            });
            //APP应用
            $("#editor_app").die().live('click', function() {
                loadoEditorappDialog($(this));
            });
            //游戏弹层
            $("#editor_game").die().live('click', function() {
                loadoEditorGameDialog($(this));
            });

            $("#text_ios_url").die().live("focusin focusout", function(event) {
                if (event.type == "focusout") {
                    if ($(this).val() == "") {
                        $(this).css("color", "#ccc").val("请输入苹果应用地址");
                    }
                } else if (event.type == 'focusin') {
                    if ($(this).val() == "请输入苹果应用地址") {
                        $(this).css("color", "#5b5b5b").val("");
                    }
                }
            });
            $("#link_post_ios").die().live('click', function() {
                var postbiz = require('../biz/post-biz')
                var postapp = require('./post-app')
                postbiz.uploadios($("#text_ios_url").val(), postapp.postAppMask);
            })
            //标签
            $("#editor_tag").die().live('click', function() {
                loadTagDialog($(this), homeOption);
            });
            $("#tags_input_text").live('paste cut keydown keyup', function(event) {
                var tagText = $(this).val();
                if (tagText.length > 14) {
                    $(this).val(tagText.substr(0, 14));
                } else {
                    postTag.checkTagssplit('text', event, 'posttext');
                }
            })

            $("#tags_tips_text div:eq(1) ul li").die().live('click', function() {
                postTag.setCommonTags($('form[id=posttext] input[name=tags]').size(), $(this).text(), 'text', 'posttext');
                //$("#tags_input_chat").blur();
            })
            $("#tagSureText").die().live('click', function() {
                hideEditorFocusBtn();
                $("#oEditroTagDialog").hide();
            })
//            $("#tags_editor_text div:eq(0) span a").die().live('click', function() {
//                postTag.delSpanItem($(this).parent().attr("id"), 'text', opts, showOption, joyconfig.ctx);
//            })
            //展开富文本框
            $('#edit_text_height').toggle(function() {
                $(this).blur();
                $(this).children('span').attr('class', 'unfold_btn01');
                $('#cke_contents_text_content').css('height', '592px');
                $('#d_editor').css('height', '625px');
                if ($.browser.msie && ($.browser.version == "6.0") && !$.support.style) {
                    $(".unfold").css("top", "667px");
                }
            }, function() {
                $(this).blur();
                $(this).children('span').attr('class', 'unfold_btn');
                $('#cke_contents_text_content').css('height', '186px');
                $('#d_editor').css('height', '220px');
                if ($.browser.msie && ($.browser.version == "6.0") && !$.support.style) {
                    $(".unfold").css("top", "262px");
                }
            });
            //长文发布
            $("#post_text_submit").die().live('click', function() {
                textsubmit.ajaxPostText(homeOption)
            });
            //
            $("#blogSubject").die().live('focusin focusout', function(event) {
                if (event.type == "focusout") {
                    if ((oEditor.getData() == null || oEditor.getData() == '') && ($("#blogSubject").val() == '' || $("#blogSubject").val() == '给你的文章加个标题吧')) {
                        $("#post_text_submit").removeClass().addClass('publish_btn')
                        $("#edit_text_submit").removeClass().addClass('publish_btn')
                    }
                    if ($(this).val() == "") {
                        $(this).css("color", "#ccc").val("给你的文章加个标题吧");
                    }
                } else if (event.type == 'focusin') {
                    postTag.getTagFocus('text');
                    if ($(this).val() == "给你的文章加个标题吧") {
                        $(this).css("color", "#5b5b5b").val("");
                    }
                    var dom = $("#post_text_submit");
                    if (!dom.hasClass('publishloadbtn')) {
                        dom.attr('class', 'publishon');
                    }
                    var editDom = $("#edit_text_submit");
                    if (!editDom.hasClass('publishloadbtn')) {
                        editDom.attr('class', 'publishon');
                    }
                }
            })
            //切换到短文模式
            $("#link_chatmodel").die().live('click', function() {
                if (postchatModel.chatModel()) {
                    $("#div_post_text").hide();
                    $("#div_post_chat").show();
                }
            });
        }
    }
    var loadoEditorDialog = function(photoDom) {
        if (oEditor != undefined) {
            if (!regexImg(oEditor)) {
                joymealert.alert({text:'一篇文章只允许只能上传40张图片',textClass:"tipstext",callbackFunction:hideEditorFocusBtn});
                return;
            }
        }
        var offset = photoDom.offset();
        var photoId = "oEditorPhotoDialog";
        var photoConfig = {
            forclosed : true,
            containTitle : true,
            popwidth : 660,
            isfocus:false,
            offsetlocation : [offset.top + 31,offset.left],
            containFoot : true,
            hideCallback:hideEditorFocusBtn
        };
        photoConfig = $.extend(config, photoConfig);
        if ($("#" + photoId).length > 0) {
            pop.resetOffset(photoConfig, photoId);
        } else {
            var htmlObj = new Object();
            htmlObj['id'] = photoId;
            htmlObj['title'] = '上传图片';
            htmlObj['html'] = '' +
                    '<div class="rel_ep_cont JmdHeight" >' +
                    '<ul class="long_list clearfix" id="photo_upload_progress">' +
                    '<div class="add">' +
                    '<div class="add_txt"  id="oEditorButtonPlaceholder"></div>' +
                    '</div></ul>' +
                    '</div>'
            htmlObj['input'] = '<input id="link_insert_photo" type="button" class="addbutton" value="加入文章">';
            pop.popupInit(photoConfig, htmlObj);
            require('../common/jquery.ui.min');
            $("#photo_upload_progress").sortable({items:'> li'});
            $("#link_insert_photo").live('click', function() {
                texthandler.insertPhotoStr();
                swfEdirorFlag = true;
                $("#" + photoId).hide().remove();
                $(".edit_hd a").each(function() {
                    var editclass = $(this).attr('class');
                    if (editclass.indexOf('_on') != -1) {
                        $(this).removeClass().addClass(editclass.substr(0, editclass.length - 3));
                    }
                });
            });
            if (swfEdirorFlag) {
                setTimeout(function() {
                    swfuploadforEdirot(texthandler);
                }, 300)
            }
        }
    }
    var loadoEditorMusicDialog = function(musicDom) {
        if (!regexAudio(oEditor)) {
            joymealert.alert({text:'一篇文章只允许上传一个音乐',textClass:"tipstext"});
            return;
        }

        var offset = musicDom.offset();
        var musicId = "oEditorMusicDialog";
        var musicConfig = {
            forclosed : true,
            containTitle : true,
            popwidth : 395,
            offset : "Custom",
            offsetlocation : [offset.top + 31,offset.left],
            containFoot : false,//包含footer
            hideCallback:hideEditorFocusBtn
        };
        musicConfig = $.extend(config, musicConfig);
        if ($("#" + musicId).length > 0) {
            pop.resetOffset(config, musicId);
        } else {
            var htmlObj = new Object();
            htmlObj['id'] = musicId;
            htmlObj['html'] = '<div class="audiocon"><p><input type="text" name="" id="audio_oEditor_code" class="audiotext" style="color:#CCCCCC;"  value="请输入歌曲名\专辑名\演唱者">' +
                    '<a class="submitbtn" id="link_oEditor_upload_audio"><span>确 定</span></a></p><p class="tipstext"></p>' +
                    '<div class="search_music" id="audio_text_search" style="display:none"><ul id="t_songlist"></ul><div class="search_music_info" id="t_songinfo"></div></div></div>';
            htmlObj['title'] = '从在线音乐库中搜索( 支持歌曲名、专辑名、演唱者 )'
            pop.popupInit(config, htmlObj);
        }
    }
    var loadoEditorLinkDialog = function(linkDom) {
        if (!regexImg(oEditor)) {
            joymealert.alert({text:'一篇文章只允许只能上传40张图片',textClass:"tipstext"});
            return;
        }

        var offset = linkDom.offset();
        var linkMaskId = "oEditorImageLinkDialog"
        var imageConfig = {forclosed:true,
            containTitle:true,
            popwidth:395,
            offset:"Custom",
            offsetlocation:[offset.top + 31,offset.left],
            hideCallback:function() {
                hideEditorFocusBtn();
                postimagelink.resetTextImageLink()
            },
            containFoot:false};
        imageConfig = $.extend(config, imageConfig);

        if ($("#" + linkMaskId).length > 0) {
            pop.resetOffset(imageConfig, linkMaskId);
        } else {
            var htmlObj = new Object();
            htmlObj['id'] = linkMaskId;
            htmlObj['html'] = '<div class="audiocon"><p><input class="audiotext" type="text" name="" id="input_text_img_link" value="请输入图片链接地址" style="color:#ccc">' +
                    '<a class="submitbtn" id="link_text_img_url"><span>确 定</span></a>' +
                    '</p><p class="tipstext" id="error_txt_img_link"></p></div>';
            htmlObj['title'] = '粘贴网上图片地址（可在博客中直接显示图片）'
            pop.popupInit(imageConfig, htmlObj);
        }
    }
    var loadoEditorVideoDialog = function(videoDom) {
        if (!regexVideo(oEditor)) {
            joymealert.alert({text:'一篇文章只允许上传一个视频',textClass:"tipstext",callbackFunction:hideEditorFocusBtn});
            return;
        }

        var offset = videoDom.offset();
        var videoId = "oEditorVideoDialog";
        var videoConfig = {forclosed:true,
            containTitle:true,
            popwidth:395,
            offset:"Custom",
            offsetlocation:[offset.top + 31,offset.left],
            containFoot : false,
            hideCallback:function() {
                hideEditorFocusBtn();
                postvideo.resetTextVideo()
            }};
        videoConfig = $.extend(config, videoConfig);

        //包含footer
        if ($("#" + videoId).length > 0) {
            pop.resetOffset(videoConfig, videoId);
        } else {
            var htmlObj = new Object();
            htmlObj['id'] = videoId;
            htmlObj['html'] = '<div class="audiocon"><p><input class="audiotext" type="text" name="" id="video_ptext_url" value="请输入视频链接地址" style="color:#ccc">' +
                    '<a class="submitbtn" id="link_post_t_video"><span>确 定</span></a>' +
                    '</p><p class="tipstext" id="video_text_error"></p></div>';
            htmlObj['title'] = '支持优酷/土豆/新浪/搜狐/酷6/56/bilibili的视频播放页链接地址'
            pop.popupInit(videoConfig, htmlObj);
        }
    }
    var loadoEditorfriendDialog = function(friendDom) {
        var offset = friendDom.offset();
        var friendId = "oEditorFriendDialog";
        var friendConfig = {
            forclosed : true ,
            containTitle:true,
            popwidth : 290,
            offsetlocation: [offset.top + 31,offset.left],
            containFoot : false,//包含footer
            hideCallback:hideEditorFocusBtn
        }
        friendConfig = $.extend(config, friendConfig);
        if ($("#" + friendId).length > 0) {
            pop.resetOffset(friendConfig, friendId);
        } else {
            var htmlObj = new Object();
            htmlObj['id'] = friendId;
            htmlObj['html'] = '<div class="friendcon" id="atfriend_area_Text"><div class="friendload"><p>读取中，请稍候...</p></div></div>';
            htmlObj['title'] = '@朋友帐号，他就能在[提到我的]页收到 '
            pop.popupInit(friendConfig, htmlObj);
            postat.getAtFriendList("Text");
        }
    }
    var loadoEditorappDialog = function(appDom) {

        if (!regexIOS(CKEDITOR.instances['text_content'])) {
            joymealert.alert({text:'一篇文章只允许上传一个苹果应用',textClass:"tipstext"});
            return;
        }
        var appId = "relAppDialog";
        var postapp = require('./post-app');
        postapp.appinit(appDom, config, appId, hideEditorFocusBtn);
    }
    var loadTagDialog = function(tagDom, homeOption) {
        var offset = tagDom.offset();
        var tagId = "oEditroTagDialog";
        var tagConfig = {
            forclosed : true,
            containTitle : true,
            popwidth : 360,
            offsetlocation : [offset.top + 31,offset.left],
            containFoot : false,//包含footer
            hideCallback:hideEditorFocusBtn
        }
        tagConfig = $.extend(config, tagConfig);
        if ($("#" + tagId).length > 0) {
            pop.resetOffset(tagConfig, tagId);
        } else {
            var htmlObj = new Object();
            htmlObj['id'] = tagId;
            htmlObj['html'] = '<div class="audiocon"><div id="tags_editor_text"><p><div id="tags_input_box_text" class="linktext" style="height: auto;">' +
                    '<input name="tags" id="tags_input_text" type="text" name="" style="color:#CCCCCC;" value="请输入标签" AUTOCOMPLETE="OFF"></div>' +
                    '<a class="submitbtn" id="tagSureText"><span>确 定</span></a></p><p class="gray">多个标签可用逗号或回车分隔</p>' +
                    '<div class="searchtags" id="tags_tips_text" style="display: none;">' +
                    '<div class="serhd">常用标签<a class="close" href="javascript:void(0)" id="close_tagBox_text"></a></div>' +
                    '<div class="serbd clearfix">' +
                    '<ul>' +
                    '</ul></div></div></div></div>';
            htmlObj['title'] = '添加标签';
            pop.popupInit(tagConfig, htmlObj);
            $("#tags_input_text").live("focusin focusout", function(event) {
                if (event.type == "focusout") {
                    postTag.hideTips('text')
                    if ($(this).val() == "") {
                        $(this).css("color", "#CCCCCC").val("请输入标签");
                    }
                } else if (event.type == 'focusin') {
                    postTag.getTagFocus('text');
                    if ($(this).val() == "请输入标签") {
                        $(this).css("color", "#5b5b5b").val("");
                    }
                }
            });
            $("#close_tagBox_text").bind('click', function() {
                postTag.hideTips('text');
                $("#tags_tips_text").remove();
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
            })
            $("#tags_input_box_text").bind('click', function() {
                $("#tags_input_text").focus();
            })
            $("#tagSureText").bind("click", function() {
                if ($("#tags_input_text").val() != "" && $("#tags_input_text").val() != '请输入标签') {
                    postTag.setCommonTags($('form[id=posttext] input[name=tags]').size(), $("#tags_input_text").val(), 'text', 'posttext');
                    $("#tags_input_text").val("请输入标签").css("color", "#ccc");
                }
                $("#oEditroTagDialog").hide();
                hideEditorFocusBtn();
            });

        }
        $("form[id=posttext] input[name=tags]").each(function(i, val) {
            postTag.initTagItem('text', $(this).val(), $(this).val(), 'posttext');
        })
    }

    var inserGameLock = false;
    var loadoEditorGameDialog = function(gameButtonDom) {
        var offset = gameButtonDom.offset();
        var tagId = "oEditorGameDialog";
        var tagConfig = {
            forclosed : true,
            containTitle : true,
            popwidth : 395,
            offsetlocation : [offset.top + 31,offset.left],
            containFoot : false,//包含footer
            hideCallback:hideEditorFocusBtn
        }
        tagConfig = $.extend(config, tagConfig);
        if ($("#" + tagId).length > 0) {
            pop.resetOffset(tagConfig, tagId);
        } else {
            var htmlObj = new Object();
            htmlObj['id'] = tagId;
            htmlObj['html'] = '<div class="audiocon">' +
                    '<p>' +
                    '<input type="text" style="color: #CCCCCC;" value="粘贴你想要添加的游戏条目地址" id="txt_insert_game" name="" class="audiotext">' +
                    '<a id="butn_insert_game" class="submitbtn"><span>确 定</span></a></p>' +
                    '<p id="error_insert_game" class="tipstext"></p></div>';
            htmlObj['title'] = '添加相关游戏条目（请粘贴游戏条目地址）';
            pop.popupInit(tagConfig, htmlObj);
            //焦点时间
            $('#txt_insert_game').live('focus',
                    function() {
                        if ($(this).val().length == 0 || $(this).val() == '粘贴你想要添加的游戏条目地址') {
                            $(this).val('').attr('style', '')
                        }
                        $('#error_insert_game').text('');
                    }).live('blur', function() {
                        if ($(this).val().length == 0) {
                            $(this).val('粘贴你想要添加的游戏条目地址').attr('style', 'color: #CCCCCC;')
                        }
                    });

            $('#butn_insert_game').live('click', function() {
                if (inserGameLock) {
                    return;
                }
                var gameUrlRegex = new RegExp(joyconfig.URL_WWW + '/game/[^/]+');

                var gameUrl = $('#txt_insert_game').val();
                var errHtmlDom = $('#error_insert_game');
                if (gameUrl.length == 0 || gameUrl == '粘贴你想要添加的游戏条目地址') {
                    errHtmlDom.html('您输入的地址不能为空');
                    return;
                } else if (gameUrl.match(gameUrlRegex) == null) {
                    errHtmlDom.html('您输入的地址不是合法的游戏条目地址，请重新输入');
                    return;
                }

                $.ajax({
                            url:joyconfig.URL_WWW + '/json/game/getbyurl',
                            type:'post',
                            data:{url:gameUrl},
                            beforeSend:function() {
                                inserGameLock = true;
                            },
                            success:function(req) {
                                var result = eval('(' + req + ')');
                                if (result.status_code != '1') {
                                    errHtmlDom.html(result.msg);
                                } else {
                                    //插入富文本框中
                                    if (result.result != null && result.result.length > 0) {
                                        var game = result.result[0];
                                        var icon = '';
                                        if (game.icon != null && game.icon.images.length > 0) {
                                            for (var i = 0; i < game.icon.images.length; i++) {
                                                icon = game.icon.images[i].ll;
                                                break;
                                            }
                                        }
                                        var src = common.genImgDomain(icon, joyconfig.DOMAIN);
                                        var gameHtml = '<img joymet="game" joymegid="' + game.resourceId + '" joymesrc="' + icon + '" src="' + src + '" /><br/>'
                                        var contentText = oEditor.getData();
                                        if (contentText != null && contentText != '' && !common.endsWithBr(contentText)) {
                                            gameHtml = '<br />' + gameHtml;
                                        }
                                        oEditor.insertHtml(gameHtml);
                                        errHtmlDom.html('');
                                        $('#txt_insert_game').val('');
                                        $('#closePop_oEditorGameDialog').click();
                                    }
                                }
                            },
                            complete:function() {
                                inserGameLock = false;
                            }
                        });

            });
        }

    }

    var swfuploadforEdirot = function(texthandler) {
        swftext = new SWFUpload({
                    // Backend Settings
                    upload_url : joyconfig.urlUpload + "/json/upload/single",
                    post_params : {
                        "at" :  joyconfig.token
                    },

                    // File Upload Settings
                    file_size_limit : "8 MB",    // 2MB
                    file_types : "*.jpg;*.png;*.gif",
                    file_types_description : "请选择图片",
                    file_upload_limit : 0,
                    file_queue_limit : 40,


                    file_dialog_complete_handler :texthandler.fileDialogComplete,
                    upload_start_handler:  texthandler.uploadStart,
                    upload_progress_handler : texthandler.uploadProgress,
                    upload_success_handler : texthandler.uploadSuccess,
                    upload_complete_handler : texthandler.uploadComplete,
                    file_queue_error_handler : texthandler.fileQueueError,

                    button_placeholder_id : "oEditorButtonPlaceholder",
                    button_width: 152,
                    button_height: 80,
                    button_text : '',
                    button_text_style : '',
                    button_text_top_padding: 0,
                    button_text_left_padding: 18,
                    button_window_mode: SWFUpload.WINDOW_MODE.TRANSPARENT,
                    button_cursor: SWFUpload.CURSOR.HAND,

                    // Flash Settings
                    flash_url :joyconfig.URL_LIB + "/static/third/swfupload/swfupload.swf",
                    flash9_url : joyconfig.URL_LIB + "/static/third/swfupload/swfupload_fp9.swf",

                    custom_settings : {
                        resource_path : joyconfig.DOMAIN
                    },

                    // Debug Settings
                    debug: false
                });
        swfEdirorFlag = false;
    }
    if (window.navigator.userAgent.indexOf('MSIE 6.0') > 0) {
        loadoEditorDialog($("#editor_pic"));
        $("#oEditorPhotoDialog").hide();
    }

    var regexIOS = function (editor) {
        var editorValue = editor.getData();
        blogContent.ios = null;
        var videoRegex = /<img\s+joymeappt=\"ios\"\s+joymec=\"([^>\"]+)\"\s+joymed=\"([^>\"]+)\"\s+joymei=\"([^>\"]+)\"\s+joymer=\"([^>\"]+)\"\s+joymet=\"app\"\s+src=\"([^>\"]+)\"\s+title="([^>\"]*)"\s*\/?>/gi;
        var joymeImgEle = videoRegex.exec(editorValue);
        if (joymeImgEle != null) {
            blogContent.ios = {icon:joymeImgEle[3],card:joymeImgEle[1],url:joymeImgEle[4],desc:joymeImgEle[2]};
            return false;
        } else {
            return true;
        }
    }

    var regexAudio = function (editor) {
        var editorValue = editor.getData();
        blogContent.audio = null;
        var audioRegex = /<img\s+joymed=\"([^>]*)\"\s+joymef=\"([^>]+)\"\s+joymet=\"audio\"\s+src=\"([^>]+)\"\s+title=\"([^>]*)\"\s*(\/>)/g;
        var joymeImgEle = audioRegex.exec(editorValue);
        if (joymeImgEle != null) {
            blogContent.audio = {album:joymeImgEle[3],flash:joymeImgEle[2],title:joymeImgEle[1]};
            return false;
        } else {
            return true;
        }
    }

    var regexImg = function (editor) {
        var editorValue = editor.getData();

        //将数量反解析成blogContent
        blogContent.image = new Array();
        //正则得到编辑器中所有的图片元素的数量
        var imgRegex = /<img\s+joymed=\"([^>]*)\"\s+joymet=\"img\"\s+joymeu=\"([^>]+)\"\s+(?:style="[^>"]*"\s+)?src=\"([^>]+)\"\s*(\/>)/g;
        var matches = editorValue.match(imgRegex);
        if (matches != null) {
            for (var i = 0; i < matches.length; i++) {
                var imgHtml = matches[i];
                var parternElement = new RegExp(imgRegex.source);
                var joymeImgEle = parternElement.exec(imgHtml);
                blogContent.image.push({key:i,value:{url:joymeImgEle[2],src:joymeImgEle[3],desc:''}});
            }
        }
        if (blogContent.image.length > 40) {
            return false;
        } else {
            return true;
        }
    }

    var regexVideo = function (editor) {
        var editorValue = editor.getData();

        blogContent.video = null;
        var videoRegex = /<img\s+joymed=\"([^>]*)\"\s+joymef=\"([^>]+)\"\s+joymeo=\"([^>]+)\"\s+joymet=\"video\"\s+joymevtime=\"([^>]*)\"\s+src=\"([^>]+)\"\s+title=\"([^>]*)\"\s*(\/>)/g;
        var joymeImgEle = videoRegex.exec(editorValue);
        if (joymeImgEle != null) {
            blogContent.video = {album:joymeImgEle[5],flash:joymeImgEle[2],title:joymeImgEle[1],orgUrl:joymeImgEle[3],vtime:joymeImgEle[4]};
            return false;
        } else {
            return true;
        }
    }
    var hideEditorFocusBtn = function() {
        $(".edit_hd a").each(function() {
            var editclass = $(this).attr('class');
            if (editclass.indexOf('_on') != -1) {
                $(this).removeClass().addClass(editclass.substr(0, editclass.length - 3));
            }
        });
    }
    var loadEditor = function() {
        oEditor = CKEDITOR.replace('text_content');
        oEditor.on('paste', function(evt) {
            var paste = require('./ckeditor-paste');
            paste.pasteInit(evt, oEditor);
        });
        oEditor.on('focus', function(evt) {
            var dom = $("#post_text_submit");
            if (!dom.hasClass('publishloadbtn')) {
                dom.attr('class', 'publishon');
            }
            var editDom = $("#edit_text_submit");
            if (!editDom.hasClass('publishloadbtn')) {
                editDom.attr('class', 'publishon');
            }
        });
        oEditor.on('blur', function(evt) {
            if ((oEditor.getData() == null || oEditor.getData() == '') && ($("#blogSubject").val() == '' || $("#blogSubject").val() == '给你的文章加个标题吧')) {
                $("#post_text_submit").removeClass().addClass('publish_btn')
                $("#edit_text_submit").removeClass().addClass('publish_btn')
            }
        });
    }
    return posttext;
})