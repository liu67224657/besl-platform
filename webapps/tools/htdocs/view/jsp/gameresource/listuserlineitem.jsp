<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台数据管理、Line查询列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/dhtmlxcalendar.css">
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/skins/dhtmlxcalendar_dhx_skyblue.css">
    <script src="/static/include/dhtmlxcalendar/dhtmlxcalendar.js"></script>
    <script src="/static/include/js/default.js" type="text/javascript"></script>
    <script src="/static/include/js/jquery.js" type="text/javascript"></script>
    <script language="JavaScript" type="text/JavaScript">

    </script>

</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="100%" valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr class="toolbar_tr">
                    <td>
                        <form action="/group/userlist" method="post">
                            用户昵称： <input type="text" name="screenName" value="${screenName}"/> &nbsp;&nbsp;&nbsp;
                            用户域名： <input type="text" name="domain" value="${domain}"/> &nbsp;&nbsp;&nbsp;
                            <input type="hidden" name="lineId" value="${line.lineId}"/>
                            <input type="hidden" name="uno" value="${uno}"/>
                            <input type="submit" value="查询"/>
                        </form>
                    </td>
                    <td align="right">
                        <c:if test="${profileBlog!=null}">
                        <form action="/group/removeitembyuser" method="post">
                            <input type="hidden" name="uno" value="${profileBlog.uno}"/>
                            <input type="hidden" name="lineId" value="${lineId}"/>
                            <input type="submit" value="删除所有"/>
                        </form>
                        </c:if>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="8" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td width="40" align="center">选择</td>
                    <td>内容预览</td>
                    <td width="60" align="center" nowrap="">是否置顶</td>
                    <td width="60" align="right" nowrap>排序值</td>
                    <td width="80" align="center" nowrap>是否有效</td>
                    <td width="100" align="center" nowrap>创建日期</td>
                    <td width="60" align="center" nowrap>操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="8" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${lineItems.size() > 0}">
                        <form action="/viewline/batchstatuslineitems" method="POST" name="batchform">
                            <input name="lineId" type="hidden" value="${line.lineId}"/>
                            <input name="pager.offset" type="hidden" value="${page.startRowIdx}"/>
                            <jsp:include
                                    page="/view/jsp/viewline/tab_listlineitem_content.jsp"></jsp:include>
                            <tr>
                                <td height="1" colspan="8" class="default_line_td"></td>
                            </tr>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="8" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td height="1" colspan="8" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="8">
                            <pg:pager url="/group/userlist"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="lineId" value="${line.lineId}"/>
                                <pg:param name="screenName" value="${screenName}"/>
                                <pg:param name="domain" value="${domain}"/>
                                <pg:param name="uno" value="${uno}"/>

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