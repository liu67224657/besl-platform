<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head><title>Simple jsp page</title>
    <script charset="utf-8" src="/static/include/kindeditor/kindeditor-min.js"></script>
    <script charset="utf-8" src="/static/include/kindeditor/lang/zh_CN.js"></script>
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
            var coustomSwfu = new SWFUpload(coustomImageSettings);
            var coustomSwfu2 = new SWFUpload(coustomImageSettings2);
            $('#form_submit').bind('submit', function () {
                var name = $('#input_text_name').val();
                if (name.length == 0) {
                    alert("请填写名称");
                    return false;
                }
                var code = $('#input_text_code').val();
                if (code.length == 0) {
                    alert("请填写code");
                    return false;
                }
                var type = $('#select_type').val();
                if (type.length == 0) {
                    alert("请选择类型");
                    return false;
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
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 分类管理 >> 攻略端ClientLine专题管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td"><span
                            style="font-size:16px; font-family: Arial, Helvetica, sans-serif; font-weight:bold;">修改一条Line</span>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/clientline/android/guidefocus/modify" method="post" id="form_submit">
                <table width="90%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" class="">
                            <input type="hidden" name="lineid" value="${clientLine.lineId}"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            名称
                        </td>
                        <td height="1" class="" width="10">
                            <input type="text" name="linename" size="20" id="input_text_name"
                                   value="${clientLine.lineName}"/> *必填项
                        </td>
                        <td height="1" align="left"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            编码:
                        </td>
                        <td height="1" class="">
                            <input id="input_text_code" type="text" size="20" name="linecode"
                                   value="${clientLine.code}">*必填项
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td>
                            <c:if test="${fn:length(codeExist)>0}">
                                <span style="color: red" id="span_name_exist"><fmt:message key="${codeExist}"
                                                                                           bundle="${def}"/></span>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            类型:
                        </td>
                        <td height="1">
                            <select name="itemtype" id="select_type">
                                <option value="">请选择</option>
                                <c:forEach items="${itemTypeCollection}" var="type">
                                    <option value="${type.code}"
                                    <c:if test="${clientLine.itemType.code == type.code}">selected="selected"</c:if>>
                                    <fmt:message key="client.item.type.${type.code}" bundle="${def}"/></option>
                                </c:forEach>
                            </select>*必选项
                        </td>
                        <td height="1"></td>
                    </tr>


                    <tr>
                        <td height="1" class="default_line_td">
                            角度:
                        </td>
                        <td height="1">
                            <select name="angular" id="select_angle">
                                <option value="">请选择</option>
                                <c:forEach items="${itemAngleCollection}" var="angle">
                                    <option value="${angle.code}"
                                    <c:if test="${clientLine.lineAngle.code == angle.code}">selected="selected"</c:if>>
                                    <fmt:message key="client.item.angle.${angle.code}" bundle="${def}"/></option>
                                </c:forEach>
                            </select>*必选项
                        </td>
                        <td height="1"></td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td">
                            小图:
                        </td>
                        <td height="1" class="">
                            <img id="menu_pic" src="${clientLine.smallpic}"/>
                            <span id="upload_button">上传</span>
                            <span id="loading" style="display:none"><img src="/static/images/loading.gif"/></span>
                            <input id="input_menu_pic" type="hidden" name="smallpic" value="">
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            大图:
                        </td>
                        <td height="1" class="">
                            <img id="menu_pic2" src="${clientLine.bigpic}"/>
                            <span id="upload_button2">上传</span>
                            <span id="loading2" style="display:none"><img src="/static/images/loading.gif"/></span>
                            <input id="input_menu_pic2" type="hidden" name="bigpic" value="">
                        </td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td">
                            描述:
                        </td>
                        <td height="1" class="">
                            <textarea name="line_desc" rows="8" cols="50">${clientLine.line_desc}</textarea>
                        </td>
                        <td height="1"></td>
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