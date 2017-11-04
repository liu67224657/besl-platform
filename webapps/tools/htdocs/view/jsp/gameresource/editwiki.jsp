<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <%
        //remove cache
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
    %>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/wikihandler.js"></script>
    <script language="JavaScript" type="text/JavaScript">

        $().ready(function () {
            var swfu = new SWFUpload(mainImageSettings);
            var coustomSwfu = new SWFUpload(coustomImageSettings);
        });

        var mainImageSettings = {
            upload_url: "${urlUpload}/json/upload/resource/logo",
            post_params: {
                "at": "${at}",
                "resourceDomain": "game",
                "scale": '3:4'
            },

            // File Upload Settings
            file_size_limit: "8 MB",    // 2MB
            file_types: "*.jpg;*.png;*.gif",
            file_types_description: "请选择图片",
            file_queue_limit: 1,

            file_dialog_complete_handler: fileDialogComplete,
            upload_start_handler: uploadStart,
            upload_progress_handler: uploadProgress,
            upload_success_handler: uploadSuccess,
            upload_complete_handler: uploadComplete,

            // Button Settings
            button_image_url: "/static/images/uploadbutton.png",
            button_placeholder_id: "uploadButton",
            button_width: 61,
            button_height: 22,
            moving_average_history_size: 40,

            // Flash Settings
            flash_url: "/static/include/swfupload/swfupload.swf",
            flash9_url: "/static/include/swfupload/swfupload_fp9.swf",

            custom_settings: {
            },
            // Debug Settings
            debug: false}
        var coustomImageSettings = {
            upload_url: "${urlUpload}/json/upload/qiniu",
            post_params: {
                "at": "joymeplatform",
                "filetype": "original"
            },

            // File Upload Settings
            file_size_limit: "8 MB",    // 2MB
            file_types: "*.jpg;*.png;*.gif",
            file_types_description: "请选择图片",
            file_queue_limit: 1,

            file_dialog_complete_handler: fileDialogComplete,
            upload_start_handler: uploadThumbimgStart,
            upload_progress_handler: uploadProgress,
            upload_success_handler: uploadThumimgSuccess,
            upload_complete_handler: uploadThumbimgComplete,

            // Button Settings
            button_image_url: "/static/images/uploadbutton.png",
            button_placeholder_id: "thumbimg_button",
            button_width: 61,
            button_height: 22,
            moving_average_history_size: 40,

            // Flash Settings
            flash_url: "/static/include/swfupload/swfupload.swf",
            flash9_url: "/static/include/swfupload/swfupload_fp9.swf",

            custom_settings: {},
            // Debug Settings
            debug: false}

        function checkForm() {
            var resourceName = document.form1.wikiName.value;
            var resourceDesc = document.form1.wikiDesc.value;
            var wikiUrl = document.form1.wikiUrl.value;
            var wikiCode = document.form1.wikiCode.value;
            var icon = document.form1.icon.value;

            if (resourceName.length == 0 || resourceName == '') {
                alert('WIKI名称不能为空');
                return false;
            }
            if (icon.length == 0 || icon == '') {
                alert('游戏主图不能为空');
                return false;
            }
            if (wikiUrl.length == 0 || wikiUrl == '') {
                alert('WIKI的地址不能空');
                return false;
            }
            if (wikiCode.length == 0 || wikiCode == '') {
                alert('游戏编码不能为空');
                return false;
            }

            document.form1.submit();
            return true;
        }
        function checkName() {
            var wikiName = $("#wikiName").val();

            if (wikiName == '') {
                alert("请输入WIKI条目主名称！");
                return false;
            }
            $.ajax({
                type: "POST",
                url: "/wiki/checkname",
                data: {wikiName: wikiName},
                success: function (data) {
                    var resultJson = eval('(' + data + ')');
                    if (resultJson.status_code == '1') {
                        alert("该WIKI名称已经存在，请重新输入！");
                    } else {
                        alert("该WIKI名称不存在,可以使用！");
                    }
                }
            });

        }
    </script>
    <title>后台数据管理、添加新管理员</title>

    <script language="JavaScript" type="text/JavaScript">
        function back() {
            window.location.href = "/wiki/list";
        }
    </script>
</head>

