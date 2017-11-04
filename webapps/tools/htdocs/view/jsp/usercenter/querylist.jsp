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
            if (starttime != "" && endtime != "") {
                var stimelong = convertDateFromString(starttime).getTime();
                var endtimelong = convertDateFromString(endtime).getTime();
                if (stimelong > endtimelong) {
                    alert("开始时间不能大于结束时间");
                    return false;
                }
                if ((endtimelong - stimelong) >= (60 * 60 * 24 * 31 * 1000)) {
                    alert("最多查询31天的信息");
                    return false;
                }
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
                    <td class="list_table_header_td">用户信息</td>
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
            <form action="/userinfo/querylist" onsubmit="return querylist();" method="post">
                <table width="800px;" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="13" class="">
                            昵称：<input type="text" name="nick" value="${nick}"/>&nbsp;
                            状态：
                            <select name="status">
                                <option value="">全部</option>
                                <option value="0" <c:if test="${status eq 0}">selected</c:if>>正常</option>
                                <option value="1" <c:if test="${status eq 1}">selected</c:if>>禁用</option>
                            </select>&nbsp;
                            注册日期：
                            <input type="text" class="Wdate"
                                   onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'endtime\')||\'new Date()\'}'})"
                                   readonly="readonly" name="starttime" value="${starttime}" id="starttime"/>--
                            <input type="text" class="Wdate"
                                   onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'starttime\')}',maxDate:'%y-%M-%d'})"
                                   readonly="readonly" name="endtime" value="${endtime}" id="endtime"/>
                            <input type="submit" value="查询"/><br/>
                            <%--<span style="color:red;">*不填写查询条件时，默认展示一个月数据</span>--%>


                        </td>
                    </tr>
                    <c:if test="${fn:length(errorMsg)>0}">
                        <tr>
                            <td height="1" colspan="13" class="error_msg_td">
                                <b><fmt:message key="${errorMsg}" bundle="${error}"/></b>
                            </td>
                        </tr>
                    </c:if>
                    <tr class="list_table_title_tr">
                        <td nowrap align="center">昵称</td>
                        <td nowrap align="center">状态</td>
                        <td nowrap align="center">注册日期</td>
                        <td nowrap align="center">操作</td>
                    </tr>
                    <tr>
                        <td height="1" colspan="13" class="default_line_td"></td>
                    </tr>
                    <c:choose>
                        <c:when test="${not empty list}">
                            <c:forEach items="${list}" var="line" varStatus="st">
                                <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                    <td nowrap align="center">${line.nick}</td>
                                    <td nowrap align="center">
                                        <c:choose>
                                            <c:when test="${not empty blackMap[line.profileId]}">
                                                禁用
                                            </c:when>
                                            <c:otherwise>
                                                正常
                                            </c:otherwise>
                                        </c:choose>

                                    </td>
                                    <td nowrap align="center">
                                        <fmt:formatDate value="${line.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </td>
                                    <td nowrap align="center">
                                        <a href="/userinfo/black/info?pid=${line.profileId}">封禁</a>&nbsp;
                                        <a href="/userinfo/detail?pid=${line.profileId}">详情</a>
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
                                <pg:pager url="/userinfo/querylist"
                                          items="${page.totalRows}" isOffset="true"
                                          maxPageItems="${page.pageSize}"
                                          export="offset, currentPageNumber=pageNumber" scope="request">
                                    <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                    <pg:param name="items" value="${page.totalRows}"/>
                                    <pg:param name="nick" value="${nick}"/>
                                    <pg:param name="status" value="${status}"/>
                                    <pg:param name="starttime" value="${starttime}"/>
                                    <pg:param name="endtime" value="${endtime}"/>
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