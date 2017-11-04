<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>活动管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script>
        function sort(sort, activityId) {

            $.post("/activity/exchange/sort/" + sort, {activityid: activityId}, function (req) {
                var resMsg = eval('(' + req + ')');
                if (resMsg.rs == '0') {
                    return false;
                } else {
                    var result = resMsg.result;
                    if (result == null) {

                    } else {
                        var itemid = result.activityid;
                        var sort = result.sort;
                        var returnid = result.returnitemid;
                        if (sort == 'up') {
                            var item = $("#activity_" + itemid).clone();
                            $("#activity_" + itemid).remove();
//
                            $("#activity_" + returnid).before(item);
                            var itemclass = item.attr("class");
                            var upclass = $("#activity_" + returnid).attr("class");
                            item.removeClass(itemclass);
                            item.addClass(upclass);
                            $("#activity_" + returnid).addClass(itemclass);
                            $("#activity_" + returnid).removeClass(upclass);
                        } else {
                            var item = $("#activity_" + itemid).clone();
                            $("#activity_" + itemid).remove();
                            var itemclass = item.attr("class");
                            var upclass = $("#activity_" + returnid).attr("class");
                            item.removeClass(itemclass);
                            item.addClass(upclass);
                            $("#activity_" + returnid).addClass(itemclass);
                            $("#activity_" + returnid).removeClass(upclass);
                            $("#activity_" + returnid).after(item);
                        }
                    }
                }
            });
        }
        $(document).ready(function () {
            $('#form_submit_p').bind('submit', function () {
                var platform = Number(0);
                $("input[name = pbox]:checkbox").each(function () {
                    if ($(this).is(":checked")) {
                        platform += Number($(this).attr("value"));
                    }
                });
                $('#input_hidden_platform').val(platform);
            });

            $('#form_submit_c').bind('submit', function () {
                var cooperation = Number(0);
                $("input[name = cbox]:checkbox").each(function () {
                    if ($(this).is(":checked")) {
                        cooperation += Number($(this).attr("value"));
                    }
                });
                $('#input_hidden_cooperation').val(cooperation);
            });

            $('select[id^=select_appkey_]').each(function () {
                var appKey = $(this).val();
                var index = $(this).attr('id').replace('select_appkey_', '');
                var typeTags = $('#select_type_tags_' + index).val();
                $('#a_push_createpage_' + index).attr('href', '/joymeapp/push/createpage?' + appKey + '&' + typeTags);
            });
        });
        function addParam(idx) {
            var appKey = $('#select_appkey_'+idx).val();
            var typeTags = $('#select_type_tags_' + idx).val();
            $('#a_push_createpage_' + idx).attr('href', '/joymeapp/push/createpage?' + appKey + '&' + typeTags);
        }
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
    <td height="22" class="page_navigation_td">>> 运营管理 >> 条目管理 >> 活动管理</td>
</tr>
<tr>
<td height="100%" valign="top"><br>
<table border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td class="list_table_header_td">活动列表</td>
    </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <c:if test="${fn:length(errorMsg)>0}">
        <tr>
            <td height="1" colspan="13" class="error_msg_td">
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
            <form method="post" action="/activity/exchange/createpage">
                <table>
                    <tr>
                        <td>
                            <p:privilege name="/activity/exchange/createpage">
                                <input type="submit" name="button" class="default_button" value="添加活动"/>
                            </p:privilege>
                        <td>
                    <tr>
                </table>
            </form>
        </td>
    </tr>
</table>
<table>
    <tr>
        <td>
            <form method="post" action="/activity/exchange/list" id="form_submit_p">
                <table>
                    <tr>
                        <td>
                            平台：
                        </td>
                        <td>
                            <input type="checkbox" name="pbox" value="1"
                            <c:if test="${platform.hasAndroid()}">checked="checked"</c:if>/>Android
                        </td>
                        <td>
                            <input type="checkbox" name="pbox" value="2"
                            <c:if test="${platform.hasIos()}">checked="checked"</c:if>/>IOS
                        </td>
                        <td>
                            <input type="checkbox" name="pbox" value="3"
                            <c:if test="${platform.hasAll()}">checked="checked"</c:if>/>全部
                        </td>
                        <td>
                            <input type="hidden" name="platform" id="input_hidden_platform"/>
                        </td>
                        <td>
                            <p:privilege name="/activity/exchange/list">
                                <input type="submit" name="button" class="default_button" value="查询"/>
                            </p:privilege>
                        <td>
                    <tr>
                </table>
            </form>
        </td>
    </tr>
</table>
<table>
    <tr>
        <td>
            <form method="post" action="/activity/exchange/list" id="form_submit_c">
                <table>
                    <tr>
                        <td>
                            合作专区：
                        </td>
                        <td>
                            <input type="checkbox" name="cbox" value="1"
                            <c:if test="${cooperation.hasJiuYao()}">checked="checked"</c:if>/>91
                        </td>
                        <td>
                            <input type="checkbox" name="cbox" value="2"
                            <c:if test="${cooperation.hasSanLiuLing()}">checked="checked"</c:if>/>360
                        </td>
                        <td>
                            <input type="checkbox" name="cbox" value="3"
                            <c:if test="${cooperation.hasAll()}">checked="checked"</c:if>/>全部
                        </td>
                        <td>
                            <input type="hidden" name="cooperation" id="input_hidden_cooperation"/>
                        </td>
                        <td>
                            <p:privilege name="/activity/exchange/list">
                                <input type="submit" name="button" class="default_button" value="查询"/>
                            </p:privilege>
                        <td>
                    <tr>
                </table>
            </form>
        </td>
    </tr>
