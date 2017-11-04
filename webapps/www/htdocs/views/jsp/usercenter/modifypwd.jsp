<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1"/>
    <title>修改密码</title>
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
                    <h3 class="setting-tit web-hide">修改密码</h3>

                    <div class="change-password-con">
                        <div>
                            <span>旧密码：</span>
                            <input type="password" name="oldpwd" id="oldpwd">
                        </div>
                        <div>
                            <span>新密码：</span>
                            <input type="password" name="newPwd" id="newPwd">
                        </div>
                        <div>
                            <span>再次输入：</span>
                            <input type="password" name="reNewPwd" id="reNewPwd">
                        </div>
                        <div class="btn-con">
                            <button class="btn-sure" id="savePwd">提交</button>
                            <a href="/usercenter/account/safe" class="btn-cancle">取消</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="user-center-footer.jsp" %>
<!-- 内容区域 结束 -->
<script type="text/javascript" src="http://static.joyme.com/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${URL_STATIC}/pc/userEncourageSys/js/bootstrap.js"></script>
<script src="${URL_LIB}/static/js/usercenter/userWindow.js"></script>
<script type="text/javascript" src="${URL_LIB}/static/js/usercenter/action.js"></script>
<script type="text/javascript" src="${URL_STATIC}/pc/userEncourageSys/js/openwindow.js"></script>
<script type="text/javascript">

    $(document).ready(function () {
        $("#savePwd").bind('click', function () {
            var oldpwd = $.trim($('#oldpwd').val());
            if (oldpwd == "") {
                mw.ugcwikiutil.msgDialog("旧密码不能为空");
                return false;
            } else if (oldpwd.length < 6) {
                mw.ugcwikiutil.msgDialog("旧密码长度不能小于6位");
                return false;
            }

            var newPwd = $.trim($('#newPwd').val());
            if (newPwd == "") {
                mw.ugcwikiutil.msgDialog("新密码不能为空");
                return false;
            } else if (newPwd.length < 6) {
                mw.ugcwikiutil.msgDialog("新密码长度不能小于6位");
                return false;
            }


            var reNewPwd = $.trim($('#reNewPwd').val());
            if (reNewPwd == "") {
                mw.ugcwikiutil.msgDialog("确认密码不能为空");
                return false;
            } else if (reNewPwd.length < 6) {
                mw.ugcwikiutil.msgDialog("确认密码长度不能小于6位");
                return false;
            }

            if ($('#newPwd').val() != $('#reNewPwd').val()) {
                mw.ugcwikiutil.msgDialog("两次输入的密码不一致");
                return false;
            }

            if (oldpwd == newPwd) {
                mw.ugcwikiutil.msgDialog("新密码不能和旧密码一样");
                return false;
            }



            $.ajax({
                url: '/servapi/auth/modify/password',
                data: {profileid: '${profile.profileId}', logindomain: '${logindomain.code}',pwd:newPwd,oldpwd:oldpwd},
                type: 'post',
                dataType: "json",
                success: function (data) {
                   if(data.rs=='-10116'){
                       mw.ugcwikiutil.msgDialog("旧密码不正确");
                   }else if(data.rs=='-10106'){
                       mw.ugcwikiutil.msgDialog(data.msg);
                   }else if(data.rs<0){
                       mw.ugcwikiutil.msgDialog("修改失败");
                   }else{
                       mw.ugcwikiutil.ensureDialog("修改成功",function(){
                           window.location.href="${URL_UC}/usercenter/account/safe";
                       });
                   }
                }
            });
        })

    });


</script>
</body>
</html>