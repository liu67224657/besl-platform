<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>游戏分类最终列表管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.min.js"></script>
    <script type="text/javascript">
        $(function () {
            $(".zxx_text_overflow").each(function () {
                var maxwidth = 23;
                if ($(this).text().length > maxwidth) {
                    $(this).attr("title", $(this).text());
                    $(this).text($(this).text().substring(0, maxwidth));
                    $(this).html($(this).html() + "...");
                }
            });

        });

        function toDelete(itemId, gameName, subLineId, subLineName, subLineCode, lineCode, lineName, platform) {
            var msg = "您确定要删除gameName为<" + gameName + ">的游戏条目吗？\n\n请确认！";
            if (confirm(msg) == true) {
                window.location.href = "/gameclient/clientline/gamecategory/itemdelete?itemId=" + itemId + "&subLineId=" + subLineId + "&subLineName=" + subLineName + "&subLineCode=" + subLineCode + "&lineName=" + lineName + "&lineCode=" + lineCode + "&platform=" + platform;
            }
        }
        var sort = (function () {
            var array = [];
            <c:if test="${not empty list}">
            <c:forEach items="${list}" var="item" varStatus="st">
            array[${st.index}] = {};
            array[${st.index}].id = ${item.itemId};
            array[${st.index}].order = ${item.displayOrder};
            </c:forEach>
            </c:if>
            return function (type, index, layer) {
                //第0个元素不能再向上，最后一个元素不能再向下
                if (type == 'up' && index == 0) {
                    return false;
                }
                if (type == 'down' && index == array.length - 1) {
                    return false;
                }
                var dataObject;
                if (type == 'up') {
                    dataObject = {
                        type: layer,
                        fromId: array[index].id,
                        fromOrder: array[index].order,
                        toId: array[index - 1].id,
                        toOrder: array[index - 1].order
                    };
                } else {
                    dataObject = {
                        type: layer,
                        fromId: array[index].id,
                        fromOrder: array[index].order,
                        toId: array[index + 1].id,
                        toOrder: array[index + 1].order
                    };
                }

                $.ajax({
                    url: '/gameclient/clientline/gamecategory/sort',
                    data: dataObject,
                    type: "POST",
                    dataType: "json",
                    success: function (data, textStatus) {
                        if (data.rs != 1) {
                            return false;
                        }
                        if (type == 'up') {
                            $("tr[data-id]").eq(index).find("td").eq(2).find("a").eq(0).attr("onclick", "sort('up'," + (index - 1) + ",'clientlineitem');");
                            $("tr[data-id]").eq(index).find("td").eq(2).find("a").eq(1).attr("onclick", "sort('down'," + (index - 1) + ",'clientlineitem');");
                            $("tr[data-id]").eq(index - 1).find("td").eq(2).find("a").eq(0).attr("onclick", "sort('up'," + (index) + ",'clientlineitem');");
                            $("tr[data-id]").eq(index - 1).find("td").eq(2).find("a").eq(1).attr("onclick", "sort('down'," + (index) + ",'clientlineitem');");

                            var item = $("tr[data-id]").eq(index).clone();
                            $("tr[data-id]").eq(index).remove();
                            $("tr[data-id]").eq(index - 1).before(item);

                            $("tr[data-id]").eq(index - 1).find("td").eq(2).find("span").html(dataObject.toOrder);
                            $("tr[data-id]").eq(index).find("td").eq(2).find("span").html(dataObject.fromOrder);

                            var tempId = array[index].id;
                            array[index].id = array[index - 1].id;
                            array[index - 1].id = tempId;
                        } else {

                            $("tr[data-id]").eq(index).find("td").eq(2).find("a").eq(0).attr("onclick", "sort('up'," + (index + 1) + ",'clientlineitem');");
                            $("tr[data-id]").eq(index).find("td").eq(2).find("a").eq(1).attr("onclick", "sort('down'," + (index + 1) + ",'clientlineitem');");

                            $("tr[data-id]").eq(index + 1).find("td").eq(2).find("a").eq(0).attr("onclick", "sort('up'," + (index) + ",'clientlineitem');");
                            $("tr[data-id]").eq(index + 1).find("td").eq(2).find("a").eq(1).attr("onclick", "sort('down'," + (index) + ",'clientlineitem');");

                            var item = $("tr[data-id]").eq(index).clone();
                            $("tr[data-id]").eq(index).remove();
                            $("tr[data-id]").eq(index).after(item);

                            $("tr[data-id]").eq(index + 1).find("td").eq(2).find("span").html(dataObject.toOrder);
                            $("tr[data-id]").eq(index).find("td").eq(2).find("span").html(dataObject.fromOrder);

                            var tempId = array[index].id;
                            array[index].id = array[index + 1].id;
                            array[index + 1].id = tempId;


                        }
                    },
                });
            };
        })();

    </script>

