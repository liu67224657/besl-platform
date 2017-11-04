<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta content="text/html; charset=utf8">
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
    <script>
        $().ready(function () {
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
            debug: false}

        function fileDialogComplete(numFilesSelected, numFilesQueued) {
            try {
                this.startUpload();
            } catch (ex) {
                this.debug(ex);
            }
        }

        function uploadStart(file) {
            $('#loading').css('display', '');
        }

        function uploadSuccess(file, serverData) {

            try {
                var jsonData = eval('(' + serverData + ')');
                if (jsonData.status_code == "1") {
                    var largeLogoSrc = genImgDomain(jsonData.result[0], DOMAIN);
                    $('#menu_pic').attr('src', largeLogoSrc);
                    $('#input_menu_pic').val(largeLogoSrc);
                } else {
                    if (jsonData.msg == 'token_faild') {
                        alert('登录失败');
                    } else {
                        if (jsonData.msg == '') {
                            alert('上传失败');
                        } else {
                            alert(jsonData.msg);
                        }
                    }
                }
            } catch (ex) {
                this.debug(ex);
            }
        }

        function uploadComplete(file) {
            try {
                if (this.getStats().files_queued <= 0) {
                    $('#loading').css('display', 'none');
                }
            } catch (ex) {
                this.debug(ex);
            }
        }

    </script>
</head>
<body>
<table height="100%" width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷APP >> 活动轮播图管理</td>
    </tr>
</table>
<table style="height: 100%;width: 100%" border="0" cellpadding="0" cellspacing="0">
    <form action="" method="post" id="form_submit">
        <table width="100%" border="0" cellspacing="1" cellpadding="0">
            <tr>
                <td height="1" colspan="3" class="default_line_td">
                    <input type="hidden" name="channeltopmenu" id="input_channel_top_menu"/>
                </td>
            </tr>
            <tr>
                <td height="1" class="default_line_td" width="100">
                    渠道:
                </td>
                <td height="1">
                    <select name="channelcode" id="select_channel_code">
                        <option value="">请选择</option>
                        <c:forEach items="${channelList}" var="channel">
                            <option value="${channel.channelCode}"
                            <c:if test="${channel.channelCode == channelTopMenu.channelCode}">selected="selected" </c:if>
                            >${channel.channelName}</option>
                        </c:forEach>
                    </select>
                    <span style="color: #ff0000;">*选择渠道后，以下内容必须填写</span>
                </td>
                <td height="1" class=>
                </td>
            </tr>
            <tr>
                <td height="1" class="default_line_td" width="100">
                    菜单名称:
                </td>
                <td height="1">
                    <input type="text" value="${channelTopMenu.name}" name="name" id="input_text_name" size="48"/>
                </td>
                <td height="1" class=>
                </td>
            </tr>
            <tr>
                <td height="1" class="default_line_td" width="100">
                    菜单URL:
                </td>
                <td height="1">
                    <input type="text" value="${channelTopMenu.url}" name="menuurl" id="input_text_mewnurl" size="48"/>
                </td>
                <td height="1" class=>
                </td>
            </tr>
            <tr>
                <td height="1" class="default_line_td" width="100">
                    图片:
                </td>
                <td>
                    <img id="menu_pic" src="${channelTopMenu.picUrl}" class="img_pic"/>
                    <span id="upload_button" class="upload_button">上传</span>
                    <span id="loading" style="display:none" class="loading"><img
                            src="/static/images/loading.gif"/></span>
                    <input id="input_menu_pic" type="hidden" name="picurl" value="${channelTopMenu.picUrl}">
                </td>
                <td height="1" class=>
                </td>
            </tr>
            <tr>
                <td height="1" class="default_line_td" width="100">
                    描述:
                </td>
                <td height="1">
                    <input type="text" name="desc" value="${channelTopMenu.desc}" id="input_text_desc" size="48"/>
                </td>
                <td height="1" class=>
                </td>
            </tr>
            <tr>
                <td height="1" class="default_line_td" width="100">
                    平台:
                </td>
                <td height="1">
                    <select name="platform" id="select_platform">
                        <option value="">请选择</option>
                        <option value="1"
                        <c:if test="${channelTopMenu.platform == 1}">selected="selected"</c:if>>
                        <fmt:message key="joymeapp.favorite.platform.1" bundle="${def}"/></option>
                        <option value="0"
                        <c:if test="${channelTopMenu.platform == 0}">selected="selected"</c:if>>
                        <fmt:message key="joymeapp.favorite.platform.0" bundle="${def}"/></option>
                    </select>
                </td>
                <td height="1" class=>
                </td>
            </tr>
            <tr>
                <td height="1" colspan="3" class="default_line_td"></td>
            </tr>
        </table>
    </form>
</table>
</body>
</html>