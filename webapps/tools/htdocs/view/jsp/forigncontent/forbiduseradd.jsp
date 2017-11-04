<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>评论禁言表---添加</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery-1.11.2.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.easyui.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/static/include/js/easyui/themes/default/easyui.css">
    <script type="text/javascript" src="/static/include/js/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>

    <script type="text/javascript">
        $(document).ready(function (e) {
            $("#submit").click(function () {

                var nickName = $.trim($("#form_submit input[name='nickName']").val());
                var startTime = $.trim($("#form_submit input[name='startTime']").val());
                var endTime = $.trim($("#form_submit input[name='endTime']").val());

                var flag_forever = $.trim($("#form_submit select[name='flag_forever']").val());


                if (nickName == '') {
                    alert("请填写需要禁言用户的呢称!");
                    return false;
                } else if (flag_forever == '2' && startTime == '') {
                    alert("请填写禁言起始时间!");
                    return false;
                } else if (flag_forever == '2' && endTime == '') {
                    alert("请填写禁言结束时间!");
                    return false;
                } else if (flag_forever == '2' && endTime != ''&& startTime!='') {
                    //new Date(yyyy,mth,dd,hh,mm,ss);
                    var sTime='';
                    var eTime='';
                    var patternStart = /(\d{4})-(\d{1,2})-(\d{1,2})\s+(\d{1,2}):(\d{1,2}):(\d{1,2})/g;
                    if (patternStart.test(startTime)) {

                        sTime =new Date(RegExp.$1,(parseInt(RegExp.$2)-1),RegExp.$3,RegExp.$4,RegExp.$5,RegExp.$6).getTime();

                    }
                    var patternEnd = /(\d{4})-(\d{1,2})-(\d{1,2})\s+(\d{1,2}):(\d{1,2}):(\d{1,2})/g;
                    if(patternEnd.test(endTime)){

                        eTime= new Date(RegExp.$1,(parseInt(RegExp.$2)-1),RegExp.$3,RegExp.$4,RegExp.$5,RegExp.$6).getTime();

                    }
                   if(sTime!=''&&eTime!=''&&eTime-sTime<=0){
                       alert("结束时间需要大于起始时间");
                       return false;

                   }

                } else {
                    $("#form_submit").submit();
                }
            });
            $("#form_submit select[name='flag_forever']").click(function () {
                if ($("#form_submit select[name='flag_forever']").val() == '1') {
                    $(".easyui-datetimebox").datetimebox({ disabled: true });
                    // $("#form_submit input[name='startTime']").prop("disabled", true);
                    // $("#form_submit input[name='endTime']").prop("disabled", true);

                } else {
                 //   $("#form_submit input[name='startTime']").prop("disabled", false);
                  //  $("#form_submit input[name='endTime']").prop("disabled", false);
                    $(".easyui-datetimebox").datetimebox({ disabled: false });
                }


            });


        });


    </script>

</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 客户服务 >> 内容审核 >> 评论禁言用户管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">添加需要禁言的用户</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="14" class="error_msg_td">${errorMsg}</td>
                    </tr>
                </c:if>
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/forign/content/forbid/create" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" class="default_line_td">
                            用户的呢称:
                        </td>
                        <td height="1">
                            <input type="text" name="nickName" size="32" value="${nickName}"/>
                            *必填项
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            禁言起始时间:
                        </td>
                        <td height="1">
                            <input type="text" class="easyui-datetimebox" editable="false"
                                   name="startTime" value="${startTime}"/>*必填项
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            禁言结束时间:
                        </td>
                        <td height="1">
                            <input type="text" name="endTime" class="easyui-datetimebox"
                                   editable="false" value="${endTime}"/>*必填项
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            是否永久禁言
                        </td>
                        <td height="1">
                            <select name="flag_forever">
                                <option value="2">否</option>
                                <option value="1">是</option>

                            </select>
                        </td>
                        <td height="1">
                        </td>
                    </tr>

                    <tr>
                        <td colspan="3" height="1" class="default_line_td"></td>
                    </tr>
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