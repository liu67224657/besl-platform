<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>奖品管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $('#submit-scan').submit(function () {
                var lotteryId = $('#select_lotteryid').val();
                if (lotteryId.length == 0) {
                    alert('请选择抽奖');
                    return false;
                }

                var awardId = $('#select_lotteryawardid').val();
                if (awardId.length == 0) {
                    alert('请选择奖品');
                    return false;
                }

                if (lotteryId.length > 0) {
                    $('#input_hidden_lotteryid').val(lotteryId);
                }
            });
        });
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷抽奖 >> 奖品库存管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">礼品列表</td>
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
                        <form action="/lottery/awarditem/list" method="post" id="submit-scan">
                            <table width="100%">
                                <tr>
                                    <td height="1" class="default_line_td" width="80">
                                        选择抽奖信息:
                                    </td>
                                    <td height="1">
                                        <select name="lotteryid" id="select_lotteryid"
                                                onchange="javascript:document.getElementById('submit-scan').submit();">
                                            <option value="">请选择</option>
                                            <c:forEach var="lottery" items="${lotteryList}">
                                                <option value="${lottery.lotteryId}"
                                                        <c:if test="${lottery.lotteryId==lotteryId}">selected="selected" </c:if>>${lottery.lotteryName}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td height="1" class="default_line_td" width="60">
                                        选择奖品:
                                    </td>
                                    <td height="1">
                                        <select name="lotteryawardid" id="select_lotteryawardid">
                                            <option value="">请选择</option>
                                            <c:forEach var="award" items="${awardList}">
                                                <option value="${award.lotteryAwardId}"
                                                        <c:if test="${award.lotteryAwardId==lotteryAwardId}">selected="selected" </c:if>>${award.lotteryAwardName}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td>
                                        <p:privilege name="/lottery/awarditem/list">
                                            <input type="submit" name="button" class="default_button" value="查询"/>
                                        </p:privilege>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </td>
                    <td>
                        <c:if test="${lotteryAwardId>0}">
                            <form method="post" action="/lottery/awarditem/createpage" id="form_createawarditem">
                                <table>
                                    <tr>
                                        <td>
                                            <input type="hidden" id="hidden_lottery_id" name="lotteryid"
                                                   value="${lotteryId}"/>
                                            <input type="hidden" id="hidden_award_id" name="lotteryawardid"
                                                   value="${lotteryAwardId}"/>
                                            <p:privilege name="/lottery/awarditem/createpage">
                                                <input type="submit" name="button" class="default_button" value="批量添加"/>
                                            </p:privilege>
                                        <td>
                                    <tr>
                                </table>
                            </form>
                        </c:if>
                    </td>
                </tr>
            </table>
            <form action="/lottery/awarditem/list" method="post">
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
                        <td nowrap align="left" width="100">礼品名1</td>
                        <td nowrap align="left" width="100">礼品值1</td>
                        <td nowrap align="left" width="100">礼品名2</td>
                        <td nowrap align="left" width="100">礼品值2</td>
                        <td nowrap align="left" width="80">兑换状态</td>
                        <td nowrap align="left" width="100">兑换人</td>
                        <td nowrap align="left" width="150">兑换日期</td>
                        <td nowrap align="left" width="">操作</td>
                    </tr>
                    <tr>
                        <td height="1" colspan="13" class="default_line_td"></td>
                    </tr>
                    <c:choose>
                        <c:when test="${list.size() > 0}">
                            <c:forEach items="${list}" var="awarditem" varStatus="st">
                                <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                    <td nowrap>
                                        <input type="hidden" name="awarditemid"
                                               value="${awarditem.lotteryAwardItemId}"/>
                                        <input type="hidden" name="awardid" value="${awarditem.lotteryAwardId}"/>
                                        <input type="hidden" name="lid" id="input_hidden_lotteryid"/>
                                            ${awarditem.name1}
                                    </td>
                                    <td nowrap>${awarditem.value1}</td>
                                    <td nowrap>${awarditem.name2}</td>
                                    <td nowrap>${awarditem.value2}</td>
                                    <td nowrap
                                            <c:choose>
                                                <c:when test="${awarditem.lotteryStatus.code eq 'invalid'}">
                                                    class="error_msg_td"
                                                </c:when>
                                                <c:when test="${awarditem.lotteryStatus.code eq 'valid'}">
                                                    style="color: #008000;"
                                                </c:when>
                                            </c:choose>>
                                        <fmt:message
                                                key="lottery.award.item.lotterystatus.${awarditem.lotteryStatus.code}"
                                                bundle="${def}"/>
                                    </td>
                                    <td nowrap>
                                        <c:if test="${profileList.size() > 0}">
                                            <c:forEach items="${profileList}" var="profile">
                                                <c:if test="${profile.uno == awarditem.ownUserNo}">${profile.blog.screenName}</c:if>
                                            </c:forEach>
                                        </c:if>
                                    </td>
                                    <td nowrap><fmt:formatDate value="${awarditem.lotteryDate}"
                                                               pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                    <td nowrap>
                                        <a href="/lottery/awarditem/received?awarditemid=${awarditem.lotteryAwardItemId}&lid=${lotteryId}&awardid=${lotteryAwardId}">已领取</a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <tr>
                                <td height="1" colspan="13" class="default_line_td"></td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="13" class="error_msg_td">暂无数据!</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    <tr>
                        <td colspan="13" height="1" class="default_line_td"></td>
                    </tr>
                    <c:if test="${page.maxPage > 1}">
                        <tr class="list_table_opp_tr">
                            <td colspan="13">
                                <pg:pager url="/lottery/awarditem/list"
                                          items="${page.totalRows}" isOffset="true"
                                          maxPageItems="${page.pageSize}"
                                          export="offset, currentPageNumber=pageNumber" scope="request">
                                    <pg:param name="lotteryid" value="${lotteryId}"/>
                                    <pg:param name="lotteryawardid" value="${lotteryAwardId}"/>
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