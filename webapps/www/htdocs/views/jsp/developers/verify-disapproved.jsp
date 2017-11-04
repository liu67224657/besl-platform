<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta name="Keywords" content="">
    <meta name="description"
          content=""/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>未通过认证</title>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/global.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/style.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/common.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/core.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/newgames.css?${version}"/>
</head>
<body>
<!-- topbar -->
<c:import url="/tiles/gamedbheader?redr=${requestScope.browsersURL}"/>

<!-- content -->
<div class="newgames-content clearfix">
	<div class="checkstate">
		<div class="ischecking">您的认证信息未通过审核，原因是：<br><span>${developer.verifyReason}</span></div>
		<div class="youcando">
			<p>您可以：</p>
			<p>1.重新<a href="${URL_WWW}/developers/verifypage?category=${developer.category.code}&reverify=true">提交申请</a></p>
			<p>2.联络<a href="http://www.joyme.com/about/contactus">我们的商务同事</a>获取帮助</p>
		</div>
	</div>
	<div class="board"><img src="${URL_LIB}/static/theme/default/img/board.jpg"></div>
</div>
<!-- 页脚 -->
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
<script type="text/javascript" src="${URL_LIB}/static/js/common/bdhm-noseajs.js"></script>
</body>
</html>