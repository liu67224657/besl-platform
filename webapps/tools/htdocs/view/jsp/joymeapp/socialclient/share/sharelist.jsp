<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>社交端分享配置</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
    <td height="22" class="page_navigation_td">>> 运营管理 >> 分类管理 >> 社交端分享列表</td>
</tr>
<tr>
<td height="100%" valign="top"><br>
    <table border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td class="list_table_header_td">社交端分享列表</td>
        </tr>
    </table>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <c:if test="${fn:length(errorMsg)>0}">
            <tr>
                <td height="1" colspan="10" class="error_msg_td">
                    ${errorMsg}
                </td>
            </tr>
        </c:if>
        <tr>
            <td height="1" class="default_line_td"></td>
        </tr>
    </table>
    <table>
        <tr>
            <td>
                    <table width="100%">
                        <tr>
                            <td>
                                <form action="/joymeapp/socialclient/share/createpage" method="post">
                                    <input type="submit" name="button" class="default_button" value="新增分享信息"/>
                                </form>
                            </td>

                        </tr>
                    </table>
            </td>
        </tr>
    </table>
    <table width="100%" border="0" cellspacing="1" cellpadding="0">
        <tr>
            <td height="1" colspan="10" class="default_line_td"></td>
        </tr>
        <tr class="list_table_title_tr">
            <td nowrap align="center" width="80">ID</td>
            <td nowrap align="center" width="80">活动ID</td>
            <td nowrap align="center" width="100">类型</td>
            <td nowrap align="center" width="80">平台</td>
            <td nowrap align="center" width="150">第三方平台</td>
            <td nowrap align="center" width="80">标题</td>
            <td nowrap align="center" width="80">内容</td>
            <td nowrap align="center" width="80">是否删除</td>
            <td nowrap align="center" width="150">最后更新时间</td>
            <td nowrap align="center" width="100">操作</td>
        </tr>
        <tr>
            <td height="1" colspan="10" class="default_line_td"></td>
        </tr>
        <c:choose>
            <c:when test="${list.size() > 0}">
                <c:forEach items="${list}" var="dto" varStatus="st">
                    <tr id="socialHotContent_${dto.share_id}" class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                    <td nowrap align="center">${dto.share_id}</td>
                    <td nowrap align="center"><c:if test="${dto.activityid==-1}">默认</c:if><c:if test="${dto.activityid!=-1}">${dto.activityid}</c:if></td>
                    <td nowrap align="center"><fmt:message key="social.share.type.${dto.share_type.code}" bundle="${def}"/></td>
                    <td nowrap align="center"> <fmt:message key="joymeapp.platform.${dto.platform}" bundle="${def}"/></td>
                    <td nowrap align="center"><fmt:message key="social.share.third.type.${dto.sharedomain.code}" bundle="${def}"/></td>
                    <td nowrap>${dto.title}</td>
                    <td nowrap>${dto.body}</td>
                    <td nowrap align="center"><fmt:message key="joymeapp.version.status.${dto.remove_status.code}" bundle="${def}"/></td>
                    <td nowrap align="center">${dto.update_time_flag}</td>
                    <td nowrap align="center">
                        <a href="/joymeapp/socialclient/share/modifypage?shareid=${dto.share_id}&pager.offset=${page.startRowIdx}">编辑</a>

                        <c:choose>
                            <c:when test="${dto.remove_status.getCode()=='n'}">
                                <a href="/joymeapp/socialclient/share/modify?appkey=${dto.appkey}&platform=${dto.platform}&shareid=${dto.share_id}&del=y&pager.offset=${page.startRowIdx}&share_type=${dto.share_type.code}&activityid=${dto.activityid}">删除</a>
                            </c:when>
                            <c:otherwise>
                                <a href="/joymeapp/socialclient/share/modify?appkey=${dto.appkey}&platform=${dto.platform}&shareid=${dto.share_id}&del=n&pager.offset=${page.startRowIdx}&share_type=${dto.share_type.code}&activityid=${dto.activityid}">恢复</a>
                            </c:otherwise>
                        </c:choose>

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
                    <pg:pager url="/joymeapp/socialclient/virtual/list"
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