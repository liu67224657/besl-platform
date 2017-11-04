<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>小组角色管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 条目管理 >>成员列表管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">成员列表管理</td>
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
                        <form action="/group/profile/list" method="post">
                            选择小组
                            <select name="groupid">
                                <option value="">请选择</option>
                                <c:forEach items="${groupList}" var="group">
                                    <option value="${group.resourceId}"
                                            <c:if test="${group.resourceId==groupId}">selected="selected"</c:if>>${group.resourceName}</option>
                                </c:forEach>
                            </select>
                            <input type="submit" name="button" class="default_button" value="查询"/>
                        </form>

                    </td>
                </tr>
            </table>
            <form action="/group/profile/list" method="post">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td colspan="22" height="1" class="default_line_td"></td>
                    </tr>
                    <tr class="list_table_title_tr">
                        <td nowrap width="">ID</td>
                        <td nowrap width="">昵称</td>
                        <td nowrap width="">小组ID</td>
                        <td nowrap width="">小组内角色</td>
                        <td nowrap width="">状态</td>
                        <td nowrap width="">加入小组时间</td>
                        <td nowrap width="">审核人uno</td>
                        <td nowrap align="left" width="">操作</td>
                    </tr>
                    <c:choose>
                        <c:when test="${list.size() > 0}">
                            <c:forEach var="dto" items="${list}" varStatus="st">
                                <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                    <td nowrap>${dto.groupProfile.groupProfileId}</td>
                                    <td nowrap>${dto.screenName}</td>
                                    <td nowrap>${dto.groupProfile.groupId}</td>
                                    <td nowrap><fmt:message
                                            key="group.profile.role.type.${dto.groupProfile.roleLevel.code}"
                                            bundle="${def}"/></td>
                                    <td><fmt:message key="group.profile.type.${dto.groupProfile.status.code}"
                                                     bundle="${def}"/></td>

                                    <td nowrap>${dto.groupProfile.createDate}</td>
                                    <td nowrap>${dto.groupProfile.createUno}</td>
                                    <td nowrap><a
                                            href="/group/profile/createrole?groupid=${dto.groupProfile.groupId}&groupprofileid=${dto.groupProfile.groupProfileId}">给成员分配角色</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="11" class="error_msg_td">暂无数据!</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    <tr>
                        <td colspan="22" height="1" class="default_line_td"></td>
                    </tr>
                    <c:if test="${page.maxPage > 1}">
                        <tr class="list_table_opp_tr">
                            <td colspan="13">
                                <pg:pager url="/group/profile/list"
                                          items="${page.totalRows}" isOffset="true"
                                          maxPageItems="${page.pageSize}"
                                          export="offset, currentPageNumber=pageNumber" scope="request">
                                    <pg:param name="groupid" value="${groupId}"/>
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