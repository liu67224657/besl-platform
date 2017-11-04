<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
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
    <script type="text/javascript" src="/static/include/swfupload/appadvertise.js"></script>
    <link href="/static/include/dtree/dtree.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/dtree/dtree.js"></script>
    <style type="text/css">
        .td_cent {
            text-align: center;
            vertical-align: middle;
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
                if(tag==","){
                    tag="";
                }
                $("#tags").val(tag);

             //   return false;
            });

            $("#getUrl").bind('click',function(){
                var phpurl =$("#phpurl").val();
                var userurl =$("#userurl").val();
                $.ajax({
                    url:"/joymeapp/anime/tv/get",
                    type:'post',
                    data:{'url':phpurl+"?url="+userurl},
                    async:false,
                    dataType: "json",
                    success:function (data) {
                        try {
                            var jsonStr = JSON.parse(data);
                            //console.log(jsonStr.result.display_order);
                            $("#display_order").val(jsonStr.result.display_order);
                            $("#tv_name").val(jsonStr.result.tv_name);
                            $("#domain_param").val(jsonStr.result.domain_param);
                            $("#url").val(jsonStr.result.url);
                            $("#picurl1").val(jsonStr.result.tv_pic);
                            $("#picurl1_src").attr("src",jsonStr.result.tv_pic);

                            $("#domain").val(jsonStr.result.domain);
                            $("#domain2").val(jsonStr.result.domain);

                            var tv_number= jsonStr.result.tv_number;
                            if(tv_number!=""){
                                $("#tv_number").val(tv_number);
                            }

                        } catch (e) {
                        }
                    }
                });
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
                    <td class="list_table_header_td">新增视频</td>
                </tr>
            </table>
            <table width="10%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="10" class="error_msg_td">
                            ${errorMsg}
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td class="" style="display: none">请求地址：<input  type="text" name="" id="phpurl" size="48" value="http://hezuo.${DOMAIN}/api/spider.php" readonly="readonly"/></td>
                    <td class="">视频地址：<input type="text" name="" id="userurl" size="60" value=""/>&nbsp;</td>
                    <td><input type="button" class="default_button" value="提交" id="getUrl"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="10" class="error_msg_td">
                            ${errorMsg}
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/joymeapp/anime/tv/create" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            集名:
                        </td>
                        <td height="1">
                            <input id="tv_name" type="text" name="tv_name" size="48"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            数字集名:
                        </td>
                        <td height="1">
                            <input id="tv_number" type="text" name="tv_number" size="48"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            采集的URL:
                        </td>
                        <td height="1">
                            <input id="url" type="text" name="url" size="48" readonly="readonly"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>


                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            m3u8地址:
                        </td>
                        <td height="1">
                            <input id="m3u8" type="text" name="m3u8" size="48"/>
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
                                <c:if test="${type.code==0}">
                                    <input type="radio" name="isnew"  value='${type.code}' checked="checked"/><fmt:message key="anime.tv.new.${type.code}" bundle="${def}"/>
                                </c:if>
                                <c:if test="${type.code!=0}">
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
                            <input id="space" type="text" name="space" size="48"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>


                    <tr>
                        <td height="1" class="default_line_td td_cent">
                            所属季/篇章	：
                        </td>
                        <td height="1" class="">
                                <input type="hidden" name="tags" id="tags" value=''/>
                                <!--
                                <c:forEach var="type" items="${animeTagList}">
                                       <input type="checkbox" name="tag" value='${type.tag_id}'/>${type.tag_name}
                                </c:forEach>
                                -->
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
                               </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent">
                            视频来源：
                        </td>
                        <td height="1" class="">
                            <select name="domain2" style="width: 81px" id="domain2" disabled="disabled" >
                                <c:forEach var="stype" items="${animeTVDomain}">
                                    <c:if test="${stype.code==1}">
                                        <option value="${stype.code}"  selected="selected"><fmt:message key="anime.tv.domain.${stype.code}" bundle="${def}"/></option>
                                    </c:if>
                                    <c:if test="${stype.code!=1}">
                                        <option value="${stype.code}"><fmt:message key="anime.tv.domain.${stype.code}" bundle="${def}"/></option>
                                    </c:if>
                                </c:forEach>
                            </select>

                            <c:forEach var="stype" items="${animeTVDomain}">
                                <c:if test="${stype.code==1}">
                                    <c:if test="${stype.code==1}">
                                        <input  type="hidden" name="domain" value="${stype.code}" id="domain"/>
                                    </c:if>
                                </c:if>
                            </c:forEach>
                        </td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            视频来源参数:
                        </td>
                        <td height="1">
                            <input id="domain_param" type="text" name="domain_param" size="48" readonly="readonly"/>
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
                                <c:when test="${fn:length(iosPic) > 0}"><img id="picurl1_src" src=""
                                                                             class="img_pic" width="200px" height="200px"/></c:when>
                                <c:otherwise><img id="picurl1_src" src="/static/images/default.jpg"
                                                  class="img_pic" width="200px" height="200px"/></c:otherwise>
                            </c:choose>
                            <span id="upload_button" class="upload_button">上传</span>
                            <span id="loading" style="display:none" class="loading"><img
                                    src="/static/images/loading.gif"/></span>
                            <input id="picurl1" type="hidden" name="tv_pic" value=""><span style="color: red">图片尺寸：230px * 140px</span>
                        </td>
                        <td height="1">
                        </td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            display_order:
                        </td>
                        <td height="1">
                            <input id="display_order" type="text" name="display_order" size="48" readonly="readonly"/>
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