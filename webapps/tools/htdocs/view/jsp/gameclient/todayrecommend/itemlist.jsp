<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>今日开测列表管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.easyui.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/static/include/js/easyui/themes/default/easyui.css"/>
    <script type="text/javascript" src="/static/include/js/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript">
        Function.prototype.method = function (name, func) {
            if (!this.prototype[name]) {
                this.prototype[name] = func;
            }
            return this;
        };
        String.method('trim', function () {
            return this.replace(/^\s+|\s+$/g, '');
        });

        $(function () {
            <c:if test="${not empty list}">
            var type = [];
            <c:forEach items="${types}" var="item" varStatus="st">
            type[${st.index}] = '<fmt:message key="client.item.redirect.${item.key}" bundle="${def}"/>';
            </c:forEach>

            $.each($("tr[data-id]"), function (index, item) {
                //   console.log($(this).find("td:eq(5)").html().trim());
//                if ($(this).find("td:eq(5)").html().trim() == 'other') {
//                    var theVal = parseInt($(this).find("td:eq(6)").html());
//                    $(this).find("td:eq(6)").html(theVal + "_" + type[theVal]);
//                }
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

            var allFilter = '${allFilter}';
            var dateFilter = '${dateFilter}';

            if (dateFilter == '') {
                var time = new Date();
                dateFilter = time.getFullYear() + '-' + (time.getMonth() + 1) + '-' + time.getDate();
            }


            $("#dateFilter").datebox("setValue", dateFilter);

            $("#allFilter").on("change", function () {
                if ($("#allFilter").prop("checked")) {
                    $("#dateFilter").datebox({disabled: true});
                } else {
                    $("#dateFilter").datebox({disabled: false});
                    $("#dateFilter").datebox("setValue", dateFilter);
                }
            });
            if (allFilter == 'on') {
                $("#allFilter").prop("checked", true);
                $("#allFilter").change();
            }


        });

        function deleteItemByItemId(itemId, gameDbId, gameName, lineId, lineName, lineCode, type) {

            var msg = "";
            if (type == 1) {
                msg = "您确定要停用gameDbId为" + gameDbId + "(" + gameName + ")的clientLineItem吗？\n\n请确认！";
            } else {
                msg = "您确定要启用gameDbId为" + gameDbId + "(" + gameName + ")的clientLineItem吗？\n\n请确认！";
            }

            if (confirm(msg) == true) {
                window.location.href = "/gameclient/clientline/todayrecommend/itemdelete?itemId=" + itemId + "&lineId=" + lineId + "&lineCode=" + lineCode + "&lineName=" + lineName + "&type=" + type;
            }
        }

    </script>

</head>
<body>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷手游画报管理 >> 今日推荐管理</td>
    </tr>
    <tr>
        <td height="15" valign="top">
            <input type="button" class="default_button" value="返回"
                   onclick="javascipt:window.location.href='/gameclient/clientline/todayrecommend/list';"/>
        </td>
        <td height="15" valign="top">
            <form method="post" id="create_item_form"
                  action="/gameclient/clientline/todayrecommend/itemcreatepage">
                <input type="hidden" value="${lineId}" name="lineId"/>
                <input type="hidden" value="${lineCode}" name="lineCode"/>
                <input type="hidden" value="${lineName}" name="lineName"/>
                <input type="submit" name="button" class="default_button" value="添加新的item"/>
            </form>
        </td>
    </tr>
</table>

<label><b>${lineName}(${lineCode})</b>的clientline</label>

<form action="/gameclient/clientline/todayrecommend/itemlist" method="post">
    <table>
        <tr>
            <input type="hidden" value="${lineId}" name="lineId"/>
            <input type="hidden" value="${lineCode}" name="lineCode"/>
            <input type="hidden" value="${lineName}" name="lineName"/>

            <td height="1" class="default_line_td">
                推荐日期:
            </td>
            <td height="1">
                <input type="text" class="easyui-datebox" editable="false" id="dateFilter"
                       name="dateFilter"/>
            </td>

            <td height="1">
                <input type="checkbox" name="allFilter" id="allFilter"/>
            </td>
            <td height="1" class="default_line_td">
                显示全部:
            </td>

            <td width="50px">
                <input type="submit" name="button" value="查询" class="default_button"/>
            </td>
        </tr>
    </table>
</form>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="1" valign="top">
            <table width="100%" border="0" cellspacing="1" cellpadding="0" id="contenttable">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="14" class="error_msg_td">
                            <b><fmt:message key="${errorMsg}" bundle="${error}"/></b>
                        </td>
                    </tr>
                </c:if>
                <tr class="list_table_title_tr">
                    <%--  <td nowrap align="center">client_line_item表的id</td>   --%>
                    <td nowrap align="center">game_db表的id</td>
                    <td nowrap align="center">游戏名称</td>
                    <td nowrap align="center">游戏主图</td>
                    <td nowrap align="center">游戏类型</td>
                    <td nowrap align="center">跳转目标</td>
                    <%--    <td nowrap align="center">跳转类型</td>  --%>
                    <%--    <td nowrap align="center">跳转链接</td>  --%>
                    <td nowrap align="center">点赞人数</td>
                    <td nowrap align="center">星级</td>
                    <td nowrap align="center">推荐语</td>
                    <td nowrap align="center">tag(右上角图标)</td>
                    <td nowrap align="center">状态</td>
                    <td nowrap align="center">推荐日期</td>
                    <td nowrap align="center">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="14" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${not empty list}">
                        <c:forEach items="${list}" var="item" varStatus="st">
                            <tr data-id="${st.index}" class="<c:choose><c:when
                                test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose> ">
                                <!--       <td nowrap align="center">${item.itemId}</td>  -->
                                <td nowrap align="center">${item.gameDbId}</td>
                                <td nowrap align="center">${item.gameName}</td>
                                <td nowrap align="center"><img src="${item.icon}" height="50" width="50"/></td>
                                <td nowrap align="center">${item.gameTypeDesc}</td>
                                <td nowrap align="center">${item.jumpTarget}</td>
                                    <%--     <td nowrap align="center">${item.jt}</td>     --%>
                                    <%--     <td nowrap align="center" class="zxx_text_overflow">${item.ji} </td>    --%>
                                <td nowrap align="center">${item.likeNum} </td>
                                <td nowrap align="center">${item.gameRate} </td>
                                <td nowrap align="center">${item.downloadRecommend}</td>
                                <td nowrap align="center"><img src="${item.tag}"/></td>
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
                                    <a href="/gameclient/clientline/todayrecommend/itemmodifypage?itemId=${item.itemId}&lineName=${lineName}&lineCode=${lineCode}&lineId=${lineId}">编辑</a>
                                    <c:choose>
                                        <c:when test="${item.validStatus.code == 'valid'}">
                                            <a href='javascript:void(0);'
                                               onclick='deleteItemByItemId(${item.itemId},${item.gameDbId},"${item.gameName}",${lineId},"${lineName}","${lineCode}",1);'>停用</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href='javascript:void(0);'
                                               onclick='deleteItemByItemId(${item.itemId},${item.gameDbId},"${item.gameName}",${lineId},"${lineName}","${lineCode}",2);'>启用</a>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="14" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="14" height="1" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="14" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="14">
                            <pg:pager url="/gameclient/clientline/todayrecommend/itemlist"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="lineId" value="${lineId}"/>
                                <pg:param name="allFilter" value="${allFilter}"/>
                                <pg:param name="dateFilter" value="${dateFilter}"/>
                                <pg:param name="lineName" value="${lineName}"/>
                                <pg:param name="lineCode" value="${lineCode}"/>
                                <pg:param name="currentPageNumber" value="${page.curPage}"/>
                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                <pg:param name="items" value="${page.totalRows}"/>
                                <%@ include file="/WEB-INF/jsp/toolspgnoincludejquery.jsp" %>
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