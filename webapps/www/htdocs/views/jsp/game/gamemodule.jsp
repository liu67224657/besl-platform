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
          content="${game.resourceName}${gameModule.moduleName}、${game.resourceName}${gameModule.moduleName}下载、${game.resourceName}${gameModule.moduleName}攻略、${game.resourceName}${gameModule.moduleName}论坛">
    <meta name="Description" content="${game.resourceName}${gameModule.moduleName}着迷游戏专区、${game.resourceName}${gameModule.moduleName}游戏下载、攻略秘籍、问答、小组"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>${game.resourceName}${gameModule.moduleName}_${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
</head>
<body>
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
<div class="sqcontent  square_hd01 clearfix">
<div class="location"><span>当前位置：</span><a href="${URL_WWW}">首页</a> &gt; <a href="${URL_WWW}/game">游戏</a> &gt; ${game.resourceName}</div>
<div class="groupcon">
<div class="sqt" style="margin-top:0"></div>
<div class="sqc clearfix">

<!-- left -->
<div class="game-group-left">

<c:if test="${fn:length(headImage)>0}">
<div class="game-top-pic">
    <c:forEach var="image" items="${headImage}" varStatus="st">
        <a href="<c:choose><c:when test="${fn:length(image.link)>0}">${image.link}</c:when><c:otherwise>javascript:void(0);</c:otherwise></c:choose>" id="roll_img_${st.index}" style="<c:if test="${st.index>0}">display:none;</c:if><c:if test="${fn:length(image.link)==0}">cursor: default;</c:if>">
            <img src="${uf:parseOrgImg(image.thumbimg)}" width="670" height="240" alt="${image.title}"/></a>
    </c:forEach>
    <c:if test="${fn:length(headImage)>1}">
    <div class="newIndex-picBox-icon">
        <c:forEach var="image" items="${headImage}" varStatus="st">
            <span
                    <c:if test="${st.index==0}">class="current"</c:if> id="roll_button_${st.index}"
                    data-no="${st.index}"></span>
        </c:forEach>
    </div>
    </c:if>
</div>
</c:if>

<c:choose>
    <c:when test="${groupResource!=null || fn:length(gameMenu)>0}">
        <ul class="game-menu" id="ul_game_menu">
    <c:forEach var="menu" items="${gameMenu}" varStatus="st">
        <li><c:choose><c:when test="${fn:length(menu.extField1)>0 && menu.extField1=='ap'}"><a name="menulink" href="${URL}/game/${game.gameCode}#${menu.extField2}">${menu.title}</a></c:when><c:otherwise><a name="menulink" href="${menu.link}">${menu.title}</a></c:otherwise></c:choose></li>
    </c:forEach>
    <li><a href="${URL_WW}/game/${game.gameCode}/post/note?gid=${groupResource.resourceId}" style="color:#945103">我要投稿</a><span class="hot"></span></li>
</ul>
    </c:when>
    <c:otherwise>
        <div style="height:38px"></div>
    </c:otherwise>
</c:choose>


<!-- 自定义模块 -->
    <h2 class="game-title clearfix">
    <div class="fl"> ${layout.layoutName}
        <span>(<a href="${URL_WW}/game/${game.gameCode}">返回游戏首页</a>)</span>
    </div>
        <div class="fr">
            <c:if test="${baikePrivacy}">
                <a href="${URL_WWW}/game/${game.gameCode}/edit/contentpage?mid=${moduleLine.lineId}">编辑栏目</a><c:if test="${groupResource!=null}">|</c:if>
            </c:if>
        <c:if test="${groupResource!=null}">
        <c:choose>
            <c:when test="${layout.extraField1=='handbook'}">
                <a  name="post_link" href="${URL_WW}/game/${game.gameCode}/post/handbook?gid=${groupResource.resourceId}">我也要发攻略</a>
            </c:when>
            <c:when test="${layout.extraField1=='news'}">
               <a  name="post_link" href="${URL_WW}/game/${game.gameCode}/post/news?gid=${groupResource.resourceId}&mid=${moduleLine.lineId}">我也要发新闻</a>
            </c:when>
            <c:when test="${layout.extraField1=='image'}">
               <a  name="post_link" href="${URL_WW}/game/${game.gameCode}/post/handbook?gid=${groupResource.resourceId}">我也要发图片</a>
            </c:when>
            <c:when test="${layout.extraField1=='video'}">
               <a  name="post_link" href="${URL_WW}/game/${game.gameCode}/post/video?gid=${groupResource.resourceId}&mid=${moduleLine.lineId}">我也要发视频</a>
            </c:when>
             <c:when test="${layout.extraField1=='review'}">
                <a  name="post_link" href="${URL_WW}/game/${game.gameCode}/post/review?gid=${groupResource.resourceId}&mid=${moduleLine.lineId}">我也要发评测</a>
             </c:when>
            <c:otherwise>
                <a  name="post_link" href="${URL_WW}/game/${game.gameCode}/post/note?gid=${groupResource.resourceId}&mid=${moduleLine.lineId}">我也要发文章</a>
             </c:otherwise>
        </c:choose>
        </c:if>
    </div>
</h2>
   <c:choose>
       <c:when test="${layout.type.code=='image'}">
            <ul class="game-img">
                <c:forEach var="content" items="${contentList}" varStatus="st">
                <li>
                    <a href="${URL_WWW}/note/${content.elementId}">
                        <span><em><img width="130" src="${uf:parseOrgImg(content.thumbimg)}" width="130" alt="${content.title}"></em></span>
                        <c:choose>
                                <c:when test="${fn:length(content.title)>11}">${fn:substring(content.title, 0,10)}…</c:when>
                                <c:otherwise>${content.title}</c:otherwise>
                         </c:choose>
                    </a>
                </li>
                </c:forEach>
            </ul>
       </c:when>
       <c:when test="${layout.type.code=='video'}">
           <div class="baikeSpecialVideo clearfix" style="padding-left:0px;">
              <ul class="clearfix">
                <c:forEach var="content" items="${contentList}" varStatus="st">
                <li>
                    <a href="${URL_WWW}/note/${content.elementId}">
                        <p><c:choose>
                                <c:when test="${fn:length(content.thumbimg)>0}">
                                     <img src="${uf:parseOrgImg(content.thumbimg)}" width="130" height="90" alt="${content.title}">
                                </c:when>
                                <c:when test="${fn:length(content.videoCoverSrc)>0}">
                                     <img src="${content.videoCoverSrc}" width="130" height="90" alt="${content.title}">
                                </c:when>
                                <c:otherwise>
                                     <img src="${URL_LIB}/static/theme/default/img/default.jpg" width="130" height="90" alt="${content.title}">
                                </c:otherwise>
                            </c:choose></p>
                        <span></span>
                        <c:choose>
                                <c:when test="${fn:length(content.title)>11}">${fn:substring(content.title, 0,10)}…</c:when>
                                <c:otherwise>${content.title}</c:otherwise>
                         </c:choose>
                    </a>
                </li>
                </c:forEach>
            </ul>
            </div>
       </c:when>
       <c:otherwise>
           <ul class="game-event">
                <c:forEach var="content" items="${contentList}" varStatus="st">
                <li  <c:if test="${st.last}">class="noborder"</c:if>>
                    <a href="${URL_WWW}/note/${content.elementId}">  <img src="${uf:parseOrgImg(content.thumbimg)}" width="130" alt="${content.title}"></a>
                    <h2><a href="${URL_WWW}/note/${content.elementId}" target="_blank">
                        <c:choose>
                                <c:when test="${fn:length(content.title)>30}">${fn:substring(content.title, 0,30)}…</c:when>
                                <c:otherwise>${content.title}</c:otherwise>
                         </c:choose></a></h2>
                    <p><c:choose>
                                <c:when test="${fn:length(content.desc)>60}">${fn:substring(content.desc, 0,60)}…</c:when>
                                <c:otherwise>${content.desc}</c:otherwise>
                         </c:choose>
                        <a href="${URL_WWW}/note/${content.elementId}" target="_blank">查看全文&gt;&gt;</a></p>
                        <p class="timer">${dateutil:parseDate(content.createDate)}<span class="authorlist">作者：<a href="${URL_WWW}/people/${content.domain}">${content.screenName}</a></span></p>
                </li>
                </c:forEach>
            </ul>
       </c:otherwise>
   </c:choose>
    <c:if test="${page!=null}">
                <div class="pagecon clearfix">
                    <c:set var="pageparam" value="mid=${moduleLine.lineId}"/>
                    <c:set var="pageurl" value="${URL_WWW}/game/${game.gameCode}/customlist"/>
                    <%@ include file="/views/jsp/page/pagenoend.jsp" %>
                </div>
    </c:if>
</div>

<!-- right -->
<%@ include file="/views/jsp/game/game-right.jsp" %>

</div>
<div class="sqb"></div>
</div>
</div>
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/game-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>

</body>
</html>