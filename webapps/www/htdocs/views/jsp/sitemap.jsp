<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>网站地图_着迷网Joyme.com</title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <link rel="icon" href="http://lib.joyme.com/static/img/favicon.ico" type="image/x-icon"/>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <script src="${URL_LIB}/static/js/common/seajs.js" data-main="${URL_LIB}/static/js/init/common-init"></script>
</head>
<body>
<div id="wraper">
    <c:import url="/views/jsp/passport/header.jsp"/>
<!-- *************网站地图_START************* -->
<div class="articlepage-white-bg">
	<div class="index-2013-box clearfix">
		<h1 class="site-map-title">网站地图</h1>
        <div style="height:auto!important; min-height:600px; height:600px;">
         <%@ include file="/hotdeploy/views/jsp/sitemap/sitemap.jsp" %>
        </div>
	</div>
</div>
<!-- *************网站地图_END************* -->
<%@ include file="/views/jsp/tiles/footer.jsp" %>
</div>
</body></html>