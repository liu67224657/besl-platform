<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <%--<script type="text/javascript" src="${URL_LIB}/static/js/common/google-statistics.js"></script>--%>
    <script src="${URL_LIB}/static/js/common/seajs.js"></script>
    <script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<head>

</head>
<body>
 <div id="wraper">
    <c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
     <input type="button" value="上传" id="uploadBuuton"/>
     <form id="uploadform" method="post" enctype="multipart/form-data" target="uploadiframe" action="http://www.baidu.com">
         <input type="file" id="upload_file" style="display:none"/>
     </form>
     <iframe vid="iframe_upload" name="uploadiframe" style="display:none">

     </iframe>
    <%@ include file="/views/jsp/tiles/footer.jsp" %>
<script>
    seajs.use("${URL_LIB}/static/js/init/test-init.js")
</script>
</body>
</html>
