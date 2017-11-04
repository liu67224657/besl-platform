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
//
//        function selectAll(obj, itemName)
//        {
//            var boxes = document.getElementsByName(itemName);
//            for(var i=0;i<boxes.length;i++){
//                boxes[i].checked = obj.checked;
//            }
//            var unselected = document.getElementById("unchk");
//            unselected.checked = false;
//            var unselected2 = document.getElementById("unchk2");
//            unselected.checked = false;
//        }
//        function unselected(obj, itemName)
//        {
//            var selected = document.getElementById("chkall");
//            selected.checked = false;
//            var selected2 = document.getElementById("chkall2");
//            selected2.checked = false;
//            var boxes = document.getElementsByName(itemName);
//            for(var i=0;i<boxes.length;i++){
//                if(boxes[i].checked){
//                    boxes[i].checked = false;
//                }else {
//                    boxes[i].checked = true;
//                }
//            }
//        }
//        function check(input)
//        {
//            var re = /^[1-9]+[0-9]*]*$/;
//         //判断正整数 /^[0-9]+.?[0-9]*$/
//             if (!re.test(input.value))
//           {
//                alert("请输入大于零的整数( 例: 2)");
//               input.focus();
//               return false;
//            }
//        }
//        function chongzhi(){
//            document.getElementById("box").value = "";
//            document.getElementById("startDate").value = "";
//            document.getElementById("endDate").value = "";
//            document.getElementById("screenName").value = "";
//            document.getElementById("key").value = "";
//            document.getElementById("totalRows").value = "";
//        }        function chongzhi(){
//            document.getElementById("box").value = "";
//            document.getElementById("startDate").value = "";
//            document.getElementById("endDate").value = "";
//            document.getElementById("screenName").value = "";
//            document.getElementById("key").value = "";
//            document.getElementById("totalRows").value = "";
//        }
         //todo
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

                        document.getElementById(div_id).innerHTML = txt + tips + "<br/><input type='button' value='"+ buttonName + "' onclick=\"modifyAudistStatus('"+contentId  +"','"+uno+"','"+ json.msg + "');\" ></td>";

                        var chkboxId = "chkbox_" + contentId;
                        var chkId =  "chk_" + contentId;

                        if(document.getElementById(chkId)){
                            document.getElementById(chkboxId).removeChild(document.getElementById(chkId));
                        }

                    }else{

                    }


                }
            }

            xmlHttp.open("GET", url , true);
            xmlHttp.send();
        }


