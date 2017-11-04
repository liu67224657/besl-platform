<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>社交端运营帐号列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 分类管理 >> 运营帐号列表</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">运营帐号列表</td>
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
                        <form action="/joymeapp/socialclient/operate/list" method="post">
                            <table width="100%">
                                <tr>
                                    <td height="1" class="default_line_td">
                                        昵称:
                                    </td>
                                    <td height="1">
                                        <input type="text" name="screenname" class="default_button" size="30"/>
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
                                    <form action="/joymeapp/socialclient/operate/createpage" method="post">
                                        <input type="submit" name="button" class="default_button" value="添加运营账号"/>
                                    </form>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="9" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center" width="100">用户ID</td>
                    <td nowrap align="center" width="150">用户昵称</td>
                    <td nowrap align="center" width="150">创建信息</td>
                    <td nowrap align="center" width="150">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="9" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="dto" varStatus="st">
                            <tr id="socialHotContent_${dto.virtual_id}" class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap>${dto.virtual_id}</td>
                                <td nowrap>
                                    <c:forEach items="${mapprofile}" var="map">
                                        <c:if test="${map.key==dto.uno}">
                                            ${map.value.blog.screenName}
                                        </c:if>
                                    </c:forEach>
                                </td>
                                <td nowrap>${dto.create_time}&nbsp;${dto.create_user}</td>
                                <td nowrap>
                                    <a href="/joymeapp/socialclient/operate/delete?vid=${dto.virtual_id}&pager.offset=${page.startRowIdx}">删除</a>
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
<script>
    $(document).ready(function () {
        var val1 = $("#srcScreenname").val();
        if (val1 == "") {
            $("#srcScreenname").val("输入用户1");
        }
        var val1 = $("#destScreenname").val();
        if (val1 == "") {
            $("#destScreenname").val("输入用户2");
        }

        $("#srcScreenname").focus(function () {
            if ($("#srcScreenname").val() == "输入用户1") {
                $("#srcScreenname").val("");
            }
        }).blur(function () {
                    if ($("#srcScreenname").val() == "") {
                        $("#srcScreenname").val("输入用户1");
                    }
                });
        $("#destScreenname").focus(function () {
            if ($("#destScreenname").val() == "输入用户2") {
                $("#destScreenname").val("");
            }
        }).blur(function () {
                    if ($("#destScreenname").val() == "") {
                        $("#destScreenname").val("输入用户2");
                    }
                });

    });
</script>
</body>
</html>