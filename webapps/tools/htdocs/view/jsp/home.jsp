<%@ page import="com.enjoyf.webapps.tools.webpage.controller.SessionConstants" %>
<%@ page import="com.enjoyf.webapps.tools.weblogic.privilege.MenuTree" %>
<%@ page import="java.util.HashMap" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台数据管理、统计系统</title>
    <Script language="javascript">
        if (self != top) {
            top.location = self.location;
        }

        function switchSysBar() {
            if (switchPoint.innerText == 3) {
                switchPoint.innerText = 4
                document.all("frmTitle").style.display = "none"
            } else {
                switchPoint.innerText = 3
                document.all("frmTitle").style.display = ""
            }
        }

    </Script>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script src="/static/include/js/treeview.js" type="text/javascript"></script>

</head>

<body scroll=no>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" bgcolor="#F4F4F4">
            <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td><b>Joyme.com 后台管理工具</b></td>
                    <td>&nbsp;</td>
                    <td nowrap align="right">登录用户:${current_user.username}&nbsp;&nbsp;</td>
                      <td width="56">
                         <a href="/privilege/user/modifypwd" target="incFrame">修改密码 </a>
                    </td>
                    <td width="16">
                        <a href="/logout">
                            <img src="/static/images/icon/close.gif" width="16" height="16" border="0" align="top">
                        </a>
                    </td>

                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td">
        </td>
    </tr>
    <tr>
        <td>
            <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td width="180" valign="top" id=frmTitle>
                        <iframe style="z-index: 2; visibility: inherit; width: 180; height: 100%" id="leftMenuFrame"
                                name="leftMenuFrame" frameBorder=0 scrolling=yes src="/home/left"></iframe>
                    </td>
                    <td width="1" class="default_line_td"></td>
                    <td width="4" align="center" bgcolor="#F4F4F4" onClick="switchSysBar()" title="关闭/打开左栏"
                        style="cursor:hand"><span class=navPoint
                                                  id=switchPoint>3</span>
                    </td>
                    <td width="1" class="default_line_td"></td>
                    <td valign="top">
                        <iframe style="z-index: 1; width: 100%; height: 100%" id="incFrame" name="incFrame"
                                src="/home/main" frameborder=0
                                scrolling=yes></iframe>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td height="1" class="default_line_td"></td>
    </tr>
    <tr class="footer">
        <td align="center"><b><i>CopyRight 2011 Joyme.com</i></b></td>
    </tr>
</table>
</body>
</html>