<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>添加模块</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/jquery-1.11.2.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>

</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 直播权限管理 >> 添加权限</td>
    </tr>
    <c:if test="${fn:length(errorMsg)>0}">
        <tr>
            <td height="1" colspan="13" class="error_msg_td">
                ${errorMsg}
            </td>
        </tr>
     </c:if>
    <tr>
        <td height="100%" valign="top"><br>
            <form action="/comment/manage/create" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" class="default_line_td">
                            昵称:
                        </td>

                        <td height="1">
                            <input type="text" name="nick" size="20" id="input_text_name"/>
                        </td>
                        
                    </tr>
                    
                    <tr>
                    <td height="1" align="left"><input type="submit"></td>
                    </tr>
                    
                </table>
            </form>
        </td>
    </tr>
</table>

</body>
</html>