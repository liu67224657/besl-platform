/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-3
 * Time: 下午1:56
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    require('../common/jquery.lazyload')($);
    var card = require('../page/card');
    var header = require('../page/header');
    var followCallback = require('../page/followcallback');
    var home = require('../page/home');
    var postChat = require('../page/post-chat');
    var postText = require('../page/post-text');
    var postOption = require('../page/post-option');
    var contentlist = require('../page/contentlist');
    var guide = require('../page/guide');
    var scrollPage = require('../page/scrollpage');
    var recommend = require('../page/recommenduser');
    var common = require('../common/common');
    window.blogContent = {content:'',audio:null,video:null,image:new Array()};//设置全局对象
    window.imgLocaL = 0;
    window.memoContent = true;
    var windowTop = document.body.scrollTop + document.documentElement.scrollTop;
    (function() {
        header.noticeSearchReTopInit();
        $(window).scroll(function() {
            var pageNo = parseInt($("#hidden_pageNo").val());
            var scrollNo = parseInt($("#hidden_scrollNo").val());
            var totalRows = parseInt($("#hidden_totalRows").val());
            scrollPage.scrollPage(pageNo, totalRows, scrollNo, scrollPage.homeLoading, scrollPage.homescRollCallback);
        });
        card.bindCard(followCallback.followCallBack, followCallback.unFollowOnList);
        postChat.postChatInit(postOption.homeOption);
        postText.posttextinit(postOption.homeOption);
        contentlist.initBind();

        home.showAllJoinedGroup();
        home.addGroup();
        home.postBox('#div_post_area');
//        home.groupGuide($('#group_tip').val());

        common.jmPostConfirm(null);

        recommend.homeload(1);

        //新上引导 提示
        $("#hot-yd").live("click", function() {
            $(this).parent().parent().remove();
        });
        $("a").live('click', function() {
            $(this).blur();
        });
        guide.guideFocusInit();
        guide.closeRecommondInit();
    })();
    $().ready(function() { //lazylaod在谷歌下跳位置的处理
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
    })
    require.async('../../third/swfupload/swfupload');
    require.async('../../third/swfupload/swfupload.queue');
    require.async('../../third/swfupload/fileprogress');
    require.async('../../third/ckeditor/ckeditor');
    require.async('../common/google-statistics');
    require.async('../common/bdhm');

});