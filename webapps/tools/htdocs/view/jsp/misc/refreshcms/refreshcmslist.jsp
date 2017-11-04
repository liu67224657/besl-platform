<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>CMS定时发布相关需求</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>


    <script language="JavaScript" type="text/JavaScript">


    </script>
</head>
<body>

<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> CMS定时发布</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <form action="/misc/refreshcms/list" method="post">
                    <tr>
                        <td height="1" class="">
                            <select name="remove_status">
                                <option value="n" <c:if test="${remove_status=='n'}">selected</c:if>>有效</option>
                                <option value="y" <c:if test="${remove_status=='y'}">selected</c:if>>无效</option>
                            </select>
                            <input type="submit" name="button" class="default_button" value="查询">
                        </td>
                    </tr>
                </form>


            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="">
                        <form action="/misc/refreshcms/createpage" method="post">
                            <input type="submit" name="button" class="default_button" value="新增定时任务">
                        </form>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="9" class="error_msg_td">
                            <fmt:message key="${errorMsg}" bundle="${error}"/>
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td height="1" colspan="9" class="default_line_td"></td>
                </tr>


                <tr class="list_table_title_tr">
                    <td nowrap width="">刷新ID</td>
                    <td nowrap width="">栏目ID</td>
                    <td nowrap width="">栏目名称</td>
                    <td nowrap width="">刷新类型</td>
                    <td nowrap width="">发布时间</td>
                    <td nowrap width="">状态</td>
                    <td nowrap width="">修改人</td>
                    <td nowrap width="">修改时间</td>
                    <td nowrap width="">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="9" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="interflow" varStatus="st">
                            <tr
                            <c:choose><c:when test="${st.index % 2 == 0}">
                                class="list_table_opp_tr"</c:when><c:otherwise>
                                class="list_table_even_tr"</c:otherwise></c:choose>>
                            <td nowrap>${interflow.time_id}</td>
                            <td nowrap>${interflow.cms_id}</td>
                            <td nowrap>${interflow.cms_name}</td>
                            <td nowrap>
                                <c:if test="${interflow.refreshReleaseType.code==1}">
                                    单次
                                </c:if>
                                <c:if test="${interflow.refreshReleaseType.code==2}">
                                    每天
                                </c:if>
                            </td>
                            <td nowrap>
                                <c:if test="${interflow.refreshReleaseType.code==1}">
                                    <fmt:formatDate value="${date:long2date(interflow.release_time)}"
                                                    pattern="yyyy-MM-dd HH:mm"/>
                                </c:if>
                                <c:if test="${interflow.refreshReleaseType.code==2}">
                                    <fmt:formatDate value="${date:long2date(interflow.release_time)}" pattern="HH:mm"/>
                                </c:if>

                            </td>
                            <td nowrap>
                                <c:if test="${interflow.remove_status.code=='n'}">
                                    有效
                                </c:if>
                                <c:if test="${interflow.remove_status.code=='y'}">
                                    无效
                                </c:if>
                            </td>
                            <td nowrap>${interflow.modify_user}</td>
                            <td nowrap>${interflow.modify_time}</td>
                            <td nowrap><a href="/misc/refreshcms/modifypage?time_id=${interflow.time_id}">编辑</a></td>
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
                        <td colspan="9">
                            <pg:pager url="/misc/refreshcms/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                <pg:param name="items" value="${page.totalRows}"/>
                                <pg:param name="remove_status" value="${remove_status}"/>
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