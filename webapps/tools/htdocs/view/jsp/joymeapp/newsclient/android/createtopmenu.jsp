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
        $().ready(function () {
            $('#form_submit').bind('submit', function () {
                var menuName = $('#input_text_menuname').val();
                if (menuName.length == 0) {
                    alert("请填写菜单名称");
                    return false;
                }
                var link = $('#input_text_link').val();
                if (link.length == 0) {
                    alert("请填写菜单URL");
                    return false;
                }
                if (link.indexOf("http://") < 0 && link.indexOf("https://") < 0) {
                    alert("URL请以http://或者https://开头");
                    return false;
                }

                var menuType = $('#select_menutype').val();
                if (menuType == '-1') {
                    alert("请选择跳转类别");
                    return false;
                }

                var picUrl = $('#input_menu_pic').val();
                if (picUrl.length == 0) {
                    alert("请上传图片");
                    return false;
                }
                var desc = $('#input_text_desc').val();
                if (desc.length == 0) {
                    alert("请填写描述");
                    return false;
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
            debug: false}
    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷APP >> 着迷新闻端Android首页轮播图</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">添加活动轮播图</td>
                </tr>
            </table>
            <form action="/joymeapp/newsclient/topmenu/android/create" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            菜单名称:
                        </td>
                        <td height="1">
                            <input id="input_text_menuname" type="text" name="menuname" size="48" value=""/>*必填项
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            菜单URL:
                        </td>
                        <td height="1">
                            <input id="input_text_link" type="text" name="linkurl" size="48" value=""/>*必填项
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            跳转类别:
                        </td>
                        <td height="1">
                            <select name="menutype" id="select_menutype">
                                <option value="-1">请选择</option>
                                <c:forEach items="${menuTypes}" var="type">
                                    <option value="${type.code}"><fmt:message key="client.item.redirect.${type.code}"
                                                                              bundle="${def}"/></option>
                                </c:forEach>
                            </select>*必选项
                        </td>
                        <td height="1"></td>
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
                            <input id="input_menu_pic" type="hidden" name="picurl" value="">*必填项
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            描述:
                        </td>
                        <td height="1">
                            <input id="input_text_desc" type="text" name="menudesc" size="48" value=""/>*必填项
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            渠道:
                        </td>
                        <td height="1">
                            <select name="channelid" id="select_channel">
                                <option value="">请选择</option>
                                <c:forEach items="${channelList}" var="channel">
                                    <option value="${channel.channelId}">${channel.channelName}</option>
                                </c:forEach>
                            </select>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            是否是new:
                        </td>
                        <td height="1">
                            <select name="isnew" id="select_new">
                                <option value="false">否</option>
                                <option value="true">是</option>
                            </select>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            是否是hot:
                        </td>
                        <td height="1">
                            <select name="ishot" id="select_hot">
                                <option value="false">否</option>
                                <option value="true">是</option>
                            </select>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
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