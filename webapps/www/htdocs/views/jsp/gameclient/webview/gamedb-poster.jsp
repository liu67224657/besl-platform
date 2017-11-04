<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/jstllibs.jsp" %>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <title>${gameDB.gameName}</title>
    <link href="${URL_LIB}/static/theme/default/css/wap_common_poster.css" rel="stylesheet" type="text/css">
        <link href="${URL_LIB}/static/theme/default/css/gamedb-poster-yxfm2.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.11.0.min.js"></script>
    <%@ include file="/views/jsp/common/jsconfig.jsp" %>
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
    </script>
</head>
<body>
<div id="_sharebtn_status" style="display: none;">yes</div>
<div id="_title" style="display: none;">${gameDB.gameName}</div>
<div id="_desc" style="display: none;">${gameDB.gameName}&nbsp;&nbsp;${gameDB.gameDBCover.coverComment}</div>
<div id="_clientpic" style="display: none;">${gameDB.gameIcon}</div>
<div id="_share_task" style="display: none;">sharehotgame</div>
<div id="_share_url" style="display: none;">${short_url}</div>
<div id="_directid" style="display: none;">${gameDB.gameDbId}</div>

<div id="wrapper">
    <div class="bg">
        <img src="${gameDB.gameDBCover.coverPicUrl}" id="loadImg" style="display: none;">


        <%--
        <div class="btn_icon back_icon">
            <a href="javascript:void(0);"></a>
        </div>
        <div class="btn_icon share_icon">
            <a href="javascript:void(0);"></a>
        </div>
        --%>
        <div>
            <div class="box yx show">
                <div>
                    <cite class="yx-icon"></cite>
                </div>
            </div>
            <div class="box jp">
                <div>
                    <cite class="jp-icon"></cite>
                </div>
                <font>${author}&nbsp;<span>开测</span></font>
            </div>
            <div class="box zan">
                <div>
                    <div class="zan_cont">
                        <cite></cite>
                        <span class="zan_num" id="agreeNum">${gameDB.gameDBCover.coverAgreeNum}</span>
                    </div>
                    <font>已期待</font>
                </div>
            </div>
        </div>
        <div class="box-right active">
            <div>
                <div class="box-right-main">
                    <div class="yx-right-box">
                        <b class="yx-title-bg"></b>

                        <div class="yx-right-box-ttile"><span>${gameDB.gameDBCover.coverTitle}</span></div>
                        <div class="yx-right-box-main">
                            <h1>${gameDB.gameDBCover.coverComment}</h1>

                            <h2>期待理由:</h2>

                            <p class="string_cut">${gameDB.gameDBCover.coverDesc}</p>
                        </div>
                    </div>

                </div>
            </div>
        </div>
        <div class="bottom_ad">
            <c:if test="${downLoadUrl!=''}">
            <a href="${downLoadUrl}" class="bottom_ad_box">
                </c:if>
                <c:if test="${downLoadUrl==''}">
                <a href="javascript:void(0);" class="bottom_ad_box no-loadBtn">
                    </c:if>
                    <cite><img src="${gameDB.gameIcon}" alt=""></cite>

                    <h1>${gameDB.gameName}</h1>

                    <h2 class="string_cut1">${gameDB.downloadRecommend}</h2>
                    <span>立即下载</span>
                </a>
        </div>
    </div>
</div>
</body>

<script type="text/javascript">
    $(document).ready(function () {
        var h = $(window).height();
        $('.bg').height(h);
        var gameDbId = window.localStorage.getItem("gameid_${gameDB.gameDbId}");
        //存在
        if (typeof (gameDbId) != "undefined" && gameDbId != null) {
            $(".zan_cont").find("cite").addClass("active");
        }
        getImageURL();
        stringCut('string_cut', 56);
        stringCut('string_cut1', 12);


        $('.jp').on('touchstart', function () {
            $(this).find('cite').addClass('action');
            $(this).find('font').show();
            $('.box-right,.box-right>div,.box-right-main,.jp-right-box,.yx-right-box').css({'visibility': 'hidden'});
            $('.jp,.yx').removeClass('show');
            $('.jp-right-box').removeClass('a');
            $('.zan').removeClass('now').find('font').hide();
            $(this).addClass('now')
        })
        $('.yx').on('touchstart', function () {
            $('.box-right,.box-right>div,.box-right-main,.yx-right-box').css({'visibility': 'initial'});
            $('.jp').removeClass('show')
            $(this).addClass('show');
            $('.jp-right-box').removeClass('a')
            $('.zan,.jp').removeClass('now').find('font').hide();

            $('.box-right').addClass('active');
            $('.jp-right-box').css({'visibility': 'hidden'});
            $('.zan').find('cite').removeClass('action');
        })
        $('.zan').on('touchstart', function () {
            var gameDbId = window.localStorage.getItem("gameid_${gameDB.gameDbId}");
            var that = this;
            if (gameDbId == null) {
                $.ajax({
                    url: 'http://api.' + joyconfig.DOMAIN + "/joymeapp/gameclient/json/gamedb/agree?gameId=${gameDB.gameDbId}",
                    type: "get",
                    dataType: "json",
                    success: function (req) {
                        if (req.rs == 1) {
                            window.localStorage.setItem("gameid_${gameDB.gameDbId}", "${gameDB.gameDbId}");
                            $(that).find('cite').addClass('action');
                            $(that).find('font').show();
                            $('.box-right,.box-right>div,.box-right-main,.jp-right-box,.yx-right-box').css({'visibility': 'hidden'});
                            $('.jp,.yx').removeClass('show');
                            $('.jp').removeClass('now').find('font').hide();
                            $('.jp-right-box').removeClass('a');
                            $(".zan").addClass('now');
                            $(".zan_cont").find("cite").addClass("active");
                            var agreeNum = $("#agreeNum").html();
                            if (!isNaN(agreeNum)) {
                                $("#agreeNum").html(parseInt(agreeNum) + 1);
                            }
                        }
                    }
                });
            } else {
                $(this).find('cite').addClass('action');
                $(this).find('font').show();
                $('.box-right,.box-right>div,.box-right-main,.jp-right-box,.yx-right-box').css({'visibility': 'hidden'});
                $('.jp,.yx').removeClass('show');
                $('.jp').removeClass('now').find('font').hide();
                $('.jp-right-box').removeClass('a');
                $(this).addClass('now');
            }
        });

    });
    function stringCut(tar, len) {
        var target = $('.' + tar);
        var text = target.text();
        var text_len = text.length;
        if (text_len > len) {
            target.text(text.substr(0, len));
        }
    }


    function getImageURL() {
        var boxBg = $('.bg');
        var loadImg = document.getElementById("loadImg");
        var imgs = $("#loadImg").attr("src");
        var t = false;
        loadImg.onload = function () {
            $('.box,.box-right,.bottom_ad').css({'opacity': '1'});
            boxBg.css('background-image', 'url(' + imgs + ')');
            t = true;
        }
        if (!t) {
            var timePic = setInterval(function () {
                if (!t) {
                    $('.box,.box-right,.bottom_ad').css({'opacity': '1'});
                    boxBg.css('background-image', 'url(' + imgs + ')');
                    t = true;
                    clearInterval(timePic);
                }
            }, 50);
        }
    }

</script>
</html>