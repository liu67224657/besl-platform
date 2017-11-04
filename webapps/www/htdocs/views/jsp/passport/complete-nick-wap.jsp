<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<!doctype html>
<html>
<head>
    <meta charset="utf-8"/>
    <meta content="width=device-width, initial-scale=1.0, user-scalable=no" name="viewport"/>
    <meta content="yes" name="apple-mobile-web-app-capable"/>
    <meta content="black" name="apple-mobile-web-app-status-bar-style"/>
    <meta content="telephone=no" name="format-detection"/>
    <title>修改昵称</title>
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
    </script>
</head>
<style>
    * {
        -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
        margin: 0;
        padding: 0;
        word-break: break-all;
        word-wrap: break-word;
    }

    a {
        text-decoration: none;
        color: #000;
    }

    .userHead {
        padding-top: 50px;
        text-align: center;
        overflow: hidden;
    }

    .userHead span {
        display: inline-block;
        width: 90px;
        height: 90px;
        border: 5px solid #dcdcdc;
        border-radius: 100%;
        -webkit-border-radius: 100%;
        -moz-border-radius: 100%;
        overflow: hidden;
    }

    .userHead span img {
        width: 90px;
        height: 90px;
    }

    .inputName {
        position: relative;
        margin: 32px 35px 0;
        border: 1px solid #2492f8;
        border-radius: 20px;
        -webkit-border-radius: 20px;
        -moz-border-radius: 20px;
    }

    .inputName .after {
        position: absolute;
        right: 6px;
        top: 6px;
        width: 24px;
        height: 24px;
        background: url(images/inputName-bg.png) no-repeat 0 0;
        background-size: 24px 24px;
    }

    .inputName input[type='text'] {
        width: 100%;
        -webkit-box-sizing: border-box;
        -moz-box-sizing: border-box;
        box-sizing: border-box;
        -webkit-appearance: none;
        appearance: none;
        border: none;
        background: none;
        font-size: 14px;
        padding: 10px 10px 10px 0;
        text-indent: 15px;
        color: #313131;
        outline: none;
        overflow: hidden;
        white-space: nowarp;
    }

    .inputTips {
        text-align: center;
        font-size: 12px;
        color: #898989;
        padding: 15px 0 25px;
    }

    .btnbox {
        margin: 0 35px;
        overflow: hidden;
    }

    .btnbox a {
        display: block;
        padding: 10px 0;
        font-size: 16px;
        background: #2492f8;
        border-radius: 20px;
        -webkit-border-radius: 20px;
        -moz-border-radius: 20px;
        text-align: center;
        color: #fff;
    }

    .btnbox a:active {
        background: #0070d8
    }

    .btnbox a:last-child {
        background: #dcdcdc;
        color: #313131;
        margin-top: 12px;
    }

    .btnbox a:last-child:active {
        background: #c6c6c6
    }

    .boxy {
        width: 90%;
        position: fixed;
        left: 5%;
        top: 50%;
        background: rgba(0, 0, 0, 0.8);
        -webkit-transform: translateY(-50%) scale(0);
        -moz-transform: translateY(-50%) scale(0);
        transform: translateY(-50%) scale(0);
        -webkit-border-radius: 5px;
        -moz-border-radius: 10px;
        border-radius: 5px;
        overflow: hidden;
        z-index: 99;
    }

    .boxy.show {
        -webkit-transform: translateY(-50%) scale(1);
        -moz-transform: translateY(-50%) scale(1);
        transform: translateY(-50%) scale(1);
        transition: all 0.5s;
    }

    .boxy h2 {
        padding: 30px 20px;
        line-height: 24px;
        text-align: center;
        font-size: 16px;
        color: #fff;
        font-weight: normal;
    }

    .boxy div {
        text-align: center;
        overflow: hidden;
        font-size: 0;
        display: -moz-box;
        display: -webkit-box;
        display: box;
        -webkit-transform: translateY(0);
        -moz-transform: translateY(0);
        transform: translateY(0);
    }

    .boxy div span {
        display: block;
        padding: 10px 0;
        font-size: 14px;
        -moz-box-flex: 3;
        -webkit-box-flex: 3;
        box-flex: 3;
        color: #fff;
        border-top: 1px solid #6B6969;
    }

    .boxy div span:nth-last-child(odd) {
        border-left: 1px solid #6B6969;
    }

    .boxy div span:active {
        background: rgba(0, 0, 0, 1);
    }

    #uesrName {
        font-weight: normal;
        font-style: normal;
    }

    .checkout {
        padding: 10px 0;
        text-align: center;
        font-size: 12px;
        color: #ff5353;
        display: none;
    }
