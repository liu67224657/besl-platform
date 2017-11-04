<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/jstllibs.jsp" %>
<!--头部开始-->

<div id="newgames-topbar">
    <div class="clearfix">
        <h1 style="cursor:pointer">着迷鲜游库</h1>

        <div class="newgames-topbar-link">

            <a href="${URL_WWW}" class="newgames-link-1">返回着迷首页</a>
            <c:choose>
                <c:when test="${userSession!=null}">
        <span>
        你好，${userSession.blogwebsite.screenName}<a href="javascript:void(0)" id="confirmloginout">退出</a>
         <span style="display:none;">
              <a href="http://passport.${DOMAIN}/auth/logout<c:if test="${fn:indexOf(pageContext.request.requestURI,'hot.jsp')>0}">?reurl=<c:out
                                    value="/discovery/hot"/></c:if>
             <c:if test="${fn:indexOf(pageContext.request.requestURI,'bloglist.jsp')>0}">?rl=http://${pageContext.request.serverName}</c:if>
             <c:if test="${fn:indexOf(pageContext.request.requestURI,'wall.jsp')>0}">?rl=<c:out value="/discovery"/></c:if>
             <c:if  test="${redr ne null}">?rl=<c:out value="${redr}"/></c:if>"><span id="newgameloginout"></span></a>
         </span>
       </span>
                </c:when>
                <c:otherwise>
                    <a href="${URL_WWW}/loginpagedev" name="slideLogin" class="newgames-link-2">机构帐号登录</a>
                    <a href="${URL_WWW}/developers/registerpage" class="newgames-link-3">申请注册</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/developer-login-init.js');
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
