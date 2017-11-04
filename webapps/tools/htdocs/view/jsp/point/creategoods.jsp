<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>商品管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>

    <script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/goodshandler.js"></script>

    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/dhtmlxcalendar.css">
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/skins/dhtmlxcalendar_dhx_skyblue.css">
    <script src="/static/include/dhtmlxcalendar/dhtmlxcalendar.js"></script>
    <script>
        function textAreaVal(textarea_id, input_id1, input_id2, select_id) {
            var input1 = document.getElementById(input_id1).value;
            var input2 = document.getElementById(input_id2).value;
            var select = document.getElementById(select_id).value;

            if (select == 0) {
                document.getElementById(textarea_id).value = input1;
            }
            if (select == 1) {
                document.getElementById(textarea_id).value = input2;
            }
        }


        $().ready(function () {
            doOnLoad();
            $('#form_submit').bind('submit', function () {

                var nameVal = $('#input_text_goodsName').val();
                if (nameVal.length == 0) {
                    alert('请填写商品名称');
                    return false;
                }

                var typeVal = $('#select_goodstype').val();
                if (typeVal.length == 0) {
                    alert('请选择商品类型');
                    return false;
                }

                var amountVal = $('#input_text_goodsAmount').val();
                if (amountVal.length == 0) {
                    alert('请填入商品数量');
                    return false;
                }

                var consumePointVal = $('#input_text_goodsConsumePoint').val();
                if (consumePointVal.length == 0) {
                    alert('请填入商品消费的积分');
                    return false;
                }

                var consumeTypeVal = $('#select_consumetimestype').val();
                if (consumeTypeVal.length == 0) {
                    alert('请选择消费次数类型');
                    return false;
                }

            });
            var coustomSwfu = new SWFUpload(coustomImageSettings);
        });

        var myCalendar;
        function doOnLoad() {
            myCalendar = new dhtmlXCalendarObject(["input_expiredate"]);
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
            debug: false
        }

    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 条目管理 >> 商品管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">添加商品</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/point/goods/create" method="post" id="form_submit">
                <table width="90%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" class="">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            商品名称:
                        </td>
                        <td height="1" class="" width="10">
                            <input type="text" name="goodsname" size="20" id="input_text_goodsName"/>
                        </td>
                        <td height="1" align="left">*必填项</td>
                    </tr>
                    <tr>
                        <td>
                            <c:if test="${fn:length(nameExist)>0}">
                                <span style="color: red" id="span_name_exist"><fmt:message key="${nameExist}"
                                                                                           bundle="${def}"/></span>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            商品描述:
                        </td>
                        <td height="1" class="">
                            <textarea name="goodsdesc" id="textarea_goodsDesc" rows="5" cols="100"
                                      style="width: 100%;float: left" class="default_input_multiline"></textarea>
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            商品图片:
                        </td>
                        <td height="1">
                            <img id="img_pic" height="100" width="100" src="/static/images/default.jpg"/>
                            <span id="upload_button">上传</span>
                            <span id="loading" style="display:none"><img src="/static/images/loading.gif"/></span>
                            <input id="input_hid_goodsPic" type="hidden" name="goodspic" value="${goodsPic}">
                            *图片分辨率为164x80，请裁剪后上传
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="200px">
                            选择商品类型:
                        </td>
                        <td height="1">
                            <select name="goodstype" id="select_goodstype"
                                    onchange="textAreaVal('textarea_notice','hide_gTemplate', 'hide_vTemplate', 'select_goodstype')">
                                <option value="">请选择</option>
                                <c:forEach var="goodstype" items="${goodstypecollection}">
                                    <option value="${goodstype.code}">
                                        <fmt:message key="goods.type.${goodstype.code}" bundle="${def}"/></option>
                                </c:forEach>
                            </select>
                        </td>
                        <td height="1">*必填项</td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            消息通知:
                        </td>
                        <td height="1" class="">
                            <textarea name="noticebody" id="textarea_notice" rows="5" cols="100"
                                      style="width: 100%;float: left" class="default_input_multiline"></textarea>
                        </td>
                        <input type="hidden" id="hide_gTemplate" value="<c:out value="${gTemplate}"/>"/>
                        <input type="hidden" id="hide_vTemplate" value="<c:out value="${vTemplate}"/>"/>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            领取礼品后分享内容:
                        </td>
                        <td height="1">
                            <select name="shardid" id="select_share_base_info">
                                <option value="">请选择</option>
                                <c:forEach var="baseinfo" items="${baseInfoList}">
                                    <option value="${baseinfo.shareId}">${baseinfo.shareKey}</option>
                                </c:forEach>
                            </select>
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            详情的URL:
                        </td>
                        <td height="1" class="">
                            <input type="text" name="detailurl" id="input_detail_url" style="width: 80%;"/>格式：http://.....
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            商品总量:
                        </td>
                        <td height="1" class="">
                            <input type="text" name="goodsamount" size="20" id="input_text_goodsAmount"/>
                        </td>
                        <td height="1">*必填项</td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            消费积分:
                        </td>
                        <td height="1">
                            <input type="text" name="goodsconsumepoint" size="20" id="input_text_goodsConsumePoint"/>
                        </td>
                        <td height="1">*必填项</td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="200px">
                            消费次数类型:
                        </td>
                        <td height="1">
                            <select name="consumetimestype" id="select_consumetimestype">
                                <option value="">请选择</option>
                                <c:forEach var="consumetimestype" items="${consumetimestypecollection}">
                                    <option value="${consumetimestype.code}">
                                        <fmt:message key="consumetimes.type.${consumetimestype.code}"
                                                     bundle="${def}"/></option>
                                </c:forEach>
                            </select>
                        </td>
                        <td height="1">*必填项</td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="200px">
                            是否新品:
                        </td>
                        <td height="1">
                            <select name="isnew">
                                <option value="true">是</option>
                                <option value="false">否</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="200px">
                            是否热门:
                        </td>
                        <td height="1">
                            <select name="ishot">
                                <option value="false">否</option>
                                <option value="true">是</option>
                            </select>
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