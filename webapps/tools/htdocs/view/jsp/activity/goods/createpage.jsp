<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head><title>Simple jsp page</title>
<script charset="utf-8" src="/static/include/kindeditor/kindeditor-min.js"></script>
<script charset="utf-8" src="/static/include/kindeditor/lang/zh_CN.js"></script>
<link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
<script type="text/javascript" src="/static/include/js/jquery.js"></script>
<script type="text/javascript" src="/static/include/js/common.js"></script>
<script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
<script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="/static/include/swfupload/joymeappmenuhandler.js"></script>
<script type="text/javascript" src="/static/include/My97DatePicker/WdatePicker.js"></script>

<script>

    var uploaddomain = '${urlUpload}';
    var at = '${at}';
    var editor;
    KindEditor.ready(function (K) {
        editor = K.create('textarea[name="content"]', {
            resizeType: 1,
            allowPreviewEmoticons: false,
            allowImageUpload: true,
            uploadJson: '/json/upload/image',
            filePostName: 'Filedata',
            extraParams: {'filetype': 'original', 'at': at},
            items: [
                'bold', 'italic', 'underline', 'image'],
            afterChange: function () {
                //  $('.word_count1').html(this.count()); //字数统计包含HTML代码
                $('.word_count2').html(this.count('text'));  //字数统计包含纯文本、IMG、EMBED，不包含换行符，IMG和EMBED算一个文字
                //////////
                //限制字数
                var limitNum = 5000;  //设定限制字数
                var pattern = '还可以输入' + limitNum + '字';
                $('.word_surplus').html(pattern); //输入显示
                if (this.count('text') > limitNum) {
                    pattern = ('字数超过限制，请适当删除部分内容');
                    //超过字数限制自动截取

                } else {
                    //计算剩余字数
                    var result = limitNum - this.count('text');
                    pattern = '还可以输入' + result + '字';
                }
                $('#word_surplus').html(pattern); //输入显示
            }
        });
    });
    $().ready(function () {
        var coustomSwfu = new SWFUpload(coustomImageSettings);
        var coustomSwfu2 = new SWFUpload(coustomImageSettings2);
        var coustomSwfu3 = new SWFUpload(coustomImageSettings3);
        $('#form_submit').bind('submit', function () {
            var subject = $("#activitySubject").val();
            var pic = $("#input_menu_pic").val();
            var desc = $("#desc").val();
            var starttime = $("#starttime").val();
            var goodsid = $("#goodsid").val();
            var jsonDesc = $("#word_surplus").text();
            var endtime = $("#endtime").val();

            if (subject == '') {
                alert('请输入标题');
                return false;
            }
            if (pic == '') {
                alert('请上传活动大图');
                return false;
            }
            if (jsonDesc == '字数超过限制，请适当删除部分内容') {
                alert('字数超过限制，请适当删除部分内容');
                return false;
            }


            $("#jsonDesc").val(editor.html());
            if (starttime == '') {
                alert('请选择开始时间');
                return false;
            }
            if (endtime == '') {
                alert('请选择结束时间');
                return false;
            }
            if (goodsid.length == 0) {
                alert('请选择商品');
                return false;
            }

            var type = Number(0);
            $("input[name = box]:checkbox").each(function () {
                if ($(this).is(":checked")) {
                    type += Number($(this).attr("value"));
                }
            });
            $('#input_hidden_goods_category').val(type);

            var caregory = Number(0);
            $("input[name='radio']").each(function () {
                if ($(this).attr("checked") == true) {
                    caregory += parseInt($(this).val());
                }
            });
            $("#caregory").val(caregory);

        });
    });
    var coustomImageSettings = {
        upload_url: "${urlUpload}/json/upload/qiniu",
        post_params: {
            "at": "joymeplatform",
            "filetype": "original"
        },

        // File Upload Settings
        file_size_limit: "2MB",    // 2MB
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
        debug: false}

</script>


</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
    <td height="22" class="page_navigation_td">>> 运营管理 >> 条目管理 >> 活动管理</td>
</tr>
<tr>
<td height="100%" valign="top"><br>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td class="list_table_header_td"><span
                style="font-size:16px; font-family: Arial, Helvetica, sans-serif; font-weight:bold;">添加活动</span>
        </td>
    </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td height="1" class="default_line_td"></td>
    </tr>
