<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>WIKI大端首页模块管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {

        });
    </script>
</head>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="90%" border="0" cellspacing="0" cellpadding="0">
                <form action="/joymeapp/wikiapp/index/item/createpage" method="post" id="form_create">
                    <tr>
                        <td><input type="hidden" name="lineid" value="${lineId}"/>
                            <input type="hidden" name="linecode" value="${lineCode}"/>
                            <input type="hidden" name="itemtype" value="${itemType}"/>
                            <input name="Button" type="submit" class="default_button" value=" 添加二级元素 ">
                        </td>
                    </tr>
                </form>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="20" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center">ID</td>
                    <td nowrap align="center">标题</td>
                    <td nowrap align="center">描述</td>
                    <td nowrap align="center">图片</td>
                    <td nowrap align="center">跳转类型</td>
                    <td nowrap align="center">跳转参数</td>
                    <td nowrap align="center">数据源</td>
                    <td nowrap align="center">排序</td>
                    <td nowrap align="center">状态</td>
                    <td nowrap align="center">操作</td>
                    <td nowrap align="center">修改信息</td>
                </tr>
                <tr>
                    <td height="1" colspan="20" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list != null && list.size() > 0}">
                        <c:forEach items="${list}" var="item" varStatus="st">
                            <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap>
                                        ${item.itemId}
                                </td>
                                <td nowrap>
                                        ${item.title}
                                </td>
                                <td nowrap>
                                        <textarea type="text" style="height: 100px;width: 300px">${item.desc}</textarea>
                                </td>
                                <td nowrap>
                                        <img src="${item.picUrl}" height="100" width=""/>
                                </td>
                                <td nowrap>
                                    <fmt:message key="client.item.redirect.${item.redirectType.code}" bundle="${def}"/>
                                </td>
                                <td nowrap>
                                    ${item.url}
                                </td>
                                <td nowrap>
                                    <fmt:message key="client.item.domain.${item.itemDomain.code}" bundle="${def}"/>
                                </td>
                                <td height="1">
                                    <form action="/joymeapp/wikiapp/index/item/sort" method="post">
                                        <input type="hidden" name="lineid" value="${lineId}"/>
                                        <input type="hidden" name="linecode" value="${lineCode}"/>
                                        <input id="input_old_order_${st.index}" name="oldorder" value="${item.displayOrder}" readonly="readonly"/>
                                        <input id="input_order_modify_${st.index}" type="button" class="default_button" value="修改" onclick="$('#input_order_save_'+${st.index}).show();$(this).hide();$('#input_order_new_'+${st.index}).show()"/>
                                        <input id="input_order_save_${st.index}" type="submit" class="default_button" value="保存" hidden="hidden" onclick="$('#input_order_modify_'+${st.index}).show();$(this).hide();$('#input_order_new_'+${st.index}).hide()"/>
                                        <input id="input_order_new_${st.index}" name="neworder" value="" type="text" hidden="hidden"/>
                                    </form>
                                </td>
                                <td
                                        <c:choose>
                                            <c:when test="${item.validStatus.code == 'valid'}">style="color: #008000"</c:when>
                                            <c:when test="${item.validStatus.code == 'removed'}">style="color: #ff0000"</c:when>
                                        </c:choose>>
                                    <fmt:message key="client.item.status.${item.validStatus.code}" bundle="${def}"/>
                                </td>
                                <td nowrap>
                                    <a href="/joymeapp/wikiapp/index/item/modifypage?lineid=${lineId}&linecode=${lineCode}&itemtype=${itemType}&itemid=${item.itemId}">编辑</a>
                                    <c:choose>
                                        <c:when test="${line.validStatus.code == 'valid'}">
                                            <a href="/joymeapp/wikiapp/index/item/remove?lineid=${lineId}&linecode=${lineCode}&itemtype=${itemType}&itemid=${item.itemId}">删除</a>
                                        </c:when>
                                        <c:when test="${line.validStatus.code == 'removed'}">
                                            <a href="/joymeapp/wikiapp/index/item/recover?lineid=${lineId}&linecode=${lineCode}&itemtype=${itemType}&itemid=${item.itemId}">恢复</a>
                                        </c:when>
                                    </c:choose>
                                </td>
                                <td nowrap>
                                    <fmt:formatDate value="${date:long2date(item.param.modifyDate)}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    <br/>${item.param.modifyUserId}
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
                        <td colspan="20">
                            <LABEL>
                                <pg:pager url="/joymeapp/wikiapp/index/item/list"
                                          items="${page.totalRows}" isOffset="true"
                                          maxPageItems="${page.pageSize}"
                                          export="offset, currentPageNumber=pageNumber" scope="request">
                                    <pg:param name="lineid" value="${lineId}"/>
                                    <pg:param name="linecode" value="${lineCode}"/>
                                    <pg:param name="itemtype" value="${itemType}"/>
                                    <pg:param name="currentPageNumber" value="${page.curPage}"/>
                                    <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                    <pg:param name="items" value="${page.totalRows}"/>
                                    <%@ include file="/WEB-INF/jsp/toolspgnoincludejquery.jsp" %>
                                </pg:pager>
                            </LABEL>
                        </td>
                    </tr>
                </c:if>
            </table>
        </td>
    </tr>
</table>
</body>
</html>