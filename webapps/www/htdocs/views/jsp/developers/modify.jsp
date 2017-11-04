<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta name="Keywords" content="">
    <meta name="description"
          content=""/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>注册</title>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/global.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/style.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/common.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/core.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/newgames.css?${version}"/>
</head>
<body>
<!-- topbar -->
<c:import url="/tiles/gamedbheader?redr=${requestScope.browsersURL}"/>

<!-- content -->
<div class="newgames-content clearfix">
    <h2 class="newgames-title">编辑认证信息<span>以下信息仅用于认证信息审核，将不会公开给其他人，请放心填写</span></h2>
    <form action="/developers/modify" method="post" id="form-submit">
        <table class="post-newgame" width="100%">
            <tbody>
            <tr>
                <td>
                    <input type="hidden" name="category" value="${developer.category.code}"/>
                    <input type="hidden" id="input_city" value="${developer.location.city.id}"/>
                </td>
            </tr>
            <tr>
                <th width="120">认证说明：</th>
                <td width="350"><input type="text" name="verifydesc" id="input_desc" class="inputs-1" placeholder="将显示为在着迷的认证信息" value="${developer.verifyDesc}"></td>
                <td>
                    <div class="tips"><b>*</b>例如“扑家汉化组官方帐号”，最多填写18个字</div>
                </td>
            </tr>
            <tr>
                <th>联系人姓名：</th>
                <td><input type="text" name="contacts" id="input_contacts" class="inputs-1" placeholder="请填写联系人姓名，如有变动以后可修改" value="${developer.contacts}"></td>
                <td>
                    <div class="tips"></div>
                </td>
            </tr>
            <tr>
                <th>联系邮箱：</th>
                <td><input type="text" name="email" id="input_email" class="inputs-1" placeholder="建议填写企业邮箱" value="${developer.email}"></td>
                <td>
                    <div class="tips"></div>
                </td>
            </tr>
            <tr>
                <th>联系QQ：</th>
                <td><input type="text" name="qq" id="input_qq" class="inputs-1" placeholder="请填写联系人使用的QQ" value="${developer.qq}"></td>
                <td>
                    <div class="tips"></div>
                </td>
            </tr>
            <tr>
                <th>联系人电话：</th>
                <td><input type="text" name="phone" id="input_phone" class="inputs-1" placeholder="请填写联系人使用的电话" value="${developer.phone}"></td>
                <td>
                    <div class="tips"></div>
                </td>
            </tr>
            <c:if test="${developer.category != 'personal'}">
                <tr>
                    <th>公司名称：</th>
                    <td><input type="text" name="company" id="input_company" class="inputs-1" placeholder="请填写联系人使用的电话" value="${developer.company}"></td>
                    <td>
                        <div class="tips"></div>
                    </td>
                </tr>
            </c:if>
            <c:if test="${developer.category != 'personal'}">
            <tr>
                <th>所在区域：</th>
                <td>
                    <select name="province" id="province">
                        <option value="-1">请选择</option>
                        <c:forEach items="${regionlist}" var="region" varStatus="status">
                            <c:if test="${region.regionLevel == 1}">
                                <option value="${region.regionId}"
                                        <c:if test="${developer.location.province.id == region.regionId}">selected="selected"</c:if>>${region.regionName}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                    <select name="city" id="city" style="display: none;">
                        <option value="-1" id="city_init">请选择</option>
                    </select>
                </td>
                <td>
                    <div class="tips"></div>
                </td>
            </tr>
            </c:if>
            </tbody>
        </table>
        <div class="send-newgame-btn"><a href="javascript:void(0)" class="submitbtn"><span>申请更新信息</span></a></div>
    </form>

</div>

<!-- 页脚 -->
<%@ include file="/views/jsp/tiles/footer.jsp" %>

<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/developer-modify-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
<script type="text/javascript" src="${URL_LIB}/static/js/common/bdhm-noseajs.js"></script>
</body>
</html>