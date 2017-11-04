<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台数据管理、后台个人信息审核列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/dhtmlxcalendar.css">
    <link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/skins/dhtmlxcalendar_dhx_skyblue.css">
    <script src="/static/include/dhtmlxcalendar/dhtmlxcalendar.js"></script>
<style>
*{margin:0;padding:0px;}
body{ height: 100%;width: 100%;}
.span{position:absolute;right:3px;top:3px;display:block;cursor:pointer;color:#fff;font-weight:bold;}
</style>
    <script>
        var myCalendar;
        function doOnLoad(startDate, endDate) {
            myCalendar = new dhtmlXCalendarObject([startDate, endDate]);
        }
    </script>
    <script language="JavaScript" type="text/JavaScript">

        function selectAll(obj, itemName)
        {
            var boxes = document.getElementsByName(itemName);
            for(var i=0;i<boxes.length;i++){
                boxes[i].checked = obj.checked;
            }
            var unselected = document.getElementById("unchk");
            unselected.checked = false;
        }
        function unselected(obj, itemName)
        {
            var selected = document.getElementById("chkall");
            selected.checked = false;
            var boxes = document.getElementsByName(itemName);
            for(var i=0;i<boxes.length;i++){
                if(boxes[i].checked){
                    boxes[i].checked = false;
                }else {
                    boxes[i].checked = true;
                }
            }
        }

        function restoreImgUrl(uno, moreheadicon){
            var url = "/json/profile/restore?uno=" + uno + "&moreheadicon=" + moreheadicon + "&timestamp=" + new Date().getTime();

            var xmlHttp;
//            alert(url);
            if(window.XMLHttpRequest){
                xmlHttp = new XMLHttpRequest();
            }else{
                xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
            }

            xmlHttp.onreadystatechange = function(){
                if(xmlHttp.readyState==4 && xmlHttp.status==200){
                    var jsonResult = xmlHttp.responseText;
                    var jsonObj = eval("(" + jsonResult + ")");
                    var newArray = eval("(" + jsonObj.msg + ")");
                    if(jsonObj.status_code == '1'){
                        alert("恢复成功");
                        var text = "<div id=\"" + uno + "\">" +
                            "<table id=\"table_" + uno + "\">";
                                for(var i=0;i<newArray.length;i++){
                                    if(i%4 == 0 || i == 0){
                                    text+="<tr align=\"left\">";
                                    }
                                        text+="<td align=\"left\">";
                                                var imgurl = newArray[i].headIcon;
                                                imgurl = "http://" + imgurl.substring(1,5)+".${DOMAIN}" +newArray[i].headIcon;
                                                if(newArray[i].validStatus){
                                                    text+="<img src=\"" + imgurl + "\" height=\"100\" width=\"100\" onclick='window.open(\"" + imgurl + "\",\"_blank\");'/><br/>" +
                                                    "<input type=\"checkbox\" name=\"moreheadicon\" value=\"" + newArray[i].headIcon + "\">";
                                                }else{
                                                    text+=  "<img src=\"" + imgurl + "\" height=\"100\" width=\"100\" onclick='window.open(\"" + imgurl + "\",\"_blank\");' style=\"filter:gray\"/><br/>" +
                                                            "该图片已被屏蔽<br/>" +
                                                    "<input type=\"button\" name=\"\" value=\"取消屏蔽\" onclick=\"restoreImgUrl('" + uno + "','" + newArray[i].headIcon + "');\">";
                                                }
                                        text+="</td>";
                                         if((i+1)%4 == 0){
                                        text+="</tr>";
                                         }
                                    if(i == newArray.length-1){
                                        for(var j=i+1;j<4-i;j++){
                                        text+="<td align=\"left\" width=\"100\" height=\"100\">" +
                                            "&nbsp;" +
                                        "</td>";
                                        }
                                    text+="</tr>";
                                    }
                                }
                            text+="</table>"  +
                        "</div> ";

                        document.getElementById(uno).innerHTML = text;
                    }else{
                        alert("恢复失败");
                    }
                }
            }

            xmlHttp.open("GET", url, true);
            xmlHttp.send();
        }

        function deleteDesc(uno){
            if(!confirm('确定要删除个人简介吗？')){
                return false;
            }
            //要加一个时间戳，不然get请求服务器会有缓存ajax不执行
            var url = "/json/profile/deldesc?uno=" + uno + "&timestamp=" + new Date().getTime();
//            alert(url);
            var xmlHttp;
            if(window.XMLHttpRequest){
                xmlHttp = new XMLHttpRequest();
            }else{
                xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
            }

            xmlHttp.onreadystatechange = function(){
                if(xmlHttp.readyState==4 && xmlHttp.status==200){
                    var jsonResult = xmlHttp.responseText;
                    var jsonObj = eval("(" + jsonResult + ")");
                    if(jsonObj.status_code == '1'){
                        alert("删除成功");
                        document.getElementById("div1_" + uno).innerHTML = jsonObj.msg + "<font color=\"red\">已被删除</font>";
                        document.getElementById("div2_" + uno).innerHTML = "已审核";
                    }else{
                        alert("删除失败");
                    }
                }
            }

            xmlHttp.open("GET", url, true);
            xmlHttp.send();
        }


        function restoreDesc(uno){

            //要加一个时间戳，不然get请求服务器会有缓存ajax不执行
            var url = "/json/profile/restoredesc?uno=" + uno + "&timestamp=" + new Date().getTime();
            var xmlHttp;
            if(window.XMLHttpRequest){
                xmlHttp = new XMLHttpRequest();
            }else{
                xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
            }

            xmlHttp.onreadystatechange = function(){
                if(xmlHttp.readyState==4 && xmlHttp.status==200){
                    var jsonResult = xmlHttp.responseText;
                    var jsonObj = eval("(" + jsonResult + ")");
                    if(jsonObj.status_code == '1'){
                        alert("恢复成功");
                        document.getElementById("div1_" + uno).innerHTML = jsonObj.msg;
                    }else{
                        alert("恢复失败");
                    }
                }
            }

            xmlHttp.open("GET", url, true);
            xmlHttp.send();
        }

        function deleteHeadIcon(uno){
            if(!confirm('确定要删除该头像吗？')){
                return false;
            }
            var url = "/json/profile/delicon?timestamp=" +  new Date().getTime();
            var xmlHttp;
            if(window.XMLHttpRequest){
                xmlHttp = new XMLHttpRequest();
            }else{
                xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
            }
            xmlHttp.onreadystatechange = function(){
                if(xmlHttp.readyState==4 && xmlHttp.status==200){
                    var jsonResult = xmlHttp.responseText;
                    var jsonObj = eval("(" + jsonResult + ")");
                    var newArray = eval("(" + jsonObj.msg + ")");
//                    alert(newArray[0].validStatus);
                    if(jsonObj.status_code == '1'){
                    var text = "<div id=\"" + uno + "\">" +
                            "<table id=\"table_" + uno + "\">";
                                <%--<c:forEach items="${ct.profileBlog.headIconSet.iconSet}" var="img" varStatus="imgCount">--%>
                                for(var i=0;i<newArray.length;i++){
                                    <%--<c:if test="${(imgCount.count)%5 eq 0 || imgCount.count == 1}">--%>
                                    if(i%4 == 0 || i == 0){
                                    text+="<tr align=\"left\">";
                                    <%--</c:if>--%>
                                    }
                                        text+="<td align=\"left\">";
                                            <%--<c:choose>--%>
                                                <%--<c:when test="${img.validStatus}">--%>
                                                var imgurl = newArray[i].headIcon;
                                                imgurl = "http://" + imgurl.substring(1,5)+".${DOMAIN}" +newArray[i].headIcon;
                                                if(newArray[i].validStatus){
                                                    text+="<img src=\"" + imgurl + "\" height=\"100\" width=\"100\" onclick='window.open(\"" + imgurl + "\",\"_blank\");'/><br/>" +
                                                    "<input type=\"checkbox\" name=\"moreheadicon\" value=\"" + newArray[i].headIcon + "\">";
                                                <%--</c:when>--%>
                                                }else{
                                                <%--<c:otherwise>--%>
                                                    text+=  "<img src=\"" + imgurl + "\" height=\"100\" width=\"100\" onclick='window.open(\"" + imgurl + "\",\"_blank\");' style=\"filter:gray\"/><br/>" +
                                                            "该图片已被屏蔽<br/>" +
                                                    "<input type=\"button\" name=\"\" value=\"取消屏蔽\" onclick=\"restoreImgUrl('" + uno + "','" + newArray[i].headIcon + "');\">";
                                                <%--</c:otherwise>--%>
                                            <%--</c:choose>--%>
                                                }
                                        text+="</td>";
                                    <%--<c:if test="${ imgCount.count%4 eq 0 }">--%>
                                         if((i+1)%4 == 0){
                                        text+="</tr>";
                                    <%--</c:if>--%>
                                         }
                                    <%--<c:if test="${imgCount.last}">--%>
                                    if(i == newArray.length-1){
                                        <%--<c:forEach begin="${imgCount.count + 1}" end="4">--%>
                                        for(var j=i+1;j<4-i;j++){
                                        text+="<td align=\"left\" width=\"100\" height=\"100\">" +
                                            "&nbsp;" +
                                        "</td>";
                                        <%--</c:forEach>--%>
                                        }
                                    text+="</tr>";
                                    <%--</c:if>--%>
                                    }
                                <%--</c:forEach>--%>
                                }
                            text+="</table>"  +
                        "</div> ";

                        document.getElementById(uno).innerHTML = text;
                    }
//                    var divobj = document.getElementById(uno);
//                    var tableobj = document.getElementById("table_" + uno);
//                    divobj.removeChild(tableobj);
                }
            }

            xmlHttp.open("POST", url, true);
            xmlHttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
            var param = "uno=" + uno;
            var checkboxobj = document.getElementById(uno).getElementsByTagName("input");
            for(var i=0;i<checkboxobj.length;i++){
                if(checkboxobj[i].checked){
//                    if(checkboxobj[i].name == "uno"){
////                        alert(checkboxobj[i]);
//                        param += "&headicon=y"
//                        continue;
//                    }
                    param += "&moreheadicon=" + checkboxobj[i].value;
                }
            }
//            alert(param);
            xmlHttp.send(param);
        }

        function optAllPass(uno){
            var flag = false;
            var checkobj = document.getElementsByName(uno);
            for(var i=0;i<checkobj.length;i++){
                if(checkobj[i].checked){
                    flag = true;
                }
            }

            if(flag){
                var url = "/audit/profile/allpass";
                var form2checkbox = document.form3.applyid;

                for(var i = 0;i<form2checkbox.length;i++){
                    if(form2checkbox[i].checked){
                        url +="&applyid=" + form2checkbox[i].value;
                    }
                }
                url = url.replace("&", "?");

                document.form2.action = url;
                document.form2.submit();
                return true;
            }else{
                return false;
            }

        }

        function optAllPass2(uno){
            var flag = false;
            var checkobj = document.getElementsByName(uno);
            for(var i=0;i<checkobj.length;i++){
                if(checkobj[i].checked){
                    flag = true;
                }
            }
            if(flag){
                document.form3.action='/audit/profile/allpass';
                document.form3.submit();
                return true;
            }else{
                return false;
            }

        }

    function delplayinggames(uno, playingGames) {
            var gnl = confirm("你真的确定要删除正在玩吗?");
            if (!gnl) {
                return false;
            }
        $.ajax({
            url: '/json/profile/delplayinggames',
            type: 'post',
            data:{'uno':uno,'playinggames':playingGames},
            dataType:"json",
            error: function(){
                alert('操作错误,请与系统管理员联系!');
             },
            success: function(data){
                if(data.status_code=="1"){
                    alert("删除成功！！！");
                }
            }
            });

        }
    </script>
</head>

<body <%--onload="doOnLoad('createStartDate', 'createEndDate');"--%>>


<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <!--header-->
    <tr>
        <td height="22" class="page_navigation_td">>> 博客管理 >> 博客信息审核</td>
    </tr>
    <!--query filter-->
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td class="list_table_header_td">博客信息审核列表</td>
                </tr>
            </table>
            <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%"  border="0" cellspacing="0" cellpadding="0">
            <form action="/audit/profile/profilelist" method="POST">
                <tr>
                    <td width="80" align="center">搜索条件</td>
                    <td>
                        <table width="100%"  border="0" cellspacing="1" cellpadding="0">
                            <tr>
                                <td width="100" align="right" class="edit_table_defaulttitle_td">昵&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称:</td>
                                <td class="edit_table_value_td"><input name="screenName" type="text" class="default_input_singleline" size="16" maxlength="32" value="${params.screenName}">
                                [不可模糊选择]</td>
                                <td width="100" align="right" class="edit_table_defaulttitle_td">排序方式:</td>
                                <td class="edit_table_value_td"><select name="sorttype" class="default_select_single">
                                    <option value="updatedatedesc"
                                            <c:if test='${params.sortType == "updatedatedesc"}'>
                                                selected="selected"
                                            </c:if>>用户最后更新时间倒序</option>
                                    <option value="createdatedesc"
                                            <c:if test='${params.sortType == "createdatedesc"}'>
                                                selected="selected"
                                            </c:if>>用户创建博客时间倒序</option>
                                </select></td>
                            </tr>
                            <tr>
                            <c:set var="createstart"><fmt:formatDate value="${params.startDate}" pattern="yyyy-MM-dd"/></c:set>
                            <c:set var="createend"><fmt:formatDate value="${params.endDate}" pattern="yyyy-MM-dd"/></c:set>
                                <td width="100" align="right" class="edit_table_defaulttitle_td">博客创建起止时间:</td>
                                <td class="edit_table_value_td" id="createDate"><input id="createStartDate" name="createstartdate" type="text" class="default_input_singleline" size="8" maxlength="10" value="${createstart}" onMouseUp="doOnLoad('createStartDate', 'createEndDate');">
                                    -<input id="createEndDate" name="createenddate" type="text" class="default_input_singleline" size="8" maxlength="10" value="${createend}" onMouseUp="doOnLoad('createStartDate', 'createEndDate');">[格式为：2011-11-20]
                                    <%--<img src="../images/icon/lookupdate.gif" name="dateImg" alt="Select Date" width="15" height="13" border="0" align="absmiddle" onMouseUp="toggleDatePicker('dateImg','datediv','schForm.ddd')">--%>
                                    <div id=datediv style="POSITION: absolute; left: 299px; top: 114px; height: 10; width: 10; z-index: 10;"></div>
                                </td>
                                <c:set var="startUpDate"><fmt:formatDate value="${params.startUpDate}" pattern="yyyy-MM-dd"/></c:set>
                                <c:set var="endUpDate"><fmt:formatDate value="${params.endUpDate}" pattern="yyyy-MM-dd"/></c:set>
                                <td width="100" align="right" class="edit_table_defaulttitle_td">最后修改起止时间: </td>
                                <td class="edit_table_value_td"><input name="startDate" id="updateStartDate" type="text" class="default_input_singleline" size="8" maxlength="10" value="${startUpDate}" onMouseUp="doOnLoad('updateStartDate', 'updateEndDate');">
                                    -<input name="endDate" id="updateEndDate" type="text" class="default_input_singleline" size="8" maxlength="10" value="${endUpDate}" onMouseUp="doOnLoad('updateStartDate', 'updateEndDate');">[格式为：2011-11-20]
                                    <%--<img src="../images/icon/lookupdate.gif" alt="Select Date" width="15" height="13" border="0" align="absmiddle"></td>--%>
                            </tr>

                            <tr>
                                <td width="100" align="right" class="edit_table_defaulttitle_td">范&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;围:</td>
                                <td class="edit_table_value_td"><input type="radio" name="audit" value="0" id="unaudit"
                                    <c:if test='${!params.auditStatus.hasAudit()}'>
                                        checked="checked"
                                    </c:if>> 未审核</input>
                                    <input type="radio" name="audit" value="1" id="audited"
                                            <c:if test='${params.auditStatus.hasAudit()}'>
                                                checked="checked"
                                            </c:if>> 已审核</input>
                                    <div style="POSITION: absolute; left: 299px; top: 114px; height: 10; width: 10; z-index: 10;"></div>
                                </td>
                                <td width="100" align="right" class="edit_table_defaulttitle_td">&nbsp; </td>
                                <td class="edit_table_value_td">&nbsp;</td>
                                    <%--<img src="../images/icon/lookupdate.gif" alt="Select Date" width="15" height="13" border="0" align="absmiddle">--%>
                            </tr>
                    </table></td>
                    <td width="80" align="center">
                        <input name="Submit" type="submit" class="default_button" value=" 搜索 ">
                    </td>
                </tr>
            </form> <!--form1 end-->
            </table>
            <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%"  border="0" cellspacing="0" cellpadding="0">
            <form method="POST" name="form2">
                <input type="hidden" name="createstartdate" value="${createstart}">
                <input type="hidden" name="createenddate" value="${createend}">
                <input type="hidden" name="startDate" value="${startUpDate}">
                <input type="hidden" name="endDate" value="${endUpDate}">
                <input type="hidden" name="screenName" value="${params.screenName}">
                <input type="hidden" name="audit" value="${params.auditStatus.value}">
                <input type="hidden" name="sorttype" value="${params.sortType}">
                <input type="hidden" name="items" value="${page.totalRows}">
                <input type="hidden" name="pager.offset"  value="${page.startRowIdx}">
                <tr class="toolbar_tr">
                    <td>
                        <nobr>全选<input type="checkbox" id="chkall2" onclick="selectAll(this, 'applyid')"/></nobr>
                        <nobr>反选<input type="checkbox" id="unchk2" onclick="unselected(this, 'applyid')"/></nobr>
                        <c:if test="${!params.auditStatus.hasAudit()}">
                        <p:privilege name="/audit/profile/allpass">
                            <input name="Submit" type="submit" class="default_button" value="通&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;过" onclick="return optAllPass('applyid'); ">
                        </p:privilege>
                        </c:if>
                    </td>
                    <td height="1" align="right">
                        <LABEL>
                            <pg:pager url="profilelist"
                                items="${page.totalRows}" isOffset="true" maxPageItems="${page.pageSize}" export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                <pg:param name="items" value="${page.totalRows}"/>
                                <pg:param name="createstartdate" value="${createstart}"/>
                                <pg:param name="createenddate" value="${createend}"/>
                                <pg:param name="startDate" value="${startUpDate}"/>
                                <pg:param name="endDate" value="${endUpDate}"/>
                                <pg:param name="screenName" value="${params.screenName}"/>
                                <pg:param name="audit" value="${params.auditStatus.value}"/>
                                <pg:param name="sorttype" value="${params.sortType}"/>
                            <%@ include file="/WEB-INF/jsp/toolspg.jsp" %>
                            </pg:pager>
                        </LABEL>
                    </td>
                </tr>
            </form><!--form2 end-->
            </table>
            <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>

            <!--列表主体-->
            <form action="" name="form3" method="POST">
                <input type="hidden" name="createstartdate" value="${createstart}">
                <input type="hidden" name="createenddate" value="${createend}">
                <input type="hidden" name="startDate" value="${startUpDate}">
                <input type="hidden" name="endDate" value="${endUpDate}">
                <input type="hidden" name="screenName" value="${params.screenName}">
                <input type="hidden" name="audit" value="${params.auditStatus.value}">
                <input type="hidden" name="sorttype" value="${params.sortType}">
                <input type="hidden" name="items" value="${page.totalRows}">
                <input type="hidden" name="pager.offset"  value="${page.startRowIdx}">
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr class="list_table_title_tr">
                    <td nowrap align="center">
                        选择
                    </td>
                    <td nowrap align="center">昵称/域名/邮箱</td>
                    <td nowrap align="center">头像</td>
                    <td nowrap align="center">个人简介</td>
                    <td nowrap align="center">正在玩</td>
                    <td nowrap align="center">最后修改时间</td>
                    <td nowrap align="center">状态</td>
                    <td nowrap align="center">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="7" class="default_line_td"></td>
                </tr>
                <c:forEach items="${rows}" var="ct" varStatus="st">
                    <tr class="
                        <c:choose>
                        <c:when test="${st.index % 2 == 0}">
                           list_table_opp_tr
                        </c:when>
                        <c:otherwise>
                            list_table_even_tr
                        </c:otherwise>
                        </c:choose>
                            ">
                        <!--uno-->
                        <td align="center">
                            <input type="checkbox" id="chk_${ct.profileBlog.uno}" name="applyid"
                                   value="${ct.profileBlog.uno}"/>
                        </td>
                        <!--昵称/站内个性域名/邮箱-->
                        <td align="left">${ct.profileBlog.screenName}<br>
                            ${ct.profileBlog.domain}<br>
                            <c:forEach items="${ct.accountList}" var="accounts" >
                                ${accounts.userid} </br>
                            </c:forEach>
                        </td>
                        <!--多头像-->
                        <td align="center" width="400px">
                        <div id="${ct.profileBlog.uno}">
                            <table id="table_${ct.profileBlog.uno}">
                                <c:forEach items="${ct.profileBlog.headIconSet.iconSet}" var="img" varStatus="imgCount">
                                    <c:if test="${(imgCount.count)%5 eq 0 || imgCount.count == 1}">
                                    <tr align="left">
                                    </c:if>
                                        <td align="left">
                                            <c:choose>
                                                <c:when test="${img.validStatus}">
                                                    <img src="<c:out value="${uf:parseMFace(img.headIcon)}"/>" height="100" width="100" onclick='window.open("<c:out value="${uf:parseBFace(img.headIcon)}"/>","_blank");'/><br/>
                                                    <input type="checkbox" name="moreheadicon" value="${img.headIcon}">
                                                </c:when>
                                                <c:otherwise>
                                                    <img src="<c:out value="${uf:parseMFace(img.headIcon)}"/>" height="100" width="100" onclick='window.open("<c:out value="${uf:parseBFace(img.headIcon)}"/>","_blank");' style="filter:gray"/><br/>
                                                    该图片已被屏蔽<br/>
                                                    <%--<p:privilege name="json/profile/restore">--%>
                                                        <input type="button" name="" value="取消屏蔽" onclick="restoreImgUrl('${ct.profileBlog.uno}','${img.headIcon}');">
                                                    <%--</p:privilege>--%>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    <c:if test="${ imgCount.count%4 eq 0 }">
                                        </tr>
                                    </c:if>
                                    <c:if test="${imgCount.last}">
                                        <c:forEach begin="${imgCount.count + 1}" end="4">
                                        <td align="left" width="100" height="100">
                                            &nbsp;
                                        </td>
                                        </c:forEach>
                                    </tr>
                                    </c:if>
                                </c:forEach>
                            </table>
                        </div>
                        </td>
                        <!--个人简介-->
                        <td align="center" width="300px" style="word-break:break-all">
                            <div  id="div1_${ct.profileBlog.uno}">
                                <c:choose>
                                    <c:when test="${ct.profileBlog.auditStatus.isBlogDescIllegal()}">
                                        ${ct.profileBlog.description}<font color="red">已被删除</font>
                                    </c:when>
                                    <c:otherwise>
                                        ${ct.profileBlog.description}
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </td>
                        <!--正在玩-->
                        <td align="center" width="300px" style="word-break:break-all">
                            <div  id="play_${ct.profileBlog.uno}">
                                ${ct.profileBlog.playingGames}
                            </div>
                        </td>
                        <!--用户最后修改时间-->
                        <td align="center">
                            <fmt:formatDate value="${ct.profileBlog.updateDate}" pattern="yyyy-MM-dd"></fmt:formatDate>
                        </td>
                        <!--记录状态-->
                        <td align="center">
                            <div id="div2_${ct.profileBlog.uno}">
                            <c:choose>
                                <c:when test="${ct.profileBlog.auditStatus.hasAudit()}">
                                    已审核
                                </c:when>
                                <c:otherwise>
                                    未审核
                                </c:otherwise>
                            </c:choose>
                            </div>
                        </td>
                        <!--操作-->
                        <td align="center">
                            <p:privilege name="json/profile/delicon">
                               <a href="" onclick='deleteHeadIcon("${ct.profileBlog.uno}");return false;'>删头像</a>
                            </p:privilege>
                             |
                            <c:choose>
                                <c:when test="${ct.profileBlog.auditStatus.isBlogDescIllegal()}">
                                    <p:privilege name="json/profile/deldesc">
                                        &nbsp;<a href="" onclick='restoreDesc("${ct.profileBlog.uno}"); return false;'>恢复简介</a></br>
                                    </p:privilege>
                                </c:when>
                                <c:otherwise>
                                    <p:privilege name="json/profile/deldesc">
                                        &nbsp;<a href="" onclick='deleteDesc("${ct.profileBlog.uno}"); return false;'>删简介</a></br>
                                    </p:privilege>
                                </c:otherwise>
                            </c:choose>
                               <a href="javascript:void(0)" onclick='delplayinggames("${ct.profileBlog.uno}","${ct.profileBlog.playingGames}");return false;'>删正在玩</a>
                            <p:privilege name="audit/profile/details">
                                <a href="details?uno=${ct.profileBlog.uno}">查详情</a>
                            </p:privilege>
                             |
                            <p:privilege name="audit/profile/banpage">
                                <a href="banpage?screenName=${params.screenName}&maxPageItems=${page.pageSize}&items=${page.totalRows}&createstartdate=${createstart}&createenddate=${createend}&startDate=${startUpDate}&endDate=${endUpDate}&uno=${ct.profileBlog.uno}&audit=${params.auditStatus.value}&sorttype=${params.sortType}">封博客</a>
                            </p:privilege>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            <!--foot-->
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td">
                        <nobr>全选<input type="checkbox" id="chkall" onclick="selectAll(this, 'applyid')"/></nobr>
                        <nobr>反选<input type="checkbox" id="unchk" onclick="unselected(this, 'applyid')"/></nobr>
                        <c:if test="${!params.auditStatus.hasAudit()}">
                        <p:privilege name="/audit/profile/allpass">
                            <input name="Submit" type="submit" class="default_button" value="通&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;过" onclick="return optAllPass2('applyid'); ">
                        </p:privilege>
                        </c:if>
                    </td>
                    <td height="1" class="default_line_td" align="right">
                        <LABEL>
                            <pg:pager url="profilelist"
                                items="${page.totalRows}" isOffset="true" maxPageItems="${page.pageSize}" export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                <pg:param name="items" value="${page.totalRows}"/>
                                <pg:param name="createstartdate" value="${createstart}"/>
                                <pg:param name="createenddate" value="${createend}"/>
                                <pg:param name="startDate" value="${startUpDate}"/>
                                <pg:param name="endDate" value="${endUpDate}"/>
                                <pg:param name="screenName" value="${params.screenName}"/>
                                <pg:param name="audit" value="${params.auditStatus.value}"/>
                                <pg:param name="sorttype" value="${params.sortType}"/>
                            <%@ include file="/WEB-INF/jsp/toolspg.jsp" %>
                            </pg:pager>
                        </LABEL>
                    </td>
                </tr>
            </table>
            </form> <!--form3 end-->
        </td>
    </tr>
</table>
</body>
</html>