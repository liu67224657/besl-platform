<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head><title>热门页自定义楼层--修改某个楼层</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/gameclienthotfloorhandler.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.easyui.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/static/include/js/easyui/themes/default/easyui.css"/>
    <script type="text/javascript" src="/static/include/js/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript">
        $(document).ready(function (e) {



            var desc = '${item.desc}';
            if (desc != '') {
                var jsonObj = jQuery.parseJSON(desc);
                $("#gameDBId").val(jsonObj.gameDBId);
                $("#form_submit input[name=floorName]").val(jsonObj.floorName);
                $("#menu_pic").attr("src", jsonObj.floorIcon);
                $("#input_menu_pic").val(jsonObj.floorIcon);

                $("#menu_pic2").attr("src", jsonObj.pic1st);
                $("#input_menu_pic2").val(jsonObj.pic1st);

                $("#form_submit select[name=jt1]").val(jsonObj.jt1);
                $("#form_submit input[name=ji1]").val(jsonObj.ji1);

                $("#menu_pic3").attr("src", jsonObj.pic2nd);
                $("#input_menu_pic3").val(jsonObj.pic2nd);
                $("#form_submit select[name=jt2]").val(jsonObj.jt2);
                $("#form_submit input[name=ji2]").val(jsonObj.ji2);
                $("#menu_pic4").attr("src", jsonObj.pic3rd);
                $("#input_menu_pic4").val(jsonObj.pic3rd);
                $("#form_submit select[name=jt3]").val(jsonObj.jt3);
                $("#form_submit input[name=ji3]").val(jsonObj.ji3);

                $("#form_submit select[name=moreLink]").val(jsonObj.moreLink);
                $("#form_submit select[name=jt]").val(jsonObj.jt);
                $("#form_submit input[name=ji]").val(jsonObj.ji);
            }


            $("#tr_ji").hide();
            $("#tr_jt").hide();
            $("#moreLink").on("change", function () {
                var value = $("#moreLink").val();
                if (value == '1') {
                    $("#tr_ji").show();
                    $("#tr_jt").show();
                } else {
                    $("#tr_ji").hide();
                    $("#tr_jt").hide();
                }
            });
            $("#moreLink").change();


             //页面表单 提交
            $("#submit").click(function () {
                var floorName = $.trim($("#form_submit input[name=floorName]").val());
                var floorIcon = $.trim($("#form_submit input[name=floorIcon]").val());
                var pic1st = $.trim($("#form_submit input[name=pic1st]").val());
                var pic2nd = $.trim($("#form_submit input[name=pic2nd]").val());
                var pic3rd = $.trim($("#form_submit input[name=pic3rd]").val());

                if (floorName == "") {
                    alert("请设置楼层名称,然后再点击提交!");
                    return false;
                } else if (floorIcon == '') {
                    alert("请设置楼层icon,然后再点击提交!");
                    return false;
                } else if (pic1st == '') {
                    alert("请设置第一个图片,然后再点击提交!");
                    return false;
                } else if (pic2nd == '') {
                    alert("请设置第二个图片,然后再点击提交!");
                    return false;
                } else if (pic3rd == '') {
                    alert("请设置第三个图片,然后再点击提交!");
                    return false;
                } else {
                    $("#form_submit").submit();
                }
            });

            var coustomSwfu = new SWFUpload(coustomImageSettings);
            var coustomSwfu2 = new SWFUpload(coustomImageSettings2);
            var coustomSwfu3 = new SWFUpload(coustomImageSettings3);
            var coustomSwfu4 = new SWFUpload(coustomImageSettings4);
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
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷手游画报管理 >> 热门页自定义楼层管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td"><span
                            style="font-size:13px; ">着迷玩霸--${lineName}(${lineCode})修改一条记录</span>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/gameclient/clientline/hotfloor/itemmodify" method="post" id="form_submit">
                <table width="90%" border="0" cellspacing="2" cellpadding="2">
                    <tr>
                        <td height="1" width="240">
                            <input type="hidden" name="lineId" value="${lineId}"/>
                            <input type="hidden" name="lineCode" value="${lineCode}"/>
                            <input type="hidden" name="lineName" value="${lineName}"/>
                            <input type="hidden" name="itemId" value="${itemId}"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            楼层名称：
                        </td>
                        <td height="1">
                            <input type="text" size="50" name="floorName"> </input>*
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="150">
                            楼层icon:
                        </td>
                        <td height="1">
                            <img id="menu_pic" src="/static/images/default.jpg"/>
                            <span id="upload_button" class="upload_button">上传</span>
                                                                         <span id="loading" style="display:none"
                                                                               class="loading"><img
                                                                                 src="/static/images/loading.gif"/></span>
                            <input id="input_menu_pic" type="hidden" name="floorIcon"/><span
                                style="color: red">*必填项</span>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="150">
                            图片1
                        </td>
                        <td height="1">
                            <img id="menu_pic2" src="/static/images/default.jpg"/>
                            <span id="upload_button2" class="upload_button">上传</span>
                               <span id="loading2" style="display:none" class="loading">
                                <img src="/static/images/loading.gif"/></span>
                            <input id="input_menu_pic2" type="hidden" name="pic1st"><span
                                style="color: red"> *必填项</span>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="150">
                            跳转类型1 :
                        </td>
                        <td height="1" width="200">
                            <select name="jt1">
                                <c:forEach items="${types}" var="item">
                                    <option value='${item.key}'>${item.key}__<fmt:message
                                            key="client.item.redirect.${item.key}" bundle="${def}"/></option>
                                </c:forEach>
                            </select> <a target="view_window"
                                         href="http://wiki.enjoyf.com/index.php?title=Gameclient_client#.E8.B7.B3.E8.BD.AC.E7.B1.BB.E5.9E.8B">使用参考</a>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            跳转地址1:
                        </td>
                        <td height="1">
                            <input type="text" name="ji1" size="50"> </input>*
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="150">
                            图片2
                        </td>
                        <td height="1">
                            <img id="menu_pic3" src="/static/images/default.jpg"/>
                            <span id="upload_button3" class="upload_button">上传</span>
                                                   <span id="loading3" style="display:none" class="loading">
                                                    <img src="/static/images/loading.gif"/></span>
                            <input id="input_menu_pic3" type="hidden" name="pic2nd"><span
                                style="color: red"> *必填项</span>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="150">
                            跳转类型2 :
                        </td>
                        <td height="1" width="200">
                            <select name="jt2">
                                <c:forEach items="${types}" var="item">
                                    <option value='${item.key}'>${item.key}__<fmt:message
                                            key="client.item.redirect.${item.key}" bundle="${def}"/></option>
                                </c:forEach>
                            </select> <a target="view_window"
                                         href="http://wiki.enjoyf.com/index.php?title=Gameclient_client#.E8.B7.B3.E8.BD.AC.E7.B1.BB.E5.9E.8B">使用参考</a>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            跳转地址2:
                        </td>
                        <td height="1">
                            <input type="text" name="ji2" size="50"> </input>*
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="150">
                            图片3
                        </td>
                        <td height="1">
                            <img id="menu_pic4" src="/static/images/default.jpg"/>
                            <span id="upload_button4" class="upload_button">上传</span>
                                                                   <span id="loading4" style="display:none"
                                                                         class="loading">
                                                                    <img src="/static/images/loading.gif"/></span>
                            <input id="input_menu_pic4" type="hidden" name="pic3rd"><span
                                style="color: red"> *必填项</span>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="150">
                            跳转类型3 :
                        </td>
                        <td height="1" width="200">
                            <select name="jt3">
                                <c:forEach items="${types}" var="item">
                                    <option value='${item.key}'>${item.key}__<fmt:message
                                            key="client.item.redirect.${item.key}" bundle="${def}"/></option>
                                </c:forEach>
                            </select> <a target="view_window"
                                         href="http://wiki.enjoyf.com/index.php?title=Gameclient_client#.E8.B7.B3.E8.BD.AC.E7.B1.BB.E5.9E.8B">使用参考</a>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            跳转地址3:
                        </td>
                        <td height="1">
                            <input type="text" name="ji3" size="50"> </input>*
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            是否有"更多":
                        </td>
                        <td height="1">
                            <select name="moreLink" id="moreLink">
                                <option value="0">没有</option>
                                <option value="1">有</option>
                            </select>
                        </td>
                    </tr>
                    <tr id="tr_jt">
                        <td height="1" class="default_line_td" width="150">
                            跳转类型 :
                        </td>
                        <td height="1" width="200">
                            <select id="jt" name="jt">
                                <c:forEach items="${types}" var="item">
                                    <option value='${item.key}'>${item.key}__<fmt:message
                                            key="client.item.redirect.${item.key}" bundle="${def}"/></option>
                                </c:forEach>
                            </select> <a target="view_window"
                                         href="http://wiki.enjoyf.com/index.php?title=Gameclient_client#.E8.B7.B3.E8.BD.AC.E7.B1.BB.E5.9E.8B">使用参考</a>
                        </td>
                    </tr>

                    <tr id="tr_ji">
                        <td height="1" class="default_line_td">
                            跳转地址:
                        </td>
                        <td height="1">
                            <input type="text" name="ji" size="50"> </input>*
                        </td>
                    </tr>
                </table>
                <table width="90%" border="0" cellspacing="1" cellpadding="0">
                    <tr align="center">
                        <td colspan="3">
                            <input type="submit" id="submit" class="default_button" value="提交">
                            <input type="button" class="default_button" value="返回"
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