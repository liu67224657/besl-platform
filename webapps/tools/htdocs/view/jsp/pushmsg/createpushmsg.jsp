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
    <title>新增待发送消息</title>
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

        function change(a){
            var xxx = document.getElementById("xxx");
            var divArray = xxx.getElementsByTagName("div");
            for (var i=0;i<divArray.length;i++) {
                if (divArray[i].id == a) {
                    divArray[i].style.display='';
                }else {
                    divArray[i].style.display='none';
                }
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
        <td height="22" class="page_navigation_td">>> <a href="/msg/msglist" >手机消息推送</a> >> 新增待发送消息</td>
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
                <form action="/msg/addpushmsg" method="post">
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">填写消息主体内容：</td>
                        <td nowrap class="edit_table_value_td">
                            <textarea rows="5" cols="100" name="msgBody" class="default_input_multiline"></textarea>
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">请选择接收者类型：</td>
                        <td nowrap class="edit_table_value_td">
                            <select name="pushMessageType" class="default_select_single">
                                <c:forEach items="${pushMessageTypes}" var="pushMessageType">
                                    <option value="${pushMessageType.code}"><fmt:message key="def.pushmsg.type.${pushMessageType.code}.name" bundle="${def}"/></option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">请选消息类型：</td>
                        <td nowrap class="edit_table_value_td">
                            <select name="pushMessageCode" class="default_select_single" onchange="change(this.value);">
                                <c:forEach items="${pushMsgCodes}" var="pushMsgCode">
                                    <option value="${pushMsgCode.code}"><fmt:message key="def.pushmsg.code.${pushMsgCode.code}.name" bundle="${def}"/></option>
                                </c:forEach>
                            </select>
                            <c:if test="${errorMsgMap.containsKey('endip')}">
                                <fmt:message key="${errorMsgMap['endip']}" bundle="${error}"></fmt:message>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">填写相应的参数：</td>
                        <td nowrap class="edit_table_value_td">
                        <div id="xxx">
                            <div id="content">
                                文章URL：<input type="text" name="url" class="default_input_singleline">
                            </div>
                            <div id="news" style="display:none ">
                                分类角度：<input type="text" name="categoryAspect" class="default_input_singleline"> <br/>
                                分类编码：<input type="text" name="categoryCode" class="default_input_singleline">
                            </div>
                            <div id="version" style="display:none ">

                            </div>
                        </div>
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