//        function optAllPass(applyid){
//            var flag = false;
//            var checkobj = document.getElementsByName(applyid);
//            for(var i=0;i<checkobj.length;i++){
//                if(checkobj[i].checked){
//                    flag = true;
//                }
//            }
//            if(flag){
//                document.form2.action='/audit/content/oprateall?pass=y';
//                document.form2.submit();
//                return true;
//            }else{
//                return false;
//            }
//
//        }
//        function optAllUnPass(applyid){
//            var flag = false;
//            var checkobj = document.getElementsByName(applyid);
//            for(var i=0;i<checkobj.length;i++){
//                if(checkobj[i].checked){
//                    flag = true;
//                }
//            }
//            if(flag){
//                document.form2.action='/audit/content/oprateall?pass=n';
//                document.form2.submit();
//                return true;
//            }else{
//                return false;
//            }
//        }
        function optAllPass2(contentid){

            var flag = false;
            var checkobj = document.getElementsByName(contentid);
            for(var i=0;i<checkobj.length;i++){
                if(checkobj[i].checked){
                    flag = true;
                }
            }

            if(flag){
                var url = "/audit/content/batchupdate";
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
        <td height="22" class="page_navigation_td">>> 发布内容管理 >> 发布内容文字审核</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">>文章内容查询列表</td>
                </tr>
            </table>
            <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <form action="/audit/content/textlist" method="POST">
                    <tr>
                        <td width="80" align="center">搜索条件</td>
                        <td>
                            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                                <tr>
                                    <td width="100" align="right" class="edit_table_defaulttitle_td">作者：</td>
                                    <td class="edit_table_value_td">
                                        <input name="screenname" type="text" class="default_input_singleline" size="16" maxlength="32" value="${screenname}">
                                        [暂不可模糊搜索]
                                        <c:if test="${fn:length(screenname) > 0 && rows.size() > 0 && auditstatus != 3}">
                                            <a href="/audit/content/allremove?screenname=${screenname}&startdate=<fmt:formatDate value="${params.startDate}" pattern="yyyy-MM-dd"/>&enddate=<fmt:formatDate value="${params.endDate}" pattern="yyyy-MM-dd"/>&key=${key}&auditstatus=${auditstatus}"><input type="button" name="button" value="全部屏蔽"/></a>
                                            <span style="color: #ff0000;">*谨慎操作</span>
                                        </c:if>
                                    </td>
                                    <td width="100" align="right" class="edit_table_defaulttitle_td">创建时间：</td>
                                    <c:set var="startDate"><fmt:formatDate value="${params.startDate}" pattern="yyyy-MM-dd"/></c:set>
                                    <c:set var="endDate"><fmt:formatDate value="${params.endDate}" pattern="yyyy-MM-dd"/></c:set>
                                    <td class="edit_table_value_td">
                                        <input id="startDate" name="startdate" type="text" class="default_input_singleline" size="8" maxlength="10" value="${startDate}" >
                                        -
                                        <input id="endDate" name="enddate" type="text" class="default_input_singleline" size="8" maxlength="10" value="${endDate}" >
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
                                            <c:forEach items="${contentStatus.keySet()}" var="contentStatusKey">
                                                <option value="${contentStatusKey}"
                                                    <c:if test="${contentStatusKey eq auditstatus}">
                                                        selected="selected"
                                                    </c:if>
                                                        ><fmt:message key="${contentStatus.get(contentStatusKey)}" bundle="${toolsProps}"/></option>
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
                <form action="/audit/content/batchupdate" method="POST" name="batchform1">
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
                                           onclick='javascript:checkall(document.forms["batchform"].contentids, document.forms["batchform1"].selectall)'>全选
                                    <input type="checkbox" name="uncheck" value="1" onclick='javascript:convertcheck(document.forms["batchform"].contentids)'>反选
                                    状态改成：
                                    <select name="updateRemoveStatusCode" class="default_select_single">
                                        <%--<option value="">--请选择--</option>--%>
                                        <option value="1" <c:if test="${updateRemoveStatusCode eq '1'}">selected="selected"</c:if>>通过</option>
                                        <option value="2" <c:if test="${updateRemoveStatusCode eq '2'}">selected="selected"</c:if>>屏蔽</option>
                                    </select>
                                    <p:privilege name="/audit/content/batchupdate">
                                        <input name="update" type="button" class="default_button" value="批量修改" onclick="return optAllPass2('contentids');">
                                    </p:privilege>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    <c:if test="${page.maxPage > 1}">
                                        <LABEL>
                                            <pg:pager url="textlist"
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
                                <td colspan="8" class="error_msg_td"></td>
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
                    <td align="center" width="100">主题</td>
                    <td align="center">发布文字内容</td>
                    <td align="center" width="60">转帖/原帖</td>
                    <td align="center" width="60">操作</td>
                    <td align="center" width="40">作者</td>
                    <td align="center" width="130">最后时间</td>
                    <td align="center" width="40">链接</td>
                </tr>
                <tr>
                    <td height="1" colspan="8" class="default_line_td"></td>
                </tr>
                <form name="batchform" action="/audit/content/batchupdate" method="POST">
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
                                        <div id="chkbox_${entity.content.contentId}">
                                            <input type="checkbox" id="chk_${entity.content.contentId}" name="contentids" value="${entity.content.contentId}@${entity.profile.blog.uno}@${entity.content.auditStatus.value}"/>
                                        </div>
                                    </td>
                                    <td valign="top">${entity.content.subject}</td>
                                    <td align="left" height="100" style="word-break:break-all" valign="top">
                                        <!--style="overflow-y: auto; overflow-x: hidden; height: 100px; width:390px;"-->

                                        <%--<br/>--%>
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
                                            <!--<input type=hidden name=screenNames value="">-->
                                             ${entity.profile.blog.screenName}
                                    </td>
                                    <td align="left" width="140px">
                                         <fmt:formatDate value="${entity.content.publishDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                         <br/>
                                         ${entity.content.publishIp}
                                    </td>
                                    <td align="left">
                                           <a href="${URL_WWW}/note/${entity.content.contentId}" target="_blank">详情</a>
                                    </td>
                                </tr>

                            </c:forEach>
                            <tr>
                                <td colspan="8" height="1" class="default_line_td"></td>
                            </tr>
                            <tr class="toolbar_tr">
                                <td colspan="8">
                                    <input type="checkbox" name="selectall" value="1"
                                           onclick='javascript:checkall(document.forms["batchform"].contentids, document.forms["batchform"].selectall)'>全选
                                    <input type="checkbox" name="uncheck" value="1" onclick='javascript:convertcheck(document.forms["batchform"].contentids)'>反选
                                    状态改成：
                                    <select name="updateRemoveStatusCode" class="default_select_single">
                                        <%--<option value="">--请选择--</option>--%>
                                        <option value="1" <c:if test="${updateRemoveStatusCode eq '1'}">selected="selected"</c:if>>通过</option>
                                        <option value="2" <c:if test="${updateRemoveStatusCode eq '2'}">selected="selected"</c:if>>屏蔽</option>
                                    </select>
                                    <p:privilege name="/audit/content/batchupdate">
                                        <input name="update" type="submit" class="default_button" value="批量修改" >
                                    </p:privilege>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    <c:if test="${page.maxPage > 1}">
                                        <LABEL>
                                            <pg:pager url="textlist"
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
                                <td colspan="8" class="error_msg_td">暂无数据!</td>
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