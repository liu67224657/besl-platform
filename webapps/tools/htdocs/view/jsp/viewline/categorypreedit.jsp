<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<%@ taglib prefix="bit" uri="/WEB-INF/tags/bitwise.tld" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/viewlineitemhandler.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.Jcrop.js"></script>
    <link type="text/css" rel="stylesheet" href="/static/include/css/jquery.Jcrop.css"/>

    <title>后台数据管理,分类管理,修改分类</title>

    <script language="JavaScript" type="text/JavaScript">
        function init() {
            var tex = document.getElementById("subject").value;
            var nun = tex.length;
            var spa = document.getElementById('subjectspan');
            spa.innerHTML = nun;
        }
    </script>
</head>

<body onload="init();">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
    <td height="22" class="page_navigation_td">>> 运营维护 >> <a
            href="/viewline/categorylist?aspectCode=${category.categoryAspect.code}">分类管理</a> >> 修改分类
    </td>
</tr>
<tr>
<td height="100%" valign="top"><br>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td class="list_table_header_td">修改分类</td>
    </tr>
    <tr>
        <td height="1" class="default_line_td"></td>
    </tr>
</table>
<c:if test="${errorMsgMap['system']!=null}">
    <table width="100%" border="0" cellspacing="1" cellpadding="0">
        <tr>
            <td class="error_msg_td">
                <fmt:message key="${errorMsgMap['system']}" bundle="${error}"/>
            </td>
        </tr>
        <tr>
            <td height="1" class="default_line_td"></td>
        </tr>
    </table>
</c:if>

