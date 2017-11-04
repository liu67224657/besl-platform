<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>clientline管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.min.js"></script>
    <script>
        function sort(sort, relationid, gamedbid, validstatus) {
            if (validstatus != 1) {
                alert("请先恢复文章再进行排序");
                return false;
            }
            $.post("/gameclient/gametab/sort/" + sort, {relationid: relationid, gamedbid: gamedbid}, function (req) {
                var resMsg = eval('(' + req + ')');
                if (resMsg.rs == '0') {
                    return false;
                } else {
                    var result = resMsg.result;

                    if (result == null) {

                    } else {
                        var itemid = result.srcid;
                        var sort = result.sort;
                        var returnid = result.destid;
                        if (sort == 'up') {
                            var item = $("#relation_tr_" + itemid).clone();
                            $("#relation_tr_" + itemid).remove();
                            $("#relation_tr_" + returnid).before(item);
                        } else {
                            var item = $("#relation_tr_" + itemid).clone();
                            $("#relation_tr_" + itemid).remove();
                            $("#relation_tr_" + returnid).after(item);
                        }
                    }
                }
            });
        }
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 运营维护 >> 新版手游画报</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">《${game.gameName}》--TAB页管理</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="8" class="default_line_td"></td>
                </tr>
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="8" class="error_msg_td">
                            <b><fmt:message key="${errorMsg}" bundle="${error}"/></b>
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td height="1" colspan="7" class="">
                        <form action="/gameclient/gametab/createpage" method="post">
                            <input type="hidden" name="gamedbid" value="${game.gameDbId}"/>
                            <input type="submit" value="创建新标签">
                        </form>
                    </td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="left" width="80">ID</td>
                    <td nowrap align="left">名称</td>
                    <td nowrap align="left">地址</td>
                    <td nowrap align="left">类型</td>
                    <td nowrap align="left">状态</td>
                    <td nowrap align="left">创建人信息</td>
                    <td nowrap align="left">排序</td>
                    <td nowrap align="left">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="8" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${relationlist.size() > 0}">
                        <c:forEach items="${relationlist}" var="relation" varStatus="st">
                            <tr id="relation_tr_${relation.relationId}"
                                class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap align="left">${relation.relationId}</td>
                                <td nowrap align="left">
                                    <c:choose>
                                        <c:when test="${relation.type.code==1}">
                                            <a href="/gameclient/gametab/newslist?lineid=${relation.uri}&gamedbid=${game.gameDbId}">${relation.title}</a>
                                        </c:when>
                                        <c:otherwise>
                                            ${relation.title}
                                        </c:otherwise>
                                    </c:choose>

                                </td>
                                <td nowrap align="left">${relation.uri}</td>
                                <td nowrap align="left">
                                    <fmt:message key="gamedb.realtion.type.${relation.type.code}" bundle="${def}"/></td>
                                <td nowrap align="left" <c:choose>
                                    <c:when test="${relation.validStatus.code == 1}">
                                        style="color: #008000;"
                                    </c:when>
                                    <c:otherwise>
                                        style="color: #ff0000;"
                                    </c:otherwise>
                                </c:choose>><fmt:message key="intvalid.stauts.${relation.validStatus.code}"
                                                         bundle="${def}"/></td>
                                <td nowrap align="left">
                                    <fmt:formatDate value="${relation.modifyTime}"
                                                    pattern="yyyy-MM-dd HH:mm:ss"/>/${relation.modifyUserid}</td>
                                <td nowrap align="center">
                                    <a href="javascript:sort('up','${relation.relationId}','${relation.gamedbId}','${relation.validStatus.code}')"><img
                                            src="/static/images/icon/up.gif"></a>
                                    &nbsp;
                                    <a href="javascript:sort('down','${relation.relationId}','${relation.gamedbId}','${relation.validStatus.code}')"><img
                                            src="/static/images/icon/down.gif"></a>
                                </td>
                                <td nowrap align="left">
                                    <a href="/gameclient/gametab/modifypage?gamedbid=${relation.gamedbId}&relationid=${relation.relationId}">编辑</a>
                                    <c:choose>
                                        <c:when test="${relation.validStatus.code == 1}">
                                            <a href="/gameclient/gametab/delete?gamedbid=${relation.gamedbId}&relationid=${relation.relationId}">删除</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="/gameclient/gametab/recover?gamedbid=${relation.gamedbId}&relationid=${relation.relationId}">激活</a>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="8" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="8" height="1" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="8" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="8">
                            <pg:pager url="/gameclient/clientline/list"
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