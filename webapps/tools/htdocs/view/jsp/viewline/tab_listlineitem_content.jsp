<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
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
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<c:forEach items="${nonTopItems}" var="nonTopItem" varStatus="vst">
    <tr class="list_table_even_tr" id="t${vst.index}">
        <td align="center">
            <input type="checkbox" name="lineItemIds" value="${nonTopItem.lineItemId}">
        </td>
        <td class="">
            <div id="displayimg2" style=" position:absolute; z-index:2;"></div>
            <table width="100%">
                <tr>
                    <td rowspan="4" width="100">
                        <a target="_blank" href="
                        <c:choose>
                            <c:when test="${nonTopItem.viewLineItem.displayInfo.linkUrl eq '' || nonTopItem.viewLineItem.displayInfo.linkUrl eq null}">
                                ${URL_WWW}/note/${nonTopItem.content.contentId}
                            </c:when>
                            <c:otherwise>
                                ${nonTopItem.viewLineItem.displayInfo.linkUrl}
                            </c:otherwise>
                        </c:choose>

                        ">
                        <img width="80" height="80" src="${uf:parseOrgImg(nonTopItem.viewLineItem.displayInfo.iconUrl)}" onmousemove="show(event,this,'浏览原图')" onmouseout="hide(this)"></a>
                    </td>
                </tr>
                <tr>
                    <td><p>标题：<a target="_blank" href="
                        <c:choose>
                            <c:when test="${nonTopItem.viewLineItem.displayInfo.linkUrl eq '' || nonTopItem.viewLineItem.displayInfo.linkUrl eq null}">
                                ${URL_WWW}/note/${nonTopItem.content.contentId}
                            </c:when>
                            <c:otherwise>
                                ${nonTopItem.viewLineItem.displayInfo.linkUrl}
                            </c:otherwise>
                        </c:choose>
                    ">
                    <c:choose>
                        <c:when test="${nonTopItem.viewLineItem.displayInfo.subject eq '' || nonTopItem.viewLineItem.displayInfo.subject eq null}">
                            ${nonTopItem.content.subject}
                        </c:when>
                        <c:otherwise>
                            ${nonTopItem.viewLineItem.displayInfo.subject}
                        </c:otherwise>
                    </c:choose>
                     </a></p></td>
                </tr>
                <tr><td><p>描述：
                    <c:choose>
                        <c:when test="${nonTopItem.viewLineItem.displayInfo.desc eq '' || nonTopItem.viewLineItem.displayInfo.desc eq null}">
                            <c:out value="${nonTopItem.content.content}"/>
                        </c:when>
                        <c:otherwise>
                            ${nonTopItem.viewLineItem.displayInfo.desc}
                        </c:otherwise>
                    </c:choose>
                </p></td></tr>
                <tr><td><p>作者： <a target="_blank" href="http://${nonTopItem.profile.domain}.${DOMAIN}">${nonTopItem.profile.screenName}</a></p></td></tr>
            </table>
        </td>
        <td align="center">
            <c:choose><c:when test="${nonTopItem.displayType.isTop()}">是</c:when><c:otherwise>否</c:otherwise></c:choose>
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
                <a href="/viewline/preeditlineitem?lineItemId=${nonTopItem.lineItemId}">修改</a>
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
            <%--<b>作者</b>: ${lineItem.profile.screenName}<br/>--%>
            <%--<b>标题</b>: ${lineItem.content.subject} <br/>--%>
            <%--<b>描述</b>: ${lineItem.itemDesc}<br/>--%>
        <%--</td>--%>
        <td class="">
            <%--<b>显示标题</b>：${lineItem.viewLineItem.displayInfo.subject}<br/>--%>
            <%--<b>显示连接</b>：${lineItem.viewLineItem.displayInfo.linkUrl}<br/>--%>
            <%--<b>显示图片</b>：<img src="${uf:parseBFace(lineItem.viewLineItem.displayInfo.iconUrl)}" width="110px" height="110px"/><br/>--%>
            <%--<b>显示描述</b>：${lineItem.viewLineItem.displayInfo.desc}<br/>--%>
            <%--<table width="100%">--%>
                <%--<tr>--%>
                    <%--<td width="30%"><b>显示标题</b></td>--%>
                    <%--<td width="70%">${lineItem.viewLineItem.displayInfo.subject}</td>--%>
                <%--</tr>--%>
                <%--<tr>--%>
                    <%--<td width="30%"><b>显示连接</b></td>--%>
                    <%--<td width="70%"><a href="${lineItem.viewLineItem.displayInfo.linkUrl}" target="_blank">${lineItem.viewLineItem.displayInfo.linkUrl}</a></td>--%>
                <%--</tr>--%>
                <%--<tr>--%>
                    <%--<td width="30%" class=""><b>显示图片</b></td>--%>
                    <%--<td width="70%">--%>
                        <%--<c:if test="${lineItem.viewLineItem.displayInfo.iconUrl ne null}">--%>
                            <%--<img src="${uf:parseBFace(lineItem.viewLineItem.displayInfo.iconUrl)}" />--%>
                        <%--</c:if>--%>
                    <%--</td>--%>
                <%--</tr>--%>
                <%--<tr>--%>
                    <%--<td width="30%"><b>显示描述</b></td>--%>
                    <%--<td width="70%">${lineItem.viewLineItem.displayInfo.desc}</td>--%>
                <%--</tr>--%>
            <%--</table>--%>

            <div id="displayimg" style=" position:absolute; z-index:2;"></div>
            <table width="100%">
                <tr>
                    <td rowspan="4" width="100">
                        <a target="_blank" href="
                        <c:choose>
                            <c:when test="${lineItem.viewLineItem.displayInfo.linkUrl eq '' || lineItem.viewLineItem.displayInfo.linkUrl eq null}">
                                ${URL_WWW}/note/${lineItem.content.contentId}
                            </c:when>
                            <c:otherwise>
                                ${lineItem.viewLineItem.displayInfo.linkUrl}
                            </c:otherwise>
                        </c:choose>

                        ">
                        <img width="80" height="80" src="${uf:parseBFace(lineItem.viewLineItem.displayInfo.iconUrl)}" onmousemove="show(event,this,'浏览原图')" onmouseout="hide(this)"></a>
                    </td>
                </tr>
                <tr>
                    <td><p>标题：<a target="_blank" href="
                        <c:choose>
                            <c:when test="${lineItem.viewLineItem.displayInfo.linkUrl eq '' || lineItem.viewLineItem.displayInfo.linkUrl eq null}">
                                ${URL_WWW}/note/${lineItem.content.contentId}
                            </c:when>
                            <c:otherwise>
                                ${lineItem.viewLineItem.displayInfo.linkUrl}
                            </c:otherwise>
                        </c:choose>
                    ">
                    <c:choose>
                        <c:when test="${lineItem.viewLineItem.displayInfo.subject eq '' || lineItem.viewLineItem.displayInfo.subject eq null}">
                            ${lineItem.content.subject}
                        </c:when>
                        <c:otherwise>
                            ${lineItem.viewLineItem.displayInfo.subject}
                        </c:otherwise>
                    </c:choose>
                     </a></p></td>
                </tr>
                <tr><td><p>描述：
                    <c:choose>
                        <c:when test="${lineItem.viewLineItem.displayInfo.desc eq '' || lineItem.viewLineItem.displayInfo.desc eq null}">
                            <%--<c:choose>--%>
                                <%--<c:when test="${fn:length(lineItem.content.content)>=30}">--%>
                                    <%--<c:out value="${fn:substring(lineItem.content.content, 0, 30)}"/>...--%>
                                <%--</c:when>--%>
                                <%--<c:otherwise>--%>
                                    <%--<c:out value="${fn:substring(lineItem.content.content, 0, 30)}"/>--%>
                                <%--</c:otherwise>--%>
                            <%--</c:choose>--%>
                            <c:out value="${lineItem.content.content}"/>
                        </c:when>
                        <c:otherwise>
                            ${lineItem.viewLineItem.displayInfo.desc}
                        </c:otherwise>
                    </c:choose>
                </p></td></tr>
                <tr><td><p>作者： <a target="_blank" href="http://${lineItem.profile.domain}.${DOMAIN}">${lineItem.profile.screenName}</a></p></td></tr>
            </table>
        </td>
        <td align="center">
            <c:choose><c:when test="${lineItem.displayType.isTop()}">是</c:when><c:otherwise>否</c:otherwise></c:choose>
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
            </span>
            <div>
                <a href="javascript:void(0)" onclick="prev(this, '${lineItem.lineItemId}');"><img src="/static/images/icon/up.gif"></a>
            </div><div>
                <a href="javascript:void(0)" onclick="next(this, '20', '${lineItem.lineItemId}');"><img src="/static/images/icon/down.gif"></a>
            </div>
            <%--<a href="${URL_WWW}/home/${lineItem.profile.domain}/${lineItem.content.contentId}" target="_blank">查看链接</a>--%>
            <%--<p:privilege name="/viewline/removelineitem">--%>
                <%--<a href="#" onclick="remove('${lineItem.lineId}', '${lineItem.lineItemId}')">删除</a>--%>
            <%--</p:privilege>--%>
        </td>
    </tr>
</c:forEach>
