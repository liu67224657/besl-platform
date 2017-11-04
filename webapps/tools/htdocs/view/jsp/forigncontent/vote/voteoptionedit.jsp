<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>为投票添加新的选项</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/joymeappmenuhandler.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript">
        $(document).ready(function (e) {
            $("#submit").click(function () {

                var title = $.trim($("#title").val());
                var displayOrder = $.trim($("#displayOrder").val());

                if (title == '') {
                    alert("选项标题不能为空或者都是空格");
                    return false;
                } else if (displayOrder == '' || displayOrder == 0 || !$.isNumeric(displayOrder)) {
                    alert("请正确填写排序值!");
                    return false;
                } else {
                    $("#form_submit").submit();
                }

            });


            var status = '${item.removeStatus}';
            if (status != '') {
                $("#removeStatus").val(status);

            }
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
    <tr height="40">
        <td height="22" class="page_navigation_td" colspan="2">>> 运营维护 >> 内容专题维护 >> wiki 投票管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">添加新的投票选项</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="14" class="error_msg_td">${errorMsg}</td>
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
            <form action="/comment/vote/wiki/option/modify" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <input type="hidden" name="commentId" value="${commentId}"/>
                        <input type="hidden" name="voteOptionId" value="${voteOptionId}"/>
                        <td height="1" class="default_line_td">
                            选项标题:
                        </td>
                        <td height="1">
                            <input type="text" name="title" size="50" id="title" maxlength="50"
                                   value="<c:out value='${item.title}' escapeXml='true' />"/>*必填项(最长50个字符)
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="150">
                            图片:
                        </td>
                        <td height="1">
                            <img id="menu_pic" <c:choose><c:when test="${not empty item.pic}"> src="${item.pic}" </c:when><c:otherwise> src="/static/images/default.jpg" </c:otherwise> </c:choose> />
                            <span id="upload_button" class="upload_button">上传</span>
                                                                        <span id="loading" style="display:none"
                                                                              class="loading"><img
                                                                                src="/static/images/loading.gif"/></span>
                            <input id="input_menu_pic" type="hidden" name="pic" value="${item.pic}"><span style="color: red">*必填项，图片尺寸：70px * 70px</span>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            排序值:
                        </td>
                        <td height="1">
                            <input type="text" name="displayOrder" size="32" id="displayOrder"
                                   value="<c:out value='${item.displayOrder}' escapeXml='true' />"/>*必填项(请填写一个正整数,将按照由小到大排序)
                            (请填写一个int型的数字,按由小到大排序)
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            状态:
                        </td>
                        <td height="1">
                            <select name="removeStatus" id="removeStatus">
                                <option value="valid">已审核</option>
                                <option value="invalid">未审核</option>
                                <option value="removed">已删除</option>
                            </select>*必填项
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>

                    <tr>
                        <td colspan="3" height="1" class="default_line_td"></td>
                    </tr>
                    <tr align="center">
                        <td colspan="3">
                            <input type="submit" id="submit" class="default_button" value="提交" />
                            <input type="button" class="default_button" value="返回"
                                   onclick="window.location.href ='/comment/vote/wiki/modifypage?commentId=${commentId}';" />
                        </td>
                    </tr>
                </table>
            </form>
        </td>
    </tr>
</table>
</body>
</html>