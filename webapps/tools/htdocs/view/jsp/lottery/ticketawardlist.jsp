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
        $(document).ready(function() {
            $('#form_select_lottery').submit(function() {
                var lotteryId = $('#select_lotteryid').val();
                if (lotteryId.length == 0) {
                    alert('请选择抽奖');
                    return false;
                }
            });
        });
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 条目管理 >> 彩票奖品管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">奖品列表</td>
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
                        <form action="/ticket/award/list" method="post" id="form_select_lottery">
                            <table>
                                <tr>
                                    <td height="1" class="default_line_td" width="80">
                                        选择彩票信息:
                                    </td>
                                    <td height="1">
                                        <select name="ticketId" id="select_ticketid">
                                            <option value="">请选择</option>
                                            <c:forEach var="tic" items="${ticket}">
                                                <option value="${tic.ticketId}"
                                                        <c:if test="${tic.ticketId==ticketId}">selected="selected" </c:if>>${tic.ticketName}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td>
                                        <input type="submit" name="button" class="default_button" value="查询"/>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </td>
                    <td>
                        <c:if test="${ticketId>0}">
                            <form method="post" action="/ticket/award/createpage">
                                <table>
                                    <tr>
                                        <td>
                                            <input type="hidden" id="hidden_award_id" name="ticketId"
                                                   value="${ticketId}"/>
                                            <input type="submit" name="button" class="default_button" value="添加奖品"/>
                                        <td>
                                    <tr>
                                </table>
                            </form>
                        </c:if>
                    </td>
                </tr>
            </table>
            <form action="/ticket/award/list" method="post">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="13" class="default_line_td"></td>
                    </tr>
                    <c:if test="${fn:length(errorMsg)>0}">
                        <tr>
                            <td height="1" colspan="13" class="error_msg_td">
                                <b><fmt:message key="${errorMsg}" bundle="${error}"/></b>
                            </td>
                        </tr>
                    </c:if>
                    <tr class="list_table_title_tr">
                        <td nowrap align="left">奖品名称</td>
                        <td nowrap align="left" width="250">奖品描述</td>
                        <td nowrap align="left">奖品图片</td>
                        <td nowrap align="left">奖品等级</td>
                        <td nowrap align="left">奖品总数</td>
                        <td nowrap align="left">剩余数量</td>
                        <td nowrap align="left">创建人/创建IP</td>
                        <td nowrap align="left">最后修改人/最后修改时间</td>
                        <td nowrap align="left">状态</td>
                        <td nowrap align="left">操作</td>
                    </tr>
                    <tr>
                        <td height="1" colspan="13" class="default_line_td"></td>
                    </tr>
                    <c:choose>
                        <c:when test="${list.size() > 0}">
                            <c:forEach items="${list}" var="app" varStatus="st">
                                <tr align="center"
                                    class="<c:choose><c:when test="
                                ${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                    <td nowrap align="left" name="">
                                            ${app.awardName}
                                    </td>

                                    <td nowrap align="left">
                                            ${app.awardDesc}
                                    </td>
                                    <td nowrap align="left">
                                        <img src="${app.awardPic}" height="50"/>
                                    </td>
                                    <td nowrap>
                                        <fmt:message key="lottery.award.level.${app.awardLevel}"
                                                     bundle="${def}"/>
                                    </td>
                                    <td nowrap>
                                            ${app.awardCount}
                                    </td>
                                    <td nowrap>
                                            ${app.currentCount}
                                    </td>
                                    <td nowrap>
                                            ${app.createUserId}/${app.createIp}
                                    </td>
                                    <td nowrap>
                                            ${app.lastModifyUserId}/${app.lastModifyIp}
                                    </td>
                                    <td nowrap>
                                        <c:choose>
                                            <c:when test="${app.validStatus.code=='valid'}">
                                                <span style="color:#5aba3a;"><fmt:message
                                                        key="lottery.award.validstatus.${app.validStatus.code}"
                                                        bundle="${def}"/> </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span style="color:red"><fmt:message
                                                        key="lottery.award.validstatus.${app.validStatus.code}"
                                                        bundle="${def}"/> </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td nowrap>
                                        <a href="/ticket/award/modifypage?ticketAwardId=${app.ticketAwardId}">编辑</a>

                                        <c:choose>
                                            <c:when test="${app.validStatus.code=='valid'}">
                                                <a href="/ticket/award/delete?ticketAwardId=${app.ticketAwardId}&ticketId=${ticketId}">删除</a>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="/ticket/award/recover?ticketAwardId=${app.ticketAwardId}&ticketId=${ticketId}">恢复</a>
                                            </c:otherwise>
                                        </c:choose>

                                    </td>

                                </tr>
                            </c:forEach>
                            <tr>
                                <td height="1" colspan="11" class="default_line_td"></td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="11" class="error_msg_td">暂无数据!</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    <tr>
                        <td colspan="13" height="1" class="default_line_td"></td>
                    </tr>
                    <c:if test="${page.maxPage > 1}">
                        <tr class="list_table_opp_tr">
                            <td colspan="13">
                                <pg:pager url="/ticket/award/list"
                                          items="${page.totalRows}" isOffset="true"
                                          maxPageItems="${page.pageSize}"
                                          export="offset, currentPageNumber=pageNumber" scope="request">
                                    <pg:param name="ticketId" value="${ticketId}"/>
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