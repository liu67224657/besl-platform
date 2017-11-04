<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>APP菜单管理</title>
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
        <td height="22" class="page_navigation_td">>> 运营维护 >> 小端APP >> 2.0版小端菜单管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">APP菜单管理</td>
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
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" width="80" align="center">应用名称</td>
                    <td>
                        <table width="100%" border="0" cellspacing="1" cellpadding="0">
                            <tr>
                                <td>
                                    <input type="text" id="input_appname" name="" value=""/>
                                    <input type="button" class="default_button" value="检索" id="input_searchapp"/>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="90%" border="0" cellspacing="0" cellpadding="0">
                <form action="/joymeapp/client/menu/list" method="post" id="form_submit_search">
                    <tr>
                        <td width="80" align="center">搜索条件</td>
                        <td>
                            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                                <tr>
                                    <td align="right" class="edit_table_defaulttitle_td">应用：</td>
                                    <td>
                                        <select name="appkey" id="select_appkey">
                                            <option value="">请选择</option>
                                            <c:forEach var="app" items="${applist}">
                                                <option value="${app.appId}"
                                                        <c:if test="${app.appId==appKey}">selected</c:if>>${app.appName}</option>
                                            </c:forEach>
                                        </select><br/>*若不好找对应的APP，请输入app的名字，检索一下，缩小选择范围
                                    </td>
                                    <td align="right" class="edit_table_defaulttitle_td">模块：</td>
                                    <td>
                                        <select name="module" id="select_module">
                                            <option value="">请选择</option>
                                            <c:forEach var="modu" items="${moduleSet}">
                                                <option value="${modu.code}"
                                                        <c:if test="${modu.code == module}">selected="selected"</c:if>>
                                                    <fmt:message key="joymeapp.menu.module.${modu.code}"
                                                                 bundle="${def}"/>
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td align="center">
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
            <table width="90%" border="0" cellspacing="0" cellpadding="0">
                <form action="/joymeapp/client/menu/createpage" method="post" id="form_submit_create">
                    <c:if test="${fn:length(appKey)>0}">
                        <tr>
                            <td>
                                <input type="hidden" id="hidden_appkey" name="appkey" value="${appKey}"/>
                                <input type="hidden" id="hidden_module" name="module" value="${module}"/>
                                <input type="submit" name="button" class="default_button" value="添加一级菜单"/>
                            </td>
                        </tr>
                    </c:if>
                </form>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="11" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center">菜单ID</td>
                    <td nowrap align="center">菜单名称</td>
                    <td nowrap align="center">跳转类型</td>
                    <td nowrap align="center">跳转参数</td>
                    <td nowrap align="center">跳转模块</td>
                    <td nowrap align="center">创建人/创建IP</td>
                    <td nowrap align="center">修改人/修改IP</td>
                    <td nowrap align="center">状态</td>
                    <td nowrap align="center">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="11" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="menu" varStatus="st">
                            <tr<c:choose><c:when test="${st.index % 2 == 0}">class="list_table_opp_tr"</c:when>
                                <c:otherwise> class="list_table_even_tr"</c:otherwise></c:choose>>
                                <td nowrap>${menu.menuId}</td>
                                <td nowrap><a href="/joymeapp/client/menu/detail?menuid=${menu.menuId}&appkey=${menu.appkey}&module=${menu.moduleType.code}">${menu.menuName}</a>
                                </td>
                                <td nowrap><fmt:message key="joymeapp.menu.type.${menu.menuType}" bundle="${def}"/></td>
                                <td nowrap>${menu.url}</td>
                                <td nowrap>
                                    <fmt:message key="joymeapp.menu.module.${menu.moduleType.code}" bundle="${def}"/></td>
                                <td nowrap>${menu.createUserId}/${menu.createIp}</td>
                                <td nowrap>${menu.lastModifyUserId}/${menu.lastModifyIp}</td>
                                <td nowrap <c:if test="${menu.removeStatus.code == 'y'}">style="color: #ff0000"</c:if>
                                    <c:if test="${menu.removeStatus.code == 'n'}">style="color: #008000"</c:if>>
                                    <fmt:message key="joymeapp.menu.status.${menu.removeStatus.code}" bundle="${def}"/></td>
                                <td nowrap>
                                    <a href="/joymeapp/client/menu/modifypage?menuid=${menu.menuId}&appkey=${appKey}&module=${module}&pid=${menu.parentId}">编辑</a>
                                    <c:choose>
                                        <c:when test="${menu.removeStatus.code!='n'}">
                                            <a href="/joymeapp/client/menu/recover?menuid=${menu.menuId}&appkey=${appKey}&pid=${menu.parentId}&module=${module}">恢复</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="/joymeapp/client/menu/remove?menuid=${menu.menuId}&appkey=${appKey}&pid=${menu.parentId}&module=${module}">删除</a>
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
                            <pg:pager url="/joymeapp/client/menu/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="appkey" value="${appKey}"/>
                                <pg:param name="module" value="${module}"/>
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