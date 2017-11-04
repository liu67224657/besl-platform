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
<script type="text/javascript" src="/static/include/swfupload/socialactivityhandler.js"></script>
<link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/dhtmlxcalendar.css">
<link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/skins/dhtmlxcalendar_dhx_skyblue.css">
<script src="/static/include/dhtmlxcalendar/dhtmlxcalendar.js"></script>
<script>
$(document).ready(function () {
    doOnLoad();
    var coustomSwfu = new SWFUpload(coustomImageSettings);
    var coustomSwfu2 = new SWFUpload(coustomImageSettings2);
    var coustomSwfu3 = new SWFUpload(coustomImageSettings3);
    var coustomSwfu4 = new SWFUpload(coustomImageSettings4);
    var coustomSwfu5 = new SWFUpload(coustomImageSettings5);
    var coustomSwfu6 = new SWFUpload(coustomImageSettings6);

    var index = parseInt(1);

    $('#input_append').click(function () {
        index = index + 1;
        $('#td_awards').append('<table id="table_award_' + index + '" name="table_award" bgcolor="#e0ffff">' +
                '<tr><td>名称：</td><td><input type="text" size="48" name="name" id="input_text_name_' + index + '"/></td></tr>' +
                '<tr><td>等级：</td><td><input type="text" size="48" name="name" id="input_text_level_' + index + '"/></td></tr>' +
                '<tr><td>总数：</td><td><input type="text" size="48" name="name" id="input_text_total_' + index + '"/></td></tr>' +
                '<tr><td height="1" colspan="2" class="default_line_td"></td></tr>' +
                '</table>');
    });

    $('#input_remove').click(function () {
        $('#table_award_' + index).remove();
        index = index - 1;
    });

    $('#form_submit').bind("submit", function () {

        var title = $('#input_title').val();
        if (title.length == 0) {
            alert("请填写标题");
            return false;
        }
        var description = $('#input_description').val();
        if (description.length == 0) {
            alert("请填写描述");
            return false;
        }
        var ios_icon = $('#input_ios_icon').val();
        if (ios_icon.length == 0) {
            alert("请上传IOS水印弹层图");
            return false;
        }
//        var android_icon = $('#input_android_icon').val();
//        if (android_icon.length == 0) {
//            alert("请上传android水印弹层图");
//            return false;
//        }
        var ios_small_pic = $('#input_ios_small_pic').val();
        if (ios_small_pic.length == 0) {
            alert("请上传IOS广场小图");
            return false;
        }
//        var android_small_pic = $('#input_android_small_pic').val();
//        if (android_small_pic.length == 0) {
//            alert("请上传android广场小图");
//            return false;
//        }
        var ios_big_pic = $('#input_ios_big_pic').val();
        if (ios_big_pic.length == 0) {
            alert("请上传IOS广场大图");
            return false;
        }
//        var android_big_pic = $('#input_android_big_pic').val();
//        if (android_big_pic.length == 0) {
//            alert("请上传android广场大图");
//            return false;
//        }

        var awardArr = new Array();
        $('table[name = table_award]').each(function () {
            var id = $(this).attr('id').replace('table_award_', '');

            var name = $('#input_text_name_' + id).val();
            var level = $('#input_text_level_' + id).val();
            var total = $('#input_text_total_' + id).val();
            var obj = {
                'name': name,
                'level': level,
                'total': total
            };
            awardArr.push(obj);
        });
        $('#input_jsonaward').val(JSON.stringify(awardArr));

    });
});

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

var coustomImageSettings5 = {
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

    file_dialog_complete_handler: fileDialogComplete5,
    upload_start_handler: uploadStart5,
    upload_success_handler: uploadSuccess5,
    upload_complete_handler: uploadComplete5,

    // Button Settings
    button_image_url: "/static/images/uploadbutton.png",
    button_placeholder_id: "upload_button5",
    button_width: 61,
    button_height: 22,
    moving_average_history_size: 40,


    // Flash Settings
    flash_url: "/static/include/swfupload/swfupload.swf",
    flash9_url: "/static/include/swfupload/swfupload_fp9.swf",

    custom_settings: {},
    // Debug Settings
    debug: false}

