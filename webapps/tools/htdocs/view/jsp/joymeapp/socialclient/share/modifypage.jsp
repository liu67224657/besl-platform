<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>修改分享模板</title>
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
            vertical-align: middle
        }
    </style>
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

        function clearPic() {
            $('#picurl1_src').attr('src', "");
            $('#picurl1').val("");
        }
    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 社交端管理 >> 修改分享模板</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">修改分享模板</td>
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
            <form action="/joymeapp/socialclient/share/modify" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <input id="shareid" type="hidden" name="shareid" size="48"
                           value="${socialShare.share_id}"/>
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
                            链接地址
                        </td>
                        <td height="1">
                            <input id="url" type="text" name="url" size="90" value="${socialShare.url}"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="24" class="default_line_td td_cent" width="100">
                            活动ID
                        </td>
                        <td height="1">
                            <input type="hidden" name="activityid" size="90" value="${socialShare.activityid}"/>
                            ${socialShare.activityid}
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
                        <td height="24" class="default_line_td td_cent">
                            选择APP
                        </td>
                        <td height="1" class="">
                            <input type="hidden" name="appkey" size="90" value="${socialShare.appkey}"/>
                            ${appName}
                        </td>
                    </tr>
                    <tr>
                        <td height="24" class="default_line_td td_cent">
                            类型
                        </td>
                        <td height="1" class="">
                            <input type="hidden" name="share_type" size="90" value="${socialShare.share_type.code}"/>
                            <fmt:message key="social.share.type.${socialShare.share_type.code}" bundle="${def}"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="24" class="default_line_td td_cent">
                            第三方平台：
                        </td>
                        <td height="1" class="">
                            <input type="hidden" name="sharedomain" size="90" value="${socialShare.sharedomain.code}"/>
                            <fmt:message key="social.share.third.type.${socialShare.sharedomain.code}" bundle="${def}"/>
                        </td>
                    </tr>

                    <tr>
                        <td height="24" class="default_line_td td_cent">
                            选择发布的平台：
                        </td>
                        <td height="1" class="">
                            <input type="hidden" name="platform" size="90" value="${socialShare.platform}"/>
                            <fmt:message key="joymeapp.platform.${socialShare.platform}" bundle="${def}"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            图片:
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${fn:length(socialShare.pic_url) > 0}"><img id="picurl1_src"
                                                                                          src="${socialShare.pic_url}"
                                                                                          class="img_pic" width="200px"
                                                                                          height="200px"/></c:when>
                                <c:otherwise><img id="picurl1_src" src="/static/images/default.jpg"
                                                  class="img_pic" width="200px" height="200px"/></c:otherwise>
                            </c:choose>
                            <span id="upload_button" class="upload_button">上传</span>
                            <span id="loading" style="display:none" class="loading"><img
                                    src="/static/images/loading.gif"/></span>
                            <input id="picurl1" type="hidden" name="pic_url" value="${socialShare.pic_url}">
                            <a href="javascript:void(0);" onclick="clearPic()">图片置空</a>
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