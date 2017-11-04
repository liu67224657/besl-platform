<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>封停博客操作页面</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript">

    </script>
</head>

<body>
  <tr>
    <td height="22" class="page_navigation_td">>> 博客管理 >> 博客信息审核 >> 封停博客操作 </td>
  </tr>
  <tr>
    <td height="100%" valign="top"><br>
      <table width="100%"  border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td class="edit_table_header_td">封停博客</td>
        </tr>
      </table>
      <table width="100%"  border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="1" class="default_line_td"></td>
        </tr>
      </table>
      <table width="100%"  border="0" cellspacing="1" cellpadding="0">
	  <form action="ban" method="post" name="moduleForm" id="moduleForm">
        <input type="hidden" name="uno" value="${params.uno}">
        <input type="hidden" name="createstartdate" value="${params.startDate}">
        <input type="hidden" name="createenddate" value="${params.endDate}">
        <input type="hidden" name="startDate" value="${params.startUpDate}">
        <input type="hidden" name="endDate" value="${params.endUpDate}">
        <input type="hidden" name="screenName" value="${params.screenName}">
        <input type="hidden" name="audit" value="${params.auditStatus.value}">
        <input type="hidden" name="sorttype" value="${params.sortType}">
        <input type="hidden" name="items" value="${page.totalRows}">
        <input type="hidden" name="maxPageItems" value="${page.pageSize}">
        <tr>
          <td width="120" align="right" class="edit_table_defaulttitle_td">选择封停状态:</td>
          <td nowrap class="edit_table_value_td">
            <input type="radio" name="type" checked="checked" value="init"/>正常状态
          </td>
          <td width="120" align="right" class="edit_table_defaulttitle_td">&nbsp;</td>
          <td nowrap class="edit_table_value_td"></td>
        </tr>
        <tr>
          <td width="120" align="right" class="edit_table_defaulttitle_td"></td>
          <td nowrap class="edit_table_value_td"><input type="radio" name="type" value="forbidpost"/>屏蔽发言</td>
          <td width="120" rowspan="2" align="right" class="edit_table_defaulttitle_td">设置到期时间:</td>
          <td nowrap class="edit_table_value_td" rowspan="2">
              <select name="during" id="during${ct.profileBlog.uno}">
                  <option value="${select6.getCode()}">3分钟</option>
                  <option value="${select7.getCode()}">30分钟</option>
					<option value="${select1.getCode()}">3天</option>
					<option value="${select2.getCode()}">7天</option>
					<option value="${select3.getCode()}">1个月</option>
					<option value="${select4.getCode()}">3个月</option>
					<option value="${select5.getCode()}">永久</option>
				</select>
          </td>
        </tr>
        <tr>
          <td width="120" align="right" class="edit_table_defaulttitle_td"></td>
          <td nowrap class="edit_table_value_td"><input type="radio" name="type" value="forbidlogin"/>限制登录</td>

        </tr>
        <tr>
          <td width="120" align="right" class="edit_table_defaulttitle_td"></td>
          <td nowrap class="edit_table_value_td"><input type="radio" name="type" value="ban"/>完全屏蔽</td>
          <td width="120" align="right" class="edit_table_defaulttitle_td"></td>
          <td nowrap class="edit_table_value_td"></td>
        </tr>
        <tr>
          <td height="1" colspan="4" class="default_line_td"></td>
        </tr>
        <tr>
          <td colspan="4">&nbsp;</td>
          </tr>
        <tr align="center">
          <td colspan="4">
          <p:privilege name="audit/profile/ban">
            <input name="Submit" type="submit" class="default_button" value="提交" >
            <input name="Reset" type="reset" class="default_button" value="重置">
          </p:privilege>
            <input name="Button" type="button" class="default_button" value="返回" onClick="history.back()"></td>
          </tr>
	  </form>
      </table>
    </td>
  </tr>
</table>
</body>
</html>