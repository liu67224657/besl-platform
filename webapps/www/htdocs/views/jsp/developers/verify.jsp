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
    <title>认证</title>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/global.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/style.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/common.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/core.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/newgames.css?${version}"/>
    <script language="JavaScript">
        javascript:window.history.forward(1);
    </script>
</head>
<body>
<!-- topbar -->
<c:import url="/tiles/gamedbheader?redr=${requestScope.browsersURL}"/>

<!-- content -->
<div class="newgames-content clearfix">
    <h2 class="newgames-title">申请
        <c:forEach items="${categoryCollection}" var="cat">
            <c:if test="${cat.code == category}"><fmt:message key="profile.developer.category.${cat.code}" bundle="${userProps}"/></c:if>
        </c:forEach>
        <span>以下信息仅用于认证信息审核，将不会公开给其他人，请放心填写</span>
    </h2>


    <form action="/developers/verify" method="post" id="form-submit">
        <table class="post-newgame" width="100%">
            <tbody>
            <tr>
                <td>
                    <input type="hidden" name="category" id="input_category" value="${category}"/>
                    <input type="hidden" id="input_city" value="${cityId}"/>
                    <input type="hidden" id="input_reverify" name="reverify" value="${reVerify}"/>
                </td>
            </tr>
            <tr>
                <th width="120">认证说明：</th>
                <td width="350"><input type="text" name="verifydesc" id="input_desc" class="inputs-1"
                                       placeholder="将显示为在着迷的认证信息" value="${verifyDesc}"></td>
                <td>
                    <div class="tips"><b>*</b>例如“扑家汉化组官方帐号”，最多填写18个字</div>
                </td>
            </tr>
            <tr>
                <th width="120"></th>
                <td width="350" style="color: #ff0000;">
                    <c:if test="${fn:length(descError) > 0}"><fmt:message key="${descError}" bundle="${userProps}"/></c:if>
                </td>
                <td>
                    <div></div>
                </td>
            </tr>
            <tr>
                <th>联系人姓名：</th>
                <td><input type="text" name="contacts" id="input_contacts" class="inputs-1"
                           placeholder="请填写联系人姓名，如有变动以后可修改" value="${contacts}"></td>
                <td>
                    <div class="tips"><b>*</b>最多填写18个字</div>
                </td>
            </tr>
            <tr>
                <th>联系邮箱：</th>
                <td><input type="text" name="email" id="input_email" class="inputs-1" placeholder="建议填写企业邮箱"
                           value="${email}"></td>
                <td>
                    <div class="tips"><b>*</b>注意邮箱的格式，最多40个字符</div>
                </td>
            </tr>
            <tr>
                <th>联系QQ：</th>
                <td><input type="text" name="qq" id="input_qq" class="inputs-1" placeholder="请填写联系人使用的QQ" value="${qq}">
                </td>
                <td>
                    <div class="tips"><b>*</b>20个字符以内的数字</div>
                </td>
            </tr>
            <tr>
                <th>联系人电话：</th>
                <td><input type="text" name="phone" id="input_phone" class="inputs-1" placeholder="请填写联系人使用的电话"
                           value="${phone}"></td>
                <td>
                    <div class="tips"><b>*</b>必须以数字开头和结尾，中间可以有“-”或“+”隔开，最多20个字符</div>
                </td>
            </tr>
            <c:if test="${category != personal}">
                <tr>
                    <th>公司名称：</th>
                    <td><input type="text" name="company" id="input_company" class="inputs-1" placeholder="请填写真实有效公司名称"
                               value="${company}">
                    </td>
                    <td>
                        <div class="tips"><b>*</b>最多填写18个字</div>
                    </td>
                </tr>
            </c:if>
            <c:if test="${category != personal}">
                <tr>
                    <th>所在区域：</th>
                    <td>
                            <%--<select name="country">--%>
                            <%--<option value="中国">中国</option>--%>
                            <%--</select>--%>
                        <select name="province" id="province">
                            <option value="-1">请选择</option>
                            <c:forEach items="${regionlist}" var="region" varStatus="status">
                                <c:if test="${region.regionLevel == 1}">
                                    <option value="${region.regionId}" <c:if test="${region.regionId == provinceId}">selected="selected"</c:if>>${region.regionName}</option>
                                </c:if>
                            </c:forEach>
                        </select>
                        <select name="city" id="city" style="display: none;">
                            <option value="-1" id="city_init">请选择</option>
                        </select>
                    </td>
                    <td>
                        <div class="tips"><b>*</b>必须选择省份和城市</div>
                    </td>
                </tr>
            </c:if>
            <tr>
                <c:choose>
                    <c:when test="${category != personal}">
                        <th>上传营业执照副本：</th>
                    </c:when>
                    <c:otherwise>
                        <th>上传身份证副本：</th>
                    </c:otherwise>
                </c:choose>
                <td>
                    <c:choose>
                        <c:when test="${fn:length(license) > 0}">
                            <img id="img_icon" height="150" width="150" src="${license}"/>
                        </c:when>
                        <c:otherwise>
                            <img id="img_icon" height="150" width="150" src="${URL_LIB}/static/theme/default/img/default.jpg"/>
                        </c:otherwise>
                    </c:choose>
                    <input id="input_icon" type="hidden" name="license" value="${license}">
                    <a id="upload_button" name="upload_button" class="submitbtn"></a>
                    <span id="loading" class="span_loading" style="display:none">
                        <img src="${URL_LIB}/static/theme/default/img/loading.gif"/></span>
                </td>
                <td valign="bottom">
                    <div class="tips"><b>*</b>扫描件或照片，建议使用扫描件，将增加认证通过几率,仅支持JPG、PNG，最多不超过2M</div>
                </td>
            </tr>
            </tbody>
        </table>
        <div class="send-newgame-btn"><a href="javascript:void(0)" class="submitbtn"><span>下一步</span></a></div>
    </form>

</div>

<!-- 页脚 -->
<%@ include file="/views/jsp/tiles/footer.jsp" %>

<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/developer-verify-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
<script type="text/javascript" src="${URL_LIB}/static/js/common/bdhm-noseajs.js"></script>
</body>
</html>