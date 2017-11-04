<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1"/>
    <title>账号安全</title>
    <link href="//cdn.bootcss.com/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
    <link href="//cdn.bootcss.com/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${URL_STATIC}/pc/userEncourageSys/css/screen.css">
    <link rel="stylesheet" type="text/css" href="${URL_STATIC}/pc/userEncourageSys/css/common.css">
    <link rel="stylesheet" type="text/css" href="${URL_STATIC}/pc/userEncourageSys/css/edit-select.css">
    <link rel="stylesheet" href="${URL_STATIC}/pc/userEncourageSys/css/setting.css">
    <link rel="stylesheet" href="${URL_STATIC}/pc/userEncourageSys/css/joymedialog.css">
    <link rel="stylesheet" href="${URL_STATIC}/pc/userEncourageSys/css/head-skin.css">
    <link rel="stylesheet" type="text/css" href="${URL_STATIC}/pc/userEncourageSys/css/userInfocc.css">
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);

    </script>

</head>
<body>
<%@ include file="user-center-header.jsp" %>
<!-- 内容区域 开始 -->
<div class="container">
    <div class="row">
        <div class="setting-con">
            <%@ include file="../customize/customize-general-left.jsp" %>
            <div class="col-md-9 pag-hor-20">
                <div class="setting-r">
                    <h3 class="setting-tit web-hide">账号安全</h3>

                    <div class="account-con">
                        <ul>
                            <c:if test="${phoneBind eq true}">
                                <li>
                                    <span>绑定手机：</span>

                                    <div>
                                        <span class="web-hide">绑定手机：</span>
                                        <span>${phone}</span>
                                        <span class="binded"><i class="fa fa-check-circle"></i>已绑定</span>
                                        <a href="/usercenter/account/modifyphonepage?step=1" class="fn-right">更改号码</a>
                                    </div>
                                </li>
                            </c:if>
                            <c:if test="${phoneBind eq false}">
                                <li>
                                    <span>绑定手机：</span>

                                    <div>
                                        <span class="web-hide">绑定手机：</span>
                                        <a href="/usercenter/account/bindphone" class="fn-right">绑定手机</a>
                                    </div>
                                </li>
                            </c:if>
                            <c:if test="${phoneBind}">
                                <li>
                                    <span>密码：</span>

                                    <div>
                                        <a href="/usercenter/account/modifypwd?profileid=${profile.profileId}">修改密码</a>
                                    </div>
                                </li>
                            </c:if>
                            <li>

                                <span>账号绑定： </span>

                                <div class="sina-con con-icon">
                                    <span><i class="sina"></i>新浪微博</span>
                                    <c:if test="${weiboBind eq true}">
                                        <span class="binded"><i class="fa fa-check-circle"></i>已绑定</span>
                                        <span class="jc-bind" data-third="sinaweibo">解除绑定</span>
                                    </c:if>
                                    <c:if test="${weiboBind eq false}">
                                        <span class="binding"><a
                                                href="http://passport.${DOMAIN}/auth/thirdapi/sinaweibo/bind?rl=true">现在绑定</a></span>

                                    </c:if>
                                </div>
                            </li>
                            <li>
                                <span></span>

                                <div class="qq-con con-icon">
                                    <span><i class="qq"></i>QQ</span>
                                    <c:if test="${qqBind eq true}">
                                        <span class="binded"><i class="fa fa-check-circle"></i>已绑定</span>
                                        <span class="jc-bind" data-third="qq">解除绑定</span>
                                    </c:if>
                                    <c:if test="${qqBind eq false}">
                                        <span class="binding"><a
                                                href="http://passport.${DOMAIN}/auth/thirdapi/qq/bind?rl=true">现在绑定</a></span>

                                    </c:if>
                                </div>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="user-center-footer.jsp" %>
<!-- 内容区域 结束 -->
<script type="text/javascript" src="http://static.joyme.com/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${URL_STATIC}/pc/userEncourageSys/js/openwindow.js"></script>
<script type="text/javascript" src="${URL_STATIC}/pc/userEncourageSys/js/bootstrap.js"></script>
<script src="${URL_LIB}/static/js/usercenter/userWindow.js"></script>
<script type="text/javascript" src="${URL_LIB}/static/js/usercenter/action.js"></script>

<script>
    $(document).ready(function () {
        var errorcode = "${errorcode}";
        if (errorcode != "" && errorcode != null) {
            mw.ugcwikiutil.ensureDialog(errorcode, function () {
                window.location.href = "${URL_UC}/usercenter/account/safe";
            });
        }


        var opt = new Object();
        opt.msg = "请再次确认是否解绑";
        $(".jc-bind").bind('click', function () {
            var isunbind = ${isunbind};
            if (isunbind < 2) {
                mw.ugcwikiutil.msgDialog("请至少保持一种登录方式");
                return;
            }

            var third = $(this).attr("data-third");
            mw.ugcwikiutil.confirmDialog(opt, function () {

                var ajax_url = "/servapi/auth/" + third + "/unbind";
                $.ajax({
                    url: ajax_url,
                    data: {uno: '${profile.uno}', profilekey: 'www'},
                    type: 'post',
                    dataType: "json",
                    success: function (data) {
                        if (data.rs == '1') {
                            mw.ugcwikiutil.ensureDialog("解绑成功", function () {
                                window.location.href = "${URL_UC}/usercenter/account/safe"
                            });
                        } else {
                            mw.ugcwikiutil.msgDialog("解绑失败");
                        }
                    }
                });
            });

        });
    });
</script>
</body>
</html>