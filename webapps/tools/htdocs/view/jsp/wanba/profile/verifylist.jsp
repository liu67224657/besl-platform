<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>玩霸认证用户列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $('a[name=delete-profile]').click(function () {
                var pid = $(this).attr('data-pid');

                if (confirm('删除验证用户操作不可逆,确定删除?')) {
                    $.post("/wanba/json/profile/verify/delete", {pid: pid}, function (req) {
                        var rsobj = eval(req);
                        if (rsobj.rs == 1) {
                            $('#tr_' + pid).remove();
                        }
                    })
                }
            });
        });
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 玩霸-认证用户 >> 玩霸认证用户列表</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">玩霸认证用户列表</td>
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
                        <form action="/wanba/profile/verifylist" method="post">
                            <table>
                                <tr>
                                    <td>
                                        认证类型：
                                    </td>
                                    <td>
                                        <select name="verifyid">
                                            <option value="">全部</option>
                                            <c:forEach items="${wanbaVerifyList}" var="verify">
                                                <option value="${verify.verifyId}"
                                                        <c:if test="${verify.verifyId eq verifyid}">selected</c:if> >
                                                        ${verify.verifyName}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td>
                                        来源：
                                    </td>
                                    <td>
                                        <select name="appkey">
                                            <c:forEach items="${appkeyList}" var="key">
                                                <option value="${key}"
                                                        <c:if test="${key eq appkey}">selected</c:if> >
                                                    <fmt:message key="verify.source.appkey.${key}"
                                                                 bundle="${def}"/>
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td>
                                        <input type="submit" name="button" class="default_button" value="筛选"/>
                                    </td>
                                </tr>
                            </table>
                        </form>
                        <form action="/wanba/profile/verifylist" method="post">
                            <table>
                                <tr>
                                    <td>
                                        昵称(支持模糊搜索)
                                    </td>
                                    <td>
                                        <input type="text" name="text" class="" value="${text}"/>
                                    </td>
                                    <td>
                                        <input type="submit" name="button" class="default_button" value="搜索"/>
                                    </td>
                                </tr>
                            </table>
                        </form>

                        <form action="/wanba/profile/createpage" method="post">
                            <table>
                                <tr>

                                    <td>
                                        <input type="submit" name="button" class="default_button" value="新增"/>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="8" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center" width="80">用户ID</td>
                    <td nowrap align="center" width="">昵称</td>
                    <td nowrap align="center" width="">来源</td>
                    <td nowrap align="center" width="">认证类型</td>
                    <td nowrap align="center" width="">认证信息</td>
                    <td nowrap align="center" width="">提问积分</td>
                    <td nowrap align="center" width="">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="16" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="profile" varStatus="st">
                            <tr id="tr_${profile.profileId}"
                                class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap>${profile.profileId}</td>
                                <td nowrap>${profile.nick}</td>
                                <td nowrap><fmt:message key="verify.source.appkey.${profile.appkey}"
                                                        bundle="${def}"/></td>
                                <td nowrap>
                                    <c:choose>
                                        <c:when test="${profile.verifyType eq 0}">
                                            无
                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach items="${wanbaVerifyList}" var="verify">
                                                <c:if test="${verify.verifyId eq profile.verifyType}">
                                                    ${verify.verifyName}
                                                </c:if>
                                            </c:forEach>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td nowrap>${profile.description}</td>
                                <td nowrap>${profile.askPoint}</td>
                                <td nowrap>
                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                    <a href="/wanba/profile/modifypage?profileid=${profile.profileId}">编辑认证信息</a>
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
                            <pg:pager url="/wanba/profile/verifylist"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                <pg:param name="items" value="${page.totalRows}"/>
                                <c:if test="${text!=null}">
                                    <pg:param name="text" value="${text}"/>
                                </c:if>
                                <c:if test="${verifyid!=null}">
                                    <pg:param name="verifyid" value="${verifyid}"/>
                                </c:if>
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