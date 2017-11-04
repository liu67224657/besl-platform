<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>新游开测榜,热门,正在玩</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.min.js"></script>
    <script type="text/javascript">
        $(function () {

            $("a[data-name=move]").on("click", function () {
                var fromIndex = $(this).attr("data-index"), toIndex;
                var trs = $("#contenttable").find("tr[data-id]");
                var type = $(this).attr("data-type");
                if ($(this).attr("data-type") === 'totop') {
                    toIndex = 0;
                    if (fromIndex === '0') {  //bug 9262
                        return;
                    }
                } else if ($(this).attr("data-type") === 'tobottom') {
                    toIndex = trs.length - 1;
                    if (fromIndex == toIndex) {     //bug 9262
                        return;
                    }
                } else if ($(this).attr("data-type") === 'up10') {
                    toIndex = parseInt($(this).attr("data-index")) - 10;
                    if (toIndex < 0) {
                        toIndex = 0;
                    }
                    if (fromIndex ===  '0') {  //bug 9262
                        return;
                    }

                } else if ($(this).attr("data-type") === 'down10') {
                    toIndex = parseInt($(this).attr("data-index")) + 10;
                    if (toIndex > trs.length - 1) {
                        toIndex = trs.length - 1;
                    }

                    if (fromIndex ==  trs.length - 1) {     //bug 9262
                        return;
                    }

                }
                toSwap(fromIndex, toIndex, type);

            });
            function toSwap(fromIndex, toIndex, type) {
                if (fromIndex === toIndex) {
                    return;
                }

                var trs = $("#contenttable").find("tr[data-id]");
                $.ajax({
                    url: "/gameclient/clientline/game/swaptwo",
                    data: {
                        type: type,
                        fromItemId: trs.eq(fromIndex).attr("data-id"),
                        fromOrder: trs.eq(fromIndex).attr("data-order"),
                        toItemId: trs.eq(toIndex).attr("data-id"),
                        toOrder: trs.eq(toIndex).attr("data-order")
                    },
                    type: "POST",
                    dataType: "json",
                    success: function (data, textStatus) {
                        if (data.rs === 1 && data.result && data.result.resultOrder != 0) {
                            //   window.location.reload();   如果刷新页面，简单 一行高定
                            //以下是不刷新页面的写法
                            var trs = $("#contenttable").find("tr[data-id]");
                            var itemid = trs.eq(fromIndex).attr("data-id");
                            var returnid = trs.eq(toIndex).attr("data-id");

                            var resultOrder = data.result.resultOrder;

                            var item = $("#clientitem_" + itemid).clone(true);
                            item.find("span[class=order]").html(resultOrder);
                            item.attr("data-order", resultOrder);

                            $("#clientitem_" + itemid).remove();

                            if (type == 'up10' || type == 'totop') {
                                $("#clientitem_" + returnid).before(item);
                            } else {
                                $("#clientitem_" + returnid).after(item);
                            }

                            trs = $("#contenttable").find("tr[data-id]");   //重新获取一下
                            //重建所有的a链接中的data-index值
                            for (var i = 0; i < trs.length; i++) {
                                $.each(trs.eq(i).find("a[data-index]"), function (index, item) {
                                    $(this).attr("data-index", i);
                                });
                            }
                        } else {
                            alert("更改排序失败！");
                        }
                    }
                })
                ;
            }

        })
        ;


        function deleteItemByItemId(itemId, lineId, lineName, lineCode) {
            var msg = "您确定要删除item_id为" + itemId + "的clientLineItem吗？\n\n请确认！";
            if (confirm(msg) == true) {
                window.location.href = "/gameclient/clientline/game/itemdelete?itemId=" + itemId + "&lineId=" + lineId + "&lineCode=" + lineCode + "&lineName=" + lineName;
            }
        }

        function sort(sort, itemid, lineid, validstatus) {
            if (validstatus == 'removed') {
                alert("请先恢复文章再进行排序");
                return false;
            }
            $.post("/gameclient/clientline/game/sort/" + sort, {itemid: itemid, lineid: lineid}, function (req) {
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
                        var indexTemp;
                        var orderTemp;

                        var item = $("#clientitem_" + itemid).clone(true);
                        indexTemp = item.find("a[data-index]").eq(0).attr("data-index");
                        orderTemp = parseInt(item.find("span[class=order]").text());


                        $.each(item.find("a[data-index]"), function (index, item) {
                            $(this).attr("data-index", $("#clientitem_" + returnid).find("a[data-index]").eq(0).attr("data-index"));
                        });
                        item.find("span[class=order]").html(parseInt($("#clientitem_" + returnid).find("span[class=order]").text()));
                        item.attr("data-order", $("#clientitem_" + returnid).attr("data-order"));

                        $.each($("#clientitem_" + returnid).find("a[data-index]"), function (index, item) {
                            $(this).attr("data-index", indexTemp);
                        });
                        $("#clientitem_" + returnid).find("span[class=order]").html(orderTemp);
                        $("#clientitem_" + returnid).attr("data-order", orderTemp);
                        if (sort == 'up') {
                            $("#clientitem_" + itemid).remove();
                            $("#clientitem_" + returnid).before(item);
                        } else {
                            $("#clientitem_" + itemid).remove();
                            $("#clientitem_" + returnid).after(item);
                        }
                        //  window.location.reload(true);
                    }
                }
            });
        }


    </script>

