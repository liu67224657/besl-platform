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
          content="${game.resourceName}、${game.resourceName}攻略、${game.resourceName}官方下载、${game.resourceName}图鉴、${game.resourceName}礼包">
    <meta name="Description" content="着迷网${game.resourceName}专区免费提供详细的${game.resourceName}攻略，${game.resourceName}官方下载，${game.resourceName}图鉴以及${game.resourceName}礼包,是玩家的聚合地和讨论区，欢迎大家来这里一起玩游戏。"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>${game.resourceName}攻略及下载、百科_${jmh_title}</title>
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
    <li><a href="${URL_WW}/game/${game.gameCode}/post/note?gid=${groupResource.resourceId}"  name="post_link" style="color:#945103">我要投稿</a><span class="hot"></span></li>
</ul>
    </c:when>
    <c:otherwise>
        <div style="height:38px"></div>
    </c:otherwise>
</c:choose>


<c:forEach var="layout" items="${layoutList}" varStatus="st">
    <c:choose>
        <c:when test="${layout.type.code== 'baike'}"><%@ include file="/views/jsp/game/layout/baike.jsp" %></c:when>
         <c:when test="${layout.type.code=='article'}"><%@ include file="/views/jsp/game/layout/article.jsp" %></c:when>
         <c:when test="${layout.type.code=='gamedesc'}"><%@ include file="/views/jsp/game/layout/gamedesc.jsp" %></c:when>
         <c:when test="${layout.type.code=='image'}"><%@ include file="/views/jsp/game/layout/image.jsp" %></c:when>
         <c:when test="${layout.type.code=='updateinfo'}"><%@ include file="/views/jsp/game/layout/updateinfo.jsp" %></c:when>
        <c:when test="${layout.type.code=='video'}"><%@ include file="/views/jsp/game/layout/video.jsp" %></c:when>
    </c:choose>
</c:forEach>
<c:if test="${groupResource != null}">
    <a name="group" id="group"></a>
    <h2 class="game-title clearfix">
        <div class="fl">相关小组<span></span></div>
        <div class="fr">${gameSum.postTimes+gameSum.replyTimes}贴&nbsp;|<a
                href="${URL_WWW}/group/${groupResource.gameCode}">进入小组讨论&gt;&gt;</a></div>
    </h2>
    <div class="stab_bd game-group">
        <c:forEach var="itemContent" items="${contentList}" varStatus="status">
         <div class="wsearch_list clearfix">
                            <dl>
                                <dt>
                                    <a href="${URL_WWW}/people/${itemContent.blogContent.profile.blog.domain}"  name="atLink" title="<c:out value="${itemContent.blogContent.profile.blog.screenName}"/>" target="_blank">
                                                <span class="commenfacecon">
                                                <img width="33" height="33"
                                                     src="<c:out value='${uf:parseFacesInclude(itemContent.blogContent.profile.blog.headIconSet,itemContent.blogContent.profile.detail.sex,"s" , true,0,1)[0]}'/>">
                                                </span>
                                    </a>
                                </dt>
                                <dd>
                                    <div class="wlistcon clearfix">
                                        <div class="wlistl">
                                            <c:choose>
                                                <c:when test="${itemContent.blogContent.content.contentType.hasPhrase()}">
                                                    <c:choose>
                                                        <c:when test="${itemContent.voteDto!=null}">
                                                            <h3>
                                                                <c:choose>
                                                                    <c:when test="${itemContent.voteDto.vote.expired}"><em
                                                                            class="poll_finish">已完成</em></c:when>
                                                                    <c:when test="${itemContent.voteDto.voteUserRecord!=null}"><em
                                                                            class="poll_finish">已投票</em></c:when>
                                                                    <c:otherwise><em
                                                                            class="poll_working">进行中</em></c:otherwise>
                                                                </c:choose>
                                                                <a href="${URL_WWW}/note/${itemContent.blogContent.content.contentId}"
                                                                   target="_blank">
                                                                    <c:out value="${itemContent.voteDto.vote.voteSubject.subject}"/>
                                                                </a>
                                                                <em class="poll_icon"></em>
                                                            </h3>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <h3>
                                                                <a href="${URL_WWW}/note/${itemContent.blogContent.content.contentId}"
                                                                   target="_blank">
                                                                        ${itemContent.blogContent.content.content}
                                                                </a>
                                                                <c:if test="${itemContent.lineItem.displayType.isEssential()}">
                                                                    <span class="best"></span>
                                                                </c:if>
                                                            </h3>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:when>
                                                <c:otherwise>
                                                    <h3>
                                                        <a href="${URL_WWW}/note/${itemContent.blogContent.content.contentId}"
                                                           target="_blank">
                                                            <c:out value="${itemContent.blogContent.content.subject}"/>
                                                        </a>
                                                        <c:if test="${itemContent.lineItem.displayType.isEssential()}">
                                                            <span class="best"></span>
                                                        </c:if>
                                                    </h3>

                                                    <p>${itemContent.blogContent.content.content}</p>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        <div class="wlistr clearfix">
                                            <div class="running">
                                                <span class="zhanwei">&nbsp; </span>
                                                            <span class="pinglun" title="评论数">
                                                                <i></i>${itemContent.blogContent.content.replyTimes}
                                                            </span>
                                            </div>
                                            <c:choose>
                                                <c:when test="${itemContent.contentInteraction != null}">
                                                    <span class="wtime">${dateutil:parseDate(itemContent.contentInteraction.createDate)}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="wtime">${dateutil:parseDate(itemContent.blogContent.content.publishDate)}</span>
                                                </c:otherwise>
                                            </c:choose>
                                            <c:if test="${itemContent.interactionProfileBlog != null}">
                                                <span class="wname"
                                                      title="最后回复人">${jstr:subStr(itemContent.interactionProfileBlog.screenName,6,'…')}</span>
                                            </c:if>
                                        </div>
                                    </div>
                                </dd>
                            </dl>
                        </div>
        </c:forEach>
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
    var gameCode='${game.gameCode}';
    seajs.use('${URL_LIB}/static/js/init/game-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
<script type="text/javascript" id="bdshare_js" data="type=slide&amp;img=8&amp;pos=right&amp;uid=6528936"></script>
<script type="text/javascript" id="bdshell_js"></script>
<script type="text/javascript">
    var bds_config = {"snsKey":"{'tsina':'1245341962','tqq':'100292513','t163':'qCEuLkaOUGKXxUdm','tsohu':''}"};
    document.getElementById("bdshell_js").src = "http://bdimg.share.baidu.com/static/js/shell_v2.js?cdnversion=" + Math.ceil(new Date() / 3600000);
</script>
</body>
</html>