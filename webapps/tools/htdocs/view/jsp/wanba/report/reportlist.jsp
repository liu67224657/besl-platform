<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>举报后台</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $('a[name=delete-report]').click(function () {
                var reportId = $(this).attr('data-pid');
                var type = $(this).attr('data-type');
                var destid=$(this).attr('data-destid');
                if (confirm('删除操作不可逆,确定删除?')) {
                    ///wanba/report/modify?reportId=${report.reportId}&validStatus=removed
                    $.post("/wanba/report/modify", {reportId: reportId,validStatus:'removed',type:type,destId:destid}, function (req) {
                        window.location.reload();
                    })
                }
            });
        });
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 玩霸-举报 >> 举报</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">举报</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="10" class="error_msg_td">
                            <fmt:message key="${errorMsg}" bundle="${error}"/>
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td height="1">
                        <form action="/wanba/report/list" method="post">
                            <input type="text" name="title" placeholder="输入问题标题" value="${title}"/>
                            状态：
                            <select name="validStatus">
                                <option value="">全部</option>
                                <option value="valid" <c:if test="${validStatus=='valid'}">selected</c:if>>可用</option>
                                <option value="removed" <c:if test="${validStatus=='removed'}">selected</c:if>>删除</option>
                            </select>
                            <input type="submit" name="button" class="default_button" id="default_button1" value="搜索"/>
                        </form>
                    </td>
                </tr>
            </table>

            <table width="100%" border="0" cellspacing="1" cellpadding="0" style="table-layout: fixed">
                <tr>
                    <td height="1" colspan="6" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center" width="50px">ID</td>
                    <td nowrap align="center" width="200px" style="overflow:hidden;white-space:pre;" >标题</td>
                    <td nowrap align="center" width="500px" style="overflow:hidden;white-space:pre;" >回答内容</td>
                    <td nowrap align="center" width="50px">举报类型</td>
                    <td nowrap align="center" width="50px">状态</td>
                    <td nowrap align="center" width="50px">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="6" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="report" varStatus="st">
                            <tr id="tr_${report.reportId}"
                                class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap width="50px">${st.index+1}</td>
                                <c:if test="${not empty qMap}">
                                    <c:if test="${not empty qMap[report.destId]}">
                                            <c:set var="questionId" value="${qMap[report.destId].questionId}"/>
                                    </c:if>
                                </c:if>
                                <c:if test="${empty questionId}">
                                    <c:if test="${not empty aMap[report.destId]}">
                                        <c:set var="questionId" value="${aMap[report.destId].questionId}"/>
                                    </c:if>
                                </c:if>

                                <td nowrap  width="200px" style="overflow:hidden;white-space:pre;" title="${report.extstr}"><a href="/wanba/ask/answer/list?qid=${questionId}">${report.extstr}</a></td>
                                <c:if test="${report.itemType.code==2}">
                                    <c:if test="${aMap[report.destId]!=null}">
                                        <c:if test="${aMap[report.destId].body!=null}">
                                            <c:set value="${aMap[report.destId].body.text}" var="answertext"></c:set>
                                        </c:if>
                                    </c:if>
                                    <c:if test="${empty aMap[report.destId]}">
                                        <c:set value="《答案已被删除》" var="answertext"></c:set>
                                    </c:if>
                                </c:if>

                                <td nowrap  width="200px" style="overflow:hidden;white-space:pre;" title="<c:out value="${answertext}"/>"><c:if test="${report.itemType.code==2}"><c:if test="${not empty answertext}"><c:out value="${answertext}"></c:out></c:if></c:if></td>
                                <td nowrap width="50px" align="center">
                                    <c:if test="${report.itemType.code==1}">问题</c:if>
                                    <c:if test="${report.itemType.code==2}">答案</c:if>
                                </td>
                                <td nowrap width="50px" align="center">
                                    <c:if test="${report.validStatus.code=='valid'}">可用</c:if>
                                    <c:if test="${report.validStatus.code=='removed'}">删除</c:if>
                                </td>
                                <td nowrap width="50px" align="center">
                                    <c:if test="${report.validStatus.code=='valid'}">
                                        <a href="javascript:void(0);" name="delete-report" data-pid="${report.reportId}" data-type="${report.itemType.code}" data-destid="${report.destId}">删除</a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="6" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="6" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="6" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="6">
                            <pg:pager url="/wanba/report/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                <pg:param name="items" value="${page.totalRows}"/>
                                <pg:param name="validStatus" value="${validStatus}"/>
                                <pg:param name="title" value="${title}"/>
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