<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>着迷APP管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script>
        function recommend() {
            var appIdVal = $('#select_appid').val();
            if (appIdVal.length == 0) {
                alert("请选择所属游戏");
                return false;
            }

            var result = new Array();
            $("[name = box]:checkbox").each(function () {
                if ($(this).is(":checked")) {
                    result.push($(this).attr("value"));
                }
            });
            if (result.length == 0) {
                alert("至少要选择一个同类推荐游戏");
                return false;
            }
            var disPlayOrder = new Array();
            $('[name=displayorder]:hidden').each(function () {
                disPlayOrder.push($(this).attr("value"))
            });

            $('#recommdend_form_hidden_appid').val(appIdVal);
            $('#recommdend_form_hidden_destid').val(result.join(','));
            $('#recommend_form_hidden_lastdisplayorder').val(disPlayOrder.join(','));

        }

        function recommendOrFavoriteList() {
            var appIdVal = $('#select_appid').val();
            if (appIdVal.length == 0) {
                alert("请选择所属游戏");
                return false;
            }
            $('#input_hidden_recommend_list_appid').val(appIdVal);
            $('#input_hidden_recommend_temp_list_appid').val(appIdVal);
        }

        $(document).ready(function() {

            $("#checkall").bind("click", function () {
                if ($('#checkall').is(":checked")) {
                    $("[name = box]:checkbox").attr("checked", true);
                } else {
                    $("[name = box]:checkbox").attr("checked", false);
                }
            });

            $("#checkinverse").bind("click", function () {
                var result = new Array();
                $("[name = box]:checkbox").each(function () {
                    if ($(this).is(":checked")) {
                        result.push($(this).attr("value"));
                    }
                });
                if (result.length == 0) {
                    alert("您没有选择任何游戏，无法反选");
                    return false;
                } else {
                    $("[name = box]:checkbox").each(function () {
                        $(this).attr("checked", !$(this).attr("checked"));
                    });
                }
            });
        });
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 着迷APP管理 >> 推荐游戏管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">同类推荐临时列表</td>
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
                        <form action="/joymeapp/recommend/temp/list" method="post">
                            <table width="100%">
                                <tr>
                                    <td height="1" width="60" class="list_table_header_td">
                                        所属游戏:
                                    </td>
                                    <td height="1">
                                        <select name="appid" id="select_appid">
                                            <option value="">请选择</option>
                                            <c:forEach var="app" items="${searchAppList}">
                                                <option value="${app.appInfoId}"
                                                        <c:if test="${app.appInfoId==appId}">selected="selected" </c:if>>${app.appName}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td align="">
                                        <p:privilege name="/joymeapp/recommend/temp/list">
                                            <input type="submit" name="button" class="default_button" value="查询"/>
                                        </p:privilege>
                                    </td>
                                    <td align="">
                                        <p:privilege name="/joymeapp/resource/list">
                                            <a href="/joymeapp/resource/list?appid=${appId}"><input type="button"
                                                                                                    name="button"
                                                                                                    class="default_button"
                                                                                                    value="去添加游戏"/></a>
                                        </p:privilege>
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
                        <form action="/joymeapp/recommend/list" method="post"
                              onsubmit="return recommendOrFavoriteList();"
                              style="vertical-align: middle;height: 13px;line-height: 13px;">
                            <input type="hidden" name="appid" id="input_hidden_recommend_list_appid"/>
                            <p:privilege name="/joymeapp/recommend/list">
                                <input type="submit" name="submit" value="查看手机端同类推荐列表" class="default_button"/>
                            </p:privilege>
                        </form>
                    </td>
                </tr>
            </table>
            <table width="50%">
                <tr>
                    <td width="50">
                        <input type="checkbox" name="all" id="checkall"/>全选
                    </td>
                    <td width="50">
                        <input type="checkbox" name="inverse" id="checkinverse"/>反选
                    </td>
                    <td width="90">
                        <form action="/joymeapp/recommend/increase" method="post" onsubmit="return recommend();"
                              style="vertical-align: middle;height: 13px;line-height: 13px;">
                            <input type="hidden" name="appid" id="recommdend_form_hidden_appid">
                            <input type="hidden" name="destid" id="recommdend_form_hidden_destid">
                            <input type="hidden" name="lastdisplayorder" value=""
                                   id="recommend_form_hidden_lastdisplayorder"/>
                            <p:privilege name="/joymeapp/recommend/increase">
                                <input type="submit" name="submit" value="添加到手机端同类推荐列表" class="default_button"/>
                            </p:privilege>
                        </form>
                    </td>
                </tr>
            </table>
            <form action="/joymeapp/recommend/temp/list" method="post">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="13" class="default_line_td"></td>
                    </tr>
                    <tr class="list_table_title_tr">
                        <td width="20"></td>
                        <td nowrap align="left" width="60">当前排序</td>
                        <td nowrap align="left" width="150">游戏名称</td>
                        <td nowrap align="left" width="200">标签</td>
                        <td nowrap align="center" width="80">排序调整</td>
                        <td nowrap align="left" width="200">录入时间</td>
                        <td nowrap align="left" width="80">操作</td>
                    </tr>
                    <c:choose>
                        <c:when test="${appRecommendTempDTOList.size() > 0}">
                            <c:forEach items="${appRecommendTempDTOList}" var="dto" varStatus="st">
                                <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                    <td nowrap>
                                        <input type="hidden" name="recommendid" id="recommendid"
                                               value="${dto.appRecommendTemp.appRecommendTempId}"/>
                                        <input type="hidden" name="appid" value="${dto.appRecommendTemp.appId}"
                                               id="id"/>
                                        <input type="hidden" name="destid" value="${dto.appRecommendTemp.destAppId}"/>
                                        <input type="hidden" name="displayorder"
                                               value="${dto.appRecommendTemp.displayOrder}"
                                               id="input_hidden_displayorder"/>
                                        <input type="checkbox" name="box" value="${dto.appResource.appinfo.appInfoId}"/>
                                    </td>
                                    <td nowrap>${dto.appRecommendTemp.displayOrder}</td>
                                    <td nowrap>${dto.appRecommendTemp.destAppName}</td>
                                    <td nowrap>
                                        <c:forEach items="${dto.appResource.appTagList}"
                                                   var="tag">${tag.tagName}&nbsp;</c:forEach>
                                    </td>
                                    <td align="center">
                                        <p:privilege name="/joymeapp/recommend/temp/sort/up">
                                            <a href="/joymeapp/recommend/temp/sort/up?appid=${dto.appRecommendTemp.appId}&recommendtempid=${dto.appRecommendTemp.appRecommendTempId}"><img
                                                    src="/static/images/icon/up.gif"></a>
                                        </p:privilege>
                                        &nbsp;
                                        <p:privilege name="/joymeapp/recommend/temp/sort/down">
                                            <a href="/joymeapp/recommend/temp/sort/down?appid=${dto.appRecommendTemp.appId}&recommendtempid=${dto.appRecommendTemp.appRecommendTempId}"><img
                                                    src="/static/images/icon/down.gif"></a>
                                        </p:privilege>
                                    </td>
                                    <td nowrap>${dto.appRecommendTemp.createTime}</td>
                                    <td nowrap>
                                        <p:privilege name="/joymeapp/recommend/temp/delete">
                                            <a href="/joymeapp/recommend/temp/delete?appid=${dto.appRecommendTemp.appId}&recommendtempid=${dto.appRecommendTemp.appRecommendTempId}">删除</a>
                                        </p:privilege>
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
                                <pg:pager url="/joymeapp/recommend/list"
                                          items="${page.totalRows}" isOffset="true"
                                          maxPageItems="${page.pageSize}"
                                          export="offset, currentPageNumber=pageNumber" scope="request">
                                    <pg:param name="appid" value="${appId}"/>
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