<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<c:choose>
    <c:when test="${baikePrivacy}">
        <a name="updateinfo" id="updateinfo" ></a>
        <h2 class="game-title clearfix">
            <div class="fl">最近更新</div>
                <div class="fr">
                    <span class="options"><a href="javascript:void(0)" name="link_setlayout">排版</a>
                    <ul class="options-box" style="display:none">
                        <c:if test="${!st.first}">
                        <li><a href="${URL_WWW}/game/${game.gameCode}/edit/uprecent">上移一层</a></li>
                        </c:if>
                        <c:if test="${!st.last}">
                        <li><a href="${URL_WWW}/game/${game.gameCode}/edit/downrecent">下移一层</a></li>
                        </c:if>
                    </ul>
                   </span>|
                    <a href="${URL_WWW}/game/${game.gameCode}/edit/recentpage">编辑最近更新</a></div>
        </h2>
        <div class="game-modify">
                ${layout.desc}
        </div>
    </c:when>
    <c:otherwise>
        <c:if test="${fn:length(layout.desc)>0}">
            <a name="updateinfo" id="updateinfo" ></a>
            <h2 class="game-title clearfix">
                <div class="fl">最近更新</div>
                    <div class="fr"><a href="${URL_WWW}/game/${game.gameCode}/edit/recentpage">编辑最近更新</a></div>
            </h2>
            <div class="game-modify">
                    ${layout.desc}
            </div>
        </c:if>
    </c:otherwise>
</c:choose>
