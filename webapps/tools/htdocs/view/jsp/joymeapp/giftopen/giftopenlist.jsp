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
        <td height="22" class="page_navigation_td">>> 运营维护 >> 小端APP >> 礼包中心开关列表</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="20%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">礼包中心开关列表</td>
                </tr>
            </table>
            <table border="0" cellspacing="0" cellpadding="0">
                <form action="/joymeapp/giftopen/list" method="post" id="form_submit_search">
                    <tr>
                        <td>选择应用:</td>
                        <td>
                            <select name="appkey">
                                <option>请选择</option>
                                <c:forEach items="${appList}" var="app">
                                    <option value="${app.appId}" <c:if test="${app.appId == appKey}">selected="selected"</c:if>>${app.appName}</option>
                                </c:forEach>
                            </select>
                        </td>
                        <td>
                            <input name="Button" type="submit" class="default_button" value=" 搜索 ">
                        </td>
                    </tr>
                </form>
            </table>
            <table border="0" cellspacing="0" cellpadding="0">
                <form action="/joymeapp/giftopen/createpage" method="post">
                    <tr>
                        <td width="80" align="left">
                            <input name="Button" type="submit" class="default_button" value=" 添加开关 ">
                        </td>
                    </tr>
                </form>
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
                    <td height="1" colspan="10" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center">ID</td>
                    <td nowrap align="center">标题</td>
                    <td nowrap align="center">appkey</td>
                    <td nowrap align="center">app</td>
                    <td nowrap align="center">版本</td>
                    <td nowrap align="center">平台</td>
                    <td nowrap align="center">状态</td>
                    <td nowrap align="center">操作</td>
                    <td nowrap align="center">创建信息</td>
                    <td nowrap align="center">修改信息</td>
                </tr>
                <tr>
                    <td height="1" colspan="10" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="info" varStatus="st">
                            <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap>${info.appInfoId}</td>
                                <td nowrap>${info.appName}</td>
                                <td nowrap>${info.appKey}</td>
                                <td nowrap>
                                    <c:forEach items="${appList}" var="app">
                                        <c:if test="${app.appId == info.appKey}">${app.appName}</c:if>
                                    </c:forEach>
                                </td>
                                <td nowrap>${info.version}</td>
                                <td nowrap><fmt:message key="joymeapp.platform.${info.appPlatform.code}"
                                                        bundle="${def}"/></td>
                                <td <c:if test="${info.removeStatus.code == 'y'}">style="color: red"</c:if>><fmt:message
                                        key="joymeapp.appinfo.remove.status.${info.removeStatus.code}" bundle="${def}"/>
                                </td>
                                <td nowrap>
                                    <a href="/joymeapp/giftopen/modifypage?infoid=${info.appInfoId}&appkey=${appKey}">编辑</a>&nbsp;
                                    <c:choose>
                                        <c:when test="${info.removeStatus.code == 'y'}">
                                            <a href="/joymeapp/giftopen/recover?infoid=${info.appInfoId}&appkey=${appKey}">恢复</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="/joymeapp/giftopen/remove?infoid=${info.appInfoId}&appkey=${appKey}">删除</a>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td nowrap>
                                    <fmt:formatDate value="${info.createDate}"
                                                    pattern="yyyy-MM-dd HH:mm:ss"/><br/>${info.createIp}<br/>${info.createUserId}
                                </td>
                                <td nowrap>
                                    <fmt:formatDate value="${info.modifyDate}"
                                                    pattern="yyyy-MM-dd HH:mm:ss"/><br/>${info.modifyIp}<br/>${info.modifyUserId}
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
                            <pg:pager url="/joymeapp/giftopen/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="appkey" value="${appKey}"/>
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