<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script src="/static/include/js/default.js" type="text/javascript"></script>
    <title>后台数据管理-修改广告发布</title>
    <script language="JavaScript" type="text/JavaScript">
        function agentList(agentid) {
            window.location.href = "/advertise/publish/listbyagent?agentid=" + agentid;
        }

        function projectList(projectid) {
            window.location.href = "/advertise/publish/listbyproject?projectid=" + projectid;
        }

        function modify(publishid,source) {
            window.location.href = "/advertise/publish/modifypage?publishid=" + publishid + "&source=" + source;
        }
    </script>
    <style type="text/css">
        td {
            overflow: hidden;
            text-overflow: ellipsis; /* for IE */
            -moz-text-overflow: ellipsis; /* for Firefox,mozilla */
            white-space: nowrap;
        }
    </style>
</head>

<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 广告管理 >> 广告位管理</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="detail_table_header_td">详细信息</td>
                </tr>
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr >
                    <td><input type="checkbox" name="checkbox" value="1"
                               onClick="hideSpan(document.getElementById('moduleView'),this.checked)"> [显示/隐藏]
                    </td>
                    <td align="right">
                        <p:privilege name="/advertise/publish/modifypage">
                            <input  name="Submit" type="submit" class="default_button"
                                    value="编辑" onClick="modify('${publish.publishId}','${source}');">
                        </p:privilege>
                        <input style="margin-right: 20px"  name="Reset" type="button" class="default_button" value="返回"
                               onclick="javascipt:window.history.go(-1);">
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
              <td width="4%" align="right" class="detail_table_title_td">广告发布名称：</td>
              <td width="10%" class="detail_table_value_td">${publish.publishId}</td>
              <td width="4%" align="right" class="detail_table_title_td">是否有效：</td>
              <td width="10%" class="detail_table_value_td">
                  <fmt:message key="def.validstatus.${publish.validStatus.code}.name" bundle="${def}"/>
              </td>
          </tr>
          <tr>
              <td align="right" class="detail_table_title_td">广告发布描述：</td>
              <td colspan="3" class="detail_table_value_td">${publish.publishDesc}</td>
          </tr>
          <tr>
              <td height="1" colspan="4" class="default_line_td"></td>
          </tr>
          <tr>
              <td align="right" class="detail_table_title_td">广告位链接描述：</td>
              <td class="detail_table_value_td" colspan="3">${URL_WWW}/click/${publish.publishId}</td>
          </tr>
          <tr>
              <td align="right" class="detail_table_title_td">广告发布跳转地址：</td>
              <td class="detail_table_value_td" colspan="3">${publish.redirectUrl}</td>
          </tr>
          <tr>
              <td height="1" colspan="4" class="default_line_td"></td>
          </tr>
          <tr>
              <td align="right" class="detail_table_title_td">广告商：</td>
              <td class="detail_table_value_td">
                  <a href="/advertise/publish/listbyagent?agentid=${agent.agentId}">${agent.agentName}</a>
              </td>
              <td align="right" class="detail_table_title_td">广告项目：</td>
              <td class="detail_table_value_td">
                  <a href="/advertise/publish/listbyproject?projectid=${project.projectId}">${project.projectName}</a>
              </td>
          </tr>
          <tr>
              <td height="1" colspan="4" class="default_line_td"></td>
          </tr>
          <tr>
              <td align="right" class="detail_table_title_td">创建人：</td>
              <td class="detail_table_value_td">${publish.createUserid}</td>
              <td align="right" class="detail_table_title_td">创建日期：</td>
              <td class="detail_table_value_td"><fmt:formatDate value="${publish.createDate}"
                                                                pattern="yyyy-MM-dd HH:mm:ss"/></td>
          </tr>
          <tr>
              <td align="right" class="detail_table_title_td">修改人：</td>
              <td class="detail_table_value_td">${publish.updateUserid}</td>
              <td align="right" class="detail_table_title_td">修改日期：</td>
              <td class="detail_table_value_td">
                  <fmt:formatDate value="${publish.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
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
        <td height="100%" valign="top">   <br>
            <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
                <form action="" method="post" name="tabForm" target="tabMain"
                      onSubmit="document.tabMain.location=this.action;return false;">
                    <tr>
                        <td height="22">
                            <input name="tab_1" type="submit" class="tab_noactive_button"
                                   value="广告发布location" title=""
                                   onClick="submitTab('tabForm','tab_',1,2,'/advertise/location/list?publishid=${publish.publishId}');">
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
</table>
</body>
</html>
<script language="JavaScript">
    document.tabForm.tab_1.click();
</script>