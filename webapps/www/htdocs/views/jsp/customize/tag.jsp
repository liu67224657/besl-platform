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
    <title>标签管理 ${jmh_title}</title>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/core.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/global.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/style.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/common.css?${version}"/>
</head>
<body>
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
<div class="content set_content clearfix">
    <!--设置导航-->
    <%@ include file="leftmenu.jsp" %>
    <!--设置内容-->
    <div id="set_right">
        <div class="set_title">
            <h3>标签管理</h3>
        </div>
        <!--设置-标签管理-->
        <div class="set_tag">
             <p id="liketag_title"><c:choose>
               <c:when test="${fn:length(likeTags)>0}">
                    我已经添加过的标签
                </c:when>
                <c:otherwise>
                   <fmt:message key="customize.addliketag.empty" bundle="${userProps}"/>
                </c:otherwise></c:choose></p>
                <ul id="favoritetag">
                    <c:forEach items="${likeTags}" var="tag" varStatus="status">
                        <li>
                            <a href="javascript:void(0)" name="tagLink"><c:out value="${tag.tag}"/></a>
                            <a href="javascript:void(0)" title="删除标签" name="tagdel" id="${tag.tagId}"
                               tag="${tag.tag}">×</a>
                        </li>
                    </c:forEach>
                </ul>

        </div>
        <form id="form_tag" action="${ctx}/json/profile/tag/like" method="post">
        <div class="set_addtag">
            <div class="set_addtaglist">

                <div class="set_tag_add" id="tags_editor_add">
                    <input type="text" name="tags" id="tags_input_add" AUTOCOMPLETE="OFF" />
                    <input name="abc" value="abc" style="display:none"/>
                </div>
                <a href="javascript:void(0)" class="submitbtn" id="savetags"><span>添加标签</span></a>
            </div>

        </div>
        </form>
        <div class="set_domain_text">
            <ul>
                <li>关于标签</li>
                <li>· 标签是自定义描述兴趣爱好的关键词，让更多人找到你，让你找到更多同类。</li>
                <li>· 已经添加的标签将显示在首页右侧栏中，方便大家了解你。</li>
                <li>· 在此查看你自己添加的所有标签，还可以方便地管理。</li>
            </ul>
        </div>

        <!--设置-标签管理 end-->
    </div>
    <!--设置内容结束-->
</div>
<!--content结束-->
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/customize-tag-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>