<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>小组角色权限管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 条目管理 >> 小组角色权限管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">小组角色权限管理</td>
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
                        <form action="/gameresource/group/role/privilege/list" method="post">
                            选择类型：
                            <select name="privilegetype">
                                <option value="">请选择</option>
                                <c:forEach items="${typeCollection}" var="type">
                                     <option value="${type.code}" <c:if test="${type.code==privilegeType}">selected="selected"</c:if>><fmt:message key="gameresource.group.privilege.type.${type.code}" bundle="${def}"/></option>
                                </c:forEach>
                            </select>
                            <input type="hidden" name="roleid" value="${roleId}"/>
                            <input type="hidden" name="groupid" value="${groupId}"/>
                            <input type="submit" name="button" class="default_button" value="查询"/>
                        </form>
                        <c:if test="${privilegeType>0}">
                            <a href="/gameresource/group/role/privilege/createpage?privilegetype=${privilegeType}&roleid=${roleId}&groupid=${groupId}"><input type="button" name="button" class="default_button" value="添加权限"/></a>
                        </c:if>
                    </td>
                </tr>
            </table>
            <form action="/gameresource/group/role/privilege/list" method="post">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td colspan="22" height="1" class="default_line_td"></td>
                    </tr>
                    <tr class="list_table_title_tr">
                        <td nowrap width="">小组ID</td>
                        <td nowrap width="">小组名</td>
                        <td nowrap width="">角色ID</td>
                        <td nowrap width="">角色名</td>
                        <td nowrap width="">角色级别</td>
                        <td nowrap width="">权限ID</td>
                        <td nowrap width="">权限名</td>
                        <td nowrap width="">权限类型</td>
                        <td nowrap width="">状态</td>
                        <td nowrap width="">创建信息</td>
                        <td nowrap width="">修改信息</td>
                        <td nowrap align="left" width="">操作</td>
                    </tr>
                    <c:choose>
                        <c:when test="${list.size() > 0}">
                            <c:forEach items="${list}" var="dto" varStatus="st">
                                <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                    <td nowrap>${dto.group.resourceId}</td>
                                    <td nowrap>${dto.group.resourceName}</td>
                                    <td nowrap>${dto.role.roleId}</td>
                                    <td nowrap>${dto.role.roleName}</td>
                                    <td nowrap><fmt:message key="gameresource.group.role.level.${dto.role.roleLevel.code}" bundle="${def}"/></td>
                                    <td nowrap>${dto.privilege.privilegeId}</td>
                                    <td nowrap>${dto.privilege.privilegeName}</td>
                                    <td nowrap><fmt:message key="gameresource.group.privilege.type.${dto.privilege.privilegeType.code}" bundle="${def}"/></td>
                                    <td <c:choose><c:when test="${dto.relation.actStatus.code eq 'n'}">
                                        style="color: #008000;"
                                    </c:when>
                                        <c:otherwise>
                                            style="color:#ff0000;"
                                        </c:otherwise></c:choose>>
                                        <fmt:message key="gameresource.group.privilege.role.relation.status.${dto.relation.actStatus.code}" bundle="${def}"/></td>
                                    <td nowrap><fmt:formatDate value="${dto.relation.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/><br/>${dto.relation.createIp}<br/>${dto.relation.createUserId}</td>
                                    <td nowrap><fmt:formatDate value="${dto.relation.lastModifyDate}" pattern="yyyy-MM-dd HH:mm:ss"/><br/>${dto.relation.lastModifyIp}<br/>${dto.relation.lastModifyUserId}</td>
                                    <td nowrap>
                                        <p:privilege name="/gameresource/group/role/privilege/modifypage">
                                            <a href="/gameresource/group/role/privilege/modifypage?relationid=${dto.relation.relationId}&groupid=${dto.group.resourceId}&roleid=${dto.role.roleId}&privilegetype=${dto.privilege.privilegeType.code}&privilegeid=${dto.privilege.privilegeId}">编辑</a>
                                        </p:privilege>
                                        <c:choose>
                                            <c:when test="${dto.relation.actStatus.code eq 'n'}">
                                                <p:privilege name="/gameresource/group/role/privilege/delete">
                                                    <a href="/gameresource/group/role/privilege/delete?relationid=${dto.relation.relationId}&groupid=${dto.group.resourceId}&roleid=${dto.role.roleId}&privilegetype=${dto.privilege.privilegeType.code}">删除</a>
                                                </p:privilege>
                                            </c:when>
                                            <c:otherwise>
                                                <p:privilege name="/gameresource/group/role/privilege/recover">
                                                    <a href="/gameresource/group/role/privilege/recover?relationid=${dto.relation.relationId}&groupid=${dto.group.resourceId}&roleid=${dto.role.roleId}&privilegetype=${dto.privilege.privilegeType.code}">恢复</a>
                                                </p:privilege>
                                            </c:otherwise>
                                        </c:choose>

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