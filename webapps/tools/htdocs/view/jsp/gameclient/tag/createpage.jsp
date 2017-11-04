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
    <script type="text/javascript" src="/static/include/swfupload/joymeapphandler.js"></script>
    <style type="text/css">
        .td_cent {
            text-align: center;
            vertical-align: middle;
        }
    </style>
    <script>
        function getByteLen(val) {
            var len = 0;
            for (var i = 0; i < val.length; i++) {
                if (val[i].match(/[^\x00-\xff]/ig) != null) //全角
                    len += 2;
                else
                    len += 1;
            }
            return len;
        }
        $(document).ready(function () {

            $("input[name='tagtype']").change( function() {
                var tagtype = $("input[name='tagtype']:checked").val();
                if(parseInt(tagtype)==0){
                    $("#url").val("");
                    $("#url").attr("disabled","disabled");
                }else{
                    $("#url").removeAttr("disabled");
                }
            });

            var coustomSwfu = new SWFUpload(coustomImageSettings);
            $('#form_submit').bind('submit', function () {
                if ($("#tag_name").val().trim() == '') {
                    alert("标签名称");
                    $("#tag_name").focus();
                    return false;
                }

                var volume = $("input[name='volume']:checked").val();
                if(volume=="-1"){
                    if($.trim($("#ch_name").val())==""){
                        alert("选择玩霸2.1.0标签，则“玩霸2.1.0标签名称”不能为空");
                        $("#ch_name").focus();
                        return false;
                    }else{
                        var len = getByteLen($.trim($("#ch_name").val()));
                        if(len>10){
                            alert("最多填写10个字符");
                            $("#ch_name").focus();
                            return false;
                        }
                    }
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
            debug: false}
    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷手游画报管理 >> 标签管理</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">新增标签列表</td>
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
            <form action="/gameclient/tag/create" method="post" id="form_submit">
                <input type="hidden" name="parent_tag_id" size="48" value="0"/>
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            标签名称:
                        </td>
                        <td height="1">
                            <input id="tag_name" type="text" name="tag_name" size="48"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent">
                            是否默认：
                        </td>
                        <td height="1" class="">
                            <c:forEach var="type" items="${gameClientTagType}">
                                <c:if test="${type.code==1}">
                                    <input type="radio" name="type" value='${type.code}' checked="checked"/><fmt:message key="gameclient.tag.type.${type.code}" bundle="${def}"/>
                                </c:if>
                                <c:if test="${type.code==2}">
                                    <input type="radio" name="type" value='${type.code}'/><fmt:message key="gameclient.tag.type.${type.code}" bundle="${def}"/>
                                </c:if>
                            </c:forEach>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="150">
                            玩霸2.1.0标签名称:
                        </td>
                        <td height="1">
                            <input id="ch_name" type="text" name="ch_name" size="48" value="${animeTag.ch_name}"/><span style="color: red;">备注：最多填写10个字符</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent">
                            是否是玩霸2.1.0标签：
                        </td>
                        <td height="1" class="">
                            <c:if test="${animeTag.volume==-1}">
                                <input type="radio" name="volume" value='-1' checked="checked"/>是
                                <input type="radio" name="volume" value='0'/>否
                            </c:if>
                            <c:if test="${animeTag.volume!=-1}">
                                <input type="radio" name="volume" value='-1'/>是
                                <input type="radio" name="volume" value='0'  checked="checked"/>否
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            ICON:
                        </td>
                        <td height="1">
                            <img id="img_icon" src="/static/images/default.jpg" height="64" width="64"/>
                            <span id="upload_button">上传</span>
                            <span id="loading" style="display:none"><img src="/static/images/loading.gif"/></span>
                            <input id="msg_icon" type="hidden" name="icon">
                        </td>
                        <td height="1">
                        </td>
                    </tr>


                    <tr>
                        <td height="1" class="default_line_td td_cent">
                            标签类型(玩霸>=2.3有效)：
                        </td>
                        <td height="1" class="">
                            <c:forEach var="type" items="${tagType}">
                                <c:if test="${type.code==0}">
                                    <input type="radio" name="tagtype" value='${type.code}' checked="checked"/><fmt:message key="gameclient.tagtype.${type.code}" bundle="${def}"/>
                                </c:if>
                                <c:if test="${type.code==1}">
                                    <input type="radio" name="tagtype" value='${type.code}'/><fmt:message key="gameclient.tagtype.${type.code}" bundle="${def}"/>
                                </c:if>
                            </c:forEach>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="150">
                            web地址(标签类型为web类型时有效):
                        </td>
                        <td height="1">
                            <input id="url" type="text" name="url" size="48" value=""/></span>
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