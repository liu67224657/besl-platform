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
    <script type="text/javascript" src="/static/include/swfupload/clientlineitemhandler.js"></script>
    <script type="text/javascript" src="/static/include/My97DatePicker/WdatePicker.js"></script>
    <script>
        $(document).ready(function () {
            $('#form_submit').bind('submit', function () {
                var redmessagetype = $("#redmessagetype").val();
                if( redmessagetype!= "" && redmessagetype==2) {
                    var text = $("#text").val()
                    if(text=="" || isNaN(text) ){
                        alert("需要填写数字");
                        $("#text").focus();
                        return false;
                    }
                }
            });

            $("#redmessagetype").change(function(){
                var redmessagetype = $("#redmessagetype").val();
                if(redmessagetype==7){
                    $("#redmessagetext").val("礼");
                }else if(redmessagetype==6){
                    $("#redmessagetext").val("活");
                }else if(redmessagetype==5){
                    $("#redmessagetext").val("新");
                }else if(redmessagetype==4){
                    $("#redmessagetext").val("攻");
                }
            });
        });
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td"><span
                            style="font-size:16px; font-family: Arial, Helvetica, sans-serif; font-weight:bold;">游戏TAB-最新创建</span>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/gameclient/gametab/news/create" method="post" id="form_submit">
                <table width="90%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" class="">
                            <input type="hidden" name="lineid" value="${line.lineId}"/>
                            <input type="hidden" name="gamedbid" value="${gamedbid}"/>
                            <input type="hidden" name="validstatus" value="${validstatus}"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            标题：
                        </td>
                        <td height="1" class="" width="10">
                            <input type="text" name="itemname" size="20" id="input_text_name"/>
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
                            描述:
                        </td>
                        <td height="1" class="">
                            <textarea name="desc" rows="8" cols="50"></textarea>
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            跳转类别:
                        </td>
                        <td height="1">

                            <select name="redirecttype" id="redirecttype">
                                <option value="">请选择</option>
                                <c:forEach items="${redirectCollection}" var="type">
                                    <option value="${type.code}"><fmt:message
                                            key="client.item.redirect.${type.code}"
                                            bundle="${def}"/></option>
                                </c:forEach>
                            </select>*
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            链接地址:
                        </td>
                        <td height="1" class="">
                            <input id="linkaddress" type="text" size="80" name="linkaddress">
                            <c:if test="${not empty errorMsgMap.urlerror}">
                                <span style="color:red"><fmt:message key="${errorMsgMap.urlerror}"
                                                                     bundle="${def}"/></span>
                            </c:if>
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            category显示分类:
                        </td>
                        <td height="1" class="">
                            <input id="category" type="text" size="80" name="category">
                        </td>
                        <td height="1">
                            <c:if test="${not empty errorMsgMap.urlerror}">
                                <span style="color:red"><fmt:message key="${errorMsgMap.cateerror}"
                                                                     bundle="${def}"/></span>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            category颜色值:
                        </td>
                        <td height="1" class="">
                            <input id="categorycolor" type="text" size="80" name="categorycolor">
                        </td>
                        <td height="1">
                            <c:if test="${not empty errorMsgMap.catecolor}">
                                <span style="color:red"><fmt:message key="${errorMsgMap.urlerror}"
                                                                     bundle="${def}"/></span>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            修改时间：
                        </td>
                        <td height="1" class="" width="10">
                            <input type="text" class="Wdate"
                                   onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',autoPickDate:true})"
                                   readonly="readonly" value="${updatetime}" name="createtime"/>
                        </td>
                        <td height="1" align="left"></td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td">
                            消息类型：
                        </td>
                        <td height="1" class="" width="10">
                            <select name="redmessagetype" id="redmessagetype">
                                <option value="">请选择</option>
                                <c:forEach items="${redMessageType}" var="type">
                                    <option value="${type.code}"><fmt:message
                                            key="gameclient.news.redmessagetype.${type.code}"
                                            bundle="${def}"/></option>
                                </c:forEach>
                            </select>*
                        </td>
                        <td height="1" align="left"></td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td">
                            消息文字：
                        </td>
                        <td height="1" class="" width="10">
                            <input id="redmessagetext" type="text" size="80" name="redmessagetext">
                        </td>
                        <td height="1" align="left"></td>
                    </tr>

                </table>
                <table width="90%" border="0" cellspacing="1" cellpadding="0">
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