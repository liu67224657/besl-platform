<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>创建APP</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>

    <script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/joymeappmenuhandler.js"></script>
    <script>
        $(document).ready(function() {
            var coustomSwfu = new SWFUpload(coustomImageSettings);
            $('#form_submit').bind('submit', function() {
                var rate = $("#rate").val();
                var wikiname = $("#wikiname").val();
                var input_menu_pic = $("#input_menu_pic").val();
                var url = $("#url").val();
                var category = $("#category").val();
                var wikikey = $("#wikikey").val();
                if (wikiname.trim() == "") {
                    alert("请填写wiki名称");
                    return false;
                }
                if (wikikey.trim() == "") {
                    alert("请填写wikikey");
                    return false;
                }
                if (input_menu_pic.trim() == "") {
                    alert("请上传图片");
                    return false;
                }
                if (url.trim() == "") {
                    alert("请填写URL");
                    return false;
                }

                if (isNaN(rate) || rate.trim() == '') {
                    alert("热度请填写数字");
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
        <td height="22" class="page_navigation_td">>> 运营管理 >> 条目管理 >> WIKITOP</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">修改wikitop</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/wiki/top/item/modify" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td">

                            <input type="hidden" value="${clientLineItem.itemId}" name="itemid"/>
                            <input type="hidden" value="${code}" name="code"/>
                        </td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td">
                            wiki名称:
                        </td>
                        <td height="1">
                            <input type="text" name="wikiname" size="32" value="${clientLineItem.title}" id="wikiname"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            wiki_key:
                        </td>
                        <td height="1">
                            <input type="text" name="wikikey" size="32" id="wikikey"
                                   value="${clientLineItem.directId}"/> <span
                                style="color:red;">必填 wiki的key,如op、mt</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            icon:
                        </td>
                        <td height="1">
                            <c:choose>
                                <c:when test="${not empty clientLineItem.picUrl}">
                                    <img id="menu_pic" src="${clientLineItem.picUrl}" onclick="deletepic(this)"/>
                                </c:when>
                                <c:otherwise>
                                    <img id="menu_pic" src="/static/images/default.jpg" onclick="deletepic(this)"/>
                                </c:otherwise>
                            </c:choose>

                            <span id="upload_button">上传</span>
                            <span id="loading" style="display:none"><img src="/static/images/loading.gif"/></span>
                            <input id="input_menu_pic" type="hidden" name="picurl" value="${clientLineItem.picUrl}">
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            链接地址:
                        </td>
                        <td height="1">
                            <input type="text" name="url" size="32" id="url" value="${clientLineItem.url}"/> <span
                                style="color:red;">必填 wiki的url</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            分类标签:
                        </td>
                        <td height="1">
                            <input type="text" name="category" size="32" id="category"
                                   value="${clientLineItem.category}"/> <span style="color:red;">标签，不填不显示</span>

                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            热度:
                        </td>
                        <td height="1">
                            <input type="text" name="rate" size="32" value="${clientLineItem.rate}" id="rate"/><span
                                style="color:red;">必填，热度做为排序字段请填写数字</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>

                    <%--<tr>--%>
                    <%--<td height="1" class="default_line_td">--%>
                    <%--“我的”模块是否显示小红点:--%>
                    <%--</td>--%>
                    <%--<td height="1" class="edit_table_defaulttitle_td">--%>
                    <%--<select name="displayred">--%>
                    <%--<option value="0">不显示</option>--%>
                    <%--<option value="1">显示</option>--%>
                    <%--</select>--%>
                    <%--</td>--%>
                    <%--<td height="1" class=>--%>
                    <%--</td>--%>
                    <%--</tr>--%>
                    <%--<tr>--%>
                    <%--<td height="1" class="default_line_td">--%>
                    <%--选择平台:--%>
                    <%--</td>--%>
                    <%--<td height="1" class="edit_table_defaulttitle_td">--%>
                    <%--<select name="platform">--%>
                    <%--<c:forEach var="platform" items="${platformList}">--%>
                    <%--<option value="${platform.code}"><fmt:message key="joymeapp.platform.${platform.code}" bundle="${def}"/></option>--%>
                    <%--</c:forEach>--%>
                    <%--</select>--%>
                    <%--</td>--%>
                    <%--<td height="1" class=>--%>
                    <%--</td>--%>
                    <%--</tr>--%>
                    <tr>

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