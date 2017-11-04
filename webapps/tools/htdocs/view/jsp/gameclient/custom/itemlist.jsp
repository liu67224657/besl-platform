<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>轮播图自定义管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function (e) {
            $(".zxx_text_overflow").each(function () {
                var maxwidth = 23;
                if ($(this).text().length > maxwidth) {
                    $(this).attr("title", $(this).text());
                    $(this).text($(this).text().substring(0, maxwidth));
                    $(this).html($(this).html() + "...");
                }
            });

            <c:if test="${fn:contains(lineCode, 'gc_topicimgs') && tagIdSearch != null}">
            $("#tagIdSearch").val("${tagIdSearch}");
            </c:if>

        });
        function deleteItemByItemId(itemId, lineId, lineName, lineCode,startRowIdx) {
            var msg = "您确定要删除item_id为" + itemId + "的clientLineItem吗？\n\n请确认！";
            if (confirm(msg) == true) {
                window.location.href = "/gameclient/clientline/custom/itemdelete?itemId=" + itemId + "&lineId=" + lineId+"&lineName="+lineName+"&lineCode="+lineCode+"&startRowIdx="+startRowIdx;
            }
        }
        function sort(sort, itemid, lineid, validstatus) {
            if (validstatus == 'removed') {
                alert("请先恢复文章再进行排序");
                return false;
            }

            var sortType = '';
            <c:if test="${fn:contains(lineCode, 'gc_topicimgs') && tagIdSearch != null}">
            sortType = '${tagIdSearch}';
            </c:if>

            $.post("/gameclient/clientline/custom/sort/" + sort, {
                itemid: itemid,
                lineid: lineid,
                sortType: sortType
            }, function (req) {
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
                            $("#clientitem_" + returnid).before(item);
                        } else {
                            var item = $("#clientitem_" + itemid).clone();
                            $("#clientitem_" + itemid).remove();
                            $("#clientitem_" + returnid).after(item);
                        }
                    }
                }
            });
        }
    </script>
</head>
<body>
<table>
    <tr>
        <td height="22" class="page_navigation_td" colspan="3">>> 运营维护 >> 着迷手游画报管理 >> 轮播图自定义管理</td>
    </tr>
</table>
<table>
    <tr>
        <td height="15" valign="top">
            <input type="button" class="default_button" value="返回"
                   onclick="javascipt:window.location.href='/gameclient/clientline/custom/list';"/>
        </td>
        <td height="15" valign="top">
            <form method="post" id="create_item_form"
                  action="/gameclient/clientline/custom/itemcreatepage">
                <input type="hidden" value="${lineId}" name="lineId"/>
                <input type="hidden" value="${lineCode}" name="lineCode"/>
                <input type="hidden" value="${lineName}" name="lineName"/>
                <input type="submit" name="button" class="default_button" value="添加新的item"/>
            </form>
        </td>
        <c:if test="${fn:contains(lineCode, 'gc_topicimgs')}">
            <td>
                <form action="/gameclient/clientline/custom/itemlist" method="post">
                    <table>
                        <tr>
                            <input type="hidden" name="lineId" value="${lineId}"/>
                            <input type="hidden" name="lineName" value="${lineName}"/>
                            <input type="hidden" name="lineCode" value="${lineCode}"/>
                            <td height="1" class="default_line_td">
                                请选择标签的名字:
                            </td>
                            <td height="1" class="edit_table_defaulttitle_td" width="50px">
                                <select name="tagIdSearch" id="tagIdSearch">
                                    <option value="-100">全部</option>

                                    <c:forEach items="${tagMap}" var="item">
                                        <option value='${item.key}'>${item.value}</option>
                                    </c:forEach>

                                </select>
                            </td>
                            <td width="50px">
                                <input type="submit" name="button" value="查询"/>
                            </td>
                        </tr>
                    </table>
                </form>
            </td>
        </c:if>
    </tr>
