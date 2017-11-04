<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台数据管理、活动文章列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script language="JavaScript" type="text/JavaScript">
    </script>
</head>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 社交端管理 >> 社交端活动文章管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">>活动文章列表</td>
                </tr>
            </table>
            <table width="50%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td width="80">
                        <a href="/joymeapp/social/activity/list"><input name="Reset" type="button"
                                                                        class="default_button" value="返回"></a>
                    </td>
                    <td width="80" align="right" class="edit_table_defaulttitle_td">活动ID：</td>
                    <td width="80">${activity.activityId}</td>
                    <td width="80" align="right" class="edit_table_defaulttitle_td">标题：</td>
                    <td>${activity.title}</td>
                </tr>
            </table>
            <br/>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="14" class="error_msg_td">${errorMsg}</td>
                    </tr>
                </c:if>
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <form action="/joymeapp/social/activity/content/list" method="post" id="form_submit_search">
                    <tr>
                        <td height="1" colspan="14" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td width="80" align="center">搜索条件</td>
                        <td>
                            <input type="hidden" name="aid" value="${activity.activityId}"/>
                        </td>
                        <td>
                            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                                <tr>
                                    <td width="80" align="right" class="edit_table_defaulttitle_td">文章ID：</td>
                                    <td>
                                        <input name="cid" type="text" size="8" value="${contentId}"/>
                                    </td>
                                    <td width="80" align="right" class="edit_table_defaulttitle_td">文章作者：</td>
                                    <td>
                                        <input name="screenname" type="text" size="24" value="${screenName}"/>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td width="80" align="center">
                            <input name="Button" type="submit" class="default_button" value=" 搜索 ">
                        </td>
                    </tr>
                </form>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="14" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center" width="">文章ID</td>
                    <td nowrap align="center" width="">标题</td>
                    <td nowrap align="center" width="">缩略图</td>
                    <td nowrap align="center" width="">排序</td>
                    <td nowrap align="center" width="">作者</td>
                    <td nowrap align="center" width="">操作</td>
                    <td nowrap align="center" width="">创建信息</td>
                    <td nowrap align="center" width="">修改信息</td>
                </tr>
                <tr>
                    <td height="1" colspan="14" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="dto" varStatus="st">
                            <tr class="<c:choose><c:when test="
                            ${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                <td nowrap>${dto.contentId}</td>
                <td nowrap><a href="/joymeapp/socialclient/content/getbyid?cid=${dto.contentId}"
                              target="_blank">${dto.body}</a></td>
                <td nowrap><a href="/joymeapp/socialclient/content/getbyid?cid=${dto.contentId}"
                              target="_blank"><img src="${dto.pic}" height="100"
                                                   width="100"/></a>
                </td>
                <td nowrap align="center">
                    <c:if test="${fn:length(screenName) <= 0 && contentId == null}">
                        <a href="/joymeapp/social/activity/content/sort/up?aid=${dto.activityId}&cid=${dto.contentId}&pager.offset=${page.startRowIdx}">
                            <img src="/static/images/icon/up.gif"></a>
                        <a href="/joymeapp/social/activity/content/sort/down?aid=${dto.activityId}&cid=${dto.contentId}&pager.offset=${page.startRowIdx}">
                            <img src="/static/images/icon/down.gif"></a>
                    </c:if>
                </td>
                <td nowrap>
                    <c:forEach items="${profileList}" var="profile">
                        <c:if test="${profile.uno==dto.uno}">${profile.blog.screenName}</c:if>
                    </c:forEach>
                </td>
                <td nowrap>
                    <c:choose>
                        <c:when test="${dto.displayOrder > 0}">
                            <p:privilege name="/joymeapp/social/activity/content/top">
                                <a href="/joymeapp/social/activity/content/top?aid=${dto.activityId}&cid=${dto.contentId}&pager.offset=${page.startRowIdx}">置顶</a>
                                &nbsp;
                                <a href="/joymeapp/social/activity/content/sink?aid=${dto.activityId}&cid=${dto.contentId}&pager.offset=${page.startRowIdx}">沉底</a>
                                &nbsp;
                                <a href="/joymeapp/social/activity/content/float?aid=${dto.activityId}&cid=${dto.contentId}&pager.offset=${page.startRowIdx}">上浮</a>
                            </p:privilege>
                        </c:when>
                        <c:when test="${dto.displayOrder < 0}">
                            <p:privilege name="/joymeapp/social/activity/content/untop">
                                <a href="/joymeapp/social/activity/content/untop?aid=${dto.activityId}&cid=${dto.contentId}&pager.offset=${page.startRowIdx}">取消置顶</a>
                                &nbsp;
                                <a href="/joymeapp/social/activity/content/sink?aid=${dto.activityId}&cid=${dto.contentId}&pager.offset=${page.startRowIdx}">沉底</a>
                                <%--&nbsp;--%>
                                <%--<a href="/joymeapp/social/activity/content/float?aid=${dto.activityId}&cid=${dto.contentId}&pager.offset=${page.startRowIdx}">上浮</a>--%>
                            </p:privilege>
                        </c:when>
                    </c:choose>

                </td>
                <td nowrap>${dto.createDate}</td>
                <td nowrap>${dto.modifyDate}<br/>${dto.modifyIp}<br/>${dto.modifyUserId}</td>

                </tr>
                </c:forEach>
                <tr>
                    <td height="1" colspan="16" class="default_line_td"></td>
                </tr>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="16" class="error_msg_td">暂无数据!</td>
                    </tr>
                </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="14" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="14">
                            <LABEL>
                                <pg:pager url="/joymeapp/social/activity/content/list"
                                          items="${page.totalRows}" isOffset="true"
                                          maxPageItems="${page.pageSize}"
                                          export="offset, currentPageNumber=pageNumber" scope="request">
                                    <pg:param name="aid" value="${activityId}"/>
                                    <pg:param name="screenname" value="${screenName}"/>
                                    <pg:param name="contentId" value="${contentId}"/>
                                    <pg:param name="currentPageNumber" value="${page.curPage}"/>
                                    <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                    <pg:param name="items" value="${page.totalRows}"/>
                                    <%@ include file="/WEB-INF/jsp/toolspg.jsp" %>
                                </pg:pager>
                            </LABEL>
                        </td>
                    </tr>
                </c:if>
            </table>
        </td>
    </tr>
</table>
</body>
</html>