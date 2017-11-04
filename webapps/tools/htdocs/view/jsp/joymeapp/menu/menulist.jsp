<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>着迷APP版本信息</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script>
        $(document).ready(function () {
            $('#create_app_form').submit(function () {
                var appKey = $('#select_appkey').val();
                if (appKey.length == 0) {
                    alert('请选择一个APP');
                    return false;
                }
                $('#hidden_appkey').val(appKey);
            })
        })
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷APP >> APP菜单管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">APP菜单信息管理</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="10" class="error_msg_td">
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
                        <form action="/joymeapp/menu/list" method="post">
                            <table width="800px">
                                <tr>
                                    <td height="1" class="default_line_td" width="200px">
                                        选择APP:
                                    </td>
                                    <td height="1" class="edit_table_defaulttitle_td">
                                        <select name="appkey" id="select_appkey">
                                            <option value="">请选择</option>
                                            <c:forEach var="app" items="${applist}">
                                                <option value="${app.appId}"
                                                <c:if test="${app.appId==appkey}">selected</c:if>>${app.appName}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td height="1" class="default_line_td" width="200px">
                                        选择菜单位置:
                                    </td>
                                    <td height="1" class="edit_table_defaulttitle_td">
                                        <select name="category" id="category">
                                            <option value="">请选择</option>
                                            <c:forEach var="cate" items="${menuCategorys}">
                                                <option value="${cate.code}" <c:if test="${cate.code == category}">selected="selected"</c:if>>
                                                    <fmt:message key="joymeapp.menu.category.${cate.code}" bundle="${def}"/>
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td>
                                        <input type="submit" name="button" value="查询"/>
                                    </td>
                                    <td>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </td>
                    <td>
                        <c:if test="${fn:length(appkey)>0}">
                            <table>
                                <tr>
                                    <td>
                                        <form method="post" id="create_app_form" action="/joymeapp/menu/createonemenupage">
                                            <input type="hidden" id="hidden_appkey" name="appkey" value="${appKey}"/>
                                            <input type="submit" name="button" value="添加一级菜单"/>
                                        </form>
                                    </td>
                                </tr>
                            </table>
                        </c:if>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="11" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="left" width="5%">菜单ID</td>
                    <td nowrap align="left" width="10%">菜单名称</td>
                    <td nowrap align="left" width="10%">菜单URL</td>
                    <td nowrap width="20%">菜单类型</td>
                    <td nowrap width="20%">展示类别</td>
                    <td nowrap align="center" width="10%">是否是new</td>
                    <td nowrap align="center" width="10%">是否是hot</td>
                    <td nowrap align="center" width="10%">创建人/创建IP</td>
                    <td nowrap align="center" width="10%">修改人/修改IP</td>
                    <td nowrap align="center" width="">状态</td>
                    <td nowrap align="left" width="55">排序操作</td>
                    <td nowrap align="center" width="10%">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="11" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="menu" varStatus="st">
                            <tr<c:choose><c:when test="${st.index % 2 == 0}"> class="list_table_opp_tr"</c:when><c:otherwise> class="list_table_even_tr"</c:otherwise></c:choose>>
                            <td nowrap>${menu.menuId}</td>
                            <td nowrap><a
                                    href="/joymeapp/menu/detail?menuid=${menu.menuId}&appkey=${menu.appkey}">${menu.menuName}</a>
                            </td>
                            <td nowrap>${menu.url}</td>
                            <td nowrap><fmt:message key="joymeapp.menu.type.${menu.menuType}" bundle="${def}"/></td>
                            <td nowrap><fmt:message key="joymeapp.menu.category.${menu.moduleType.code}"
                                                    bundle="${def}"/></td>
                            <td nowrap><fmt:message key="joymeapp.menu.status.${menu.isNew()}"
                                                    bundle="${def}"/></td>
                            <td nowrap><fmt:message key="joymeapp.menu.status.${menu.isHot()}"
                                                    bundle="${def}"/></td>
                            <td nowrap>${menu.createUserId}/${menu.createIp}</td>
                            <td nowrap>${menu.lastModifyUserId}/${menu.lastModifyIp}</td>
                            <td nowrap><fmt:message key="joymeapp.menu.status.${menu.removeStatus.code}"
                                                    bundle="${def}"/></td>
                            <td nowrap>
                                <a href="/joymeapp/menu/sort/up?menuId=${menu.menuId}&appkey=${appkey}&pid=${menu.parentId}&oid=0&category=${category}"><img
                                        src="/static/images/icon/up.gif"></a>

                                <a href="/joymeapp/menu/sort/down?menuId=${menu.menuId}&appkey=${appkey}&pid=${menu.parentId}&oid=0&category=${category}"><img
                                        src="/static/images/icon/down.gif"></a>
                            </td>
                            <td nowrap>
                                <a href="/joymeapp/menu/modifypage?menuid=${menu.menuId}&appkey=${appkey}&pid=${menu.parentId}">编辑</a>
                                <c:choose>
                                    <c:when test="${menu.removeStatus.code!='n'}">
                                        <a href="/joymeapp/menu/recover?menuid=${menu.menuId}&appkey=${appkey}&pid=${menu.parentId}&category=${category}">恢复</a>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="/joymeapp/menu/delete?menuid=${menu.menuId}&appkey=${appkey}&pid=${menu.parentId}&category=${category}">删除</a>
                                    </c:otherwise>
                                </c:choose>

                            </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="11" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="11" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="11" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="10">
                            <pg:pager url="/joymeapp/menu/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="appkey" value="${appkey}"/>
                                <pg:param name="category" value="${category}"/>
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