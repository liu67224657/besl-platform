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
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 大动漫管理 >> 大动漫首页管理</td>
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
                        <td height="1" colspan="10" class="error_msg_td">
                            <fmt:message key="${errorMsg}" bundle="${error}"/>

                        </td>
                    </tr>
                </c:if>
                <tr>
                    <%--<td height="1" class="default_line_td"><input type="hidden" id="appkey" value="${appKey}"/></td>--%>
                </tr>
            </table>
            <table>
                <tr>
                    <td>
                        <form action="/anime/index/createpage" method="post">
                            <table width="100%">
                                <tr>
                                    <td>
                                        <select name="appkey">
                                            <option value="0G30ZtEkZ4vFBhAfN7Bx4v" <c:if test="${appkey=='0G30ZtEkZ4vFBhAfN7Bx4v'}">selected</c:if>>海贼迷</option>
                                            <option value="1zBwYvQpt3AE6JsykiA2es" <c:if test="${appkey=='1zBwYvQpt3AE6JsykiA2es'}">selected</c:if>>火影迷</option>
                                        </select>
                                    </td>
                                    <td>
                                        <input type="submit" name="button" class="default_button" value="新增一条数据"/>
                                    </td>
                                    <td>
                                    </td>
                                </tr>
                            </table>
                        </form>

                    </td>
                    <td>
                         <form action="/anime/index/list" method="post">
                            <table width="100%">
                                <tr>
                                    <td>
                                        <select name="appkey">
                                            <option value="0G30ZtEkZ4vFBhAfN7Bx4v" <c:if test="${appkey=='0G30ZtEkZ4vFBhAfN7Bx4v'}">selected</c:if>>海贼迷</option>
                                            <option value="1zBwYvQpt3AE6JsykiA2es" <c:if test="${appkey=='1zBwYvQpt3AE6JsykiA2es'}">selected</c:if>>火影迷</option>
                                        </select>
                                    </td>
                                    <td>
                                        <select name="platform">
                                            <option value="">请选择</option>
                                            <option value="0" <c:if test="${platform=='0'}">selected</c:if> >IOS</option>
                                            <option value="1" <c:if test="${platform=='1'}">selected</c:if>>ANDORID</option>
                                        </select>
                                        <input type="submit" name="button" class="default_button" value="按平台查询"/>
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
                    <td height="1" colspan="15" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center" width="">ID</td>
                    <td nowrap align="center" width="">首页名称</td>
                    <td nowrap align="center" width="">标题</td>
                     <td nowrap align="center" width="">操作平台</td>
                    <td nowrap align="center" width="">角标</td>
                    <td nowrap align="center" width="">Code</td>
                    <td nowrap align="center" width="">图下显示文字</td>
                    <td nowrap align="center" width="">跳转类型</td>
                    <td nowrap align="center" width="">图片</td>

                    <%--<td nowrap align="center" width="">appkey</td>--%>
                    <%--<td nowrap align="center" width="">创建时间</td>--%>
                    <td nowrap align="center" width="">状态</td>
                    <td nowrap align="center" width="">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="15" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="dto" varStatus="st">
                            <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap align="center">${dto.animeIndexId}</td>
                                <td nowrap align="center">${dto.line_name}</td>
                                <td nowrap align="center">${dto.title}</td>
                                  <td nowrap align="center">   <fmt:message key="joymeapp.platform.${dto.platform}"
                                                                       bundle="${def}"/>
                                <td nowrap align="center">${dto.superScript}</td>
                                <td nowrap align="center">${dto.code}</td>
                                <td nowrap align="center">${dto.desc}</td>
                                <td nowrap align="center"><fmt:message key="anime.special.attr.${dto.animeRedirectType.code}"
                                                                       bundle="${def}"/>
                                </td>
                                <td nowrap align="center"><img src="${dto.pic_url}" width="50" height="50"></td>

                              </td>
                                    <%--<td nowrap align="center">${dto.appkey}</td>--%>
                                    <%--<td nowrap>${dto.createDate}</td>--%>
                                <td nowrap align="center"
                                        <c:if test="${dto.validStatus.code eq 'removed'}">
                                            style="color:red;"
                                        </c:if>
                                        <c:if test="${dto.validStatus.code eq 'invalid'}">
                                            style="color:#9400D3;"
                                        </c:if>
                                        <c:if test="${dto.validStatus.code eq 'valid'}">
                                            style="color:#008000;"
                                        </c:if>
                                        ><fmt:message
                                        key="anime.remove.status.${dto.validStatus.code}"
                                        bundle="${def}"/></td>
                                <td nowrap align="center">
                                    <a href="/anime/index/modifypage?id=${dto.animeIndexId}">编辑</a>&nbsp;

                                    <c:if test="${dto.validStatus.code eq 'removed'}">
                                        <a href="/anime/index/recover?id=${dto.animeIndexId}&appkey=${appkey}">恢复</a>
                                    </c:if>
                                    <c:if test="${dto.validStatus.code eq 'invalid'}">
                                        <a href="/anime/index/recover?id=${dto.animeIndexId}&appkey=${appkey}">发布</a>
                                    </c:if>
                                    <c:if test="${dto.validStatus.code eq 'valid'}">
                                        <a href="/anime/index/delete?id=${dto.animeIndexId}&appkey=${appkey}">删除</a>
                                    </c:if>

                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="15" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="15" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="15" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="10">
                            <pg:pager url="/anime/index/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="appkey" value="${appKey}"/>
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