<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title></title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/joymeappmenuhandler.js"></script>
    <script>
        $().ready(function() {
            $('#form_submit').bind('submit', function() {
                var tagname = $("[name='tagname']").val().trim();
                var tagdesc = $("[name='tagdesc']").val().trim();
                var pepolenum = $("[name='pepolenum']").val().trim();
                var picurl = $("[name='picurl']").val();
                if (tagname == '') {
                    alert("请填写圈子名称");
                    return false;
                }
                if (tagdesc == '') {
                    alert("请填写圈子介绍");
                    return false;
                }
                if (pepolenum == '') {
                    alert("请填写关注人数");
                    return false;
                }
                if (picurl == '') {
                    alert("请上传图片");
                    return false;
                }
            });
            var coustomSwfu = new SWFUpload(coustomImageSettings);
            var coustomSwfu2 = new SWFUpload(coustomImageSettings2);
        });
        var coustomImageSettings = {
            upload_url : "${urlUpload}/json/upload/qiniu",
            post_params : {
                "at" : "joymeplatform",
                "filetype":"original"
            },

            // File Upload Settings
            file_size_limit : "15 kb",    // 2MB
            file_types : "*.jpg;*.png;*.gif",
            file_types_description : "请选择图片",
            file_queue_limit : 1,

            file_dialog_complete_handler : fileDialogComplete,
            upload_start_handler:  uploadStart,
            upload_success_handler : uploadSuccess,
            upload_complete_handler : uploadComplete,
            file_queue_error_handler: uploadError,

            // Button Settings
            button_image_url : "/static/images/uploadbutton.png",
            button_placeholder_id : "upload_button",
            button_width: 61,
            button_height: 22,
            moving_average_history_size: 40,

            // Flash Settings
            flash_url : "/static/include/swfupload/swfupload.swf",
            flash9_url : "/static/include/swfupload/swfupload_fp9.swf",

            custom_settings : {},
            // Debug Settings
            debug: false}

        var coustomImageSettings2 = {
            upload_url : "${urlUpload}/json/upload/qiniu",
            post_params : {
                "at" : "joymeplatform",
                "filetype":"original"
            },

            // File Upload Settings
            file_size_limit : "2 MB",    // 2MB
            file_types : "*.jpg;*.png;*.gif",
            file_types_description : "请选择图片",
            file_queue_limit : 1,

            file_dialog_complete_handler : fileDialogComplete2,
            upload_start_handler:  uploadStart2,
            upload_success_handler : uploadSuccess2,
            upload_complete_handler : uploadComplete2,

            // Button Settings
            button_image_url : "/static/images/uploadbutton.png",
            button_placeholder_id : "upload_button2",
            button_width: 61,
            button_height: 22,
            moving_average_history_size: 40,

            // Flash Settings
            flash_url : "/static/include/swfupload/swfupload.swf",
            flash9_url : "/static/include/swfupload/swfupload_fp9.swf",

            custom_settings : {},
            // Debug Settings
            debug: false}

    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷手游画报管理 >> 圈子管理</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">创建新圈子</td>
                </tr>
            </table>
            <form action="/animetag/miyou/create" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            圈子名称:
                        </td>
                        <td height="1">
                            <input type="text" name="tagname" size="48" maxlength="10"/> *必填项 最多10个字
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            圈子介绍:
                        </td>
                        <td height="1">
                            <textarea rows="10" cols="50" maxlength="50" name="tagdesc"></textarea>*必填项 最多20个字
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            关注人数:
                        </td>
                        <td height="1">
                            <input type="text" name="pepolenum"   onkeyup="value=value.replace(/[^\d]/g,'')" /> *必填项
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            图片:
                        </td>
                        <td>
                            <img id="menu_pic" src="/static/images/default.jpg" class="img_pic"/>
                            <span id="upload_button" class="upload_button">上传</span>
                            <span id="loading" style="display:none" class="loading"><img
                                    src="/static/images/loading.gif"/></span>
                            <input id="input_menu_pic" type="hidden" name="picurl" value=""><span style="color: red">*必填项 120*120 不超过15KB</span>
                        </td>
                        <td height="1" class=>
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