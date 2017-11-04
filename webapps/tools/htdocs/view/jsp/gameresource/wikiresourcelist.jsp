<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台数据管理、WIKI条目列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script language="JavaScript" type="text/JavaScript">
        function remove(resourceId) {
            if (window.confirm("你确定要删除该信息吗?")) {
                window.location.href = '/gameresource/removegameresource?resourceId=' + resourceId;
            }
        }
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 条目管理 >> WIKI条目查询</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">WIKI条目列表</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <form name="schForm" method="post" action="/wiki/list">
                    <tr>
                        <td width="80" align="center">搜索条件</td>
                        <td>
                            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                                <tr>
                                    <td width="100" align="right" class="edit_table_defaulttitle_td">WIKI名称：</td>
                                    <td class="edit_table_value_td">
                                        <input name="resourceName" type="text" class="default_input_singleline" size="24"
                                               maxlength="64" id="resourceName" value="${resourceName}">
                                    </td>
                                    <td width="100" align="right" class="edit_table_defaulttitle_td">是否删除：</td>
                                    <td class="edit_table_value_td">
                                        <select name="removeStatusCode" class="default_select_single">
                                            <option value="">--所有--</option>
                                            <c:forEach items="${removeStatuses}" var="removeStatus">
                                                <option value="${removeStatus.code}" <c:if test="${removeStatusCode == removeStatus.code}">selected="true"</c:if>>
                                                    <fmt:message key="def.removestatus.${removeStatus.code}.name" bundle="${def}"/>
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td width="80" align="center">
                            <p:privilege name="/wiki/list">
                                <input name="submit" type="submit" class="default_button" value=" 搜索 ">
                            </p:privilege>
                        </td>
                    </tr>
                </form>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="8" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center" width="10%">选择</td>
                    <td nowrap align="center" width="10%">条目名称</td>
                    <td nowrap align="center" width="8%">条目编码</td>
                    <td nowrap align="center" width="10%">条目ID</td>
                      <td nowrap align="center" width="13%">条目URL</td>
                    <td nowrap align="center" width="10%">是否删除</td>
                    <td nowrap align="center" width="14%">添加时间</td>
                    <td nowrap align="center" width="13%">添加人</td>
                    <td nowrap align="center" >操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="9" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${rows.size() > 0}">
                        <form action="/wiki/batchmodifystatus" method="POST" name="batchform">
                            <input name="resourceName" type="hidden" value="${resourceName}"/>
                            <input name="removeStatusCode" type="hidden" value="${removeStatusCode}"/>
                            <input name="pager.offset" type="hidden" value="${page.startRowIdx}"/>
                            <input name="maxPageItems" type="hidden" value="${page.pageSize}"/>
                            <c:forEach items="${rows}" var="wiki" varStatus="st">
                                <tr class="
                        <c:choose>
                        <c:when test="${st.index % 2 == 0}">
                           list_table_opp_tr
                        </c:when>
                        <c:otherwise>
                            list_table_even_tr
                        </c:otherwise>
                        </c:choose>">
                                    <td nowrap align="center">
                                        <input type="checkbox" name="resourceIds" value="${wiki.resourceId}">
                                    </td>
                                    <td >
                                            <a href="/wiki/detail?resid=${wiki.resourceId}">${wiki.wikiName}</a>
                                    </td>
                                    <td >
                                          ${wiki.wikiCode}
                                    </td>
                                    <td nowrap>${wiki.resourceId}</td>
                                    <td nowrap>${wiki.wikiUrl}</td>
                                    <%--<td align="center">--%>
                                        <%--<fmt:message key="def.gameresource.domain.${gameres.resourceDomain.code}.name" bundle="${def}"/>--%>
                                    <%--</td>--%>
                                    <td align="center">
                                        <fmt:message key="def.removestatus.${wiki.removeStatus.code}.name" bundle="${def}"/>
                                    </td>
                                    <td align="center">
                                        <fmt:formatDate value="${wiki.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </td>
                                    <td align="center">
                                            ${wiki.createUserid}
                                    </td>
                                    <td align="center">
                                        <p:privilege name="/wiki/editpage">
                                            <a href="/wiki/editpage?resourceId=${wiki.resourceId}">编辑</a>
                                        </p:privilege>

                                        <p:privilege name="/wiki/stat">
                                            <a href="/wiki/stat?resid=${wiki.resourceId}">统计WIKI</a>
                                        </p:privilege>
                                    </td>
                                </tr>
                            </c:forEach>
                            <tr>
                                <td height="1" colspan="9" class="default_line_td"></td>
                            </tr>
                            <tr class="toolbar_tr">
                                <td colspan="9">
                                    <input type="checkbox" name="selectall" value="1"
                                           onclick='javascript:checkall(document.forms["batchform"].resourceIds, document.forms["batchform"].selectall)'>全选
                                    <input type="checkbox" name="uncheck" value="1" onclick='javascript:convertcheck(document.forms["batchform"].resourceIds)'>反选
                                    将选中记录删除状态改成：
                                    <select name="updateRemoveStatusCode" class="default_select_single">
                                        <option value="">--请选择--</option>
                                        <c:forEach items="${removeStatuses}" var="removeStatus">
                                            <option value="${removeStatus.code}" <c:if test="${updateRemoveStatusCode == removeStatus.code}">selected="true"</c:if>>
                                                <fmt:message key="def.removestatus.${removeStatus.code}.name" bundle="${def}"/>
                                            </option>
                                        </c:forEach>
                                    </select>
                                    <input name="submit" type="submit" class="default_button" value="批量修改">
                                </td>
                            </tr>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="9" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="9" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="9">
                            <pg:pager url="/gameresource/gameresourcelist"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="resourceName" value="${resourceName}"/>
                                <pg:param name="removeStatusCode" value="${removeStatusCode}"/>
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