</table>
<form action="/activity/goods/create" method="post" id="form_submit">
<table width="90%" border="0" cellspacing="1" cellpadding="0">
    <tr>
        <td height="1" class="">
        </td>
    </tr>
    <tr>
        <td height="1" class="">
            <c:if test="${errorMessage!=null}">
                <span style="color:red"><fmt:message key="${errorMessage}" bundle="${def}"/></span>
            </c:if>
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
            活动标题
        </td>
        <td height="1" class="" width="10">
            <input type="text" name="activitySubject" size="20" maxlength="30" id="activitySubject"/> *必填项，最好不要超过10个字
        </td>
        <td height="1" align="left"></td>
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
            活动主图:
        </td>
        <td height="1" class="">
            <img id="menu_pic" with="320" height="160" src="/static/images/default.jpg"/>
            <span id="upload_button">上传</span>
            <span id="loading" style="display:none"><img src="/static/images/loading.gif"/></span>
            <input id="input_menu_pic" type="hidden" name="picurl1" value="">
            *必填项，图片请剪切成，164*80大小后上传，否则展示不完全。
        </td>
        <td height="1"></td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
            活动详情页背景图:
        </td>
        <td height="1" class="">
            <img id="menu_pic3" with="320" height="160" src="/static/images/default.jpg"/>
            <span id="upload_button3">上传</span>
            <span id="loading3" style="display:none"><img src="/static/images/loading.gif"/></span>
            <input id="input_menu_pic3" type="hidden" name="picurl3" value="">
            *必填项。
        </td>
        <td height="1"></td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
            活动简介:
        </td>
        <td height="1">
            <textarea name="content" id="editor_id"></textarea>
            <input type="hidden" id="jsonDesc" name="jsonDesc"/>

            <p>
                <!--您当前输入了 <span class="word_count1">0</span> 个文字。（字数统计包含HTML代码。）<br/>  -->
                您当前输入了 <span class="word_count2">0</span>
                个文字。（字数统计包含纯文本、IMG、EMBED，包含换行符，IMG和EMBED算一个文字。）如果有部分功能问题，请换一个浏览器<br>
                <span id="word_surplus"></span>
        </td>
        <td height="1"></td>
    </tr>
    <tr>
        <td height="1" class="default_line_td" width="200px">
            积分兑换的小标题描述:
        </td>
        <td height="1">
            <textarea name="subdesc" id="subdesc" cols="45" rows="8"></textarea>
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td" width="200px">
            活动开始时间:
        </td>
        <td height="1">
            <input type="text" class="Wdate"
                   onClick="WdatePicker({autoPickDate:true})"
                   readonly="readonly" name="starttime" id="starttime"/>
            <input type="hidden">
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
            活动结束时间:
        </td>
        <td height="1" class="">
            <input type="text" class="Wdate"
                   onClick="WdatePicker({autoPickDate:true})"
                   readonly="readonly" name="endtime" id="endtime"/>
        </td>

    </tr>
    <tr>
        <td height="1" class="default_line_td">
            活动关联的积分兑换商品:
        </td>
        <td height="1" class="">
            <select name="goodsid" id="goodsid">
                <option value=""></option>
                <c:forEach items="${goodsList}" var="goods">
                    <option value="${goods.goodsId}">${goods.goodsName}</option>
                </c:forEach>
            </select>*必填项
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
            主动分享ID
        </td>
        <td height="1" class="">
            <select name="activitysid" id="select_share_base_info">
                <option value="0">请选择</option>
                <c:forEach var="baseinfo" items="${baseInfoList}">
                    <option value="${baseinfo.shareId}">${baseinfo.shareKey}</option>
                </c:forEach>
            </select>
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
            搜索类型:
        </td>
        <td height="1" class="">
            <input type="hidden" name="goodscategory" id="input_hidden_goods_category"/>
            <input type="checkbox" name="box" value="1"/>精品推荐&nbsp;&nbsp;
            <input type="checkbox" name="box" value="2"/>热卖实物&nbsp;&nbsp;
            <input type="checkbox" name="box" value="4"/>最新上架&nbsp;&nbsp;
            <input type="checkbox" name="box" value="8"/>特价商品&nbsp;&nbsp;
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
            出口位置:
        </td>
        <td height="1" class="">
            <select name="goodsactiontype">
                <c:forEach items="${shopTypes}" var="item">
                    <option value="${item.code}">${item.name}</option>
                </c:forEach>
            </select>
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
            开放权限:
        </td>
        <td height="1" class="">
            <select name="reserve">
                <option value="0">可兑换</option>
                <option value="1">可预订</option>
            </select>
        </td>
    </tr>
