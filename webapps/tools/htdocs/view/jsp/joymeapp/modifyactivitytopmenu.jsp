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
    <script type="text/javascript" src="/static/include/swfupload/clientlineitemhandler.js"></script>
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/dhtmlxcalendar.css">
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/skins/dhtmlxcalendar_dhx_skyblue.css">
    <script src="/static/include/dhtmlxcalendar/dhtmlxcalendar.js"></script>
    <script>
        $(document).ready(function () {
            doOnLoad();
            $('#form_submit').bind('submit', function () {
                var menuName = $('#input_text_menuname').val();
                if (menuName.length == 0) {
                    alert("请填写菜单名称");
                    return false;
                }
                var link = $('#input_text_link').val();
                if (link.length == 0) {
                    alert("请填写菜单URL");
                    return false;
                }
                var menuType = $('#select_menutype').val();
                if (menuType.length == 0) {
                    alert("请选择菜单类型");
                    return false;
                }
                var picUrl = $('#input_menu_pic').val();
                if (picUrl.length == 0) {
                    alert("请上传图片");
                    return false;
                }
                var desc = $('#input_text_desc').val();
                if (desc.length == 0) {
                    alert("请填写描述");
                    return false;
                }
                var platform = $('#select_platform').val();
                if (platform.length == 0) {
                    alert("请选择平台");
                    return false;
                }
            });
            var coustomSwfu = new SWFUpload(coustomImageSettings);
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

        var myCalendar;
        function doOnLoad() {
            myCalendar = new dhtmlXCalendarObject(["publishdate"]);
        }
    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷APP >> 活动轮播图管理</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">APP顶部菜单添加</td>
                </tr>
            </table>
            <form action="/joymeapp/activitytopmenu/modify" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td">
                            <input id="input_hidden_id" type="hidden" name="activitytopmenuid" size="48"
                                   value="${activityTopMenu.activityTopMenuId}"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            菜单名称:
                        </td>
                        <td height="1">
                            <input id="input_text_menuname" type="text" name="menuname" size="48"
                                   value="${activityTopMenu.menuName}"/>*必填项
                            <span style="color: #ff0000;">*注：手游画报轮播图，如果是文章，填写marticle文章标题</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            菜单URL:
                        </td>
                        <td height="1">
                            <input id="input_text_link" type="text" name="linkurl" size="48"
                                   value="${activityTopMenu.linkUrl}"/>*必填项
                            <span style="color: #ff0000;">*注：手游画报轮播图，如果是文章，填写marticle文章地址</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            菜单类型:
                        </td>
                        <td height="1">
                            <select name="menutype" id="select_menutype">
                                <option value="">请选择</option>
                                <option value="0"
                                <c:if test="${activityTopMenu.menuType==0}">selected="selected"</c:if>>
                                <fmt:message key="joymeapp.menu.type.0" bundle="${def}"/></option>
                                <option value="1"
                                <c:if test="${activityTopMenu.menuType==1}">selected="selected"</c:if>>
                                <fmt:message key="joymeapp.menu.type.1" bundle="${def}"/></option>
                            </select>*必填项
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            跳转地址:
                        </td>
                        <td height="1">
                            <select name="redirect" id="select_redirecttype">
                                <option value="-1">请选择</option>
                                <c:forEach items="${redirectTypes}" var="re">
                                    <option value="${re.code}"
                                    <c:if
                                            test="${re.code == activityTopMenu.redirectType}">selected="selected"</c:if></option>
                                    <fmt:message
                                            key="client.item.redirect.${re.code}" bundle="${def}"/></option>
                                </c:forEach>
                            </select><span style="color: #ff0000;">*注：着迷大端的礼包中心轮播图，手游画报轮播图，此项必选</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            游戏资料库ID:
                        </td>
                        <td>
                            <input type="text" name="gameid" value="${activityTopMenu.param.gameId}"/><span style="color: #ff0000;">*注：优酷游戏中心合作项目idfa上报，绑定的游戏</span>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            图片:
                        </td>
                        <td>
                            <img id="menu_pic" src="${activityTopMenu.picUrl}" class="img_pic"/>
                            <span id="upload_button" class="upload_button">上传</span>
                            <span id="loading" style="display:none" class="loading"><img
                                    src="/static/images/loading.gif"/></span>
                            <input id="input_menu_pic" type="hidden" name="picurl" value="${activityTopMenu.picUrl}">*必填项
                            <span style="color: #ff0000;">*注：手游画报轮播图，如果是文章，上传marticle文章图片（根据情况选择图片，不做死要求,注意尺寸）</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            描述:
                        </td>
                        <td height="1">
        <textarea id="input_text_desc" type="text" name="menudesc"
                  style="height: 100px;width: 300px">${activityTopMenu.menuDesc}</textarea>*必填项
                            <span style="color: #ff0000;">*注：手游画报轮播图，如果是文章，填写marticle文章简介</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            作者:
                        </td>
                        <td height="1">
                            <input type="text" name="author" size="48" value="${activityTopMenu.param.author}"/>
                            <span style="color: #ff0000;">*注：手游画报轮播图，如果是文章，填写marticle文章作者</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            文章发布时间:
                        </td>
                        <td height="1">
                            <input type="text" class="default_input_singleline" size="16" maxlength="20"
                                   name="publishdate" id="publishdate"
                                   value="${activityTopMenu.param.publishDate}"/>
                            <span style="color: #ff0000;">*注：手游画报轮播图，如果是文章，填写marticle文章发布时间</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            APP:
                        </td>
                        <td height="1">
                            <select name="appkey" id="select_appkey">
                                <option value="">请选择</option>
                                <c:forEach items="${appList}" var="app">
                                    <option value="${app.appId}"
                                    <c:if test="${activityTopMenu.appKey==app.appId}">selected="selected"</c:if>>${app.appName}</option>
                                </c:forEach>
                            </select>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            平台:
                        </td>
                        <td height="1">
                            <select name="platform" id="select_platform">
                                <option value="" selected="selected">请选择</option>
                                <option value="0"
                                <c:if test="${activityTopMenu.platform==0}">selected="selected"</c:if>>
                                <fmt:message key="joymeapp.platform.0" bundle="${def}"/></option>
                                <option value="1"
                                <c:if test="${activityTopMenu.platform==1}">selected="selected"</c:if>>
                                <fmt:message key="joymeapp.platform.1" bundle="${def}"/></option>
                            </select>*必填项
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            渠道:
                        </td>
                        <td height="1">
                            <select name="channelid" id="select_channel">
                                <option value="">请选择</option>
                                <c:forEach items="${channelList}" var="channel">
                                    <option value="${channel.channelId}"
                                    <c:if test="${activityTopMenu.channelId==channel.channelId}">selected="selected"</c:if>>${channel.channelName}</option>
                                </c:forEach>
                            </select>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            是否是new:
                        </td>
                        <td height="1">
                            <select name="isnew" id="select_new">
                                <option value="false">否</option>
                                <option value="true"
                                <c:if test="${activityTopMenu.isNew}">selected="selected"</c:if>>
                                是
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
                            <select name="ishot" id="select_hot">
                                <option value="false">否</option>
                                <option value="true"
                                <c:if test="${activityTopMenu.isHot}">selected="selected"</c:if>>
                                是
                                </option>
                            </select>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
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