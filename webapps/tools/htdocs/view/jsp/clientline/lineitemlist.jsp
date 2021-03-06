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
            $.post("/clientline/item/sort/" + sort, {itemid:itemid,lineid:lineid}, function(req) {
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
            window.location.href = "/clientline/item/createpage?lineid=" + lineId + "&itemtype=" + itemType;
        }

        function modify(lineId, itemType) {
            window.location.href = "/clientline/item/createpage?lineid=" + lineId + "&itemtype=" + itemType;
        }
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">

    <tr>
        <td height="1" valign="top">
            <table>
                <tr>
                    <td height="1">
                        <p:privilege name="/clientline/item/createpage">
                            <input type="button" name="create_button" class="default_button" value="添加一条子元素"
                                   onclick="create('${clientLine.lineId}','${clientLine.itemType.code}')"/>
                        </p:privilege>
                    </td>
                </tr>
            </table>
            <form action="/clientline/item/list" method="post">
                <input type="hidden" value="${clientLine.lineId}" name="lineid"/>
                按条件查询：<select name="validstatus">
                <option value="">请选择</option>
                <option value="valid"
                        <c:if test="${validstatus=='valid'}">selected</c:if> >可用
                </option>
                <option value="removed" <c:if test="${validstatus=='removed'}">selected</c:if>>已删除</option>
            </select>
                按标题查询： <input type="text" size="32" name="title"/>
                <input type="submit" value="查询"/>


                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <c:if test="${fn:length(errorMsg)>0}">
                        <tr>
                            <td height="1" colspan="13" class="error_msg_td">
                                <b><fmt:message key="${errorMsg}" bundle="${error}"/></b>
                            </td>
                        </tr>
                    </c:if>
                    <tr class="list_table_title_tr">
                        <td nowrap align="left" width="80">ID</td>
                        <td nowrap align="left">标题</td>
                        <td nowrap align="left">图片</td>
                        <td nowrap align="left">排序</td>
                        <td nowrap align="left" width="80">关联ID</td>

                        <td nowrap align="left">文章来源</td>
                        <%--<td nowrap align="left" width="100">描述</td>--%>
                        <td nowrap align="left">状态</td>
                        <td nowrap align="left">创建信息</td>
                        <td nowrap align="left">地址回调类型</td>
                        <td nowrap align="left">操作</td>
                        <td nowrap align="left" width="80">地址</td>

                    </tr>
                    <tr>
                        <td height="1" colspan="13" class="default_line_td"></td>
                    </tr>
                    <c:choose>
                        <c:when test="${list.size() > 0}">
                            <c:forEach items="${list}" var="dto" varStatus="st">
                                <tr id="clientitem_${dto.id}"
                                    style="<c:choose><c:when test="${not empty dto.bgColor}">background-color:${dto.bgColor}</c:when><c:otherwise>background-color:#ffffff</c:otherwise></c:choose>">
                                    <td nowrap align="left">${dto.id}</td>
                                    <td nowrap align="left">${dto.title}</td>
                                    <td nowrap align="left"><img src="${dto.pic}" height="100" width="100"/></td>
                                    <td nowrap align="center">

                                        <a href="javascript:sort('up','${dto.id}','${dto.lid}','${validstatus}')"><img
                                                src="/static/images/icon/up.gif"></a>
                                        &nbsp;
                                        <a href="javascript:sort('down','${dto.id}','${dto.lid}','${validstatus}')"><img
                                                src="/static/images/icon/down.gif"></a>
                                    </td>
                                    <td nowrap align="left">${dto.did}</td>

                                    <td nowrap align="left"><fmt:message key="client.item.domain.${dto.domain}"
                                                                         bundle="${def}"/></td>
                                        <%--<td align="left" width="250" style="word-wrap: break-word">${dto.desc}</td>--%>
                                    <td nowrap align="left" <c:choose>
                                        <c:when test="${dto.status == 'valid'}">
                                            style="color: #008000;"
                                        </c:when>
                                        <c:otherwise>
                                            style="color: #ff0000;"
                                        </c:otherwise>
                                    </c:choose>><fmt:message key="client.item.status.${dto.status}"
                                                             bundle="${def}"/></td>
                                    <td nowrap align="left"><fmt:formatDate value="${dto.createDate}"
                                                                            pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                    <td nowrap align="left"><fmt:message key="client.item.redirect.${dto.rType}"
                                                                         bundle="${def}"/></td>

                                    <td nowrap align="left">
                                        <a href="/clientline/item/modifypage?itemid=${dto.id}&lineid=${dto.lid}&itemtype=${dto.type}">编辑</a>
                                        <c:choose>
                                            <c:when test="${dto.status == 'valid'}">
                                                <a href="/clientline/item/delete?itemid=${dto.id}&lineid=${dto.lid}">删除</a>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="/clientline/item/recover?itemid=${dto.id}&lineid=${dto.lid}">激活</a>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td nowrap align="left" width="80">${dto.link}</td>


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
                                <pg:pager url="/clientline/item/list"
                                          items="${page.totalRows}" isOffset="true"
                                          maxPageItems="${page.pageSize}"
                                          export="offset, currentPageNumber=pageNumber" scope="request">
                                    <pg:param name="lineid" value="${clientLine.lineId}"/>
                                    <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                    <pg:param name="items" value="${page.totalRows}"/>
                                    <pg:param name="validstatus" value="${validstatus}"/>

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