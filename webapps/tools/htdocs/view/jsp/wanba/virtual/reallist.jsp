<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>玩霸用户列表</title>
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
        <td height="22" class="page_navigation_td">>> 运营维护 >> 玩霸-虚拟用户 >> 玩霸用户列表</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">玩霸用户列表</td>
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
                        <form action="/wanba/virtual/reallist" method="post">
                            <table>
                                <tr>
                                    <td>
                                       昵称(不支持模糊搜索)
                                    </td>
                                    <td>
                                        <input type="text" name="nick" class="" value="${nick}"/>
                                    </td>
                                    <td>
                                        <input type="submit" name="button" class="default_button" value="搜索"/>
                                    </td>
                                </tr>
                            </table>
                        </form>


                    </td>
                </tr>
            </table>
            <table width="" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="6" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center" width="50px">ID</td>
                    <td nowrap align="center" width="200px">昵称</td>
                    <td nowrap align="center" width="50px">达人</td>
                    <td nowrap align="center" width="250px">用户ID</td>
                    <td nowrap align="center" width="50px">提问</td>
                    <td nowrap align="center" width="50px">回答</td>
                </tr>
                <tr>
                    <td height="1" colspan="6" class="default_line_td"></td>
                </tr>
                        <c:if test="${not empty  profile}">
                            <tr id="tr_${profile.profileId}"
                                class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap>${st.index+1}</td>
                                <td nowrap>${profile.nick}</td>
                                <td nowrap>
                                    <c:if test="${empty verifyProfile}">否</c:if>
                                    <c:if test="${not empty verifyProfile}">是</c:if>
                                </td>
                                <td nowrap>${profile.profileId}</td>
                                <td nowrap><a href="/wanba/ask/question/list?nick=${profile.nick}">${questionPageRows}</a></td>
                                <td nowrap><a href="/wanba/ask/profile/answer/list?nick=${profile.nick}">${wanbaProfileSum.answerSum}</a></td>

                            </tr>
                        <tr>
                            <td height="1" colspan="6" class="default_line_td"></td>
                        </tr>
                </c:if>
                <c:if test="${empty  profile}">
                        <tr>
                            <td colspan="6" class="error_msg_td">暂无数据!</td>
                        </tr>
                </c:if>
                <tr>
                    <td colspan="8" height="1" class="default_line_td"></td>
                </tr>


            </table>
        </td>
    </tr>
</table>
</body>
</html>