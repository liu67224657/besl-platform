<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <title>后台数据管理-批量发码</title>
    <script type="text/javascript">
        function textAreaRow(textarea_id,input_id){
            var text = document.getElementById(textarea_id).value;
            var index = 0;
            var lastIndex = 0;
            while(true)
            {
                lastIndex = text.indexOf("\n",lastIndex + 1);
                if(lastIndex == -1){
                    break;
                }
                index++;
            }
            index++;
            document.getElementById(input_id).value=index;
            if(text==""){
                document.getElementById(input_id).value=0;
            }
            $("#import_error").text("");
        }
        function textAreaWords(textarea_id,input_id){
            $("#"+input_id).text("共计"+document.getElementById(textarea_id).value.length+"字");
            if(document.getElementById(textarea_id).value.length>300){
                $("#"+input_id).css("color","red");
            }else{
                $("#"+input_id).css("color","green");
            }
        }
        function emailCheck(){
            $("#email_error").text("");
        }
        $(function(){
            textAreaRow('username','username_number');
            textAreaRow('price_code','code_number')
            textAreaWords('notice','notice_error');
            var messageResult="${messageResult}";
            if(messageResult=="sendSuccess"){
                messageResult="";
                alert("发送成功！");
            }else if(messageResult=="lengthError"){
                $("#import_error").text("发送失败！请验证用户名称及奖品码数量是否一致！");
            }else if(messageResult=="sendNameError"){
                $("#noticeradio_error").text("发送失败！请验证发送人是否正确！");
            }
            $("#addradio").click(function(){
                if($("#addtext").val()!=""){
                    $("#add").append('<input name="noticeradio" type="radio" value="'+$("#addtext").val()+'"/>'+$("#addtext").val());
                    $("#addtext").val("");
                }
            })
            $("#batch_sendcode").submit(function(){
                if($("#username_number").val()!=$("#code_number").val()&&$("#price_code").val()!=""){
                    $("#import_error").text("用户名单与奖品码数量不一致！");
                    return false;
                }else if($("#username").val()==""){
                    $("#import_error").text("用户名单不能为空！");
                    return false;
                }else if(document.getElementById("notice").value.length>"300"){
                    $("#notice_error").text("通知内容字数过长！");
                    return false;
                }else if(document.getElementById("notice").value.indexOf("\${text1}")=="-1"){
                    $("#notice_error").text("必须含有\${text1}等相似关键字！");
                    $("#notice_error").css("color","red");
                    return false;
                }else if($("#email").val().indexOf("，")!="-1"){
                    $("#email_error").text("请用半角逗号（,）隔开！");
                    return false;
                }else if($("#email").val()==""){
                    $("#email_error").text("必须填写1个以上地址!");
                    return false;
                }else{
                    var email_length=document.getElementById("email").value.length;
                    if (email_length<7) {
                        $("#email_error").text("请输入正确的e-mail地址!");
                        return false;
                    }
                }
            })
        })
    </script>
</head>

<body>
<form id="batch_sendcode" name="batch_sendcode" action="/batchmanage/sendcode" method="post">
<table width="100%"  border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 批量管理 >> 批量发码</td>
    </tr>
    <tr>
        <td><br></td>
    </tr>
    <tr>
        <td height="1" class="default_line_td" colspan="2"></td>
    </tr>
    <tr>
        <td width="100%">
            <table width="100%">
                <tr>
                    <td class="edit_table_defaulttitle_td" align="right" width="120px">导入：</td>
                    <td class="edit_table_value_td">
                        <table>
                            <tr>
                                <td >
                                    用户名单&nbsp;&nbsp; 共计&nbsp;
                                    <input id="username_number" type="text" readonly="readonly" style="color: green;width: 40px;border: 0px" value="0"/>&nbsp;个
                                </td>
                                <td >
                                    奖品码&nbsp;&nbsp;共计&nbsp;
                                    <input id="code_number" type="text" readonly="readonly" style="color: green;width: 40px;border: 0px" value="0"/>&nbsp;个
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <textarea id="username" name="username"
                                              onkeyup="textAreaRow('username','username_number')"
                                              class="default_input_multiline" cols="60" rows="15">${username}</textarea>
                                </td>
                                <td>
                                    <textarea id="price_code" name="pricecode"
                                              onkeyup="textAreaRow('price_code','code_number')"
                                              class="default_input_multiline" cols="60" rows="15">${pricecode}</textarea>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                <span id="import_error" style="color: red;"></span>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td class="edit_table_defaulttitle_td" align="right">通知内容：</td>
                    <td class="edit_table_value_td">
                        <textarea id="notice" name="notice" onkeyup="textAreaWords('notice','notice_error')" class="default_input_multiline" cols="120" rows="5">亲爱的\${text1},恭喜您获得***活动奖励的****奖品码，您的奖品码为\${text2},请到以下网址进行相关的激活操作。感谢您的支持。</textarea>
                        <span id="notice_error" style="color: green;"></span>
                        <br><span style="color: green;">*通知或私信内容不超过300字,且必须含有\${text1}等相似关键字</span>
                    </td>
                </tr>
                <tr>
                    <td class="edit_table_defaulttitle_td" align="right">邮件主题：</td>
                    <td class="edit_table_value_td">
                        <select name="emailname">
                            <option >批量发码结果</option>
                            <option >批量通知结果</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td class="edit_table_defaulttitle_td" align="right">发送结果邮件：</td>
                    <td class="edit_table_value_td">
                        <input id="email" name="email" onkeyup="emailCheck();" type="text" size="80" class="default_input_singleline" >
                        <span id="email_error" style="color: red;"></span>
                        <br><span style="color: green;">*支持多个地址，用半角逗号分隔，必须填写1个以上地址</span>
                    </td>
                </tr>
                <tr>
                    <td class="edit_table_defaulttitle_td" align="right">发送方式：</td>
                    <td class="edit_table_value_td">
                        <c:forEach items="${noticeRadios}" var="noticeradio" varStatus="i">
                            <input name="noticeradio" type="radio"
                                   <c:if test="${i.index==1}">checked="true"</c:if>
                                   <c:if test="${noticeradio!='系统通知'}">value="${noticeradio}"</c:if>
                                   <c:if test="${noticeradio=='系统通知'}">value=""</c:if>>${noticeradio}
                        </c:forEach>
                        <span id="add" style="margin-right: 20px"></span>
                        <input id="addtext" class="default_input_singleline" type="text">
                        <input id="addradio" name="addradio" type="button" value="添加"/>
                        <span id="noticeradio_error" style="color: red"></span>
                    </td>
                </tr>
                <tr>
                    <td  class="default_line_td" colspan="2"></td>
                </tr>
                <tr>
                    <td align="center" colspan="2"><input id="send_button" type="submit" value="批量发送"></td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</form>
</body>
</html>