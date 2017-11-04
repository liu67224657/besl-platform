<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<html>
<head>
    <title>虚拟用户-wikiapp列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $('#search_form').submit(function () {
                var type = $('#search_type').val();
                var text = $('#search_text').val();
                if (type == 1) {
                    if (text.length == 0 || isNaN(text)) {
                        alert('请填写数字用户ID')
                        return false;
                    }
                } else {
                    if (text.length == 0) {
                        alert('请填写昵称')
                        return false;
                    }
                }
            })
        });
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> Joymewiki管理>> 虚拟用户管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td>
                        <form action="/apiwiki/vertualprofile/list" method="post" id="search_form">
                            搜索：<select name="type" id="search_type">
                            <option value="2" <c:if test="${type=='2'}">selected</c:if>>用户昵称</option>
                            <option value="1" <c:if test="${type=='1'}">selected</c:if>>用户ID</option>
                        </select>
                            <input type="text" id="search_text" value="${text}" name="text" class="default_button" value="${searchText}"
                                   size="20"/>
                            <input type="submit" name="button" class="default_button" value="搜索"/>
                        </form>
                    </td>
                    <td> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;</td>
                    <td>
                        <form action="/apiwiki/vertualprofile/createpage" method="post">
                            <input type="submit" name="button" class="default_button" id="default_button2"
                                   value="添加虚拟用户"/>
                        </form>
                    </td>
                </tr>
            </table>

            <table width="80%" border="0" cellspacing="1" cellpadding="0">
                <c:if test="${error != null}">
                    <tr>
                        <td height="1" colspan="7" class="default_line_td">
                                <%--<fmt:message key="${error}" bundle="${errorProps}"/>--%>
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td height="1" colspan="7" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center" width="50px">ID</td>
                    <td nowrap align="center" width="">昵称</td>
                    <td nowrap align="center" width="">描述</td>
                    <td nowrap align="center" width="70px">头像</td>
                    <td nowrap align="center">性别</td>
                    <td nowrap align="center" width="80px">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="7" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="dto" varStatus="st">
                            <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td>${dto.id}</td>
                                <td>${dto.nick}</td>
                                </td>
                                <td>${dto.description}</td>
                                <td><img src="${dto.icon}" height="50px" width="50px"></td>
                                <td><c:choose>
                                    <c:when test="${dto.sex==0}">女</c:when>
                                    <c:when test="${dto.sex==1}">男</c:when>
                                    <c:otherwise>-</c:otherwise>
                                </c:choose></td>
                                <td>
                                    <a href="/apiwiki/vertualprofile/updatepage?uid=${dto.id}">修改</a>
                                </td>

                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="7" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="7" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="7" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="7">
                            <pg:pager url="/apiwiki/vertualprfile/list"
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
