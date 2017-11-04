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
        $().ready(function() {
            $('#form_submit').bind('submit', function() {
                var result = Number(0);
                $("input[name = box]:checkbox").each(function () {
                    if ($(this).is(":checked")) {
                        result += Number($(this).attr("value"));
                    }
                });
                $('#input_hidden_goods_category').val(result);
            });
        });
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
    <td height="22" class="page_navigation_td">>> 运营管理 >> 条目管理 >> 活动管理</td>
</tr>
<tr>
<td height="100%" valign="top"><br>
<table border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td class="list_table_header_td">活动列表</td>
    </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <c:if test="${fn:length(errorMsg)>0}">
        <tr>
            <td height="1" colspan="13" class="error_msg_td">
                <fmt:message key="${errorMsg}" bundle="${error}"/>
            </td>
        </tr>
    </c:if>
    <tr>
        <td height="1" class="default_line_td"></td>
    </tr>
</table>
<table>
    <tr>
        <td>
            <form method="post" action="/activity/goods/createpage">
                <table>
                    <tr>
                        <td>
                            <p:privilege name="/activity/goods/createpage">
                            <input type="submit" name="button" class="default_button" value="添加活动"/>
                            </p:privilege>
                        <td>
                    <tr>
                </table>
            </form>
        </td>
    </tr>
</table>
<table>
    <tr>
        <td>
            <form method="post" action="/activity/goods/list" id="form_submit">
                <table>
                    <%--<tr>--%>
                    <%--<td>--%>
                    <%--<input type="checkbox" name="box" value="1"--%>
                    <%--<c:if test="${goodsCategory.hasClassic()}">checked="checked"</c:if>/>精品推荐--%>
                    <%--</td>--%>
                    <%--<td>--%>
                    <%--<input type="checkbox" name="box" value="2"--%>
                    <%--<c:if test="${goodsCategory.hasHot()}">checked="checked"</c:if>/>热卖实物--%>
                    <%--</td>--%>
                    <%--<td>--%>
                    <%--<input type="checkbox" name="box" value="4"--%>
                    <%--<c:if test="${goodsCategory.hasNew()}">checked="checked"</c:if>/>最新上架--%>
                    <%--</td>--%>
                    <%--<td>--%>
                    <%--<input type="checkbox" name="box" value="8"--%>
                    <%--<c:if test="${goodsCategory.hasSpecial()}">checked="checked"</c:if>/>特价商品--%>
                    <%--</td>--%>
                    <%--<td>--%>
                    <%--<input type="hidden" name="goodscategory" id="input_hidden_goods_category"/>--%>
                    <%--</td>--%>

                    <%--<td>--%>
                    <%--<p:privilege name="/activity/goods/list">--%>
                    <%--<input type="submit" name="button" class="default_button" value="查询"/>--%>
                    <%--</p:privilege>--%>
                    <%--<td>--%>
                    <%--</tr>--%>
                    <tr>
                        <td colspan="2">
                            出口查询：<select name="goodsactiontype">
                            <option value="">请选择</option>
                            <c:forEach items="${shopTypes}" var="item">
                                <option value="${item.code}"
                                        <c:if test="${goodsActionType==item.code}">selected</c:if>>${item.name}</option>
                            </c:forEach>
                        </select>
                        </td>
                        <td>
                            <p:privilege name="/activity/goods/list">
                            <input type="submit" name="button" class="default_button" value="查询"/>
                            </p:privilege>
                        <td>
                    </tr>
                </table>
            </form>
        </td>
    </tr>
</table> <table>
    <tr>
        <td>
            <form method="post" action="/activity/goods/list" id="form_submit_c">
                <table>
                    <tr>
                        <td>
                            活动名称(模糊)： <input type="text" name="activityname" id=""/>
                        </td>
                        <td>
                            <p:privilege name="/activity/exchange/list">
                                <input type="submit" name="button" class="default_button" value="查询"/>
                            </p:privilege>
                        <td>
                    <tr>
                </table>
            </form>
        </td>
    </tr>
</table>

