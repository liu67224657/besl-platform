<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1x.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/jstllibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <title>任务</title>
    <link href="${URL_LIB}/static/theme/wap/css/activity/wap_common20150304.css" rel="stylesheet" type="text/css">
    <link href="${URL_LIB}/static/theme/wap/css/activity/wap_styles20140304.css" rel="stylesheet" type="text/css">
    <%--<link href="${URL_LIB}/static/theme/wap/css/wap_common.css?${version}" rel="stylesheet" type="text/css">--%>

    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.11.0.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            window.addEventListener('DOMContentLoaded', function () {
                document.addEventListener('touchstart', function () {
                    return false
                }, true)
            }, true);
            <%--var error = '${error}';--%>
            <%--if (error != '') {--%>
            <%--alert(error);--%>
            <%--window.history.back();--%>
            <%--}--%>
            $('.popup_box').on('touchstart', function () {
                $('.mark_box').hide();
                $('.popup_box').hide();
            });
            $("#sharegetcode").click(function() {
                var clientid = $("[name='clientid']").val();
                var taskid = $("[name='taskid']").val();
                var logindomain = $("[name='logindomain']").val();
                var platform = $("[name='platform']").val();

                if (clientid == '' || clientid == null || logindomain == '' || logindomain == 'client') {
                    $("#reserveSuccess").addClass("close");
                    $("#reserveSuccess").text("您还没有登录，请先登录");
                    var t = setInterval(function () {
                        $("#reserveSuccess").removeClass("close");
                        clearTimeout(t);
                    }, 3000);
                    showLoginWindow();
                    return false;
                }
                $.post("/joymeapp/gameclient/json/task/shareaward", {clientid:clientid,taskid:taskid,platform:platform}, function(req) {
                    var resMsg = eval('(' + req + ')');
                    if (resMsg.rs == '1') {
                        $("#sharecode").submit();
                    } else if (resMsg.rs == '-1') {
                        $("#dialog").find("h2").html("您还没有领取新手奖励呢！")
                        $("#dialog").addClass("show");
                    } else {
                        $("#dialog2").addClass("show");
                    }
                });
            });
        });
        function cannelButton() {
            $("#dialog").removeClass("show");
            $("#dialog2").removeClass("show");
        }
        function showShareButtion() {
            $('.mark_box').show();
            $('.popup_box').show();
            $("#dialog").removeClass("show");
            $("#dialog2").removeClass("show");
        }
    </script>
