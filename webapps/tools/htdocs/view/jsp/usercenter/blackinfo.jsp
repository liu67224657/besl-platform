<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>用户信息</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/My97DatePicker/WdatePicker.js"></script>

    <script>
        $().ready(function () {
            $('#form_submit').bind('submit', function () {


            });
        });
        var date = new Date();
        Date.prototype.format = function (format) {
            var o = {
                "M+": this.getMonth() + 1, //month
                "d+": this.getDate(),    //day
                "h+": this.getHours(),   //hour
                "m+": this.getMinutes(), //minute
                "s+": this.getSeconds(), //second
                "q+": Math.floor((this.getMonth() + 3) / 3),  //quarter
                "S": this.getMilliseconds() //millisecond
            }
            if (/(y+)/.test(format)) format = format.replace(RegExp.$1,
                    (this.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var k in o)if (new RegExp("(" + k + ")").test(format))
                format = format.replace(RegExp.$1,
                        RegExp.$1.length == 1 ? o[k] :
                                ("00" + o[k]).substr(("" + o[k]).length));
            return format;
        }
        function convertDateFromString(dateString) {
            if (dateString) {
                var date = new Date(dateString.replace(/-/, "/"))

                return date;
            }
        }

        function querylist() {
            var starttime = $("input[name='starttime']").val();
            var endtime = $("input[name='endtime']").val();
            var reason = $("[name='reason']").val();
            if (starttime == '' || endtime == '') {
                alert("开始/结束时间不能为空");
                return false;
            }
            if (reason == '') {
                alert("请填写封禁原因");
                return false;
            }
            if (reason.length > 20) {
                alert("封禁原因限20个字符");
                return false;
            }
            if (starttime != "" && endtime != "") {
                var stimelong = convertDateFromString(starttime).getTime();
                var endtimelong = convertDateFromString(endtime).getTime();
                if (stimelong > endtimelong) {
                    alert("开始时间不能大于结束时间");
                    return false;
                } else if (stimelong == endtimelong) {
                    alert("开始时间不能等于结束时间");
                    return false;
                }
            }
        }

        function removeBlack() {
            if (!confirm("是否解封该用户？")) {
                return false;
            }
        }
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 用户信息</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">
                        <c:choose>
                            <c:when test="${empty info}">
                                封禁
                            </c:when>
                            <c:otherwise>
                                解禁
                            </c:otherwise>
                        </c:choose>

                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="13" class="error_msg_td">
                                ${errorMsg}
                        </td>
                    </tr>
                </c:if>
            </table>
            <c:choose>
                <c:when test="${not empty info}">
                    <form action="/userinfo/black/remove" onsubmit="return removeBlack();" method="post">

                        <table width="800px;" border="0" cellspacing="1" cellpadding="0">
                            <tr>
                                <td height="1" colspan="13" class="">
                                    <input type="hidden" value="${pid}" name="pid"/>

                                    封禁开始时间：
                                    <fmt:formatDate value="${info.startTime}" pattern="yyyy-MM-dd"/>
                                    封禁结束时间：
                                    <fmt:formatDate value="${info.endTime}" pattern="yyyy-MM-dd"/>

                                    <br/>
                                    <br/>
                                    *封禁原因：
                                        ${info.reason}<br/>
                                    <br/>
                                    <input type="submit" value="手动解禁"/>
                                    <input name="Reset" type="button" class="default_button" value="返回"
                                           onclick="window.location.href='/userinfo/querylist';">
                                    <br/>
                                </td>
                            </tr>
                        </table>
                    </form>
                </c:when>
                <c:otherwise>
                    <form action="/userinfo/black/add" onsubmit="return querylist();" method="post">
                        <table width="800px;" border="0" cellspacing="1" cellpadding="0">
                            <tr>
                                <td height="1" colspan="13" class="">
                                    <input type="hidden" value="${pid}" name="pid"/>
                                    封禁开始时间：
                                    <input type="text" class="Wdate"
                                           onFocus="WdatePicker({dateFmt:'yyyy-MM-dd 00:00:00',maxDate:'%y-%M-%d'})"
                                           readonly="readonly" name="starttime" value="${starttime}" id="starttime"/>
                                    封禁结束时间：
                                    <input type="text" class="Wdate"
                                           onFocus="WdatePicker({dateFmt:'yyyy-MM-dd 00:00:00',minDate:'#F{$dp.$D(\'starttime\')}'})"
                                           readonly="readonly" name="endtime" value="${endtime}" id="endtime"/>
                                    <br/>
                                    <br/>
                                    *封禁原因：
                                    <textarea cols="50" rows="5" name="reason"></textarea><br/>
                                    <br/>
                                    <input type="submit" value="提交"/>
                                    <input name="Reset" type="button" class="default_button" value="返回"
                                           onclick="window.location.href='/userinfo/querylist';">
                                    <br/>
                                </td>
                            </tr>
                        </table>
                    </form>

                </c:otherwise>
            </c:choose>
            <table width="800px;" border="0" cellspacing="1" cellpadding="0">

                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="13" class="error_msg_td">
                            <b><fmt:message key="${errorMsg}" bundle="${error}"/></b>
                        </td>
                    </tr>
                </c:if>
                <tr class="list_table_title_tr">
                    <td nowrap align="center">封禁开始时间</td>
                    <td nowrap align="center">封禁结束时间</td>
                    <td nowrap align="center">封禁原因</td>
                </tr>
                <tr>
                    <td height="1" colspan="13" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${not empty list}">
                        <c:forEach items="${list}" var="line" varStatus="st">
                            <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap align="center">
                                    <fmt:formatDate value="${line.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </td>
                                <td nowrap align="center">
                                    <c:if test="${!(st.index==0&&not empty info)}">
                                        <fmt:formatDate value="${line.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/></c:if>
                                </td>
                                <td nowrap align="center">
                                        ${line.reason}
                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="13" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="13" height="1" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="13" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="13">
                            <pg:pager url="/userinfo/black/info"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                <pg:param name="items" value="${page.totalRows}"/>
                                <pg:param name="pid" value="${pid}"/>
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