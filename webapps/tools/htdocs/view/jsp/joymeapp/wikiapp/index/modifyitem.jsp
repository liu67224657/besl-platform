<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>WIKI大端首页模块管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/jquery-1.11.2.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/joymeapphandler.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            var coustomSwfu = new SWFUpload(coustomImageSettings);

            $('#input_delete_pic').click(function () {
                $('#img_icon').attr('src', '/static/images/default.jpg');
                $('#msg_icon').val('');
            });

            $('#form_submit').bind('submit', function () {

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
            debug: false
        }

    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">添加二级元素</td>
                </tr>
            </table>
            <form action="/joymeapp/wikiapp/index/item/modify" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td">
                            <input type="hidden" name="lineid" value="${lineId}"/>
                            <input type="hidden" name="linecode" value="${lineCode}"/>
                            <input type="hidden" name="itemtype" value="${itemType}"/>
                            <input type="hidden" name="itemid" value="${item == null ? itemId : item.itemId}"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            来源:
                        </td>
                        <td height="1">
                            <select name="itemdomain" id="select_doamin">
                                <option value="">请选择</option>
                                <c:forEach items="${itemDomainSet}" var="domain">
                                    <option value="${domain.code}"
                                            <c:if test="${domain.code == (item == null ? itemDomain : item.itemDomain.code)}">selected="selected"</c:if>>
                                        <fmt:message key="client.item.domain.${domain.code}" bundle="${def}"/>
                                    </option>
                                </c:forEach>
                            </select>*必选项，优先选择此项
                        </td>
                    </tr>
                    <tr id="tr_directid">
                        <td height="1" class="default_line_td" width="100">
                            关联ID:
                        </td>
                        <td height="1">
                            <input type="text" name="directid" value="${directId}" id="input_directid"/>*必填项，wiki资料库wikikey
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr id="tr_title">
                        <td height="1" class="default_line_td" width="100">
                            标题:
                        </td>
                        <td height="1">
                            <input type="text" name="title" value="${item == null ? title : item.title}" id="input_title"/>*必填项
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr id="tr_desc">
                        <td height="1" class="default_line_td" width="100">
                            描述:
                        </td>
                        <td height="1">
                            <textarea id="textarea_desc" type="text" name="desc"
                                      style="height: 100px;width: 600px">${item == null ? desc : item.desc}</textarea>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            图片:
                        </td>
                        <td height="1">
                            <img id="img_icon" src="${item == null ? picUrl : item.picUrl}"/>
                            <span id="upload_button">上传</span>
                            <span id="loading" style="display:none"><img src="/static/images/loading.gif"/></span>
                            <input id="input_delete_pic" type="button" value="重置"/>
                            <input id="msg_icon" type="hidden" name="picurl" value="${item == null ? picUrl : item.picUrl}">*选填
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            跳转类型:
                        </td>
                        <td height="1">
                            <select id="select_redirect" name="redirecttype">
                                <option value="">请选择</option>
                                <c:forEach items="${redirectTypeSet}" var="redirect">
                                    <option value="${redirect.code}"
                                            <c:if test="${redirect.code == (item == null ? redirectType : item.redirectType.code)}">selected="selected"</c:if>>
                                        <fmt:message key="client.item.redirect.${redirect.code}" bundle="${def}"/>
                                    </option>
                                </c:forEach>
                            </select>*必选项
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            跳转参数:
                        </td>
                        <td height="1">
                            <input type="text" name="url" value="${item == null ? url : item.url}" style="width: 500px"/>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>
                </table>
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr align="center">
                        <td colspan="3">
                            <input name="Submit" type="submit" class="default_button" value="提交">
                            <input name="Reset" type="button" class="default_button" value="返回"
                                   onclick="javascript:window.history.go(-1);">
                        </td>
                    </tr>
                </table>
            </form>
        </td>
    </tr>
</table>
</body>
</html>