<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
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
    <script type="text/javascript" src="/static/include/swfupload/joymeappmenuhandler.js"></script>
    <script>
        $().ready(function() {
            $("#codeselect").change(function() {
                $("#wikinum").val("");
                $("#title").val("");
                var p1 = $(this).children('option:selected').val();
                if (p1 == '1') {
                    $("#titleid").html("wiki页面数量");
                    $("#wikinum").css("display", "block");
                    $("#title").css("display", "none");
                } else if (p1 == '0' || p1 == '2') {
                    $("#titleid").html("标题");
                    $("#title").css("display", "block");
                    $("#wikinum").css("display", "none");
                } else {
                    $("#titleid").html("");
                    $("#wikinum").css("display", "none");
                    $("#title").css("display", "none");
                }
            });
            $('#form_submit').bind('submit', function() {
                var menuName = $('#input_text_menuname').val();
                if (menuName.length == 0) {
                    alert("名称不能为空");
                    return false;
                }
                var link = $('#codeselect').val();
                if (link == '') {
                    alert("请选择CODE");
                    return false;
                } else {
                    if (link == '1') {
                        var wikinum = $("#wikinum").val();
                        if (wikinum == '') {
                            alert("请填写wiki数量");
                            return false;
                        }
                    } else {
                        var title = $("#title").val();
                        if (title == '') {
                            alert("请填写标题");
                            return false;
                        }
                    }
                }
                var superscript = $("#superscript").val();
                if (superscript == '') {
                    alert("请填写角标");
                    return false;
                }
                var pici = $("#input_menu_pic").val();
                if (pici == '') {
                    alert("请上传IOS图片");
                    return false;
                }

                var pica = $("#input_menu_pic2").val();
                if (pica == '') {
                    alert("请上传安卓图片");
                    return false;
                }
//                var linkUrl = $("#linkurl").val();
//                if (linkUrl == '') {
//                    alert("请填写跳转链接");
//                    return false;
//                }
                var linkType = $("$linktype").val();
                if (linkType == '') {
                    alert("请选择跳转类型");
                    return false;
                }

            });

            var coustomSwfu = new SWFUpload(coustomImageSettings);
            var coustomSwfu2 = new SWFUpload(coustomImageSettings2);
        });
        var coustomImageSettings = {
            upload_url : "${urlUpload}/json/upload/qiniu",
            post_params : {
                "at" : "joymeplatform",
                "filetype":"original"
            },

            // File Upload Settings
            file_size_limit : "2 MB",    // 2MB
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

        var coustomImageSettings2 = {
            upload_url : "${urlUpload}/json/upload/qiniu",
            post_params : {
                "at" : "joymeplatform",
                "filetype":"original"
            },

            // File Upload Settings
            file_size_limit : "2 MB",    // 2MB
            file_types : "*.jpg;*.png;*.gif",
            file_types_description : "请选择图片",
            file_queue_limit : 1,

            file_dialog_complete_handler : fileDialogComplete2,
            upload_start_handler:  uploadStart2,
            upload_success_handler : uploadSuccess2,
            upload_complete_handler : uploadComplete2,

            // Button Settings
            button_image_url : "/static/images/uploadbutton.png",
            button_placeholder_id : "upload_button2",
            button_width: 61,
            button_height: 22,
            moving_average_history_size: 40,

            // Flash Settings
            flash_url : "/static/include/swfupload/swfupload.swf",
            flash9_url : "/static/include/swfupload/swfupload_fp9.swf",

            custom_settings : {},
            // Debug Settings
            debug: false}
    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 大动漫管理 >> 大动漫首页管理</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">添加活动轮播图</td>
                </tr>
            </table>
            <form action="/anime/index/modify" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td">
                            <input type="hidden" name="id" size="48"
                                   value="${animeIndex.animeIndexId}"/>

                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            APPKEY:
                        </td>
                        <td height="1">
                            <input type="text" name="appkey" size="48" value="${animeIndex.appkey}" readonly="readonly"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            首页名称:
                        </td>
                        <td height="1">
                            <input id="input_text_menuname" type="text" name="linename" size="48"
                                   value="${animeIndex.line_name}"/>*必填项
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            请选择页面:
                        </td>
                        <td height="1">
                            <select name="code" id="codeselect">
                                <option value="">请选择</option>
                                <option value="0" <c:if test="${fn:contains(animeIndex.code, '0')}">selected</c:if>>追番
                                </option>
                                <option value="1" <c:if test="${fn:contains(animeIndex.code, '1')}">selected</c:if>>事典
                                </option>
                                <option value="2" <c:if test="${fn:contains(animeIndex.code, '2')}">selected</c:if>>海湾
                                </option>
                            </select>*请选择，<b style="color:red">注意每个页面只可存在一个</b>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            <div id="titleid">
                                <c:choose>
                                    <c:when test="${animeIndex.code=='ONEPIECE_1'}">
                                        wiki页面数量
                                    </c:when>
                                    <c:otherwise>
                                        标题
                                    </c:otherwise>
                                </c:choose>

                            </div>
                        </td>
                        <td height="1">
                            <input type="text" name="title" id="title" value="${animeIndex.title}" size="48"
                                   <c:if test="${animeIndex.code=='ONEPIECE_1'}">style="display:none;"</c:if>/>
                            <input type="text" name="wikinum" id="wikinum" value="${animeIndex.wikiPageNum}" size="48"
                                   <c:if test="${animeIndex.code!='ONEPIECE_1'}">style="display:none;"</c:if>/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            角标:
                        </td>
                        <td height="1">
                            <input type="text" name="superscript" id="superscript" value="${animeIndex.superScript}"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            图片:
                        </td>
                        <td>
                            <img id="menu_pic" src="${animeIndex.pic_url}" class="img_pic"/>
                            <span id="upload_button" class="upload_button">上传</span>
                            <span id="loading" style="display:none" class="loading"><img
                                    src="/static/images/loading.gif"/></span>
                            <input id="input_menu_pic" type="hidden" name="picurl" value="${animeIndex.pic_url}"><span style="color: red">*必填项，图片尺寸：558px * 376px</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            所属平台
                        </td>
                        <td>
                            <select name="platform">
                                <option value="">请选择</option>
                                <option value="0"
                                        <c:if test="${animeIndex.platform==0}">selected</c:if> >IOS
                                </option>
                                <option value="1" <c:if test="${animeIndex.platform==1}">selected</c:if>> android
                                </option>
                            </select>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            链接类型:
                        </td>
                        <td height="1">
                            <select name="animeredirect" id="animeredirect">
                                <option value="">请选择</option>
                                <c:forEach items="${list}" var="dto">
                                    <option value="${dto}"
                                            <c:if test="${animeIndex.animeRedirectType.code==dto}">selected</c:if> >
                                        <fmt:message key="anime.special.attr.${dto}"
                                                     bundle="${def}"/></option>
                                </c:forEach>
                            </select>*请选择
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            跳转链接\标签ID:
                        </td>
                        <td height="1">
                            <input type="text" name="linkurl" id="linkurl" value="${animeIndex.linkUrl}"/> 如果选择的是跳转<b
                                style="color:red;">追番</b>请填写<b style="color:red;">标签的ID</b>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            图片下文字:
                        </td>
                        <td height="1">
                            <input type="text" name="desc" id="desc" value="${animeIndex.desc}"/>
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