<body>
<div id="wrapper">
    <div class="wap-center">
        <header>
            <div>
                <p><c:choose>
                    <c:when test="${type==1}">
                        欢迎你的到来！
                    </c:when>
                    <c:otherwise>
                        感谢你的分享
                    </c:otherwise>
                </c:choose></p>

                <p>恭喜您获得1Q币奖励！</p>
            </div>
        </header>
        <h2>兑换码为</h2>

        <div class="form-bar">
            <dl>
                <dt>卡号：</dt>
                <dd>${goodsItem.snValue1}</dd>
            </dl>
            <dl>
                <dt>密码：</dt>
                <dd>${goodsItem.snValue2}</dd>
            </dl>
            <p class="form-tips">建议截屏本页保存</p>
        </div>
        <div class="info-box">
            <h3><span>兑换Q币方式:</span></h3>

            <p>1. 访问骏网：<a href="http://www.jcard.cn/">http://www.jcard.cn/ </a>或下载骏网APP；</p>

            <p>2. 选择“Q币充值”，选择“1元1个”；</p>

            <p>3. 输入自己的QQ号，将以上卡号密码输入“骏卡卡号”
                “骏卡密码”，即可充值。</p>
        </div>
        <c:choose>
            <c:when test="${type==1}">
                <div class="btn">
                    <form id='sharecode' action='/joymeapp/gameclient/webview/task/taskgetcode?type=2' method='post'>

                        <input type="hidden" name="clientid" value="${clientId}"/>
                        <input type="hidden" name="taskid" value="${taskId}"/>
                        <input type="hidden" name="platform" value="${platform}"/>
                        <input type="hidden" name="uno" value="${uno}"/>
                        <input type="hidden" name="appkey" value="${appkey}"/>
                        <input type="hidden" name="uid" value="${uid}"/>
                        <a href="javascript:void(0);" id="sharegetcode" class="button">分享给其他好友再得（1Q币）</a>
                    </form>
                </div>
                <div class="info-tips">注：每台设备只能获取新人奖励和分享奖励各一次</div>
            </c:when>
            <c:otherwise>
                <div class="info-tips info-share">注：每台设备只能获取新人奖励和分享奖励各一次</div>
            </c:otherwise>
        </c:choose>


    </div>
    <div id="_sharebtn_status" style="display: none;">yes</div>
    <div id="_title" style="display: none;">我下载着迷玩霸，领取了1Q币！你也来试试看吧！</div>
    <div id="_desc" style="display: none;">下着迷玩霸，立即领Q币！</div>
    <div id="_clientpic" style="display: none;">http://lib.joyme.com/static/theme/wap/images/syhbcon.png</div>
    <div id="_share_task" style="display: none;">qbshare</div>
    <div id="_share_url" style="display: none;">${URL_WWW}/joymeapp/gameclient/webview/task/share</div>
    <footer><span>本活动最终解释权归属着迷网所有</span></footer>
    <div class="wap-bg">
        <span><img src="${URL_LIB}/static/theme/wap/images/activity/qbg-1.jpg" alt="" title=""/></span>
        <span><img src="${URL_LIB}/static/theme/wap/images/activity/qbg-2.jpg" alt="" title=""/></span>
        <span><img src="${URL_LIB}/static/theme/wap/images/activity/qbg-3.jpg" alt="" title=""/></span>
        <span><img src="${URL_LIB}/static/theme/wap/images/activity/qbg-4.jpg" alt="" title=""/></span>
        <span><img src="${URL_LIB}/static/theme/wap/images/activity/qbg-5.jpg" alt="" title=""/></span>
        <span><img src="${URL_LIB}/static/theme/wap/images/activity/qbg-6.jpg" alt="" title=""/></span>
        <span><img src="${URL_LIB}/static/theme/wap/images/activity/qbg-7.jpg" alt="" title=""/></span>
        <span><img src="${URL_LIB}/static/theme/wap/images/activity/qbg-8.jpg" alt="" title=""/></span>
        <span><img src="${URL_LIB}/static/theme/wap/images/activity/qbg-9.jpg" alt="" title=""/></span>
        <span><img src="${URL_LIB}/static/theme/wap/images/activity/qbg-10.jpg" alt="" title=""/></span>
        <span><img src="${URL_LIB}/static/theme/wap/images/activity/qbg-11.jpg" alt="" title=""/></span>
        <span><img src="${URL_LIB}/static/theme/wap/images/activity/qbg-12.jpg" alt="" title=""/></span>
        <span><img src="${URL_LIB}/static/theme/wap/images/activity/qbg-13.jpg" alt="" title=""/></span>
    </div>
    <div class="dialog" id="dialog">
        <h2></h2>

        <div>
            <a href="javascript:cannelButton();" id="close">朕知道了</a>
        </div>
    </div>
    <div class="dialog" id="dialog2">
        <h2>您还没分享过本次活动呢！</h2>

        <div>
            <a href="javascript:showShareButtion()">马上分享</a>
            <a href="javascript:cannelButton();">一会再说</a>
        </div>
    </div>
    <div class="popup_box" style="display:none;">
        <img src="${URL_LIB}/static/theme/wap/images/share.png" alt="">
    </div>
    <div class="mark_box" id="mark_box" style="display:none;"></div>
</div>
</body>
</html>