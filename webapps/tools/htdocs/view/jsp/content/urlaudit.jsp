<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<%@ taglib prefix="bit" uri="/WEB-INF/tags/bitwise.tld" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台数据管理、发布内容审核列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script language="JavaScript" type="text/JavaScript">

         //todo
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

                        document.getElementById("txt_"+replyId).innerHTML = tips;
                        document.getElementById(div_id).innerHTML = txt + tips + "<br/><input type='button' value='"+ buttonName + "' onclick=\"modifyReplyStatus('"+replyId  +"','"+contentId+"','"+ json.msg + "');\" ></td>";

                        document.getElementById("chkbox_" + replyId).removeChild(document.getElementById("chk_" + replyId));

                    }else{

                    }


                }
            }

            xmlHttp.open("GET", url , true);
            xmlHttp.send();
        }

        function modifyAudistStatus(contentId, uno, originalValue){
//            var screenNameEncode = encodeURI(encodeURI(screenName));

            var url = "/json/content/modifytextstatus?contentid="+contentId +"&uno=" + uno+"&originalvalue="+ originalValue +"&timestamp="+new Date().getTime();
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
                        var div_id = "txtHint_" + contentId;
                        var tips;
                        var buttonName;
//                        alert(json.msg & 2);
                        if((json.msg & 2) > 0 ){
                            tips = "<font color='red'>已屏蔽</font>";
                            buttonName = "解除屏蔽";
                        }else{
                            tips = "<font color='red'>已通过</font> ";
                            buttonName = "屏蔽";
                        }
                        document.getElementById("txt_"+contentId).innerHTML = tips;
                        document.getElementById(div_id).innerHTML = txt + tips + "<br/><input type='button' value='"+ buttonName + "' onclick=\"modifyAudistStatus('"+contentId  +"','"+uno+"','"+ json.msg + "');\" ></td>";

                        if(document.getElementById("chk_" + contentId)){
                            document.getElementById("chkbox_" + contentId).removeChild(document.getElementById("chk_" + contentId));
                        }


                    }else{

                    }


                }
            }

            xmlHttp.open("GET", url , true);
            xmlHttp.send();
        }

        function auditImgStatus(uno, contentId, originalValue, auditStatus){

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
                    if(tableObj){
                        divObject.removeChild(tableObj);
                    }

                    var jsonResult = xmlHttp.responseText;
                    if(jsonResult != null){

                    }
                    var json = eval("(" + jsonResult + ")");

                    json = eval("(" + json.msg + ")");
                    var size = json.length;

                    var text = "<table id=\""+tab_id + "\" style=\"table-layout: fixed\">";
                    var tips;

                    for(var i=0; i<size;i++){
                        if(i == 0 || i % 5 == 0){
                            text +="<tr>";
                        }
                        var imgurl = json[i].m;
                        imgurl = "http://" + imgurl.substring(1,5)+".${DOMAIN}" +json[i].m;


                            if(!json[i].validStatus ){
                                text+="<td align=\"center\" height='180' width='180'>"+
                                                    "该图片已被屏蔽<br/>" +
                                                    "<input type=\"checkbox\" name=\"values\" value=\""+json[i].url+"\">"+
                                                    "</td>"
                            } else{
                                text+="<td align=\"center\" height='180' width='180'>"+
                                                    "<img src=\""+imgurl +"\" height=\"160\", width=\"170\" onclick = 'window.open(\""+imgurl+"\","+"\"_bank\")'/><br/>"+
                                                    "<input type=\"checkbox\" name=\"values\" value=\""+json[i].url+"\">"+
                                                    "</td>" ;
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
            params+="&auditstatus=" + auditStatus;

            return params;
        }

        function selectAllX(obj, contentId){
            var chkObj = document.getElementById("div_" +contentId).getElementsByTagName("input");
            for(var i=0;i<chkObj.length;i++){
                chkObj[i].checked = obj.checked;
            }
        }

    </script>
</head>

<body>

<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 发布内容管理 >> 发布内容文字审核</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">>根据文章URL查询</td>
                </tr>
            </table>
            <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <form action="/audit/content/auditurl" method="POST">
                    <tr>
                        <td width="80" align="center">搜索条件</td>
                        <td>
                            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                                <tr>
                                    <td width="100" align="right" class="edit_table_defaulttitle_td">原文链接：</td>
                                    <td class="edit_table_value_td">
                                        <input name="url" type="text" class="default_input_singleline" size="60" value="${url}">
                                        [暂不可模糊搜索]
                                    </td>
                                    <td width="100" align="right" class="edit_table_defaulttitle_td">查询类型：</td>
                                    <td class="edit_table_value_td">
                                        <select name="type" id="all" class="default_select_single">
                                            <option value="content"
                                                <c:if test="${type eq 'content'}">selected="selected"</c:if>
                                                     >文章</option>
                                            <option value="image"
                                                <c:if test="${type eq 'image'}">selected="selected"</c:if>
                                                    >图片</option>
                                            <option value="reply"
                                                <c:if test="${type eq 'reply'}">selected="selected"</c:if>
                                                    >评论及回复</option>
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
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <c:choose>
                    <c:when test="${type eq 'reply'}">
                        <tr class="list_table_title_tr">
                            <td align="center" width="30">选择</td>
                            <td align="center" width="200">原帖预览</td>
                            <td align="center" width="200">评论内容</td>
                            <td align="center" width="60">原帖作者</td>
                            <td align="center" width="60">当前状态</td>
                            <td align="center" width="60">操作</td>
                            <td align="center" width="40">评论者</td>
                            <td align="center" width="80">最后时间</td>
                            <td align="center" width="40">链接</td>
                        </tr>
                        <tr>
                            <td height="1" colspan="9" class="default_line_td"></td>
                        </tr>
                        <form name="batchform" action="/audit/content/rybatchupdate" method="POST">
                            <input type="hidden" name="type" value="${type}">
                            <input type="hidden" name="url" value="${url}">
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
                                            <td>
                                                ${entity.authorProfile.blog.screenName}
                                            </td>
                                            <td>
                                                <div id="txt_${entity.contentReply.interactionId}">
                                                    <c:choose>
                                                        <c:when test="${!entity.contentReply.auditStatus.hasAuditReply()}">
                                                            <font color='red'>未审核</font>
                                                        </c:when>
                                                        <c:when test="${entity.contentReply.auditStatus.hasAuditReply() && entity.contentReply.auditStatus.isReplyPass()}">
                                                            <font color='red'>已通过</font>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <font color='red'>已屏蔽</font>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
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
                                            将选中文章状态改成：
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
                                                        <pg:param name="type" value="${type}"/>
                                                        <pg:param name="url" value="${url}"/>
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
                    </c:when>
                    <c:when test="${type eq 'content'}">
                        <tr class="list_table_title_tr">
                            <td align="center" width="30">选择</td>
                            <td align="center" width="100">主题</td>
                            <td align="center">发布文字内容</td>
                            <td align="center" width="60">转帖/原帖</td>
                            <td align="center" width="60">当前状态</td>
                            <td align="center" width="60">操作</td>
                            <td align="center" width="40">作者</td>
                            <td align="center" width="80">最后时间</td>
                            <td align="center" width="40">链接</td>
                        </tr>
                        <tr>
                            <td height="1" colspan="9" class="default_line_td"></td>
                        </tr>
                        <form name="batchform" action="/audit/content/batchupdate" method="POST">
                            <input type="hidden" name="type" value="${type}">
                            <input type="hidden" name="url" value="${url}">
                            <input type="hidden" name="pager.offset" value="${page.startRowIdx}">
                            <input type="hidden" name="items" value="${page.totalRows}">
                            <input type="hidden" name="auditstatus" value="${auditstatus}">
                            <c:choose>
                                <c:when test="${rows.size() > 0}">
                                    <c:forEach items="${rows}" var="entity" varStatus="st">
                                        <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                            <td align="center">
                                                <div id="chkbox_${entity.content.contentId}">
                                                    <input type="checkbox" id="chk_${entity.content.contentId}" name="contentids" value="${entity.content.contentId}@${entity.profile.blog.uno}@${entity.content.auditStatus.value}"/>
                                                </div>
                                            </td>
                                            <td valign="top">${entity.content.subject}</td>
                                            <td align="left" height="100" style="word-break:break-all" valign="top">
                                                ${entity.content.content}
                                           </td>
                                            <td>
                                                <c:if test="${entity.content.publishType.code eq 'org'}">
                                                    原创
                                                </c:if>
                                                <c:if test="${entity.content.publishType.code eq 'fwd'}">
                                                    转发
                                                </c:if>
                                            </td>
                                            <td>
                                                <div id="txt_${entity.content.contentId}">
                                                    <c:choose>
                                                        <c:when test="${!entity.content.auditStatus.hasAuditText()}">
                                                            <font color='red'>未审核</font>
                                                        </c:when>
                                                        <c:when test="${entity.content.auditStatus.hasAuditText() && entity.content.auditStatus.isTextPass()}">
                                                            <font color='red'>已通过</font>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <font color='red'>已屏蔽</font>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </td>
                                            <td width="100px">
                                            <div id="txtHint_${entity.content.contentId}">
                                                <p:privilege name="/json/content/modifytextstatus">
                                                    <input type="button"
                                                        <c:choose>
                                                            <c:when test="${!entity.content.auditStatus.isTextPass()}">
                                                                value="解除屏蔽"
                                                            </c:when>
                                                            <c:otherwise>
                                                                value="屏蔽"
                                                            </c:otherwise>
                                                        </c:choose>
                                                            onclick="modifyAudistStatus('${entity.content.contentId}','${entity.profile.blog.uno}','${entity.content.auditStatus.value}');" >
                                                </p:privilege>
                                            </div>
                                            </td>

                                            <td align="left">
                                                     ${entity.profile.blog.screenName}
                                            </td>
                                            <td align="left">
                                                    ${entity.content.publishDate}
                                            </td>
                                            <td align="left">
                                                   <a href="${URL_WWW}/note/${entity.content.contentId}" target="_blank">详情</a>
                                            </td>
                                        </tr>

                                    </c:forEach>
                                    <tr>
                                        <td colspan="9" height="1" class="default_line_td"></td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="9" class="error_msg_td">暂无数据!</td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </form>
                    </c:when>
                    <c:when test="${type eq 'image'}">
                        <tr class="list_table_title_tr">
                            <td nowrap align="center" width="30">选择</td>
                            <td nowrap align="center">发布图片内容</td>
                            <td nowrap align="center">操作</td>
                            <td nowrap align="center">作者</td>
                            <td nowrap align="center">最后时间</td>
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
                                                                    <c:when test="${img.validStatus}">
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
                                                                        <td align="left" width="180" height="180">
                                                                            该图片已被屏蔽<br/>
                                                                            <input type="checkbox" name="values" value="${img.url}">
                                                                        </td>
                                                                        <c:if test="${countt.last}">
                                                                            <c:forEach begin="${countt.count + 1}" end="5">
                                                                                <td align="left" width="180" height="180">
                                                                                   &nbsp;
                                                                                </td>
                                                                            </c:forEach>
                                                                        </c:if>
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

                                                    <c:choose>
                                                        <c:when test="${entity.content.auditStatus.value eq 0}">
                                                            <input type="button" value="通过" onclick='auditImgStatus("${entity.profile.blog.uno}", "${entity.content.contentId}", "${entity.content.auditStatus.value}", "0");' >
                                                            <input type="button" value="屏蔽" onclick='auditImgStatus("${entity.profile.blog.uno}", "${entity.content.contentId}", "${entity.content.auditStatus.value}", "0");' >
                                                        </c:when>
                                                        <c:otherwise>
                                                            <input type="button" value="通过" onclick='auditImgStatus("${entity.profile.blog.uno}", "${entity.content.contentId}", "${entity.content.auditStatus.value}", "12");' >
                                                            <input type="button" value="屏蔽" onclick='auditImgStatus("${entity.profile.blog.uno}", "${entity.content.contentId}", "${entity.content.auditStatus.value}", "4");' >
                                                        </c:otherwise>
                                                    </c:choose>
                                                </p:privilege>
                                            </td>
                                            <td align="left" width="100px">
                                                ${entity.profile.blog.screenName}
                                            </td>
                                            <td align="left" width="100px">
                                                ${entity.content.publishDate}
                                            </td>
                                            <td align="left" width="25px">
                                                <a href="${URL_WWW}/note/${entity.content.contentId}" target="_blank">详情</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    <tr>
                                        <td colspan="6" height="1" class="default_line_td"></td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="6" class="error_msg_td">暂无数据!</td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>

                        </form>
                    </c:when>
                </c:choose>
            </table>
        </td>
    </tr>
</table>
</body>
</html>