</table>
<table>
    <tr>
        <td>
            <form method="post" action="/activity/exchange/list" id="form_submit_c">
                <table>
                    <tr>
                        <td>
                            活动名称(模糊)： <input type="text" name="activityname" id=""/>
                        </td>
                        <td>
                            <p:privilege name="/activity/exchange/list">
                                <input type="submit" name="button" class="default_button" value="查询"/>
                            </p:privilege>
                        <td>
                    <tr>
                </table>
            </form>
        </td>
    </tr>
</table>
<form action="/point/goods/list" method="post">
    <table width="100%" border="0" cellspacing="1" cellpadding="0">
        <tr>
            <td height="1" colspan="13" class="default_line_td"></td>
        </tr>
        <c:if test="${fn:length(errorMsg)>0}">
            <tr>
                <td height="1" colspan="13" class="error_msg_td">
                    <b><fmt:message key="${errorMsg}" bundle="${error}"/></b>
                </td>
            </tr>
        </c:if>
        <tr class="list_table_title_tr">
            <td nowrap align="left" width="80">活动ID</td>
            <td nowrap align="left" width="80">活动标题</td>
            <td nowrap align="left" width="120">活动图片</td>
            <td nowrap align="left">排序</td>
            <td nowrap align="left">状态</td>
            <td nowrap align="left" width="120">开始时间</td>
            <td nowrap align="left" width="120">结束时间</td>
            <td nowrap align="left" width="100">游戏名称</td>
            <td nowrap align="left" width="80">游戏ICON图</td>
            <%--<td nowrap align="left" width="130">商品过期时间</td>--%>
            <td nowrap align="left" width="150">专区地址</td>
            <td nowrap align="left" width="150">跳转链接</td>
            <td nowrap align="left" width="150">IOS下载地址</td>
            <td nowrap align="left" width="150">安卓下载地址</td>
            <td nowrap align="left" width="200">关注游戏推送</td>
            <td nowrap align="left">操作</td>
        </tr>
        <tr>
            <td height="1" colspan="13" class="default_line_td"></td>
        </tr>
        <c:choose>
            <c:when test="${list.size() > 0}">
                <c:forEach items="${list}" var="activity" varStatus="st">
                    <tr id="activity_${activity.activityId}"
                        class="<c:choose><c:when test="
                    ${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
        <td nowrap align="left">${activity.activityId}</td>
        <td nowrap align="left">${activity.activitySubject}</td>
        <td nowrap align="left"><img src="${activity.activityPicUrl}" height="30" width="100"/></td>
        <td nowrap>
            <a href="javascript:sort('up','${activity.activityId}')"><img
                    src="/static/images/icon/up.gif"></a>
            <a href="javascript:sort('down','${activity.activityId}')"><img
                    src="/static/images/icon/down.gif"></a>
        </td>
        <td nowrap align="left"
        <c:if test="${activity.removeStatus.code=='n'}">style="color:red"</c:if> >
        <fmt:message key="activity.exchange.type.${activity.removeStatus.code}"
                     bundle="${def}"/></td>
        <td nowrap align="left">${activity.startTime}</td>
        <td nowrap align="left">${activity.endTime}</td>
        <td nowrap align="left">${activity.gameName}</td>
        <td nowrap align="left"><img src="${activity.gameIconUrl}" height="30" width="100"/>
        </td>
        <%--<td nowrap align="left" width="130">商品过期时间</td>--%>
        <td nowrap align="left">${activity.gameUrl}</td>
        <td nowrap align="left">${URL_WWW}/appclick/${activity.qrUrl}</td>
        <td nowrap align="left">${activity.iosDownloadUrl}</td>
        <td nowrap align="left">${activity.androidDownloadUrl}</td>
        <td nowrap align="left">
            <select id="select_appkey_${st.index}" onchange="addParam(${st.index})">
                <option value="appid=25AQWaK997Po2x300CQeP0" selected="selected">着迷玩霸</option>
            </select>
            <select id="select_type_tags_${st.index}" onchange="addParam(${st.index})">
                <option value="pushlisttype=4&tags=${activity.activityId}">预定礼包</option>
                <c:if test="${activity.gameDbId > 0}">
                    <option value="pushlisttype=3&tags=game${activity.gameDbId}">关注游戏</option>
                </c:if>
            </select>
            <a href="#" target="_blank" id="a_push_createpage_${st.index}">去创建一条推送消息</a>
        </td>
        <td nowrap align="left">
            <a href="/activity/exchange/modifypage?activityId=${activity.activityId}">修改</a>
            <c:choose>
                <c:when test="${activity.removeStatus.code=='n'}">
                    <a href="/activity/exchange/recover?activityId=${activity.activityId}&gameid=${activity.gameDbId}">激活</a>
                </c:when>
                <c:otherwise>
                    <a href="/activity/exchange/delete?activityId=${activity.activityId}">删除</a>
                </c:otherwise>
            </c:choose>
        </td>
        </tr>
        </c:forEach>
        <tr>
            <td height="1" colspan="13" class="default_line_td"></td>
        </tr>
        </c:when>
        <c:otherwise>
            <tr>
                <td colspan="13" class="error_msg_td">暂无数据!</td>
            </tr>
        </c:otherwise>
        </c:choose>
        <tr>
            <td colspan="13" height="1" class="default_line_td"></td>
        </tr>
        <c:if test="${page.maxPage > 1}">
            <tr class="list_table_opp_tr">
                <td colspan="13">
                    <pg:pager url="/activity/exchange/list"
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
</form>
</td>
</tr>
</table>
</body>
</html>