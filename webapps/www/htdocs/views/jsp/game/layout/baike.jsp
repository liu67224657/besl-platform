<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<c:if test="${fn:length(layout.baikeDTOTree)>0 || baikePrivacy}">
<a name="${layout.viewId}" id="${layout.viewId}"></a>
<h2 class="game-title clearfix">
    <a name="baike" id="baike"></a>
    <div class="fl">
        ${game.resourceName}攻略
    </div>
    <div class="fr">
        <c:if test="${baikePrivacy}">
            <c:choose>
                <c:when test="${layout.viewId==0}">
                        <a href="${URL_WW}/game/${game.gameCode}/baike/create">+攻略百科</a>
                </c:when>
                <c:otherwise>
                    <span class="options"><a href="javascript:void(0)" name="link_setlayout">排版</a>
                    <ul class="options-box" style="display:none">
                        <c:if test="${!st.first}">
                        <li><a href="${URL_WWW}/game/${game.gameCode}/edit/upbaike">上移一层</a></li>
                        </c:if>
                        <c:if test="${!st.last}">
                        <li><a href="${URL_WWW}/game/${game.gameCode}/edit/downbaike">下移一层</a></li>
                        </c:if>
                    </ul>
                   </span>|
                        <a href="${URL_WW}/game/${game.gameCode}/baike/editpage" >编辑百科</a>
                </c:otherwise>
            </c:choose>
            <c:if test="${fn:length(layout.baikeDTOTree)>0 && groupResource != null}">|</c:if>
        </c:if>
        <c:if test="${fn:length(layout.baikeDTOTree)>0 && groupResource != null}">
            <a name="post_link" href="${URL_WW}/game/${game.gameCode}/post/handbook?gid=${groupResource.resourceId}&mid=${layout.viewId}">我也要发攻略</a>
        </c:if>
    </div>
</h2>
<div class="blank10"></div>
<c:if test="${fn:length(layout.baikeDTOTree)>0}">
        <c:forEach var="baike" items="${layout.baikeDTOTree}">
                <%--一级标题--%>
                <h2 class="game-gl-title">${baike.category.categoryName}</h2>
                <c:if test="${fn:length(baike.itemsByCategory)>0}">
                    <ul class="game-gl-content">
                        <c:forEach var="items" items="${baike.itemsByCategory}">
                             <li><a href="${items.displayInfo.linkUrl}" target="_blank" <c:if test="${items.displayInfo!=null && fn:length(items.displayInfo.extraField1)>0}">style="color:${items.displayInfo.extraField1}"</c:if>>${items.displayInfo.subject}</a></li>
                        </c:forEach>
                    </ul>
                </c:if>
                <c:if test="${fn:length(baike.children)>0}">
                        <c:forEach var="children" items="${baike.children}">
                            <h2 class="game-gl-title-second">${children.category.categoryName}</h2>
                            <c:if test="${fn:length(children.itemsByCategory)>0}">
                                <ul class="game-gl-content">
                                    <c:forEach var="items" items="${children.itemsByCategory}">
                                        <li><a href="${items.displayInfo.linkUrl}" target="_blank" <c:if test="${items.displayInfo!=null && fn:length(items.displayInfo.extraField1)>0}">style="color:${items.displayInfo.extraField1}"</c:if>>${items.displayInfo.subject}</a></li>
                                    </c:forEach>
                                </ul>
                            </c:if>
                        </c:forEach>
                </c:if>
        </c:forEach>
    </c:if>
 </c:if>
