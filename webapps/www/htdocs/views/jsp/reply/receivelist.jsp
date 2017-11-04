<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>收到的评论 ${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
</head>
<body>
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
<div class="wrapper clearfix">
    <div class="con">
        <div class="con_hd"></div>
        <div class="con_area con_blog clearfix">
            <div class="conleft_title">
                <h3>我的评论</h3>
            </div>
            <div class="mycomment_tags">
                <a href="${ctx}/reply/receivelist" class="up">收到的评论</a>
                <a href="${ctx}/reply/publishlist">发出的评论</a>
            </div>
            <c:if test="${fn:length(receiveReplys)<1}">
                <div class="empty_text">
                    <fmt:message key="reply.receive.empty" bundle="${userProps}">
                    </fmt:message>
                </div>
            </c:if>
            <!--列表-->
            <c:forEach items="${receiveReplys}" var="repl" varStatus="status">
                <div class="mycomment_list clearfix" name="cont_cmt_list_${repl.interaction.interactionId}">
                    <dl>
                        <dt class="personface">
                            <a name="atLink" title="<c:out value='${repl.interactionProfile.blog.screenName}'/>"
                               href="javascript:void(0)">
                                <img width="58" height="58"
                                     src="<c:out value='${uf:parseFacesInclude(repl.interactionProfile.blog.headIconSet,repl.interactionProfile.detail.sex,"s",true,0,1)[0]}'/>">
                            </a>
                        </dt>
                        <dd class="mycomment">
                            <p><a href="javascript:void(0)" name="atLink"
                                  title="<c:out value='${repl.interactionProfile.blog.screenName}'/>">
                                <c:out value="${repl.interactionProfile.blog.screenName}"/></a>
                                <c:if test="${repl.interactionProfile.detail.verifyType !=null && repl.interactionProfile.detail.verifyType.code!= 'n'}">
                                    <a href="${URL_WWW}/people/${repl.interactionProfile.blog.domain}"
                                       class="${repl.interactionProfile.detail.verifyType.code}vip"
                                       title="<fmt:message key="verify.profile.${repl.interactionProfile.detail.verifyType.code}" bundle="${userProps}"/>"></a>
                                </c:if>：</p>

                            <p>${repl.interaction.interactionContent}<em>(${dateutil:parseDate(repl.interaction.createDate)})</em>
                            </p>
                            <c:set var="locationId" value="${repl.interaction.interactionId}"></c:set>
                            <c:if test="${fn:length(repl.interaction.rootId)>0}">
                                <c:set var="locationId" value="${repl.interaction.rootId}"></c:set>
                            </c:if>
                            <c:choose>
                                <c:when test="${repl.interaction.rootUno!=null && repl.rootProfileBlog!=null}">
                                    <p class="c_text">回复
                                        <c:choose>
                                            <c:when test="${repl.rootProfileBlog.uno == userSession.blogwebsite.uno}">
                                                &nbsp;我&nbsp;
                                            </c:when>
                                            <c:otherwise>
                                                <a href="javascript:void(0)" name="atLink"
                                                   title="${repl.rootProfileBlog.screenName}">${repl.rootProfileBlog.screenName}</a>
                                            </c:otherwise>
                                        </c:choose>的评论：<c:choose><c:when
                                                test="${repl.parentInteraction.removeStatus.code eq 'n'}">“<a class="ccl_right_a"
                                        <c:choose>
                                            <c:when test="${repl.commentType.code == 0}">
                                                href="${URL_WWW}/note/${repl.content.contentId}?rid=${repl.interaction.interactionId}#${locationId}"
                                            </c:when>
                                            <c:otherwise>
                                                href="${repl.content.contentUrl}"
                                            </c:otherwise>
                                        </c:choose>
                                            >${repl.parentInteraction.interactionContent}</a>”</c:when>
                                            <c:otherwise>该内容已被删除。</c:otherwise></c:choose>
                                    </p>
                                </c:when>
                                <c:otherwise>
                                    <p class="c_text">评论
                                        <c:choose>
                                            <c:when test="${repl.contentProfile.blog.uno == userSession.blogwebsite.uno}">
                                                &nbsp;我&nbsp;
                                            </c:when>
                                            <c:otherwise>
                                                ${repl.contentProfile.blog.screenName}
                                            </c:otherwise>
                                        </c:choose>的文章：“<a class="ccl_right_a"
                                                           href="${URL_WWW}/note/${repl.content.contentId}?rid=${locationId}#${locationId}">
                                            <c:out value="${repl.content.subject}"/></a>”
                                    </p>
                                </c:otherwise>
                            </c:choose>

                        </dd>
                        <dd class="delmycomment"><a href="javascript:void(0)" title="删除"
                                                    data-commenttype="${repl.commentType.code}"
                                                    data-rootid = "${repl.interaction.rootId}"
                                                    data-replyid="${repl.interaction.interactionId}"
                                                    data-cuno="${repl.interaction.contentUno}"
                                                    data-cid="${repl.interaction.contentId}" name="del_reply">×</a></dd>
                        <dd class="mycomment_hf">
                            <span class="span_reply">
                                <a href="javascript:void(0)" name="show_childrenreply"
                                   data-cid="${repl.interaction.contentId}"
                                   data-cuno="${repl.interaction.contentUno}"
                                   data-commenttype="${repl.commentType.code}"
                                    <%--回复或者是评论--%>
                                        <c:choose>
                                            <c:when test="${fn:length(repl.interaction.rootId)>0}">
                                                data-rid="${repl.interaction.rootId}"
                                                data-runo="${repl.interaction.rootUno}"
                                                data-pid="${repl.interaction.interactionId}"
                                                data-puno="${repl.interaction.interactionUno}"
                                            </c:when>
                                            <c:otherwise>
                                                data-rid="${repl.interaction.interactionId}"
                                                data-runo="${repl.interaction.interactionUno}"
                                            </c:otherwise>
                                        </c:choose>
                                   data-pname="${repl.interactionProfile.blog.screenName}">回复</a>
                            </span>
                        </dd>
                    </dl>
                </div>
            </c:forEach>
            <!--列表-->
            <!--翻页开始-->
            <c:set var="pageurl" value="${ctx}/reply/receivelist"/>
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
    seajs.use('${URL_LIB}/static/js/init/reply-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>