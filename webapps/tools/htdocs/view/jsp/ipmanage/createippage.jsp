<%@ include file="/view/jsp/common/taglibs.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <%
        //remove cache
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
    %>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>IP管理、新增被屏蔽的IP</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>

    <script language="JavaScript" type="text/javascript">
        function window_onload() {
            //表单的第一个元素获得焦点
            Form.focusFirstElement("form1");
        }

        function checkNum() {
            var re = /^[1-9]+[0-9]*]*$/;
            //判断正整数 /^[0-9]+.?[0-9]*$/
            var input = document.getElementById("num");
            if (!re.test(input.value)) {
                alert("[屏蔽时间]输入类型必须为整数");
                return false;
            } else {
                return true;
            }
        }

        function back() {
            window.location.href = "/privilege/res/reslist";
        }
    </script>
</head>

<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> IP管理 >> 增加被屏蔽的IP</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="edit_table_header_td">增加IP</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <form action="/cs/ipmanage/increaseip" method="post">
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">起始IP：</td>
                        <td nowrap class="edit_table_value_td">
                            <input name="startip" type="text" class="default_input_singleline" size="24" maxlength="64">
                            <c:if test="${errorMsgMap.containsKey('startip')}">
                                <fmt:message key="${errorMsgMap['startip']}" bundle="${error}"></fmt:message>
                            </c:if>
                        </td>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">描述：</td>
                        <td nowrap class="edit_table_value_td">
                            <input name="desc" type="text" class="default_input_singleline" size="32" maxlength="100">
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">结束IP：</td>
                        <td nowrap class="edit_table_value_td">
                            <input name="endip" type="text" class="default_input_singleline" size="24" maxlength="64">
                            <c:if test="${errorMsgMap.containsKey('endip')}">
                                <fmt:message key="${errorMsgMap['endip']}" bundle="${error}"></fmt:message>
                            </c:if>
                        </td>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">屏蔽时间：</td>
                        <td nowrap class="edit_table_value_td">
                            <input name="num" type="text" class="default_input_singleline" size="4" maxlength="4" id="num" value="${num}">
                            <select name="datetype" class="default_select_single">
                                <option value="day"
                                        <c:if test="${datetype eq 'day'}">selected="true"</c:if>
                                        >天</option>
                                <option value="week"
                                        <c:if test="${datetype eq 'week'}">selected="true"</c:if>
                                        >周</option>
                                <option value="month"
                                        <c:if test="${datetype eq 'month'}">selected="true"</c:if>
                                        >月</option>
                                <option value="year"
                                        <c:if test="${datetype eq 'year' || datetype == null || datetype eq ''}">selected="true"</c:if>
                                        >年</option>
                            </select>
                            <c:if test="${errorMsgMap.containsKey('num')}">
                                <fmt:message key="${errorMsgMap['num']}" bundle="${error}"></fmt:message>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" colspan="4" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td colspan="4">&nbsp;</td>
                    </tr>
                    <tr align="center">
                        <td colspan="4">
                            <input name="submit" type="submit" class="default_button" value="提交" onclick="return checkNum();">
                            <input name="reset" type="reset" class="default_button" value="重置">
                            <input name="button" type="button" class="default_button" value="返回" onClick="history.back()">
                        </td>
                    </tr>
                </form>
            </table>
        </td>
    </tr>
</table>
</body>
</html>