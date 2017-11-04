<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>答案列表</title>
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

        //跳转到添加禁言用户的页面
        function toforbiduser(nickName) {
            var href = "/forign/content/forbid/createpage?nickName=" + nickName;
            window.location.href = href;
        }


        //采纳
        function accept(questionid,answerid) {
            var href = "/wanba/virtual/answeraccept?questionid="+questionid+"&answerid="+answerid
            window.location.href = href;
        }
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 玩霸-问答 >> 答案列表</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">答案列表</td>
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
                        <form action="/wanba/ask/answer/list" method="post">
                            <table>
                                <tr>
                                    <input type="hidden" name="qid" value="${qid}"/>
                                    <td>
                                        删除状态:
                                    </td>
                                    <td>
                                        <select name="rstatus">
                                            <option value="0"
                                                    <c:if test="${rstatus==0}">selected</c:if> >
                                                <fmt:message key="wanba.removestatus.0" bundle="${toolsProps}"/>
                                            </option>
                                            <option value="1"
                                                    <c:if test="${rstatus==1}">selected</c:if> >
                                                <fmt:message key="wanba.removestatus.1" bundle="${toolsProps}"/>
                                            </option>
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
                <tr class="list_table_even_tr">
                    <td>
                        提问ID:${question.questionId}
                    </td>
                    <td>
                        ${question.questionId}
                    </td>
                    <td>
                        问题标题:
                    </td>
                    <td>
                        ${question.title}
                    </td>
                    <td>
                        提问人:<c:if test="${profileMap[question.askProfileId]!=null}">
                        ${profileMap[question.askProfileId].nick}
                        <input type="button" name="toforbiduser"
                               onclick="toforbiduser('${profileMap[question.askProfileId].nick}');"
                               class="default_button" value="禁言"/>
                        <a href="/wanba/virtual/answerpage?questionid=${question.questionId}">虚拟用户回答</a>
                    </c:if>
                    </td>
                    <td>
                        <c:if test="${question.type.code==1}">
                        被提问人:<c:if test="${profileMap[question.inviteProfileId]!=null}">
                        ${profileMap[question.inviteProfileId].nick}
                        <input type="button" name="toforbiduser"
                               onclick="toforbiduser('${profileMap[question.inviteProfileId].nick}');"
                               class="default_button" value="禁言"/>
                            </c:if>
                        </c:if>
                    </td>

                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="5" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center" width="80">答案ID</td>
                    <td nowrap align="center" width="">回答</td>
                    <td nowrap align="center" width="">回答人</td>
                    <td nowrap align="center" width="">删除状态</td>
                    <td nowrap align="center" width="">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="16" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="answer" varStatus="st">
                            <tr id="tr_${answer.answerId}"
                                class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap>${answer.answerId}</td>
                                <td nowrap style="word-wrap:break-word;" width="700px">${answer.body.text}</td>
                                <td nowrap>
                                    &nbsp;&nbsp; ${profileMap[answer.answerProfileId]==null?answer.answerProfileId:profileMap[answer.answerProfileId].nick}
                                    <c:if test="${profileMap[answer.answerProfileId]!=null}">
                                        <input type="button" name="toforbiduser"
                                               onclick="toforbiduser('${profileMap[answer.answerProfileId].nick}');"
                                               class="default_button" value="禁言"/>
                                    </c:if>
                               &nbsp;&nbsp; <a href="/wanba/virtual/postreplypage?answerid=${answer.answerId}&parentid=0">虚拟用户评论</a>

                                            <c:if test="${not empty classify}">
                                                &nbsp;&nbsp;&nbsp;&nbsp; <a href="javascript:void(0);" onclick="accept(${question.questionId},${answer.answerId})">采纳</a>
                                            </c:if>
                                </td>
                                <td nowrap id="td_rstatus_${answer.answerId}"><fmt:message
                                        key="wanba.removestatus.${answer.removeStatus.code}"
                                        bundle="${toolsProps}"/></td>
                                <td nowrap id="td_action_${answer.answerId}">
                                    <c:if test="${answer.removeStatus.code==0}">
                                        <a href="javascript:void(0);" name="delete-answer"
                                           data-aid="${answer.answerId}">删除</a>
                                    </c:if>
                                </td>

                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="5" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="5" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="5" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="10">
                            <pg:pager url="/wanba/ask/answer/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="qid" value="${qid}"/>
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