<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<a name="${layout.viewId}" id="${layout.viewId}"></a>
    <h2 class="game-title clearfix">
    <div class="fl"> ${layout.viewName}
        <c:if test="${layout.contentRows!=null && layout.contentRows.page.maxPage>1}">
        <span>(<a href="${URL_WW}/game/${game.gameCode}/customlist?mid=${layout.viewId}"><c:choose><c:when test="${layout.type.code=='image'}">更多图片</c:when><c:when test="${layout.type.code=='video'}">更多视频</c:when><c:otherwise>更多动态</c:otherwise></c:choose></a>)</span>
        </c:if>
    </div>
        <div class="fr">
            <c:if test="${baikePrivacy}">
                <span class="options"><a href="javascript:void(0)" name="link_setlayout">排版</a>
                    <ul class="options-box" style="display:none">
                        <c:if test="${!st.first}">
                        <li><a href="${URL_WWW}/game/${game.gameCode}/edit/upmodule?mid=${layout.viewId}">上移一层</a></li>
                        </c:if>
                        <c:if test="${!st.last}">
                        <li><a href="${URL_WWW}/game/${game.gameCode}/edit/downmodule?mid=${layout.viewId}">下移一层</a></li>
                        </c:if>
                        <li><a name="delmoudle_link" data-mid="${layout.viewId}" href="javascript:void(0);" >删除栏目</a></li>
                    </ul>
                   </span>|
                <a href="${URL_WWW}/game/${game.gameCode}/edit/contentpage?mid=${layout.viewId}">编辑栏目</a><c:if test="${groupResource!=null}">|</c:if>
        </c:if>
        <c:if test="${groupResource!=null}">
        <c:choose>
            <c:when test="${layout.modulePostType.code=='handbook'}">
                <a  name="post_link" href="${URL_WW}/game/${game.gameCode}/post/handbook?gid=${groupResource.resourceId}">我也要发攻略</a>
            </c:when>
            <c:when test="${layout.modulePostType.code=='news'}">
                <a  name="post_link" href="${URL_WW}/game/${game.gameCode}/post/news?gid=${groupResource.resourceId}&mid=${layout.viewId}">我也要发新闻</a>
            </c:when>
            <c:when test="${layout.modulePostType.code=='image'}">
                <a  name="post_link" href="${URL_WW}/game/${game.gameCode}/post/handbook?gid=${groupResource.resourceId}">我也要发图片</a>
            </c:when>
            <c:when test="${layout.modulePostType.code=='video'}">
                <a  name="post_link" href="${URL_WW}/game/${game.gameCode}/post/video?gid=${groupResource.resourceId}&mid=${layout.viewId}">我也要发视频</a>
            </c:when>
             <c:when test="${layout.modulePostType.code=='review'}">
                 <a  name="post_link" href="${URL_WW}/game/${game.gameCode}/post/review?gid=${groupResource.resourceId}&mid=${layout.viewId}">我也要发评测</a>
             </c:when>
            <c:otherwise>
                <a  name="post_link" href="${URL_WW}/game/${game.gameCode}/post/note?gid=${groupResource.resourceId}&mid=${layout.viewId}">我也要发文章</a>
             </c:otherwise>
        </c:choose>
        </c:if>
    </div>
</h2>
<div class="baikeSpecialVideo clearfix" style="padding-left:0px;">
              <ul class="clearfix">
                <c:forEach var="content" items="${layout.contentRows.rows}" varStatus="st">
                <li>
                    <a href="${URL_WWW}/note/${content.elementId}">
                        <p>
                            <c:choose>
                                 <c:when test="${fn:length(content.thumbimg)>0}">
                                     <img src="${uf:parseOrgImg(content.thumbimg)}" width="130" height="90" alt="${content.title}">
                                </c:when>
                                <c:when test="${fn:length(content.videoCoverSrc)>0}">
                                     <img src="${content.videoCoverSrc}" width="130" height="90" alt="${content.title}">
                                </c:when>
                                <c:otherwise>
                                     <img src="${URL_LIB}/static/theme/default/img/default.jpg" width="130" height="90" alt="${content.title}">
                                </c:otherwise>
                            </c:choose>
                        </p>
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