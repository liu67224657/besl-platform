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
        function sort(sort, itemid, platform) {
            if (platform == '') {
                alert("请先选择专题平台");
                return false;
            }
            $.post("/anime/special/sort/" + sort, {id: itemid,platform:platform,appkey:'${appkey}'}, function (req) {
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
                        <form action="/anime/special/createpage" method="post">
                            <table width="100%">
                                <tr>
                                    <td>
                                        <select name="appkey">
                                            <option value="0G30ZtEkZ4vFBhAfN7Bx4v" <c:if test="${appkey=='0G30ZtEkZ4vFBhAfN7Bx4v'}">selected</c:if>>海贼迷</option>
                                            <option value="1zBwYvQpt3AE6JsykiA2es" <c:if test="${appkey=='1zBwYvQpt3AE6JsykiA2es'}">selected</c:if>>火影迷</option>
                                        </select>
                                    </td>
                                    <td>
                                        <input type="submit" name="button" class="default_button" value="创建新视频专题"/>
                                    </td>
                                    <td>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </td>
                    <td>
                        <form action="/anime/special/list" method="post">
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
                                            <option value="0" <c:if test="${platform=='0'}">selected</c:if>>IOS</option>
                                            <option value="1" <c:if test="${platform=='1'}">selected</c:if>>Android
                                            </option>
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
                    <td nowrap align="center" width="">专题名</td>
                    <td nowrap align="center" width="">排序操作</td>
                    <td nowrap align="center" width="">封面图</td>
                    <td nowrap align="center" width="">专题头图</td>
                    <td nowrap align="center" width=""> 专题分类</td>
                    <td nowrap align="center" width=""> 背景颜色</td>
                    <td nowrap align="center" width=""> 跳转分类</td>
                    <%--<td nowrap align="center" width=""> 直达链接</td>--%>
                    <td nowrap align="center" width=""> 所属平台</td>
                    <td nowrap align="center" width="">状态</td>
                    <td nowrap align="center" width="">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="15" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="dto" varStatus="st">
                            <tr id="clientitem_${dto.specialId}"
                                class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap align="center">${dto.specialId}</td>
                                <td nowrap align="center">
                                    <c:if test="${dto.animeRedirectType.code eq '3'}">
                                        ${dto.specialName}
                                    </c:if>
                                    <c:if test="${dto.animeRedirectType.code ne '3'}">
                                        <a href="/anime/special/item/list?specialid=${dto.specialId}&specialattr=${dto.animeRedirectType.code}">${dto.specialName}</a>
                                    </c:if>
                                </td>
                                <td nowrap align="center">
                                    <a href="javascript:sort('up','${dto.specialId}','${platform}')"><img
                                            src="/static/images/icon/up.gif"></a>
                                    &nbsp;
                                    <a href="javascript:sort('down','${dto.specialId}','${platform}')"><img
                                            src="/static/images/icon/down.gif"></a>
                                </td>
                                <td nowrap align="center"><img src="${dto.coverPic}" width="50" height="50"></td>
                                <td nowrap align="center"><img src="${dto.specialPic}" width="50" height="50"></td>
                                <td nowrap align="center">
                                    <fmt:message key="special.category.type.${dto.specialType.code}" bundle="${def}"/>
                                </td>
                                <td nowrap align="center"><fmt:message key="anime.type.bgcolo.${dto.specialTypeBgColor}" bundle="${def}"/></td>
                                <td nowrap align="center"><fmt:message
                                        key="anime.special.attr.${dto.animeRedirectType.code}"
                                        bundle="${def}"/></td>

                                <%--<td nowrap align="center">${dto.linkUrl}</td>--%>
                                <td nowrap align="center">
                                    <fmt:message key="joymeapp.favorite.platform.${dto.platform}" bundle="${def}"/>
                                </td>
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
                                    <a href="/anime/special/modifypage?id=${dto.specialId}">编辑</a>&nbsp;
                                    <c:if test="${dto.removeStatus.code eq 'removed'}">

                                        <a href="/anime/special/recover?id=${dto.specialId}&appkey=${appkey}">恢复</a>
                                    </c:if>
                                    <c:if test="${dto.removeStatus.code eq 'invalid'}">
                                        <a href="/anime/special/recover?id=${dto.specialId}&appkey=${appkey}">发布</a>
                                         <a href="/anime/special/delete?id=${dto.specialId}&appkey=${appkey}">删除</a>
                                    </c:if>
                                    <c:if test="${dto.removeStatus.code eq 'valid'}">

                                        <a href="/anime/special/delete?id=${dto.specialId}&appkey=${appkey}">删除</a>
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
                            <pg:pager url="/anime/special/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="appkey" value="${appkey}"/>
                                <pg:param name="platform" value="${platform}"/>
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