<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>自定义推荐楼层管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.min.js"></script>
    <script type="text/javascript">
        $(function () {

            <c:if test="${not empty list}">
            var type = {};
            <c:forEach items="${types}" var="item" varStatus="st">
            type['type' +${item.key}] = '${item.key}__<fmt:message key="client.item.redirect.${item.key}" bundle="${def}"/>';
            </c:forEach>
            var desc = [];
            <c:forEach items="${list}" var="item" varStatus="st">
            desc[${st.index}] = '${item.desc}';
            </c:forEach>
            $.each($("tr[data-id]"), function (index, item) {
                var jsonObj = jQuery.parseJSON(desc[index]);
                $(this).find("td:eq(1)").html(jsonObj.floorName);
                $(this).find("td:eq(2)").find("img").attr("src", jsonObj.floorIcon);
                $(this).find("td:eq(4)").find("img").attr("src", jsonObj.pic1st);
                $(this).find("td:eq(5)").html(type['type' + jsonObj.jt1]);
                $(this).find("td:eq(6)").html(jsonObj.ji1);
                $(this).find("td:eq(7)").find("img").attr("src", jsonObj.pic2nd);
                $(this).find("td:eq(8)").html(type['type' + jsonObj.jt2]);
                $(this).find("td:eq(9)").html(jsonObj.ji2);
                $(this).find("td:eq(10)").find("img").attr("src", jsonObj.pic3rd);
                $(this).find("td:eq(11)").html(type['type' + jsonObj.jt3]);
                $(this).find("td:eq(12)").html(jsonObj.ji3);
                if (jsonObj.moreLink == '1') {
                    $(this).find("td:eq(13)").html('有');
                    $(this).find("td:eq(14)").html(type['type' + jsonObj.jt]);
                    $(this).find("td:eq(15)").html(jsonObj.ji);
                } else {
                    $(this).find("td:eq(13)").html('没有');
                    $(this).find("td:eq(14)").html("----");
                    $(this).find("td:eq(15)").html("----");
                }

            });

            </c:if>

            $(".zxx_text_overflow").each(function () {
                var maxwidth = 23;
                if ($(this).text().length > maxwidth) {
                    $(this).attr("title", $(this).text());
                    $(this).text($(this).text().substring(0, maxwidth));
                    $(this).html($(this).html() + "...");
                }
            });

        });


        function deleteItemByItemId(itemId, lineId, lineName, lineCode, index, type) {
            var floorName = $("tr[data-id]").eq(index).find("td:eq(1)").html();

            var msg = "";
            if (type == 1) {
                msg = "您确定要停用item_id为" + itemId + ",楼层名称为:<<" + floorName + ">>的自定义楼层吗？\n\n请确认！";
            } else {
                msg = "您确定要启用item_id为" + itemId + ",楼层名称为:<<" + floorName + ">>的自定义楼层吗？\n\n请确认！";
            }
            if (confirm(msg) == true) {
                window.location.href = "/gameclient/clientline/hotfloor/itemdelete?itemId=" + itemId + "&lineId=" + lineId + "&lineCode=" + lineCode + "&lineName=" + lineName + "&type=" + type;
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
            return function (type, index) {
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
                        sort: "",
                        fromId: array[index].id,
                        fromOrder: array[index].order,
                        toId: array[index - 1].id,
                        toOrder: array[index - 1].order
                    };
                } else {
                    dataObject = {
                        sort: "",
                        fromId: array[index].id,
                        fromOrder: array[index].order,
                        toId: array[index + 1].id,
                        toOrder: array[index + 1].order
                    };
                }

                $.ajax({
                    url: '/gameclient/clientline/hotfloor/sort',
                    data: dataObject,
                    type: "POST",
                    dataType: "json",
                    success: function (data, textStatus) {
                        if (data.rs != 1) {
                            return false;
                        }
                        if (type == 'up') {
                            $("tr[data-id]").eq(index).find("td").eq(3).find("a").eq(0).attr("onclick", "sort('up'," + (index - 1) + ");");
                            $("tr[data-id]").eq(index).find("td").eq(3).find("a").eq(1).attr("onclick", "sort('down'," + (index - 1) + ");");
                            $("tr[data-id]").eq(index - 1).find("td").eq(3).find("a").eq(0).attr("onclick", "sort('up'," + (index) + ");");
                            $("tr[data-id]").eq(index - 1).find("td").eq(3).find("a").eq(1).attr("onclick", "sort('down'," + (index) + ");");


                            var item = $("tr[data-id]").eq(index).clone();
                            $("tr[data-id]").eq(index).remove();
                            $("tr[data-id]").eq(index - 1).before(item);

                            $("tr[data-id]").eq(index - 1).find("td").eq(3).find("span").html(dataObject.toOrder);
                            $("tr[data-id]").eq(index).find("td").eq(3).find("span").html(dataObject.fromOrder);

                            var tempId = array[index].id;
                            array[index].id = array[index - 1].id;
                            array[index - 1].id = tempId;
                        } else {

                            $("tr[data-id]").eq(index).find("td").eq(3).find("a").eq(0).attr("onclick", "sort('up'," + (index + 1) + ");");
                            $("tr[data-id]").eq(index).find("td").eq(3).find("a").eq(1).attr("onclick", "sort('down'," + (index + 1) + ");");

                            $("tr[data-id]").eq(index + 1).find("td").eq(3).find("a").eq(0).attr("onclick", "sort('up'," + (index) + ");");
                            $("tr[data-id]").eq(index + 1).find("td").eq(3).find("a").eq(1).attr("onclick", "sort('down'," + (index) + ");");


                            var item = $("tr[data-id]").eq(index).clone();
                            $("tr[data-id]").eq(index).remove();
                            $("tr[data-id]").eq(index).after(item);

                            $("tr[data-id]").eq(index + 1).find("td").eq(3).find("span").html(dataObject.toOrder);
                            $("tr[data-id]").eq(index).find("td").eq(3).find("span").html(dataObject.fromOrder);

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
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷手游画报管理 >> 热门页自定义楼层管理</td>
    </tr>
    <tr> 
        <td height="15" valign="top">
            <input type="button" class="default_button" value="返回"
                   onclick="javascipt:window.location.href='/gameclient/clientline/hotfloor/list';"/>
        </td>
        <td height="15" valign="top">
            <form method="post" id="create_item_form"
                  action="/gameclient/clientline/hotfloor/itemcreatepage">
                <input type="hidden" value="${lineId}" name="lineId"/>
                <input type="hidden" value="${lineCode}" name="lineCode"/>
                <input type="hidden" value="${lineName}" name="lineName"/>
                <input type="submit" name="button" class="default_button" value="添加新的item"/>
            </form>
        </td>
    </tr>
</table>

<label><b>${lineName}(${lineCode})</b>的clientline</label>
<table width="100%" height="100%" border="0" cellpadding="2" cellspacing="1">
    <tr>
        <td height="1" valign="top">
            <table width="100%" border="0" cellspacing="1" cellpadding="0" id="contenttable">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="18" class="error_msg_td">
                            <b><fmt:message key="${errorMsg}" bundle="${error}"/></b>
                        </td>
                    </tr>
                </c:if>
                <tr class="list_table_title_tr">
                    <td nowrap align="center">client_line_item表的id</td>
                    <td nowrap align="center">楼层名称</td>
                    <td nowrap align="center">楼层icon</td>
                    <td nowrap align="center">排序</td>
                    <td nowrap align="center">图片1</td>
                    <td nowrap align="center">'图片1'的跳转类型</td>
                    <td nowrap align="center">'图片1'的跳转地址</td>
                    <td nowrap align="center">图片2</td>
                    <td nowrap align="center">'图片2'的跳转类型</td>
                    <td nowrap align="center">'图片2'的跳转地址</td>
                    <td nowrap align="center">图片3</td>
                    <td nowrap align="center">'图片3'的跳转类型</td>
                    <td nowrap align="center">'图片3'的跳转地址</td>
                    <td nowrap align="center">是否有链接--'更多'</td>
                    <td nowrap align="center">'更多'的跳转类型</td>
                    <td nowrap align="center">'更多'的跳转地址</td>
                    <td nowrap align="center">状态</td>
                    <td nowrap align="center">创建日期</td>
                    <td nowrap align="center">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="18" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${not empty list}">
                        <c:forEach items="${list}" var="item" varStatus="st">
                            <tr data-id="" class="<c:choose><c:when
                                test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose> ">
                                <td nowrap align="center">${item.itemId}</td>
                                <td nowrap align="center"></td>
                                <td nowrap align="center"><img height="50" width="50"/></td>
                                <td nowrap align="center"><span>${item.displayOrder}</span> &nbsp;&nbsp; <a
                                        href="javascript:void(0);" onclick="sort('up',${st.index});"><img
                                        src="/static/images/icon/up.gif"></a>
                                    &nbsp;
                                    <a href="javascript:void(0);" onclick="sort('down',${st.index});"><img
                                            src="/static/images/icon/down.gif"></a></td>
                                <td nowrap align="center"><img height="50" width="50"/></td>
                                <td nowrap align="center" class="zxx_text_overflow"></td>
                                <td nowrap align="center" class="zxx_text_overflow"></td>
                                <td nowrap align="center"><img height="50" width="50"/></td>
                                <td nowrap align="center" class="zxx_text_overflow"></td>
                                <td nowrap align="center" class="zxx_text_overflow"></td>
                                <td nowrap align="center"><img height="50" width="50"/></td>
                                <td nowrap align="center" class="zxx_text_overflow"></td>
                                <td nowrap align="center" class="zxx_text_overflow"></td>
                                <td nowrap align="center"></td>
                                <td nowrap align="center"></td>
                                <td nowrap align="center" class="zxx_text_overflow"></td>
                                <td nowrap align="center" <c:choose>
                                    <c:when test="${item.validStatus.code == 'valid'}">
                                        style="color: #008000;"
                                    </c:when>
                                    <c:otherwise>
                                        style="color: #ff0000;"
                                    </c:otherwise>
                                </c:choose>>
                                    <c:choose>
                                        <c:when test="${item.validStatus.code == 'valid'}">
                                            可用
                                        </c:when>
                                        <c:otherwise>
                                            已停用
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td nowrap align="center"><fmt:formatDate value="${item.itemCreateDate}"
                                                                          pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                <td nowrap align="center">
                                    <a href="/gameclient/clientline/hotfloor/itemmodifypage?itemId=${item.itemId}&lineName=${lineName}&lineCode=${lineCode}&lineId=${lineId}">编辑</a>
                                    <c:choose>
                                        <c:when test="${item.validStatus.code == 'valid'}">
                                            <a href='javascript:void(0);' onclick='deleteItemByItemId(${item.itemId},${lineId},"${lineName}","${lineCode}","${st.index}",1);'>停用</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href='javascript:void(0);' onclick='deleteItemByItemId(${item.itemId},${lineId},"${lineName}","${lineCode}","${st.index}",2);'>启用</a>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="18" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="18" height="1" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="18" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="14">
                            <pg:pager url="/gameclient/clientline/hotfloor/itemlist"
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