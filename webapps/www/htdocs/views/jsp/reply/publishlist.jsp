<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>发出的评论 ${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <%--<script type="text/javascript" src="${URL_LIB}/static/js/common/google-statistics.js"></script>--%>
</head>
<body>
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
<div class="wrapper clearfix">
    <!--左侧开始-->
    <div class="con">
        <div class="con_hd"></div>
        <div class="con_area con_blog clearfix">

            <div class="conleft_title">
                <h3>我的评论</h3>
            </div>
            <div class="mycomment_tags">
                <a href="${ctx}/reply/receivelist">收到的评论</a>
                <a href="${ctx}/reply/publishlist" class="up">发出的评论</a>
            </div>
            <c:if test="${fn:length(receiveReplys)<1}">
                <div class="empty_text">
                    <fmt:message key="reply.publish.empty" bundle="${userProps}">
                    </fmt:message>
                </div>
            </c:if>
            <!--列表-->
            <c:forEach items="${receiveReplys}" var="repl" varStatus="status">
                <div class="mycomment_list clearfix" name="cont_cmt_list_${repl.interaction.interactionId}">
                    <dl>
                        <c:if test="${repl.commentType.code == 0}">
                            <dt class="personface">
                                <c:choose>
                                    <c:when test="${repl.parentInteraction != null && repl.parentProfileBlog!=null}">
                                        <a href="${URL_WWW}/people/${repl.parentProfileBlog.domain}" name="atLink"
                                           title="${repl.parentProfileBlog.screenName}">
                                            <img width="58" height="58"
                                                 src="${uf:parseFacesInclude(repl.parentProfileBlog.headIconSet, repl.contentProfile.detail.sex,"s", true, 0,1)[0]}">
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="${URL_WWW}/people/${repl.contentProfile.blog.domain}" name="atLink"
                                           title="${repl.contentProfile.blog.screenName}">
                                            <img width="58" height="58"
                                                 src="${uf:parseFacesInclude(repl.contentProfile.blog.headIconSet,repl.contentProfile.detail.sex, "s", true, 0,1)[0]}">
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                            </dt>
                        </c:if>
                        <dd class="mycomment" <c:if test="${repl.commentType.code != 0}">style="width: 648px;"</c:if>>
                            <p>${repl.interaction.interactionContent}<em>(${dateutil:parseDate(repl.interaction.createDate)})</em>
                            </p>

                            <p class="c_text">
                                <c:set var="locationId" value="${repl.interaction.interactionId}"></c:set>
                                <c:if test="${fn:length(repl.interaction.rootId)>0}">
                                    <c:set var="locationId" value="${repl.interaction.rootId}"></c:set>
                                </c:if>
                                <c:choose>
                                    <c:when test="${repl.parentInteraction != null}">
                                        回复<c:choose><c:when
                                            test="${repl.parentProfileBlog.uno == userSession.blogwebsite.uno}">
                                        &nbsp;我&nbsp;
                                    </c:when>
                                        <c:otherwise>
                                            <a href="${URL_WWW}/people/${repl.parentProfileBlog.domain}" name="atLink"
                                               title="${repl.parentProfileBlog.screenName}">${repl.parentProfileBlog.screenName}</a>
                                        </c:otherwise>
                                    </c:choose>
                                        的评论:<c:choose><c:when test="${repl.parentInteraction.removeStatus.code eq 'n'}">

                                        “<a class="ccl_right_a"
                                            <c:choose>
                                                <c:when test="${repl.commentType.code == 1}">
                                                    href="${repl.content.contentUrl}"
                                                </c:when>
                                                <c:otherwise>
                                                    href="${URL_WWW}/note/${repl.interaction.contentId}?rid=${repl.interaction.interactionId}#${locationId}"
                                                </c:otherwise>
                                            </c:choose>
                                        >
                                             ${repl.parentInteraction.interactionContent}</a>”</c:when>
                                        <c:otherwise>该内容已被删除。</c:otherwise></c:choose>
                                    </c:when>
                                    <c:otherwise>
                                        评论<c:choose>
                                        <c:when test="${repl.contentProfile.blog.uno == userSession.blogwebsite.uno}">
                                            &nbsp;我&nbsp;
                                        </c:when>
                                        <c:otherwise>
                                            <a href="${URL_WWW}/people/${repl.contentProfile.blog.domain}" name="atLink"
                                               title="${repl.contentProfile.blog.screenName}">${repl.contentProfile.blog.screenName}</a>
                                        </c:otherwise>
                                    </c:choose><c:if test="${repl.contentProfile != null}">的</c:if>文章:“
                                        <a class="ccl_right_a"
                                            <c:choose>
                                                <c:when test="${repl.commentType.code == 0}">
                                                    href="${URL_WWW}/note/${repl.interaction.contentId}?rid=${repl.interaction.interactionId}#${locationId}"
                                                </c:when>
                                                <c:otherwise>
                                                    href="${repl.content.contentUrl}"
                                                </c:otherwise>
                                            </c:choose>
                                           >
                                            <c:out value="${repl.content.subject}"/>
                                        </a>”
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </dd>
                        <dd class="delmycomment"><a href="javascript:void(0)" title="删除"
                                                    data-commenttype="${repl.commentType.code}"
                                                    data-rootid = "${repl.interaction.rootId}"
                                                    data-replyid="${repl.interaction.interactionId}"
                                                    data-cuno="${repl.interaction.contentUno}"
                                                    data-cid="${repl.interaction.contentId}" name="del_reply">×</a></dd>
                        <!--replyId="${repl.interaction.interactionId}"
                        cuno="${repl.interaction.contentUno}"
                        cid="${repl.interaction.contentId}"-->
                    </dl>

                </div>
            </c:forEach>
            <!--列表-->
            <!--翻页开始-->
            <c:set var="pageurl" value="${ctx}/reply/publishlist"/>
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