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
    <script>
        $(document).ready(function() {
            $('#agreeBatch').bind("submit", function() {
                var contentId = $.trim($('#contentId').val());
                if (contentId.length == 0) {
                    alert("请填写文章ID");
                    return false;
                }
                if(isNaN(contentId)){
                    alert("文章ID请填写数字");
                    return false;
                }
                var agreeNum = $.trim($('#agreeNum').val());
                if (agreeNum.length == 0) {
                    alert("请填写点赞数");
                    return false;
                }
                if(isNaN(agreeNum)){
                    alert("点赞数请填写数字");
                    return false;
                }

                var accountType =$("#accountType").val();
                if(accountType==0 && agreeNum>${defaultNum}){//虚拟用户
                    alert("点赞数必须小于虚拟用户总数");
                    return false;
                }else if(accountType==1 && agreeNum>${formalNum}){//运营
                    alert("点赞数必须小于运营用户总数");
                    return false;
                }

            });
        });
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
                    <td class="list_table_header_td">文章列表</td>
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
                        <form action="/joymeapp/socialclient/content/list" method="post">
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
                                            <option value="y" <c:if test="${status=='y'}">selected</c:if>>已删除
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
            <table>
                <tr>
                    <td>
                        <form action="/joymeapp/socialclient/virtual/agree" method="post" id="agreeBatch">
                            <table width="100%">
                                <tr>
                                    <td height="1" class="default_line_td">
                                        批量点赞:
                                    </td>
                                    <td height="1">
                                        文章ID：<input type="text" id="contentId" name="contentId" class="default_button" size="30"/>
                                    </td>
                                    <td>
                                        点赞数：<input type="text" id="agreeNum" name="agreeNum" class="default_button" size="30" />
                                    </td>
                                    <td height="1">
                                        <select name="timerType">
                                            <option value="0" selected="selected">每1分钟执行一次</option>
                                            <option value="1">每5分钟执行一次</option>
                                            <option value="2">每10分钟执行一次</option>
                                            <option value="3">每15分钟执行一次</option>
                                            <option value="4">每30分钟执行一次</option>
                                        </select>
                                    </td>
                                    <td height="1">
                                        <select name="accountType" id="accountType">
                                            <option value="0" selected="selected">虚拟用户批量点赞(共${defaultNum})</option>
                                            <option value="1">运营号批量点赞(共${formalNum})</option>
                                        </select>
                                    </td>
                                    <td>
                                          <input type="submit" name="button" class="default_button" value="确定"/>
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
                    <td nowrap align="center" width="">标题</td>
                    <td nowrap align="center" width="">缩略图</td>
                    <td nowrap align="center" width="">发帖人</td>
                    <td nowrap align="center" width="">创建时间</td>
                    <td nowrap align="center" width="">平台</td>
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
                                              target="_blank">${dto.title}</a></td>
                                <td nowrap><a href="/joymeapp/socialclient/content/getbyid?cid=${dto.contentId}"
                                              target="_blank"><img src="${dto.pic.pic_s}" height="80" width="80"/></a>
                                </td>
                                <td nowrap>
                                    <c:forEach items="${mapprofile}" var="map">
                                        <c:if test="${map.key==dto.uno}">${map.value.blog.screenName}</c:if>
                                    </c:forEach>
                                </td>
                                <td nowrap>${dto.createTime}</td>
                                <td nowrap><fmt:message key="joymeapp.platform.${dto.socialContentPlatformDomain.code}"
                                                        bundle="${def}"/></td>
                                <td nowrap>
                                    <c:if test="${status!='y'}">
                                        <c:choose>
                                            <c:when test="${page.maxPage>1}">
                                                <a href="/joymeapp/socialclient/content/delete?cid=${dto.contentId}&uno=${dto.uno}&curpage=${page.curPage}">删除</a>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="/joymeapp/socialclient/content/delete?cid=${dto.contentId}&uno=${dto.uno}&curpage=0">删除</a>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:if>
                                    &nbsp;<a href="/joymeapp/socialclient/content/createhot?cid=${dto.contentId}&uno=${dto.uno}&offset=${page.startRowIdx}">添加到热门</a>
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
                            <pg:pager url="/joymeapp/socialclient/content/list"
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