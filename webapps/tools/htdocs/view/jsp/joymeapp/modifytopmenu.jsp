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
        $().ready(function () {
            var coustomSwfu = new SWFUpload(coustomImageSettings);
            var coustomSwfu2 = new SWFUpload(coustomImageSettings2);
            var index = Number(1);
            $('iframe').each(function () {
                index = index + 1;
            });
            $('#input_add_channel_menu').click(function () {
                $('#table_add_channel_button').before('<iframe src="/joymeapp/channeltopmenu/createpage" id="iframe" scrolling="no" marginheight="0" width="100%" frameborder="no" onload="Javascript:SetWinHeight(this)"></iframe>');
                $('#iframe').attr('id', 'iframe_channel_' + index).attr('name', 'iframe_channel_' + index);
                index = index + 1;
            });
        });

        function SetWinHeight(obj) {
            var win = obj;
            if (document.getElementById) {
                if (win && !window.opera) {
                    if (win.contentDocument && win.contentDocument.body.offsetHeight)
                        win.height = win.contentDocument.body.offsetHeight;
                    else if (win.Document && win.Document.body.scrollHeight)
                        win.height = win.Document.body.scrollHeight;
                }
            }
        }

        function reLoad() {
            $('iframe').each(function () {
                SetWinHeight(this);
            });
        }
        window.setInterval("reLoad()", 200);

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

        function isNotNull() {
            var menuName = $("#input_text_menuName").val();
            var picurl = $("#input_menu_pic").val();
            var menuType = $("#menuType").val();
            var picurl2 = $("#input_menu_pic2").val();
            if (menuName == '') {
                alert("菜单名称不能为空!");
                return false;
            }
            if (picurl == '') {
                alert("请选择图片");
                return false;
            }
//            if (picurl2 == '') {
//                alert('请选择一张andiord图片');
//                return false;
//            }
            if (menuType == '请选择') {
                alert("请选择菜单类型。");
                return false;
            }
            var index = Number(1);
            var result = new Array();

            var bool = true;
            $('iframe').each(function () {
                var channelCode = $('#iframe_channel_' + index).contents().find('#select_channel_code').attr('value');
                var name = $('#iframe_channel_' + index).contents().find('#input_text_name').attr('value');
                var menuUrl = $('#iframe_channel_' + index).contents().find('#input_text_mewnurl').attr('value');
                var picUrl = $('#iframe_channel_' + index).contents().find('#input_menu_pic').attr('value');
                var desc = $('#iframe_channel_' + index).contents().find('#input_text_desc').attr('value');
                var platform = $('#iframe_channel_' + index).contents().find('#select_platform').attr('value');
                if (channelCode.length > 0) {
                    if (name.length == 0) {
                        alert("请填写第" + index + "个渠道菜单名称");
                        bool = false;
                        return;
                    }
                    if (menuUrl.length == 0) {
                        alert("请填写第" + index + "个渠道菜单地址");
                        bool = false;
                        return;
                    }
                    if (picUrl.length == 0) {
                        alert("请上传第" + index + "个渠道图片");
                        bool = false;
                        return;
                    }
                    if (desc.length == 0) {
                        alert("请填写第" + index + "个渠道描述");
                        bool = false;
                        return;
                    }
                    if (platform.length == 0) {
                        alert("请选择第" + index + "个渠道平台");
                        bool = false;
                        return;
                    }
                    var obj = {
                        'channelCode': channelCode,
                        'platform': platform,
                        'name': name,
                        'desc': desc,
                        'url': menuUrl,
                        'picUrl': picUrl
                    };
                    result.push(JSON.stringify(obj));
                    index = index + 1;
                }
            });
            if (!bool) {
                return false;
            }
            $('#input_channel_top_menu').attr('value', result.join('@'));
            var value = $('#input_channel_top_menu').attr('value');
        }
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷APP >> APP轮播图</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">编辑轮播图</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/joymeapp/topmenu/modify" method="post" id="form_submit" onsubmit="return isNotNull();">
                <input type="hidden" name="menuid" value="${joymeAppTopMenu.menuId}"/>
                <input type="hidden" name="appkey" value="${appkey}"/>
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            菜单名称:
                        </td>
                        <td height="1">
                            <input id="input_text_menuName" type="text" name="menuname" size="48"
                                   value="${joymeAppTopMenu.menuName}"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            菜单URL:
                        </td>
                        <td height="1">
                            <input id="input_text_url" type="text" name="url" size="48" value="${joymeAppTopMenu.url}"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            游戏资料库ID:
                        </td>
                        <td>
                            <input type="text" name="gameid" value="${joymeAppTopMenu.gameId}"/><span
                                style="color: #ff0000;">*注：优酷游戏中心合作项目idfa上报，绑定的游戏</span>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            <c:choose>
                                <c:when test="${appkey eq 'default'}">
                                    图片地址PC&M
                                </c:when>
                                <c:otherwise>
                                     IOS图片地址:
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td height="1">
                            <c:choose>
                                <c:when test="${fn:length(joymeAppTopMenu.picUrl1)>0}">
                                    <img id="menu_pic" src="${joymeAppTopMenu.picUrl1}"/></c:when>
                                <c:otherwise><img id="menu_pic" src="/static/images/default.jpg"/></c:otherwise>
                            </c:choose>
                            <span id="upload_button">上传</span>
                            <span id="loading" style="display:none"><img src="/static/images/loading.gif"/></span>
                            <input id="input_menu_pic" type="hidden" name="picurl1" value="${joymeAppTopMenu.picUrl1}">
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <c:if test="${appkey ne 'default'}">
                        <tr>
                            <td height="1" class="default_line_td" width="100">
                                Android图片地址:
                            </td>
                            <td height="1">
                                <c:choose>
                                    <c:when test="${fn:length(joymeAppTopMenu.picUrl2)>0}">
                                        <img id="menu_pic2" src="${joymeAppTopMenu.picUrl2}"/></c:when>
                                    <c:otherwise><img id="menu_pic2" src="/static/images/default.jpg"/></c:otherwise>
                                </c:choose>
                                <span id="upload_button2">上传</span>
                                <span id="loading2" style="display:none"><img src="/static/images/loading.gif"/></span>
                                <input id="input_menu_pic2" type="hidden" name="picurl2"
                                       value="${joymeAppTopMenu.picUrl2}">
                            </td>
                            <td height="1" class=>
                            </td>
                        </tr>
                    </c:if>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            菜单类型:
                        </td>
                        <td height="1">
                            <select name="menutype" id="menuType">
                                <option value="请选择">请选择</option>
                                <c:forEach var="menuType" items="${menuTypes}">
                                    <option value="${menuType}"
                                            <c:if test="${menuType==joymeAppTopMenu.menuType}">selected</c:if>>
                                        <fmt:message key="joymeapp.menu.type.${menuType}" bundle="${def}"/>
                                    </option>
                                </c:forEach>
                            </select>
                            <%--<input id="input_text_menutype" type="text" name="menutype" size="32" value="${joymeAppMenu.menuType}"/>--%>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            是否是new:
                        </td>
                        <td height="1">
                            <select name="isnew" id="input_text_isnew">
                                <option value="true"
                                        <c:if test="${joymeAppTopMenu.isNew()==true}">selected</c:if>>true
                                </option>
                                <option value="false"
                                        <c:if test="${joymeAppTopMenu.isNew()==false}">selected</c:if>>
                                    false
                                </option>
                            </select>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            是否是hot:
                        </td>
                        <td height="1">
                            <select name="ishot" id="input_text_ishot">
                                <option value="true"
                                        <c:if test="${joymeAppTopMenu.isHot()==true}">selected</c:if> >true
                                </option>
                                <option value="false"
                                        <c:if test="${joymeAppTopMenu.isHot()==false}">selected</c:if>>
                                    false
                                </option>
                            </select>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                </table>
                <c:if test="${joymeAppTopMenu.channelTopMenuSet!=null && !empty joymeAppTopMenu.channelTopMenuSet && joymeAppTopMenu.channelTopMenuSet.channelTopMenuSet != null && !empty joymeAppTopMenu.channelTopMenuSet.channelTopMenuSet}">
                    <c:forEach items="${joymeAppTopMenu.channelTopMenuSet.channelTopMenuSet}" var="channeltopmenu"
                               varStatus="st">
                        <iframe src="/joymeapp/channeltopmenu/modifypage?channelcode=${channeltopmenu.channelCode}&menuurl=${channeltopmenu.url}&name=${channeltopmenu.name}&picurl=${channeltopmenu.picUrl}&desc=${channeltopmenu.desc}&platform=${channeltopmenu.platform}"
                                id="iframe_channel_${st.index+1}" name="iframe_channel_${st.index+1}" scrolling="no"
                                marginheight="0" frameSpacing="0"
                                width="100%" frameborder="no" onload="Javascript:SetWinHeight(this)"></iframe>
                    </c:forEach>
                </c:if>
                <table width="100%" border="0" cellspacing="1" cellpadding="0" id="table_add_channel_button">
                    <tr align="center">
                        <td height="1">
                            <input type="hidden" id="input_channel_top_menu" name="channeltopmenu"/>
                            <input type="button" id="input_add_channel_menu" class="default_button" name="button"
                                   value="增加渠道轮播图"/>
                        </td>
                    </tr>
                </table>
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
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