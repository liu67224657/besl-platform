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
    <style type="text/css">
        .td_cent {
            text-align: center;
            vertical-align: middle;
        }
    </style>
    <script>
        $(document).ready(function () {
            $('#form_submit').bind('submit', function () {
                var platform = $("#platform").val();
                if ( platform== '' || platform=="请选择"  ) {
                    alert("平台");
                    $("#platform").focus();
                    return false;
                }
                if ($("#title").val() == '') {
                    alert("通知标题");
                    $("#title").focus();
                    return false;
                }
                if ($("#text").val() == '') {
                    alert("通知内容");
                    $("#text").focus();
                    return false;
                }
                if ($("#jt").val() == '') {
                    alert("跳转类别");
                    $("#jt").focus();
                    return false;
                }


            });

        });

        function getVersion() {
                var platform = $("#platform").val();
                $.post("/wanba/systemnotice/getversionv2", {platform: platform, appkey: "3iiv7VWfx84pmHgCUqRwun", pushlisttype: 0, enterprise:1}, function (req) {
                    var resMsg = eval('(' + req + ')');
                    if (resMsg.rs == 0) {
                        return false;
                    } else {
                        if (resMsg.result == null) {
                            return;
                        }
                        var radioHtml = '<input type="checkbox" id="all_version" onclick="checkAllVersion($(this))"/>全选<input type="checkbox" id="inverse_version" onclick="checkInverseVersion()"/>反选<br/>';
                        if (resMsg.result.versionList != null && resMsg.result.versionList.length > 0) {
                            var versionList = resMsg.result.versionList;
                            for (var i = 0; i < versionList.length; i++) {
                                radioHtml += '<input type="checkbox" name="version" value="' + versionList[i] + '"/>' + versionList[i] + '&nbsp;'
                                if (i % 6 == 5) {
                                    radioHtml += '<br/>';
                                }
                            }
                        }
                        radioHtml += '<br/><span style="color: red">*必选项</span>';
                        $('#td_version').html(radioHtml);

                    }
                });

        }

        function checkAllVersion(dom) {
            if (dom.prop('checked')) {
                $('input[name=check_version]:checkbox').each(function () {
                    $(this).prop('checked', true);
                });
            } else {
                $('input[name=check_version]:checkbox').each(function () {
                    $(this).prop('checked', false);
                });
            }
        }

        function checkInverseVersion() {
            $('input[name=check_version]:checkbox').each(function () {
                if ($(this).prop('checked')) {
                    $(this).prop('checked', false);
                } else {
                    $(this).prop('checked', true);
                }
            });
        }


    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 玩霸-问答 >> 系统通知</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">新增系统通知</td>
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
            <form action="/wanba/systemnotice/create" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            平台:
                        </td>
                        <td height="1">
                            <select name="platform" id="platform" onchange="getVersion()">
                                <option>请选择</option>
                                <option value="0">IOS</option>
                                <option value="1">安卓</option>
                            </select>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            通知标题:
                        </td>
                        <td height="1">
                            <input id="title" type="text" name="title" size="48"/><span>*必填</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            通知内容:
                        </td>
                        <td height="1">
                            <input id="text" type="text" name="text" size="48"/><span>*必填</span>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            跳转类别:
                        </td>
                        <td height="1">
                            <select name="jt" id="jt">
                                <option value="-1">请选择</option>
                                <c:forEach items="${wanbaJt}" var="type">
                                    <option value="${type.code}">${type.name}</option>
                                </c:forEach>
                            </select>*必选项
                        </td>
                        <td height="1"></td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="80">
                            跳转目标:
                        </td>
                        <td height="1">
                            <input id="ji" type="text" name="ji" size="48"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td">
                            选择版本:
                            <input type="hidden" id="input_version" name="version" value=""/>
                        </td>
                        <td height="1" id="td_version">

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