<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <meta http-equiv="Cache-Control" content="no-store">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="0">
    <meta name="Keywords" content="hao123游戏-着迷网联合活动,CJ,游戏大联欢,美女">
    <meta name="description" content="抽奖,礼包,发号,实物奖品,新闻,资讯,吐槽,图片">
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon">
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css">
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/cj2014.css?${version}" rel="stylesheet" type="text/css">
    <title>hao123游戏-着迷网 CJ 游戏大联欢</title>
    <%--<script type="text/javascript" src="http://lib.joyme.com/static/js/common/google-statistics-noseajs.js"></script>--%>
</head>
<body style="padding-top: 0px;">
<!--头部开始-->
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>

<!-- 内容 -->
<div class="wrap">
    <div class="contain">
        <a href="http://www.joyme.com/games/chinajoy/" class="goto">进入CJ专题</a>

        <!-- 活动说明 -->
        <div class="dis">
            <p>1.本活动仅在2014年China Joy期间（2014.7.31~2014.8.3）进行。</p>

            <p>2.每个第三方账号每天仅限一次抽奖机会。</p>

            <p>3.点击下方按钮可直接参与抽奖！如中奖请保存好奖品兑换码！</p>

            <p>4.实物奖品均以快递形式发出，请中奖用户携带奖品兑换码（每个兑换码仅对应一个奖品），联系本活动唯一客服QQ：1635789029。奖品将在5~8个工作日内送达您的手中！</p>

            <p>5.奖品分为一等奖（3名）、二等奖（5名）、三等奖（15名）、四等奖（25名）参与奖（30名名）5个奖项，具体奖品请参考下方图片。</p>

            <p>6.如您中的是参与奖，请同样联系上述唯一客服QQ！</p>

            <p>7.本活动最终解释权归着迷网所有。</p>
        </div>

        <!-- 获奖名单 -->
        <div id="winners">
            <div id="winnersbox" style="top: -78px;">
                <c:if test="${userLogList.size() > 0}">
                    <c:forEach items="${userLogList}" var="log">
                        <p align="left">&nbsp;${log.screenName}&nbsp;抽到${log.lotteryAwardName}</p>
                    </c:forEach>
                </c:if>
            </div>
        </div>

        <!-- 活动奖品 -->
        <div id="goods">
            <ul>
                <c:if test="${awardList.size() > 0}">
                    <c:forEach items="${awardList}" var="award">
                        <li><img src="${award.lotteryAwardPic}" alt="${award.lotteryAwardName}"></li>
                    </c:forEach>
                </c:if>
            </ul>
        </div>

        <!-- 抽奖按钮 -->
        <a href="javascript:;" id="Lottery-btn">点击抽奖</a>

        <!-- 友链 -->
        <div class="link">
            <div>
                <a href="http://www.joyme.com/news/gamenews/201407/0943634.html"><img src="${URL_LIB}/static/theme/default/images/cj2014/cj2014-1.jpg"></a>
                <a href="http://www.joyme.com/news/gamenews/201407/1043636.html"><img src="${URL_LIB}/static/theme/default/images/cj2014/cj2014-2.jpg"></a>
                <a href="http://html.joyme.com/mobile/gameguides.html"><img src="${URL_LIB}/static/theme/default/images/cj2014/cj2014-5.jpg"></a>
                <a href="http://kada.joyme.com/"><img src="${URL_LIB}/static/theme/default/images/cj2014/cj2014-6.jpg"></a>
            </div>
        </div>

    </div>
    <div class="bg bg-1"></div>
    <div class="bg bg-2"></div>
    <div class="bg bg-3"></div>
    <div class="bg bg-4"></div>
    <div class="bg bg-5"></div>
    <div class="bg bg-6"></div>
    <div class="bg bg-7"></div>
    <div class="bg bg-8"></div>
    <div class="bg bg-9"></div>
    <div class="bg bg-10"></div>
    <div class="bg bg-11"></div>
    <div class="bg bg-12"></div>
    <div class="bg bg-13"></div>
    <div class="bg bg-14"></div>
</div>

<!-- footer -->
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/lottery-init.js');
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
<script>
    !function () {
        var a = document.getElementById('winnersbox');
        var b = -(a.getElementsByTagName('p').length * 32);
        var c = 0;
        var d = null;

        if (b > -224) return;

        a.innerHTML += a.innerHTML;

        function roll() {
            d = setInterval(function () {
                c >= b ? (c--) : (c = 0);
                a.style.top = c + 'px';
            }, 50);

        }

        ;
        roll();
    }()
</script>

<script type="text/javascript" src="${URL_LIB}/static/js/common/bdhm-noseajs.js"></script>
</body>
</html>