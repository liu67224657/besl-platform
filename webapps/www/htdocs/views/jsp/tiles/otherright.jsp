<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!--右侧开始-->
<div class="sidebar">
    <!--头像部分开始-->
    <div class="side_item">
        <div class="side_hd01"></div>
        <div class="side_bd01">
            <div class="side_bd_line">
                <dl class="user_area clearfix">
                    <dt class="tx"><img
                            src='${uf:parseFacesInclude(profile.blog.headIconSet,profile.detail.sex,"m",true,0,1)[0]}'
                            width="64" height="64"/></dt>
                    <dd class="about_meg">
                        <p class="tx_hd">
                            <a href="${URL_WWW}/people/${profile.blog.domain}">${profile.blog.screenName}</a>
                            <c:if test="${profile.detail.verifyType !=null && profile.detail.verifyType.code!= 'n'}">
                                <a href="${URL_WWW}/people/${profile.blog.domain}"
                                   class="${profile.detail.verifyType.code}vip"
                                   title="<fmt:message key="verify.profile.${profile.detail.verifyType.code}" bundle="${userProps}"/>"></a>
                            </c:if>
                        </p>

                        <p>
                            <c:choose>
                                <c:when test="${userSession.blogwebsite.uno eq profile.blog.uno}">
                                </c:when>
                                <c:when test="${relation.srcStatus.code eq 'y' && relation.destStatus.code eq 'n'}">
                                    <a class="attentioned" href="javascript:void(0)" name="unfollow"
                                       data-uno="${profile.blog.uno}"></a>
                                </c:when>
                                <c:when test="${relation.srcStatus.code eq 'y' && relation.destStatus.code eq 'y'}">
                                    <a class="attentionedall" href="javascript:void(0)" name="unfollow"
                                       data-uno="${profile.blog.uno}"></a>
                                </c:when>
                                <c:when test="${relation.srcStatus.code eq 'n' && relation.destStatus.code eq 'y'}">
                                    <a class="add_attention_ok" href="javascript:void(0)" name="follow"
                                       data-uno="${profile.blog.uno}"></a>
                                </c:when>
                                <c:otherwise>
                                    <a class="add_attention" href="javascript:void(0)" name="follow"
                                       data-uno="${profile.blog.uno}"></a>
                                </c:otherwise>
                            </c:choose>
                        </p>
                    </dd>
                </dl>
            </div>
        </div>
        <div class="side_bd">
            <c:if test="${profile.detail!=null && profile.detail.verifyType!=null && profile.detail.verifyType.getCode()!='n'}">
                <div class="blog_renzheng">
                    <div class="blog_rzcon">
                        <c:if test="${profile.detail.verifyType !=null && profile.detail.verifyType.code!= 'n'}">
                            <span class="blog_rz_${profile.detail.verifyType.code}"
                                  title="<fmt:message key="verify.profile.${profile.detail.verifyType.code}" bundle="${userProps}"/>"></span>
                        </c:if>
                        <c:if test="${userSession.userDetailinfo ==null||userSession.userDetailinfo.verifyType==null||userSession.userDetailinfo.verifyType.getCode()=='n'}">
                        <span>
                            <a href="${URL_WWW}/note/1CGoLChZ18yHHQJW_jVV1S" target="_blank">我也要认证</a>
                        </span>
                        </c:if>
                        <p>${profile.detail.verifyDesc}</p>
                    </div>
                </div>
            </c:if>
            <ul class="attention clearfix">
                <li>
                    <a href="${ctx}/social/follow/list/${profile.blog.uno}"> <span>关注</span>

                        <p id="r_focus_num">${profile.sum.focusSum}</p>
                    </a>
                </li>
                <li><a href="${ctx}/social/fans/list/${profile.blog.uno}"> <span>粉丝</span>

                    <p id="r_fans_num">${profile.sum.fansSum}</p>
                </a></li>
                <li><a href="${URL_WWW}/people/<c:out value="${profile.blog.domain}"/>">
                    <span>文章</span>

                    <p id="r_blog_num">${profile.sum.blogSum}</p>
                </a></li>
            </ul>
        </div>
        <div class="side_ft"></div>
    </div>


    <!--小组开始-->
    <div class="side_item">
        <c:if test="${fn:length(favorBoardList)>0}">
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
                        <a href="${URL_WWW}/group/${board.gameCode}" target="_blank">${board.resourceName}</a>
                    </c:forEach>
                </div>
            </div>
            <div class="side_ft"></div>
        </c:if>
        <!--我关注的小组结束-->
    </div>
</div>
