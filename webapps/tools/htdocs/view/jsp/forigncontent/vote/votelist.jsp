<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>wiki 投票管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.easyui.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/static/include/js/easyui/themes/default/easyui.css">
    <script type="text/javascript" src="/static/include/js/easyui/locale/easyui-lang-zh_CN.js"></script>

    <script type="text/javascript">
        $(document).ready(function (e) {
            var arrays = new Array();
            <c:if test="${list.size() > 0}">
            <c:forEach items="${list}" var="item" varStatus="st">
            arrays.push('${item.description}');
            </c:forEach>
            </c:if>

            var trs = $("#content tr");
            for (var i = 0; arrays[i]; i++) {
                var tr = trs.eq(i + 3);
                var jsonObj = $.parseJSON(arrays[i]);

                if (jsonObj.choosetype == 0) {
                    tr.find("td").eq(5).html("单选");


                } else if (jsonObj.choosetype == 1) {
                    tr.find("td").eq(5).html("多选");

                }
                tr.find("td").eq(6).html("最多" + jsonObj.maxchooseitems + "项");


                if (jsonObj.restrict == 0) {
                    tr.find("td").eq(7).html("无限制");
                } else if (jsonObj.restrict == 1) {
                    tr.find("td").eq(7).html("一个IP每天限投一次");
                } else if (jsonObj.restrict == 2) {
                    tr.find("td").eq(7).html("一个用户限投一次");
                } else if (jsonObj.restrict == 3) {
                    tr.find("td").eq(7).html("一个浏览器限投一次");
                }


                if (jsonObj.resultvisible == 0) {
                    tr.find("td").eq(8).html("任何人可见");
                } else if (jsonObj.resultvisible == 1) {
                    tr.find("td").eq(8).html("投票后可见");
                } else if (jsonObj.resultvisible == 2) {
                    tr.find("td").eq(8).html("只有tools后台可见");
                }

                tr.find("td").eq(9).html(jsonObj.starttime);
                tr.find("td").eq(10).html(jsonObj.endtime);


            }


        });
        function deleteById(commentId) {
            var msg = "您确定要把id为  \"" + commentId + "\"  的投票删除吗.删后它仍在此列表中,只是改变了状态.\n\n请确认！";
            if (confirm(msg) == true) {
                window.location.href = "/comment/vote/wiki/delete?commentId=" + commentId;

            }
        }

    </script>

</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr height="40">
        <td height="22" class="page_navigation_td" colspan="2">>> 运营维护 >> 内容专题维护 >> wiki 投票管理</td>
    </tr>
    <c:if test="${fn:length(errorMsg)>0}">
        <tr>
            <td height="1" class="error_msg_td" colspan="2">
                <fmt:message key="${errorMsg}" bundle="${error}"/>
            </td>
        </tr>
    </c:if>
    <tr>
        <td height="1" colspan="2" class="default_line_td"></td>
    </tr>

    <tr>
        <td width="680px">
            <form action="/comment/vote/wiki/list" method="post">
                <table>
                    <tr>
                        <td height="1" class="default_line_td" width="60px">
                            投票主题:
                        </td>
                        <td height="1" class="edit_table_defaulttitle_td" width="80px">
                            <input type="text" name="searchTitle" value="${searchTitle}"/>
                        </td>

                        <td height="1" class=>
                        </td>
                        <td height="1" class="default_line_td" width="50px">
                            创建人:
                        </td>
                        <td height="1" class="edit_table_defaulttitle_td" width="80px">
                            <input type="text" name="searchNick" value="${searchNick}"/>
                        </td>

                        <td height="1" width="100px">
                            [暂不可模糊搜索]
                        </td>
                        <td width="50">
                            <input type="submit" name="button" value="查询"/>
                        </td>
                    </tr>
                </table>
            </form>

        </td>
        <td align="left">
            <form method="post" id="create_vote_form"
                  action="/comment/vote/wiki/createpage">
                <input type="submit" name="button" value="添加新的投票"/>
            </form>
        </td>
    </tr>
