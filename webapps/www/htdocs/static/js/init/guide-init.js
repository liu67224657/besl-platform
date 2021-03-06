define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var card = require('../page/card');
    var header = require('../page/header');
    var followCallback = require('../page/followcallback');
    var home = require('../page/home');
    var postChat = require('../page/post-chat');
    var postText = require('../page/post-text');
    var postOption = require('../page/post-option');
    var contentlist = require('../page/contentlist');
    require('../common/jquery.lazyload')($);
    require('../common/jquery.cookie')($);
    var guide = require('../page/guide');
    var recommend = require('../page/recommenduser');
    var common = require('../common/common');
    window.blogContent = {content:'', audio:null, video:null, image:new Array()};//设置全局对象
    window.imgLocaL = 0;
    var windowTop = document.body.scrollTop + document.documentElement.scrollTop;
    $().ready(function () {
        $(".set_tx_div").click(function () {
            window.location.href = "/profile/customize/headicon";
        })
        $("#set_tx_div").live('mouseenter mouseleave', function (event) {
            if (event.type == "mouseenter") {
                $("#txbg").show();
                $("#set_tx").show();
            } else if (event.type == "mouseleave") {
                $("#txbg").hide();
                $("#set_tx").hide();
            }
        });
        $("#set_tx,#txbg").click(function() {
            window.location.href = "/profile/customize/headicon";
        })
        $(".lazy").scrollLoading();
        var chrome = window.navigator.userAgent.indexOf("Chrome") >= 0 ? true : false;
        if (chrome) {
            window.scrollTo(0, windowTop);
        }
        recommend.homeload(1);

        common.jmPostConfirm(null);
    });
    (function () {
//        if (Sys.ie != "6.0") {
//            beginnersGuide();
//        }
        card.bindCard(followCallback.followCallBack, followCallback.unFollowOnList);
        postChat.postChatInit(postOption.guideOption);
        postText.posttextinit(postOption.guideOption);
        contentlist.initBind();
        guide.guideFocusInit();
        guide.closeRecommondInit();
        header.noticeSearchReTopInit();

        home.showAllJoinedGroup();
        home.addGroup();
        home.postBox('#div_post_con');
    })();
//    function beginnersGuide() {
//        var guideStep = $.cookie('registerGuide');
//        if (typeof guideStep != 'undefined' && guideStep != null) {
//            if (guideStep == 0) {
//                $.cookie('registerGuide', null, {path:'/',domain: joyconfig.DOMAIN});
//                $.cookie('registerGuide', 1, {expires:7, path:'/',domain:joyconfig.DOMAIN});
//            }
//            guideStep = $.cookie('registerGuide');
//            window.scrollTo(0, 0);
//            document.body.parentNode.style.overflowY = "hidden";
//            $('body').append('<div class="fugai" id="fugai"></div>')
//            $('#fugai').css({'height':$(document).height() + 'px', 'width':$(window).width() + 'px'});
//            switch (parseInt(guideStep)) {
//                case 1:
//                    step01();
//                    break;
////                case 2:
////                    step02();
////                    break;
//                case 3:
//                    step03();
//                    break;
//                case 4:
//                    step04();
//                    break;
//            }
//        }
//    }
//
//    function step01() {
//        $('body').append('<div class="step05" id="step05"><a href="javascript:void(0)"></a></div>');
//        $('#step05').css('left', $('#content').offset().left - 31 + 'px');
//        $("#step05 a").live('click', function () {
//            $(this).parent().remove();
//            $.cookie('registerGuide', null, {path:'/',domain: joyconfig.DOMAIN});
//            $.cookie('registerGuide', 3, {expires:7, path:'/',domain:joyconfig.DOMAIN});
//            $.ajax({
//                        url:'/json/home/insertgrouptip',
//                        type:'post'
//                    });
//            step03();
//        });
//    }
//
//    function step02() {
//        $('body').append('<div class="step02" id="step02"><a href="javascript:void(0)"></a></div>');
//        $('#step02').css('left', $('.headb').offset().left + 282 + 'px');
//        $("#step02 a").one('click', function () {
//            $(this).parent().remove();
//            $.cookie('registerGuide', null, {path:'/',domain:joyconfig.DOMAIN});
//            $.cookie('registerGuide', 3, {expires:7, path:'/',domain:joyconfig.DOMAIN});
//            step03()
//        });
//    }
//
//    function step03() {
//        $('body').append('<div class="step03" id="step03"><a href="javascript:void(0)"></a></div>');
//        //$(window).width()
//        $('#step03').css('left', $('.headb').offset().left + 356 + 'px');
//        $("#step03 a").one('click', function () {
//            $(this).parent().remove();
//            $.cookie('registerGuide', null, {path:'/',domain:joyconfig.DOMAIN});
//            $.cookie('registerGuide', 4, {expires:7, path:'/',domain:joyconfig.DOMAIN});
//            step04();
//        });
//    }
//
//    function step04() {
//        $('body').append('<div class="step04" id="step04"><a href="javascript:void(0)"></a></div>');
//        $('#step04').css('left', $('.side_bd_line').offset().left - 83 + 'px');
//        $("#step04 a").one('click', function () {
//            $.cookie('registerGuide', null, {path:'/',domain:joyconfig.DOMAIN});
//            document.body.parentNode.style.overflowY = "scroll";
//            $(this).parent().remove();
//            $('#fugai').remove();
//        })
//    }

    require.async('../../third/swfupload/swfupload');
    require.async('../../third/swfupload/swfupload.queue');
    require.async('../../third/swfupload/fileprogress');
    require.async('../../third/ckeditor/ckeditor');
    require.async('../common/google-statistics');
    require.async('../common/bdhm')
});