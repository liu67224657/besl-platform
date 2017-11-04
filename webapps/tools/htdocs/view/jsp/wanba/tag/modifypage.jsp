<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title></title>
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
            vertical-align: middle;
        }
    </style>
    <script>
        $(document).ready(function () {
            var coustomSwfu = new SWFUpload(coustomImageSettings);
            $('#form_submit').bind('submit', function () {
                if ($("#tag_name").val() == '') {
                    alert("标签名称");
                    $("#tag_name").focus();
                    return false;
                }
                if ($("#tag_desc").val() == '') {
                    alert("标签描述");
                    $("#tag_desc").focus();
                    return false;
                }

                var tagtype = $("input[name='tagtype']:checked").val();
                if(parseInt(tagtype)==1){
                    var url = $("#url").val();
                    if($.trim(url)==""){
                        alert("请填写web地址");
                        return false;
                    }
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


    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 玩霸-问答 >> 问答标签</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">新增问答标签</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="10" class="error_msg_td">
                            ${errorMsg}
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/wanba/tag/modify" method="post" id="form_submit">
                <input type="hidden" name="tag_id" size="48" value="${animeTag.tag_id}"/>
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            标签名称:
                        </td>
                        <td height="1">
                            <input id="tag_name" type="text" name="tag_name" size="48" value="${animeTag.tag_name}"/><span>*必填</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="80">
                            标签描述:
                        </td>
                        <td height="1">
                            <textarea id="tag_desc" type="text" name="tag_desc" style="height: 60px;width: 370px">${animeTag.tag_desc}</textarea><span>*必填</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            图片ICON:
                        </td>
                        <td>
                            <img id="picurl1_src" src="${animeTag.picjson.ios}"
                                 class="img_pic" width="200px" height="200px"/>
                            <span id="upload_button" class="upload_button">上传</span>
                            <span id="loading" style="display:none" class="loading"><img
                                    src="/static/images/loading.gif"/></span>
                            <input id="picurl1" type="hidden" name="spic" value="${animeTag.picjson.ios}">
                        </td>
                        <td height="1">
                        </td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td td_cent" width="80">
                            标签类型：
                        </td>
                        <td height="1" class="">

                                <c:if test="${animeTag.picjson.type==0}">
                                    <input type="radio"  name="tagtype" value='1'
                                           />web标签
                                    <input type="radio" name="tagtype" value='0'
                                           checked="checked"/>普通标签
                                    <input type="radio" name="tagtype" value='2'/>活动列表
                                </c:if>
                                <c:if test="${animeTag.picjson.type==1}">
                                    <input type="radio" name="tagtype" value='1'
                                           checked="checked"/>web标签
                                    <input  type="radio"  name="tagtype" value='0'
                                           />普通标签
                                    <input type="radio" name="tagtype" value='2'/>活动列表
                                </c:if>
                            <c:if test="${animeTag.picjson.type==2}">
                                <input type="radio" name="tagtype" value='1'/>web标签
                                <input  type="radio"  name="tagtype" value='0'/>普通标签
                                <input type="radio" name="tagtype" value='2' checked="checked"/>活动列表
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="80">
                            web地址
                        </td>
                        <td height="1">
                            <input id="url" type="text" name="url" size="48" value="${animeTag.picjson.url}"/><span
                                style="color: red;">标签类型为web类型时有效</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td td_cent" width="80">
                            状态
                        </td>
                        <td height="1">

                            <c:forEach var="type" items="${remove_status}">
                                <input type="radio" name="remove_status" value="${type.code}"
                                <c:if test="${type.code==animeTag.remove_status.code}">checked='checked'</c:if> /><fmt:message key="anime.remove.status.${type.code}"  bundle="${def}"/>

                            </c:forEach>
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