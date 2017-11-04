<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>贴吧、QQ帐号管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="/static/include/My97DatePicker/WdatePicker.js"></script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 微信公众号 >> 微信公众号用户消息列表</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="20%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">用户消息列表</td>
                </tr>
            </table>
            <table width="1000" border="0" cellspacing="0" cellpadding="0">
                <form action="/misc/weixin/message/list" method="post" id="form_submit_search">
                    <tr>
                        <td width="80" align="center">搜索条件</td>
                        <td>时间：</td>
                        <td>
                            <input type="text" class="Wdate"
                                   onClick="WdatePicker({autoPickDate:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"
                                   readonly="readonly" name="starttime" id="starttime" value="${startTime}"/>--
                            <input type="text" class="Wdate"
                                   onClick="WdatePicker({autoPickDate:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"
                                   readonly="readonly" name="endtime" id="endtime" value="${endTime}"/>
                        </td>
                        <td width="80" align="center">
                            <input name="Button" type="submit" class="default_button" value=" 搜索 ">
                        </td>
                    </tr>
                </form>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="900" border="0" cellspacing="0" cellpadding="0" height="1">
                <tr>
                    <td height="1" width="600">
                        <a href="/misc/weixin/message/outexcel?starttime=${startTime}&endtime=${endTime}"><input type="button" value="导出excel" class="default_button"/></a>
                        <span>*若非Chrome、ie浏览器无，请换Chrome或ie试一试，推荐Chrome。</span>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="19" class="error_msg_td">
                            <fmt:message key="${errorMsg}" bundle="${error}"/>
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td height="1" colspan="19" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap width="">内容</td>
                    <td nowrap width="">昵称</td>
                    <td nowrap width="">城市</td>
                    <td nowrap width="">省份</td>
                    <td nowrap width="">国家</td>
                    <td nowrap width="">时间</td>
                </tr>
                <tr>
                    <td height="1" colspan="19" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="dto" varStatus="st">
                            <tr
                                    <c:choose><c:when test="${st.index % 2 == 0}">class="list_table_opp_tr"</c:when>
                                        <c:otherwise>class="list_table_even_tr"</c:otherwise>
                            </c:choose>>
                                <td nowrap>${dto.content}</td>
                                <td nowrap>${dto.nickname}</td>
                                <td nowrap>${dto.city}</td>
                                <td nowrap>${dto.province}</td>
                                <td nowrap>${dto.country}</td>
                                <td nowrap>
                                    <fmt:formatDate value="${dto.messageDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="19" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="19" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="19" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="19">
                            <pg:pager url="/misc/weixin/message/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="starttime" value="${startTime}"/>
                                <pg:param name="endtime" value="${endTime}"/>
                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                <pg:param name="items" value="${page.totalRows}"/>
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