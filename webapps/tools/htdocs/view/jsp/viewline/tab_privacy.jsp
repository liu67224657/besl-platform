<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台数据管理、分类列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script src="/static/include/js/default.js" type="text/javascript"></script>
    <script src="/static/include/js/jquery.js" type="text/javascript"></script>
    <script language="JavaScript" type="text/JavaScript">
        function add(categoryId) {
            window.location.href = "/viewline/presetprivacy?categoryId=" + categoryId ;
        }

        function remove(lineId, lineItemId) {
            if (window.confirm("你确定要删除该信息吗？")) {
                window.location.href = '/viewline/removelineitem?lineId=' + lineId + "&lineItemId=" + lineItemId;
            }
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
                        <input name="Submit" type="submit" class="default_button" value="添加编辑人员" onClick="add('${categoryId}');"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="6" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td width="40" align="center">选择</td>
                    <td align="left">用户昵称</td>
                    <td width="100" align="center" nowrap>隐私等级</td>
                    <td width="140" align="center" nowrap>创建日期</td>
                    <td width="160" align="left" nowrap>创建者</td>
                    <td width="60" align="center" nowrap>操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="6" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${privacies.size() > 0}">
                        <form action="/viewline/privacybatchstatus" method="POST" name="batchform">
                            <%--<input name="lineId" type="hidden" value="${lineId}"/>--%>
                            <input name="categoryId" type="hidden" value="${categoryId}"/>
                            <input name="pager.offset" type="hidden" value="${page.startRowIdx}"/>
                            <c:forEach items="${privacies}" var="privacy" varStatus="st">
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
                                        <input type="checkbox" name="unos" value="${privacy.profileBlog.uno}">
                                    </td>
                                    <td align="left">
                                        ${privacy.profileBlog.screenName}
                                    </td>
                                    <td align="center">
                                        <fmt:message key="def.viewline.category.privacy.${privacy.categoryPrivacy.privacyLevel.code}.name" bundle="${def}"/>
                                    </td>
                                    <td align="center">
                                        <fmt:formatDate value="${privacy.categoryPrivacy.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </td>
                                    <td align="left">
                                        ${privacy.categoryPrivacy.createUserid}
                                    </td>
                                    <td align="center">
                                        <p:privilege name="/viewline/preeditprivacy">
                                            <a href="/viewline/preeditprivacy?categoryId=${categoryId}&uno=${privacy.profileBlog.uno}">修改</a>
                                        </p:privilege>
                                    </td>
                            </c:forEach>
                            <tr>
                                <td height="1" colspan="6" class="default_line_td"></td>
                            </tr>
                            <tr class="toolbar_tr">
                                <td colspan="6">
                                    <input type="checkbox" name="selectall" value="1"
                                           onclick='javascript:checkall(document.forms["batchform"].categoryIdUnos, document.forms["batchform"].selectall)'>全选
                                    <input type="checkbox" name="uncheck" value="1" onclick='javascript:convertcheck(document.forms["batchform"].categoryIdUnos)'>反选
                                    将选中彻底删除：
                                    <input name="submit" type="submit" class="default_button" value="批量删除">
                                </td>
                            </tr>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="6" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td height="1" colspan="6" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="6">
                            <pg:pager url="/viewline/listlineitem"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="lineId" value="${line.lineId}"/>
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