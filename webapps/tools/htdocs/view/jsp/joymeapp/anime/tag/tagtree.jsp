<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>大动漫标签数</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <link href="/static/include/dtree/dtree.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/dtree/dtree.js"></script>
    <script>
        $(document).ready(function() {
            $('#focusBatch').bind("submit", function() {
//                var srcScreenname = $.trim($('#srcScreenname').val());
//                if (srcScreenname.length == 0) {
//                    alert("用户昵称");
//                    return false;
//                }


            });
        });


    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 大动漫管理 >> 大动漫标签列表</td>
    </tr>
<tr>
<td height="100%" valign="top"><br>
    <table border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td class="list_table_header_td">标签列表</td>
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

    <div class="dtree">
        <script>
            d = new dTree('d');
            d.add(0,-1,'大动漫标签展示页面');

            <c:forEach var="tag" items="${animeTagList}">
            d.add(${tag.tag_id}, ${tag.parent_tag_id}, '<input type="checkbox" name="where" value="2" id="t100">${tag.tag_name}','/joymeapp/anime/tv/list?tag_id=${tag.tag_id}');
            </c:forEach>

            //alert(d);
            //console.log(d)
            document.write(d);
            d.openAll();
           // d.closeAll();
        </script>
    </div>
</td>
</tr>
</table>
</body>
</html>