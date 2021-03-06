<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>小组权限管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 条目管理 >> 小组权限管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">小组权限管理</td>
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
            <table border="0">
                <tr>
                    <td height="1" valign="middle">
                        <form action="/gameresource/group/privilege/list" method="post" style="vertical-align: middle;height: 12">
                                        选择类型：
                                        <select name="privilegetype">
                                            <option value="">请选择</option>
                                            <c:forEach items="${privilegeTypeCollection}" var="type">
                                                <option value="${type.code}"
                                                        <c:if test="${type.code==privilegeType}">selected="selected"</c:if>>
                                                    <fmt:message key="gameresource.group.privilege.type.${type.code}"
                                                                 bundle="${def}"/></option>
                                            </c:forEach>
                                        </select>
                                        <input type="submit" name="button" class="default_button" value="查询"/>
                        </form>
                    </td>
                    <td height="1" valign="middle">
                        <a href="/gameresource/group/privilege/createpage?privilegetype=${privilegeType}"><input type="button" name="button" class="default_button" value="添加权限"/></a>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/gameresource/group/privilege/list" method="post">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td colspan="22" height="1" class="default_line_td"></td>
                    </tr>
                    <tr class="list_table_title_tr">
                        <td nowrap width="">权限ID</td>
                        <td nowrap width="">权限名</td>
                        <td nowrap width="">权限code</td>
                        <td nowrap width="">权限类型</td>
                        <td nowrap width="">权限描述</td>
                        <td nowrap width="">状态</td>
                        <td nowrap width="">创建信息</td>
                        <td nowrap width="">修改信息</td>
                        <td nowrap align="left" width="">操作</td>
                    </tr>
                    <c:choose>
                        <c:when test="${list.size() > 0}">
                            <c:forEach items="${list}" var="privilege" varStatus="st">
                                <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                    <td nowrap>${privilege.privilegeId}</td>
                                    <td nowrap>${privilege.privilegeName}</td>
                                    <td nowrap>
                                        <fmt:message
                                                key="gameresource.group.privilege.code.${privilege.privilegeCode.code}"
                                                bundle="${def}"/>
                                    </td>
                                    <td nowrap>
                                        <fmt:message
                                                key="gameresource.group.privilege.type.${privilege.privilegeType.code}"
                                                bundle="${def}"/>
                                    </td>
                                    <td nowrap>${privilege.privilegeDesc}</td>
                                    <td
                                            <c:choose>
                                            <c:when test="${privilege.actStatus.code eq 'n'}">style="color: #008000;"
                                            </c:when>
                                                <c:otherwise>style="color:#ff0000;"</c:otherwise>
                                    </c:choose>>
                                        <fmt:message
                                                key="gameresource.group.privilege.status.${privilege.actStatus.code}"
                                                bundle="${def}"/>
                                    </td>
                                    <td nowrap><fmt:formatDate value="${privilege.createDate}"
                                                               pattern="yyyy-MM-dd HH:mm:ss"/><br/>${privilege.createIp}<br/>${privilege.createUserId}
                                    </td>
                                    <td nowrap><fmt:formatDate value="${privilege.lastModifyDate}"
                                                               pattern="yyyy-MM-dd HH:mm:ss"/><br/>${privilege.lastModifyIp}<br/>${privilege.lastModifyUserId}
                                    </td>
                                    <td nowrap>
                                        <p:privilege name="/gameresource/group/privilege/modifypage">
                                            <a href="/gameresource/group/privilege/modifypage?privilegeid=${privilege.privilegeId}">编辑</a>
                                        </p:privilege>
                                        <c:choose>
                                            <c:when test="${privilege.actStatus.code eq 'n'}">
                                                <p:privilege name="/gameresource/group/privilege/delete">
                                                    <a href="/gameresource/group/privilege/delete?privilegeid=${privilege.privilegeId}">删除</a>
                                                </p:privilege>
                                            </c:when>
                                            <c:otherwise>
                                                <p:privilege name="/gameresource/group/privilege/recover">
                                                    <a href="/gameresource/group/privilege/recover?privilegeid=${privilege.privilegeId}">恢复</a>
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
                                    <pg:param name="privilegetype" value="${privilegeType}"/>
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