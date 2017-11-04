<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>玩霸虚拟用户列表</title>
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
        <td height="22" class="page_navigation_td">>> 运营维护 >> 玩霸-虚拟用户 >> 玩霸虚拟用户列表</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">玩霸虚拟用户列表</td>
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
                        <form action="/wanba/virtual/createpage" method="post">
                            <table>
                                <tr>

                                    <td>
                                        <input type="submit" name="button" class="default_button" value="新增虚拟用户"/>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </td>
                    <td>
                        <form action="/wanba/virtual/postquestionpage" method="post">
                            <table>
                                <tr>

                                    <td>
                                        <input type="submit" name="button" class="default_button" value="虚拟用户提问【抢答】"/>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </td>
                    <td>
                        <form action="/wanba/virtual/invitequestionpage" method="post">
                            <table>
                                <tr>

                                    <td>
                                        <input type="submit" name="button" class="default_button" value="虚拟用户提问【1对1】"/>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </td>
                    <td>
                        <form action="/wanba/virtual/list" method="post">
                            <table>
                                <tr>
                                    <td>
                                        昵称(支持模糊搜索)
                                    </td>
                                    <td>
                                        <input type="text" name="ext" class="" value="${ext}"/>
                                    </td>
                                    <td>
                                        状态
                                    </td>
                                    <td>
                                        <select name="remove_status">
                                            <option value="1" <c:if test="${remove_status==1}">selected="" </c:if>>可用</option>
                                            <option value="0" <c:if test="${remove_status==0}">selected="" </c:if>>删除</option>
                                        </select>
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
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="10" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center" width="">ID</td>
                    <td nowrap align="center" width="">头像</td>
                    <td nowrap align="center" width="">昵称</td>
                    <td nowrap align="center" width="">用户ID</td>
                    <td nowrap align="center" width="">状态</td>
                    <td nowrap align="center" width="">回复我的</td>
                    <td nowrap align="center" width="">问答通知</td>
                    <td nowrap align="center" width="">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="10" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="profile" varStatus="st">
                            <tr id="tr_${profile.profileid}"
                                class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap>${st.index+1}</td>
                                <td nowrap><c:if test="${profileMap[profile.profileid]!=null}">
                                    <img src="${profileMap[profile.profileid].icon}" height="30px" width="30px"/>
                                </c:if></td>
                                <td nowrap><c:if test="${profileMap[profile.profileid]!=null}">${profileMap[profile.profileid].nick}</c:if></td>
                                <td nowrap>${profile.profileid}</td>
                                <td nowrap>
                                    <c:if test="${profile.removeStatus.code==1}">
                                         可用
                                    </c:if>
                                    <c:if test="${profile.removeStatus.code==0}">
                                        删除
                                    </c:if>
                                </td>
                                <td nowrap align="center">
                                    <a href="/wanba/virtual/messagelist?profilieid=${profile.profileid}">
                                        <c:set value="${appNoticeSumMap[profile.profileid]}" var="appmap"></c:set>
                                        <c:if test="${empty appmap['reply']}">0</c:if>
                                        <c:if test="${not empty appmap['reply']}">${appmap['reply'].value}</c:if>
                                    </a>
                                </td>
                                <td nowrap align="center">
                                    <a href="/wanba/virtual/questionlist?profilieid=${profile.profileid}">
                                        <c:if test="${empty appmap['answer']}">0</c:if>
                                        <c:if test="${not empty appmap['answer']}">${appmap['answer'].value}</c:if>
                                    </a>
                                </td>
                                <td nowrap>
                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                    <a href="/wanba/virtual/modifypage?classifyid=${profile.classifyid}">编辑</a>
                                    <c:if test="${profile.removeStatus.code==1}">
                                        <a href="/wanba/virtual/remove?classifyid=${profile.classifyid}&remove_status=0">删除</a>
                                    </c:if>
                                    <c:if test="${profile.removeStatus.code==0}">
                                        <a href="/wanba/virtual/remove?classifyid=${profile.classifyid}&remove_status=1">恢复</a>
                                    </c:if>
                                </td>

                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="10" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="10" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="8" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="10">
                            <pg:pager url="/wanba/virtual/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                <pg:param name="items" value="${page.totalRows}"/>
                                <pg:param name="remove_status" value="${remove_status}"/>
                                <pg:param name="ext" value="${ext}"/>
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