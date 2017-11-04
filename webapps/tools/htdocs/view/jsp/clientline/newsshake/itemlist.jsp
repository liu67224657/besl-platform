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
            $.post("/wechat/line/item/sort/" + sort, {itemid: itemid, lineid: lineid}, function (req) {
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
        $(document).ready(function () {

            $('#form_submit').bind('submit', function () {


            });

            $('#batch_submit').bind('submit', function () {
                var result = new Array();
                $("[name = box]:checkbox").each(function () {
                    if ($(this).is(":checked")) {
                        result.push($(this).attr("value"));
                    }
                });
                if (result.length == 0) {
                    alert("至少要选择一个游戏");
                    return false;
                }
                $('#input_hidden_itemids').val(result.join('|'));
            });

            $("#checkall").bind("click", function () {
                if ($('#checkall').is(":checked")) {
                    $("[name = box]:checkbox").attr("checked", true);
                } else {
                    $("[name = box]:checkbox").attr("checked", false);
                }
            });

            $("#checkinverse").bind("click", function () {
                $("[name = box]:checkbox").each(function () {
                    if($(this).is(":checked")){
                        $(this).attr("checked", false);
                    }else{
                        $(this).attr("checked", true);
                    }

                });
            });

        });
        function create(lineId, itemType) {
            window.location.href = "/shake/line/item/createpage?lineid=" + lineId + "&itemtype=" + itemType;
        }

        function modify(lineId, itemType) {
            window.location.href = "/shake/line/item/createpage?lineid=" + lineId + "&itemtype=" + itemType;
        }
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">运营维护 >> 着迷手游画报管理 >> <a href="/joymeapp/newsclient/shake/list">手游画报摇一摇管理</a>
        </td>
    </tr>
    <tr>
        <td height="1" valign="top">
            <table>
                <tr>
                    <td height="1">
                        <form action="/shake/line/item/list" method="post">
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
                        <form action="/shake/line/item/createpage" method="post">
                            <table>
                                <tr>
                                    <td>
                                        <input type="hidden" name="lineid" value="${clientLine.lineId}"/>
                                        <input type="hidden" name="itemtype" value="${clientLine.itemType.code}"/>
                                        <p:privilege name="/shake/line/item/createpage">
                                            <input type="submit" name="create_button" class="default_button"
                                                   value="添加子元素"/>
                                        </p:privilege>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </td>
                    <td height="1">
                        <form action="/shake/line/item/create" method="post">
                            <table>
                                <tr>
                                    <td>
                                        <input type="hidden" name="updatecache" value="true"/>
                                        <input type="hidden" name="linecode" value="${clientLine.code}"/>
                                        <input type="hidden" name="lineid" value="${clientLine.lineId}"/>
                                        <input type="hidden" name="itemtype" value="${clientLine.itemType.code}"/>
                                        <p:privilege name="/shake/line/item/create">
                                            <input type="submit" name="create_button" class="default_button"
                                                   value="更新权重库缓存"/>
                                        </p:privilege>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <form action="/shake/line/item/modify" method="post" id="batch_submit">
                    <tr>
                        <td>
                            <input type="checkbox" name="all" id="checkall"/>全选
                        </td>
                        <td>
                            <input type="checkbox" name="inverse" id="checkinverse"/>反选
                        </td>
                        <td>
                            修改权重：<input type="text" name="shakeweight"/>
                            <input name="button" type="submit" class="default_button" value="批量修改"/>
                            <input type="hidden" name="itemids" id="input_hidden_itemids" value=""/>
                            <input type="hidden" name="lineid" value="${clientLine.lineId}"/>
                            <input type="hidden" name="itemtype" value="${clientLine.itemType.code}"/>
                        </td>
                        <c:if test="${page.maxPage > 1}">
                            <tr class="list_table_opp_tr">
                                <td colspan="13">
                                    <pg:pager url="/shake/line/item/list"
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
                    </tr>
                </form>
            </table>
            <form action="/shake/line/item/list" method="post">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <c:if test="${fn:length(errorMsg)>0}">
                        <tr>
                            <td height="1" colspan="13" class="error_msg_td">
                                <b><fmt:message key="${errorMsg}" bundle="${error}"/></b>
                            </td>
                        </tr>
                    </c:if>
                    <tr class="list_table_title_tr">
                        <td nowrap align="left" width="20"></td>
                        <td nowrap align="left" width="80">ID</td>
                        <td nowrap align="left">游戏资料库ID</td>
                        <td nowrap align="left">标题</td>
                        <%--<td nowrap align="left">图片</td>--%>
                        <td nowrap align="left">权重</td>
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
                    <td nowrap align="left"><input type="checkbox" name="box" value="${item.itemId}"/></td>
                    <td nowrap align="left">${item.itemId}</td>
                    <td nowrap align="left">${item.directId}</td>
                    <td nowrap align="left">${item.title}</td>
                    <td nowrap align="left">${item.contentid}</td>
                    <%--<td nowrap align="left"><img src="${item.picUrl}" height="125" width="300"/></td>--%>
                    <%--<td nowrap align="left">--%>
                    <%--<a href="/wechat/line/item/sort/up?lineid=${lineId}&itemid=${item.itemId}"><img--%>
                    <%--src="/static/images/icon/up.gif"></a>--%>
                    <%--<a href="/wechat/line/item/sort/down?lineid=${lineId}&itemid=${item.itemId}"><img--%>
                    <%--src="/static/images/icon/down.gif"></a>--%>
                    <%--</td>td--%>
                    <td nowrap align="left"
                    <c:choose><c:when test="${item.validStatus.code == 'valid'}">style="color:
                        #008000;"</c:when><c:otherwise>style="color: #ff0000;"</c:otherwise></c:choose>>
                    <fmt:message key="client.item.status.${item.validStatus.code}" bundle="${def}"/>
                    </td>
                    <td nowrap align="left">
                        <a href="/shake/line/item/modifypage?itemid=${item.itemId}&lineid=${item.lineId}&itemtype=${item.itemType.code}">编辑</a>
                        <c:choose>
                            <c:when test="${item.validStatus.code == 'valid'}">
                                <a href="/shake/line/item/delete?itemid=${item.itemId}&lineid=${item.lineId}">删除</a>
                            </c:when>
                            <c:otherwise>
                                <a href="/shake/line/item/recover?itemid=${item.itemId}&lineid=${item.lineId}">恢复</a>
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
                                <pg:pager url="/shake/line/item/list"
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