<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>系统通知 ${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/style.css?${version}" rel="stylesheet" type="text/css"/>
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
                <a href="${ctx}/message/private/list">私信</a>
                <a href="javascript:void(0)" class="up">通知</a>
            </div>
            <c:choose>
            <c:when test="${fn:length(pageRows.rows)<1}">
                <div class="empty_text">
                    <fmt:message key="notice.empty" bundle="${userProps}">
                    </fmt:message>
                </div>
            </c:when>
            <c:otherwise>
            <!--列表-->
            <div class="notice_con">
                <ul>
                    <c:forEach items="${pageRows.rows}" var="notice" varStatus="st">
                    <li class="clearfix" id="notice_l_${notice.msgId}">
                        <div class="notice_face">
                              <span class="personface">
                                  <%--todo--%>
                                <img
                                      src="${URL_LIB}/static/theme/default/img/notice.jpg"
                                      width="58px" height="58px">
                              </span>
                               着迷系统通知:
                        </div>
                        <div class="notice_txt">${notice.body}</div>
                        <div class="notice_time">${dateutil:parseDate(notice.sendDate)}</div>
                        <a href="javascript:void(0);" class="notice_delete" title="删除" name="removeNotice" id="${notice.msgId}">删除</a>
                    </li>
                    </c:forEach>
                </ul>
            </div>
             </c:otherwise>
            </c:choose>
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