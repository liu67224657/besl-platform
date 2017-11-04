<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
            $('#form_submit').submit(function(){
//                var profileNameVal=$('#input_text_profilename').val();
//                if(profileNameVal.length==0){
//                    alert("请输入用户账号");
//                    return false;
//                }
//
//                var dateFromVal=$('#input_text_startdate').val();
//                if(dateFromVal.length==0){
//                    alert("请输入起始时间");
//                    return false;
//                }
//
//                var dateToVal=$('#input_text_enddate').val();
//                if(dateToVal.length==0){
//                    alert("请输入结束时间");
//                    return false;
//                }
            });
            var coustomSwfu= new SWFUpload(coustomImageSettings);
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
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷抽奖 >> 用户抽奖记录</td>
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
                        <form action="/lottery/log/list" method="post" id="form_submit">
                            <table>
                                <tr>
                                    <td height="1" class="default_line_td">
                                        选择抽奖活动:
                                    </td>
                                    <td height="1">
                                        <select name="lid">
                                            <c:forEach items="${lotteryList}" var="lottery">
                                                <option value="${lottery.lotteryId}" <c:if test="${lotteryId == lottery.lotteryId}">selected="selected"</c:if>>${lottery.lotteryName}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td height="1" class="default_line_td">
                                        输入用户账号:
                                    </td>
                                    <td height="1">
                                        <input type="text" style="width: 80;" name="profilename" id="input_text_profilename" value="${profilename}"/>
                                    </td>
                                    <td height="1"></td>
                                    <td height="1" class="default_line_td">
                                        起始时间:
                                    </td>
                                    <td height="1">
                                        <input type="text" style="width: 80;" name="startdate" id="input_text_startdate" value="<fmt:formatDate value="${from}" pattern="yyyy-MM-dd"/>"/>
                                    </td>
                                    <td height="1"></td>
                                    <td height="1" class="default_line_td">
                                        结束时间:
                                    </td>
                                    <td height="1">
                                        <input type="text" style="width: 80;" name="enddate" id="input_text_enddate" value="<fmt:formatDate value="${to}" pattern="yyyy-MM-dd"/>"/>
                                    </td>

                                    <td>
                                        <p:privilege name="/lottery/log/list">
                                            <input type="submit" name="button" class="default_button" value="查询"/>
                                        </p:privilege>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </td>
                </tr>
            </table>
            <form action="/lottery/log/list" method="post">
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
                        <td nowrap align="left" width="80">ID</td></td>
                        <td nowrap align="left" width="80">抽奖信息</td>
                        <td nowrap align="left" width="80">奖品名称</td>
                        <%--<td nowrap align="left" width="300">奖品描述</td>--%>
                        <%--<td nowrap align="left" width="100">奖品图片</td>--%>
                        <td nowrap align="left" width="60">奖品等级</td>
                        <td nowrap align="left" width="80">抽奖人</td>
                        <td nowrap align="left" width="60">子奖品名1</td>
                        <td nowrap align="left" width="60">子奖品值1</td>
                        <td nowrap align="left" width="60">子奖品名2</td>
                        <td nowrap align="left" width="60">子奖品值2</td>
                        <td nowrap align="left" width="150">抽奖时间</td>
                        <td nowrap align="left" width="150">抽奖人IP</td>
                    </tr>
                    <tr>
                        <td height="1" colspan="11" class="default_line_td"></td>
                    </tr>
                    <c:choose>
                        <c:when test="${list.size() > 0}">
                            <c:forEach items="${list}" var="log" varStatus="st">
                                <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                    <td nowrap>${log.userLotteryId}</td>
                                    <td nowrap>
                                        <c:forEach items="${lotteryList}" var="lottery">
                                            <c:if test="${lottery.lotteryId==log.lotteryId}">
                                                ${lottery.lotteryName}
                                            </c:if>
                                        </c:forEach>
                                    </td>
                                    <td nowrap>${log.lotteryAwardName}</td>
                                    <%--<td nowrap>${log.lotteryAwardDesc}</td>--%>
                                    <%--<td nowrap><img width="100" height="100" id="img_pic" src="${log.lotteryAwardPic}"/></td>--%>
                                    <td nowrap>${log.lotteryAwardLevel}</td>
                                    <td nowrap>${log.screenName}</td>
                                    <td nowrap>${log.name1}</td>
                                    <td nowrap>${log.value1}</td>
                                    <td nowrap>${log.name2}</td>
                                    <td nowrap>${log.value2}</td>
                                    <td nowrap>
                                       <fmt:formatDate value="${log.lotteryDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </td>
                                    <td nowrap>${log.lotteryIp}</td>
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
                                <pg:pager url="/lottery/log/list"
                                          items="${page.totalRows}" isOffset="true"
                                          maxPageItems="${page.pageSize}"
                                          export="offset, currentPageNumber=pageNumber" scope="request">
                                    <pg:param name="profilename" value="${profilename}"/>
                                    <pg:param name="lid" value="${lotteryId}"/>
                                    <pg:param name="startdate" value="${from}"/>
                                    <pg:param name="enddate" value="${to}"/>
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