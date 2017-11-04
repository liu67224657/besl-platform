<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台数据管理、special查询列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script language="JavaScript" type="text/JavaScript">
        function add() {
            window.location.href = "/viewspecial/precreatespecial";
        }
    </script>
</head>

<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
    <td height="22" class="page_navigation_td">>> 运营维护 >> 多维度LINE >> 专题管理</td>
</tr>
<tr>
    <td>
        <br>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td class="list_table_header_td">专题查询</td>
            </tr>
        </table>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td height="1" class="default_line_td"></td>
            </tr>
        </table>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <form name="schForm" method="post" action="/viewspecial/listspecial">
                <tr>
                    <td width="80" align="center">搜索条件</td>
                    <td>
                        <table width="100%" border="0" cellspacing="1" cellpadding="0">
                            <tr>
                                <td width="100" align="right" class="edit_table_defaulttitle_td">专题名称/编码：</td>
                                <td class="edit_table_value_td">
                                    <input name="specialName" type="text" id="specialName"
                                           value="${specialName}" class="default_input_singleline" size="32" maxlength="32">
                                </td>
                                <td width="100" align="right" class="edit_table_defaulttitle_td">专题类型：</td>
                                <td class="edit_table_value_td">
                                    <select name="specialTypeCode" class="default_select_single">
                                        <option value="">--所有--</option>
                                        <c:forEach items="${specialTypes}" var="specialType">
                                            <option value="${specialType.code}" <c:if test="${specialTypeCode == specialType.code}">selected="true"</c:if>>
                                                <fmt:message key="def.viewspecial.type.${specialType.code}.name" bundle="${def}"/>
                                            </option>
                                        </c:forEach>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td width="100" align="right" class="edit_table_defaulttitle_td">发布状态：</td>
                                <td nowrap class="edit_table_value_td">
                                    <select name="publishStatusCode" class="default_select_single">
                                        <option value="">--所有--</option>
                                        <c:forEach items="${publishStatuses}" var="publishStatus">
                                            <option value="${publishStatus.code}" <c:if test="${publishStatusCode == publishStatus.code}">selected="true"</c:if>>
                                                <fmt:message key="def.actstatus.${publishStatus.code}.name" bundle="${def}"/>
                                            </option>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td width="100" align="right" class="edit_table_defaulttitle_td">是否可用：</td>
                                <td nowrap class="edit_table_value_td">
                                    <select name="validStatusCode" class="default_select_single">
                                        <option value="">--所有--</option>
                                        <c:forEach items="${validStatuses}" var="validStatus">
                                            <option value="${validStatus.code}" <c:if test="${validStatusCode == validStatus.code}">selected="true"</c:if>>
                                                <fmt:message key="def.validstatus.${validStatus.code}.name" bundle="${def}"/>
                                            </option>
                                        </c:forEach>
                                    </select>
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td width="80" align="center">
                        <p:privilege name="/viewspecial/listspecial">
                            <input name="submit" type="submit" class="default_button" value=" 搜索 ">
                        </p:privilege>
                    </td>
                </tr>
            </form>
        </table>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td height="1" class="default_line_td"></td>
            </tr>
        </table>
    </td>
