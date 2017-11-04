<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>热门页游戏子分类管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.min.js"></script>
    <script>
        $(function () {
            $("#backButton").on("click", function () {
                window.location.href = "/gameclient/clientline/gamecategory/list";
            });
        });
        function toDelete(subLineId, subLineName, type, lineName, lineCode, platform) {
            var msg = "";
            if (type == 1) {
                msg = "您确定要停用lineName为<<" + subLineName + ">>(lineId=" + subLineId + "),的clientLine吗？\n\n请确认！";
            } else {
                msg = "您确定要启用lineName为<<" + subLineName + ">>(lineId=" + subLineId + "),的clientLine吗？\n\n请确认！";
            }
            if (confirm(msg) == true) {
                window.location.href = "/gameclient/clientline/gamecategory/subdelete?subLineId=" + subLineId + "&type=" + type + "&lineName=" + lineName + "&lineCode=" + lineCode + "&platform=" + platform;
            }
        }

        var sort = (function () {
            var array = [];
            <c:if test="${not empty list}">
            <c:forEach items="${list}" var="item" varStatus="st">
            array[${st.index}] = {};
            array[${st.index}].id = ${item.lineId};
            array[${st.index}].order = ${item.display_order};
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
                            $("tr[data-id]").eq(index).find("td").eq(3).find("a").eq(0).attr("onclick", "sort('up'," + (index - 1) + ",'clientline');");
                            $("tr[data-id]").eq(index).find("td").eq(3).find("a").eq(1).attr("onclick", "sort('down'," + (index - 1) + ",'clientline');");
                            $("tr[data-id]").eq(index - 1).find("td").eq(3).find("a").eq(0).attr("onclick", "sort('up'," + (index) + ",'clientline');");
                            $("tr[data-id]").eq(index - 1).find("td").eq(3).find("a").eq(1).attr("onclick", "sort('down'," + (index) + ",'clientline');");


                            var item = $("tr[data-id]").eq(index).clone();
                            $("tr[data-id]").eq(index).remove();
                            $("tr[data-id]").eq(index - 1).before(item);

                            $("tr[data-id]").eq(index - 1).find("td").eq(3).find("span").html(dataObject.toOrder);
                            $("tr[data-id]").eq(index).find("td").eq(3).find("span").html(dataObject.fromOrder);

                            var tempId = array[index].id;
                            array[index].id = array[index - 1].id;
                            array[index - 1].id = tempId;
                        } else {

                            $("tr[data-id]").eq(index).find("td").eq(3).find("a").eq(0).attr("onclick", "sort('up'," + (index + 1) + ",'clientline');");
                            $("tr[data-id]").eq(index).find("td").eq(3).find("a").eq(1).attr("onclick", "sort('down'," + (index + 1) + ",'clientline');");

                            $("tr[data-id]").eq(index + 1).find("td").eq(3).find("a").eq(0).attr("onclick", "sort('up'," + (index) + ",'clientline');");
                            $("tr[data-id]").eq(index + 1).find("td").eq(3).find("a").eq(1).attr("onclick", "sort('down'," + (index) + ",'clientline');");


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
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷手游画报管理 >> 热门页游戏分类管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td><label>所属平台: <b><c:choose><c:when
                            test="${platform==0}">ios</c:when><c:otherwise>android</c:otherwise></c:choose></b>
                        &nbsp;父类别名:<b>${lineName}</b>(${lineCode}) &nbsp;&nbsp;</label></td>
                    <td height="15" valign="top">
                        <form method="post" action="/gameclient/clientline/gamecategory/subcreatepage">
                            <input type="hidden" value="${lineCode}" name="lineCode"/>
                            <input type="hidden" value="${lineName}" name="lineName"/>
                            <input type="hidden" value="${platform}" name="platform"/>
                            <input type="submit" name="button" class="default_button" value="添加新的子类别"/>
                        </form>
                    </td>
                    <td height="15" valign="top">  &nbsp;&nbsp; <input type="submit" id="backButton"
                                                                       class="default_button" value="返回"/></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="14" class="error_msg_td">
                            <fmt:message key="${errorMsg}" bundle="${error}"/>
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>

            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="14" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="left" width="80">ID</td>
                    <td nowrap align="left">名称</td>
                    <td nowrap align="left">编码</td>
                    <td nowrap align="left">排序</td>
                    <td nowrap align="left">类型</td>
                    <td nowrap align="left">所属平台</td>
                    <td nowrap align="left">line_intro</td>
                    <td nowrap align="left">状态</td>
                    <td nowrap align="left">创建人信息</td>
                    <td nowrap align="left">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="14" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="line" varStatus="st">
                            <tr data-id=""
                                class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap align="left">${line.lineId}</td>
                                <td nowrap align="left"><a
                                        href="/gameclient/clientline/gamecategory/itemlist?subLineId=${line.lineId}&subLineName=${line.lineName}&subLineCode=${line.code}&lineCode=${lineCode}&lineName=${lineName}&platform=${platform}">${line.lineName}</a>
                                </td>
                                <td nowrap align="left">${line.code}</td>
                                <td nowrap align="left"><span>${line.display_order}</span> &nbsp;&nbsp; <a
                                        href="javascript:void(0);" onclick="sort('up',${st.index},'clientline');"><img
                                        src="/static/images/icon/up.gif"></a>
                                    &nbsp;
                                    <a href="javascript:void(0);" onclick="sort('down',${st.index},'clientline');"><img
                                            src="/static/images/icon/down.gif"></a></td>
                                <td nowrap align="left"><fmt:message key="client.item.type.${line.itemType.code}"
                                                                     bundle="${def}"/></td>
                                <td nowrap align="left"><c:choose>
                                    <c:when test="${line.platform == 0}">
                                        ios
                                    </c:when>
                                    <c:otherwise>
                                        android
                                    </c:otherwise>
                                </c:choose></td>
                                <td nowrap align="left">${line.line_intro}</td>
                                <td nowrap align="left" <c:choose>
                                    <c:when test="${line.validStatus.code == 'valid'}">
                                        style="color: #008000;"
                                    </c:when>
                                    <c:otherwise>
                                        style="color: #ff0000;"
                                    </c:otherwise>
                                </c:choose>><c:choose>
                                    <c:when test="${line.validStatus.code == 'valid'}">
                                        可用
                                    </c:when>
                                    <c:otherwise>
                                        已停用
                                    </c:otherwise>
                                </c:choose></td>
                                <td nowrap align="left"><fmt:formatDate value="${line.createDate}"
                                                                        pattern="yyyy-MM-dd HH:mm:ss"/>/${line.createUserid}</td>
                                <td nowrap align="left">
                                    <a href="/gameclient/clientline/gamecategory/submodifypage?subLineId=${line.lineId}&subLineCode=${line.code}&subLineName=${line.lineName}&lineName=${lineName}&platform=${platform}&lineCode=${lineCode}">编辑</a>
                                    <c:choose>
                                        <c:when test="${line.validStatus.code == 'valid'}">
                                            <a href="javascript:void(0);"
                                               onclick="toDelete(${line.lineId},'${line.lineName}',1,'${lineName}','${lineCode}',${platform});">停用</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="javascript:void(0);"
                                               onclick="toDelete(${line.lineId},'${line.lineName}',2,'${lineName}','${lineCode}',${platform});">启用</a>
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
                            <pg:pager url="/gameclient/clientline/gamecategory/sublist"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="lineName" value="${lineName}"/>
                                <pg:param name="lineCode" value="${lineCode}"/>
                                <pg:param name="platform" value="${platform}"/>
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