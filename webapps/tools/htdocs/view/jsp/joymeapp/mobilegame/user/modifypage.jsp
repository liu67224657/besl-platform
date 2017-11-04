<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>修改用户</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/socialvirtualusermobilegame.js"></script>
    <style type="text/css">
        .td_cent {
            text-align: center;
            vertical-align: middle
        }
    </style>
    <script>
        $().ready(function () {
            var coustomSwfu = new SWFUpload(coustomImageSettings);
            $('#form_submit').bind('submit', function () {
                if ($("#screenname").val() == '') {
                    alert("昵称");
                    $("#screenname").focus();
                    return false;
                }
                if ($("#picurl1").val() == '') {
                    alert("头像");
                    $("#picurl1").focus();
                    return false;
                }
            });

        });


        var coustomImageSettings = {
            upload_url: "${urlUpload}/json/upload/face",
            post_params: {
                "at": "${at}",
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
    <tr height="40">
        <td height="22" class="page_navigation_td" colspan="2">>> 运营维护 >> 手游排行榜 >> 手游排行榜用户列表</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">修改用户</td>
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
            <form action="/joymeapp/mobilegame/virtual/modify/${accountVirtualType}" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <input type="hidden" name="virtualId"  value="${accountVirtual.virtual_id}"/>
                    <input  type="hidden" name="pager.offset"  value="${pageStartIndex}"/>

                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                           昵称:
                        </td>
                        <td height="1">
                            <input id="screenname" type="text" name="screenname" size="48"
                                   value="${accountVirtual.screenname}"/><span style="color:red;">*必填项</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            头像:
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${fn:length(accountVirtual.headicon.pic) > 0}"><img id="picurl1_src" src="${accountVirtual.headicon.pic}"
                                                                              class="img_pic" width="200px"
                                                                              height="200px"/></c:when>
                                <c:otherwise><img id="picurl1_src" src="/static/images/default.jpg"
                                                  class="img_pic" width="200px" height="200px"/></c:otherwise>
                            </c:choose>
                            <span id="upload_button" class="upload_button">上传</span>
                            <span id="loading" style="display:none" class="loading"><img
                                    src="/static/images/loading.gif"/></span>
                            <input id="picurl1" type="hidden" name="picurl1" value="${accountVirtual.headicon.pic}"><span
                                style="color:red;">*必填项</span>

                            <input id="oldpicurl1" type="hidden" name="oldpicurl1" value="${accountVirtual.headicon.pic}">
                        </td>

                        <td height="1">
                        </td>
                    </tr>

                    <tr>
                        <td height="24" class="default_line_td td_cent" width="100">
                            创建人
                        </td>
                        <td height="1">
                            ${accountVirtual.create_user}
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="24" class="default_line_td td_cent" width="100">
                            创建时间
                        </td>
                        <td height="1">
                            ${accountVirtual.create_time}
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="24" class="default_line_td td_cent" width="100">
                            是否删除
                        </td>
                        <td height="1">
                            <fmt:message key="joymeapp.version.status.${accountVirtual.remove_status.code}" bundle="${def}"/>
                        </td>
                        <td height="1" class=>
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