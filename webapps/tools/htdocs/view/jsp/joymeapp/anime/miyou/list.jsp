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
    <script type="text/javascript" src="/static/include/My97DatePicker/WdatePicker.js"></script>
    <script>
        function del(id, removestaus, archiveId, tag_id) {
            var message = "你真的确定要删除吗?";
            if (removestaus == "valid") {
                message = "你真的发布吗?";
            } else if (removestaus == "invalid") {
                message = "你真的预发布吗?";
            }
            var gnl = confirm(message);
            if (!gnl) {
                return false;
            }
            window.location.href = "/comment/bean/delete?tag_id=" + tag_id + "&remove_status=" + removestaus + "&id=" + id + "&archiveId=" + archiveId + "&pager.offset=${page.startRowIdx}";
        }

        function deletetag(id, size) {
            if (size <= 6) {
                alert("圈子数最少要有6个");
            } else {
                window.location.href = "/animetag/miyou/delete?tagid=" + id + "&valid=removed";
            }
        }
    </script>
    <style>
        a {
            text-decoration: none;
        }
    </style>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷手游画报管理 >> 圈子管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td"></td>
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
                </tr>
            </table>
            <form action="/animetag/miyou/list" method="post" id="all_remove_form">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td height="1" class="default_line_td" colspan="2"></td>
                    </tr>
                    <tr class="toolbar_tr">
                        <td width="28%">
                            圈子名称：<input type="text" name="tagname" value="${tagname}"/>
                            <input type="submit" value="筛选"/>
                        </td>
                        <td width="4%">

                        </td>
                    </tr>
                </table>
            </form>
            <form action="/animetag/miyou/createpage" method="post">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td height="1" class="default_line_td" colspan="2"></td>
                    </tr>
                    <tr class="toolbar_tr">
                        <td width="28%">

                            <input type="submit" value="创建新圈子"/>
                        </td>
                        <td width="4%">

                        </td>
                    </tr>
                </table>
            </form>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="10" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center">id</td>
                    <td nowrap align="center">圈子名称</td>
                    <td nowrap align="center">帖子数 <a href="/animetag/miyou/list?postorder=asc">↑</a> <a
                            href="/animetag/miyou/list?postorder=desc">↓</a></td>
                    <td nowrap align="center">评论数<a href="/animetag/miyou/list?replyorder=asc">↑</a> <a
                            href="/animetag/miyou/list?replyorder=desc">↓</a></td>
                    <td nowrap align="center">关注人数</td>
                    <td nowrap align="center">创建日期</td>
                    <td nowrap align="center">排序操作</td>
                    <td nowrap align="center">状态</td>
                    <td nowrap align="center" width="130px">管理</td>
                </tr>
                <tr>
                    <td height="1" colspan="10" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="dto" varStatus="st">
                            <tr id="socialHotContent_${dto.tag_id}"
                                class="<c:choose><c:when
                                test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap align="center">${dto.tag_id}</td>
                                <td nowrap align="center">${dto.tag_name}</td>
                                <td nowrap align="center">${dto.play_num}</td>
                                <td nowrap align="center">${dto.favorite_num}</td>
                                <td nowrap align="center">${dto.total_sum}</td>
                                <td nowrap align="center">${dto.create_date}</td>
                                <td nowrap align="center">${dto.display_order}
                                    <form action="/animetag/miyou/sort" method="post">
                                        <input type="text" name="display_order"
                                               onkeyup="value=value.replace(/[^\d]/g,'')" value="" size="8"/>
                                        <input type="hidden" name="tagid" value="${dto.tag_id}"/>
                                        <input type="submit" name="button" class="default_button" value="修改"/>
                                    </form>
                                </td>
                                <td nowrap align="center"
                                        <c:if test="${dto.remove_status.code eq 'removed'}">
                                            style="color:red;"
                                        </c:if>
                                        <c:if test="${dto.remove_status.code eq 'invalid'}">
                                            style="color:#9400D3;"
                                        </c:if>
                                        <c:if test="${dto.remove_status.code eq 'valid'}">
                                            style="color:#008000;"
                                        </c:if>>
                                    <fmt:message key="anime.remove.status.${dto.remove_status.code}"
                                                 bundle="${def}"/></td>
                                <td nowrap align="center" width="130px">
                                    <a href="/animetag/miyou/modifypage?tagid=${dto.tag_id}">编辑</a>&nbsp;
                                    <c:if test="${dto.remove_status.code eq 'removed'}">
                                        <a href="/animetag/miyou/delete?tagid=${dto.tag_id}&valid=valid">恢复</a>
                                    </c:if>
                                    <c:if test="${dto.remove_status.code eq 'invalid'}">
                                        <a href="/animetag/miyou/delete?tagid=${dto.tag_id}&valid=valid">发布</a>
                                    </c:if>
                                    <c:if test="${dto.remove_status.code eq 'valid'}">
                                        <a href="javascript:deletetag('${dto.tag_id}','${validnum}');">删除</a>
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
                    <td colspan="10" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="10">
                            <pg:pager url="/animetag/miyou/list"
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