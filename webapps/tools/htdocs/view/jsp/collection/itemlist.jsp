<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>活动管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script>
        $(document).ready(function () {
            $('#form_submit').bind('submit', function () {


            });
        });
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 网站-游戏库 >> 游戏库模块列表</td>
    </tr>
    <tr>
        <td height="1" valign="top">
            <table>
                <tr>
                    <td height="1">
                        <form action="/collection/game/lineitem/list" method="post">
                            <table>
                                <tr>
                                    <td>
                                        按条件查询:
                                    </td>
                                    <td>
                                        <input type="hidden" value="${clientLine.lineId}" name="lineid"/>
                                        <input type="hidden" name="linecode" value="${clientLine.code}"/>
                                        <select name="validstatus">
                                            <option value="valid"
                                                    <c:if test="${validstatus=='valid'}">selected</c:if> >可用
                                            </option>
                                            <option value="removed"
                                                    <c:if test="${validstatus=='removed'}">selected</c:if>>已删除
                                            </option>
                                        </select>
                                    </td>
                                    <td>
                                        <input type="submit" class="default_button" value="查询"/>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </td>
                    <td height="1">
                        <form action="/collection/game/lineitem/createpage" method="post">
                            <table>
                                <tr>
                                    <td>
                                        <input type="hidden" name="lineid" value="${clientLine.lineId}"/>
                                        <input type="hidden" name="itemtype" value="${clientLine.itemType.code}"/>
                                        <p:privilege name="/collection/game/lineitem/createpage">
                                            <input type="submit" name="create_button" class="default_button"
                                                   value="添加游戏"/>
                                        </p:privilege>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </td>
                    <td height="1">
                        <form action="/collection/game/lineitem/updatecache" method="post">
                            <table>
                                <tr>
                                    <td>
                                        <input type="hidden" name="linecode" value="${clientLine.code}"/>
                                        <input type="hidden" name="lineid" value="${clientLine.lineId}"/>
                                        <input type="hidden" name="itemtype" value="${clientLine.itemType.code}"/>
                                        <p:privilege name="/collection/game/lineitem/updatecache">
                                            <input type="submit" name="create_button" class="default_button"
                                                   value="更新缓存"/>
                                        </p:privilege>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="13" class="error_msg_td">
                            <b><fmt:message key="${errorMsg}" bundle="${error}"/></b>
                        </td>
                    </tr>
                </c:if>
                <tr class="list_table_title_tr">
                    <td nowrap align="left">序号</td>
                    <td nowrap align="left">ID</td>
                    <td nowrap align="left">游戏资料库ID</td>
                    <td nowrap align="left">标题</td>
                    <td nowrap align="left">排序</td>
                    <td nowrap align="left">状态</td>
                    <td nowrap align="left">操作</td>
                    <td nowrap align="left">创建信息</td>
                </tr>
                <tr>
                    <td height="1" colspan="13" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="item" varStatus="st">
                            <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap align="left">${st.index + 1}</td>
                                <td nowrap align="left">${item.itemId}</td>
                                <td nowrap align="left">${item.directId}</td>
                                <td nowrap align="left">${item.title}</td>
                                <td nowrap align="left">${item.displayOrder}
                                    <form action="/collection/game/lineitem/sort" method="post">
                                        <input type="text" name="ordernum" value=""/>
                                        <input type="hidden" name="itemid" value="${item.itemId}"/>
                                        <input type="hidden" name="lineid" value="${item.lineId}"/>
                                        <input type="hidden" name="itemtype" value="${item.itemType.code}"/>
                                        <input type="hidden" name="validstatus" value="${validstatus}"/>
                                        <input type="submit" name="button" class="default_button" value="修改"/>
                                    </form>
                                </td>
                                <td nowrap align="left"
                                        <c:choose>
                                            <c:when test="${item.validStatus.code == 'valid'}">style="color:
                                                #008000;" </c:when>
                                            <c:otherwise>style="color: #ff0000;"</c:otherwise>
                                        </c:choose>>
                                    <fmt:message key="client.item.status.${item.validStatus.code}" bundle="${def}"/>
                                </td>
                                <td nowrap align="left">
                                    <%--<a href="/collection/game/lineitem/modifypage?itemid=${item.itemId}&lineid=${item.lineId}&itemtype=${item.itemType.code}&validstatus=${validstatus}">编辑</a>--%>
                                    <c:choose>
                                        <c:when test="${item.validStatus.code == 'valid'}">
                                            <a href="/collection/game/lineitem/delete?itemid=${item.itemId}&lineid=${item.lineId}&itemtype=${item.itemType.code}&validstatus=${validstatus}">删除</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="/collection/game/lineitem/recover?itemid=${item.itemId}&lineid=${item.lineId}&itemtype=${item.itemType.code}&validstatus=${validstatus}">恢复</a>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td nowrap align="left">
                                    <fmt:formatDate value="${item.itemCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="13" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="13" height="1" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="13" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="13">
                            <pg:pager url="/collection/game/lineitem/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="lineid" value="${clientLine.lineId}"/>
                                <pg:param name="itemtype" value="${clientLine.itemType.code}"/>
                                <pg:param name="validstatus" value="${validstatus}"/>
                                <pg:param name="currentPageNumber" value="${page.curPage}"/>
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