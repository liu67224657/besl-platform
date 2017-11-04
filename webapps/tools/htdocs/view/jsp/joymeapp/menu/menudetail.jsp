<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>着迷APP菜单详细页面</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script>
        $(document).ready(function () {
            $('#tab_1').click();
        });
        function back() {
            window.location.href = "/joymeapp/menu/list?appkey=${menu.appkey}";
        }
        function edit(menuId, appkey) {
            window.location.href = "/joymeapp/menu/modifypage?menuid=" + menuId + "&appkey=" + appkey;
        }
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷APP >> APP菜单管理</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="detail_table_header_td">一级菜单详细信息</td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr class="toolbar_tr">
                    <td>
                        <p:privilege name="/joymeapp/menu/list">
                            <input name="Submit" type="submit" class="default_button" value="返回菜单列表" onClick="back();">
                        </p:privilege>
                        <input type="checkbox" name="checkbox" value="1"
                               onClick="hideSpan(document.getElementById('moduleView'),this.checked)">
                        [显示/隐藏]
                    </td>
                </tr>
            </table>
	  <span id="moduleView">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
              <td height="1" class="default_line_td"></td>
          </tr>
      </table>
      <table width="100%" border="0" cellspacing="1" cellpadding="0">
          <tr>
              <td width="120" align="right" class="detail_table_title_td">菜单名称：</td>
              <td class="detail_table_value_td">${menu.menuName}</td>
              <td width="120" align="right" class="detail_table_title_td">菜单的类型：</td>
              <td class="detail_table_value_td">
                  <fmt:message key="joymeapp.menu.type.${menu.menuType}" bundle="${def}"/>
              </td>
          </tr>
      </table>
      </span>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <br>
        </td>
    </tr>
    <tr>
        <td height="100%" valign="top">
            <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
                <form action="" method="post" name="tabForm" target="tabMain"
                      onSubmit="document.tabMain.location=this.action;return false;">
                    <tr>
                        <td height="22">
                            <input id="tab_1" name="tab_1" type="submit" class="tab_noactive_button"
                                   value="二级菜单列表" title="二级菜单列表"
                                   onClick="submitTab('tabForm','tab_',1,2,'/joymeapp/menu/sublist?appkey=${menu.appkey}&pid=${menu.menuId}&displaytype=${menu.displayType.code}&menucategory=${menu.moduleType.code}');">
                            <input id="tab_2" name="tab_2" type="submit" class="tab_noactive_button"
                                   value="一级菜单标签列表" title="一级菜单标签列表"
                                   onClick="submitTab('tabForm','tab_',2,2,'/joymeapp/menu/tag/taglist?menuid=${menu.menuId}');">
                    </tr>
                </form>
                <tr>
                    <td height="2" class="default_line_td"></td>
                </tr>
                <tr>
                    <td style="height:100%" valign="top">
                        <iframe style="z-index: 1; width: 100%; height: 100%" name=tabMain src="" frameBorder=0
                                scrolling=auto></iframe>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>