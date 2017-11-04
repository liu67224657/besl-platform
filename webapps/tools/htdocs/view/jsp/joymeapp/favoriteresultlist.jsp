<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>着迷APP版本信息</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script>
        $(document).ready(function() {

        })

    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 条目管理 >> 猜你喜欢>>${appResource.appinfo.appName}</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>

            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="10" class="error_msg_td">
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
                        <form action="/joymeapp/favorite/list" method="post">
                            <input type="hidden" name="appid" value="${appid}">
                            <input type="submit" name="返回" value="返回临时列表" class="default_button">
                        </form>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="11" class="default_line_td"></td>
                </tr>
                <tr align="center" class="list_table_title_tr">
                    <td nowrap>App名称</td>
                    <td nowrap>平台</td>
                    <td nowrap>本次排名</td>
                    <td nowrap>上次排名</td>
                    <td nowrap>排名对比</td>
                </tr>
                <tr>
                    <td height="1" colspan="11" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${listAppFavorite.size() > 0}">
                        <c:forEach items="${listAppFavorite}" var="app" varStatus="st">
                            <tr align="center"
                                class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap>${app.destAppName}</td>
                                <td nowrap><fmt:message key="joymeapp.favorite.platform.${app.appPlatform.code}"
                                                        bundle="${def}"/></td>
                                <td nowrap>${app.displayOrder}</td>
                                <td nowrap>${app.lastDisplayOrder}</td>
                                <td nowrap>
                                    <c:if test="${app.lastDisplayOrder>app.displayOrder}">
                                        <span style="color:#00cc33">上升${app.lastDisplayOrder-app.displayOrder}位</span></c:if>

                                    <c:if test="${app.lastDisplayOrder!=0}">
                                        <c:if test="${app.lastDisplayOrder<app.displayOrder}">
                                            <span style="color:red">下降${app.displayOrder-app.lastDisplayOrder}位</span>
                                        </c:if>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="11" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="11" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>

                </c:choose>
                <tr>
                    <td colspan="11" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="10">
                            <pg:pager url="/joymeapp/favorite/result"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="appid" value="${appId}"/>
                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                <pg:param name="items" value="${page.totalRows}"/>
                                <%@ include file="/WEB-INF/jsp/toolspg.jsp" %>
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