</tr>
<tr>
    <td height="100%" valign="top">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td class="list_table_header_td">
                    <input type="button" value="新增专题" onclick="add();" class="default_button"/>
                </td>
            </tr>
        </table>
        <table width="100%" border="0" cellspacing="1" cellpadding="0">
            <tr>
                <td height="1" colspan="8" class="default_line_td"></td>
            </tr>
            <tr class="list_table_title_tr">
                <td nowrap align="center" width="40">选择</td>
                <td nowrap>专题编码</td>
                <td nowrap>专题名称</td>
                <td nowrap align="center" width="100">专题类型</td>
                <td nowrap align="center" width="60">发布状态</td>
                <td nowrap align="center" width="60">是否有效</td>
                <td nowrap align="center" width="150">创建日期</td>
                <td nowrap align="center" width="60">操作</td>
            </tr>
            <tr>
                <td height="1" colspan="8" class="default_line_td"></td>
            </tr>
            <c:choose>
                <c:when test="${rows.size() > 0}">
                    <form action="/viewspecial/batchstatusspecials" method="POST" name="batchform">
                        <input name="specialName" type="hidden" value="${specialName}"/>
                        <input name="specialTypeCode" type="hidden" value="${specialTypeCode}"/>
                        <input name="validStatusCode" type="hidden" value="${validStatusCode}"/>
                        <input name="publishStatusCode" type="hidden" value="${publishStatusCode}"/>
                        <input name="pager.offset" type="hidden" value="${page.startRowIdx}"/>
                        <c:forEach items="${rows}" var="special" varStatus="st">
                            <tr class="
                        <c:choose>
                        <c:when test="${st.index % 2 == 0}">
                           list_table_opp_tr
                        </c:when>
                        <c:otherwise>
                            list_table_even_tr
                        </c:otherwise>
                        </c:choose>">
                                <td align="center">
                                    <input type="checkbox" name="specialIds" value="${special.specialId}">
                                </td>
                                <td>
                                        ${special.specialCode}
                                </td>
                                <td>
                                    <a href="${ctx}/viewspecial/detailspecial?specialId=${special.specialId}"> ${special.specialName} </a>
                                </td>
                                <td align="center">
                                    <fmt:message key="def.viewspecial.type.${special.specialType.code}.name" bundle="${def}"/>
                                </td>
                                <td align="center">
                                    <fmt:message key="def.actstatus.${special.publishStatus.code}.name" bundle="${def}"/>
                                </td>
                                <td align="center">
                                    <fmt:message key="def.validstatus.${special.validStatus.code}.name" bundle="${def}"/>
                                </td>
                                <td align="center">
                                    <fmt:formatDate value="${special.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </td>
                                <td align="center">
                                    <p:privilege name="/viewspecial/preeditspecial">
                                        <a href="${ctx}/viewspecial/preeditspecial?specialId=${special.specialId}">修改</a>
                                    </p:privilege>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="8" class="default_line_td"></td>
                        </tr>
                        <tr class="toolbar_tr">
                            <td colspan="8">
                                <input type="checkbox" name="selectall" value="1" onclick='javascript:checkall(document.forms["batchform"].specialCodes, document.forms["batchform"].selectall)'>全选
                                <input type="checkbox" name="uncheck" value="1" onclick='javascript:convertcheck(document.forms["batchform"].specialCodes)'>反选
                                将选中记录有效状态改成：
                                <select name="updateValidStatusCode" class="default_select_single">
                                    <option value="">--请选择--</option>
                                    <c:forEach items="${validStatuses}" var="validStatus">
                                        <option value="${validStatus.code}" <c:if test="${updateValidStatusCode == validStatus.code}">selected="true"</c:if>>
                                            <fmt:message key="def.validstatus.${validStatus.code}.name" bundle="${def}"/>
                                        </option>
                                    </c:forEach>
                                </select>
                                发布状态改成：
                                <select name="updatePublishStatusCode" class="default_select_single">
                                    <option value="">--请选择--</option>
                                    <c:forEach items="${publishStatuses}" var="publishStatus">
                                        <option value="${publishStatus.code}" <c:if test="${updateUpdateStatusCode == publishStatus.code}">selected="true"</c:if>>
                                            <fmt:message key="def.actstatus.${publishStatus.code}.name" bundle="${def}"/>
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
                        <td colspan="8" class="error_msg_td">暂无数据!</td>
                    </tr>
                </c:otherwise>
            </c:choose>
        </table>

        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td height="1" class="list_table_title_tr">
                    <pg:pager url="/viewspecial/listspecial"
                              items="${page.totalRows}" isOffset="true"
                              maxPageItems="${page.pageSize}"
                              export="offset, currentPageNumber=pageNumber" scope="request">

                        <pg:param name="specialName" value="${specialName}"/>
                        <pg:param name="validStatusCode" value="${validStatusCode}"/>
                        <pg:param name="specialTypeCode" value="${specialTypeCode}"/>
                        <pg:param name="publishStatusCode" value="${publishStatusCode}"/>
                        <%@ include file="/WEB-INF/jsp/toolspg.jsp" %>
                    </pg:pager>
                </td>
            </tr>
        </table>
    </td>
</tr>
</table>
</body>
</html>