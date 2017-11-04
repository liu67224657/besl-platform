<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<div class="searchr w_game">
    <div class="w_area clearfix">
        <c:if test="${fn:length(gameList)>0}">
            <h3>"<c:out value="${key}"/>"相关的游戏条目</h3>

            <div class="like-game clearfix">
                <ul class="game-interest">
                    <c:forEach var="game" items="${gameList}" varStatus="st">
                        <li>
                            <div>
                                <a href="${URL_WWW}/game/${game.gameCode}" target="_blank">
                                    <c:if test="${fn:length(game.icon.images)>0}">
                                        <c:forEach items="${game.icon.images}" var="icon"><img
                                                src="${uf:parseBFace(icon.ll)}" width="78"/></c:forEach>
                                    </c:if>
                                </a>
                            </div>
                            <a href="${URL_WWW}/game/${game.gameCode}" target="_blank">${game.resourceName}</a>
                        </li>
                    </c:forEach>
                </ul>
                <a class="w_more" href="${URL}/search/game/<c:out value="${key}"/>">查看全部相关游戏条目&gt;&gt;</a>
            </div>
        </c:if>
        <c:if test="${fn:length(profileList)>0}">
            <h3>"<c:out value="${key}"/>"相关的用户</h3>

            <c:forEach var="profile" items="${profileList}" varStatus="st">
                <div class="dr_con clearfix">
                    <!--头像链接-->
                    <a href="${URL_WWW}/people/${profile.profile.blog.domain}" name="atLink"
                       title="<c:out value="${profile.profile.blog.screenName}"/>" target="_blank">
                        <span class="commenfacecon">
                            <img width="33" height="33"
                                 src="${uf:parseFacesInclude(profile.profile.blog.headIconSet,profile.profile.detail.sex,"s",true,0,1)[0]}"/>
                        </span>
                    </a>
                    <!--链接到博客 title昵称-->
                    <a target="_blank" title="<c:out value="${profile.profile.blog.screenName}"/>"
                       href="${URL_WWW}/people/${profile.profile.blog.domain}">
                        <c:out value="${profile.profile.blog.screenName}"/>
                    </a>
                </div>
            </c:forEach>
            <a class="w_more" href="${URL_WWW}/search/profile/<c:out value='${key}'/>">查看所有相关人&gt;&gt;</a>
        </c:if>
    </div>
</div>