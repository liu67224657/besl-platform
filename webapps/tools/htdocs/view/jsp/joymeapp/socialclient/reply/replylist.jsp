<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>社交端评论列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript">
        $().ready(function() {
            $("#checkall").bind("click", function () {
                if ($('#checkall').is(":checked")) {
                    $("[name=box]:checkbox").attr("checked", true);
                } else {
                    $("[name=box]:checkbox").attr("checked", false);
                }
            });

            $("#checkinverse").bind("click", function () {
                $("[name=box]:checkbox").each(function () {
                    $(this).attr("checked", !$(this).attr("checked"));
                });
            });

            $('#form_submit').bind('submit', function() {

                var result = new Array();
                $("[name=box]:checkbox").each(function () {
                    if ($(this).is(":checked")) {
                        result.push($(this).attr("value"));
                    }
                });
                if (result.length == 0) {
                    alert("至少要选择一条评论");
                    return false;
                }

                $('#input_hidden_ids').val(result.join('@'));
            });
        });
        function del(contentid, replyid, curpage) {
            var gnl = confirm("你真的确定要删除吗?");
            if (!gnl) {
                return false;
            }
            window.location.href = "/joymeapp/socialclient/reply/delete?contentid=" + contentid + "&replyid=" + replyid + "&curpage=" + curpage;
        }
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 分类管理 >> 社交端评论列表</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">评论列表</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" colspan="10" class="error_msg_td">
                        ${errorMsg}
                    </td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table>
                <tr>
                    <td>
                        <form action="/joymeapp/socialclient/reply/list" method="post">
                            <table width="100%">
                                <tr>
                                    <td height="1" class="default_line_td">
                                        选择状态:
                                    </td>
                                    <td height="1">
                                        <select name="status" id="select_channel">
                                            <option value="n"
                                            <c:if test="${status=='n'}">selected</c:if> >可用</option>
                                            <option value="y"
                                            <c:if test="${status=='y'}">selected</c:if>>已删除</option>
                                        </select>
                                    </td>
                                    <td height="1">
                                        文章ID查询：<input type="text" name="contentid" value="${contentid}"/>
                                    </td>
                                    <td height="1">
                                        评论人昵称查询：<input type="text" name="screenname" value="${screenname}"/>
                                    </td>
                                    <td height="1">
                                        评论内容查询：<input type="text" name="body" value="${body}"/>
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

                </tr>
            </table>

            <table width="400px" border="0" cellspacing="0" cellpadding="0">
                <form action="/joymeapp/socialclient/reply/recover" method="post" id="form_submit">
                    <tr>
                        <td>
                            <input type="checkbox" name="all" id="checkall"/>全选
                        </td>
                        <td>
                            <input type="checkbox" name="inverse" id="checkinverse"/>反选
                        </td>
                        <input type="hidden" name="replyids" id="input_hidden_ids">
                        <input type="hidden" name="hiddenstatus" id="hiddenstatus" value="${status}">
                        <td colspan="5">
                            状态改成：
                            <select name="updatestatuscode" class="default_select_single">
                                <%--<option value="">--请选择--</option>--%>
                                <option value="n">恢复</option>
                                <option value="y">删除</option>
                            </select>
                            <input name="button" type="submit" class="default_button" value="批量修改">
                        </td>
                        <td></td>
                    </tr>
                </form>
            </table>

            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="16" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center" width="15px"></td>
                    <td nowrap align="center" width="70px">评论ID</td>
                    <td nowrap align="center" width="200px">评论人</td>
                    <td nowrap align="center" width="70px">文章ID</td>
                    <td nowrap align="center" width="200px">文章发布人</td>
                    <td nowrap align="center" width="430px"> 内容</td>
                    <td nowrap align="center" width="150px">创建时间</td>
                    <td nowrap align="center" width="70px">操作</td>

                </tr>
                <tr>
                    <td height="1" colspan="16" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="dto" varStatus="st">
                            <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                <td nowrap>
                    <input type="checkbox" name="box" value="${dto.replyId}_${dto.contentId}"/>
                </td>
                <td nowrap>${dto.replyId}</td>
                <td nowrap><c:forEach items="${mapprofile}" var="map">
                    <c:if test="${map.key==dto.replyUno}">${map.value.blog.screenName}</c:if>
                </c:forEach></td>
                <td nowrap>${dto.contentId}</td>
                <td nowrap>
                    <c:forEach items="${mapprofile}" var="map">
                        <c:if test="${map.key==dto.contentUno}">${map.value.blog.screenName}</c:if>
                    </c:forEach>
                </td>
                <td nowrap>${dto.body}</td>

                <td nowrap>${dto.createTime}</td>
                <td nowrap>
                    <c:choose>
                        <c:when test="${dto.removeStatus.getCode()=='n'}">
                            <a href="javascript:del(${dto.contentId},${dto.replyId},${page.curPage})">删除</a>
                        </c:when>
                        <c:otherwise>
                            <a href="/joymeapp/socialclient/reply/recover?contentid=${dto.contentId}&replyid=${dto.replyId}&curpage=0">恢复</a>
                        </c:otherwise>
                    </c:choose>
                </td>


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
                            <pg:pager url="/joymeapp/socialclient/reply/list"
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