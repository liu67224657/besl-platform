<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>我的信箱 ${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>

    <%--<script type="text/javascript" src="${URL_LIB}/static/js/common/google-statistics.js"></script>--%>

</head>
<body>
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
<div class="wrapper clearfix">
    <div class="con">
        <div class="con_hd"></div>
        <div class="con_area con_blog clearfix">
            <div class="conleft_title">
                <h3>我的信箱</h3>
            </div>
            <div class="mycomment_tags">
                <a href="javascript:void(0)" class="up">私信</a>
                <a href="${ctx}/message/notice/list">通知</a>
                <a href="javascript:void(0)" name="sendMsgMask" class="send_more">发私信</a>
            </div>
            <c:if test="${fn:length(reMessages.rows)<1}">
                <div class="empty_text">
                    <fmt:message key="message.empty" bundle="${userProps}">
                    </fmt:message>
                </div>
            </c:if>
            <!--列表-->
            <c:forEach items="${reMessages.rows}" var="message" varStatus="st">
                <div class="messages_list clearfix" id="msg_l_${message.profile.blog.uno}">
                    <dl>
                        <dt class="personface">
                            <a name="atLink" title="<c:out value='${message.profile.blog.screenName}'/>"
                               href="${URL_WWW}/people/<c:out value="${message.profile.blog.domain}"/>">
                                <img src="<c:out value='${uf:parseFacesInclude(message.profile.blog.headIconSet,message.profile.detail.sex,"s", true,0,1)[0]}'/>"
                                     width="58"
                                     height="58"/>
                            </a>
                        </dt>
                        <dd class="messages">
                            <div class="messages_l"></div>
                            <p class="p_text">
                                <c:choose>
                                    <c:when test="${message.messageTopic.lastestMessage.senderUno eq userSession.blogwebsite.uno}">
                                        发给 <a href="${URL_WWW}/people/<c:out value="${message.profile.blog.domain}"/>"
                                              name="atLink" title="<c:out value='${message.profile.blog.screenName}'/>"><c:out
                                            value="${message.profile.blog.screenName}"/></a> : ${message.messageTopic.lastestMessage.body}
                                    </c:when>
                                    <c:otherwise>
                                        <a href="${URL_WWW}/people/<c:out value="${message.profile.blog.domain}"/>"
                                           name="atLink"
                                           title="<c:out value='${message.profile.blog.screenName}'/>"><c:out
                                                value="${message.profile.blog.screenName}"/></a>: ${message.messageTopic.lastestMessage.body}
                                    </c:otherwise>
                                </c:choose>
                            </p>

                            <p class="delmycomment">
                                <a href="javascript:void(0)" title="删除" name="removeMsg"
                                   uno="<c:out value="${message.profile.blog.uno}"/>">×</a>
                            </p>

                            <p class="p_tools">
                                <span>${dateutil:parseDate(message.messageTopic.lastestMessage.sendDate)}
                                | <a href="${ctx}/message/private/reply?senduno=<c:out value="${message.messageTopic.reletionUno}"/>">共<c:out
                                            value="${message.messageTopic.msgSize}"/>条对话</a>
                                | <a href="${ctx}/message/private/reply?senduno=<c:out value="${message.messageTopic.reletionUno}"/>">回复</a>
                                </span>
                            </p>
                        </dd>
                    </dl>
                </div>
            </c:forEach>
            <!--列表-->
            <!--翻页开始-->
            <c:set var="pageurl" value="${ctx}/message/private/list"/>
            <%@ include file="/views/jsp/page/goto.jsp" %>
            <!--翻页结束-->

        </div>
        <div class="con_ft"></div>
    </div>
    <!--右侧开始-->
    <%@ include file="/views/jsp/tiles/rightmenu.jsp" %>
    <!--右侧结束-->
</div>

<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use("${URL_LIB}/static/js/init/message-init.js");
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>