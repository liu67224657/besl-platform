<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <script src="${URL_LIB}/static/js/common/seajs.js"></script>
    <script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
</head>
<body>
<div id="wraper">
    <c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
    <div class="content homebg clearfix" id="content">
        <!--左侧开始-->
        <div id="cont_left" class="conleft">
            <div style="height: 22px;margin-top: 10px;width: 256px">您处在 >> <a href="${URL_WWW}/editor/category">分类</a> >> ${viewCategory.categoryName}</div>
            <div id="div_post_area" class="comment clearfix">
                <!--发布开始-->
                <div id="post_area" class="conmentbd clearfix">
                    <%@ include file="/views/jsp/post/post-editor-frame.jsp" %>
                    <div class="commentline"></div>
                    <!--发布结束-->
                </div>
                <div id="memo_content" style="display:none" class="mgtips">
                </div>
                <%@ include file="/views/jsp/content/content-previewlist.jsp" %>
                <!-- 内容展示list -->
            </div>
            <!--左侧结束-->
            <input type="hidden" id="hidden_pageNo" value="${page.curPage}"/>
            <input type="hidden" id="hidden_totalRows" value="${page.totalRows}"/>
            <c:set var="pageparam" value="cgid=${categoryId}"/>
            <c:set var="pageurl" value="${URL_WWW}/editor/list"/>
            <%@ include file="/views/jsp/page/pagenoend.jsp" %>
        </div>
        <!--右侧开始-->
        <%@ include file="/views/jsp/tiles/rightmenu.jsp" %>
        <!--右侧结束-->
    </div>
    <%@ include file="/views/jsp/tiles/footer.jsp" %>


</body>
<script>
    seajs.use("${URL_LIB}/static/js/init/editorlist-init.js")
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</html>
