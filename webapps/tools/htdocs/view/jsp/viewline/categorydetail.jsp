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
            window.location.href = "/viewline/categorylist?aspectCode=" + aspectCode;
        }

        function edit(categoryId) {
            window.location.href = "/viewline/categorypreedit?categoryId=" + categoryId;
        }

    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">当前位置：运营维护 >> <a href="/viewline/categorylist?aspectCode=${category.categoryAspect.code}"> 分类管理</a> >> 分类详细信息</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="detail_table_header_td">分类详细信息</td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr class="toolbar_tr">
                    <td>
                        <p:privilege name="/viewline/categorylist">
                            <input name="Submit" type="submit" class="default_button" value="返回分类列表"
                                   onClick="back('${category.categoryAspect.code}');">
                        </p:privilege>
                        <p:privilege name="/viewline/categorypreedit">
                            <input name="Submit" type="submit" class="default_button" value="修改分类"
                                   onClick="edit('${category.categoryId}');">
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
              <td width="120" align="right" class="detail_table_title_td">分类编码：</td>
              <td class="detail_table_value_td">${category.categoryCode}</td>
              <td width="120" align="right" class="detail_table_title_td">是否有效：</td>
              <td class="detail_table_value_td">
                  <fmt:message key="def.validstatus.${category.validStatus.code}.name" bundle="${def}"/>
              </td>
          </tr>
          <tr>
              <td width="120" align="right" class="detail_table_title_td">分类名称：</td>
              <td class="detail_table_value_td">${category.categoryName}</td>
              <td width="120" align="right" class="detail_table_title_td">发布状态：</td>
              <td class="detail_table_value_td">
                  <fmt:message key="def.actstatus.${category.publishStatus.code}.name" bundle="${def}"/>
              </td>
          </tr>
          <tr>
              <td height="1" colspan="4" class="default_line_td"></td>
          </tr>
          <tr>
              <td width="120" align="right" class="detail_table_title_td">分类角度：</td>
              <td class="detail_table_value_td">
                  <fmt:message key="def.viewline.category.aspect.${category.categoryAspect.code}.name" bundle="${def}"/>
              </td>
              <td width="120" align="right" class="detail_table_title_td">上级分类：</td>
              <td class="detail_table_value_td">
                  <a href="/viewline/categorydetail?categoryId=${category.parentCategory.categoryId}">${category.parentCategory.categoryName}</a>
              </td>
          </tr>
          <tr>
              <td height="1" colspan="4" class="default_line_td"></td>
          </tr>
          <tr>
              <td width="120" align="right" class="detail_table_title_td">SEO关键字：</td>
              <td colspan="3" class="detail_table_value_td">${category.seoKeyWord}</td>
          </tr>
          <tr>
              <td width="120" align="right" class="detail_table_title_td">SEO描述：</td>
              <td colspan="3" class="detail_table_value_td">${category.seoDesc}</td>
          </tr>
          <tr>
              <td height="1" colspan="4" class="default_line_td"></td>
          </tr>
          <tr>
              <td width="120" align="right" class="detail_table_title_td">专题描述：</td>
              <td colspan="3" class="detail_table_value_td">${category.categoryDesc}</td>
          </tr>
          <tr>
              <td height="1" colspan="4" class="default_line_td"></td>
          </tr>
          <tr>
              <td width="120" align="right" class="detail_table_title_td">创建人：</td>
              <td class="detail_table_value_td">${category.createUserid}</td>
              <td width="120" align="right" class="detail_table_title_td">创建日期：</td>
              <td class="detail_table_value_td"><fmt:formatDate value="${category.createDate}"
                                                                pattern="yyyy-MM-dd HH:mm:ss"/></td>
          </tr>
          <tr>
              <td width="120" align="right" class="detail_table_title_td">修改人：</td>
              <td class="detail_table_value_td">${category.updateUserid}</td>
              <td width="120" align="right" class="detail_table_title_td">修改日期：</td>
              <td class="detail_table_value_td">
                  <fmt:formatDate value="${category.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
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
                            <input name="tab_1" type="submit" class="tab_noactive_button"
                                   value="分类关联关系(Line)列表" title="分类关联关系列表(ViewLine)"
                                   onClick="submitTab('tabForm','tab_',1,2,'/viewline/linelist?categoryId=${category.categoryId}');">
                            <%--<c:if test="${category.parentCategory ne null}">--%>
                                <input name="tab_2" type="submit" class="tab_noactive_button"
                                       value="管理人员列表" title="管理人员列表"
                                       onClick="submitTab('tabForm','tab_',2,2,
                                               '${ctx}/viewline/listprivacy?categoryId=${category.categoryId}');">
                            <%--</c:if>--%>
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
<script language="JavaScript">
    document.tabForm.tab_1.click();
</script>