<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>新游帐号详情</title>
    <script charset="utf-8" src="/static/include/kindeditor/kindeditor-min.js"></script>
    <script charset="utf-8" src="/static/include/kindeditor/lang/zh_CN.js"></script>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script>
        $().ready(function() {
            $('#form_submit').bind('submit', function() {
                var reason = $('#input_reason').val();
                if (reason.length == 0 || reason.length > 150) {
                    alert("认证理由不能为空，最多150个字");
                    return false;
                }
            });
        });
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
    <td height="22" class="page_navigation_td">>> 运营管理 >> 条目管理 >> 新游帐号管理</td>
</tr>
<tr>
    <td height="100%" valign="top"><br>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td class="list_table_header_td">新游帐号详情</td>
            </tr>
        </table>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td height="1" class="default_line_td"></td>
            </tr>
        </table>
        <form action="/developers/verify" method="post" id="form_submit">
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td>
                        <input type="hidden" name="verifytype" value="${dto.detail.verifyType.code}"/>
                        <input type="hidden" name="verifydesc" value="${dto.developer.verifyDesc}"/>
                        <input type="hidden" name="uno" value="${dto.developer.uno}"/>
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        帐号:
                    </td>
                    <td height="1">
                        ${dto.blog.screenName}
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td" width="10%">
                        uno:
                    </td>
                    <td height="1">
                        ${dto.developer.uno}
                    </td>
                </tr>

                <tr>
                    <td height="1" class="default_line_td">
                        认证说明:
                    </td>
                    <td height="1">
                        ${dto.developer.verifyDesc}
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        认证信息:
                    </td>
                    <td height="1">
                        <fmt:message key="profile.verify.type.${dto.detail.verifyType.code}" bundle="${def}"/>
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        联系人:
                    </td>
                    <td height="1">
                        ${dto.developer.contacts}
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        联系邮箱:
                    </td>
                    <td height="1">
                        ${dto.developer.email}
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        联系人QQ:
                    </td>
                    <td height="1">
                        ${dto.developer.qq}
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        联系人电话:
                    </td>
                    <td height="1">
                        ${dto.developer.phone}
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        公司:
                    </td>
                    <td height="1">
                        ${dto.developer.company}
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        地址:
                    </td>
                    <td height="1">
                        ${dto.developer.location.province.name}&nbsp;${dto.developer.location.city.name}
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        执照:
                    </td>
                    <td height="1">
                        <img src="${dto.developer.licensePic}"/>
                    </td>
                </tr>
                <tr>
                    <td height="1" colspan="3" class="default_line_td"></td>
                </tr>

                <tr>
                    <td height="1" class="default_line_td">
                        申请时间:
                    </td>
                    <td height="1">
                        <fmt:formatDate value="${dto.developer.createDate}" pattern="yyyy年MM月dd日 HH:mm:ss"/>
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        申请IP:
                    </td>
                    <td height="1">
                        ${dto.developer.createIp}
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        审核状态:
                    </td>
                    <td height="1"
                            <c:choose>
                                <c:when test="${dto.developer.verifyStatus.code == 'access'}">
                                    style="color:#008000;"
                                </c:when>
                                <c:when test="${dto.developer.verifyStatus.code == 'remove'}">
                                    style="color:#808080;"
                                </c:when>
                                <c:when test="${dto.developer.verifyStatus.code == 'disapproved'}">
                                    style="color:#ff0000;"
                                </c:when>
                            </c:choose>>
                        <fmt:message key="profile.developer.status.${dto.developer.verifyStatus.code}"
                                     bundle="${def}"/>
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        审核时间:
                    </td>
                    <td height="1">
                        <fmt:formatDate value="${dto.developer.verifyDate}" pattern="yyyy年MM月dd日 HH:mm:ss"/>
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        审核人IP:
                    </td>
                    <td height="1">
                        ${dto.developer.verifyIp}
                    </td>
                </tr>
                <tr>
                    <td colspan="3" height="1" class=""></td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td">
                        审核理由:
                    </td>
                    <td height="1">
                        <input type="text" name="reason" id="input_reason" value="${dto.developer.verifyReason}"/>*必填项
                    </td>
                </tr>
                <tr>
                    <td colspan="3" height="1" class=""></td>
                </tr>
                <tr align="center">
                    <td colspan="3">
                        <select name="status">
                            <option value="">修改审核状态</option>
                            <c:forEach items="${statusCollection}" var="status">
                                <option value="${status.code}"><fmt:message
                                        key="profile.developer.status.${status.code}" bundle="${def}"/></option>
                            </c:forEach>
                        </select>
                        <input type="submit" value="提交" name="submit" class="default_button"/>
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