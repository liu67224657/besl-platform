<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<script language="javascript">
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
<c:forEach items="${nonTopItems}" var="nonTopItem" varStatus="vst">
    <tr class="list_table_even_tr" id="t${vst.index}">

        <td align="center">
            <input type="checkbox" name="lineItemIds" value="${nonTopItem.lineItemId}">
        </td>
        <%--<td>--%>
            <%--博客/昵称: ${nonTopItems.profile.blog.domain} / ${nonTopItems.profile.blog.screenName}<br>--%>
            <%--描述: ${nonTopItems.itemDesc}--%>
        <%--</td>--%>
        <td>
            <%--<table>--%>
                <%--<tr>--%>
                    <%--<td><b>显示标题</b></td>--%>
                    <%--<td>${nonTopItems.viewLineItem.displayInfo.subject}</td>--%>
                <%--</tr>--%>
                <%--<tr>--%>
                    <%--<td><b>显示连接</b></td>--%>
                    <%--<td><a href="${nonTopItems.viewLineItem.displayInfo.linkUrl}" target="_blank">${nonTopItems.viewLineItem.displayInfo.linkUrl}</a></td>--%>
                <%--</tr>--%>
                <%--<tr>--%>
                    <%--<td><b>显示图片</b></td>--%>
                    <%--<td><img src="${uf:parseBFace(nonTopItems.viewLineItem.displayInfo.iconUrl)}" /></td>--%>
                <%--</tr>--%>
                <%--<tr>--%>
                    <%--<td><b>显示描述</b></td>--%>
                    <%--<td>${nonTopItems.viewLineItem.displayInfo.desc}</td>--%>
                <%--</tr>--%>
            <%--</table>--%>
            <div id="displayimg2" style=" position:absolute; z-index:2;"></div>
            <table width="100%">
                <tr>
                    <td rowspan="5" width="100">
                        <a target="_blank" href="${nonTopItem.viewLineItem.displayInfo.linkUrl}">
                        <img width="80" height="80" src="${uf:parseBFace(nonTopItem.viewLineItem.displayInfo.iconUrl)}" onmousemove="show(event,this,'浏览原图')" onmouseout="hide(this)"></a>
                    </td>
                </tr>
                <tr>
                    <td><p>标题：<c:if test="${nonTopItem.viewLineItem.displayInfo.linkUrl ne null || nonTopItem.viewLineItem.displayInfo.linkUrl ne ''}"><a target="_blank" href="${nonTopItem.viewLineItem.displayInfo.linkUrl}">
                        ${nonTopItem.viewLineItem.displayInfo.subject}
                     </a></c:if></p></td>
                </tr>
                <tr><td><p>描述：
                    ${nonTopItem.viewLineItem.displayInfo.desc}
                </p></td></tr>
                <tr><td><p>连接：<a target="_blank" href="${nonTopItem.viewLineItem.displayInfo.linkUrl}">${nonTopItem.viewLineItem.displayInfo.linkUrl}</a></p></td></tr>
                <tr><td><p>博客/昵称: ${nonTopItem.profile.blog.domain} / ${nonTopItem.profile.blog.screenName}</p></td></tr>
            </table>
        </td>
        <td>
            不支持
        </td>
        <td id="tdisplayorder${vst.index}" align="right">
                ${nonTopItem.displayOrder}
        </td>
        <td align="center">
            <fmt:message key="def.validstatus.${nonTopItem.validStatus.code}.name" bundle="${def}"/>
        </td>
        <td align="center">
            <fmt:formatDate value="${nonTopItem.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
        </td>
        <td align="center">
            <span>
            <p:privilege name="/viewline/preeditlineitem">
                <a href="/viewline/preeditlineitem?lineItemId=${lineItem.lineItemId}">修改</a>
            </p:privilege>

            <p:privilege name="/viewline/removelineitem">
                <a href="javascript:void(0)" onclick="remove('${nonTopItem.lineId}', '${nonTopItem.lineItemId}')">删除</a>
            </p:privilege>
            </span>
            <div>
                <a href="javascript:void(0)" onclick="tprev(this, '${nonTopItem.lineItemId}');"><img src="/static/images/icon/up.gif"></a>
            </div>
            <div>
                <a href="javascript:void(0)" onclick="tnext(this, '20', '${nonTopItem.lineItemId}');"><img src="/static/images/icon/down.gif"></a>
            </div>
        </td>
    </tr>
