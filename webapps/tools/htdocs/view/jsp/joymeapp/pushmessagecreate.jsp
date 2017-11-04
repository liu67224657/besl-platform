<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台消息推送列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/joymeapphandler.js"></script>
    <script language="JavaScript" type="text/JavaScript">

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
        $(document).ready(function () {
            $('#form_submit').bind('submit', function () {
                var msgSubjectVal = $('#msg_subject').val();
                if (msgSubjectVal.length == 0) {
                    alert('请填写消息标题');
                    return false;
                }
                var msgMsgVal = $('#msg_message').val();
                if (msgMsgVal.length == 0) {
                    alert('请填写消息简述');
                    return false;
                }
                var msgUrlVal = $('#url').val();
                if (msgUrlVal.length == 0) {
                    alert('请填写消息URL');
                    return false;
                }
                var type = $("#redirecttype").val();
                if (type == '') {
                    alert("请选择跳转类型");
                    return false;
                }
                var url = $('#url').val();
                var size = url.indexOf("http://marticle.joyme.com/marticle/") + url.indexOf("http://t.cn");
                if (type == '1') {
                    if (size == -2) {
                        alert("请填写正确的CMS文章单页地址：" + "\n" + "http://marticle.joyme.com/marticle/ or http://t.cn");
                        return false;
                    }
                }
                if (type == '4') {
                    if (url.indexOf("http://www.joyme.com/appwiki/") < 0) {
                        alert("请填写正确的WIKI页地址：" + "\n" + "http://www.joyme.com/appwiki/" + "\n" + "如：http://www.joyme.com/appwiki/wzzj/index.shtml 或 http://www.joyme.com/appwiki/wzzj/52739.shtml");
                        return false;
                    }
                }
                if (type == '5') {
                    if (url.indexOf("http://marticle.joyme.com/marticle/tags/") < 0);
                    alert("请填写正确的标签地址：" + "\n" + "http://marticle.joyme.com/marticle/tags/*_*.html" + "\n" + "第一个*标签id，第二个*分页");
                    return false;
                }
            });
        });

    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 条目管理 >> 创建消息推送</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="30%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">创建消息推送</td>
                    <td class="">
                        <form></form>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/joymeapp/pushmessage/create" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            消息ICON
                        </td>
                        <td height="1" class="edit_table_defaulttitle_td">
                            <img id="img_icon" src="/static/images/default.jpg"/>
                            <span id="upload_button">上传</span>
                            <span id="loading" style="display:none"><img src="/static/images/loading.gif"/></span>
                            <input id="msg_icon" type="hidden" name="icon">
                        </td>
                        <td height="1" class="edit_table_defaulttitle_td">
                            <c:if test="${fn:length(iconError)>0}">
                                <fmt:message key="${iconError}" bundle="${error}"/>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            消息标题
                        </td>
                        <td height="1" class="edit_table_defaulttitle_td">
                            <input id="msg_subject" type="text" name="subject">
                        </td>
                        <td height="1" class="edit_table_defaulttitle_td">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            消息简述
                        </td>
                        <td height="1" class="edit_table_defaulttitle_td">
                            <input id="msg_message" type="text" name="shortmessage">*不能超过20个字，负责苹果服务器会拒绝推送
                        </td>
                        <td height="1" class="edit_table_defaulttitle_td">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            跳转链接
                        </td>
                        <td height="1" class="edit_table_defaulttitle_td">
                            <input id="url" type="text" name="url" size="32"/>
                        </td>
                        <td height="1" class="edit_table_defaulttitle_td">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            选择APP
                        </td>
                        <td height="1" class="edit_table_defaulttitle_td">
                            <select name="appkey">
                                <c:forEach var="app" items="${appList}">
                                    <option value="${app.appId}">${app.appName}</option>
                                </c:forEach>
                            </select>
                        </td>
                        <td height="1" class="edit_table_defaulttitle_td">
                            <c:if test="${fn:length(platformError)>0}">
                                <fmt:message key="${platformError}" bundle="${error}"/>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            选择推送的平台：
                        </td>
                        <td height="1" class="edit_table_defaulttitle_td">
                            <select name="platform">
                                <c:forEach var="platform" items="${platformList}">
                                    <option value="${platform.code}"><fmt:message
                                            key="joymeapp.platform.${platform.code}" bundle="${def}"/></option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            选择跳转类型：
                        </td>
                        <td height="1" class="edit_table_defaulttitle_td">
                            <select name="type" id="redirecttype">
                                <option value="">请选择</option>
                                <c:forEach var="type" items="${typeCollection}">
                                    <option value="${type.code}"><fmt:message key="client.item.redirect.${type.code}"
                                                                              bundle="${def}"/></option>
                                </c:forEach>
                            </select>
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