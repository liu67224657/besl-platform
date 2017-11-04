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
    <script>
        function sort(sort, itemid, specialid) {
            $.post("/anime/special/item/sort/" + sort, {id: itemid,specialid:specialid}, function (req) {
                var resMsg = eval('(' + req + ')');
                if (resMsg.rs == '0') {
                    return false;
                } else {
                    var result = resMsg.result;

                    if (result == null) {

                    } else {
                        var itemid = result.itemid;
                        var sort = result.sort;
                        var returnid = result.returnitemid;
                        if (sort == 'up') {
                            var item = $("#clientitem_" + itemid).clone();
                            $("#clientitem_" + itemid).remove();
                            var itemclass = item.attr("class");
                            var upclass = $("#clientitem_" + returnid).attr("class");
                            item.removeClass(itemclass);
                            item.addClass(upclass);
                            $("#clientitem_" + returnid).addClass(itemclass);
                            $("#clientitem_" + returnid).removeClass(upclass);
                            $("#clientitem_" + returnid).before(item);
                        } else {
                            var item = $("#clientitem_" + itemid).clone();
                            $("#clientitem_" + itemid).remove();
                            var itemclass = item.attr("class");
                            var upclass = $("#clientitem_" + returnid).attr("class");
                            item.removeClass(itemclass);
                            item.addClass(upclass);
                            $("#clientitem_" + returnid).addClass(itemclass);
                            $("#clientitem_" + returnid).removeClass(upclass);
                            $("#clientitem_" + returnid).after(item);
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
        <td height="22" class="page_navigation_td">>> 运营维护 >> 大动漫管理 >> 大动漫专题维护</td>
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
                        <form action="/anime/special/item/createpage" method="post">
                            <table width="100%">
                                <tr>
                                    <td>
                                        <b> 所属专题：${animeSpecial.specialName} <br/> <br/>
                                            专题属性：<fmt:message key="anime.special.attr.${animeSpecial.animeRedirectType.code}"
                                                              bundle="${def}"/> </b>
                                        <input type="hidden" name="specialid" value="${specialId}"/>
                                        <input type="hidden" name="attr" value="${animeSpecial.animeRedirectType.code}"/>

                                    </td>

                                    <td>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <input type="submit" name="button" class="default_button" value="创建详情"/>
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
                    <c:if test="${animeSpecial.animeRedirectType.code==1}">
                        <td nowrap align="center">视频ID</td>
                    </c:if>
                    <c:if test="${animeSpecial.animeRedirectType.code==2}">
                        <td nowrap align="center">标题</td>
                        <td nowrap align="center">链接</td>
                        <td nowrap align="center">描述</td>
                        <td nowrap align="center">图片</td>
                    </c:if>
                    <c:if test="${animeSpecial.animeRedirectType.code==3}">
                        <td nowrap align="center">直达链接（只会返回最顶部的一条可用数据给客户端）</td>
                    </c:if>
                      <c:if test="${animeSpecial.animeRedirectType.code==4}">
                        <td nowrap align="center">标题</td>
                        <td nowrap align="center">链接</td>
                        <td nowrap align="center">描述</td>
                    </c:if>
                    <td nowrap align="center">排序</td>
                    <td nowrap align="center">更新时间</td>
                    <td nowrap align="center">更新人</td>
                    <td nowrap align="center" width="">状态</td>
                    <td nowrap align="center" width="">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="15" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="dto" varStatus="st">
                            <tr id="clientitem_${dto.specialItemId}"
                                class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap align="center">${dto.specialItemId}</td>
                                <c:if test="${animeSpecial.animeRedirectType.code==1}">
                                    <td nowrap align="left"><a href="/joymeapp/anime/tv/modifypage?tv_id=${dto.tvId}&pager.offset=0">${dto.tvId}</a></td>
                                </c:if>
                                <c:if test="${animeSpecial.animeRedirectType.code==2}">
                                    <td nowrap align="left">${dto.title}</td>
                                    <td nowrap align="left"><a href="${dto.linkUrl}" target="_blank">点击查看</a></td>
                                    <td nowrap align="left">${dto.desc}</td>
                                    <td nowrap align="left">
                                        <img src="${dto.pic}" width="50" height="50"></td>
                                </c:if>
                                <c:if test="${animeSpecial.animeRedirectType.code==3}">
                                    <td nowrap align="left"><a href="${dto.linkUrl}" target="_blank">点击查看</a></td>
                                </c:if>
                                    <c:if test="${animeSpecial.animeRedirectType.code==4}">
                                    <td nowrap align="left">${dto.title}</td>
                                    <td nowrap align="left"><a href="${dto.linkUrl}" target="_blank">点击查看</a></td>
                                    <td nowrap align="left">${dto.desc}</td>
                                </c:if>
                                <td nowrap align="center">
                                    <a href="javascript:sort('up','${dto.specialItemId}','${dto.specialId}')"><img
                                            src="/static/images/icon/up.gif"></a>
                                    &nbsp;
                                    <a href="javascript:sort('down','${dto.specialItemId}','${dto.specialId}')"><img
                                            src="/static/images/icon/down.gif"></a>
                                </td>
                                <td nowrap align="center">${dto.updateTime}</td>
                                <td nowrap align="center">${dto.updateUser}</td>
                                <td nowrap align="center"
                                        <c:if test="${dto.removeStatus.code eq 'removed'}">
                                            style="color:red;"
                                        </c:if>
                                        <c:if test="${dto.removeStatus.code eq 'invalid'}">
                                            style="color:#9400D3;"
                                        </c:if>
                                        <c:if test="${dto.removeStatus.code eq 'valid'}">
                                            style="color:#008000;"
                                        </c:if>
                                        >
                                    <fmt:message key="anime.remove.status.${dto.removeStatus.code}" bundle="${def}"/>
                                </td>
                                    <%--<td nowrap align="center">${dto.appkey}</td>--%>
                                    <%--<td nowrap>${dto.createDate}</td>--%>
                                <td nowrap align="center">
                                    <a href="/anime/special/item/modifypage?itemid=${dto.specialItemId}&specialid=${dto.specialId}&attr=${animeSpecial.animeRedirectType.code}">编辑</a>&nbsp;
                                        <c:if test="${dto.removeStatus.code eq  'removed'}">

                                            <a href="/anime/special/item/recover?id=${dto.specialItemId}&specialid=${dto.specialId}">恢复</a>
                                        </c:if>
                                        <c:if  test="${dto.removeStatus.code eq  'invalid'}">
                                            <a href="/anime/special/item/recover?id=${dto.specialItemId}&specialid=${dto.specialId}">发布</a>
                                              <a href="/anime/special/item/delete?itemid=${dto.specialItemId}&specialid=${dto.specialId}">删除</a>
                                        </c:if>
                                       <c:if  test="${dto.removeStatus.code eq  'valid'}">
                                            <a href="/anime/special/item/delete?itemid=${dto.specialItemId}&specialid=${dto.specialId}">删除</a>
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
                            <pg:pager url="/anime/special/item/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="appkey" value="${appKey}"/>
                                <pg:param name="specialid" value="${specialId}"/>

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