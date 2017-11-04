<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>社交端文章列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script>
        $(document).ready(function () {
            $('input[name=input_sort_button]').click(function () {
                var index = $(this).attr('id').replace('input_button_sort_', '');
                var sort = $('#select_sort_' + index).val();
                var cid = $('#input_cid_' + index).val();
                var num = $('#input_sort_' + index).val();
                var rowidx = $('#input_hidden_rowidx_' + index).val();
                window.location.href= "/joymeapp/socialclient/hot/content/sort" + sort + "?cid=" + cid + "&sortnum=" + num + "&offset=" + rowidx;
            });
        });
        function sort(sort, cid, validstatus, rowidx) {

            if (validstatus == 'y') {
                alert("已删除的文章不能进行排序");
                return false;
            }
            $.post("/joymeapp/socialclient/hot/content/sort/" + sort, {cid: cid, offset:rowidx}, function (req) {
                var resMsg = eval('(' + req + ')');
                if (resMsg.rs == '0') {
                    return false;
                } else {
                    var result = resMsg.result;

                    if (result == null) {

                    } else {
                        var cid = result.cid;
                        var sort = result.sort;
                        var recid = result.recid;
                        if (sort == 'up') {
                            var item = $("#socialHotContent_" + cid).clone();
                            $("#socialHotContent_" + cid).remove();
                            var itemclass = item.attr("class");
                            var upclass = $("#socialHotContent_" + recid).attr("class");
                            item.removeClass(itemclass);
                            item.addClass(upclass);
                            $("#socialHotContent_" + recid).addClass(itemclass);
                            $("#socialHotContent_" + recid).removeClass(upclass);
                            $("#socialHotContent_" + recid).before(item);
                        } else {
                            var item = $("#socialHotContent_" + cid).clone();
                            $("#socialHotContent_" + cid).remove();
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
        <td height="22" class="page_navigation_td">>> 运营管理 >> 分类管理 >> 社交端文章列表</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">热门文章列表</td>
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
                        <form action="/joymeapp/socialclient/hot/content/list" method="post">
                            <table width="100%">
                                <tr>
                                    <td height="1" class="default_line_td">
                                        选择渠道:
                                    </td>
                                    <td height="1">
                                        <select name="status" id="select_channel">
                                            <option value="n"
                                            <c:if test="${status=='n'}">selected</c:if> >可用
                                            </option>
                                            <option value="y"
                                            <c:if test="${status=='y'}">selected</c:if>>已删除
                                            </option>
                                        </select>
                                    </td>
                                    <td height="1">
                                        按昵称查询：<input type="text" name="screenname" value="${screenname}"/>
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
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="16" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center" width="">文章ID</td>
                    <td nowrap align="center" width="">查看文章</td>
                    <td nowrap align="center" width="">缩略图</td>
                    <td nowrap align="center" width="">发帖人</td>
                    <td nowrap align="center" width="">排序</td>
                    <td nowrap align="center" width="">移动</td>
                    <td nowrap align="center" width="">创建时间</td>
                    <td nowrap align="center" width="">操作</td>

                </tr>
                <tr>
                    <td height="1" colspan="16" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="dto" varStatus="st">
                            <tr id="socialHotContent_${dto.contentId}" class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                <td nowrap>${dto.contentId}</td>
                <td nowrap><a href="/joymeapp/socialclient/content/getbyid?cid=${dto.contentId}"
                              target="_blank">查看文章详情</a></td>
                <td nowrap>
                    <c:forEach items="${contentMap}" var="cmap">
                        <c:if test="${cmap.key == dto.contentId}"><img src="${cmap.value.pic.pic_s}" height="100"
                                                                       width="100"/></c:if>
                    </c:forEach>
                </td>
                <td nowrap>
                    <c:forEach items="${mapprofile}" var="map">
                        <c:if test="${map.key==dto.uno}">${map.value.blog.screenName}</c:if>
                    </c:forEach>
                </td>
                <td nowrap align="center">
                    <a href="javascript:sort('up','${dto.contentId}','${dto.removeStatus.code}','${page.startRowIdx}')"><img
                            src="/static/images/icon/up.gif"></a>
                    &nbsp;
                    <a href="javascript:sort('down','${dto.contentId}','${dto.removeStatus.code}','${page.startRowIdx}')"><img
                            src="/static/images/icon/down.gif"></a>
                </td>
                <td nowrap align="center">
                    向
                    <select id="select_sort_${st.index}">
                        <option value="/up">上</option>
                        <option value="/down">下</option>
                    </select>
                    移动
                    <input type="hidden" id="input_hidden_rowidx_${st.index}" value="${page.startRowIdx}"/>
                    <input type="hidden" id="input_cid_${st.index}" value="${dto.contentId}"/>
                    <input type="text" id="input_sort_${st.index}" value=""/>
                    <input type="button" name="input_sort_button" value="确定" id="input_button_sort_${st.index}" class="default_button"/>
                </td>
                <td nowrap>${dto.createDate}</td>

                <td nowrap>
                    <c:if test="${status!='y'}">
                        <c:choose>
                            <c:when test="${page.maxPage>1}">
                                <a href="/joymeapp/socialclient/hot/content/delete?cid=${dto.contentId}&curpage=${page.curPage}">删除</a>
                            </c:when>
                            <c:otherwise>
                                <a href="/joymeapp/socialclient/hot/content/delete?cid=${dto.contentId}&curpage=0">删除</a>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
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
                            <pg:pager url="/joymeapp/socialclient/hot/content/list"
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
<%--<table>--%>
    <%--<tr>--%>
        <%--<td id="td_submit">--%>
            <%--<form action="" method="post" id="sort_form_submit">--%>
                <%--<input type="hidden" name="sortnum" value="" id="input_hidden_num"/>--%>
                <%--<input type="hidden" name="cid" value="" id="input_hidden_cid"/>--%>
            <%--</form>--%>
        <%--</td>--%>
    <%--</tr>--%>
<%--</table>--%>
</body>
</html>