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
        $(document).ready(function() {

        });


        function deleteStallInfo(seqid, logid, appid) {

            if (confirm("是否删除？删除后不可恢复")) {
                location.href = "/joymeapp/favorite/delete?seqid=" + seqid + "&logid=" + logid + "&appid=" + appid;
            }

        }
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 条目管理 >> 猜你喜欢</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">APP猜你喜欢临时列表</td>
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
                        <form action="/joymeapp/favorite/list" method="post">
                            <table width="400px">
                                <tr>
                                    <td height="1" class="default_line_td" width="200px">
                                        选择APP:
                                    </td>
                                    <td height="1" class="edit_table_defaulttitle_td">
                                        <select name="appid" id="select_appkey">
                                            <option value="">请选择</option>
                                            <c:forEach var="fa" items="${appInfoList}">
                                                <option value="${fa.appInfoId}"
                                                        <c:if test="${fa.appInfoId==appid}">selected</c:if> >${fa.appName}---<fmt:message key="joymeapp.favorite.platform.${fa.appPlatform.code}"
                                                        bundle="${def}"/></option>
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
                                    <c:if test="${appid!=null}">
                                        <form method="post" id="create_app_form"
                                              action="/joymeapp/favorite/createpage">
                                            <input type="hidden" name="appid" value="${appid}"/>
                                            <input type="submit" name="button" value="添加"/>
                                        </form>
                                    </c:if>
                                </td>
                            </tr>
                        </table>

                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="11" class="default_line_td"></td>
                </tr>
                <tr align="center" class="list_table_title_tr">
                    <td nowrap>App名称</td>
                    <td nowrap>安装量</td>
                    <td nowrap>平台</td>
                    <td nowrap>创建日期</td>
                    <td nowrap>排序</td>
                    <td nowrap align="center" width="10%">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="11" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${listAppStatInstallLog.size() > 0}">
                        <c:forEach items="${listAppStatInstallLog}" var="log" varStatus="st">
                            <tr align="center"
                                class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap>${log.destAppName}</td>
                                <td nowrap>${log.destInstallCount}</td>
                                <td nowrap><fmt:message key="joymeapp.favorite.platform.${log.appPlatform.code}"
                                                        bundle="${def}"/></td>
                                <td nowrap>${log.createDate}</td>
                                <td nowrap>
                                    <a href="/joymeapp/favorite/sort/up?seqid=${log.appSeqId}&logid=${log.appInstallLogId}&appid=${appid}"><img
                                            src="/static/images/icon/up.gif"></a>

                                    <a href="/joymeapp/favorite/sort/down?seqid=${log.appSeqId}&logid=${log.appInstallLogId}&appid=${appid}"><img
                                            src="/static/images/icon/down.gif"></a>
                                </td>
                                <td nowrap>
                                    <a href="javascript:deleteStallInfo('${log.appSeqId}','${log.appInstallLogId}','${appid}')">删除</a>
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
                        <td colspan="10">
                            <pg:pager url="/joymeapp/favorite/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="appid" value="${appId}"/>
                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                <pg:param name="items" value="${page.totalRows}"/>
                                <%@ include file="/WEB-INF/jsp/toolspg.jsp" %>
                            </pg:pager>
                        </td>
                    </tr>
                </c:if>

            </table>
            <table>
                <tr>
                    <td><c:if test="${listAppStatInstallLog.size()>0}">
                        <form method="post" action="/joymeapp/favorite/apppublish">
                            <input type="hidden" name="appid" value="${appid}"/>
                            <input type="hidden" name="seqid" value="${seqid}"/>
                            <input type="submit" name="button" value="发布到猜你喜欢"/>
                        </form>
                    </c:if>
                    </td>
                    <td>
                        <c:if test="${appid>0}">
                            <form method="post" action="/joymeapp/favorite/result">
                                <input type="hidden" name="appid" value="${appid}"/>
                                <input type="submit" name="button" value="查看猜你喜欢正式列表"/>
                            </form>
                        </c:if>
                    </td>
                </tr>
            </table>
        </td>
    </tr>

</table>
</body>
</html>