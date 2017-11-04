<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <meta http-equiv="Refresh" content="3;url=/viewline/linelist?categoryId=${categoryId}">
    <title>后台数据管理、复制Line到目标Line中</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script language="JavaScript" type="text/JavaScript">
        function copy(id) {
            window.location.href = "";
        }
    </script>
</head>
<body>
    <div align="center">复制成功，有${failure}条记录因为重复没有插入成功</div>
</body>
</html>