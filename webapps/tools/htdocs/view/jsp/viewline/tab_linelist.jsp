<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台数据管理、Line查询列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script language="JavaScript" type="text/JavaScript">
        function add(categoryId) {
            window.location.href = "/viewline/lineprecreate?categoryId=" + categoryId;
        }
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="100%" valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr class="toolbar_tr">
                    <td>
                        <p:privilege name="/viewline/lineprecreate">
                            <input name="Submit" type="submit" class="default_button" value="建立关联关系" onClick="add('${categoryId}');">
                        </p:privilege>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="11" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center" width="40">选择</td>
                    <td nowrap align="left" >Line ID</td>
                    <td nowrap align="center" width="100">列表位置</td>
                    <td nowrap align="left">列表名称</td>
                    <td nowrap align="left">列表描述</td>
                    <td nowrap align="center" width="80">元素类型</td>
                    <td nowrap align="center" width="80">最少条数</td>
                    <td nowrap align="center" width="80">排序值</td>
                    <td nowrap align="center" width="160">创建日期</td>
                    <td nowrap align="center" width="80">当前状态</td>
                    <td nowrap align="center" width="60">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="11" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${rows.size() > 0}">
                        <form action="/viewline/batchstatuslines" method="POST" name="batchform">
                            <input type="hidden" name="categoryId" value="${categoryId}">
                            <c:forEach items="${rows}" var="line" varStatus="st">
                                <tr class="<c:choose>
                            <c:when test="${st.index % 2 == 0}">
                               list_table_opp_tr
                            </c:when>
                            <c:otherwise>
                                list_table_even_tr
                            </c:otherwise>
                            </c:choose>">
                                    <td align="center">
                                        <input type="checkbox" name="lineIds" value="${line.lineId}">
                                    </td>
                                    <td align="left">
                                        ${line.lineId}
                                    </td>
                                    <td align="center">
                                        ${line.locationCode}
                                    </td>
                                    <td align="left">
                                        <a href="${ctx}/viewline/linedetail?lineId=${line.lineId}" target="_parent"> ${line.lineName}</a>
                                    </td>
                                    <td align="left">
                                        ${line.lineDesc}
                                    </td>
                                    <td align="center">
                                        <fmt:message key="def.viewline.itemtype.${line.itemType.code}.name" bundle="${def}"/>
                                    </td>
                                    <td align="center">
                                        ${line.itemMinCount}
                                    </td>
                                    <td align="center">
                                        ${line.displayOrder}
                                    </td>
                                    <td align="center">
                                        <fmt:formatDate value="${line.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </td>
                                    <td align="center">
                                        <fmt:message key="def.validstatus.${line.validStatus.code}.name" bundle="${def}"/>
                                    </td>
                                    <td align="center">
                                        <p:privilege name="/viewline/linepreedit">
                                            <a href="/viewline/linepreedit?lineId=${line.lineId}&categoryId=${categoryId}">修改</a>
                                        </p:privilege>
                                        <a href="/viewline/copypage?lineId=${line.lineId}">复制</a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <tr>
                                <td height="1" colspan="11" class="default_line_td"></td>
                            </tr>
                            <tr class="toolbar_tr">
                                <td colspan="11">
                                    <input type="checkbox" name="selectall" value="1"
                                           onclick='javascript:checkall(document.forms["batchform"].lineIds, document.forms["batchform"].selectall)'>全选
                                    <input type="checkbox" name="uncheck" value="1" onclick='javascript:convertcheck(document.forms["batchform"].lineIds)'>反选
                                    将选中记录有效状态改成：
                                    <select name="updateValidStatusCode" class="default_select_single">
                                        <option value="">--请选择--</option>
                                        <c:forEach items="${validStatuses}" var="validStatus">
                                            <option value="${validStatus.code}" <c:if test="${updateValidStatusCode == validStatus.code}">selected="true"</c:if>>
                                                <fmt:message key="def.validstatus.${validStatus.code}.name" bundle="${def}"/>
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
                            <td colspan="11" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="11" height="1" class="default_line_td"></td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>