<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div class="game-group-title">
    <div class="fl">
        <div>
                  <span>
                      <c:if test="${fn:length(group.icon.images)>0}">
                              <c:forEach items="${group.icon.images}" var="icon"><img src="${uf:parseBFace(icon.ll)}"  width="55"/></c:forEach>
                      </c:if>
                  </span>
        </div>
        <span class="img-bottom-shadow"></span>
    </div>
    <h2>${group.resourceName}</h2>
    <c:choose>
        <c:when test="${groupUser!=null && groupUser.validStatus.code == 1}">
            <div class="joined">我是这个小组的成员&nbsp;&gt;&nbsp;<a href="javascript:void(0);" onclick="return false;"
                                                            name="unFollowGroup" data-gid="${group.resourceId}">退出小组</a>
            </div>
        </c:when>
        <c:otherwise>
            <a href="javascript:void(0);" onclick="return false;" name="followGroup" class="joingroup"
               data-gid="${group.resourceId}">加入小组</a>
        </c:otherwise>
    </c:choose>
</div>
<div class="game-group-dis"><c:choose><c:when test="${fn:length(group.resourceDesc)>0}">${group.resourceDesc}</c:when><c:otherwise><fmt:message key="group.desc.empty" bundle="${userProps}"/></c:otherwise></c:choose></div>