</head>
<body>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷手游画报管理 >> 热门页游戏分类管理</td>
    </tr>
    <tr>
        <td height="32px">
            <input type="button" class="default_button" value="返回"
                   onclick="javascipt:window.location.href='/gameclient/clientline/gamecategory/sublist?lineCode=${lineCode}&lineName=${lineName}&platform=${platform}';"/>
            &nbsp;&nbsp;
            <input type="button" class="default_button" value="添加新的item"
                   onclick="javascipt:window.location.href='/gameclient/clientline/gamecategory/itemcreatepage?lineCode=${lineCode}&lineName=${lineName}&platform=${platform}&subLineId=${subLineId}&subLineName=${subLineName}&subLineCode=${subLineCode}';"/>

        </td>
    </tr>
</table>

<label>所属平台: <b><c:choose><c:when test="${platform==0}">ios</c:when><c:otherwise>android</c:otherwise></c:choose></b>
    &nbsp;父类别名:<b>${lineName}</b>(${lineCode}) &nbsp;&nbsp;子类别名:<b>${subLineName}</b>(${subLineCode})的clientline</label>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="1" valign="top">
            <table width="100%" border="0" cellspacing="1" cellpadding="0" id="contenttable">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="16" class="error_msg_td">
                            <b><fmt:message key="${errorMsg}" bundle="${error}"/></b>
                        </td>
                    </tr>
                </c:if>
                <tr class="list_table_title_tr">
                    <%--    <td nowrap align="center">client_line_item表的id</td>    --%>
                    <td nowrap align="center">game_db表的id</td>
                    <td nowrap align="center">游戏名称</td>
                    <td nowrap align="center">排序</td>
                    <td nowrap align="center">游戏主图</td>
                    <td nowrap align="center">数据显示</td>
                    <td nowrap align="center">游戏类型</td>
                    <td nowrap align="center">跳转目标</td>
                    <%--     <td nowrap align="center">跳转类型</td>    --%>
                    <%--    <td nowrap align="center">跳转链接</td>    --%>
                    <td nowrap align="center">点赞人数</td>
                    <td nowrap align="center">星级</td>
                    <td nowrap align="center">推荐语</td>
                    <td nowrap align="center">右上角图标(tag)</td>
                    <td nowrap align="center">创建日期</td>
                    <td nowrap align="center">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="16" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${not empty list}">
                        <c:forEach items="${list}" var="item" varStatus="st">
                            <tr data-id="" class="<c:choose><c:when
                                                  test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose> ">
                                    <%--     <td nowrap align="center">${item.itemId}</td>      --%>
                                <td nowrap align="center">${item.gameDbId}</td>
                                <td nowrap align="center">${item.gameName}</td>
                                <td nowrap align="left"><span>${item.displayOrder}</span> &nbsp;&nbsp; <a
                                        href="javascript:void(0);"
                                        onclick="sort('up',${st.index},'clientlineitem');"><img
                                        src="/static/images/icon/up.gif"></a>
                                    &nbsp;
                                    <a href="javascript:void(0);"
                                       onclick="sort('down',${st.index},'clientlineitem');"><img
                                            src="/static/images/icon/down.gif"></a></td>
                                <td nowrap align="center"><img src="${item.icon}" height="50" width="50"/></td>
                                <td nowrap align="center"><c:choose><c:when
                                        test="${item.showType=='type'}">游戏类型</c:when><c:otherwise>点赞人数</c:otherwise></c:choose></td>
                                <td nowrap align="center">${item.gameTypeDesc}</td>
                                <td nowrap align="center">${item.jumpTarget}</td>
                                    <%--    <td nowrap align="center">${item.jt}</td>      --%>
                                    <%--    <td nowrap align="center" class="zxx_text_overflow">${item.ji} </td>     --%>
                                <td nowrap align="center">${item.likeNum} </td>
                                <td nowrap align="center">${item.gameRate} </td>
                                <td nowrap align="center">${item.downloadRecommend}</td>
                                <td nowrap align="center"><img src="${item.tag}" height="50" width="50"/></td>
                                <td nowrap align="center"><fmt:formatDate value="${item.itemCreateDate}"
                                                                          pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                <td nowrap align="center">
                                    <a href="/gameclient/clientline/gamecategory/itemmodifypage?itemId=${item.itemId}&subLineId=${subLineId}&subLineName=${subLineName}&subLineCode=${subLineCode}&lineName=${lineName}&lineCode=${lineCode}&platform=${platform}">编辑</a>
                                    <a href="javascript:void(0);"
                                       onclick="toDelete(${item.itemId},'${item.gameName}',${subLineId},'${subLineName}','${subLineCode}','${lineCode}','${lineName}',${platform});">删除</a>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="16" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="16" height="1" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="16" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="16">
                            <pg:pager url="/gameclient/clientline/gamecategory/itemlist"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="subLineId" value="${subLineId}"/>
                                <pg:param name="subLineName" value="${subLineName}"/>
                                <pg:param name="subLineCode" value="${subLineCode}"/>
                                <pg:param name="platform" value="${platform}"/>
                                <pg:param name="lineName" value="${lineName}"/>
                                <pg:param name="lineCode" value="${lineCode}"/>
                                <pg:param name="currentPageNumber" value="${page.curPage}"/>
                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                <pg:param name="items" value="${page.totalRows}"/>
                                <%@ include file="/WEB-INF/jsp/toolspgwithnewversionjquery.jsp" %>
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