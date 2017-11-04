<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.enjoyf.webapps.tools.weblogic.privilege.MenuTree" %>
<%@ page import="com.enjoyf.webapps.tools.webpage.controller.SessionConstants" %>
<%@ page import="java.util.HashMap" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>菜单栏</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <link href="/static/include/css/dtree.css" rel="stylesheet" type="text/css"/>
    <script src="/static/include/js/dtree.js" type="text/javascript"></script>
    <script src="/static/include/js/treeview.js" type="text/javascript"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript">
        $(document).ready(function(){
            $('span[id^="NodeItem"]').click(function(){
                $('span[id^="NodeItem"]').removeClass('curitem');
                $(this).addClass('curitem');
            });
        });
    </script>
    <style>
        span.curitem {background-color:#FFFF00;font-weight:700}
    </style>
</head>

<body scroll=no border="10">
<%
    if (request.getSession().getAttribute(SessionConstants.CURRENT_USER) != null) {
        HashMap treeMap = (HashMap) session.getAttribute(SessionConstants.LEFT_ROOT_MENU_TREE);
        for (int i = 0; i < treeMap.size(); i++) {
            MenuTree tree = (MenuTree) treeMap.get(i + "");
            if (tree != null) {
                out.println(tree.treeToString());
                break;
            }
        }

    } %>

</body>
</html>