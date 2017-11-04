<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="textml;charset=UTF-8">
    <title>批量管理</title>
</head>

<body>
<table>
    <tr>
        <td colspan="3" ><b>您好：</b><td>
    </tr>
    <tr>
        <td colspan="3">您于${senddate}向${sumperson}名用户发送<#if sendcode>奖品码</#if><#if sendcode=false>通知</#if>，目前发送完毕。<td>
    </tr>
    <tr>
        <td colspan="3">
            成功发送${successperson}人，失败${failperson}人，结果如下
        <td>
    </tr>
    <tr>
        <td>通知用户</td>
        <#if sendcode>
        <td>奖品码</td>
        </#if>
        <td>结果</td>
    </tr>
    <#list resultlist as result>
    <tr>
        <td>${result.username}</td>
        <#if sendcode>
        <td>${result.pricecode}</td>
        </#if>
        <td>${result.sendresult}</td>
    </tr>
    </#list>
</table>
</body>
</html>