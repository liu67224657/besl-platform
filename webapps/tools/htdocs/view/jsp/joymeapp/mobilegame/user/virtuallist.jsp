<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>用户列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script>
        $(document).ready(function() {
            $('#focusBatch').bind("submit", function() {
                var srcScreenname = $.trim($('#srcScreenname').val());
                if (srcScreenname.length == 0) {
                    alert("用户昵称");
                    return false;
                }


            });
        });
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr height="40">
        <td height="22" class="page_navigation_td" colspan="2">>> 运营维护 >> 手游排行榜 >> 手游排行榜用户列表</td>
    </tr>
<tr>
<td height="100%" valign="top"><br>
    <table border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td class="list_table_header_td">用户列表</td>
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
                <form action="/joymeapp/mobilegame/virtual/list/${accountVirtualType}" method="post">
                    <table width="100%">

                        <tr>

                            <td height="1" class="default_line_td">
                                用户昵称:
                            </td>
                            <td height="1">
                                <input type="text" name="screenname" class="default_button" size="30"/>
                            </td>
                            <td height="1">
                                <select name="remove_status" id="select_channel">
                                    <option value="" selected>全部</option>
                                    <option value="n">可用</option>
                                    <option value="y">已删除</option>
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
            <td>
                <table>
                    <tr>
                        <td>
                            <form action="/joymeapp/mobilegame/virtual/createpage/${accountVirtualType}" method="post">
                                <input type="submit" name="button" class="default_button" value="新增用户"/>
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
            <td nowrap align="center" width="100">用户ID</td>
            <td nowrap align="center" width="200">用户UNO</td>
            <td nowrap align="center" width="150">用户昵称</td>
            <td nowrap align="center" width="150">头像</td>
            <td nowrap align="center" width="150">是否删除</td>
            <td nowrap align="center" width="150">创建人</td>
            <td nowrap align="center" width="150">创建时间</td>
            <td nowrap align="center" width="150">操作</td>
        </tr>
        <tr>
            <td height="1" colspan="10" class="default_line_td"></td>
        </tr>
        <c:choose>
            <c:when test="${list.size() > 0}">
                <c:forEach items="${list}" var="dto" varStatus="st">
                    <tr id="socialHotContent_${dto.virtual_id}"
                        class="<c:choose><c:when test="
                    ${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
        <td nowrap>${dto.virtual_id}</td>
        <td nowrap>${dto.uno}</td>
        <td nowrap>${dto.screenname}</td>
        <td nowrap><img src="${dto.headicon.pic_M}" alt="" height="60" width="120"/></td>
         <td nowrap> <fmt:message key="joymeapp.version.status.${dto.remove_status.code}" bundle="${def}"/></td>
        <td nowrap>${dto.create_user}</td>
        <td nowrap>${dto.create_time}</td>
        <td nowrap>
            <a href="/joymeapp/mobilegame/virtual/modifypage/${accountVirtualType}?virtualId=${dto.virtual_id}&pager.offset=${page.startRowIdx}">编辑</a>
            <c:choose>
                <c:when test="${dto.remove_status.getCode()=='n'}">
                    <a href="/joymeapp/mobilegame/virtual/modify/${accountVirtualType}?virtualId=${dto.virtual_id}&pager.offset=${page.startRowIdx}&del=y">删除</a>
                </c:when>
                <c:otherwise>
                    <a href="/joymeapp/mobilegame/virtual/modify/${accountVirtualType}?virtualId=${dto.virtual_id}&pager.offset=${page.startRowIdx}&del=n">恢复</a>
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
            <td colspan="9" height="1" class="default_line_td"></td>
        </tr>
        <c:if test="${page.maxPage > 1}">
            <tr class="list_table_opp_tr">
                <td colspan="9">
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