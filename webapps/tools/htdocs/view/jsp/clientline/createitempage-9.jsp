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
    <script type="text/javascript" src="/static/include/swfupload/clientlineitemhandler.js"></script>
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
            var domain = $('#domain').val();
            if (domain == '') {
                alert("请选择文章来源");
                return false;
            }
            var type = $("#redirecttype").val();
            if (type == '') {
                alert("请选择跳转类型");
                return false;
            }
            var url = $('#linkaddress').val();
            var did = $('#directid').val();
            if (domain == '1') {

            }
            if (type == '5') {
                if (did == "" && url == "") {
                    alert("请填写标签ID或正确的标签地址：" + "\n" + "http://marticle.joyme.com/marticle/tags/*_*.html" + "\n" + "第一个*标签id，第二个*分页");
                    return false;
                }
                if (url.length() >= 1 && url.indexOf("http://marticle.joyme.com/marticle/tags/") < 0);
                alert("请填写正确的标签地址：" + "\n" + "http://marticle.joyme.com/marticle/tags/*_*.html" + "\n" + "第一个*标签id，第二个*分页");
                return false;
            } else if (type == '1') {
            } else if (type == '4') {
                if (url.indexOf("http://www.joyme.com/appwiki/") > 0 && url.indexOf("http://172.16.75.30/") > 0) {
                    alert("请填写正确的WIKI页地址：" + "\n" + "http://www.joyme.com/appwiki/" + "\n" + "如：http://www.joyme.com/appwiki/wzzj/index.shtml 或 http://www.joyme.com/appwiki/wzzj/52739.shtml");
                    return false;
                }
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
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td"><span
                            style="font-size:16px; font-family: Arial, Helvetica, sans-serif; font-weight:bold;">添加一条Line</span>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/clientline/item/create" method="post" id="form_submit" onsubmit="return checkClientLine()">
                <table width="90%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" class="">
                            <input type="hidden" name="lineid" value="${lineid}"/>
                            <input type="hidden" name="itemtype" value="${itemtype}"/>
                            <input type="hidden" name="itemdomain" value="0">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            标题：
                        </td>
                        <td height="1" class="" width="10">
                            <input type="text" name="itemname" size="20" id="input_text_name"/>
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
                    <tr>
                        <td height="1" class="default_line_td">
                            描述:
                        </td>
                        <td height="1" class="">
                            <textarea name="desc" rows="8" cols="50"></textarea>
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            图片:
                        </td>
                        <td height="1">
                            <img id="menu_pic" src="/static/images/default.jpg" onclick="deletepic(this)"/>
                            <span id="upload_button">上传</span>
                            <span id="loading" style="display:none"><img src="/static/images/loading.gif"/></span>
                            <input id="input_menu_pic" type="hidden" name="picurl" value="">
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            跳转类别:
                        </td>
                        <td height="1">

                            <select name="redirecttype" id="redirecttype">
                                <option value="">请选择</option>
                                <c:choose>
                                    <c:when test="${newclient==1}">
                                        <option value="1" selected>
                                            <fmt:message
                                                    key="client.item.redirect.1"
                                                    bundle="${def}"/>
                                        </option>
                                        <option value="4">
                                            <fmt:message
                                                    key="client.item.redirect.4"
                                                    bundle="${def}"/>
                                        </option>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach items="${redirectCollection}" var="type">
                                            <option value="${type.code}"><fmt:message
                                                    key="client.item.redirect.${type.code}"
                                                    bundle="${def}"/></option>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>


                            </select>*手游画报ios暂时不支持webview
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            链接地址:
                        </td>
                        <td height="1" class="">
                            <input id="linkaddress" type="text" size="80" name="linkaddress">
                            <c:if test="${not empty errorMsgMap.urlerror}">
                                <span style="color:red"><fmt:message key="${errorMsgMap.urlerror}"
                                                                     bundle="${def}"/></span>
                            </c:if>
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            category显示分类:
                        </td>
                        <td height="1" class="">
                            <input id="category" type="text" size="80" name="category">
                        </td>
                        <td height="1">
                            <c:if test="${not empty errorMsgMap.urlerror}">
                                <span style="color:red"><fmt:message key="${errorMsgMap.cateerror}"
                                                                     bundle="${def}"/></span>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            category颜色值:
                        </td>
                        <td height="1" class="">
                            <input id="categorycolor" type="text" size="80" name="categorycolor">
                        </td>
                        <td height="1">
                            <c:if test="${not empty errorMsgMap.catecolor}">
                                <span style="color:red"><fmt:message key="${errorMsgMap.urlerror}"
                                                                     bundle="${def}"/></span>
                            </c:if>
                        </td>
                    </tr>
                </table>
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