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
                    <dt class="tx">
                        <span id="set_tx_div">
                            <a href="${URL_WWW}/people/${userSession.blogwebsite.domain}" target="_blank"><img
                                    src="<c:out value='${uf:parseFacesInclude(userSession.blogwebsite.headIconSet,userSession.userDetailinfo.sex,"m" , true,0,1)[0]}'/>"
                                    width="64" height="64"/></a>
                                <span class='set_txbg' id="txbg" style="display: none;cursor: pointer"></span><span
                                class='set_tx' style="display:none;cursor: pointer" id="set_tx">设置头像</span>
                        </span>
                    </dt>
                    <dd class="about_meg">
                        <p class="tx_hd"><a
                                href="${URL_WWW}/people/${userSession.blogwebsite.domain}">${userSession.blogwebsite.screenName}</a>
                            <c:if test="${userSession.userDetailinfo.verifyType !=null && userSession.userDetailinfo.verifyType.code!= 'n'}">
                                <a href="${URL_WWW}/people/${userSession.blogwebsite.domain}"
                                   class="${userSession.userDetailinfo.verifyType.code}vip"
                                   title="<fmt:message key="verify.profile.${userSession.userDetailinfo.verifyType.code}" bundle="${userProps}"/>">
                                </a>
                            </c:if>
                        </p>
                    </dd>
                </dl>

                <!--粉丝开始-->
                <div class="attention clearfix">
                    <ul>
                        <li>
                            <a href="${ctx}/social/follow/list"> <span>关注</span>

                                <p id="r_focus_num">${userSession.countdata.focusSum}</p>

                            </a>
                        </li>
                        <li><a href="${ctx}/social/fans/list"> <span>粉丝</span>

                            <p id="r_fans_num">${userSession.countdata.fansSum}</p>
                        </a></li>
                        <li><a href="${URL_WWW}/people/<c:out value="${userSession.blogwebsite.domain}"/>">
                            <span>文章</span>

                            <p id="r_blog_num">${userSession.countdata.blogSum}</p>
                        </a></li>
                    </ul>
                </div>
                <!--粉丝结束-->
            </div>
        </div>
        <div id="rightmenu_profile_info" class="side_bd">
            <div class="user_tool clearfix">
                <ul>
                    <li>
                        <a href="${ctx}/reply/receivelist" <c:choose>
                            <c:when test="${rmflag == 'reply'}">
                                class="t1on"
                            </c:when>
                            <c:otherwise>
                                class="t1"
                            </c:otherwise>
                        </c:choose>
                                >我的评论</a>
                    </li>
                    <li>
                        <a href="${ctx}/atme"  <c:choose>
                            <c:when test="${rmflag == 'atme'}">
                                class="t3on"
                            </c:when>
                            <c:otherwise>
                                class="t3"
                            </c:otherwise>
                        </c:choose>
                                >提到我的</a>
                    </li>
                    <li>
                        <a href="${ctx}/message/private/list"<c:choose>
                            <c:when test="${rmflag == 'message'}">
                                class="t2on"
                            </c:when>
                            <c:otherwise>
                                class="t2"
                            </c:otherwise>
                        </c:choose>>我的信箱</a>
                    </li>
                    <li>
                        <a href="${ctx}/profile/favorite"
                                <c:choose>
                                    <c:when test="${rmflag == 'favorite'}">
                                        class="t4on"
                                    </c:when>
                                    <c:otherwise>
                                        class="t4"
                                    </c:otherwise>
                                </c:choose>>我的喜欢</a>
                    </li>
                </ul>
            </div>
        </div>
        <div class="side_ft"></div>
    </div>
    <!--头像部分结束-->

    <!--第二个sideitem-->
    <div class="side_item">
        <div class="side_hd">我的小组</div>
        <div class="side_bd">
            <div class="mygroup">
                <c:choose>
                    <c:when test="${userSession.boardMap.size() > 0}">
                        <ul class="clearfix" id="group_list">
                            <c:forEach items="${userSession.boardMap}" var="board" varStatus="index">
                                <li class="<c:if test="${index.index > 7}">hide</c:if>">• <a
                                        href="${URL_WWW}/group/${board.value.gameCode}" target="_blank"
                                        title="${board.value.gameName}">
                                    <c:choose><c:when
                                            test="${fn:length(board.value.gameName) > 5}">${fn:substring(board.value.gameName, 0, 5)}…</c:when>
                                        <c:otherwise>${board.value.gameName}</c:otherwise></c:choose></a></li>
                            </c:forEach>
                        </ul>
                        <div class="clearfix">
                            <a href="javascript:void(0);" href="javascript:void(0);" id="add_group">+ 添加</a>

                            <c:if test="${userSession.boardMap.size() > 8}">
                                <span id="show_more_group_list" class="open">展开</span>
                            </c:if>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <p id="nojoingroup" class="nojoingroup">
                            你还没有关注的小组，<br>来看看<a href="javascript:void(0);" id="add_group">推荐小组</a>!
                        </p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <div class="side_ft"></div>
    </div>

    <div id="area_recommend"></div>
    <!--我关注的小组结束-->
    <%@ include file="/hotdeploy/views/jsp/topic/topic-right.jsp" %>
</div>
<!--右侧结束-->
