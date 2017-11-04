<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>用户积分</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷积分管理 >> 用户积分</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">用户积分列表</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">

                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="6" class="error_msg_td">
                            <fmt:message key="${errorMsg}" bundle="${error}"/>
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table>
                <tr>
                    <td>
                        <form action="/point/userpoint/list" method="post">
                            <table>
                                <tr>
                                    <td height="1" class="default_line_td">
                                        输入用户账号:
                                    </td>
                                    <td height="1" class="edit_table_defaulttitle_td">
                                        <input name="profilename" type="text" value="${profilename}"/>
                                    </td>
                                    <td>
                                        <p:privilege name="/point/userpoint/list">
                                            <input type="submit" name="button" value="查询"/>
                                        </p:privilege>
                                    </td>
                                    <td height="1" class=>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </td>

                </tr>
            </table>
            <form action="/point/userpoint/list" method="post">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="6" class="default_line_td"></td>
                    </tr>
                    <tr class="list_table_title_tr">
                        <td nowrap align="left" width="100">用户积分</td>
                        <td nowrap align="left" width="">操作</td>
                    </tr>
                    <tr>
                        <td height="1" colspan="6" class="default_line_td"></td>
                    </tr>
                    <c:choose>
                        <c:when test="${userPointObj != null}">
                            <tr>
                                <input name="userno" value="${userPointObj.userNo}" type="hidden"/>
                                <td nowrap>${userPointObj.userPoint}</td>
                                <td nowrap>
                                    <a href="/point/pointactionhistory/list?profilid=${userPointObj.profileId}">查看积分明细</a>
                                </td>
                            </tr>
                            <tr>
                                <td height="1" colspan="6" class="default_line_td"></td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="6" class="error_msg_td">暂无数据!</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    <tr>
                        <td colspan="6" height="1" class="default_line_td"></td>
                    </tr>
                </table>
            </form>
        </td>
    </tr>
</table>
</body>
</html>