</table>
<br/>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td><span
                style="font-size:16px; font-family: Arial, Helvetica, sans-serif; font-weight:bold;">附加游戏信息</span>
        </td>
    </tr>
    <tr>
        <td><span
                style="font-size:16px; font-family: Arial, Helvetica, sans-serif; font-weight:bold;">如果有游戏信息，请确保游戏名称，图片，类型不为空</span>
        </td>
    </tr>
</table>
<br/>
<table width="90%" border="0" cellspacing="1" cellpadding="0">
    <tr>
        <td height="1" class="">
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
            游戏名称:
        </td>
        <td height="1" class="" width="10">
            <input type="text" name="gameName" size="20" maxlength="30" id="gameName"/>
        </td>
        <td height="1" align="left"></td>
    </tr>
    <tr>
        <td>

        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
            游戏Icon:
        </td>
        <td height="1" class="">
            <img id="menu_pic2" with="320" height="160" src="/static/images/default.jpg"/>
            <span id="upload_button2">上传</span>
            <span id="loading2" style="display:none"><img src="/static/images/loading.gif"/></span>
            <input id="input_menu_pic2" type="hidden" name="picurl2" value="">图片请剪切成，96*96大小后上传，否则会出现样式错误。
        </td>
        <td height="1"></td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
            游戏类型:
        </td>
        <td height="1">
            <input type="checkbox" value="1" name="radio" id="radio1"/>卡牌&nbsp;&nbsp;
            <input type="checkbox" value="2" name="radio" id="radio2"/>RPG&nbsp;&nbsp;
            <input type="checkbox" value="4" name="radio" id="radio3"/>休闲&nbsp;&nbsp;
            <input type="checkbox" value="8" name="radio" id="radio4"/>益智&nbsp;&nbsp;
            <br/>
            <input type="checkbox" value="16" name="radio" id="radio5"/>动作&nbsp;&nbsp;
            <input type="checkbox" value="32" name="radio" id="radio6"/>射击&nbsp;&nbsp;
            <input type="checkbox" value="64" name="radio" id="radio7"/>策略&nbsp;&nbsp;
            <input type="checkbox" value="128" name="radio" id="radio8"/>角色扮演&nbsp;&nbsp;
            <br/>
            <input type="checkbox" value="256" name="radio" id="radio9"/>模拟经营&nbsp;&nbsp;
            <input type="checkbox" value="512" name="radio" id="radio10"/>体育&nbsp;&nbsp;
            <input type="checkbox" value="1024" name="radio" id="radio11"/>竞速&nbsp;&nbsp;
            <input type="checkbox" value="2048" name="radio" id="radio12"/>休闲&nbsp;&nbsp;
            <br/>
            <input type="checkbox" value="4096" name="radio" id="radio13"/>音乐&nbsp;&nbsp;
            <input type="checkbox" value="8192" name="radio" id="radio14"/>养成&nbsp;&nbsp;
            <input type="checkbox" value="16384" name="radio" id="radio15"/>棋牌&nbsp;&nbsp;
            <input type="checkbox" value="32768" name="radio" id="radio16"/>三消&nbsp;&nbsp;
            <br/>
            <input type="checkbox" value="65536" name="radio" id="radio17"/>塔防&nbsp;&nbsp;
            <input type="checkbox" value="131072" name="radio" id="radio18"/>解谜&nbsp;&nbsp;
            <input type="checkbox" value="262144" name="radio" id="radio19"/>恋爱&nbsp;&nbsp;
            <input type="hidden" value="" id="caregory" name="caregory"/>
        </td>
        <td height="1"></td>
    </tr>
    <tr>
        <td height="1" class="default_line_td" width="200px">
            发行商/开发商:
        </td>
        <td height="1">
            <input type="text" name="developer" size="20" id="developer"/>
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
            游戏专区地址:
        </td>
        <td height="1" class="">
            <input type="text" name="gameUrl" size="20" id="gameUrl"/>
            <span style="color:red;">*地址请以http://或者https://开头，谢谢</span>
        </td>

    </tr>
    <tr>
        <td height="1" class="default_line_td" width="200px">
            游戏下载地址:
        </td>
        <td height="1">
            苹果下载地址:<input type="text" name="iosUrl" size="20" id="iosUrl"/>
            <br/>
            安卓下载地址:<input type="text" name="androidUrl" size="20" id="androidUrl"/>
        </td>
        <td>
            <span style="color:red;">*地址请以http://或者https://开头，谢谢</span>
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