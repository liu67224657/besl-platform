<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/views/jsp/common/taglibs.jsp"%>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps" />
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>申请注册码 ${jmh_title}</title>
    <style>
    body{background:url(${URL_LIB}/static/default/img/loginbg2.jpg) top center no-repeat; height: 100%; }
    </style>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/default/css/login.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/default/css/mask.css?${version}"/>
    <script type="text/javascript" src="${URL_LIB}/static/js/googleStatistics.js"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/jquery-1.5.2.js"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/common.js?${version}"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/Tips.js?${version}"></script>
</head>
<body>
	<div id="login">
        <div class="reg_zm" >
        	<div class="reg_logo"></div>
            <div class="login_tab"><a href="${ctx}/login" class="log"></a></div>
            <div class="reg_cont"style=" margin-top:10px;">
            	<div class="login_bg1" style="margin-left:40px; display:inline;">
                	<div class="login_l">
                    </div>
                     <div id="email_ok" class="text_true" style="display:none"></div>
                    <div id="email_tips" class="login_tips">
                        <h3><fmt:message key="${errorInfo}" bundle="${userProps}"/></h3>
                    </div>
                </div>

                <div class="">
                    <div id="introduce_tips" class="login_tips">
                    </div>
                </div>
            </div>
        </div>

	</div>
    <%@ include file="/views/jsp/tiles/footer.jsp" %>
</body>
</html>