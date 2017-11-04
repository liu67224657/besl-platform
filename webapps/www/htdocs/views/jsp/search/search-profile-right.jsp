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
        <c:if test="${fn:length(groupList)>0}">
            <h3>"<c:out value="${key}"/>"相关的小组</h3>

            <div class="like-group clearfix">
                <div>
                    <c:forEach var="group" items="${groupList}">
                        <a href="${URL_WWW}/group/${group.gameCode}" target="_blank">
                            <c:if test="${fn:length(group.icon.images)>0}">
                                <c:forEach items="${group.icon.images}" var="icon"><img
                                        src="${uf:parseBFace(icon.ll)}" width="50" height="50"/></c:forEach>
                            </c:if>
                             ${group.resourceName}
                        </a>
                    </c:forEach>
                </div>
                <a class="w_more" href="${URL_WWW}/search/group/${key}">查看全部相关小组&gt;&gt;</a>
            </div>
        </c:if>
    </div>
</div>