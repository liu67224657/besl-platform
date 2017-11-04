<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div class="yindao clearfix">
    <table cellspacing="0" cellpadding="0" border="0" class="div_warp">
        <tbody>
            <tr>
                <td class="top_l"></td>
                <td class="top_c"></td>
                <td class="top_r"></td>
            </tr>
            <tr>
                <td class="mid_l"></td>
                <td class="mid_c">
                    <form action="${ctx}/guide/focus" id="guideForm" name="guideForm">
                    <div class="div_cont" style="width: 620px;">
                        <div class=" cont_xinshou">
                        欢迎你来到着迷，在这里，大家一起分享一切有意思的事，比如游戏。
                            <br>您可以先看看这里有哪些你感兴趣的人，例如如下这些：
                        <a href="#" class="x"></a>
                        </div>
                        <div class="cont_c con_first">
                        <c:forEach items="${recommendUserMap}" var="recommendUser" varStatus="st">
                        <c:if test="${st.index%2==0}"><div class="yindao_list clearfix"></c:if>
                            <div class="xinshou_list clearfix">
                                <input type="checkbox" checked="checked" name="userno" value="${recommendUser.value.uno}">
                                <img src="<c:out value="${uf:parseSFace(recommendUser.value.headIcon)}"/>" width="58px" height="58px">
                                <div class="near clearfix">
                                    <p class="name" title="${recommendUser.value.screenName}">
                                    <c:choose>
                                        <c:when test="${recommendUser.value.screenName!=null && fn:length(recommendUser.value.screenName)>9}">
                                            <c:out value="${fn:substring(recommendUser.value.screenName, 0, 9)}"/>...
                                        </c:when>
                                        <c:otherwise>
                                             <c:out value="${recommendUser.value.screenName}"/>
                                        </c:otherwise>
                                    </c:choose>
                                    </p>
                                    <p class="summuray">
                                    <c:choose>
                                        <c:when test="${recommendUser.value.description!=null && fn:length(recommendUser.value.description)>15}">
                                            <c:out value="${fn:substring(recommendUser.value.description, 0, 15)}"/>...
                                        </c:when>
                                        <c:otherwise>
                                             <c:out value="${recommendUser.value.description}"/>
                                        </c:otherwise>
                                    </c:choose>
                                    </p>
                                    <p class="aboutsth">${recommendUser.value.tips}</p>
                                </div>
                            </div>
                        <c:if test="${(st.index+1)%2==0 && st.index!=0}"></div></c:if>
                        </c:forEach>
                        </div>
                        </div>
                        <div>
                        	<div class="first_cont"><a href="javascript:void(0)" onclick="guideFocus();">关注所选</a>
                        </div>
                    </div>
                    </form>
                </td>
                <td class="mid_r"></td>
            </tr>
            <tr>
                <td class="bottom_l"></td>
                <td class="bottom_c"></td>
                <td class="bottom_r"></td>
            </tr>
        </tbody>
    </table>
</div>