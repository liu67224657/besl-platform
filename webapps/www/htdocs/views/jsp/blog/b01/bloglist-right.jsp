<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div class="sidebar">
    <div class="side_item">
        <div class="side_hd02"></div>
        <div class="side_bd">
            <c:if test="${profile.detail.verifyType!=null && profile.detail.verifyType.getCode() != 'n'}">
                <div class="blog_renzheng">
                    <div class="blog_rzcon">
                        <c:if test="${profile.detail.verifyType!=null && profile.detail.verifyType.code!='n'}">
                            <span class="blog_rz_${profile.detail.verifyType.code}" title="<fmt:message key="verify.profile.${profile.detail.verifyType.code}" bundle="${userProps}"/>"></span>
                        </c:if>
                        <c:if test="${userSession==null || userSession.userDetailinfo.verifyType==null || userSession.userDetailinfo.verifyType.getCode() eq 'n'}">
                            <span><a href="${URL_WWW}/note/1CGoLChZ18yHHQJW_jVV1S" target="_blank">我也要认证</a></span>
                        </c:if>
                        <p><c:out value="${profile.detail.verifyDesc}"/></p>
                    </div>
                </div>
            </c:if>
            <ul class="attention clearfix">
                <li><a href="javascript:void(0)" name="followfans" data-link="${ctx}/social/follow/list/${profile.blog.uno}"> <span>关注</span>
                    <p id="r_focus_num">${profile.sum.focusSum}</p>
                </a></li>
                <li><a href="javascript:void(0)" name="followfans" data-link="${ctx}/social/fans/list/${profile.blog.uno}"> <span>粉丝</span>
                    <p id="r_fans_num">${profile.sum.fansSum}</p>
                </a></li>
                <li><a href="${URL_WWW}/people/<c:out value="${profile.blog.domain}"/>"><span>文章</span>
                    <p id="r_blog_num">${profile.sum.blogSum}</p>
                </a></li>
            </ul>
        </div>
        <div class="side_ft"></div>
    </div>
    <c:if test="${fn:length(favorBoardList)>0}">
    <div class="side_item">
        <div class="side_hd">
            <c:choose>
                <c:when test="${userSession!=null && userSession.blogwebsite.uno eq profile.blog.uno}">
                    我的小组
                </c:when>
                <c:when test="${profile.detail.sex==null || profile.detail.sex=='1'}">
                    他的小组
                </c:when>
                <c:when test="${profile.detail.sex=='0'}">
                    她的小组
                </c:when>
            </c:choose>
        </div>
        <div class="side_bd side_pd">
            <div class="other_like">
                <c:forEach var="board" items="${favorBoardList}" varStatus="st">
                    <c:if test="${board.resourceDomain.code eq 'group'}">
                        <a href="${URL_WWW}/group/${board.gameCode}" target="_blank">${board.resourceName}</a>
                    </c:if>
                </c:forEach>
            </div>
        </div>
        <div class="side_ft"></div>
    </div>
    </c:if>
</div>