<form action="/viewline/categoryedit" method="POST">
    <table width="100%" border="0" cellspacing="1" cellpadding="0">
        <input name="categoryId" type="hidden" value="${category.categoryId}"/>
        <input name="aspectCode" type="hidden" value="${category.categoryAspect.code}"/>
        <c:if test="${category.parentCategory ne null}">
            <input name="parentCategoryId" type="hidden" value="${category.parentCategory.categoryId}"/>
        </c:if>
        <tr>
            <td width="120" align="right" class="edit_table_notnulltitle_td">分类角度：</td>
            <td class="edit_table_value_td">
                <fmt:message key="def.viewline.category.aspect.${category.categoryAspect.code}.name" bundle="${def}"/>
            </td>
        </tr>
        <tr>
            <td width="120" align="right" class="edit_table_notnulltitle_td">上级分类：</td>
            <td class="edit_table_value_td">
                ${category.parentCategory.categoryName}
            </td>
        </tr>
        <tr>
            <td height="1" colspan="2" class="default_line_td"></td>
        </tr>
        <tr>
            <td width="120" align="right" class="edit_table_notnulltitle_td">分类编码：</td>
            <td class="edit_table_value_td">
                <c:if test="${errorMsgMap['categoryCode']!=null}">
                    <span class="error_msg_td"><fmt:message key="${errorMsgMap['categoryCode']}"
                                                            bundle="${error}"/></span><br>
                </c:if>
                <input name="categoryCode" type="text" class="default_input_singleline" id="categoryCode"
                       value="${category.categoryCode}" size="32" maxlength="32" title="不能重复的英文或拼音短句"> * 只能含有英文字母、数字。
            </td>
        </tr>
        <tr>
            <td width="120" align="right" class="edit_table_notnulltitle_td">分类名称：</td>
            <td nowrap class="edit_table_value_td">
                <c:if test="${errorMsgMap['categoryName']!=null}">
                    <span class="error_msg_td"><fmt:message key="${errorMsgMap['categoryName']}"
                                                            bundle="${error}"/></span><br>
                </c:if>
                <input name="categoryName" type="text" class="default_input_singleline" size="32"
                       maxlength="32" value="${category.categoryName}"
                       id="categoryName"> * 分类中文名称。
            </td>
        </tr>
        <tr>
            <td width="120" align="right" class="edit_table_notnulltitle_td">Location Code：</td>
            <td nowrap class="edit_table_value_td">
                <input name="locationCode" type="text" class="default_input_singleline" size="32"
                       maxlength="32" value="${category.locationCode}"> * (注意：此项由技术人员填写，非技术人员禁止随意改动)
            </td>
        </tr>
        <tr>
            <td height="1" colspan="2" class="default_line_td"></td>
        </tr>
        <tr>
            <td width="120" align="right" class="edit_table_defaulttitle_td">SEO关键字：</td>
            <td nowrap class="edit_table_value_td">
                <input name="seoKeyWord" type="text" class="default_input_singleline" size="64"
                       maxlength="128" value="${category.seoKeyWord}"
                       id="seoKeyWord">
            </td>
        </tr>
        <tr>
            <td width="120" align="right" class="edit_table_defaulttitle_td">SEO描述：</td>
            <td nowrap class="edit_table_value_td">
                <textarea rows="5" cols="100" name="seoDesc"
                          class="default_input_singleline">${category.seoDesc}</textarea>
            </td>
        </tr>
        <tr>
            <td height="1" colspan="2" class="default_line_td"></td>
        </tr>
        <tr>
            <td width="120" align="right" class="edit_table_defaulttitle_td">分类描述：</td>
            <td nowrap class="edit_table_value_td">
                <textarea rows="5" cols="100" name="categoryDesc"
                          class="default_input_singleline">${category.categoryDesc}</textarea>
            </td>
        </tr>
        <tr>
            <td width="120" align="right" class="edit_table_defaulttitle_td">排序字段：</td>
            <td nowrap class="edit_table_value_td">
                <input name="displayOrder" type="text" class="default_input_singleline" size="10"
                       maxlength="32" value="${category.displayOrder}">
            </td>
        </tr>
        <tr>
            <td width="120" align="right" class="edit_table_defaulttitle_td">设置显示信息：</td>
            <td class="edit_table_value_td"><input type="checkbox" name="checkbox" value="1"
                                                   onClick="hideSpan(document.getElementById('moduleView'),this.checked)">
                [显示/隐藏]
            </td>
        </tr>
        <tr>
            <td height="1" colspan="2" class="default_line_td"></td>
        </tr>
    </table>
                <span id="moduleView">
                    <table width="100%" border="0" cellspacing="1" cellpadding="0">
                        <tr>
                            <td width="120" align="right" class="edit_table_defaulttitle_td">图标：</td>
                            <td nowrap class="edit_table_value_td">
                                <div>
                                    <img id="img_game_logo"
                                    <c:choose>
                                        <c:when test="${category.displaySetting.iconURL eq null || category.displaySetting.iconURL eq ''}">src="/static/images/default.jpg" </c:when>
                                        <c:otherwise>src="${uf:parseOrgImg(category.displaySetting.iconURL)}"</c:otherwise>
                                    </c:choose> />
                                </div>
                                <div>

                                    <div id="coords">
                                        <input type="hidden" size="4" id="x1" name="x1"/>
                                        <input type="hidden" size="4" id="y1" name="y1"/>
                                        当前截取的宽度：<input type="text" size="4" id="w" name="w" disabled="true"/>
                                        当前截取的长度：<input type="text" size="4" id="h" name="h" disabled="true"/>
                                    </div>
                                </div>
                                <input type="hidden" id="hiddenValue"
                                       value="<c:choose><c:when test="${category.displaySetting.iconURL eq null || category.displaySetting.iconURL eq ''}">/static/images/default.jpg</c:when>
                                <c:otherwise>${category.displaySetting.iconURL}</c:otherwise></c:choose>">
                                <input type="hidden" id="at" name="at" value="${at}">

                                <div>
                                    <span id="uploadButton">上传图片</span>
                                    <span id="loading" style="display:none"><img
                                            src="/static/images/loading.gif"/></span>
                                </div>
                                <div>
                                    <label style="color:blue">生成图片的长宽</label>
                                    <label>宽度: <input type="text" size="4" id="aw" name="aw"
                                                      class="default_input_singleline"/></label>
                                    <label>长度: <input type="text" size="4" id="ah" name="ah"
                                                      class="default_input_singleline"/></label>
                                    <input type="button" id="createNewImg" value="截取并保存图片">
                                    <span id="loading2" style="display:none"><img
                                            src="/static/images/loading.gif"/></span>
                                </div>


                                <div id="interface" style="margin: 1em 0;"></div>
                                <input type="hidden" name="iconURL" class="default_input_singleline" size="64"
                                       id="iconURL" value="${category.displaySetting.iconURL}">
                            </td>
                        </tr>
                        <tr>
                            <td width="120" align="right" class="edit_table_defaulttitle_td">URL：</td>
                            <td nowrap class="edit_table_value_td">
                                <input type="text" name="url" class="default_input_singleline" size="64"
                                       value="${category.displaySetting.url}">
                            </td>
                        </tr>
                        <tr>
                            <td width="120" align="right" class="edit_table_defaulttitle_td">标题：</td>
                            <td nowrap class="edit_table_value_td">
                                <input id="subject" type="text" name="subject" class="default_input_singleline"
                                       size="64" value="${category.displaySetting.subject}"
                                       onfocus="ss=setInterval(sp,600,'subject')" onblur="clearInterval(ss)">
                                你已经输入了<span id="subjectspan">0</span>字
                            </td>
                        </tr>
                        <tr>
                            <td width="120" align="right" class="edit_table_defaulttitle_td">摘要：</td>
                            <td nowrap class="edit_table_value_td">
                                <textarea rows="5" cols="100" name="summary" class="default_input_singleline"
                                          size="32">${category.displaySetting.summary}</textarea>
                            </td>
                        </tr>
                        <tr>
                            <td width="120" align="right" class="edit_table_defaulttitle_td">扩展信息1：</td>
                            <td nowrap class="edit_table_value_td">
                                <input type="text" name="extrafield1" class="default_input_singleline" size="64"
                                       value="${category.displaySetting.extraField1}">
                                这里目前可填写chinajoy的【会议论坛】模块的【时间】内容
                            </td>
                        </tr>
                        <tr>
                            <td width="120" align="right" class="edit_table_defaulttitle_td">扩展信息2：</td>
                            <td nowrap class="edit_table_value_td">
                                <input type="text" name="extrafield2" class="default_input_singleline" size="64"
                                       value="${category.displaySetting.extraField2}">
                                这里目前可填写chinajoy的【会议论坛】模块的【地点】内容
                            </td>
                        </tr>
                        <tr>
                            <td width="120" align="right" class="edit_table_defaulttitle_td">扩展信息3：</td>
                            <td nowrap class="edit_table_value_td">
                                <input type="text" name="extrafield3" class="default_input_singleline" size="64"
                                       value="${category.displaySetting.extraField3}">
                            </td>
                        </tr>
                        <tr>
                            <td width="120" align="right" class="edit_table_defaulttitle_td">扩展信息4：</td>
                            <td nowrap class="edit_table_value_td">
                                <input type="text" name="extrafield4" class="default_input_singleline" size="64"
                                       value="${category.displaySetting.extraField4}">
                            </td>
                        </tr>
                        <tr>
                            <td width="120" align="right" class="edit_table_defaulttitle_td">扩展信息5：</td>
                            <td nowrap class="edit_table_value_td">
                                <input type="text" name="extrafield5" class="default_input_singleline" size="64"
                                       value="${category.displaySetting.extraField5}">
                            </td>
                        </tr>
                    </table>
                </span>
    <table width="100%" border="0" cellspacing="1" cellpadding="0">
        <tr>
            <td class="default_line_td" height="1" colspan="2"></td>
        </tr>
        <tr align="center">
            <td colspan="2">
                <input name="Submit" type="submit" class="default_button" value="提交">
                <input name="Reset" type="button" class="default_button" value="返回"
                       onclick="javascript:window.history.go(-1);">
            </td>
        </tr>
    </table>
