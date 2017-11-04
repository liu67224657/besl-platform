<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>添加推送证书</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script>
        $(document).ready(function () {
//            $('#form_submit').bind('submit', function () {
//                var appNameVal = $('#input_text_appname').val();
//                if (appNameVal.length == 0) {
//                    alert('请填写APP名称');
//                    return false;
//                }
//            });
        });
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 小端APP >> 推送证书列表</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">修改证书</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="14" class="error_msg_td">${errorMsg}</td>
                    </tr>
                </c:if>
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/joymeapp/certificate/modify" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td">
                            <input type="hidden" name="queryappid" value="${queryAppId}"/>
                            <input type="hidden" name="queryplatform" value="${queryPlatform}"/>
                            <input type="hidden" name="cid" value="${appCertificate.deploymentId}"/>
                            <input type="hidden" name="offset" value="${pageStartIndex}"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            资源路径:
                        </td>
                        <td height="1">
                            <input type="text" name="path" size="32" id="input_text_path" value="${appCertificate.path}"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            密码:
                        </td>
                        <td height="1">
                            <input type="text" name="password" size="32" id="input_text_password" value="${appCertificate.password}"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            是否产品:
                        </td>
                        <td height="1">
                            <select name="isproduct">
                                <option value="true" <c:if test="${appCertificate.isProduct}">selected="selected"</c:if>>是</option>
                                <option value="false" <c:if test="${!appCertificate.isProduct}">selected="selected"</c:if>>否</option>
                            </select>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            APP:
                        </td>
                        <td height="1">
                            <select name="appkey" id="select_appkey">
                                <option value="">请选择</option>
                                <c:forEach items="${appList}" var="app">
                                    <option value="${app.appId}" <c:if test="${app.appId == appCertificate.appkey}">selected="selected"</c:if>>${app.appName}</option>
                                </c:forEach>
                            </select>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            选择平台:
                        </td>
                        <td height="1">
                            <select name="platform">
                                <c:forEach var="plat" items="${platformList}">
                                    <option value="${plat.code}" <c:if test="${plat.code == appCertificate.appPlatform.code}">selected="selected"</c:if>>
                                        <fmt:message key="joymeapp.platform.${plat.code}" bundle="${def}"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3" height="1" class="default_line_td"></td>
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