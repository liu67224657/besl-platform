<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台数据管理,运营维护,分类管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script language="JavaScript" type="text/JavaScript">
        function add(aspectCode, parentCategoryId) {
//            var aspectCode = aspectCode.replace(".", "_");
            window.location.href = "/viewline/categoryprecreate?aspectCode=" + aspectCode + "&parentCategoryId=" + parentCategoryId;
        }
    </script>
</head>

<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 分类管理 >> 类别管理</td>
    </tr>
    <tr>
        <td>
            <br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">类别查询</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <form name="schForm" method="post" action="/viewline/categorylist">
                    <tr>
                        <td width="80" align="center">搜索条件</td>
                        <td>
                            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                                <tr>
                                    <td width="100" align="right" class="edit_table_defaulttitle_td">类别角度：</td>
                                    <td class="edit_table_value_td">
                                        <select name="aspectCode" class="default_select_single">
                                            <c:forEach items="${aspects}" var="aspect">
                                                <option value="${aspect.code}" <c:if test="${aspectCode eq aspect.code}">selected="true"</c:if>>
                                                    <fmt:message key="def.viewline.category.aspect.${aspect.code}.name" bundle="${def}"/>
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td width="100" align="right" class="edit_table_defaulttitle_td">一级分类名称：</td>
                                    <td class="edit_table_value_td">
                                        <input name="categoryName" type="text" class="default_input_singleline" size="16" maxlength="32" value="${categoryName}">
                                    </td>

                                </tr>
                            </table>
                        </td>
                        <td width="80" align="center">
                            <input name="submit" type="submit" class="default_button" value=" 搜索 ">
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
                        <input type="button" value="新增该角度下的一级分类" onclick="add('${aspectCode}', '0');" class="default_button"/>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="10" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center" width="40">选择</td>
                    <td>分类ID</td>
                    <td nowrap>分类名称</td>
                    <td nowrap>分类编码</td>
                    <td nowrap align="center" width="100">排序值</td>
                    <td nowrap align="center" width="100">排序操作</td>
                    <td nowrap align="center" width="60">发布状态</td>
                    <td nowrap align="center" width="60">是否有效</td>
                    <td nowrap align="center" width="150">创建日期</td>
                    <td nowrap align="center" width="160">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="10" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${rows.size() > 0}">
                        <form action="/viewline/categorybatchstatus" method="POST" name="batchform">
                            <input name="aspectCode" type="hidden" value="${aspectCode}"/>
                            <c:forEach items="${rows}" var="category" varStatus="st">
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
                                        <input type="checkbox" name="categoryIds" value="${category.categoryId}">
                                    </td>
                                    <td>
                                        ${category.categoryId}
                                    </td>
                                    <td>
                                        <span style="display:block;text-indent:${2 * category.displayLevel}em;"><a href="/viewline/categorydetail?categoryId=${category.categoryId}"> ${category.categoryName} </a></span>
                                    </td>
                                    <td>
                                            ${category.categoryCode}
                                    </td>
                                    <td align="right">
                                         ${category.displayOrder}
                                        <%--${requestScope}--%>
                                    </td>
                                    <td align="center">
                                        <c:if test="${category.displayLevel == 1}">
                                            <p:privilege name="/json/viewcategory/sort">
                                                <a href="/json/viewcategory/sort?categoryId=${category.categoryId}&type=prev&aspectCode=${aspectCode}&items=${page.totalRows}&maxPageItems=${page.pageSize}&categoryName=${categoryName}&pager.offset=${page.curPage}"><img src="/static/images/icon/up.gif"></a>
                                            <a href="/json/viewcategory/sort?categoryId=${category.categoryId}&type=next&aspectCode=${aspectCode}&items=${page.totalRows}&maxPageItems=${page.pageSize}&categoryName=${categoryName}&pager.offset=${page.curPage}"><img src="/static/images/icon/down.gif"></a>
                                            </p:privilege>
                                        </c:if>
                                    </td>
                                    <td align="center">
                                        <fmt:message key="def.actstatus.${category.publishStatus.code}.name" bundle="${def}"/>
                                    </td>
                                    <td align="center">
                                        <fmt:message key="def.validstatus.${category.validStatus.code}.name" bundle="${def}"/>
                                    </td>
                                    <td align="center">
                                        <fmt:formatDate value="${category.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </td>
                                    <td align="center">
                                        <p:privilege name="/viewline/categorypreedit">
                                            <a href="/viewline/categorypreedit?categoryId=${category.categoryId}">修改</a>
                                        </p:privilege>
                                        <p:privilege name="/viewline/categoryprecreate">
                                            <a href="/viewline/categoryprecreate?aspectCode=${aspectCode}&parentCategoryId=${category.categoryId}">新建下级</a>
                                        </p:privilege>
                                    </td>
                                </tr>
                            </c:forEach>
                            <tr>
                                <td height="1" colspan="10" class="default_line_td"></td>
                            </tr>
                            <tr class="toolbar_tr">
                                <td colspan="10">
                                    <input type="checkbox" name="selectall" value="1"
                                           onclick='javascript:checkall(document.forms["batchform"].categoryIds, document.forms["batchform"].selectall)'>全选
                                    <input type="checkbox" name="uncheck" value="1" onclick='javascript:convertcheck(document.forms["batchform"].categoryIds)'>反选
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
                            <td colspan="10" class="error_msg_td">暂无数据!</td>
                        </tr>
                        <tr>
                            <td height="1" colspan="10" class="default_line_td"></td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="10">
                            <pg:pager url="/viewline/categorylist"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="aspectCode" value="${aspectCode}"/>
                                <pg:param name="maxPageItems" value="${page.pageSize}"/><!--不能缺少的-->
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