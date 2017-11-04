<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>定时后台</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {

        });
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 玩霸-举报 >> 定时列表</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">定时列表：定时任务有一定的延时，大概10分钟</td>
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
                        <form action="/wanba/release/list" method="post">
                            <input type="text" name="title" placeholder="输入标题" value="${title}"/>
                            类型：
                            <select name="type">
                                <option value="">全部</option>
                                <option value="1" <c:if test="${type==1}">selected</c:if>>抢答</option>
                                <option value="2" <c:if test="${type==2}">selected</c:if>>活动</option>
                            </select>
                            状态：
                            <select name="validStatus">
                                <option value="">全部</option>
                                <option value="valid" <c:if test="${validStatus=='valid'}">selected</c:if>>进行中</option>
                                <option value="invalid" <c:if test="${validStatus=='invalid'}">selected</c:if>>已删除</option>
                                <option value="removed" <c:if test="${validStatus=='removed'}">selected</c:if>>已发布</option>
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
                    <td nowrap align="center" >标题</td>
                    <td nowrap align="center" >类型</td>
                    <td nowrap align="center" >状态</td>
                    <td nowrap align="center" width="50px">定时发布时间</td>
                    <td nowrap align="center" width="50px">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="6" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="report" varStatus="st">
                            <tr id="tr_${report.timeid}"
                                class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap width="50px">${st.index+1}</td>
                                <td nowrap >${report.title}</td>
                                <td nowrap >
                                    <c:if test="${report.timeRelseaseType.code==1}">抢答</c:if>
                                    <c:if test="${report.timeRelseaseType.code==2}">活动</c:if>
                                </td>

                                <td nowrap >
                                    <c:if test="${report.validStatus.code=='valid'}">进行中</c:if>
                                    <c:if test="${report.validStatus.code=='invalid'}">已删除</c:if>
                                    <c:if test="${report.validStatus.code=='removed'}">已发布</c:if>
                                </td>
                                <td nowrap ><fmt:formatDate value="${report.releaseTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                <td nowrap width="50px">
                                    <c:if test="${report.timeRelseaseType.code==1 && report.validStatus.code=='valid'}">
                                        <a href="/wanba/release/qmodifypage?timeid=${report.timeid}">编辑</a>
                                    </c:if>
                                    <c:if test="${report.timeRelseaseType.code==2 && report.validStatus.code=='valid'}">
                                        <a href="/wanba/release/modifypage?timeid=${report.timeid}">编辑</a>
                                    </c:if>

                                    <c:if test="${report.validStatus.code=='valid'}">
                                    &nbsp;&nbsp;<a href="/wanba/release/modify?timeid=${report.timeid}&validstatus=invalid">删除</a>
                                    </c:if>
                                    <c:if test="${report.validStatus.code=='invalid'}">
                                        &nbsp;&nbsp;&nbsp;&nbsp; <a href="/wanba/release/modify?timeid=${report.timeid}&validstatus=valid">恢复</a>
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
                            <pg:pager url="/wanba/release/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                <pg:param name="items" value="${page.totalRows}"/>
                                <pg:param name="validStatus" value="${validStatus}"/>
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