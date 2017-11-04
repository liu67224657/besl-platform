<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>注册 ${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <style>
        .login-form{ overflow:hidden; text-align:left; }
        .login-form p label,.login-form p font{ float:left; line-height:32px; }
        .login-form p label{ width:100px; text-align:right; }
        .login-form p td{ font-size:12px; line-height:32px; }
        .login-form input{ width:200px; height: 16px; padding:8px 10px; border: 1px solid #ddd;}
        .login-form input.inp-checkbox{ width:20px; height:14px; vertical-align:middle; cursor:pointer;  }
        .login-form select{ height:32px; font-size:12px; border: 1px solid #ddd; margin-right:5px; cursor:pointer; }
        .login-form div{ padding:0 0 0 10px; color:#f00; text-align:left; }
        .login-form div.Validform_right{ color:#6d9b19 }
        .login-form td{ text-align:left; padding:10px 0; position:relative; }
        .Validform_checktip,.d-tips{ position:absolute; left:225px; top:17px; line-height:20px;height:20px;overflow:hidden;color:#999;font-size:12px;}
        .need{ color:#f00; }
        .d-tips{ top:5px; }
        .Validform_right{color:#71b83d;padding-left:20px;}
        .Validform_wrong{color:red;padding-left:20px;white-space:nowrap;}
        .Validform_error{background-color:#ffe7e7;}
        #Validform_msg{color:#7d8289; width:280px;background:#fff;position:absolute;top:0px;right:50px;z-index:99999; display:none;filter:progid:DXImageTransform.Microsoft.Shadow(Strength=3, Direction=135, Color='#000');-webkit-box-shadow:2px 2px 3px #aaa; -moz-box-shadow:2px 2px 3px #aaa;}
        #Validform_msg .iframe{position:absolute;left:0px;top:-1px;z-index:-1;}
        #Validform_msg .Validform_title{line-height:25px;height:25px;text-align:left;font-weight:bold;padding:0 8px;color:#fff;position:relative;background-color:#000;}
        #Validform_msg a.Validform_close:link,#Validform_msg a.Validform_close:visited{line-height:22px;position:absolute;right:8px;top:0px;color:#fff;text-decoration:none;}
        #Validform_msg a.Validform_close:hover{color:#cc0;}
        #Validform_msg .Validform_info{padding:8px;border:1px solid #000;border-top:none;text-align:left;}
        .login-form input[type='submit']{ height:35px; background: #9B9EA0; color:#fff; border:none; cursor:pointer; }
        #fw-info{ display:inline; line-height:35px; vertical-align:0; background:none; padding:0; margin:0;}
        .pay-popup{ display:none; width:350px; line-height:40px; background:rgba(0,0,0,0.6); *background:#000; *opacity:0.5; position:fixed; top:50%; left:50%; margin:-25px 0 0 -125px; font-family:"微软雅黑"; -webkit-border-radius:3px; -moz-border-radius:3px; border-radius:3px; z-index:999; }
        .pay-popup h2{ text-align:center; font-weight:normal; font-size:18px; color:#fff; }
    </style>
</head>
<body>
<!--头部开始-->
<c:import url="/views/jsp/passport/header.jsp"/>
<!--头部结束-->
<div class="content clearfix">
    <div class="reg-left">
        <h2>注册着迷</h2>

        <div class="login-form">
            <form class="registerform">
                <table width="100%" style="table-layout:fixed;">
                    <tr>
                        <td class="need" style="width:10px;">*</td>
                        <td style="width:70px;">用户名：</td>
                        <td style="width:550px;"><input type="text" value="" name="name" class="inputxt" nullmsg="请输入用户名！" /><div class="Validform_checktip"></div></td>
                    </tr>
                    <tr>
                        <td class="need">*</td>
                        <td>密码：</td>
                        <td><input type="password" value="" name="userpassword" class="inputxt" nullmsg="请设置密码！" errormsg="密码范围在6~16位之间,不能使用空格！" /><div class="Validform_checktip"></div></td>
                    </tr>
                    <tr>
                        <td class="need">*</td>
                        <td>确认密码：</td>
                        <td><input type="password" value="" name="userpassword2" class="inputxt" recheck="userpassword" nullmsg="请再输入一次密码！" errormsg="您两次输入的账号密码不一致！" /><div class="Validform_checktip"></div></td>
                    </tr>
                    <tr>
                        <td class="need">*</td>
                        <td>真实姓名：</td>
                        <td><input type="text" value="" name="name" class="inputxt" /><div class="Validform_checktip"></div></td>
                    </tr>
                    <tr>
                        <td class="need">*</td>
                        <td>身份证：</td>
                        <td><input type="text" value="" name="card" class="inputxt" id="sfz" errormsg="请输入身份证号有误！" /><div class="Validform_checktip"></div></td>
                    </tr>
                    <tr>
                        <td class="need"></td>
                        <td>出生年月：</td>
                        <td><cite class="year">1997</cite>年<cite class="month">01</cite>月<cite class="day">01</cite>日<div class="d-tips"></div></td>
                    </tr>
                    <tr>
                        <td class="need"></td>
                        <td></td>
                        <td><input type="checkbox"  value="" name="" class="inputxt inp-checkbox" errormsg="您尚未同意着迷注册服务协议" /><div class="Validform_checktip"></div>我已同意<a id="fw-info" href="http://www.joyme.com/help/law">《着迷注册服务协议》</a></td>

                    </tr>
                    <tr>
                        <td class="need"></td>
                        <td></td>
                        <td colspan="2" style="padding:10px 0 18px 0;">
                            <!-- 注意：三个全角空格 -->
                            <input id="submit" type="submit" value="注册" disabled="disabled"/>　　　注册功能正在维护中，请先使用QQ或微博登录
                        </td>
                    </tr>
                </table>
            </form>

        </div>

        <div>
            <a href="http://passport.${DOMAIN}/auth/thirdapi/qq/bind?reurl=${reurl}" class="reg-sns-qq">QQ帐号登录</a>
            <a href="http://passport.${DOMAIN}/auth/thirdapi/sinaweibo/bind?rurl=${reurl}"
               class="reg-sns-weibo">新浪微博登录</a></div>

    </div>
    <div class="reg-right">
        <h2>登录着迷</h2>

        <p>如果你是老用户，请点击&nbsp;<a href="http://passport.${DOMAIN}/auth/loginpage?reurl=${reurl}">这里登录</a></p>
    </div>
</div>
<%@ include file="/views/jsp/tiles/footer.jsp" %>

<div class="scroll_top">
    <a class="home_gotop" href="javascript:void(0)" id="linkHome" title="返回" style="display:none"></a>
</div>
<div class="pay-popup" style="display: none;">
    <h2>对不起，着迷注册服务正在维护中~</h2>
</div>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/register-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>
<script src="${URL_LIB}/static/js/login/validate.js"></script>
<script type="text/javascript">
    lz_main();
    $(function(){
        var t=false;
        var s=false;
        var current=function(){
            var sfz=$('#sfz');
            var val=null;
            var y,m,d;
            var d=new Date();
            var cy=d.getFullYear();
            var cm=d.getMonth()+1;
            var cd=d.getDate();
            var sfzFn=function(){
                val=sfz.val();
                y= val.substring(6,10);//年
                m= val.substring(10,12);
                d= val.substring(12,14);
                $('.year').html(y);
                $('.month').html(m);
                $('.day').html(d);
                if((Number(cd)-Number(d))<0){
                    cd -= 1;
                }
                if((Number(cm)-Number(m))<0){
                    cy -= 1;
                }
                if((Number(cy)-Number(y))>=18&&$('.year').html()!==''&&$('.month').html()!==''&&$('.day').html()!==''){
                    $('.d-tips').addClass('Validform_right').text("通过验证");
                    t=true;
                }else{
                    if($('.year').html()==''&&$('.month').html()==''&&$('.day').html()==''){
                        $('.year').html('1997');
                        $('.month').html('01');
                        $('.day').html('01');
                        //$('.d-tips').show().removeClass('Validform_right').addClass('Validform_wrong').text("信息错误");
                    }else{
                        //$('.d-tips').hide();
                        $('.d-tips').show().removeClass('Validform_right').addClass('Validform_wrong').text("未满18岁");
                        $('.pay-popup').show().find('h2').text("对不起，年龄未满18岁不可以进行注册");
                        var ts=setTimeout(function(){
                            //$('.d-tips').show();
                            $('.pay-popup').hide();
                        },3000);
                    }
                    t=false;
                }
                s=true;
            }
            sfzFn();
            sfz.blur(sfzFn);
        };
        current();
        var demo=$(".registerform").Validform({
            tiptype:3,
            showAllError:true,
            datatype:{
                "mz":/^[A-Za-z0-9\u4E00-\u9FA5\uf900-\ufa2d]+$/,
                "zh1-6":/^[\u4E00-\u9FA5\uf900-\ufa2d]{1,5}$/,
                "sfz":/^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{4}$/
            },
            beforeSubmit:function(curform){
                current();
                if(t){
                    $('.pay-popup').show().find('h2').html("对不起，着迷注册服务正在维护中...<br />请先使用QQ或微博登录");
                    var ts=setTimeout(function(){
                        $('.pay-popup').hide();
                    },3000);
                }
                return false;
            }
        });
        //demo.Tipmsg.tit="请输入1到6个中文字符！";
        demo.addRule([{
            ele:".inputxt:eq(0)",
            datatype:"mz"
        },
            {
                ele:".inputxt:eq(1)",
                datatype:"*6-16"
            },
            {
                ele:".inputxt:eq(2)",
                datatype:"*6-16",
                recheck:"userpassword"
            },
            {
                ele:".inputxt:eq(3)",
                datatype:"zh1-5"
            },
            {
                ele:".inputxt:eq(4)",
                datatype:"sfz"
            },
            {
                ele:"select",
                datatype:"*"
            },
            {
                ele:":radio:first",
                datatype:"*"
            },
            {
                ele:":checkbox",
                datatype:"*"
            }]);

    });
</script>
</body>
</html>