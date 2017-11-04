<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>小端滚动信息列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script>
        $(document).ready(function() {
            $('#create_app_form').submit(function() {
                var appKey = $('#select_appkey').val();
                if (appKey.length == 0) {
                    alert('请选择一个APP');
                    return false;
                }

                $('#hidden_appkey').val(appKey);
            })
        })
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 小端APP >> 小端滚动列表</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">小端滚动信息列表</td>
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
                        <form action="/joymeapp/topnews/list" method="post">
                            <table width="400px">
                                <tr>
                                    <td height="1" class="default_line_td" width="200px">
                                        选择APP:
                                    </td>
                                    <td height="1" class="edit_table_defaulttitle_td">
                                        <select name="appkey" id="select_appkey">
                                            <option value="">请选择</option>
                                            <c:forEach var="app" items="${applist}">
                                                <option value="${app.appId}"
                                                        <c:if test="${app.appId==appkey}">selected</c:if> >${app.appName}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td height="1" class=>
                                    </td>
                                    <td>
                                        <input type="submit" name="button" value="查询"/>
                                    </td>
                                    <td>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </td>
                    <td>
                            <table>
                                <tr>
                                    <td>
                                        <form method="post" id="create_app_form" action="/joymeapp/topnews/createtopnewspage">
                                            <input type="hidden" id="hidden_appkey" name="appkey" value="${appkey}"/>
                                            <input type="submit" name="button" value="添加滚动新闻"/>
                                        </form>
                                    </td>
                                </tr>
                            </table>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="14" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="left" width="50">ID</td>
                    <td nowrap align="left" width="">标题</td>
                    <td nowrap align="left" width="">URL</td>
                    <td nowrap align="left" width="">APPKEY</td>
                    <td nowrap align="left" width="">状态</td>
                    <td nowrap align="left" width="100">创建时间</td>
                    <td nowrap align="left" width="100">修改时间</td>
                    <td nowrap align="left" width="100">创建人</td>
                    <td nowrap align="left" width="100">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="9" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="news" varStatus="st">
                            <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap>${news.top_news_id}</td>
                                <td nowrap>${news.title}</td>
                                <td nowrap>${news.url}</td>
                                <td nowrap>${news.appkey}</td>
                                <td nowrap>
                                    <c:if test="${news.removestatus.code=='n'}">未删除</c:if>
                                    <c:if test="${news.removestatus.code=='y'}">已删除</c:if>
                                </td>
                                <td nowrap>${news.createdate}</td>
                                <td nowrap>${news.modifydate}</td>
                                <td nowrap>${news.create_userid}</td>
                                <td nowrap>
                                    <a href="/joymeapp/topnews/modifypage?newsid=${news.top_news_id}&appkey=${appkey}">编辑</a>
                                    <c:choose>
                                        <c:when test="${news.removestatus.code!='n'}">
                                            <a href="/joymeapp/topnews/delete?newsid=${news.top_news_id}&appkey=${appkey}&removestatus=n">恢复</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="/joymeapp/topnews/delete?newsid=${news.top_news_id}&appkey=${appkey}&removestatus=y">删除</a>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="9" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="9" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="9" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="10">
                            <pg:pager url="/joymeapp/menu/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="appkey" value="${appkey}"/>
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