<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>编辑属于积分墙${appkey}的某个app</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript">
        $(document).ready(function (e) {

            $("#platform").prop("value", ${platform});
            $("#hotStatus").prop("value", ${hotStatus});
            $("#status").prop("value", '${status}');
            $("#submit").click(function () {


                var displayOrder = $.trim($("#displayOrder").val());
                var pointAmount = $.trim($("#pointAmount").val());


                if (displayOrder == '' || displayOrder == 0 || isNaN(displayOrder)) {
                    alert("排序值不能为空或者为0,且必须是数字!");
                    return false;
                } else if (pointAmount == '' || pointAmount == 0 || isNaN(pointAmount)) {
                    alert("下载可获得点数不能为空或者为0,且必须是数字!");
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
                    <td class="list_table_header_td">编辑积分墙${appkey}(${appKeyName})的某个app</td>
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
            <form action="/point/pointwall/wall/appmodify" method="post" id="form_submit">
                <input type="hidden" name="appkey" value="${appkey}" />
                <input type="hidden" name="platform" value="${platform}" />
                <input type="hidden" name="wallAppId" value="${wallAppId}" />

                <table width="100%" border="0" cellspacing="1" cellpadding="0">

                    <tr>
                        <td height="1" class="default_line_td">
                            应用的包名或者bundleid:
                        </td>
                        <td height="1">
                            <input type="text"  size="32" readonly="readonly" disabled="disabled"
                                   value="<c:out value='${packageName}' escapeXml='true' />"/>*不可修改
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            应用的名称:
                        </td>
                        <td height="1">
                            <input type="text"  size="32" readonly="readonly" disabled="disabled"
                                   value="<c:out value='${appName}' escapeXml='true' />"/>*不可修改
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            应用所在积分墙对应的appkey:
                        </td>
                        <td height="1">
                            <input type="text" size="32" readonly="readonly" disabled="disabled"
                                   value="<c:out value='${appkey}' escapeXml='true' />"/>*不可修改
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                                        <td height="1" class="default_line_td">
                                            含用积分墙的应用名称:
                                        </td>
                                        <td height="1">
                                            <input type="text" size="32" readonly="readonly" disabled="disabled"
                                                   value="<c:out value='${appKeyName}' escapeXml='true' />"/> *不可修改
                                        </td>
                                        <td height="1" >
                                        </td>
                                    </tr>

                    <tr>
                        <td height="1" class="default_line_td">
                            app所属平台
                        </td>
                        <td height="1">
                            <select id="platform" readonly="readonly" disabled="disabled">
                                <option value="0">ios</option>
                                <option value="1">android</option>
                            </select>*不可修改
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            app在此积分墙中的排序值:
                        </td>
                        <td height="1">
                            <input type="text" name="displayOrder" size="32" id="displayOrder"
                                   value="<c:out value='${displayOrder}' escapeXml='true' />"/>*必填项
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            是否热门:
                        </td>
                        <td height="1">
                            <select id="hotStatus" name="hotStatus">
                                <option value="1">热门</option>
                                <option value="0">非热门</option>
                            </select>*必填项
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            下载可以获得的点数
                        </td>
                        <td height="1">
                            <input type="text" name="pointAmount" size="32" id="pointAmount"
                                   value="<c:out value='${pointAmount}' escapeXml='true' />"/>*必填项
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            状态
                        </td>
                        <td height="1">
                            <select id="status" name="status">
                                <option value="valid">显示</option>
                                <option value="invalid">不显示</option>
                                <option value="removed">已删除</option>
                            </select>*必填项
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