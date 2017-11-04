<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>创建APP</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script>
        $(document).ready(function() {
            $('#form_submit').bind('submit', function() {
                var versionUrlVal = $('#input_text_version_url').val();
                if (versionUrlVal.length == 0) {
                    alert('请填写版本地址');
                    return false;
                }
                var versionInfoVal = $('#textarea_wersion_info').val();
                if (versionInfoVal.length == 0) {
                    alert('请填写版本信息');
                    return false;
                }

                var ptype = $('#select_packagetype').val();
                if (ptype.length == 0) {
                    alert('请选择内容包类型');
                    return false;
                }

                var nessUpdate = $('#select_necessaryupdate').val();
                if (nessUpdate.length == 0) {
                    alert('请选择是否强制更新');
                    return false;
                }

            });
        });
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷APP >> APP版本内容信息</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">编辑APP版本</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/joymeapp/updatecontent/modify" method="post" id="form_submit">
                <input type="hidden" name="vid" value="${versionInfo.id}"/>
                <input type="hidden" name="appkey" value="${app.appId}"/>

                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td">
                            APPKey:
                        </td>
                        <td height="1" class="edit_table_defaulttitle_td">
                            ${app.appName}
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td">
                            版本地址:
                        </td>
                        <td height="1" class="edit_table_defaulttitle_td">
                            <input id="input_text_version_url" size="70" type="text" name="version_url"
                                   value="${versionInfo.version_url}"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            版本信息:
                        </td>
                        <td height="1" class="edit_table_defaulttitle_td">
                            <textarea id="textarea_wersion_info" name="version_info" rows="5" cols="51"
                                      style="width: 418px;float: left"
                                      class="default_input_multiline">${versionInfo.version_info}</textarea>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            版本类型:
                        </td>
                        <td height="1" class="edit_table_defaulttitle_td">
                            <select name="packagetype" id="select_packagetype">
                                <option value="">请选择</option>
                                <c:forEach var="packageType" items="${packageTypes}">
                                    <option value="${packageType.code}" <c:if test="${versionInfo.packageType==packageType.code}">selected</c:if>><fmt:message key="joymeapp.contentversion.packagetype.${packageType.code}" bundle="${def}"/></option>
                                </c:forEach>
                            </select>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            是否必须更新:
                        </td>
                        <td height="1" class="edit_table_defaulttitle_td">
                            <select name="necessaryupdate" id="select_necessaryupdate">
                                <option value="">请选择</option>
                                <option value="true" <c:if test="${versionInfo.necessaryUpdate}">selected</c:if>>是
                                </option>
                                <option value="false" <c:if test="!${versionInfo.necessaryUpdate}">selected</c:if>>否
                                </option>
                            </select>
                        </td>
                        <td height="1" class=>
                            *是否必须更新只针对<b>完整包</b>，请慎重填写
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