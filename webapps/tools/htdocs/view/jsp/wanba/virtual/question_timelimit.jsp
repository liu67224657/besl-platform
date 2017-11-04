<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>限时提问页</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" charset="utf-8" src="http://static.joyme.com/tools/ueditor1_4_3_3-utf8-jsp/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="http://static.joyme.com/tools/ueditor1_4_3_3-utf8-jsp/ueditor.all.min.js"> </script>
    <script type="text/javascript" charset="utf-8" src="http://static.joyme.com/tools/ueditor1_4_3_3-utf8-jsp/lang/zh-cn/zh-cn.js"></script>
    <script type="text/javascript" charset="utf-8" src="/static/include/ueditor-utf8-jsp/wanbaask_addCustomizeDialog.js"></script>
    <script type="text/javascript" src="/static/include/My97DatePicker/WdatePicker.js"></script>
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



                var askpid =$("#askpid").val();
                if (askpid.trim() == "") {
                    alert("请选择用户");
                    return false;
                }
                var timelimit =$("#timelimit").val();
                var tagid =$("#tagid").val();


                var releasetime =$("#releasetime").val();
                if($.trim(releasetime) != ''){
                    var _str=releasetime.toString();
                    _str = _str.replace(/-/g,"/");
                    var oDate1 = new Date(_str);
                    if(oDate1.getTime()<=new Date().getTime()){
                        alert("定时发布时间 不能小于 当前时间");
                        return false;
                    }
                }

                $.ajax({
                    type: "post",
                    url: "/wanba/virtual/postquestion",
                    data: {title: title,text:text,askpid:askpid,timelimit:timelimit,tagid:tagid,releasetime:releasetime},
                    dataType: "json",
                    success: function (data) {
                        if (data.rs == '1') {
                            alert("提问成功");
//                            $("#title").val("");
//                            UE.getEditor('editor').execCommand('cleardoc');
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
    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 玩霸-问答 >> 提问页</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">提问页</td>
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
                            <select name="askpid" id="askpid">
                                <c:forEach items="${list}" var="profile" varStatus="st">
                                    <option value="${profile.profileid}" <c:if test="${askpid==profile.profileid}">selected</c:if> ><c:if test="${profileMap[profile.profileid]!=null}">${profileMap[profile.profileid].nick}</c:if></option>
                                </c:forEach>
                            </select>
                            <span></span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            选择游戏:
                        </td>
                        <td height="1">
                            <select name="tagid" id="tagid">
                                <c:forEach items="${animeTagList}" var="tag" varStatus="st">
                                    <option value="${tag.tag_id}"  <c:if test="${tagid==tag.tag_id}">selected</c:if>>${tag.tag_name}</option>
                                </c:forEach>
                            </select>
                            <span></span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            设置时间:
                        </td>
                        <td height="1">
                            <select name="timelimit" id="timelimit">
                                <c:forEach items="${questionconfigtype}" var="questionconfig" varStatus="st">
                                    <option value="${questionconfig.timeLimit}" <c:if test="${timelimit==questionconfig.timeLimit}">selected</c:if>>${questionconfig.timeLimit/(1000*60)}分钟__${questionconfig.questionPoint}积分</option>
                                </c:forEach>
                            </select>
                            <span></span>
                        </td>
                        <td height="1" class=>
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

                    <tr>
                        <td height="1" class="default_line_td">
                            定时发布时间:
                        </td>
                        <td height="1">
                            <input placeholder="非必填"  type="text" id="releasetime" name="releasetime" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly" value="">
                        </td>
                        <td height="1">

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