<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>新游戏预告管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 条目管理 >> 新游戏预告管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">新游戏列表</td>
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
                        <a href="/gameresource/newrelease/list"><input type="button" name="button"
                                                                       class="default_button" value="全部查询"/></a>
                        <a href="/gameresource/newrelease/list?status=invalid"><input type="button" name="button"
                                                                                      class="default_button"
                                                                                      value="待审核查询"/></a>
                        <a href="/gameresource/newrelease/list?status=valid"><input type="button" name="button"
                                                                                    class="default_button"
                                                                                    value="已通过审核查询"/></a>
                    </td>
                </tr>
            </table>
            <form action="/gameresource/newrelease/list" method="post">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td colspan="22" height="1" class="default_line_td"></td>
                    </tr>
                    <tr class="list_table_title_tr">
                        <td nowrap align="left" width="">游戏名称</td>
                        <td nowrap align="left" width="">游戏图标</td>
                        <td nowrap align="left" width="">团队名称</td>
                        <td nowrap align="left" width="">团队人数</td>
                        <td nowrap align="left" width="">合作方式</td>
                        <c:if test="${validStatus eq 'valid'}">
                            <td nowrap align="left" width="">排序</td>
                        </c:if>
                        <td nowrap align="left" width="">审核状态</td>
                        <td nowrap align="left" width="">操作</td>
                    </tr>
                    <c:choose>
                        <c:when test="${list.size() > 0}">
                            <c:forEach items="${list}" var="dto" varStatus="st">
                                <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                    <td nowrap><a
                                            href="/gameresource/newrelease/detail?infoid=${dto.newRelease.newReleaseId}">${dto.newRelease.newGameName}</a>
                                    </td>
                                    <td nowrap><img src="${dto.newRelease.newGameIcon}" height="100" width="100"></td>
                                    <td nowrap>${dto.newRelease.companyName}</td>
                                    <td nowrap><fmt:message
                                            key="gameres.newgame.peoplenumtype.${dto.newRelease.peopleNumType.value}"
                                            bundle="${def}"/></td>
                                    <td nowrap>
                                        <c:if test="${dto.newRelease.cooprateType.hasExclusive()}">独代&nbsp;</c:if>
                                        <c:if test="${dto.newRelease.cooprateType.hasBenefit()}">分成&nbsp;</c:if>
                                    </td>
                                    <c:if test="${validStatus eq 'valid'}">
                                        <td align="center">
                                            <p:privilege name="/gameresource/newrelease/sort/up">
                                                <a href="/gameresource/newrelease/sort/up?infoid=${dto.newRelease.newReleaseId}"><img
                                                        src="/static/images/icon/up.gif"></a>
                                            </p:privilege>
                                            &nbsp;
                                            <p:privilege name="/gameresource/newrelease/sort/down">
                                                <a href="/gameresource/newrelease/sort/down?infoid=${dto.newRelease.newReleaseId}"><img
                                                        src="/static/images/icon/down.gif"></a>
                                            </p:privilege>
                                        </td>
                                    </c:if>
                                    <td <c:choose>
                                        <c:when test="${dto.newRelease.validStatus.code eq 'valid'}">
                                            style="color: #008000;"
                                        </c:when>
                                        <c:otherwise>
                                            style="color:red"
                                        </c:otherwise>
                                    </c:choose>><fmt:message
                                            key="gameres.newgame.validstatus.${dto.newRelease.validStatus.code}"
                                            bundle="${def}"/>
                                    </td>
                                    <td nowrap>
                                        <p:privilege name="/gameresource/newrelease/delete">
                                            <a href="/gameresource/newrelease/delete?infoid=${dto.newRelease.newReleaseId}">删除</a>
                                        </p:privilege>
                                    </td>
                                </tr>
                            </c:forEach>
                            <tr>
                                <td height="1" colspan="22" class="default_line_td"></td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="22" class="error_msg_td">暂无数据!</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    <tr>
                        <td colspan="22" height="1" class="default_line_td"></td>
                    </tr>
                    <c:if test="${page.maxPage > 1}">
                        <tr class="list_table_opp_tr">
                            <td colspan="13">
                                <pg:pager url="/joymeapp/resource/list"
                                          items="${page.totalRows}" isOffset="true"
                                          maxPageItems="${page.pageSize}"
                                          export="offset, currentPageNumber=pageNumber" scope="request">
                                    <pg:param name="validStatus" value="${validStatus}"/>
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