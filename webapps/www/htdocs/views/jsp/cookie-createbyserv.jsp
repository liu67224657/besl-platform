<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<html>
<head>
    <title>cookie-getbyclient</title>
</head>
<body>

客户端生成cookie&nbsp;&nbsp;test:<b>${cookievalue}</b>
<br/>
服务端生成cookie&nbsp;&nbsp;createbyserver:<b>${createbyserver}</b>

<br/>

<a href="/test/cookie/createbyclient">返回</a>

</body>
</html>