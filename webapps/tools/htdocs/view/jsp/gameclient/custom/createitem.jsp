<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
<title>新手游画报--热门轮播图和自定义榜--新增</title>
<link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="/static/include/js/default.js"></script>
<script type="text/javascript" src="/static/include/js/easyui/jquery.min.js"></script>
<script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
<script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="/static/include/swfupload/joymeappmenuhandler.js"></script>
<script type="text/javascript" src="/static/include/js/common.js"></script>
<script type="text/javascript" src="/static/include/js/easyui/jquery.easyui.min.js"></script>
<link rel="stylesheet" type="text/css" href="/static/include/js/easyui/themes/default/easyui.css"/>
<script type="text/javascript" src="/static/include/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="/static/include/js/colpick/colpick.js"></script>
<link rel="stylesheet" href="/static/include/js/colpick/colpick.css" type="text/css"/>

<style type="text/css">
    #color {
        margin: 0;
        padding: 0;
        border: 0;
        width: 110px;
        height: 20px;
        border-right: 20px solid green;
        line-height: 20px;
    }

    #author {
        margin: 0;
        padding: 0;
        border: 0;
        width: 110px;
        height: 20px;
        border-right: 20px solid green;
        line-height: 20px;
    }

</style>

<script type="text/javascript">
    $(document).ready(function (e) {

        <%--                        platform   title    tagId     lineDesc     picUrl                   category      url      redirectType   gamePublicTime    categoryColor   rate                  author
          gc_hotgameimgs热门轮播图 平台       名字               描述          图片                                  跳转地址，重定向类型，    添加时间
          gc_topicimgs  标签轮播图 平台       名字 所属标签名称  描述          图片                 小图片/iconurl   跳转地址, 重定向类型 ，   添加时间
          gc_hotgamelinks右侧链接             名字               描述                              小图片           跳转地址，重定向类型 ，   添加时间，         标签颜色，   热门下面标签的数量    热门下面标签的数量的外圈颜色
          一律使用contains 判断，防止看晕
        --%>


        $("#tagId").on("change", function () {
            var selectedTagNames = "";
            var selectedTagIds = '';
            $('#tagId :selected').each(function (i, selected) {
                selectedTagNames += $(selected).text() + ",";
                selectedTagIds += $(selected).val() + ",";
            });
            if (selectedTagNames.charAt(selectedTagNames.length - 1) == ',') {
                selectedTagNames = selectedTagNames.substr(0, selectedTagNames.length - 1);
                selectedTagIds = selectedTagIds.substr(0, selectedTagIds.length - 1);
            }

            $("#selectedTagNames").html("已选择:<b>" + selectedTagNames.replace(/\s+/g, ",") + "</b>");
            $("#tagIdSubmit").val(selectedTagIds);
        });

        //玩霸2.0.3新增是否显示露出的图片和标题

        $("#submit").click(function () {

            var lineCode = '${lineCode}';
            var title = $.trim($("#form_submit input[name='title']").val());
            var url = $.trim($("#form_submit input[name='url']").val());


            var lineDesc = $.trim($("#form_submit input[name='lineDesc']").val());
            var picUrl = $.trim($("#form_submit input[name='picUrl']").val());
            var category = $.trim($("#form_submit input[name='category']").val());
            var gamePublicTime = $('#gamePublicTime').datetimebox('getValue');
            var tagId = $.trim($("#tagId").val());
            var author = $.trim($("#author").val());
            var rate = $("#rate").val();

            if ((lineCode.indexOf('gc_hotgamelinks') > -1) && title == "") {
                alert("请填写名字,然后再点击提交!");
                return false;
            } else if (url == "") {
                alert("请填写跳转地址,然后再点击提交!");
                return false;
            } else if (gamePublicTime == '') {
                alert("请设置游戏的上市时间,然后再点击提交!");
                return false;
            } else if ((lineCode.indexOf('gc_hotgamelinks') > -1 ) && lineDesc == '') {
                alert("请填写描述,然后再点击提交!");
                return false;
            } else if ((lineCode.indexOf('gc_topicimgs') > -1 || lineCode.indexOf('gc_hotgameimgs') > -1) && picUrl == '') {
                alert("请上传图片,然后再点击提交!");
                return false;
            } else if (lineCode.indexOf('gc_topicimgs') > -1 && tagId == '') {
                alert("请至少选择一个标签，然后再提交!");
                return false;
            }  else if (rate == '') {
                alert("请填写数字");
                return false;
            } else {
                $("#form_submit").submit();
            }
        });
        var coustomSwfu = new SWFUpload(coustomImageSettings);
        var coustomSwfu2 = new SWFUpload(coustomImageSettings2);

        //颜色选择器
        $('#color').colpick({
                    layout: 'full',
                    submit: true,
                    colorScheme: 'light',
                    onChange: function (hsb, hex, rgb, el, bySetColor) {
                        $(el).css('border-color', '#' + hex);
                        // Fill the text box just if the color was set using the picker, and not the colpickSetColor function.
                        if (!bySetColor) $(el).val("#" + hex);
                    },
                    onSubmit: function (hsb, hex, rgb, el, bySetColor) {
                        $(el).css('border-color', '#' + hex);
                        // Fill the text box just if the color was set using the picker, and not the colpickSetColor function.
                        if (!bySetColor) $(el).val("#" + hex);
                        $(el).colpickHide();
                    }
                }).keyup(function () {
                    $(this).colpickSetColor(this.value);
                });

        $("#color").css('border-color', $("#color").val());  //input框右边的颜色设为正确的值
        $('#author').colpick({
                    layout: 'full',
                    submit: true,
                    colorScheme: 'light',
                    onChange: function (hsb, hex, rgb, el, bySetColor) {
                        $(el).css('border-color', '#' + hex);
                        // Fill the text box just if the color was set using the picker, and not the colpickSetColor function.
                        if (!bySetColor) $(el).val("#" + hex);
                    },
                    onSubmit: function (hsb, hex, rgb, el, bySetColor) {
                        $(el).css('border-color', '#' + hex);
                        // Fill the text box just if the color was set using the picker, and not the colpickSetColor function.
                        if (!bySetColor) $(el).val("#" + hex);
                        $(el).colpickHide();
                    }
                }).keyup(function () {
                    $(this).colpickSetColor(this.value);
                });
        $("#author").css('border-color', $("#author").val()); //input框右边的颜色设为正确的值


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

</script>

</head>
<body>

<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷手游画报管理 >> 轮播图自定义管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td"><label>新手游画报--热门轮播图和自定义榜--<b>${lineName}</b></label>
                        <span>添加一条记录</span>
                    </td>
                    <td class="list_table_header_td">

                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/gameclient/clientline/custom/itemcreate" method="post" id="form_submit">
                <table width="90%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" class="" width="150">
                            <input type="hidden" name="lineId" value="${lineId}"/>
                            <input type="hidden" name="lineCode" value="${lineCode}"/>
                            <input type="hidden" name="lineName" value="${lineName}"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="150">
                            名字:
                        </td>
                        <td height="1" width="200">
                            <input type="text" name="title" size="50"/>*
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <c:if test="${fn:contains(lineCode, 'gc_topicimgs')}">
                        <tr>
                            <td height="1" class="default_line_td" width="150">
                                所属标签名称:
                            </td>
                            <td height="1" width="200">
                                <input name="tagId" type="hidden" id="tagIdSubmit"/>
                                <select id="tagId" multiple="multiple">
                                    <c:forEach items="${tagMap}" var="item">
                                        <option value='${item.key}'>${item.value}</option>
                                    </c:forEach>
                                </select>*(按ctrl键多选,取消选择)
                                <div id="selectedTagNames"></div>
                            </td>
                            <td height="1">
                            </td>
                        </tr>
                    </c:if>
                    <c:if test="${fn:contains(lineCode, 'gc_topicimgs') || fn:contains(lineCode, 'gc_hotgameimgs')  }">
                        <tr>
                            <td height="1" class="default_line_td" width="150">
                                先择平台:
                            </td>
                            <td height="1" width="200">
                                <select name="platform">
                                    <option value="current">当前</option>
                                    <option value="all">ios和android</option>
                                </select>
                            </td>
                            <td height="1" class=>
                            </td>
                        </tr>
                    </c:if>
                    <tr>
                        <td height="1" class="default_line_td" width="150">
                            描述:
                        </td>
                        <td height="1" width="200">
                            <input type="text" name="lineDesc" size="50"/>*
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <c:if test="${fn:contains(lineCode, 'gc_topicimgs')||fn:contains(lineCode, 'gc_hotgameimgs')}">
                        <tr>
                            <td height="1" class="default_line_td" width="150">
                                图片:
                            </td>
                            <td height="1">
                                <img id="menu_pic" src="/static/images/default.jpg"/>
                                <span id="upload_button" class="upload_button">上传</span>
                                                        <span id="loading" style="display:none" class="loading"><img
                                                                src="/static/images/loading.gif"/></span>
                                <input id="input_menu_pic" type="hidden" name="picUrl"/><span style="color: red">*必填项，图片尺寸：70px * 70px</span>
                            </td>
                        </tr>
                    </c:if>
                    <c:if test="${fn:contains(lineCode, 'gc_topicimgs')||fn:contains(lineCode, 'gc_hotgamelinks')}">
                        <tr>
                            <td height="1" class="default_line_td" width="150">
                                <c:choose><c:when
                                        test="${fn:contains(lineCode, 'gc_topicimgs')}">左上角小图:</c:when><c:otherwise>iconurl</c:otherwise>
                                </c:choose>
                            </td>
                            <td height="1">
                                <img id="menu_pic2" src="/static/images/default.jpg"/>
                                <span id="upload_button2" class="upload_button">上传</span>
                            <span id="loading2" style="display:none" class="loading">
                                <img src="/static/images/loading.gif"/></span>
                                <input id="input_menu_pic2" type="hidden" name="category"><span
                                    style="color: red">  <c:choose><c:when
                                    test="${fn:contains(lineCode, 'gc_topicimgs')}">*可选项,</c:when><c:otherwise>*必填项，</c:otherwise>
                            </c:choose>图片尺寸：70px * 70px</span>
                        </tr>
                    </c:if>
                    <tr>
                        <td height="1" class="default_line_td" width="150">
                            跳转地址:
                        </td>
                        <td height="1" width="200">
                            <input name="url" size="100"/>*
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td" width="150">
                            重定向类型 :
                        </td>
                        <td height="1" width="200">
                            <select id="redirectType" name="redirectType">
                                <c:forEach items="${types}" var="item">
                                    <option value='${item.key}'>${item.key}__<fmt:message
                                            key="client.item.redirect.${item.key}" bundle="${def}"/></option>
                                </c:forEach>
                            </select> <a target="view_window"
                                         href="http://wiki.enjoyf.com/index.php?title=Gameclient_client#.E8.B7.B3.E8.BD.AC.E7.B1.BB.E5.9E.8B">使用参考</a>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="150">
                            添加时间:
                        </td>
                        <td height="1">
                            <input type="text" class="easyui-datetimebox" editable="false" id="gamePublicTime"
                                   name="gamePublicTime"/>*必填项
                        </td>
                    </tr>
                    <c:if test="${fn:contains(lineCode, 'gc_hotgamelinks')}">
                        <%--<tr>--%>
                            <%--<td height="1" class="default_line_td" width="150">--%>
                                <%--标签颜色:--%>
                            <%--</td>--%>
                            <%--<td height="1">--%>
                                <%--<input type="text" id="color" name="color" value="#000000"/>*必填项--%>
                            <%--</td>--%>
                        <%--</tr>--%>
                        <tr>
                            <td height="1" class="default_line_td" width="150">
                                热门下面标签的数量:
                            </td>
                            <td height="1">
                                <input type="text" id="rate" onkeyup="value=value.replace(/[^\d]/g,'')" name="rate"/>*必填项
                                数字
                            </td>
                        </tr>
                        <%--<tr>--%>
                            <%--<td height="1" class="default_line_td" width="150">--%>
                                <%--热门下面标签的数量的外圈颜色:--%>
                            <%--</td>--%>
                            <%--<td height="1" width="200">--%>
                                <%--<input type="text" name="author" id="author" maxlength="50" value="#000000"/>*必填项--%>
                            <%--</td>--%>
                            <%--<td height="1">--%>
                            <%--</td>--%>
                        <%--</tr>--%>
                    </c:if>
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