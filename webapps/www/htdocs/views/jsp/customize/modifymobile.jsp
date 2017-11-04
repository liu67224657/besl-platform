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
    <link rel="icon" href="http://lib.joyme.com/static/img/favicon.ico" type="image/x-icon"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/global.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/style.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/common.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/core.css?${version}"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <title>绑定手机 着迷网Joyme.com</title>
</head>
<body>
<!--头部开始-->
<c:import url="/views/jsp/passport/header.jsp"/>
<!--设置导航-->
<div class="content set_content clearfix">
    <%@ include file="leftmenu.jsp" %>

    <!--设置导航结束-->
    <!--设置内容-->
    <div id="set_right">
        <div class="set_title">
            <h3>修改手机绑定</h3>
        </div>
        <!--设置-绑定手机-->
        <div class="bind-mobile">
            <h4>输入新的手机号码</h4>

            <form action="/profile/mobile/modify" method="post" id="verifyform">
            <input type="hidden" name="vcode" id="vcode">
                <input type="hidden" name="type" value="${type}"/>
                <input type="hidden" name="reurl" value=""/>
                <table width="100%">
                    <tr>
                        <th valign="top"><b>*</b> 手机号码：</th>
                        <td>
                            <div><input type="text" name="phone" style="width:270px" id="phone" value="${phone}">
                                <c:if test="${fn:length(phoneError)>0}">
                                    <span class="error" id="phoneError">&nbsp;请输入正确的手机号码</span>
                                </c:if>
                            </div>
                            <div>
                                <c:choose>
                                <c:when test="${intravel!=null}">
                                <div><input type="button" id="send" value="发送验证码(${intravel})" disabled/><span
                                        id="send_info"><strong>√</strong> 发送成功，请查看手机</span>
                                    </c:when>
                                    <c:otherwise>
                                        <div><input type="button" id="send" value="发送验证码"/></div>
                                    </c:otherwise>
                                    </c:choose>


                                </div>
                            </div>
                            <div>最多可发送5次验证码</div>
                        </td>
                    </tr>
                    <tr>
                        <th valign="top"><b>*</b> 验证码：</th>
                        <td>
                            <div><input type="text" name="verifycode" id="verifycode" style="width:86px;"/></div>
                            <c:if test="${fn:length(codeError)>0}">
                                <div class="error"> <fmt:message key="${codeError}" bundle="${userProps}"/></div>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <th>&nbsp;</th>
                        <td><br/><a href="javascript:void(0)" class="submitbtn"
                                    id="modifyformsubmit"><span>确定</span></a>&nbsp;&nbsp;
                            <a href="javascript:void(0)" class="submitbtn" id="canclebtn"><span>取消</span></a>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <!--设置-绑定手机 end-->
    </div>
    <!--设置内容结束-->
</div>
<!--content结束-->
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/customize-mobile-init.js')
</script>
<%--<script src="${URL_LIB}/static/js/common/pv.js"></script>--%>
<%--<script type="text/javascript">--%>
    <%--lz_main();--%>
<%--</script>--%>
</body>
</html>
