<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>游戏列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>

    <script type="text/javascript" src="/static/include/js/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.easyui.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/static/include/js/easyui/themes/default/easyui.css"/>
    <script type="text/javascript" src="/static/include/js/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript">
        var lock = false;
        $(document).ready(function () {
            $("#addnewgame").click(function () {
                $("#reset").click(function () {
                    $('.easyui-datetimebox').datetimebox('clear');
                    $("#form_submit :text").prop("value", '');
                    $("#form_submit input[name='gameDbId']").prop("value", '');
                });

                $('#dgrules').datagrid({
                    url: '/json/gameclient/clientline/wikiaddgame',
                    pagination: true,
                    rownumbers: false,
                    singleSelect: true,
                    top: '30px',
                    width: 'auto',
                    height: 'auto',
                    striped: true,
                    idField: 'appId',
                    pageList: [10, 20],
                    fitColumns: true,
                    loadMsg: '数据加载中请稍后……',
                    columns: [
                        [
                            {field: 'gameDbId', title: '游戏id', width: 60},
                            {field: 'gameName', title: '游戏名称', width: 100},
                            {
                                field: 'gameIcon', title: '游戏图标', width: 60,
                                formatter: function (value, row, index) {
                                    return "<img src=" + row.gameIcon + " width='40' height='40>'";
                                }
                            },
                            {field: 'wikiKey', title: 'WIKI_KEY', width: 100},
                            {field: 'ck', checkbox: true}
                        ]
                    ]
                });

                $('#chooseWindow').window('open')
            });


            //从gamedb 中读取的查询按钮
            $("#buttonInWindow").click(function () {

                var url = '/json/gameclient/clientline/wikiaddgame';
                if ($.trim($("#gameNameInWindow").val()) != '') {

                    url += '?gameName=' + $.trim($("#gameNameInWindow").val());
                }
                $('#dgrules').datagrid('options').url = url;
                $("#dgrules").datagrid('reload');

            });

            //弹窗中选择一个游戏后
            $("#selectone").click(function () {

                var item = $('#dgrules').datagrid('getSelected');
                var gameId = item.gameDbId;
                var gameName = item.gameName;
                var wikiKey = item.wikiKey;

                if (!lock) {
                    lock = true;
                    $.ajax({
                        url: "/json/joyme/gameres/addgame",
                        data: {gameid: gameId, gamename: gameName, wikikey: wikiKey},
                        timeout: 5000,
                        dataType: "json",
                        type: "POST",
                        success: function (data) {
                            lock = false;
                            if (data.rs == 1) {
                                alert("游戏添加成功");
                                location.reload();
                                $('#chooseWindow').window('close');
                            }
                            else if (data.rs == -90001) {
                                alert("该游戏已经添加");
                            } else if (data.rs == -2009) {
                                alert("该游戏还没有添加wiki，请在WIKI管理>wiki列表中添加WIKI");
                            } else {

                            }
                        },
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                            lock = false;
                            alert("网络错误");
                            return;
                        }
                    });
                }
            });
        });

        function sort(sorttype, id) {
            var url = "/joyme/wiki/gameres/sort/" + sorttype + "?id=" + id;
            if ("top" == sorttype || "up" == sorttype) {
                if (!$("#game_" + id).prev("tr").hasClass("recommend")) {
                    alert("已经是第一个推荐的游戏");
                    return;
                }

            }
            if ("down" == sorttype || "bottom" == sorttype) {
                if (!$("#game_" + id).next("tr").hasClass("recommend")) {
                    alert("已经是最后一个推荐的游戏");
                    return;
                }
            }

            window.location.href = url;
        }
    </script>
<body>
<div id="chooseWindow" class="easyui-window" title="选择要填加的app"
     data-options="modal:true,closed:true,collapsible:false,minimizable:false,maximizable:false"
     style="width:750px; top:50px;">
    游戏名称&nbsp;:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <input type="text" id="gameNameInWindow" size="10"/>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <input type="button" id="buttonInWindow" value="查询"/>
    <table id="dgrules" data-options="
              rownumbers:false,
              autoRowHeight:true,
              pagination:true">
    </table>
    <div style="text-align:center">
        <div class=" clear mt20" style=" margin:0 auto;">
            <input type="button" onclick="$('#chooseWindow').window('close');" value="取消"/>
            <input type="button" value="选择" id="selectone" value="保存"/></div>
    </div>
</div>

