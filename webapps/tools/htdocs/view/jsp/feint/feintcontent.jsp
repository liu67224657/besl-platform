<%@ include file="/view/jsp/common/taglibs.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <%
        //remove cache
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
    %>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>模拟文章喜欢操作</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/dhtmlxcalendar.css">
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/skins/dhtmlxcalendar_dhx_skyblue.css">
    <script src="/static/include/dhtmlxcalendar/dhtmlxcalendar.js"></script>
    <script>
        var myCalendar;
        function doOnLoad() {
            myCalendar = new dhtmlXCalendarObject(["startDate"]);
            myCalendar.setDateFormat("%Y-%m-%d %H:%i:%s");
        }
    </script>
    <script language="JavaScript" type="text/javascript">
        function window_onload() {
            //表单的第一个元素获得焦点
            Form.focusFirstElement("form1");
        }

        function checkNum() {
            var re = /^[1-9]+[0-9]*]*$/;
            //判断正整数 /^[0-9]+.?[0-9]*$/
            var input = document.getElementById("count");
            if (!re.test(input.value)) {
                alert("输入类型必须为整数");
                return false;
            }

            var url = document.getElementById("url");
            if(trim(url).length==0){
                alert("文章链接不能为空");
                return false;
            }

            return true;
        }

    </script>
</head>

<body onload="doOnLoad();">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 模拟文章喜欢操作</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="edit_table_header_td">&gt;新增待发送消息</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <form action="/feint/addcontent" method="post">
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">模拟数量：</td>
                        <td nowrap class="edit_table_value_td">
                            <input type="text" name="count" id="count" class="default_input_singleline">
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">开始时间：</td>
                        <td nowrap class="edit_table_value_td">
                            <input type="text" id="startDate" name="startDate" maxlength="19" class="default_input_singleline">
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">文章URL：</td>
                        <td nowrap class="edit_table_value_td">
                            <input type="text" name="url" id="url" class="default_input_singleline" size="80"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${message}
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
                        </td>
                    </tr>
                </form>
            </table>
        </td>
    </tr>
</table>
</body>
</html>