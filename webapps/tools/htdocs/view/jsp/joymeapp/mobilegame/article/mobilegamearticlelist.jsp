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

        function sort(sort, id,gameDbId) {
               if(gameDbId==''||gameDbId==0){
                   alert("请先查询游戏再进行排序");
                   return ;
               }
            $.post("/mobilegame/article/sort/" + sort, {id: id,gamedbid:gameDbId}, function (req) {
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
            })
        }


    </script>
</head>
<body>

<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr height="40">
        <td height="22" class="page_navigation_td" colspan="2">>> 运营维护 >> 手游排行榜 >> 排行榜长评维护列表</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">长评列表</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="13" class="error_msg_td">
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
                        <form method="post" action="/mobilegame/article/createpage">
                            <table>
                                <tr>
                                    <td>
                                        <%--<p:privilege name="/clientline/iphone/createpage">--%>
                                        <input type="submit" name="button" class="default_button" value="新增长评"/>
                                        <%--</p:privilege>--%>
                                    <td>
                                <tr>
                            </table>
                        </form>



                    </td>
                    <td>
                        <form method="post" action="/mobilegame/article/list">
                            <table>
                                <tr>
                                    <td>
                                       游戏名称： <input type="text" name="gamename" <c:if test="${not empty gameName}">value="${gameName}"</c:if> size="32"/>
                                        <%--<p:privilege name="/clientline/iphone/createpage">--%>
                                        <input type="submit" name="button" class="default_button" value="按游戏名称查询"/>
                                        <%--</p:privilege>--%>
                                    <td>
                                <tr>
                            </table>
                        </form>
                    </td>
                </tr>
            </table>

            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="13" class="default_line_td"></td>
                </tr>
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="13" class="error_msg_td">
                            <b><fmt:message key="${errorMsg}" bundle="${error}"/></b>
                        </td>
                    </tr>
                </c:if>
                <tr class="list_table_title_tr">
                    <td nowrap align="center" width="80">ID</td>
                    <td nowrap align="center">标题</td>
                    <td nowrap align="center">链接</td>
                    <td nowrap align="center">排序</td>
                    <td nowrap align="center">状态</td>
                    <td nowrap align="center">所属游戏库</td>
                    <td nowrap align="center">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="13" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="article" varStatus="st">
                            <tr id="clientitem_${article.id}"
                                class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap align="left">${article.id}</td>
                                <td nowrap align="left">${article.title}</td>
                                <td nowrap align="left">${article.articleUrl}</td>
                                <td nowrap align="center">

                                    <a href="javascript:sort('up','${article.id}','${gameDbId}')"><img
                                            src="/static/images/icon/up.gif"></a>
                                    &nbsp;
                                    <a href="javascript:sort('down','${article.id}','${gameDbId}')"><img
                                            src="/static/images/icon/down.gif"></a>
                                </td>
                                <td nowrap align="left" <c:choose>
                                    <c:when test="${article.validStatus.code == 'valid'}">
                                        style="color: #008000;"
                                    </c:when>
                                    <c:otherwise>
                                        style="color: #ff0000;"
                                    </c:otherwise>
                                </c:choose>><fmt:message key="client.line.status.${article.validStatus.code}"
                                                         bundle="${def}"/></td>
                                <td nowrap align="center">
                                    <c:if test="${not empty gameDbs}">
                                           <c:forEach items="${gameDbs}" var="t">
                                               <c:if test="${t.gameDbId==article.gameDbId}">${t.gameName}</c:if>
                                           </c:forEach>
                                    </c:if>

                                </td>
                                <td nowrap align="center">
                                    <a href="/mobilegame/article/modifypage?id=${article.id}">编辑</a>
                                    <c:choose>
                                        <c:when test="${article.validStatus.code == 'valid'}">
                                            <a href="/mobilegame/article/delete?id=${article.id}">删除</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="/mobilegame/article/recover?id=${article.id}">激活</a>
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
                            <pg:pager url="/mobilegame/article/list"
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
            </form>
        </td>
    </tr>
</table>
</body>
</html>