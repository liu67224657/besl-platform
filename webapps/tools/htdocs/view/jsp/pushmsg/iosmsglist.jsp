<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>推送消息列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/dhtmlxcalendar.css">
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/skins/dhtmlxcalendar_dhx_skyblue.css">
    <script src="/static/include/dhtmlxcalendar/dhtmlxcalendar.js"></script>
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script language="JavaScript" type="text/JavaScript">
        var myCalendar;

        function doOnLoad() {
            myCalendar = new dhtmlXCalendarObject(["pushStartDate", "pushEndDate"]);
        }


        function add() {
            window.location.href = "/msg/preaddpushmsg";
        }

        function push(pushMsgId) {
            var serviceType = document.getElementById("serviceType").value;
            window.location.href = "/msg/push?pushMsgId=" + pushMsgId + "&serviceType=" + serviceType;
        }

        function edit(pushMsgId) {
            window.location.href = "/msg/preeditmsg?pushMsgId=" + pushMsgId ;
        }
    </script>

</head>

<body onload="doOnLoad();">


<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> IOS推送消息</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">>推送消息列表</td>
                </tr>
            </table>
            <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <form action="/msg/msglist" method="POST">
                    <tr>
                        <td width="80" align="center">搜索条件</td>
                        <td><table width="100%"  border="0" cellspacing="1" cellpadding="0">
                            <tr>
                                <td width="100" align="right" class="edit_table_defaulttitle_td">推送时间：</td>
                                <td class="edit_table_value_td">
                                    <input name="pushStartDate" id="pushStartDate" type="text" class="default_input_singleline" size="8" maxlength="10" value="${pushStartDate}">
                                    -
                                    <input name="pushEndDate" id="pushEndDate" type="text" class="default_input_singleline" size="8" maxlength="10" value="${pushEndDate}">
                                    </td>
                                <td width="100" align="right" class="edit_table_defaulttitle_td">发送结果：</td>
                                <td class="edit_table_value_td">
                                    <select name="sendStatus" class="default_select_single">
                                        <c:forEach items="${sendStatuses}" var="status">
                                            <option value="${status.code}" <c:if test="${status.code eq sendStatus}">selected="selected"</c:if>><fmt:message key="def.actstatus.${status.code}.name" bundle="${def}"/></option>
                                        </c:forEach>
                                    </select></td>
                            </tr>
                        </table></td>
                        <td width="80" align="center">
                            <input name="Button" type="submit" class="default_button" value=" 搜索 ">
                        </td>
                    </tr>
                </form>
            </table>
            <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                <tr class="toolbar_tr">
                    <td>
                        <p:privilege name="/msg/preaddpushmsg">
                            <input type="button" value="新增待发送消息" onclick="add();" class="default_button"/>
                        </p:privilege>
                    </td>
                </tr>
            </table>
            <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr class="list_table_title_tr">
                    <td align="center" width="40">选择</td>
                    <td align="center">接收者类型</td>
                    <td align="center">消息类型</td>
                    <td align="left">消息主体</td>
                    <td align="left">参数</td>
                    <td align="center">选择服务器类型</td>
                    <td align="center">发送状态</td>
                    <td align="center">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="8" class="default_line_td"></td>
                </tr>
                <form action="/msg/batchpushmsg" method="POST" name="batchform">
                    <input name="pushStartDate" type="hidden" value="${pushStartDate}"/>
                    <input name="pushEndDate" type="hidden" value="${pushEndDate}"/>
                    <input name="sendStatus" type="hidden" value="${sendStatus}"/>
                    <input name="pager.offset" type="hidden" value="${page.startRowIdx}"/>
                    <input name="items" type="hidden" value="${page.totalRows}"/>
                    <c:choose>
                        <c:when test="${rows.size() > 0}">
                            <c:forEach items="${rows}" var="ct" varStatus="st">
                                <tr class="
                                    <c:choose>
                                    <c:when test="${st.index % 2 == 0}">
                                       list_table_opp_tr
                                    </c:when>
                                    <c:otherwise>
                                        list_table_even_tr
                                    </c:otherwise>
                                    </c:choose>">
                                    <td align="center">
                                        <input type="checkbox" name="pushmsgids" value="${ct.pushMsgId}">
                                    </td>
                                    <td align="center">
                                        ${ct.pushMessageType.code}
                                    </td>
                                    <td align="center">
                                        ${ct.pushMsgCode.code}
                                    </td>
                                    <td align="left">
                                        ${ct.msgBody}
                                    </td>
                                    <td align="left">
                                        ${ct.msgOptions.toJson()}
                                    </td>
                                    <td align="center">
                                        <select name="serviceType" class="default_select_single" id="serviceType">
                                            <option value="test">内部测试</option>
                                            <option value="product">正式用户</option>
                                        </select>
                                    </td>
                                    <td align="center">
                                        <fmt:message key="def.actstatus.${ct.sendStatus.code}.name" bundle="${def}"/>
                                    </td>
                                    <td align="center">
                                        <p:privilege name="/msg/push">
                                            <input type="button" value="推送" class="default_button" onclick="push('${ct.pushMsgId}');">
                                        </p:privilege>
                                        <c:if test="${ct.sendStatus.code eq 'n'}">
                                            <p:privilege name="/msg/preeditmsg">
                                                <input type="button" value="修改" class="default_button" onclick="edit('${ct.pushMsgId}');">
                                            </p:privilege>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                            <tr>
                                <td colspan="8" height="1" class="default_line_td"></td>
                            </tr>
                            <tr class="toolbar_tr">
                                <td colspan="8">
                                    <input type="checkbox" name="selectall" value="1"
                                           onclick='javascript:checkall(document.forms["batchform"].pushmsgids, document.forms["batchform"].selectall)'>全选
                                    <input type="checkbox" name="uncheck" value="1" onclick='javascript:convertcheck(document.forms["batchform"].pushmsgids)'>反选
                                    将选中记录用户状态改成：
                                    <select name="serviceType" class="default_select_single">
                                        <option value="">--请选择--</option>
                                        <option value="test" >测试服务器</option>
                                        <option value="product" >正式服务器</option>
                                    </select>
                                    <p:privilege name="/msg/push">
                                        <input name="update" type="submit" class="default_button" value="批量推送">
                                    </p:privilege>
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="8" class="error_msg_td">暂无数据!</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    <tr>
                        <td colspan="8" height="1" class="default_line_td"></td>
                    </tr>
                    <c:if test="${page.maxPage > 1}">
                        <tr>
                            <td colspan="8" height="1" class="list_table_opp_tr">
                                <LABEL>
                                    <pg:pager url="/msg/msglist"
                                              items="${page.totalRows}" isOffset="true"
                                              maxPageItems="${page.pageSize}"
                                              export="offset, currentPageNumber=pageNumber" scope="request">

                                        <pg:param name="pushStartDate" value="${pushStartDate}"/>
                                        <pg:param name="pushEndDate" value="${pushEndDate}"/>
                                         <pg:param name="sendStatus" value="${sendStatus}"/>
                                        <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                        <pg:param name="items" value="${page.totalRows}"/>
                                        <%@ include file="/WEB-INF/jsp/toolspg.jsp" %>
                                    </pg:pager>
                                </LABEL>
                            </td>
                        </tr>
                    </c:if>
                </form>
            </table>
        </td>
    </tr>
</table>
</body>
</html>