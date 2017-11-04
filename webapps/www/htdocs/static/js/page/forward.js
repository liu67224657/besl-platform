/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-5
 * Time: 下午6:12
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var common = require('../common/common');
    var login = require('../biz/login-biz');
    var ajaxverify = require('../biz/ajaxverify');
    var joymealert = require('../common/joymealert');
    var pop = require('../common/jmpopup');
    var moodBiz = require('../biz/mood-biz');
    var forwardBiz = require('../biz/forward-biz');
    var tag = require('./tag');
    var postsync = require('./post-sync');
    var contentGenerator = require('./post-contentgenerator');
    var joymealert = require('../common/joymealert');
    require('../common/tips');

    var forward = {
        bindForwardMask:function() {
            $("a[id^='forward_']").live('click', function() {
                if (login.checkLogin($(this))) {
                    var uno = $(this).attr('data-bloguno');
                    var blogid = $(this).attr('data-blogid');
                    window.isOut = null;
                    showForwardMask(blogid, uno, forwardSubmitCallback.forwardSubmitCallback);
                }
            });
        },

        bindForwardNotAppendMask:function() {
            $("a[id^='forward_']").live('click', function() {
                if (login.checkLogin($(this))) {
                    var uno = $(this).attr('data-bloguno');
                    var blogid = $(this).attr('data-blogid');
                    window.isOut = null;
                    showForwardMask(blogid, uno, forwardSubmitCallback.forwardNotAppendContentCallback);
                }
            });
        },

        bindForwardMaskOnBlog:function() {
            $("a[id^='forward_']").live('click', function() {
                if (login.checkLogin($(this))) {
                    var uno = $(this).attr('data-bloguno');
                    var blogid = $(this).attr('data-blogid');
                    showForwardMask(blogid, uno, forwardSubmitCallback.forwardOnBlogCallback);
                }
            });
        },

        bindEditForwardMask:function() {
            $('a[id^=edit_forward_]').live('click', function() {
                if (login.checkLogin($(this))) {
                    var uno = $(this).attr('data-bloguno');
                    var blogid = $(this).attr('data-blogid');
                    window.isOut = null;
                    showEditMask(blogid, uno, forwardSubmitCallback.editForwardCallback);
                }
            })
        },

        bindEditForwardMaskOnBlog:function() {
            $('a[id^=edit_forward_]').live('click', function() {
                if (login.checkLogin($(this))) {
                    var uno = $(this).attr('data-bloguno');
                    var blogid = $(this).attr('data-blogid');
                    showEditMask(blogid, uno, forwardSubmitCallback.editForwardInBlogCallback);
                }
            })
        },

        bindForwardCtrlCenter:function() {
            $("#forward_content").live('keydown', function(event) {
                if (joyconfig.joyuserno == '') {
                    login.maskLogin();
                    return;
                }
                common.joymeCtrlEnter(event, $("#submit_forward"));
            })
        },
        forwardDiscovertOnWall:function(id, uno, forwardSubmitCallbackOnWall) {
            bindsubmit(id, uno, forwardSubmitCallbackOnWall);
        }
    }
    var showForwardMask = function(blogid, bloguno, forwardSubmitCallback) {
        var privilegeObj = ajaxverify.verifyPrivilege(true);
        if (privilegeObj.status_code == '-1') {
            loginBiz.maskLoginByJsonObj(privilegeObj);
            return;
        } else if (privilegeObj.status_code != '1') {
            var alertOption = {text:privilegeObj.msg,tipLayer:true,textClass:"tipstext"};
            joymealert.alert(alertOption);
            return;
        }
        forwardBiz.getForwardBlog(blogid, bloguno, true, function() {
            forwardMaskCallback.loadForwardMask('forwardmask', '转帖文章');
        }, forwardMaskCallback.getForwardMaskCallback, forwardSubmitCallback);
    }

    var showEditMask = function(blogid, bloguno, editForwardCallback) {
        var privilegeObj = ajaxverify.verifyPrivilege(true);
        if (privilegeObj.status_code == '-1') {
            loginBiz.maskLoginByJsonObj(privilegeObj);
            return;
        } else if (privilegeObj.status_code != '1') {
            var alertOption = {text:privilegeObj.msg,tipLayer:true,textClass:"tipstext"};
            joymealert.alert(alertOption);
            return;
        }
        forwardBiz.getForwardBlog(blogid, bloguno, false, function() {
            forwardMaskCallback.loadForwardMask('editforwardmask', '编辑转贴');
        }, forwardMaskCallback.getEditForwardMaskCallback, editForwardCallback);
    }

    var forwardMaskCallback = {
        getForwardMaskCallback:function(resultMsg, forwardSubmitCallback) {

            if (resultMsg.status_code == '-1') {
                pop.hidepopById('forwardmask', true, true, null);
                login.maskLoginByJsonObj(resultMsg);
                return;
            } else if (resultMsg.status_code == '1') {
                forwardMaskInit(resultMsg, forwardSubmitCallback);
            } else if (resultMsg.status_code == '-5') {
                pop.hidepopById('forwardmask', true, true, null);
                var alertOption = {text:tipsText.profile.user_ipforbidden_forbidpost,
                    tipLayer:true,
                    textClass:"tipstext",
                    callbackFunction:function() {
                        if (resultMsg.result != null && resultMsg.result.length > 0) {
                            window.location.href = resultMsg.result[0];
                        }
                    }};
                joymealert.alert(alertOption);
            } else {
                pop.hidepopById('forwardmask', true, true, null);
                var alertOption = {text:resultMsg.msg,tipLayer:true,textClass:"tipstext"};
                joymealert.alert(alertOption);
            }
        },
        loadForwardMask:function(maskid, title) {
            var forwardJmPopConfig = {
                pointerFlag : false,//是否有指针
                tipLayer : true,//是否遮罩
                containTitle : true,//包含title
                containFoot : false,//包含footer
                forclosed:true,
                offset:"",
                popwidth:505,
                popscroll:true,
                allowmultiple:false,
                isremovepop:true,
                hideCallback:moodBiz.hideFace
            };
            var htmlObj = new Object();
            htmlObj['id'] = maskid; //设置层ID
            htmlObj['html'] = '<div class="zhuantie" id="loading_mask"><div class="zhuantieload"></div></div>';//设置内容
            htmlObj['title'] = title;
            pop.popupInit(forwardJmPopConfig, htmlObj);
        },

        getEditForwardMaskCallback:function(resultMsg, forwardEditCallback) {
            if (resultMsg.status_code == '-1') {
                pop.hidepopById('editforwardmask', true, true, null);
                login.maskLoginByJsonObj(resultMsg);
                return;
            } else if (resultMsg.status_code == '1') {
                editForwardMaskInit(resultMsg, forwardEditCallback);
            } else if (resultMsg.status_code == '-5') {
                pop.hidepopById('editforwardmask', true, true, null);
                var alertOption = {text:tipsText.profile.user_ipforbidden_forbidpost,
                    tipLayer:true,
                    textClass:"tipstext",
                    callbackFunction:function() {
                        if (resultMsg.result != null && resultMsg.result.length > 0) {
                            window.location.href = resultMsg.result[0];
                        }
                    }};
                joymealert.alert(alertOption);
            } else {
                pop.hidepopById('editforwardmask', true, true, null);
                var alertOption = {text:resultMsg.msg,tipLayer:true,textClass:"tipstext"};
                joymealert.alert(alertOption);
            }
        }
    }

    var forwardSubmitCallback = {
        forwardSubmitCallback:function(forwardParam, resultMsg) {
            login.locationLoginByJsonObj(resultMsg);

            if (resultMsg.status_code == "1") {

                var blogContent = resultMsg.result[0];
                common.increaseCount('r_blog_num', 1);
                var forwardMaskId = 'forwardmask';
                if (blogContent.content.parentContentId != '' && blogContent.content.rootContentId != blogContent.content.parentContentId) {
                    common.increaseCount('forward_root_num_' + blogContent.content.rootContentId, 1);
                    common.increaseCount('forward_num_' + blogContent.content.parentContentId, 1);
                } else {
                    common.increaseCount('forward_num_' + blogContent.content.rootContentId, 1);
                }

                if (forwardParam.replypcid != null && forwardParam.replypcid.length > 0) {
                    common.increaseCount('feedbacknum_' + blogContent.content.parentContentId, 1);
                }
                if (forwardParam.replyrcid && forwardParam.replyrcid.length > 0) {
                    common.increaseCount('feedbacknum_' + blogContent.content.rootContentId, 1);
                }

                pop.hidepopById(forwardMaskId, true, true, moodBiz.hideFace);

                var sucesMsg = '转发成功';
                if (resultMsg.billingStatus) {
                    sucesMsg += resultMsg.billingMsg;
                }
                var alertOption = {text:sucesMsg,tipLayer:true,callbackFunction:function() {
                    var contentObj = contentGenerator.generator(resultMsg.result[0]);
                    $('#post_area').after(contentObj.contentHtml);
                    $('#' + contentObj.htmlId).fadeIn();
                }};
                joymealert.alert(alertOption);
            } else {
                var alertOption = {};
                if (resultMsg.status_code == '-5') {
                    alertOption = {text:tipsText.profile.user_ipforbidden_forbidpost,
                        tipLayer:true,
                        textClass:"tipstext",
                        callbackFunction:function() {
                            pop.hidepopByJqObj($('div[id^=forwardmask_]'), true, true, moodBiz.hideFace);
                            if (resultMsg.result != null && resultMsg.result.length > 0) {
                                window.location.href = resultMsg.result[0];
                            }
                        }};
                } else {
                    alertOption = {text: resultMsg.msg,tipLayer:true,textClass:'tipstext',callbackFunction:function() {
                        pop.hidepopByJqObj($('div[id^=forwardmask_]'), true, true, moodBiz.hideFace);
                    }};
                }
                joymealert.alert(alertOption);
            }
        },

        forwardSubmitLoadingCallback:function(butonId, text) {
            $('#' + butonId).attr('class', 'loadbtn fr').html('<span><em class="loadings"></em>' + text + '</span>');
        },

        forwardSubmitCompleteCallback:function(butonId, text) {
            $('#' + butonId).attr('class', 'submitbtn mr').html('<span>' + text + '</span>');
        },

        forwardOnBlogCallback:function(forwardParam, resultMsg) {
            login.locationLoginByJsonObj(resultMsg);
            if (resultMsg.status_code == "1") {

                var blogContent = resultMsg.result[0];

                var forwardMaskId = 'forwardmask';
                if (blogContent.content.parentContentId != '' && blogContent.content.rootContentId != blogContent.content.parentContentId) {
                    common.increaseCount('forward_root_num_' + blogContent.content.rootContentId, 1);
                    common.increaseCount('forward_num_' + blogContent.content.parentContentId, 1);
                } else {
                    common.increaseCount('forward_num_' + blogContent.content.rootContentId, 1);
                }

                if (forwardParam.replypcid != null && forwardParam.replypcid.length > 0) {
                    common.increaseCount('replynum_' + blogContent.content.parentContentId, 1);
                }
                if (forwardParam.replyrcid && forwardParam.replyrcid.length > 0) {
                    common.increaseCount('replynum_' + blogContent.content.rootContentId, 1);
                }

                var sucesMsg = '转发成功';
                if (resultMsg.billingStatus) {
                    sucesMsg += resultMsg.billingMsg;
                }
                pop.hidepopById(forwardMaskId, true, true, moodBiz.hideFace);

                var alertOption = {text:sucesMsg,tipLayer:true};
                joymealert.alert(alertOption);
            } else {
                var alertOption = {};
                if (resultMsg.status_code == '-5') {
                    alertOption = {text:tipsText.profile.user_ipforbidden_forbidpost,
                        tipLayer:true,
                        textClass:"tipstext",
                        callbackFunction:function() {
                            pop.hidepopByJqObj($('div[id^=forwardmask_]'), true, true, moodBiz.hideFace);
                            if (resultMsg.result != null && resultMsg.result.length > 0) {
                                window.location.href = resultMsg.result[0];
                            }
                        }};
                } else {
                    alertOption = {text: resultMsg.msg,tipLayer:true,textClass:'tipstext',callbackFunction:function() {
                        pop.hidepopByJqObj($('div[id^=forwardmask_]'), true, true, moodBiz.hideFace);
                    }};
                }
                joymealert.alert(alertOption);
            }
        },

        forwardNotAppendContentCallback:function(forwardParam, resultMsg) {
            login.locationLoginByJsonObj(resultMsg);
            if (resultMsg.status_code == "1") {

                var blogContent = resultMsg.result[0];
                common.increaseCount('r_blog_num', 1);

                var forwardMaskId = 'forwardmask';
                if (blogContent.content.parentContentId != '' && blogContent.content.rootContentId != blogContent.content.parentContentId) {
                    common.increaseCount('forward_root_num_' + blogContent.content.rootContentId, 1);
                    common.increaseCount('forward_num_' + blogContent.content.parentContentId, 1);
                } else {
                    common.increaseCount('forward_num_' + blogContent.content.rootContentId, 1);
                }

                if (forwardParam.replypcid != null && forwardParam.replypcid.length > 0) {
                    common.increaseCount('feedbacknum_' + blogContent.content.parentContentId, 1);
                }
                if (forwardParam.replyrcid && forwardParam.replyrcid.length > 0) {
                    common.increaseCount('feedbacknum_' + blogContent.content.rootContentId, 1);
                }

                var sucesMsg = '转发成功';
                if (resultMsg.billingStatus) {
                    sucesMsg += resultMsg.billingMsg;
                }
                pop.hidepopById(forwardMaskId, true, true, moodBiz.hideFace);

                var alertOption = {text:sucesMsg,tipLayer:true};
                joymealert.alert(alertOption);
            } else {
                var alertOption = {};
                if (resultMsg.status_code == '-5') {
                    alertOption = {text:tipsText.profile.user_ipforbidden_forbidpost,
                        tipLayer:true,
                        textClass:"tipstext",
                        callbackFunction:function() {
                            pop.hidepopByJqObj($('div[id^=forwardmask_]'), true, true, moodBiz.hideFace);
                            if (resultMsg.result != null && resultMsg.result.length > 0) {
                                window.location.href = resultMsg.result[0];
                            }
                        }};
                } else {
                    alertOption = {text: resultMsg.msg,tipLayer:true,textClass:'tipstext',callbackFunction:function() {
                        pop.hidepopByJqObj($('div[id^=forwardmask_]'), true, true, moodBiz.hideFace);
                    }};
                }
                joymealert.alert(alertOption);
            }
        },

        editForwardCallback:function(forwardParam, resultMsg) {
            login.locationLoginByJsonObj(resultMsg);
            if (resultMsg.status_code == "1") {

                var blogContent = resultMsg.result[0];

                pop.hidepopById('editforwardmask', true, true, moodBiz.hideFace);

                var alertOption = {text:'编辑成功',tipLayer:true,callbackFunction:function() {
                    var contentObj = contentGenerator.generator(resultMsg.result[0]);
                    $('#conent_' + blogContent.content.contentId).replaceWith(contentObj.contentHtml)
                    $('#conent_' + blogContent.content.contentId).css('display', '');
                }};
                joymealert.alert(alertOption);
            } else {
                var alertOption = {};
                if (resultMsg.status_code == '-5') {
                    alertOption = {text:tipsText.profile.user_ipforbidden_forbidpost,
                        tipLayer:true,
                        textClass:"tipstext",
                        callbackFunction:function() {
                            pop.hidepopByJqObj($('div[id^=forwardmask_]'), true, true, moodBiz.hideFace);
                            if (resultMsg.result != null && resultMsg.result.length > 0) {
                                window.location.href = resultMsg.result[0];
                            }
                        }};
                } else {
                    alertOption = {text: resultMsg.msg,tipLayer:true,textClass:'tipstext',callbackFunction:function() {
                        pop.hidepopByJqObj($('div[id^=forwardmask_]'), true, true, moodBiz.hideFace);
                    }};
                }
                joymealert.alert(alertOption);
            }
        },
        editForwardInBlogCallback:function(resultMsg) {
            login.locationLoginByJsonObj(resultMsg);
            if (resultMsg.status_code == "1") {

                var blogContent = resultMsg.result[0];

                pop.hidepopById('editforwardmask', true, true, moodBiz.hideFace);
                var alertOption = {text:'编辑成功',tipLayer:true,callbackFunction:function() {
                    $('#forward_content_area').text(blogContent.content.content);
                }};
                joymealert.alert(alertOption);
            } else {
                var alertOption = {};
                if (resultMsg.status_code == '-5') {
                    alertOption = {text:tipsText.profile.user_ipforbidden_forbidpost,
                        tipLayer:true,
                        textClass:"tipstext",
                        callbackFunction:function() {
                            pop.hidepopByJqObj($('div[id^=forwardmask_]'), true, true, moodBiz.hideFace);
                            if (resultMsg.result != null && resultMsg.result.length > 0) {
                                window.location.href = resultMsg.result[0];
                            }
                        }};
                } else {
                    alertOption = {text: resultMsg.msg,tipLayer:true,textClass:'tipstext',callbackFunction:function() {
                        pop.hidepopByJqObj($('div[id^=forwardmask_]'), true, true, moodBiz.hideFace);
                    }};
                }
                joymealert.alert(alertOption);
            }
        }
    }


    function forwardMaskInit(resultMsg, forwardSubmitCallback) {
        var blogContent = resultMsg.result[0];
        var tagList = resultMsg.result[1];
        var syncProvider = resultMsg.result[2];

        var content = '';
        if (blogContent.rootContent != null) {
            content = '//@' + blogContent.profile.blog.screenName + '：' + blogContent.content.content;
        }

        var forwardLengthText = '';
        var forwardContentLength = Math.ceil(tipsText.comment.blog_reply_length - common.getInputLength(content));
        if (forwardContentLength < 0) {
            forwardLengthText = "已超过<b style='color:red'>" + Math.abs(forwardContentLength) + "</b>字";
        } else {
            forwardLengthText = "还可以输入<b>" + forwardContentLength + "</b>字";
        }


        var snycCheckBoxHtml = ''
        if (syncProvider != null && syncProvider.length > 0) {
            snycCheckBoxHtml = '<input class="publish_s" type="checkbox" id="chk_forward_sync" value="true" checked="true" />';
        } else {
            snycCheckBoxHtml = '<input class="publish_s" type="checkbox" id="chk_forward_sync" value="true" disabled="true" />';
        }

        var checkBox = '';
        if (blogContent.rootContent != null && blogContent.rootProfile.blog.uno != blogContent.profile.blog.uno) {
            checkBox += '<p><input type="checkbox" class="checktext" id="chk_replyrcid" name="replyrcid" value="' + blogContent.rootContent.contentId + '">' +
                    '<label for="checkbox01">同时评论给原文作者</label>' +
                    '<a href="'+joyconfig.URL_WWW + '/people/' + blogContent.rootProfile.blog.domain + '" class="author">' + blogContent.rootProfile.blog.screenName + '</a>' +
                    '</p>';
            checkBox += '<p><input type="checkbox" class="checktext" id="chk_replypcid" name="replypcid" value="' + blogContent.content.contentId + '">同时评论给' +
                    '<a href="'+joyconfig.URL_WWW + '/people/' + blogContent.profile.blog.domain + '" class="author">' + blogContent.profile.blog.screenName + '</a></p>';
        } else {
            checkBox += '<p><input type="checkbox" class="checktext" id="chk_replyrcid" name="replyrcid" value="' + blogContent.content.contentId + '"/>' +
                    '<label for="checkbox01">同时评论给</label>' +
                    '<a href="'+joyconfig.URL_WWW + '/people/' + blogContent.profile.blog.domain +'" class="author">' + blogContent.profile.blog.screenName + '</a>' +
                    '</p>';
        }

        $('#loading_mask').replaceWith('<div id="forwardform"><div class="zhuantie"><div class="related clearfix">' +
                '<div class="commenface" id="forwadmask_mood"></div>' +
                '<span id="forward_inputnum" class="limited right">' + forwardLengthText + '</span></div>' +
                '<textarea class="textcont" rows="" cols="" name="content" id="forward_content" style="font-family:Tahoma, \'宋体\';">' + content + '</textarea>' +
                '<p class="tipstext" id="forwardmask_error"></p>' +
                '<div class="addtags clearfix" id="tags_editor_forward">' +
                '<input class="addtagtxt" type="text" name="tags" id="tags_input_forward" AUTOCOMPLETE="OFF" style="color:#CCCCCC;" value="加个标签吧（多个标签用逗号或空格分开）"/>' +
                '</div>' +
                '<div class="commontag">' + checkBox +
                '</div></div>');
        $('#forwardform').parent().after('<div class="ft ftr"><span class="tongbu">' +
                snycCheckBoxHtml +
                '同步' +
                '<em class="install" id="forward_syn_install"></em>' +
                '</span><a id="submit_forward" class="submitbtn mr" href="javascript:void(0);"><span>转 发</span></a></div>');//设置按钮内容

        $('#submit_forward').die().live('click', function() {
            bindsubmit(blogContent.content.contentId, blogContent.profile.blog.uno, forwardSubmitCallback);
        });
        setTimeout(function() {
            bindForwardMask(blogContent);
            $("#forward_content").focus()
        }, 200);
        $('#forward_content').die().live('keydown keyup', function(event) {
            common.joymeCtrlEnter(event, $('#submit_forward'));
        });
    }

    function editForwardMaskInit(resultMsg, forwardEditCallback) {
        var blogContent = resultMsg.result[0];

        var content = blogContent.content.content;

        var forwardLengthText = '';
        var forwardContentLength = Math.ceil(300 - common.getInputLength(content));
        if (forwardContentLength < 0) {
            forwardLengthText = "已超过<b style='color:red'>" + Math.abs(forwardContentLength) + "</b>字";
        } else {
            forwardLengthText = "还可以输入<b>" + forwardContentLength + "</b>字";
        }

        $('#loading_mask').replaceWith('<div id="editforwardform"><div class="zhuantie"><div class="related clearfix">' +
                '<div class="commenface" id="edit_forwadmask_mood"></div>' +
                '<span id="edit_forward_inputnum" class="limited right">' + forwardLengthText + '</span></div>' +
                '<textarea class="textcont" rows="" cols="" name="content" id="edit_forward_content" style="font-family:Tahoma, \'宋体\';">' + content + '</textarea>' +
                '<p class="tipstext" id="editforwardmask_error"></p>' +
                '<div class="addtags clearfix" id="tags_editor_editforward">' +
                '<input class="addtagtxt" type="text" name="tags" id="tags_input_editforward" AUTOCOMPLETE="OFF" style="color:#CCCCCC;" value="加个标签吧（多个标签用逗号或空格分开）"/>' +
                '</div></div></div>');
        $('#editforwardform').parent().after('<div class="ft ftr"><a id="edit_forward" class="submitbtn mr" href="javascript:void(0);"><span>编 辑</span></a></div>');

        bindEditForwardMask(blogContent);

        $('#edit_forward').die().live('click', function() {
            bindEditsubmit(blogContent.content.contentId, blogContent.profile.blog.uno, forwardEditCallback);
        });
        $('#edit_forward_content').die().live('keydown', function(event) {
            common.joymeCtrlEnter(event, $('#edit_forward'));
        })
    }

    function bindForwardMask(blogContent) {
        bindMoody('forwadmask_mood', 'forward_content');
        bindCheckInput('forward_content', 'forward_inputnum', 'forwardmask_error');
        bindTag('tags_input_forward', 'forward', 'forwardform', blogContent.content.contentTag);
        bindSync();
    }

    function bindEditForwardMask(blogContent) {
        bindCheckInput('edit_forward_content', 'edit_forward_inputnum', 'editforwardmask_error');
        bindMoody('edit_forwadmask_mood', 'edit_forward_content');
        bindTag('tags_input_editforward', 'editforward', 'editforwardform', blogContent.content.contentTag);
    }

    function bindCheckInput(textid, contAreaId, errorId) {
        $("#" + textid).keyup(
                function() {
                    common.checkInputLength(tipsText.comment.blog_reply_length, textid, contAreaId);
                }).focusin(function() {
                    $('#' + errorId).html('');
                });
    }

    function bindMoody(moodButId, textId) {
        var te = new TextareaEditor(document.getElementById(textId));
        $('#' + moodButId).die().live('click', function() {
            var config = {
                allowmultiple:true,
                isremovepop:true,
                isfocus:false
            };
            moodBiz.docFaceBindOnReply($(this), textId, config, te);
        });
    }

    function bindTag(tagTextId, tagInputId, tagFormId, userTag) {
        $("#" + tagTextId).die().live("keyup",
                function(event) {
                    $(this).css('color', '#494949');
                    tag.checkTagssplit(tagInputId, event, tagFormId);
                }).live('focusout',
                function() {
                    var value = $(this).val();
                    if (value == '加个标签吧（多个标签用逗号或空格分开）') {
                        $(this).val('');
                    } else if (value == '') {
                        $(this).val('加个标签吧（多个标签用逗号或空格分开）').css('color', '#CCCCCC');
                    }
                }).live('focusin', function() {
                    var value = $(this).val();
                    if (value == '加个标签吧（多个标签用逗号或空格分开）') {
                        $(this).val('');
                    } else if (value == '') {
                        $(this).val('加个标签吧（多个标签用逗号或空格分开）').css('color', '#CCCCCC');
                    }
                });
        if (userTag != null) {
            $.each(userTag.tags, function(i, val) {
                tag.initTagItem(tagInputId, val, val, tagFormId);
            });
        }
    }

    function bindSync() {
        //同步显示
        $('#forward_syn_install').click(function() {
            var offset = $(this).offset();
            var syncDivId = "forward_sync_info";
            var popConfig = {
                pointerFlag : true,//是否有指针
                tipLayer : false,//是否遮罩
                offset:"Custom",
                containTitle : true,//包含title
                containFoot : false,//包含footer
                offsetlocation:[offset.top + 19,offset.left - 50],
                forclosed:true,
                popwidth:240 ,
                allowmultiple:true
            };
            if ($("#" + syncDivId).size() > 0) {
                pop.resetOffsetById(popConfig, syncDivId);
            } else {
                var syncHtml = postsync.getSyncHtml();

                var htmlObj = new Object();
                htmlObj['id'] = syncDivId;
                htmlObj['html'] = syncHtml;
                htmlObj['title'] = '您的内容将被同步到以下社区'
                pop.popupInit(popConfig, htmlObj);
            }
        });
    }


    function bindsubmit(cid, cuno, submitCallback) {

        var content = $("#forward_content").val();
        var tags = '';
        $.each($('#forwardform input[name=tags]'), function(i, val) {
            if (val.value != '加个标签吧（多个标签用逗号或空格分开）') {
                tags += val.value + ',';
            }
        });

        var replypcid = '';
        var replyrcid = '';
        if ($("#chk_replypcid").attr('checked')) {
            replypcid = $("#chk_replypcid").val();
        }

        if ($("#chk_replyrcid").attr('checked') != undefined) {
            if ($("#chk_replyrcid").attr('checked')) {
                replyrcid = $("#chk_replyrcid").val();
            }
        }
        var forwardParam = new Object();
        forwardParam.cid = cid;
        forwardParam.cuno = cuno;
        forwardParam.content = content;
        forwardParam.replypcid = replypcid;
        forwardParam.replyrcid = replyrcid,
                forwardParam.tags = tags,
                forwardParam.sync = $('#chk_forward_sync').attr('checked');
        forwardParam.submitCallback = submitCallback;
        forwardParam.loadingCallback = function() {
            forwardSubmitCallback.forwardSubmitLoadingCallback('submit_forward', '发布中…');
        }
        forwardParam.completeCallback = function() {
            forwardSubmitCallback.forwardSubmitCompleteCallback('submit_forward', '转 发');
        }

        var returnError = forwardBiz.forward(forwardParam);
        if (returnError != null && returnError.length > 0) {
            $('#forwardmask_error').html(returnError);
            return false;
        }
        return true;
    }


    function bindEditsubmit(cid, cuno, forwardEditCallback) {

        var content = $("#edit_forward_content").val();
        var tags = '';
        $.each($('#editforwardform input[name=tags]'), function(i, val) {
            if (val.value != '加个标签吧（多个标签用逗号或空格分开）') {
                tags += val.value + ',';
            }
        });

        var forwardParam = new Object();
        forwardParam.cid = cid;
        forwardParam.cuno = cuno;
        forwardParam.content = content;
        forwardParam.tags = tags;
        forwardParam.submitCallback = forwardEditCallback;
        forwardParam.loadingCallback = function() {
            forwardSubmitCallback.forwardSubmitLoadingCallback('edit_forward', '编辑中…');
        }
        forwardParam.completeCallback = function() {
            forwardSubmitCallback.forwardSubmitCompleteCallback('edit_forward', '编 辑');
        }

        var returnError = forwardBiz.editForward(forwardParam);
        if (returnError != null && returnError.length > 0) {
            $('#editforwardmask_error').html(returnError);
            return false;
        }
        return true;
    }

    return forward;
});
