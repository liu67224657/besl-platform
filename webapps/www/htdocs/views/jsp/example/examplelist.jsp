<%--
  Created by IntelliJ IDEA.
  User: zhaoxin
  Date: 12-3-27
  Time: 下午4:30
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>

<html>
<head>
    <title>example list</title>
</head>
<body>

<c:forEach var="example" items="${name}">
    <c:out value="${example}"></c:out>
</c:forEach>

</body>
</html>