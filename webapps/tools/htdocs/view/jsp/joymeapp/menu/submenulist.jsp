<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台数据管理、二级菜单管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script language="JavaScript" type="text/JavaScript">
        function add(pid, appkey,displaytype) {
            window.location.href = "/joymeapp/menu/subecreatepage?pid=" + pid + "&appkey=" + appkey+"&displaytype="+displaytype;
        }
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷APP >> APP菜单管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr class="toolbar_tr">
                    <td>
                        <p:privilege name="/joymeapp/menu/subecreatepage">
                            <input name="Submit" type="submit" class="default_button" value="添加二级菜单"
                                   onClick="add('${pid}','${appkey}','${displaytype}');">
                        </p:privilege>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="12" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="left" width="5%">菜单ID</td>
                    <td nowrap align="left" width="10%">菜单名称</td>
                    <td nowrap align="left" width="10%">菜单URL</td>
                    <td nowrap width="5%">菜单类型</td>
                    <td nowrap align="center" width="10%">是否是new</td>
                    <td nowrap align="center" width="10%">是否是hot</td>
                    <td nowrap align="center" width="10%">标签</td>
                    <td nowrap align="center" width="10%">创建人/创建IP</td>
                    <td nowrap align="center" width="10%">修改人/修改IP</td>
                    <td nowrap align="center" width="">状态</td>
                    <td nowrap align="center" width="">排序操作</td>
                    <td nowrap align="center" width="10%">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="12" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <input type="hidden" name="categoryId" value="">
                        <c:forEach items="${list}" var="submenu" varStatus="st">
                            <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr </c:when><c:otherwise>list_table_even_tr </c:otherwise></c:choose>">
                                <td nowrap>${submenu.menuId}</td>
                                <td nowrap><a href="/joymeapp/menu/subdetail?menuid=${submenu.menuId}&pid=${pid}&appkey=${submenu.appkey}">${submenu.menuName}</a></td>
                                <td nowrap>${submenu.url}</td>
                                <td nowrap><fmt:message key="joymeapp.menu.type.${submenu.menuType}"
                                                        bundle="${def}"/></td>
                                <td nowrap><fmt:message key="joymeapp.menu.status.${submenu.isNew()}"
                                                        bundle="${def}"/></td>
                                <td nowrap><fmt:message key="joymeapp.menu.status.${submenu.isHot()}"
                                                        bundle="${def}"/></td>
                                <td nowrap>
                                 <c:if test="${submenu.tagId!=null && submenu.tagId>0 && tagmap[submenu.tagId]!=null}">
                                     ${tagmap[submenu.tagId].tagName}
                                 </c:if>
                                </td>
                                <td nowrap>${submenu.createUserId}/${submenu.createIp}</td>
                                <td nowrap>${submenu.lastModifyUserId}/${submenu.lastModifyIp}</td>
                                <td nowrap><fmt:message key="joymeapp.menu.status.${submenu.removeStatus.getCode()}"
                                                        bundle="${def}"/></td>
                                <td nowrap>
                                    <a href="/joymeapp/menu/sort/up?menuId=${submenu.menuId}&appkey=${appkey}&pid=${pid}&oid=0"><img
                                            src="/static/images/icon/up.gif"></a>
                                    <a href="/joymeapp/menu/sort/down?menuId=${submenu.menuId}&appkey=${appkey}&pid=${pid}&oid=0"><img
                                            src="/static/images/icon/down.gif"></a>
                                </td>
                                <td nowrap>
                                    <a href="/joymeapp/menu/submodifypage?menuid=${submenu.menuId}&appkey=${appkey}&pid=${pid}">编辑</a>
                                    <c:choose>
                                        <c:when test="${submenu.removeStatus.code!='n'}">
                                            <a href="/joymeapp/menu/subrecover?menuid=${submenu.menuId}&pid=${pid}&appkey=${appkey}">恢复</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="/joymeapp/menu/subdelete?menuid=${submenu.menuId}&pid=${pid}&appkey=${appkey}">删除</a>
                                        </c:otherwise>
                                    </c:choose></td>

                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="12" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="12" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="12" height="1" class="default_line_td"></td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>