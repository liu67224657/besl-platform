<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>403 - 缺少权限</title>
    <link href="${URL_LIB}/static/default/css/core.css" rel="stylesheet" type="text/css"/>
    <style>
        .error_zm {
            width: 904px;
            height: 435px;
            background: url(${URL_LIB}/static/default/img/errorbg.jpg) no-repeat;
            margin: 0 auto;
        }

        .error_zm ul {
            float: right;
            width: 215px;
            margin: 190px 310px 0 0;
            display: inline;
        }

        .error_zm ul a {
            width: 215px;
            display: block;
            height: 35px;
        }
    </style>
</head>
<body style="background:none;">
<div class="error_zm">
    <ul>
        <a href="${URL_WWW}/" title="返回个人中心"></a>
        <a href="${URL_WWW}/discovery" title="去发现随便看看"></a>
    </ul>
</div>
<script type="text/javascript" src="${URL_LIB}/static/js/pv.js"></script>
</body>
</html>