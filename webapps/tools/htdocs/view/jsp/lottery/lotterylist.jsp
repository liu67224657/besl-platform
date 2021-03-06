<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>抽奖管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript">

    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷抽奖 >> 抽奖管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">抽奖列表</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="14" class="error_msg_td">
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
                        <form method="post" action="/lottery/createpage">
                            <table>
                                 <tr>
                                     <td>
                                         <p:privilege name="/lottery/createpage">
                                            <input type="submit" name="button" class="default_button" value="添加抽奖"/>
                                         </p:privilege>
                                     <td>
                                 <tr>
                            </table>
                        </form>
                    </td>
                </tr>
            </table>
            <form action="/lottery/list" method="post">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="14" class="default_line_td"></td>
                    </tr>
                    <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="14" class="error_msg_td">
                            <b><fmt:message key="${errorMsg}" bundle="${error}"/></b>
                        </td>
                    </tr>
                    </c:if>
                    <tr class="list_table_title_tr">
                        <td nowrap align="left" width="">抽奖ID</td>
                        <td nowrap align="left" width="80">抽奖名称</td>
                        <td nowrap align="left" width="200">抽奖描述</td>
                        <td nowrap align="left" width="80">抽奖基数</td>
                        <td nowrap align="left">总抽奖等级</td>
                        <td nowrap align="left" width="130">创建时间</td>
                        <td nowrap align="left" width="130">最后修改时间</td>
                        <td nowrap align="left" width="50">状态</td>
                        <td nowrap align="left">活动开始时间</td>
                        <td nowrap align="left">活动结束时间</td>
                        <td nowrap align="left" width="">操作</td>
                    </tr>
                    <tr>
                        <td height="1" colspan="14" class="default_line_td"></td>
                    </tr>
                    <c:choose>
                        <c:when test="${list.size() > 0}">
                            <c:forEach items="${list}" var="lottery" varStatus="st">
                                <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                    <td nowrap>
                                            ${lottery.lotteryId}
                                                <input type="hidden" name="lotteryid" value="${lottery.lotteryId}"/>
                                    </td>
                                    <td nowrap>
                                        
                                        ${lottery.lotteryName}
                                    </td>
                                    <td nowrap>${lottery.lotteryDesc}</td>
                                    <td nowrap>${lottery.baseRate}</td>
                                    <td nowrap>${lottery.awardLevelCount}</td>
                                    <td ><fmt:formatDate value="${lottery.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                    <td ><fmt:formatDate value="${lottery.lastModifyDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                    <td nowrap
                                        <c:choose>
                                            <c:when test="${lottery.validStatus.code eq 'valid'}">
                                                    style="color: #008000;"
                                            </c:when>
                                            <c:otherwise>
                                                 class="error_msg_td"
                                            </c:otherwise>
                                        </c:choose>>
                                        <fmt:message key="lottery.validstatus.${lottery.validStatus.code}" bundle="${def}"/>
                                    </td>
                                    <td ><fmt:formatDate value="${lottery.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                    <td ><fmt:formatDate value="${lottery.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                    <td nowrap>
                                        <c:choose>
                                            <c:when test="${lottery.validStatus.code eq 'valid'}">
                                                <p:privilege name="/lottery/modifypage">
                                                    <a href="/lottery/modifypage?lotteryid=${lottery.lotteryId}">编辑</a>
                                                </p:privilege>
                                                <p:privilege name="/lottery/delete">
                                                    <a href="/lottery/delete?lotteryid=${lottery.lotteryId}">删除</a>
                                                </p:privilege>
                                            </c:when>
                                            <c:otherwise>
                                                <p:privilege name="/lottery/recover">
                                                    <a href="/lottery/recover?lotteryid=${lottery.lotteryId}">恢复</a>
                                                </p:privilege>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                            <tr>
                                <td height="1" colspan="14" class="default_line_td"></td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="14" class="error_msg_td">暂无数据!</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    <tr>
                        <td colspan="14" height="1" class="default_line_td"></td>
                    </tr>
                    <c:if test="${page.maxPage > 1}">
                        <tr class="list_table_opp_tr">
                            <td colspan="14">
                                <pg:pager url="/lottery/list"
                                          items="${page.totalRows}" isOffset="true"
                                          maxPageItems="${page.pageSize}"
                                          export="offset, currentPageNumber=pageNumber" scope="request">
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