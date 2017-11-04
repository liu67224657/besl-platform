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
        });

        function changepic() {
            var id = $("#displaytype").val()
            if (id != 0) {
                $("#templet").attr("src", "/static/images/templet" + id + ".jpg");
            }
            if (id == 2) {
                $("#bgcolor").val("#FFFFFF");
            } else {
                $("#bgcolor").val("");
            }
        }
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

        function checkClientLine() {

            var id = $('#directid').val();
            if (id.length <= 0) {
                alert("请填写游戏ID");
                return false;
            }
            var weight = $('#input_shakeweight').val();
            if (weight.length <= 0 || parseInt(weight) < 0 || parseInt(weight) > 1000) {
                alert("权重在0~1000");
                return false;
            }

        }
        function deletepic() {
            if (window.confirm("是否删除该图片")) {
                $("#menu_pic").attr("src", "/static/images/default.jpg");
                $("#input_menu_pic").val("");
            }
        }
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">运营维护 >> 着迷手游画报管理 >> <a href="/joymeapp/newsclient/shake/list">手游画报摇一摇管理</a>
        </td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td"><span
                            style="font-size:16px; font-family: Arial, Helvetica, sans-serif; font-weight:bold;">修改</span>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="13" class="error_msg_td">
                            <fmt:message key="${errorMsg}" bundle="${def}"/>
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/shake/line/item/modify" method="post" id="form_submit" onsubmit="return checkClientLine()">
                <table width="90%" border="0" cellspacing="1" cellpadding="0">

                    <tr>
                        <td height="1" class="">

                            <input type="hidden" name="lineid" value="${item.lineId}"/>
                            <input type="hidden" name="itemid" value="${item.itemId}"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            游戏名称：
                        </td>
                        <td height="1" class="" width="10">
                            <input type="text" name="itemname" value="${item.title}" size="32"
                                   id="input_text_name"/>
                        </td>
                        <td height="1" align="left"></td>
                    </tr>
                    <tr>
                        <td>
                            <c:if test="${fn:length(nameExist)>0}">
                                <span style="color: red" id="span_name_exist"><fmt:message key="${nameExist}"
                                                                                           bundle="${def}"/></span>
                            </c:if>
                        </td>
                    </tr>
                    <%--<tr>--%>
                    <%--<td height="1" class="default_line_td">--%>
                    <%--图片:--%>
                    <%--</td>--%>
                    <%--<td height="1">--%>
                    <%--<img id="menu_pic" src="${clienLineItem.picUrl}" onclick="deletepic(this)"/>--%>
                    <%--<span id="upload_button">上传</span>--%>
                    <%--<span id="loading" style="display:none"><img src="/static/images/loading.gif"/></span>--%>
                    <%--<input id="input_menu_pic" type="hidden" name="picurl" value="${clienLineItem.picUrl}">--%>
                    <%--</td>--%>
                    <%--<td height="1"></td>--%>
                    <%--</tr>--%>
                    <%--<tr>--%>
                    <%--<td height="1" class="default_line_td">--%>
                    <%--排序字段:--%>
                    <%--</td>--%>
                    <%--<td height="1">--%>
                    <%--<input type="text" value="${clienLineItem.displayOrder}" name="order"/> <span style="color: red;">数字越小排名越高 因为网速原因，如遇到相同的请手动减少1</span>--%>
                    <%--</td>--%>
                    <%--<td height="1"></td>--%>
                    <%--</tr>--%>
                    <tr>
                        <td height="1" class="default_line_td">
                            游戏资料库ID：
                        </td>
                        <td height="1" class="">
                            <input id="directid" type="text" size="20" name="directid"
                                   value="${item.directId}">
                            <c:if test="${not empty errorMsgMap.paramError}">
                                    <span style="color:red"><fmt:message key="${errorMsgMap.paramError}"
                                                                         bundle="${def}"/></span>
                            </c:if>
                            <c:if test="${not empty errorMsgMap.gameDbError}">
                                    <span style="color:red"><fmt:message key="${errorMsgMap.gameDbError}"
                                                                         bundle="${def}"/></span>
                            </c:if>
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            权重：
                        </td>
                        <td height="1" class="">
                            <input id="input_shakeweight" type="text" name="shakeweight" value="${item.contentid}">*正整数，若为0不会进入权重库。
                        </td>
                        <td height="1"></td>
                    </tr>
                </table>
                <span style="color:red;font-size:15px">游戏名称可不填</span>
                <%--<span style="color:red;font-size:15px">注意：此处游戏名称和图片是为了给编辑查看用（如果不填会直接用游戏资料库里的填充）、前台页面显示会取游戏资料库里的游戏名称和图片</span>--%>
                <%--<table width="100%" border="0" cellspacing="0" cellpadding="0">--%>
                <%--<tr>--%>
                <%--<td class="list_table_header_td"><span--%>
                <%--style="font-size:16px; font-family: Arial, Helvetica, sans-serif; font-weight:bold;">相关文章（选填）</span>--%>
                <%--</td>--%>
                <%--</tr>--%>
                <%--</table>--%>
                <%--<div width="200" name="downloadinfo" id="downloadInfo1">--%>
                <%--<span>文章标题：</span> <input type="text" name="exttitle" id="exttitle1" size="32"/> <br/>--%>
                <%--<span>文章链接：</span> <input type="text" name="extlink" id="extlink1" size="32"/> <br/>--%>
                <%--<span>跳转类别：</span>--%>
                <%--<select name="extredirecttype" id="extredirecttype1">--%>
                <%--<option value="">请选择</option>--%>
                <%--<c:forEach items="${redirectCollection}" var="type">--%>
                <%--<option value="${type.code}"><fmt:message--%>
                <%--key="client.item.redirect.${type.code}"--%>
                <%--bundle="${def}"/></option>--%>
                <%--</c:forEach>--%>
                <%--</select>--%>
                <%--<br/>--%>
                <%--<br/>--%>
                <%--</div>--%>
                <%--<input type="button" id="createUploadInfo" value="新增一个下载地址"/>--%>
                <%--<input type="button" id="removeUploadInfo" value="删除一个下载地址"/>--%>
                <table width="90%" border="0" cellspacing="1" cellpadding="0">


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
<%--<script type="text/javascript">--%>
<%--var id = 2;--%>
<%--$("#createUploadInfo").click(function() {--%>
<%--var downloadinfo = $("div[name='downloadinfo']").size();--%>
<%--if (downloadinfo > 10) {--%>
<%--alert("最多只能有十个相关文章信息");--%>
<%--} else {--%>
<%--var divTest = $("#downloadInfo1");--%>
<%--var newDiv = divTest.clone(true);--%>
<%--newDiv.find("#downloadInfo1").attr("id", "downloadInfo" + id);--%>
<%--newDiv.find("#exttitle1").attr("id", "exttitle" + id);--%>
<%--newDiv.find("#extlink1").attr("id", "extlink" + id);--%>
<%--newDiv.find("#extredirecttype1").attr("id", "extredirecttype" + id);--%>
<%--$("#createUploadInfo").before(newDiv);--%>
<%--id++;--%>
<%--}--%>
<%--});--%>
<%--$("#removeUploadInfo").click(function() {--%>
<%--var downloadinfo = $("div[name='downloadinfo']").size();--%>
<%--if (downloadinfo < 2) {--%>
<%--alert("至少保留一个相关文章信息");--%>
<%--} else {--%>
<%--$("div[name='downloadinfo']:last").remove();--%>
<%--}--%>
<%--});--%>
<%--</script>--%>
</body>
</html>