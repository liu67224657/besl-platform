<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>着迷APP版本信息</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script>
        $(document).ready(function(){
             $('#create_app_form').submit(function(){
                 var appKey=$('#select_appkey').val();
                 if(appKey.length==0){
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
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷APP >> APP版本内容信息</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">APP版本信息管理</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="11" class="error_msg_td">
                            <fmt:message key="${errorMsg}" bundle="${error}"/>
                        </td>
                    </tr>
                </c:if>

                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table><tr><td>
                  <form action="/joymeapp/updatecontent/list" method="post">
                                <table width="400px">
                                    <tr>
                                        <td height="1" class="default_line_td" width="200px">
                                            选择平台:
                                        </td>
                                        <td height="1" class="edit_table_defaulttitle_td">
                                            <select name="appkey" id="select_appkey">
                                                <option value="">请选择</option>
                                                <c:forEach var="app" items="${applist}">
                                                    <option value="${app.appId}"
                                                            <c:if test="${app.appId==appKey}">selected</c:if> >${app.appName}</option>
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
                                </td><td>
                          <c:if test="${fn:length(appKey)>0}">
                            <table>
                                <tr>
                                    <td>

                                                <form method="post" id="create_app_form" action="/joymeapp/updatecontent/createpage">
                                                    <input type="hidden" id="hidden_appkey" name="appkey"  value="${appKey}"/>
                                                    <input type="submit" name="button" value="添加APP版本"/>
                                                </form>

                                    </td>
                                </tr>
                            </table>
              </c:if>
                </td></tr></table>
            <form action="/joymeapp/updatecontent/list" method="post">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="11" class="default_line_td"></td>
                    </tr>
                    <tr class="list_table_title_tr">
                        <td nowrap align="left" width="5%">appid</td>
                        <td nowrap align="left" width="10%">appkey</td>
                        <td nowrap align="left" width="10%">当前版本</td>
                        <td nowrap width="15%">版本地址</td>
                        <td nowrap width="15%">版本信息</td>
                        <td nowrap align="center" width="10%">版本类型</td>
                        <td nowrap align="center" width="10%">是否必须更新</td>
                        <td nowrap align="center" width="10%">完成时间</td>
                        <td nowrap align="center" width="10%">创建人/创建IP</td>
                        <td nowrap align="center" width="">状态</td>
                        <td nowrap align="center" width="10%">操作</td>
                    </tr>
                    <tr>
                        <td height="1" colspan="11" class="default_line_td"></td>
                    </tr>
                    <c:choose>
                        <c:when test="${list.size() > 0}">
                            <c:forEach items="${list}" var="contentversion" varStatus="st">
                                <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                    <td >${contentversion.id}</td>
                                    <td>${contentversion.appKey}</td>
                                    <td >${contentversion.current_version}</td>
                                    <td nowrap>${contentversion.version_url}</td>
                                    <td >${contentversion.version_info}</td>
                                     <td ><fmt:message key="joymeapp.contentversion.packagetype.${contentversion.packageType}" bundle="${def}"/></td>
                                    <td ><fmt:message key="joymeapp.contentversion.updatenessary.${contentversion.necessaryUpdate}" bundle="${def}"/></td>

                                    <td >${contentversion.publishDate}</td>
                                    <td >${contentversion.createUserId}/${contentversion.createIp}</td>
                                    <td >
                                    <fmt:message key="def.app.removestatus.${contentversion.removeStatus.code}" bundle="${def}"/>
                                   </td>
                                    <td >
                                        <a href="/joymeapp/updatecontent/modifypage?vid=${contentversion.id}">编辑</a>
                                        <a href="/joymeapp/updatecontent/delete?vid=${contentversion.id}&appkey=${contentversion.appKey}">删除</a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <tr>
                                <td height="1" colspan="11" class="default_line_td"></td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="11" class="error_msg_td">暂无数据!</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    <tr>
                        <td colspan="11" height="1" class="default_line_td"></td>
                    </tr>
                    <c:if test="${page.maxPage > 1}">
                        <tr class="list_table_opp_tr">
                            <td colspan="11">
                                <pg:pager url="/joymeapp/app/list"
                                          items="${page.totalRows}" isOffset="true"
                                          maxPageItems="${page.pageSize}"
                                          export="offset, currentPageNumber=pageNumber" scope="request">
                                    <pg:param name="resourceName" value="${resourceName}"/>
                                    <pg:param name="removeStatusCode" value="${removeStatusCode}"/>
                                    <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                    <pg:param name="items" value="${page.totalRows}"/>
                                    <%@ include file="/WEB-INF/jsp/toolspg.jsp" %>
                                </pg:pager>
                            </td>
                        </tr>
                    </c:if>
                </table>
            </form>
        </td>
    </tr>
</table>
</body>
</html>