</table>
<table width="100%" border="0" cellspacing="2" cellpadding="0" id="content">
    <tr>
        <td height="1" colspan="14" class="default_line_td"></td>
    </tr>
    <tr class="list_table_title_tr">
        <td nowrap align="left">unikey</td>
        <td nowrap align="left">创建者呢称</td>
        <td nowrap align="left">投票主题</td>
        <td nowrap align="left">投票选项数</td>
        <td nowrap align="left">总投票人次</td>
        <td nowrap align="left">模式</td>
        <td nowrap align="left">最多可选项数</td>
        <td nowrap align="left">投票限制</td>
        <td nowrap align="left">结果可见性</td>
        <td nowrap align="left">投票开始时间</td>
        <td nowrap align="left">投票结束时间</td>
        <td nowrap align="left">投票主题创建时间</td>
        <td nowrap align="left">状态</td>
        <td nowrap align="center">操作</td>
    </tr>
    <tr>
        <td height="1" colspan="14" class="default_line_td"></td>
    </tr>
    <c:choose>
        <c:when test="${not empty list}">
            <c:forEach items="${list}" var="item" varStatus="st">
                <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                    <td nowrap><c:out value="${item.uniqueKey}" escapeXml="true"/></td>
                    <td nowrap>
                        <c:if test="${not empty item.uri}">
                            <c:out value="${item.profile.nick}" escapeXml="true"/>
                        </c:if></td>
                    <td nowrap><c:out value="${item.title}" escapeXml="true"/></td>
                    <td nowrap><c:out value="${item.voteList.size()}" escapeXml="true"/></td>
                    <td nowrap><c:out value="${item.totalRows}" escapeXml="true"/></td>
                    <td nowrap></td>
                    <td nowrap></td>
                    <td nowrap></td>
                    <td nowrap></td>
                    <td nowrap></td>
                    <td nowrap></td>
                    <td nowrap><fmt:formatDate value='${item.createTime}' pattern='yyyy-MM-dd HH:mm:ss'
                                               type="both"/></td>
                    <td nowrap>
                        <c:choose>
                            <c:when test="${item.removeStatus.code=='n'}">
                                未审核
                            </c:when>
                            <c:when test="${item.removeStatus.code=='ing'}">
                                已审核
                            </c:when>
                            <c:otherwise>
                                已删除
                            </c:otherwise>
                        </c:choose>
                    </td>

                    <td nowrap>
                        <a href="/comment/vote/wiki/modifypage?commentId=${item.commentId}">编辑</a>
                        <a href="javascript:;"
                           onclick="deleteById('${item.commentId}');">删除</a>
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td height="1" colspan="14" class="default_line_td"></td>
            </tr>
        </c:when>
        <c:otherwise>
            <tr>
                <td colspan="14" class="error_msg_td">暂无数据!</td>
            </tr>
        </c:otherwise>
    </c:choose>
    <tr>
        <td colspan="14" height="1" class="default_line_td"></td>
    </tr>
    <c:if test="${page.maxPage > 1}">
        <tr class="list_table_opp_tr">
            <td colspan="14">
                <pg:pager url="/comment/vote/wiki/list"
                          items="${page.totalRows}" isOffset="true"
                          maxPageItems="${page.pageSize}"
                          export="offset, currentPageNumber=pageNumber" scope="request">
                    <pg:param name="currentPageNumber" value="${page.curPage}"/>
                    <pg:param name="searchTitle" value="${searchTitle}"/>
                    <pg:param name="searchNick" value="${searchNick}"/>
                    <pg:param name="maxPageItems" value="${page.pageSize}"/>
                    <pg:param name="items" value="${page.totalRows}"/>
                    <%@ include file="/WEB-INF/jsp/toolspgnoincludejquery.jsp" %>
                </pg:pager>
            </td>
        </tr>
    </c:if>
</table>


</body>
</html>