<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> <a href="/wiki/=list">WIKI条目维护</a> >> 编辑WIKI条目
        </td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">编辑WIKI条目</td>
                    <td align="left">
                    </td>
                </tr>
            </table>
            <c:if test="${errorMsgMap['system']!=null}">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td class="error_msg_td">
                            <fmt:message key="${errorMsgMap['system']}" bundle="${error}"/>
                        </td>
                    </tr>
                </table>
            </c:if>
            <c:if test="${errorMsgMap['wikicode']!=null}">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td class="error_msg_td">
                            <fmt:message key="${errorMsgMap['wikicode']}" bundle="${error}"/>
                        </td>
                    </tr>
                </table>
            </c:if>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <form name="form1" action="/wiki/edit" method="POST" onsubmit="return checkForm();">
                    <input type="hidden" value="${resourceId}" name="resourceId"/>
                    <tr>
                        <td height="1" colspan="4" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">WIKI条目主名称：</td>
                        <td class="edit_table_value_td">
                            <c:if test="${errorMsgMap['wikiName']!=null}">
                                <span class="error_msg_td"><fmt:message key="${errorMsgMap['wikiName']}"
                                                                        bundle="${error}"/></span><br>
                            </c:if>
                            <input name="wikiName" type="text" class="default_input_singleline" id="wikiName"
                                   value="${wikiName}" size="24" maxlength="32">
                            <input type="button" onclick="checkName()" value="检查此名称是否存在"/>
                            <font color="red">*</font>
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">WIKI编码：</td>
                        <td class="edit_table_value_td">
                            <input name="wikiCode" type="text" class="default_input_singleline" id="wikiCode"
                                   value="${wikiCode}" size="32" maxlength="32">
                            <font color="red">*此处为必填项，小组编码不能重复</font>
                        </td>
                        <td nowrap class="edit_table_value_td">
                            <c:if test="${errorMsgMap['wikicode']!=null}">
                                <fmt:message key="${errorMsgMap['wikicode']}" bundle="${error}"/>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">WIKI地址：</td>
                        <td class="edit_table_value_td">
                            <input name="wikiUrl" type="text" class="default_input_singleline" id="wikiUrl"
                                   value="${wikiUrl}" size="100" maxlength="100">
                            <font color="red">*此处为必填项WIKI对应的地址</font>
                        </td>
                        <td nowrap class="edit_table_value_td">
                            <c:if test="${errorMsgMap['wikiurl']!=null}">
                                <fmt:message key="${errorMsgMap['wikiurl']}" bundle="${error}"/>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">WIKI主图：</td>
                        <td nowrap class="edit_table_value_td">
                            <img id="img_game_logo" src="${uf:parseBFace(icon)}"/>
                            <span id="uploadButton">上传</span>
                            <span id="loading" style="display:none"><img src="/static/images/loading.gif"/></span>
                            <font color="red">*</font>
                            <input name="icon" type="hidden" value="${icon}" id="icon">
                        </td>
                        <td nowrap class="edit_table_value_td"></td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">WIKI附图：</td>
                        <td nowrap class="edit_table_value_td">
                            <c:choose>
                                <c:when test="${fn:length(thumbIcon) == 0}">
                                    <img id="img_thumb_logo" src="/static/images/default.jpg"/>
                                    <input name="thumbimg" type="hidden" value="" id="thumbimg_url">
                                </c:when>
                                <c:otherwise>
                                    <img id="img_thumb_logo" src="${uf:parseBFace(thumbIcon)}"/>
                                    <input name="thumbimg" type="hidden" value="${thumbIcon}" id="thumbimg_url">
                                </c:otherwise>
                            </c:choose>
                            <span id="thumbimg_button">上传</span>
                            <span id="loading_thumimg" style="display:none"><img
                                    src="/static/images/loading.gif"/></span>
                            <font color="red">*</font>
                        </td>
                        <td nowrap class="edit_table_value_td"></td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">WIKI简介：</td>
                        <td nowrap class="edit_table_value_td">
                            <textarea rows="5" cols="100" name="wikiDesc"
                                      class="default_input_singleline">${wikiDesc}</textarea>
                        </td>
                    </tr>

                    <tr>
                        <td height="1" colspan="4" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td colspan="4">&nbsp;</td>
                    </tr>
                    <tr align="center">
                        <td colspan="4">
                            <input name="Submit" type="button" class="default_button" value="提交"
                                   onclick="javascript:return checkForm();">
                            <input name="Reset" type="reset" class="default_button" value="返回" onclick="back();">
                        </td>
                    </tr>
                </form>
            </table>
        </td>
    </tr>
</table>
</body>
</html>