<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 游戏管理 >> 游戏列表</td>
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
                        <td height="1" colspan="10" class="error_msg_td">
                                ${errorMsg}
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

                    </td>
                    <td>
                        <table>
                            <tr>
                                <td style="padding-right: 30px;">
                                    <form action="/joyme/wiki/gameres/list" method="post">
                                        游戏名称：<input type="text" name="gamename" value="${gameName}"/> &nbsp;&nbsp;
                                        状态：
                                        <select name="validstatus">
                                            <option value="">全部</option>
                                            <option value="valid"
                                                    <c:if test="${validstatus eq 'valid'}">selected</c:if> >启用
                                            </option>
                                            <option value="invalid"
                                                    <c:if test="${validstatus eq 'invalid'}">selected</c:if>>禁用
                                            </option>
                                        </select> &nbsp;&nbsp;

                                        是否推荐：
                                        <select name="recommend">
                                            <option value="">全部</option>
                                            <option value="1" <c:if test="${recommend eq '1'}">selected</c:if>>是
                                            </option>
                                            <option value="0" <c:if test="${recommend eq '0'}">selected</c:if>>否
                                            </option>
                                        </select> &nbsp;&nbsp;
                                        <input type="submit" value="查询" size="20"/>
                                    </form>
                                </td>
                                <td>
                                    <input type="submit" name="button" id="addnewgame" class="default_button"
                                           value="添加游戏"/>
                                </td>

                                <td>

                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            <table width="800px" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="8" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center">游戏名称</td>
                    <td nowrap align="center">状态</td>
                    <td nowrap align="center">是否推荐</td>
                    <td nowrap align="center">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="8" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${not empty rows}">
                        <c:forEach items="${rows}" var="dto" varStatus="st">
                            <tr id="game_${dto.id}"
                                class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose><c:if test="${dto.recommend eq 1}"> recommend</c:if>">
                                <td nowrap align="center">${dto.gameName}</td>
                                <td nowrap align="center">
                                    <c:choose>
                                        <c:when test="${dto.validStatus.code eq 'valid'}">
                                            <span style="color:green;">启用</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span style="color:red;">未启用</span>
                                        </c:otherwise>
                                    </c:choose>

                                </td>
                                <td nowrap align="center">
                                    <c:choose>
                                        <c:when test="${dto.recommend eq '1'}">
                                            <span style="color:green;">是</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span style="color:red;"> 否 </span>
                                        </c:otherwise>
                                    </c:choose>

                                </td>
                                <td nowrap align="center">
                                    <c:choose>
                                        <c:when test="${dto.recommend eq '0'}">
                                            <a href="/joyme/wiki/gameres/recommend?id=${dto.id}&recommend=1">推荐</a> &nbsp;&nbsp;
                                        </c:when>
                                        <c:otherwise>
                                            <a href="/joyme/wiki/gameres/recommend?id=${dto.id}&recommend=0">取消推荐</a> &nbsp;&nbsp;
                                        </c:otherwise>
                                    </c:choose>

                                    <c:choose>
                                        <c:when test="${dto.recommend eq '1'}">
                                            <a href="javascript:sort('top','${dto.id}')">置顶</a> &nbsp;&nbsp;
                                            <a href="javascript:sort('up','${dto.id}')">上移</a> &nbsp;&nbsp;
                                            <a href="javascript:sort('down','${dto.id}')">下移</a> &nbsp;&nbsp;
                                            <a href="javascript:sort('bottom','${dto.id}')">置底</a> &nbsp;&nbsp;
                                        </c:when>
                                        <c:otherwise>
                                            置顶&nbsp;&nbsp;上移&nbsp;&nbsp;下移&nbsp;&nbsp;置底 &nbsp;&nbsp;
                                        </c:otherwise>
                                    </c:choose>

                                    <c:choose>
                                        <c:when test="${dto.validStatus.code eq 'valid'}">
                                            <a href="/joyme/wiki/gameres/status?validstatus=invalid&id=${dto.id}">禁用</a> &nbsp;&nbsp;
                                        </c:when>
                                        <c:otherwise>
                                            <a href="/joyme/wiki/gameres/status?validstatus=valid&id=${dto.id}">启用</a> &nbsp;&nbsp;
                                        </c:otherwise>
                                    </c:choose>


                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="8" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="8" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="8" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="8">
                            <pg:pager url="/joyme/wiki/gameres/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">

                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                <pg:param name="items" value="${page.totalRows}"/>
                                <%@ include file="/WEB-INF/jsp/toolspgnoincludejquery.jsp" %>
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