<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>修改APP广告素材</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
     <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/gameclientadvertise.js"></script>
    <style type="text/css">
        .td_cent{text-align:center;vertical-align:middle}
    </style>
    <script>
        $(document).ready(function() {
            var coustomSwfu = new SWFUpload(coustomImageSettings);
            var coustomSwfu2 = new SWFUpload(coustomImageSettings2);
            var coustomSwfu2 = new SWFUpload(coustomImageSettings3);
            var coustomSwfu2 = new SWFUpload(coustomImageSettings4);



            $('#form_submit').bind('submit', function() {
                if ($("#input_text_adname").val() == '') {
                    alert("请填写广告名称");
                     $("#input_text_adname").focus();
                    return false;
                }

                 if ($("#picurl1").val() == '') {
                    alert("请填写图片1");
                    return false;
                }
                 if ($("#picurl2").val() == '') {
                    alert("请填写图片2");
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
            debug: false
        }

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
            debug: false
        }

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
            debug: false
        }

        var coustomImageSettings4 = {
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

            file_dialog_complete_handler: fileDialogComplete4,
            upload_start_handler: uploadStart4,
            upload_success_handler: uploadSuccess4,
            upload_complete_handler: uploadComplete4,

            // Button Settings
            button_image_url: "/static/images/uploadbutton.png",
            button_placeholder_id: "upload_button4",
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
        <td height="22" class="page_navigation_td">>> 运营维护 >>  着迷手游画报管理 >> 广告素材列表</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">修改APP广告素材</td>
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
            <form action="/gameclient/advertise/modify" method="post" id="form_submit">
                 <input type="hidden" name="advertiseId" value="${appAdvertise.advertiseId}">
                 <input type="hidden" name="pager.offset" value="${pageStartIndex}">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100" color="red" backg>
                            广告名称:
                        </td>
                        <td height="1">
                            <input id="advertise_name" type="text" name="advertise_name" size="100" value="${appAdvertise.advertiseName}"/><span
                                style="color:red;">*必填项</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100" color="red" backg>
                            广告描述:
                        </td>
                        <td height="1">
                            <input id="advertise_desc" type="text" name="advertise_desc" size="100" value="${appAdvertise.advertiseDesc}"/><span
                                style="color:red;">*必填项</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            平台:
                        </td>
                        <td height="1">
                            <c:if test="${appAdvertise.appPlatform.code == '0'}">
                                <input type="radio" name="appPlatform" checked="true" value="0"/>IOS&nbsp;
                                <input type="radio" name="appPlatform" value="1"/>ANDROID&nbsp;
                            </c:if>
                            <c:if test="${appAdvertise.appPlatform.code == '1'}">
                                <input type="radio" name="appPlatform" value="0"/>IOS&nbsp;
                                <input type="radio" name="appPlatform" checked="true" value="1"/>ANDROID&nbsp;
                            </c:if>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100" color="red" backg>
                            广告1名称:
                        </td>
                        <td height="1">
                            <input id="input_text_adname1" type="text" name="adname1" size="100" value="${gameExistJson.title1}"/><span
                                style="color:red;">*必填项</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            广告1描述:
                        </td>
                        <td height="1">
                            <textarea id="input_description1" type="text" name="addesc1"
                                      style="height: 100px;width: 634px">${gameExistJson.desc1}</textarea>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            广告1链接:
                        </td>
                        <td height="1">
                            <input id="input_text_url1" type="text" name="url1" size="100" value="${gameExistJson.url1}"/>
                            <br/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            广告1图片:
                        </td>
                        <td>
                                <img id="picurl1_src" src="${gameExistJson.picurl1}"
                                                  class="img_pic" width="100px" height="100px"/>
                            <span id="upload_button" class="upload_button">上传</span>
                            <span id="loading" style="display:none" class="loading"><img
                                    src="/static/images/loading.gif"/></span>
                            <input id="picurl1" type="hidden" name="picurl1" value="${gameExistJson.picurl1}"><span
                                style="color:red;">*必填项</span>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            广告1跳转类型:
                        </td>
                        <td height="1">
                            <c:if test="${gameExistJson.type1==0}">
                                <input type="radio" name="redirecttype1" checked="true" value="0"/>WEB_VIEW&nbsp;
                                <input type="radio" name="redirecttype1" value="1"/>APP_STORE&nbsp;
                            </c:if>
                            <c:if test="${gameExistJson.type1==1}">
                                <input type="radio" name="redirecttype1"  value="0"/>WEB_VIEW&nbsp;
                                <input type="radio" name="redirecttype1" checked="true" value="1"/>APP_STORE&nbsp;
                            </c:if>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>


                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            广告2名称:
                        </td>
                        <td height="1">
                            <input id="input_text_adname2" type="text" name="adname2" size="100" value="${gameExistJson.title2}"/><span
                                style="color:red;">*必填项</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            广告2描述:
                        </td>
                        <td height="1">
                            <textarea id="input_description2" type="text" name="addesc2"
                                      style="height: 100px;width: 634px">${gameExistJson.desc2}</textarea>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            广告2链接:
                        </td>
                        <td height="1">
                            <input id="input_text_url2" type="text" name="url2" size="100" value="${gameExistJson.url2}"/>
                            <br/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            广告2图片:
                        </td>
                        <td>
                           <img id="picurl2_src" src="${gameExistJson.picurl2}"
                                                  class="img_pic" width="100px" height="100px"/>
                            <span id="upload_button2" class="upload_button2">上传</span>
                            <span id="loading2" style="display:none" class="loading"><img
                                    src="/static/images/loading.gif"/></span>
                            <input id="picurl2" type="hidden" name="picurl2" value="${gameExistJson.picurl2}"><span
                                style="color:red;">*必填项</span>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            广告2跳转类型:
                        </td>
                        <td height="1">
                            <c:if test="${gameExistJson.type2==0}">
                                <input type="radio" name="redirecttype2" checked="true" value="0"/>WEB_VIEW&nbsp;
                                <input type="radio" name="redirecttype2" value="1"/>APP_STORE&nbsp;
                            </c:if>
                            <c:if test="${gameExistJson.type2==1}">
                                <input type="radio" name="redirecttype2"  value="0"/>WEB_VIEW&nbsp;
                                <input type="radio" name="redirecttype2" checked="true" value="1"/>APP_STORE&nbsp;
                            </c:if>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>


                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            广告3名称:
                        </td>
                        <td height="1">
                            <input id="input_text_adname3" type="text" name="adname3" size="100" value="${gameExistJson.title3}"/><span
                                style="color:red;">*必填项</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            广告3描述:
                        </td>
                        <td height="1">
                            <textarea id="input_description3" type="text" name="addesc3"
                                      style="height: 100px;width: 634px">${gameExistJson.desc3}</textarea>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            广告3链接:
                        </td>
                        <td height="1">
                            <input id="input_text_url3" type="text" name="url3" size="100" value="${gameExistJson.url3}"/>
                            <br/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            广告3图片:
                        </td>
                        <td>
                           <img id="picurl3_src" src="${gameExistJson.picurl3}"
                                                  class="img_pic" width="100px" height="100px"/>
                            <span id="upload_button3" class="upload_button3">上传</span>
                            <span id="loading3" style="display:none" class="loading"><img
                                    src="/static/images/loading.gif"/></span>
                            <input id="picurl3" type="hidden" name="picurl3" value="${gameExistJson.picurl3}"><span
                                style="color:red;">*必填项</span>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            广告3跳转类型:
                        </td>
                        <td height="1">
                            <c:if test="${gameExistJson.type3==0}">
                                <input type="radio" name="redirecttype3" checked="true" value="0"/>WEB_VIEW&nbsp;
                                <input type="radio" name="redirecttype3" value="1"/>APP_STORE&nbsp;
                            </c:if>
                            <c:if test="${gameExistJson.type3==1}">
                                <input type="radio" name="redirecttype3"  value="0"/>WEB_VIEW&nbsp;
                                <input type="radio" name="redirecttype3" checked="true" value="1"/>APP_STORE&nbsp;
                            </c:if>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>



                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            广告4名称:
                        </td>
                        <td height="1">
                            <input id="input_text_adname4" type="text" name="adname4" size="100" value="${gameExistJson.title4}"/><span
                                style="color:red;">*必填项</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            广告4描述:
                        </td>
                        <td height="1">
                            <textarea id="input_description4" type="text" name="addesc4"
                                      style="height: 100px;width: 634px">${gameExistJson.desc4}</textarea>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            广告4链接:
                        </td>
                        <td height="1">
                            <input id="input_text_url4" type="text" name="url4" size="100" value="${gameExistJson.url4}"/>
                            <br/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            广告4图片:
                        </td>
                        <td>
                           <img id="picurl4_src" src="${gameExistJson.picurl4}"
                                                  class="img_pic" width="100px" height="100px"/>
                            <span id="upload_button4" class="upload_button4">上传</span>
                            <span id="loading4" style="display:none" class="loading"><img
                                    src="/static/images/loading.gif"/></span>
                            <input id="picurl4" type="hidden" name="picurl4" value="${gameExistJson.picurl4}"><span
                                style="color:red;">*必填项</span>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            广告4跳转类型:
                        </td>
                        <td height="1">
                            <c:if test="${gameExistJson.type4==0}">
                                <input type="radio" name="redirecttype4" checked="true" value="0"/>WEB_VIEW&nbsp;
                                <input type="radio" name="redirecttype4" value="1"/>APP_STORE&nbsp;
                            </c:if>
                            <c:if test="${gameExistJson.type4==1}">
                                <input type="radio" name="redirecttype4"  value="0"/>WEB_VIEW&nbsp;
                                <input type="radio" name="redirecttype4" checked="true" value="1"/>APP_STORE&nbsp;
                            </c:if>
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