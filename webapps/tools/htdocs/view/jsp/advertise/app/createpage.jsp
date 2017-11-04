<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>创建APP广告素材</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/appadvertise.js"></script>
    <style type="text/css">
        .td_cent {
            text-align: center;
            vertical-align: middle
        }

        ;
    </style>

    <script>
        $().ready(function () {
            var coustomSwfu = new SWFUpload(coustomImageSettings);
            var coustomSwfu2 = new SWFUpload(coustomImageSettings2);


            $('#form_submit').bind('submit', function () {
                if ($("#input_text_adname").val() == '') {
                    alert("请填写广告名称");
                    $("#input_text_adname").focus();
                    return false;
                }


                var url = $.trim($('#input_text_url').val());
                if (url.length > 0) {
                    if(url.indexOf("#")!=-1){
                        alert("请不要填写包含#的URL");
                        return false;
                    }
                }


//                 if ($("#input_text_url").val() == '') {
//                    alert("请填写广告链接");
//                     $("#input_text_url").focus();
//                    return false;
//                }

//                if ($("#picurl1").val() == '') {
//                    alert("请填写图片1");
//                    return false;
//                }
//                if ($("#picurl2").val() == '') {
//                    alert("请填写图片1");
//                    return false;
//                }
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
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> APP广告管理 >> APP广告素材列表</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">新增APP广告素材</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="10" class="error_msg_td">
                            <fmt:message key="${errorMsg}" bundle="${error}"/>
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/advertise/app/create" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            广告名称:
                        </td>
                        <td height="1">
                            <input id="input_text_adname" type="text" name="adname" size="100" value=""/>
                            <span style="color:red;">*必填项</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            广告描述:
                        </td>
                        <td height="1">
                            <textarea id="input_description" type="text" name="addesc"
                                      style="height: 100px;width: 634px">${description}</textarea>
                            <span style="color:red;">*文字广告的文本内容</span>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            广告链接:
                        </td>
                        <td height="1">
                            <input id="input_text_url" type="text" name="url" size="100" value=""/>
                            <br/><span style="color:red;">若需要填写，必须以"http://"开头，特殊情况可不填，需慎重考虑。</span>
                            <br/><span style="color:red">请不要填写包含#的URL</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            链接是否采用短链:
                        </td>
                        <td height="1">
                            是<input type="radio" name="shortlinkurl" value="1"/>
                            &nbsp;&nbsp;&nbsp;&nbsp;否<input checked="true" type="radio" name="shortlinkurl" value="0"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            跳转类型:
                        </td>
                        <td height="1">
                            <select name="redirecttype">
                                <option value="0">WEB_VIEW</option>
                                <option value="1">APP_STORE</option>
                                <c:forEach items="${WanbaJt}" var="type">
                                    <option value="${type.code}">${type.name}</option>
                                </c:forEach>
                            </select>

                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            平台:
                        </td>
                        <td height="1">
                            <input type="radio" name="appPlatform" checked="true" value="0"/>IOS&nbsp;
                            <input type="radio" name="appPlatform" value="1"/>ANDROID&nbsp;
                            <input type="radio" name="appPlatform" value="2"/>WEB&nbsp;
                            <input type="radio" name="appPlatform" value="3"/>CLIENT&nbsp;
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <%--<tr>--%>
                        <%--<td height="1" class="default_line_td td_cent" width="100">--%>
                            <%--iphone4图片:--%>
                        <%--</td>--%>
                        <%--<td>--%>
                            <%--<c:choose>--%>
                                <%--<c:when test="${fn:length(iosPic) > 0}"><img id="picurl1_src" src=""--%>
                                                                             <%--class="img_pic" width="200px"--%>
                                                                             <%--height="200px"/></c:when>--%>
                                <%--<c:otherwise><img id="picurl1_src" src="/static/images/default.jpg"--%>
                                                  <%--class="img_pic" width="200px" height="200px"/></c:otherwise>--%>
                            <%--</c:choose>--%>
                            <%--<span id="upload_button" class="upload_button">上传</span>--%>
                            <%--<span id="loading" style="display:none" class="loading"><img--%>
                                    <%--src="/static/images/loading.gif"/></span>--%>
                            <%--<input id="picurl1" type="hidden" name="picurl1" value=""><span--%>
                                <%--style="color:red;">*已废弃</span>--%>
                        <%--</td>--%>
                        <%--<td height="1">--%>
                        <%--</td>--%>
                    <%--</tr>--%>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            ios/安卓图片:
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${fn:length(androidPic) > 0}"><img id="picurl2_src" src=""
                                                                                 class="img_pic" width="200px"
                                                                                 height="200px"/></c:when>
                                <c:otherwise><img id="picurl2_src" src="/static/images/default.jpg"
                                                  class="img_pic" width="200px" height="200px"/></c:otherwise>
                            </c:choose>
                            <span id="upload_button2" class="upload_button">上传</span>
                            <span id="loading2" style="display:none" class="loading"><img
                                    src="/static/images/loading.gif"/></span>
                            <input id="picurl2" type="hidden" name="picurl2" value="">
                            <span style="color:red;">*广告图</span>
                            <br/>开屏广告1920 x 1080,小于260KB。
                            <br/>开屏广告1920 x 1080,小于260KB。
                            <br/>开屏广告1920 x 1080,小于260KB。
                            <br/>开屏广告1920 x 1080,小于260KB。
                            <br/>开屏广告1920 x 1080,小于260KB。
                            <br/>开屏广告1920 x 1080,小于260KB。
                        </td>
                        <td height="1">
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