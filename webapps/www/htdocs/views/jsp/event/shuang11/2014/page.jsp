<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta name="Keywords" content="游戏礼包,游戏周边,游戏兑换码,游戏手办">
    <meta name="description"
          content="着迷网双十一活动为广泛游戏玩家提供各种游戏礼包,激活码,特权码,测试礼包,新手礼包,兑换码等免费领取,还有多种游戏周边礼品等您拿，双十一给力游戏礼包就在着迷网."/>
    <script type="text/javascript" src="${URL_LIB}/static/js/common/bdhm-noseajs.js"></script>
    <title>着迷网双十一_游戏礼包_游戏周边_兑换码_手办_玩偶_着迷网Joyme.com</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/event/shuang11/2014/css/shuang11.css?${version}" rel="stylesheet" type="text/css"/>
</head>
<body>
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
<div id="wrap" class="pr">
    <!--center-->
    <div id="center" class="clearfix">
        <!--cen_box-->
        <div class="cen_box ma">
            <ul class="clearfix">
                <c:choose>
                    <c:when test="${not empty giftPage}">
                        <c:forEach items="${giftPage}" var="dto" varStatus="st">
                            <li>
                                <div class="game_bg"><img
                                        src="${URL_LIB}/static/event/shuang11/2014/images/game_bg_${st.index+1}.jpg" alt=""
                                        title=""/></div>
                                <a class="gaem_Icon" href="${URL_WWW}/gift/${dto.gid}"><img src="${dto.gipic}" alt="" title=""/></a>
                                <a class="link_btn" href="${URL_WWW}/gift/${dto.gid}">领取礼包</a>

                                <div class="game_box">
                                    <em>${dto.title}</em>

                                    <p><span>${dto.rn}</span>人领取</p>

                                    <p>剩余：<span>${dto.sn}</span></p>
                                </div>

                            </li>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <li>暂时没有礼包</li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
        <!--cen_box==end-->
    </div>
    <!--center==end-->
    <!--footer-->
    <div id="footer" class="pr">
        <div class="f_link"><img src="${URL_LIB}/static/event/shuang11/2014/images/link_bg.jpg" alt="" title=""/></div>
    </div>
    <!--footer==end-->
    <!--wp_bg-->
    <div id="wp_bg" class="pa">
        <span class="bg_1"></span>
        <span class="bg_2"></span>
        <span class="bg_3"></span>
        <span class="bg_4"></span>
        <span class="bg_5"></span>
        <span class="bg_6"></span>
        <span class="bg_7"></span>
        <span class="bg_8"></span>
        <span class="bg_9"></span>
        <span class="bg_10"></span>
        <span class="bg_11"></span>
        <span class="bg_12"></span>
        <span class="bg_13"></span>
        <span class="bg_14"></span>
        <span class="bg_15"></span>
        <span class="bg_16"></span>
    </div>
    <!--wp_bg==end-->
</div>
<script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${URL_LIB}/static/js/common/jquery.lazyload.js"></script>


<script type="text/javascript">

    document.getElementById('returnTop').onclick = function () {
        document.documentElement.scrollTop = 0;
        document.body.scrollTop = 0;
        document.getElementById('returntop').style.display = 'none';
    }
    document.getElementById('returnGift').onclick = function() {
        document.documentElement.scrollTop = document.getElementById('gamegift').offsetTop;
        document.body.scrollTop = document.getElementById('gamegift').offsetTop;
    }
    document.getElementById('returnPoint').onclick = function() {
        document.documentElement.scrollTop = document.getElementById('pointGift').offsetTop;
        document.body.scrollTop = document.getElementById('pointGift').offsetTop;
    }
</script>

<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/giftmarket-init.js');
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>