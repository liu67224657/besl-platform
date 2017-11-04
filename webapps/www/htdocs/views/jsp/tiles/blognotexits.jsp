<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>${webSiteInfo.screenName} ${jmh_title}</title>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/default/css/core.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/default/css/home.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/default/css/mask.css?${version}"/>
    <script type="text/javascript" src="${URL_LIB}/static/js/common/google-statistics.js"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/jquery-1.5.2.js"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/jquery.validate.min.js"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/jquery.form.js"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/common.js?${version}"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/jmdialog/jmdialog.js"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/atme/atme.js?${version}"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/card/card.js?${version}"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/post/post.js?${version}"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/blog/blog.js?${version}"></script>
</head>
<body>
<div id="wraper">
    <%@ include file="/views/jsp/blog/b01/blog-header.jsp" %>
    <div id="content">
        <div class="blog_left">
            <div class="tag_cl_right  blog_con blog_reset">
                <div class="cont_text">
                    <p><fmt:message key="blog.content.not.exists" bundle="${userProps}"/></p>
                </div>
                <!-- 原文footer -->
            </div>
            <!-- 评论 -->
        </div>
        <!--cont_left-->
        <%@ include file="rightmenu.jsp" %>
    </div>
    <!--content-->
    <%@ include file="/views/jsp/tiles/footer.jsp" %>
</div>
</body>
</html>