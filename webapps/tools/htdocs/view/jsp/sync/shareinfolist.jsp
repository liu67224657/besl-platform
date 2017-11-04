<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>分享管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript">

    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷分享 >> 分享信息</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">信息列表</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="11" class="error_msg_td">
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
                        <form action="/sync/shareinfo/list" method="post">
                            <table width="400px">
                                <tr>
                                    <td height="1" class="default_line_td" width="200px">
                                        选择分享类型:
                                    </td>
                                    <td height="1" class="edit_table_defaulttitle_td">
                                        <select name="sharetype">
                                            <option value="">请选择</option>
                                            <c:forEach var="sharetype" items="${shareTypeCollection}">
                                                <option value="${sharetype.code}"
                                                        <c:if test="${sharetype.code==stype}">selected</c:if>>
                                                    <fmt:message key="share.type.${sharetype.code}" bundle="${def}"/></option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td>
                                        <p:privilege name="/sync/shareinfo/list">
                                        <input type="submit" name="button" value="查询"/>
                                        </p:privilege>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </td>
                    <td>
                        <form method="post" action="/sync/shareinfo/createpage">
                            <table>
                                 <tr>
                                     <td>
                                         <input type="hidden" name="shareid" value="${baseInfo.shareId}"/>
                                         <p:privilege name="/sync/shareinfo/createpage">
                                            <input type="submit" name="button" value="添加分享信息"/>
                                         </p:privilege>
                                     <td>
                                 <tr>
                            </table>
                        </form>
                    </td>
                </tr>
            </table>
            <form action="/sync/shareinfo/list" method="post">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="11" class="default_line_td"></td>
                    </tr>
                    <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="11" class="error_msg_td">
                            <b><fmt:message key="${errorMsg}" bundle="${error}"/></b>
                        </td>
                    </tr>
                    </c:if>
                    <tr class="list_table_title_tr">
                        <td nowrap align="left" width="80">分享ID</td>
                        <td nowrap align="left" width="150">分享KEY</td>
                        <td nowrap align="left" width="300">分享URL</td>
                        <td nowrap width="80">过期时间</td>
                        <td nowrap align="left" width="60">分享类型</td>
                        <td nowrap align="center" width="60">奖励类型</td>
                         <td nowrap align="center" width="100">创建时间</td>
                        <td nowrap align="center" width="100">创建人ID</td>
                        <td nowrap align="center" width="100">创建人IP</td>
                        <td nowrap align="center" width="40">状态</td>
                        <td nowrap align="center" width="60">奖励积分</td>
                        <td nowrap align="center" width="80">奖励的ID</td>
                        <td nowrap align="center" width="">操作</td>
                    </tr>
                    <tr>
                        <td height="1" colspan="11" class="default_line_td"></td>
                    </tr>
                    <c:choose>
                        <c:when test="${list.size() > 0}">
                            <c:forEach items="${list}" var="info" varStatus="st">
                                <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                    <td name="shareid">${info.shareId}</td>
                                    <td name="shareid">${info.shareKey}</td>
                                    <td>${info.shareSource}</td>
                                    <td nowrap>${info.expireDate}</td>
                                    <td nowrap><fmt:message key="share.type.${info.shareType.code}" bundle="${def}"/></td>
                                    <td nowrap>
                                        <c:if test="${info.shareRewardType.hasPoint()}">积分</c:if>
                                        <c:if test="${info.shareRewardType.hasLottery()}">抽奖</c:if>
                                    </td>
                                    <td nowrap>
                                       <fmt:formatDate value="${info.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </td>
                                    <td nowrap>${info.createUserId}</td>
                                    <td nowrap>${info.createUserIp}</td>
                                    <td nowrap id="td_info_removestatus" <c:if test="${info.removeStatus.code eq 'y'}">class="error_msg_td"</c:if>
                                            <c:if test="${info.removeStatus.code eq 'n'}">style="color: #008000;" </c:if>>
                                        <fmt:message key="share.removestatus.${info.removeStatus.code}" bundle="${def}"/>
                                    </td>
                                    <td nowrap>${info.shareRewardPoint}</td>
                                    <td nowrap>${info.shareRewardId}</td>
                                    <td nowrap>
                                        <c:if test="${info.removeStatus.code eq 'n'}">
                                            <p:privilege name="/sync/shareinfo/modifypage">
                                                <a href="/sync/shareinfo/modifypage?shareid=${info.shareId}">编辑</a>
                                            </p:privilege>
                                            <p:privilege name="/sync/shareinfo/delete">
                                                <a href="/sync/shareinfo/delete?shareid=${info.shareId}&sharetype=${stype}">删除</a>
                                            </p:privilege>
                                        </c:if>
                                        <c:if test="${info.removeStatus.code eq 'y'}">
                                            <p:privilege name="sync/shareinfo/recover">
                                                <a href="/sync/shareinfo/recover?shareid=${info.shareId}&sharetype=${stype}">恢复</a>
                                            </p:privilege>
                                        </c:if>
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
                        <td colspan="11" height="1" class="default_line_td"></td>
                    </tr>
                    <c:if test="${page.maxPage > 1}">
                        <tr class="list_table_opp_tr">
                            <td colspan="11">
                                <pg:pager url="/sync/shareinfo/list"
                                          items="${page.totalRows}" isOffset="true"
                                          maxPageItems="${page.pageSize}"
                                          export="offset, currentPageNumber=pageNumber" scope="request">
                                    <pg:param name="sharetype" value="${stype}"/>
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