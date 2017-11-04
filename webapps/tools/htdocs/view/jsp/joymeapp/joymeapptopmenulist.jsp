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
        $(document).ready(function() {
            $('#create_app_form').submit(function() {
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
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷APP >> APP轮播图</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">APP顶部菜单信息管理</td>
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
                        <form action="/joymeapp/topmenu/list" method="post">
                            <table width="400px">
                                <tr>
                                    <td height="1" class="default_line_td" width="200px">
                                        选择APP:
                                    </td>
                                    <td height="1" class="edit_table_defaulttitle_td">
                                        <select name="appkey" id="select_appkey">
                                            <option value="">请选择</option>
                                            <c:forEach var="app" items="${applist}">
                                                <option value="${app.appId}"
                                                        <c:if test="${app.appId==appkey}">selected</c:if> >${app.appName}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td height="1" class=>
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
                                        <form method="post" id="create_app_form"
                                              action="/joymeapp/topmenu/createtopmenupage">
                                            <input type="hidden" id="hidden_appkey" name="appkey" value="${appKey}"/>
                                            <input type="submit" name="button" value="添加顶部菜单"/>
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
                    <td height="1" colspan="14" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="left" width="">菜单名称</td>
                    <td nowrap align="left" width="">菜单URL</td>
                    <td nowrap align="left" width="">IOS图片</td>
                    <td nowrap align="left" width="">Android图片</td>
                    <td nowrap width="">渠道图片信息</td>
                    <td nowrap width="">菜单类型</td>
                    <td nowrap align="center" width="">是否是new</td>
                    <td nowrap align="center" width="">是否是hot</td>
                    <td nowrap align="center" width="">创建人/创建IP</td>
                    <td nowrap align="center" width="">修改人/修改IP</td>
                    <td nowrap align="center" width="">状态</td>
                    <td nowrap align="center" width="">排序操作</td>
                    <td nowrap align="center" width="">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="14" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="menu" varStatus="st">
                            <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap>${menu.menuName}</td>
                                <td nowrap>${menu.url}</td>
                                <td nowrap><img width="120" height="30" src="${menu.picUrl1}"/></td>
                                <td nowrap><img width="120" height="30" src="${menu.picUrl2}"/></td>
                                <td nowrap>
                                    <c:forEach items="${menu.channelTopMenuSet.channelTopMenuSet}" var="channeltopmenu">
                                        ${channeltopmenu.channelCode}~<fmt:message key="joymeapp.favorite.platform.${channeltopmenu.platform}" bundle="${def}"/>~<img width="120" height="30" src="${channeltopmenu.picUrl}"/>
                                    </c:forEach>
                                </td>
                                <td nowrap><fmt:message key="joymeapp.menu.type.${menu.menuType}" bundle="${def}"/></td>
                                <td nowrap><fmt:message key="joymeapp.menu.status.${menu.isNew()}"
                                                        bundle="${def}"/></td>
                                <td nowrap><fmt:message key="joymeapp.menu.status.${menu.isHot()}"
                                                        bundle="${def}"/></td>
                                <td nowrap>${menu.createUserId}/${menu.createIp}</td>
                                <td nowrap>${menu.lastModifyUserId}/${menu.lastModifyIp}</td>
                                <td nowrap><fmt:message key="joymeapp.menu.status.${menu.removeStatus.code}"
                                                        bundle="${def}"/></td>
                                <td nowrap>
                                    <a href="/joymeapp/topmenu/sort/up?menuId=${menu.menuId}&appkey=${appkey}"><img
                                            src="/static/images/icon/up.gif"></a>

                                    <a href="/joymeapp/topmenu/sort/down?menuId=${menu.menuId}&appkey=${appkey}"><img
                                            src="/static/images/icon/down.gif"></a>
                                </td>
                                <td nowrap>
                                    <a href="/joymeapp/topmenu/modifypage?menuid=${menu.menuId}&appkey=${appkey}">编辑</a>
                                    <c:choose>
                                        <c:when test="${menu.removeStatus.code!='n'}">
                                            <a href="/joymeapp/topmenu/recover?menuid=${menu.menuId}&appkey=${appkey}">恢复</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="/joymeapp/topmenu/delete?menuid=${menu.menuId}&appkey=${appkey}">删除</a>
                                        </c:otherwise>
                                    </c:choose>

                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="14" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="14" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="14" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="10">
                            <pg:pager url="/joymeapp/menu/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="appkey" value="${appkey}"/>
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