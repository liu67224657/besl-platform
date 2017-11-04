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
        function sort(sort, itemid, lineid, validstatus) {
            if (validstatus == 'removed') {
                alert("请先恢复文章再进行排序");
                return false;
            }
            $.post("/platinum/item/sort/" + sort, {itemid:itemid,lineid:lineid}, function(req) {
                var resMsg = eval('(' + req + ')');
                if (resMsg.rs == '0') {
                    return false;
                } else {
                    var result = resMsg.result;

                    if (result == null) {

                    } else {
                        var itemid = result.itemid;
                        var sort = result.sort;
                        var returnid = result.returnitemid;
                        if (sort == 'up') {
                            var item = $("#clientitem_" + itemid).clone();
                            $("#clientitem_" + itemid).remove();
//                            var itemclass = item.attr("class");
//                            var upclass = $("#clientitem_" + returnid).attr("class");
//                            item.removeClass(itemclass);
//                            item.addClass(upclass);
//                            $("#clientitem_" + returnid).addClass(itemclass);
//                            $("#clientitem_" + returnid).removeClass(upclass);
                            $("#clientitem_" + returnid).before(item);
                        } else {
                            var item = $("#clientitem_" + itemid).clone();
                            $("#clientitem_" + itemid).remove();
//                            var itemclass = item.attr("class");
//                            var upclass = $("#clientitem_" + returnid).attr("class");
//                            item.removeClass(itemclass);
//                            item.addClass(upclass);
//                            $("#clientitem_" + returnid).addClass(itemclass);
//                            $("#clientitem_" + returnid).removeClass(upclass);
                            $("#clientitem_" + returnid).after(item);
                        }
                    }
                }
            });
        }
        function aaa() {
            $("#clientitem_77").after($("#clientitem_78").clone());


        }
        $().ready(function() {

            $('#form_submit').bind('submit', function() {


            });
        });
        function create(lineId, itemType) {
            window.location.href = "/platinum/item/createpage?lineid=" + lineId + "&itemtype=" + itemType;
        }

        function modify(lineId, itemType) {
            window.location.href = "/platinum/item/createpage?lineid=" + lineId + "&itemtype=" + itemType;
        }
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷APP >> APP精选推荐列表</td>
    </tr>
    <tr>
        <td height="1" valign="top">
            <table>
                <tr>
                    <td height="1">
                        <form action="/platinum/item/list" method="post">
                            <table>
                                <tr>
                                    <td>
                                        按条件查询:
                                    </td>
                                    <td>
                                        <input type="hidden" value="${clientLine.lineId}" name="lineid"/>
                                        <input type="hidden" name="itemtype" value="${clientLine.itemType.code}"/>
                                        <select name="validstatus">
                                            <option value="">请选择</option>
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
                        <form action="/platinum/item/createpage" method="post">
                            <table>
                                <tr>
                                    <td>
                                        <input type="hidden" name="lineid" value="${clientLine.lineId}"/>
                                        <input type="hidden" name="itemtype" value="${clientLine.itemType.code}"/>
                                        <p:privilege name="/platinum/item/createpage">
                                            <input type="submit" name="create_button" class="default_button"
                                                   value="添加子元素"/>
                                        </p:privilege>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </td>
                </tr>
            </table>
            <form action="/platinum/item/list" method="post">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <c:if test="${fn:length(errorMsg)>0}">
                        <tr>
                            <td height="1" colspan="13" class="error_msg_td">
                                <b><fmt:message key="${errorMsg}" bundle="${error}"/></b>
                            </td>
                        </tr>
                    </c:if>
                    <tr class="list_table_title_tr">
                        <td nowrap align="left">ID</td>
                        <td nowrap align="left">标题</td>
                        <td nowrap align="left">描述</td>
                        <td nowrap align="left">图片</td>
                        <td nowrap align="left">链接地址</td>
                        <td nowrap align="left">跳转类别</td>
                        <c:choose>
                            <c:when test="${clientLine.itemType.code == apps}">
                                <td nowrap align="left">应用大小</td>
                                <td nowrap align="left">排序</td>
                            </c:when>
                            <c:when test="${clientLine.itemType.code == events}">
                                <td nowrap align="left">有效期</td>
                                <td nowrap align="left">热门</td>
                            </c:when>
                        </c:choose>
                        <td nowrap align="left">状态</td>
                        <td nowrap align="left">创建信息</td>
                        <td nowrap align="left">操作</td>
                    </tr>
                    <tr>
                        <td height="1" colspan="13" class="default_line_td"></td>
                    </tr>
                    <c:choose>
                        <c:when test="${list.size() > 0}">
                            <c:forEach items="${list}" var="item" varStatus="st">
                                <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                    <td nowrap align="left">${item.itemId}</td>
                                    <td nowrap align="left">${item.title}</td>
                                    <td nowrap align="left">${item.desc}</td>
                                    <td nowrap align="left"><img src="${item.picUrl}" height="100" width="100"/></td>
                                    <td nowrap align="left">${item.url}</td>
                                    <td nowrap align="left"><fmt:message
                                            key="platinum.item.redirect.${item.redirectType.code}"
                                            bundle="${def}"/></td>
                                    <c:choose>
                                        <c:when test="${clientLine.itemType.code == apps}">
                                            <td nowrap align="left">${item.param.size}</td>
                                            <td nowrap align="left">
                                                <a href="/platinum/item/sort/up?lineid=${lineId}&itemid=${item.itemId}"><img
                                                        src="/static/images/icon/up.gif"></a>
                                                <a href="/platinum/item/sort/down?lineid=${lineId}&itemid=${item.itemId}"><img
                                                        src="/static/images/icon/down.gif"></a>
                                            </td>
                                        </c:when>
                                        <c:when test="${clientLine.itemType.code == events}">
                                            <td nowrap align="left">${item.param.startDate}-<br/>-${item.param.endDate}</td>
                                            <td nowrap align="left"><c:choose><c:when test="${item.hot}">是</c:when><c:otherwise>否</c:otherwise></c:choose></td>
                                        </c:when>
                                    </c:choose>
                                    <td nowrap align="left" <c:choose>
                                        <c:when test="${item.validStatus.code == 'valid'}">
                                            style="color: #008000;"
                                        </c:when>
                                        <c:otherwise>
                                            style="color: #ff0000;"
                                        </c:otherwise>
                                    </c:choose>>
                                        <fmt:message key="client.item.status.${item.validStatus.code}" bundle="${def}"/>
                                    </td>
                                    <td nowrap align="left">
                                        <fmt:formatDate value="${item.itemCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </td>
                                    <td nowrap align="left">
                                        <a href="/platinum/item/modifypage?itemid=${item.itemId}&lineid=${item.lineId}&itemtype=${item.itemType.code}">编辑</a>
                                        <c:choose>
                                            <c:when test="${item.validStatus.code == 'valid'}">
                                                <a href="/platinum/item/delete?itemid=${item.itemId}&lineid=${item.lineId}">删除</a>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="/platinum/item/recover?itemid=${item.itemId}&lineid=${item.lineId}">恢复</a>
                                            </c:otherwise>
                                        </c:choose>
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
                                <pg:pager url="/platinum/item/list"
                                          items="${page.totalRows}" isOffset="true"
                                          maxPageItems="${page.pageSize}"
                                          export="offset, currentPageNumber=pageNumber" scope="request">
                                    <pg:param name="lineid" value="${clientLine.lineId}"/>
                                    <pg:param name="itemtype" value="${clientLine.itemType.code}"/>
                                    <pg:param name="currentPageNumber" value="${page.curPage}"/>
                                    <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                    <pg:param name="items" value="${page.totalRows}"/>
                                    <%@ include file="/WEB-INF/jsp/toolspg.jsp" %>
                                </pg:pager>
                            </td>
                        </tr>
                    </c:if>
                </table>
            </form>
        </td>
    </tr>
</table>
</body>
</html>