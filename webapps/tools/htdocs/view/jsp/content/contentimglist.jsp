<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<%@ taglib prefix="bit" uri="/WEB-INF/tags/bitwise.tld" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台数据管理、发布内容审核列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/dhtmlxcalendar.css">
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/skins/dhtmlxcalendar_dhx_skyblue.css">
    <script src="/static/include/dhtmlxcalendar/dhtmlxcalendar.js"></script>
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script>
        var myCalendar;
        function doOnLoad() {
            myCalendar = new dhtmlXCalendarObject(["startDate", "endDate"]);
        }
    </script>
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script language="JavaScript" type="text/JavaScript">

        function selectAllX(obj, contentId){
            var chkObj = document.getElementById("div_" +contentId).getElementsByTagName("input");
            for(var i=0;i<chkObj.length;i++){
                chkObj[i].checked = obj.checked;
            }
        }

        function modifyImgStatus(uno, contentId, originalValue, auditStatus){

            //判断checkbox是否被选中
            var odiv = document.getElementById("div_" + contentId);
            var oinput = odiv.getElementsByTagName("input");
            for(var i=0;i<oinput.length;i++){
                if(oinput[i].checked){
                   break;
                }
                if((i == oinput.length-1) && !oinput[i].checked){
                    return false;
                }
            }

            var chkObj = document.getElementById("chkdiv_" + contentId);
            var chko = document.getElementById("chk_" + contentId)
            if(chko != null){
                chkObj.removeChild(chko);
            }


            var url = "/json/content/modifyimgstatus";
            var xmlHttp;
            if(window.XMLHttpRequest){
                xmlHttp = new XMLHttpRequest();
            }else{
                xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
            }

            xmlHttp.onreadystatechange = function(){
                if(xmlHttp.readyState == 4 && xmlHttp.status == 200){
                    var tab_id = "tb_" + contentId;
                    var tableObj = document.getElementById(tab_id);
                    var divObject = document.getElementById("div_" + contentId);
                    divObject.removeChild(tableObj);
                    var jsonResult = xmlHttp.responseText;
                    if(jsonResult != null){

                    }
                    var json = eval("(" + jsonResult + ")");

                    json = eval("(" + json.msg + ")");
                    var size = json.length;

                    var text = "<table id=\""+tab_id + "\" style=\"table-layout: fixed\">";
                    var tips;
                    if(auditStatus == 12){
                        tips="该图片已通过审核";
                    }else{
                        tips="该图片已被屏蔽";
                    }
                    for(var i=0; i<size;i++){
                        if(i == 0 || i % 5 == 0){
                            text +="<tr>";
                        }
                        var imgurl = json[i].m;
                        imgurl = "http://" + imgurl.substring(1,5)+".${DOMAIN}" +json[i].m;

                        if(auditStatus == 12){
                            if(json[i].validStatus ){
                                text+="<td align=\"center\" height='180' width='180'>"+ tips +
                                                    "<br/>" +
                                                    "<input type=\"checkbox\" name=\"values\" value=\""+json[i].url+"\">"+
                                                    "</td>"
                            } else{
                                text+="<td align=\"center\" height='180' width='180'>"+
                                                    "<img src=\""+imgurl +"\" height=\"160\", width=\"170\" onclick = 'window.open(\""+imgurl+"\","+"\"_bank\")'/><br/>"+
                                                    "<input type=\"checkbox\" name=\"values\" value=\""+json[i].url+"\">"+
                                                    "</td>" ;
                            }
                        } else{
                            if(!json[i].validStatus ){
                                text+="<td align=\"center\" height='180' width='180'>"+ tips +
                                                    "<br/>" +
                                                    "<input type=\"checkbox\" name=\"values\" value=\""+json[i].url+"\">"+
                                                    "</td>"
                            } else{
                                text+="<td align=\"center\" height='180' width='180'>"+
                                                    "<img src=\""+imgurl +"\" height=\"160\", width=\"170\" onclick = 'window.open(\""+imgurl+"\","+"\"_bank\")'/><br/>"+
                                                    "<input type=\"checkbox\" name=\"values\" value=\""+json[i].url+"\">"+
                                                    "</td>" ;
                            }
                        }

                        if((i+1)%5==0 ||(i+1)==5){
                                        text+="</tr>";
                        }
                    }
                    text+= "</table>";
                    divObject.innerHTML = text;
                }
            }

            xmlHttp.open("POST", url, true);
            xmlHttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
            xmlHttp.send(getRequestBody(uno, contentId, originalValue, auditStatus));
        }
        function getRequestBody(uno, contentId, originalValue, auditStatus){
            var params = "uno=" + uno +"&contentid=" + contentId + "&originalvalue=" + originalValue;
            var odiv = document.getElementById("div_" + contentId);
            var oinput = odiv.getElementsByTagName("input");
            var count = 0;
            for(var i=0;i<oinput.length;i++){
                if(oinput[i].checked){
                    params += "&snglimg="+oinput[i].value;
                    count++;
                }
            }
//            if(oinput.length == count){
//                params+="&chk=all";
//            }else{
//                params+="&chk=notall";
//            }
            params+="&auditstatus=" + auditStatus;

            return params;
        }

        function optAllPass2(contentids){

            var flag = false;
            var checkobj = document.getElementsByName(contentids);
            for(var i=0;i<checkobj.length;i++){
                if(checkobj[i].checked){
                    flag = true;
                }
            }

            if(flag){
                var url = "/audit/content/imgbatchupdate";
                var form2checkbox = document.batchform.contentids;

                for(var i = 0;i<form2checkbox.length;i++){
                    if(form2checkbox[i].checked){

                        url+=("&contentids=" + form2checkbox[i].value);
                    }
                }
                url = url.replace('&', '?');

                document.batchform1.action = url;

                document.batchform1.submit();
                return true;
            }else{
                return false;
            }

        }

    </script>
