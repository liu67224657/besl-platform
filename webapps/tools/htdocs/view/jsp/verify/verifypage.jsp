<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<%@ taglib prefix="bit" uri="/WEB-INF/tags/bitwise.tld" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台数据管理、发布内容审核列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script language="JavaScript" type="text/JavaScript">
    </script>
</head>

<body>

<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 客户服务 >> 加V认证</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">>加V认证</td>
                </tr>
            </table>
            <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <form action="/cs/getp" method="POST">
                    <tr>
                        <td width="80" align="center">搜索条件</td>
                        <td>
                            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                                <tr>
                                    <td width="100" align="right" class="edit_table_defaulttitle_td">用户昵称：</td>
                                    <td class="edit_table_value_td">
                                        <input name="screenname" type="text" class="default_input_singleline" value="${screenname}">
                                        [暂不可模糊搜索]
                                    </td>
                                    <td width="100" align="right" class="edit_table_defaulttitle_td">个人博客URL：</td>
                                    <td class="edit_table_value_td">
                                        <input name="purl" type="text" class="default_input_singleline" size=60 value="${purl}">
                                        [条件输入其一即可]
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td width="80" align="center">
                            <input name="Button" type="submit" class="default_button" value=" 搜索 ">
                        </td>
                    </tr>
                </form>
            </table>
            <c:if test="${errorMsgMap['parameter']!=null}">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td class="error_msg_td">
                            <fmt:message key="${errorMsgMap['parameter']}" bundle="${error}"/>
                        </td>
                    </tr>
                </table>
            </c:if>
            <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <c:choose>
            <c:when test="${profileBlog != null || result eq 'f'}">
            <form action="/cs/verify">
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr class="list_table_title_tr">
                    <td nowrap align="center" width="30">昵称</td>
                    <td nowrap align="center">头像</td>
                    <td nowrap align="center">是否认证</td>
                    <td nowrap align="center">认证描述</td>
                    <td nowrap align="center">认证类型</td>
                    <td nowrap align="center">操作</td>
                </tr>
                <tr>
                    <input type="hidden" name="uno" value="${profileBlog.uno}">
                    <td>
                        ${profileBlog.screenName}
                    </td>
                    <td width="170">
                        <img src="${uf:parseMFace(profileBlog.headIcon)}" height="80" width="80" />
                    </td>
                    <td>
                        <c:choose >
                            <c:when test="${profileDetail.verifyType.code eq 'n' || profileDetail.verifyType.code eq null}">
                                否
                            </c:when>
                            <c:otherwise>
                                是
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td><textarea name="vdesc" cols="50" rows="3" class="default_input_singleline">${profileDetail.verifyDesc}</textarea></td>
                    <td>
                        <select name="vtype" class="default_select_single">
                            <option value="n" <c:if test="${profileDetail.verifyType.code eq 'n'}">selected="selected" </c:if>>暂未认证</option>
                            <option value="p" <c:if test="${profileDetail.verifyType.code eq 'p'}">selected="selected" </c:if>>个人认证</option>
                            <option value="c" <c:if test="${profileDetail.verifyType.code eq 'c'}">selected="selected" </c:if>>公司认证</option>
                            <option value="i" <c:if test="${profileDetail.verifyType.code eq 'i'}">selected="selected" </c:if>>行业人士</option>
                        </select>
                    </td>
                    <td><input type="submit" value="修改"> </td>
                </tr>
                <tr>
                    <td colspan="6" height="1" class="default_line_td"></td>
                </tr>
            </table>
            </form>
            </c:when>
            <c:otherwise>
                <c:if test="${result eq 's'}">操作成功！</c:if>
            </c:otherwise>
            </c:choose>
        </td>
    </tr>
</table>
</body>
</html>