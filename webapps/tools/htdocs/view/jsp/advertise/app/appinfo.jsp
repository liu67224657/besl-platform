<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>APP广告素材详情</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <style type="text/css">
        .td_cent{text-align:center;vertical-align:middle};
    </style>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> APP广告管理 >> APP广告素材列表</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">APP广告素材详情</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="10" class="error_msg_td">
                            <fmt:message key="${errorMsg}" bundle="${error}"/>
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <c:if test="${appAdvertise==null}">
                 <table width="100%" border="0" cellspacing="1" cellpadding="0">
                     <tr align="center">
                        <td height="100px">
                             APP素材广告不存在
                        </td>
                    </tr>
                       <tr align="center">
                        <td>
                            <input name="Reset" type="button" class="default_button" value="返回"
                                   onclick="javascipt:window.history.go(-1);">
                        </td>
                    </tr>
                 </table>

            </c:if>
            <c:if test="${appAdvertise!=null}">
            <form action="/advertise/app/create" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td height="24" class="default_line_td td_cent" width="100">
                            广告名称:
                        </td>
                        <td height="24">
                            ${appAdvertise.advertiseName}
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="24" class="default_line_td td_cent" width="100">
                            广告描述:
                        </td>
                        <td height="1">
                            ${appAdvertise.advertiseDesc}
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="24" class="default_line_td td_cent" width="100">
                            广告链接:
                        </td>
                        <td height="1">
                            ${appAdvertise.url}
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>

                     <tr>
                        <td height="24" class="default_line_td td_cent" width="100">
                            跳转类型:
                        </td>
                        <td height="1">
                            <c:if test="${appAdvertise.appAdvertiseRedirectType.code == '0'}">WEB_VIEW</c:if>
                           <c:if test="${appAdvertise.appAdvertiseRedirectType.code == '1'}">APP_STORE</c:if>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                     <tr>
                        <td height="24" class="default_line_td td_cent" width="100">
                            平台:
                        </td>
                        <td height="1">
                            <c:if test="${appAdvertise.appPlatform.code == '0'}">IOS</c:if>
                            <c:if test="${appAdvertise.appPlatform.code == '1'}">ANDROID</c:if>
                            <c:if test="${appAdvertise.appPlatform.code == '2'}">WEB</c:if>
                            <c:if test="${appAdvertise.appPlatform.code == '3'}">CLIENT</c:if>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="24" class="default_line_td td_cent" width="100">
                           状态:
                        </td>
                        <td height="1">
                            <c:if test="${appAdvertise.removeStatus.code == 'n'}">启用中</c:if>
                            <c:if test="${appAdvertise.removeStatus.code == 'y'}">停用中</c:if>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>

                    <tr>
                        <td height="24" class="default_line_td td_cent" width="100">
                            图片1:
                        </td>
                        <td>
                           <img  src="${appAdvertise.picUrl1}" class="img_pic" />
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="24" class="default_line_td td_cent" width="100">
                            图片2:
                        </td>
                        <td>
                             <img src="${appAdvertise.picUrl2}" class="img_pic"/>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                     <tr>
                        <td height="24" class="default_line_td td_cent" width="100">
                            创建时间:
                        </td>
                        <td height="1">
                            ${appAdvertise.createTime}
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                     <tr>
                        <td height="24" class="default_line_td td_cent" width="100">
                            创建人:
                        </td>
                        <td height="1">
                            ${appAdvertise.createUser}
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                     <tr>
                        <td height="24" class="default_line_td td_cent" width="100">
                            创建IP:
                        </td>
                        <td height="1">
                            ${appAdvertise.creatIp}
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr align="center">
                        <td colspan="3">
                            <input name="Reset" type="button" class="default_button" value="返回"
                                   onclick="javascipt:window.history.go(-1);">
                        </td>
                    </tr>
                </table>
            </form>
           </c:if>
        </td>
    </tr>
</table>
</body>
</html>