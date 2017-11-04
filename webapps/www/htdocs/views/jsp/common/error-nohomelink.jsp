<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>页面不存在 - ${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <script src="${URL_LIB}/static/js/common/seajs.js" data-main="${URL_LIB}/static/js/init/common-init"></script>
</head>
<body>
<!--头部开始-->
<c:import url="/views/jsp/passport/header.jsp"/>
<!--头部结束-->
<!--中间部分开始-->
<div class="content clearfix">
   <div class="normal_error">
      <h3>                <c:if test="${message != null}">
                    <fmt:message key="${message}" bundle="${userProps}"></fmt:message>
                </c:if></h3>
   </div>
</div>
<!--中间部分结束-->
<!--页尾开始-->
<%@include file="/views/jsp/tiles/footer.jsp" %>
<!--页尾结束-->
</body>
</html>