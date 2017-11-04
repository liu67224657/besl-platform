<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>游戏权限</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script src="/static/include/js/default.js" type="text/javascript"></script>
    <script src="/static/include/js/jquery.js" type="text/javascript"></script>
</head>
<script type="text/javascript">
     function create(resid){
         window.location.href="/gameresource/privacy/createpage?resid="+resid;
     }
</script>
<body>
<c:if test="${fn:length(errorMsg)>0}">
    <b><fmt:message key="${errorMsg}" bundle="${error}"/></b>
</c:if>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="100%" valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr class="toolbar_tr">
                    <td><input name="Submit" type="submit" class="default_button" value="添加人员"  onClick="create('${resid}');"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="7" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td align="left"  width="100">用户昵称</td>
                    <td width="100" align="center" nowrap>用户权限</td>
                    <td width="140" align="center" nowrap>创建日期</td>
                    <td width="160" align="left" nowrap>创建者</td>
                     <td width="160" align="left" nowrap>修改日期</td>
                     <td width="160" align="left" nowrap>修改人</td>
                    <td width="130" align="center" nowrap>操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="7" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${privacyList.size() > 0}">
                        <form action="/game/privacy/batchmodifystatus" method="POST" name="batchform">
                            <input name="categoryId" type="hidden" value="${categoryId}"/>
                            <input name="pager.offset" type="hidden" value="${page.startRowIdx}"/>
                            <c:forEach items="${privacyList}" var="privacy" varStatus="st">
                            <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when>
                                   <c:otherwise> list_table_even_tr</c:otherwise></c:choose>">
                                <td align="left">
                                        ${privacy.screenName}
                                </td>
                                <td align="center">
                                    <fmt:message key="def.gameres.privacy.${privacy.gamePrivacy.privacyLevel.code}.name" bundle="${def}"/>
                                </td>
                                <td align="center">
                                    <fmt:formatDate value="${privacy.gamePrivacy.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </td>
                                <td align="left">
                                        ${privacy.gamePrivacy.createUserid}
                                </td>
                                <td align="center">
                                    <fmt:formatDate value="${privacy.gamePrivacy.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </td>
                                <td align="left">
                                        ${privacy.gamePrivacy.updateUserid}
                                </td>
                                <td align="center">
                                    <p:privilege name="/gameresource/privacy/modifypage">
                                        <a href="/gameresource/privacy/modifypage?resid=${resid}&uno=${privacy.gamePrivacy.uno}&level=${privacy.gamePrivacy.privacyLevel.code}">修改</a>
                                    </p:privilege>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    <p:privilege name="/gameresource/privacy/delete">
                                        <a href="/gameresource/privacy/delete?resid=${resid}&uno=${privacy.gamePrivacy.uno}&level=${privacy.gamePrivacy.privacyLevel.code}">删除</a>
                                    </p:privilege>
                                </td>
                                </c:forEach>
                            <tr>
                                <td height="1" colspan="7" class="default_line_td"></td>
                            </tr>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="7" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <%--<c:if test="${page.maxPage > 1}">--%>
                    <%--<tr class="list_table_opp_tr">--%>
                        <%--<td colspan="6">--%>
                            <%--<pg:pager url="/viewline/listlineitem"--%>
                                      <%--items="${page.totalRows}" isOffset="true"--%>
                                      <%--maxPageItems="${page.pageSize}"--%>
                                      <%--export="offset, currentPageNumber=pageNumber" scope="request">--%>
                                <%--<pg:param name="lineId" value="${line.lineId}"/>--%>
                                <%--<%@ include file="/WEB-INF/jsp/toolspg.jsp" %>--%>
                            <%--</pg:pager>--%>
                        <%--</td>--%>
                    <%--</tr>--%>
                <%--</c:if>--%>
            </table>
        </td>
    </tr>
</table>
</body>
</html>