</c:forEach>
<c:forEach items="${lineItems}" var="lineItem" varStatus="st">
    <tr class="
                        <c:choose>
                        <c:when test="${st.index % 2 == 0}">
                           list_table_opp_tr
                        </c:when>
                        <c:otherwise>
                            list_table_even_tr
                        </c:otherwise>
                        </c:choose>" id="${st.index}">

        <td align="center">
            <input type="checkbox" name="lineItemIds" value="${lineItem.lineItemId}">
        </td>
        <%--<td>--%>
            <%--播客: ${lineItem.profile.blog.domain} / ${lineItem.profile.blog.screenName}<br>--%>
            <%--描述: ${lineItem.itemDesc}--%>
        <%--</td>--%>
        <td>
            <%--<b>显示标题</b>：${lineItem.viewLineItem.displayInfo.subject}<br/>--%>
            <%--<b>显示连接</b>：${lineItem.viewLineItem.displayInfo.linkUrl}<br/>--%>
            <%--<b>显示图片</b>：<img src="${uf:parseBFace(lineItem.viewLineItem.displayInfo.iconUrl)}" width="110px" height="110px"/>--%>
            <%--<b>显示描述</b>：${lineItem.viewLineItem.displayInfo.desc}<br/>--%>
            <%--<table>--%>
                <%--<tr>--%>
                    <%--<td><b>显示标题</b></td>--%>
                    <%--<td>${lineItem.viewLineItem.displayInfo.subject}</td>--%>
                <%--</tr>--%>
                <%--<tr>--%>
                    <%--<td><b>显示连接</b></td>--%>
                    <%--<td><a href="${lineItem.viewLineItem.displayInfo.linkUrl}" target="_blank">${lineItem.viewLineItem.displayInfo.linkUrl}</a></td>--%>
                <%--</tr>--%>
                <%--<tr>--%>
                    <%--<td><b>显示图片</b></td>--%>
                    <%--<td><img src="${uf:parseBFace(lineItem.viewLineItem.displayInfo.iconUrl)}" /></td>--%>
                <%--</tr>--%>
                <%--<tr>--%>
                    <%--<td><b>显示描述</b></td>--%>
                    <%--<td>${lineItem.viewLineItem.displayInfo.desc}</td>--%>
                <%--</tr>--%>
            <%--</table>--%>
                <div id="displayimg" style=" position:absolute; z-index:2;"></div>
                <table width="100%">
                    <tr>
                        <td rowspan="5" width="100">
                            <a target="_blank" href="${lineItem.viewLineItem.displayInfo.linkUrl}">
                            <img width="80" height="80" src="${uf:parseBFace(lineItem.viewLineItem.displayInfo.iconUrl)}" onmousemove="show(event,this,'浏览原图')" onmouseout="hide(this)"></a>
                        </td>
                    </tr>
                    <tr>
                        <td><p>标题：<c:if test="${lineItem.viewLineItem.displayInfo.linkUrl ne null || lineItem.viewLineItem.displayInfo.linkUrl ne ''}"><a target="_blank" href="${lineItem.viewLineItem.displayInfo.linkUrl}">
                            ${lineItem.viewLineItem.displayInfo.subject}
                         </a></c:if></p></td>
                    </tr>
                    <tr><td><p>描述：
                        ${lineItem.viewLineItem.displayInfo.desc}
                    </p></td></tr>
                    <tr><td><p>连接：<a target="_blank" href="${lineItem.viewLineItem.displayInfo.linkUrl}">${lineItem.viewLineItem.displayInfo.linkUrl}</a></p></td></tr>
                    <tr><td><p>博客/昵称: ${lineItem.profile.blog.domain} / ${lineItem.profile.blog.screenName}</p></td></tr>
                </table>

        </td>
        <td>
            不支持
        </td>
        <td id="displayorder${st.index}" align="right">
                ${lineItem.displayOrder}
        </td>
        <td align="center">
            <fmt:message key="def.validstatus.${lineItem.validStatus.code}.name" bundle="${def}"/>
        </td>
        <td align="center">
            <fmt:formatDate value="${lineItem.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
        </td>
        <td align="center">
            <span>
            <p:privilege name="/viewline/preeditlineitem">
                <a href="/viewline/preeditlineitem?lineItemId=${lineItem.lineItemId}">修改</a>
            </p:privilege>

            <p:privilege name="/viewline/removelineitem">
                <a href="javascript:void(0)" onclick="remove('${lineItem.lineId}', '${lineItem.lineItemId}')">删除</a>
            </p:privilege>
            </span>
            <div>
                <a href="javascript:void(0)" onclick="prev(this, '${lineItem.lineItemId}');"><img src="/static/images/icon/up.gif"></a>
            </div><div>
                <a href="javascript:void(0)" onclick="next(this, '20', '${lineItem.lineItemId}');"><img src="/static/images/icon/down.gif"></a>
            </div>
        </td>
    </tr>
</c:forEach>
