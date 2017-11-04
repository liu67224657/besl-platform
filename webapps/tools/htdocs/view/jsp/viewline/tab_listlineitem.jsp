<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf8">
<title>后台数据管理、Line查询列表</title>
<link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/dhtmlxcalendar.css">
<link rel="stylesheet" type="text/css" href="/static/include/dhtmlxcalendar/skins/dhtmlxcalendar_dhx_skyblue.css">
<script src="/static/include/dhtmlxcalendar/dhtmlxcalendar.js"></script>
<script src="/static/include/js/default.js" type="text/javascript"></script>
<script src="/static/include/js/jquery.js" type="text/javascript"></script>
<script language="JavaScript" type="text/JavaScript">
var myCalendar;

function doOnLoad() {
    myCalendar = new dhtmlXCalendarObject(["startDate", "endDate"]);
}

function add(lineId) {
    window.location.href = "/viewline/preaddlineitem?lineId=" + lineId;
}

function search(lineId) {
    var istop = document.getElementById('istop').value;
    var startDate = document.getElementById('startDate').value;
    var endDate = document.getElementById('endDate').value;
    var searchValidStatus = document.getElementById('searchValidStatus').value;

    window.location.href = '/viewline/listlineitem?lineId=' + lineId + "&istop=" + istop
            + "&startDate=" + startDate + "&endDate=" + endDate + "&searchValidStatus=" + searchValidStatus;
}

function remove(lineId, lineItemId) {
    if (window.confirm("你确定要删除该信息吗？")) {
        window.location.href = '/viewline/removelineitem?lineId=' + lineId + "&lineItemId=" + lineItemId;
    }
}

function show2(event, _this, mess) {
    event = event || window.event;
    //width='300' height='225'
    var t1 = "<table     cellspacing='1' cellpadding='10' style='border-color:#CCCCCC;background-color:#FFFFFF;font-size:12px;border-style:solid;    border-width:thin;text-align:center;'><tr><td><img src='" + _this.src + "' >    <br>" + mess + "</td></tr></table>";
    document.getElementById("displayimg2").innerHTML = t1;
    //document.getElementById("a1").innerHTML = "<img src='" + _this.src + "' >";
    document.getElementById("displayimg2").style.top = document.body.scrollTop + event.clientY + 10 + "px";
    document.getElementById("displayimg2").style.left = document.body.scrollLeft + event.clientX + 10 + "px";
    document.getElementById("displayimg2").style.display = "block";
}
function hide2(_this) {
    document.getElementById("displayimg2").innerHTML = "";
    document.getElementById("displayimg2").style.display = "none";
}

function prev(dom, itemid) {
    var preid;
    var id = parseInt(dom.parentNode.parentNode.parentNode.id);
    $.post("/json/viewline/paixu", {type:'prev',lineItemId:itemid,'top':'notop'}, function(req) {
        var json = eval("(" + req + ")");
        if (json.status_code == '1') {
            var text = document.getElementById('displayorder' + id).innerText ?
                    document.getElementById('displayorder' + id).innerText : //ie等浏览器支持的属性
                    document.getElementById('displayorder' + id).textContent //FF支持的属性
            var nextid;
            if (id > 0) {
                preid = id - 1;
                nextid = id + 1;
            }
            var temp;
            var thisDisplayOrder = parseInt(document.getElementById('displayorder' + id).innerText ?
                    document.getElementById('displayorder' + id).innerText : //ie等浏览器支持的属性
                    document.getElementById('displayorder' + id).textContent);//FF支持的属性

            var thatDisplayOrder = parseInt(document.getElementById('displayorder' + preid).innerText ?
                    document.getElementById('displayorder' + preid).innerText : //ie等浏览器支持的属性
                    document.getElementById('displayorder' + preid).textContent);//FF支持的属性

            $('#displayorder' + id).html(thatDisplayOrder);

            $('#displayorder' + preid).html(thisDisplayOrder);

            var thisTr = document.getElementById(id).innerHTML;
            $('#' + id).html(document.getElementById(preid).innerHTML);
            $('#' + preid).html(thisTr);

        } else {
            alert("调整失败");
        }
    });
}

