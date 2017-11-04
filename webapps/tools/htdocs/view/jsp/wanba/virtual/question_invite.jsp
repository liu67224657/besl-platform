<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>1对1提问页</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" charset="utf-8" src="http://static.joyme.com/tools/ueditor1_4_3_3-utf8-jsp/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="http://static.joyme.com/tools/ueditor1_4_3_3-utf8-jsp/ueditor.all.min.js"> </script>
    <script type="text/javascript" charset="utf-8" src="http://static.joyme.com/tools/ueditor1_4_3_3-utf8-jsp/lang/zh-cn/zh-cn.js"></script>
    <script type="text/javascript" charset="utf-8" src="/static/include/ueditor-utf8-jsp/wanbaask_addCustomizeDialog.js"></script>


    <script type="text/javascript">
        $(document).ready(function () {
            $('#Submit').bind('click', function () {
                var title = $("#title").val();
                if (title.trim() == "") {
                    alert("请填写标题");
                    return false;
                }

                var text = UE.getEditor('editor').getContent();
                if (text.trim() == "") {
                    alert("请填写内容");
                    return false;
                }


                var pid =$("#pid").val();
                if (pid.trim() == "") {
                    alert("请选择用户");
                    return false;
                }

                var invitepid =$("#invitepid").val();
                if (invitepid.trim() == "") {
                    alert("请选择邀请用户");
                    return false;
                }

                if(pid==invitepid){
                    alert("自己不能邀请自己");
                    return false;
                }

                $.ajax({
                    type: "post",
                    url: "/wanba/virtual/postquestioninvite",
                    data: {title: title,text:text,pid:pid,invitepid:invitepid,tagid:$("#tagid").val()},
                    dataType: "json",
                    success: function (data) {
                        if (data.rs == '1') {
                            alert("提问成功");
                           // $("#title").val("");
                           // UE.getEditor('editor').execCommand('cleardoc');
                            var href = "/wanba/virtual/list";
                            window.location.href = href;
                        }else if(data.rs == '-50401'){
                            alert("用户积分不够")
                        }else if(data.rs == '-50100'){
                            alert("标题超长")
                        }else if(data.rs=='-40019'){
                            alert("用户被加入黑名单禁止提问");
                        }else if(data.rs=='-40017'){
                            alert("内容含有敏感词");
                        }
                    }
                });
            });
        });


        function getVerifyProfile() {
            var tagid = $("#tagid").val();
            if (tagid.trim() == "") {
                alert("请选择达人标签");
                return false;
            }
            $.post("/wanba/virtual/listbytagid", {tagid: tagid}, function (req) {
                var resMsg = eval('(' + req + ')');
                if (resMsg.rs == 0) {
                    return false;
                } else {
                    if (resMsg.result == null) {
                        return;
                    }
                    var radioHtml = '<select  name="invitepid" id="invitepid">';
                    radioHtml+='<option value="">请选择达人</option>';
                    if (resMsg.result.profileSet != null && resMsg.result.profileSet.length > 0) {
                        var profileSet = resMsg.result.profileSet;
                        for (var i = 0; i < profileSet.length; i++) {
                            var pid = profileSet[i].split("____");
                            radioHtml += '<option name="version" value="' + pid[1] + '">' + pid[0] + '</option>'
                        }
                    }
                     radioHtml+= '</select>';
                    $('#td_verifyProfile').html(radioHtml);

                }
            });

        }
    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 玩霸-问答 >> 1对1提问页</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">1对1提问页</td>
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

                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            用户:
                        </td>
                        <td height="1">
                            <select name="pid" id="pid">
                                <c:forEach items="${list}" var="profile" varStatus="st">
                                    <option value="${profile.profileid}" <c:if test="${askpid==profile.profileid}">selected</c:if>><c:if test="${profileMap[profile.profileid]!=null}">${profileMap[profile.profileid].nick}</c:if></option>
                                </c:forEach>
                            </select>
                            <span></span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            选择达人标签:
                        </td>
                        <td height="1">
                            <select name="tagid" id="tagid"  onchange="getVerifyProfile()">
                                <option value="">请选择达人标签</option>
                                <c:forEach items="${animeTagList}" var="tag" varStatus="st">
                                    <option value="${tag.tag_id}" <c:if test="${tagid==tag.tag_id}">selected</c:if>>${tag.tag_name}</option>
                                </c:forEach>
                            </select>
                            <span></span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            选择达人:
                            <input type="hidden" id="input_version" name="version" value=""/>
                        </td>
                        <td height="1" id="td_verifyProfile">
                            <select name="invitepid" id="invitepid">
                                <option value="${invitepid.profileId}" ><c:if test="${not empty invitepid}">${invitepid.nick}</c:if><c:if test="${empty invitepid}">请选择达人</c:if></option>
                            </select>
                        </td>
                        <td height="1">
                        </td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            标题:
                        </td>
                        <td height="1">
                            <input name="title" id="title" type="text" size="100" class="default_button" value="">
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            内容:
                        </td>
                        <td height="1">
                            <script id="editor" type="text/plain" style="width:800px;height:200px;"></script>

                        <td height="1" class=>
                        </td>
                    </tr>


                    <tr align="center">
                        <td colspan="3">
                            <input name="Submit" id="Submit" type="button" class="default_button" value="提交">
                            <input name="Reset" type="button" class="default_button" value="返回"
                                   onclick="javascipt:window.history.go(-1);">
                        </td>
                    </tr>
                </table>

        </td>
    </tr>
</table>
<script>
    var ue = UE.getEditor('editor',{
        toolbars: [
            ['source','undo', 'redo']
        ],
        autoHeightEnabled: true,
        autoFloatEnabled: true
    });
</script>
</body>
</html>