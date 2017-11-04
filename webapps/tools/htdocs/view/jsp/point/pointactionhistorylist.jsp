<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>用户积分</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>

    <script type="text/javascript">
        $(document).ready(function() {
            $('#create_form').submit(function() {
                var uno = $('#user_no').val();
                if (uno.length == 0) {
                    alert('请选择一个用户查询后进行修改');
                    return false;
                }
            });
        });
    </script>

</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷积分管理 >> 积分历史明细</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">积分历史列表</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="7" class="error_msg_td">
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
                        <table>
                            <tr>
                                <td>
                                    <form action="/point/pointactionhistory/list" method="post">
                                        <table>
                                            <tr>
                                                <td height="1" class="default_line_td">
                                                    输入用户账号:
                                                </td>
                                                <td height="1" class="edit_table_defaulttitle_td">
                                                    <input name="profilename" type="text" value="${profilename}"/>
                                                </td>

                                                <td>
                                                    <p:privilege name="/point/pointactionhistory/list">
                                                        <input type="submit" name="button" value="查询"/>
                                                    </p:privilege>
                                                </td>
                                                <td height="1" class=>
                                                </td>
                                            </tr>
                                        </table>
                                    </form>
                                    <form action="/point/pointactionhistory/list" method="post">
                                        <table>
                                            <tr>

                                                <td height="1" class="default_line_td">
                                                    输入用户profileid:
                                                </td>
                                                <td height="1" class="edit_table_defaulttitle_td">
                                                    <input name="profileid" type="text" size="32" value="${profileid}"/>
                                                </td>
                                                <td>
                                                    <p:privilege name="/point/pointactionhistory/list">
                                                        <input type="submit" name="button" value="查询"/>
                                                    </p:privilege>
                                                </td>
                                                <td height="1" class=>
                                                </td>
                                            </tr>
                                        </table>
                                    </form>
                                    <form id="create_form" action="/point/pointactionhistory/createpage"
                                          method="post">
                                        <input type="hidden" name="userno" value="${userno}" id="user_no"/>
                                        <input type="hidden" name="profilid" value="${profileid}" id="profile_id"/>
                                        <p:privilege name="/point/pointactionhistory/createpage">
                                            <input type="submit" value="加/减用户积分"/>
                                        </p:privilege>
                                    </form>
                                </td>
                                <td>

                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            <form action="/point/pointactionhistory/list" method="post">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="7" class="default_line_td"></td>
                    </tr>
                    <tr class="list_table_title_tr">
                        <td nowrap align="center" width="100">获取类型</td>
                        <td nowrap align="center" width="200">对象ID</td>
                        <td nowrap align="center" width="100">积分值</td>
                        <td nowrap align="center" width="">积分历史描述</td>
                        <td nowrap align="center" width="200">积分历史时间</td>
                    </tr>
                    <tr>
                        <td height="1" colspan="7" class="default_line_td"></td>
                    </tr>
                    <c:choose>
                        <c:when test="${list.size() > 0}">
                            <c:forEach items="${list}" var="actionhistory" varStatus="st">
                                <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                    <input type="hidden" name="actionhistoryid"
                                           value="${actionhistory.actionHistoryId}"/>
                                    <td nowrap>
                                        <fmt:message key="def.point.actiontype.${actionhistory.actionType.code}"
                                                     bundle="${def}"/>
                                    </td>
                                    <td nowrap="">${actionhistory.destId}</td>
                                    <td nowrap="">${actionhistory.pointValue}</td>
                                    <td nowrap>${actionhistory.actionDescription}</td>
                                    <td nowrap>
                                        <fmt:formatDate value="${actionhistory.createDate}"
                                                        pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </td>
                                </tr>
                            </c:forEach>
                            <tr>
                                <td height="1" colspan="7" class="default_line_td"></td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="7" class="error_msg_td">暂无数据!</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    <tr>
                        <td colspan="7" height="1" class="default_line_td"></td>
                    </tr>
                    <c:if test="${page.maxPage > 1}">
                        <tr class="list_table_opp_tr">
                            <td colspan="7">
                                <pg:pager url="/point/pointactionhistory/list"
                                          items="${page.totalRows}" isOffset="true"
                                          maxPageItems="${page.pageSize}"
                                          export="offset, currentPageNumber=pageNumber" scope="request">
                                    <pg:param name="profilename" value="${profilename}"/>
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