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
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷积分管理 >> 积分历史明细</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">添加积分历史</td>
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
            <form action="/point/pointactionhistory/create" method="post">
                <input type="hidden" name="userno" value="${userno}"/>
                <input type="hidden" name="profileid" value="${profileid}"/>
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="6" class="default_line_td"></td>
                    </tr>
                    <tr class="list_table_title_tr">
                        <td height="1" class="default_line_td" width="200px">
                            用户账号
                        </td>
                        <td height="1" class="default_line_td" width="200px">
                            ${profilename}
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="200px">
                            积分历史类型
                        </td>
                        <td height="1" class="default_line_td" width="200px">
                            <input type="hidden" name="actiontype" value="${type.code}"/>
                            <fmt:message key="def.point.actiontype.${type.code}" bundle="${def}"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="200px">
                            积分历史描述
                        </td>
                        <td height="1" class="default_line_td" width="200px">
                            <textarea name="actiondescription" rows="5" cols="51" style="width: 418px;float: left"
                                      class="default_input_multiline"></textarea>
                        </td>
                    </tr>
                    <tr class="list_table_title_tr">
                        <td height="1" class="default_line_td" width="200px">
                            添加积分的类型
                        </td>
                        <td height="1" class="default_line_td" width="200px">
                            <select name="pointkey">
                                <c:forEach items="${pointKeyTypes}" var="key">
                                    <option value="${key.code}">${key.name}</option>
                                </c:forEach>

                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="200px">
                            积分值
                        </td>
                        <td height="1" class="default_line_td" width="200px">
                            <input name="pointvalue" type="text"/>
                        </td>
                    </tr>
                    <tr align="center">
                        <td colspan="3">
                            <input name="Submit" type="submit" class="default_button" value="提交">
                            <input name="Reset" type="button" class="default_button" value="返回"
                                   onclick="javascipt:window.history.go(-1);">
                        </td>
                    </tr>
                </table>
            </form>
        </td>
    </tr>
</table>
</body>
</html>