function next(dom, length, itemid) {
    var preid;
    var id = parseInt(dom.parentNode.parentNode.parentNode.id);
    var nextid;
    $.post("/json/viewline/paixu", {type:'next',lineItemId:itemid,'top':'notop'}, function(req) {
        var json = eval("(" + req + ")");
        if (json.status_code == '1') {
            if (id < length) {
                preid = id - 1;

                nextid = id + 1;
            }
            var temp;
            var thisDisplayOrder = parseInt(document.getElementById('displayorder' + id).innerText ?
                    document.getElementById('displayorder' + id).innerText : //ie等浏览器支持的属性
                    document.getElementById('displayorder' + id).textContent);//FF支持的属性

            var thatDisplayOrder = parseInt(document.getElementById('displayorder' + nextid).innerText ?
                    document.getElementById('displayorder' + nextid).innerText : //ie等浏览器支持的属性
                    document.getElementById('displayorder' + nextid).textContent);//FF支持的属性

            $('#displayorder' + id).html(thatDisplayOrder);

            $('#displayorder' + nextid).html(thisDisplayOrder);

            var thisTr = document.getElementById(id).innerHTML;
            $('#' + id).html(document.getElementById(nextid).innerHTML);
            $('#' + nextid).html(thisTr);

        } else {
            alert("调整失败");
        }
    });
}


function tprev(dom, itemid) {
//    var url = "/json/viewline/paixu?type=prev&lineItemId=" + itemid + "&top=top";
    var preid;
    var oid = dom.parentNode.parentNode.parentNode.id;
    var id = parseInt(oid.substring(1, oid.length));

     $.post("/json/viewline/paixu", {type:'prev',lineItemId:itemid,'top':'top'}, function(req) {
        var json = eval("(" + req + ")");
        if (json.status_code == '1') {

                var nextid;
                if (id > 0) {
                    preid = id - 1;
                    nextid = id + 1;
                }
                var temp;
                var thisDisplayOrder = parseInt(document.getElementById('tdisplayorder' + id).innerText ?
                        document.getElementById('tdisplayorder' + id).innerText : //ie等浏览器支持的属性
                        document.getElementById('tdisplayorder' + id).textContent);//FF支持的属性

                var thatDisplayOrder = parseInt(document.getElementById('tdisplayorder' + preid).innerText ?
                        document.getElementById('tdisplayorder' + preid).innerText : //ie等浏览器支持的属性
                        document.getElementById('tdisplayorder' + preid).textContent);//FF支持的属性

                $('#tdisplayorder' + id).html(thatDisplayOrder);

                $('#tdisplayorder' + preid).html(thisDisplayOrder);

                var thisTr = document.getElementById(oid).innerHTML;
                $('#' + oid).html(document.getElementById('t' + preid).innerHTML);
                $('#t' + preid).html(thisTr);

            } else {
                alert("调整失败");
            }
    });
}

function tnext(dom, length, itemid) {
    var url = "/json/viewline/paixu?type=next&lineItemId=" + itemid + "&top=top";
    var preid;
    var oid = dom.parentNode.parentNode.parentNode.id;
    var id = parseInt(oid.substring(1, oid.length));

    var nextid;
    $.post("/json/viewline/paixu", {type:'next',lineItemId:itemid,'top':'top'}, function(req) {
        var json = eval("(" + req + ")");
         if (json.status_code == '1') {

                if (id < length) {
                    preid = id - 1;
                    nextid = id + 1;
                }
                var temp;
                var thisDisplayOrder = parseInt(document.getElementById('tdisplayorder' + id).innerText ?
                        document.getElementById('tdisplayorder' + id).innerText : //ie等浏览器支持的属性
                        document.getElementById('tdisplayorder' + id).textContent);//FF支持的属性

                var thatDisplayOrder = parseInt(document.getElementById('tdisplayorder' + nextid).innerText ?
                        document.getElementById('tdisplayorder' + nextid).innerText : //ie等浏览器支持的属性
                        document.getElementById('tdisplayorder' + nextid).textContent);//FF支持的属性

                $('#tdisplayorder' + id).html(thatDisplayOrder);

                $('#tdisplayorder' + nextid).html(thisDisplayOrder);

                var thisTr = document.getElementById(oid).innerHTML;
                $('#' + oid).html(document.getElementById('t' + nextid).innerHTML);
                $('#t' + nextid).html(thisTr);

            } else {
                alert("调整失败");
            }
    });
}
</script>

