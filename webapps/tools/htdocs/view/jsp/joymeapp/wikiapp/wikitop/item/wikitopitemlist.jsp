<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>活动轮播图</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script>
        function sort(sort, activitytopmenuid) {
            var appkey = $("#appkey").val();
            $.post("/joymeapp/activitytopmenu/sort/" + sort, {activitytopmenuid:activitytopmenuid,appkey:appkey}, function(req) {
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
                            var itemclass = item.attr("class");
                            var upclass = $("#clientitem_" + returnid).attr("class");
                            item.removeClass(itemclass);
                            item.addClass(upclass);
                            $("#clientitem_" + returnid).addClass(itemclass);
                            $("#clientitem_" + returnid).removeClass(upclass);
                            $("#clientitem_" + returnid).before(item);
                        } else {
                            var item = $("#clientitem_" + itemid).clone();
                            $("#clientitem_" + itemid).remove();
                            var itemclass = item.attr("class");
                            var upclass = $("#clientitem_" + returnid).attr("class");
                            item.removeClass(itemclass);
                            item.addClass(upclass);
                            $("#clientitem_" + returnid).addClass(itemclass);
                            $("#clientitem_" + returnid).removeClass(upclass);
                            $("#clientitem_" + returnid).after(item);
                        }
                    }
                }
            });
        }
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
    <td height="22" class="page_navigation_td">>> 运营管理 >> 条目管理 >> wikiTOP</td>
