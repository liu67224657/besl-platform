<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title></title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/joymeappmenuhandler.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.Jcrop.js"></script>
    <link type="text/css" rel="stylesheet" href="/static/include/css/jquery.Jcrop.css"/>
    <style type="text/css">
        .td_cent {
            text-align: center;
            vertical-align: middle
        }

        a {
            text-decoration: none;
        }

    </style>
    <script>
        var urlUpload = '${urlUpload}';
        var corpObj;
        $(window).load(function () {
            $('#form_submit').bind('submit', function () {
//                if ($.trim($("#desc").val()) == "") {
//                    $("#desc").focus();
//                    alert('请填写帖子内容');
//                    return false;
//                }
//                var strlen = isChinese($("#desc"));
//                strlen = Math.ceil(strlen / 2);
//                if (strlen <= 1) {
//                    alert("最少要输入两个字");
//                    return false;
//                }
//                if (strlen > 140) {
//                    alert("最多只能输入140个字，现在字数" + strlen);
//                    return false;
//                }
                if ($.trim($("#nickname").val()) == "") {
                    alert("请选择虚拟用户昵称。");
                    return false;
                }
                if ($.trim($("[name=groupid]").val()) == "") {
                    alert("请选择圈子。");
                    return false;
                }

                var gamepic = '';
                $("img[name='gamepicurl']").each(function () {
                    var src = $(this).attr("src");
                    gamepic = gamepic + (src + "@");
                });
                gamepic = gamepic.substring(0, gamepic.length - 1);
                $("#input_menu_pic").val(gamepic);
                var desc = $.trim($("#desc").val());
                var pic = $("#input_menu_pic").val();
                if (desc == "" && pic == "") {
                    alert("图片或文字至少选择一个输入");
                    return false;
                }

            });
            var coustomSwfu = new SWFUpload(coustomImageSettings);
        });
        var coustomImageSettings = {
            upload_url : "${urlUpload}/json/upload/qiniu",
            post_params : {
                "at" : "joymeplatform",
                "filetype":"original"
            },

            // File Upload Settings
            file_size_limit : "1 MB",    // 2MB
            file_types : "*.jpg;*.png;*.gif",
            file_types_description : "请选择图片",
            file_queue_limit : 1,

            file_dialog_complete_handler : fileDialogComplete,
            upload_start_handler:  uploadStart,
            upload_success_handler : uploadSuccess,
            upload_complete_handler : uploadComplete,

            // Button Settings
            button_image_url : "/static/images/uploadbutton.png",
            button_placeholder_id : "upload_button",
            button_width: 61,
            button_height: 22,
            moving_average_history_size: 40,

            // Flash Settings
            flash_url : "/static/include/swfupload/swfupload.swf",
            flash9_url : "/static/include/swfupload/swfupload_fp9.swf",

            custom_settings : {},
            // Debug Settings
            debug: false}

        function isChinese(str) { //判断是不是中文
            var oVal = str.val();
            var oValLength = 0;
            oVal.replace(/n*s*/, '') == '' ? oValLength = 0 : oValLength = oVal.match(/[^ -~]/g) == null ? oVal.length : oVal.length + oVal.match(/[^ -~]/g).length;
            return oValLength;

        }

        function fileDialogComplete(numFilesSelected, numFilesQueued) {
            var piclength = $("img[name='gamepicurl']").size();
            if (piclength > 8) {
                alert('最多只能上传9张图片');
                return;
            } else {
                this.startUpload();
            }

        }
        window.imgLocaL = 0;
        function uploadSuccess(file, serverData) {
            try {
                var jsonData = eval('(' + serverData + ')');
                if (jsonData.status_code == '1') {
                    var imgSrc = genImgDomain(jsonData.result[0], DOMAIN);

                    if (imgLocaL > 9) {

                    } else {
                        $("#menu_pic").css("display", "none");
                        var img = $('<span style="padding-left:15px;"  id="gamepic' + imgLocaL + '" onclick="deletePic(this)"><img src="' + imgSrc + '" name="gamepicurl" /></span><br/>');
                        //$("#gamepic").attr("src", imgSrc);
                        //$("#picurl").val(imgSrc);

                        $("#upload").before(img);
                        $("#loading").css("display", "none");
                    }
                    imgLocaL++;
                }
            } catch (ex) {
                this.debug(ex);
            }
        }

        function deletePic(pic) {
            if (confirm("是否删除该图片？")) {
                var id = pic.id;
                $("#" + id).remove();
            }
        }

        function username(name) {
            $("#nickname").val(name);
        }
    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷手游画报管理 >> 玩霸迷友圈主贴</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">玩霸迷友圈主贴</td>
                </tr>
            </table>
            <form action="/comment/bean/create" method="post" id="form_submit">
                <%--<span style="color:red;font-size: 20px;">注意:在玩霸新版本(2.2)过审之前，为了兼容老版本，帖子内容和图片都必须填</span>--%>
                <table width="800px" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            帖子内容
                        </td>
                        <td height="1" style="width:150px;">
                            <textarea name="text" id="desc" cols="50" rows="10">${text}</textarea>
                        </td>
                        <td height="1" class=>

                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="150" height="100">
                            虚拟用户名称:
                        </td>
                        <td height="1" style="width:150px;">
                            <input id="nickname" type="text" name="nick" value="${nick}" readonly="readonly" size="32"/>

                            <span style="color:red">
                                <c:if test="${not empty errorMsg}">
                                    <fmt:message
                                            key="${errorMsg}" bundle="${error}"/>
                                </c:if>
                            </span>
                        </td>
                        <td height="1" class="default_line_td" style="width:350px;">
                            <c:forEach items="${names}" var="name">
                                <a href="javascript:username('${name}')">${name}</a>&nbsp;&nbsp;&nbsp;
                            </c:forEach>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="150">
                            所属圈子:
                        </td>
                        <td height="1">
                            <select name="groupid">
                                <option value="">请选择</option>
                                <c:forEach items="${animetags}" var="item">
                                    <option value="${item.tag_id}">${item.tag_name}</option>
                                </c:forEach>
                            </select>
                        </td>
                        <td height="1" class=>

                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="150">
                            图片:
                        </td>
                        <td>

                            <img id="menu_pic"  <c:choose>
                                <c:when test="${not empty pic}">
                                    src="${pic}"
                                </c:when>
                                <c:otherwise>
                                    src="/static/images/default.jpg"
                                </c:otherwise>
                            </c:choose> />
                            <a href="javascript;;" id="upload"></a>
                            <span id="upload_button">上传</span>
                            <span id="loading" style="display:none"><img src="/static/images/loading.gif"/></span>
                            <input id="input_menu_pic" type="hidden" name="pic" value="${pic}">
                            <span style="color:red">*图片大小请压缩在1MB以下</span>
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