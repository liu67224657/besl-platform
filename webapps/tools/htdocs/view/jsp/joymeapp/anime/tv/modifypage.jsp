<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>修改节目列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/appadvertise.js"></script>
    <link href="/static/include/dtree/dtree.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/dtree/dtree.js"></script>
    <style type="text/css">
        .td_cent {
            text-align: center;
            vertical-align: middle
        }
    </style>
    <script>
        $(document).ready(function () {
            var coustomSwfu = new SWFUpload(coustomImageSettings);
            $('#form_submit').bind('submit', function () {
                if ($("#tv_name").val() == '') {
                    alert("集名");
                    $("#tv_name").focus();
                    return false;
                }

                if($("#tv_number").val() != "") {
                    if(isNaN($("#tv_number").val())){
                        alert("数字集名需要填写数字。");
                        $("#tv_number").focus();
                        return false;
                    }
                }


                var tag = ',';
                $("input[name='tag']:checked").each(function() {
                    tag += $(this).val()+",";
                });
                $("#tags").val(tag);

            });


            var checkTags= '${animeTV.tags}';
            $("input[name='tag']").each(function() {
                var arr = checkTags.split(",");
                for(i=0;i<arr.length;i++){
                    if(arr[i]==$(this).val()){
                        $(this).attr("checked","checked");
                    }
                }
            });


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

    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 大动漫管理 >> 大动漫视频列表</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">修改视频</td>
                </tr>
            </table>
            <form action="/joymeapp/anime/tv/modify" method="post" id="form_submit">
                <input type="hidden" name="tv_id" size="48" value="${animeTV.tv_id}"/>
                <input type="hidden" name="tag_id" size="48" value="${tag_id}"/>
                <input type="hidden" name="pager.offset" size="48" value="${pageStartIndex}"/>
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            视频ID:
                        </td>
                        <td height="1">
                            <input  type="text" size="48" value="${animeTV.tv_id}" readonly="readonly"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            是否删除:
                        </td>
                        <td height="1">
                            <select name="remove_status" id="remove_status">
                                <option value="invalid" <c:if test="${animeTV.remove_status.code=='invalid'}">selected="selected"</c:if>>预发布</option>
                                <option value="valid" <c:if test="${animeTV.remove_status.code=='valid'}">selected="selected"</c:if>>发布</option>
                                <option value="removed" <c:if test="${animeTV.remove_status.code=='removed'}">selected="selected"</c:if>>删除</option>
                            </select>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            集名:
                        </td>
                        <td height="1">
                            <input id="tv_name" type="text" name="tv_name" size="48" value="${animeTV.tv_name}"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>


                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            数字集名:
                        </td>
                        <td height="1">
                            <input id="tv_number" type="text" name="tv_number" size="48" value="${animeTV.tv_number}"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            采集的URL:
                        </td>
                        <td height="1">
                            <input id="url" type="text" name="url" size="48" value="${animeTV.url}" readonly="readonly"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>


                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            m3u8地址:
                        </td>
                        <td height="1">
                            <input id="m3u8" type="text" name="m3u8" size="48" value="${animeTV.m3u8}"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>


                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            是否new:
                        </td>
                        <td height="1">
                            <c:forEach var="type" items="${animeTvIsNewType}">
                                <c:if test="${type.code==animeTV.animeTvIsNewType.code}">
                                    <input type="radio" name="isnew" value='${type.code}' checked="checked"/><fmt:message key="anime.tv.new.${type.code}" bundle="${def}"/>
                                </c:if>
                                <c:if test="${type.code!=animeTV.animeTvIsNewType.code}">
                                    <input type="radio" name="isnew" value='${type.code}'/><fmt:message key="anime.tv.new.${type.code}" bundle="${def}"/>
                                </c:if>
                            </c:forEach>

                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>


                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            视频大小(单位MB):
                        </td>
                        <td height="1">
                            <input id="space" type="text" name="space" size="48" value="${animeTV.space}"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td td_cent">
                            所属标签	：
                        </td>
                        <td height="1" class="">
                            <input type="hidden" name="tags" id="tags" value=''/>
                            <div class="dtree">
                                <script>
                                    d = new dTree('d');
                                    d.add(0,-1,'大动漫标签展示页面');
                                    <c:forEach var="tag" items="${animeTagList}">
                                            d.add(${tag.tag_id}, ${tag.parent_tag_id}, '<input type="checkbox" name="tag" value="${tag.tag_id}"/>${tag.tag_name}');
                                    </c:forEach>
                                    document.write(d);
                                    //d.openAll();
                                    d.closeAll();
                                </script>
                            </div>


                            <%--<c:forEach var="type" items="${animeTagList}">--%>
                                <%--<input type="checkbox" name="tag" value='${type.tag_id}'/>${type.tag_name}--%>
                            <%--</c:forEach>--%>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent">
                            视频来源：
                        </td>
                        <td height="1" class="">
                            <select name="domain2" style="width: 81px" disabled="disabled">
                                <c:forEach var="stype" items="${animeTVDomain}">
                                    <c:if test="${stype.code==animeTV.domain.code}">
                                        <option value="${stype.code}" selected="selected"><fmt:message key="anime.tv.domain.${stype.code}" bundle="${def}"/></option>
                                        <input  type="hidden" name="domain" value="${stype.code}"/>
                                    </c:if>
                                    <c:if test="${stype.code!=animeTV.domain.code}">
                                        <option value="${stype.code}"><fmt:message key="anime.tv.domain.${stype.code}" bundle="${def}"/></option>
                                    </c:if>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            视频来源参数:
                        </td>
                        <td height="1">
                            <input id="domain_param" type="text" name="domain_param" size="48" value="${animeTV.domain_param}" readonly="readonly"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>


                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            图片:
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${fn:length(animeTV.tv_pic) > 0}"><img id="picurl1_src" src="${animeTV.tv_pic}"
                                                                             class="img_pic" width="200px" height="200px"/></c:when>
                                <c:otherwise><img id="picurl1_src" src="/static/images/default.jpg"
                                                  class="img_pic" width="200px" height="200px"/></c:otherwise>
                            </c:choose>
                            <span id="upload_button" class="upload_button">上传</span>
                            <span id="loading" style="display:none" class="loading"><img
                                    src="/static/images/loading.gif"/></span>
                            <input id="picurl1" type="hidden" name="tv_pic" value="${animeTV.tv_pic}"><span style="color: red">图片尺寸：230px * 140px</span>
                        </td>
                        <td height="1">
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