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
    <meta name="Keywords" content="游戏攻略,游戏下载，游戏资料大全">
    <meta name="description" content="着迷网提供最新和热门游戏的游戏攻略及下载、游戏资料、任何人都有机会参与创建和完善着迷的游戏条目,将有用的内容提供给所有有共同兴趣的玩家.。"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>游戏攻略,游戏下载,游戏资料大全-${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
</head>

<body>
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>

<!-- ****开始**** -->
<div class="articlepage-white-bg">
    <div class="carousel-and-articleBox">
	<div class="index-2013-box clearfix">
		<!-- 轮播图和文章列表 -->
		<div class="clearfix">
			${gamePageDTO.headLine}
			<div class="articleBox">
				${gamePageDTO.bulletin}
			</div>
		</div>
     </div>
    </div>

      <div class="index-2013-box clearfix">
		<!-- wiki列表和热门游戏推荐 -->
		<div class="clearfix">
			<div class="wiki-detail">
				${gamePageDTO.gameWiki}
			</div>

			<div class="wiki-right">
				<!-- 广告 -->
				 <%@ include file="/hotdeploy/views/jsp/game/gameindex-ad.jsp" %>
                 ${gamePageDTO.recommendGame}
			</div>
		</div>
	</div>
</div>


<!--页尾开始-->
<%@ include file="/views/jsp/tiles/footer-game-partner.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/gameindex-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>
