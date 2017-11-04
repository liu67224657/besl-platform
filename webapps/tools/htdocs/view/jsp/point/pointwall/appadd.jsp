<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>添加新的app</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/joymeappmenuhandler.js"></script>
    <script type="text/javascript">
        $(document).ready(function (e) {
            $("#submit").click(function () {

                var packageName = $.trim($("#packageName").val());
                var appName = $.trim($("#appName").val());
                var verName = $.trim($("#verName").val());
                //   var platform = $.trim($("#platform").val());
                var appIcon = $.trim($("#input_menu_pic").val());
                var appDesc = $.trim($("#appDesc").val());
                // var sponsorName = $.trim($("#sponsorName").val());
                var downloadUrl = $.trim($("#downloadUrl").val());
                //  var reportUrl = $.trim($("#reportUrl").val());
                var initScore = $.trim($("#initScore").val());

                if (packageName == '') {
                    alert("请填写包名或者bundle id,不能都是空格!");
                    return false;
                } else if (appName == '') {
                    alert("请填写app在Wap页面显示的名字,不能都是空格!");
                    return false;
                }
                /*      else if (verName == '') {
                 alert("请填写app在wap页面显示的版本,不能都是空格!");
                 return false;
                 } */
                else if (appIcon == '') {
                    alert("请填写app的icon地址,不能都是空格!");
                    return false;
                } else if (appDesc == '') {
                    alert("请填写app的描述,不能都是空格!");
                    return false;
                } else if (downloadUrl == '') {
                    alert("请填写下载的URL,不能都是空格!");
                    return false;
                } else if (initScore == '' || initScore == 0 || isNaN(initScore)) {
                    alert("请正确填写app下载后默认可以获得的积分值,不能填0!");
                    return false;
                } else {
                    document.forms[0].submit();
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
        <td height="22" class="page_navigation_td">>> 运营维护 >> 我的模块管理 >> 热门应用管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">添加新的app</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(nameExist)>0}">
                    <tr>
                        <td height="1" colspan="14" class="error_msg_td">${nameExist}</td>
                    </tr>
                </c:if>
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/point/pointwall/app/create" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" class="default_line_td">
                            应用的包名或者bundle id:
                        </td>
                        <td height="1">
                            <input type="text" id="packageName" name="packageName" size="32" value="${packageName}"/>
                            *必填项(此名字在热门应用中不能重复）
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            在wap页显示的名字:
                        </td>
                        <td height="1">
                            <input type="text" name="appName" size="32" id="appName" maxlength="10"
                                   value="<c:out value='${appName}' escapeXml='true' />"/>*必填项(最多10个字符）
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            版本:
                        </td>
                        <td height="1">
                            <input type="text" name="verName" size="32" id="verName"
                                   value="<c:out value='${verName}' escapeXml='true' />"/>*可不填(暂时未用到)
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            所在平台
                        </td>
                        <td height="1">
                            <select name="platform">
                                <option value="0">ios</option>
                                <option value="1">android</option>
                            </select>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            icon:
                        </td>
                        <td height="1">
                            <img id="menu_pic" src="/static/images/default.jpg" class="img_pic"/>
                            <span id="upload_button" class="upload_button">上传</span>
                                                     <span id="loading" style="display:none" class="loading"><img
                                                             src="/static/images/loading.gif"/></span>
                            <input id="input_menu_pic" type="hidden" name="appIcon"
                                   value="<c:out value='${appIcon}' escapeXml='true' />"><span style="color: red">*必填项，图片尺寸：70px * 70px</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            描述
                        </td>
                        <td height="1">
                            <input type="text" name="appDesc" size="32" id="appDesc" maxlength="13"
                                   value="<c:out value='${appDesc}' escapeXml='true' />"/>*必填项(最多13个字符）
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            所属广告主:
                        </td>
                        <td height="1">
                            <input type="text" name="sponsorName" size="32" id="sponsorName"
                                   value="<c:out value='${sponsorName}' escapeXml='true' />"/>*可不填(暂时未用到)
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            下载的URL:
                        </td>
                        <td height="1">
                            <input type="text" name="downloadUrl" size="32" id="downloadUrl"
                                   value="<c:out value='${downloadUrl}' escapeXml='true' />"/>*必填项
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            广告主的URL
                        </td>
                        <td height="1">
                            <input type="text" name="reportUrl" size="32" id="reportUrl"
                                   value="<c:out value='${reportUrl}' escapeXml='true' />"/>*可不填(暂时未用到)
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            积分值:
                        </td>
                        <td height="1">
                            <input type="text" name="initScore" size="32" id="initScore"
                                   value="<c:out value='${initScore}' escapeXml='true' />"/>*必填项
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3" height="1" class="default_line_td"></td>
                    </tr>
                    <tr align="center">
                        <td colspan="3">
                            <input name="Submit" type="submit" id="submit" class="default_button" value="提交">
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