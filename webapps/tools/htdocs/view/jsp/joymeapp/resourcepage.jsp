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

            $('#recommdend_form_hidden_appid2').val(appIdVal);
            $('#recommdend_form_hidden_destid2').val(result.join(','));

        }

        function recommendOrFavoriteList() {
            var appIdVal = $('#select_appid').val();
            if (appIdVal.length == 0) {
                alert("请选择所属游戏");
                return false;
            }
            $('#input_hidden_recommend_list_appid').val(appIdVal);
            $('#input_hidden_favorite_list_appid').val(appIdVal);

        }

        function formSubmit() {
            $('#form_submit_search').submit();
        }
        $(document).ready(function() {

            $('#form_submit_search').bind('submit', function() {

                var sVal = $('#select_search_value').val();
                var sStr = $('#input_text_search_str').val();
                if (sVal == 0 && sStr.length > 0) {
                    alert("请选择筛选类型");
                    return false;
                }
                if (sVal > 0 && sStr.length == 0) {
                    alert("请填写筛选内容");
                    return false;
                }

                var appId = $('#select_appid').val();
                $('#input_hidden_appid').val(appId);
            });

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
    <td height="22" class="page_navigation_td">>> 运营管理 >> 条目管理 >> 批量管理</td>
</tr>
<tr>
    <td height="100%" valign="top"><br>
        <table border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td class="list_table_header_td">游戏列表</td>
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
                    <form action="/joymeapp/resource/list" method="post" id="form_submit_search">
                        <table width="100%">
                            <tr>
                                <input type="hidden" name="seqid" value="${seqid}"/>
                                <input type="hidden" name="appid" value="" id="input_hidden_appid"/>
                                <td height="1" class="list_table_header_td">类型：
                                    <select name="searchtype" id="select_search_value">
                                        <option value="">请选择</option>
                                        <c:forEach var="searchType" items="${appSearchTypeCollection}">
                                            <option value="${searchType.code}"
                                                    <c:if test="${searchType.code==appSearchType.code}">selected="selected" </c:if>>
                                                <fmt:message key="joymeapp.search.type.${searchType.code}"
                                                             bundle="${def}"/></option>
                                        </c:forEach>
                                    </select>筛选:
                                    <input type="text" name="searchstr" value="${searchStr}"
                                           id="input_text_search_str"/>(多个标签中间用<span style="color:red">空格</span>隔开)
                                </td>
                                <td>
                                    <p:privilege name="/joymeapp/resource/list">
                                        <input type="submit" name="button" class="default_button" value="查询"/>
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
                <td height="1" class="list_table_header_td" width="60">
                    所属游戏:
                </td>
                <td height="1">
                    <input type="hidden" name="seqid" value="${seqid}"/>
                    <select name="appid" id="select_appid" onchange="formSubmit();">
                        <option value="">请选择</option>
                        <c:forEach var="app" items="${searchAppList}">
                            <option value="${app.appInfoId}"
                                    <c:if test="${app.appInfoId==appId}">selected="selected" </c:if>>${app.appName}--<fmt:message
                                    key="joymeapp.platform.${app.appPlatform.code}" bundle="${def}"/></option>
                        </c:forEach>
                    </select>
                </td>
                <td>
                    <form action="/joymeapp/recommend/list" method="post"
                          onsubmit="return recommendOrFavoriteList();"
                          style="vertical-align: middle;height: 13px;line-height: 13px;">
                        <input type="hidden" name="appid" id="input_hidden_recommend_list_appid"/>
                        <p:privilege name="/joymeapp/recommend/list">
                            <input type="submit" name="submit" value="查看同类推荐列表" class="default_button"/>
                        </p:privilege>
                    </form>
                </td>
                <td>
                    <form action="/joymeapp/favorite/result" method="post"
                          onsubmit="return recommendOrFavoriteList();"
                          style="vertical-align: middle;height: 13px;line-height: 13px;">
                        <input type="hidden" name="appid" id="input_hidden_favorite_list_appid"/>
                        <p:privilege name="/joymeapp/favorite/result">
                            <input type="submit" name="submit" value="查看猜你喜欢列表" class="default_button"/>
                        </p:privilege>
                    </form>
                </td>
            </tr>
        </table>
        <table width="100%">
            <tr>
                <td width="50">
                    <input type="checkbox" name="all" id="checkall"/>全选
                </td>
                <td width="50">
                    <input type="checkbox" name="inverse" id="checkinverse"/>反选
                </td>
                <td width="90">
                    <form action="/joymeapp/recommend/temp/increase" method="post" onsubmit="return recommend();"
                          style="vertical-align: middle;height: 13px;line-height: 13px;">
                        <input type="hidden" name="appid" id="recommdend_form_hidden_appid">
                        <input type="hidden" name="destid" id="recommdend_form_hidden_destid">
                        <p:privilege name="/joymeapp/recommend/temp/increase">
                            <input type="submit" name="submit" value="添加到同类推荐临时列表" class="default_button"/>
                        </p:privilege>
                    </form>
                </td>
                <td>
                    <form action="/joymeapp/favorite/list" method="post" onsubmit="return recommend();"
                          style="vertical-align: middle;height: 13px;line-height: 13px;">
                        <input type="hidden" name="appid" id="recommdend_form_hidden_appid2">
                        <input type="hidden" name="destid" id="recommdend_form_hidden_destid2">
                        <input type="hidden" name="seqid" value="${seqid}"/>
                        <p:privilege name="/joymeapp/favorite/list">
                            <input type="submit" name="submit" value="添加到猜你喜欢临时列表" class="default_button"/>
                        </p:privilege>
                    </form>
                </td>
                <td align="right">
                    <a href="/joymeapp/resource/createpage">
                        <p:privilege name="/joymeapp/resource/createpage">
                            <input type="button" name="添加游戏" value="添加游戏" class="default_button"/>
                        </p:privilege>
                    </a>
                </td>
            </tr>
        </table>
        <form action="/joymeapp/resource/list" method="post">
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="13" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="left" width="30"></td>
                    <td nowrap align="left" width="50">序号</td>
                    <td nowrap align="left" width="200">游戏名称</td>
                    <td nowrap align="left" width="100">游戏平台</td>
                    <td nowrap align="left" width="200">标签</td>
                    <td nowrap align="left" width="150">录入时间</td>
                    <td nowrap align="left" width="50">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="13" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="resource" varStatus="st">
                            <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap>
                                    <input type="checkbox" name="box" value="${resource.appinfo.appInfoId}"/>
                                </td>
                                <td nowrap>${st.index+1}</td>
                                <td nowrap>${resource.appinfo.appName}</td>
                                <td nowrap><fmt:message key="joymeapp.platform.${resource.appinfo.appPlatform.code}"
                                                        bundle="${def}"/></td>
                                <td nowrap>
                                    <c:forEach items="${resource.appTagList}" var="tag">
                                        ${tag.tagName}&nbsp;
                                    </c:forEach>
                                </td>
                                <td nowrap>${resource.appinfo.createDate}</td>
                                <td nowrap><p:privilege name="/joymeapp/resource/modifypage"><a
                                        href="/joymeapp/resource/modifypage?appid=${resource.appinfo.appInfoId}">编辑</a></p:privilege>
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
                            <pg:pager url="/joymeapp/resource/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="searchtype" value="${appSearchType.code}"/>
                                <pg:param name="searchstr" value="${searchStr}"/>
                                <pg:param name="appid" value="${appId}"/>
                                <pg:param name="seqid" value="${seqid}"/>
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