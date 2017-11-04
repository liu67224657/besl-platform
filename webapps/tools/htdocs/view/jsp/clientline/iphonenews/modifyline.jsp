<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head><title>Simple jsp page</title>
    <script charset="utf-8" src="/static/include/kindeditor/kindeditor-min.js"></script>
    <script charset="utf-8" src="/static/include/kindeditor/lang/zh_CN.js"></script>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script>
        $().ready(function() {
            $('#form_submit').bind('submit', function() {
                var name = $('#input_text_name').val();
                if (name.length == 0) {
                    alert("请填写名称");
                    return false;
                }
                var code = $('#input_text_code').val();
                if (code.length == 0) {
                    alert("请填写code");
                    return false;
                }
                var type = $('#select_type').val();
                if (type.length == 0) {
                    alert("请选择类型");
                    return false;
                }
            });
        });
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 手游画报 >> 手游画报iphone</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td"><span
                            style="font-size:16px; font-family: Arial, Helvetica, sans-serif; font-weight:bold;">修改一条Line</span>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/clientline/iphone/news/modify" method="post" id="form_submit">
                <table width="90%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" class="">
                            <input type="hidden" name="lineid" value="${clientLine.lineId}"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            名称
                        </td>
                        <td height="1" class="" width="10">
                            <input type="text" name="linename" size="20" id="input_text_name"
                                   value="${clientLine.lineName}"/> *必填项
                        </td>
                        <td height="1" align="left"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            编码:
                        </td>
                        <td height="1" class="">
                            <input id="input_text_code" type="text" size="20" name="linecode"
                                   value="${clientLine.code}">*必填项
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td>
                            <c:if test="${fn:length(codeExist)>0}">
                                <span style="color: red" id="span_name_exist"><fmt:message key="${codeExist}" bundle="${def}"/></span>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            类型:
                        </td>
                        <td height="1">
                            <select name="itemtype" id="select_type">
                                <option value="">请选择</option>
                                <c:forEach items="${itemTypeCollection}" var="type">
                                    <option value="${type.code}"
                                            <c:if test="${clientLine.itemType.code == type.code}">selected="selected"</c:if>>
                                        <fmt:message key="client.item.type.${type.code}" bundle="${def}"/></option>
                                </c:forEach>
                            </select>*必选项
                        </td>
                        <td height="1"></td>
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