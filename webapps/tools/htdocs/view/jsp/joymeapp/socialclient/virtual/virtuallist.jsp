<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>社交端虚拟用户列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script>
        $(document).ready(function() {
            $('#focusBatch').bind("submit", function() {
                var srcScreenname = $.trim($('#srcScreenname').val());
                if (srcScreenname.length == 0) {
                    alert("用户昵称");
                    return false;
                }
                var focusNum = $.trim($('#focusNum').val());
                if (focusNum.length == 0) {
                    alert("请填写关注数量");
                    return false;
                }
                if(isNaN(focusNum)){
                    alert("关注数量请填写数字");
                    return false;
                }

                var accountType =$("#accountTypeFocus").val();
                if(accountType==0 && agreeNum>${defaultNum}){//虚拟用户
                    alert("关注数量必须小于虚拟用户总数");
                    return false;
                }else if(accountType==1 && agreeNum>${formalNum}){//运营
                    alert("关注数量必须小于运营用户总数");
                    return false;
                }

            });
        });
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
    <td height="22" class="page_navigation_td">>> 运营管理 >> 分类管理 >> 虚拟用户列表</td>
</tr>
<tr>
<td height="100%" valign="top"><br>
    <table border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td class="list_table_header_td">虚拟用户列表</td>
        </tr>
    </table>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <c:if test="${fn:length(errorMsg)>0}">
            <tr>
                <td height="1" colspan="10" class="error_msg_td">
                    ${errorMsg}
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
                <form action="/joymeapp/socialclient/virtual/focus" method="post" id="focusBatch">
                    <table width="100%">
                        <tr>
                            <td height="1" class="default_line_td">
                                批量关注:
                            </td>
                            <td height="1">
                                用户昵称：<input type="text" id="srcScreenname" name="srcScreenname" value="${srcScreenname}"
                                       class="default_button" size="30"/>
                                关注数量：<input type="text" id="focusNum" name="focusNum" value="${focusNum}"
                                       class="default_button" size="30"/>
                            </td>
                            <td height="1">
                                <select name="action" id="action">
                                    <option value="0">用户关注</option>
                                    <option value="1" selected="selected">用户被关注</option>
                                    <option value="3">取消用户关注</option>
                                    <option value="4">取消用户被关注</option>
                                </select>
                            </td>
                            <td height="1">
                                <select name="accountTypeFocus" id="accountTypeFocus">
                                    <option value="0" selected="selected">虚拟用户(共${defaultNum})</option>
                                    <option value="1">运营号(共${formalNum})</option>
                                </select>
                            </td>
                            <td height="1">
                                <select name="timerType">
                                    <option value="0" selected="selected">每1分钟执行一次</option>
                                    <option value="1">每5分钟执行一次</option>
                                    <option value="2">每10分钟执行一次</option>
                                    <option value="3">每15分钟执行一次</option>
                                    <option value="4">每30分钟执行一次</option>
                                </select>
                            </td>
                            <td>
                                <input type="submit" name="button" class="default_button" value="确定"/>
                            </td>
                        </tr>
                    </table>
                </form>
            </td>
        </tr>
    </table>
    <table>
        <tr>
            <td>
                <form action="/joymeapp/socialclient/virtual/list" method="post">
                    <table width="100%">
                        <tr>
                            <td height="1" class="default_line_td">
                                虚拟用户昵称:
                            </td>
                            <td height="1">
                                <input type="text" name="screenname" class="default_button" size="30"/>
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
                            <form action="/joymeapp/socialclient/virtual/createpage" method="post">
                                <input type="submit" name="button" class="default_button" value="新增虚拟用户"/>
                            </form>
                        </td>
                        <td>
                            <form action="/joymeapp/socialclient/virtual/replypage" method="post">
                                <input type="submit" name="button" class="default_button" value="批量评论"/>
                            </form>
                        </td>
                        <td>
                            <form action="/joymeapp/socialclient/operate/list" method="get">
                            <input type="submit"  name="button" class="default_button" value="运营账号管理"/>
                            </form>
                         </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
    <table width="100%" border="0" cellspacing="1" cellpadding="0">
        <tr>
            <td height="1" colspan="9" class="default_line_td"></td>
        </tr>
        <tr class="list_table_title_tr">
            <td nowrap align="center" width="100">用户ID</td>
            <td nowrap align="center" width="200">用户UNO</td>
            <td nowrap align="center" width="150">用户昵称</td>
            <td nowrap align="center" width="80">性别</td>
            <td nowrap align="center" width="150">用户生日</td>
            <td nowrap align="center" width="80">关注数</td>
            <td nowrap align="center" width="80">粉丝数</td>
            <td nowrap align="center" width="150">创建时间</td>
            <td nowrap align="center" width="150">操作</td>
        </tr>
        <tr>
            <td height="1" colspan="9" class="default_line_td"></td>
        </tr>
        <c:choose>
            <c:when test="${list.size() > 0}">
                <c:forEach items="${list}" var="dto" varStatus="st">
                    <tr id="socialHotContent_${dto.virtual_id}"
                        class="<c:choose><c:when test="
                    ${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
        <td nowrap>${dto.virtual_id}</td>
        <td nowrap>${dto.uno}</td>
        <td nowrap>
            <c:forEach items="${mapprofile}" var="map">
                <c:if test="${map.key==dto.uno}">
                    ${map.value.blog.screenName}
                </c:if>
            </c:forEach>
        </td>
        <td nowrap>
            <c:forEach items="${mapprofile}" var="map">
                <c:if test="${map.key==dto.uno}">
                    <c:if test="${map.value.detail.sex==0}">女</c:if>
                    <c:if test="${map.value.detail.sex==1}">男</c:if>
                </c:if>
            </c:forEach>
        </td>
        <td nowrap>
            <c:forEach items="${mapprofile}" var="map">
                <c:if test="${map.key==dto.uno}">
                    ${map.value.detail.birthday}
                </c:if>
            </c:forEach>
        </td>
        <td nowrap>
            <c:forEach items="${mapprofile}" var="map">
                <c:if test="${map.key==dto.uno}">
                    ${map.value.sum.socialFocusSum}
                </c:if>
            </c:forEach>
        </td>
        <td nowrap>
            <c:forEach items="${mapprofile}" var="map">
                <c:if test="${map.key==dto.uno}">
                    ${map.value.sum.socialFansSum}
                </c:if>
            </c:forEach>
        </td>
        <td nowrap>${dto.create_time}</td>
        <td nowrap>
            <a href="/joymeapp/socialclient/virtual/modifypage?virtualId=${dto.virtual_id}&pager.offset=${page.startRowIdx}">编辑</a>
            <a href="/joymeapp/socialclient/virtual/postpage?uno=${dto.uno}&pager.offset=${page.startRowIdx}">发文章</a>
        </td>
        </tr>
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
                    <pg:pager url="/joymeapp/socialclient/virtual/list"
                              items="${page.totalRows}" isOffset="true"
                              maxPageItems="${page.pageSize}"
                              export="offset, currentPageNumber=pageNumber" scope="request">

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