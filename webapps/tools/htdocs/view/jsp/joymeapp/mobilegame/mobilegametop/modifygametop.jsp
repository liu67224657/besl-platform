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
            $('#form_submit').bind('submit', function () {
                var name = $('#input_text_name').val().trim();
                var intro = $("[name='intro']").val().trim();
                var linedesc = $("[name='linedesc']").val().trim();
                var bigpic = $("#input_menu_pic").val();
                var url = $("#reurl").val().trim();

                $("#input_text_name").val(name);
                $("[name='intro']").val(intro);
                $("[name='linedesc']").val(linedesc);
                if (itemType == '4') {
                    if (name.length == 0) {
                        alert("请填写名称");
                        return false;
                    }
                    if (name.trim().length > 18) {
                        alert("标题请控制在18个字符以内");
                        return false;
                    }
                }
                var code = $('#input_text_code').val();
                if (code.length == 0) {
                    alert("请填写code");
                    return false;
                }
                if (bigpic == '') {
                    alert("请上传图片");
                    return false;
                }
//                var type = $('#select_type').val();
//                if (type.length == 0) {
//                    alert("请选择类型");
//                    return false;
//                }
                var platform = $("[name='platform']").val();
                if (platform == '') {
                    alert("请选择平台");
                    return false;
                }
                $("#input_menu_pic3").val(url);
                if (url != "") {
                    if (url.indexOf("http://") < 0 && url.indexOf("https://") < 0) {
                        alert("URL请以http://或者https://开头");
                        return false;
                    }
                    $("#input_menu_pic3").val(url);
                }

            });
            var itemType = '${clientLine.itemType.code}';
            if (itemType == 7) {
                $("#pic2").css("display", "none");
                $("#linedesc").css("display", "none");
                $("#pic3").css("display", "none");
                $("#url").css("display", "table-row");
            }
            var coustomSwfu = new SWFUpload(coustomImageSettings);
            var coustomSwfu2 = new SWFUpload(coustomImageSettings2);
            var coustomSwfu3 = new SWFUpload(coustomImageSettings3);
        });

        function itemtypeChange() {
            var itemType = $("[name='itemtype']").val();
            if (itemType == '7') {
                $("#pic2").css("display", "none");
                $("#linedesc").css("display", "none");
                $("#pic3").css("display", "none");
                $("#url").css("display", "table-row");
            } else {
                $("#pic2").css("display", "table-row");
                $("#linedesc").css("display", "table-row");
                $("#pic3").css("display", "table-row");
                $("#url").css("display", "none");
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

        var coustomImageSettings3 = {
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

            file_dialog_complete_handler: fileDialogComplete3,
            upload_start_handler: uploadStart3,
            upload_success_handler: uploadSuccess3,
            upload_complete_handler: uploadComplete3,

            // Button Settings
            button_image_url: "/static/images/uploadbutton.png",
            button_placeholder_id: "upload_button3",
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
    <tr height="40">
        <td height="22" class="page_navigation_td" colspan="2">>> 运营维护 >> 手游排行榜 >> 手游排行榜集合</td>
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
            <form action="/mobile/top/modify" method="post" id="form_submit">
                <table width="90%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" class="">
                            <input type="hidden" name="lineid" value="${clientLine.lineId}"/>
                        </td>
                    </tr>
                    <%--<tr>--%>
                    <%--<td height="1"> 类型：<select onchange="itemtypeChange();" name="itemtype">--%>
                    <%--<option value="">请选择</option>--%>
                    <%--<option value="4"--%>
                    <%--　<c:if test="${clientLine.itemType.code==4}">selected</c:if> >手游排行榜</option>--%>
                    <%--<option value="7"--%>
                    <%--<c:if test="${clientLine.itemType.code==7}">selected</c:if> >推广</option>--%>
                    <%--</select>--%>
                    <%--</td>--%>
                    <%--</tr>--%>
                    <tr>
                        <td height="1" class="default_line_td">
                            名称
                        </td>
                        <td height="1" class="" width="10">
                            <input type="text" name="linename" size="32" id="input_text_name"
                                   value="${clientLine.lineName}"/> *必填项
                        </td>
                        <td height="1" align="left"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            编码:
                        </td>
                        <td height="1" class="">
                            <input id="input_text_code" type="text" size="32" name="linecode"
                                   value="${clientLine.code}">*必填项
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            排行榜简介:
                        </td>
                        <td height="1" class="">
                            <textarea cols="55" rows="20" name="intro">${clientLine.line_intro}</textarea><span
                                style="color:red">必填 </span>
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr id="linedesc">
                        <td height="1" class="default_line_td">
                            排行榜描述:
                        </td>
                        <td height="1" class="">
                            <textarea cols="55" rows="20" name="linedesc">${clientLine.line_desc}</textarea><span
                                style="color:red">必填 </span>
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            图片:
                        </td>
                        <td>
                            <img id="menu_pic" src="${clientLine.bigpic}" class="img_pic"/>
                            <span id="upload_button" class="upload_button">上传</span>
                            <span id="loading" style="display:none" class="loading"><img
                                    src="/static/images/loading.gif"/></span>
                            <input id="input_menu_pic" type="hidden" name="picurl" value="${clientLine.bigpic}">*必填项
                            <span style="color: #ff0000;">*(根据情况选择图片，不做死要求,注意尺寸）</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr id="pic2">
                        <td height="1" class="default_line_td" width="100">
                            排行榜子页头图:
                        </td>
                        <td>
                            <img id="menu_pic2" src="${clientLine.smallpic}" class="img_pic"/>
                            <span id="upload_button2" class="upload_button">上传</span>
                            <span id="loading2" style="display:none" class="loading"><img
                                    src="/static/images/loading.gif"/></span>
                            <input id="input_menu_pic2" type="hidden" name="picurl2" value="${clientLine.smallpic}">*必填项
                            <span style="color: #ff0000;">*(根据情况选择图片，不做死要求,注意尺寸）</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr id="pic3">
                        <td height="1" class="default_line_td" width="100">
                            分享出去的icon:
                        </td>
                        <td>
                            <img id="menu_pic3" src="${clientLine.sharePic}" class="img_pic"/>
                            <span id="upload_button3" class="upload_button">上传</span>
                            <span id="loading3" style="display:none" class="loading"><img
                                    src="/static/images/loading.gif"/></span>
                            <input id="input_menu_pic3" type="hidden" name="picurl3" value="${clientLine.sharePic}">*必填项
                            <span style="color: #ff0000;">*(图片禁止大于30KB！！！）</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            平台 ;
                        </td>
                        <td height="1" class="" width="10">
                            <select name='platform'>
                                <option value=''>请选择</option>
                                <option value='0'
                                <c:if test="${clientLine.platform==0}">selected</c:if> >IOS
                                </option>
                                <option value='1'
                                <c:if test="${clientLine.platform==1}">selected</c:if>>Android
                                </option>

                            </select>
                            *必填项
                        </td>
                        <td height="1" align="left"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            标签属性;
                        </td>
                        <td height="1" class="" width="10">
                            <select name='hot'>
                                <option value='0'
                                <c:if test="${clientLine.hot==0}">selected</c:if>>不加入
                                </option>
                                <option value='1'
                                <c:if test="${clientLine.hot==1}">selected</c:if>>加入热门
                                </option>
                                <option value='2'
                                <c:if test="${clientLine.hot==2}">selected</c:if>>加入最新
                                </option>
                                <option value='3'
                                <c:if test="${clientLine.hot==3}">selected</c:if>>推广
                                </option>
                            </select>
                            *必填项
                        </td>
                        <td height="1" align="left"></td>
                    </tr>
                    <tr id="url" style="display:none;">
                        <td height="1" class="default_line_td">
                            跳转链接：
                        </td>
                        <td height="1" class="" width="10">
                            <input type="text" id="reurl" value="${clientLine.sharePic}"/>
                            *不填不跳转，填了请用http://或https://开头
                        </td>
                        <td height="1" align="left"></td>
                    </tr>
                    <tr>
                        <td>
                            <c:if test="${fn:length(codeExist)>0}">
                                <span style="color: red" id="span_name_exist"><fmt:message key="${codeExist}"
                                                                                           bundle="${def}"/></span>
                            </c:if>
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