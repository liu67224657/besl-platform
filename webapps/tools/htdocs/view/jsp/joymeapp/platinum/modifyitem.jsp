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

    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/dhtmlxcalendar.css">
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/skins/dhtmlxcalendar_dhx_skyblue.css">
    <script src="/static/include/dhtmlxcalendar/dhtmlxcalendar.js"></script>
    <script>
        $(document).ready(function () {
            doOnLoad();
            var coustomSwfu = new SWFUpload(coustomImageSettings);

            $('#form_submit').bind("submit", function () {
                var title = $('#input_title').val();
                if (title.length == 0) {
                    alert("请填写标题");
                    return false;
                }
                var desc = $('#textarea_desc').val();
                if (desc.length == 0) {
                    alert("请填写描述");
                    return false;
                }
                var pic = $('#input_menu_pic').val();
                if (pic.length == 0) {
                    alert("请上传图片");
                    return false;
                }
                var url = $('#input_url').val();
                if (url.length == 0) {
                    alert("请填写URL");
                    return false;
                }
                if ($('#input_size').length > 0) {
                    var softSize = $('#input_size').val();
                    if (softSize.length == 0) {
                        alert("请填写应用大小");
                        return false;
                    }
                }
                if ($('#startdate').length > 0) {
                    var startDate = $('#startdate').val();
                    if (startDate.length == 0) {
                        alert("请选择开始时间");
                        return false;
                    }
                }
                if ($('#enddate').length > 0) {
                    var endDate = $('#enddate').val();
                    if (endDate.length == 0) {
                        alert("请选择结束时间");
                        return false;
                    }
                }
            });
        });

        var myCalendar;
        function doOnLoad() {
            myCalendar = new dhtmlXCalendarObject(["startdate", "enddate"]);
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

    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷APP >> APP精选推荐列表</td>
    </tr>
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
            <form action="/platinum/item/modify" method="post" id="form_submit">
                <table width="90%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" class="">
                            <input type="hidden" name="itemid" value="${item.itemId}"/>
                            <input type="hidden" name="lineid" value="${item.lineId}"/>
                            <input type="hidden" name="itemtype" value="${item.itemType.code}"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            标题：
                        </td>
                        <td height="1" class="" width="10">
                            <input type="text" name="title" size="60" id="input_title" value="${item.title}"/>
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
                            <textarea name="desc" rows="8" cols="50" id="textarea_desc">${item.desc}</textarea>
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            图片:
                        </td>
                        <td height="1">
                            <c:choose>
                                <c:when test="${fn:length(item.picUrl) > 0}">
                                    <img id="menu_pic" src="${item.picUrl}"/>
                                </c:when>
                                <c:otherwise>
                                    <img id="menu_pic" src="/static/images/default.jpg"/>
                                </c:otherwise>
                            </c:choose>
                            <span id="upload_button">上传</span>
                            <span id="loading" style="display:none"><img src="/static/images/loading.gif"/></span>
                            <input id="input_menu_pic" type="hidden" name="pic" value="${item.picUrl}">
                        </td>
                        <td height="1"></td>
                    </tr>
                    <%--<tr>--%>
                    <%--<td height="1" class="default_line_td">--%>
                    <%--跳转类别:--%>
                    <%--</td>--%>
                    <%--<td height="1">--%>
                    <%--<select name="redirecttype" id="select_redirect">--%>
                    <%--<option value="">请选择</option>--%>
                    <%--<c:forEach items="${redirectTypes}" var="type">--%>
                    <%--<option value="${type.code}" <c:if test="${type.code == item.redirectType.code}">selected="selected"</c:if>>--%>
                    <%--<fmt:message key="platinum.item.redirect.${type.code}" bundle="${def}"/>--%>
                    <%--</option>--%>
                    <%--</c:forEach>--%>
                    <%--</select>--%>
                    <%--</td>--%>
                    <%--<td height="1"></td>--%>
                    <%--</tr>--%>
                    <tr>
                        <td height="1" class="default_line_td">
                            跳转地址:
                        </td>
                        <td height="1" class="">
                            <input id="input_url" type="text" size="80" name="url" value="${item.url}">
                        </td>
                        <td height="1"></td>
                    </tr>
                    <c:choose>
                        <c:when test="${item.itemType.code == apps}">
                            <tr>
                                <td height="1" class="default_line_td">
                                    应用大小:
                                </td>
                                <td height="1">
                                    <input type="text" name="size" value="${item.param.size}" id="input_size"/>
                                </td>
                                <td height="1">
                                </td>
                            </tr>
                        </c:when>
                        <c:when test="${item.itemType.code == events}">
                            <tr>
                                <td height="1" class="default_line_td">
                                    有效时间:
                                </td>
                                <td height="1">
                                    <input type="text" class="default_input_singleline" size="16" maxlength="20"
                                           name="startdate" id="startdate"
                                           value="${item.param.startDate}"/>
                                    &nbsp;--&nbsp;
                                    <input type="text" class="default_input_singleline" size="16" maxlength="20"
                                           name="enddate"
                                           id="enddate"
                                           value="${item.param.endDate}"/>
                                </td>
                                <td height="1">
                                </td>
                            </tr>
                            <tr>
                                <td class="default_line_td">
                                    是否热门:
                                </td>
                                <td>
                                    <select name="hot">
                                        <option value="false"
                                        <c:if test="${!item.hot}">selected="selected"</c:if>>否</option>
                                        <option value="true"
                                        <c:if test="${item.hot}">selected="selected"</c:if>>是</option>
                                    </select>
                                </td>
                                <td height="1">
                                </td>
                            </tr>
                        </c:when>
                    </c:choose>
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
</body>
</html>