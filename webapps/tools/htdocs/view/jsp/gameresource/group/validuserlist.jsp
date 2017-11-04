<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>小组成员审核表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/dhtmlxcalendar.css">
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/skins/dhtmlxcalendar_dhx_skyblue.css">
    <script src="/static/include/dhtmlxcalendar/dhtmlxcalendar.js"></script>
    <script src="/static/include/js/default.js" type="text/javascript"></script>
    <script src="/static/include/js/jquery.js" type="text/javascript"></script>
    <script language="JavaScript" type="text/JavaScript">
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="100%" valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr class="toolbar_tr">
                    <td>
                        <form action="/group/validuserlist" method="post">
                            用户昵称：
                            <input type="text" name="screenName" value="${screenName}"/> &nbsp;&nbsp;&nbsp;
                            选择状态：
                            <select name="status">
                                <c:forEach var="status" items="${statusList}">
                                    <option value="${status.code}"
                                            <c:if test="${status.code==queryStatus}">selected</c:if> ><fmt:message
                                            key="group.validstatus.${status.code}" bundle="${def}"/></option>
                                </c:forEach>
                            </select>
                            <input type="hidden" name="resid" value="${resid}"/>
                            <input name="pager.offset" type="hidden" value="${page.startRowIdx}"/>
                            <input name="maxPageItems" type="hidden" value="${page.pageSize}"/>


                            <input type="submit" value="查询"/>
                        </form>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="11" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td width="80" align="center">姓名</td>
                    <td width="80" align="center" nowrap>申请日期</td>
                    <td width="80" align="center" nowrap>申请IP</td>
                    <td width="80" align="center" nowrap>申请理由</td>
                    <td width="50" align="center" nowrap="">审核状态</td>
                    <td width="80" align="right" nowrap>审核日期</td>
                    <td width="60" align="center" nowrap>审核人</td>
                    <td width="60" align="center" nowrap>审核组长</td>
                    <td width="60" align="center" nowrap>上一次发布的文章</td>
                    <td width="60" align="center" nowrap>上一次发布文章的时间</td>
                    <td width="120" align="center" nowrap>操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="11" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${groupUserList.size() > 0}">
                        <c:forEach var="dto" items="${groupUserList}" varStatus="st">
                            <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap>${dto.screenName}</td>
                                <td nowrap>${dto.groupUser.createTime}</td>
                                <td nowrap>${dto.groupUser.createIp}</td>
                                <td nowrap width="120">${dto.groupUser.createReason}</td>
                                <td <c:choose>
                                    <c:when test="${dto.groupUser.validStatus.code==1}">
                                        style="color: #008000;"
                                    </c:when>
                                    <c:otherwise>
                                        style="color:red"
                                    </c:otherwise>
                                </c:choose>><fmt:message key="group.validstatus.${dto.groupUser.validStatus.code}"
                                                         bundle="${def}"/></td>
                                <td nowrap>${dto.groupUser.validTime}</td>
                                <td nowrap>${dto.groupUser.validUserid}</td>
                                <td nowrap>${dto.groupUser.validUno}</td>
                                <td nowrap>${dto.groupUser.lastContentId}</td>
                                <td nowrap>${dto.groupUser.lastContentDate}</td>
                                <td nowrap>
                                    <c:choose>
                                        <c:when test="${dto.groupUser.validStatus.code==1}">
                                            <a href="/group/removegroup?uno=${dto.groupUser.uno}&resid=${resid}&pager.offset=${page.startRowIdx}&maxPageItems=${page.pageSize}&status=${queryStatus}">移除话版</a>
                                        </c:when>
                                        <c:when test="${dto.groupUser.validStatus.code==2}">
                                            <a href="/group/joingroup?uno=${dto.groupUser.uno}&resid=${resid}&pager.offset=${page.startRowIdx}&maxPageItems=${page.pageSize}&status=${queryStatus}">加入话版</a>
                                        </c:when>
                                        <c:when test="${dto.groupUser.validStatus.code==3}">
                                        </c:when>
                                        <c:otherwise>
                                            <a href="/group/joingroup?uno=${dto.groupUser.uno}&resid=${resid}&pager.offset=${page.startRowIdx}&maxPageItems=${page.pageSize}&status=${queryStatus}">审核通过</a>
                                            <a href="/group/removegroup?uno=${dto.groupUser.uno}&resid=${resid}&pager.offset=${page.startRowIdx}&maxPageItems=${page.pageSize}&status=${queryStatus}">忽略</a>
                                        </c:otherwise>
                                    </c:choose>
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
                    <td height="1" colspan="11" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="11">
                            <pg:pager url="/group/validuserlist"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="screenName" value="${screenName}"/>
                                <pg:param name="resid" value="${resid}"/>
                                <pg:param name="status" value="${queryStatus}"/>
                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
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