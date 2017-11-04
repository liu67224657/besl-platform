<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>博客细节</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script src="/static/include/js/default.js" type="text/javascript"></script>
    <script language="JavaScript" type="text/JavaScript">
    function submitTab($formName,$tabPrix,$curTab,$tabNum,$url){
        var tabObject;

        var formObject;
        formObject = eval("document." + $formName);
        formObject.action = $url;

        for(var i = 1;i <= $tabNum;i++){
            tabObject = eval("document." + $formName + "." + $tabPrix + i);

            if($curTab - i == 0){
                tabObject.className = "tab_active_button";

            }else{
                tabObject.className = "tab_noactive_button";
            }
        }

        return false;
    }
    </script>
</head>

<body scroll=no>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 博客信息管理 >> 博客信息审核 >> 查看博客详细内容</td>
    </tr>
      <tr>
        <td valign="top"><br>
          <table width="100%"  border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td class="detail_table_header_td"><font color="red">${profileBlog.screenName}</font>的详细信息</td>
            </tr>
          </table>
          <table width="100%"  border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td height="1" class="default_line_td"></td>
            </tr>
          </table>
          <%--<table width="100%"  border="0" cellspacing="0" cellpadding="0">--%>
            <%--<tr class="toolbar_tr">--%>
              <%--<td><input name="Submit" type="submit" class="default_button" value="Return To Module List" onClick="document.location='list.htm'">--%>
              <%--<input name="Submit" type="submit" class="default_button" value="Modify The Module Info" onClick="document.location='edit.htm'">--%>
              <%--<input name="Submit" type="submit" class="default_button" value="Delete The Module Info" onClick="document.location='list.htm'">--%>
              <%--<input type="checkbox" name="checkbox" value="1" onClick="hideSpan(document.getElementById('moduleView'),this.checked)">--%>
              <%--[Hide Module Detail View]</td>--%>
            <%--</tr>--%>
          <%--</table>--%>
          <span id=moduleView>
          <table width="100%"  border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td height="1" class="default_line_td"></td>
            </tr>
          </table>
          <table width="100%"  border="0" cellspacing="1" cellpadding="0">
            <tr>
              <td width="120" align="right" class="detail_table_title_td">UNO: </td>
              <td class="detail_table_value_td">${profileBlog.uno}</td>
              <td width="120" align="right" class="detail_table_title_td">昵称: </td>
              <td class="detail_table_value_td">${profileBlog.screenName}</td>
            </tr>
            <tr>
              <td width="120" align="right" class="detail_table_title_td">个性域名: </td>
              <td class="detail_table_value_td">${profileBlog.domain}</td>
              <td width="120" align="right" class="detail_table_title_td">Profile Domain: </td>
              <td class="detail_table_value_td"><fmt:message key="def.profile.domain.${profileBlog.profileDomain.code}.name" bundle="${def}"/></td>
            </tr>
            <tr>
              <td width="120" align="right" class="detail_table_title_td">创建时间: </td>
              <td class="detail_table_value_td"><fmt:formatDate value="${profileBlog.createDate}" pattern="yyyy-MM-dd hh:mm:ss"></fmt:formatDate></td>
              <td width="120" align="right" class="detail_table_title_td">修改时间: </td>
              <td class="detail_table_value_td"><fmt:formatDate value="${profileBlog.updateDate}" pattern="yyyy-MM-dd hh:mm:ss"></fmt:formatDate></td>
            </tr>
            <tr>
              <td align="right" class="detail_table_title_td">头像:</td>
              <td colspan="3" class="detail_table_value_td">
                  <c:forEach items="${profileBlog.headIconSet.iconSet}" var="img" varStatus="imgCount">
                      <img src="<c:out value="${uf:parseMFace(img.headIcon)}"/>" height="100" width="100" />
                  </c:forEach>
              </td>
            </tr>
            <tr>
              <td width="120" align="right" class="detail_table_title_td">个人描述:</td>
              <td colspan="3" class="detail_table_value_td">${profileBlog.description}</td>
            </tr>
            <tr>
              <td height="1" colspan="4" class="default_line_td"></td>
            </tr>
            <tr>
              <td width="120" align="right" class="detail_table_title_td">审核者:</td>
              <td class="detail_table_value_td">${profileBlog.auditUserId}</td>
              <td width="120" align="right" class="detail_table_title_td">审核状态: </td>
              <td class="detail_table_value_td"><c:choose>
                                <c:when test="${profileBlog.auditStatus.hasAudit()}">
                                    已审核
                                </c:when>
                                <c:otherwise>
                                    未审核
                                </c:otherwise>
                            </c:choose></td>
            </tr>
            <tr>
              <td width="120" align="right" class="detail_table_title_td">审核时间:</td>
              <td class="detail_table_value_td">${profileBlog.auditDate}</td>
              <td width="120" align="right" class="detail_table_title_td">封停状态: </td>
              <td class="detail_table_value_td">
                <c:choose>
                    <c:when test="${profileBlog.activeStatus.code eq 'ban'}">
                        永久封停
                    </c:when>
                    <c:when test="${profileBlog.activeStatus.code eq 'init'}">
                        正常状态
                    </c:when>
                    <c:when test="${profileBlog.activeStatus.code eq 'forbidpost'}">
                        限制发言
                    </c:when>
                    <c:when test="${profileBlog.activeStatus.code eq 'forbidlogin'}">
                        限制登录
                    </c:when>
                </c:choose>
              </td>
            </tr>
            <tr>
              <td width="120" align="right" class="detail_table_title_td">到期时间:</td>
              <td colspan="3" class="detail_table_value_td">
                  <c:set var="tilldate"><fmt:formatDate value="${profileBlog.inactiveTillDate}" pattern="yyyy-MM-dd hh:mm:ss"></fmt:formatDate></c:set>
                  <c:choose>
                      <c:when test="${profileBlog.activeStatus.code eq 'init' || fn:substring(tilldate, 0, 4) eq '1970'}">
                        无
                      </c:when>
                      <c:otherwise>
                          ${tilldate}
                      </c:otherwise>
                  </c:choose>
              </td>
            </tr>
          </table>
          </span>
          <table width="100%"  border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td height="1" class="default_line_td"></td>
            </tr>
          </table>
          <br></td>
      </tr>
  <tr>
    <td height="100%" valign="top"><table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
      <form action="" method="post" name="tabForm" target="tabMain" onSubmit="document.tabMain.location=this.action;return false;">
        <tr>
          <td height="22">
              <p:privilege name="audit/profile/accountlist">
                <input name="tab_1" type="submit" class="tab_noactive_button" id="tab_1" value="账号列表" title="查看博客对应的账号列表" onClick="submitTab('tabForm','tab_',1,2,'accountlist?uno=${profileBlog.uno}');">
              </p:privilege>
              <input name="tab_2" type="submit" class="tab_noactive_button" id="tab_2" value="修改账号域" title="修改账号对应的PrifileDomain" onClick="submitTab('tabForm','tab_',2,2,'/audit/profile/preupdatedomain?uno=${profileBlog.uno}');" >
          </td>
            <%--<input name="tab_2" type="submit" class="tab_noactive_button" id="tab_2" onClick="submitTab('tabForm','tab_',2,2,'../access/tab_list.htm');" value="Module's Access"></td>--%>
        </tr>
      </form>
      <tr>
        <td height="2" class="default_line_td"></td>
      </tr>
      <tr>
        <td style="height:100%" valign="top"><iframe style="z-index: 1; width: 100%; height: 100%" name=tabMain src="" frameBorder=0 scrolling=auto> </iframe>
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