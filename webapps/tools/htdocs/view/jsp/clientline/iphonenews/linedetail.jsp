<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<%@ taglib prefix="bit" uri="/WEB-INF/tags/bitwise.tld" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台数据管理-Line查询列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script src="/static/include/js/jquery.js" type="text/javascript"></script>
    <script src="/static/include/js/default.js" type="text/javascript"></script>
    <script language="JavaScript" type="text/JavaScript">

        function back(aspectCode) {
            window.location.href = "/clientline/iphone/news/list";
        }

        function edit(lineId) {
            window.location.href = "/clientline/iphone/news/modifypage?lineid=" + lineId;
        }
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 手游画报 >> 手游画报iphone</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="detail_table_header_td">ClientLine详细信息</td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr class="toolbar_tr">
                    <td>
                        <p:privilege name="/clientline/iphone/news/list">
                            <input name="Submit" type="submit" class="default_button" value="返回着迷新闻端ClientLine列表"
                                   onClick="back();">
                        </p:privilege>
                        <p:privilege name="/clientline/iphone/news/modifypage">
                            <input name="Submit" type="submit" class="default_button" value="修改ClientLine"
                                   onClick="edit('${clientLine.lineId}');">
                        </p:privilege>
                        <input type="checkbox" name="checkbox" value="1"
                               onClick="hideSpan(document.getElementById('moduleView'),this.checked)">
                        [显示/隐藏]
                    </td>
                </tr>
            </table>
	  <span id=moduleView>
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
              <td height="1" class="default_line_td"></td>
          </tr>
      </table>
      <table width="100%" border="0" cellspacing="1" cellpadding="0">
          <tr>
              <td width="120" align="right" class="detail_table_title_td">ID：</td>
              <input type="hidden" value="${clientLine.lineId}" id="input_hidden_lineid"/>
              <td class="detail_table_value_td">${clientLine.lineId}</td>
              <td width="120" align="right" class="detail_table_title_td">名称：</td>
              <td class="detail_table_value_td">${clientLine.lineName}</td>
          </tr>
          <tr>
              <td width="120" align="right" class="detail_table_title_td">code编码：</td>
              <td class="detail_table_value_td">${clientLine.code}</td>
              <td width="120" align="right" class="detail_table_title_td">类型：</td>
              <input type="hidden" value="${clientLine.itemType.code}" id="input_hidden_type"/>
              <td class="detail_table_value_td"><fmt:message key="client.item.type.${clientLine.itemType.code}" bundle="${def}"/></td>
          </tr>
          <tr>
              <td width="120" align="right" class="detail_table_title_td">可用状态：</td>
              <td height="1" colspan="3"><fmt:message key="client.line.status.${clientLine.validStatus.code}" bundle="${def}"/></td>
          </tr>
          <tr>
              <td width="120" align="right" class="detail_table_title_td">创建人：</td>
              <td class="detail_table_value_td">${clientLine.createUserid}</td>
              <td width="120" align="right" class="detail_table_title_td">创建日期：</td>
              <td class="detail_table_value_td"><fmt:formatDate value="${clientLine.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
          </tr>
          <tr>
              <td width="120" align="right" class="detail_table_title_td">修改人：</td>
              <td class="detail_table_value_td">${clientLine.updateUserid}</td>
              <td width="120" align="right" class="detail_table_title_td">修改日期：</td>
              <td class="detail_table_value_td"><fmt:formatDate value="${clientLine.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
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
                <tr>
                    <td height="2" class="default_line_td"></td>
                </tr>
                <tr>
                    <td style="height:100%" valign="top">
                        <iframe style="z-index: 1; width: 100%; height: 100%" src="/clientline/item/list?lineid=${clientLine.lineId}" frameBorder=0 scrolling=auto></iframe>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>