</style>
<body>
<div id="wrapper">
    <div class="userbox">
        <div class="userHead">
            <span><img src="${icon:parseIcon(icon, sex, "")}" alt="" title=""/></span>
        </div>
        <div class="inputName">
            <form action="http://passport.${DOMAIN}/auth/savenick" method="POST" id="form">
                <input type="text" id="nick" name="nick" value="${nick}" maxlength="32"/>
                <input type="hidden" name="uno" value="${uno}"/>
                <input type="hidden" name="sex" value="${sex}"/>
                <input type="hidden" name="icon" value="${icon}"/>
                <input type="hidden" name="appkey" value="${appkey}"/>
                <input type="hidden" name="reurl" value="${reurl}"/>
                <input type="hidden" name="logindomain" value="${logindomain}"/>

            </form>
            <cite class="after"></cite>
        </div>

        <div id="message" class="checkout" <c:if test="${!empty message}">style="display:block"</c:if>>
            <c:if test="${!empty message}"><fmt:message key="${message}" bundle="${userProps}"></fmt:message></c:if>
        </div>

        <div class="inputTips">注意：昵称一旦设定将不能修改</div>
        <div class="btnbox">
            <a id="confirm_nick" href="#">确认昵称</a>
            <%--<a href="${reurl}">暂时跳过</a>--%>
        </div>
    </div>
    <!--弹框-->
    <div class='boxy'>
        <h2>您的昵称将定为："<em id="uesrName">${nick}</em>"<br>今后将不能修改，您确定吗？</h2>

        <div>
            <span id="submit_form">确定</span>
            <span id="cancel_form">取消</span>
        </div>
    </div>
</div>
<script type="text/javascript">
    var Tools = {
        oValue: null,    //获取用户的名字
        int: function () {
            Tools.info();
            Tools.action();
        },
        //文本框
        info: function () {
            var obox = document.getElementsByClassName('inputName')[0];
            var ocite = document.getElementsByClassName('after')[0];
            var oInput = obox.getElementsByTagName('input')[0];
            //获得焦点touchstart
            ocite.addEventListener('touchstart', function (e) {
                e.preventDefault();
                e.stopPropagation();
                oInput.value = '';
                oInput.focus();
                this.style.display = 'none';
            }, false);
            //获取当前的value
            oInput.onchange = function () {
                Tools.oValue = this.value;
            };
            //失去焦点
            oInput.onblur = function () {
                this.value = Tools.oValue;               //用户的昵称
                ocite.style.display = 'block';
            };
        },
        //点击
        action: function () {
            var boxy = document.getElementsByClassName('boxy')[0];
            var confirmNick = document.getElementById('confirm_nick');
            var submit = document.getElementById('submit_form');
            var cancel = document.getElementById('cancel_form');
            var nickInput = document.getElementById('nick');
            var uesrName = document.getElementById('uesrName');
            confirmNick.addEventListener('touchstart', function () {
                var nick = nickInput.value;

                if (nick == null || nick.length == 0) {
                    document.getElementById('message').innerHTML = '用户昵称不能为空';
                    document.getElementById('message').style.display = 'block';
                    return false;
                }

                var reg = /^[\u4e00-\u9fa5a-zA-Z0-9]+$/gi;
                if (!reg.test(nick)) {
                    document.getElementById('message').innerHTML = '昵称不能含有特殊字符';
                    document.getElementById('message').style.display = 'block';
                    return false;
                }

                var oValLength = 0;
                nick.replace(/n*s*/, '') == '' ? oValLength = 0 : oValLength = nick.match(/[^ -~]/g) == null ? nick.length : nick.length + nick.match(/[^ -~]/g).length;
                if (oValLength < 4) {
                    document.getElementById('message').innerHTML = '您输入的昵称过短,2-10汉字或4-20字母';
                    document.getElementById('message').style.display = 'block';
                    return false;
                } else if (oValLength > 20) {
                    document.getElementById('message').innerHTML = '您输入的昵称过长,2-10汉字或4-20字母';
                    document.getElementById('message').style.display = 'block';
                    return false;
                }

                uesrName.innerHTML = nick;
                boxy.className = 'boxy show';

            }, false);

            submit.addEventListener('touchstart', function () {
                document.getElementById('form').submit();
            }, false);

            cancel.addEventListener('touchstart', function () {
                boxy.className = 'boxy';	//隐藏
            }, false);

        }
    }
    function strLen(str) {
        if (str == null || str.length == 0) {
            return 0;
        }
        var len = str.length;
        var reLen = 0;
        for (var i = 0; i < len; i++) {
            if (str.charCodeAt(i) < 27 || str.charCodeAt(i) > 126) {
                // 全角
                reLen += 1;
            } else {
                reLen += 0.5;
            }
        }
        return Math.ceil(reLen);
    }
    Tools.int();
</script>
</body>
</html>
