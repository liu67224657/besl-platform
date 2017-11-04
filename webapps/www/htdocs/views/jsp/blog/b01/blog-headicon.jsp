<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div class="con_area con_blog con_min clearfix">
    <div class="bokehead clearfix">
        <div class="boketx">
            <c:choose>
                <c:when test="${userSession!=null && userSession.blogwebsite.uno==profile.blog.uno}">
                    <a href="${URL_WWW}/profile/customize/headicon">
                        <img src="<c:out value='${uf:parseFacesInclude(profile.blog.headIconSet,profile.detail.sex,"m" , true, 0, 1)[0]}'/>"
                             id="headimgid" width="150"
                             height="150"/>
                    </a>
                </c:when>
                <c:otherwise>
                    <img src="<c:out value='${uf:parseFacesInclude(profile.blog.headIconSet,profile.detail.sex,"m" , true, 0, 1)[0]}'/>"
                         id="headimgid" width="150"
                         height="150"/>
                </c:otherwise>
            </c:choose>

        </div>
        <div class="boketxdetails">
            <div>
                <!-- 积分  -->
                <%--<c:choose>--%>
                <%--<c:when test="${walletBalance!=null && walletBalance.moneyAmount != null}">--%>
                <%--<span class="integral">--%>
                <%--<a href="http://www.joyme.com/home/joyme/0-dPpegnp37FMoMIHxFaBo" title="${walletBalance.moneyAmount}" target="_blank">--%>
                <%--<c:out value="${jstr:long2Str(walletBalance.moneyAmount, walletBalance.getMoneyCode())}"/>--%>
                <%--</a>--%>
                <%--</span>--%>
                <%--</c:when>--%>
                <%--<c:otherwise>--%>
                <%--<a href="http://www.joyme.com/home/joyme/0-dPpegnp37FMoMIHxFaBo" target="_blank"><span--%>
                <%--class="integral">积分：0</span></a>--%>
                <%--</c:otherwise>--%>
                <%--</c:choose>--%>
                <!-- 积分  -->
                <h3>${profile.blog.screenName}
                    <c:if test="${profile.detail.verifyType!=null && profile.detail.verifyType.code!='n'}">
                        <a class="${profile.detail.verifyType.code}vip"
                           title="<fmt:message key="verify.profile.${profile.detail.verifyType.code}" bundle="${userProps}"/>"></a>
                    </c:if>
                </h3>
            </div>
            <c:if test="${(profile.detail.provinceId!=null && profile.detail.provinceId!=-1) || profile.detail.sex != null}">
                <div class="bokesex">
                    <c:choose>
                        <c:when test="${profile.detail.sex=='1'}"><b class="nan"></b></c:when>
                        <c:when test="${profile.detail.sex=='0'}"><b class="nv"></b></c:when>
                    </c:choose>
                    <c:if test="${profile.detail.provinceId!=null && profile.detail.provinceId!=-1}">
                        <c:out value="${st:regionById(profile.detail.provinceId)}"/>
                    </c:if>
                    <c:if test="${profile.detail.cityId!=null && profile.detail.cityId!=-1}">
                        <c:out value="${st:regionById(profile.detail.cityId)}"/>
                    </c:if>
                </div>
            </c:if>
            <div class="yum">
                <a href="${URL_WWW}/people/<c:out value="${profile.blog.domain}"/>">${URL_WWW}/people/<c:out
                        value="${profile.blog.domain}"/></a>
            </div>
            <c:if test="${profile.blog.description!=null && fn:length(profile.blog.description)>0 && !profile.blog.auditStatus.isBlogDescIllegal()}">
                <div class="brief">
                    个人简介：
                    <c:choose>
                        <c:when test="${fn:length(profile.blog.description)>=140}">
                            <c:out value="${fn:substring(profile.blog.description, 0, 140) }"/>...
                        </c:when>
                        <c:otherwise>
                            <c:out value="${profile.blog.description}"/>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>
            <div class="blog_related">
                <span>
                <c:choose>
                    <c:when test="${userSession.blogwebsite.uno eq profile.blog.uno}">
                    </c:when>
                    <c:when test="${relation.srcStatus.code eq 'y' && relation.destStatus.code eq 'n'}">
                        <a class="attentioned" href="javascript:void(0)" id="attention_${profile.blog.uno}"
                           name="unfollow" data-uno="${profile.blog.uno}"></a>
                    </c:when>
                    <c:when test="${relation.srcStatus.code eq 'y' && relation.destStatus.code eq 'y'}">
                        <a class="attentionedall" href="javascript:void(0)" id="attention_${profile.blog.uno}"
                           name="unfollow"
                           data-uno="${profile.blog.uno}"></a>
                    </c:when>
                    <c:when test="${relation.srcStatus.code eq 'n' && relation.destStatus.code eq 'y'}">
                        <a class="add_attention_ok" href="javascript:void(0)" id="attention_${profile.blog.uno}"
                           name="follow"
                           data-uno="${profile.blog.uno}"></a>
                    </c:when>
                    <c:otherwise>
                        <a class="add_attention" href="javascript:void(0)" id="attention_${profile.blog.uno}"
                           name="follow" data-uno="${profile.blog.uno}"></a>
                    </c:otherwise>
                </c:choose>
                </span>
                <c:if test="${userSession.blogwebsite.uno != profile.blog.uno}">
                <span class="atsb">
                    <a href="javascript:void(0)" name="atTa" id="atta_${profile.blog.uno}"
                       data-nick="${profile.blog.screenName}">
                        <c:choose>
                            <c:when test="${profile.detail.sex=='0'}">她</c:when>
                            <c:otherwise>他</c:otherwise>
                        </c:choose></a>
                </span>
                <span class="blog_privateleter">
                    <a href="javascript:void(0)" name="sendMsgMask" id="sendmsg_${profile.blog.uno}"
                       data-nick="${profile.blog.screenName}">私信</a>
                </span>
                </c:if>
            </div>
        </div>
    </div>
    <!-- 多头像开始 -->
    <c:if test="${userSession!=null && userSession.blogwebsite.uno eq profile.blog.uno && profile.blog.headIconSet.validSize()==1}">
        <div class="uploadtx">
            <a href="${ctx}/profile/customize/headicon">+上传更多头像</a>
        </div>
    </c:if>
    <c:if test="${profile.blog.headIconSet.validSize()>1}">
        <div class="multitx clearfix" id="headIcons">
            <c:forEach var="headIcons" varStatus="status"
                       items="${uf:parseFacesInclude(profile.blog.headIconSet, userSession.userDetailinfo.sex,'m', true, 1,7)}">
                <c:choose>
                    <c:when test="${userSession!=null && userSession.blogwebsite.uno==profile.blog.uno}">
                <span><a href="${URL_WWW}/profile/customize/headicon">
                    <img src="${headIcons}" width="88px" height="88px"/></a>
                </span>
                    </c:when>
                    <c:otherwise>
                        <span><a href="javascript:void(0)"><img src="${headIcons}" width="88px"
                                                                height="88px"/></a></span>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <c:if test="${userSession!=null && userSession.blogwebsite.uno eq profile.blog.uno && profile.blog.headIconSet.validSize()<8}">
                <a class="manytxadd" href="${ctx}/profile/customize/headicon"></a>
            </c:if>
        </div>
        <div style="clear:both"></div>
    </c:if>
    <c:if test="${userSession!=null && userSession.blogwebsite.uno != profile.blog.uno && profile.blog.headIconSet.validSize()>1}">
        <div class="scmanytx"><a href="${ctx}/profile/customize/headicon">我也要设置多个头像&gt;&gt;</a></div>
    </c:if>
    <!-- 多头像结束 -->
</div>
