<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <title>热门页游戏分类修改最终条目页</title>
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

            //页面表单 提交
            $("#submit").click(function () {
                // var gameDbId = $.trim($("#form_submit input[name=gameDbId]").val());
                //    var gamePublicTime = $('#itemCreateDate').datetimebox('getValue');


                var gameName = $.trim($("#form_submit input[name=gameName]").val());
                var gameTypeDesc = $.trim($("#form_submit input[name=gameTypeDesc]").val());
                var downloadRecommend = $.trim($("#form_submit input[name=downloadRecommend]").val());

                if (gameName == '' || gameName.length > 7) {
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


            $("#gameDbId").val('${game.gameDbId}');
            $("#gameIcon").attr("src", '${game.gameIcon}');
            $("#form_submit input[name=likeNum]").val('${game.gameDBCover.coverAgreeNum}');

            var gameRate = (Number(${game.gameDBCoverFieldJson.value1}) + Number(${game.gameDBCoverFieldJson.value2}) + Number(${game.gameDBCoverFieldJson.value3}) + Number(${game.gameDBCoverFieldJson.value4}) + Number(${game.gameDBCoverFieldJson.value5})) / 5;
            $("#form_submit input[name=gameRate]").val(gameRate.toFixed(1));

            var desc = '${item.desc}';
            if (desc != '') {
                var jsonObj = jQuery.parseJSON(desc);

                $("#form_submit input[name=gameName]").val(jsonObj.gameName);

                $("#form_submit select[name=showType]").val(jsonObj.showType);
                $("#form_submit input[name=gameTypeDesc]").val(jsonObj.gameTypeDesc);
                $("#form_submit select[name=jumpTarget]").val(jsonObj.jumpTarget);


                $("#form_submit select[name=jt]").val(jsonObj.jt);
                $("#form_submit input[name=ji]").val(jsonObj.ji);

                $("#form_submit input[name=downloadRecommend]").val(jsonObj.downloadRecommend);
                $("#form_submit input[name=tag]").val(jsonObj.tag);
                $("#menu_pic").attr("src", jsonObj.tag);
            }

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

            $("#toDeleteTag").on("click", function () {
                var msg = "您确定要删除右上角小图标吗？\n\n请确认！";
                if (confirm(msg) == true) {
                    $("#menu_pic").attr("src", "");
                    $("#input_menu_pic").val("");
                }
            });

        });


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
            的修改条目</label></td>
    </tr>
    <tr>
        <td height="32px">
            <input type="button" class="default_button" value="返回"
                   onclick="javascipt:window.location.href='/gameclient/clientline/gamecategory/itemlist?subLineId=${subLineId}&subLineName=${subLineName}&subLineCode=${subLineCode}&lineCode=${lineCode}&lineName=${lineName}&platform=${platform}';"/>

        </td>
    </tr>
</table>

<form action="/gameclient/clientline/gamecategory/itemmodify" method="post" id="form_submit">
    <table border="0" cellspacing="2" cellpadding="2">
        <tr>
            <td height="1" width="240">
                <input type="hidden" value="${itemId}" name="itemId"/>
                <input type="hidden" value="${platform}" name="platform"/>
                <input type="hidden" value="${lineCode}" name="lineCode"/>
                <input type="hidden" value="${lineName}" name="lineName"/>
                <input type="hidden" value="${subLineId}" name="subLineId"/>
                <input type="hidden" value="${subLineName}" name="subLineName"/>
                <input type="hidden" value="${subLineCode}" name="subLineCode"/>
                <input type="hidden" value="${game.gameDbId}" name="gameDbId"/>
            </td>
        </tr>
        <tr>
            <td height="1" class="default_line_td">
                client_line_item表的id：
            </td>
            <td height="1">
                <input type="text" size="50" id="itemId" value="${itemId}" disabled="disabled"> </input>*
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
                    style="color: red">*可选项(没有请留空)</span> <input type="button" class="default_button"
                                                                 id="toDeleteTag" value="删除右上角图标"/>
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