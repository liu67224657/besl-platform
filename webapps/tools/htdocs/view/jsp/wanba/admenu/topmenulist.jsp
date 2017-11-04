<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>推荐广告</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 玩霸-问答 >> 推荐广告</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">推荐广告</td>
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
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table>
                <tr>
                    <td>

                    </td>
                    <td>
                        <table>

                            </td>
                            <tr>
                                <td>

                                    <form action="/wanba/admenu/createpage" method="post">
                                        <input type="submit" name="button" class="default_button" value="添加热门广告位"/>
                                    </form>
                            </tr>

                        </table>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="8" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center" width="80">ID</td>
                    <td nowrap align="center" width="">菜单名称</td>
                    <td nowrap align="center" width="">链接</td>
                    <td nowrap align="center" width="">平台</td>
                    <td nowrap align="center" width="">位置</td>
                    <td nowrap align="center" width="">排序操作</td>
                    <td nowrap align="center" width="">状态</td>
                    <td nowrap align="center" width="">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="16" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="dto" varStatus="st">
                            <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap>${dto.activityTopMenuId}</td>

                                <td nowrap>${dto.menuName}</td>
                                <td nowrap>${dto.linkUrl}</td>
                                <td nowrap>
                                    <c:if test="${dto.platform==0}">
                                        IOS
                                    </c:if>
                                    <c:if test="${dto.platform==1}">
                                        Android
                                    </c:if>
                                </td>
                                <td nowrap>${dto.channelId}</td>
                                <td nowrap>
                                    <a href="/wanba/admenu/sort?sort=up&activitytopmenuid=${dto.activityTopMenuId}&channelid=${dto.channelId}"><img
                                            src="/static/images/icon/up.gif"></a>
                                    <a href="/wanba/admenu/sort?sort=down&activitytopmenuid=${dto.activityTopMenuId}&channelid=${dto.channelId}"><img
                                            src="/static/images/icon/down.gif"></a>
                                </td>
                                <td nowrap>
                                    <c:if test="${dto.validStatus.code=='valid'}">
                                        可用
                                    </c:if>
                                    <c:if test="${dto.validStatus.code=='invalid'}">
                                        不可用
                                    </c:if>
                                </td>

                                <td nowrap>
                                    <a href="/wanba/admenu/modifypage?activitytopmenuid=${dto.activityTopMenuId}">编辑</a>&nbsp;
                                </td>

                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="8" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="8" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="8" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="10">
                            <pg:pager url="/wanba/admenu/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
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