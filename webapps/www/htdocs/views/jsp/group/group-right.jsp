<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div class="game-group-box-right">
    <c:if test="${fn:length(aboutGameList)>0}">
        <h2>相关游戏条目</h2>
        <ul class="relevance-game">
            <c:forEach var="game" items="${aboutGameList}">
                <li>
                    <div class="fl">
                        <c:forEach items="${game.icon.images}" var="icon">
                            <a href="${URL_WWW}/game/${game.gameCode}"><span><img width="80"
                                                                                  src="${uf:parseOrgImg(icon.ll)}"></span></a>
                        </c:forEach>
                        <span class="img-bottom-shadow"></span>
                    </div>
                    <p><a href="${URL_WWW}/game/${game.gameCode}">${game.resourceName}</a></p>
                    <c:if test="${game.categorySet!=null}">
                        <p>类型：<c:forEach items="${game.categorySet.categorySet}" var="category" varStatus="st">${category.code}<c:if test="${!st.last}">/</c:if></c:forEach></p>
                    </c:if>
                    <c:if test="${game.resourceStatus.showDevelop() && fn:length(game.develop)>0}">
                        <p>开发商：${game.develop}</p>
                    </c:if>
                    <a href="${URL_WWW}/game/${game.gameCode}" class="gotogame">去游戏条目页&gt;</a>
                </li>
            </c:forEach>
        </ul>
    </c:if>

    <c:if test="${wiki!=null}">
    <h2>相关wiki</h2>
    <div class="relevance-wiki">
        <div>
            <a href="${wiki.link}">
            <img src="${uf:parseOrgImg(wiki.thumbimg)}" width="220" alt="${wiki.title}"><span></span>
            </a>
        </div>
        <p>${wiki.desc}</p>
    </div>
    </c:if>
    <!--组长-->
    <h2>组长<c:if test="${fn:length(moderators)>0}">（${fn:length(moderators)}）</c:if></h2>
    <div class="item_pd01">
        <div class="dr_con clearfix">
            <c:choose>
                    <c:when test="${fn:length(moderators)>0}">
                        <c:forEach items="${moderators}" var="profile" varStatus="status">
                            <div class="dr_con clearfix">
                                <span class="commenfacecon">
                                    <a href="${URL_WWW}/people/${profile.blog.domain}" name="atLink"  title="${profile.blog.screenName}" target="_blank">
                                        <img width="33" height="33" src="<c:out value='${uf:parseFacesInclude(profile.blog.headIconSet,profile.detail.sex,"s" , true,0,1)[0]}'/>">
                                    </a>
                                </span>
                                <a href="${URL_WWW}/people/${profile.blog.domain}" target="_blank" name="atLink" title="${profile.blog.screenName}">${profile.blog.screenName}</a>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                    <div class="dr_con clearfix">
                        &nbsp;&nbsp;&nbsp;这里还没有组长
                    </div>
                    </c:otherwise>
                </c:choose>
        </div>
        <a target="_blank" href="http://www.joyme.com/note/3NxDguvsRe6qYV8iWgRfek" class="ask_for">我要申请组长！&gt;</a>
    </div>

    <c:if test="${fn:length(aboutGroupList)>0}">
    <h2>友情小组</h2>
    <div class="another-group clearfix">
        <c:forEach var="group" items="${aboutGroupList}">
         <a href="${URL_WWW}/group/${group.gameCode}">
             <c:forEach items="${group.icon.images}" var="icon">
                 <img width="48" height="48" src="${uf:parseOrgImg(icon.ll)}"/>
             </c:forEach>
             <span class="img-bottom-shadow"></span>${group.resourceName}</a>
        </c:forEach>
    </div>
    </c:if>
</div>