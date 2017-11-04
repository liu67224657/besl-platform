<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/views/jsp/common/taglibs.jsp"%>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps" />
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/views/jsp/common/meta.jsp"%>
<title>英雄联盟专题 ${jmh_title}</title>
<link href="${libdomain}/static/default/css/core.css" rel="stylesheet" type="text/css"/>
<link href="${libdomain}/static/default/css/home.css" rel="stylesheet" type="text/css"/>
<link href="${libdomain}/hotdeploy/static/css/subject/active.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="${libdomain}/static/js/jquery-1.5.2.js"></script>
    <script type="text/javascript" src="${libdomain}/static/js/common.js"></script>
    <script type="text/javascript" src="${libdomain}/static/js/jquery.form.js"></script>
    <script type="text/javascript" src="${libdomain}/static/js/jmdialog/jmdialog.js"></script>
</head>
<body>
<div id="wraper">
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
<%@ include file="/hotdeploy/views/jsp/subject/lol.jsp" %>
<%@ include file="/views/jsp/tiles/footer.jsp" %>
</div>
</body>
</html>