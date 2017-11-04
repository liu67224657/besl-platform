<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>社交端文章列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script>
        function sort(sort, cid, validstatus) {

            if (validstatus == 'y') {
                alert("已删除推荐人不能进行排序");
                return false;
            }
            $.post("/json/joymeapp/social/profile/recommend/sort/" + sort, {sid: cid}, function (req) {
                var resMsg = eval('(' + req + ')');
                if (resMsg.rs == '0') {
                    return false;
                } else {
                    var result = resMsg.result;

                    if (result == null) {

                    } else {
                        var sid = result.sid;
                        var sort = result.sort;
                        var recid = result.recid;
                        if (sort == 'up') {
                            var item = $("#socialHotContent_" + sid).clone();
                            $("#socialHotContent_" + sid).remove();
                            var itemclass = item.attr("class");
                            var upclass = $("#socialHotContent_" + recid).attr("class");
                            item.removeClass(itemclass);
                            item.addClass(upclass);
                            $("#socialHotContent_" + recid).addClass(itemclass);
                            $("#socialHotContent_" + recid).removeClass(upclass);
                            $("#socialHotContent_" + recid).before(item);
                        } else {
                            var item = $("#socialHotContent_" + sid).clone();
                            $("#socialHotContent_" + sid).remove();
                            var itemclass = item.attr("class");
                            var upclass = $("#socialHotContent_" + recid).attr("class");
                            item.removeClass(itemclass);
                            item.addClass(upclass);
                            $("#socialHotContent_" + recid).addClass(itemclass);
                            $("#socialHotContent_" + recid).removeClass(upclass);
                            $("#socialHotContent_" + recid).after(item);
                        }
                    }
                }
            });

        }
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 分类管理 >> 推荐人列表</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">推荐人列表</td>
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
                        <form action="/joymeapp/social/profile/recommend/list" method="post">
                            <table width="100%">
                                <tr>
                                    <td height="1" class="default_line_td">
                                        选择渠道:
                                    </td>
                                    <td height="1">
                                        <select name="status" id="select_channel">
                                            <option value="n" <c:if test="${status=='n'}">selected</c:if> >可用
                                            </option>
                                            <option value="y" <c:if test="${status=='y'}">selected</c:if>>已删除
                                            </option>
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
                                    <form action="/joymeapp/social/profile/recommend/createpage" method="post">
                                        <input type="submit" name="button" class="default_button" value="新增推荐人"/>
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
                    <td nowrap align="center" width="">推荐ID</td>
                    <td nowrap align="center" width="">昵称</td>
                    <td nowrap align="center" width="">分类</td>
                    <td nowrap align="center" width="">排序</td>
                    <td nowrap align="center" width="">操作</td>
                    <td nowrap align="center" width="">创建信息</td>
                    <td nowrap align="center" width="">修改信息</td>
                </tr>
                <tr>
                    <td height="1" colspan="16" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="dto" varStatus="st">
                            <tr id="socialHotContent_${dto.recommendId}" class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                <td nowrap>${dto.recommendId}</td>
                <td nowrap>
                    <c:forEach items="${mapprofile}" var="map">
                        <c:if test="${map.key==dto.uno}">${map.value.blog.screenName}</c:if>
                    </c:forEach>
                </td>
                <td><fmt:message key="social.profile.recommend.type.${dto.recommendType.code}" bundle="${def}"/></td>
                <td nowrap align="center">
                    <a href="javascript:sort('up','${dto.recommendId}','${dto.actStatus.code}')"><img
                            src="/static/images/icon/up.gif"></a>
                    &nbsp;
                    <a href="javascript:sort('down','${dto.recommendId}','${dto.actStatus.code}')"><img
                            src="/static/images/icon/down.gif"></a>
                </td>
                <td nowrap>
                    <c:choose>
                        <c:when test="${status!='y'}">
                            <c:choose>
                                <c:when test="${page.maxPage>1}">
                                    <a href="/joymeapp/social/profile/recommend/delete?sid=${dto.recommendId}&curpage=${page.curPage}">删除</a>
                                </c:when>
                                <c:otherwise>
                                    <a href="/joymeapp/social/profile/recommend/delete?sid=${dto.recommendId}&curpage=0">删除</a>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <c:choose>
                                <c:when test="${page.maxPage>1}">
                                    <a href="/joymeapp/social/profile/recommend/recover?sid=${dto.recommendId}&curpage=${page.curPage}">恢复</a>
                                </c:when>
                                <c:otherwise>
                                    <a href="/joymeapp/social/profile/recommend/recover?sid=${dto.recommendId}&curpage=0">恢复</a>
                                </c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td nowrap>${dto.createTime}<br/>${dto.createUserId}</td>
                <td nowrap>${dto.modifyDate}<br/>${dto.modifyUserId}</td>
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
                            <pg:pager url="/joymeapp/social/profile/recommend/list"
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