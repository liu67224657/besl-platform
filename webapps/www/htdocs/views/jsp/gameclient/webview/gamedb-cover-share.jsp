<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<html lang="en">
<meta charset="utf-8">
<meta content="width=device-width, initial-scale=1.0, user-scalable=no" name="viewport">
<meta content="yes" name="apple-mobile-web-app-capable">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<meta content="telephone=no" name="format-detection">
<meta name="description" content="${gameDB.recommendReason}"/>
<title>${gameDB.gameName}</title>
<link href="${URL_LIB}/static/theme/wap/css/wap_common.css" rel="stylesheet" type="text/css">
<link href="${URL_LIB}/static/theme/wap/css/yxfm.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>
<script type="text/javascript">
    window.addEventListener('DOMContentLoaded', function () {
        document.addEventListener('touchstart', function () {
            return false
        }, true)
    }, true);
</script>
<style>
    body, html {
        height: auto;
    }

    .box.now font {
        width: auto;
        margin-top: -24px;
        left: 40%;
        white-space: nowrap;
    }

    .floatBanner {
        position: absolute;
    }
</style>
<body>

<div id="wrapper">
    <!-- 手游画报-->
    <div class="floatBanner" style="display:block;top:0;z-index:99;height:45px;">
        <div class="box-sizing display">
            <div class="hb-dw-box">
                <i class="fl"><img src="${URL_LIB}/static/theme/wap/images/syhbcon.png" alt="" title=""/></i>

                <p class="fl">
                    <em>着迷玩霸</em>
                    <span>只要你着迷 陪你玩到底</span>
                </p>
            </div>
            <a href="javascript:void(0);" class="hn-dw-btn">免费下载</a>
        </div>
    </div>
    <div class="popup_box">
        <img src="${URL_LIB}/static/theme/wap/images/popup.jpg" alt="">
    </div>
    <div class="mark_box"></div>
    <!--手游画报==end -->

    <div class="bg" style="background-image:url(${gameDB.gameDBCover.coverPicUrl});background-size:100% auto;"><!--修改游戏封面切换图片背景-->
        <%--<div class="btn_icon back_icon">--%>
        <%--<a href="#"></a>--%>
        <%--</div>--%>
        <%--<div class="btn_icon share_icon">--%>
        <%--<a href="#"></a>--%>
        <%--</div>--%>
        <div>
            <div class="box yx show">
                <div>
                    <cite class="yx-icon"></cite>
                </div>
            </div>
            <div class="box jp">
                <div>
                    <div class="jp_cont">
                        <span>
                            ${averageValue}
                        </span>
                        <b>${average}</b>
                        <cite></cite>
                    </div>
                </div>
            </div>
            <div class="box zan">
                <div>
                    <div class="zan_cont">
                        <cite class="active"></cite>
                        <span class="zan_num" id="agreeNum">${gameDB.gameDBCover.coverAgreeNum}</span>
                    </div>
                    <font>用"着迷玩霸"<br>和小伙伴一起玩</font>
                </div>
            </div>
        </div>
        <div class="box-right active">
            <div>
                <div class="btn lb-btn" id="gongl"><cite></cite><span>礼包</span><!--<b></b>-->
                </div>
                <div class="btn gl-btn" id="libao"><cite></cite><span>攻略</span></div>
                <div class="box-right-main">
                    <div class="yx-right-box">
                        <b class="yx-title-bg"></b>

                        <div class="yx-right-box-ttile"><span>${gameDB.gameDBCover.coverTitle}</span></div>
                        <div class="yx-right-box-main">
                            <h1>${gameDB.gameDBCover.coverComment}</h1>

                            <h2>玩霸点评：</h2>

                            <p class="string_cut">${gameDB.gameDBCover.coverDesc}</p>
                        </div>
                    </div>
                    <!--游戏右侧内容==end-->
                    <!--精品右侧内容-->
                    <div class="jp-right-box" style="visibility:hidden">
                        <h1><span>${gameDB.gameDBCoverFieldJson.key1}</span>

                            <p class="rate"><b>${gameDB.gameDBCoverFieldJson.value1}</b>/<em>10</em></p></h1>
                        <div class="jp-right-box-overall">
                            <div class="jp-right-box-now"></div>
                        </div>
                        <h1><span>${gameDB.gameDBCoverFieldJson.key2}</span>

                            <p class="rate"><b>${gameDB.gameDBCoverFieldJson.value2}</b>/<em>10</em></p></h1>
                        <div class="jp-right-box-overall">
                            <div class="jp-right-box-now"></div>
                        </div>
                        <h1><span>${gameDB.gameDBCoverFieldJson.key3}</span>

                            <p class="rate"><b>${gameDB.gameDBCoverFieldJson.value3}</b>/<em>10</em></p></h1>
                        <div class="jp-right-box-overall">
                            <div class="jp-right-box-now"></div>
                        </div>
                        <h1><span>${gameDB.gameDBCoverFieldJson.key4}</span>

                            <p class="rate"><b>${gameDB.gameDBCoverFieldJson.value4}</b>/<em>10</em></p></h1>
                        <div class="jp-right-box-overall">
                            <div class="jp-right-box-now"></div>
                        </div>
                        <h1><span>${gameDB.gameDBCoverFieldJson.key5}</span>

                            <p class="rate"><b>${gameDB.gameDBCoverFieldJson.value5}</b>/<em>10</em></p></h1>
                        <div class="jp-right-box-overall">
                            <div class="jp-right-box-now"></div>
                        </div>
                    </div>
                    <!--精品右侧内容==end-->
                </div>
            </div>
        </div>
        <div class="bottom_ad">

            <c:if test="${downLoadUrl!=''}">
                <a href="${downLoadUrl}" class="bottom_ad_box" id="downLoadUrl">
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
<%--<script type="text/javascript" src="${URL_LIB}/static/js/wap/slider.js"></script>--%>
<script type="text/javascript">

    var h = $(window).height();
    $('.bg').height(h)
    function bili() {
        $('.jp-right-box-now').each(function (i) {
            var now = parseInt($(this).parent().prev('h1').find('b').text());
            var total = parseInt($(this).parent().prev('h1').find('em').text());
            var w = $('.jp-right-box-overall:first').width();
            $(this).width(w / total * now)
        })
    }
    function bili2() {
        $('.jp-right-box-now').each(function (i) {
            $(this).width(0)
        })
    }

    $('.jp').on('click', function () {
        $('.box-right,.box-right>div,.box-right-main,.jp-right-box').css({'visibility': 'initial'});
        $('.yx').removeClass('show')
        $(this).addClass('show')
        $('.jp-right-box').addClass('a')
        bili();
        $('.box-right').removeClass('active');
        $('.yx-right-box').css({'visibility': 'hidden'});
        $('.zan').removeClass('now')
    })
    $('.yx').on('click', function () {
        $('.box-right,.box-right>div,.box-right-main,.yx-right-box').css({'visibility': 'initial'});
        $('.jp').removeClass('show')
        $(this).addClass('show');
        $('.jp-right-box').removeClass('a')
        $('.zan').removeClass('now')
        bili2();
        $('.box-right').addClass('active');
        $('.jp-right-box').css({'visibility': 'hidden'});
    })
    $('.zan').on('click', function () {
        //存在
        $('.box-right,.box-right>div,.box-right-main,.jp-right-box,.yx-right-box').css({'visibility': 'hidden'});
        $('.jp,.yx').removeClass('show');
        $('.jp-right-box').removeClass('a');
        bili();
        bili2();
        $(this).addClass('now');
        return;

    })

    function is_weixn() {
        var ua = navigator.userAgent.toLowerCase();
        if (ua.match(/MicroMessenger/i) == "micromessenger") {
            return true;
        } else {
            return false;
        }


    }

    $(document).ready(function () {

        $('.hn-dw-btn').on('touchstart', function () {
            if (is_weixn()) {
                $('.mark_box').show();
                $('.popup_box').show();
            } else {
                window.location.href = "http://www.joyme.com/appclick/rli4niip";
            }
        });
        $('#downLoadUrl').on('touchstart', function () {
            if (is_weixn()) {
                $('.mark_box').show();
                $('.popup_box').show();
            } else {
                window.location.href = $("#downLoadUrl").attr("href");
            }
        })
        $('.mark_box').on('touchstart', function (argument) {
            if (is_weixn()) {
                $('.mark_box').hide();
                $('.popup_box').hide();
            }
        })
    });
    function stringCut(tar,len){
        var target=$('.'+tar)
        var text=target.text();
        var text_len=text.length;
        if(text_len>len){
            target.text(text.substr(0,len));
        }
    }

    $(document).ready(function(){
        //截字
        stringCut('string_cut',56);
        stringCut('string_cut1',12);
    });

</script>
<%--<script src="${URL_LIB}/static/js/pwiki/wanba_game.js" type="text/javascript"></script>--%>
</html>
