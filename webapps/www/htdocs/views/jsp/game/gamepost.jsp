<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta name="KeyWords"
          content="${game.resourceName}、${game.resourceName}下载、${game.resourceName}攻略、${game.resourceName}论坛">
    <meta name="Description" content="${game.resourceName}着迷游戏专区、${game.resourceName}游戏下载、攻略秘籍、问答、小组"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>${game.resourceName}游戏百科及下载、攻略_${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <script>
        var POSTCONFIG = {};
        <c:choose><c:when test="${postType.code=='handbook'}">POSTCONFIG.initTag = ['${game.resourceName}','攻略','资料'];</c:when><c:when test="${postType.code=='news'}">POSTCONFIG.initTag = ['${game.resourceName}','新闻','资料'];</c:when><c:when test="${postType.code=='image'}">POSTCONFIG.initTag = ['${game.resourceName}','图片','资料']</c:when><c:when test="${postType.code=='video'}">POSTCONFIG.initTag = ['${game.resourceName}','视频','资料'];</c:when>
        <c:when test="${postType.code=='review'}">POSTCONFIG.initTag = ['${game.resourceName}','评测','资料'];</c:when><c:otherwise>POSTCONFIG.initTag = ['${game.resourceName}'];</c:otherwise></c:choose>
        <c:choose><c:when test="${displayType==null}"></c:when><c:when test="${displayType.code=='list'}"><c:set var="verifyText" value="发表文章必须要包含图片附件"></c:set>POSTCONFIG.formula = [ {code:'image',text:'${verifyText}'}];</c:when><c:when test="${displayType.code=='image'}"><c:set var="verifyText" value="发表文章必须要包含图片附件"></c:set>POSTCONFIG.formula = [{code:'image',text:'${verifyText}'}];</c:when><c:when test="${displayType.code=='video'}"><c:set var="verifyText" value="发表视频文章必须要包含视频附件"></c:set>POSTCONFIG.formula = [ {code:'video',text:'${verifyText}'}];</c:when></c:choose>
    </script>
</head>
<body>
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>

<div class="wrapper clearfix">
    <div class="con_new">
        <div class="con_new_top"></div>
        <div class="con_new_center">
            <!-- 第一栏 -->
            <h2 class="newBaikeTitle"><a href="${URL_WWW}/game/${game.gameCode}"
                                         class="returnback">返回${game.resourceName}条目页&gt;&gt;</a>${game.resourceName}--发表<c:choose><c:when test="${postType.code=='handbook'}">攻略资料</c:when><c:when test="${postType.code=='news'}">新闻</c:when><c:when test="${postType.code=='image'}"> 图片</c:when><c:when test="${postType.code=='video'}">视频</c:when><c:when test="${postType.code=='review'}">评测</c:when><c:otherwise>文章</c:otherwise></c:choose>
            </h2>

            <input type="hidden" id="hid_gamename" value="${game.resourceName}"/>
            <input type="hidden" id="hid_gamecode" value="${game.gameCode}"/>
            <input type="hidden" id="hid_groupname" value="${group.resourceName}"/>
            <input type="hidden" id="hid_groupcode" value="${group.gameCode}"/>

            <div class="fabu-box-new">
                <!-- ==============套用线上的发布框_START=============== -->
                <div class="fabucon">
                    <div class="conmentbd clearfix">
                        <%@ include file="/views/jsp/post/post-talk.jsp" %>
                    </div>
                </div>
                <!-- ==========套用线上的发布框_END============= -->
            </div>
        </div>
        <div class="con_new_bottom"></div>
    </div>

    <div class="right_side_new">
        <div class="right_side_new_top">投稿提示</div>
        <%--<div class="right_side_new_top">文章会发布到的小组</div>--%>
        <div class="right_side_new_center clearfix">
            <p class="add-baike-dis-top">
                文章会发布到的小组
            </p>

            <div class="xggroup xggroup_2 noborder">
                <div class="clearfix">
                    <a class="groupface" href="${URL_WWW}/group/${group.gameCode}" target="_blank">
                        <c:forEach items="${group.icon.images}" var="icon">
                            <img src="${uf:parseBFace(icon.ll)}" width="48" height="48"/>
                        </c:forEach>
                    </a>
                    <a href="${URL_WWW}/group/${group.gameCode}" target="_blank">${group.resourceName}</a>

                    <p>${sumData.postTimes+sumData.replyTimes}贴 | <a href="${URL_WWW}/group/${group.gameCode}/talk"
                                                                     target="_blank">进入小组讨论&gt;&gt;</a>
                    </p>
                </div>
                <p class="add-baike-dis">
                    文章通过审核，就可以入选${game.resourceName}游戏条目了
                </p>
            </div>


        </div>
        <div class="right_side_new_bottom"></div>


    </div>
</div>

<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/posthandbook-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>

</body>
</html>