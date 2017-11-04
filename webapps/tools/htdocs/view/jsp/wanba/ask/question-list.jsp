<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>问题列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $('a[name=delete-qestion]').click(function () {
                var qid = $(this).attr('data-qid');

                if (confirm('删除问题操作不可逆,确定删除?')) {
                    $.post("/wanba/json/ask/question/delete", {qid: qid}, function (req) {
                        var rsobj = eval('(' + req + ')');
                        if (rsobj.rs == 1) {
                            //$('#tr_' + qid).remove();
                            $('#td_rstatus_' + qid).html('<font color="red">已删除</font>');
                            $('#td_action_' + qid).html('');
                        }
                    })
                }
            });


            //推荐
            $('a[name=askrecommend-qestion]').click(function () {
                var qid = $(this).attr('data-qid');
                if ($.trim(qid) == "") {
                    alert("问题id不能为空");
                    return
                }
                $.post("/wanba/askrecommend/create", {destId: qid}, function (req) {
                    var rsobj = eval('(' + req + ')');
                    if (rsobj.rs == 1) {
                        alert("添加成功");
                    } else {
                        alert(rsobj.msg);
                    }
                });
            });


            //重新激活
            $('a[name=reactivated-qestion]').click(function () {
                var qid = $(this).attr('data-qid');
                var pid = $(this).attr('data-pid');

                $.post("/wanba/virtual/reactivated", {questionid: qid,pid:pid}, function (req) {
                    var rsobj = eval('(' + req + ')');
                    if (rsobj.rs == 1) {
                        alert("激活成功");
                        window.location.reload();
                    } else {
                        alert(rsobj.msg);
                    }
                });
            });
        });
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 玩霸-问答 >><c:if test="${not empty profile}"><span style="color:red">${profile.nick}</span>，的</c:if>问题列表</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td"><c:if test="${not empty profile}"><span style="color:red">${profile.nick}</span>，的</c:if>问题列表</td>
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
                        <form action="/wanba/ask/question/list" method="post">
                            <table>
                                <tr>
                                    <c:if test="${not empty profile}">
                                        <input type="hidden" name="nick" class="" value="${profile.nick}"/>
                                    </c:if>
                                    <td>
                                        问答标题(支持模糊搜索):
                                    </td>
                                    <td>
                                        <input type="text" name="text" class="" value="${text}"/>
                                    </td>
                                    <td>
                                        游戏【抢答有效】:
                                    </td>
                                    <td>
                                        <select name="tagid">
                                            <option value="">全部</option>
                                            <c:forEach items="${tagList}" var="tag" varStatus="st">
                                                <option value="${tag.tag_id}" <c:if test="${tagid==tag.tag_id}">selected</c:if>>${tag.tag_name}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td>
                                        类型:
                                    </td>
                                    <td>
                                        <select name="qtype">
                                            <option value="">全部</option>
                                            <option value="2"
                                                    <c:if test="${qtype==2}">selected</c:if> >
                                                <fmt:message key="wanba.ask.question.type.2" bundle="${toolsProps}"/>
                                            </option>
                                            <option value="1"
                                                    <c:if test="${qtype==1}">selected</c:if> >
                                                <fmt:message key="wanba.ask.question.type.1" bundle="${toolsProps}"/>
                                            </option>
                                        </select>
                                    </td>
                                    <td>
                                        状态:
                                    </td>
                                    <td>
                                        <select name="qstatus">
                                            <option value="">全部</option>
                                            <option value="0"
                                                    <c:if test="${qstatus==0}">selected</c:if> >
                                                <fmt:message key="wanba.ask.question.status.0" bundle="${toolsProps}"/>
                                            </option>
                                            <option value="1"
                                                    <c:if test="${qstatus==1}">selected</c:if> >
                                                <fmt:message key="wanba.ask.question.status.1" bundle="${toolsProps}"/>
                                            </option>
                                            <option value="2"
                                                    <c:if test="${qstatus==2}">selected</c:if> >
                                                <fmt:message key="wanba.ask.question.status.2" bundle="${toolsProps}"/>
                                            </option>
                                        </select>
                                    </td>
                                    <td>
                                        删除状态:
                                    </td>
                                    <td>
                                        <select name="rstatus">
                                            <option value="">全部</option>
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
                <tr>
                    <td height="1" colspan="9" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center" width="70">问题ID</td>
                    <td nowrap align="center" width="150px">操作</td>
                    <td nowrap align="center" width="300px" style="overflow:hidden; white-space:pre;">标题</td>
                    <td nowrap align="center" width="">游戏</td>
                    <td nowrap align="center" width="">类型</td>
                    <td nowrap align="center" width="70">状态</td>
                    <td nowrap align="center" width="">积分</td>
                    <td nowrap align="center" width="">回答数</td>
                    <td nowrap align="center" width="">删除状态</td>
                </tr>
                <tr>
                    <td height="1" colspan="9" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="question" varStatus="st">
                            <tr id="tr_${question.questionId}"
                                class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">


                                <td nowrap><a href="/wanba/ask/answer/list?qid=${question.questionId}">${question.questionId}</a></td>
                                <td width="150px" nowrap id="td_action_${question.questionId}">
                                    <c:if test="${question.removeStatus.code==0}">
                                        <a href="javascript:void(0);" name="delete-qestion"
                                           data-qid="${question.questionId}">删除</a>
                                    </c:if>
                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                    <c:if test="${question.questionStatus.code==1 && question.removeStatus.code==0}"><a href="javascript:void(0);" name="askrecommend-qestion" data-qid="${question.questionId}">推荐</a></c:if>
                                    <c:if test="${question.questionStatus.code!=1}">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</c:if>

                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                    <c:if test="${question.reactivated==0 && question.questionStatus.code==2 && question.removeStatus.code==0}">
                                        <c:if test="${profileClassifyMap[question.askProfileId]!=null}">
                                            <a href="javascript:void(0);" name="reactivated-qestion" data-qid="${question.questionId}" data-pid="${question.askProfileId}">重新激活</a>
                                        </c:if>

                                    </c:if>
                                </td>
                                <td nowrap width="200px" style="overflow:hidden; white-space:pre;" title="${question.title}">${question.title}</td>
                                <td nowrap>
                                    <c:forEach items="${tagList}" var="tag" varStatus="st">
                                        <c:if test="${tag.tag_id==question.gameId}">
                                            ${tag.tag_name}
                                        </c:if>
                                    </c:forEach>
                                <td nowrap><fmt:message key="wanba.ask.question.type.${question.type.code}"
                                                        bundle="${toolsProps}"/></td>


                                <td nowrap><fmt:message key="wanba.ask.question.status.${question.questionStatus.code}"
                                                        bundle="${toolsProps}"/></td>
                                <td nowrap>${question.questionPoint}</td>
                                <td nowrap>${questionSumMap[question.questionId].ansewerSum}</td>
                                <td nowrap id="td_rstatus_${question.questionId}"><fmt:message key="wanba.removestatus.${question.removeStatus.code}"
                                                        bundle="${toolsProps}"/></td>


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
                            <pg:pager url="/wanba/ask/question/list"
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
                                <c:if test="${profile!=null}">
                                    <pg:param name="nick" value="${profile.nick}"/>
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