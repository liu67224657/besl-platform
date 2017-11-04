define(function(require, exprots, module) {
    var $ = require('../common/jquery-1.5.2');
    var common = require('../common/common');
    var loginBiz = require('../biz/login-biz');
    var joymealert = require('../common/joymealert');
    var ajaxverify = require('../biz/ajaxverify');
    var moodBiz = require('../biz/mood-biz');
    var pop = require('../common/jmpopup');
    var comment = require('../page/comment');
    var contentGenerator = require('./post-contentgenerator');
    var tag = require('./tag');
    var replyImage = require('./reply-image');
    var followBiz = require('../biz/follow-biz.js');
    var vote = require('./vote');
    var syncObj = require('./sync');
    var alertOption = {tipLayer: true, alertFooter: true, alertButtonText: '确 定', timeOutMills: -1,
        width: 400
    };
    require('../common/jquery.autotextarea')($);
    require('../common/atme');

    var contentoperateinit = {
        homeLiveHot:function() {
            eventLive('click', $("a[name=replyLink]"), contentShow.showReply)//评论
            eventLive('click', $("a[name=del_reply]"), function(dom) {
                var paramObj = {replyid:dom.attr('data-replyid') ,cid:dom.attr('data-cid'),cuno:dom.attr('data-cuno'),successCallback:replyObj.callback.listDelCallback};
                replyObj.biz.delReply(paramObj);
            }, false, false)
            eventLive('click', $('a[name="replypost_mask"]'), bindReplyMask, false, true);
            addDocmentListerner();

            eventLive('click', $('a[name=childrenreply_area]'), bindShowChildrenOnList, false, false);
            eventLive('click', $('a[name=togglechildrenreply_area]'), bindShowChildrenOnList, false, false);
            eventLive('click', $("a[name=favLink]"), favorite.toggleOnList)//喜欢
            eventLive('click', $('a[id^=edit_forward_]'), forward.editMask);
            eventLive('click', $('a[name=prePartVote]'), vote.prePartVote);
            eventLive('click', $('a[name=closePoll]'), vote.closePoll);
            eventLive('click', $('a[name=submit_poll]'), vote.partVote);
            replyImage.replyImageInit();
            eventLive('click', $('.install'), syncObj.showSyncArea);//喜欢
        },
        blogListHot:function() {
            eventLive('click', $("a[name=replyLink]"), contentShow.showReply)//评论
            eventLive('click', $("a[name=del_reply]"), function(dom) {
                var paramObj = {replyid:dom.attr('data-replyid') ,cid:dom.attr('data-cid'),cuno:dom.attr('data-cuno'),successCallback:replyObj.callback.listDelCallback};
                replyObj.biz.delReply(paramObj);
            }, false, false)
            eventLive('click', $('a[name="replypost_mask"]'), bindReplyMask, false, false);
            addDocmentListerner();
            eventLive('click', $('a[name=childrenreply_area]'), bindShowChildrenOnList, false, false);
            eventLive('click', $('a[name=togglechildrenreply_area]'), bindShowChildrenOnList, false, false);
            eventLive('click', $("a[name=favLink]"), favorite.toggleOnList);//喜欢
            eventLive('click', $("a[name=interactionTab]"), contentShow.changeTab)
            eventLive('click', $('a[id^=edit_forward_]'), forward.editMaskOnBlogList);
            replyImage.replyImageInit();
            eventLive('click', $('.install'), syncObj.showSyncArea);//喜欢
        },
        blogfavListHot:function() {
            eventLive('click', $("a[name=replyLink]"), contentShow.showReply)//评论
            eventLive('click', $("a[name=del_reply]"), function(dom) {
                var paramObj = {replyid:dom.attr('data-replyid') ,cid:dom.attr('data-cid'),cuno:dom.attr('data-cuno'),successCallback:replyObj.callback.listDelCallback};
                replyObj.biz.delReply(paramObj);
            }, false, false)
            eventLive('click', $('a[name="replypost_mask"]'), bindReplyMask, false, false);
            addDocmentListerner();
            eventLive('click', $('a[name=childrenreply_area]'), bindShowChildrenOnList, false, false);
            eventLive('click', $('a[name=togglechildrenreply_area]'), bindShowChildrenOnList, false, false);
            eventLive('click', $("a[name=favLink]"), favorite.toggleOnList);//喜欢
            eventLive('click', $("a[name=interactionTab]"), contentShow.changeTab)
            eventLive('click', $('a[id^=edit_forward_]'), forward.editMask);
            replyImage.replyImageInit();
        },
        blogLiveHot:function() {
            eventLive('click', $('a[name=favButton]'), favorite.toggleOnBlog);
            eventLive('click', $("#locationReply"), animateToReply, false, false);
            eventLive('click', $('a[id^=edit_forward_]'), forward.editMaskOnBlog);
            eventLive('click', $('#reply_submit'), function() {
                if (joyconfig.joyuserno == '') {
                    loginBiz.maskLoginByJsonObj();
                    return;
                }
                bindSubmitReplyOnBlog();
            }, false, false);
            eventLive('click', $('a[name=reply_link]'), function() {
                if (joyconfig.joyuserno == '') {
                    loginBiz.maskLoginByJsonObj();
                    return;
                }
                $('#reply_content').focus();
            }, true, false);
            addDocmentListerner();
            eventLive('click', $("a[name=del_reply]"), function(dom) {
                var paramObj = {replyid:$(dom).attr('data-replyid') ,cid:$(dom).attr('data-cid'),cuno:$(dom).attr('data-cuno'),successCallback:replyObj.callback.listDelCallback};
                replyObj.biz.delReply(paramObj);
            }, false, true);
            eventLive('click', $('a[name="replypost_mask"]'), bindReplyMask, false, false);
            eventLive('click', $("a[name=togglechildrenreply_area]"), bindToggleChildrenReplyOnWall, false, false);
            eventLive('click', $("a[name=childrenreply_area]"), bindShowChildrenReplyOnWall, false, false);
            eventLive('click', $("a[name=childreply_submit]"), bindSubmitChildrenReplyOnWall, false, false);
            eventLive('click', $('a[name=chidren_reply_page]'), bindChildrenPage, false, false);

            replylivemood($("#reply_mood"), 'reply_content', 18002);
            window.autoAt({id:'reply_content',ohterStyle:'line-height:22px;padding:15px;font-size:14px;font-family:monospace'});

            eventLive('click', $('.install'), syncObj.showSyncArea);
            eventLive('keydown keypress keyup', $('[id^=childrenreply_textarea_]'), function(dom, event) {
                common.joymeCtrlEnter(event, dom.next().find('a[name=childreply_submit]'));
            }, false, true);
            vote.voteEventLiveOnBlog();
            eventLive('click', $("a[name=favLink]"), favorite.toggleOnList);
            eventLive('keydown keypress keyup', $("#reply_content"), function(dom, event) {
                common.checkInputLength(tipsText.comment.blog_reply_length, 'reply_content', 'reply_num');
                ctrlEnterOnWall(dom, event);
            }, false, true);
            eventLive('click', $("#favDetailLink"), favorite.showFavProfileByBlog, false, true);
            replyImage.replyImageInit();
        },
        favlistLive:function() {
            eventLive('click', $("a[name=replyLink]"), contentShow.showReply)
            eventLive('click', $("a[name=del_reply]"), function(dom) {
                var paramObj = {replyid:dom.attr('data-replyid') ,cid:dom.attr('data-cid'),cuno:dom.attr('data-cuno'),successCallback:replyObj.callback.listDelCallback};
                replyObj.biz.delReply(paramObj);
            }, false, false)
            eventLive('click', $('a[name="replypost_mask"]'), bindReplyMask, false, false);
            addDocmentListerner();

            eventLive('click', $("a[name=favLink]"), favorite.toggleOnList);//喜欢
            eventLive('click', $('a[id^=edit_forward_]'), forward.editMask);
            replyImage.replyImageInit();
        },
        walllistLive:function() {
            eventLive('click', $('#reply_submit'), function() {
                if (joyconfig.joyuserno == '') {
                    var discovery = require('./discovery');
                    discovery.appendLogin(bindSubmitReplyOnWall, []);
                    return;
                }
                bindSubmitReplyOnWall();
            }, false, false);
            eventLive('click', $("a[name=del_reply]"), function(dom) {
                var paramObj = {replyid:$(dom).attr('data-replyid') ,cid:$(dom).attr('data-cid'),cuno:$(dom).attr('data-cuno'),successCallback:replyObj.callback.listDelCallback};
                replyObj.biz.delReply(paramObj);
            }, false, true);
            eventLive('click', $('a[name="replypost_mask"]'), bindReplyMask, false, false);
            addDocmentListerner();
            eventLive('click', $("a[name=togglechildrenreply_area]"), bindToggleChildrenReplyOnWall, false, false);
            eventLive('click', $("a[name=childrenreply_area]"), bindShowChildrenReplyOnWall, false, false);
            eventLive('click', $("a[name=childreply_submit]"), bindSubmitChildrenReplyOnWall, false, false);
            eventLive('click', $("#fav_wall_link"), favorite.toggleOnWall);//喜欢
            replylivemood($("#reply_mood"), 'reply_content', 18002);
            eventLive('click', $("#showReplyOnWall"), animateToReply, false, false);
            eventLive('keydown', $("#reply_content"), ctrlEnterOnWall, false, true);
            bindCheckInputOnWall('reply_content', 'reply_num', '');
            eventLive('click', $('.install'), syncObj.showSyncAreaOnWall);//喜欢
            eventLive('click', $('#maskLoginOnWall'), bindPostReplyOnWall);
            eventLive('click', $("a[name=favDetailLink]"), favorite.showFavProfileByWall, false, true);
            eventLive('click', $("a[name=follow-simple]"), followSimple);
            eventLive('click', $("a[name=unfollow]"), unFollowSimple);
            eventLive('keydown keypress keyup', $('[id^=childrenreply_textarea_]'), function(dom, event) {
                common.joymeCtrlEnter(event, dom.next().find('a[name=childreply_submit]'));
            }, false, true);
            replyImage.replyImageInit();
        },
        receiveReplyList :function() {
            eventLive('click', $("a[name=del_reply]"), bindDelReplyOnReceive);//删除评论
            eventLive('click', $("a[name=show_childrenreply]"), bindToggleChildrenOnReceive, false, false);
            eventLive('click', $("a[name=submit_reply]"), bindSubmitReplyOnReceive, false, false);
            eventLive('keydown', $("textarea[id^=reply_textarea_]"), ctrlEnterOnOther, false, true);
            replyImage.replyImageInit();
        },
        postVoteLive:function() {
            replyImage.replyImageInit();
            eventLive('click', $('.install'), syncObj.showSyncArea);//喜欢
        },
        atmeListBind:function() {
            eventLive('click', $("a[name=show_reply_onreply]"), bindToggleChildrenOnReceive, false, false);
            eventLive('click', $("a[name=submit_reply]"), bindSubmitReplyOnReceive, false, false);
        }
    }

    var eventLive = function(eventType, dom, callback, eventbind, eventFlag) {
        if (!eventbind) {
            dom.die().live(eventType, function(event) {
                if (eventFlag) {
                    callback($(this), event);
                } else {
                    callback($(this));
                }
            })
        } else {
            dom.bind(eventType, function(event) {
                if (eventFlag) {
                    callback($(this), event);
                } else {
                    callback($(this));
                }
            })
        }
    }

    var replyObj = {
        biz:{
            listBiz : function(dom, paramObj) {
                var ajaxParam = {cid:paramObj.cid,cuno:paramObj.cuno,type:paramObj.interactionType}
                $.ajax({
                            type: "POST",
                            url: "/json/interaction/list",
                            data: ajaxParam,
                            success: function(req) {
                                var resultMsg = eval('(' + req + ')');
                                var callbackObj = {cid:paramObj.cid,cuno:paramObj.cuno,type:paramObj.interactionType}
                                paramObj.successCallback(dom, resultMsg, callbackObj)
                            },
                            beforeSend:function() {
                                if (paramObj.loadingCallback != null) {
                                    paramObj.loadingCallback(dom, paramObj.cid);
                                }
                            }
                        });
            },
            delReply:function(paramObj) {
                joymealert.confirm({offset:'',tipLayer:true,text:'确定删除该评论？',submitFunction:function() {
                            var url = "/json/reply/delete";
                            var param = {replyid:paramObj.replyid ,cid:paramObj.cid,cuno:paramObj.cuno};
                            if ('1' == paramObj.commenttype) {
                                url = "/json/comment/remove";
                                param = {rid:paramObj.replyid};
                            }
                            $.post(url, param, function(req) {
                                var jsonObj = eval('(' + req + ')');
                                if ('1' == paramObj.commenttype) {
                                    jsonObj.status_code = jsonObj.rs;
                                }
                                var callbackObj = {replyid:paramObj.replyid,cid: paramObj.cid};
                                paramObj.successCallback(jsonObj, callbackObj);
                            });
                        },popzindex:18002});
            },
            postReply:function(paramObj, loadingCallback, callback, replyDom) {
                var params = parseReplyParams(paramObj);
                if (paramObj.sync != null) {
                    params.sync = paramObj.sync;
                }
                if (window.replyLock) {
                    return false;
                }
                var url = "/json/reply/publish";
                if ("1" == paramObj.commenttype) {
                    url = "/json/comment/post";
                    params = {cid:params.cid,pid:params.pid,rid:params.rid,body:params.replycontent,pic:params.pic};
                }
                $.ajax({
                            url: url,
                            type:'post',
                            data:params,
                            success:function(req) {
                                var jsonObj = eval('(' + req + ')');
                                if ("1" == paramObj.commenttype) {
                                    jsonObj.status_code = jsonObj.rs;
                                }
                                callback(jsonObj, paramObj, replyDom)
                            },
                            beforeSend:function() {
                                window.replyLock = true;
                                loadingCallback(replyDom);
                            },
                            error:function() {
                                window.replyLock = false;
                            },
                            complete:function() {
                                window.replyLock = false;
                            }
                        });
            },
            queryChildrenList:function(pageDom, params, callback) {
                $.ajax({
                            url: "/json/interaction/children",
                            type:'post',
                            data:params,
                            success:function(req) {
                                var jsonObj = eval('(' + req + ')');
                                callback(jsonObj, params, pageDom)
                            }
                        });
            }
        },

        callback:{
            listCallback:function(dom, resultMsg, callbackObj) {
                $('#reply_loading_' + callbackObj.cid).replaceWith(generatorList(resultMsg, dom));
                bindSubmitReplyOnList(callbackObj.cid, callbackObj.cuno);

                bindSubmitChildrenReplyOnList();
            },
            listLoadingCallback : function(dom, cid) {
                var loadingHtml = '<div class="disarea replytextarea" id="ia_area_' + cid + '" style="display:none">' +
                        '<div class="discussoncorner"><span class="discorner"></span></div>' +
                        '<div class="discusson discuss_border">' +
                        '<div id="reply_loading_' + cid + '" class="discuss_load">' +
                        '<p>读取中，请稍候... </p>' +
                        '</div>' +
                        '</div></div>';
                dom.parent().parent().parent().parent().after(loadingHtml);
                //显示评论层
                taggleEffective($('#ia_area_' + cid), null);
            },
            listDelCallback:function(jsonObj, callbackObj) {
                if (jsonObj.status_code == '0') {
                    alertOption.text = jsonObj.msg;
                    alertOption.title = '';
                    joymealert.alert(alertOption);
                    return;
                }
                $("div[name=cont_cmt_list_" + callbackObj.replyid + "]").slideUp(function() {
                    $(this).remove();
                });
                common.increaseCountByDom($("span[name=ia_header_num_reply_" + callbackObj.cid + "]"), -1);
                if ($("span[name=ia_header_num_reply_" + callbackObj.cid + "]").text() == "0") {
                    $("a[name=replyLink][data-cid=" + callbackObj.cid + "]").html('评论');
                }
            },

            //
            submitingReplyCallback:function(replyDom) {
                replyDom.attr('class', 'loadbtn fr').html('<span><em class="loadings"></em>发布中…</span>');
            },
            submitReplyCallbackOnList:function(jsonObj, paramObj, replyDom) {
                replyDom.attr('class', 'submitbtn fr').html('<span>评论</span>');

                if (jsonObj.status_code == '1') {
                    var reReplyAlertText = '评论成功';
                    var alertOption = {text:reReplyAlertText,tipLayer:true,callbackFunction:moodBiz.hideFace};
                    joymealert.alert(alertOption);

                    //修改计数
                    var replyLinkDom = $('a[name=replyLink][data-cid=' + jsonObj.result[0].content.contentId + ']');
                    if (replyLinkDom.html() == "评论") {
                        replyLinkDom.html('评论(<span name="ia_header_num_reply_' + jsonObj.result[0].content.contentId + '">0</span>)');
                    }
                    common.increaseCountByDom($('span[name=ia_header_num_reply_' + jsonObj.result[0].content.contentId + ']'), 1);

                    //拼装返回的html
                    var replyHtml = htmlGenerator.replyData(jsonObj.result[0], true);
                    $('#postreply_area_' + paramObj.cid).before(replyHtml);

                    var childrenTextareaDom = $('#childrenreply_textarea_' + jsonObj.result[0].interaction.interactionId);
                    bindChildrenTextArea(childrenTextareaDom);

                    //评论图片
                    var replyUploadId = "reply_image_" + paramObj.cid;
                    resetReplyUpload(replyUploadId);
                    resetAfterReplyByReplyDom(replyDom);
                }
                else if (jsonObj.status_code == '-4') {
                    var alertOption = {text:jsonObj.msg,tipLayer:true,textClass:"tipstext"};
                    joymealert.alert(alertOption);
                } else if (jsonObj.status_code == '-5') {
                    var alertOption = {text:tipsText.profile.user_ipforbidden_forbidlogin,
                        tipLayer:true,
                        textClass:"tipstext",
                        callbackFunction:function() {
                            if (jsonObj.result != null && jsonObj.result.length > 0) {
                                window.location.href = jsonObj.result[0];
                            }
                        }};
                    joymealert.alert(alertOption);
                } else if (jsonObj.status_code == '-1') {
                    loginBiz.maskLoginByJsonObj(jsonObj);
                } else {
                    var msg = jsonObj.msg;
                    if (msg == null || msg.length == 0) {
                        msg = '回复失败';
                    }
                    var alertOption = {text:msg,tipLayer:true,textClass:"tipstext"};
                    joymealert.alert(alertOption);
                }
            },
            submitChildrenReplyCallbackOnList:function(jsonObj, paramObj, replyDom) {
                replyDom.attr('class', 'submitbtn fr').html('<span>评论</span>');
                if (jsonObj.status_code == '1') {
                    var reReplyAlertText = '评论成功';
                    var alertOption = {text:reReplyAlertText,tipLayer:true,callbackFunction:moodBiz.hideFace};
                    joymealert.alert(alertOption);

                    //修改计数
                    var replyLinkDom = $('a[name=replyLink][data-cid=' + jsonObj.result[0].content.contentId + ']');
                    if (replyLinkDom.html() == "评论") {
                        replyLinkDom.html('评论(<span name="ia_header_num_reply_' + jsonObj.result[0].content.contentId + '">0</span>)');
                    }
                    common.increaseCountByDom($('span[name=ia_header_num_reply_' + jsonObj.result[0].content.contentId + ']'), 1);

                    //拼装返回的html
                    var replyHtml = htmlGenerator.childrenReplyData(jsonObj.result[0], true);
                    $('#children_reply_list_' + paramObj.rid).append(replyHtml);

                    var replyUploadId = "reply_image_" + paramObj.cid;
                    resetReplyUpload(replyUploadId);
                    resetAfterReplyByReplyDom(replyDom);
                } else if (jsonObj.status_code == '-4') {
                    var alertOption = {text:jsonObj.msg,tipLayer:true,textClass:"tipstext"};
                    joymealert.alert(alertOption);
                } else if (jsonObj.status_code == '-5') {
                    var alertOption = {text:tipsText.profile.user_ipforbidden_forbidlogin,
                        tipLayer:true,
                        textClass:"tipstext",
                        callbackFunction:function() {
                            if (jsonObj.result != null && jsonObj.result.length > 0) {
                                window.location.href = jsonObj.result[0];
                            }
                        }};
                    joymealert.alert(alertOption);
                } else if (jsonObj.status_code == '-1') {
                    loginBiz.maskLoginByJsonObj(jsonObj);
                } else {
                    var msg = jsonObj.msg;
                    if (msg == null || msg.length == 0) {
                        msg = '回复失败';
                    }
                    var alertOption = {text:msg,tipLayer:true,textClass:"tipstext"};
                    joymealert.alert(alertOption);
                }
            },
            submitReplyCallbackOnWall:function(jsonObj, paramObj, replyDom) {
                replyDom.attr('class', 'submitbtn fr').html('<span>评论</span>');

                if (jsonObj.status_code == '1') {

                    //计数ia_header_num_reply
                    if ($('#ia_header_num_reply').length == 0) {
                        $('#ia_tab_reply').html(' 评论（<span id="ia_header_num_reply">' + 1 + '</span>）');
                        $("#noComments").hide();
                    } else {
                        $('#ia_header_num_reply').text(common.increaseCountByDom($('#ia_header_num_reply'), 1));
                    }
                    $('#reply_area').append(htmlGenerator.replyDataOnBlog(jsonObj.result[0]));
                    //评论图片
                    $('#reply_content').val('');
                    $("#reply_num>b").text(tipsText.comment.blog_reply_length);
                    resetReplyUpload("reply_image_" + paramObj.cid);
                    var alertText = '评论成功';

                    var alertOption = {text:alertText,tipLayer:false,callbackFunction:moodBiz.hideFace,forclosed:false,popzindex:18002};
                    joymealert.alert(alertOption);
                    if (Sys.ie != '7.0') {
                        var styleStr = $("#joymealert_").attr('style');
                        $("#joymealert_").attr('style', styleStr.replace('absolute', 'fixed'));
                    }
                } else if (jsonObj.status_code == '-4') {
                    var alertOption = {text:jsonObj.msg,tipLayer:true,textClass:"tipstext"};
                    joymealert.alert(alertOption);
                } else if (jsonObj.status_code == '-5') {
                    var alertOption = {text:tipsText.profile.user_ipforbidden_forbidlogin,
                        tipLayer:true,
                        textClass:"tipstext",
                        callbackFunction:function() {
                            if (jsonObj.result != null && jsonObj.result.length > 0) {
                                window.location.href = jsonObj.result[0];
                            }
                        }};
                    joymealert.alert(alertOption);
                } else if (jsonObj.status_code == '-1') {
                    loginBiz.maskLoginByJsonObj(jsonObj);
                } else {
                    var msg = jsonObj.msg;
                    if (msg == null || msg.length == 0) {
                        msg = '回复失败';
                    }
                    var alertOption = {text:msg,tipLayer:true,textClass:"tipstext"};
                    joymealert.alert(alertOption);
                }
            },
            submitReplyChildrenCallbackOnWall:function(jsonObj, paramObj, replyDom) {
                replyDom.attr('class', 'submitbtn fr').html('<span>评论</span>');

                if (jsonObj.status_code == '1') {

                    //计数ia_header_num_reply
                    $('#ia_header_num_reply').text(common.increaseCountByDom($('#ia_header_num_reply'), 1));
                    $('#children_reply_list_' + paramObj.rid).append(htmlGenerator.childrenReplyDataOnBlog(jsonObj.result[0]));

                    //评论图片
                    resetAfterReplyByReplyDom(replyDom);
                    resetReplyUpload("reply_image_" + paramObj.cid);
                    var alertText = '评论成功';
                    var alertOption = {text:alertText,tipLayer:false,callbackFunction:moodBiz.hideFace,forclosed:false,popzindex:18002};
                    joymealert.alert(alertOption);

                } else if (jsonObj.status_code == '-4') {
                    var alertOption = {text:jsonObj.msg,tipLayer:true,textClass:"tipstext"};
                    joymealert.alert(alertOption);
                } else if (jsonObj.status_code == '-5') {
                    var alertOption = {text:tipsText.profile.user_ipforbidden_forbidlogin,
                        tipLayer:true,
                        textClass:"tipstext",
                        callbackFunction:function() {
                            if (jsonObj.result != null && jsonObj.result.length > 0) {
                                window.location.href = jsonObj.result[0];
                            }
                        }};
                    joymealert.alert(alertOption);
                } else if (jsonObj.status_code == '-1') {
                    loginBiz.maskLoginByJsonObj(jsonObj);
                } else {
                    var msg = jsonObj.msg;
                    if (msg == null || msg.length == 0) {
                        msg = '回复失败';
                    }
                    var alertOption = {text:msg,tipLayer:true,textClass:"tipstext"};
                    joymealert.alert(alertOption);
                }
            },

            submitReplyCallbackOnBlog:function(jsonObj, paramObj, replyDom) {
                replyDom.attr('class', 'submitbtn fr').html('<span>评论</span>');

                if (jsonObj.status_code == '1') {
                    window.location.href = joyconfig.URL_WWW + '/note/' + paramObj.cid + "?rid=" + jsonObj.result[0].interaction.interactionId + "#" + jsonObj.result[0].interaction.interactionId;
                } else if (jsonObj.status_code == '-4') {
                    var alertOption = {text:jsonObj.msg,tipLayer:true,textClass:"tipstext"};
                    joymealert.alert(alertOption);
                } else if (jsonObj.status_code == '-5') {
                    var alertOption = {text:tipsText.profile.user_ipforbidden_forbidlogin,
                        tipLayer:true,
                        textClass:"tipstext",
                        callbackFunction:function() {
                            if (jsonObj.result != null && jsonObj.result.length > 0) {
                                window.location.href = jsonObj.result[0];
                            }
                        }};
                    joymealert.alert(alertOption);
                } else if (jsonObj.status_code == '-1') {
                    loginBiz.maskLoginByJsonObj(jsonObj);
                } else {
                    var msg = jsonObj.msg;
                    if (msg == null || msg.length == 0) {
                        msg = '回复失败';
                    }
                    var alertOption = {text:msg,tipLayer:true,textClass:"tipstext"};
                    joymealert.alert(alertOption);
                }
            },

            submitChidlrenReplyOnReceive:function(jsonObj, paramObj, replyDom) {
                replyDom.attr('class', 'submitbtn').html('<span>评论</span>');
                replyDom.prev().val("@" + paramObj.screenName + "：");
                if (jsonObj == null || jsonObj.status_code == '-1') {
                    loginBiz.maskLoginByJsonObj(jsonObj);
                    return;
                }
                if (jsonObj.status_code != '1') {
                    var msg = jsonObj.msg;
                    if (msg == null || msg.length == 0) {
                        msg = '回复失败';
                    }
                    var errorDom = replyDom.parent().parent().find('.discuss_null')
                    if (errorDom.length > 0 && errorDom.hasClass('discuss_null')) {
                        errorDom.html(msg);
                    } else {
                        replyDom.parent().parent().append('<div class="discuss_null">' + msg + '</div>');
                    }
                    return;
                }else{
                    var alertOption = {text:"回复成功~",tipLayer:true,textClass:"tipstext"};
                    joymealert.alert(alertOption);
                }

                moodBiz.hideFace();
                //增加评论数
                replyDom.parent().parent().parent().slideToggle('fast');
            },

            //楼中楼翻页
            queryChildrenReplyCallback:function(jsonObj, paramObj, pageDom) {
                var page = jsonObj.result[0].page;

                var rows = jsonObj.result[0].rows;
                var rid = paramObj.rid;
                var childrenListHtml = ''
                $.each(rows, function(i, val) {
                    childrenListHtml += htmlGenerator.childrenReplyDataOnBlog(val);
                })
                $('#children_reply_list_' + rid).html(childrenListHtml);

                calPageDom(page, pageDom);
            }
        }
    }

    var calPageDom = function(page, pageDom) {
        var pno = parseInt(pageDom.attr('data-pno'));
        var cid = pageDom.attr('data-cid');
        var cuno = pageDom.attr('data-cuno');
        var rid = pageDom.attr('data-rid');

        var pageAreaDom = pageDom.parent();

        var lastPageNo = parseInt(pageAreaDom.attr('data-end'));
        var startPageNo = parseInt(pageAreaDom.attr('data-start'));

        var pageEnd = lastPageNo;
        var pageStart = startPageNo;
        if (page.curPage >= lastPageNo) {
            pageEnd = page.curPage + 1 > page.maxPage ? page.maxPage : page.curPage + 1;
            pageStart = pageEnd - 5 < 1 ? 1 : pageEnd - 5;
        } else if (page.curPage <= startPageNo) {
            pageStart = page.curPage <= 1 ? 1 : page.curPage - 1;
            pageEnd = pageStart + 5 > page.maxPage ? page.maxPage : pageStart + 5;
        }

        pageAreaDom.attr('data-end', pageEnd);
        pageAreaDom.attr('data-start', pageStart);


        var pageNoHtml = '';
        for (var i = pageStart; i <= pageEnd; i++) {
            if (i == page.curPage) {
                pageNoHtml += '<b>' + i + '</b> ';
            } else {
                pageNoHtml += '<a name="chidren_reply_page"  href="javascript:void(0)" data-pno="' + i + '" data-cid="' + cid + '" data-cuno="' + cuno + '" data-rid="' + rid + '">' + i + '</a> ';
            }
        }

        //高亮显示
        var pageHtml = '<a name="chidren_reply_page" href="javascript:void(0)" data-pno="1" data-cid="' + cid + '" data-cuno="' + cuno + '" data-rid="' + rid + '">首页</a> ' +
                '<a name="chidren_reply_page" href="javascript:void(0)" data-pno="' + (pno - 1 < 1 ? 1 : pno - 1) + '" data-cid="' + cid + '" data-cuno="' + cuno + '" data-rid="' + rid + '">上一页</a> ' +
                pageNoHtml +
                '<a name="chidren_reply_page" href="javascript:void(0)" data-pno="' + (pno + 1 > page.maxPage ? page.maxPage : pno + 1) + '" data-cid="' + cid + '" data-cuno="' + cuno + '" data-rid="' + rid + '">下一页</a> ' +
                '<a name="chidren_reply_page" href="javascript:void(0);" data-pno="' + page.maxPage + '" data-cid="' + cid + '" data-cuno="' + cuno + '" data-rid="' + rid + '">末页</a> ';

        pageAreaDom.html(pageHtml);
    }

    //绑定事件
    var bindSubmitReplyOnList = function(cid, cuno) {
        $("#subreply_" + cid).die().live('click', function() {
            var replyDom = $(this);

            $("#replyUploadImageDialog").hide();//让上传图片框消失
            if (window['imageUploadFlag']) {
                return;
            }
            var content = $(this).parent().parent().prev().val();
            var trimContent = content.replace(/\s+/g, " ");
            //同时转帖原文
            var forwardRoot = false;
            //发送原作者
            var replayRoot = false;
            var syncFoward = false;
            //判断是否为选中状态
            $(this).next().find('input').each(function(i) {
                if ($(this).attr("checked")) {
                    if ($(this).attr('name') == "forwardRoot") {
                        forwardRoot = true;
                    }
                    if ($(this).attr('name') == "replayRoot") {
                        replayRoot = true;
                    }
                    if ($(this).attr('name') == "syncContent") {
                        syncFoward = true;
                    }
                }
            });

            var paramObj = {cid:cid,cuno:cuno, pid:'', puno:'', replycontent:trimContent, forwardroot:forwardRoot,screenName:'',replyroot:replayRoot, sync:syncFoward};

            postReplyLogic(paramObj, $(this), function(msg) {
                checkReplyFailedFunction(replyDom, msg);
            }, function(paramObj, replyDom) {
                replyObj.biz.postReply(paramObj, replyObj.callback.submitingReplyCallback, replyObj.callback.submitReplyCallbackOnList, replyDom);
            });
        })
        //回复下拉弹层
        var textareaDom = $("#replyCont_" + cid);
        setTimeout(function() {
            var len = textareaDom.val().length;
            if (len != 0) {
                var te = new TextareaEditor(document.getElementById("replyCont_" + cid));
                te.setSelectionRange(len, len);
                te = null;
            } else {
                textareaDom.focus();
            }
        }, 400);
        replylivemood($("#replyContmood_" + cid), "replyCont_" + cid);
        eventLive('keydown keypress keyup', textareaDom, function(dom, event) {
            common.joymeCtrlEnter(event, $("#subreply_" + cid));
        }, false, true);
        eventLive('click', $("#forwardRoot_" + cid), syncDom, false, false);
        textareaDom.AutoHeight({maxHeight:200});
        if (!textareaDom.is(":animated")) {
            window.autoAt({id:"replyCont_" + cid,ohterStyle:'line-height:22px'});
        }

        replyImage.replyImageInit();
    }
    var bindShowChildrenOnList = function(dom) {
        var pid = dom.attr('data-pid');
        var puno = dom.attr('data-puno');
        var pname = dom.attr('data-pname');

        var rid = dom.attr('data-rid');
        var runo = dom.attr('data-runo');

        var subReplyArea = dom.parent().parent().next();
        $('#childreply_submit_' + rid).attr('data-pid', pid == null ? '' : pid);
        $('#childreply_submit_' + rid).attr('data-puno', puno == null ? '' : puno);
        if (subReplyArea.is(':hidden')) {
            subReplyArea.show();
        }
        $('#post_childreply_area_' + rid).show().prev().hide();
        var textarea = $('#childrenreply_textarea_' + rid);
        if (pname != null && pname.length > 0) {
            textarea.val('@' + pname + ' ');
        }
        common.cursorPosition.focusTextarea(textarea[0]);
    }
    var bindSubmitChildrenReplyOnList = function() {
        $('a[id^=childreply_submit_]').die().live('click', function() {
            var replyDom = $(this);
            $("#replyUploadImageDialog").hide();//让上传图片框消失
            if (window['imageUploadFlag']) {
                return;
            }
            var content = $(this).parent().parent().prev().val();
            var trimContent = content.replace(/\s+/g, " ");
            //同时转帖原文
            var forwardRoot = false;
            //发送原作者
            var replayRoot = false;
            var syncFoward = false;
            //判断是否为选中状态
            $(this).next().find('input').each(function(i) {
                if ($(this).attr("checked")) {
                    if ($(this).attr('name') == "forwardRoot") {
                        forwardRoot = true;
                    }
                    if ($(this).attr('name') == "replayRoot") {
                        replayRoot = true;
                    }
                    if ($(this).attr('name') == "syncContent") {
                        syncFoward = true;
                    }
                }
            });
            var cid = $(this).attr('data-cid');
            var cuno = $(this).attr('data-cuno');
            var pid = $(this).attr('data-pid');
            var puno = $(this).attr('data-puno');
            var rid = $(this).attr('data-rid');
            var runo = $(this).attr('data-runo');

            var paramObj = {cid:cid,cuno:cuno,rid:rid,runo:runo,pid:pid, puno:puno, replycontent:trimContent, forwardroot:forwardRoot,screenName:'',replyroot:replayRoot, sync:syncFoward};
            postReplyLogic(paramObj, $(this), function(msg) {
                checkReplyFailedFunction(replyDom, msg)
            }, function(paramObj, replyDom) {
                replyObj.biz.postReply(paramObj, replyObj.callback.submitingReplyCallback, replyObj.callback.submitChildrenReplyCallbackOnList, replyDom);
            });
        });

        $.each($('[id^=childrenreply_textarea_]'), function(i, val) {
            var id = $(this).attr('id');
            var rid = id.substr('childrenreply_textarea_'.length, id.length - 'childrenreply_textarea_'.length);
            setTimeout(function() {
                var len = $(this).val().length;
                if (len != 0) {
                    var te = new TextareaEditor(document.getElementById(id));
                    te.setSelectionRange(len, len);
                    te = null;
                } else {
                    $(this).focus();
                }
            }, 400);
            replylivemood($('#childrenreply_mood_' + rid), id, 18002);
            eventLive('keydown keypress keyup', $('#' + id), function(dom, event) {
                common.joymeCtrlEnter(event, dom.next().find('a[id^=childreply_submit_]'));
            }, false, true);
            $(this).AutoHeight({maxHeight:200});
            if (!$(this).is(":animated")) {
                window.autoAt({id:id,ohterStyle:'line-height:22px'});
            }
        })
    }
    var bindSubmitReplyOnWall = function () {
        var replyTextArea = $("#reply_content");
        if ($("#maskLoginOnWall").length > 0) {
            replyTextArea.removeAttr('disabled');
            $("#maskLoginOnWall").parent().remove();
            replyTextArea.focus();
            return;
        }

        var replyDom = $('#reply_submit');
        var trimContent = replyTextArea.val().replace(/\s+/g, " ");
        var forwardRoot = $('#check_forwardroot').attr('checked');
        var replayRoot = $('#check_replyroot').attr('checked');
        var syncFoward = $('#sync_forward').attr('checked');
        var cid = replyDom.attr('data-cid');
        var cuno = replyDom.attr('data-cuno');
        var pid = replyDom.attr('data-pid');
        var puno = replyDom.attr('data-puno');
        var rid = replyDom.attr('data-rid');
        var runo = replyDom.attr('data-runo');
        var paramObj = {cid:cid,cuno:cuno,rid:rid,runo:runo,pid:pid, puno:puno, replycontent:trimContent, forwardroot:forwardRoot,screenName:'',replyroot:replayRoot, sync:syncFoward};
        postReplyLogic(paramObj, replyDom, function(msg) {
            checkReplyFailedByBlogFunction(replyDom, msg)
        }, function(paramObj, replyDom) {
            replyObj.biz.postReply(paramObj, replyObj.callback.submitingReplyCallback, replyObj.callback.submitReplyCallbackOnWall, replyDom);
        });
    }
    var bindToggleChildrenReplyOnWall = function(dom) {
        if (dom.hasClass("putaway")) {
            dom.html("回复").removeClass("putaway");
            dom.parent().next().slideUp();
            return;
        }
        dom.html("收起回复").addClass("putaway");
        dom.parent().next().fadeIn();

        var pid = dom.attr('data-pid');
        var puno = dom.attr('data-puno');
        var pname = dom.attr('data-pname');
        var rid = dom.attr('data-rid');
        var runo = dom.attr('data-runo');

        var submitDom = $('[name=childreply_submit][data-rid=' + rid + ']')
        submitDom.attr('data-pid', pid == null ? '' : pid).attr('data-puno', puno == null ? '' : puno);
        var subReplyArea = submitDom.parent().parent().parent();
        subReplyArea.show().prev().hide();
        var textarea = $('#childrenreply_textarea_' + rid);
        if (pname != null && pname.length > 0) {
            textarea.val('@' + pname + ' ');
        }
        common.cursorPosition.focusTextarea(textarea[0]);

        bindChildrenTextArea(textarea);
    }
    var bindShowChildrenReplyOnWall = function(dom) {

        var pid = dom.attr('data-pid');
        var puno = dom.attr('data-puno');
        var pname = dom.attr('data-pname');
        var rid = dom.attr('data-rid');
        var runo = dom.attr('data-runo');

        var submitDom = $('[name=childreply_submit][data-rid=' + rid + ']')
        submitDom.attr('data-pid', pid == null ? '' : pid).attr('data-puno', puno == null ? '' : puno);
        var subReplyArea = submitDom.parent().parent().parent();
        subReplyArea.show().prev().hide();
        var textarea = $('#childrenreply_textarea_' + rid);
        if (pname != null && pname.length > 0) {
            textarea.val('@' + pname + ' ');
        }
        common.cursorPosition.focusTextarea(textarea[0]);

        bindChildrenTextArea(textarea);
    }
    var bindSubmitChildrenReplyOnWall = function(dom) {
        $("#replyUploadImageDialog").hide();//让上传图片框消失
        if (window['imageUploadFlag']) {
            return;
        }
        var content = dom.parent().parent().prev().val();
        var trimContent = content.replace(/\s+/g, " ");
        //同时转帖原文
        var forwardRoot = false;
        //发送原作者
        var replayRoot = false;
        var syncFoward = false;
        //判断是否为选中状态
        dom.next().find('input').each(function(i) {
            if ($(this).attr("checked")) {
                if ($(this).attr('name') == "forwardRoot") {
                    forwardRoot = true;
                }
                if ($(this).attr('name') == "replayRoot") {
                    replayRoot = true;
                }
                if ($(this).attr('name') == "syncContent") {
                    syncFoward = true;
                }
            }
        });
        var cid = dom.attr('data-cid');
        var cuno = dom.attr('data-cuno');
        var pid = dom.attr('data-pid');
        var puno = dom.attr('data-puno');
        var rid = dom.attr('data-rid');
        var runo = dom.attr('data-runo');

        var paramObj = {cid:cid,cuno:cuno,rid:rid,runo:runo,pid:pid, puno:puno, replycontent:trimContent, forwardroot:forwardRoot,screenName:'',replyroot:replayRoot, sync:syncFoward};
        postReplyLogic(paramObj, dom, function(msg) {
            checkReplyFailedFunction(dom, msg)
        }, function(paramObj, replyDom) {
            replyObj.biz.postReply(paramObj, replyObj.callback.submitingReplyCallback, replyObj.callback.submitReplyChildrenCallbackOnWall, replyDom);
        });
    }

    var bindChildrenTextArea = function(textAreaDom) {
        var id = textAreaDom.attr('id');
        var rid = id.substr('childrenreply_textarea_'.length, id.length - 'childrenreply_textarea_'.length);
        setTimeout(function() {
            var len = textAreaDom.val().length;
            if (len != 0) {
                var te = new TextareaEditor(textAreaDom[0]);
                te.setSelectionRange(len, len);
                te = null;
            } else {
                textAreaDom.focus();
            }
        }, 400);
        replylivemood($('#childrenreply_mood_' + rid), id, 18002);
        textAreaDom.AutoHeight({maxHeight:200}).css('font-size', '12px');
        if (!textAreaDom.is(":animated")) {
            window.autoAt({id:id,ohterStyle:'line-height:22px;'});
        }
    }

    var bindReplyMask = function(maskDom, event) {
        maskDom.hide();
        maskDom.next().show();
        bindChildrenTextArea(maskDom.next().find('textarea'));
    }

    function addDocmentListerner() {
        $(document).click(function(event) {
            var event = window.event ? window.event : event;
            var target = event.srcElement || event.target;

            if (target.tagName == null || target.name == 'replypost_mask' || target.tagName.toLowerCase() == 'a') {
                return;
            }

            if ($('[data-id^=reply_image_]').length > 0) {
                return;
            }

            var targetJq = $(target);
            if (targetJq.closest('.discuss_reply').length <= 0 && targetJq.closest('.pop').length <= 0) {
                $.each($('[id^=replyCont_],[id^=childrenreply_textarea_]'), function(i, val) {
                    bindHideReplyPost($(this));
                });
            }
        })
    }

    var bindHideReplyPost = function(textAreaDom) {
        if (textAreaDom.val() == null || textAreaDom.val().length == 0) {
            textAreaDom.parent().hide().prev().show();
        }
    }

    var bindChildrenPage = function(pageDom) {
        var pageNo = pageDom.attr("data-pno");
        var rid = pageDom.attr("data-rid");
        var cid = pageDom.attr("data-cid");
        var cuno = pageDom.attr("data-cuno");

        replyObj.biz.queryChildrenList(pageDom, {p:pageNo,rid:rid,cid:cid,cuno:cuno}, replyObj.callback.queryChildrenReplyCallback);
    }

    var bindPostReplyOnWall = function(replyDom) {
        if (joyconfig.joyuserno == '') {
            var discovery = require('./discovery');
            discovery.appendLogin(bindSubmitReplyOnWall, []);
            return;
        }
    }

    var bindToggleChildrenOnReceive = function(reReplyDom) {
        var replyPostDom = reReplyDom.parent().parent().parent().find(".mycomment_c");
        if (replyPostDom.is(':visible')) {
            var privilegeObj = ajaxverify.verifyPrivilege(true);
            if (privilegeObj.status_code == '-1') {
                loginBiz.maskLoginByJsonObj(privilegeObj);
                return;
            } else if (privilegeObj.status_code != '1') {
                joymealert.alert({text:privilegeObj.msg,tipLayer:true,textClass:"tipstext"});
                return;
            }
        }
        $(".mycomment_c").slideUp();
        if (replyPostDom.size() == 0) {
            var cid = reReplyDom.attr("data-cid");
            var cuno = reReplyDom.attr("data-cuno");
            var rid = reReplyDom.attr("data-rid");
            var runo = reReplyDom.attr("data-runo");
            var pid = reReplyDom.attr("data-pid") == null || reReplyDom.attr("data-pid") == undefined ? '' : reReplyDom.attr("data-pid");
            var puno = reReplyDom.attr("data-puno") == null || reReplyDom.attr("data-pid") == undefined ? '' : reReplyDom.attr("data-puno");
            var pscreenName = reReplyDom.attr("data-pname");
            var commenttype = reReplyDom.attr("data-commenttype");

            var showReHtml = '<dd class="mycomment_c" style="display: none">' +
                    '<div class="discusson">' +
                    '<span class="corner"></span>' +
                    '<div class="commentbox clearfix">' +
                    '<textarea class="commenttext" style="font-family:Tahoma, \'宋体\'; heigth:24px;font-size: 12px" id="reply_textarea_' + reReplyDom.attr("data-rid") + '">@' + pscreenName + '：</textarea>' +
                    '<a class="submitbtn" name="submit_reply" data-cid="' + cid + '" data-cuno="' + cuno + '" data-rid="' + rid + '" data-runo="' + runo + '" data-pname="' + pscreenName + '" data-puno="' + puno + '" data-pid="' + pid + '"data-commenttype="' + commenttype + '"><span>回复</span></a>' +
                    '</div>' +
                    '<div class="related clearfix">' +
                    '<div id="rereply_image_' + reReplyDom.attr("data-rid") + '" class="transmit_pic clearfix">' +
                    '<a class="commenface" id="reply_mood_' + reReplyDom.attr("data-rid") + '" href="javascript:void(0)" title="表情"></a> ' +
                    '<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="tip_msg_' + rid + '"></span>' +
                    '<div>' +
                    '</div>' +
                    '</dd>';
            reReplyDom.parent().parent().append(showReHtml);

            replylivemood($("#reply_mood_" + reReplyDom.attr("data-rid")), "reply_textarea_" + reReplyDom.attr("data-rid"), 16002);

            $('#reply_textarea_' + reReplyDom.attr("data-rid")).AutoHeight({maxHeight:200});
            window.autoAt({id:"reply_textarea_" + reReplyDom.attr("data-rid"),ohterStyle:'line-height:22px'});
        }

        var replyPostDom = reReplyDom.parent().parent().parent().find(".mycomment_c");
        replySlideToggle(replyPostDom, null, function() {
            replyPostDom.children().show();

            common.cursorPosition.focusTextarea(replyPostDom.children().children().children()[0]);
        });
    }
    var bindSubmitReplyOnReceive = function(dom) {
        var content = dom.parent('div').find('textarea').val();
        var trimContent = content.replace(/\s+/g, " ");

        var cid = dom.attr("data-cid");
        var cuno = dom.attr("data-cuno");
        var rid = dom.attr("data-rid");
        var runo = dom.attr("data-runo");
        var pid = dom.attr("data-pid");
        var puno = dom.attr("data-puno");
        var pscreenName = dom.attr("data-pname");
        var commenttype = dom.attr("data-commenttype");

        trimContent = trimContent.replace('@' + pscreenName + '：', '');

        var paramObj = {commenttype:commenttype,cid:cid,cuno:cuno,rid:rid,runo:runo,pid:pid, puno:puno, replycontent:trimContent,screenName:pscreenName};
        postReplyLogic(paramObj, dom, function(msg) {
            checkReplyFailedByReceiveFunction(dom, msg)
        }, function(paramObj, replyDom) {
            replyObj.biz.postReply(paramObj, replyObj.callback.submitingReplyCallback, replyObj.callback.submitChidlrenReplyOnReceive, replyDom);
        });
    }
    var bindDelReplyOnReceive = function(dom) {
        var rootid = $(dom).attr('data-rootid');
        if (!rootid || '' == rootid) {
            rootid = '0';
        }
        var paramObj = {commenttype:$(dom).attr('data-commenttype'),rid:$(dom).attr('data-replyid'),roid:rootid,replyid:$(dom).attr('data-replyid') ,cid:$(dom).attr('data-cid'),cuno:$(dom).attr('data-cuno'),successCallback:replyObj.callback.listDelCallback};
        replyObj.biz.delReply(paramObj);
    }

    var bindSubmitReplyOnBlog = function () {
        var replyTextArea = $("#reply_content");
        if ($("#maskLoginOnWall").length > 0) {
            replyTextArea.removeAttr('disabled');
            $("#maskLoginOnWall").parent().remove();
            replyTextArea.focus();
            return;
        }

        var replyDom = $('#reply_submit');
        var trimContent = replyTextArea.val().replace(/\s+/g, " ");
        var forwardRoot = $('#check_forwardroot').attr('checked');
        var replayRoot = $('#check_replyroot').attr('checked');
        var syncFoward = $('#sync_forward').attr('checked');
        var cid = replyDom.attr('data-cid');
        var cuno = replyDom.attr('data-cuno');
        var pid = replyDom.attr('data-pid');
        var puno = replyDom.attr('data-puno');
        var rid = replyDom.attr('data-rid');
        var runo = replyDom.attr('data-runo');
        var paramObj = {cid:cid,cuno:cuno,rid:rid,runo:runo,pid:pid, puno:puno, replycontent:trimContent, forwardroot:forwardRoot,screenName:'',replyroot:replayRoot, sync:syncFoward};
        postReplyLogic(paramObj, replyDom, function(msg) {
            checkReplyFailedByBlogFunction(replyDom, msg)
        }, function(paramObj, replyDom) {
            replyObj.biz.postReply(paramObj, replyObj.callback.submitingReplyCallback, replyObj.callback.submitReplyCallbackOnBlog, replyDom);
        });
    }


    //发送逻辑
    var postReplyLogic = function(paramObj, replyDom, replyContentFaildFucntion, checkSuccessCallback) {
        if (paramObj.replycontent == null || paramObj.replycontent.length == 0) {
            if ($("#reply_image_" + paramObj.cid).find("input[type=hidden]").length > 0) {
                paramObj.replycontent = tipsText.comment.image_reply;
            }
        }

        var msg = checkreplyContent(paramObj.replycontent);
        if (msg != null && msg.length > 0) {
            if (replyContentFaildFucntion != null) {
                replyContentFaildFucntion(msg);
            }
            return;
        }

        common.checkAtSize(paramObj.replycontent, function(resultMsg) {
            common.checkAtNicks(paramObj.replycontent, function(resultMsg) {
                checkSuccessCallback(paramObj, replyDom);
            }, function(resultMsg) {
                showAtNicksTips(paramObj, replyDom, resultMsg, checkSuccessCallback)
            });
        }, function(resultMsg) {
            showAtSizeTips(paramObj, function(resultMsg) {
                checkSuccessCallback(paramObj, replyDom);
            }, replyDom, resultMsg);
        });
    }
    var parseReplyParams = function(paramObj) {
        var params = {cid:paramObj.cid,cuno:paramObj.cuno,rid:paramObj.rid,runo:paramObj.runo,pid:paramObj.pid,puno:paramObj.puno,forwardroot:paramObj.forwardroot,replyroot:paramObj.replyroot,replycontent:paramObj.replycontent};
        //reply_image_
        if ($("#reply_image_" + paramObj.cid).find("input[type=hidden]").length > 0) {
            var imageB = $("#reply_image_" + paramObj.cid).find("input[name=picurl_b]").val();
            var imageM = $("#reply_image_" + paramObj.cid).find("input[name=picurl_m]").val();
            var imageS = $("#reply_image_" + paramObj.cid).find("input[name=picurl_s]").val();
            var imageSS = $("#reply_image_" + paramObj.cid).find("input[name=picurl_ss]").val();
            var w = $("#reply_image_" + paramObj.cid).find("input[name=w]").val();
            var h = $("#reply_image_" + paramObj.cid).find("input[name=h]").val();
            params["imgs"] = imageS;
            params["imgss"] = imageSS;
            params["imgb"] = imageB;
            params["imgm"] = imageM;
            params["w"] = w;
            params["h"] = h;
        }

        return params;
    }
    var checkreplyContent = function(replycontent) {
        if (replycontent == null || $.trim(replycontent).length == 0) {
            return tipsText.comment.blog_pl_content_notnull;
        } else if (common.getInputLength(replycontent) > tipsText.comment.blog_reply_length) {
            return '评论内容已经超过了' + (common.getInputLength(replycontent) - 300) + '字';
        } else if (! ajaxverify.verifyPost(replycontent)) {
            return tipsText.comment.blog_pl_illegl;
        }
        return "";
    }
    var checkReplyFailedFunction = function(replyDom, msg) {
        var errorDom = replyDom.parent().next();
        if (errorDom.length > 0 && errorDom.hasClass('discuss_null')) {
            errorDom.html(msg);
        } else {
            replyDom.parent().after('<div class="discuss_null">' + msg + '</div>');
        }

    }

    var checkReplyFailedByBlogFunction = function(replyDom, msg) {
        $('#reply_error').html('<div class="discuss_null">' + msg + '</div>');
    }

    var checkReplyFailedByReceiveFunction = function(replyDom, msg) {
        var errorDom = replyDom.parent().next().next();
        if (errorDom.length > 0 && errorDom.hasClass('discuss_null')) {
            errorDom.html(msg);
        } else {
            replyDom.parent().next().after('<div class="discuss_null">' + msg + '</div>');
        }
    }

    //重新消失
    var resetAfterReplyByReplyDom = function(replyDom) {
        var replyAreaDom = replyDom.parent().parent().parent()
        replyAreaDom.find('input').attr('checked', false);
        replyAreaDom.find('textarea').val('');
        replyAreaDom.hide();
        replyAreaDom.prev().show();
        $('.discuss_null').remove();
    };

    var generatorList = function(resultMsg, triggerDom) {
        if (resultMsg.result[0].blogContent != null) {
            return '<div id="ia_cont_' + resultMsg.result[0].blogContent.content.contentId + '">' +
                    htmlGenerator.list(resultMsg, triggerDom) +
                    htmlGenerator.interaction(resultMsg.result[0].blogContent) + '</div>';
        } else {
            return "";
        }
    }
    var htmlGenerator = {
        interaction:function(blogContent) {
            var interaction = '';
            var replyAndForwardText = '';
            if (blogContent.content.publishType.code == 'org') {
                replyAndForwardText += '<label><input class="checktext" type="checkbox" name="forwardRoot" id="forwardRoot_' + blogContent.content.contentId + '">同时转发到我的博客</label><span class="tongbu">' +
                        '<label><input type="checkbox" class="publish_s" disabled="disabled" name="syncContent">' +
                        '<span>同步</span></label><em class="install"></em>' +
                        '</span>';
            } else if (blogContent.content.publishType.code == 'fwd' && blogContent.rootContent != undefined && blogContent.rootContent.removeStatus.code == 'n') {
                replyAndForwardText += '<label><input class="checktext" type="checkbox" name="forwardRoot" id="forwardRoot_' + blogContent.content.contentId + '">同时转发到我的博客</label><span class="tongbu">' +
                        '<label><input type="checkbox" class="publish_s" disabled="disabled" name="syncContent">' +
                        '<span>同步</span></label><em class="install"></em>' +
                        '</span><br>';
                replyAndForwardText += '<label><input class="checktext" type="checkbox" name="replayRoot">同时评论给原文作者</label> <a name="atLink" title="' + blogContent.rootProfile.blog.screenName + '" class="author" href="' + joyconfig.URL_WWW + '/people/' + blogContent.rootProfile.blog.domain + '">' + blogContent.rootProfile.blog.screenName + '</a>';
            }
            interaction = '<div class="discuss_reply" id="postreply_area_' + blogContent.content.contentId + '">' +
                    '<a name="replypost_mask" href="javascript:void(0);" class="discuss_text01">我也说一句</a>' +
                    '<div style="display:none">' +
                    '<textarea class="discuss_text focus" style="font-family:Tahoma, \'宋体\'; height: 24px;font-size:12px; " id="replyCont_' + blogContent.content.contentId + '"></textarea>' +
                    '<div class="related clearfix">' +
                    '<div class="transmit_pic clearfix" id="reply_image_' + blogContent.content.contentId + '">' +
                    '<div class="commenface"><a href="javascript:void(0)" id="replyContmood_' + blogContent.content.contentId + '" title="表情"></a></div>' +
                    '<div class="t_pic" name="reply_image_icon"><a class="t_pic1" href="javascript:void(0)">图片</a></div>' +
                    '<div style="display:none;" name="reply_image_icon_more" class="t_pic_more">' +
                    '<a title="图片" class="t_pic1" href="javascript:void(0)" name="reply_upload_img" data-cid="reply_image_' + blogContent.content.contentId + '">图片</a>' +
                    '<a title="链接" class="t_more" href="javascript:void(0)" name="reply_upload_img_link" data-cid="reply_image_' + blogContent.content.contentId + '">链接</a>' +
                    '</div>' +
                    '</div>' +
                    '<div class="transmit clearfix">' +
                    '<a class="submitbtn fr" id="subreply_' + blogContent.content.contentId + '" data-cid="' + blogContent.content.contentId + '" data-cuno="' + blogContent.content.uno + '"><span>评 论</span></a>' +
                    '<span class="y_zhuanfa">' + replyAndForwardText + '</span>' +
                    '</div>' +
                    '</div>' +
                    '</div>' +
                    '</div>';
            return interaction;
        },
        list:function(resultMsg, triggerDom) {
            var dataHtml = '';
            var result = resultMsg.result[0];
            if (result.interactionInfoList.length > 0) {
                if (result.blogContent.content.floorTimes > 5) {
                    dataHtml += '剩余' + (result.blogContent.content.floorTimes - 5) + '条评论，<a href="' + joyconfig.URL_WWW + '/note/' + result.blogContent.content.contentId + '">点击查看&gt;</a>';
                }
                $.each(result.interactionInfoList, function(i, val) {
                    if (val.interaction.interactionType.code == 'reply') {
                        dataHtml += htmlGenerator.replyData(val, true, triggerDom);
                    }
                });
            }
            return dataHtml;
        },
        replyData:function(interactionInfo, isDisplay) {
            var displyStyle = isDisplay ? '' : 'display:none';
            var headimg = "";//左侧头像内容
            if (interactionInfo.interactionProfile.blog.headIconSet == null || interactionInfo.interactionProfile.blog.headIconSet.iconSet == null
                    || interactionInfo.interactionProfile.blog.headIconSet.iconSet.length <= 0) {
                headimg = joyconfig.URL_LIB + '/static/theme/default/img/head_is_s.jpg';
                if (interactionInfo.interactionProfile.detail.sex == '1') {
                    headimg = joyconfig.URL_LIB + '/static/theme/default/img/head_boy_s.jpg';
                } else if (interactionInfo.interactionProfile.detail.sex == '0') {
                    headimg = joyconfig.URL_LIB + '/static/theme/default/img/head_girl_s.jpg';
                }

            } else {
                for (var i = 0; i < interactionInfo.interactionProfile.blog.headIconSet.iconSet.length; i++) {
                    if (interactionInfo.interactionProfile.blog.headIconSet.iconSet[i].validStatus) {
                        headimg = common.parseSimg(interactionInfo.interactionProfile.blog.headIconSet.iconSet[i].headIcon, joyconfig.DOMAIN);
                        break;
                    }
                }
            }

            var vipStr = '';
            var verifyType = interactionInfo.interactionProfile.detail.verifyType;
            if (verifyType != null) {
                vipStr = '<a href="' + joyconfig.URL_WWW + '/people/' + interactionInfo.interactionProfile.blog.domain + '" class="' + verifyType.code + 'vip" title="' + joyconfig.viptitle[verifyType.code] + '"></a>';
            }

            var dataHtml = '';
            var rightStr = '';//右侧文本内容
            var delStr = '';
            if (interactionInfo.interaction.contentUno == joyconfig.joyuserno || interactionInfo.interaction.interactionUno == joyconfig.joyuserno) {
                delStr = '<a href="javascript:void(0);" name="del_reply" data-cuno="' + interactionInfo.interaction.contentUno + '" data-replyid="' + interactionInfo.interaction.interactionId + '" data-cid="' + interactionInfo.interaction.contentId + '">删除</a> |  ';
                //为该段代码注册live事件
            }

            rightStr = '<span class="delete" >' + delStr +
                    ' <a href="javascript:void(0);"  name="childrenreply_area" data-rid="' + interactionInfo.interaction.interactionId + '"  data-runo="' + interactionInfo.interaction.interactionUno + '" >回复</a>' +
                    '</span>';

            var subReplyPostHtml = '<a href="javascript:void(0);" name="replypost_mask" class="discuss_text01">我也说一句</a>' +
                    '<div style="display:none" id="post_childreply_area_' + interactionInfo.interaction.interactionId + '">' +
                    '<textarea name="content" id="childrenreply_textarea_' + interactionInfo.interaction.interactionId + '" cols="" rows="" class="discuss_text focus" style="font-family:Tahoma,\'宋体\'; height: 24px; " warp="off"></textarea>' +
                    '<div class="related clearfix">' +
                    '<a class="commenface" id="childrenreply_mood_' + interactionInfo.interaction.interactionId + '" title="表情" href="javascript:void(0)"></a>' +
                    '<div class="transmit clearfix">' +
                    '<a id="childreply_submit_' + interactionInfo.interaction.interactionId + '" data-cid="' + interactionInfo.interaction.contentId + '" data-cuno="' + interactionInfo.interaction.contentUno + '" data-rid="' + interactionInfo.interaction.interactionId + '" data-runo="' + interactionInfo.interaction.interactionUno + '" data-pid="" data-puno="" class="submitbtn fr"><span>评 论</span></a>' +
                    '<span class="y_zhuanfa">' +
                    '<label>' +
                    '<input type="checkbox" class="checktext" name="forwardRoot">同时转发到我的博客</label>' +
                    '</span>' +
                    '</div>' +
                    '</div>' +
                    '</div>';

            var childrenReplyListHtml = '';
            if (interactionInfo.childrenRows != null && interactionInfo.childrenRows.rows.length > 0) {
                if (interactionInfo.interaction.replyTimes > 5) {
                    childrenReplyListHtml += '剩余' + (interactionInfo.interaction.replyTimes - 5) + '条评论，<a href="' + joyconfig.URL_WWW + '/note/' + interactionInfo.content.contentId + '" target="_blank">点击查看</a>';
                }

                $.each(interactionInfo.childrenRows.rows, function(i, val) {
                    childrenReplyListHtml += htmlGenerator.childrenReplyData(val);
                });
            }

            var replyImageStr = replyImageHtml(interactionInfo);
            dataHtml += '<div class="conmenttx clearfix" name="cont_cmt_list_' + interactionInfo.interaction.interactionId + '" style="' + displyStyle + '">' +
                    '<div class="conmentface">' +
                    '<div class="commenfacecon">' +
                    '<a class="cont_cl_left" name="atLink" title="' + interactionInfo.interactionProfile.blog.screenName + '" href="' + joyconfig.URL_WWW + '/people/' + interactionInfo.interactionProfile.blog.domain + '">' +
                    '<img src="' + headimg + '" width="33px" height="33px"/>' +
                    '</a>' +
                    '</div>' +
                    '</div>' +
                    '<div class="conmentcon">' +
                    '<a href="' + joyconfig.URL_WWW + '/people/' + interactionInfo.interactionProfile.blog.domain + '" name="atLink" title="' + interactionInfo.interactionProfile.blog.screenName + '">' + interactionInfo.interactionProfile.blog.screenName + '</a>' + vipStr + '：' + interactionInfo.interaction.interactionContent +
                    replyImageStr +
                    '<div class="commontft clearfix"><span class="reply_time">' + interactionInfo.interaction.createDateStr + '</span>' + rightStr + '</div>' +
                    '<div style="' + ((interactionInfo.childrenRows == null || interactionInfo.childrenRows.page.totalRows == 0) ? 'display:none' : '') + '">' +
                    '<div class="discuss_list"><div id="children_reply_list_' + interactionInfo.interaction.interactionId + '">' + childrenReplyListHtml + '</div>' +
                    '<div class="discuss_reply" >' + subReplyPostHtml + '</div>' +
                    '</div>' +
                    '</div>' +
                    '</div>' +
                    '</div>';
            return dataHtml;
        },
        childrenReplyData:function(interactionInfo) {
            var headimg = "";//左侧头像内容
            if (interactionInfo.interactionProfile.blog.headIconSet == null || interactionInfo.interactionProfile.blog.headIconSet.iconSet == null
                    || interactionInfo.interactionProfile.blog.headIconSet.iconSet.length <= 0) {
                headimg = joyconfig.URL_LIB + '/static/theme/default/img/head_is_s.jpg';
                if (interactionInfo.interactionProfile.detail.sex == '1') {
                    headimg = joyconfig.URL_LIB + '/static/theme/default/img/head_boy_s.jpg';
                } else if (interactionInfo.interactionProfile.detail.sex == '0') {
                    headimg = joyconfig.URL_LIB + '/static/theme/default/img/head_girl_s.jpg';
                }

            } else {
                for (var i = 0; i < interactionInfo.interactionProfile.blog.headIconSet.iconSet.length; i++) {
                    if (interactionInfo.interactionProfile.blog.headIconSet.iconSet[i].validStatus) {
                        headimg = common.parseSimg(interactionInfo.interactionProfile.blog.headIconSet.iconSet[i].headIcon, joyconfig.DOMAIN);
                        break;
                    }
                }
            }

            var vipStr = '';
            var verifyType = interactionInfo.interactionProfile.detail.verifyType;
            if (verifyType != null) {
                vipStr = '<a href="' + joyconfig.URL_WWW + '/people/' + interactionInfo.interactionProfile.blog.domain + '" class="' + verifyType.code + 'vip" title="' + joyconfig.viptitle[verifyType.code] + '"></a>';
            }

            var rightStr = '<span class="delete" >';//右侧文本内容
            if (interactionInfo.interaction.contentUno == joyconfig.joyuserno || interactionInfo.interaction.interactionUno == joyconfig.joyuserno) {
                rightStr += '<a href="javascript:void(0);" name="del_reply" data-cuno="' + interactionInfo.interaction.contentUno + '" data-replyid="' + interactionInfo.interaction.interactionId + '" data-cid="' + interactionInfo.interaction.contentId + '">删除</a> |  ';
                //为该段代码注册live事件
            }
            rightStr += '<a href="javascript:void(0);"  name="togglechildrenreply_area" data-pname="' + interactionInfo.interactionProfile.blog.screenName + '" data-pid="' + interactionInfo.interaction.interactionId + '"  data-puno="' + interactionInfo.interaction.interactionUno + '" data-rid="' + interactionInfo.interaction.rootId + '"  data-runo="' + interactionInfo.interaction.rootUno + '" >回复</a>' +
                    '</span>';

            var childHtml = '<div class="conmenttx clearfix" name="cont_cmt_list_' + interactionInfo.interaction.interactionId + '">' +
                    '<div class="conmentface">' +
                    '<div class="commenfacecon">' +
                    '<a class="cont_cl_left" name="atLink" title="' + interactionInfo.interactionProfile.blog.screenName + '" href="' + joyconfig.URL_WWW + '/people/' + interactionInfo.interactionProfile.blog.domain + '">' +
                    '<img src="' + headimg + '" width="33px" height="33px">' +
                    '</a>' +
                    '</div></div>' +
                    '<div class="conmentcon">' +
                    '<a name="atLink" title="' + interactionInfo.interactionProfile.blog.screenName + '" href="' + joyconfig.URL_WWW + '/people/' + interactionInfo.interactionProfile.blog.domain + '">' + interactionInfo.interactionProfile.blog.screenName + '</a>' + vipStr + '：' + interactionInfo.interaction.interactionContent +
                    '<div class="commontft clearfix">' +
                    '<span class="reply_time">' + generateDate(interactionInfo.interaction.createDate) + '</span><span class="delete">' + rightStr + '</span></div></div></div>';

            return childHtml;
        },
        replyDataOnBlog:function(interactionInfo) {
            var headimg = "";//左侧头像内容
            if (interactionInfo.interactionProfile.blog.headIconSet == null || interactionInfo.interactionProfile.blog.headIconSet.iconSet == null
                    || interactionInfo.interactionProfile.blog.headIconSet.iconSet.length <= 0) {
                headimg = joyconfig.URL_LIB + '/static/theme/default/img/head_is_s.jpg';
                if (interactionInfo.interactionProfile.detail.sex == '1') {
                    headimg = joyconfig.URL_LIB + '/static/theme/default/img/head_boy_s.jpg';
                } else if (interactionInfo.interactionProfile.detail.sex == '0') {
                    headimg = joyconfig.URL_LIB + '/static/theme/default/img/head_girl_s.jpg';
                }

            } else {
                for (var i = 0; i < interactionInfo.interactionProfile.blog.headIconSet.iconSet.length; i++) {
                    if (interactionInfo.interactionProfile.blog.headIconSet.iconSet[i].validStatus) {
                        headimg = common.parseSimg(interactionInfo.interactionProfile.blog.headIconSet.iconSet[i].headIcon, joyconfig.DOMAIN);
                        break;
                    }
                }
            }

            var vipStr = '';
            var verifyType = interactionInfo.interactionProfile.detail.verifyType;
            if (verifyType != null) {
                vipStr = '<a href="' + joyconfig.URL_WWW + '/people/' + interactionInfo.interactionProfile.blog.domain + '" class="' + verifyType.code + 'vip" title="' + joyconfig.viptitle[verifyType.code] + '"></a>';
            }

            var rightHtml = '';
            if (interactionInfo.interaction.contentUno == joyconfig.joyuserno || interactionInfo.interaction.interactionUno == joyconfig.joyuserno) { //判断是否有删除按钮
                rightHtml += '<a href="javascript:void(0);" name="del_reply" data-replyid="' + interactionInfo.interaction.interactionId + '" data-cid="' + interactionInfo.interaction.contentId + '" data-cuno="' + interactionInfo.interaction.contentUno + '">删除</a> | ';
            }
            rightHtml += '<a href="javascript:void(0);"  name="togglechildrenreply_area" data-rid="' + interactionInfo.interaction.interactionId + '"  data-runo="' + interactionInfo.interaction.interactionUno + '" >回复</a>';
            var replyImageStr = replyImageHtmlOnBlog(interactionInfo);

            var childrenReplyHtml = '<div style="display:none" class="discuss_bd_list discuss_border">' +
                    '<div id="children_reply_list_' + interactionInfo.interaction.interactionId + '"></div>' +
                    '<div class="discuss_reply" id="post_childreply_area_' + interactionInfo.interaction.interactionId + '">' +
                    '<a class="discuss_text01" href="javascript:void(0);" name="replypost_mask">我也说一句</a>' +
//                    '<div style="display:none">' +
                    '<div class="discuss_reply reply_box01">' +
                    '<textarea warp="off" style="font-family:Tahoma, \'宋体\';" class="discuss_text focus" rows="" cols="" id="childrenreply_textarea_' + interactionInfo.interaction.interactionId + '" data-rid="' + interactionInfo.interaction.interactionId + '" name="content"></textarea>' +
                    '<div class="related clearfix">' +
                    '<a href="javascript:void(0)" title="表情" id="childrenreply_mood_' + interactionInfo.interaction.interactionId + '" class="commenface"></a>' +
                    '<div class="transmit clearfix">' +
                    '<a class="submitbtn fr" name="childreply_submit" data-rid="' + interactionInfo.interaction.interactionId + '" data-runo="' + interactionInfo.interaction.interactionUno + '" data-pid="" data-puno="" data-cid="' + interactionInfo.interaction.contentId + '" data-cuno="' + interactionInfo.interaction.contentUno + '"><span>评 论</span></a>' +
                    '<span class="y_zhuanfa">' +
                    '<label><input type="checkbox" name="forwardRoot" class="checktext">同时转发到我的博客</label>' +
                    '</span>' +
                    '</div>' +
                    '</div>' +
                    '</div>' +
//                    '</div>' +
                    '</div>' +
                    '</div>';

            var html = '<div name="cont_cmt_list_' + interactionInfo.interaction.interactionId + '" style="" class="area blogopinion clearfix">' +
                    '<dl>' +
                    '<dt class="personface">' +
                    '<a title="' + interactionInfo.interactionProfile.blog.screenName + '" name="atLink" href="' + joyconfig.URL_WWW + '/people/' + interactionInfo.interactionProfile.blog.domain + '">' +
                    '<img width="58" height="58" class="user" src="' + common.parseSimg(headimg, joyconfig.DOMAIN) + '">' +
                    '</a>' +
                    '</dt>' +
                    '<dd class="textcon discuss_building">' +
                    '<em>#' + interactionInfo.interaction.floorNo + '</em>' +
                    '<a title="' + interactionInfo.interactionProfile.blog.screenName + '" class="author" name="atLink" href="' + joyconfig.URL_WWW + '/people/' + interactionInfo.interactionProfile.blog.domain + '">' + interactionInfo.interactionProfile.blog.screenName + '</a>' +
                    vipStr +
                    '<p> ' + interactionInfo.interaction.interactionContent + ' </p>' + replyImageStr +
                    '<div class="discuss_bdfoot">' + generateDate(interactionInfo.interaction.createDate) + rightHtml +
                    '</div>' +
                    childrenReplyHtml +
                    '</dd>' +
                    '</dl>' +
                    '</div>';
            return html;
        },
        childrenReplyDataOnBlog:function(interactionInfo) {
            var headimg = "";//左侧头像内容
            if (interactionInfo.interactionProfile.blog.headIconSet == null || interactionInfo.interactionProfile.blog.headIconSet.iconSet == null
                    || interactionInfo.interactionProfile.blog.headIconSet.iconSet.length <= 0) {
                headimg = joyconfig.URL_LIB + '/static/theme/default/img/head_is_s.jpg';
                if (interactionInfo.interactionProfile.detail.sex == '1') {
                    headimg = joyconfig.URL_LIB + '/static/theme/default/img/head_boy_s.jpg';
                } else if (interactionInfo.interactionProfile.detail.sex == '0') {
                    headimg = joyconfig.URL_LIB + '/static/theme/default/img/head_girl_s.jpg';
                }

            } else {
                for (var i = 0; i < interactionInfo.interactionProfile.blog.headIconSet.iconSet.length; i++) {
                    if (interactionInfo.interactionProfile.blog.headIconSet.iconSet[i].validStatus) {
                        headimg = common.parseSimg(interactionInfo.interactionProfile.blog.headIconSet.iconSet[i].headIcon, joyconfig.DOMAIN);
                        break;
                    }
                }
            }

            var vipStr = '';
            var verifyType = interactionInfo.interactionProfile.detail.verifyType;
            if (verifyType != null) {
                vipStr = '<a href="' + joyconfig.URL_WWW + '/people/' + interactionInfo.interactionProfile.blog.domain + '" class="' + verifyType.code + 'vip" title="' + joyconfig.viptitle[verifyType.code] + '"></a>';
            }

            var rightStr = '<span class="delete" >';//右侧文本内容
            if (interactionInfo.interaction.contentUno == joyconfig.joyuserno || interactionInfo.interaction.interactionUno == joyconfig.joyuserno) {
                rightStr += '<a href="javascript:void(0);" name="del_reply" data-cuno="' + interactionInfo.interaction.contentUno + '" data-replyid="' + interactionInfo.interaction.interactionId + '" data-cid="' + interactionInfo.interaction.contentId + '">删除</a> |  ';
                //为该段代码注册live事件
            }
            rightStr += '<a href="javascript:void(0);"  name="childrenreply_area" data-pname="' + interactionInfo.interactionProfile.blog.screenName + '" data-pid="' + interactionInfo.interaction.interactionId + '"  data-puno="' + interactionInfo.interaction.interactionUno + '" data-rid="' + interactionInfo.interaction.rootId + '"  data-runo="' + interactionInfo.interaction.rootUno + '" >回复</a>' +
                    '</span>';

            var html = '<div style="" name="cont_cmt_list_' + interactionInfo.interaction.interactionId + '" class="conmenttx clearfix">' +
                    '<div class="conmentface">' +
                    '<div class="commenfacecon">' +
                    '<a href="' + joyconfig.URL_WWW + '/people/' + interactionInfo.interactionProfile.blog.domain + '" title="' + interactionInfo.interactionProfile.blog.screenName + '" name="atLink" class="cont_cl_left">' +
                    '<img width="33px" height="33px" src="' + headimg + '">' +
                    '</a>' +
                    '</div>' +
                    '</div>' +
                    '<div class="conmentcon">' +
                    '<a title="' + interactionInfo.interactionProfile.blog.screenName + '" name="atLink" href="' + joyconfig.URL_WWW + '/people/' + interactionInfo.interactionProfile.blog.domain + '">' + interactionInfo.interactionProfile.blog.screenName + '</a>' + vipStr + '：' +
                    interactionInfo.interaction.interactionContent +
                    '<div class="commontft clearfix">' +
                    '<span class="reply_time">' + generateDate(interactionInfo.interaction.createDate) + '</span>' +
                    rightStr +
                    '</div>' +
                    '</div>' +
                    '</div>';
            return html;
        }
    }

    var contentShow = {
        showReply:function(dom) {
            var cid = dom.attr('data-cid');
            var cuno = dom.attr('data-cuno');
            var type = dom.attr('data-itype');
            toggleReplyList(dom, cid, cuno, type);
        }
    }

    var toggleReplyList = function(dom, cid, cuno, type) {
        //喜欢框是否存在,不存在就创建并显示
        if ($('#ia_area_' + cid).length == 0) {
            var paramObj = {cid:cid,
                cuno:cuno,
                interactionType:type,
                loadingCallback:replyObj.callback.listLoadingCallback,
                successCallback: replyObj.callback.listCallback}
            replyObj.biz.listBiz(dom, paramObj);//加载喜欢框
        } else {
            //显示时操作
            taggleEffective($('#ia_area_' + cid), null)
        }
        common.cursorPosition.focusTextarea(document.getElementById('replyCont_' + cid));
    }
    var taggleEffective = function(selctor, showFunction) {
        if (Sys.ie == '7.0') {
            selctor.fadeToggle(function() {
                if (showFunction != null) {
                    showFunction()
                }
            });
        } else {
            selctor.slideToggle(function() {
                if (showFunction != null) {
                    showFunction()
                }
            });
        }
    }
    var replySlideToggle = function(jqdom, hideFunction, showFunction) {
        if (jqdom.is(':visible')) {
            jqdom.slideUp(function() {
                if (hideFunction != null) {
                    hideFunction();
                }
            });
        } else {
            jqdom.slideDown(function() {
                if (showFunction != null) {
                    showFunction();
                }
            });
        }
    }


//////////////////////////////////////////////////////////////////////////////////////////

//喜欢相关
    var favorite = {
        toggleOnList:function(dom) {
            if (joyconfig.joyuserno == '') {
                loginBiz.maskLogin({referer:window.location.href});
                return;
            }
            if (dom.attr("title") == "喜欢") {
                var movieStep = 3
                var domLove = dom.children('i')
                dom.movie = setInterval(function() {
                    domLove.removeAttr('class').addClass('step' + movieStep);
                    movieStep++;
                    if (movieStep == 8) {
                        clearInterval(dom.movie)
                    }
                }, 150)
                favBiz.favorite(dom, dom.attr("data-cid"), dom.attr("data-cuno"), favCallback.onlist, function() {
                    dom.children('i').removeClass().addClass('step1');
                });
            } else {
                if (dom.children('i').hasClass('step7')) {
                    var offSet = dom.offset();
                    var tips = '确定要取消喜欢吗?';
                    var confirmOption = {
                        confirmid:'remove_fav' + dom.attr("data-cid"),
                        offset:"Custom",
                        offsetlocation:[offSet.top,offSet.left],
                        text:tips,
                        width:229,
                        submitButtonText:'确 定',
                        submitFunction:function() {
                            favBiz.removeFavorite(dom, dom.attr("data-cid"), favCallback.removeOnlist);
                        },
                        cancelButtonText:'取 消',
                        cancelFunction:null};
                    joymealert.confirm(confirmOption);
                }
            }
        },
        toggleOnBlog:function(dom) {
            if (loginBiz.checkLogin(dom)) {
                if (dom.attr("class") == "favorite_btn") {
                    favBiz.favorite(dom, dom.attr("data-cid"), dom.attr("data-cuno"), favCallback.onBlog);
                } else if (dom.attr("class") == "favorite_btn_on") {
                    favBiz.removeFavorite(dom, dom.attr("data-cid"), favCallback.removeOnBlog);
//                    var offSet = dom.offset();
//                    var tips = '确定要取消喜欢吗?';
//                    var confirmOption = {
//                        confirmid:'remove_fav' + dom.attr("data-cid"),
//                        offset:"center",
////                        offsetlocation:[offSet.top,offSet.left],
//                        text:tips,
//                        width:229,
//                        submitButtonText:'确 定',
//                        submitFunction:function() {
//                            favBiz.removeFavorite(dom, dom.attr("data-cid"), favCallback.removeOnBlog);
//                        },
//                        cancelButtonText:'取 消',
//                        cancelFunction:null};
//                    joymealert.confirm(confirmOption);
                }
            }
        },
        toggleOnWall:function(dom) {
            if (joyconfig.joyuserno == '') {
                var discovery = require('./discovery');
                discovery.appendLogin(showFavOnWall, []);
                return;
            } else {
                showFavOnWall();
            }
            function showFavOnWall() {
                if ($("#maskLoginOnWall").length > 0) {
                    $("#reply_content").removeAttr('disabled');
                    $("#maskLoginOnWall").parent().remove();
                }
                if (dom.attr("class") == "pic_love") {
                    favBiz.favorite(dom, dom.attr("data-cid"), dom.attr("data-cuno"), favCallback.onWall);
                } else if (dom.attr("class") == "pic_love_on") {
                    var offSet = dom.offset();
                    var tips = '确定要取消喜欢吗?';
                    var confirmOption = {
                        confirmid:'remove_fav' + dom.attr("data-cid"),
                        offset:"Custom",
                        offsetlocation:[offSet.top,offSet.left],
                        text:tips,
                        width:229,
                        submitFunction:function() {
                            favBiz.removeFavorite(dom, dom.attr("data-cid"), favCallback.removeOnWall);
                        },
                        popzindex:18002,
                        cancelFunction:null};
                    joymealert.confirm(confirmOption);
//                    var afterBodyTop = $("#previewBox").data('bodyTop');
//                    if (Sys.ie == "7.0") {
//                        $("#joymeconfirm_remove_fav" + dom.attr("data-cid")).css('top', dom.offset().top + 'px')
//                    } else {
////                        $("#joymeconfirm_remove_fav" + dom.attr("data-cid")).css('top', dom.offset().top + afterBodyTop + 'px')
//                    }
                }
            }
        },
        showFavProfileByBlog:function(dom) {
            var cid = dom.attr('data-cid');
            var cuno = dom.attr('data-cuno');
            favBiz.favProfileListByContent(cid, cuno, null, null, favCallback.favProfileList, dom);
        } ,
        showFavProfileByWall:function(dom) {
            var cid = dom.attr('data-cid');
            var cuno = dom.attr('data-cuno');
            favBiz.favProfileListByContent(cid, cuno, null, null, favCallback.favProfileListOnWall, dom);
        }
    }
    window.favLock = false;
    window.unFavLock = false;
    window.favlist = false;
    var favBiz = {

        favorite:function(dom, cid, cuno, callback, errorcallback) {
            if (window.favLock) {
                return false;
            }
            $.ajax({
                        type: "POST",
                        url: "/json/profile/favorite",
                        data: {cid:cid,cuno:cuno},
                        success: function(req) {
                            var jsonObj = eval('(' + req + ')');
                            callback(dom, cid, jsonObj, errorcallback);
                        },
                        beforeSend:function() {
                            window.favLock = true;
                        },
                        error:function() {
                            window.favLock = false;
                        },
                        complete:function() {
                            window.favLock = false;
                        }
                    });
        },
        removeFavorite:function(dom, cid, callback) {
            if (window.unFavLock) {
                return false;
            }
            $.ajax({
                        type: "POST",
                        url: "/json/profile/favorite/remove",
                        data: {cid:cid},
                        success: function(req) {
                            var jsonObj = eval('(' + req + ')');
                            callback(dom, cid, jsonObj);
                        },
                        beforeSend:function() {
                            window.unFavLock = true;
                        },
                        error:function() {
                            window.unFavLock = false;
                        },
                        complete:function() {
                            window.unFavLock = false;
                        }
                    });
        },
        favProfileListByContent:function(cid, cuno, p, total, callback, dom) {
            if (window.favlist) {
                return false;
            }
            var param = {};
            param.cid = cid;
            param.cuno = cuno;
            if (p != null && typeof(p) == 'number') {
                param.p = p;
            }
            if (total != null && typeof(total) == 'number') {
                param.total = total;
            }

            $.ajax({
                        type: "POST",
                        url: "/json/interaction/favorcontentprofile",
                        data: param,
                        success: function(req) {
                            var jsonObj = eval('(' + req + ')');
                            callback(jsonObj, dom);
                        },
                        beforeSend:function() {
                            window.favlist = true;
                        },
                        error:function() {
                            window.favlist = false;
                        },
                        complete:function() {
                            window.favlist = false;
                        }
                    });
        }
    };
    var favCallback = {
        onlist : function(dom, contentId, resultJson, errorcallback) {
            if (resultJson.status_code == '1') {
                favEffective(contentId, "取消喜欢", "step7", 1);

            } else if (resultJson.status_code == '2') {
                favEffective(contentId, "取消喜欢", "step1", 0);

            } else if (resultJson.status_code == '-1') {
                errorcallback();
                loginBiz.maskLoginByJsonObj(resultJson);
            } else if (resultJson.status_code == '-5') {
                var alertOption = {text:tipsText.profile.user_ipforbidden_forbidlogin,
                    tipLayer:true,
                    textClass:"tipstext",
                    callbackFunction:function() {
                        if (resultJson.result != null && resultJson.result.length > 0) {
                            window.location.href = resultJson.result[0];
                        }
                        errorcallback();
                    }};
                joymealert.alert(alertOption);
            } else if (resultJson.status_code == '0') {
                var alertOption = {text:resultJson.msg,
                    tipLayer:true,
                    textClass:"tipstext",
                    callbackFunction:function() {
                        errorcallback();
                    }};
                joymealert.alert(alertOption);
            }
        },
        onBlog:function(dom, contentId, resultJson) {
            if (resultJson.status_code == '1') {
                var favObj = $('#favDetailLink');
                var num = parseInt(favObj.children('b').text()) + 1;
                favObj.children('b').text(num);
                var favDiv = $("#favorite_detail");
                if (favDiv.is(":hidden")) {
                    favDiv.show();
                }
                dom.attr('class', 'favorite_btn_on');
            } else if (resultJson.status_code == '2') {
                favEffective(contentId, "", "favorite_btn", 0);
            } else if (resultJson.status_code == '-1') {
                loginBiz.maskLoginByJsonObj(resultJson);
            } else if (resultJson.status_code == '-5') {
                var alertOption = {text:tipsText.profile.user_ipforbidden_forbidlogin,
                    tipLayer:true,
                    textClass:"tipstext",
                    callbackFunction:function() {
                        if (resultJson.result != null && resultJson.result.length > 0) {
                            window.location.href = resultJson.result[0];
                        }
                    }};
                joymealert.alert(alertOption);
            }
        },
        onWall:function(dom, contentId, resultJson) {
            if (resultJson.status_code == '1' || resultJson.status_code == '2') {
                $('#fav_wall_link').attr('class', 'pic_love_on').attr('title', '取消喜欢');
                if (resultJson.status_code == '1') {
                    var favObj = $('a[name=favDetailLink]');
                    if (favObj.length == 0) {
                        $("#wallDescription").append('<span>本文已被<a href="javascript:void(0);" name="favDetailLink" data-cid="' + dom.attr('data-cid') + '" data-cuno="' + dom.attr('data-cuno') + '"><b>1</b>人</a>喜欢</span>')
                    } else {
                        favObj.children('b').text(parseInt(favObj.children('b').text()) + 1);
                    }
                }

            } else if (resultJson.status_code == '-1') {
                loginBiz.maskLoginByJsonObj(resultJson);
            } else if (resultJson.status_code == '-5') {
                var alertOption = {text:tipsText.profile.user_ipforbidden_forbidlogin,
                    tipLayer:true,
                    textClass:"tipstext",
                    callbackFunction:function() {
                        if (resultJson.result != null && resultJson.result.length > 0) {
                            window.location.href = resultJson.result[0];
                        }
                    }};
                joymealert.alert(alertOption);
            }
        },
        removeOnlist:function(dom, cid, resultJson) {
            if (resultJson.status_code == '1') {
                favEffective(cid, "喜欢", "step1", -1);
                var likeDom = $('a[name=favLink][data-cid=' + cid + ']');
                likeDom.children('span').each(function() {
                    if ($(this).text() == "0") {
                        $(this).text('喜欢');
                    }
                });
            } else if (resultJson.status_code == '-1') {
                loginBiz.maskLoginByJsonObj(resultJson);
            } else if (resultJson.status_code == '-5') {
                var alertOption = {text:tipsText.profile.user_ipforbidden_forbidlogin,
                    tipLayer:true,
                    textClass:"tipstext",
                    callbackFunction:function() {
                        if (resultJson.result != null && resultJson.result.length > 0) {
                            window.location.href = resultJson.result[0];
                        }
                    }};
                joymealert.alert(alertOption);
            }
        },
        removeOnBlog:function(dom, cid, resultJson) {
            if (resultJson.status_code == '1') {
                var favObj = $('#favDetailLink');
                var num = parseInt(favObj.children('b').text()) - 1;
                favObj.children('b').text(num);
                if (num == 0) {
                    $("#favorite_detail").hide();
                }
                dom.removeClass().addClass('favorite_btn');
            } else if (resultJson.status_code == '-1') {
                loginBiz.maskLoginByJsonObj(resultJson);
            } else if (resultJson.status_code == '-5') {
                var alertOption = {text:tipsText.profile.user_ipforbidden_forbidlogin,
                    tipLayer:true,
                    textClass:"tipstext",
                    callbackFunction:function() {
                        if (resultJson.result != null && resultJson.result.length > 0) {
                            window.location.href = resultJson.result[0];
                        }
                    }};
                joymealert.alert(alertOption);
            }
        },
        removeOnWall:function(dom, cid, resultJson) {
            if (resultJson.status_code == '1') {
                $('#fav_wall_link').attr('class', 'pic_love').attr('title', '喜欢');
                var favObj = $('a[name=favDetailLink]');
                if ((parseInt(favObj.children('b').text()) - 1) == 0) {
                    $("#wallDescription").children('span').remove();
                } else {
                    favObj.children('b').text(parseInt(favObj.children('b').text()) - 1);
                }
            } else if (resultJson.status_code == '-1') {
                loginBiz.maskLoginByJsonObj(resultJson);
            } else if (resultJson.status_code == '-5') {
                var alertOption = {text:tipsText.profile.user_ipforbidden_forbidlogin,
                    tipLayer:true,
                    textClass:"tipstext",
                    callbackFunction:function() {
                        if (resultJson.result != null && resultJson.result.length > 0) {
                            window.location.href = resultJson.result[0];
                        }
                    }};
                joymealert.alert(alertOption);
            }
        },
        favProfileList:function(resultJson, dom) {
            if (resultJson.status_code != '1') {
                return;
            }
            var result = resultJson.result[0];
            var page = result.page;
            var socialProfileList = result.rows;
            var listHtml = favgenprofilelisthtml(socialProfileList);
            $("#loveUserBox").remove();

            var loveMoreHtml = '';
            if (!page.lastPage) {
                loveMoreHtml += '<div id="love_onload" class="love_onload"><a href="javascript:void(0)" id="loveMore">查看更多</a></div>';
            }

            $("#content_operate").append('<div class="love_tc" id="loveUserBox" style="position:absolute;display:none; z-index: 16000"><div class="love_tips"></div><a id="closePop_LoveDialog" class="close" href="javascript:void(0)"></a><div class="love_pd_att"><ul class="clearfix" id="loveListUl">' + listHtml + '</ul>' +
                    loveMoreHtml +
                    '</div></div>');

            var areaDiv = $("#content_operate");
            var posContainer = areaDiv.position();
            var listBox = $("#loveUserBox");
//            var distance = $("#content_operate").offset().top-$(document).scrollTop();
//            if (distance < listBox.height()) {
            listBox.css({'left':posContainer.left + 'px' ,'top':parseInt(posContainer.top) + 31 + 'px'})
            listBox.children('div:first').removeClass().addClass('love_tips_down').attr('style', 'left:43px');
            listBox.fadeIn();
//            } else {
//                listBox.css({'left':posContainer.left+'px','top':parseInt(posContainer.top) - listBox.height() - 30 + 'px','z-index':'16000'})
//                listBox.children('div:first').removeClass().addClass('love_tips').attr('style','left:43px');
//                listBox.fadeIn();
//            }
//
            $("#closePop_LoveDialog").die().live('click', function() {
                $("#loveUserBox").fadeOut(function() {
                    $(this).remove()
                })
            })

            $("#loveMore").live('click', function() {
                favBiz.favProfileListByContent(dom.attr('data-cid'), dom.attr('data-cuno'), page.curPage + 1, page.totalRows, function(data, dom) {
                    if (data.status_code != '1') {
                        return;
                    }
                    var result = data.result[0];
                    var page = result.page;
                    var socialProfileList = result.rows;
                    if (page.lastPage) {
                        $("#love_onload").remove();
                    }

                    $("#loveListUl").append(favgenprofilelisthtml(socialProfileList));
                }, null)
            });
        },
        favProfileListOnWall:function(resultJson, dom) {
            if (resultJson.status_code != '1') {
                return;
            }
            var result = resultJson.result[0];
            var page = result.page;
            var socialProfileList = result.rows;
            var listHtml = favgenprofilelisthtml(socialProfileList);
            $("#loveUserBox").remove();

            var loveMoreHtml = '';
            if (!page.lastPage) {
                loveMoreHtml += '<div id="love_onload" class="love_onload"><a href="javascript:void(0)" id="loveMore">查看更多</a></div>';
            }

            $("#wallDescription").append('<div class="love_tc" id="loveUserBox" style="position:absolute;display:none; z-index: 16000"><div class="love_tips"></div><a id="closePop_LoveDialog" class="close" href="javascript:void(0)"></a><div class="love_pd_att"><ul class="clearfix" id="loveListUl">' + listHtml + '</ul>' +
                    loveMoreHtml +
                    '</div></div>');

            var posContainer = dom.position();
            var listBox = $("#loveUserBox");
            var distance = posContainer.top;
            if (distance < listBox.height()) {
                listBox.css({'left': 464 + 'px','top':parseInt(posContainer.top) + $("#sea_area").scrollTop() + 24 + 'px'})
                listBox.children('div:first').removeClass().addClass('love_tips_down');
                listBox.fadeIn();
            } else {
                listBox.css({'left':464 + 'px','top':parseInt(posContainer.top) + $("#sea_area").scrollTop() - listBox.height() - 32 + 'px','z-index':'16000'})
                listBox.children('div:first').removeClass().addClass('love_tips');
                listBox.fadeIn();
            }

            $("#closePop_LoveDialog").die().live('click', function() {
                $("#loveUserBox").fadeOut(function() {
                    $(this).remove()
                })
            })

            $("#loveMore").live('click', function() {
                favBiz.favProfileListByContent(dom.attr('data-cid'), dom.attr('data-cuno'), page.curPage + 1, page.totalRows, function(data, dom) {
                    if (data.status_code != '1') {
                        return;
                    }
                    var result = data.result[0];
                    var page = result.page;
                    var socialProfileList = result.rows;
                    if (page.lastPage) {
                        $("#love_onload").remove();
                    }

                    $("#loveListUl").append(favgenprofilelisthtml(socialProfileList));
                }, null)
            });
        }
    }
    var favgenprofilelisthtml = function(socialProfileList) {
        var listHtml = "";
        for (var i = 0; i < socialProfileList.length; i++) {
            var profile = socialProfileList[i].profile;
            var socialRelation = socialProfileList[i].relation;

            var relationHtml = '';
            if (profile.blog.uno == joyconfig.joyuserno) {

            } else if (socialRelation == null || socialRelation.srcStatus.code == 'n' && socialRelation.destStatus.code == 'n') {
                //陌生人
                relationHtml = '<a name="follow-simple" data-uno="' + profile.blog.uno + '" class="add_attention" href="javascript:void(0);"></a>';
            } else if (socialRelation.srcStatus.code == 'y' && socialRelation.destStatus.code == 'n') {
                //以关注
                relationHtml = '<a class="attentioned_nocancel" href="javascript:void(0);"></a>';
            } else if (socialRelation.srcStatus.code == 'y' && socialRelation.destStatus.code == 'y') {
                //相互关注
                relationHtml = '<a class="attentioned_nocancel" href="javascript:void(0);"></a>';
            } else if (socialRelation.srcStatus.code == 'n' && socialRelation.destStatus.code == 'y') {
                //粉丝
                relationHtml = '<a class="attentioned_nocancel" href="javascript:void(0);"></a>';
            }

            var imgHtml = '';
            if (profile.blog.headIconSet != null && profile.blog.headIconSet.iconSet != null && profile.blog.headIconSet.iconSet.length > 0) {
                for (var j = 0; j < profile.blog.headIconSet.iconSet.length; j++) {
                    var icon = profile.blog.headIconSet.iconSet[j];
                    if (icon.validStatus) {
                        imgHtml = '<img height="33" width="33" src="' + common.parseSimg(icon.headIcon, joyconfig.DOMAIN) + '">';
                        break;
                    }
                }
            }
            if (imgHtml.length == 0) {
                imgHtml = '<img height="33" width="33" src="' + joyconfig.URL_LIB + '/static/theme/default/img/default.jpg">';
            }

            var sexString = '';
            if (profile.detail.sex == '1') {
                sexString += '<b class="nan"></b> ';
            } else if (profile.detail.sex == '0') {
                sexString += '<b class="nv"></b>';
            }

            listHtml += '<li>' +
                    '<div class="love_tx">' +
                    '<a target="_blank" title="' + profile.blog.screenName + '" href="' + joyconfig.URL_WWW + '/people/' + profile.blog.domain + '">' +
                    '<span class="commenfacecon">' +
                    imgHtml +
                    '</span>' +
                    '</a>' +
                    '</div>' +
                    '<div class="love_area">' +
                    '<p><a class="author" href="' + joyconfig.URL_WWW + '/people/' + profile.blog.domain + '" title="' + profile.blog.screenName + '">' + profile.blog.screenName +
                    '</a></p>' +
                    '<p>' + sexString + '</p>' +
                    '<div class="love_att">' +
                    relationHtml +
                    '</div>' +
                    '</div>' +
                    '</li>';
        }
        return listHtml;
    }
    var favEffective = function(contentId, title, className, num) {
        var favLink = $('a[name=favLink][data-cid=' + contentId + ']');
        favLink.each(function() {
            $(this).attr('title', title);
            $(this).children('i').attr('class', className);
            common.increaseCountByDom($(this).children('span'), num);
        })
    }

    //转贴相关
    var forward = {
        editMask:function(dom) {
            if (loginBiz.checkLogin(dom)) {
                var uno = dom.attr('data-cuno');
                var blogid = dom.attr('data-cid');
                window.isOut = null;
                forward.showEditMask(blogid, uno, forwardCallback.editForwardCallback);
            }
        },
        editMaskOnBlogList:function(dom) {
            if (loginBiz.checkLogin(dom)) {
                var uno = dom.attr('data-cuno');
                var blogid = dom.attr('data-cid');
                window.isOut = null;
                forward.showEditMask(blogid, uno, forwardCallback.editForwardOnBlogListCallback);
            }
        },
        editMaskOnBlog:function(dom) {
            if (loginBiz.checkLogin(dom)) {
                var uno = dom.attr('data-cuno');
                var blogid = dom.attr('data-cid');
                forward.showEditMask(blogid, uno, forwardCallback.editForwardInBlogCallback);
            }
        },
        showEditMask:function(blogid, uno, submitCallback) {
            var privilegeObj = ajaxverify.verifyPrivilege(true);
            if (privilegeObj.status_code == '-1') {
                loginBiz.maskLoginByJsonObj(privilegeObj);
                return;
            } else if (privilegeObj.status_code != '1') {
                var alertOption = {text:privilegeObj.msg,tipLayer:true,textClass:"tipstext"};
                joymealert.alert(alertOption);
                return;
            }
            forwardBiz.getForwardBlog(blogid, uno, false, function() {
                forwardCallback.loadForwardMask('editforwardmask', '编辑转贴');
            }, forwardCallback.getEditForwardMaskCallback, submitCallback);
        }

    };
    window.forwardLock = false;
    var forwardBiz = {
        getForwardBlog:function(blogid, bloguno, isForward, loadingCallback, callback, forwardSubmitCallback) {
            $.ajax({
                        url:'/json/content/forward/getblog',
                        type:'post',
                        data: {blogid:blogid,bloguno:bloguno,isforward:isForward},
                        success:function(req) {
                            var resultMsg = eval('(' + req + ')');
                            callback(resultMsg, forwardSubmitCallback);
                        },
                        beforeSend:function() {
                            if (loadingCallback != null) {
                                loadingCallback(blogid);
                            }
                        }
                    });

        },

        editForward:function(forwardParam) {
            var len = common.getInputLength(forwardParam.content)
            if (len > tipsText.comment.blog_reply_length) {
                return tipsText.turnPost.blog_zt_content_maxlength;
            }

            if (!ajaxverify.verifyPost(forwardParam.content)) {
                return tipsText.turnPost.forward_content_illegl;
            }

            if (!ajaxverify.verifyTags(forwardParam.tags)) {
                return tipsText.turnPost.forward_tag_illegl;
            }

            $.ajax({
                        url:'/json/content/forward/edit',
                        type:'post',
                        data: {cid:forwardParam.cid,
                            cuno:forwardParam.cuno,
                            content:forwardParam.content,
                            tags:forwardParam.tags,
                            imgb:forwardParam.imgb,
                            imgm:forwardParam.imgm,
                            imgs:forwardParam.imgs,
                            imgss:forwardParam.imgss,
                            w:forwardParam.w,
                            h:forwardParam.h
                        },
                        success:function(req) {
                            var resultMsg = eval('(' + req + ')');
                            forwardParam.submitCallback(forwardParam, resultMsg);
                        },
                        beforeSend:function() {
                            if (forwardParam.loadingCallback != null) {
                                forwardParam.loadingCallback();
                            }
                        },
                        complete:function() {
                            if (forwardParam.completeCallback != null) {
                                forwardParam.completeCallback();
                            }
                        }
                    });
        }
    };
    var forwardCallback = {
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
                loginBiz.maskLoginByJsonObj(resultMsg);
                return;
            } else if (resultMsg.status_code == '1') {
                forwardCallback.editForwardMaskInit(resultMsg, forwardEditCallback);
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
        },
        editForwardMaskInit:function(resultMsg, forwardEditCallback) {
            var blogContent = resultMsg.result[0];

            var content = blogContent.content.content;

            var forwardLengthText = '';
            var forwardContentLength = Math.ceil(tipsText.comment.blog_reply_length - common.getInputLength(content));
            if (forwardContentLength < 0) {
                forwardLengthText = "已超过<b style='color:red'>" + Math.abs(forwardContentLength) + "</b>字";
            } else {
                forwardLengthText = "还可以输入<b>" + forwardContentLength + "</b>字";
            }

            var forwardImageStr = '';
            if (blogContent.content.images != null && blogContent.content.images.images.length > 0) {
                var image;
                $.each(blogContent.content.images.images, function(i, val) {
                    image = val;
                });
                forwardImageStr += '<div class="preview clearfix">' +
                        '<ul id="replyUploadImagePreview" name="replyUploadImagePreview">' +
                        '<li data-id="edit_forward_mask">' + common.subfilename(image.ss, 5) + '<a name="closeReplyUploadImagePreview" class="close" title="取消" href="javascript:void(0)"></a>' +
                        '<input type="hidden" value="' + image.m + '" name="picurl_m">' +
                        '<input type="hidden" value="' + image.url + '" name="picurl_b">' +
                        '<input type="hidden" value="' + image.ss + '" name="picurl_ss">' +
                        '<input type="hidden" value="' + image.s + '" name="picurl_s">' +
                        '<input type="hidden" value="' + image.w + '" id="w" name="w">' +
                        '<input type="hidden" value="' + image.h + '" id="h" name="h">' +
                        '</li>' +
                        '</ul>' +
                        '<div class="pop previewlist" id="prev_edit_forward_mask" style="position: absolute; width: 230px; z-index: 19999; top: 26px; display: none;">' +
                        '<div class="corner"></div>' +
                        '<div class="bd">' +
                        '<div class="uploadpic">' +
                        '<div class="viewpicload">' +
                        '<img width="208" height="156" src="' + common.parseSSimg(image.ss, joyconfig.DOMAIN) + '">' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '<div class="t_pic" name="reply_image_icon" style="display:none">' +
                        '<a class="t_pic1" href="javascript:void(0)">图片</a>' +
                        '</div>' +
                        '<div style="display:none;" name="reply_image_icon_more" class="t_pic_more">' +
                        '<a title="图片" class="t_pic1" href="javascript:void(0)" name="reply_upload_img" data-cid="edit_forward_mask">图片</a>' +
                        '<a title="链接" class="t_more" href="javascript:void(0)" name="reply_upload_img_link" data-cid="edit_forward_mask">链接</a>' +
                        '</div>';

            } else {
                forwardImageStr = '<div class="t_pic" name="reply_image_icon">' +
                        '<a class="t_pic1" href="javascript:void(0)">图片</a>' +
                        '</div>' +
                        '<div style="display:none;" name="reply_image_icon_more" class="t_pic_more">' +
                        '<a title="图片" class="t_pic1" href="javascript:void(0)" name="reply_upload_img" data-cid="edit_forward_mask">图片</a>' +
                        '<a title="链接" class="t_more" href="javascript:void(0)" name="reply_upload_img_link" data-cid="edit_forward_mask">链接</a>' +
                        '</div>';
            }

            $('#loading_mask').replaceWith('<div id="editforwardform"><div class="zhuantie">' +
                    '<div class="related clearfix">' +
                    '<div class="transmit_pic clearfix" id="edit_forward_mask">' +
                    '<a class="commenface" id="edit_forwadmask_mood" title="表情" href="javascript:void(0)"></a>' +
                    forwardImageStr +
                    '<span id="edit_forward_inputnum" class="limited right">' + forwardLengthText + '</span>' +
                    '</div>' +
                    '</div>' +
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
        },

        forwardSubmitLoadingCallback:function(butonId, text) {
            $('#' + butonId).attr('class', 'loadbtn fr').html('<span><em class="loadings"></em>' + text + '</span>');
        },
        forwardSubmitCompleteCallback:function(butonId, text) {
            $('#' + butonId).attr('class', 'submitbtn mr').html('<span>' + text + '</span>');
        },
        editForwardCallback:function(forwardParam, resultMsg) {
            if (resultMsg.status_code == "1") {

                var blogContent = resultMsg.result[0];

                pop.hidepopById('editforwardmask', true, true, moodBiz.hideFace);

                var alertOption = {text:'编辑成功',tipLayer:true,callbackFunction:function() {
                    var contentObj = contentGenerator.generator(resultMsg.result[0]);
                    $('#conent_' + blogContent.content.contentId).replaceWith(contentObj.contentHtml)
                    $('#conent_' + blogContent.content.contentId).css('display', '');
                    $(".lazy").scrollLoading();
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
        editForwardOnBlogListCallback:function(forwardParam, resultMsg) {
            if (resultMsg.status_code == "1") {

                var blogContent = resultMsg.result[0];

                pop.hidepopById('editforwardmask', true, true, moodBiz.hideFace);

                var alertOption = {text:'编辑成功',tipLayer:true,callbackFunction:function() {
                    var contentObj = contentGenerator.generatorOnBlogList(resultMsg.result[0]);
                    $('#conent_' + blogContent.content.contentId).replaceWith(contentObj.contentHtml)
                    $('#conent_' + blogContent.content.contentId).css('display', '');
                    $(".lazy").scrollLoading();
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
        editForwardInBlogCallback:function(forwardParam, resultMsg) {
            loginBiz.locationLoginByJsonObj(resultMsg);
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
    };

    function bindEditForwardMask(blogContent) {
        bindCheckInput('edit_forward_content', 'edit_forward_inputnum', 'editforwardmask_error');
        bindMoody('edit_forwadmask_mood', 'edit_forward_content');
        bindTag('tags_input_editforward', 'editforward', 'editforwardform', blogContent.content.contentTag);
    }

    function bindCheckInput(textid, contAreaId, errorId) {
        $("#" + textid).keyup(
                function() {
                    common.checkInputLength(tipsText.comment.blog_reply_length, textid, contAreaId);
//                }).focusin(function() {
//                    $('#' + errorId).html('');
                });
    }

    function bindCheckInputOnWall(textid, contAreaId, errorId) {
        $("#" + textid).live('keyup',
                function() {
                    common.checkInputLength(tipsText.comment.blog_reply_length, textid, contAreaId);
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
            forwardCallback.forwardSubmitLoadingCallback('edit_forward', '编辑中…');
        }
        forwardParam.completeCallback = function() {
            forwardCallback.forwardSubmitCompleteCallback('edit_forward', '编 辑');
        }

        if ($("#edit_forward_mask").find("input[type=hidden]").length > 0) {
            forwardParam.imgb = $("#edit_forward_mask").find("input[type=hidden][name=picurl_b]").val();
            forwardParam.imgm = $("#edit_forward_mask").find("input[type=hidden][name=picurl_m]").val();
            forwardParam.imgs = $("#edit_forward_mask").find("input[type=hidden][name=picurl_s]").val();
            forwardParam.imgss = $("#edit_forward_mask").find("input[type=hidden][name=picurl_ss]").val();
            forwardParam.w = $("#edit_forward_mask").find("input[type=hidden][name=w]").val();
            forwardParam.h = $("#edit_forward_mask").find("input[type=hidden][name=h]").val();
        }

        var returnError = forwardBiz.editForward(forwardParam);
        if (returnError != null && returnError.length > 0) {
            $('#editforwardmask_error').html(returnError);
            return false;
        }
        return true;
    }

    function generateDate(date) {
        var dateArray = date.split('-');
        var year = dateArray[0]
        var month = dateArray[1]
        var dayArray = dateArray[2].split(' ');
        var day = dayArray[0];
        var hhmm = dayArray[1];
        if (common.isCurrentDay(year, month, day)) {
            return '今天 ' + hhmm;
        } else {
            return  month + "月" + day + "日";
        }
    }

    //注册表情
    var replylivemood = function(moodDom, textareaID, popzindex) {
        var moodBiz = require('../biz/mood-biz');
        moodDom.live('click', function() {
            var te = new TextareaEditor(document.getElementById(textareaID));
            var config = {
                allowmultiple:true,
                isremovepop:true,
                isfocus:true
            };
            if (popzindex != undefined || popzindex != null) {
                config.popzindex = popzindex
            }
            moodBiz.docFaceBindOnReply($(this), textareaID, config, te);
        });
    }

    var followSimple = function(dom) {
        if (joyconfig.joyuserno == '') {
            if ($("#see_login").length == 0) {
                var discovery = require('./discovery');
                discovery.appendLogin(followOnWall, [dom]);
            } else {
                $("#see_login").show();
            }
            $("#loveUserBox").hide().remove();
            return;
        } else {
            followOnWall(dom);
        }
        function followOnWall(dom) {
            if ($("#maskLoginOnWall").length > 0) {
                $("#reply_content").removeAttr('disabled');
                $("#maskLoginOnWall").parent().remove();
                return;
            }
            if (dom.attr("title") != "已关注") {
                followBiz.ajaxFocus(joyconfig.joyuserno, dom.attr('data-uno'), function() {
                    $('a[name$=follow-simple][data-uno=' + dom.attr('data-uno') + ']').each(function(i, val) {
                        val.title = "已关注";
                        val.className = "attentioned_nocancel";
                    });
                });
            }
        }
    }
    var unFollowSimple = function(dom) {
        if ($("#maskLoginOnWall").length > 0) {
            $("#reply_content").removeAttr('disabled');
            $("#maskLoginOnWall").parent().remove();
            return;
        }
        followBiz.ajaxUnFocus(dom.attr('data-uno'), callback);
    }

    //超过@次数提示层
    var showAtSizeTips = function (paramObj, callback1, replyDom, resMsg) {
        var offSet = replyDom.parent().parent().parent().offset();
        var tips = '您在评论中使用@的次数超过15次超出的部分不能传达给对方信息。'

        var submitFunction = function() {
            common.checkAtNicks(paramObj.replycontent, callback1, function(resultMsg) {
                showAtNicksTips(paramObj, replyDom, resultMsg, callback1)
            });
        }

        var confirmOption = {
            confirmid:"atSizeTips",
            offset:"Custom",
            offsetlocation:[offSet.top,offSet.left + Math.floor((replyDom.parent().width() - 229) / 2)],
            text:tips,
            width:229,

            submitButtonText:'继续发送',
            submitFunction:submitFunction,
            cancelButtonText:'返回修改',
            cancelFunction:null};
        joymealert.confirm(confirmOption);
    }
    var showAtNicksTips = function (paramObj, replyDom, resMsg, transfercallback) {
        var list = resMsg.result;
        var str = '';
        $.each(list, function(i, val) {
            str = str + '“' + val + '”、';
        });

        var offSet = replyDom.parent().parent().parent().offset();
        var tips = '您在评论@到的用户' + str.substring(0, str.length - 1) + '，不存在。';

        var confirmOption = {
            confirmid:"atNicksTips",
            offset:"Custom",
            offsetlocation:[offSet.top,offSet.left + Math.floor((replyDom.parent().width() - 229) / 2)],
            title:"确认继续发送",
            text:tips,
            width:229,
            popzindex:18002,
            submitButtonText:'继续发送',
            submitFunction:function() {
                transfercallback(paramObj, replyDom);
            },
            cancelButtonText:'返回修改',
            cancelFunction:function() {
            }};
        joymealert.confirm(confirmOption);

    }

    //动画方法
    var animateToReply = function(dom) {
        $("#reply_content").focus();
        setTimeout(function() {
            $("#sea_area").scrollTop($(".area").height() + 30);
        }, 200)
    }

    var ctrlEnterOnWall = function(dom, event) {
        common.joymeCtrlEnter(event, $("#reply_submit"));
    }
    var ctrlEnterOnOther = function(dom, event) {
        common.joymeCtrlEnter(event, dom.next());
        Sys.ie == '7.0' ? dom.parent().next().children().eq(0).hide().show() : "";
        if (!$("#replyUploadImageDialog").is(":hidden")) {
            var replyUploadDom = $("#replyUploadImageDialog").parent().children().eq(1);
            if (replyUploadDom.length != 0) {
                $("#replyUploadImageDialog").css('top', $("#replyUploadImageDialog").parent().children().eq(1).position().top + 28 + 'px');
            }
        }
    }

    var syncDom = function(dom) {
        var syncInput = dom.parent().next().children().children('input');
        if (dom.attr('checked')) {
            if (joyconfig.syncFlag) {
                syncInput.removeAttr('disabled');
                syncInput.attr('checked', true);
                syncInput.next().find('span').css('color', '#494949');
            }
        } else {
            syncInput.removeAttr('checked').attr('disabled', 'disabled');
            syncInput.next().find('span').css('color', '#a2a2a2');
        }
    }

    var resetReplyUpload = function(replyUploadId) {
        $("#" + replyUploadId).find(".preview").remove();
        $("#" + replyUploadId).find(".t_pic").show();
    }

    var replyImageHtml = function(interactionInfo) {
        var replyImageStr = '';
        if (interactionInfo.interaction.interactionImages != null && interactionInfo.interaction.interactionImages.images.length > 0) {
            var liStr = '';
            $.each(interactionInfo.interaction.interactionImages.images, function(i, val) {
                var imgStr = '';
                if (val.h > 60) {
                    imgStr = '<img src="' + common.parseSSimg(val.ss, joyconfig.DOMAIN) + '" original="' + common.parseSSimg(val.ss, joyconfig.DOMAIN) + '" data-jw="' + val.w + '" data-jh="' + val.h + '" height="60px"/>';
                } else {
                    imgStr = '<img src="' + common.parseSSimg(val.ss, joyconfig.DOMAIN) + '" original="' + common.parseSSimg(val.ss, joyconfig.DOMAIN) + '" data-jw="' + val.w + '" data-jh="' + val.h + '"/>';
                }
                liStr += '<li style="display: list-item;">' +
                        '<a name="replyimgpreview" href="javascript:void(0)">' + imgStr + '</a>' +
                        '</li>';
            });
            replyImageStr = '<ul class="search_piclist clearfix">' + liStr + "</ul>";
        }
        return replyImageStr;
    }

    var replyImageHtmlOnBlog = function(interactionInfo) {
        var replyImageStr = '';
        if (interactionInfo.interaction.interactionImages != null && interactionInfo.interaction.interactionImages.images.length > 0) {
            var liStr = '';
            $.each(interactionInfo.interaction.interactionImages.images, function(i, val) {
                var imgStr = '<img src="' + common.parseMimg(val.m, joyconfig.DOMAIN) + '" original="' + common.parseMimg(val.m, joyconfig.DOMAIN) + '" data-jw="' + val.w + '" data-jh="' + val.h + '"/>';
                liStr += '<li style="display: list-item;">' +
                        '<a href="' + common.parseBimg(val.m, joyconfig.DOMAIN) + '" target="_blank">' + imgStr + '</a>' +
                        '</li>';
            });
            replyImageStr = '<ul class="clearfix">' + liStr + "</ul>";
        }
        return replyImageStr;
    }

    return contentoperateinit;
})
