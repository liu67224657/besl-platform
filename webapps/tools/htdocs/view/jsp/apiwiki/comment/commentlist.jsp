<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>点评列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#createTime").click(function () {
                $("input[name='order']").val("createTime");
                $("#formPost").submit();
            });

            $("#agreeNum").click(function () {
                $("input[name='order']").val("agreeNum");
                $("#formPost").submit();
            });

            $("#replyNum").click(function () {
                $("input[name='order']").val("replyNum");
                $("#formPost").submit();
            });
        });
    </script>
    <style>
        .name {
            max-width: 300px;
            overflow: hidden;
            text-overflow: ellipsis;
        }

        .target {
            max-width: 300px;
            overflow: hidden;
            text-overflow: ellipsis;
        }
    </style>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 点评管理>> 点评列表</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0" width="80%;">
                <tr>
                    <td>
                        <form action="/apiwiki/comment/list" method="post" id="formPost">
                            搜索：<select name="type">
                            <option value="1" <c:if test="${type=='1'}">selected</c:if>>游戏ID</option>
                            <option value="2" <c:if test="${type=='2'}">selected</c:if>>点评ID</option>
                            <option value="3" <c:if test="${type=='3'}">selected</c:if>>点评内容</option>
                        </select>
                            <input type="text" name="searchText" class="default_button" value="${searchText}"
                                   size="20"/>
                            <input type="hidden" name="order" value="${order}"/>
                            <select name="status">
                                <option value="valid" <c:if test="${status eq 'valid'}">selected</c:if>>正常</option>
                                <option value="removed" <c:if test="${status eq 'removed'}">selected</c:if>>已删除</option>
                            </select>
                            <input type="submit" name="button" class="default_button" value="搜索"/>
                        </form>
                    </td>
                    <td> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;</td>
                    <td align="right" width="30%;">
                        <c:if test="${status eq 'valid'}">
                            <input type="button" id="createTime" value="最新发布"/>
                            <input type="button" id="agreeNum" value="有用次数最多"/>
                            <input type="button" id="replyNum" value="回复最多"/>
                            <input type="button" onclick="window.location.href='/apiwiki/comment/postcomment/page'"
                                   value="发布点评"/>
                        </c:if>
                    </td>
                </tr>
            </table>

            <table width="80%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="11" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center" width="50px">点评ID</td>
                    <td nowrap align="center" width="">游戏名称</td>
                    <td nowrap align="center" width="">发布人</td>
                    <td nowrap align="center" width="">发布人状态</td>
                    <td nowrap align="center" width="70px">添加时间</td>
                    <td nowrap align="center" width="70px">评分</td>
                    <td nowrap align="center">内容</td>
                    <td nowrap align="center">有用次数</td>
                    <td nowrap align="center">回复次数</td>
                    <td nowrap align="center">状态</td>
                    <td nowrap align="center" width="80px">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="11" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="dto" varStatus="st">
                            <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap align="center">${dto.id}</td>
                                <td nowrap align="center">${dto.gameName}</td>
                                <td nowrap align="center">${dto.nick}</td>
                                <td nowrap align="center">
                                    <c:choose>
                                        <c:when test="${dto.verifyProfileType eq 1}">
                                            普通
                                        </c:when>
                                        <c:otherwise>
                                            认证
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td nowrap align="center">
                                        <jsp:useBean id="dateValue" class="java.util.Date"/>
                                        <jsp:setProperty name="dateValue" property="time" value="${dto.time}"/>
                                        <fmt:formatDate value="${dateValue}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                <td nowrap align="center">${dto.score}</td>
                                <td nowrap align="center" width="200">${dto.body}</td>
                                <td nowrap align="center">${dto.agreeNum}</td>
                                <td nowrap align="center">${dto.replyNum}</td>
                                <td nowrap align="center">
                                    <c:choose>
                                        <c:when test="${status eq 'removed'}">
                                            已删除
                                        </c:when>
                                        <c:otherwise>
                                            <c:choose>
                                                <c:when test="${dto.highQuality eq 1}">
                                                    优质评论
                                                </c:when>
                                                <c:otherwise>
                                                    正常
                                                </c:otherwise>
                                            </c:choose>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td nowrap align="center">
                                    <a href="/apiwiki/comment/createpage?id=${dto.id}&status=${status}">编辑</a>&nbsp;&nbsp;&nbsp;

                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="11" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="11" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="11" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="11">
                            <pg:pager url="/apiwiki/comment/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                <pg:param name="items" value="${page.totalRows}"/>
                                <pg:param name="type" value="${type}"/>
                                <pg:param name="status" value="${status}"/>
                                <pg:param name="searchText" value="${searchText}"/>
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