<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<%@ taglib prefix="bit" uri="/WEB-INF/tags/bitwise.tld" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台数据管理、Line查询列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script src="/static/include/js/jquery.js" type="text/javascript"></script>
    <script src="/static/include/js/default.js" type="text/javascript"></script>
    <script language="JavaScript" type="text/JavaScript">
        function back(categoryId) {
            window.location.href = "/viewline/categorydetail?categoryId=" + categoryId;
        }

        function edit(lineId) {
            window.location.href = "${ctx}/viewline/preeditline?lineId=" + lineId;
        }
    </script>
</head>

<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">当前位置：运营维护 >> <a href="/viewline/categorylist?aspectCode=${line.categoryAspect.code}">分类管理</a> >> <a href="/viewline/categorydetail?categoryId=${line.categoryId}" >Line管理</a> >> Line详细列表</td>
    </tr>
    <tr>
        <td valign="top">
            <br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="detail_table_header_td">Line详细内容</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr class="toolbar_tr">
                    <td>
                        <p:privilege name="/viewline/categorydetail">
                            <input name="Submit" type="button" class="default_button" value="返回分类元素(Line)列表" onclick="back('${line.categoryId}');">
                        </p:privilege>
                        <p:privilege name="/viewline/preeditline">
                            <input name="Submit" type="submit" class="default_button" value="修改分类元素(Line)"
                                   onClick="edit('${line.lineId}');">
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
              <td width="120" align="right" class="detail_table_title_td">分类元素(Line)名称:</td>
              <td class="detail_table_value_td" width="40%">${line.lineName}</td>
              <td width="120" align="right" class="detail_table_title_td">最少条数：</td>
              <td class="detail_table_value_td" width="40%">
                  ${line.itemMinCount}
              </td>
          </tr>
          <tr>
              <td width="120" align="right" class="detail_table_title_td">元素类型：</td>
              <td class="detail_table_value_td"><fmt:message key="def.viewline.itemtype.${line.itemType.code}.name" bundle="${def}"/></td>
              <td width="120" align="right" class="detail_table_title_td">列表位置：</td>
              <td class="detail_table_value_td" width="40%">
                  ${line.locationCode}
              </td>
          </tr>
          <tr>
              <td width="120" align="right" class="detail_table_title_td" >分类元素(Line)描述:</td>
              <td class="detail_table_value_td" colspan="3">
                ${line.lineDesc}
              </td>
          </tr>
          <tr>
              <td height="1" colspan="4" class="default_line_td"></td>
          </tr>
          <tr>
              <td width="120" align="right" class="detail_table_title_td">填充方式：</td>
              <td class="detail_table_value_td" colspan="3">
                  <fmt:message key="def.viewline.autofill.type.${line.autoFillType.code}.name" bundle="${def}"/><br>
              </td>
          </tr>
          <%--<tr>--%>
              <%--<td width="120" align="right" class="detail_table_title_td">填充设置：</td>--%>
              <%--<td class="detail_table_value_td" colspan="3">--%>
                  <%--<jsp:include page="/view/jsp/viewline/detailautofillrule_${line.itemType.code}.jsp"></jsp:include>--%>
              <%--</td>--%>
          <%--</tr>--%>
          <tr>
              <td height="1" colspan="4" class="default_line_td"></td>
          </tr>
          <tr>
              <td width="120" align="right" class="detail_table_title_td">创建人：</td>
              <td class="detail_table_value_td">${line.createUserid}</td>
              <td width="120" align="right" class="detail_table_title_td">创建日期：</td>
              <td class="detail_table_value_td">
                  <fmt:formatDate value="${line.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
              </td>
          </tr>
          <tr>
              <td width="120" align="right" class="detail_table_title_td">修改人：</td>
              <td class="detail_table_value_td">${line.updateUserid}</td>
              <td width="120" align="right" class="detail_table_title_td">修改日期：</td>
              <td class="detail_table_value_td">
                  <fmt:formatDate value="${line.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
              </td>
          </tr>
      </table>
      </span>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td height="100%" valign="top">
            <br>
            <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
                <form action="" method="post" name="tabForm" target="tabMain"
                      onSubmit="document.tabMain.location=this.action;return false;">
                    <tr>
                        <td height="22">
                            <input name="tab_1" type="submit" class="tab_noactive_button"
                                   value="分类元素(Line)列表" title="分类元素(Line)列表"
                                   onClick="submitTab('tabForm','tab_',1,1,
                                           '${ctx}/viewline/listlineitem?lineId=${line.lineId}');">
                    </tr>
                </form>
                <tr>
                    <td height="2" class="default_line_td"></td>
                </tr>
                <tr>
                    <td style="height:100%" valign="top">
                        <iframe style="z-index: 1; width: 100%; height: 90%" name=tabMain src="" frameBorder=0
                                scrolling=auto></iframe>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>
<script language="JavaScript">
    document.tabForm.tab_1.click();
</script>