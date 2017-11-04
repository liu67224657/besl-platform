<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
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
    <script type="text/javascript" src="/static/include/swfupload/socialshare.js"></script>
    <style type="text/css">
        .td_cent {
            text-align: center;
            vertical-align: middle;
        }
    </style>
    <script>
        $().ready(function () {
            var coustomSwfu = new SWFUpload(coustomImageSettings);
            $('#form_submit').bind('submit', function () {

                var share_type = $.trim($("#share_type").val())
                if (share_type == '') {
                    alert("请选择一个类型");
                    $("#share_type").focus();
                    return false;
                }
                if ($("#platform").val() == '') {
                    alert("请选择一个选择发布的平台");
                    $("#platform").focus();
                    return false;
                }

                if ($("#sharedomain").val() == '') {
                    alert("请选择一个第三方平台");
                    $("#sharedomain").focus();
                    return false;
                }

                var activityid = $('#activityid').val();
                if ($("#sharedomain").val() == "def") {
                    if (activityid != "") {
                        alert("第三方平台为默认，活动ID不需要填写。");
                        return false;
                    }
                }
                if ($("#sharedomain").val() != "def" && share_type == 2) {
                    if (activityid == "") {
                        alert("请需要活动ID。");
                        return false;
                    }
                    if (isNaN(activityid)) {
                        alert("活动ID需要填写数字。");
                        return false;
                    }
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
            debug: false}
    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 条目管理 >> 新增社交端分享</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">新增社交端分享</td>
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
            <form action="/joymeapp/socialclient/share/create" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            标题:
                        </td>
                        <td height="1">
                            <input id="title" type="text" name="title" size="90" value="${socialShare.title}"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            链接地址:
                        </td>
                        <td height="1">
                            <input id="url" type="text" name="url" size="90" value="${socialShare.url}"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            活动ID:
                        </td>
                        <td height="1">
                            <input id="activityid" type="text" name="activityid" size="90"
                                   value="${socialShare.activityid}"/><span style="color: red">默认填-1</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            描述:
                        </td>
                        <td height="1">
                            <textarea id="body" type="text" name="body"
                                      style="height: 100px;width: 575px">${socialShare.body}</textarea><span
                                style="color: red">标签:{body} 内容 {url} 链接</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent">
                            选择APP
                        </td>
                        <td height="1" class="">
                            <select name="appkey" style="width: 81px">
                                <c:forEach var="app" items="${appList}">
                                    <option value="${app.appId}">${app.appName}</option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent">
                            类型：
                        </td>
                        <td height="1" class="">
                            <select name="share_type" id="share_type" style="width: 81px">
                                <option value="">请选择</option>
                                <c:forEach var="type" items="${socialShareTypes}">
                                    <option value="${type.code}"><fmt:message key="social.share.type.${type.code}"
                                                                              bundle="${def}"/></option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent">
                            第三方平台：
                        </td>
                        <td height="1" class="">
                            <select name="sharedomain" id="sharedomain">
                                <option value="">请选择</option>
                                <c:forEach var="type" items="${accountDomainList}">
                                    <option value="${type.code}"
                                    <c:if test="${type.code==socialShare.sharedomain.code}">selected='selected'</c:if>><fmt:message
                                        key="social.share.third.type.${type.code}" bundle="${def}"/></option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            选择发布的平台：
                        </td>
                        <td height="1" class="">
                            <select name="platform" style="width: 81px" id="platform">
                                <c:forEach var="platform" items="${platformList}">
                                    <option value="${platform.code}"><fmt:message
                                            key="joymeapp.platform.${platform.code}" bundle="${def}"/></option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            图片:
                        </td>
                        <td>
                            <img id="picurl1_src" src="/static/images/default.jpg"
                                 class="img_pic" width="200px" height="200px"/>
                            <span id="upload_button" class="upload_button">上传</span>
                            <span id="loading" style="display:none" class="loading"><img
                                    src="/static/images/loading.gif"/></span>
                            <input id="picurl1" type="hidden" name="pic_url">
                        </td>
                        <td height="1">
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