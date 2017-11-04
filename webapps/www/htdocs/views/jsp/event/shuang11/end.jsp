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
    <script type="text/javascript" src="http://lib.joyme.com/static/js/common/bdhm-noseajs.js"></script>
    <title>着迷网双十一_游戏礼包_游戏周边_兑换码_手办_玩偶_着迷网Joyme.com</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/event/shuang11/css/shuang11.css?${version}" rel="stylesheet" type="text/css"/>
</head>
<body>
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>

<%@ include file="/hotdeploy/views/jsp/event/shuang11/end.jsp" %>
<!-- 页脚 -->
<div class="footercon clearfix">
    <div class="footer">
        <%@ include file="/hotdeploy/views/jsp/tiles/all-rights-reserved.jsp" %>
        <span> 京ICP备11029291号</span>
        <a rel="nofollow" target="_blank" href="http://www.joyme.com/help/aboutus">关于着迷</a> |
        <a rel="nofollow" target="_blank" href="http://www.joyme.com/about/job/zhaopin">工作在着迷</a> |
        <a rel="nofollow" target="_blank" href="http://www.joyme.com/about/contactus">商务合作</a>|
        <a target="_blank" href="http://www.joyme.com/sitemap.htm">网站地图</a>
    </div>
</div>

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