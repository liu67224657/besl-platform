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
    <script language="JavaScript" type="text/JavaScript">

        function modifyReplyStatus(replyId, contentId, originalValue){
            var url = "/json/content/modifyreplystatus?replyid="+replyId +"&contentid=" + contentId+"&originalvalue="+ originalValue +"&timestamp="+new Date().getTime();

            var xmlHttp;
            //惨痛教训啊
            if(window.XMLHttpRequest){
                xmlHttp = new XMLHttpRequest();
            } else{
                xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
            }

            xmlHttp.onreadystatechange = function(){
                if(xmlHttp.readyState == 4 && xmlHttp.status == 200){
                    var txt = "<td align='left' width='100px'>";
                    var jsonResult = xmlHttp.responseText;

                    var json = eval("(" + jsonResult + ")");

                    if(json.status_code == '1'){
                        var div_id = "txtHint_" + replyId;
                        var tips;
                        var buttonName;

                        if((json.msg & 2) > 0 ){
                            tips = "<font color='red'>已屏蔽</font>";
                            buttonName = "解除屏蔽";
                        }else{
                            tips = "<font color='red'>已通过</font> ";
                            buttonName = "屏蔽";
                        }

                        document.getElementById(div_id).innerHTML = txt + tips + "<br/><input type='button' value='"+ buttonName + "' onclick=\"modifyReplyStatus('"+replyId  +"','"+contentId+"','"+ json.msg + "');\" ></td>";

                        if(document.getElementById("chk_" + replyId)){
                            document.getElementById("chkbox_" + replyId).removeChild(document.getElementById("chk_" + replyId));
                        }


                    }else{

                    }


                }
            }

            xmlHttp.open("GET", url , true);
            xmlHttp.send();
        }

        function optAllPass2(replyid){

            var flag = false;
            var checkobj = document.getElementsByName(replyid);
            for(var i=0;i<checkobj.length;i++){
                if(checkobj[i].checked){
                    flag = true;
                }
            }

            if(flag){
                var url = "/audit/content/rybatchupdate";
                var form2checkbox = document.batchform.replyids;

                for(var i = 0;i<form2checkbox.length;i++){
                    if(form2checkbox[i].checked){

                        url+=("&replyids=" + form2checkbox[i].value);
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
        function show(event,_this,mess)
        {
            event = event || window.event;
            //width='300' height='225'
            var t1="<table     cellspacing='1' cellpadding='10' style='border-color:#CCCCCC;background-color:#FFFFFF;font-size:12px;border-style:solid;    border-width:thin;text-align:center;'><tr><td><img src='" + _this.src   + "' >    <br>"+mess+"</td></tr></table>";
            document.getElementById("displayimg").innerHTML =t1;
            //document.getElementById("a1").innerHTML = "<img src='" + _this.src + "' >";
            document.getElementById("displayimg").style.top   = document.body.scrollTop + event.clientY + 10 + "px";
            document.getElementById("displayimg").style.left = document.body.scrollLeft + event.clientX + 10 + "px";
            document.getElementById("displayimg").style.display = "block";
        }
        function hide(_this)
        {
            document.getElementById("displayimg").innerHTML = "";
            document.getElementById("displayimg").style.display = "none";
        }
    </script>
</head>

<body onload="doOnLoad();">

<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 发布内容管理 >> 发布内容文字审核</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">>评论查询列表</td>
                </tr>
            </table>
            <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <form action="/audit/content/textreplylist" method="POST">
                    <tr>
                        <td width="80" align="center">搜索条件</td>
                        <td>
                            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                                <tr>
                                    <td width="100" align="right" class="edit_table_defaulttitle_td">评论者：</td>
                                    <td class="edit_table_value_td">
                                        <input name="screenname" type="text" class="default_input_singleline" size="16" maxlength="32" value="${screenname}">
                                        [暂不可模糊搜索]
                                    </td>
                                    <td width="100" align="right" class="edit_table_defaulttitle_td">创建时间：</td>
                                    <c:set var="startDate"><fmt:formatDate value="${params.startDate}" pattern="yyyy-MM-dd"/></c:set>
                                    <c:set var="endDate"><fmt:formatDate value="${params.endDate}" pattern="yyyy-MM-dd"/></c:set>
                                    <td class="edit_table_value_td">
                                        <input id="startDate" name="startdate" type="text" class="default_input_singleline" size="9" maxlength="10" value="${startDate}">
                                        -
                                        <input id="endDate" name="enddate" type="text" class="default_input_singleline" size="9" maxlength="10" value="${endDate}">
                                    </td>
                                </tr>
                                <tr>
                                    <td width="100" align="right" class="edit_table_defaulttitle_td">关键字：</td>
                                    <td class="edit_table_value_td">
                                        <input name="key" type="text" class="default_input_singleline" size="16" maxlength="32" value="${key}">
                                    </td>
                                    <td width="100" align="right" class="edit_table_defaulttitle_td">范围：</td>
                                    <td class="edit_table_value_td">
                                        <select name="auditstatus" id="all" class="default_select_single">
                                            <c:forEach items="${replyStatus.keySet()}" var="statusKey">
                                                <option value="${statusKey}"
                                                    <c:if test="${statusKey eq auditstatus}">
                                                        selected="selected"
                                                    </c:if>
                                                        ><fmt:message key="${replyStatus.get(statusKey)}" bundle="${toolsProps}"/></option>
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
                <form action="/audit/content/rybatchupdate" method="POST" name="batchform1">
                    <c:choose>
                        <c:when test="${rows.size() > 0}">
                            <tr class="toolbar_tr">
                                <input type="hidden" name="startdate" value="${startDate}">
                                <input type="hidden" name="enddate" value="${endDate}">
                                <input type="hidden" name="screenname" value="${screenname}">
                                <input type="hidden" name="key" value="${key}">
                                <input type="hidden" name="pager.offset" value="${page.startRowIdx}">
                                <input type="hidden" name="items" value="${page.totalRows}">
                                <input type="hidden" name="auditstatus" value="${auditstatus}">
                                <td>
                                    <input type="checkbox" name="selectall" value="1"
                                           onclick='javascript:checkall(document.forms["batchform"].replyids, document.forms["batchform1"].selectall)'>全选
                                    <input type="checkbox" name="uncheck" value="1" onclick='javascript:convertcheck(document.forms["batchform"].replyids)'>反选
                                    状态改成：
                                    <select name="updateRemoveStatusCode" class="default_select_single">
                                        <option value="1" <c:if test="${updateRemoveStatusCode eq '1'}">selected="selected"</c:if>>通过</option>
                                        <option value="2" <c:if test="${updateRemoveStatusCode eq '2'}">selected="selected"</c:if>>屏蔽</option>
                                    </select>
                                    <p:privilege name="/audit/content/rybatchupdate">
                                        <input name="update" type="button" class="default_button" value="批量修改" onclick="return optAllPass2('replyids');">
                                    </p:privilege>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    <c:if test="${page.maxPage > 1}">
                                        <LABEL>
                                            <pg:pager url="textreplylist"
                                                items="${page.totalRows}" isOffset="true" maxPageItems="${page.pageSize}" export="offset, currentPageNumber=pageNumber" scope="request">
                                                <pg:param name="maxPageItems" value="${page.pageSize}"/><!--不能缺少的-->
                                                <pg:param name="items" value="${page.totalRows}"/>
                                                <pg:param name="startdate" value="${startDate}"/>
                                                <pg:param name="enddate" value="${endDate}"/>
                                                <pg:param name="screenname" value="${screenname}"/>
                                                <pg:param name="key" value="${key}"/>
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
                                <td colspan="9" class="error_msg_td"></td>
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
                    <td align="center" width="30">选择</td>
                    <td align="center" width="200">原帖预览</td>
                    <td align="center" width="200">评论内容</td>
                    <td align="center" width="100">图片</td>
                    <td align="center" width="60">原帖作者</td>
                    <td align="center" width="60">操作</td>
                    <td align="center" width="40">评论者</td>
                    <td align="center" width="80">最后时间</td>
                    <td align="center" width="40">链接</td>
                </tr>
                <tr>
                    <td height="1" colspan="9" class="default_line_td"></td>
                </tr>
                <form name="batchform" action="/audit/content/rybatchupdate" method="POST">
                    <input type="hidden" name="startdate" value="${startDate}">
                    <input type="hidden" name="enddate" value="${endDate}">
                    <input type="hidden" name="screenname" value="${screenname}">
                    <input type="hidden" name="key" value="${key}">
                    <input type="hidden" name="pager.offset" value="${page.startRowIdx}">
                    <input type="hidden" name="items" value="${page.totalRows}">
                    <input type="hidden" name="auditstatus" value="${auditstatus}">
                    <c:choose>
                        <c:when test="${rows.size() > 0}">
                            <c:forEach items="${rows}" var="entity" varStatus="st">
                                <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                    <td align="center">
                                        <div id="chkbox_${entity.contentReply.interactionId}">
                                            <input type="checkbox" id="chk_${entity.contentReply.interactionId}" name="replyids" value="${entity.contentReply.interactionId}@${entity.content.contentId}@${entity.contentReply.auditStatus.value}"/>
                                        </div>
                                    </td>
                                    <td valign="top">${entity.content.content}</td>
                                    <td align="left" height="100" style="word-break:break-all" valign="top">
                                        ${entity.contentReply.interactionContent}
                                    </td>
                                    <td align="center">
                                        <div id="displayimg" style=" position:absolute; z-index:2;"></div>
                                        <c:forEach items="${entity.contentReply.interactionImages.images}" var="image">
                                            <img src="<c:out value="${uf:parseMFace(image.url)}"/>" height="100" width="100" onmousemove="show(event,this,'浏览原图')" onmouseout="hide(this)"/>
                                        </c:forEach>
                                    </td>
                                    <td>
                                        ${entity.authorProfile.blog.screenName}
                                    </td>

                                    <td width="100px">
                                    <div id="txtHint_${entity.contentReply.interactionId}">
                                        <p:privilege name="/json/content/modifyreplystatus">
                                            <input type="button"
                                                <c:choose>
                                                    <c:when test="${!entity.contentReply.auditStatus.isReplyPass()}">
                                                        value="解除屏蔽"
                                                    </c:when>
                                                    <c:otherwise>
                                                        value="屏蔽"
                                                    </c:otherwise>
                                                </c:choose>
                                                    onclick="modifyReplyStatus('${entity.contentReply.interactionId}','${entity.content.contentId}','${entity.contentReply.auditStatus.value}');" >
                                        </p:privilege>
                                    </div>
                                    </td>

                                    <td align="left">
                                            <!--<input type=hidden name=screenNames value="">-->
                                             ${entity.profile.blog.screenName}
                                    </td>
                                    <td align="left">
                                            ${entity.contentReply.createDate}
                                    </td>
                                    <td align="left">
                                       <a href="${URL_WWW}/note/${entity.contentReply.contentId}" target="_blank">详情</a>
                                    </td>
                                </tr>

                            </c:forEach>
                            <tr>
                                <td colspan="9" height="1" class="default_line_td"></td>
                            </tr>
                            <tr class="toolbar_tr">
                                <td colspan="9">
                                    <input type="checkbox" name="selectall" value="1"
                                           onclick='javascript:checkall(document.forms["batchform"].replyids, document.forms["batchform"].selectall)'>全选
                                    <input type="checkbox" name="uncheck" value="1" onclick='javascript:convertcheck(document.forms["batchform"].replyids)'>反选
                                    状态改成：
                                    <select name="updateRemoveStatusCode" class="default_select_single">
                                        <option value="1" <c:if test="${updateRemoveStatusCode eq '1'}">selected="selected"</c:if>>通过</option>
                                        <option value="2" <c:if test="${updateRemoveStatusCode eq '2'}">selected="selected"</c:if>>屏蔽</option>
                                    </select>
                                    <p:privilege name="/audit/content/rybatchupdate">
                                        <input name="update" type="submit" class="default_button" value="批量修改" >
                                    </p:privilege>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    <c:if test="${page.maxPage > 1}">
                                        <LABEL>
                                            <pg:pager url="textreplylist"
                                                items="${page.totalRows}" isOffset="true" maxPageItems="${page.pageSize}" export="offset, currentPageNumber=pageNumber" scope="request">
                                                <pg:param name="maxPageItems" value="${page.pageSize}"/><!--不能缺少的-->
                                                <pg:param name="items" value="${page.totalRows}"/>
                                                <pg:param name="startdate" value="${startDate}"/>
                                                <pg:param name="enddate" value="${endDate}"/>
                                                <pg:param name="screenname" value="${screenname}"/>
                                                <pg:param name="key" value="${key}"/>
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
                                <td colspan="9" class="error_msg_td">暂无数据!</td>
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