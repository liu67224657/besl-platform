<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <title>域名设置 ${jmh_title}</title>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/core.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/global.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/style.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/common.css?${version}"/>
</head>
<body>
<c:import url="/views/jsp/passport/header.jsp"/>
<div class="content set_content clearfix">
    <%@ include file="leftmenu.jsp" %>
    <!--设置内容-->
    <div id="set_right">
        <div class="set_title">
            <h3>域名设置</h3>
        </div>
        <!--设置-域名设置-->
        <div class="set_domain_text">
            <ul>
                <li>记得自己的博客地址是什么吗？设置个性域名，让朋友更容易记住！</li>
                <li>可以输入5至20位的英文、数字和横线。</li>
                <li>修改个性域名后，之前使用的域名将会失效。</li>
                <li>之前使用的域名被其他人使用，你将无法恢复之前使用的域名！</li>
            </ul>
        </div>
        <form method="post" id="form_domaininfo" action="${URL_WWW}/json/profile/customize/savedomain">
            <div class="set_domain_text domain">
                <ul>
                    <li>设置个性域名</li>
                    <li>${URL_WWW}/people/<input type="text" class="domain_input" onkeydown="this.onkeyup(); "
                                      onkeyup="this.size=(this.value.length&gt; 6?this.value.length+4:6); " size="6"
                                      maxlength="20" value="<c:out value="${userSession.domain}"/>"
                                      name="blogdomain" id="blogdomain">
                    </li>
                    <li><em id="blogdomaintips"></em></li>
                    <li><a href="javascript:void(0)" class="submitbtn" id="savedomaininfo"><span>保 存</span></a></li>
                </ul>
            </div>
        </form>
        <!--设置-域名设置 end-->
    </div>
    <!--设置内容结束-->
</div>
<!--content结束-->
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/customize-domain-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>