</head>

<body onload="doOnLoad();">

<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 发布内容管理 >> 发布内容图片审核</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">>文章图片查询列表</td>
                </tr>
            </table>
            <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <form action="/audit/content/imglist" method="POST">
                    <tr>
                        <td width="80" align="center">搜索条件</td>
                        <td>
                            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                                <tr>
                                    <td width="100" align="right" class="edit_table_defaulttitle_td">作者：</td>
                                    <td class="edit_table_value_td">
                                        <input name="screenname" type="text" class="default_input_singleline" size="16" maxlength="32" value="${screenname}">
                                        [暂不可模糊搜索]
                                    </td>
                                    <td width="100" align="right" class="edit_table_defaulttitle_td">创建时间：</td>
                                    <c:set var="startDate"><fmt:formatDate value="${params.startDate}" pattern="yyyy-MM-dd"/></c:set>
                                    <c:set var="endDate"><fmt:formatDate value="${params.endDate}" pattern="yyyy-MM-dd"/></c:set>
                                    <td class="edit_table_value_td">
                                        <input id="startDate" name="startdate" type="text" class="default_input_singleline" size="8" maxlength="10" value="${startDate}">
                                        -
                                        <input id="endDate" name="enddate" type="text" class="default_input_singleline" size="8" maxlength="10" value="${endDate}" >
                                    </td>
                                </tr>
                                <tr>
                                    <td width="100" align="right" class="edit_table_defaulttitle_td"></td>
                                    <td class="edit_table_value_td">

                                    </td>
                                    <td width="100" align="right" class="edit_table_defaulttitle_td">范围：</td>
                                    <td class="edit_table_value_td">
                                        <select name="auditstatus" id="all" class="default_select_single">
                                            <c:forEach items="${imageStatus.keySet()}" var="statusKey">
                                                <option value="${statusKey}"
                                                    <c:if test="${statusKey eq auditstatus}">
                                                        selected="selected"
                                                    </c:if>
                                                        ><fmt:message key="${imageStatus.get(statusKey)}" bundle="${toolsProps}"/></option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td width="80" align="center">
                            <input name="Button" type="submit" class="default_button" value=" 搜索 ">
                        </td>
                    </tr>
                </form>
            </table>
            <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                <form action="/audit/content/imgbatchupdate" method="POST" name="batchform1">
                    <c:choose>
                        <c:when test="${rows.size() > 0}">
                            <tr class="toolbar_tr">
                                <input type="hidden" name="startdate" value="${startDate}">
                                <input type="hidden" name="enddate" value="${endDate}">
                                <input type="hidden" name="screenname" value="${screenname}">
                                <input type="hidden" name="pager.offset" value="${page.startRowIdx}">
                                <input type="hidden" name="items" value="${page.totalRows}">
                                <input type="hidden" name="auditstatus" value="${auditstatus}">
                                <td>
                                    <input type="checkbox" name="selectall" value="1"
                                           onclick='javascript:checkall(document.forms["batchform"].contentids, document.forms["batchform1"].selectall)'>全选
                                    <input type="checkbox" name="uncheck" value="1" onclick='javascript:convertcheck(document.forms["batchform"].contentids)'>反选
                                    状态改成：
                                    <select name="updateRemoveStatusCode" class="default_select_single">
                                        <option value="4" <c:if test="${updateRemoveStatusCode eq '4'}">selected="selected"</c:if>>通过</option>
                                        <option value="8" <c:if test="${updateRemoveStatusCode eq '8'}">selected="selected"</c:if>>屏蔽</option>
                                    </select>
                                    <p:privilege name="/audit/content/imgbatchupdate">
                                        <input name="update" type="button" class="default_button" value="批量修改" onclick="return optAllPass2('contentids');">
                                    </p:privilege>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    <c:if test="${page.maxPage > 1}">
                                        <LABEL>
                                            <pg:pager url="imglist"
                                                items="${page.totalRows}" isOffset="true" maxPageItems="${page.pageSize}" export="offset, currentPageNumber=pageNumber" scope="request">
                                                <pg:param name="maxPageItems" value="${page.pageSize}"/><!--不能缺少的-->
                                                <pg:param name="items" value="${page.totalRows}"/>
                                                <pg:param name="startdate" value="${startDate}"/>
                                                <pg:param name="enddate" value="${endDate}"/>
                                                <pg:param name="screenname" value="${screenname}"/>
                                                <pg:param name="auditstatus" value="${auditstatus}"/>
                                            <%@ include file="/WEB-INF/jsp/toolspg.jsp" %>
                                            </pg:pager>
                                        </LABEL>
                                    </c:if>
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="6" class="error_msg_td"></td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </form>
            </table>
            <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr class="list_table_title_tr">
                    <td nowrap align="center" width="30">选择</td>
                    <td nowrap align="center">发布图片内容</td>
                    <td nowrap align="center">操作</td>
                    <td nowrap align="center">作者</td>
                    <td nowrap align="center" width="140">最后时间/发贴IP</td>
                    <td nowrap align="center">链接</td>
                </tr>
                <tr>
                    <td height="1" colspan="6" class="default_line_td"></td>
                </tr>
                <form name="batchform" action="/audit/content/imgbatchupdate" method="POST">
                    <input type="hidden" name="startdate" value="${startDate}">
                    <input type="hidden" name="enddate" value="${endDate}">
                    <input type="hidden" name="screenname" value="${screenname}">
                    <input type="hidden" name="pager.offset" value="${page.startRowIdx}">
                    <input type="hidden" name="items" value="${page.totalRows}">
                    <input type="hidden" name="auditstatus" value="${auditstatus}">
                    <c:choose>
                        <c:when test="${rows.size() > 0}">
                            <c:forEach items="${rows}" var="entity" varStatus="st">
                                <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                    <td align="center">
                                        <div id="chkdiv_${entity.content.contentId}">
                                            <input type="checkbox" id="chk_${entity.content.contentId}" name="contentids" value="${entity.content.contentId}@${entity.profile.blog.uno}@${entity.content.auditStatus.value}" onclick="selectAllX(this, '${entity.content.contentId}')"/>
                                        </div>
                                    </td>
                                    <td align="left">
                                        <div id="div_${entity.content.contentId}">
                                            <table id="tb_${entity.content.contentId}" style="table-layout: fixed">
                                                <c:forEach items="${entity.content.images.images}" var="img" varStatus="countt">
                                                    <c:if test="${countt.count eq 1 || (countt.count-1)%5 eq 0}">
                                                        <tr>
                                                    </c:if>
                                                    <c:choose>
                                                        <c:when test="${auditstatus eq 12}">
                                                            <c:choose>
                                                                <c:when test="${!img.validStatus}">
                                                                    <td align="left" width="180" height="180">
                                                                        <img src="<c:out value="${uf:parseMFace(img.url)}"/>" height="160" width="170" onclick='window.open("<c:out value="${uf:parseBFace(img.url)}"/>","_blank");'/><br/> <!--style="filter: Alpha(opacity=10);-moz-opacity:0.1;opacity:0.1;"-->
                                                                        <input type="checkbox" name="values" value="${img.url}">
                                                                    </td>
                                                                    <c:if test="${countt.last}">
                                                                        <c:forEach begin="${countt.count + 1}" end="5">
                                                                            <td align="left" width="180" height="180">
                                                                               &nbsp;
                                                                            </td>
                                                                        </c:forEach>
                                                                    </c:if>
                                                                </c:when>
                                                                <c:otherwise>
                                                                          <!--<td align="left">
                                                                      ${!img.validStatus}    </td>-->
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:choose>
                                                                <c:when test="${img.validStatus}">
                                                                    <td align="left" height="180" width="180">
                                                                        <img src="<c:out value="${uf:parseMFace(img.url)}"/>" height="160" width="170" onclick='window.open("<c:out value="${uf:parseBFace(img.url)}"/>","_blank");'/><br/> <!--style="filter: Alpha(opacity=10);-moz-opacity:0.1;opacity:0.1;"-->
                                                                        <input type="checkbox" name="values" value="${img.url}">
                                                                    </td>
                                                                    <c:if test="${countt.last}">
                                                                        <c:forEach begin="${countt.count + 1}" end="5">
                                                                            <td align="left" width="180" height="180">
                                                                               &nbsp;
                                                                            </td>
                                                                        </c:forEach>
                                                                    </c:if>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                              <!--<td align="left">
                                                                              </td>-->
                                                                    </c:otherwise>
                                                            </c:choose>
                                                        </c:otherwise>
                                                    </c:choose>

                                                    <c:if test="${countt.count%5 eq 0 || countt.last}">
                                                        </tr>
                                                    </c:if>
                                                </c:forEach>
                                            </table>
                                        </div>
                                    </td>

                                    <td>
                                        <p:privilege name="/json/content/modifyimgstatus">
                                            <input type="button"
                                                <c:choose>
                                                    <c:when test="${auditstatus eq 12}">
                                                        value="通过"
                                                    </c:when>
                                                    <c:otherwise>
                                                        value="屏蔽"
                                                    </c:otherwise>
                                                </c:choose>
                                                    onclick='modifyImgStatus("${entity.profile.blog.uno}", "${entity.content.contentId}", "${entity.content.auditStatus.value}","${auditstatus}");' >
                                        </p:privilege>
                                    </td>
                                    <td align="left" width="100px">
                                        <!-- <input type=hidden name=screenNames value="">
                                         <input type="hidden" id = "original" name="originalValue" value="">  -->
                                        ${entity.profile.blog.screenName}
                                    </td>
                                    <td align="left" width="140px">
                                         <fmt:formatDate value="${entity.content.publishDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                         <br/>
                                         ${entity.content.publishIp}
                                    </td>
                                    <td align="left" width="25px">
                                        <a href="${URL_WWW}/note/${entity.content.contentId}" target="_blank">详情</a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <tr>
                                <td colspan="6" height="1" class="default_line_td"></td>
                            </tr>
                            <tr class="toolbar_tr">
                                <td colspan="6">
                                    <input type="checkbox" name="selectall" value="1"
                                           onclick='javascript:checkall(document.forms["batchform"].contentids, document.forms["batchform"].selectall)'>全选
                                    <input type="checkbox" name="uncheck" value="1" onclick='javascript:convertcheck(document.forms["batchform"].contentids)'>反选
                                    状态改成：
                                    <select name="updateRemoveStatusCode" class="default_select_single">
                                        <option value="4" <c:if test="${updateRemoveStatusCode eq '4'}">selected="selected"</c:if>>通过</option>
                                        <option value="8" <c:if test="${updateRemoveStatusCode eq '8'}">selected="selected"</c:if>>屏蔽</option>
                                    </select>
                                    <p:privilege name="/audit/content/imgbatchupdate">
                                        <input name="update" type="submit" class="default_button" value="批量修改" >
                                    </p:privilege>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    <c:if test="${page.maxPage > 1}">
                                        <LABEL>
                                            <pg:pager url="imglist"
                                                items="${page.totalRows}" isOffset="true" maxPageItems="${page.pageSize}" export="offset, currentPageNumber=pageNumber" scope="request">
                                                <pg:param name="maxPageItems" value="${page.pageSize}"/><!--不能缺少的-->
                                                <pg:param name="items" value="${page.totalRows}"/>
                                                <pg:param name="startdate" value="${startDate}"/>
                                                <pg:param name="enddate" value="${endDate}"/>
                                                <pg:param name="screenname" value="${screenname}"/>
                                                <pg:param name="auditstatus" value="${auditstatus}"/>
                                            <%@ include file="/WEB-INF/jsp/toolspg.jsp" %>
                                            </pg:pager>
                                        </LABEL>
                                    </c:if>
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="6" class="error_msg_td">暂无数据!</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>

                </form>
            </table>
        </td>
    </tr>
</table>
</body>
</html>