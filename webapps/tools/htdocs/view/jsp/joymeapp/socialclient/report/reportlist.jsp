<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>社交举报后台</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 分类管理 >> 社交端举报后台</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">举报列表</td>
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
                        <form action="/joymeapp/socialclient/report/list" method="post">
                            <table width="100%">
                                <tr>
                                    <td height="1" class="default_line_td">
                                        选择渠道:
                                    </td>
                                    <td height="1">
                                        <select name="status" id="select_channel">
                                            <option value="">请选择</option>
                                            <option value="invalid"
                                                    <c:if test="${status=='invalid'}">selected</c:if> >未审核
                                            </option>
                                            <option value="valid" <c:if test="${status=='valid'}">selected</c:if>>已忽略
                                            </option>
                                            <option value="removed" <c:if test="${status=='removed'}">selected</c:if>>
                                                已删除
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

                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="16" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center" width="">文章ID</td>
                    <td nowrap align="center" width="">查看文章</td>
                    <td nowrap align="center" width="">评论ID</td>
                    <td nowrap align="center" width="">缩略图</td>
                    <td nowrap align="center" width="">录音</td>
                    <td nowrap align="center" width="">被举报用户</td>
                    <td nowrap align="center" width="">举报用户</td>
                    <td nowrap align="center" width="">时间</td>
                    <td nowrap align="center" width="">举报类型</td>
                    <td nowrap align="center" width="">举报原因</td>
                    <td nowrap align="center" width="">操作</td>

                </tr>
                <tr>
                    <td height="1" colspan="16" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="dto" varStatus="st">
                            <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap>${dto.contentId}</td>
                                <td nowrap><a href="/joymeapp/socialclient/content/getbyid?cid=${dto.contentId}"
                                              target="_blank">查看文章</a></td>

                                <td nowrap>${dto.replyId}</td>
                                <td nowrap>
                                    <c:forEach items="${contentList}" var="con">
                                        <c:if test="${con.contentId==dto.contentId}"><img src="${con.pic.pic_s}" height="80" width="80"/></c:if>
                                    </c:forEach>
                                </td>
                <td nowrap>
                    <c:forEach items="${contentList}" var="con">
                    <c:if test="${con.contentId==dto.contentId}"> <audio controls="controls"><source src="${con.audio.mp3}" type="audio/mpeg"><embed src="${con.audio.mp3}" mastersound hidden="true" loop="false"
                                                                        autostart="false" width="100" height="70" controls="console"></embed></audio></c:if>
                    </c:forEach>
                </td>
                            <td nowrap>
                                    <c:forEach items="${mapprofile}" var="map">
                                        <c:if test="${map.key==dto.postUno}">${map.value.blog.screenName}</c:if>
                                    </c:forEach>
                               </td>
                                <td nowrap>
                                    <c:forEach items="${mapprofile}" var="map">
                                    <c:if test="${map.key==dto.uno}">${map.value.blog.screenName}</c:if>
                                   </c:forEach>
                                </td>
                                <td nowrap>${dto.createDate}</td>

                                <td nowrap><fmt:message key="social.report.type.${dto.reportType.code}"
                                                        bundle="${def}"/></td>
                                <td nowrap><fmt:message key="social.report.reason.type.${dto.reportReason}"
                                                        bundle="${def}"/></td>

                                <td nowrap>
                                    <c:if test="${status=='invalid'}">
                                        <a href="/joymeapp/socialclient/report/modify?reportid=${dto.reportId}&type=${dto.reportType.code}&validstatus=removed">删除</a>
                                        <a href="/joymeapp/socialclient/report/modify?reportid=${dto.reportId}&type=${dto.reportType.code}&validstatus=valid">忽略</a>
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
                            <pg:pager url="/joymeapp/socialclient/report/list"
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