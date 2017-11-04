<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="org.slf4j.Logger,org.slf4j.LoggerFactory" %>
<%response.setStatus(200);%>
<%
    Throwable ex = null;
    if (exception != null)
        ex = exception;
    if (request.getAttribute("javax.servlet.error.exception") != null)
        ex = (Throwable) request.getAttribute("javax.servlet.error.exception");

    //记录日志
    Logger logger = LoggerFactory.getLogger("500.jsp");
    logger.error(ex.getMessage(), ex);
%>

<head>
    <title>500 - 系统内部错误</title>
    <link href="${ctx}/static/default/css/core.css" rel="stylesheet" type="text/css"/>
    <style>

        .error_zm {
            width: 954px;
            height: 471px;
            background: url(${URL_LIB}/static/images/error500.jpg) no-repeat;
            margin: 0 auto;
        }

        .error_zm ul {
            float: right;
            width: 215px;
            margin: 265px 340px 0 0;
            display: inline;
        }

        .error_zm ul a {
            width: 215px;
            display: block;
            height: 35px;
        }
    </style>
</head>
<body style="background:none;">
<div class="error_zm">
    <ul>
        <a href="${URL_WWW}/" title="返回个人中心"></a>
        <a href="${URL_WWW}/discovery" title="去发现随便看看"></a>
    </ul>
</div>
<script type="text/javascript" src="${URL_LIB}/static/js/pv.js"></script>
</body>
</html>