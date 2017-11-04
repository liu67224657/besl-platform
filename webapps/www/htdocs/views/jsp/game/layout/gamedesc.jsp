<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/jstllibs.jsp" %>
<c:choose>
    <c:when test="${baikePrivacy}">
            <a name="gamedesc" id="gamedesc" ></a>
            <h2 class="game-title clearfix">
                <div class="fl">游戏介绍</div>
                <div class="fr">
                    <span class="options"><a href="javascript:void(0)" name="link_setlayout">排版</a>
                    <ul class="options-box" style="display:none">
                        <c:if test="${!st.first}">
                        <li><a href="${URL_WWW}/game/${game.gameCode}/edit/updesc">上移一层</a></li>
                        </c:if>
                        <c:if test="${!st.last}">
                        <li><a href="${URL_WWW}/game/${game.gameCode}/edit/downdesc">下移一层</a></li>
                        </c:if>
                    </ul>
                   </span>|
                    <a href="${URL_WWW}/game/${game.gameCode}/edit/descpage">编辑游戏介绍</a>
                </div>
            </h2>
            <div id="game_desc" class="game-dis<c:if test="${layout.desc2==layout.desc}">-all</c:if>">${layout.desc2}</div>
            <c:if test="${layout.desc2!=layout.desc}">
                <div id="game_desc_hide" style="display:none">${layout.desc}</div>
                <div class="game-viewmore"><a id="game_desc_toggle" class="c-open" href="javascript:void(0);"  onclick="return false;">查看全部</a></div>
            </c:if>
    </c:when>
    <c:otherwise>
         <c:if test="${fn:length(layout.desc2)>0}">
             <a name="gamedesc" id="gamedesc" ></a>
            <h2 class="game-title clearfix">
                <div class="fl">游戏介绍</div>
            </h2>
            <div id="game_desc"  class="game-dis<c:if test="${layout.desc2==layout.desc}">-all</c:if>">${layout.desc2}</div>
            <c:if test="${layout.desc2!=layout.desc}">
                <div id="game_desc_hide" style="display:none">${layout.desc}</div>
                <div class="game-viewmore"><a id="game_desc_toggle" class="c-open" href="javascript:void(0);"
                                              onclick="return false;">查看全部</a></div>
            </c:if>
        </c:if>
    </c:otherwise>
</c:choose>