</table>

<label><b>${lineName}</b></label>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="1" valign="top">

            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="15" class="error_msg_td">
                            <b><fmt:message key="${errorMsg}" bundle="${error}"/></b>
                        </td>
                    </tr>
                </c:if>
                <tr class="list_table_title_tr">
                    <td width="80">client_line_item表的id</td>
                    <td>名字</td>
                    <td>描述</td>
                    <c:if test="${fn:contains(lineCode, 'gc_topicimgs')||fn:contains(lineCode, 'gc_hotgameimgs')}">
                        <td>图片</td>
                    </c:if>
                    <c:if test="${fn:contains(lineCode, 'gc_topicimgs')}">
                        <td>标签名称</td>
                        <td>左上角小图</td>
                    </c:if>
                    <c:if test="${fn:contains(lineCode, 'gc_hotgamelinks')}">
                        <td> 标签颜色</td>
                        <td> iconurl</td>
                        <td>热门下面标签的数量</td>
                        <td>热门下面标签的数量的外圈颜色</td>
                    </c:if>
                    <td nowrap align="left">排序</td>
                    <td>跳转地址</td>
                    <td>重定向类型</td>
                    <td>上市时间</td>
                    <td nowrap align="center" width="">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="15" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="item" varStatus="st">
                            <tr id="clientitem_${item.itemId}"
                                class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td>${item.itemId}</td>
                                <td>${item.title}</td>
                                <td>${item.desc}</td>
                                <c:if test="${fn:contains(lineCode, 'gc_topicimgs') || fn:contains(lineCode, 'gc_hotgameimgs') }">
                                    <td><img src="${item.picUrl}" height="50" width="50"/></td>
                                </c:if>
                                <c:if test="${fn:contains(lineCode, 'gc_topicimgs')}">
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty item.contentid && not empty tagMap}">
                                                ${tagMap[item.contentid]}
                                            </c:when>
                                            <c:otherwise>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><img src="${item.category}" height="50" width="50"/></td>
                                </c:if>
                                <c:if test="${fn:contains(lineCode, 'gc_hotgamelinks')}">
                                    <td>${item.categoryColor}</td>
                                    <td><img src="${item.category}" height="50" width="50"/></td>
                                    <td>${item.rate}</td>
                                    <td>${item.author}</td>
                                </c:if>
                                <td nowrap align="center">
                                    <a href="javascript:sort('up','${item.itemId}','${item.lineId}','${item.validStatus.code}')"><img
                                            src="/static/images/icon/up.gif"></a>
                                    &nbsp;
                                    <a href="javascript:sort('down','${item.itemId}','${item.lineId}','${item.validStatus.code}')"><img
                                            src="/static/images/icon/down.gif"></a>
                                </td>
                                <td class="zxx_text_overflow">${item.url}</td>
                                <td>${item.redirectType.code}</td>
                                <td>${item.itemCreateDate}</td>
                                <td nowrap>
                                    <a href="/gameclient/clientline/custom/itemmodifypage?itemId=${item.itemId}&lineId=${lineId}&lineName=${lineName}&lineCode=${lineCode}&startRowIdx=${page.startRowIdx}">编辑</a>
                                    <a href="/gameclient/clientline/custom/itemdelete?itemId=${item.itemId}&lineId=${lineId}&lineName=${lineName}&lineCode=${lineCode}&startRowIdx=${page.startRowIdx}">删除</a>

                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="15" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="15" height="1" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="15" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="15">
                            <pg:pager url="/gameclient/clientline/custom/itemlist"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="lineId" value="${lineId}"/>
                                <pg:param name="lineName" value="${lineName}"/>
                                <pg:param name="lineCode" value="${lineCode}"/>
                                <c:if test="${fn:contains(lineCode, 'gc_topicimgs')}">
                                    <pg:param name="tagIdSearch" value="${tagIdSearch}"/>
                                </c:if>
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