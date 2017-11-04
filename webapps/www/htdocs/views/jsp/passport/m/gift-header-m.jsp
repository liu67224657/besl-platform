<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<script>
    document.write('<script src="http://passport.${DOMAIN}/auth/header/m?v=' + Math.random() + '"><\/script>');
</script>
<div class="topbar border-b topbar-mod">
    <h1 class="logo-img">
        <a href="http://m.joyme.com/"><i><img src="http://static.joyme.com/mobile/cms/jmsy/images/logo-icon2.png"
                                              alt="资讯" title="资讯" width="100%"></i> </a>
        <a href="/gift">礼包中心
        </a>
        <c:choose>
            <c:when test="${not empty mygiftdisplay}">
                <a href="${URL_M}/mygift/m" style="display:none;">我的礼包</a>

            </c:when>
            <c:otherwise>
                <c:choose>
                    <c:when test="${not empty userSession}">
                        <a href="${URL_M}/mygift/m">我的礼包</a>
                    </c:when>
                    <c:otherwise>
                        <a href="javascript:mygiftLoginDiv();">我的礼包</a>
                    </c:otherwise>
                </c:choose>
            </c:otherwise>
        </c:choose>
        <cite class="nav-icon"></cite>
    </h1>
</div>
