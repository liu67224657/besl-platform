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
    <title>基本信息 ${jmh_title}</title>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/core.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/global.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/style.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/common.css?${version}"/>
</head>
<body>
<c:import url="/views/jsp/passport/header.jsp"/>
<div class="content set_content clearfix">
    <!--设置导航-->
    <%@ include file="leftmenu.jsp" %>
    <!--设置内容-->
    <div id="set_right">
        <div class="set_title">
            <h3>基本信息</h3>
        </div>
        <!--设置-基本信息-->
        <form id="form_baseinfo" action="/json/profile/customize/savebaseinfo" method="post">
        <div class="set_basic">
                <dl>
                    <dt>昵称：</dt>
                    <dd style="padding-top: 5px;"><span><c:out value="${userSession.nick}"/></span></dd>
                    <dt>个人简介：</dt>
                        <dd><textarea id="description" name="description" cols="" rows="" class="setarea" style="font-family:Tahoma, '宋体';"><c:out
                            value="${userSession.description}"/></textarea></dd>
                    <dt></dt>
                    <dd>(<span id="description_num"></span>)<em id="descriptiontips"></em></dd>
                        <dt>性别：</dt>
                        <dd>
                        <label>
                            <input type="radio" name="sex" id="sex_male"
                                   <c:if test="${userSession.sex == '1'}">checked="checked"</c:if> value="1">
                            男</label>
                        <label>
                            <input type="radio" name="sex" id="sex_female"
                                   <c:if test="${userSession.sex == '0'}">checked="checked"</c:if> value="0">
                            女</label>
                    </dd>
                        <dt>所在地：</dt>
                        <dd><input type="hidden" value="${userSession.cityId}" id="joycityid"/>
                        <select name="provinceId" id="provinceId">
                            <option value="-1">请选择</option>
                            <c:forEach items="${regionlist}" var="region" varStatus="status">
                                <c:if test="${region.regionLevel == 1}">
                                    <option value="${region.regionId}"
                                            <c:if test="${userSession.provinceId == region.regionId}">selected="selected"</c:if>>${region.regionName}</option>
                                </c:if>
                            </c:forEach>
                        </select>
                        <select name="cityid" id="cityid" style="display:none;">
                            <option value="-1" id="cityid_init">请选择</option>
                        </select>
                    </dd>
                    <dt></dt>
                    <dd><a href="javascript:void(0);" class="submitbtn" id="savebaseinfo"><span>保 存</span></a></dd>
                </dl>
            </div>
        </form>
        <!--设置-基本信息 end-->
    </div>
    <!--设置内容结束-->
</div>
<!--content结束-->
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/customize-basic-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>
