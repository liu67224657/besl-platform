<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>任务组列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script>
    </script>
</head>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <c:if test="${appkey == '17yfn24TFexGybOF0PqjdY'}">
            <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷手游画报管理 >> 玩霸任务管理</td>
        </c:if>
        <c:if test="${appkey == '08pkvrWvx5ArJNvhYf19kN'}">
            <td height="22" class="page_navigation_td">>> 运营维护 >> app任务管理 >> 优酷合作任务管理</td>
        </c:if>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">>任务组列表</td>
                </tr>
            </table>
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
            <table width="50%" border="0" cellspacing="0" cellpadding="0">
                <form action="/task/group/${appkey}" method="post" id="form_submit_search">
                    <tr>
                        <td width="80" align="center">搜索条件</td>
                        <td>
                            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                                <tr>
                                    <%--<td width="200" align="right" class="edit_table_defaulttitle_td">选择appkey平台：</td>--%>
                                    <%--<td width="200">--%>
                                        <%--<select name="appkey">--%>
                                            <%--<option value="">请选择</option>--%>
                                            <%--<c:forEach items="${appKeys}" var="ak">--%>
                                                <%--<option value="${ak.appId}"--%>
                                                        <%--<c:if test="${ak.appId == appkey}">selected</c:if>>${ak.appName}</option>--%>
                                            <%--</c:forEach>--%>
                                        <%--</select>--%>
                                    <%--</td>--%>
                                    <td width="200" align="right" class="edit_table_defaulttitle_td">选择平台：</td>
                                    <td>
                                        <select name="platform">
                                            <option value="">请选择</option>
                                            <c:forEach items="${platforms}" var="platformCode">
                                                <option value="${platformCode}"
                                                        <c:if test="${platformCode == platform}">selected</c:if> >
                                                    <fmt:message key="joymeapp.platform.${platformCode}"
                                                                 bundle="${def}"/>
                                                </option>
                                            </c:forEach>
                                        </select>
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
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td>
                        <form method="post" action="/task/group/createpage">
                            <input name="appkey" hidden="${appkey}">
                            <table>
                                <tr>
                                    <td>
                                        <input type="submit" name="button" class="default_button" value="添加任务组"/>
                                    <td>
                                        <%--      <td>
                                                  <span style="color:red"> dailytasks :《日常任务》; gameclient.sign:《签到》; newbietasks:《新手任务》; 这三个名称不要改变,新创建的任务组也不要包含这些 标识符</span>
                                              </td>--%>
                                <tr>
                            </table>
                        </form>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="10" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center" width="50">任务组ID</td>
                    <td nowrap align="center">名称</td>
                    <td nowrap align="center">APPKEY</td>
                    <td nowrap align="center">平台</td>
                    <td nowrap align="center">任务组类型</td>
                    <td nowrap align="center">排序</td>
                    <td nowrap align="center">任务都完成后分组在任务中心的显示情况</td>
                    <%--   <td nowrap align="center">任务组奖励（暂时无用）</td>--%>
                    <td nowrap align="center">状态</td>
                    <td nowrap align="center">操作</td>
                    <td nowrap align="center">创建人信息</td>
                    <td nowrap align="center">修改人信息</td>
                </tr>
                <tr>
                    <td height="1" colspan="17" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${not empty list}">
                        <c:forEach items="${list}" var="group" varStatus="st">
                            <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap align="center">${group.taskGroupId}</td>
                                <td nowrap align="center"><a
                                        href="/task/list?groupid=${group.taskGroupId}">${group.taskGroupName}</a>
                                </td>
                                <td nowrap align="center">${group.appKey}</td>
                                <td nowrap align="center"><fmt:message key="joymeapp.platform.${group.appPlatform.code}"
                                                                       bundle="${def}"/>
                                </td>
                                <td nowrap align="center"><fmt:message key="task.taskgroup.type.${group.type.code}"
                                                                       bundle="${def}"/></td>
                                <td nowrap align="center">
                                    <a href="/task/group/sort/up?groupid=${group.taskGroupId}&appkey=${appkey}&platform=${platform}">
                                        <img src="/static/images/icon/up.gif"/>
                                    </a>
                                    <a href="/task/group/sort/down?groupid=${group.taskGroupId}&appkey=${appkey}&platform=${platform}">
                                        <img src="/static/images/icon/down.gif"/>
                                    </a>
                                </td>
                                <td nowrap align="center"><c:choose><c:when
                                        test="${group.type.code=='sign'}">------------------</c:when><c:otherwise>
                                    <fmt:message key="task.taskgroup.showtype.${group.showtype.code}"  bundle="${def}"/></c:otherwise></c:choose></td>
                                <td nowrap <c:choose>
                                    <c:when test="${group.removeStatus.code eq 'n'}">style="color: #008000;" </c:when>
                                    <c:otherwise>style="color: #ff0000;"</c:otherwise>
                                </c:choose>>
                                    <fmt:message key="joyme.task.status.${group.removeStatus.code}" bundle="${def}"/>
                                </td>
                                <td nowrap>
                                    <a href="/task/group/modifypage?groupid=${group.taskGroupId}">编辑</a>
                                    &nbsp;
                                    <c:choose>
                                        <c:when test="${group.removeStatus.code eq 'n'}">
                                            <a href="/task/group/remove?groupid=${group.taskGroupId}">删除</a>
                                        </c:when>
                                        <c:when test="${task.removeStatus.code eq 'ing'}">
                                            <a href="/task/group/publish?groupid=${group.taskGroupId}">发布</a>
                                            <a href="/task/group/remove?groupid=${group.taskGroupId}">删除</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="/task/group/recover?groupid=${group.taskGroupId}">恢复</a>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td nowrap align="center">
                                        ${group.createUserId}/<fmt:formatDate value="${group.createTime}"
                                                                              pattern="yyyy-MM-dd HH:mm:ss"/>
                                </td>
                                <td nowrap align="center">
                                        ${group.modifyUserId}/<fmt:formatDate value="${group.modifyTime}"
                                                                              pattern="yyyy-MM-dd HH:mm:ss"/>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="17" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="17" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="17" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="17">
                            <LABEL>
                                <pg:pager url="/task/group/${appkey}"
                                          items="${page.totalRows}" isOffset="true"
                                          maxPageItems="${page.pageSize}"
                                          export="offset, currentPageNumber=pageNumber" scope="request">
                                    <pg:param name="appkey" value="${appkey}"/>
                                    <pg:param name="platform" value="${platform}"/>
                                    <pg:param name="currentPageNumber" value="${page.curPage}"/>
                                    <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                    <pg:param name="items" value="${page.totalRows}"/>
                                    <%@ include file="/WEB-INF/jsp/toolspgnoincludejquery.jsp" %>
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