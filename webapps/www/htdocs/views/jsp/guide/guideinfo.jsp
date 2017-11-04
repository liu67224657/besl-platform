<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>完善个人信息 ${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <script src="${URL_LIB}/static/js/common/seajs.js"></script>
    <script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
</head>
<body>
<%@ include file="/views/jsp/tiles/header-only-icon.jsp" %>
<div class="content clearfix contentnoheader">
    <div class="reg_ok">
        <div class="reg_oktitle">
            <h3>注册成功！</h3>
            <h4>完善你的个人信息，使大家更好的认识你</h4>
        </div>
        <form action="${ctx}/guide/saveuserinfo" method="post" id="form1" target="_parent">
            <input type="hidden" value="${headIcons.headIcon}" name="headimg" id="headIconInput"/>

            <div class="reg_oklist">
                <dl>
                    <dt>设置头像</dt>
                    <dd><a class="regfl"><span id="spanButtonPlaceHolder">选一张你满意的照片</span></a>

                        <p>支持格式jpg、gif、png、bmp，且图片大小不超过8M</p></dd>
                    <div class="set_avatar_update" id="headIconDiv">
                        <div class="set_avatar_update_l">
                            <div>
                                <img src='${uf:parseFacesInclude(userSession.blogwebsite.headIconSet,userSession.userDetailinfo.sex, "m",true,0,1)[0]}'
                                     onError="this.src='${URL_LIB}/static/default/img/sz_tx.gif'" id="headIcon"
                                     width="200px" height="200px"/>
                            </div>
                        </div>
                    </div>

                    <div class="set_avatar_update set_avatar_update_line" style="display:none" id="headIconSetDiv">
                        <div class="set_avatar_update_l">
                            <div class="pic_change" id="previewDiv"
                                 style="width: 157px; height: 157px; overflow: hidden;">
                                <p></p>
                            </div>
                            <a class="submitbtn" href="javascript:void(0)" id="thumbHref"><span>保存头像</span></a>
                            <a class="graybtn" href="javascript:void(0)" id="btnCancel"><span>取消</span></a>
                            <input type="hidden" value="" id="tumName"/>
                        </div>
                        <div class="tx_set tx_setIE7">
                            <div class="set_avatar_update_r set_avatar_update_rIE7" id="previewSetDiv">
                                <span></span>
                            </div>
                        </div>
                    </div>
                </dl>
                <dl>
                    <dt>请选择性别</dt>
                    <dd>
                        <label>
                            <input type="radio" name="sex" id="sex_male"
                                   <c:if test="${mapMessage.detailinfo.sex == '1'}">checked="checked"</c:if> value="1">
                            男</label>
                        <label>
                            <input type="radio" name="sex" id="sex_female"
                                   <c:if test="${mapMessage.detailinfo.sex == '0'}">checked="checked"</c:if> value="0"/>
                            女</label>
                    </dd>
                </dl>
                <dl class="unline">
                    <dd>
                        <a class="regsave" href="javascript:void(0)">保存信息</a>
                        <a class="skip" href="${ctx}/guide/skip">以后再说</a>
                    </dd>
                </dl>
            </div>
        </form>
    </div>

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
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script>
    seajs.use("${URL_LIB}/static/js/init/register-ok-init.js");
</script>
<script type="text/javascript" src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main(26);
</script>
</body>
</html>