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
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/My97DatePicker/WdatePicker.js"></script>
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
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷抽奖 >> 奖品管理</td>
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
                        <form action="/lottery/award/list" method="post" id="form_select_lottery">
                            <table>
                                <tr>
                                    <td height="1" class="default_line_td" width="80">
                                        选择抽奖信息:
                                    </td>
                                    <td height="1">
                                        <select name="lotteryid" id="select_lotteryid">
                                            <option value="">请选择</option>
                                            <c:forEach var="lot" items="${lotteryList}">
                                                <option value="${lot.lotteryId}"
                                                        <c:if test="${lot.lotteryId==lotteryId}">selected="selected" </c:if>>${lot.lotteryName}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td>
                                        <p:privilege name="/lottery/award/list">
                                            <input type="submit" name="button" class="default_button" value="查询"/>
                                        </p:privilege>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </td>
                    <td>
                        <c:if test="${lotteryId>0}">
                            <form method="post" action="/lottery/award/createpage">
                                <table>
                                    <tr>
                                        <td>
                                            <input type="hidden" id="hidden_award_id" name="lotteryid"
                                                   value="${lottery.lotteryId}"/>
                                            <p:privilege name="/lottery/award/createpage">
                                            <input type="submit" name="button" class="default_button" value="添加奖品"/>
                                            </p:privilege>
                                        <td>
                                    <tr>
                                </table>
                            </form>
                        </c:if>
                    </td>
                </tr>
            </table>


            <form action="/lottery/award/list" method="post">
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
                        <td nowrap align="left" width="80">奖品名称</td>
                        <td nowrap align="left" width="200">奖品描述</td>
                        <td nowrap align="left" width="80">奖品等级</td>
                        <td nowrap align="left" width="80">奖品类型</td>

                        <td nowrap align="left" width="80">奖品总数</td>

                        <td nowrap align="left" width="130">开始时间</td>
                        <td nowrap align="left" width="130">结束时间</td>
                        <td nowrap align="left" width="50">状态</td>
                        <td nowrap align="left" width="">操作</td>
                    </tr>
                    <tr>
                        <td height="1" colspan="11" class="default_line_td"></td>
                    </tr>
                    <c:choose>
                        <c:when test="${list.size() > 0}">
                            <c:forEach items="${list}" var="award" varStatus="st">
                                <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                    <td nowrap>
                                        <input type="hidden" name="lotteryid" value="${award.lotteryId}"/>
                                        <input type="hidden" name="lotteryawardid" value="${award.lotteryAwardId}"/>
                                            ${award.lotteryAwardName}
                                    </td>
                                    <td nowrap>${award.lotteryAwardDesc}</td>
                                    <%--<td nowrap><img src="${award.lotteryAwardPic}" height="100" width="100"></td>--%>
                                    <td nowrap><fmt:message key="lottery.award.level.${award.lotteryAwardLevel}"
                                                            bundle="${def}"/></td>
                                    <td nowrap><fmt:message key="lottery.award.type.${award.lotteryAwardType.code}"
                                                            bundle="${def}"/></td>

                                    <td nowrap>${award.lotteryAwardAmount}</td>
                                    <td><fmt:formatDate value="${award.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                    <td><fmt:formatDate value="${award.lastModifyDate}"
                                        pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                    <td nowrap
                                            <c:choose>
                                                <c:when test="${award.validStatus.code eq 'valid'}">
                                                    style="color: #008000;"
                                                </c:when>
                                                <c:otherwise>
                                                    class="error_msg_td"
                                                </c:otherwise>
                                            </c:choose>>
                                        <fmt:message key="lottery.award.validstatus.${award.validStatus.code}"
                                                     bundle="${def}"/>
                                    </td>
                                    <td nowrap>
                                        <c:choose>
                                            <c:when test="${award.validStatus.code eq 'valid' || award.validStatus.code eq 'invalid'}">
                                                <p:privilege name="/lottery/award/modifypage">
                                                    <a href="/lottery/award/modifypage?lotteryawardid=${award.lotteryAwardId}&lotteryid=${award.lotteryId}">编辑</a>
                                                </p:privilege>
                                                <p:privilege name="/lottery/award/delete">
                                                    <a href="/lottery/award/delete?lotteryawardid=${award.lotteryAwardId}&lotteryid=${award.lotteryId}">删除</a>
                                                </p:privilege>
                                            </c:when>
                                            <c:otherwise>
                                                <p:privilege name="/lottery/award/recover">
                                                    <a href="/lottery/award/recover?lotteryawardid=${award.lotteryAwardId}&lotteryid=${award.lotteryId}">恢复</a>
                                                </p:privilege>
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
                        <td colspan="11" height="1" class="default_line_td"></td>
                    </tr>
                    <c:if test="${page.maxPage > 1}">
                        <tr class="list_table_opp_tr">
                            <td colspan="11">
                                <pg:pager url="/lottery/award/list"
                                          items="${page.totalRows}" isOffset="true"
                                          maxPageItems="${page.pageSize}"
                                          export="offset, currentPageNumber=pageNumber" scope="request">
                                    <pg:param name="lotteryid" value="${lotteryId}"/>
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