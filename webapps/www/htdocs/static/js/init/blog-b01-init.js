/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-3
 * Time: 下午1:56
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var card = require('../page/card');
    var header = require('../page/header');
    var common = require('../common/common');
    var joymealert = require('../common/joymealert');
    var contentOperate = require('../page/content-operate');
    var loginBiz = require('../biz/login-biz');
    var blog = require('../page/blog');
    var msg = require('../page/message');
    var atta = require('../page/atta');
    var follow = require('../page/follow');
    var surl = require('../page/surl');
    var category = require('../page/category');
    var followCallback = require('../page/followcallback');
    var ajaxverify = require('../biz/ajaxverify');
    var joymealert = require('../common/joymealert');
    var gamePrivacy = require('../page/gameprivacy');
    require('../common/atme')
    require('../common/jquery.cookie')($);
    require('../common/tips');
    require('../common/jquery.autotextarea')($);

    (function() {
        header.noticeSearchReTopInit();

        contentOperate.blogLiveHot();
        $('#check_forwardroot').click(function() {
            if ($(this).attr('checked')) {
                if (joyconfig.syncFlag) {
                    $('#sync_forward').removeAttr('disabled').attr('checked', true);
                }
            } else {
                $('#sync_forward').attr('disabled', 'disabled').attr('checked', false);
            }
        });

        surl.surlBind();
        //私信
        msg.bindSendMsg();
        //@Ta
        atta.attaBind();

        //加关注
        follow.ajaxFollowBind(followCallback.followCallbackOnBlog);
        follow.ajaxUnFollowBind(followCallback.unFollowOnBlog);
        follow.ajaxFollowSimpleBind(followCallback.followSimpleCallBack);

        loginBiz.bindMaskLoginOnBlog();

        $("body").die().live('mousedown', function() {
            if (window.isOut) {
                $("#mood").hide();
                isOut = true;
            }
        });

        $('#link_del_content').click(function() {
            var confirmOption = {
                text:'确定要删除该文章？',
                width:229,
                submitButtonText:'删 除',
                submitFunction:function () {
                    $('#del_content_form').submit();
                },
                cancelButtonText:'取 消',
                cancelFunction:null};
            joymealert.confirm(confirmOption);
        });

        //查看关注链接
        follow.followfanslink();

        card.bindCard(followCallback.followCallbackOnBlog, followCallback.unFollowOnBlog);
        //初始化右边
//        try {
//            $.post("/ajax/baike/loadtree", {cid:cid}, function(req) {
//                if (req.replace(/\s+/g, "") != "") {
//                    $('#div_load_baike').replaceWith(req);
//                    $('#tree_baike').fadeIn();
//                    setTreeMenu();
////                    setCookie()
//                }
//            });
//        } catch(e) {
//        }
        if ($('#select_game_privacy').size() > 0) {
            gamePrivacy.bindPrivacyOnBlog();
        }
        if ($('#select_magazine_privacy').size() > 0) {
            gamePrivacy.bindMagazinePrivacyOnBlog();
        }

        if ($('#input_search_word').size() > 0) {
            gamePrivacy.bindSuperGroupPrivacyOnBlog();
        }
        if ($('#adjust_point').size() > 0) {
            gamePrivacy.bindAdjustPotinOnBlog();
        }
        setTreeMenu();
    })();

//    require.async('http://static1.baifendian.com/service/zhaomi/zm_blog.js');

    require.async('../common/google-statistics');
    require.async('../common/bdhm');

//    window.viewedAlsoView = function (bdata) {
//        var divView = $("#bodyViewAlsoViewId");
//
//        if (bdata.rec_allfu[1] == "OK") {
//            var bfd_uno_cid = "";
//            $.each(bdata.rec_allfu[3], function(i, val) {
//                bfd_uno_cid = bfd_uno_cid + "" + val.uno + ":" + "" + val.cid + "|";
//            });
//            innerBfdPost(bfd_uno_cid, bdata.rec_vav[2], divView);
//        }
//        if (bdata.rec_vav[1] == "OK") {
//            var bfd_uno_cid = "";
//            $.each(bdata.rec_vav[3], function(i, val) {
//                bfd_uno_cid = bfd_uno_cid + "" + val.uno + ":" + "" + val.cid + "|";
//            });
//            innerBfdPost(bfd_uno_cid, bdata.rec_vav[2], divView);
//        }
//        $('#bodyViewAlsoViewId>li:last').addClass('class', 'item_no');
//    };
//
//    var innerBfdPost = function(bfddata, reqid, divView) {
//        $.post("/json/search/content/bfd", {'unocids': bfddata}, function(data) {
//            var result = eval('(' + data + ')');
//            if (result.status_code) {
//                var viewDivStr = "";
//                for (var i = 0; i < result.result.length; i++) {
//                    var rs = result.result[i];
//                    if (rs == null || rs.content == null) {
//                        continue;
//                    }
//                    var subj = rs.content.subject;
//                    viewDivStr += '<li><span>•</span><a target="_blank" href="' + joyconfig.URL_WWW + '/note/' + rs.content.contentId + '?srid=7.c' + '&req_id=' + reqid + '">' + subj + '</a></li>';
//                }
//                if (viewDivStr != null && viewDivStr != "") {
//                    divView.html(divView.html() + viewDivStr);
//                }
//
//                if ($("#bodyViewAlsoViewId").find("li").length > 2) {
//                    $("#viewAlsoViewId").slideDown();
//                    $('#lastdotted-line').css('display', '');
//                }
//            }
//        });
//    }

//    function setCookie() {
//        if ($.cookie('joymeknow') == 'know') {
//            return;
//        }
//        var html = '<div class="lovetips" style="position:absolute;">' +
//                '<div class="love_hd"></div>' +
//                '<div class="love_bd clearfix">' +
//                '<p>这里还有更多攻略哦!</p>' +
//                '<div class="iknow">知道了</div>' +
//                '</div>' +
//                '<div class="yicon_d"></div><div class="love_ft"></div>' +
//                '</div>';
//        $("body").append(html);
//        var tipsHeight = $('.lovetips').height();
//        $('.lovetips').css({'left':$("#tree_baike").offset().left + 'px','top':$("#tree_baike").offset().top - tipsHeight + 'px'})
//        $(".iknow").one('click', function() {
//            $(".lovetips").fadeOut().remove();
//            $.cookie('joymeknow', "know", {expires:7,path:'/'});
//        })
//    }

    function setTreeMenu() {
        $("a[id^=menu_]").bind('click', function() {
            var sonLine;
            var addTreeClass,delTreeClass;
            if ($(this).parent().is('h3')) {
                sonLine = $(this).parent().next();
                delTreeClass = 'guanbi'
                addTreeClass = ''
            } else {
                sonLine = $(this).next();
                delTreeClass = 'jia'
                addTreeClass = 'jian'
            }
            if (sonLine.is(":hidden")) {
                $(this).find('em').removeClass().addClass(addTreeClass)
                sonLine.slideDown();
            } else {
                $(this).find('em').removeClass().addClass(delTreeClass)
                sonLine.slideUp();
            }
        });
    }


});
function pointReasonFocus() {
    var pointReason = document.getElementById("point_reason").value;
    if (pointReason == '请输入加分理由...') {
        document.getElementById("point_reason").value = "";
    }
}

function pointReasonBlur() {
    var pointReason = document.getElementById("point_reason").value;
    if (pointReason == '') {
        document.getElementById("point_reason").value = "请输入加分理由...";
    }
}