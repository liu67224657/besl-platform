<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>APP菜单子列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 小端APP >> 2.0版小端菜单管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top">
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <form action="/joymeapp/client/menu/createpage" method="post">
                    <tr>
                        <td>
                            <input type="hidden" name="appkey" value="${appKey}"/>
                            <input type="hidden" name="module" value="${module}"/>
                            <input type="hidden" name="pid" value="${pid}"/>
                            <input type="submit" class="default_button" name="button" value="添加子菜单"/>
                        </td>
                    </tr>
                </form>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="20" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap>ID</td>
                    <td nowrap>标题</td>
                    <td nowrap>图片(IOS)</td>
                    <td nowrap>图片(Android)</td>
                    <td nowrap>描述</td>
                    <td nowrap>跳转类型</td>
                    <td nowrap>跳转参数</td>
                    <td nowrap>是否new</td>
                    <td nowrap>是否hot</td>
                    <td nowrap>展示类型</td>
                    <td nowrap>拓展描述</td>
                    <td nowrap>推荐星级</td>
                    <td nowrap>作者</td>
                    <td nowrap>发布时间</td>
                    <td nowrap>排序操作</td>
                    <td nowrap>状态</td>
                    <td nowrap>操作</td>
                    <td nowrap>创建信息</td>
                    <td nowrap>修改信息</td>
                </tr>
                <tr>
                    <td height="1" colspan="20" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <input type="hidden" name="categoryId" value="">
                        <c:forEach items="${list}" var="submenu" varStatus="st">
                            <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr </c:otherwise></c:choose>">
                                <td nowrap>${submenu.menuId}</td>
                                <td nowrap>
                                    <a href="/joymeapp/client/menu/detail?menuid=${submenu.menuId}&pid=${pid}&appkey=${submenu.appkey}&module=${submenu.moduleType.code}">${submenu.menuName}</a>
                                </td>
                                <td nowrap><c:if test="${submenu.pic != null}"><img src="${submenu.pic.iosPic}" width="100" height="100"/></c:if></td>
                                <td nowrap><c:if test="${submenu.pic != null}"><img src="${submenu.pic.androidPic}" width="100" height="100"/></c:if></td>
                                <td nowrap>${submenu.menuDesc}</td>
                                <td nowrap><fmt:message key="joymeapp.menu.type.${submenu.menuType}" bundle="${def}"/>
                                </td>
                                <td nowrap>${submenu.url}</td>
                                <td nowrap><c:if test="${submenu.isNew()}">是</c:if><c:if
                                        test="${!submenu.isNew()}">否</c:if></td>
                                <td nowrap><c:if test="${submenu.isHot()}">是</c:if><c:if
                                        test="${!submenu.isHot()}">否</c:if></td>
                                <td nowrap><fmt:message key="joymeapp.menu.displaytype.${submenu.displayType.code}"
                                                        bundle="${def}"/></td>
                                <td nowrap><c:if test="${submenu.expField != null}">${submenu.expField.expDesc}</c:if></td>
                                <td nowrap><c:if test="${submenu.expField != null}">${submenu.expField.star}</c:if></td>
                                <td nowrap><c:if test="${submenu.expField != null}">${submenu.expField.author}</c:if></td>
                                <td nowrap><c:if test="${submenu.expField != null}">${submenu.expField.publishDate}</c:if></td>
                                <td nowrap>
                                    <a href="/joymeapp/client/menu/sort/up?menuid=${submenu.menuId}&appkey=${appKey}&module=${module}&pid=${submenu.parentId}">
                                        <img src="/static/images/icon/up.gif"></a>
                                    <a href="/joymeapp/client/menu/sort/down?menuid=${submenu.menuId}&appkey=${appKey}&module=${module}&pid=${submenu.parentId}">
                                        <img src="/static/images/icon/down.gif"></a>
                                </td>
                                <td
                                        <c:if test="${submenu.removeStatus.code == 'n'}">style="color: #008000"</c:if>
                                        <c:if test="${submenu.removeStatus.code == 'y'}">style="color: #FF0000"</c:if>>
                                    <fmt:message key="joymeapp.menu.status.${submenu.removeStatus.code}"
                                                 bundle="${def}"/>
                                </td>
                                <td nowrap>
                                    <a href="/joymeapp/client/menu/modifypage?menuid=${submenu.menuId}&appkey=${appKey}&pid=${pid}&module=${module}">编辑</a>
                                    <c:choose>
                                        <c:when test="${submenu.removeStatus.code!='n'}">
                                            <a href="/joymeapp/client/menu/recover?menuid=${submenu.menuId}&pid=${pid}&appkey=${appKey}&module=${module}">恢复</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="/joymeapp/client/menu/remove?menuid=${submenu.menuId}&pid=${pid}&appkey=${appKey}&module=${module}">删除</a>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td nowrap>${submenu.createUserId}<br/>
                                    <fmt:formatDate value="${submenu.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </td>
                                <td nowrap>${submenu.lastModifyUserId}<br/>
                                    <fmt:formatDate value="${submenu.lastModifyDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="20" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="20" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="20" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="10">
                            <pg:pager url="/joymeapp/client/menu/sublist"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="appkey" value="${appKey}"/>
                                <pg:param name="module" value="${module}"/>
                                <pg:param name="pid" value="${pid}"/>
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