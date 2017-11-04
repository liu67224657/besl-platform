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
    <title>个人信息 ${jmh_title}</title>
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
        <!--设置title-->
        <div class="set_title">
            <h3>个人信息</h3>
        </div>
        <!--设置title end-->

        <!--设置-个人信息-->
        <div class="set_domain_text">
            <p>填写你的学校信息和职业信息。</p>
        </div>
        <div id="addSchool">
            <c:choose>
                <c:when test="${fn:length(profileExperienceDTO.schoolExp)>0}">
                    <div class="set_adduserdata_line" id="school">
                        <div class="set_adduserdata">
                            <p>学校</p>

                            <div class="set_adduserdata_list">
                                <ul id="scLi">
                                    <div>
                                        <c:choose>
                                            <c:when test="${profileExperienceDTO.expSchAllowType.getCode() eq 'a'}">
                                                (对所有人可见)
                                            </c:when>
                                            <c:otherwise>(仅对我关注的人可见)</c:otherwise>
                                        </c:choose>
                                        <a href="javascript:void(0)" id="${profileExperienceDTO.expSchAllowType.getCode()}" name="updateSchool" class="schcool_change">修改</a>
                                    </div>
                                    <c:forEach var="exp" items="${profileExperienceDTO.schoolExp}"
                                               varStatus="st">
                                        <li><c:out value="${exp.expName}"/></li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="addgame" id="addschoolbtn" style="display:none;">
                        <a href="javascript:void(0)">+ 添加学校信息</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="addgame" id="addschoolbtn">
                        <a href="javascript:void(0)">+ 添加学校信息</a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>


        <div id="addCompany">
            <c:choose>
                <c:when test="${fn:length(profileExperienceDTO.companyExp)>0}">
                    <div class="set_adduserdata_line unline" id="company">
                        <div class="set_adduserdata">
                            <p>公司</p>
                            <div class="set_adduserdata_list">
                                <ul id="comLi">
                                    <div>
                                        <c:choose>
                                            <c:when test="${profileExperienceDTO.expCompAllowType.getCode() eq 'a'}">(对所有人可见)</c:when>
                                            <c:otherwise>(仅对我关注的人可见)</c:otherwise>
                                        </c:choose>
                                        <a href="javascript:void(0)" name="updateCompany" id = "${profileExperienceDTO.expCompAllowType.getCode()}" class="schcool_change">修改</a>
                                    </div>
                                    <c:forEach var="exp" items="${profileExperienceDTO.companyExp}"
                                               varStatus="st">
                                        <li><c:out value="${exp.expName}"/></li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="addgame clear_b" id="addCompanyBtn" style="display:none;">
                        <a href="javascript:void(0)">+ 添加单位信息</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="addgame clear_b" id="addCompanyBtn">
                        <a href="javascript:void(0)">+ 添加单位信息</a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
        <!--设置-个人信息 end-->
    </div>
    <!--设置内容结束-->
</div>
<!--content结束-->
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/customize-experience-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>
