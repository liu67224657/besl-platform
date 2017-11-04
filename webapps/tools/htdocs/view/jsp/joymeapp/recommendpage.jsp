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

            $('#recommdend_form_hidden_appid').val(appIdVal);
            $('#recommdend_form_hidden_destid').val(result.join(','));

        }

        function recommendTempList() {
            var appIdVal = $('#select_appid').val();
            if (appIdVal.length == 0) {
                alert("请选择所属游戏");
                return false;
            }
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
                $("[name = box]:checkbox").each(function () {
                    $(this).attr("checked", !$(this).attr("checked"));
                });
            });
        });

        function sort(appId, recommendId, sortSuffix) {

            var sortNum = document.getElementById('text_sortnum_' + sortSuffix).value;
            if (sortNum.length == 0) {
                alert('请填写非0的数字');
            }

            document.getElementById('sort_appid').value = appId;
            document.getElementById('sort_recommendid').value = recommendId;
            document.getElementById('sort_num').value = sortNum;

            $('#form_submit_sort').submit();
        }
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
                    <td class="list_table_header_td">手机端推荐列表</td>
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
                        <form action="/joymeapp/recommend/list" method="post">
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
                                        <p:privilege name="/joymeapp/recommend/list">
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
                        <form action="/joymeapp/recommend/temp/list" method="post" onsubmit="return recommendTempList();" style="vertical-align: middle;height: 13px;line-height: 13px;">
                            <input type="hidden" name="appid" id="input_hidden_recommend_temp_list_appid"/>
                            <p:privilege name="/joymeapp/recommend/temp/list">
                            <input type="submit" name="submit" value="去同类推荐临时列表" class="default_button"/>
                            </p:privilege>
                        </form>
                    </td>
                </tr>
            </table>
            <form action="/joymeapp/recommend/list" method="post">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="13" class="default_line_td"></td>
                    </tr>
                    <tr class="list_table_title_tr">
                        <td nowrap align="left" width="60">当前排序</td>
                        <td nowrap align="left" width="60">上次排序</td>
                        <td nowrap align="left" width="60">排序对比</td>
                        <td nowrap align="left" width="150">游戏名称</td>
                        <td nowrap align="left" width="300">标签</td>
                        <td nowrap align="left" width="50">排序调整</td>
                        <td nowrap align="left" width="150">录入时间</td>
                        <td nowrap align="left" width="50">操作</td>
                    </tr>

                    <c:choose>
                        <c:when test="${appRecommendDTOList.size() > 0}">
                            <c:forEach items="${appRecommendDTOList}" var="dto" varStatus="st">
                                <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                    <td nowrap>
                                        <input type="hidden" name="recommendid" id="recommendid"
                                               value="${dto.recommend.appRecommendId}"/>
                                        <input type="hidden" name="appid" value="${dto.recommend.appId}" id="id"/>
                                        <input type="hidden" name="destid" value="${dto.recommend.destAppId}"/>
                                            ${dto.displayOrder}
                                    </td>
                                    <td nowrap>${dto.lastDisplayOrder}</td>
                                    <td nowrap>
                                        <c:choose>
                                        <c:when test="${dto.displayOrder>dto.lastDisplayOrder}">
                                        <span style="color: #ff0000;">下降${dto.displayOrder-dto.lastDisplayOrder}</span>
                                        </c:when>
                                        <c:when test="${dto.displayOrder<dto.lastDisplayOrder}">
                                        <span style="color: #008000;">上升${dto.lastDisplayOrder-dto.displayOrder}</span>
                                        </c:when>
                                        <c:otherwise>
                                        <span style="">${dto.displayOrder-dto.lastDisplayOrder}</span>
                                        </c:otherwise>
                                        </c:choose>
                                    <td nowrap>${dto.recommend.destAppName}</td>
                                    <td nowrap>
                                        <c:forEach items="${dto.appResource.appTagList}"
                                                   var="tag">${tag.tagName}&nbsp;</c:forEach>
                                    </td>
                                    <td nowrap>
                                        移动到第<input type="text" name="sortnum" id="text_sortnum_${st.index}" size="3"/>位
                                        <p:privilege name="/joymeapp/recommend/sort">
                                        <input type="button" name="确定" value="确定" class="default_button" onclick="sort('${dto.recommend.appId}','${dto.recommend.appRecommendId}','${st.index}')"/>
                                        </p:privilege>
                                    </td>
                                    <td nowrap>${dto.createDate}</td>
                                    <td nowrap>
                                        <p:privilege name="/joymeapp/recommend/delete">
                                        <a href="/joymeapp/recommend/delete?appid=${dto.recommend.appId}&destid=${dto.recommend.destAppId}">删除</a>
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
<form action="/joymeapp/recommend/sort" method="post" id="form_submit_sort">
    <input type="hidden" name="appid" id="sort_appid" value=""/>
    <input type="hidden" name="recommendid" id="sort_recommendid" value=""/>
    <input type="hidden" name="sortnum" id="sort_num" value=""/>
</form>
</body>
</html>