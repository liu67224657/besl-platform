<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>商品管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>

    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/dhtmlxcalendar.css">
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/skins/dhtmlxcalendar_dhx_skyblue.css">
    <script src="/static/include/dhtmlxcalendar/dhtmlxcalendar.js"></script>
    <script type="text/javascript">
        $().ready(function() {
            doOnLoad();
            $('#form_submit').submit(function() {
                var profileNameVal = $('#input_text_profilename').val();
                if (profileNameVal.length == 0) {
                    alert("请输入用户账号");
                    return false;
                }

                var dateFromVal = $('#input_text_startdate').val();
                if (dateFromVal.length == 0) {
                    alert("请输入起始时间");
                    return false;
                }

                var dateToVal = $('#input_text_enddate').val();
                if (dateToVal.length == 0) {
                    alert("请输入结束时间");
                    return false;
                }
            });
            var coustomSwfu = new SWFUpload(coustomImageSettings);
        });

        var myCalendar;
        function doOnLoad() {
            myCalendar = new dhtmlXCalendarObject(["input_text_startdate","input_text_enddate"]);
        }
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 礼包信息管理 >> 积分消费记录</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">记录列表</td>
                </tr>
            </table>
            <table>
                <tr>
                    <td>
                        <form action="/point/consumelog/list" method="post" id="form_submit">
                            <table>
                                <tr>
                                    <td height="1" class="default_line_td">
                                        按商城查询:
                                    </td>
                                    <td height="1">
                                        <select name="goodsactiontype">
                                            <option value=''>请选择</option>
                                            <c:forEach items="${shopTypes}" var="item">
                                                <option value="${item.code}" <c:if
                                                        test="${goodsActionType==item.code}">selected</c:if>>${item.name}</option>
                                            </c:forEach>
                                        </select>

                                    </td>
                                    <td height="1"></td>
                                    <td height="1" class="default_line_td">
                                        按发货状态查询:
                                    </td>
                                    <td height="1">
                                        <select name="validstatus">
                                            <option value=''>请选择</option>
                                            <option value="invalid" <c:if
                                                    test="${status=='invalid'}">selected</c:if>>未发货
                                            </option>
                                            <option value="valid" <c:if test="${status=='valid'}">selected</c:if>>已发货
                                            </option>

                                        </select>
                                    </td>
                                    <td height="1"></td>
                                    <%--<td height="1" class="default_line_td">--%>
                                    <%--结束时间:--%>
                                    <%--</td>--%>
                                    <%--<td height="1">--%>
                                    <%--<input type="text" style="width: 80;" name="enddate" id="input_text_enddate"--%>
                                    <%--value="<fmt:formatDate value="${enddate}" pattern="yyyy-MM-dd"/>"/>--%>
                                    <%--</td>--%>

                                    <td>
                                        <p:privilege name="/point/consumelog/list">
                                            <input type="submit" name="button" class="default_button" value="查询"/>
                                        </p:privilege>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </td>
                </tr>
            </table>
            <form action="/point/consumelog/list" method="post">
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
                        <td nowrap align="left" width="80">订单编号</td>
                        <td nowrap align="left" width="80">商品名称</td>
                        <td nowrap align="left" width="60">消费积分</td>
                        <td nowrap align="left" width="60">所属APP</td>
                        <td nowrap align="left" width="60">用户地址</td>

                        <td nowrap align="left" width="150">消费时间</td>
                        <td nowrap align="left" width="150">消费者IP</td>
                    </tr>
                    <tr>
                        <td height="1" colspan="11" class="default_line_td"></td>
                    </tr>
                    <c:choose>
                        <c:when test="${not empty rows}">
                            <c:forEach items="${rows}" var="consumelog" varStatus="st">
                                <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                    <td nowrap>${consumelog.consumeOrder}</td>
                                    <td nowrap>
                                        <input type="hidden" name="goodsid" value="${consumelog.goodsId}"/>
                                        <input type="hidden" name="goodsitemid" value="${consumelog.goodsItemId}"/>
                                            ${consumelog.goodsName}
                                    </td>
                                    <td nowrap>${consumelog.consumeAmount}</td>
                                    <td nowrap>

                                        <c:forEach var="app" items="${applist}">
                                            <c:if test="${consumelog.appkey == app.appId}">
                                                ${app.appName}
                                            </c:if>
                                        </c:forEach>
                                    </td>
                                    <td nowrap>
                                        联系人：${consumelog.address.contact}，电话：${consumelog.address.phone}，邮编：${consumelog.address.postcode}，<br/>详细地址：${consumelog.address.province}-${consumelog.address.city}-${consumelog.address.county}-${consumelog.address.address}
                                    </td>

                                    <td nowrap>
                                        <fmt:formatDate value="${consumelog.consumeDate}"
                                                        pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </td>
                                    <td nowrap>${consumelog.consumeIp}</td>
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
                                <c:set var="startDateStr">
                                    <fmt:formatDate value="${startdate}" pattern="yyyy-MM-dd"/>
                                </c:set>
                                <c:set var="endDateStr">
                                    <fmt:formatDate value="${enddate}" pattern="yyyy-MM-dd"/>
                                </c:set>
                                <pg:pager url="/point/consumelog/list"
                                          items="${page.totalRows}" isOffset="true"
                                          maxPageItems="${page.pageSize}"
                                          export="offset, currentPageNumber=pageNumber" scope="request">
                                    <pg:param name="profilename" value="${profilename}"/>
                                    <pg:param name="startdate" value="${startDateStr}"/>
                                    <pg:param name="enddate" value="${endDateStr}"/>
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