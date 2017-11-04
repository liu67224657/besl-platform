<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>社交端文章列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <style>
        .contentinfo div {
            margin-top: 30px;
        }
    </style>
</head>

<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 分类管理 >> 社交端文章详情</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>

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
        </td>
    </tr>
</table>
<div style="width:100%; font-size:13px;" class="contentinfo">
    <div>文章ID ：${socialContent.contentId}</div>


    <div> 标题 ：${socialContent.title}</div>

    <div> 内容：${socialContent.body} </div>

    <div>图片 ：<img src="${socialContent.pic.pic}"/></div>

    <div> mp3 ：
        <embed src="${socialContent.audio.mp3}" mastersound hidden="true" loop="false"
               autostart="false" width="350" height="70"></embed>
    </div>

</div>

</body>
</html>