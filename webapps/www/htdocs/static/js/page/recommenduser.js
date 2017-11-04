define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var common = require('../common/common');
    var follow = require('./follow');
    var followCallback = require('./followcallback');
    var page = null;
    var recommend = {
        homeload:function(pageNo) {
            biz.loadRecommend(pageNo, function(resultMsg) {
                if (resultMsg.status_code == '2') {
                    window['recommendInterval'] = setInterval(function() {
                        biz.loadRecommend(pageNo, callback.homeCallback);
                    }, 1000 * 3);
                    return;
                }
                callback.homeCallback(resultMsg);
            });


            bindEvent.bindLinkMore();
            bindEvent.bindUninterested();
            bindEvent.toggleUninterestedLink();
            follow.ajaxFollowSimpleBind(function(focusuno, resMsg) {
                followCallback.followSimpleCallBack(focusuno, resMsg);
            });
        }
    }

    var biz = {
        loadRecommend:function (pageNo, callback) {
            $.post("/json/social/recommend/list", {p:pageNo}, function(req) {
                var resultMsg = eval('(' + req + ')')
                callback(resultMsg);
            });
        },
        uninterested:function(pageNo, rowId, destUno, callback) {
            $.post("/json/social/recommend/unlike", {p:pageNo,duno:destUno,rowid:rowId}, function(req) {
                var resultMsg = eval('(' + req + ')')
                callback(destUno, resultMsg);
            });
        }
    }

    var bindEvent = {
        bindLinkMore:function () {
            $('#link_recommend_reason').live('click', function() {
                var uno = $(this).attr('data-uno');
                var arrowSpan = $('#span_more_arrow_' + uno);
                arrowSpan.attr('class', arrowSpan.hasClass('more') ? 'more_down' : 'more');
                $('#recommend_reason_' + uno).fadeToggle();
            });
        },

        bindUninterested:function () {
            $('a[name=link_uninterested_recommend]').live('click', function() {
                var uno = $(this).attr('data-uno');
                var rowid = $(this).attr('data-rowid');
                var pageNo = page.curPage;
                biz.uninterested(pageNo, rowid, uno, callback.uninterestedCallback);
            });
        },

        toggleUninterestedLink:function() {
            $('dl[id^=dl_recommend_user_]').live('mouseenter',
                    function() {
                        var uno = $(this).attr('data-uno');
                        $('#link_uninterested_recommend_' + uno).css('display', '')
                    }).live('mouseleave', function() {
                        var uno = $(this).attr('data-uno');
                        $('#link_uninterested_recommend_' + uno).css('display', 'none');
                    });
        }


    }

    var callback = {
        homeCallback:function(resultMsg) {
            if (resultMsg.status_code == '2') {
                return;
            }

            if (resultMsg.status_code == '1') {
                clearInterval(window['recommendInterval']);

                $('#area_recommend').replaceWith(html.generatorHomeHtml(resultMsg));

                page = resultMsg.result[0].page;
                $('#change_recommend').die().live('click', function() {
                    var pageNo = page.curPage;
                    if (page.lastPage) {
                        pageNo = 1;
                    } else {
                        pageNo = pageNo + 1;
                    }
                    biz.loadRecommend(pageNo, callback.homeCallback)
                });
            }
        },
        uninterestedCallback:function(destUno, resultMsg) {
            if (resultMsg.status_code == '1') {
                $('#dl_recommend_user_' + destUno).fadeOut(function() {
                    $(this).remove();
                    if (resultMsg.result != null && resultMsg.result.length > 0) {
                        var profile = resultMsg.result[0];
                        $('#list_user_recommend').append(html.generatorUserHtml(profile, 2, false));
                        $('#dl_recommend_user_' + profile.uno).fadeIn();

                    }
                    if ($('dl[id^=dl_recommend_user_]').length <= 0) {
                        $('#area_recommend').remove();
                    }
                });
            }

        }
    }


    var html = {
        generatorUserHtml:function(profile, rowid, isdiplay) {
            var detail = '';
            if (profile.destProfileList == null || profile.destProfileList.length == 0) {
                var detailStr = '';
                if (profile.desc != null && profile.desc.length > 0) {
                    detailStr = (common.strLen(profile.desc) > 9 ? common.subStr(profile.desc, 8) + '…' : profile.desc);
                }
                detail += '<p class="W_moreup" id="link_recommend_reason" data-uno="' + profile.uno + '">' + detailStr + '</p>'
            } else {
                var name = '';
                for (var i = 0; i < profile.destProfileList.length; i++) {
                    name += profile.destProfileList[i].screenName;
                    if (i < profile.destProfileList.length - 1) {
                        name += '、';
                    }
                }
                var nameString = name;
                if (profile.destProfileSize > 5) {
                    nameString += ' 等' + profile.destProfileSize + '人与他互相关注';
                }

                detail += '<p class="W_moreup" id="link_recommend_reason" data-uno="' + profile.uno + '">有' + profile.destProfileSize + '个共同好友<span id="span_more_arrow_' + profile.uno + '" class="' + (rowid > 0 ? 'more_down' : 'more') + '" ></span></p>';
                detail += '<p class="detail" style="' + (rowid > 0 ? 'display:none' : '') + '"  id="recommend_reason_' + profile.uno + '">' + nameString + '</p>';
            }

            var screenNameStr = (common.strLen(profile.screenName) > 8 ? common.subStr(profile.screenName, 7) + '…' : profile.screenName);
            var verifyType = '';
            if (profile.verifyType != null && profile.verifyType.code != 'n') {
                //todo
                verifyType = ' <a href="' + joyconfig.URL_WWW + '/people/' + profile.blogDomain + '" class="' + profile.verifyType.code + 'vip" title="' + joyconfig.viptitle[profile.verifyType.code] + '"></a>';
            }

//            var headIcon = (profile.headIcon != null && profile.headIcon.length > 0) ? profile.headIcon : joyconfig.URL_LIB + '/static/theme/default/img/default.jpg';
            var headIcon = joyconfig.URL_LIB + '/static/theme/default/img/head_is_s.jpg';
            if (profile.headIcon != null && profile.headIcon.length > 0) {
                headIcon = profile.headIcon;
            } else if (profile.sex == '1') {
                headIcon = joyconfig.URL_LIB + '/static/theme/default/img/head_boy_s.jpg';
            } else if (profile.sex == '0') {
                headIcon = joyconfig.URL_LIB + '/static/theme/default/img/head_girl_s.jpg';
            }
            var userHtml = '<dl style="' + (isdiplay ? '' : 'display:none') + '" id="dl_recommend_user_' + profile.uno + '" data-uno="' + profile.uno + '" class="interest_con clearfix">' +
                    '<dt class="personface"><a title="' + profile.screenName + '" name="atLink" href="' + joyconfig.URL_WWW + '/people/' + profile.blogDomain + '" class="tag_cl_left">' +
                    '<img width="58px" height="58px" src="' + headIcon + '"></a> </dt>' +
                    '<dd class="W_textb">' +
                    '<p class="name"><a href="' + joyconfig.URL_WWW + '/people/' + profile.blogDomain + '" class="author" title="' + profile.screenName + '" name="atLink">' + screenNameStr + '</a>' +
                    verifyType +
                    '</p>' +
                    '<p class="info"><a id="" data-uno="' + profile.uno + '" name="follow-simple" href="javascript:void(0)" class="add_attention"></a>' +
                    '<a href="javascript:void(0);" style="display:none" name="link_uninterested_recommend" id="link_uninterested_recommend_' + profile.uno + '" data-uno="' + profile.uno + '" data-rowid="' + rowid + '" class="nointerest">不感兴趣</a></p>' +
                    detail +
                    '</dd>' +
                    '</dl>';
            return  userHtml;
        },

        generatorHomeHtml:function(resultMsg) {
            page = resultMsg.result[0].page;
            var list = resultMsg.result[0].rows;
            if (list.length <= 0) {
                return '';
            }

            //title
            var changeLink = page.maxPage == 1 ? '' : '<a id="change_recommend" href="javascript:void(0)"  class="more">换一换</a>';
            var title = '<div class="side_hd">可能感兴趣的人' + changeLink + '</div>';

            var userHtml = '';
            $.each(list, function(i, val) {
                userHtml += html.generatorUserHtml(val, i, true)
            })
            var content = '<div class="side_bd side_pd"><div id="list_user_recommend" class="interest_panel">' + userHtml + '</div></div>';

            return '<div style="" id="area_recommend" class="side_item">' + title + content + '<div class="side_ft"></div></div>';
        }


    }

    return recommend;
});