<form action="/activity/goods/list" method="post">
    <table width="100%" border="0" cellspacing="1" cellpadding="0">
        <tr>
            <td height="1" colspan="13" class="default_line_td"></td>
        </tr>
        <c:if test="${fn:length(errorMsg)>0}">
            <tr>
                <td height="1" colspan="13" class="error_msg_td">
                    <b><fmt:message key="${errorMsg}" bundle="${error}"/></b>
                </td>
            </tr>
        </c:if>
        <tr class="list_table_title_tr">
            <td nowrap align="left" width="80">活动ID</td>
            <td nowrap align="left" width="80">活动标题</td>
            <td nowrap align="left" width="120">活动图片</td>
            <td nowrap align="left" width="120">开始时间</td>
            <td nowrap align="left" width="120">结束时间</td>
            <td nowrap align="left" width="100">游戏名称</td>
            <td nowrap align="left" width="80">游戏ICON图</td>
            <%--<td nowrap align="left" width="130">商品过期时间</td>--%>
            <td nowrap align="left" width="100">开发商</td>
            <td nowrap align="left" width="150">专区地址</td>
            <td nowrap align="left" width="150">跳转链接</td>
            <td nowrap align="left" width="150">IOS下载地址</td>
            <td nowrap align="left" width="150">安卓下载地址</td>
            <c:if test="${goodsCategoryValue==null || goodsCategoryValue==0}">
                <td nowrap align="left">排序</td>
            </c:if>
            <td nowrap align="left">状态</td>
            <td nowrap align="left">创建人信息</td>
            <td nowrap align="left">操作</td>
        </tr>
        <tr>
            <td height="1" colspan="13" class="default_line_td"></td>
        </tr>
        <c:choose>
            <c:when test="${list.size() > 0}">
                <c:forEach items="${list}" var="activity" varStatus="st">
                    <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                        <td nowrap align="left">${activity.activityId}</td>
                        <td nowrap align="left">${activity.activitySubject}</td>
                        <td nowrap align="left"><img src="${activity.activityPicUrl}" height="30"
                                                     width="100"/></td>
                        <td nowrap align="left">${activity.startTime}</td>
                        <td nowrap align="left">${activity.endTime}</td>
                        <td nowrap align="left">${activity.gameName}</td>
                        <td nowrap align="left"><img src="${activity.gameIconUrl}" height="30" width="100"/>
                        </td>
                            <%--<td nowrap align="left" width="130">商品过期时间</td>--%>
                        <td nowrap align="left">${activity.gameProduct}</td>
                        <td nowrap align="left">${activity.gameUrl}</td>
                        <td nowrap align="left">${URL_WWW}/appclick/${activity.qrUrl}</td>
                        <td nowrap align="left">${activity.iosDownloadUrl}</td>
                        <td nowrap align="left">${activity.androidDownloadUrl}</td>
                        <c:if test="${goodsCategoryValue==null || goodsCategoryValue==0}">
                            <td nowrap>

                                <a href="/activity/goods/sort/up?activityid=${activity.activityId}&goodscategory=${goodsCategoryValue}">
                                    <img src="/static/images/icon/up.gif"></a>
                                <a href="/activity/goods/sort/down?activityid=${activity.activityId}&goodscategory=${goodsCategoryValue}">
                                    <img src="/static/images/icon/down.gif"></a>

                            </td>
                        </c:if>
                        <td nowrap align="left"
                            <c:if test="${activity.removeStatus.code=='n'}">style="color:red"</c:if> >
                            <fmt:message key="activity.exchange.type.${activity.removeStatus.code}"
                                         bundle="${def}"/></td>
                        <td nowrap align="left">${activity.createIp}</td>
                        <td nowrap align="left"><a
                                href="/activity/goods/modifypage?activityId=${activity.activityId}">修改</a>
                            <c:choose>
                                <c:when test="${activity.removeStatus.code=='n'}">
                                    <a href="/activity/goods/recover?activityId=${activity.activityId}">激活</a>
                                </c:when>
                                <c:otherwise>
                                    <a href="/activity/goods/delete?activityId=${activity.activityId}">删除</a>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
                <tr>
                    <td height="1" colspan="13" class="default_line_td"></td>
                </tr>
            </c:when>
            <c:otherwise>
                <tr>
                    <td colspan="13" class="error_msg_td">暂无数据!</td>
                </tr>
            </c:otherwise>
        </c:choose>
        <tr>
            <td colspan="13" height="1" class="default_line_td"></td>
        </tr>
        <c:if test="${page.maxPage > 1}">
            <tr class="list_table_opp_tr">
                <td colspan="13">
                    <pg:pager url="/activity/goods/list"
                              items="${page.totalRows}" isOffset="true"
                              maxPageItems="${page.pageSize}"
                              export="offset, currentPageNumber=pageNumber" scope="request">
                        <pg:param name="goodscategory" value="${goodsCategoryValue}"/>
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