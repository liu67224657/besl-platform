<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <title>从game db添加一个game到新游开测榜</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.easyui.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/static/include/js/easyui/themes/default/easyui.css"/>
    <script type="text/javascript" src="/static/include/js/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>


    <script type="text/javascript">
        $(document).ready(function (e) {
            <c:if test="${fn:contains(lineCode, 'gc_newgame')}">
            $("#isHot").change(function () {
                if ($("#isHot").val() == 0) {
                    $(".newgame_block").hide();
                } else {
                    $(".newgame_block").show();
                }
                $("#category").change();
            });
            $("#category").change(function () {
                if ($("#category").val() == 0 || $("#isHot").val() == 0) {
                    $(".newgame_block_start").hide();
                    $(".newgame_block_end").hide();
                } else {
                    $(".newgame_block_start").show();
                    $(".newgame_block_end").show();
                }
            });

            $("#isHot").change();

            $("input[name=categoryColor][type=radio]").change(function () {
                        var name = $("input:radio[name=categoryColor]:checked").val();
                        if (name == 'gamecustom') {
                            $(".gamecustom").show();
                        } else {
                            $(".gamecustom").hide();
                        }
                    }
            );

            $("input:radio[name=categoryColor][value=gamenone]").prop("checked", true);
            $("input:radio[name=categoryColor][value=gamenone]").change();

            $("#customShowContentTr").hide();
            $("#showType").on("change", function () {
                var showType = $("#showType").val();
                if (showType == 1) {
                    $("#customShowContentTr").hide();
                } else if (showType == 2) {
                    $("#customShowContentTr").show();
                }
            });


            </c:if>


            $("#addnewgame").click(function () {

                $("#reset").click(function () {
                    $('.easyui-datetimebox').datetimebox('clear');
                    $("#form_submit :text").prop("value", '');
                    $("#form_submit input[name='gameDbId']").prop("value", '');
                });

                $('#dgrules').datagrid({
                    url: '/json/gameclient/clientline/toaddgame?lineId=${lineId}',
                    pagination: true,
                    rownumbers: false,
                    singleSelect: true,
                    width: 'auto',
                    height: 'auto',
                    striped: true,
                    idField: 'appId',
                    pageList: [10, 20, 30, 40],
                    fitColumns: true,
                    loadMsg: '数据加载中请稍后……',
                    columns: [
                        [
                            {field: 'gameDbId', title: 'game_db表的_id', width: 60},
                            {field: 'gameName', title: '游戏名称', width: 100},
                            {field: 'anotherName', title: '别名', width: 100},
                            {field: 'downloadRecommend', title: '下载推荐', width: 150},
                            {
                                field: 'gameIcon', title: '游戏主图', width: 60,
                                formatter: function (value, row, index) {
                                    return "<img src=" + row.gameIcon + " width='40' height='40>'";

                                }
                            },
                            {
                                field: 'gamePublicTime', title: '上市时间', width: 100,
                                formatter: function (value, row, index) {
                                    if (row.gamePublicTime != null && row.gamePublicTime.toString() != '') {
                                        var time = new Date(row.gamePublicTime);
                                        return time.getFullYear() + '-' + (time.getMonth() + 1) + '-' + time.getDate() + ' ' + time.getHours() + ':' + time.getMinutes() + ':' + time.getSeconds();
                                    }
                                    else {
                                        return '';
                                    }

                                }
                            },
                            {
                                field: 'gameDevices', title: '游戏设备', width: 120,
                                formatter: function (value, row, index) {
                                    var result = '';
                                    for (var item in row.gameDBDevicesSet) {
                                        if (row.gameDBDevicesSet[item].gameDbDeviceId == 1 && row.gameDBDevicesSet[item].gameDbDeviceStatus) {
                                            result += "iphone,"
                                        } else if (row.gameDBDevicesSet[item].gameDbDeviceId == 2 && row.gameDBDevicesSet[item].gameDbDeviceStatus) {
                                            result += "ipad,"
                                        } else if (row.gameDBDevicesSet[item].gameDbDeviceId == 3 && row.gameDBDevicesSet[item].gameDbDeviceStatus) {
                                            result += "android,"
                                        }

                                    }
                                    //去掉最后的 逗号
                                    if (result.length > 0 && result.charAt(result.length - 1) == ',') {
                                        result = result.substr(0, result.length - 1)

                                    }
                                    return result;

                                }
                            },
                            {field: 'ck', checkbox: true}
                        ]
                    ]
                });

                $('#chooseWindow').window('open');
            });


            //从gamedb 中读取的查询按钮
            $("#buttonInWindow").click(function () {

                var url = '/json/gameclient/clientline/toaddgame?lineId=${lineId}';
                if ($.trim($("#gameNameInWindow").val()) != '') {

                    url += '&gameName=' + $.trim($("#gameNameInWindow").val());
                }
                $('#dgrules').datagrid('options').url = url;
                $("#dgrules").datagrid('reload');

            });


            //弹窗中选择一个游戏后
            $("#selectone").click(function () {
                var lineCode = '${lineCode}';
                var item = $('#dgrules').datagrid('getSelected');
                $("#form_submit input[name='gameDbId']").val(item.gameDbId);
                $("#form_submit input[name='gameName']").val(item.gameName);

                $("#form_submit input[name='anotherName']").val(item.anotherName);

                $("#gameDbId").prop('value', item.gameDbId);

                $("#form_submit textarea[name='gameProfile']").html(item.downloadRecommend);

                if (item.gameIcon != '') {

                    $("#gameIcon").prop('src', item.gameIcon);
                }

                var time = '';
                if (lineCode.charAt(lineCode.length - 1) == 0 && item.gameDBCover.posterGamePublicTimeIos !=null) {     //ios
                    time = new Date(Number(item.gameDBCover.posterGamePublicTimeIos));
                } else if (lineCode.charAt(lineCode.length - 1) == 1 && item.gameDBCover.posterGamePublicTimeAndroid !=null ) {    //android
                    time = new Date(Number(item.gameDBCover.posterGamePublicTimeAndroid));
                  //  console.log(" item.gameDBCover.posterGamePublicTimeAndroid-->" + item.gameDBCover.posterGamePublicTimeAndroid);
                } else if (item.gamePublicTime != null && item.gamePublicTime.toString() != '') {
                    time = new Date(item.gamePublicTime);
                }
                if (time != '') {
                    var timeStr = time.getFullYear() + '-' + (time.getMonth() + 1) + '-' + time.getDate() + ' ' + time.getHours() + ':' + time.getMinutes() + ':' + time.getSeconds();
                    $("#gamePublicTime").datetimebox('setValue', timeStr);
                }

                if (lineCode == 'gc_newgame_0') {
                    if (item.gameDBCover.posterShowTypeIos != '' && item.gameDBCover.posterShowTypeIos != null) {
                        $("#showType").val(item.gameDBCover.posterShowTypeIos);
                        $("#showType").change();
                    }
                    if (item.gameDBCover.posterShowContentIos != '' && item.gameDBCover.posterShowContentIos != null) {
                        $("#customShowContent").val(item.gameDBCover.posterShowContentIos);
                    }
                } else if (lineCode == 'gc_newgame_1') {
                    if (item.gameDBCover.posterShowTypeAndroid != '' && item.gameDBCover.posterShowTypeAndroid != null) {
                        $("#showType").val(item.gameDBCover.posterShowTypeAndroid);
                        $("#showType").change();
                    }
                    if (item.gameDBCover.posterShowContentAndroid != '' && item.gameDBCover.posterShowContentAndroid != null) {
                        $("#customShowContent").val(item.gameDBCover.posterShowContentAndroid);
                    }
                }


                $('#chooseWindow').window('close');
            });

            //页面表单 提交
            $("#submit").click(function () {
                var gameDbId = $.trim($("#form_submit input[name='gameDbId']").val());
                var gamePublicTime = $('#gamePublicTime').datetimebox('getValue');
                var lineCode = '${lineCode}';

                //针对新游开测榜的单独验证
                if (lineCode.indexOf('gc_newgame') > -1) {

                    var isHot = $.trim($("#isHot").val());
                    var category = $.trim($("#category").val());
                    var startTime = $('#startTime').datebox('getValue');
                    var endTime = $('#endTime').datebox('getValue');
                    var categoryColor = $("input:radio[name=categoryColor]:checked").val();
                    var url = $.trim($("#url").val());
                    var showType = $.trim($("#showType").val());
                    var customShowContent = $.trim($("#customShowContent").val());
                    if (category == '1' && isHot == '1') {
                        if (startTime == '') {
                            alert("请填写起始时间!");
                            return false;
                        } else if (endTime == '') {
                            alert("请填写结束时间!");
                            return false;
                        }

                        var sTime = '';
                        var eTime = '';
                        var patternStart = /(\d{4})-(\d{1,2})-(\d{1,2})\s*/g;
                        if (patternStart.test(startTime)) {
                            sTime = new Date(RegExp.$1, (parseInt(RegExp.$2) - 1), RegExp.$3).getTime();
                        }
                        var patternEnd = /(\d{4})-(\d{1,2})-(\d{1,2})\s*/g;
                        if (patternEnd.test(endTime)) {
                            eTime = new Date(RegExp.$1, (parseInt(RegExp.$2) - 1), RegExp.$3).getTime();
                        }
                        if (sTime == '' || eTime == '' || eTime - sTime <= 0) {
                            alert("结束时间需要大于起始时间");
                            return false;
                        }
                    }

                    if (categoryColor == 'gamecustom' && url == '') {
                        alert("请填写合适的链接");
                        return false;
                    }
                    if (showType == 2 && customShowContent == '') {
                        alert("请填写合适的自定义显示文字内容");
                        return false;
                    }

                }


                if (gameDbId == "") {
                    alert("请选择一个游戏,然后再点击提交!");
                    return false;
                } else if (gamePublicTime == '') {
                    alert("请设置游戏的上市时间,然后再点击提交!");
                    return false;
                } else {
                    $("#form_submit").submit();
                }
            });

        });

    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷手游画报管理 >> 新游开测,大家正在玩,热门的clientline</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br/>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td"><span
                            style="font-size:14px; font-family: Arial, Helvetica, sans-serif; font-weight:bold;">新手游画报--<b>${lineName}</b>(${lineCode})添加一条记录</span>

                        <input type="button" id="addnewgame" name="button" value="请选择要填加的game"/>
                    </td>
                    <td class="list_table_header_td">

                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/gameclient/clientline/game/itemcreate" method="post" id="form_submit">
                <table border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" class="" width="240">
                            <input type="hidden" name="lineId" value="${lineId}"/>
                            <input type="hidden" name="lineCode" value="${lineCode}"/>
                            <input type="hidden" name="lineName" value="${lineName}"/>
                            <input type="hidden" name="gameDbId" value=""/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="240">
                            game_db表的_id：
                        </td>
                        <td height="1" class="" width="10" width="200">
                            <input type="text" size="50" id="gameDbId" disabled="disabled"/>*
                        </td>
                        <td height="1" align="left"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="240">
                            游戏名称:
                        </td>
                        <td height="1" width="200">
                            <input type="text" name="gameName" size="50" disabled="disabled"/>*
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="240">
                            别名:
                        </td>
                        <td height="1" width="200">
                            <input type="text" name="anotherName" size="50" disabled="disabled"/>*
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="240">
                            下载推荐:
                        </td>
                        <td height="1" width="200">
                            <textarea name="gameProfile" rows="8" cols="50" disabled="disabled"></textarea>*
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="240">
                            游戏主图:
                        </td>
                        <td height="1">
                            <img id="gameIcon" src="/static/images/default.jpg"/>
                        </td>
                    </tr>
                    <c:if test="${fn:contains(lineCode, 'gc_newgame')}">
                        <tr class="newgame">
                            <td height="1" class="default_line_td" width="240">
                                是否出现在热门页:
                            </td>
                            <td height="1">
                                <select id="isHot" name="isHot">
                                    <option value="0">不出现</option>
                                    <option value="1">出现</option>
                                </select>
                            </td>
                            <td height="1" class=>
                            </td>
                        </tr>
                        <tr class="newgame_block">
                            <td height="1" class="default_line_td" width="240">
                                是否出现在热门页中的热门块:
                            </td>
                            <td height="1">
                                <select name="category" id="category">
                                    <option value="1">出现</option>
                                    <option value="0">不出现</option>
                                </select>*必填项
                            </td>
                            <td height="1" class=>
                            </td>
                        </tr>
                        <tr class="newgame_block_start">
                            <td height="1" class="default_line_td" width="240">
                                出现在热门块中的开始时间(以天为单位):
                            </td>
                            <td height="1">
                                <input type="text" class="easyui-datebox" editable="false" id="startTime"
                                       name="startTime" value="${startTime}"/>*必填项
                            </td>
                            <td height="1">
                            </td>
                        </tr>
                        <tr class="newgame_block_end">
                            <td height="1" class="default_line_td" width="240">
                                出现在热门块中的结束时间(以天为单位):
                            </td>
                            <td height="1">
                                <input type="text" name="endTime" class="easyui-datebox" id="endTime"
                                       editable="false" value="${endTime}"/>*必填项
                            </td>
                            <td height="1">
                            </td>
                        </tr>
                        <tr>
                            <td height="1" class="default_line_td" width="240">
                                选择开测时间显示方式:
                            </td>
                            <td height="1" width="240px">
                                <select name="showType" id="showType">
                                    <option value="1" selected="selected">精确时间</option>
                                    <option value="2">自定义显示的文字</option>
                                </select> *必填项
                            </td>
                            <td height="1">
                            </td>
                        </tr>
                        <tr id="customShowContentTr">
                            <td height="1" class="default_line_td" width="240">
                                自定义显示文字内容:
                            </td>
                            <td height="1" width="240px">
                                <input type="text" name="customShowContent" id="customShowContent" value=""/> *必填项
                            </td>
                            <td height="1">
                            </td>
                        </tr>

                    </c:if>
                    <tr>
                        <td height="1" class="default_line_td" width="240">
                            上市时间:
                        </td>
                        <td height="1">
                            <input type="text" class="easyui-datetimebox" editable="false" id="gamePublicTime"
                                   name="gamePublicTime"/>*必填项
                        </td>
                    </tr>
                    <c:if test="${fn:contains(lineCode, 'gc_newgame')}">
                        <tr>
                            <td height="1" class="default_line_td" width="240">
                                链接事件:
                            </td>
                            <td height="1">
                                <input type="radio" name="categoryColor" value="gamenone"/> 无链接 &nbsp;&nbsp;
                                <input type="radio" name="categoryColor" value="gamecover"/> 游戏封面 &nbsp;&nbsp;
                                <input type="radio" name="categoryColor" value="gameposter"/> 宣传海报 &nbsp;&nbsp;
                                <input type="radio" name="categoryColor" value="gamedetail"/> 游戏详情 &nbsp;&nbsp;
                                <input type="radio" name="categoryColor" value="gamecustom"/> 自定义链接 &nbsp;&nbsp;
                            </td>
                        </tr>

                        <tr class="gamecustom">
                            <td height="1" class="default_line_td" width="150">
                                跳转类型 :
                            </td>
                            <td height="1" width="200">
                                <select id="redirectType" name="redirectType">
                                    <c:forEach items="${types}" var="item">
                                        <option value='${item.key}'>${item.key}__<fmt:message
                                                key="client.item.redirect.${item.key}" bundle="${def}"/></option>
                                    </c:forEach>
                                </select> <a target="view_window"
                                             href="http://wiki.enjoyf.com/index.php?title=Gameclient_client#.E8.B7.B3.E8.BD.AC.E7.B1.BB.E5.9E.8B">使用参考</a>
                            </td>
                        </tr>
                        <tr class="gamecustom">
                            <td height="1" class="default_line_td" width="240">
                                链接:
                            </td>
                            <td height="1">
                                <input type="text" name="url" id="url" size="80"/>
                            </td>
                        </tr>
                    </c:if>
                </table>
                <table width="90%" border="0" cellspacing="1" cellpadding="0">
                    <tr align="center">
                        <td colspan="3">
                            <input type="submit" id="submit" class="default_button" value="提交">
                            <input type="button" class="default_button" value="返回"
                                   onclick="javascipt:window.history.go(-1);">
                        </td>
                    </tr>
                </table>
            </form>
        </td>
    </tr>
</table>


<div id="chooseWindow" class="easyui-window" title="选择要填加的app"
     data-options="modal:true,closed:true,collapsible:false,minimizable:false,maximizable:false" style="width:750px;">
    游戏名称&nbsp;:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text"
                                                                                        id="gameNameInWindow"
                                                                                        size="10"/>
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
</body>
</html>