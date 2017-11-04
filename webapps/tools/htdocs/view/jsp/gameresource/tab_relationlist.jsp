<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台数据管理、Line查询列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script src="/static/include/js/jquery.js" type="text/javascript"></script>
    <script language="JavaScript" type="text/JavaScript">
        function add(resourceId) {
            window.location.href = "/gameresource/preaddresrelation?resid=" + resourceId;
        }

        function prev(dom, itemid) {
            var url = "/json/gameresource/relationsort?sorttype=prev&relationid=" + itemid ;
            var preid;
            var id = dom.parentNode.parentNode.parentNode.id;

            var xmlHttp;
            if(window.XMLHttpRequest){
                xmlHttp = new XMLHttpRequest();
            } else {
                xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
            }

            xmlHttp.onreadystatechange = function(){
                if(xmlHttp.readyState == 4 && xmlHttp.status == 200){

                    var jsonResult = xmlHttp.responseText;

                    var json = eval("(" + jsonResult + ")");

                    if(json.status_code == '1'){
                    var text = document.getElementById('displayorder'+id).innerText ?
                    document.getElementById('displayorder'+id).innerText : //ie等浏览器支持的属性
                     document.getElementById('displayorder'+id).textContent //FF支持的属性
                        var nextid;
                        if(id > 0){
                            preid = id - 1;
                            nextid = id + 1;
                        }
                        var temp;
                        var thisDisplayOrder = parseInt(document.getElementById('displayorder'+id).innerText ?
                                                        document.getElementById('displayorder'+id).innerText : //ie等浏览器支持的属性
                                                        document.getElementById('displayorder'+id).textContent );//FF支持的属性

                        var thatDisplayOrder = parseInt(document.getElementById('displayorder'+preid).innerText ?
                                                        document.getElementById('displayorder'+preid).innerText : //ie等浏览器支持的属性
                                                        document.getElementById('displayorder'+preid).textContent );//FF支持的属性

                        $('#displayorder'+ id).html(thatDisplayOrder);

                        $('#displayorder'+ preid).html(thisDisplayOrder);

                        var thisTr = document.getElementById(id).innerHTML;
                        $('#'+id).html(document.getElementById(preid).innerHTML);
                        $('#'+preid).html(thisTr);

                    }else{
                        alert("不能调整了");
                    }


                }
            }

            xmlHttp.open("GET", url , true);
            xmlHttp.send();

        }

        function next(dom, length, itemid) {
            var url = "/json/gameresource/relationsort?sorttype=next&relationid=" + itemid;
            var preid;
            var id = dom.parentNode.parentNode.parentNode.id;
            var nextid;

            var xmlHttp;
            if(window.XMLHttpRequest){
                xmlHttp = new XMLHttpRequest();
            } else {
                xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
            }

            xmlHttp.onreadystatechange = function(){
                if(xmlHttp.readyState == 4 && xmlHttp.status == 200){

                    var jsonResult = xmlHttp.responseText;

                    var json = eval("(" + jsonResult + ")");

                    if(json.status_code == '1'){

                        if(id < length){
                            preid = id - 1;
                            nextid = parseInt(id) + 1;
                        }

                        var temp;
                        var thisDisplayOrder = parseInt(document.getElementById('displayorder'+id).innerText ?
                                                        document.getElementById('displayorder'+id).innerText : //ie等浏览器支持的属性
                                                        document.getElementById('displayorder'+id).textContent );//FF支持的属性

                        var thatDisplayOrder = parseInt(document.getElementById('displayorder'+nextid).innerText ?
                                                        document.getElementById('displayorder'+nextid).innerText : //ie等浏览器支持的属性
                                                        document.getElementById('displayorder'+nextid).textContent );//FF支持的属性

                        $('#displayorder'+ id).html(thatDisplayOrder);

                        $('#displayorder'+ nextid).html(thisDisplayOrder);

                        var thisTr = document.getElementById(id).innerHTML;
                        $('#'+id).html(document.getElementById(nextid).innerHTML);
                        $('#'+nextid).html(thisTr);

                    }else{
                        alert("不能调整了");
                    }


                }
            }

            xmlHttp.open("GET", url , true);
            xmlHttp.send();
        }
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="100%" valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr class="toolbar_tr">
                    <td>
                        <p:privilege name="/gameresource/preaddresrelation">
                            <input name="Submit" type="submit" class="default_button" value="增加资源连接" onClick="add('${resid}');">
                        </p:privilege>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="7" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center" width="40">选择</td>
                    <td nowrap align="left" >关联的资源类型</td>
                    <td nowrap align="left" >关联的资源名称</td>
                    <td nowrap align="left">关联的资源的值</td>
                    <td nowrap align="center">排序值</td>
                    <td nowrap align="center">当前状态</td>
                    <td nowrap align="center">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="7" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${relationList.size() > 0}">
                        <%--<form action="/gameresource/batchupdaterelation" method="POST" name="batchform">--%>
                            <input type="hidden" name="resid" value="${resid}">
                            <c:forEach items="${relationList}" var="relation" varStatus="st">
                                <tr class="<c:choose>
                            <c:when test="${st.index % 2 == 0}">
                               list_table_opp_tr
                            </c:when>
                            <c:otherwise>
                                list_table_even_tr
                            </c:otherwise>
                            </c:choose>" id="${st.index}">
                                    <td align="center">
                                        <input type="checkbox" name="relationids" value="${relation.relationId}">
                                    </td>
                                    <td align="left">
                                        <fmt:message key="gameres.relation.type.${relation.gameRelationType.code}.name" bundle="${def}"/>
                                    </td>
                                    <td align="left">
                                        ${relation.relationName}
                                    </td>
                                    <td align="left">
                                        ${relation.relationValue}
                                    </td>
                                    <td id="displayorder${st.index}" align="center">
                                        ${relation.sortNum}
                                    </td>
                                    <td align="center">
                                        <fmt:message key="def.validstatus.${relation.validStatus.code}.name" bundle="${def}"/>
                                    </td>
                                    <td align="center">
                                        <a href="/gameresource/preupdaterelation?relationid=${relation.relationId}&resid=${resid}">修改</a>
                                        <c:choose>
                                           <c:when test="${relation.gameRelationType.code=='article'
                                                        || relation.gameRelationType.code=='download'
                                                        || relation.gameRelationType.code=='groupcontent'
                                                        || relation.gameRelationType.code=='talent'
                                                        || relation.gameRelationType.code=='menu'
                                                        || relation.gameRelationType.code=='contribute'
                                                        || relation.gameRelationType.code=='headimage'}">
                                          <a href="/viewline/linedetail?lineId=${relation.relationValue}" target="_blank">编辑内容</a>
                                        </c:when>
                                         <c:otherwise>
                                             <div>
                                                 <a href="javascript:void(0)"
                                                    onclick="prev(this, '${relation.relationId}');"><img
                                                         src="/static/images/icon/up.gif"></a>
                                             </div>
                                             <div>
                                                 <a href="javascript:void(0)"
                                                    onclick="next(this, '20', '${relation.relationId}');"><img
                                                         src="/static/images/icon/down.gif"></a>
                                             </div>
                                         </c:otherwise>
                                        </c:choose>


                                    </td>
                                </tr>
                            </c:forEach>
                            <tr>
                                <td height="1" colspan="7" class="default_line_td"></td>
                            </tr>
                            <%--<tr class="toolbar_tr">--%>
                                <%--<td colspan="7">--%>
                                    <%--<input type="checkbox" name="selectall" value="1"--%>
                                           <%--onclick='javascript:checkall(document.forms["batchform"].relationids, document.forms["batchform"].selectall)'>全选--%>
                                    <%--<input type="checkbox" name="uncheck" value="1" onclick='javascript:convertcheck(document.forms["batchform"].relationids)'>反选--%>
                                    <%--将选中记录有效状态改成：--%>
                                    <%--<select name="updateValidStatusCode" class="default_select_single">--%>
                                        <%--<option value="">--请选择--</option>--%>
                                        <%--<c:forEach items="${validStatuses}" var="validStatus">--%>
                                            <%--<option value="${validStatus.code}" <c:if test="${updateValidStatusCode == validStatus.code}">selected="true"</c:if>>--%>
                                                <%--<fmt:message key="def.validstatus.${validStatus.code}.name" bundle="${def}"/>--%>
                                            <%--</option>--%>
                                        <%--</c:forEach>--%>
                                    <%--</select>--%>
                                    <%--<input name="submit" type="submit" class="default_button" value="批量修改">--%>
                                <%--</td>--%>
                            <%--</tr>--%>
                        <%--</form>--%>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="7" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="7" height="1" class="default_line_td"></td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>