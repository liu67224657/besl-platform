<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!--设置导航-->
<div id="set_left">
    <ul>
        <li><a href="${ctx}/profile/customize/basic" <c:if test="${nav == 'basic'}">class="setnavon"</c:if>>基本信息</a></li>
        <li><a href="${ctx}/profile/customize/headicon" <c:if test="${nav == 'headicon'}">class="setnavon"</c:if>>头像设置</a></li>
        <%--<li><a href="${ctx}/profile/customize/domain" <c:if test="${nav == 'domain'}">class="setnavon"</c:if>>域名设置</a></li>--%>
        <li><a href="${ctx}/profile/customize/bind" <c:if test="${nav == 'bind'}">class="setnavon"</c:if>>绑定社区</a></li>
       <li><a href="${ctx}/profile/mobile/gopage" <c:if test="${nav == 'mobile'}">class="setnavon"</c:if>>手机绑定</a></li>
        <%--<li><a href="${ctx}/profile/customize/game" <c:if test="${nav == 'game'}">class="setnavon"</c:if>>游戏资料</a></li>--%>
        <%--<li><a href="${ctx}/profile/customize/experience" <c:if test="${nav == 'experience'}">class="setnavon"</c:if>>个人信息</a></li>--%>
        <c:if test="${userSession.flag.hasPassword()}">
            <li><a href="${ctx}/profile/customize/pwd" <c:if test="${nav == 'pwd'}">class="setnavon"</c:if>>修改密码</a></li>
            <li><a href="${ctx}/profile/customize/email" <c:if test="${nav == 'email'}">class="setnavon"</c:if>>邮箱管理</a></li>
        </c:if>
        <%--<li><a href="${ctx}/activity/pay" <c:if test="${nav == 'pay'}">class="setnavon"</c:if>>充值</a></li>--%>
        <%--<li><a href="${ctx}/profile/customize/define" <c:if test="${nav == 'define'}">class="setnavon"</c:if>>互动设置</a></li>--%>
        <%--<li class="last"><a href="${ctx}/profile/customize/tag" <c:if test="${nav == 'tag'}">class="setnavon"</c:if>>标签管理</a></li>--%>
    </ul>
</div>
<!--设置导航结束-->