<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>历史上的列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="/static/include/My97DatePicker/WdatePicker.js"></script>
    <script language="JavaScript" type="text/JavaScript">

        $(document).ready(function () {


            $('#form_submit').bind('submit', function () {
                var msg_subject = $('#msg_subject').val();
                if (msg_subject.length == 0) {
                    alert('标题');
                    return false;
                }
                var input_sortmsg = $('#input_sortmsg').val();
                if (input_sortmsg.length == 0) {
                    alert('描述');
                    return false;
                }

                var select_rtype = $('#select_rtype').val();
                if (select_rtype.length == 0) {
                    alert('跳转类型');
                    return false;
                }
                var senddate = $('#senddate').val();
                if (senddate.length == 0) {
                    alert('日期');
                    return false;
                }


            });
        });


    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 大动漫管理 >> 大动漫历史上的今天</td>
    </tr>
<tr>
<td height="100%" valign="top"><br>
<table width="30%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td class="list_table_header_td">历史上的列表</td>
        <td class="">
            <form></form>
        </td>
    </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td height="1" class="default_line_td">
        </td>
    </tr>
</table>
<form action="/joymeapp/anime/history/modify" method="post" id="form_submit">
<table width="100%" border="0" cellspacing="1" cellpadding="0">
<tr>
    <td height="1" colspan="3" class="default_line_td">
        <input type="hidden" name="msgid" value="${msgDTO.pushMessage.pushMsgId}"/>
        <input type="hidden" name="appkey" value="${appkey}" id="input_appid"/>
    </td>
</tr>

<tr>
    <td height="1" class="default_line_td">
        标题:
    </td>
    <td height="1">
        <input id="msg_subject" type="text" name="subject" size="64" value="${msgDTO.pushMessage.msgSubject}">*必填
    </td>
    <td height="1">
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td">
        简述:
    </td>
    <td height="1">
        <textarea id="input_sortmsg" name="shortmessage" cols="40"
                  rows="5">${msgDTO.pushMessage.shortMessage}</textarea>*<span
            style="color: red">必填项</span>
    </td>
    <td height="1">
    </td>
</tr>

<c:choose>
    <c:when test="${appId == kada}">
        <tr>
            <td height="1" class="default_line_td">
                跳转类型:
            </td>
            <td height="1">
                <select name="rtype" id="select_rtype_${appId}">
                    <option value="">请选择</option>
                    <c:forEach var="type" items="${redirectTypes}">
                        <c:choose>
                            <c:when test="${type.code == msgDTO.pushMessage.options.list.get(0).type}">
                                <option value="${type.code}" selected="selected"><fmt:message
                                        key="anime.special.attr.${type.code}"
                                        bundle="${def}"/></option>
                            </c:when>
                            <c:otherwise>
                                <option value="${type.code}"><fmt:message key="anime.special.attr.${type.code}"
                                                                          bundle="${def}"/>
                                </option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>*<span style="color: red">必选项</span>
            </td>
            <td height="1">
            </td>
        </tr>
    </c:when>
    <c:when test="${appId == gamepictorial}">
        <tr>
            <td height="1" class="default_line_td">
                跳转类型：
            </td>
            <td height="1">
                <select name="rtype" id='select_rtype_${appId}'>
                    <option value="">请选择</option>
                    <c:forEach var="type" items="${redirectTypes}">
                        <option value="${type.code}"
                                <c:if test="${type.code == msgDTO.pushMessage.options.list.get(0).type}">selected="selected"</c:if>>
                            <fmt:message key="gamepictorial.push.type.${type.code}" bundle="${def}"/>
                        </option>
                    </c:forEach>
                </select>*<span style="color: red">必选项</span>
            </td>
        </tr>
    </c:when>
</c:choose>
<tr>
    <td height="1" class="default_line_td">
        跳转链接:
    </td>
    <td height="1">
        <input id="input_info" type="text" name="info" size="64"
               value="${msgDTO.pushMessage.options.list.get(0).info}"/><br/>
        <%--*<span style="color: red">必填项</span>--%>
        <c:choose>
            <c:when test="${appId == kada}">
                如果跳转类型是文章，请填写文章的ID，如果跳转类型是活动，请填写活动的ID，如果跳转类型是WAP页，请填写以"http://"开头的链接，如果是纯文本，可以不填
            </c:when>
            <c:when test="${appId == gamepictorial}">
                如果是纯文本，可以不填
                <br/>native跳转类型用新浪微博生成短链时，微博用json格式的链接填写，http://marticle.joyme.com/json/syhbao/pingguoduan/201408/2549463.html
            </c:when>
        </c:choose>
    </td>
    <td height="1">
    </td>
</tr>

    <tr>
        <td height="1" class="default_line_td">
            日期:
        </td>
        <td height="1">
            <input id="senddate" name="senddate" onClick="WdatePicker({autoPickDate:true})"
                   readonly="readonly"  value="<fmt:formatDate value="${msgDTO.pushMessage.sendDate}" pattern="yyyy-MM-dd"/>"/>

            *必填项
        </td>
        <td height="1">
        </td>
    </tr>


<tr>
    <td height="1" colspan="3" class="default_line_td"></td>
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