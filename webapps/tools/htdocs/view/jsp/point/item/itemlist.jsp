<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script>
        function sort(sort, activitytopmenuid) {
            var appkey = $("#appkey").val();
            $.post("/joymeapp/activitytopmenu/sort/" + sort, {activitytopmenuid:activitytopmenuid,appkey:appkey}, function(req) {
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

        function modifySort(index) {

            $("#displayOrder" + index).removeAttr("disabled");
            $("#save" + index).css("display", "inline");
            $("#cancel" + index).css("display", "inline");
            $("#update" + index).css("display", "none");
        }

        function cancelSort(index, displayorder) {
            $("#displayOrder" + index).val(displayorder);
            $("#displayOrder" + index).attr("disabled", "true");
            $("#save" + index).css("display", "none");
            $("#cancel" + index).css("display", "none");
            $("#update" + index).css("display", "inline");
        }

        function submitSort(id, index) {
            var display = $("#displayOrder" + index).val();
            if (isNaN(display)) {
                alert("请填写数字");
                return false;
            }
            var type = $("[name='type']").val();
            var name = $("#subjectName").val();
            var activityType = $("#activityType").val();
            window.location = "/point/exchangegoods/modifygift?itemid=" + id + "&displayorder=" + display + "&type=" + type;
        }
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 条目管理 >> <c:choose>
            <c:when test="${type eq 1}">
                微信礼包搜索页热词推荐列表
            </c:when>
            <c:otherwise>
                PC独家礼包列表
            </c:otherwise>
        </c:choose>
        </td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
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
                        <form action="/point/exchangegoods/addgiftpage" method="post">
                            <table width="100%">
                                <tr>
                                    <td>
                                        <input type="hidden" name="lineid" value="${lineId}"/>
                                        <input type="hidden" name="type" name="type" value="${type}"/>

                                        <input type="submit" name="button" class="default_button"  <c:choose>
                                            <c:when test="${type eq 1}">
                                                value="添加新的热词"
                                            </c:when>
                                            <c:otherwise>
                                                value="添加新的礼包"
                                            </c:otherwise>
                                        </c:choose> />
                                    </td>
                                    <td>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </td>
                    <td>
                        <%--<table>--%>
                        <%--<tr>--%>
                        <%--<td>--%>
                        <%--<form action="/joymeapp/activitytopmenu/createpage" method="post">--%>
                        <%--<input type="submit" name="button" class="default_button" value="添加活动轮播图"/>--%>
                        <%--</form>--%>
                        <%--</td>--%>
                        <%--</tr>--%>
                        <%--</table>--%>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="15" class="default_line_td"></td>
                </tr>

                <tr class="list_table_title_tr">
                    <td nowrap align="center" width="">ID</td>
                    <c:choose>
                        <c:when test="${type eq 1}">
                            <td nowrap align="center" width="">热词名称</td>
                        </c:when>
                        <c:otherwise>
                            <td nowrap align="center" width="">礼包ID</td>
                            <td nowrap align="center" width="">礼包名称</td>
                            <td nowrap align="center" width="">图片</td>
                        </c:otherwise>
                    </c:choose>

                    <td nowrap align="center" width="">排序</td>


                    <td nowrap align="center" width="">状态</td>
                    <td nowrap align="center" width="">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="15" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${not empty list}">
                        <c:forEach items="${list}" var="dto" varStatus="st">
                            <tr align="center" id="clientitem_${dto.itemId}"
                                class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">

                                <td nowrap>${dto.itemId}</td>
                                <c:choose>
                                    <c:when test="${type eq 1}">
                                        <td nowrap>${dto.title}</td>
                                    </c:when>
                                    <c:otherwise>
                                        <td nowrap>${dto.directId}</td>
                                        <td nowrap>${dto.title}</td>
                                        <td nowrap><img src="${dto.picUrl}" width="100px;" height="50"/></td>
                                    </c:otherwise>
                                </c:choose>

                                <td nowrap>
                                    <input type="text" size="10" disabled="disabled"
                                           onkeyup="this.value=this.value.replace(/[^\d]/g,'') "
                                           id="displayOrder${st.index}"
                                           name="displayorder"
                                           value="${dto.displayOrder}"/>
                                    <input type="button" onclick="modifySort(${st.index});" id="update${st.index}"
                                           value="修改"/>
                                    <input type="button" style="display:none;"
                                           onclick="submitSort(${dto.itemId},${st.index})"
                                           id="save${st.index}" value="保存"/>
                                    <input type="button" style="display:none;"
                                           onclick="cancelSort(${st.index},${dto.displayOrder})"
                                           id="cancel${st.index}" value="取消"/>

                                </td>

                                <td nowrap>
                                    <c:choose>
                                        <c:when test="${dto.validStatus.code eq 'valid'}">
                                            <span style="color: #008000;">
                                                <fmt:message key="client.line.status.${dto.validStatus.code}"
                                                             bundle="${def}"/>
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <span style="color: #ff0000;">
                                                <fmt:message key="client.line.status.${dto.validStatus.code}"
                                                             bundle="${def}"/>
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td nowrap>
                                    <a href="/point/exchangegoods/modifygiftpage?itemId=${dto.itemId}&type=${type}">编辑</a>&nbsp;
                                    <c:choose>
                                        <c:when test="${dto.validStatus.code eq 'valid'}">
                                            <a href="/point/exchangegoods/addgiftdelete?itemid=${dto.itemId}&valid=removed&type=${type}">删除</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="/point/exchangegoods/addgiftdelete?itemid=${dto.itemId}&valid=valid&type=${type}">恢复</a>
                                        </c:otherwise>
                                    </c:choose>

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
                            <pg:pager url="/point/exchangegoods/exclusive"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="linecode" value="${code}"/>
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