<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<c:if test="${baikeTreeDTOList!=null && fn:length(baikeTreeDTOList)>0}">
    <c:forEach var="baikeTreeDTO" items="${baikeTreeDTOList}">
     <h2 class="index-2013-title">${baikeTreeDTO.gameResource.resourceName}的攻略<c:choose>
        <c:when test="${baikeTreeDTO.gameResource.resourceDomain.code eq 'game'}">
                <a href="${URL_WWW}/game/${baikeTreeDTO.gameResource.gameCode}" class="moreGl">攻略大全&nbsp;&gt;&gt;</a>
            </c:when>
        <c:otherwise>
            <a href="${URL_WWW}/baike/${baikeTreeDTO.gameResource.gameCode}" class="moreGl">攻略大全&nbsp;&gt;&gt;</a>
        </c:otherwise>
    </c:choose></h2>
     <div class="game-strategy-2th">
      <c:forEach var="baike" items="${baikeTreeDTO.baikeDTOList}" varStatus="st">
         <div class="side_bd_list_con <c:if test="${st.last}">bordernone</c:if>">
             <%--一级--%>
             <h3><a href="javascript:void(0);" id="menu_${baike.category.categoryId}" title="${baike.category.categoryName}">
                 <em <c:if test="${pathCategoryIdMap[baike.category.categoryId]==null}">class="guanbi"</c:if>></em>
                 <c:choose>
                     <c:when test="${fn:length(baike.category.categoryName)>18}">
                         ${fn:substring(baike.category.categoryName,0,18)}…
                     </c:when>
                     <c:otherwise>
                         ${baike.category.categoryName}
                     </c:otherwise>
                 </c:choose>
             </a></h3>

                 <ul class="bk_shu" id="tree_${baike.category.categoryId}" <c:if test="${pathCategoryIdMap[baike.category.categoryId]==null}">style="display:none"</c:if>>
                     <li>
                        <%--一级->三级--%>
                         <c:if test="${fn:length(baike.itemsByCategory)>0}">
                             <c:forEach var="items" items="${baike.itemsByCategory}">
                                 <p><a href="${items.displayInfo.linkUrl}" target="_blank" title="${items.displayInfo.subject}"><i></i>
                                    <c:choose>
                                         <c:when test="${fn:length(items.displayInfo.subject)>14}">
                                             ${fn:substring(items.displayInfo.subject,0,14)}…
                                         </c:when>
                                         <c:otherwise>
                                             ${items.displayInfo.subject}
                                         </c:otherwise>
                                     </c:choose></a></p>
                             </c:forEach>
                         </c:if>
                          <%--一级->二级--%>
                         <c:if test="${fn:length(baike.children)>0}">
                             <c:forEach var="children" items="${baike.children}">
                                 <a href="javascript:void(0);" id="menu_${children.category.categoryId}" title="${children.category.categoryName}">
                                     <em <c:choose>
                                         <c:when test="${pathCategoryIdMap[children.category.categoryId]==null}">class="jia"</c:when>
                                         <c:otherwise>class="jian"</c:otherwise>
                                     </c:choose>></em>
                                     <c:choose>
                                         <c:when test="${fn:length(children.category.categoryName)>16}">
                                             ${fn:substring(children.category.categoryName,0,16)}…
                                         </c:when>
                                         <c:otherwise>
                                             ${children.category.categoryName}
                                         </c:otherwise>
                                     </c:choose>

                                 </a>
                                 <c:if test="${fn:length(children.itemsByCategory)>0}">
                                     <ul class="dijian" id="tree_${children.category.categoryId}" <c:if test="${pathCategoryIdMap[children.category.categoryId]==null}">style="display:none"</c:if>>
                                         <c:forEach var="items" items="${children.itemsByCategory}">
                                             <li><p><a href="${items.displayInfo.linkUrl}" target="_blank" title="${items.displayInfo.subject}"><i></i>
                                                 <c:choose>
                                                     <c:when test="${fn:length(items.displayInfo.subject)>14}">
                                                         ${fn:substring(items.displayInfo.subject,0,14)}…
                                                     </c:when>
                                                     <c:otherwise>
                                                         ${items.displayInfo.subject}
                                                     </c:otherwise>
                                                 </c:choose>
                                                    </a></p></li>
                                         </c:forEach>
                                     </ul>
                                 </c:if>
                             </c:forEach>
                         </c:if>
                     </li>
                 </ul>
         </div>
         </c:forEach>
     </div>
      <div class="dotted-line"></div>
     </c:forEach>
</c:if>