var coustomImageSettings6 = {
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

    file_dialog_complete_handler: fileDialogComplete6,
    upload_start_handler: uploadStart6,
    upload_success_handler: uploadSuccess6,
    upload_complete_handler: uploadComplete6,

    // Button Settings
    button_image_url: "/static/images/uploadbutton.png",
    button_placeholder_id: "upload_button6",
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
<form action="/joymeapp/social/activity/modify" method="post" id="form_submit">
<table width="100%" border="0" cellspacing="1" cellpadding="0">
<tr>
    <td height="1" colspan="3" class="default_line_td">
        <input type="hidden" name="aid" value="${activity.activityId}"/>
        <input type="hidden" name="qtitle" value="${queryTitle}"/>
        <input type="hidden" name="qstarttime" value="${queryStartTime}"/>
        <input type="hidden" name="qendtime" value="${queryEndTime}"/>
        <input type="hidden" name="qcreateuserid" value="${queryCreateUserId}"/>
        <input type="hidden" name="qstatus" value="${queryStatus}"/>
        <input type="hidden" name="qordertype" value="${queryOrderType}"/>
        <input type="hidden" name="pager.offset" value="${page.startRowIdx}"/>
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td" width="100">
        标题:
    </td>
    <td height="1">
        <input id="input_title" type="text" name="title" size="48" value="${activity.title}"/>*必填项
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
                  style="height: 100px;width: 300px">${activity.description}</textarea>*必填项
    </td>
    <td height="1">
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td" width="100">
        IOS水印弹层图:
    </td>
    <td>
        <c:choose>
            <c:when test="${fn:length(activity.iosIcon) > 0}"><img id="ios_icon" src="${activity.iosIcon}"
                                                                   class="img_pic"/></c:when>
            <c:otherwise><img id="ios_icon" src="/static/images/default.jpg"
                              class="img_pic"/></c:otherwise>
        </c:choose>
        <span id="upload_button" class="upload_button">上传</span>
                            <span id="loading" style="display:none" class="loading"><img
                                    src="/static/images/loading.gif"/></span>
        <input id="input_ios_icon" type="hidden" name="iosicon" value="${activity.iosIcon}">*必填项(500px * 500px)
    </td>
    <td height="1">
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td" width="100">
        Android水印弹层图:
    </td>
    <td>
        <c:choose>
            <c:when test="${fn:length(activity.androidIcon) > 0}"><img id="android_icon" src="${activity.androidIcon}"
                                                                       class="img_pic"/></c:when>
            <c:otherwise><img id="android_icon" src="/static/images/default.jpg"
                              class="img_pic"/></c:otherwise>
        </c:choose>
        <span id="upload_button2" class="upload_button">上传</span>
                            <span id="loading2" style="display:none" class="loading"><img
                                    src="/static/images/loading.gif"/></span>
        <input id="input_android_icon" type="hidden" name="androidicon" value="${activity.androidIcon}">
    </td>
    <td height="1">
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td" width="100">
        IOS广场小图:
    </td>
    <td>
        <c:choose>
            <c:when test="${fn:length(activity.iosSmallPic) > 0}"><img id="ios_small_pic" src="${activity.iosSmallPic}"
                                                                       class="img_pic"/></c:when>
            <c:otherwise><img id="ios_small_pic" src="/static/images/default.jpg"
                              class="img_pic"/></c:otherwise>
        </c:choose>
        <span id="upload_button3" class="upload_button">上传</span>
                            <span id="loading3" style="display:none" class="loading"><img
                                    src="/static/images/loading.gif"/></span>
        <input id="input_ios_small_pic" type="hidden" name="iossmallpic" value="${activity.iosSmallPic}">*必填项(285px *
        200px)
    </td>
    <td height="1">
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td" width="100">
        Android广场小图:
    </td>
    <td>
        <c:choose>
            <c:when test="${fn:length(activity.androidSmallPic) > 0}"><img id="android_small_pic"
                                                                           src="${activity.androidSmallPic}"
                                                                           class="img_pic"/></c:when>
            <c:otherwise><img id="android_small_pic" src="/static/images/default.jpg"
                              class="img_pic"/></c:otherwise>
        </c:choose>
        <span id="upload_button4" class="upload_button">上传</span>
                            <span id="loading4" style="display:none" class="loading"><img
                                    src="/static/images/loading.gif"/></span>
        <input id="input_android_small_pic" type="hidden" name="androidsmallpic" value="${activity.androidSmallPic}">
    </td>
    <td height="1">
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td" width="100">
        IOS广场大图:
    </td>
    <td>
        <c:choose>
            <c:when test="${fn:length(activity.iosBigPic) > 0}"><img id="ios_big_pic" src="${activity.iosBigPic}"
                                                                     class="img_pic"/></c:when>
            <c:otherwise><img id="ios_big_pic" src="/static/images/default.jpg"
                              class="img_pic"/></c:otherwise>
        </c:choose>
        <span id="upload_button5" class="upload_button">上传</span>
                            <span id="loading5" style="display:none" class="loading"><img
                                    src="/static/images/loading.gif"/></span>
        <input id="input_ios_big_pic" type="hidden" name="iosbigpic" value="${activity.iosBigPic}">*必填项(580px * 200px)
    </td>
    <td height="1">
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td" width="100">
        Android广场大图:
    </td>
    <td>
        <c:choose>
            <c:when test="${fn:length(activity.androidBigPic) > 0}"><img id="android_big_pic"
                                                                         src="${activity.androidBigPic}"
                                                                         class="img_pic"/></c:when>
            <c:otherwise><img id="android_big_pic" src="/static/images/default.jpg"
                              class="img_pic"/></c:otherwise>
        </c:choose>
        <span id="upload_button6" class="upload_button">上传</span>
                            <span id="loading6" style="display:none" class="loading"><img
                                    src="/static/images/loading.gif"/></span>
        <input id="input_android_big_pic" type="hidden" name="androidbigpic" value="${activity.androidBigPic}">
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
            <c:if test="${subscriptType.code == activity.subscript.type}">checked="true"</c:if>
            value="${subscriptType.code}"/>
            <fmt:message key="subscript.type.${subscriptType.code}" bundle="${def}"/>&nbsp;
        </c:forEach>
        <br/>
        <input type="text" class="default_input_singleline" size="16" maxlength="20"
               name="startdate" id="startdate"
               value="${activity.subscript.startDate}"/>
        &nbsp;--&nbsp;
        <input type="text" class="default_input_singleline" size="16" maxlength="20" name="enddate"
               id="enddate"
               value="${activity.subscript.endDate}"/>
    </td>
    <td height="1">
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td" width="100">
        分享:
    </td>
    <td height="1">
        <select name="shareid" id="select_shareid">
            <option value="">请选择</option>
            <c:forEach items="${shareInfoList}" var="share">
                <option value="${share.shareId}"
                <c:if test="${share.shareId == activity.shareId}">selected="selected"</c:if>>
                ${share.shareKey}
                </option>
            </c:forEach>
        </select>
    </td>
    <td height="1" class=>
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td" width="100">
        跳转类型:
    </td>
    <td height="1">
        <select name="retype" id="select_retype">
            <option value="0"
            <c:if test="${activity.redirectType == 0}">selected="selected"</c:if>>WEBVIEW</option>
            <option value="1"
            <c:if test="${activity.redirectType == 1}">selected="selected"</c:if>>NATIVE</option>
        </select>
    </td>
    <td height="1" class=>
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td" width="100">
        跳转地址:
    </td>
    <td height="1">
        <input type="text" name="reurl" size="64" value="${activity.redirectUrl}"/>
    </td>
    <td height="1" class=>
    </td>
</tr>
<tr>
    <td height="1" class="default_line_td" width="100">
        奖品:
        <input type="hidden" name="jsonaward" id="input_jsonaward"/>
    </td>
    <td height="1" id="td_awards">
        <c:choose>
            <c:when test="${! empty activity.awardSet.awardSet}">
                <c:forEach items="${activity.awardSet.awardSet}" var="award" varStatus="st">
                    <table id="table_award_${st.index+1}" name="table_award" bgcolor="#e0ffff">
                        <tr>
                            <td>
                                名称：
                            </td>
                            <td>
                                <input type="text" size="48" name="name"
                                       id="input_text_name_${st.index+1}" value="${award.name}"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                等级：
                            </td>
                            <td>
                                <input type="text" size="48" name="level"
                                       id="input_text_level_${st.index+1}" value="${award.level}"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                总数：
                            </td>
                            <td>
                                <input type="text" size="48" name="total"
                                       id="input_text_total_${st.index+1}" value="${award.total}"/>
                            </td>
                        </tr>
                        <tr>
                            <td height="1" colspan="2" class="default_line_td"></td>
                        </tr>
                    </table>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <table id="table_award_1" name="table_award" bgcolor="#e0ffff">
                    <tr>
                        <td>
                            名称：
                        </td>
                        <td>
                            <input type="text" size="48" name="name"
                                   id="input_text_name_1"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            等级：
                        </td>
                        <td>
                            <input type="text" size="48" name="level"
                                   id="input_text_level_1"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            总数：
                        </td>
                        <td>
                            <input type="text" size="48" name="total"
                                   id="input_text_total_1"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" colspan="2" class="default_line_td"></td>
                    </tr>
                </table>
            </c:otherwise>
        </c:choose>
    </td>
    <td height="1" class=>
        <input type="button" name="button" id="input_append" value="添加"/>
        <input type="button" name="button" id="input_remove" value="删除"/>
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