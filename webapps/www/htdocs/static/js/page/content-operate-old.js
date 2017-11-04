define(function(require, exprots, module) {
    var $ = require('../common/jquery-1.5.2');
    var common = require('../common/common');
    var loginBiz = require('../biz/login-biz');
    var joymealert = require('../common/joymealert');
    var ajaxverify = require('../biz/ajaxverify');
    var moodBiz = require('../biz/mood-biz');
    var pop = require('../common/jmpopup');
    var contentGenerator = require('./post-contentgenerator');
    var tag = require('./tag');
    var replyImage = require('./reply-image');
    var followBiz = require('../biz/follow-biz.js');
    var vote = require('./vote');
    require('../common/jquery.autotextarea')($);

    var contentoperateinit = {
        homeLiveHot:function() {
            eventLive('click', $("a[name=hotLink]"), contentShow.showHot);//热度
            eventLive('click', $("a[name=replyLink]"), contentShow.showReply)//评论
            eventLive('click', $("a[name=favLink]"), favorite.toggleOnList)//喜欢
            eventLive('click', $("a[name=interactionTab]"), contentShow.changeTab);
            eventLive('click', $('a[id^=edit_forward_]'), forward.editMask);

            vote.voteEventLive();
            replyImage.replyImageInit();
            eventLive('click', $('.install'), syncObj.showSyncArea);//喜欢
//            appendLoveTips();
        },
        blogListHot:function() {
            eventLive('click', $("a[name=hotLink]"), contentShow.showHot);//热度
            eventLive('click', $("a[name=replyLink]"), contentShow.showReply)//评论
            eventLive('click', $("a[name=favLink]"), favorite.toggleOnList);//喜欢
            eventLive('click', $("a[name=interactionTab]"), contentShow.changeTab)
            eventLive('click', $('a[id^=edit_forward_]'), forward.editMaskOnBlogList);

            vote.voteEventLive();
            replyImage.replyImageInit();
            eventLive('click', $('.install'), syncObj.showSyncArea);//喜欢
        },
        blogfavListHot:function() {
            eventLive('click', $("a[name=hotLink]"), contentShow.showHot);//热度
            eventLive('click', $("a[name=replyLink]"), contentShow.showReply)//评论
            eventLive('click', $("a[name=favLink]"), favorite.toggleOnList);//喜欢
            eventLive('click', $("a[name=interactionTab]"), contentShow.changeTab)
            eventLive('click', $('a[id^=edit_forward_]'), forward.editMask);
            replyImage.replyImageInit();
        },
        blogLiveHot:function() {
            eventLive('click', $('a[name=favButton]'), favorite.toggleOnBlog);
            eventLive('click', $('a[id^=edit_forward_]'), forward.editMaskOnBlog);
            eventLive('click', $('#reply_submit'), reply.bindPostReplyOnBlog);
            eventLive('click', $('.install'), syncObj.showSyncArea);
            eventLive('click', $('a[name=reReplyLink]'), reply.bindPostReReplyOnBlog);
            eventLive("click", $('a[name=showReplyTextBox]'), reply.showReplyTextBoxOnBlog);
            eventLive('click', $("a[name=favLink]"), favorite.toggleOnList);
            vote.voteEventLiveOnBlog();
            eventLive('click', $('a[name^=del_reply_]'), function(dom) {
                var cuno = dom.attr('data-cuno');
                var cid = dom.attr('data-cid');
                var replyid = dom.attr('data-replyid')
                reply.bindPostReplyDelOnBlog(cid, cuno, replyid);
            }, true);
            eventLive('keydown', $("#reply_content"), ctrlEnterOnWall, false, true);
            eventLive('click', $("a[name=favDetailLink]"), favorite.showFavProfileByBlog, false, true);
            replyImage.replyImageInit();
        },
        favlistLive:function() {
            eventLive('click', $("a[name=replyLink]"), contentShow.showReply)
            eventLive('click', $("a[name=favLink]"), favorite.toggleOnList);//喜欢
            eventLive('click', $('a[id^=edit_forward_]'), forward.editMask);
            replyImage.replyImageInit();
        },
        walllistLive:function() {
            eventLive('click', $("a[name=interactionTab]"), contentShow.chageTabOnWall);//喜欢
            eventLive('click', $("#reply_submit"), reply.bindPostReplyOnWall);//喜欢
            eventLive('click', $("#fav_wall_link"), favorite.toggleOnWall);//喜欢
            eventLive('click', $("a[name=reReplyLink]"), reply.bindPostReReplyOnWall);//评论回复
            eventLive('click', $('a[name^=del_reply_]'),
                    function(dom) {
                        var cuno = dom.attr('data-cuno');
                        var cid = dom.attr('data-cid');
                        var replyid = dom.attr('replyid');
                        reply.bindPostReplyDelOnWall(cid, cuno, replyid);
                    }
            );
            wallLivemood();
            eventLive('click', $("#showReplyOnWall"), animateToReply);
            eventLive('keydown', $("#reply_content"), ctrlEnterOnWall, false, true);
            bindCheckInputOnWall('reply_content', 'reply_num', '');
            eventLive('click', $('.install'), syncObj.showSyncAreaOnWall);//喜欢
            eventLive('click', $('#maskLoginOnWall'), reply.bindPostReplyOnWall);
            eventLive('click', $("a[name=favDetailLink]"), favorite.showFavProfileByWall, false, true);
            eventLive('click', $("a[name=follow-simple]"), followSimple);
            eventLive('click', $("a[name=unfollow]"), unFollowSimple);
            replyImage.replyImageInit();
        },
        receiveReplyList :function() {
            eventLive('click', $("a[name=removeReply]"), reply.bindDelReplyOnReceiveList);//删除评论
            eventLive('click', $("a[name=reReply]"), reply.togglereReplyOnReplyReceive);
            eventLive('keydown', $("textarea[id^=reply_textarea_]"), ctrlEnterOnOther, false, true);
            replyImage.replyImageInit();
        },
        postVoteLive:function(){
            replyImage.replyImageInit();
            eventLive('click', $('.install'), syncObj.showSyncArea);//喜欢
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

    var contentShow = {
        showHot:function(dom) {
            var cid = dom.attr('data-cid');
            var cuno = dom.attr('data-cuno');
            var type = dom.attr('data-itype');
            toggleInteractionList(dom, cid, cuno, type)
        },
        showReply:function(dom) {
            var cid = dom.attr('data-cid');
            var cuno = dom.attr('data-cuno');
            var type = dom.attr('data-itype');
            toggleInteractionList(dom, cid, cuno, type);
        },
        changeTab:function(dom) { //喜欢框内评论事件
            var cid = dom.attr('data-cid');
            var cuno = dom.attr('data-cuno');
            var interactionType = dom.attr('data-itype');
            if (interactionType == 'reply') {
                setTimeout(function() {
                    $("#replyCont_" + cid).focus();
                }, 500)
            }
            if (dom.attr('class') == 'weight') {
                return;
            }
            discornerFun(interactionType, cid);
            if ($('#ia_cont_' + cid + '_' + interactionType).length == 0) {
                interactionBiz.listBiz(dom, cid, cuno, interactionType, null, contentShowCallback.showInteractionTabCallback)
            }
            $('div[id^=ia_cont_' + cid + ']').hide();
            $('#ia_cont_' + cid + '_' + interactionType).show();
            dom.attr('class', 'weight').siblings().removeClass('weight');
            if (interactionType == 'reply') {
                focusreplyhtml(cid, 'replyCont_');
            }
        },
        chageTabOnWall:function(dom) {
            var cid = dom.attr('data-cid');
            var cuno = dom.attr('data-cuno');
            var interactionType = dom.attr('data-itype');
            if (dom.attr('class') == 'weight') {
                return;
            }

            if ($('#data_area_' + interactionType).length == 0) {
                interactionBiz.listBiz(dom, cid, cuno, interactionType, null, contentShowCallback.showInteractionTabOnWallCallback)
            }

            $('div[id^=data_area_]').hide();
            $('#data_area_' + interactionType).show();
            dom.attr('class', 'weight').siblings().removeClass('weight');
        }
    }


    var interactionBiz = {
        listBiz:function(dom, cid, cuno, interactionType, loadingCallback, callback) {
            $.ajax({
                        type: "POST",
                        url: "/json/interaction/list",
                        data: {cid:cid,cuno:cuno,type:interactionType},
                        success: function(req) {
                            var resultMsg = eval('(' + req + ')')
                            callback(dom, cid, cuno, interactionType, resultMsg)
                        },
                        beforeSend:function() {
                            if (loadingCallback != null) {
                                loadingCallback(dom, cid);
                            }
                        }
                    });
        }
    }

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
        toggleOnListAddFavDate:function(dom) {
            if (joyconfig.joyuserno == '') {
                loginBiz.maskLogin({referer:window.location.href});
                return;
            }

            if (dom.attr("class") == "save") {
                favBiz.favorite(dom, dom.attr("data-cid"), dom.attr("data-cuno"), favCallback.onlistAddFavDate);
            } else {
                if (dom.attr("class") == "save_on") {
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
                            favBiz.removeFavorite(dom.attr("data-cid"), favCallback.removeOnlistRemoveFavDate);
                        },
                        cancelButtonText:'取 消',
                        cancelFunction:null};
                    joymealert.confirm(confirmOption);
                }
            }
        },
        toggleOnBlog:function(dom) {
            if (loginBiz.checkLogin(dom)) {
                if (dom.attr("class") == "loveicon") {
                    favBiz.favorite(dom, dom.attr("data-cid"), dom.attr("data-cuno"), favCallback.onBlog);
                } else if (dom.attr("class") == "loveon") {
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
                            favBiz.removeFavorite(dom, dom.attr("data-cid"), favCallback.removeOnBlog);
                        },
                        cancelButtonText:'取 消',
                        cancelFunction:null};
                    joymealert.confirm(confirmOption);
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
                        popzindex:18500,
                        submitButtonText:'确 定',
                        submitFunction:function() {
                            favBiz.removeFavorite(dom, dom.attr("data-cid"), favCallback.removeOnWall);
                        },
                        cancelButtonText:'取 消',
                        cancelFunction:null};
                    joymealert.confirm(confirmOption);
                    var afterBodyTop = $("#previewBox").data('bodyTop');
                    if (Sys.ie == "7.0") {
                        $("#joymeconfirm_remove_fav" + dom.attr("data-cid")).css('top', dom.offset().top + 'px')
                    } else {
                        $("#joymeconfirm_remove_fav" + dom.attr("data-cid")).css('top', dom.offset().top + afterBodyTop + 'px')
                    }
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
//                slideCount(dom, '积分+3', contentId, resultJson.billingStatus);

                var profile = resultJson.result[0].profile;
                var contentId = resultJson.result[0].userFavorite.srcId;
                var contentUno = resultJson.result[0].userFavorite.srcUno;
                var createDate = resultJson.result[0].userFavorite.createDate;

                var hotTabObj = $('a[name=interactionTab][data-cid=' + contentId + '][data-itype=hot]');
                var hotHtml = '';


                if (hotTabObj.attr('class') == 'weight') {
                    hotHtml = dataHtmlGenerator.generatorFavData(profile, contentId, contentUno, createDate, false);
                } else {
                    hotHtml = dataHtmlGenerator.generatorFavData(profile, contentId, contentUno, createDate, true);
                }
                var favHtml = '';
                var favTabObj = $('a[name=interactionTab][data-cid=' + contentId + '][data-itype=fav]');
                if (favTabObj.attr('class') == 'weight') {
                    favHtml = dataHtmlGenerator.generatorFavData(profile, contentId, contentUno, createDate, false);
                } else {
                    favHtml = dataHtmlGenerator.generatorFavData(profile, contentId, contentUno, createDate, true);
                }

                if ($('#cont_hot_empty_' + contentId).length == 0) {
                    $('#ia_cont_' + contentId + '_hot').prepend(hotHtml);
                } else {
                    $('#cont_hot_empty_' + contentId).replaceWith(hotHtml);
                }

                if ($('#cont_fav_empty_' + contentId).length == 0) {
                    $('#ia_cont_' + contentId + '_fav').prepend(favHtml);
                } else {
                    $('#cont_fav_empty_' + contentId).replaceWith(favHtml);
                }

                var iaHtmlId = 'cont_fav_list_' + contentId + '_' + profile.blog.uno;
                if (hotTabObj.attr('class') == 'weight' || favTabObj.attr('class') == 'weight') {
                    var isIE7 = window.navigator.userAgent.indexOf('MSIE 7.0') > 0;
                    if (isIE7) {
                        $('[name=' + iaHtmlId + ']').fadeIn();
                    } else {
                        $('[name=' + iaHtmlId + ']').slideDown();
                    }
                }

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
        onlistAddFavDate : function(dom, contentId, resultJson) {
            if (resultJson.status_code == '1') {
                favEffective(contentId, "取消喜欢", "save_on", 1);
                slideCount(dom, '积分+3', contentId, resultJson.billingStatus);
                if (currentProfile != undefined && currentProfile.uno == joyconfig.joyuserno) {
                    $('.chakan[data-cid=' + contentId + ']').before('<span class="mylove" style="display:none">于' + generateDateYYYMMdd(resultJson.result[0].userFavorite.createDate) + '喜欢</span>');
                }

                var profile = resultJson.result[0].profile;
                var contentId = resultJson.result[0].userFavorite.srcId;
                var contentUno = resultJson.result[0].userFavorite.srcUno;
                var createDate = resultJson.result[0].userFavorite.createDate;

                var hotTabObj = $('a[name=interactionTab][data-cid=' + contentId + '][data-itype=hot]');
                var hotHtml = '';


                if (hotTabObj.attr('class') == 'weight') {
                    hotHtml = dataHtmlGenerator.generatorFavData(profile, contentId, contentUno, createDate, false);
                } else {
                    hotHtml = dataHtmlGenerator.generatorFavData(profile, contentId, contentUno, createDate, true);
                }
                var favHtml = '';
                var favTabObj = $('a[name=interactionTab][data-cid=' + contentId + '][data-itype=fav]');
                if (favTabObj.attr('class') == 'weight') {
                    favHtml = dataHtmlGenerator.generatorFavData(profile, contentId, contentUno, createDate, false);
                } else {
                    favHtml = dataHtmlGenerator.generatorFavData(profile, contentId, contentUno, createDate, true);
                }

                if ($('#cont_hot_empty_' + contentId).length == 0) {
                    $('#ia_cont_' + contentId + '_hot').prepend(hotHtml);
                } else {
                    $('#cont_hot_empty_' + contentId).replaceWith(hotHtml);
                }

                if ($('#cont_fav_empty_' + contentId).length == 0) {
                    $('#ia_cont_' + contentId + '_fav').prepend(favHtml);
                } else {
                    $('#cont_fav_empty_' + contentId).replaceWith(favHtml);
                }

                var iaHtmlId = 'cont_fav_list_' + contentId + '_' + profile.blog.uno;
                if (hotTabObj.attr('class') == 'weight' || favTabObj.attr('class') == 'weight') {
                    var isIE7 = window.navigator.userAgent.indexOf('MSIE 7.0') > 0;
                    if (isIE7) {
                        $('[name=' + iaHtmlId + ']').fadeIn();
                    } else {
                        $('[name=' + iaHtmlId + ']').slideDown();
                    }
                }
            } else if (resultJson.status_code == '2') {
                favEffective(contentId, "取消喜欢", "save_on", 0);
                if (currentProfile.uno == joyconfig.joyuserno) {
                    $('.chakan[data-cid=' + contentId + ']').before('<span class="mylove" style="display:none">于' + generateShortDate(resultJson.result[0].userFavorite.createDate) + '喜欢</span>');
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
        onBlog:function(dom, contentId, resultJson) {
            if (resultJson.status_code == '1') {
//                slideCount(dom, '积分+3', contentId, resultJson.billingStatus);
//                var result = resultJson.result[0];
//                favEffectiveOnBlog(contentId, "", "loveon", 1);
//                if ($('#ia_tab_hot').attr('class') == 'weight' || $('#ia_tab_fav').attr('class') == 'weight') {
//                    var favHtml = dataHtmlGenerator.generatorFavDataOnBlog(result.profile, result.userFavorite.srcId, result.userFavorite.srcUno, result.userFavorite.createDate, false);
//                    $('#div_ia_tab').after(favHtml);
//                    var iaHtmlId = 'cont_fav_' + contentId + '_' + result.profile.blog.uno;
//                    var isIE7 = window.navigator.userAgent.indexOf('MSIE 7.0') > 0;
//                    if (isIE7) {
//                        $('[name=' + iaHtmlId + ']').fadeIn();
//                    } else {
//                        $('[name=' + iaHtmlId + ']').slideDown();
//                    }
//                }
                var favObj = $('a[name=favDetailLink]');
                if (favObj.length == 0) {
                    $("#wallDescription").append('<span>本文已被<a href="javascript:void(0);" name="favDetailLink" data-cid="' + dom.attr('data-cid') + '" data-cuno="' + dom.attr('data-cuno') + '"><b>1</b>人</a>喜欢</span>')
                } else {
                    favObj.children('b').text(parseInt(favObj.children('b').text()) + 1);
                }
                dom.removeClass().addClass('loveon');
            } else if (resultJson.status_code == '2') {
                favEffective(contentId, "", "loveon", 0);
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
//                    var result = resultJson.result[0];
//                    var hotNumObj = $('#ia_header_num_hot');
//                    var addNum = parseInt(hotNumObj.text()) + 1;
//                    $('#ia_header_num_hot').text(addNum);

                    var favObj = $('a[name=favDetailLink]');
                    if (favObj.length == 0) {
                        $("#wallDescription").append('<span>本文已被<a href="javascript:void(0);" name="favDetailLink" data-cid="' + dom.attr('data-cid') + '" data-cuno="' + dom.attr('data-cuno') + '"><b>1</b>人</a>喜欢</span>')
                    } else {
                        favObj.children('b').text(parseInt(favObj.children('b').text()) + 1);
                    }
//                    if ($('#ia_tab_fav').attr('class') == 'weight') {
//                        var favHtml = dataHtmlGenerator.generatorFavDataOnWall(result.profile, result.userFavorite.srcId, result.userFavorite.srcUno, result.userFavorite.createDate, true);
//                        $('#data_area_fav').prepend(favHtml);
//                    }
//                    slideCount(dom, '积分+3', contentId, resultJson.billingStatus);
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
        removeOnlistRemoveFavDate:function(cid, resultJson) {
            if (resultJson.status_code == '1') {
                favEffective(cid, "", "save", -1);
                if (currentProfile != undefined && currentProfile.uno == joyconfig.joyuserno) {
                    if ($('.chakan[data-cid=' + cid + ']').prev().attr('class') == 'mylove') {
                        $('.chakan[data-cid=' + cid + ']').prev().remove();
                    }
                }
                $('div[name=cont_fav_list_' + cid + '_' + joyconfig.joyuserno + ']').remove();
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
//                favEffectiveOnBlog(cid, "收藏", "loveicon", -1);
//                $('[name=cont_fav_' + cid + '_' + joyconfig.joyuserno + ']').remove();
                var favObj = $('a[name=favDetailLink]');
                if ((parseInt(favObj.children('b').text()) - 1) == 0) {
                    $("#wallDescription").children('span').remove();
                } else {
                    favObj.children('b').text(parseInt(favObj.children('b').text()) - 1);
                }
                dom.removeClass().addClass('loveicon');
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
//                var hotNumObj = $('#ia_header_num_hot');
//                var removeNum = parseInt(hotNumObj.text()) - 1;
//                $('#ia_header_num_hot').text(removeNum);
//                var favObj = $('#ia_header_num_fav');
//                favObj.text(parseInt(favObj.text()) - 1);
//                $('[name=cont_fav_list_' + cid + '_' + joyconfig.joyuserno + ']').remove();
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

            $("#wallDescription").append('<div class="love_tc" id="loveUserBox" style="position:absolute;display:none; z-index: 16000"><div class="love_tips"></div><a id="closePop_LoveDialog" class="close" href="javascript:void(0)"></a><div class="love_pd_att"><ul class="clearfix" id="loveListUl">' + listHtml + '</ul>' +
                    loveMoreHtml +
                    '</div></div>');

            var posContainer = $("#wallDescription").position();
            var distance = $("#wallDescription").parent().parent().parent().offset().top - $(document).scrollTop();
            var listBox = $("#loveUserBox");
            if (distance < listBox.height()) {
                listBox.css({'left':posContainer.left - 233 + 'px','top':parseInt(posContainer.top) + 28 + 'px'})
                listBox.children('div:first').removeClass().addClass('love_tips_down');
                listBox.fadeIn();
            } else {
                listBox.css({'left':posContainer.left - 233 + 'px','top':parseInt(posContainer.top) - listBox.height() - 30 + 'px','z-index':'16000'})
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

            var sexHtml='';
            if (profile.detail.sex == null) {

            } else if (profile.detail.sex == '1') {
                sexHtml = '<b class="nan"></b>';
            } else if (profile.detail.sex == '0') {
                sexHtml = '<b class="nv"></b>';
            }
            listHtml += '<li>' +
                    '<div class="love_tx">' +
                    '<a target="_blank" title="' + profile.blog.screenName + '" href="http://' + profile.blog.domain + '.' + joyconfig.DOMAIN + '">' +
                    '<span class="commenfacecon">' +
                    imgHtml +
                    '</span>' +
                    '</a>' +
                    '</div>' +
                    '<div class="love_area">' +
                    '<p><a class="author" href="http://' + profile.blog.domain + '.' + joyconfig.DOMAIN + '" title="' + profile.blog.screenName + '">' + profile.blog.screenName +
                    '</a></p>' +
                    '<p>'+sexHtml+'</p>' +
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
            $(this).children('i').removeClass().addClass(className);
            common.increaseCountByDom($(this).children('span'), num);
        })
    }
    var favEffectiveOnBlog = function(contentId, title, className, num) {
        $.each($('a[name=favButton]'), function(i, val) {
            val.title = title;
            val.className = className;
        });
        common.increaseCountByDom($('[name=hot_num_' + contentId + ']'), num);
        common.increaseCountByDom($('[name=fav_num_' + contentId + ']'), num);
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
//                }).live('focusin', function() {
//                    $('#' + errorId).html('');
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


    var toggleInteractionList = function(dom, cid, cuno, type) {
        //喜欢框是否存在,不存在就创建并显示
        if ($('#ia_area_' + cid).length == 0) {
            interactionBiz.listBiz(dom, cid, cuno, type, generatorLoadingArea, contentShowCallback.showListCallback);//加载喜欢框
        } else {
            //显示时操作
            if (!$('#ia_area_' + cid).is(":hidden")) {
                showInteractionHtml(cid, 'ia_area_', dom, null);
            } else {
                showInteractionHtml(cid, 'ia_area_', dom, null);
//                contentShow.changeTab($("#ia_header_" + cid).find('a[data-itype=' + type + ']'));//选择相应的tab事件
            }
        }
        discornerFun(type, cid)
        discornerFun(type, cid)
        showReplyLayerFocus(cid, 'replyCont_', dom)

    }

    var contentShowCallback = {
        showListCallback:function(dom, cid, cuno, type, resultMsg) {//没有喜欢框时调用
            var html = generatorArea(type, resultMsg, dom);
            $('#reply_loading_' + cid).replaceWith(html);
            reply.bindReplySubFunOnList(cid, cuno, dom);
//            focusreplyhtml(cid, 'replyCont_');
        },
        showInteractionTabCallback:function(dom, cid, cuno, type, resultMsg) { //有喜欢框时切换数据调用
            var html = generatorInteractionContent(type, resultMsg);
            $('#ia_header_' + cid).after(html);
            reply.bindReplySubFunOnList(cid, cuno);
        },
        showInteractionTabOnWallCallback:function(dom, cid, cuno, type, resultMsg) {
            var html = generatorInteractionContentOnWall(type, resultMsg);
            $('#data_area_hot').after(html);
        }
    }

    //loding加载
    var generatorLoadingArea = function(dom, cid) {
        var loadingHtml = '<div class="disarea replytextarea" id="ia_area_' + cid + '" style="display:none">' +
                '<div class="discussoncorner"><span class="discorner"></span></div>' +
                '<div class="discusson">' +
                '<div id="reply_loading_' + cid + '" class="discuss_load">' +
                '<p>读取中，请稍候... </p>' +
                '</div>' +
                '</div></div>';
        dom.parent().parent().parent().parent().after(loadingHtml);
        //显示评论层
        showInteractionHtml(cid, 'ia_area_', dom);
    }
    //内容加载
    var generatorArea = function(tabName, resultMsg, triggerDom) {
        if (resultMsg.result[0].blogContent != null) {
            var replyNum = resultMsg.result[0].blogContent.content.replyTimes;
            var favNum = resultMsg.result[0].blogContent.content.favorTimes;
            return dataHtmlGenerator.header(resultMsg.result[0].blogContent.content.contentId, resultMsg.result[0].blogContent.content.uno, tabName, replyNum, favNum) +
                    '<div id="ia_cont_' + resultMsg.result[0].blogContent.content.contentId + '_' + tabName + '">' +
                    dataHtmlGenerator.interaction(tabName, resultMsg.result[0].blogContent) +
                    dataHtmlGenerator.data(tabName, resultMsg.result[0].blogContent.content.contentId, resultMsg, triggerDom) +
                    dataHtmlGenerator.footer(tabName, resultMsg.result[0].blogContent, replyNum, favNum) + '<div>';
        } else {
            return "";
        }
    }
    //内容区域
    var generatorInteractionContent = function(tabName, resultMsg) {
        var replyNum = resultMsg.result[0].blogContent.content.replyTimes;
        var favNum = resultMsg.result[0].blogContent.content.favorTimes;
        return  '<div id="ia_cont_' + resultMsg.result[0].blogContent.content.contentId + '_' + tabName + '">' +
                dataHtmlGenerator.interaction(tabName, resultMsg.result[0].blogContent) +
                dataHtmlGenerator.data(tabName, resultMsg.result[0].blogContent.content.contentId, resultMsg) +
                dataHtmlGenerator.footer(tabName, resultMsg.result[0].blogContent, replyNum, favNum) + '<div>';
    }
    var generatorInteractionContentOnWall = function(tabName, resultMsg) {
        var replyNum = resultMsg.result[0].blogContent.content.replyTimes;
        var favNum = resultMsg.result[0].blogContent.content.favorTimes;
        return  '<div id="data_area_' + tabName + '">' +
                dataHtmlGenerator.wallData(resultMsg) +
                dataHtmlGenerator.wallFooter(tabName, resultMsg.result[0].blogContent, replyNum, favNum) + '<div>';
    }

    var dataHtmlGenerator = {
        header:function(cid, cuno, tabName, replyNum, favNum) {
            var header = '';
            return header;
        },
        //评论输入框 喜欢无
        interaction:function(tabName, blogContent) {
            var interaction = '';
            if (tabName == 'reply') {
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
                    replyAndForwardText += '<label><input class="checktext" type="checkbox" name="replayRoot">同时评论给原文作者</label> <a name="atLink" title="' + blogContent.rootProfile.blog.screenName + '" class="author" href="http://' + blogContent.rootProfile.blog.domain + '.' + joyconfig.DOMAIN + '">' + blogContent.rootProfile.blog.screenName + '</a>';
                }
                interaction = '<div class="commentborder" id="ia_reply_post_' + blogContent.content.contentId + '">' +
                        '<div class="commentbox clearfix">' +
                        '<textarea class="commenttext" style="font-family:Tahoma, \'宋体\'; height: 24px;font-size:12px; " id="replyCont_' + blogContent.content.contentId + '"></textarea>' +
                        '' +
                        '<a name="subreplybtn" class="submitbtn" id="subreply_' + blogContent.content.contentId + '"><span>评论</span></a>' +
                        '</div>' +
                        '<div class="related clearfix">' +
                        '<div class="transmit_pic clearfix" id="reply_image_' + blogContent.content.contentId + '">' +
                        '<div class="commenface"><a href="javascript:void(0)" id="replyContmood_' + blogContent.content.contentId + '" title="表情"></a></div>' +
                        '<div class="t_pic" name="reply_image_icon">' +
                        '<a class="t_pic1" href="javascript:void(0)">图片</a>' +
                        '</div>' +
                        '<div style="display:none;" name="reply_image_icon_more" class="t_pic_more">' +
                        '<a title="图片" class="t_pic1" href="javascript:void(0)" name="reply_upload_img" data-cid="reply_image_' + blogContent.content.contentId + '">图片</a>' +
                        '<a title="链接" class="t_more" href="javascript:void(0)" name="reply_upload_img_link" data-cid="reply_image_' + blogContent.content.contentId + '">链接</a>' +
                        '</div>' +
                        '</div>' +
                        '<div class="transmit clearfix">' +
                        '<span class="y_zhuanfa">' +
                        replyAndForwardText +
                        '</span>' +
                        '</div>' +
                        '</div>' +
                        '</div>';
            }
            return interaction;
        },
        data:function(tabName, cid, resultMsg, triggerDom) {
            var dataHtml = '';
            var result = resultMsg.result[0];
            if (result.interactionInfoList.length > 0) {
                $.each(result.interactionInfoList, function(i, val) {
                    if (val.interaction.interactionType.code == 'reply') {
                        dataHtml += dataHtmlGenerator.replyData(val, true, triggerDom);
                    } else if (val.interaction.interactionType.code == 'fav') {
                        dataHtml += dataHtmlGenerator.favData(val);
                    }
                });
            } else {
                if (tabName == 'hot') {
                    dataHtml = '<div class="partical_love" id="cont_hot_empty_' + cid + '">' +
                            '暂时还没有用户喜欢、评论这篇文章！' +
                            '</div>';
                } else if (tabName == 'fav') {
                    dataHtml = '<div class="partical_love" id="cont_fav_empty_' + cid + '">' +
                            '暂时还没有用户喜欢这篇文章！' +
                            '</div>';
                }
            }
            return dataHtml
        },
        footer:function(tabName, blogContent, replyNum, favNum) {
            var footer = '';
            if (tabName == 'hot') {
                var times = replyNum + favNum - 10;
                if (times > 0) {
                    footer += '<div class="partical_more">后面还有' + times + '条热度，<a href="' + joyconfig.URL_WWW + '/home/' + blogContent.profile.blog.domain + '/' + blogContent.content.contentId + '#interaction">点击查看&gt;</a></div>';
                }
            } else if (tabName == 'reply') {
                var times = replyNum - 10;
                if (times > 0) {
                    footer += '<div class="partical_more">后面还有' + times + '条评论，<a href="' + joyconfig.URL_WWW + '/home/' + blogContent.profile.blog.domain + '/' + blogContent.content.contentId + '?iat=reply#interaction">点击查看&gt;</a></div>';
                }
            } else if (tabName == 'fav') {
                var times = favNum - 10;
                if (times > 0) {
                    footer += '<div class="partical_more">后面还有' + times + '条喜欢，<a href="' + joyconfig.URL_WWW + '/home/' + blogContent.profile.blog.domain + '/' + blogContent.content.contentId + '?iat=fav#interaction">点击查看&gt;</a></div>';
                }
            }
            return footer;
        },

        //
        wallData:function(resultMsg) {
            var dataHtml = '';
            var result = resultMsg.result[0];
            $.each(result.interactionInfoList, function(i, val) {
                if (val.interaction.interactionType.code == 'reply') {
                    dataHtml += dataHtmlGenerator.replyDataOnWall(val, true);
                } else if (val.interaction.interactionType.code == 'fav') {
                    dataHtml += dataHtmlGenerator.favDataOnWall(val, true);
                }
            });
            return dataHtml
        },
        wallFooter:function(tabName, blogContent, replyNum, favNum) {
            var footer = '';
            if (tabName == 'hot') {
                var times = replyNum + favNum - 10;
                if (times > 0) {
                    footer += '<div class="see_more">后面还有' + times + '条热度，<a href="' + joyconfig.URL_WWW + '/home/' + blogContent.profile.blog.domain + '/' + blogContent.content.contentId + '#interaction">点击查看&gt;</a></div>';
                }
            } else if (tabName == 'reply') {
                var times = replyNum - 10;
                if (times > 0) {
                    footer += '<div class="see_more">后面还有' + times + '条评论，<a href="' + joyconfig.URL_WWW + '/home/' + blogContent.profile.blog.domain + '/' + blogContent.content.contentId + '?iat=reply#interaction">点击查看&gt;</a></div>';
                }
            } else if (tabName == 'fav') {
                var times = favNum - 10;
                if (times > 0) {
                    footer += '<div class="see_more">后面还有' + times + '条喜欢，<a href="' + joyconfig.URL_WWW + '/home/' + blogContent.profile.blog.domain + '/' + blogContent.content.contentId + '?iat=fav#interaction">点击查看&gt;</a></div>';
                }
            }
            return footer;
        },

        replyData:function(interactionInfo, isDisplay, triggerDom) {
            var displyStyle = isDisplay ? '' : 'display:none';

            var headimg = "";//左侧头像内容
            if (interactionInfo.interactionProfile.blog.headIconSet == null || interactionInfo.interactionProfile.blog.headIconSet.iconSet == null
                    || interactionInfo.interactionProfile.blog.headIconSet.iconSet.length <= 0) {
                headimg = joyconfig.URL_LIB + '/static/theme/default/img/default.jpg';
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
                if (verifyType.code == 'p') {
                    vipStr = '<a href="http://' + interactionInfo.interactionProfile.blog.domain + '.' + joyconfig.DOMAIN + '" class="vip" title="' + joyconfig.vip_title + '"></a>';
                }
                if (verifyType.code == 'c') {
                    vipStr = '<a href="http://' + interactionInfo.interactionProfile.blog.domain + '.' + joyconfig.DOMAIN + '" class="tvip" title="' + joyconfig.vipc_title + '"></a>';
                }
            }

            var dataHtml = '';
            var rightStr = '';//右侧文本内容
            var delStr = '';
            if (interactionInfo.interaction.contentUno == joyconfig.joyuserno || interactionInfo.interaction.interactionUno == joyconfig.joyuserno) {
                delStr = '<a href="javascript:void(0);" name="del_reply_' + interactionInfo.interaction.interactionId + '">删除</a> |  ';
                //为该段代码注册live事件
                $("a[name=del_reply_" + interactionInfo.interaction.interactionId + "]").live('click', function() {
                    reply.bindPostReplyDelOnList(interactionInfo.interaction.contentId, interactionInfo.interaction.contentUno, interactionInfo.interaction.interactionId, triggerDom)
                });
            }

            rightStr = '<span class="delete" >' + delStr +
                    ' <a href="javascript:void(0);"  name="sub_reply_' + interactionInfo.interaction.interactionId + '" >回复</a>' +
                    '</span>';
            $("a[name=sub_reply_" + interactionInfo.interaction.interactionId + "]").die().live('click', function() {
                reply.togglereReplyOnList(interactionInfo, $(this));
            });

            var replyImageStr = replyImageHtml(interactionInfo);
            dataHtml += '<div class="conmenttx clearfix" name="cont_cmt_list_' + interactionInfo.interaction.interactionId + '" style="' + displyStyle + '">' +
                    '<div class="conmentface" id="cont_cmt_list_' + interactionInfo.interaction.interactionId + '">' +
                    '<div class="commenfacecon">' +
                    '<a class="cont_cl_left" name="atLink" title="' + interactionInfo.interactionProfile.blog.screenName + '" href="http://' + interactionInfo.interactionProfile.blog.domain + '.' + joyconfig.DOMAIN + '">' +
                    '<img src="' + headimg + '" width="33px" height="33px"/>' +
                    '</a>' +
                    '</div>' +
                    '</div>' +
                    '<div class="conmentcon">' +
                    '<a href="http://' + interactionInfo.interactionProfile.blog.domain + '.' + joyconfig.DOMAIN + '" name="atLink" title="' + interactionInfo.interactionProfile.blog.screenName + '">' + interactionInfo.interactionProfile.blog.screenName + '</a>' + vipStr + '：' + interactionInfo.interaction.interactionContent +
                    replyImageStr +
                    '<div class="commontft clearfix"><span class="reply_time">' + interactionInfo.interaction.createDateStr + '</span>' + rightStr + '</div>' +
                    '</div>' +
                    '</div>';
            return dataHtml;
        },
        favData:function(interactionInfo) {
            return dataHtmlGenerator.generatorFavData(interactionInfo.interactionProfile, interactionInfo.interaction.contentId, interactionInfo.interaction.interactionUno, interactionInfo.interaction.createDate, true);
        },

        replyDataOnWall:function(interactionInfo, isDisplay) {
            var displyStyle = isDisplay ? '' : 'display:none';

            var iconSet = interactionInfo.interactionProfile.blog.headIconSet.iconSet;
            var reReplyHtml = '<a href="javascript:void(0)"  name="reReplyLink"  data-pid="' + interactionInfo.interaction.interactionId + '" data-puno="' + interactionInfo.interaction.interactionUno + '" data-pname="' + interactionInfo.interactionProfile.blog.screenName + '" data-cid="' + interactionInfo.interaction.contentId + '" data-cuno="' + interactionInfo.interaction.contentUno + '">回复</a>';

            var headIcon;
            for (var i = 0; i < iconSet.length; i++) {
                if (iconSet[i].validStatus) {
                    headIcon = iconSet[i].headIcon;
                    break;
                }
            }
            var delHtml = '';
            if (interactionInfo.interaction.contentUno == joyconfig.joyuserno || interactionInfo.interaction.interactionUno == joyconfig.joyuserno) { //判断是否有删除按钮
                delHtml = '<a href="javascript:void(0);" name="del_reply_' + interactionInfo.interaction.interactionId + '" data-cid="' + interactionInfo.interaction.contentId + '" data-cuno="' + interactionInfo.interaction.contentUno + '">删除</a>|';
                $("a[name=del_reply_" + interactionInfo.interaction.interactionId + "]").live('click', function() {
                    reply.bindPostReplyDelOnWall(interactionInfo.interaction.contentId, interactionInfo.interaction.contentUno, interactionInfo.interaction.interactionId)
                });
            }
            var replyImageStr = replyImageHtmlOnBlog(interactionInfo);
            var noborder = $("#data_area_hot").children("div").size() == 0 ? "noborder" : "";
            var html = '<div class="area blogopinion ' + noborder + '" name="cont_reply_' + interactionInfo.interaction.interactionId + '" style="' + displyStyle + '">' +
                    '<dl>' +
                    '<dt class="personface">' +
                    '<a class="tag_cl_left" href="http://' + interactionInfo.interactionProfile.blog.domain + '.' + joyconfig.DOMAIN + '" target="_blank" title="' + interactionInfo.interactionProfile.blog.screenName + '">' +
                    '<img width="58" height="58" src="' + common.parseSimg(headIcon, joyconfig.DOMAIN) + '"></a>' +
                    '</dt>' +
                    '<dd class="textcon">' +
                    '<a class="author" href="http://' + interactionInfo.interactionProfile.blog.domain + '.' + joyconfig.DOMAIN + '" target="_blank" title="' + interactionInfo.interactionProfile.blog.screenName + '">' +
                    interactionInfo.interactionProfile.blog.screenName +
                    '</a>' +
                    '<p> ' + interactionInfo.interaction.interactionContent + ' </p>' + replyImageStr +
                    '<div class="commontft clearfix">' + generateDate(interactionInfo.interaction.createDate) +
                    '<span class="delete">' +
                    delHtml +
                    reReplyHtml +
                    '</span>' +
                    '</div>' +
                    '</dd>' +
                    '</dl>' +
                    '</div>';
            return html;
        },
        favDataOnWall:function(interactionInfo, isDisplay) {
            return dataHtmlGenerator.generatorFavDataOnWall(interactionInfo.interactionProfile, interactionInfo.interaction.contentId, interactionInfo.interaction.contentUno, interactionInfo.interaction.createDate, isDisplay);
        },

        replyDataOnBlog:function(replyInfo) {
            var reReplyHtml = '<a href="javascript:void(0);"  name="reReplyLink"  data-pid="' + replyInfo.interaction.interactionId + '" data-puno="' + replyInfo.interaction.interactionUno + '" data-pname="' + replyInfo.interactionProfile.blog.screenName + '" data-cid="' + replyInfo.interaction.contentId + '" data-cuno="' + replyInfo.interaction.contentUno + '">回复</a>';
            var iconSet = replyInfo.interactionProfile.blog.headIconSet.iconSet;
            var headIcon;
            for (var i = 0; i < iconSet.length; i++) {
                if (iconSet[i].validStatus) {
                    headIcon = iconSet[i].headIcon;
                    break;
                }
            }
            var delHtml = '';
            if (replyInfo.interaction.contentUno == joyconfig.joyuserno || replyInfo.interaction.interactionUno == joyconfig.joyuserno) { //判断是否有删除按钮
                delHtml = '<a href="javascript:void(0);" name="del_reply_' + replyInfo.interaction.interactionId + '" data-cid="' + replyInfo.interaction.contentId + '" data-cuno="' + replyInfo.interaction.contentUno + '">删除</a>|';
                $("a[name=del_reply_" + replyInfo.interaction.interactionId + "]").live('click', function() {
                    reply.bindPostReplyDelOnBlog(replyInfo.interaction.contentId, replyInfo.interaction.contentUno, replyInfo.interaction.interactionId)
                });
            }
            var replyImageStr = replyImageHtmlOnBlog(replyInfo);
            var noborder = $("div[id^=cont_reply_]").size() == 0 ? 'noborder' : '';
            var html = '<div class="area blogopinion ' + noborder + '" id="cont_reply_' + replyInfo.interaction.interactionId + '" style="display:none">' +
                    '<dl>' +
                    '<dt class="personface">' +
                    '<a class="tag_cl_left" href="http://' + replyInfo.interactionProfile.blog.domain + '.' + joyconfig.DOMAIN + '" name="atLink" title="' + replyInfo.interactionProfile.blog.screenName + '">' +
                    '<img width="58" height="58" src="' + common.parseSimg(headIcon, joyconfig.DOMAIN) + '"></a>' +
                    '</dt>' +
                    '<dd class="textcon">' +
                    '<a class="author" href="http://' + replyInfo.interactionProfile.blog.domain + '.' + joyconfig.DOMAIN + '" name="atLink" title="' + replyInfo.interactionProfile.blog.screenName + '">' +
                    replyInfo.interactionProfile.blog.screenName +
                    '</a>' +
                    '<p> ' + replyInfo.interaction.interactionContent + ' </p>' + replyImageStr +
                    '<div class="commontft clearfix">' + generateDate(replyInfo.interaction.createDate) +
                    '<span class="delete">' +
                //'<a href="javascript:void(0);" id="del_reply_' + replyInfo.interaction.interactionId + '" data-cid="' + replyInfo.interaction.contentId + '" data-cuno="' + replyInfo.interaction.contentUno + '">删除</a>|' +
                    delHtml + reReplyHtml +
                    '</span>' +
                    '</div>' +
                    '</dd>' +
                    '</dl>' +
                    '</div>';
            return html;
        },

        generatorFavDataOnWall:function(profile, contentId, contentUno, createDate, isDisplay) {
            var displyStyle = isDisplay ? '' : 'display  :none';

            var headimg = "";//左侧头像内容
            if (profile.blog.headIconSet == null || profile.blog.headIconSet.iconSet == null
                    || profile.blog.headIconSet.iconSet.length <= 0) {
                headimg = joyconfig.URL_LIB + '/static/theme/default/img/default.jpg';
            } else {
                for (var i = 0; i < profile.blog.headIconSet.iconSet.length; i++) {
                    if (profile.blog.headIconSet.iconSet[i].validStatus) {
                        headimg = common.parseSimg(profile.blog.headIconSet.iconSet[i].headIcon, joyconfig.DOMAIN);
                        break;
                    }
                }
            }

            var vipStr = '';
            var verifyType = profile.detail.verifyType;
            if (verifyType != null) {
                if (verifyType.code == 'p') {
                    vipStr = '<a href="http://' + profile.blog.domain + '.' + joyconfig.DOMAIN + '" class="vip" title="' + joyconfig.vip_title + '"></a>';
                }
                if (verifyType.code == 'c') {
                    vipStr = '<a href="http://' + profile.blog.domain + '.' + joyconfig.DOMAIN + '" class="tvip" title="' + joyconfig.vipc_title + '"></a>';
                }
            }

            var html = '<div class="area blogopinion" name="cont_fav_list_' + contentId + '_' + profile.blog.uno + '" style="' + displyStyle + '">' +
                    '<dl>' +
                    '<dt class="personface">' +
                    '<a class="tag_cl_left" href="http://' + profile.blog.domain + '.' + joyconfig.DOMAIN + '" target="_blank" title="' + profile.blog.screenName + '">' +
                    '<img width="58" height="58" src="' + headimg + '"></a>' +
                    '</dt>' +
                    '<dd class="textcon">' +
                    '<a class="author" href="http://' + profile.blog.domain + '.' + joyconfig.DOMAIN + '" target="_blank" title="' + profile.blog.screenName + '">' +
                    profile.blog.screenName +
                    '</a>' +
                    '<p>很喜欢这篇文章</p>' +
                    '<div class="commontft clearfix">' + generateDate(createDate) +
                    '</div>' +
                    '</dd>' +
                    '</dl>' +
                    '</div>';
            return html;
        },
        generatorFavData:function(profile, contentId, contentUno, createDate, isDisplay) {
            var displyStyle = isDisplay ? '' : 'display:none';

            var headimg = "";//左侧头像内容
            if (profile.blog.headIconSet == null || profile.blog.headIconSet.iconSet == null
                    || profile.blog.headIconSet.iconSet.length <= 0) {
                headimg = joyconfig.URL_LIB + '/static/theme/default/img/default.jpg';
            } else {
                for (var i = 0; i < profile.blog.headIconSet.iconSet.length; i++) {
                    if (profile.blog.headIconSet.iconSet[i].validStatus) {
                        headimg = common.parseSimg(profile.blog.headIconSet.iconSet[i].headIcon, joyconfig.DOMAIN);
                        break;
                    }
                }
            }

            var vipStr = '';
            var verifyType = profile.detail.verifyType;
            if (verifyType != null) {
                if (verifyType.code == 'p') {
                    vipStr = '<a href="http://' + profile.blog.domain + '.' + joyconfig.DOMAIN + '" class="vip" title="' + joyconfig.vip_title + '"></a>';
                }
                if (verifyType.code == 'c') {
                    vipStr = '<a href="http://' + profile.blog.domain + '.' + joyconfig.DOMAIN + '" class="tvip" title="' + joyconfig.vipc_title + '"></a>';
                }
            }

            var dataHtml = '';
            dataHtml += '<div class="conmenttx clearfix"  name="cont_fav_list_' + contentId + '_' + profile.blog.uno + '" style="' + displyStyle + '">' +
                    '<div class="conmentface" id="cont_cmt_list_' + contentId + '_' + contentId + '">' +
                    '<div class="commenfacecon">' +
                    '<a class="cont_cl_left" name="atLink" title="' + profile.blog.screenName + '" href="http://' + profile.blog.domain + '.' + joyconfig.DOMAIN + '">' +
                    '<img src="' + headimg + '" width="33px" height="33px"/>' +
                    '</a>' +
                    '</div>' +
                    '</div>' +
                    '<div class="conmentcon">' +
                    '<a href="http://' + profile.blog.domain + '.' + joyconfig.DOMAIN + '" name="atLink" title="' + profile.blog.screenName + '">' + profile.blog.screenName + '</a>：很喜欢这篇文章' +
                    '<div class="commontft clearfix">' +
                    '<span class="reply_time">' + generateDate(createDate) + '</span>' +
                    '<span class="delete"></span>' +
                    '</div></div></div>';
            return dataHtml;
        },
        generatorFavDataOnBlog:function(profile, contentId, contentUno, createDate, isDisplay) {
            var displyStyle = isDisplay ? '' : 'display:none';

            var headimg = "";//左侧头像内容
            if (profile.blog.headIconSet == null || profile.blog.headIconSet.iconSet == null
                    || profile.blog.headIconSet.iconSet.length <= 0) {
                headimg = joyconfig.URL_LIB + '/static/theme/default/img/default.jpg';
            } else {
                for (var i = 0; i < profile.blog.headIconSet.iconSet.length; i++) {
                    if (profile.blog.headIconSet.iconSet[i].validStatus) {
                        headimg = common.parseSimg(profile.blog.headIconSet.iconSet[i].headIcon, joyconfig.DOMAIN);
                        break;
                    }
                }
            }

            var vipStr = '';
            var verifyType = profile.detail.verifyType;
            if (verifyType != null) {
                if (verifyType.code == 'p') {
                    vipStr = '<a href="http://' + profile.blog.domain + '.' + joyconfig.DOMAIN + '" class="vip" title="' + joyconfig.vip_title + '"></a>';
                }
                if (verifyType.code == 'c') {
                    vipStr = '<a href="http://' + profile.blog.domain + '.' + joyconfig.DOMAIN + '" class="tvip" title="' + joyconfig.vipc_title + '"></a>';
                }
            }

            var dataHtml = '';
            dataHtml += '<div class="area blogopinion clearfix" name="cont_fav_' + contentId + '_' + profile.blog.uno + '" style="' + displyStyle + '">' +
                    '<dl>' +
                    '<dt class="personface">' +
                    '<a href="http://' + profile.blog.domain + '.' + joyconfig.DOMAIN + '" name="atLink" title="' + profile.blog.screenName + '">' +
                    '<img src="' + headimg + '" class="user" width="58" height="58"/>' +
                    '</a>' +
                    '</dt>' +
                    '<dd class="textcon">' +
                    '<a href="http://' + profile.blog.domain + '.' + joyconfig.DOMAIN + '" name="atLink" class="author" title="' + profile.blog.screenName + '">' + profile.blog.screenName + '</a>' +
                    vipStr +
                    '<p>很喜欢这篇文章</p>' +
                    '<div class="commontft clearfix">' + generateDate(createDate) + '</div>' +
                    '</dd>' +
                    '</dl>' +
                    '</div>';
            return dataHtml;
        }
    }

    function generateDateYYYMMdd(date) {
        var dateArray = date.split('-');
        var year = dateArray[0]
        var month = dateArray[1]
        var dayArray = dateArray[2].split(' ');
        var day = dayArray[0];
        var hhmm = dayArray[1];
        return  year + "年" + month + "月" + day + "日";
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

    function generateShortDate(date) {
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

    var reply = {
        bindReplySubFunOnList:function(cid, cuno, triggerDom) {
            $("#subreply_" + cid).die().live('click', function() {
                $("#replyUploadImageDialog").hide();//让上传图片框消失
                if (window['imageUploadFlag']) {
                    return;
                }
                var content = $(this).prev().val();
                var trimContent = content.replace(/\s+/g, " ");
                //同时转帖原文
                var forwardRoot = false;
                //发送原作者
                var replayRoot = false;
                var syncFoward = false;
                //判断是否为选中状态
                $(this).parent().parent().children().eq(1).children().eq(1).find('input').each(function(i) {
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
                ajaxSaveReply(cid, cuno, '', '', '', trimContent, forwardRoot, replayRoot, syncFoward, $(this), subreplyOnlist, triggerDom)
            })
            var textareaDom = $("#replyCont_" + cid);
            var len = textareaDom.val().length;
            setTimeout(function() {
                if (len != 0) {
                    var te = new TextareaEditor(document.getElementById("replyCont_" + cid));
                    te.setSelectionRange(len, len);
                    te = null;
                } else {
                    document.getElementById("replyCont_" + cid).focus();
                }
            }, 400);
            replylivemood(cid, "replyCont_" + cid);
            eventLive('keydown keypress keyup', textareaDom, ctrlEnterOnOther, false, true);
//            eventLive('keypress keyup', textareaDom, function(dom) {
//                Sys.ie == '7.0' ? dom.parent().next().children().eq(0).hide().show() : "";
//                if (!$("#replyUploadImageDialog").is(":hidden")) {
//                    if ($("#replyUploadImageDialog") != null) {
//                        $("#replyUploadImageDialog").css('top', $("#replyUploadImageDialog").parent().children().eq(1).position().top + 28 + 'px');
//                    }
//                }
//            }, false, false)
            eventLive('click', $("#forwardRoot_" + cid), syncDom);
            $("#replyCont_" + cid).AutoHeight({maxHeight:200});
            if ($("#ia_area_" + cid).is(":animated")) {
                window.autoAt({id:"replyCont_" + cid,ohterStyle:'line-height:22px'});
            }
            replyImage.replyImageInit();
        },
        togglereReplyOnList:function(interactionInfo, reReplyDom) {
            var dataArea = reReplyDom.parent().parent().parent().parent().parent();
            var id = '';
            if (dataArea != null) {
                id = '#' + dataArea.attr('id') + " ";
            }

            if ($(id + '#reply_comment_' + interactionInfo.interaction.interactionId).length == 0) {
                var showReHtml = '<div style="display:none" id="reply_comment_' + interactionInfo.interaction.interactionId + '">' +
                        '<div class="commentbox clearfix">' +
                        '<textarea class="commenttext" style="heigth:24px;font-size:12px; font-family:Tahoma, \'宋体\';" id="Rereply_textarea_' + interactionInfo.interaction.interactionId + '">回复@' + interactionInfo.interactionProfile.blog.screenName + ':</textarea>' +
                        '<a name="subreplybtn" class="submitbtn" id="subrereply_' + interactionInfo.interaction.interactionId + '"><span>评论</span></a></div>' +
                        '<div class="related clearfix">' +
                        '<div class="transmit_pic clearfix" id="rereply_image_' + interactionInfo.interaction.interactionId + '">' +
                        '<div class="commenface"><a href="javascript:void(0)" id="replyContmood_Re_' + interactionInfo.interaction.interactionId + '" title="表情"></a></div>' +
                        '<div class="t_pic" name="reply_image_icon">' +
                        '<a class="t_pic1" href="javascript:void(0)">图片</a>' +
                        '</div>' +
                        '<div style="display:none;" name="reply_image_icon_more" class="t_pic_more">' +
                        '<a title="图片" class="t_pic1" href="javascript:void(0)" name="reply_upload_img" data-cid="rereply_image_' + interactionInfo.interaction.interactionId + '">图片</a>' +
                        '<a title="链接" class="t_more" href="javascript:void(0)" name="reply_upload_img_link" data-cid="rereply_image_' + interactionInfo.interaction.interactionId + '">链接</a>' +
                        '</div>' +
                        '</div>';
                if (interactionInfo.content.publishType.code == 'fwd') {
                    showReHtml += '<div class="transmit clearfix"><span class="y_zhuanfa"><label><input type="checkbox" name="forwardRoot" class="checktext" id="forwardRoot_' + interactionInfo.content.contentId + '" />同时转发到我的博客</label><br />' +
                            '<label><input type="checkbox" name="replayRoot" class="checktext">同时评论给原文作者</label>' +
                            '<a class="author" href="javascript:void(0)" name="atLink" title="' + interactionInfo.rootProfileBlog.screenName + '">' + interactionInfo.rootProfileBlog.screenName + '</a></span></div>';
                } else {
                    showReHtml += '<div class="transmit clearfix"><span class="y_zhuanfa"><label><input type="checkbox" name="forwardRoot" class="checktext" id="forwardRoot_' + interactionInfo.content.contentId + '" />同时转发到我的博客</label></span></di>';
                }
                showReHtml += '</div></div>';
                reReplyDom.parent().parent().after(showReHtml);
                $('#subrereply_' + interactionInfo.interaction.interactionId).die().live('click', function() {
                    //cid, cuno, pid, puno, pscreenName, replyContent, forwardRoot, replayRoot, replyDom, checkSuccessCallback
                    $("#replyUploadImageDialog").hide();//让上传图片框消失
                    if (window['imageUploadFlag']) {
                        return;
                    }
                    var content = $(this).prev().val();
                    var trimContent = content.replace(/\s+/g, " ");
                    //同时转帖原文
                    var forwardRoot = false;
                    //发送原作者
                    var replyRoot = false;
                    //判断是否为选中状态
                    $(this).parent().parent().find("input[type=checkbox]").each(function(i, val) {
                        if (val.checked) {
                            if (val.name == "forwardRoot") {
                                forwardRoot = true;
                            }
                            if (val.name == "replayRoot") {
                                replyRoot = true;
                            }
                        }
                    });

                    var syncForward = false;
                    ajaxSaveReply(interactionInfo.interaction.contentId,
                            interactionInfo.interaction.contentUno,
                            interactionInfo.interaction.interactionId,
                            interactionInfo.interaction.interactionUno,
                            interactionInfo.interactionProfile.blog.screenName, trimContent, forwardRoot, replyRoot, syncForward, $(this), subreplyOnlist);
                });
                eventLive('keydown keypress keyup', $("#Rereply_textarea_" + interactionInfo.interaction.interactionId), ctrlEnterOnOther, false, true)
                replylivemood("Re_" + interactionInfo.interaction.interactionId, "Rereply_textarea_" + interactionInfo.interaction.interactionId);
                eventLive('click', $("#forwardRoot_" + interactionInfo.interaction.interactionId), syncDom);
                setTimeout(function() {
                    $("#Rereply_textarea_" + interactionInfo.interaction.interactionId).AutoHeight({maxHeight:200,minHeight:24})
                    window.autoAt({id:"Rereply_textarea_" + interactionInfo.interaction.interactionId,ohterStyle:'line-height:22px'});
                }, 500)
            }

//            $('div[id^=reply_comment_]').fadeOut();
            showInteractionHtml(interactionInfo.interaction.interactionId, 'reply_comment_', reReplyDom, function() {
                focusreplyhtml(interactionInfo.interaction.interactionId, 'Rereply_textarea_');
            });
        },
        bindPostReplyOnBlog:function(replyDom) {
            var replyContent = $.trim($('#reply_content').val());
            $('#reply_content').blur();
            var cid = $('#hidden_cid').val();
            var cuno = $('#hidden_cuno').val();
            var forwardroot = $('#check_forwardroot').attr('checked');
            var replyroot = false;

            if ($('#check_replyroot').size() > 0) {
                replyroot = $('#check_replyroot').attr('checked');
            }

            var alertText = ''
            var privilegeObj = ajaxverify.verifyPrivilege(true);
            if (privilegeObj.status_code == '-1') {
                loginBiz.maskLoginByJsonObj(privilegeObj);
                return;
            } else if (privilegeObj.status_code != '1') {
                alertText = privilegeObj.msg;
            }
            if (alertText.length > 0) {
                joymealert.alert({text:alertText,tipLayer:true,textClass:"tipstext"});
                return false;
            }

            var syncForward = $('#sync_forward').attr('checked');
            ajaxSaveReply(cid, cuno, '', '', '', replyContent, forwardroot, replyroot, syncForward, replyDom, function(paramObj, replyDom) {
                replyBiz.commonreply(paramObj, replyCallback.loadreplyOnBlogCallback, replyCallback.postReplyOnBlogCallback, replyDom);
            });
        },
        bindPostReReplyOnBlog:function(replyDom) {
            var pid = replyDom.attr('data-pid');
            var puno = replyDom.attr('data-puno');
            var pscreenName = replyDom.attr('data-pname');
            var cid = replyDom.attr('data-cid');
            var cuno = replyDom.attr('data-cuno');

            var replyArea = $('#rereply_area_' + pid);
            //为空
            if (replyArea.size() == 0) {
                var rcid = $('#hidden_rcid').val();
                var rdomain = $('#hidden_rdomain').val();
                var rname = $('#hidden_rname').val();

                var isForward = rcid != null && rcid.length > 0;
                var forwardCheckHtml = '';
                if (isForward) {
                    forwardCheckHtml = '<p><input id="rereply_replyroot_' + pid + '" class="checktext" type="checkbox"  ><label for="checkbox">同时评论给原文作者' +
                            '<a href="http://' + rdomain + '.' + joyconfig.DOMAIN + '" name="atLink" title="' + rname + '">' + rname + '</a>' +
                            '</label></p>';
                }

                var html = '<div class="blog_discusson" id="rereply_area_' + pid + '" style="display:none">' +
                        '<div class="commentbox clearfix">' +
                        '<textarea class="commenttext" style="font-family:Tahoma, \'宋体\';" id="rereply_content_' + pid + '" type="text">回复@' + pscreenName + '：</textarea>' +
                        '<a class="submitbtn" id="submit_rereply_' + pid + '"><span>回复</span></a>' +
                        '</div>' +
                        '<div class="related clearfix">' +
                        '<div id="rereply_image_' + pid + '" class="transmit_pic clearfix">' +
                        '<div class="commenface"><a href="javascript:void(0);" id="replyContmood_re_' + pid + '" title="表情"></a></div>' +
                        '<div name="reply_image_icon" class="t_pic" style="display: block;">' +
                        '<a href="javascript:void(0)" class="t_pic1">图片</a>' +
                        '</div>' +
                        '<div class="t_pic_more" name="reply_image_icon_more" style="display: none;">' +
                        '<a data-cid="rereply_image_' + pid + '" name="reply_upload_img" href="javascript:void(0)" class="t_pic1" title="图片">图片</a>' +
                        '<a data-cid="rereply_image_' + pid + '" name="reply_upload_img_link" href="javascript:void(0)" class="t_more" title="链接">链接</a></div>' +
                        '</div>' +
                        '<div>' +
                        '<p><input id="rereply_forwardroot_' + pid + '" class="checktext" type="checkbox"><label for="checkbox">同时转发到我的博客</label></p>' +
                        forwardCheckHtml +
                        '</div>'
                '</div>' +
                '</div>';
                replyDom.parent().parent().after(html);

                replylivemood("re_" + pid, "rereply_content_" + pid);
//                $("#rereply_content_" + pid).parent().css("background", "#fff");
                $('#submit_rereply_' + pid).die().live('click', function() {

                    var replyContent = $('#rereply_content_' + pid).val();
                    var forwardroot = $('#rereply_forwardroot_' + pid).attr('checked');

                    var replyroot = false;
                    if ($('#rereply_replyroot_' + pid).size()) {
                        replyroot = $('#rereply_replyroot_' + pid).attr('checked');
                    }

                    var syncForward = false;
                    ajaxSaveReply(cid, cuno, pid, puno, pscreenName, replyContent, forwardroot, replyroot, syncForward, $(this), function(paramObj, replyDom) {
                        replyBiz.commonreply(paramObj, replyCallback.loadreplyOnBlogCallback, replyCallback.postReplyOnBlogCallback, replyDom);
                    });

                });

                $("#rereply_content_" + pid).die().live("keydown", function(event) {
                    common.joymeCtrlEnter(event, $('#submit_rereply_' + pid));
                })
                $('#rereply_content_' + pid).AutoHeight({maxHeight:200,minHeight:24}).css('font-size', '12px');
                window.autoAt({id:"rereply_content_" + pid,ohterStyle:'line-height:22px'});
            }
            $('div[id^=rereply_area_]').fadeOut();

            showInteractionHtml(pid, 'rereply_area_', replyDom, function() {
                focusreplyhtml(pid, 'rereply_content_');
            });

        },
        showReplyTextBoxOnBlog:function(replyDom){
            var html=
                '<textarea warp="off" style="font-family:Tahoma, \'宋体\';" class="discuss_text focus" rows="" cols="" id="reply_content" name="content"></textarea>'+
                    '<div class="related clearfix">'+
                    '<div id="reply_image_3Vi3BdVLR04EzCf4gt5K14" class="transmit_pic clearfix">'+
                    '<a href="javascript:void(0)" title="表情" id="reply_mood" class="commenface"></a>'+
                    '<span class="y_zhuanfa">'+
                    '<label><input type="checkbox" id="check_forwardroot" class="checktext">同时转发到我的博客</label>'+
                    '</span>'+
                    '<a class="submitbtn fr" id="reply_submit"><span>评 论</span></a>'+
                    '</div>'+
                    '</div>';

            replyDom.parent().html(html);
        },
        bindPostReplyOnWall:function(replyDom) {
            if (joyconfig.joyuserno == '') {
                var discovery = require('./discovery');
                discovery.appendLogin(subReplyOnWall, []);
                return;
            } else {
                subReplyOnWall();
            }
            function subReplyOnWall() {
                if ($("#maskLoginOnWall").length > 0) {
                    $("#reply_content").removeAttr('disabled');
                    $("#maskLoginOnWall").parent().remove();
                    $("#reply_content").focus();
                    return;
                }
                var content = $("#reply_content").val();
                $("#reply_content").val("");
                var trimContent = content.replace(/\s+/g, " ");
                //同时转帖原文
                var forwardRoot = $('#check_forwardroot').attr('checked');
                //发送原作者
                var replayRoot = false;
                var syncFoward = $('#sync_forward').attr('checked');

                ajaxSaveReplyOnWall($("#hidden_cid").val(), $("#hidden_cuno").val(), '', '', '', trimContent, forwardRoot, replayRoot, syncFoward, replyDom, function(paramObj, replyDom) {
                    replyBiz.commonreply(paramObj, replyCallback.loadreplyOnBlogCallback, replyCallback.postReplyOnWallCallback, replyDom);
                });
            }
        },
        bindPostReReplyOnWall:function(replyDom) {
            var pid = replyDom.attr('data-pid');
            var replyArea = $('#rereply_area_' + pid);
            if (replyArea.size() == 0) {
                var rcid = $('#hidden_rcid').val();
                var rdomain = $('#hidden_rdomain').val();
                var rname = $('#hidden_rname').val();
                var pid = replyDom.attr('data-pid');
                var puno = replyDom.attr('data-puno');
                var pscreenName = replyDom.attr('data-pname');
                var cid = replyDom.attr('data-cid');
                var cuno = replyDom.attr('data-cuno');

                var isForward = rcid != null && rcid.length > 0;
                var forwardCheckHtml = '';
                if (isForward) {
                    forwardCheckHtml = '<p><input id="rereply_replyroot_' + pid + '" class="checktext" type="checkbox"  ><label for="checkbox">同时评论给原文作者' +
                            '<a href="http://' + rdomain + '.' + joyconfig.DOMAIN + '" name="atLink" title="' + rname + '">' + rname + '</a>' +
                            '</label></p>';
                }

                var html = '<div class="blog_discusson" id="rereply_area_' + pid + '" style="display:none">' +
                        '<div class="commentbox clearfix">' +
                        '<div class="commentbox clearfix">' +
                        '<textarea class="commenttext" id="rereply_content_' + pid + '" type="text">回复@' + pscreenName + '：</textarea>' +
                        '<a class="submitbtn" id="submit_rereply_' + pid + '"><span>回复</span></a>' +
                        '</div>' +
                        '<div class="related clearfix">' +
                        '<div id="rereply_image_' + pid + '" class="transmit_pic clearfix">' +
                        '<div class="commenface"><a href="javascript:void(0);" title="表情" id="replyContmood_re_' + pid + '"></a></div>' +
                        '<div name="reply_image_icon" class="t_pic" style="display: block;">' +
                        '<a href="javascript:void(0)" class="t_pic1">图片</a>' +
                        '</div>' +
                        '<div class="t_pic_more" name="reply_image_icon_more" style="display: none;">' +
                        '<a data-cid="rereply_image_' + pid + '" name="reply_upload_img" href="javascript:void(0)" class="t_pic1" title="图片">图片</a>' +
                        '<a data-cid="rereply_image_' + pid + '" name="reply_upload_img_link" href="javascript:void(0)" class="t_more" title="链接">链接</a></div>' +
                        '</div>' +
                        '<div>' +
                        '<p><input id="rereply_forwardroot_' + pid + '" class="checktext" type="checkbox"><label for="checkbox">同时转发到我的博客</label></p>' +
                        forwardCheckHtml +
                        '</div>'
                '</div>' +
                '</div>';

                replyDom.parent().parent().after(html);

                $('#submit_rereply_' + replyDom.attr('data-pid')).die().live('click', function() {
                    var pid = replyDom.attr('data-pid');
                    var puno = replyDom.attr('data-puno');
                    var pscreenName = replyDom.attr('data-pname');
                    var cid = replyDom.attr('data-cid');
                    var cuno = replyDom.attr('data-cuno');

                    var replyContent = $('#rereply_content_' + pid).val();
                    var trimContent = replyContent.replace(/\s+/g, " ");
                    var forwardroot = $('#rereply_forwardroot_' + pid).attr('checked');

                    var replyroot = false;
                    if ($('#rereply_replyroot_' + pid).size()) {
                        replyroot = $('#rereply_replyroot_' + pid).attr('checked');
                    }

                    var syncFoward = false;
                    ajaxSaveReplyOnWall(cid, cuno, pid, puno, pscreenName, trimContent, forwardroot, replyroot, syncFoward, replyDom, function(paramObj, replyDom) {
                        replyBiz.commonreply(paramObj, replyCallback.loadreplyOnWallCallback, replyCallback.postReplyOnWallCallback, replyDom);
                    })
                });

//                $("#rereply_content_" + pid).parent().css("background", "#fff");

                $("#rereply_content_" + pid).die().live("keydown", function(event) {
                    common.joymeCtrlEnter(event, $('#submit_rereply_' + pid));
                })
                $('#rereply_content_' + pid).AutoHeight({maxHeight:200,minHeight:24}).css('font-size', '12px');

                var te = new TextareaEditor(document.getElementById('rereply_content_' + pid));
                $('#replyContmood_re_' + pid).die().live('click', function() {
                    var config = {
                        allowmultiple:true,
                        isremovepop:true,
                        isfocus:true
                    };
                    moodBiz.docFaceBindOnWall($(this), 'rereply_content_' + pid, config, te);
                });
                replyArea = $('#rereply_area_' + pid);
            }

            if (replyArea.is(':hidden')) {
                if (Sys.ie == '7.0') {
                    $('.blog_discusson').fadeOut(function() {
                        replyArea.fadeIn();
                        common.cursorPosition._setCurPos($('#rereply_content_' + pid).val().length, $('#rereply_content_' + pid)[0]);
                    });
                } else {
                    $('.blog_discusson').slideUp(function() {
                        replyArea.slideDown();
                        common.cursorPosition._setCurPos($('#rereply_content_' + pid).val().length, $('#rereply_content_' + pid)[0]);
                    });
                }
            } else {
                Sys.ie == '7.0' ? replyArea.fadeOut() : replyArea.slideUp();
            }
        },
        bindPostReplyDelOnList:function(cid, cuno, replyid, triggerDom) { //删除评论的方法->列表
            joymealert.confirm({offset:'',tipLayer:true,text:'确定删除该评论？',submitFunction:function() {
                        var paramObj = {replyid:replyid ,cid:cid,cuno:cuno}
                        replyBiz.deleteReply(paramObj, replyCallback.delReplyOnListCallback, triggerDom);
                    }});
        },
        bindPostReplyDelOnBlog:function(cid, cuno, replyid) {
            joymealert.confirm({offset:'',tipLayer:true,text:'确定删除该评论？',submitFunction:function() {
                        var paramObj = {replyid:replyid ,cid:cid,cuno:cuno}
                        replyBiz.deleteReply(paramObj, replyCallback.delReplyOnBlogCallback);
                    }});

        },
        bindPostReplyDelOnWall:function(cid, cuno, replyid) { //删除评论的方法->列表
            joymealert.confirm({offset:'',tipLayer:false,text:'确定删除该评论？',submitFunction:function() {
                        var paramObj = {replyid:replyid ,cid:cid,cuno:cuno}
                        replyBiz.deleteReply(paramObj, replyCallback.delReplyOnWallCallback);
                    }});
            var afterBodyTop = $("#previewBox").data('bodyTop');
            $("#joymeconfirm_").css('top', afterBodyTop + $(window).height() / 2 + 'px')
        },
        togglereReplyOnReplyReceive:function(reReplyDom) {
            var replyPostDom = reReplyDom.parent().parent().parent().find(".mycomment_c");
            if (replyPostDom.is(':visible')) {
                var privilegeObj = ajaxverify.verifyPrivilege(true);
                if (privilegeObj.status_code == '-1') {
                    loginBiz.maskLoginByJsonObj(privilegeObj);
                    return;
                } else if (privilegeObj.status_code != '1') {
                    var alertOption = {text:privilegeObj.msg,tipLayer:true,textClass:"tipstext"};
                    joymealert.alert(alertOption);
                    return;
                }
            }

            if (replyPostDom.size() > 0) {
                $(".mycomment_c").slideUp();
                showReReplyOnReceiveHtml(replyPostDom, replyPostDom.children().children().children());
            } else {
                $(".mycomment_c").slideUp();
                var showReHtml = '<dd class="mycomment_c" style="display: none">' +
                        '<div class="discusson">' +
                        '<span class="corner"></span>' +
                        '<div class="commentbox clearfix">' +
                        '<textarea class="commenttext" style="font-family:Tahoma, \'宋体\'; heigth:24px;font-size: 12px" id="reply_textarea_' + reReplyDom.attr("data-rid") + '">回复@' + reReplyDom.attr("data-nick") + ':</textarea>' +
                        '<a class="submitbtn" id="subreply_' + reReplyDom.attr("data-rid") + '" data-cid="' + reReplyDom.attr("data-cid") + '" data-cuno="' + reReplyDom.attr("data-cuno") + '" data-rid="' + reReplyDom.attr("data-rid") + '" data-runo="' + reReplyDom.attr("data-runo") + '" data-pname="' + reReplyDom.attr("data-nick") + '"><span>回复</span></a>' +
                        '</div>' +
                        '<div class="related clearfix">' +
                        '<div id="rereply_image_' + reReplyDom.attr("data-rid") + '" class="transmit_pic clearfix">' +
                        '<a class="commenface" id="reply_mood_' + reReplyDom.attr("data-rid") + '" href="javascript:void(0)" title="表情"></a> ' +
                        '<div name="reply_image_icon" class="t_pic" style="display: block;">' +
                        '<a href="javascript:void(0)" class="t_pic1">图片</a>' +
                        '</div>' +
                        '<div class="t_pic_more" name="reply_image_icon_more" style="display: none;">' +
                        '<a data-cid="rereply_image_' + reReplyDom.attr("data-rid") + '" name="reply_upload_img" href="javascript:void(0)" class="t_pic1" title="图片">图片</a>' +
                        '<a data-cid="rereply_image_' + reReplyDom.attr("data-rid") + '" name="reply_upload_img_link" href="javascript:void(0)" class="t_more" title="链接">链接</a></div>' +
                        '</div>' +
                        '<div>' +
                        '</div>' +
                        '</dd>';
                reReplyDom.parent().parent().append(showReHtml);
//                $("#reply_textarea_" + reReplyDom.attr("data-rid")).parent().css("background", "#fff");
                var replyPostDom = reReplyDom.parent().parent().parent().find(".mycomment_c");

                showReReplyOnReceiveHtml(replyPostDom, replyPostDom.children().children().children());
                $("#subreply_" + reReplyDom.attr("data-rid")).die().live('click', function() {

                    var content = $(this).parent('div').find('textarea').val();
                    var trimContent = content.replace(/\s+/g, " ");


                    var cid = $(this).attr("data-cid");
                    var cuno = $(this).attr("data-cuno");
                    var pid = $(this).attr("data-rid");
                    var puno = $(this).attr("data-runo");
                    var pscreenName = $(this).attr("data-pname");
                    var forwardRoot = false;
                    var replyRoot = false;
                    var syncForward = false;
                    ajaxSaveReply(cid, cuno, pid, puno, pscreenName, trimContent, forwardRoot, replyRoot, syncForward, $(this), function(paramObj, replyDom) {
                        replyBiz.commonreply(paramObj, replyCallback.loadreplyCallback, replyCallback.postReReplyOnReceiveCallback, replyDom);
                    });
                })

                livemood("reply_mood_" + reReplyDom.attr("data-rid"), "reply_textarea_" + reReplyDom.attr("data-rid"));
//                replyCenterfun(rDom.attr('data-rid'), 'reply_textarea_');
//                reReplyLiveSubFun($('#subreply_' + rDom.attr("data-rid")));
//                bindReplyMood(rDom.attr("data-rid"));
                //为评论框绑定自适应高度方法
                $('#reply_textarea_' + reReplyDom.attr("data-rid")).AutoHeight({maxHeight:200});
                window.autoAt({id:"reply_textarea_" + reReplyDom.attr("data-rid"),ohterStyle:'line-height:22px'});
            }
        },
        bindDelReplyOnReceiveList:function(delDom) {
            joymealert.confirm({offset:'',tipLayer:true,text:'确定删除该评论？',submitFunction:function() {
                        var paramObj = {
                            replyid:delDom.attr("replyId"),
                            cid:delDom.attr("cid"),
                            cuno:delDom.attr("cuno")
                        }
                        replyBiz.deleteReply(paramObj, replyCallback.delReplyOnListCallback);
                    }});
        }
    };

    var replyBiz = {
        getreplyobject:function(dom, cid, cuno, loadingCallback, callback) {
            $.ajax({
                        type: "POST",
                        url: "/json/reply/getblogreplys",
                        data: {cid:cid,cuno:cuno},
                        success: function(req) {
                            var resultMsg = eval('(' + req + ')');
                            callback(dom, cid, cuno, resultMsg)
                        },
                        beforeSend:function() {
                            if (loadingCallback != null) {
                                loadingCallback(dom, cid);
                            }
                        }
                    });
        },
        commonreply:function(paramObj, loadingCallback, callback, replyDom, triggerDom) {
            var params = parseReplyParams(paramObj);
            if (paramObj.sync != null) {
                params.sync = paramObj.sync;
            }
            if (window.replyLock) {
                return false;
            }

            $.ajax({
                        url: "/json/reply/publish",
                        type:'post',
                        data:params,
                        success:function(req) {
                            var jsonObj = eval('(' + req + ')');
                            callback(jsonObj, paramObj, replyDom, triggerDom)
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
        deleteReply:function(paramObj, callback, triggerDom) {
            $.post("/json/reply/delete", paramObj, function(req) {
                var jsonObj = eval('(' + req + ')');
                callback(jsonObj, paramObj.replyid, paramObj.cid, triggerDom);
            });
        }
    };
    var replyCallback = {
        loadreplyCallback:function(replyDom) {
            replyDom.attr('class', 'loadbtn').html('<span><em class="loadings"></em>发布中…</span>');
        } ,
        subreplyCallback : function(jsonObj, paramObj, replyDom, triggerDom) {
            replyDom.attr('class', 'submitbtn').html('<span>评论</span>');

            if (jsonObj.status_code == '1') {

                //alert
                var reReplyAlertText = '';
                if (jsonObj.billingStatus) {
                    reReplyAlertText = "评论成功，" + jsonObj.billingMsg;
                } else {
                    reReplyAlertText = "评论成功";
                }
                var alertOption = {text:reReplyAlertText,tipLayer:true,callbackFunction:moodBiz.hideFace};
                joymealert.alert(alertOption);
                if (triggerDom != undefined) {
                    if (triggerDom.html() == "评论") {
                        triggerDom.html('评论(<span name="ia_header_num_reply_' + jsonObj.result[0].content.contentId + '">0</span>)');
                    }
                }
                //修改计数
                common.increaseCountByDom($('[name=hot_num_' + jsonObj.result[0].content.contentId + ']'), 1);
                common.increaseCountByDom($('span[name=ia_header_num_reply_' + jsonObj.result[0].content.contentId + ']'), 1);
                //common.increaseCountByDom($('span[name=ia_header_num_hot_' + jsonObj.result[0].content.contentId + ']'), 1);
                if (paramObj.replyroot) {
                    common.increaseCountByDom($('[name=hot_num_' + jsonObj.result[0].content.rootContentId + ']'), 1);
                }

                //拼装返回的html
                var hotHtml = '';
                var replyHtml = '';
                var hotTabObj = $('a[name=interactionTab][data-cid=' + jsonObj.result[0].content.contentId + '][data-itype=hot]');
                var replyTabObj = $('a[name=interactionTab][data-cid=' + jsonObj.result[0].content.contentId + '][data-itype=reply]');
//                if (hotTabObj.attr('class') == 'weight') {
//                    hotHtml = dataHtmlGenerator.replyData(jsonObj.result[0], false, triggerDom);
//                } else {
//                    hotHtml = dataHtmlGenerator.replyData(jsonObj.result[0], true, triggerDom);
//                }

                if (replyTabObj.attr('class') == 'weight') {
                    replyHtml = dataHtmlGenerator.replyData(jsonObj.result[0], false, triggerDom);
                } else {
                    replyHtml = dataHtmlGenerator.replyData(jsonObj.result[0], true, triggerDom);
                }

                if ($('#cont_hot_empty_' + paramObj.cid).length == 0) {
                    $('#ia_cont_' + paramObj.cid + '_hot').prepend(hotHtml);
                } else {
                    $('#cont_hot_empty_' + paramObj.cid).replaceWith(hotHtml);
                }

                $('#ia_reply_post_' + paramObj.cid).after(replyHtml);
                var iaHtmlId = 'cont_cmt_list_' + jsonObj.result[0].interaction.interactionId;
                if (hotTabObj.attr('class') == 'weight' || replyTabObj.attr('class') == 'weight') {
                    var isIE7 = window.navigator.userAgent.indexOf('MSIE 7.0') > 0;
                    if (isIE7) {
                        $('[name=' + iaHtmlId + ']').fadeIn();
                    } else {
                        $('[name=' + iaHtmlId + ']').slideDown();
                    }
                }

                //如果是回复
                var screenText = "";
                if (paramObj.screenName != undefined && paramObj.screenName.length > 0) {
                    screenText = "回复@" + paramObj.screenName + ":";
                }
                replyDom.prev().val(screenText).css('height', '24px').css('font-size', '12px');
                $("#reply_comment_" + paramObj.pid).slideUp();

                //评论图片
                var replyUploadId = "reply_image_" + paramObj.cid;
                if (paramObj.pid != null && paramObj.pid != '') {
                    replyUploadId = "rereply_image_" + paramObj.pid;
                }
                resetReplyUpload(replyUploadId);
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
                var alertOption = {text:"回复失败",tipLayer:true,textClass:"tipstext"};
                joymealert.alert(alertOption);
            }
        },
        loadreplyOnBlogCallback : function(replyDom) {
            replyDom.replaceWith('<a id="reply_submit_later" class="loadbtn fr"><span><em class="loadings"></em>发布中…</span></a>')
        },
        postReplyOnBlogCallback : function(resultMsg, param, replyDom) {
            if (param.pid == '') {
                $("#reply_submit_later").replaceWith('<a id="reply_submit" class="submitbtn fr"><span>评论</span></a>');
            } else {
                $("#reply_submit_later").replaceWith('<a id="submit_rereply_' + param.pid + '" class="submitbtn fr"><span>回复</span></a>');
            }


            if (resultMsg.status_code != '1') {
                var alertOption = {text:resultMsg.msg,tipLayer:false,textClass:"tipstext"};
                joymealert.alert(alertOption);
                return false;
            }

            //回复收起
            $("#rereply_area_" + param.pid).slideUp();
            $("#rereply_content_" + param.pid).val("回复@" + param.screenName + "：");
            var insertBlogReplyList = function() {
                var replyInfo = resultMsg.result[0];

                var html = dataHtmlGenerator.replyDataOnBlog(resultMsg.result[0]);

//                if ($('#ia_tab_hot').attr('class') == 'weight' || $('#ia_tab_reply').attr('class') == 'weight') {
                $('#div_ia_tab').after(html);
                var isIE7 = window.navigator.userAgent.indexOf('MSIE 7.0') > 0;
                if (isIE7) {
                    $('#cont_reply_' + replyInfo.interaction.interactionId).fadeIn();
                } else {
                    $('#cont_reply_' + replyInfo.interaction.interactionId).slideDown();
                }
//                }


                $('#reply_content').val('');//清空评论框
                $("#reply_num").find('b').text(tipsText.comment.blog_reply_length); //恢复评论框可输入字数初始值
                //计数
                if ($.trim($("#ia_tab_reply").text()) == "评论") {
                    $("#ia_tab_reply").html('评论(<span name="reply_num_' + $("#hidden_cid").val() + '">0</span>)');
                    $("#noComments").hide();
                }
                common.increaseCountByDom($('span[name=reply_num_' + replyInfo.content.contentId + ']'), 1);
                //评论图片
                var replyUploadId = "reply_image_" + param.cid;
                if (param.pid != null && param.pid != '') {
                    replyUploadId = "rereply_image_" + param.pid;
                }
                resetReplyUpload(replyUploadId);

            }

            var alertText = '';
            if (resultMsg.billingMsg != undefined) {
                alertText = '评论成功,' + resultMsg.billingMsg + resultMsg.msg;
            } else {
                alertText = '评论成功' + resultMsg.msg;
            }
            var alertOption = {text:alertText,tipLayer:true,callbackFunction:insertBlogReplyList,forclosed:false};
            joymealert.alert(alertOption);
        },
        loadreplyOnWallCallback :function(replyDom) {
            var pid = replyDom.attr("data-pid");
            $("#submit_rereply_" + pid).attr('class', 'loadbtn fr').html('<span><em class="loadings"></em>发布中…</span>');
        },
        postReplyOnWallCallback : function(resultMsg, param, replyDom) {

            var pid = param.pid;
            //回复

            if (param.pid == '') {
                $("#reply_submit_later").replaceWith('<a id="reply_submit" class="submitbtn fr"><span>评论</span></a>');
            } else {
                $("#submit_rereply_" + param.pid).replaceWith('<a id="submit_rereply_' + param.pid + '" class="submitbtn fr"><span>回复</span></a>');
            }

//            if (pid != null && pid != '') {
//                $("#submit_rereply_" + pid).attr('class', 'submitbtn fr').html('<span>回复</span>');
//            } else {
//                replyDom.attr('class', 'submitbtn fr').html('<span>评论</span>');
//            }

            $("#rereply_area_" + pid).slideUp();
            $("#rereply_content_" + pid).val("回复@" + param.screenName + "：");
            if (resultMsg.status_code != '1') {
                var alertOption = {text:resultMsg.msg,tipLayer:false,textClass:"tipstext"};
                joymealert.alert(alertOption);
                return false;
            }
            var insertBlogReplyList = function() {
                var replyInfo = resultMsg.result[0];
                var id = 'cont_reply_' + replyInfo.interaction.interactionId;

                //当前标签是评论或者热点，生成的reply display 是none;
                var hotHtml = '';
                var replyHtml = '';
                if ($('#ia_tab_hot').attr('class') == 'weight') {
                    hotHtml = dataHtmlGenerator.replyDataOnWall(replyInfo, false);
                } else {
                    hotHtml = dataHtmlGenerator.replyDataOnWall(replyInfo, true);
                }

                if ($('#ia_tab_reply').attr('class') == 'weight') {
                    replyHtml = dataHtmlGenerator.replyDataOnWall(replyInfo, false);
                } else {
                    replyHtml = dataHtmlGenerator.replyDataOnWall(replyInfo, true);
                }

                $('#data_area_hot').prepend(hotHtml);
                $('#data_area_reply').prepend(replyHtml);

                $('#reply_content').val('');
                if ($('#ia_tab_hot').attr('class') == 'weight' || $('#ia_tab_reply').attr('class') == 'weight') {
                    var isIE7 = window.navigator.userAgent.indexOf('MSIE 7.0') > 0;
                    if (isIE7) {
                        $('[name=' + id + ']').fadeIn();
                    } else {
                        $('[name=' + id + ']').slideDown();
                    }
                }
                if ($.trim($("#ia_tab_reply").text()) == "评论") {
                    $("#ia_tab_reply").html('评论(<span id="ia_header_num_reply">0</span>)');
                    $("#noComments").hide();
                }
                common.increaseCount('ia_header_num_reply', 1);
                common.increaseCount('ia_header_num_hot', 1);
                $("#reply_num>b").text(tipsText.comment.blog_reply_length);
            }
            //评论图片
            var replyUploadId = "reply_image_" + param.cid;
            if (param.pid != null && param.pid != '') {
                replyUploadId = "rereply_image_" + param.pid;
            }
            resetReplyUpload(replyUploadId);
            var alertText = '';
            if (resultMsg.billingMsg != undefined) {
                alertText = '评论成功,' + resultMsg.billingMsg + resultMsg.msg;
            } else {
                alertText = '评论成功' + resultMsg.msg;
            }
            var alertOption = {text:alertText,tipLayer:false,callbackFunction:insertBlogReplyList,forclosed:false};
            joymealert.alert(alertOption);
            if (!(navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.split(";")[1].replace(/[ ]/g, "") == "MSIE7.0")) {
                var styleStr = $("#joymealert_").attr('style');
                $("#joymealert_").attr('style', styleStr.replace('absolute', 'fixed'));
            }
        },
        postReReplyOnReceiveCallback:function(jsonObj, paramObj, replyDom) {
            replyDom.attr('class', 'submitbtn').html('<span>评论</span>');
            replyDom.prev().find('textarea').val("回复@" + replyDom.attr('data-pname') + ":").css('height', '24px').css('font-size', '12px');
            if (jsonObj == null || jsonObj.status_code == '-1') {
                loginBiz.maskLoginByJsonObj(jsonObj);
            }

            if (jsonObj.status_code == '1') {
                var reReplyAlertText = '';
                if (jsonObj.billingStatus) {
                    reReplyAlertText = "回复成功" + jsonObj.billingMsg;
                } else {
                    reReplyAlertText = "回复成功";
                }
                var alertOption = {text:reReplyAlertText,tipLayer:true,callbackFunction:moodBiz.hideFace};
                joymealert.alert(alertOption);

                //增加评论数
                common.increaseCount('feedbacknum_' + paramObj.cid, 1)
                replyDom.parent().parent().parent().slideToggle('fast');
                replyDom.prev().val("回复@" + paramObj.screenName + "：");
            } else {
                var alertOption = {text:"回复失败",tipLayer:true,textClass:"tipstext"};
                joymealert.alert(alertOption);
            }

        },
        delReplyOnListCallback:function(resultMsg, replyid, cid, triggerDom) {
            $("div[name=cont_cmt_list_" + replyid + "]").slideUp(function() {
                $(this).remove();
            });
            common.increaseCountByDom($("span[name=ia_header_num_hot_" + cid + "]"), -1);
            common.increaseCountByDom($("span[name=ia_header_num_reply_" + cid + "]"), -1);
            if ($("span[name=ia_header_num_reply_" + cid + "]").text() == "0") {
                triggerDom.html('评论')
            }
        } ,
        delReplyOnBlogCallback:function(resultMsg, replyid, cid) {
            $("#cont_reply_" + replyid).slideUp(function() {
                $(this).remove();
                common.increaseCountByDom($("span[name=hot_num_" + cid + "]"), -1);
                common.increaseCountByDom($("span[name=reply_num_" + cid + "]"), -1);
                if ($("span[name=reply_num_" + cid + "]").text() == "0") {
                    $("#ia_tab_reply").html('评论');
                    if ($("#div_ia_tab").next().length > 0) {
                        $("#noComments").show();
                    } else {
                        $("#div_ia_tab").after('<div id="noComments" class="noComments">目前没有评论，欢迎你发表观点</div>')
                    }
                }
            });

        },
        delReplyOnWallCallback:function(resultMsg, replyid, cid) {
            $("div[name=cont_reply_" + replyid + "]").slideUp(function() {
                $(this).remove();
                if ($("#data_area_hot").children().size() == 0) {
                    $("#ia_tab_reply").text("评论")
                    $("#noComments").show();
                }
                common.increaseCountByDom($("#ia_header_num_hot"), -1);
                common.increaseCountByDom($("#ia_header_num_reply"), -1);
            });
        }
    };
    //提交reply
    var subreplyOnlist = function(paramObj, replyDom, triggerDom) {
        replyBiz.commonreply(paramObj, replyCallback.loadreplyCallback, replyCallback.subreplyCallback, replyDom, triggerDom);
    }
    //注册表情
    var replylivemood = function(cid, textareaID) {
        var moodBiz = require('../biz/mood-biz');
        $("#replyContmood_" + cid).live('click', function() {
            var te = new TextareaEditor(document.getElementById(textareaID));
            var config = {
                allowmultiple:true,
                isremovepop:true,
                isfocus:true
            };
            moodBiz.docFaceBindOnReply($(this), textareaID, config, te);
        });
    }

    var livemood = function(domid, textareaID) {
        var te = new TextareaEditor(document.getElementById(textareaID));
        var moodBiz = require('../biz/mood-biz');
        $("#" + domid).live('click', function() {
            var config = {
                allowmultiple:true,
                isremovepop:true,
                isfocus:true
            };
            moodBiz.docFaceBindOnReply($(this), textareaID, config, te);
        });
    }

    var wallLivemood = function(dom) {
        var moodBiz = require('../biz/mood-biz');
        $("#reply_mood").live('click', function() {
            var te = new TextareaEditor(document.getElementById('reply_content'));
            var config = {
                allowmultiple:true,
                isremovepop:true,
                isfocus:true
            };
            moodBiz.docFaceBindOnWall($(this), 'reply_content', config, te);
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

    //点击提交事件
    var ajaxSaveReply = function(cid, cuno, pid, puno, pscreenName, replyContent, forwardRoot, replayRoot, syncForward, replyDom, checkSuccessCallback, triggerDom) {
        var paramObj = {cid:cid,
            cuno:cuno,
            pid:pid,
            puno:puno,
            replycontent:replyContent,
            forwardroot:forwardRoot,
            screenName:pscreenName,
            replyroot:replayRoot,
            sync:syncForward,
            onWall:false
        };

        if (paramObj.replycontent == null || $.trim(paramObj.replycontent).length == 0) {
            if (paramObj.pid != null && paramObj.pid != '') {
                //rereply_image_
                if ($("#rereply_image_" + paramObj.pid).find("input[type=hidden]").length > 0) {
                    paramObj.replycontent = tipsText.comment.image_reply;
                }
            } else {
                //reply_image_
                if ($("#reply_image_" + paramObj.cid).find("input[type=hidden]").length > 0) {
                    paramObj.replycontent = tipsText.comment.image_reply;
                }
            }

        }

        if (checkContext(paramObj.replycontent, paramObj.onWall)) {
            common.checkAtSize(paramObj.replycontent, function(resultMsg) {
                common.checkAtNicks(paramObj.replycontent, function(resultMsg) {
                    checkSuccessCallback(paramObj, replyDom, triggerDom);
                }, function(resultMsg) {
                    showAtNicksTips(paramObj, replyDom, resultMsg, checkSuccessCallback)
                });
            }, function(resultMsg) {
                showAtSizeTips(paramObj, function(resultMsg) {
                    checkSuccessCallback(paramObj, replyDom, triggerDom);
                }, replyDom, resultMsg);
            });
        }
    }
    var ajaxSaveReplyOnWall = function(cid, cuno, pid, puno, pscreenName, replyContent, forwardRoot, replayRoot, syncForward, replyDom, checkSuccessCallback) {
        var paramObj = {cid:cid,
            cuno:cuno,
            pid:pid,
            puno:puno,
            replycontent:replyContent,
            forwardroot:forwardRoot,
            screenName:pscreenName,
            replyroot:replayRoot,
            sync:syncForward,
            onWall:true
        };
        if (paramObj.replycontent == null || $.trim(paramObj.replycontent).length == 0) {
            if (paramObj.pid != null && paramObj.pid != '') {
                //rereply_image_
                if ($("#rereply_image_" + paramObj.pid).find("input[type=hidden]").length > 0) {
                    paramObj.replycontent = tipsText.comment.image_reply;
                }
            } else {
                //reply_image_
                if ($("#reply_image_" + paramObj.cid).find("input[type=hidden]").length > 0) {
                    paramObj.replycontent = tipsText.comment.image_reply;
                }
            }

        }
        if (checkContext(paramObj.replycontent, paramObj.onWall)) {
            common.checkAtSize(paramObj.replycontent, function(resultMsg) {
                common.checkAtNicks(paramObj.replycontent, function(resultMsg) {
                    checkSuccessCallback(paramObj, replyDom);
                }, function(resultMsg) {
                    showAtNicksTips(paramObj, replyDom, resultMsg, function(resultMsg) {
                        checkSuccessCallback(paramObj, replyDom);
                    })

                });
            }, function(resultMsg) {
                showAtSizeTips(paramObj, function(resultMsg) {
                    checkSuccessCallback(paramObj, replyDom);
                }, replyDom, resultMsg);
            });
        } else {
            $("#reply_content").val(paramObj.replycontent);
            $("#reply_content")
            $("#reply_content").focus();
            var te = new TextareaEditor(document.getElementById('reply_content'));
            var len = paramObj.replycontent.length;
            te.sesetSelectionRanget(len, len);
            var styleStr = $("#joymealert_").attr('style');
            $("#joymealert_").attr('style', styleStr.replace('absolute', 'fixed'));
        }
    }


    //todo 这倆方法可以合并？
//    var toggleReplyhtml = function(id, reReplyPrefix, rDom, reReplyPrefixTextArea) {
//        var isIE7 = window.navigator.userAgent.indexOf('MSIE 7.0') > 0;
//        if (isIE7) {
//            $("#" + reReplyPrefix + id).fadeToggle('fast', function() {
//                showReplyLayerFocus(id, reReplyPrefixTextArea, rDom)
//            });
//        } else {
//            $("#" + reReplyPrefix + id).slideToggle('fast', function() {
//                showReplyLayerFocus(id, reReplyPrefixTextArea, rDom)
//            });
//        }
//    }

    //显示层方法
    var showInteractionHtml = function(id, Prefix, dom, showFunction) {
        var isIE7 = window.navigator.userAgent.indexOf('MSIE 7.0') > 0;
        if ($("#" + Prefix + id).length == 0) {
            if (isIE7) {
                $("a[name=" + Prefix + id + "]").fadeToggle(function() {
                    if (showFunction != null) {
                        showFunction()
                    }
                });
            } else {
                $("a[name=" + Prefix + id + "]").slideToggle(function() {
                    if (showFunction != null) {
                        showFunction()
                    }
                });
            }
        } else {
            if (isIE7) {
                $("#" + Prefix + id).fadeToggle(function() {
                    if (showFunction != null) {
                        showFunction()
                    }
                });
            } else {
                $("#" + Prefix + id).slideToggle(function() {
                    if (showFunction != null) {
                        showFunction()
                    }
                });
            }
        }
    }


    var showReplyLayerFocus = function(id, Prefix, dom) {
//        $("html,body").animate({scrollTop:dom.offset().top - parseInt($(window).height() / 2 + 100)}, function() {
//            var ele = $("#" + Prefix + id).parent('div');
        //common.joymeShake(ele, 'red', 2);
        //获得焦点
        focusreplyhtml(id, Prefix);
//        });

    }

    var focusreplyhtml = function(id, prefix) {
        var obj = document.getElementById(prefix + id);
        if (obj == null) {
            return;
        }
        try {
            obj.focus()
        } catch(e) {
        }
        ;
        var len = obj.value.length;
        if (document.selection) {
            var sel = obj.createTextRange();
            sel.moveStart('character', len);
            sel.collapse();
            sel.select();
        } else if (typeof  obj.selectionStart == 'number' && typeof  obj.selectionEnd == 'number') {
            obj.selectionStart = obj.selectionEnd = len;
        }
    }
    var focusreplyhtmlByDom = function(focusDom) {
        var obj = focusDom[0];
        if (obj == null) {
            return;
        }
        try {
            obj.focus()
        } catch(e) {
        }
        ;
        var len = 0;
        if (obj.value.length != "undefined") {
            len = obj.value.length
        }
        if (document.selection) {
            var sel = obj.createTextRange();
            sel.moveStart('character', len);
            sel.collapse();
            sel.select();
        } else if (typeof  obj.selectionStart == 'number' && typeof  obj.selectionEnd == 'number') {
            obj.selectionStart = obj.selectionEnd = len;
        }
    }

    var showReReplyOnReceiveHtml = function(jqdom, focusDom) {
        if (jqdom.is(':visible')) {
            jqdom.slideUp();
        } else {
            jqdom.slideDown(function() {
                $(this).children().show();
                focusreplyhtmlByDom(focusDom);
            });
        }
    }

    //超过@次数提示层
    var checkContext = function(replycontent, isOnWall) {
        var alertOption = {};
        if (replycontent == null || $.trim(replycontent).length == 0) {
            alertOption = {text:tipsText.comment.blog_pl_content_notnull,tipLayer:false,textClass:"tipstext"};
            joymealert.alert(alertOption);
            if (isOnWall) {
                var styleStr = $("#joymealert_").attr('style');
                $("#joymealert_").attr('style', styleStr.replace('absolute', 'fixed'));
            }
            return false;
        } else if (common.getInputLength(replycontent) > tipsText.comment.blog_reply_length) {
            alertOption = {text:tipsText.comment.blog_pl_content_maxlength,tipLayer:false,textClass:"tipstext"};
            joymealert.alert(alertOption);
            if (isOnWall) {
                var styleStr = $("#joymealert_").attr('style');
                $("#joymealert_").attr('style', styleStr.replace('absolute', 'fixed'));
            }
            return false;
        } else if (! ajaxverify.verifyPost(replycontent)) {
            alertOption = {text:tipsText.comment.blog_pl_illegl,tipLayer:false,textClass:"tipstext"};
            joymealert.alert(alertOption);
            if (isOnWall) {
                var styleStr = $("#joymealert_").attr('style');
                $("#joymealert_").attr('style', styleStr.replace('absolute', 'fixed'));
            }
            return false;
        }
        return true;
    }
    var showAtSizeTips = function (paramObj, callback1, replyDom, resMsg, focusDom) {
        var offSet = replyDom.parent().offset();
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
        focusDom.focus();
    }
    var showAtNicksTips = function (paramObj, replyDom, resMsg, transfercallback, focusDom) {
        var list = resMsg.result;
        var str = '';
        $.each(list, function(i, val) {
            str = str + '“' + val + '”、';
        });

        var offSet = replyDom.parent().offset();
        var tips = '您在评论@到的用户' + str.substring(0, str.length - 1) + '，不存在。';

        var confirmOption = {
            confirmid:"atNicksTips",
            offset:"Custom",
            offsetlocation:[offSet.top,offSet.left + Math.floor((replyDom.parent().width() - 229) / 2)],
            title:"确认继续发送",
            text:tips,
            width:229,
            submitButtonText:'继续发送',
            submitFunction:function() {
                transfercallback(paramObj, replyDom);
            },
            cancelButtonText:'返回修改',
            cancelFunction:function() {
                focusDom.focus();
            }};
        joymealert.confirm(confirmOption);

    }

    var slideCount = function (jqDom, text, contentId, billingStatus) {
        if (billingStatus) {
            var discoveryTop = 0;
            discoveryTop = $("#previewBox").data('bodyTop') || 0;
            $("body").append("<div id='lide_area_" + contentId + "' style='position: absolute;color:#FF8773; display:none; z-index:10000;font-size:12px;'>" + text + "</div>");
            $("#lide_area_" + contentId).css({left:jqDom.offset().left + "px",top:jqDom.offset().top + discoveryTop - 20 + "px"}).show().animate({
                        top:'-=7',
                        opacity:'0'
                    }, 1000, function() {
                $(this).remove();
            });
        }
    }
    var animateToReply = function(dom) {
        $("#reply_content").focus();
        setTimeout(function() {
            $("#sea_area").scrollTop($(".area").height() + 30);
        }, 200)

    }
    var animateToHot = function() {
        $("#ia_tab_hot").click();
        setTimeout(function() {
            $("#sea_area").scrollTop($(".area").height() + $("#post_reply").height() + 50);
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


    var syncObj = {
        showSyncArea:function(dom) {
            var offset = dom.offset();
            var syncDivId = "sync_info_mask";
            var afterBodyTop = $("#previewBox").data('bodyTop') || 0;
            var popConfig = {
                pointerFlag : true,//是否有指针
                tipLayer : false,//是否遮罩
                offset:"Custom",
                containTitle : true,//包含title
                containFoot : false,//包含footer
                offsetlocation:[offset.top - 83 + afterBodyTop,offset.left - 50],
                forclosed:true,
                popwidth:240 ,
                allowmultiple:true
            };
            if ($("#" + syncDivId).size() > 0) {
                pop.resetOffsetById(popConfig, syncDivId);
            } else {
                var resultMsg = ''
                $.ajax({
                            url:'/json/profile/sync/getprovider' ,
                            type:'POST',
                            async:false,
                            success:function(data) {
                                resultMsg = eval('(' + data + ')');
                            }
                        });
                var syncHtml = syncObj.initBindSyncHtml(resultMsg);

                var htmlObj = new Object();
                htmlObj['id'] = syncDivId;
                htmlObj['html'] = syncHtml;
                htmlObj['title'] = '您的内容将被同步到以下社区'
                pop.popupInit(popConfig, htmlObj);
            }
        },
        initBindSyncHtml:function(resultMsg) {
            var html = '';
            html += '<div class="tbcon">';
            if (resultMsg.result == 0) {
                html += '<p>您还没有绑定任何网站，无法同步</p>';

            } else {
                html += '<ul class="clearfix">';
                $.each(resultMsg.result, function(i, val) {
                    html += '<li class="bind_' + val.code + '"></li>';
                })
                html += '</ul>';
            }
            html += '<a href="/profile/customize/bind" target="_blank">修改同步设置>></a></div>';
            return html;
        } ,
        showSyncAreaOnWall:function(dom) {
            if ($("#sync_info_mask").length > 0) {
                $("#sync_info_mask").show();
            } else {
                if (joyconfig.joyuserno != "") {
                    var resultMsg = ''
                    $.ajax({
                                url:'/json/profile/sync/getprovider' ,
                                type:'POST',
                                async:false,
                                success:function(data) {
                                    resultMsg = eval('(' + data + ')');
                                }
                            });
                    var syncHtml = syncObj.initBindSyncHtml(resultMsg);
                    var domHtml = '<div style="width: 240px; position:absolute;" id="sync_info_mask" class="pop">' +
                            '<div class="hd clearfix">' +
                            '您的内容将被同步到以下社区<a href="javascript:void(0)" id="closePop_sync_info_mask" class="close"></a>' +
                            '</div>' +
                            '<div class="bd clearfix">' + syncHtml +
                            '</div>' +
                            '</div>';
                    dom.parent().parent().append('<div style="position:relative;" id="sync_mask"></div>');
                    $("#sync_mask").append(domHtml);
                    $("#sync_info_mask").css({"left":$("#sync_info_mask").width() - 32 + "px","top":5 - $("#sync_info_mask").height() - 30 + "px"})
                    $("#closePop_sync_info_mask").die().live('click', function() {
                        $("#sync_info_mask").css('display', 'none');
                    });
                }
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
    var discornerFun = function(interactionType, cid) {
        var discorner = $("#ia_area_" + cid).find('div:eq(0)').children();
        discorner.css('margin-right', '78px');
    }

    var parseReplyParams = function(paramObj) {
        var params = {cid:paramObj.cid,cuno:paramObj.cuno,pid:paramObj.pid,puno:paramObj.puno,forwardroot:paramObj.forwardroot,replyroot:paramObj.replyroot,replycontent:paramObj.replycontent};
        if (paramObj.pid != null && paramObj.pid != '') {
            //rereply_image_
            if ($("#rereply_image_" + paramObj.pid).find("input[type=hidden]").length > 0) {
                var imageB = $("#rereply_image_" + paramObj.pid).find("input[name=picurl_b]").val();
                var imageM = $("#rereply_image_" + paramObj.pid).find("input[name=picurl_m]").val();
                var imageS = $("#rereply_image_" + paramObj.pid).find("input[name=picurl_s]").val();
                var imageSS = $("#rereply_image_" + paramObj.pid).find("input[name=picurl_ss]").val();
                var w = $("#rereply_image_" + paramObj.pid).find("input[name=w]").val();
                var h = $("#rereply_image_" + paramObj.pid).find("input[name=h]").val();
                params["imgs"] = imageS;
                params["imgss"] = imageSS;
                params["imgb"] = imageB;
                params["imgm"] = imageM;
                params["w"] = w;
                params["h"] = h;
            }
        } else {
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
        }

        return params;
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
