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

    <script type="text/javascript" src="/static/include/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript">
        $().ready(function() {
            $('#form_submit').submit(function() {
                var dateFromVal = $('#input_text_startdate').val();
                if (dateFromVal == "") {
                    alert("请输入起始时间");
                    return false;
                }

                var dateToVal = $('#input_text_enddate').val();
                if (dateToVal == "") {
                    alert("请输入结束时间");
                    return false;
                }
            });
            var coustomSwfu = new SWFUpload(coustomImageSettings);
        });


    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 礼包信息管理 >> 领号礼包兑换记录</td>
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
                        <form action="/point/exchangelog/list" method="post" id="form_submit">
                            <table>
                                <tr>
                                    <td height="1" class="default_line_td">
                                        起始时间:
                                    </td>
                                    <td height="1">
                                        <input type="text" class="Wdate"
                                               onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',vel:'startdate',autoPickDate:true})"
                                               readonly="readonly" name="startdate" id="input_text_startdate"
                                               value="<fmt:formatDate value='${startdate}' pattern='yyyy-MM-dd HH:mm:ss'/>"/>

                                    </td>
                                    <td height="1"></td>
                                    <td height="1" class="default_line_td">
                                        结束时间:
                                    </td>
                                    <td height="1">

                                        <input type="text" class="Wdate"
                                               onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',vel:'enddate',autoPickDate:true})"
                                               readonly="readonly" name="enddate" id="input_text_enddate"
                                               value="<fmt:formatDate value='${enddate}' pattern='yyyy-MM-dd HH:mm:ss'/>"/>
                                    </td>

                                    <td>
                                        <p:privilege name="/point/exchangelog/list">
                                            <input type="submit" name="button" class="default_button" value="查询"/>
                                        </p:privilege>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </td>
                </tr>
            </table>
            <form action="/point/exchangelog/list" method="post">
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
                    <tr class="list_table_title_tr" height="30">
                        <td nowrap align="left" width="80">商品名称</td>
                        <td nowrap align="left" width="100">礼品名1</td>
                        <td nowrap align="left" width="100">礼品值1</td>
                        <td nowrap align="left" width="100">礼品名2</td>
                        <td nowrap align="left" width="100">礼品值2</td>
                        <td nowrap align="left" width="100">APP</td>
                        <td nowrap align="left" width="150">消耗积分</td>
                        <td nowrap align="left" width="60">领取方式</td>
                        <td nowrap align="left" width="60">领取类型</td>
                        <td nowrap align="left" width="150">领取日期</td>
                        <td nowrap align="left" width="150">领取者IP</td>
                    </tr>
                    <tr>
                        <td height="1" colspan="11" class="default_line_td"></td>
                    </tr>
                    <c:choose>
                        <c:when test="${list.size() > 0}">
                            <c:forEach items="${list}" var="consumelog" varStatus="st">
                                <tr class="<c:choose><c:when test='${st.index % 2 == 0}'>list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                    <td nowrap>
                                        <input type="hidden" name="goodsid" value="${consumelog.goodsId}"/>
                                        <input type="hidden" name="goodsitemid" value="${consumelog.goodsItemId}"/>
                                            ${consumelog.goodsName}
                                    </td>
                                    <td nowrap>${consumelog.snName1}</td>
                                    <td nowrap>${consumelog.snValue1}</td>
                                    <td nowrap>${consumelog.snName2}</td>
                                    <td nowrap>${consumelog.snValue2}</td>
                                    <td nowrap><c:forEach var="app" items="${applist}">
                                        <c:if test="${app.appId==consumelog.appkey}">
                                            ${app.appName}
                                        </c:if>
                                    </c:forEach>
                                    </td>
                                    <td nowrap>${consumelog.exchangePoint}</td>
                                    <td nowrap>${consumelog.exchangeDomain.code}</td>
                                    <td nowrap><fmt:message key="user.exchange.type.${consumelog.exchangeType.code}"
                                                            bundle="${def}"/></td>
                                    <td nowrap>
                                            ${consumelog.exhangeTime}
                                    </td>
                                    <td nowrap>${consumelog.exchangeIp}</td>
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
                        <c:set var="startDateStr">
                            <fmt:formatDate value="${startdate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                        </c:set>
                        <c:set var="endDateStr">
                            <fmt:formatDate value='${enddate}' pattern='yyyy-MM-dd HH:mm:ss'/>
                        </c:set>
                        <tr class="list_table_opp_tr">
                            <td colspan="11">
                                <pg:pager url="/point/exchangelog/list"
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