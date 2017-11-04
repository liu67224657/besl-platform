<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>添加新的积分墙</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript">
        $(document).ready(function (e) {
            <c:forEach items="${types}" var="item" >
            $("#pointKey").prepend("<option value='${item.code}'>${item.name}</option>");
            </c:forEach>

            <c:if test="${not empty  pointKey}" >
            $("#pointKey").val("${pointKey}");

            </c:if>

            <c:forEach items="${shopTypes}" var="item" >
            $("#shopKey").prepend("<option value='${item.code}'>${item.name}</option>");
            </c:forEach>

            <c:if test="${not empty  shopKey}" >
            $("#shopKey").val("${shopKey}");

            </c:if>


            <c:if test="${empty  nameExist}" >
            <c:forEach items="${list}" var="item" >
            $("#appkey").append("<option value='${item.appId}'>${item.appName}</option>");
            </c:forEach>
            </c:if>

            <c:if test="${not empty appkey && empty nameExist}" >
            $("#appkey").val("${appkey}");

            </c:if>


            //用于当appkey列表为空时，可手动输入，当手动输入重复时，可继续输入
            <c:if test="${ empty  list || not empty nameExist}" >


            $("#appkey").remove();
            $("#appkeytd").html('<input type="text" name="appkey" size="32"  id="appkey" value="${appkey}" />');

            </c:if>


            $("#submit").click(function () {

                var appkey = $.trim($("#appkey").val());
                var pointKey = $.trim($("#pointKey").val());
                var wallMoneyName = $.trim($("#wallMoneyName").val());

                if (appkey == '') {
                    alert("appkey不能为空或者都是空格");
                    return false;
                } else if (pointKey == '') {
                    alert("pointKey不能为空或者都是空格!");
                    return false;
                } else if (wallMoneyName == '') {
                    alert("平台货币名称不能为空或者都是空格!");
                    return false;
                } else {
                    $("#form_submit").submit();
                }


            });
        });
    </script>

</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 我的模块管理 >> 积分墙管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">添加新的积分墙</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(nameExist)>0}">
                    <tr>
                        <td height="1" colspan="14" class="error_msg_td">${nameExist}</td>
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
            <form action="/point/pointwall/wall/create" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" class="default_line_td">
                            appkey:
                        </td>
                        <td height="1" id="appkeytd">
                            <select id="appkey" name="appkey"/>*必填项
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td">
                            积分归属类型 :
                        </td>
                        <td height="1">
                            <select id="pointKey" name="pointKey"/>
                        <td height="1">
                        </td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td">
                            商城类型 :
                        </td>
                        <td height="1">
                            <select id="shopKey" name="shopKey"/>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            平台货币名称:
                        </td>
                        <td height="1">
                            <input type="text" name="wallMoneyName" size="32" id="wallMoneyName"
                                   value="<c:out value='${wallMoneyName}' escapeXml='true' />"/>*必填项
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            【我的】链接打开类型:
                        </td>
                        <td height="1">
                            <select name="template">
                                <option value="1">模板1</option>
                                <option value="2">模板2</option>
                                <option value="3">模板3</option>
                            </select>
                            *【我的】链接会打开对应的模板
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            模板类型查看:
                        </td>
                        <td height="1">
                            <a href="/static/images/pointwall/template1.PNG" target="_blank">模板1</a>
                            <a href="/static/images/pointwall/template2.PNG" target="_blank">模板2</a>
                            <a href="/static/images/pointwall/template3.PNG" target="_blank">模板3</a>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3" height="1" class="default_line_td"></td>
                    </tr>
                    <tr align="center">
                        <td colspan="3">
                            <input name="Submit" type="submit" id="submit" class="default_button" value="提交">
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