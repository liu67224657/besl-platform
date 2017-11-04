<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>CMS定时发布相关需求</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="/static/include/My97DatePicker/WdatePicker.js"></script>
    <script language="JavaScript" type="text/JavaScript">
        $(document).ready(function () {
            $('#form_submit').bind('submit', function () {
                var cmsid = $.trim($('#cms_id').val());
                if (cmsid.length == 0) {
                    alert("请填写栏目id");
                    return false;
                }

                if (isNaN(cmsid)) {
                    alert("栏目id请填写数字");
                    return false;
                }

//                var cmsname = $.trim($('#cms_name').val());
//                if (cmsname.length == 0) {
//                    alert("请填写栏目名称");
//                    return false;
//                }

                var release_time = $.trim($('#release_time').val());
                if (release_time.length == 0) {
                    alert("请填写刷新时间");
                    return false;
                }

            });
        });
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> CMS定时发布</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="30%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">CMS定时发布</td>
                    <td class="">
                        <form></form>
                    </td>
                </tr>
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="10" class="error_msg_td">
                                ${errorMsg}
                        </td>
                    </tr>
                </c:if>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/misc/refreshcms/modify" method="post" id="form_submit">
                <input type="hidden" name="time_id" value="${refreshCMSTiming.time_id}">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" class="default_line_td">
                            栏目ID:
                        </td>
                        <td height="1">
                            <input type="text" name="cms_id" value="${refreshCMSTiming.cms_id}" id="cms_id">
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            栏目名称:
                        </td>
                        <td height="1">
                            <input type="text" name="cms_name" value="${refreshCMSTiming.cms_name}" id="cms_name" readonly>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            刷新类型:
                        </td>
                        <td height="1">
                            <c:if test="${refreshCMSTiming.refreshReleaseType.code==1}">
                                <input type="radio" name="release_type" checked value="1">单次
                                <input type="radio" name="release_type" value="2">每天
                            </c:if>
                            <c:if test="${refreshCMSTiming.refreshReleaseType.code==2}">
                                <input type="radio" name="release_type" value="1">单次
                                <input type="radio" name="release_type" checked value="2">每天
                            </c:if>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            刷新时间:
                        </td>
                        <td height="1">
                            <input type="text" name="release_time" id="release_time"
                                   onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly"
                                   value="<fmt:formatDate value='${date:long2date(refreshCMSTiming.release_time)}' pattern='yyyy-MM-dd HH:mm:ss'/>">
                        </td>
                        <td height="1">
                        </td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td">
                            状态:
                        </td>
                        <td height="1">
                            <select name="remove_status">
                                <c:if test="${refreshCMSTiming.remove_status.code=='n'}">
                                    <option value="n" selected>有效</option>
                                    <option value="y">无效</option>
                                </c:if>
                                <c:if test="${refreshCMSTiming.remove_status.code=='y'}">
                                    <option value="n">有效</option>
                                    <option value="y" selected>无效</option>
                                </c:if>
                            </select>
                        </td>
                        <td height="1">
                        </td>
                    </tr>


                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
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