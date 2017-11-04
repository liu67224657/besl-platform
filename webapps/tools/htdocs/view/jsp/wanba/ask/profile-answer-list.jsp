<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>回答列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $('a[name=delete-answer]').click(function () {
                var aid = $(this).attr('data-aid');

                if (confirm('删除回答操作不可逆,确定删除?')) {
                    $.post("/wanba/json/ask/answer/delete", {aid: aid}, function (req) {
                        var rsobj = eval('(' + req + ')');
                        if (rsobj.rs == 1) {
                            $('#td_rstatus_' + aid).html('<font color="red">已删除</font>');
                            $('#td_action_' + aid).html('');
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
        <td height="22" class="page_navigation_td">>> 运营维护 >> 玩霸-问答 >><c:if test="${not empty profile}"><span style="color:red">${profile.nick}</span>，的</c:if>回答列表</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td"><c:if test="${not empty profile}"><span style="color:red">${profile.nick}</span>，的</c:if>回答列表</td>
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
                        <form action="/wanba/ask/profile/answer/list" method="post">
                            <table>
                                <tr>
                                    <c:if test="${not empty profile}">
                                        <input type="hidden" name="nick" class="" value="${profile.nick}"/>
                                    </c:if>
                                    <td>
                                        答案(支持模糊搜索):
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
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="9" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center" width="70px">答案ID</td>
                    <td nowrap align="center" width="70px">操作</td>
                    <td nowrap align="center" width="200px" style="overflow:hidden; white-space:pre;">问题标题</td>
                    <td nowrap align="center" width="200px" style="overflow:hidden; white-space:pre;">回答内容</td>
                    <td nowrap align="center" width="100px">游戏</td>
                    <td nowrap align="center" width="70px">状态</td>
                </tr>
                <tr>
                    <td height="1" colspan="9" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="answer" varStatus="st">
                            <tr id="tr_${answer.answerId}"
                                class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap><a href="/wanba/ask/answer/list?qid=${answer.questionId}">${answer.answerId}</a></td>
                                <td width="70px" nowrap id="td_action_${answer.answerId}">
                                    <c:if test="${answer.removeStatus.code==0}">
                                    <a href="javascript:void(0);" name="delete-answer"
                                                                                              data-aid="${answer.answerId}">删除</a></td>
                                 </c:if>


                                <c:if test="${not empty questionMap}">
                                    <c:if test="${not empty questionMap[answer.questionId]}">
                                        <c:set var="question" scope="session" value="${questionMap[answer.questionId]}"/>
                                    </c:if>
                                    <c:if test="${empty questionMap[answer.questionId]}">
                                        <c:set var="question" scope="session" value=""/>
                                    </c:if>
                                </c:if>

                                <td nowrap width="200px" style="overflow:hidden; white-space:pre;" title='<c:if test="${not empty question}"><c:out value="${question.title}"/></c:if>'><c:if test="${not empty question}"><c:out value="${question.title}"/></c:if></td>
                                <td nowrap width="200px" style="overflow:hidden; white-space:pre;" title="${answer.richText}">${answer.richText}</td>
                                <td width="100px" nowrap id="td_action_${answer.answerId}">
                                    <c:forEach items="${tagList}" var="tag" varStatus="st">
                                        <c:if test="${not empty question}">
                                            <c:if test="${tag.tag_id==question.gameId}">
                                                ${tag.tag_name}
                                            </c:if>
                                        </c:if>
                                    </c:forEach>
                                </td>
                                <td width="70px" nowrap id="td_rstatus_${answer.answerId}"><fmt:message key="wanba.removestatus.${answer.removeStatus.code}"
                                                                                                        bundle="${toolsProps}"/></td>

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
                        <td colspan="10">
                            <pg:pager url="/wanba/ask/profile/answer/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                <pg:param name="items" value="${page.totalRows}"/>
                                <c:if test="${qtype!=null}">
                                    <pg:param name="qtype" value="${qtype}"/>
                                </c:if>
                                <c:if test="${qstatus!=null}">
                                    <pg:param name="qstatus" value="${qstatus}"/>
                                </c:if>
                                <c:if test="${rstatus!=null}">
                                    <pg:param name="rstatus" value="${rstatus}"/>
                                </c:if>
                                <c:if test="${text!=null}">
                                    <pg:param name="text" value="${text}"/>
                                </c:if>
                                <c:if test="${tagid!=null}">
                                    <pg:param name="tagid" value="${tagid}"/>
                                </c:if>
                                <c:if test="${empty nick}">
                                    <pg:param name="nick" value="${nick}"/>
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