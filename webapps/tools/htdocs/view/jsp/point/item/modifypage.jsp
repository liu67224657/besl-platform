<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>修改</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>

    <script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/joymeappmenuhandler.js"></script>
    <script>
        $(document).ready(function() {
            $('#form_submit').bind('submit', function() {
                var type = $("#type").val();
                if (type == "1") {
                    var searcname = $("#searcname").val();
                    if (searcname.trim() == "") {
                        alert("请填写关键词");
                        return false;
                    }
                } else {
                    var aid = $("#aid").val();
                    if (aid.trim() == "") {
                        alert("请填写礼包ID");
                        return false;
                    }
                }
            });
        });
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 条目管理 >> 修改</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">修改</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/point/exchangegoods/modifygift" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td">

                            <input type="hidden" value="${itemId}" name="itemid"/>
                            <input type="hidden" value="${type}" name="type"/>
                        </td>
                    </tr>


                    <tr>
                        <td height="1" class="default_line_td">
                            <c:choose>
                                <c:when test="${type eq 1}">
                                    关键词:
                                </c:when>
                                <c:otherwise>
                                    礼包ID:
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td height="1">
                            <c:choose>
                                <c:when test="${type eq 1}">
                                    <input type="text" name="searchname" value="${item.title}" size="32" id=""/> <span
                                        style="color:red;"></span>
                                </c:when>
                                <c:otherwise>
                                    <input type="text" name="aid" size="32" id="aid" value="${item.directId}"/> <span
                                        style="color:red;"></span>
                                </c:otherwise>
                            </c:choose>


                            <c:if test="${not empty  errorMap}">
                                <span style="color:red">${errorMap}</span>
                            </c:if>
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