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

<div class="sqcontent  square_hd01 clearfix">
    <div class="groupcapition gamecapition">
        <h3 class="group_hd">游戏<span class="little_title">玩家自己创建，由有爱的达人不断维护。</span></h3>
    </div>
    <div class="groupcon">
        <div class="sqc clearfix">
            <div class="group_left">

                <div class="blank10 clear"></div>
                     <%--<a name="wiki" id="wiki"></a>--%>
                     <%--${gamePageDTO.wiki}--%>

                <!-- ======移动游戏推荐====== -->
                <div class="blank10 clear"></div>
                  <a name="mobile" id="mobile"></a>
                     ${gamePageDTO.mobile}

                <!-- ======掌机游戏推荐====== -->
                <div class="blank20 clear"></div>
                 <a name="zhangji" id="zhangji"></a>
                     ${gamePageDTO.zhangji}

                <!-- ======网络游戏推荐====== -->
                <div class="blank20 clear"></div>
                 <a name="mmo" id="mmo"></a>
                    ${gamePageDTO.mmo}

                <!-- ======单机游戏推荐====== -->
                <div class="blank20 clear"></div>
                  <a name="danji" id="danji"></a>
                    ${gamePageDTO.danji}

                <div class="clear" style="height:30px"></div>
            </div>
            <!--group_left-->

            <div class="group_right">
                <div class="addNewItemBox">
				<h2>没找到？我来创建游戏条目...</h2>
				<p>
					<a href="http://www.joyme.com/note/2BIoVTYU176Wz7cHDjLmHI" target="_blank" class="addNewItemBtn addNewItemBtn-newItem">创建游戏条目</a>
				</p>
			  </div>

                ${gamePageDTO.newList}
                <div class="blank10"></div>
                ${gamePageDTO.changeList}
            </div>

            <!--group_right-->
        </div>
        <div class="sqb"></div>
    </div>
</div>


<!--页尾开始-->
<%@ include file="/views/jsp/tiles/footer-game-partner.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/moregame-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>
