<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>认证申请 ${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/style.css?${version}" rel="stylesheet" type="text/css"/>
    <%--<script type="text/javascript" src="${URL_LIB}/static/js/common/google-statistics.js"></script>--%>
</head>
<body>
<%@ include file="/views/jsp/tiles/header-only-icon.jsp" %>
<div id="wraper" class="content about-bg clearfix">
    <%@ include file="/views/jsp/help/left.jsp" %>
    <div class="about-right">
        <div class="renzheng">
            <img src="${URL_LIB}/static/theme/default/img/renzhenpic.jpg"/>

            <h3>如果您满足以上条件某一项并想申请认证的话，您需要：</h3>

            <p>• 在着迷网申请账号并上传头像，<br/>
                • 进行优质内容的建设<br/>
                然后发送如下信息到 <b><a href="mailto:verify@joyme.com">verify@joyme.com</a></b>
            </p>

            <div class="rz_con">
                <div class="rz_corner"></div>
                <div class="rz_tit01"></div>
                <p>
                    <em>提供资料：</em>真实姓名、联系电话、认证说明、身份证复印件、申请认证内容的证明等
                    <br/><em>申请表下载：</em><a href="${URL_LIB}/static/download/person.doc">着迷网个人认证申请表.doc</a>
                </p>

                <div class="rz_tit02"></div>
                <p><em>提供资料：</em>公司联系人、联系电话、营业执照复印件、认证说明、游戏著作权证复印件等
                    <br/><em>申请表下载：</em><a href="${URL_LIB}/static/download/company.doc">着迷网公司&官方认证申请表.doc</a>
                </p>
            </div>
            请将以上相应资料，和填好的申请表电子版发送到我们的邮箱：<b><a href="mailto:verify@joyme.com">verify@joyme.com</a></b>
            <br/>我们会在三个工作日内答复您。
        </div>

    </div>
    <!--about-right结束-->
</div>
<!--content结束-->
<%@ include file="/views/jsp/tiles/footer.jsp" %>
</div>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
<div class="piccon"></div>
</body>
</html>
