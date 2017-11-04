<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台数据管理、推送证书</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>

    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/dhtmlxcalendar.css">
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/skins/dhtmlxcalendar_dhx_skyblue.css">
    <script src="/static/include/dhtmlxcalendar/dhtmlxcalendar.js"></script>
    <script language="JavaScript" type="text/JavaScript">
        $().ready(function () {
            doOnLoad();
        });

        var myCalendar;
        function doOnLoad() {
            myCalendar = new dhtmlXCalendarObject(["startDate", "endDate"]);
        }
    </script>
</head>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 小端APP >> 提示板列表</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">>提示板列表</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="14" class="error_msg_td">${errorMsg}</td>
                    </tr>
                </c:if>
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="50%" border="0" cellspacing="0" cellpadding="0">
                <form action="/joymeapp/tips/list" method="post" id="form_submit_search">
                    <tr>
                        <td width="80" align="center">搜索条件</td>
                        <td>
                            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                                <tr>
                                    <td height="1" class="default_line_td" width="100">
                                        选择APP:
                                    </td>
                                    <td height="1">
                                        <select name="queryappid" id="select_appkey">
                                            <option value="">请选择</option>
                                            <c:forEach items="${appList}" var="app">
                                                <c:choose>
                                                    <c:when test="${app.appId == queryAppId}">
                                                        <option value="${app.appId}"
                                                                selected="selected">${app.appName}</option>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <option value="${app.appId}">${app.appName}</option>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td height="1" class="default_line_td">
                                        选择平台:
                                    </td>
                                    <td height="1">
                                        <select name="queryplatform">
                                            <option value="">请选择</option>
                                            <c:forEach var="plat" items="${platformList}">
                                                <c:choose>
                                                    <c:when test="${plat.code == queryPlatform}">
                                                        <option value="${plat.code}" selected="selected">
                                                            <fmt:message key="joymeapp.platform.${plat.code}" bundle="${def}"/>
                                                        </option>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <option value="${plat.code}">
                                                            <fmt:message key="joymeapp.platform.${plat.code}" bundle="${def}"/>
                                                        </option>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>
                                        </select>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td width="80" align="center">
                            <input name="Button" type="submit" class="default_button" value=" 搜索 ">
                        </td>
                    </tr>
                </form>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td>
                        <form method="post" action="/joymeapp/tips/createpage">
                            <table>
                                <tr>
                                    <td>
                                        <input type="hidden" name="queryappid" value="${queryAppId}"/>
                                        <input type="hidden" name="queryplatform" value="${queryPlatform}"/>
                                        <p:privilege name="/joymeapp/tips/createpage">
                                            <input type="submit" name="button" class="default_button" value="添加提示板"/>
                                        </p:privilege>
                                    <td>
                                <tr>
                            </table>
                        </form>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="17" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center" width="">ID</td>
                    <td nowrap align="center" width="">标题</td>
                    <td nowrap align="center" width="">内容</td>
                    <td nowrap align="center" width="">图片</td>
                    <td nowrap align="center" width="">链接</td>
                    <td nowrap align="center" width="">APP</td>
                    <td nowrap align="center" width="">平台</td>
                    <td nowrap align="center" width="">状态</td>
                    <td nowrap align="center" width="">操作</td>
                    <td nowrap align="center" width="">创建信息</td>
                    <td nowrap align="center" width="">修改信息</td>
                </tr>
                <tr>
                    <td height="1" colspan="17" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="tips" varStatus="st">
                            <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap>${tips.tipsId}</td>
                                <td nowrap>${tips.tipsTitle}</td>
                                <td nowrap><textarea rows="8" cols="50">${tips.tipsDescription}</textarea></td>
                                <td nowrap>${tips.tipsPic}</td>
                                <td nowrap>${tips.tipsUrl}</td>
                                <td nowrap>
                                    <c:forEach items="${appList}" var="app">
                                        <c:if test="${app.appId == tips.appId}">${app.appName}</c:if>
                                    </c:forEach>
                                </td>
                                <td nowrap><fmt:message key="joymeapp.platform.${tips.platform}" bundle="${def}"/></td>
                                <td nowrap
                                        <c:choose>
                                            <c:when test="${tips.removeStatus.code eq 'n'}">style="color: #008000;"</c:when>
                                            <c:otherwise>style="color: #ff0000;"</c:otherwise></c:choose>>
                                    <fmt:message key="joymeapp.tips.status.${tips.removeStatus.code}" bundle="${def}"/>
                                </td>
                                <td nowrap>
                                    <c:choose>
                                        <c:when test="${tips.removeStatus.code eq 'n'}">
                                            <p:privilege name="/joymeapp/tips/remove">
                                                <a href="/joymeapp/tips/remove?tid=${tips.tipsId}&offset=${page.startRowIdx}&queryappid=${queryAppId}&queryplatform=${queryPlatform}">删除</a>
                                            </p:privilege>
                                        </c:when>
                                        <c:otherwise>
                                            <p:privilege name="/joymeapp/tips/recover">
                                                <a href="/joymeapp/tips/recover?tid=${tips.tipsId}&offset=${page.startRowIdx}&queryappid=${queryAppId}&queryplatform=${queryPlatform}">恢复</a>
                                            </p:privilege>
                                        </c:otherwise>
                                    </c:choose>
                                    &nbsp;
                                    <p:privilege name="/joymeapp/tips/modifypage">
                                        <a href="/joymeapp/tips/modifypage?tid=${tips.tipsId}&offset=${page.startRowIdx}&queryappid=${queryAppId}&queryplatform=${queryPlatform}">编辑</a>
                                    </p:privilege>
                                </td>
                                <td nowrap><fmt:formatDate value="${tips.createDate}" pattern="yyyy-MM-dd"/><br/>${tips.createUserId}<br/>${tips.createIp}
                                </td>
                                <td nowrap><fmt:formatDate value="${tips.modifyDate}" pattern="yyyy-MM-dd"/><br/>${tips.modifyUserId}<br/>${tips.modifyIp}
                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="17" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="17" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="17" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="17">
                            <LABEL>
                                <pg:pager url="/joymeapp/tips/list"
                                          items="${page.totalRows}" isOffset="true" maxPageItems="${page.pageSize}"
                                          export="offset, currentPageNumber=pageNumber" scope="request">
                                    <pg:param name="queryappid" value="${queryAppId}"/>
                                    <pg:param name="queryplatform" value="${queryPlatform}"/>
                                    <pg:param name="currentPageNumber" value="${page.curPage}"/>
                                    <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                    <pg:param name="items" value="${page.totalRows}"/>
                                    <%@ include file="/WEB-INF/jsp/toolspg.jsp" %>
                                </pg:pager>
                            </LABEL>
                        </td>
                    </tr>
                </c:if>
            </table>
        </td>
    </tr>
</table>
</body>
</html>