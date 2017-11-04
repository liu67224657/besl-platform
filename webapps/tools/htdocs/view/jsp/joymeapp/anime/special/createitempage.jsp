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


            });
            var coustomSwfu = new SWFUpload(coustomImageSettings);
        });
        var coustomImageSettings = {
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

    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 大动漫管理 >> 大动漫专题维护</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">添加新专题</td>
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
            <form action="/anime/special/item/create" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td">

                            <input type="hidden" name="specialid" value="${specialId}"/>
                            <input type="hidden" name="attr" value="${attr}"/>
                        </td>
                    </tr>
                    <c:if test="${attr==1}">
                        <tr>
                            <td height="1" class="default_line_td" width="100">
                                视频ID:
                            </td>
                            <td height="1">
                                <input type="text" name="tvid" size="48" value="${tvid}"/>*必填项
                            </td>
                            <td height="1" class=>
                            </td>
                        </tr>
                    </c:if>
                    <c:if test="${attr==2}">
                        <tr>
                            <td height="1" class="default_line_td" width="100">
                                链接标题:
                            </td>
                            <td height="1">
                                <input id="input_text_menuname" type="text" name="title" size="48" value=""/>*必填项(如果是wiki有评论，必须严格依照wiki的title填写,否则会取不到评论)
                            </td>
                            <td height="1" class=>
                            </td>
                        </tr>
                        <tr>
                            <td height="1" class="default_line_td" width="100">
                                链接地址:
                            </td>
                            <td height="1">
                                <input type="text" name="linkurl" size="48" value=""/>*必填项
                            </td>
                            <td height="1" class=>
                            </td>
                        </tr>
                        <tr>
                            <td height="1" class="default_line_td" width="100">
                                描述：
                            </td>
                            <td height="1">
                                <textarea rows="12" cols="52" name="desc"></textarea>
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
                                <input id="input_menu_pic" type="hidden" name="pic" value=""><span style="color: red">必填项，图片尺寸：212px * 148px</span>
                            </td>
                            <td height="1" class=>
                            </td>
                        </tr>

                    </c:if>
                    <c:if test="${attr==3}">
                        <tr>
                            <td height="1" class="default_line_td" width="100">
                                链接地址:
                            </td>
                            <td height="1">
                                <input type="text" name="linkurl" size="48" value=""/>*必填项
                            </td>
                            <td height="1" class=>
                            </td>
                        </tr>
                    </c:if>
                     <c:if test="${attr==4}">
                        <tr>
                            <td height="1" class="default_line_td" width="100">
                                链接标题:
                            </td>
                            <td height="1">
                                <input id="input_text_menuname" type="text" name="title" size="48" value=""/>*必填项
                            </td>
                            <td height="1" class=>
                            </td>
                        </tr>
                        <tr>
                            <td height="1" class="default_line_td" width="100">
                                链接地址:
                            </td>
                            <td height="1">
                                <input type="text" name="linkurl" size="48" value=""/>*必填项
                            </td>
                            <td height="1" class=>
                            </td>
                        </tr>
                        <tr>
                            <td height="1" class="default_line_td" width="100">
                                描述：
                            </td>
                            <td height="1">
                                <textarea rows="12" cols="52" name="desc"></textarea>
                            </td>
                            <td height="1" class=>
                            </td>
                        </tr>
                    </c:if>

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