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
    <title>头像设置 ${jmh_title}</title>
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
            <h3>头像设置</h3>
        </div>
        <!--设置-设置头像-->
        <div class="set_avatar_text">
            <ul>
                <li>有头像才有面子。最多可设置8个头像，第一个图片会成为你在着迷显示的头像，拖动调整顺序。</li>
            </ul>
        </div>
        <div class="set_avatar">
            <form method="post" id="headIconForm" action="${ctx}/json/profile/headicon/save">
                <div class="set_avatar_show">
                    <ul id="sortableul">
                        <c:forEach var="icon" varStatus="status" items="${userSession.icons.iconList}">
                            <li name="delHeadicon"><a href="javascript:void(0)">
                                <img src="${icon:parseIcon(icon.icon, userSession.sex,"")}" width="90px" height="90px"/></a>
                                <%--<c:choose>--%>
                                <%--<c:when test='${headIcons.validStatus}'>--%>
                                <%--<img src="${uf:parseMFace(headIcons.headIcon)}" width="90px" height="90px"/></a>--%>
                                <%--</c:when>--%>
                                <%--<c:otherwise>--%>
                                <%--<img src="${URL_LIB}/static/theme/default/img/default.jpg" width="90px"--%>
                                <%--height="90px"/></a>--%>
                                <%--</c:otherwise>--%>
                                <%--</c:choose>--%>
                                <input type="hidden" value="${icon.icon}" name="headIcon"/>

                                <%--<div class="set_avatar_close"></div>--%>
                            </li>
                        </c:forEach>
                        <li id="szlistBtn" <c:if test="${fn:length(userSession.icons.iconList)>=8}">style="display:none"</c:if>>
                        <a class="set_avatar_upbut" href="javascript:void(0)" id="upButton">
                            <em id="spanButtonPlaceHolder"></em>
                        </a>
                        </li>
                    </ul>
                </div>
            </form>
            <div class="set_avatar_update set_avatar_update_line" style="display: none;" id="headIconSetDiv">
                <div class="set_avatar_update_l">
                    <div class="pic_change" id="previewDiv" style="width: 157px; height: 157px; overflow: hidden;">
                        <p>
                            <%--<img id="preview" src=""/>--%>
                        </p>
                    </div>
                    <p class="clearfix">
                        <a class="submitbtn" href="javascript:void(0)" id="thumbHref"><span>保存头像</span></a>
                        <a class="graybtn" href="javascript:void(0)" id="btnCancel"><span>取消</span></a>
                        <input type="hidden" value="" id="tumName"/>
                    </p>

                    <p id="headicon_error" class="tipstext txtips clearfix"></p>
                </div>
                <div class="tx_set">
                    <div class="set_avatar_update_r" id="previewSetDiv">
                        <span>
                            <%--<img id="face_img_id" src=""/>--%>
                        </span>
                    </div>
                </div>
            </div>
            <div class="set_avatar_text">
                <ul>
                    <li>最多设置8个头像，支持jpg、gif、png、bmp图片文件，且单个文件小于8M。</li>
                </ul>
            </div>
        </div>
        <!--设置-设置头像 end-->
    </div>
    <!--设置内容结束-->
</div>
<div style="display:none">
    <form onsubmit="return false;">
        <input type="hidden" id="x1" value="0"><br/>
        <input type="hidden" id="y1" value="0"><br/>
        <input type="hidden" id="x2" value="0"><br/>
        <input type="hidden" id="y2" value="0"><br/>
        <input type="hidden" id="oh" value="0"><br/>
        <input type="hidden" id="ow" value="0">
    </form>
</div>
<input type="hidden" value="${qnuptk:getToken()}" id="input_token"/>
<!--content结束-->
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script type="text/javascript" src="${URL_LIB}/static/third/qiniuupload/qiniu.js"></script>
<script type="text/javascript" src="${URL_LIB}/static/third/qiniuupload/plupload/plupload.full.min.js"></script>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/customize-headicon-init.js')
</script>
<%--<script src="${URL_LIB}/static/js/common/pv.js"></script>--%>
<%--<script type="text/javascript">--%>
<%--lz_main();--%>
<%--</script>--%>
</body>
</html>
