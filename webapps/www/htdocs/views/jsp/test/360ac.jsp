<%--
  Created by IntelliJ IDEA.
  User: ericliu
  Date: 13-9-4
  Time: 上午10:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Simple jsp page</title></head>
<body>
result:${result}<br/>

accessToken:${at}<br/>
refreshToken:${rt}<br/>
 <a href="/oautht/360/refreshtoken?rt=${rt}">refresh token</a>
</body>
</html>