</head>
<body onload="doOnLoad();">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="100%" valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr class="toolbar_tr">
                    <td>
                        <input name="Submit" type="submit" class="default_button" value="添加分类元素"
                               onClick="add('${line.lineId}');"></td>
                    <td align="right">
                        <table>
                            <tr>
                                <td>
                                    创建日期(区间)：
                                    <input name="startDate" id="startDate" type="text" class="default_input_singleline"
                                           size="8" maxlength="10" value="${startDate}">
                                    -
                                    <input name="endDate" id="endDate" type="text" class="default_input_singleline"
                                           size="8" maxlength="10" value="${endDate}">
                                </td>
                                <td>
                                    记录状态：
                                    <select name="searchValidStatus" id="searchValidStatus"
                                            class="default_select_single">
                                        <option value="">--请选择--</option>
                                        <c:forEach items="${allValidStatus}" var="validStatus">
                                            <option value="${validStatus.code}"
                                                    <c:if test="${searchValidStatus == validStatus.code}">selected="true"</c:if>>
                                                <fmt:message key="def.validstatus.${validStatus.code}.name"
                                                             bundle="${def}"/>
                                            </option>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td>
                                    是否置顶：
                                    <select name="istop" id="istop" class="default_select_single">
                                        <option value="">--请选择--</option>
                                        <option value="y" <c:if test="${istop eq 'y'}">selected="true"</c:if>>是</option>
                                        <option value="n" <c:if test="${istop eq 'n'}">selected="true"</c:if>>否</option>
                                    </select>
                                </td>
                                <td>
                                    <input type="button" class="default_button" value="查询"
                                           onclick="search('${line.lineId}');">
                                </td>
                            </tr>
                        </table>
                    </td>

                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="8" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td width="40" align="center">选择</td>
                    <td>内容预览</td>
                    <td width="60" align="center" nowrap="">是否置顶</td>
                    <td width="60" align="right" nowrap>排序值</td>
                    <td width="80" align="center" nowrap>是否有效</td>
                    <td width="100" align="center" nowrap>创建日期</td>
                    <td width="60" align="center" nowrap>操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="8" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${lineItems.size() > 0}">
                        <form action="/viewline/batchstatuslineitems" method="POST" name="batchform">
                            <input name="lineId" type="hidden" value="${line.lineId}"/>
                            <input name="pager.offset" type="hidden" value="${page.startRowIdx}"/>
                            <jsp:include
                                    page="/view/jsp/viewline/tab_listlineitem_${line.itemType.code}.jsp"></jsp:include>
                            <tr>
                                <td height="1" colspan="8" class="default_line_td"></td>
                            </tr>
                            <tr class="toolbar_tr">
                                <td colspan="8">
                                    <input type="checkbox" name="selectall" value="1"
                                           onclick='javascript:checkall(document.forms["batchform"].lineItemIds, document.forms["batchform"].selectall)'>全选
                                    <input type="checkbox" name="uncheck" value="1"
                                           onclick='javascript:convertcheck(document.forms["batchform"].lineItemIds)'>反选
                                    将选中记录状态改成：
                                    <select name="validStatusCode" class="default_select_single">
                                        <option value="">--请选择--</option>
                                        <c:forEach items="${validStatuses}" var="validStatus">
                                            <option value="${validStatus.code}"
                                                    <c:if test="${validStatusCode == validStatus.code}">selected="true"</c:if>>
                                                <fmt:message key="def.validstatus.${validStatus.code}.name"
                                                             bundle="${def}"/>
                                            </option>
                                        </c:forEach>
                                    </select>
                                    <input name="submit" type="submit" class="default_button" value="批量修改">
                                </td>
                            </tr>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="8" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td height="1" colspan="8" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="8">
                            <pg:pager url="/viewline/listlineitem"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="lineId" value="${line.lineId}"/>
                                <%@ include file="/WEB-INF/jsp/toolspg.jsp" %>
                            </pg:pager>
                        </td>
                    </tr>
                </c:if>
            </table>
        </td>
    </tr>
</table>
</body>
</html>