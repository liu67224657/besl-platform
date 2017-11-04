<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>积分墙app信息管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.min.js"></script>
    <script type="text/javascript">
        function deleteById(profileid,nickname) {
            var msg = "您确定要把呢称为  \"" + nickname + "\"  的用户从禁言表中删除吗？\n删除后,该用户可以正常评论.\n\n请确认！";
            if (confirm(msg) == true) {
                window.location.href = "/forign/content/forbid/delete?profileid=" + profileid;

            }
        }

    </script>

</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 客户服务 >> 内容审核 >> 评论禁言用户管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td"> 评论禁言用户管理</td>
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
                <c:if test="${fn:length(errorFK)>0}">
                    <tr>
                        <td height="1" colspan="10" class="error_msg_td">
                            <c:out value="${errorFK}" escapeXml="true"/>
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
                        <form action="/forign/content/forbid/list" method="post">
                            <table width="500px">
                                <tr>
                                    <td height="1" class="default_line_td" width="100px">
                                        用户的昵称: [暂不可模糊搜索]
                                    </td>
                                    <td height="1" class="edit_table_defaulttitle_td" width="50px">
                                        <input type="text" name="nickName" value="${nickName}"/>
                                    </td>

                                    <td height="1" class=>
                                    </td>

                                    <td width="50px">
                                        <input type="submit" name="button" value="查询"/>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </td>
                    <td>

                        <table>
                            <tr>
                                <td>
                                    <form method="post" id="create_app_form"
                                          action="/forign/content/forbid/createpage">

                                        <input type="submit" name="button" value="添加需要禁言的用户"/>
                                    </form>
                                </td>
                            </tr>
                        </table>

                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="6" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="left" width="">用户的profileid</td>
                    <td nowrap align="left" width="">用户的呢称</td>
                    <td nowrap align="left" width="">禁言开始时间</td>
                    <td nowrap align="left" width="">禁言结束时间</td>
                    <td nowrap align="left" width="">是否永久禁言</td>
                    <td nowrap align="center" width="">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="6" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${not empty list}">
                        <c:forEach items="${list}" var="item" varStatus="st">
                            <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap><c:out value="${item.profileid}" escapeXml="true"/></td>
                                <td nowrap><c:out value="${item.nickName}" escapeXml="true"/></td>
                                <td nowrap><fmt:formatDate value='${item.startTime}' pattern='yyyy-MM-dd HH:mm:ss'
                                                           type="both"/></td>



                                    <c:choose>
                                    <c:when test="${item.length==0}"><td nowrap>----------------------------</td><td nowrap>  永久禁言</td></c:when>
                                    <c:otherwise> <td nowrap><fmt:formatDate value='${item.endTime}' pattern='yyyy-MM-dd HH:mm:ss'
                                                                                               type="both"/></td><td nowrap>指定时间段禁言</td></c:otherwise>
                                </c:choose>

                                <td nowrap>
                                    <a href="/forign/content/forbid/modifypage?nickName=${item.nickName}&startTime=<fmt:formatDate value='${item.startTime}' pattern='yyyy-MM-dd HH:mm:ss'
                                                                                               type='both'/>&length=${item.length}">编辑</a>
                                    <a href="javascript:;" onclick="deleteById('${item.profileid}','${item.nickName}');" >删除</a>
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
                        <td colspan="10">
                            <pg:pager url="/forign/content/forbid/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="currentPageNumber" value="${page.curPage}"/>
                                <pg:param name="nickName" value="${nickName}"/>
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