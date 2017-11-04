<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>

<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>文章小组权限管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript">

    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
    <td height="22" class="page_navigation_td">>> 运营管理 >> 条目管理 >> 文章小组权限管理</td>
</tr>
<tr>
<td height="100%" valign="top"><br>
<table border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td class="list_table_header_td">文章小组权限管理</td>
    </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <c:if test="${fn:length(errorMsg)>0}">
        <tr>
            <td height="1" colspan="13" class="error_msg_td">
                <fmt:message key="${errorMsg}" bundle="${error}"/>
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
            <form action="/authority/article/createpage" method="post">
                <table width="100%">
                    <tr>
                        <td align="">
                            <input type="submit" name="button" class="default_button" value="添加权限"/>
                        </td>
                    </tr>
                </table>
            </form>
        </td>
    </tr>
</table>
<form action="/authority/article/list" method="post" name="batchform">
    <table width="100%" border="0" cellspacing="1" cellpadding="0">
        <tr>
            <td height="1" colspan="10" class="default_line_td"></td>
        </tr>
        <c:if test="${fn:length(errorMsg)>0}">
            <tr>
                <td height="1" colspan="10" class="error_msg_td">
                    <b><fmt:message key="${errorMsg}" bundle="${error}"/></b>
                </td>
            </tr>
        </c:if>
        <tr class="list_table_title_tr">
            <td nowrap align="left" width=""></td>
            <td nowrap align="left" width="">权限ID</td>
            <td nowrap align="left" width="">权限名称</td>
            <td nowrap align="left" width="">权限描述</td>
            <td nowrap align="left" width="">权限类型</td>
            <td nowrap align="left" width="">状态</td>
            <td nowrap align="left" width="">排序</td>
            <td nowrap align="left" width="">创建信息</td>
            <td nowrap align="left" width="">修改信息</td>
            <td nowrap align="left" width="">操作</td>
        </tr>
        <tr>
            <td height="1" colspan="13" class="default_line_td"></td>
        </tr>
        <tr class="list_table_opp_tr">
            <td nowrap align="center"><input type="checkbox" name="checkbox" value="1"/></td>
            <td nowrap align="left">1</td>
            <td nowrap align="left">读</td>
            <td nowrap align="left">是否对文章可读</td>
            <td nowrap align="left">文章</td>
            <td nowrap align="left" style="color: #008000;">启用</td>
            <td nowrap align="left"></td>
            <td nowrap align="left"></td>
            <td nowrap align="left"></td>
            <td nowrap align="left">
                <a href="list.jsp#">编辑</a>
                <a href="list.jsp#">删除</a>
            </td>
        </tr>
        <tr class="list_table_even_tr">
            <td nowrap align="center"><input type="checkbox" name="checkbox" value="2"/></td>
            <td nowrap align="left">2</td>
            <td nowrap align="left">写</td>
            <td nowrap align="left">是否对文章可写</td>
            <td nowrap align="left">文章</td>
            <td nowrap align="left" style="color: #008000;">启用</td>
            <td nowrap align="left"></td>
            <td nowrap align="left"></td>
            <td nowrap align="left"></td>
            <td nowrap align="left">
                <a href="list.jsp#">编辑</a>
                <a href="list.jsp#">删除</a>
            </td>
        </tr>
        <tr class="list_table_opp_tr">
            <td nowrap align="center"><input type="checkbox" name="checkbox" value="3"/></td>
            <td nowrap align="left">3</td>
            <td nowrap align="left">置顶</td>
            <td nowrap align="left">对文章置顶操作</td>
            <td nowrap align="left">文章</td>
            <td nowrap align="left" style="color: #008000;">启用</td>
            <td nowrap align="left"></td>
            <td nowrap align="left"></td>
            <td nowrap align="left"></td>
            <td nowrap align="left">
                <a href="list.jsp#">编辑</a>
                <a href="list.jsp#">删除</a>
            </td>
        </tr>
        <tr class="list_table_even_tr">
            <td nowrap align="center"><input type="checkbox" name="checkbox" value="4"/></td>
            <td nowrap align="left">4</td>
            <td nowrap align="left">加精</td>
            <td nowrap align="left">对文章加精操作</td>
            <td nowrap align="left">文章</td>
            <td nowrap align="left" style="color: #008000;">启用</td>
            <td nowrap align="left"></td>
            <td nowrap align="left"></td>
            <td nowrap align="left"></td>
            <td nowrap align="left">
                <a href="list.jsp#">编辑</a>
                <a href="list.jsp#">删除</a>
            </td>
        </tr>
        <tr class="list_table_opp_tr">
            <td nowrap align="center"><input type="checkbox" name="checkbox" value="5"/></td>
            <td nowrap align="left">5</td>
            <td nowrap align="left">封帖</td>
            <td nowrap align="left">删除违规违法的帖子</td>
            <td nowrap align="left">文章</td>
            <td nowrap align="left" style="color: #008000;">启用</td>
            <td nowrap align="left"></td>
            <td nowrap align="left"></td>
            <td nowrap align="left"></td>
            <td nowrap align="left">
                <a href="list.jsp#">编辑</a>
                <a href="list.jsp#">删除</a>
            </td>
        </tr>
        <%--<c:choose>--%>
        <%--<c:when test="${list.size() > 0}">--%>
        <%--<c:forEach items="${list}" var="activity" varStatus="st">--%>
        <%--<tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">--%>
        <%--<td nowrap align="left">${activity.activitySubject}</td>--%>
        <%--<td nowrap align="left"><img src="${activity.activityPicUrl}" height="30"--%>
        <%--width="100"/></td>--%>
        <%--<td nowrap align="left">${activity.startTime}</td>--%>
        <%--<td nowrap align="left">${activity.endTime}</td>--%>
        <%--<td nowrap align="left">${activity.gameName}</td>--%>
        <%--<td nowrap align="left"><img src="${activity.gameIconUrl}" height="30" width="100"/>--%>
        <%--</td>--%>
        <%--&lt;%&ndash;<td nowrap align="left" width="130">商品过期时间</td>&ndash;%&gt;--%>
        <%--<td nowrap align="left">${activity.gameProduct}</td>--%>
        <%--<td nowrap align="left">${activity.gameUrl}</td>--%>
        <%--<td nowrap align="left">${URL_WWW}/appclick/${activity.qrUrl}</td>--%>
        <%--<td nowrap align="left">${activity.iosDownloadUrl}</td>--%>
        <%--<td nowrap align="left">${activity.androidDownloadUrl}</td>--%>
        <%--<td nowrap>--%>
        <%--<a href="/activity/goods/sort/up?activityid=${activity.activityId}"><img--%>
        <%--src="/static/images/icon/up.gif"></a>--%>
        <%--<a href="/activity/goods/sort/down?activityid=${activity.activityId}"><img--%>
        <%--src="/static/images/icon/down.gif"></a>--%>
        <%--</td>--%>
        <%--<td nowrap align="left"--%>
        <%--<c:if test="${activity.removeStatus.code=='n'}">style="color:red"</c:if> >--%>
        <%--<fmt:message key="activity.exchange.type.${activity.removeStatus.code}"--%>
        <%--bundle="${def}"/></td>--%>
        <%--<td nowrap align="left">${activity.createIp}</td>--%>
        <%--<td nowrap align="left"><a--%>
        <%--href="/activity/goods/modifypage?activityId=${activity.activityId}">修改</a>--%>
        <%--<c:choose>--%>
        <%--<c:when test="${activity.removeStatus.code=='n'}">--%>
        <%--<a href="/activity/goods/recover?activityId=${activity.activityId}">激活</a>--%>
        <%--</c:when>--%>
        <%--<c:otherwise>--%>
        <%--<a href="/activity/goods/delete?activityId=${activity.activityId}">删除</a>--%>
        <%--</c:otherwise>--%>
        <%--</c:choose>--%>
        <%--</td>--%>
        <%--</tr>--%>
        <%--</c:forEach>--%>
        <%--<tr>--%>
        <%--<td height="1" colspan="13" class="default_line_td"></td>--%>
        <%--</tr>--%>
        <%--</c:when>--%>
        <%--<c:otherwise>--%>
        <%--<tr>--%>
        <%--<td colspan="13" class="error_msg_td">暂无数据!</td>--%>
        <%--</tr>--%>
        <%--</c:otherwise>--%>
        <%--</c:choose>--%>
        <tr>
            <td colspan="10" height="1" class="default_line_td"></td>
        </tr>
        <tr class="toolbar_tr">
            <td colspan="10">
                <input type="checkbox" name="selectall" value="1"
                       onclick="javascript:checkall(document.forms['batchform'].checkbox, document.forms['batchform'].selectall)">全选
                <input type="checkbox" name="uncheck" value="1"
                       onclick="javascript:convertcheck(document.forms['batchform'].checkbox)">反选
                将选中记录用户状态改成：
                <select name="updateRemoveStatusCode" class="default_select_single">
                    <option value="">--请选择--</option>
                    <option value="y">停用</option>
                    <option value="n">启用</option>
                </select>
                <input name="update" type="submit" class="default_button" value="批量修改"></td>
        </tr>
        <tr>
            <td colspan="10" height="1" class="default_line_td"></td>
        </tr>
        <c:if test="${page.maxPage > 1}">
            <tr class="list_table_opp_tr">
                <td colspan="10">
                    <pg:pager url="/activity/goods/list"
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
</form>
</td>
</tr>
</table>
</body>
</html>