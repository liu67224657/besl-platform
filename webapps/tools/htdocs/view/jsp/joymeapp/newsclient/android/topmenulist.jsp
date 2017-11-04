<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>活动轮播图</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷APP >> 着迷新闻端Android首页轮播图</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">轮播图列表</td>
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
                        <form action="/joymeapp/newsclient/topmenu/android/list" method="post">
                            <table width="100%">
                                <tr>
                                    <td height="1" class="default_line_td">
                                        选择渠道:
                                    </td>
                                    <td height="1">
                                        <select name="channelid" id="select_channel">
                                            <option value="">请选择</option>
                                            <c:forEach items="${channelList}" var="channel">
                                                <option value="${channel.channelId}"
                                                        <c:if test="${channel.channelId==channelId}">selected="selected"</c:if>>${channel.channelName}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td>
                                        <input type="submit" name="button" class="default_button" value="查询"/>
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
                                    <form action="/joymeapp/newsclient/topmenu/android/createpage" method="post">
                                        <input type="submit" name="button" class="default_button" value="添加轮播图"/>
                                    </form>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="16" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center" width="80">ID</td>

                    <td nowrap align="center" width="">菜单名称</td>
                    <td nowrap align="center" width="">地址URL</td>
                    <td nowrap align="center" width="">菜单图片</td>
                    <td nowrap align="center" width="">描述</td>
                    <td nowrap align="center" width="">平台</td>
                    <td nowrap align="center" width="">渠道</td>
                    <td nowrap align="center" width="">菜单类型</td>
                    <td nowrap align="center" width="">是否是new</td>
                    <td nowrap align="center" width="">是否是hot</td>
                    <td nowrap align="center" width="">排序操作</td>
                    <td nowrap align="center" width="">状态</td>
                    <td nowrap align="center" width="">创建人/创建IP</td>
                    <td nowrap align="center" width="">修改人/修改IP</td>
                    <td nowrap align="center" width="">操作</td>
                    <td nowrap align="center" width="">APPKEY</td>
                </tr>
                <tr>
                    <td height="1" colspan="16" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="dto" varStatus="st">
                            <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap>${dto.activityTopMenu.activityTopMenuId}</td>

                                <td nowrap>${dto.activityTopMenu.menuName}</td>
                                <td nowrap>${dto.activityTopMenu.linkUrl}</td>
                                <td nowrap><img width="120" height="30" src="${dto.activityTopMenu.picUrl}"/></td>
                                <td nowrap>${dto.activityTopMenu.menuDesc}</td>
                                <td nowrap><fmt:message key="client.platform.${dto.activityTopMenu.platform}"
                                                        bundle="${def}"/></td>
                                <td nowrap>${dto.appChannel.channelName}</td>
                                <td nowrap><fmt:message key="client.item.redirect.${dto.activityTopMenu.redirectType}"
                                                        bundle="${def}"/></td>
                                <td nowrap><fmt:message key="joymeapp.menu.status.${dto.activityTopMenu.isNew}"
                                                        bundle="${def}"/></td>
                                <td nowrap><fmt:message key="joymeapp.menu.status.${dto.activityTopMenu.isHot}"
                                                        bundle="${def}"/></td>
                                <td nowrap>
                                    <a href="/joymeapp/newsclient/topmenu/android/sort/up?activitytopmenuid=${dto.activityTopMenu.activityTopMenuId}"><img
                                            src="/static/images/icon/up.gif"></a>
                                    <a href="/joymeapp/newsclient/topmenu/android/sort/down?activitytopmenuid=${dto.activityTopMenu.activityTopMenuId}"><img
                                            src="/static/images/icon/down.gif"></a>
                                </td>
                                <td nowrap>
                                    <c:choose>
                                        <c:when test="${dto.activityTopMenu.validStatus.code eq 'valid'}">
                                            <span style="color: #008000;">可用</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span style="color: #ff0000;">删除</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td nowrap>${dto.activityTopMenu.createUserId}/${dto.activityTopMenu.createIp}</td>
                                <td nowrap>${dto.activityTopMenu.lastModifyUserId}/${dto.activityTopMenu.lastModifyIp}</td>
                                <td nowrap>
                                    <a href="/joymeapp/newsclient/topmenu/android/modifypage?activitytopmenuid=${dto.activityTopMenu.activityTopMenuId}">编辑</a>&nbsp;
                                    <c:choose>
                                        <c:when test="${dto.activityTopMenu.validStatus.code eq 'valid'}">
                                            <a href="/joymeapp/newsclient/topmenu/android/delete?activitytopmenuid=${dto.activityTopMenu.activityTopMenuId}">删除</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="/joymeapp/newsclient/topmenu/android/recover?activitytopmenuid=${dto.activityTopMenu.activityTopMenuId}">恢复</a>
                                        </c:otherwise>
                                    </c:choose>

                                </td>
                                <td nowrap>${dto.activityTopMenu.appKey}</td>
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
                    <td colspan="16" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="10">
                            <pg:pager url="/joymeapp/newsclient/topmenu/android/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="channelid" value="${channelId}"/>
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