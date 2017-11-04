<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<html>
<head>
    <meta http-equiv="refresh"
          content="1;url=/audit/profile/profilelist?maxPageItems=${page.pageSize}&items=${page.totalRows}&createstartdate=${params.startDate}&createenddate=${params.endDate}&startDate=${params.startUpDate}&endDate=${params.endUpDate}&screenName=${params.screenName}&audit=${params.auditStatus.value}&sorttype=${params.sortType}">
    <title>封号成功页面</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript">

    </script>
</head>

<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td ><h3>操作成功，点击返回</h3></td>
    </tr>
</table>
</body>
</html>