<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>游戏列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {

        });
    </script>
    <style>
        .name {
            max-width: 300px;
            overflow: hidden;
            text-overflow: ellipsis;
        }

        .target {
            max-width: 300px;
            overflow: hidden;
            text-overflow: ellipsis;
        }
    </style>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> Joymewiki管理>> 游戏列表</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td>
                        <form action="/gamev2/list" method="post">
                            搜索：<select name="type">
                            <option value="2" <c:if test="${type=='2'}">selected</c:if>>游戏名称</option>
                            <option value="1" <c:if test="${type=='1'}">selected</c:if>>游戏ID</option>
                        </select>
                             <input type="text" name="searchText" class="default_button" value="${searchText}" size="20"/>
                            <input type="submit" name="button" class="default_button" value="搜索"/>
                        </form>
                    </td>
                    <td> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;</td>
                    <td>
                        <form action="/gamev2/createpage" method="post">
                            <input type="submit" name="button" class="default_button" id="default_button2"
                                   value="添加游戏"/>
                        </form>
                    </td>
                </tr>
            </table>

            <table width="80%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="8" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center" width="50px">游戏ID</td>
                    <td nowrap align="center" width="">游戏名称</td>
                    <td nowrap align="center" width="">修改人</td>
                    <td nowrap align="center" width="70px">添加时间</td>
                    <td nowrap align="center">一句话推荐</td>
                    <td nowrap align="center">评论人</td>
                    <td nowrap align="center">状态</td>
                    <td nowrap align="center" width="80px">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="8" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="dto" varStatus="st">
                            <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap align="center">${dto.id}</td>
                                <input type="hidden" id="display_${dto.id}" size="48" value="${dto.id}"/>
                                <td nowrap class="name" title="${dto.id}">${dto.name}</td>
                                <td nowrap class="target"
                                    title="${dto.extJson.createUser}">${dto.extJson.createUser}</td>
                                <td nowrap class="target"><fmt:formatDate value='${dto.createTime}' pattern='yyyy-MM-dd HH:mm:ss' /></td>
                                <td nowrap class="target" title="${dto.extJson.recommend}">${dto.extJson.recommend}</td>
                                <td nowrap class="name" title="${dto.extJson.recommendAuth}">${dto.extJson.recommendAuth}</td>
                                <td nowrap align="center">
                                    <fmt:message key="game.validstatus.${dto.validStatus}" bundle="${toolsProps}"/>
                                </td>

                                <td nowrap align="center">
                                    <a href="/gamev2/modifypage?id=${dto.id}">编辑</a>&nbsp;&nbsp;&nbsp;

                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="8" class="default_line_td"></td>
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
                    <tr class="list_table_opp_tr">
                        <td colspan="8">
                            <pg:pager url="/gamev2/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                <pg:param name="items" value="${page.totalRows}"/>
                                <pg:param name="type" value="${type}"/>
                                <pg:param name="searchText" value="${searchText}"/>
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