</head>
<body>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷手游画报管理 >> 新游开测,大家正在玩,热门的clientline</td>
    </tr>
    <tr>
        <td height="15" valign="top">
            <input type="button" class="default_button" value="返回"
                   onclick="javascipt:window.location.href='/gameclient/clientline/game/list';"/>
        </td>
        <td height="15" valign="top">
            <form method="post" id="create_item_form"
                  action="/gameclient/clientline/game/itemcreatepage">
                <input type="hidden" value="${lineId}" name="lineId"/>
                <input type="hidden" value="${lineCode}" name="lineCode"/>
                <input type="hidden" value="${lineName}" name="lineName"/>
                <input type="submit" name="button" class="default_button" value="添加新的item"/>
            </form>
        </td>
        <td height="15" valign="top">
            <form method="post" action="/gameclient/clientline/game/itemlist">
                <input type="hidden" value="${lineId}" name="lineId"/>
                <input type="hidden" value="${lineCode}" name="lineCode"/>
                <input type="hidden" value="${lineName}" name="lineName"/>
                <input type="text"  name="gamename"/>
                <input type="submit" name="button" class="default_button" value="搜索"/>
            </form>
        </td>
    </tr>
</table>

<label><b>${lineName}(${lineCode})</b>的clientline</label>     <c:if test="${fn:contains(lineCode, 'gc_newgame')}"> <span style="color: red;">排序值仅适用于热门页中热门块的排序,按由小到大排序</span>  </c:if>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">

    <tr>

        <td height="1" valign="top">

            <table width="100%" border="0" cellspacing="1" cellpadding="0" id="contenttable">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="10" class="error_msg_td">
                            <b><fmt:message key="${errorMsg}" bundle="${error}"/></b>
                        </td>
                    </tr>
                </c:if>
                <tr class="list_table_title_tr">
                    <td width="80">client_line_item表的id</td>
                    <td>game_db表的id</td>
                    <td>游戏名称</td>
                    <td>游戏主图</td>
                    <c:if test="${!fn:contains(lineCode, 'gc_newgame')}">
                        <td nowrap align="center">排序操作</td>
                        <td nowrap align="center">置顶操作</td>
                        <td nowrap align="center">上、下移操作</td>
                    </c:if>
                    <td nowrap align="center">下载推荐</td>
                    <td nowrap align="center">上市时间</td>
                    <c:if test="${fn:contains(lineCode, 'gc_newgame')}">
                        <td nowrap align="center">排序值</td>
                        <td>链接事件</td>
                        <td>开测时间显示方式</td>
                        <td>自定义显示文字</td>
                    </c:if>
                    <td nowrap align="center">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="10" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${not empty list}">
                        <c:forEach items="${list}" var="item" varStatus="st">
                            <tr id="clientitem_${item.itemId}" data-order="${item.displayOrder}"
                                data-id="${item.itemId}"
                                class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose> ">
                                <td>${item.itemId}</td>
                                <td>${item.gameid}</td>
                                <td>${item.name}</td>
                                <td><img src="${item.icon}" height="50" width="50"/></td>
                                <c:if test="${!fn:contains(lineCode, 'gc_newgame')}">
                                    <td nowrap align="center">
                                        <span class="order"> ${item.displayOrder}</span>&nbsp;&nbsp;&nbsp;
                                        <a href="javascript:sort('up','${item.itemId}','${lineId}','')"><img
                                                src="/static/images/icon/up.gif"></a>
                                        &nbsp;
                                        <a href="javascript:sort('down','${item.itemId}','${lineId}','')"><img
                                                src="/static/images/icon/down.gif"></a>
                                    </td>
                                    <td nowrap align="center">
                                        <a href="javascript:void(0);" data-index="${st.index}" data-type="totop"
                                           data-name="move">置顶</a> &nbsp;&nbsp; <a href="javascript:void(0);"
                                                                                   data-index="${st.index}"
                                                                                   data-type="tobottom"
                                                                                   data-name="move">置底</a>
                                    </td>
                                    <td nowrap align="center">
                                        <a href="javascript:void(0);" data-index="${st.index}"
                                           data-type="up10"
                                           data-name="move">上移10条</a> &nbsp;&nbsp; <a href="javascript:void(0);"
                                                                                      data-index="${st.index}"
                                                                                      data-type="down10"
                                                                                      data-name="move">下移10条</a>
                                    </td>
                                </c:if>
                                <td><textarea style="margin: 0px; width: 100%; height: 66px;"
                                              disabled="disabled">${item.description}</textarea></td>
                                <td nowrap align="center">${item.gamePublicTime}</td>
                                <c:if test="${fn:contains(lineCode, 'gc_newgame')}">
                                    <td> ${item.displayOrder}</td>
                                    <td><fmt:message key="gameclient.newgametoplay.linkevent.${item.categoryColor}"
                                                     bundle="${def}"/></td>
                                    <c:choose><c:when test="${item.showType=='1'}">
                                        <td>精确时间</td>
                                        <td>此项无效</td>
                                    </c:when><c:when test="${item.showType=='2'}">
                                        <td>自定义显示文字</td>
                                        <td>${item.customContent}</td>
                                    </c:when><c:otherwise>
                                        <td>${item.showType}</td>
                                        <td></td>
                                    </c:otherwise></c:choose>
                                </c:if>
                                <td nowrap>
                                    <a href="/gameclient/clientline/game/itemmodifypage?itemId=${item.itemId}&lineName=${lineName}&lineCode=${lineCode}&name=${item.name}">编辑</a>
                                    <a href='javascript:;'
                                       onclick='deleteItemByItemId(${item.itemId},${lineId},"${lineName}","${lineCode}");'>删除</a>

                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="10" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="10" height="1" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="10" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="10">
                            <pg:pager url="/gameclient/clientline/game/itemlist"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="lineId" value="${lineId}"/>
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