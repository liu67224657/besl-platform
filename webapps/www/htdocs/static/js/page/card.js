define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');

    var cardBiz = require('../biz/card-biz');
    var common = require('../common/common');
    var followBiz = require('../biz/follow-biz');
    var loginBiz = require('../biz/login-biz');
    var message = require('./message');
    var joymealert = require('../common/joymealert');

    var cardLoadTimeoutId;
    var cardHideTimeoutId;
    var card = {
        bindCard:function(followCallback, unFollowCallback) {
            $("a[name=atLink]").live("mouseenter",
                    function() {
                        var nickName = $(this).attr("title");
                        card.initCard($(this), 'user_desc_' + nickName, nickName, followCallback, unFollowCallback);
                    }).live("mouseleave",
                    function() {
                        card.hideCard();
                    }).live("click", function() {
                        getAtLink($(this).attr("title"));
                    });
        },
        bindCardUnClick:function(followCallback, unFollowCallback) {
            $("a[name=atLink]").live("mouseenter",
                    function() {
                        var nickName = $(this).attr("title");
                        card.initCard($(this), 'user_desc_' + nickName, nickName, followCallback, unFollowCallback);
                    }).live("mouseleave",
                    function() {
                        card.hideCard();
                    });
        },
        initCard:function(cardSource, cardId, nickName, followCallBack, unFollowCallback) {
            var _this = this;
            clearTimeout(cardHideTimeoutId);
            cardLoadTimeoutId = setTimeout(function() {
                if ($('#' + cardId).length == 0) {
                    cardBiz.loadCardByNick(cardSource, cardId, nickName, cardCallback.loadingCallback, cardCallback.commonCallback, followCallBack, unFollowCallback);
                    $('#' + cardId + ',#joymeconfirm_' + cardId).die().live('mouseenter',
                            function() {
                                isOut = false;
                                clearTimeout(cardHideTimeoutId);
                            }).live('mouseleave', function() {
                                _this.hideCard();
                            });
                    showCard(cardSource, cardId);
                } else {
                    showCard(cardSource, cardId);
                }
            }, 500);
        },

        hideCard:function() {
            clearTimeout(cardLoadTimeoutId);
            if ($(".user_card:visible").length > 0) {
                cardHideTimeoutId = setTimeout(function() {
                    $(".user_card:visible").fadeOut();
                    $('div[id^=joymeconfirm_user_desc_]').remove();
                }, 500);
            }
        }
    }

    var cardCallback = {
        commonCallback:function(resultMsg, cardId, cardFollowCallBack, cardUnFollowCallBack) {
            if (resultMsg.status_code == '1') {
                generatorCardhtml(resultMsg, cardId, cardFollowCallBack, cardUnFollowCallBack);
            } else {
                generatorBanCardHtml(resultMsg, cardId);
            }
        },
        loadingCallback:function(cardSource, cardId) {
            var html = generatorLoadingCardHtml(cardSource, cardId);
            $(html).appendTo($("body"));
            showCard(cardSource, cardId);
        }
    }

    function generatorLoadingCardHtml(cardSource, cardId) {
        var html = '<div id="' + cardId + '" class="pop user_card" style="position: absolute; z-index: 18005; width:338px;">' +
                '<div class="bd">' +
                '<div id="' + cardId + '_corner" class="cornerdowm"></div>' +
                '<div id="' + cardId + '_cardloading" class="mpload"></div></div></div>';
        return html;
    }

    function generatorBanCardHtml(resultMsg, cardId) {
        var html = '<div class="publicuse"><p>' +
                resultMsg.msg +
                '</p></div>';
        $('#' + cardId + '_cardloading').replaceWith(html);
    }


    function generatorCardhtml(resultMsg, cardId, cardFollowCallBack, cardUnFollowCallBack) {
        var socialProfile = resultMsg.result[0];
        var imgHtml = '';
        if (socialProfile.profile.blog.headIconSet != null && socialProfile.profile.blog.headIconSet.iconSet != null
                && socialProfile.profile.blog.headIconSet.iconSet.length > 0) {
            for (var i = 0; i < socialProfile.profile.blog.headIconSet.iconSet.length; i++) {
                var icon = socialProfile.profile.blog.headIconSet.iconSet[i];
                if (icon.validStatus) {
                    imgHtml = '<a class="personface" href="' + joyconfig.URL_WWW + '/people/' + socialProfile.profile.blog.domain + '">' +
                            '<img height="58" width="58" src="' + common.parseSimg(icon.headIcon, joyconfig.DOMAIN) + '">' +
                            '</a>';
                    break;
                }
            }

        }
        if (imgHtml.length == 0) {
            var picurl = joyconfig.URL_LIB + '/static/theme/default/img/head_is_s.jpg';
            if (socialProfile.profile.detail.sex == '1') {
                picurl = joyconfig.URL_LIB + '/static/theme/default/img/head_boy_s.jpg';
            } else if (socialProfile.profile.detail.sex == '0') {
                picurl = joyconfig.URL_LIB + '/static/theme/default/img/head_girl_s.jpg';
            }
            imgHtml = '<a class="personface" href="' + joyconfig.URL_WWW + '/people/' + socialProfile.profile.blog.domain + '">' +
                    '<img height="58" width="58" src="' + picurl + '">' +
                    '</a>';
        }
        var detailHtml = ''
        if (socialProfile.profile.detail != null) {
            if (socialProfile.profile.detail.sex == null) {

            } else if (socialProfile.profile.detail.sex == '1') {
                detailHtml = '<b class="nan"></b>';
            } else if (socialProfile.profile.detail.sex == '0') {
                detailHtml = '<b class="nv"></b>';
            }

            if (socialProfile.profile.detail.provinceId == null) {

            } else if (socialProfile.profile.detail.provinceId != null) {
                detailHtml += common.getRegionByid(socialProfile.profile.detail.provinceId);
            }
            if (socialProfile.profile.detail.cityId == null) {

            } else if (socialProfile.profile.detail.cityId != null) {
                detailHtml += ' ' + common.getRegionByid(socialProfile.profile.detail.cityId);
            }
        }

        var relationHtml = '';
        if (socialProfile.profile.blog.uno == joyconfig.joyuserno) {

        } else if (socialProfile.relation == null || socialProfile.relation.srcStatus.code == 'n' && socialProfile.relation.destStatus.code == 'n') {
            //陌生人
            relationHtml = '<a class="add_attention" href="javascript:void(0);"></a>';
        } else if (socialProfile.relation.srcStatus.code == 'y' && socialProfile.relation.destStatus.code == 'n') {
            //以关注
            relationHtml = '<a class="attentioned" href="javascript:void(0);"></a>';
        } else if (socialProfile.relation.srcStatus.code == 'y' && socialProfile.relation.destStatus.code == 'y') {
            //相互关注
            relationHtml = '<a class="attentionedall" href="javascript:void(0);"></a>';
        } else if (socialProfile.relation.srcStatus.code == 'n' && socialProfile.relation.destStatus.code == 'y') {
            //粉丝
            relationHtml = '<a class="add_attention_ok" href="javascript:void(0);"></a>';
        }


        var headIcons = '';
        if (socialProfile.profile.blog.headIconSet != null) {
            var iconSet = socialProfile.profile.blog.headIconSet.iconSet;
            var j = 0;
            for (var i = 0; i < iconSet.length; i++) {
                var icon = iconSet[i];
                if (icon.validStatus) {    //2012/3/6  edit by xyj
                    var head = common.parseSimg(icon.headIcon, joyconfig.DOMAIN);
                    j++;
                    if (j > 1) {
                        headIcons += '<a target="_blank" href="' + joyconfig.URL_WWW + '/people/' + socialProfile.profile.blog.domain + '"><img src="' + head + '" width="40" height="40"></a>';
                    }
                }
            }
        }

        var verifyType = '';  //认证
        var description = '';
        if (socialProfile.profile.detail.verifyType == null || socialProfile.profile.detail.verifyType.code == 'n') {
            if (!socialProfile.profile.blog.auditStatus.blogDescIllegal) {
                if (socialProfile.profile.blog.description != null && socialProfile.profile.blog.description.length > 0) {
                    description = socialProfile.profile.blog.description;
                }
            }
        } else {
            verifyType = '<a target="_blank" href="' + joyconfig.URL_WWW + '/people/' + socialProfile.profile.blog.domain + '" class="' + socialProfile.profile.detail.verifyType.code + 'vip" title="' + joyconfig.viptitle[socialProfile.profile.detail.verifyType.code] + '" ></a>';
            description = socialProfile.profile.detail.verifyDesc;
        }

        if (description != null && description.length > 0) {
            description = description.length > 20 ? description.substring(0, 19) + '…' : description;
        }
        var screenName = socialProfile.profile.blog.screenName.length > 10 ? socialProfile.profile.blog.screenName.substring(0, 10) + '…' : socialProfile.profile.blog.screenName;
        var html = '<div class="div_user clearfix">' +
//                coinHtml +
                imgHtml +
                '<ul><li><a href="' + joyconfig.URL_WWW + '/people/' + socialProfile.profile.blog.domain + '" class="author">' + screenName + verifyType + '</a></li>' +
                '<li>' + detailHtml + '</li>' +
                '<li>' +
                '<a href="javascript:void(0)" name="cardfollowfans" data-link="/social/follow/list/' + socialProfile.profile.blog.uno + '"><em>关注</em>' + socialProfile.profile.sum.focusSum + '</a>' +
                '<a href="javascript:void(0)" name="cardfollowfans" data-link="/social/fans/list/' + socialProfile.profile.blog.uno + '"><em>粉丝</em>' + socialProfile.profile.sum.fansSum + '</em></a> ' +
                '<a href="' + joyconfig.URL_WWW + '/people/' + socialProfile.profile.blog.domain + '"><em>帖子</em>' + socialProfile.profile.sum.blogSum + '</a>' +
                '</li>' +
                '</ul></div>' +
                '<p class="summary">' + description + '</p>' +
                '<div class="manytx clearfix">' + headIcons + '</div>' +
                '<div class="mp_ft">' +
                '<div class="group"><a href="javascript:void(0)" id="card_send_' + socialProfile.profile.blog.uno + '">私信</a></div>' +
                '<div id="card_relation_' + socialProfile.profile.blog.uno + '" class="condition">' +
                relationHtml +
                '</div></div>';
        $('#' + cardId + '_cardloading').replaceWith(html);
        bindCardEvent(cardId, socialProfile, cardFollowCallBack, cardUnFollowCallBack);
    }

    function bindCardEvent(cardId, socialProfile, cardFollowCallBack, cardUnFollowCallBack) {
        $('#card_relation_' + socialProfile.profile.blog.uno).die().live('click', function() {
            if (loginBiz.checkLogin($(this))) {
                var followClass = $(this).children('a')[0].className;
                if (followClass == 'add_attention_ok' || followClass == 'add_attention') {
                    followBiz.ajaxFocus(joyconfig.joyuserno, socialProfile.profile.blog.uno, cardFollowCallBack);
                } else {
                    var confirmUnfocus = function() {
                        followBiz.ajaxUnFocus(socialProfile.profile.blog.uno, cardUnFollowCallBack);
                    }
                    var offSet = $('#' + cardId).offset();
                    var tips = '确定要取消' + socialProfile.profile.blog.screenName + '吗?'
                    var confirmOption = {
                        confirmid:cardId,
                        offset:"Custom",
                        offsetlocation:[offSet.top,offSet.left],
                        text:tips,
                        width:229,
                        submitButtonText:'确 定',
                        submitFunction:confirmUnfocus,
                        cancelButtonText:'取 消',
                        cancelFunction:null};
                    joymealert.confirm(confirmOption);
                }
            }
        });

        $('#card_send_' + socialProfile.profile.blog.uno).die().live('click',
                function() {
                    if (loginBiz.checkLogin($(this))) {
                        $('#' + cardId).hide();
                        message.initMessageMask(socialProfile.profile.blog.screenName);
                    }
                });

        $("a[name=cardfollowfans]").live("click", function() {
            var url = joyconfig.URL_WWW + $(this).attr("data-link");
            if (joyconfig.joyuserno == '') {
                var loginOption = {
                    referer:url
                };
                loginBiz.maskLogin(loginOption);
            } else {
                window.location.href = url;
            }
        });
    }

    function showCard(cardSource, cardId) {
        var cardOffset = $(cardSource).offset();
        var distance = cardOffset.top - $(document).scrollTop();
        $(".user_card").hide();
        if (distance > 176) {
            if (($(window).width() - cardOffset.left) < $("#" + cardId).width()) {
                $('#' + cardId + '_corner').removeClass().addClass("cornerdown_r");
                $('#' + cardId).css("display", "block").css({top:cardOffset.top - $('#' + cardId).height() - 6 + "px",left:cardOffset.left - $("#" + cardId).width() + $(cardSource).width() + "px"}, 'fast');
            } else {
                $('#' + cardId + '_corner').removeClass().addClass("cornerdowm");
                $('#' + cardId).css("display", "block").css({top:cardOffset.top - $('#' + cardId).height() - 6 + "px",left:cardOffset.left + "px"}, 'fast');
            }
        } else {
            if (($(window).width() - cardOffset.left) < $("#" + cardId).width()) {
                $('#' + cardId + '_corner').removeClass().addClass("corner_r");
                $('#' + cardId).css("display", "block").css({top:cardOffset.top + $(cardSource).height() + 6 + "px",left:cardOffset.left - $("#" + cardId).width() + $(cardSource).width() + "px"}, 'fast');
            } else {
                $('#' + cardId + '_corner').removeClass().addClass("corner");
                $('#' + cardId).css("display", "block").css({top:cardOffset.top + $(cardSource).height() + 6 + "px",left:cardOffset.left + "px"}, 'fast');
            }
        }
    }

    function getAtLink(nick) {
        cardBiz.getAtLink(nick, getAtLinkCallback);
    }

    function getAtLinkCallback(jsonObj) {
        if (jsonObj.status_code == "1") {
            var result = jsonObj.result[0];
            //跳转到博客页
            window.location.href = joyconfig.URL_WWW + '/people/' + result.domain;
        }
    }

    return card;
});










