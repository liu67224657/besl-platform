<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>今日推荐管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.min.js"></script>
    <script>

        $(function () {

            <c:if test="${not empty list}">
            var type = {};
            <c:forEach items="${types}" var="item" varStatus="st">
            type['type'+${item.key}] = '${item.key}__<fmt:message key="client.item.redirect.${item.key}" bundle="${def}"/>';
            </c:forEach>
            var desc = [];
            <c:forEach items="${list}" var="item" varStatus="st">
            desc[${st.index}] = '${item.line_desc}';
            </c:forEach>
            $.each($("tr[data-id]"), function (index, item) {

                try {
                    var jsonObj = jQuery.parseJSON(desc[index]);
                    $(this).find("td:eq(7)").find("img").attr("src", jsonObj.icon);
                    if (jsonObj.more== '1') {
                        $(this).find("td:eq(8)").html('有');
                        $(this).find("td:eq(9)").html(type['type'+jsonObj.jt]);
                        $(this).find("td:eq(10)").html(jsonObj.ji);
                    } else {
                        $(this).find("td:eq(8)").html('没有');
                        $(this).find("td:eq(9)").html("----");
                        $(this).find("td:eq(10)").html("----");
                    }
                } catch (e) {
                    console.log("parse Error" + desc);
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
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷手游画报管理 >> 今日推荐管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">clientline管理</td>
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
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="14" class="error_msg_td">
                            <b><fmt:message key="${errorMsg}" bundle="${error}"/></b>
                        </td>
                    </tr>
                </c:if>
                <tr class="list_table_title_tr">
                    <td nowrap align="left" width="80">ID</td>
                    <td nowrap align="left">名称</td>
                    <td nowrap align="left">编码</td>
                    <td nowrap align="left">类型</td>
                    <td nowrap align="left">平台</td>
                    <td nowrap align="left">状态</td>
                    <td nowrap align="left">创建人信息</td>
                    <td>楼层icon</td>
                    <td nowrap align="center">是否有链接--'更多'</td>
                    <td>链接的跳转类型</td>
                    <td>链接的跳转地址</td>
                    <td nowrap align="left">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="14" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="line" varStatus="st">
                            <tr data-id="${st.index}"
                                class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap align="left">${line.lineId}</td>
                                <td nowrap align="left"><a
                                        href="/gameclient/clientline/todayrecommend/itemlist?lineId=${line.lineId}&lineName=${line.lineName}&lineCode=${line.code}">${line.lineName}</a>
                                </td>
                                <td nowrap align="left">${line.code}</td>
                                <td nowrap align="left"><fmt:message key="client.item.type.${line.itemType.code}"
                                                                     bundle="${def}"/></td>
                                <td nowrap align="left"><c:choose><c:when test="${line.platform==0}">ios</c:when><c:otherwise>android</c:otherwise></c:choose></td>
                                <td nowrap align="left" <c:choose>
                                    <c:when test="${line.validStatus.code == 'valid'}">
                                        style="color: #008000;"
                                    </c:when>
                                    <c:otherwise>
                                        style="color: #ff0000;"
                                    </c:otherwise>
                                </c:choose>><fmt:message key="client.line.status.${line.validStatus.code}"
                                                         bundle="${def}"/></td>
                                <td nowrap align="left"><fmt:formatDate value="${line.createDate}"
                                                                        pattern="yyyy-MM-dd HH:mm:ss"/>/${line.createUserid}</td>
                                <td><img height="50" width="50"/></td>
                                <td nowrap align="center"></td>
                                <td></td>
                                <td class="zxx_text_overflow"></td>
                                <td nowrap align="left">
                                    <a href="/gameclient/clientline/todayrecommend/modifypage?lineId=${line.lineId}&lineCode=${line.code}">编辑</a>
                                        <%--
                                        <c:choose>
                                            <c:when test="${line.validStatus.code == 'valid'}">
                                                <a href="/gameclient/clientline/delete?lineId=${line.lineId}">删除</a>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="/wechat/line/recover?lineId=${line.lineId}">激活</a>
                                            </c:otherwise>
                                        </c:choose>
                                        --%>
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
                            <pg:pager url="/gameclient/clientline/todayrecommend/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
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