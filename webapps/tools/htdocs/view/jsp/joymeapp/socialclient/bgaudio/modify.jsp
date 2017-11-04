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
    <script type="text/javascript" src="/static/include/swfupload/socialbgaudiohandler.js"></script>
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/dhtmlxcalendar.css">
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/skins/dhtmlxcalendar_dhx_skyblue.css">
    <script src="/static/include/dhtmlxcalendar/dhtmlxcalendar.js"></script>
    <script>
        $().ready(function () {
            doOnLoad();
            var coustomSwfu = new SWFUpload(coustomSettings);
            var coustomSwfu2 = new SWFUpload(coustomSettings2);
            var coustomSwfu3 = new SWFUpload(coustomImageSettings);

            $('#form_submit').bind("submit", function () {

                var name = $('#input_name').val();
                if (name.length == 0) {
                    alert("请填写歌名");
                    return false;
                }
                var pic = $('#input_pic').val();
                if (pic.length == 0) {
                    alert("请上传图片");
                    return false;
                }
                var mp3 = $('#input_mp3_src').val();
                if (mp3.length == 0) {
                    alert("请上传mp3文件");
                    return false;
                }

            });

        });

        var myCalendar;
        function doOnLoad() {
            myCalendar = new dhtmlXCalendarObject(["startdate", "enddate"]);
        }

        var coustomSettings = {
            upload_url: "${urlUpload}/json/upload/mp3",
            post_params: {
                "at": "${at}"
            },

            // File Upload Settings
            file_size_limit: "8 MB",    // 2MB
            file_types: "*.mp3",
            file_types_description: "请选择音频文件",
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
            debug: false}

        var coustomSettings2 = {
            upload_url: "${urlUpload}/json/upload/wav",
            post_params: {
                "at": "${at}"
            },

            // File Upload Settings
            file_size_limit: "8 MB",    // 2MB
            file_types: "*.wav",
            file_types_description: "请选择音频文件",
            file_queue_limit: 1,

            file_dialog_complete_handler: fileDialogComplete2,
            upload_start_handler: uploadStart2,
            upload_success_handler: uploadSuccess2,
            upload_complete_handler: uploadComplete2,

            // Button Settings
            button_image_url: "/static/images/uploadbutton.png",
            button_placeholder_id: "upload_button2",
            button_width: 61,
            button_height: 22,
            moving_average_history_size: 40,


            // Flash Settings
            flash_url: "/static/include/swfupload/swfupload.swf",
            flash9_url: "/static/include/swfupload/swfupload_fp9.swf",

            custom_settings: {},
            // Debug Settings
            debug: false}

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

            file_dialog_complete_handler: fileDialogComplete3,
            upload_start_handler: uploadStart3,
            upload_success_handler: uploadSuccess3,
            upload_complete_handler: uploadComplete3,

            // Button Settings
            button_image_url: "/static/images/uploadbutton.png",
            button_placeholder_id: "upload_button3",
            button_width: 61,
            button_height: 22,
            moving_average_history_size: 40,


            // Flash Settings
            flash_url: "/static/include/swfupload/swfupload.swf",
            flash9_url: "/static/include/swfupload/swfupload_fp9.swf",

            custom_settings: {},
            // Debug Settings
            debug: false}

    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 分类管理 >> 社交端背景音乐管理</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">编辑背景音乐</td>
                </tr>
            </table>
            <form action="/joymeapp/social/bgaudio/modify" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td">
                            <input type="hidden" name="bgaid" value="${bgAudio.audioId}"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            歌名:
                        </td>
                        <td height="1">
                            <input id="input_name" type="text" name="audioname" size="48"
                                   value="${bgAudio.audioName}"/>*必填项
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
                                <c:when test="${fn:length(bgAudio.audioPic) > 0}"><img id="img_pic"
                                                                                       src="${bgAudio.audioPic}"
                                                                                       class="img_pic"/></c:when>
                                <c:otherwise><img id="img_pic" src="/static/images/default.jpg"
                                                  class="img_pic"/></c:otherwise>
                            </c:choose>
                            <span id="upload_button3" class="upload_button">上传</span>
                            <span id="loading3" style="display:none" class="loading"><img
                                    src="/static/images/loading.gif"/></span>
                            <input id="input_pic" type="hidden" name="audiopic" value="${bgAudio.audioPic}">*必填项(100px *
                            100px)
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            描述:
                        </td>
                        <td height="1">
                            <textarea id="input_description" type="text" name="description"
                                      style="height: 100px;width: 300px">${bgAudio.audioDescription}</textarea>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            歌手:
                        </td>
                        <td height="1">
                            <input id="input_singer" type="text" name="singer" size="48" value="${bgAudio.singer}"/>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            mp3文件:
                        </td>
                        <td>
                            <span id="div_mp3_src" style="width: 300px;height: 70px">
                                <c:if test="${fn:length(bgAudio.mp3Src) > 0}">
                                    <audio controls="controls">
                                        <source src="${bgAudio.mp3Src}" type="audio/mpeg">
                                        <embed src="${bgAudio.mp3Src}" controls="console" align="middle" loop="false"
                                               autostart="false"
                                               width="300"
                                               height="30"></embed>
                                    </audio>
                                </c:if>
                            </span>
                            <span id="upload_button" class="upload_button">上传</span>
                            <span id="loading" style="display:none" class="loading"><img
                                    src="/static/images/loading.gif"/></span>
                            <input id="input_mp3_src" type="hidden" name="mp3src" value="${bgAudio.mp3Src}">*必填项
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            wav文件:
                        </td>
                        <td>
                            <span id="div_wav_src" style="width: 300px;height: 70px">
                                <c:if test="${fn:length(bgAudio.wavSrc) > 0}">
                                    <audio controls="controls">
                                        <source src="${bgAudio.wavSrc}" type="audio/mpeg">
                                        <embed src="${bgAudio.wavSrc}" controls="console" align="middle" loop="false"
                                               autostart="false"
                                               width="300"
                                               height="30"></embed>
                                    </audio>
                                </c:if>
                            </span>
                            <span id="upload_button2" class="upload_button">上传</span>
                            <span id="loading2" style="display:none" class="loading"><img
                                    src="/static/images/loading.gif"/></span>
                            <input id="input_wav_src" type="hidden" name="wavsrc" value="${bgAudio.wavSrc}">
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="200px">
                            角标属性:
                        </td>
                        <td height="1">
                            <c:forEach items="${SubscriptTypeColl}" var="subscriptType">
                                <input type="radio" name="subscripttype"
                                <c:if test="${subscriptType.code == bgAudio.subscript.type}">checked="true"</c:if>
                                value="${subscriptType.code}"/>
                                <fmt:message key="subscript.type.${subscriptType.code}" bundle="${def}"/>&nbsp;
                            </c:forEach>
                            <br/>
                            <input type="text" class="default_input_singleline" size="16" maxlength="20"
                                   name="startdate" id="startdate"
                                   value="${bgAudio.subscript.startDate}"/>
                            &nbsp;--&nbsp;
                            <input type="text" class="default_input_singleline" size="16" maxlength="20" name="enddate"
                                   id="enddate"
                                   value="${bgAudio.subscript.endDate}"/>
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