</tr>
<tr>
    <td height="100%" valign="top"><br>
        <table border="0" cellspacing="0" cellpadding="0">
            <tr>
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
                    <form action="/wiki/top/item/createpage" method="post">
                        <table width="100%">
                            <tr>
                                <td>
                                    <input type="hidden" name="code" value="${code}"/>
                                    <input type="submit" name="button" class="default_button" value="添加新的wiki"/>
                                </td>
                                <td>
                                </td>
                            </tr>
                        </table>
                    </form>


                    <form action="/wiki/top/item/list" method="post">
                        <table width="100%">
                            <tr>
                                <td>
                                    <input type="hidden" name="linecode" value="${code}"/>
                                    <input type="text" name="title" value="${title}"/>
                                    <input type="submit" name="button" class="default_button" value="按wiki名称查询"/>
                                </td>
                                <td>
                                </td>
                            </tr>
                        </table>
                    </form>
                </td>
                <td>
                    <%--<table>--%>
                    <%--<tr>--%>
                    <%--<td>--%>
                    <%--<form action="/joymeapp/activitytopmenu/createpage" method="post">--%>
                    <%--<input type="submit" name="button" class="default_button" value="添加活动轮播图"/>--%>
                    <%--</form>--%>
                    <%--</td>--%>
                    <%--</tr>--%>
                    <%--</table>--%>
                </td>
            </tr>
        </table>
        <table width="100%" border="0" cellspacing="1" cellpadding="0">
            <tr>
                <td height="1" colspan="15" class="default_line_td"></td>
            </tr>
            <tr class="list_table_title_tr">
                <td nowrap align="center" width="">ID</td>
                <td nowrap align="center" width="">WIKI名称</td>
                <td nowrap align="center" width="">图片</td>
                <td nowrap align="center" width="">链接</td>
                <td nowrap align="center" width="">热度</td>
                <td nowrap align="center" width="">wikikey</td>
                <td nowrap align="center" width="">标签</td>
                <td nowrap align="center" width="">itemtype</td>
                <td nowrap align="center" width="">状态</td>
                <td nowrap align="center" width="">操作</td>
            </tr>
            <tr>
                <td height="1" colspan="15" class="default_line_td"></td>
            </tr>
            <c:choose>
                <c:when test="${list.size() > 0}">
                    <c:forEach items="${list}" var="dto" varStatus="st">
                        <tr align="center" id="clientitem_${dto.itemId}"
                            class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                            <td nowrap>${dto.itemId}</td>
                            <td nowrap>${dto.title}</td>
                            <td nowrap><img src="${dto.picUrl}" width="120px;"/></td>
                            <td nowrap><a href="${dto.url}" target="_blank">链接</a></td>
                            <td nowrap>${dto.rate}</td>
                            <td nowrap>${dto.directId}</td>
                            <td nowrap>${dto.category}</td>
                            <td nowrap>
                                <fmt:message key="client.item.type.${dto.itemType.code}" bundle="${def}"/>
                            </td>
                            <td nowrap>
                                <c:choose>
                                    <c:when test="${dto.validStatus.code eq 'valid'}">
                                            <span style="color: #008000;">
                                                <fmt:message key="client.line.status.${dto.validStatus.code}"
                                                             bundle="${def}"/>
                                            </span>
                                    </c:when>
                                    <c:otherwise>
                                            <span style="color: #ff0000;">
                                                <fmt:message key="client.line.status.${dto.validStatus.code}"
                                                             bundle="${def}"/>
                                            </span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                                <%--<td nowrap>--%>

                                <%--<fmt:message key="joymeapp.favorite.platform.${dto.platform}" bundle="${def}"/>--%>
                                <%--</td>--%>


                                <%--<td nowrap>--%>
                                <%--${dto.createDate}/${dto.createUserid}--%>
                                <%--</td>--%>
                                <%--<td nowrap>--%>
                                <%--${dto.updateDate}/${dto.updateUserid}--%>
                                <%--</td>--%>
                            <td nowrap>
                                <a href="/wiki/top/item/modifypage?itemid=${dto.itemId}&code=${code}">编辑</a>&nbsp;
                                <c:choose>
                                    <c:when test="${dto.validStatus.code eq 'valid'}">
                                        <a href="/wiki/top/item/delete?itemid=${dto.itemId}&code=${code}">删除</a>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="/wiki/top/item/recover?itemid=${dto.itemId}&code=${code}">恢复</a>
                                    </c:otherwise>
                                </c:choose>

                            </td>
                                <%--<td nowrap>${dto.activityTopMenu.linkUrl}</td>--%>
                                <%--<td nowrap><img width="120" height="30" src="${dto.activityTopMenu.picUrl}"/></td>--%>
                                <%--<td nowrap>${dto.activityTopMenu.menuDesc}</td>--%>
                                <%--<td nowrap><fmt:message key="joymeapp.favorite.platform.${dto.activityTopMenu.platform}"--%>
                                <%--bundle="${def}"/></td>--%>
                                <%--<td nowrap>${dto.authApp.appName}</td>--%>
                                <%--<td nowrap>${dto.appChannel.channelName}</td>--%>
                                <%--<td nowrap><fmt:message key="joymeapp.menu.type.${dto.activityTopMenu.menuType}"--%>
                                <%--bundle="${def}"/></td>--%>
                                <%--<td nowrap><fmt:message key="joymeapp.menu.status.${dto.activityTopMenu.isNew}"--%>
                                <%--bundle="${def}"/></td>--%>
                                <%--<td nowrap><fmt:message key="joymeapp.menu.status.${dto.activityTopMenu.isHot}"--%>
                                <%--bundle="${def}"/></td>--%>

                                <%--<td nowrap>${dto.activityTopMenu.createUserId}/${dto.activityTopMenu.createIp}</td>--%>
                                <%--<td nowrap>${dto.activityTopMenu.lastModifyUserId}/${dto.activityTopMenu.lastModifyIp}</td>--%>
                                <%--<td nowrap>--%>
                                <%--<a href="/joymeapp/activitytopmenu/modifypage?activitytopmenuid=${dto.activityTopMenu.activityTopMenuId}">编辑</a>&nbsp;--%>
                                <%--<c:choose>--%>
                                <%--<c:when test="${dto.activityTopMenu.validStatus.code eq 'valid'}">--%>
                                <%--<a href="/joymeapp/activitytopmenu/delete?activitytopmenuid=${dto.activityTopMenu.activityTopMenuId}">删除</a>--%>
                                <%--</c:when>--%>
                                <%--<c:otherwise>--%>
                                <%--<a href="/joymeapp/activitytopmenu/recover?activitytopmenuid=${dto.activityTopMenu.activityTopMenuId}">恢复</a>--%>
                                <%--</c:otherwise>--%>
                                <%--</c:choose>--%>

                                <%--</td>--%>
                        </tr>
                    </c:forEach>
                    <tr>
                        <td height="1" colspan="15" class="default_line_td"></td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="15" class="error_msg_td">暂无数据!</td>
                    </tr>
                </c:otherwise>
            </c:choose>
            <tr>
                <td colspan="15" height="1" class="default_line_td"></td>
            </tr>
            <c:if test="${page.maxPage > 1}">
                <tr class="list_table_opp_tr">
                    <td colspan="10">
                        <pg:pager url="/wiki/top/item/list"
                                  items="${page.totalRows}" isOffset="true"
                                  maxPageItems="${page.pageSize}"
                                  export="offset, currentPageNumber=pageNumber" scope="request">
                            <pg:param name="linecode" value="${code}"/>
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