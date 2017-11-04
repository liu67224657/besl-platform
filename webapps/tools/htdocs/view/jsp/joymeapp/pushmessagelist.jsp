<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台消息推送列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 条目管理 >> 后台消息推送列表</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="20%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">后台消息推送列表</td>
                    <td class="">
                        <form action="/joymeapp/pushmessage/createpage" method="post">
                            <input type="submit" value="创建推送消息"/>
                        </form>

                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
               <c:if test="${fn:length(errorMsg)>0}">
                <tr>
                    <td height="1" colspan="9" class="error_msg_td">
                        <fmt:message key="${errorMsg}" bundle="${error}"/>
                    </td>
                </tr>
               </c:if>
                <tr>
                    <td height="1" colspan="9" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap width="30">消息ID</td>
                    <td width="">消息ICON</td>
                    <td nowrap align="center" width="100">消息标题</td>
                    <td nowrap align="center" width="100">消息信息</td>
                    <td nowrap align="center" width="160">额外信息</td>
                    <td nowrap align="center" width="80">添加人</td>
                    <td nowrap align="center" width="80">APP</td>
                    <td nowrap align="center" width="80">推送平台</td>
                    <td nowrap align="center" width="80">状态</td>
                    <td nowrap align="center" width="100">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="9" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="msg" varStatus="st">
                            <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap>${msg.pushMessage.pushMsgId}</td>
                                <td><img src="${msg.pushMessage.msgIcon}" width="30px"></td>
                                <td nowrap>${msg.pushMessage.msgSubject}</td>
                                <td nowrap>${msg.pushMessage.shortMessage}</td>
                                <td nowrap>${msg.pushMessage.options.toJson()}</td>
                                <td nowrap>${msg.pushMessage.createUserid}</td>
                                <td nowrap>${msg.appName}</td>
                                 <td nowrap>${msg.appName}</td>
                                <td nowrap >
                                    <fmt:message key="joymeapp.platform.${msg.pushMessage.appPlatform.code}" bundle="${def}"/>
                               </td>
                                <td nowrap>
                                    <c:if test="${msg.pushMessage.pushStatus.code=='n'}"><a href="/joymeapp/pushmessage/send?msgid=${msg.pushMessage.pushMsgId}">推送</a></c:if>
                                    <c:if test="${msg.pushMessage.pushStatus.code=='ing'}"><B>已经发送</B></c:if>
                                    &nbsp;&nbsp;&nbsp;&nbsp;<a href="/joymeapp/pushmessage/modifypage?msgid=${msg.pushMessage.pushMsgId}">编辑</a>
                                    <c:if test="${msg.pushMessage.pushStatus.code!='y'}">&nbsp;&nbsp;&nbsp;&nbsp;<a href="/joymeapp/pushmessage/delete?pushmsgid=${msg.pushMessage.pushMsgId}&appkey=${msg.pushMessage.appKey}">删除</a></c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="9" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="9" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="9" height="1" class="default_line_td"></td>
                </tr>
                 <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="9">
                            <pg:pager url="/joymeapp/pushmessage/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                <pg:param name="items" value="${page.totalRows}"/>
                                <%@ include file="/WEB-INF/jsp/toolspg.jsp" %>
                            </pg:pager>
                        </td>
                    </tr>
                </c:if>
            </table>
        </td>
    </tr>
</table>
</body>
</html>