<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title></title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 大动漫管理 >> 大动漫历史上的今天</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table>
                <tr>
                    <td>
                        <form action="/joymeapp/anime/history/list" method="post">
                            <table width="100%">
                                <tr>
                                    <td>
                                        <select name="appkey">
                                            <option value="0G30ZtEkZ4vFBhAfN7Bx4v" <c:if test="${appkey=='0G30ZtEkZ4vFBhAfN7Bx4v'}">selected</c:if>>海贼迷</option>
                                            <option value="1zBwYvQpt3AE6JsykiA2es" <c:if test="${appkey=='1zBwYvQpt3AE6JsykiA2es'}">selected</c:if>>火影迷</option>
                                        </select>
                                    </td>
                                    <td>
                                        <input type="submit" name="button" class="default_button" value="查询"/>
                                    </td>
                                    <td>
                                    </td>
                                </tr>
                            </table>
                        </form>

                    </td>
                    <td class="">
                        <form action="/joymeapp/anime/history/createpage" method="post">
                            <select name="appkey">
                                <option value="0G30ZtEkZ4vFBhAfN7Bx4v" <c:if test="${appkey=='0G30ZtEkZ4vFBhAfN7Bx4v'}">selected</c:if>>海贼迷</option>
                                <option value="1zBwYvQpt3AE6JsykiA2es" <c:if test="${appkey=='1zBwYvQpt3AE6JsykiA2es'}">selected</c:if>>火影迷</option>
                            </select>
                            <input type="submit" value="创建消息"/>
                        </form>

                    </td>


                    </tr>
                </table>
            <table width="20%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">历史上的列表</td>

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
                        <td height="1" colspan="10" class="error_msg_td">
                            ${errorMsg}
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td height="1" colspan="10" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap width="50">消息ID</td>
                    <td nowrap width="">标题</td>
                    <td nowrap width="">简述</td>
                    <td nowrap width="">日期</td>
                    <td nowrap width="">额外信息</td>
                    <td nowrap width="">添加人</td>
                    <td nowrap width="">APP</td>
                    <td nowrap width="">状态</td>
                    <td nowrap width="">操作</td>
                    <td nowrap width="">创建时间</td>
                </tr>
                <tr>
                    <td height="1" colspan="10" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="msg" varStatus="st">
                            <tr class="<c:choose><c:when test="
                            ${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                <td nowrap>${msg.pushMessage.pushMsgId}</td>
                <td nowrap>${msg.pushMessage.msgSubject}</td>
                <td nowrap>${msg.pushMessage.shortMessage}</td>
                <td nowrap><fmt:formatDate value="${msg.pushMessage.sendDate}" pattern="yyyy-MM-dd"/></td>
                <td nowrap>${msg.pushMessage.options.toJson()}</td>
                <td nowrap>${msg.pushMessage.createUserid}</td>
                <td nowrap>${msg.appName}</td>
                <td nowrap>
                <fmt:message key="joymeapp.version.status.${msg.pushMessage.pushStatus.code}" bundle="${def}"/>
                </td>
                <td nowrap>
                    &nbsp;&nbsp;&nbsp;&nbsp;<a
                        href="/joymeapp/anime/history/modifypage?msgid=${msg.pushMessage.pushMsgId}&appkey=${appkey}">编辑</a>
                    <c:if test="${msg.pushMessage.pushStatus.code=='y'}">
                        <a href="/joymeapp/anime/history/remove?msgid=${msg.pushMessage.pushMsgId}&appkey=${appkey}&status=n">发布</a>
                    </c:if>
                    <c:if test="${msg.pushMessage.pushStatus.code=='n'}">
                        <a href="/joymeapp/anime/history/remove?msgid=${msg.pushMessage.pushMsgId}&appkey=${appkey}&status=y">删除</a>
                    </c:if>
                </td>
                <td>
                    <fmt:formatDate value="${msg.pushMessage.sendDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                </td>
                </tr>
                </c:forEach>
                <tr>
                    <td height="1" colspan="10" class="default_line_td"></td>
                </tr>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="10" class="error_msg_td">暂无数据!</td>
                    </tr>
                </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="10" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="10">
                            <pg:pager url="/anime/history/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="appkey" value="${appkey}"/>
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