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
    <meta name="Keywords" content="着迷,着迷网,joyme,joyme.com,游戏,游戏社区,好玩,攻略,最新游戏,最热游戏,游戏资讯,达人,高手,游戏经历,游戏成绩,美图,游戏原声,代言人">
    <meta name="description"
          content="着迷网（Joyme.com）是一个以游戏为主题的游戏玩家社区，记录你的游戏生活和情感 ，相遇结交志同道合的朋友，互动属于自己的游戏文化 ，有趣、新鲜的游戏话题，每天等你来讨论!,着迷,着迷网,joyme,joyme.com,游戏,游戏社区,好玩,攻略,最新游戏,最热游戏,游戏资讯,达人,高手,游戏经历,游戏成绩,美图,游戏原声,代言人"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <title>基本信息 着迷网Joyme.com</title>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/core.css?"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/global.css?"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/style.css?"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/common.css?"/>
</head>
<body>
<!--头部开始-->
<c:import url="/views/jsp/passport/header.jsp"/>

<!--content结束-->
<div class="content set_wrapper clearfix">
    <div id="set_right">
        <div class="set_title">
            <h3>基本信息</h3>
        </div>
        <!--修改昵称-->
        <div class="set_userName">
            <div class="set_userName_head">
                <div class="set_avatar_show">
                    <ul id="sortableul" class="ui-sortable">
                        <li name="delHeadicon"><a href="javascript:void(0)">
                            <img src="${icon:parseIcon(icon, sex, "")}" width="90px" height="90px">
                        </a>
                        </li>
                    </ul>
                </div>
            </div>
            <dl class="set_userName_box">
                <dt>昵称：</dt>
                <dd>
                    <form action="http://passport.${DOMAIN}/auth/savenick" method="POST" id="form">
                        <p style="height:29px">
                        <span>
                            <input type="text" id="nick" name="nick" value="${nick}" class="settext" maxlength="32"/>
                            <input type="hidden" name="uno" value="${uno}"/>
                            <input type="hidden" name="sex" value="${sex}"/>
                            <input type="hidden" name="icon" value="${icon}"/>
                            <input type="hidden" name="appkey" value="${appkey}"/>
                            <input type="hidden" name="reurl" value="${reurl}"/>
                            <input type="hidden" name="logindomain" value="${logindomain}"/>

                        </span>
                        <em id="message" class="set_userName_errmsg"
                            <c:if test="${!empty message}">style="display:inline"</c:if>>
                        <c:if test="${!empty message}"><fmt:message key="${message}"
                                                                        bundle="${userProps}"></fmt:message></c:if>
                        </em>
                    </p>
                    </form>
                    <p class="set_userName_tips"><em>*</em>注意：昵称一旦设定将不能修改</p>

                    <p class="set_userName_btn">
                        <a href="javascript:void(0);" class="submitbtn" id="savenick"><span>确定昵称</span></a>
                        <%--<a href="${reurl}" class="graybtn" id="skip_link"><span>暂时跳过</span></a>--%>
                    </p>
                </dd>
            </dl>
        </div>
        <!--修改昵称-->
    </div>
    <!--设置内容结束-->
</div>
<!--content结束-->
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/complete-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
</body>
</html>
