<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <title>热门页游戏分类新建最终条目页</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/gameclienthotfloorhandler.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.easyui.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/static/include/js/easyui/themes/default/easyui.css"/>
    <script type="text/javascript" src="/static/include/js/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>

    <script type="text/javascript">

        $(function () {

            $("#addnewgame").click(function () {

                $("#reset").click(function () {
                    $('.easyui-datetimebox').datetimebox('clear');
                    $("#form_submit :text").prop("value", '');
                    $("#form_submit input[name='gameDbId']").prop("value", '');
                });

                $('#dgrules').datagrid({
                    url: '/json/gameclient/clientline/toaddgame?lineId=${subLineId}',
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
                                field: 'coverComment', title: '点评描述', width: 170,
                                formatter: function (value, row, index) {
                                    return row.gameDBCover.coverComment;
                                }
                            },
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

                var item = $('#dgrules').datagrid('getSelected');
                $("#form_submit input[name=gameDbId]").val(item.gameDbId);
                $("#gameDbId").val(item.gameDbId);
                $("#gameIcon").attr("src", item.gameIcon);
                $("#form_submit input[name=likeNum]").val(item.gameDBCover.coverAgreeNum);

                var fieldJson = item.gameDBCoverFieldJson;
                var gameRate = (Number(fieldJson.value1) + Number(fieldJson.value2) + Number(fieldJson.value3) + Number(fieldJson.value4) + Number(fieldJson.value5)) / 5;
                $("#form_submit input[name=gameRate]").val(gameRate.toFixed(1));
                $("#form_submit input[name=gameName]").val(item.gameName);

                $("#form_submit input[name=downloadRecommend]").val(item.gameDBCover.coverComment);

                var gameTypeDesc = getGameTypeDesc(item);
                $("#form_submit input[name=gameTypeDesc]").val(gameTypeDesc);
                if (item.gamePublicTime != null && item.gamePublicTime.toString() != '') {
                    var time = new Date(item.gamePublicTime);
                    var timeStr = time.getFullYear() + '-' + (time.getMonth() + 1) + '-' + time.getDate() + ' ' + time.getHours() + ':' + time.getMinutes() + ':' + time.getSeconds();
                    $("#itemCreateDate").datetimebox('setValue', timeStr);
                }

                $('#chooseWindow').window('close');
            });

            //页面表单 提交
            $("#submit").click(function () {
                var gameDbId = $.trim($("#form_submit input[name=gameDbId]").val());
                //    var gamePublicTime = $('#itemCreateDate').datetimebox('getValue');


                var gameName = $.trim($("#form_submit input[name=gameName]").val());
                var gameTypeDesc = $.trim($("#form_submit input[name=gameTypeDesc]").val());
                var downloadRecommend = $.trim($("#form_submit input[name=downloadRecommend]").val());

                if (gameDbId == "") {
                    alert("请选择一个游戏,然后再点击提交!");
                    return false;
                } else if (gameName == '' || gameName.length > 7) {
                    alert("请设置游戏的游戏名称,前端显示不能大于7");
                    return false;
                } else if (gameTypeDesc == '' || gameTypeDesc.length > 5) {
                    alert("请设置游戏的游戏类型,前端显示不能大于5");
                    return false;
                } else if (downloadRecommend == '' || downloadRecommend.length > 7) {
                    alert("请设置游戏的推荐语,前端显示不能大于7");
                    return false;
                } else {
                    $("#form_submit").submit();
                }
            });

            $("#tr_ji").hide();
            $("#tr_jt").hide();
            $("#jumpTarget").on("change", function () {
                var value = $("#jumpTarget").val();
                if (value == 'other') {
                    $("#tr_ji").show();
                    $("#tr_jt").show();
                } else {
                    $("#tr_ji").hide();
                    $("#tr_jt").hide();
                }
            });
            $("#jumpTarget").change();

            var coustomSwfu = new SWFUpload(coustomImageSettings);

        });

        function getGameTypeDesc(item) {
            var result = '';
            //   console.log(item.gameDBCategorySet);
            $.each(item.gameDBCategorySet, function (index, item) {
                if (item.gameDbCategoryStatus) {
                    switch (item.gameDbCategoryId) {
                        case 1:
                            result += 'RPG' + ',';
                            break;
                        case 2:
                            result += '策略' + ',';
                            break;
                        case 3:
                            result += '模拟' + ',';
                            break;
                        case 4:
                            result += '益智' + ',';
                            break;
                        case 5:
                            result += '休闲' + ',';
                            break;
                        case 6:
                            result += '棋牌' + ',';
                            break;
                        case 7:
                            result += '动作' + ',';
                            break;
                        case 8:
                            result += '格斗' + ',';
                            break;
                        case 9:
                            result += '体育竞技' + ',';
                            break;
                        case 10:
                            result += '音乐' + ',';
                            break;
                        case 11:
                            result += '经营' + ',';
                            break;
                        case 12:
                            result += '卡牌' + ',';
                            break;
                        case 13:
                            break;
                        case 14:
                            result += '射击' + ',';
                            break;
                        case 15:
                            result += '赛车' + ',';
                            break;
                        case 16:
                            result += '冒险' + ',';
                            break;
                        case 17:
                            result += '解谜' + ',';
                            break;
                        case 18:
                            result += '竞速' + ',';
                            break;
                        case 19:
                            result += '跑酷' + ',';
                            break;
                        case 20:
                            result += '塔防' + ',';
                            break;
                        case 21:
                            result += '养成' + ',';
                            break;
                        case 22:
                            result += '脱出' + ',';
                            break;
                        case 23:
                            result += '恐怖' + ',';
                            break;
                        case 24:
                            result += '弹幕' + ',';
                            break;
                        case 25:
                            result += '节奏' + ',';
                            break;
                        case 26:
                            result += '像素风格' + ',';
                            break;
                        case 27:
                            result += '黑白风格' + ',';
                            break;
                        case 28:
                            result += '粉丝向' + ',';
                            break;
                        case 29:
                            result += '无需联网' + ',';
                            break;
                        case 30:
                            result += '无需内购' + ',';
                            break;
                        case 31:
                            result += '本地联机' + ',';
                            break;
                        case 32:
                            result += '线上游戏' + ',';
                            break;
                        case 33:
                            result += '三消' + ',';
                            break;
                        default:
                            break;

                    }
                }
            });
            if (result.length > 0) {
                result = result.substr(0, result.length - 1);
            }
            return result;
        }


        var coustomImageSettings = {
            upload_url: "${urlUpload}/json/upload/qiniu",
            post_params: {
                "at": "joymeplatform",
                "filetype": "original"
            },

            // File Upload Settings
            file_size_limit: "2 MB",    // 2MB
            file_types: "*.jpg;*.png;*.gif",
            file_types_description: "请选择图片",
            file_queue_limit: 1,

            file_dialog_complete_handler: fileDialogComplete,
            upload_start_handler: uploadStart,
            upload_success_handler: uploadSuccess,
            upload_complete_handler: uploadComplete,

            // Button Settings
            button_image_url: "/static/images/uploadbutton.png",
            button_placeholder_id: "upload_button",
            button_width: 61,
            button_height: 22,
            moving_average_history_size: 40,

            // Flash Settings
            flash_url: "/static/include/swfupload/swfupload.swf",
            flash9_url: "/static/include/swfupload/swfupload_fp9.swf",

            custom_settings: {},
            // Debug Settings
            debug: false
        }


    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷手游画报管理 >> 热门页游戏分类管理</td>
    </tr>
    <tr>
        <td><label>所属平台: <b><c:choose><c:when
                test="${platform==0}">ios</c:when><c:otherwise>android</c:otherwise></c:choose></b>
            &nbsp;父类别名:<b>${lineName}</b>(${lineCode}) &nbsp;&nbsp;子类别名:<b>${subLineName}</b>(${subLineCode})的clientline
            的新增游戏</label></td>
    </tr>
    <tr>
        <td height="32px">
            <input type="button" class="default_button" value="返回"
                   onclick="javascipt:window.location.href='/gameclient/clientline/gamecategory/itemlist?subLineId=${subLineId}&subLineName=${subLineName}&subLineCode=${subLineCode}&lineCode=${lineCode}&lineName=${lineName}&platform=${platform}';"/>

            &nbsp;&nbsp;
            <input type="button" id="addnewgame" name="button" class="default_button" value="请选择要填加的game"/>
        </td>
    </tr>
</table>

<form action="/gameclient/clientline/gamecategory/itemcreate" method="post" id="form_submit">
    <table border="0" cellspacing="2" cellpadding="2">
        <tr>
            <td height="1" width="240">
                <input type="hidden" value="${platform}" name="platform"/>
                <input type="hidden" value="${lineCode}" name="lineCode"/>
                <input type="hidden" value="${lineName}" name="lineName"/>
                <input type="hidden" value="${subLineId}" name="subLineId"/>
                <input type="hidden" value="${subLineName}" name="subLineName"/>
                <input type="hidden" value="${subLineCode}" name="subLineCode"/>
                <input type="hidden" name="gameDbId" value=""/>
            </td>
        </tr>
        <tr>
            <td height="1" class="default_line_td">
                game_db表的_id：
            </td>
            <td height="1">
                <input type="text" size="50" id="gameDbId" disabled="disabled"> </input>*
            </td>
        </tr>
        <tr>
            <td height="1" class="default_line_td">
                游戏主图:
            </td>
            <td height="1">
                <img id="gameIcon" src="/static/images/default.jpg"/>
            </td>
        </tr>
        <tr>
            <td height="1" class="default_line_td">
                点赞人数:
            </td>
            <td height="1">
                <input type="text" name="likeNum" size="50" disabled="disabled"> </input>*
            </td>
        </tr>
        <tr>
            <td height="1" class="default_line_td">
                星级(评分）:
            </td>
            <td height="1">
                <input type="text" name="gameRate" size="50" disabled="disabled"> </input>*
            </td>
        </tr>
        <tr>
            <td height="1" class="default_line_td">
                数据显示:
            </td>
            <td height="1">
                <select name="showType" id="showType">
                    <option value="type">游戏类型</option>
                    <option value="likes">点赞人数</option>
                </select>
            </td>
        </tr>
        <tr>
            <td height="1" class="default_line_td">
                游戏名称:
            </td>
            <td height="1">
                <input type="text" name="gameName" size="50"/><span style="color: red">*前端无tag时显示7个字，上传tag时只显示6个字</span>
            </td>
        </tr>

        <tr>
            <td height="1" class="default_line_td">
                游戏类型:
            </td>
            <td height="1">
                <input type="text" name="gameTypeDesc" size="50"> </input><span style="color: red">*前端显示不能大于5个字</span>
            </td>
        </tr>

        <tr>
            <td height="1" class="default_line_td">
                跳转目标:
            </td>
            <td height="1">
                <select name="jumpTarget" id="jumpTarget">
                    <option value="cover">游戏封面</option>
                    <option value="poster">游戏海报</option>
                    <option value="detail">游戏详情页</option>
                    <option value="other">自定义的wap页</option>
                </select>
            </td>
        </tr>

        <tr id="tr_jt">
            <td height="1" class="default_line_td">
                跳转类型:
            </td>
            <td height="1" width="200">
                <select id="jt" name="jt">
                    <c:forEach items="${types}" var="item">
                        <option value='${item.key}'>${item.key}__<fmt:message
                                key="client.item.redirect.${item.key}" bundle="${def}"/></option>
                    </c:forEach>
                </select> <a target="view_window"
                             href="http://wiki.enjoyf.com/index.php?title=Gameclient_client#.E8.B7.B3.E8.BD.AC.E7.B1.BB.E5.9E.8B">使用参考</a>
            </td>
        </tr>
        <tr id="tr_ji">
            <td height="1" class="default_line_td">
                跳转地址:
            </td>
            <td height="1">
                <input type="text" name="ji" size="50"> </input>*
            </td>
        </tr>
        <tr>
            <td height="1" class="default_line_td">
                推荐语:
            </td>
            <td height="1">
                <input name="downloadRecommend" size="25"/><span style="color: red">*前端显示不能大于7个字</span>
        </tr>

        <tr>
            <td height="1" class="default_line_td">
                右上角图标(如公测,限免等):
            </td>
            <td height="1">
                <img id="menu_pic" src="/static/images/default.jpg"/>
                <span id="upload_button" class="upload_button">上传</span>
                                                                                                <span id="loading"
                                                                                                      style="display:none"
                                                                                                      class="loading"><img
                                                                                                        src="/static/images/loading.gif"/></span>
                <input id="input_menu_pic" type="hidden" name="tag"/><span
                    style="color: red">*可选项(没有请留空)</span>
            </td>

        </tr>
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


<div id="chooseWindow" class="easyui-window" title="选择要填加的app"
     data-options="modal:true,closed:true,collapsible:false,minimizable:false,maximizable:false" style="width:950px;">
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