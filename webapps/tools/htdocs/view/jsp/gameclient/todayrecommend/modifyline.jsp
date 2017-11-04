<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head><title>今日推荐管理---${line.lineName}</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/gameclienthotfloorhandler.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script>
        $(function () {
            var desc = '${line.line_desc}'
            if (desc != '') {
                try {
                    var jsonObj = jQuery.parseJSON(desc);
                    $("#menu_pic").attr("src", jsonObj.icon);
                    $("#input_menu_pic").val(jsonObj.icon);

                    $("#form_submit select[name=more]").val(jsonObj.more);
                    $("#form_submit select[name=jt]").val(jsonObj.jt);
                    $("#form_submit input[name=ji]").val(jsonObj.ji);

                } catch (e) {
                 console.log("parse Error"+desc);
                }
            }
            $("#tr_ji").hide();
            $("#tr_jt").hide();
            $("#moreLink").on("change", function () {
                var value = $("#moreLink").val();
                if (value == '1') {
                    $("#tr_ji").show();
                    $("#tr_jt").show();
                } else {
                    $("#tr_ji").hide();
                    $("#tr_jt").hide();
                }
            });
            $("#moreLink").change();

            $('#form_submit').on('submit', function () {
                var name = $.trim($('#input_text_name').val());
                if (name == '') {
                    alert("请填写名称");
                    return false;
                } else {

                    $('#form_submit').submit();
                }
            });
            var coustomSwfu = new SWFUpload(coustomImageSettings);
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
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷手游画报管理 >> 今日推荐管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td"><span
                            style="font-size:16px; font-family: Arial, Helvetica, sans-serif; font-weight:bold;">修改一条Line (${lineCode})</span>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/gameclient/clientline/todayrecommend/modify" method="post" id="form_submit">
                <table width="90%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" class="">
                            <input type="hidden" name="lineId" value="${line.lineId}"/>
                            <input type="hidden" name="lineCode" value="${lineCode}"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            名称
                        </td>
                        <td height="1" class="" width="10">
                            <input type="text" size="20" id="input_text_name" name="lineName"
                                   value="${line.lineName}" /> *必填项(此值会显示在ios/android的热门页面上)
                        </td>
                        <td height="1" align="left"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="150">
                            楼层icon:
                        </td>
                        <td height="1">
                            <img id="menu_pic" src="/static/images/default.jpg"/>
                            <span id="upload_button" class="upload_button">上传</span>
                                                                         <span id="loading" style="display:none"
                                                                               class="loading"><img
                                                                                 src="/static/images/loading.gif"/></span>
                            <input id="input_menu_pic" type="hidden" name="icon"/><span
                                style="color: red">*必填项</span>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            是否有"更多":
                        </td>
                        <td height="1">
                            <select name="more" id="moreLink">
                                <option value="0">没有</option>
                                <option value="1">有</option>
                            </select>
                        </td>
                    </tr>
                    <tr id="tr_jt">
                        <td height="1" class="default_line_td" width="150">
                            跳转类型 :
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
                    <tr align="center">
                        <td colspan="3">
                            <input name="Submit" type="submit" class="default_button" value="提交">
                            <input name="Reset" type="button" class="default_button" value="返回"
                                   onclick="javascipt:window.history.go(-1);">
                        </td>
                    </tr>
                </table>
            </form>
        </td>
    </tr>
</table>

</body>
</html>