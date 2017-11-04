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
    <script type="text/javascript" src="/static/include/swfupload/socialwatermarkhandler.js"></script>
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/dhtmlxcalendar.css">
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/skins/dhtmlxcalendar_dhx_skyblue.css">
    <script src="/static/include/dhtmlxcalendar/dhtmlxcalendar.js"></script>
    <script>
        $().ready(function () {
            doOnLoad();
            var coustomSwfu = new SWFUpload(coustomImageSettings);
            var coustomSwfu2 = new SWFUpload(coustomImageSettings2);
            var coustomSwfu3 = new SWFUpload(coustomImageSettings3);
            var coustomSwfu4 = new SWFUpload(coustomImageSettings4);

            $('#form_submit').bind("submit", function () {
                var name = $('#input_title').val();
                if (name.length == 0) {
                    alert("请填写标题");
                    return false;
                }
                var iosicon = $('#input_ios_icon').val();
                if (iosicon.length == 0) {
                    alert("请上传ios小图标");
                    return false;
                }
//                var androidicon = $('#input_android_icon').val();
//                if (androidicon.length == 0) {
//                    alert("请上传android小图标");
//                    return false;
//                }
                var iospic = $('#input_ios_pic').val();
                if (iospic.length == 0) {
                    alert("请上传ios图片");
                    return false;
                }
//                var androidpic = $('#input_android_pic').val();
//                if (androidpic.length == 0) {
//                    alert("请上传android图片");
//                    return false;
//                }
            });
        });

        function check() {
            if ($("#select_bind_aid").find("option:selected").attr('data-status') == "y") {
                $("#select_bind_aid option").eq(0).attr("selected", true);
            }
        }

        var myCalendar;
        function doOnLoad() {
            myCalendar = new dhtmlXCalendarObject(["startdate", "enddate"]);
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
            debug: false}

        var coustomImageSettings2 = {
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

        var coustomImageSettings3 = {
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

        var coustomImageSettings4 = {
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

            file_dialog_complete_handler: fileDialogComplete4,
            upload_start_handler: uploadStart4,
            upload_success_handler: uploadSuccess4,
            upload_complete_handler: uploadComplete4,

            // Button Settings
            button_image_url: "/static/images/uploadbutton.png",
            button_placeholder_id: "upload_button4",
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
        <td height="22" class="page_navigation_td">>> 运营管理 >> 分类管理 >> 社交端活动管理</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">添加活动</td>
                </tr>
            </table>
            <form action="/joymeapp/social/watermark/create" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            标题:
                        </td>
                        <td height="1">
                            <input id="input_title" type="text" name="title" size="48" value="${title}"/>*必填项
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
                                      style="height: 100px;width: 300px">${description}</textarea>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            IOS正方形小图标:
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${fn:length(iosIcon) > 0}"><img id="ios_icon" src="${iosIcon}"
                                                                              class="img_pic"/></c:when>
                                <c:otherwise><img id="ios_icon" src="/static/images/default.jpg"
                                                  class="img_pic"/></c:otherwise>
                            </c:choose>
                            <span id="upload_button" class="upload_button">上传</span>
                            <span id="loading" style="display:none" class="loading"><img
                                    src="/static/images/loading.gif"/></span>
                            <input id="input_ios_icon" type="hidden" name="iosicon" value="${iosIcon}">*必填项(100px *
                            100px)
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            Android正方形小图标:
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${fn:length(androidIcon) > 0}"><img id="android_icon" src="${androidIcon}"
                                                                                  class="img_pic"/></c:when>
                                <c:otherwise><img id="android_icon" src="/static/images/default.jpg"
                                                  class="img_pic"/></c:otherwise>
                            </c:choose>
                            <span id="upload_button2" class="upload_button">上传</span>
                            <span id="loading2" style="display:none" class="loading"><img
                                    src="/static/images/loading.gif"/></span>
                            <input id="input_android_icon" type="hidden" name="androidicon" value="${androidIcon}">
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            IOS水印图片:
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${fn:length(iosPic) > 0}"><img id="ios_pic" src="${iosPic}"
                                                                             class="img_pic"/></c:when>
                                <c:otherwise><img id="ios_pic" src="/static/images/default.jpg"
                                                  class="img_pic"/></c:otherwise>
                            </c:choose>
                            <span id="upload_button3" class="upload_button">上传</span>
                            <span id="loading3" style="display:none" class="loading"><img
                                    src="/static/images/loading.gif"/></span>
                            <input id="input_ios_pic" type="hidden" name="iospic" value="${iosPic}">*必填项(640px * 380px)
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            Android水印图片:
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${fn:length(androidPic) > 0}"><img id="android_pic" src="${androidPic}"
                                                                                 class="img_pic"/></c:when>
                                <c:otherwise><img id="android_pic" src="/static/images/default.jpg"
                                                  class="img_pic"/></c:otherwise>
                            </c:choose>
                            <span id="upload_button4" class="upload_button">上传</span>
                            <span id="loading4" style="display:none" class="loading"><img
                                    src="/static/images/loading.gif"/></span>
                            <input id="input_android_pic" type="hidden" name="androidpic" value="${androidPic}">
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
                                <c:if test="${subscriptType.code == 0}">checked="true"</c:if>
                                value="${subscriptType.code}"/><fmt:message
                                    key="subscript.type.${subscriptType.code}" bundle="${def}"/>&nbsp;
                            </c:forEach>
                            <br/>
                            <input type="text" class="default_input_singleline" size="16" maxlength="20"
                                   name="startdate" id="startdate"
                                   value="<fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                            &nbsp;--&nbsp;
                            <input type="text" class="default_input_singleline" size="16" maxlength="20" name="enddate"
                                   id="enddate"
                                   value="<fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            绑定活动:
                        </td>
                        <td height="1">
                            <select name="activityid" id="select_bind_aid" onchange="check()">
                                <option value="">请选择</option>
                                <c:forEach items="${activityList}" var="activity">
                                    <option value="${activity.activityId}" data-status="${activity.bindStatus}"
                                    <c:if test="${activity.bindStatus == 'y'}">style="color: #ff0000;"</c:if>
                                    <c:if test="${activity.activityId == activityId}">selected="selected"</c:if>>
                                    ${activity.title}
                                    <c:if test="${activity.bindStatus == 'y'}">(已有绑定)</c:if>
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                        <td height="1" class=>
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