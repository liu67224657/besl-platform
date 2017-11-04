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
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/joymeappmenuhandler.js"></script>
    <script>
        function isNotNull() {
            var menuName = $("#input_text_tagname").val();

            if (menuName == '') {
                alert("标签名称不能为空!");
                return false;
            }
        }

    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">

    <tr>
        <td height="100%" valign="top">
            <form action="/joymeapp/menu/tag/modify" method="post" id="form_submit" onsubmit="return isNotNull();">
                <input type="hidden" name="menuid" value="${tag.topMenuId}"/>
                <input type="hidden" name="tagid" value="${tag.tagId}"/>
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td">
                            标签名称:
                        </td>
                        <td height="1" class="edit_table_defaulttitle_td">
                            <input id="input_text_tagname" type="text" name="name" size="32" value="${tag.tagName}"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            排序值:
                        </td>
                        <td height="1" class="edit_table_defaulttitle_td">
                            <input id="input_text_displayorder" type="text" name="displayorder" size="32" value="${tag.displayOrder}"/>
                        </td>
                        <td height="1" class=>*填入整数越小越在前面
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            状态:
                        </td>
                        <td height="1" class="edit_table_defaulttitle_td">
                            <select name="removestatus">
                                <c:forEach var="status" items="${removeStatusSet}">
                                    <option value="${status.code}" <c:if test="${status.code eq tag.removeStatus.code}">selected</c:if>>
                                        <fmt:message key="joymeapp.menu.status.${status.code}"
                                                     bundle="${def}"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                        <td height="1" class=>
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