<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf8">
<title>创建APP</title>
<link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
<script type="text/javascript" src="/static/include/js/jquery.js"></script>
<script type="text/javascript" src="/static/include/js/common.js"></script>
<script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
<script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="/static/include/swfupload/socialvirtualhandler.js"></script>
<script>
$(document).ready(function() {
    var coustomSwfu = new SWFUpload(coustomImageSettings);
    var coustomSwfu2 = new SWFUpload(coustomImageSettings2);

    $('#form_submit').bind("submit", function() {

        var body = $('#input_body').val();
        if (body.length == 0) {
            alert("请填写内容");
            return false;
        }
        var pic = $('#input_pic').val();
        if (pic.length == 0) {
            alert("请上传图片");
            return false;
        }
        var pics = $('#input_pics').val();
        if (pics.length == 0) {
            alert("请上传图片");
            return false;
        }
        var amr = $('#input_amr').val();
        if (amr.length == 0) {
            alert("请上传音频");
            return false;
        }
        var mp3 = $('#input_mp3').val();
        if (mp3.length == 0) {
            alert("请上传音频");
            return false;
        }
        var audiotime = $('#input_audiotime').val();
        if (audiotime.length == 0) {
            alert("请填写时长");
            return false;
        }

    });
});

var coustomImageSettings = {
    upload_url : "${urlUpload}/json/upload/socialapp/pic",
    post_params : {
        "at" : "${at}"
    },

    // File Upload Settings
    file_size_limit : "2 MB",    // 2MB
    file_types : "*.jpg;*.png;*.gif",
    file_types_description : "请选择图片",
    file_queue_limit : 1,

    file_dialog_complete_handler : fileDialogComplete,
    upload_start_handler:  uploadStart,
    upload_success_handler : uploadSuccess,
    upload_complete_handler : uploadComplete,

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
    upload_url : "${urlUpload}/json/upload/socialapp/amr",
    post_params : {
        "at" : "${at}"
    },

    // File Upload Settings
    file_size_limit : "8 MB",    // 2MB
    file_types : "*.amr",
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
        <td height="22" class="page_navigation_td">>> 运营管理 >> 分类管理 >> 社交端活动管理</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">添加活动</td>
                </tr>
            </table>
            <form action="/joymeapp/socialclient/operate/postcontent" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td">
                            <input type="hidden" name="uno" value="${uno}"/>
                            <input type="hidden" name="pager.offset" value="${pageStartIndex}"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            标题:
                        </td>
                        <td height="1">
                            <input id="input_title" type="text" name="title" size="48" value="${title}"/>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            内容:
                        </td>
                        <td height="1">
                            <textarea id="input_body" type="text" name="body"
                                      style="height: 100px;width: 300px">${body}</textarea>*必填项
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            图片:
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${fn:length(pic) > 0 && fn:length(pics) > 0}">
                                    <img id="pic" src="${pic}" />
                                    <img id="pics" src="${pics}" />
                                </c:when>
                                <c:otherwise>
                                    <img id="pic" src="/static/images/default.jpg" class="img_pic"/>
                                    <img id="pics" src="/static/images/default.jpg" class="img_pic"/>
                                </c:otherwise>
                            </c:choose>
                            <span id="upload_button" class="upload_button">上传</span>
                            <span id="loading" style="display:none" class="loading"><img
                                    src="/static/images/loading.gif"/></span>
                            <input id="input_pic" type="hidden" name="pic" value="${pic}">
                            <input id="input_pics" type="hidden" name="pics" value="${pics}">*必填项
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            音频:
                        </td>
                        <td>
                            <span id="div_audio" style="width: 300px;height: 70px">
                                <c:if test="${fn:length(amr) > 0 && fn:length(mp3) > 0}">
                                    <audio controls="controls">
                                        <source src="${amr}" type="audio/mpeg">
                                        <embed src="${amr}" controls="console" align="middle" loop="false"
                                               autostart="false"
                                               width="300"
                                               height="30"></embed>
                                    </audio>
                                    <audio controls="controls">
                                        <source src="${mp3}" type="audio/mpeg">
                                        <embed src="${mp3}" controls="console" align="middle" loop="false"
                                               autostart="false"
                                               width="300"
                                               height="30"></embed>
                                    </audio>
                                </c:if>
                            </span>
                            <span id="upload_button2" class="upload_button">上传</span>
                            <span id="loading2" style="display:none" class="loading"><img
                                    src="/static/images/loading.gif"/></span>
                            <input id="input_amr" type="hidden" name="amr" value="${amr}">
                            <input id="input_mp3" type="hidden" name="mp3" value="${mp3}">*必填项
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            时长:
                        </td>
                        <td height="1">
                            <input type="text" name="audiotime" id="input_audiotime" value="${audiotime}"/>*必填项,单位：（秒）
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            经纬:
                        </td>
                        <td height="1">
                            经度:<input type="text" name="lon" value="${lon}"/>&nbsp;
                            纬度:<input type="text" name="lat" value="${lat}"/>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            活动ID:
                        </td>
                        <td height="1">
                            <input type="text" name="activityid" value="${activityid}"/>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            平台:
                        </td>
                        <td height="1">
                            <select name="platform">
                                <option value="-1" class="">请选择</option>
                                <option value="0" class="">IOS</option>
                                <option value="1" class="">Android</option>
                            </select>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>
                </table>
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr align="center">
                        <td colspan="3">
                            <input name="Submit" type="submit" class="default_button" value="提交">
                            <input name="Reset" type="button" class="default_button" value="返回"
                                   onclick="javascript:window.history.go(-1);">
                        </td>
                    </tr>
                </table>
            </form>
        </td>
    </tr>
</table>
</body>
</html>