</form>
</td>
</tr>
</table>
</body>
</html>
<script language="JavaScript" type="text/JavaScript">

    window.jcrop_api;
    $('#img_game_logo').Jcrop({
        bgFade: true,
        onChange: showCoords,
        onSelect: showCoords,
        onRelease: clearCoords,
        bgOpacity: 0.3,
        setSelect: [ 0, 0, 180, 180 ]
    }, function () {
        jcrop_api = this;
    });

    jQuery(function ($) {
        var settings = {
            upload_url: "${urlUpload}/json/upload/qiniu",
            post_params: {
                "at": "joymeplatform",
                "filetype": "original"
            },

            // File Upload Settings
            file_size_limit: "8 MB",    // 2MB
            file_types: "*.jpg;*.png;*.gif",
            file_types_description: "请选择图片",
            file_queue_limit: 1,

            file_dialog_complete_handler: fileDialogComplete,
            upload_start_handler: uploadStart,
            upload_progress_handler: uploadProgress,
            upload_success_handler: uploadSuccess,
            upload_complete_handler: uploadComplete,

            // Button Settings
            button_image_url: "/static/images/uploadbutton.png",
            button_placeholder_id: "uploadButton",
            button_width: 61,
            button_height: 22,
            moving_average_history_size: 40,

            // Flash Settings
            flash_url: "/static/include/swfupload/swfupload.swf",
            flash9_url: "/static/include/swfupload/swfupload_fp9.swf",

            // Debug Settings
            debug: false    };

        var swfu = new SWFUpload(settings);

        // Define page sections
        var sections = {
            anim_buttons: '动态选图'
        };
        // Define animation buttons
        var ac = {
            '380x210（首页推荐图1）': [0, 0, 380, 210],
            '380x320(首页推荐图2)': [0, 0, 380, 320],
            '185x100（首页推荐图3）': [0, 0, 185, 100],
            '130x98(条目文章图片)': [0, 0, 130, 98],
            '80x90（首页文章图片）': [0, 0, 80, 90]
        };

        // Create fieldset targets for buttons
        for (i in sections)
            insertSection(i, sections[i]);

        // Create animation buttons
        for (i in ac) {
            $('#anim_buttons').append(
                    $('<button />').append(i).click(animHandler(ac[i])), ' '
            );
        }

        // Function to insert named sections into interface
        function insertSection(k, v) {
            $('#interface').append(
                    $('<fieldset style="white-space: normal"></fieldset>').attr('id', k).append(
                            $('<legend></legend>').append(v)
                    )
            );
        }

        ;
        // Handler for option-setting buttons
        function setoptHandler(k, v) {
            return function () {
                var opt = { };
                opt[k] = v;
                jcrop_api.setOptions(opt);
                return false;
            };
        }

        ;

        function showCoords(c) {
            $('#x1').val(c.x);
            $('#y1').val(c.y);
            $('#aw').val(c.xMax);
            $('#ah').val(c.yMax);
            $('#w').val(c.w);
            $('#h').val(c.h);
        }

        ;
        function clearCoords() {
            $('#coords input').val('');
        }

        ;
        // Handler for animation buttons
        function animHandler(v) {

            return function () {
                jcrop_api.setSelect(v);

                var ratioValue = (v[2] - v[0]) / (v[3] - v[1]);

                jcrop_api.setOptions({ aspectRatio: ratioValue });
                $('#aw').val(v[2] - v[0]);
                $('#ah').val(v[3] - v[1]);
                $('#w').val(v[2] - v[0]);
                $('#h').val(v[3] - v[1]);
                return false;
            };
        }

        ;

        $('#anim_buttons').append(
                $('<button></button>').append('任意比例').click(function () {
                    jcrop_api.animateTo(
                            [300, 200, 300, 200],
                            function () {
                                jcrop_api.setOptions({ aspectRatio: 0 });
                                this.release();
                            }
                    );
                    return false;
                })
        );

        $('#interface').show();


        $("#createNewImg").click(function () {
            $('#loading2').css('display', '');
            var imgNew = $('#hiddenValue').val();
            var httpImgNew = "http://" + imgNew.substring(1, 5) + ".${DOMAIN}" + imgNew;

            if ($('#x1').val() == '' && $('#y1').val() == '' && $('#w').val() == '' && $('#h').val() == '') {
                alert("请选择一个区域");
                $('#loading2').css('display', 'none');
                return false;
            }

            $.ajax({
                url: "/json/viewline/imgcropper",
                type: 'post',
                data: {filename: imgNew, x1: $("#x1").val(), y1: $("#y1").val(), w: $("#w").val(), h: $("#h").val(), at: $('#at').val(), rw: $('#aw').val(), rh: $('#ah').val()},
                success: function (data) {

                    var jsonObj = eval('(' + data + ')');
                    if (jsonObj.status_code == '1') {

                        $('#img_game_logo').attr('src', genImgDomain(jsonObj.result[0], '${DOMAIN}'));
                        jcrop_api.setImage(genImgDomain(jsonObj.result[0], '${DOMAIN}'));
                        $('#iconURL').val(jsonObj.result[0]);

                        $('#hiddenValue').val(jsonObj.result[0]);
                    } else {
                        alert('切割失败!');
                    }
                    $('#loading2').css('display', 'none');
                }
            });

        });
    });

    function sp() {
        var tex = document.getElementById('subject').value;
        var nun = tex.length;
        var spa = document.getElementById('subjectspan');
        spa.innerHTML = nun;
    }

</script>