<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<c:if test="${consumeRankList != null && !empty consumeRankList}">
    <div class="gift-center-title clearfix">
        <h2>积分消费排行</h2>
    </div>
    <div class="top-bg consume-ranking">
        <ul>
            <c:forEach items="${consumeRankList}" var="dto" varStatus="st">
                <li>
                    <em>${st.index + 1}</em>
                    <a href="${URL_WWW}/people/${dto.domain}">
<%--todo --%>
                        <%--<img src="${icon:parseFacesInclude(dto.iconSet,dto.sex,"s" , true,0,1)[0]}" width="34" height="34">--%>
                    </a>

                    <div>
                        <p>
                            <%--<a href="${URL_WWW}/people/${dto.domain}">--%>
                                <a href="javascript:void(0);">
                        ${dto.screenName}</a></p>

                        <p>消费积分<span>${dto.point}</span>分</p>
                    </div>
                </li>
            </c:forEach>
        </ul>
    </div>
</c:if>