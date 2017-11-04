<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/youkutaglibs.jsp" %>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">

    <!--手机style-->
    <link rel="stylesheet" media="screen and (max-width:768px)" href="${URL_LIB}/static/theme/youku/css/club.css">
    <!--平板style-->
    <link rel="stylesheet" media="screen and (min-width:768px)" href="${URL_LIB}/static/theme/youku/css/ipad_common.css">
    <link rel="stylesheet" media="screen and (min-width:768px)" href="${URL_LIB}/static/theme/youku/css/ipad_club.css">

    <link rel="stylesheet" href="${URL_LIB}/static/theme/youku/css/common.css" type="text/css">
    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.11.0.min.js"></script>
    <title>签到</title>
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false;
            }, true);
        }, true);
    </script>
    <script type="text/javascript" src="http://static.joyme.com/app/youku/js/youkubtn.js"></script>
    <c:set var="url"
           value="/youku/webview/task/signpage?appkey=${appkey}&platform=0"/>
    <script type="text/javascript">
        var userAgentInfo = navigator.userAgent.toLowerCase();
        function getCookie(name) {
            var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
            if(arr=document.cookie.match(reg))
                return unescape(arr[2]);
            else
                return null;
        }
        <c:choose>
        <c:when test="${!signcomplete}">
        $(document).ready(function () {
            initNotSignComplete();
            touchSign();

            var reqUid = '${profile == null ? -1 : profile.uid}';
            var uid = -1;
            var number=0;
            var cookieInterval = setInterval(function () {
                number++;
                var cookieUid = getCookie("jmuc_u");
                if(cookieUid != null){
                    uid = cookieUid;
                }
                if(parseInt(reqUid) != uid || number >= 10){
                    clearInterval(cookieInterval);
                    if(parseInt(reqUid) != uid){
                        window.location.href = window.location.href;
                    }
                }
            }, 1000);
        });

        function initNotSignComplete() {
            window.className = 'coral';
            var processFlagInt = -100;
            try {
                var processFlag = '${tasklist[0].taskid}';
                processFlag = processFlag.charAt(processFlag.length - 1);
                processFlagInt = parseInt(processFlag);
                if (isNaN(processFlagInt) || !isFinite(processFlagInt)) {
                    processFlagInt = -100;
                }
            } catch (e) {
                console.error(e);
            }

            if (processFlagInt == -100) {
                window.className = 'coral';
            } else if (processFlagInt == 6) {
                window.className = 'coral';
            } else {
                window.className = 'coral' + (processFlagInt + 1);
            }

            $('#processBar').removeClass('coral');
            $("#processBar").addClass(window.className);
            $(".kudou").find("span").html('${tasklist[1].taskAward.value}');
        }

        function touchSign() {
            $('.top-shell').on('touchstart', function (e) {
                if (userAgentInfo.indexOf('youku hd') == -1 && userAgentInfo.indexOf('youku') == -1) {
                    alert("请先登录");
                    return false;
                }
                <c:choose>
                <c:when test="${empty profile}">
                return;
                </c:when>
                <c:otherwise>
                var appkey = '${appkey}';
                var clientid = '${clientid}';
                var platform = '0';
                var uid = jsconfig.uid;
                window.queryData = {platform: platform};
                window.resultData = undefined;
                if (clientid != null && clientid.length > 0) {
                    window.queryData.clientid = clientid;
                }

                $.ajax({
                    url: jsconfig.YK_DOMAIN + "/youku/api/task/sign",
                    data: queryData,
                    type: "POST",
                    dataType: "json",
                    success: function (data, textStatus) {
                        if (data.rs == -1001) {
                            alert("签到失败，有空参数!");
                        } else if (data.rs == -10104) {
                            alert("签到失败，用户不存在!");
                        } else if (data.rs == -70000) {
                            alert("签到失败，任务不存在");
                        } else if (data.rs == -70001) {
                            alert("签到失败，任务已完成");
                        } else {
                            var nextClassName = 'coral';

                            var processFlagInt = -100;
                            var currentClass = $('#processBar').attr("class");
                            currentClass = currentClass.replace(/bottom-box\s*/g, '');
                            if (currentClass == '' || currentClass.length == 0) {
                                nextClassName = 'coral1';
                            } else if (currentClass == 'coral') {
                                nextClassName = 'coral1';
                            } else {
                                currentClass = currentClass.charAt(currentClass.length - 1);
                                processFlagInt = parseInt(currentClass);
                                if (isNaN(processFlagInt) || !isFinite(processFlagInt) || processFlagInt >= 7) {
                                    console.log("processFlagInt" + processFlagInt);
                                    processFlagInt = -100;
                                }

                                if (processFlagInt == -100) {
                                    nextClassName = 'coral1';
                                } else {
                                    nextClassName = 'coral' + (processFlagInt + 1);
                                }
                            }

                            $('#processBar').attr("class", "bottom-box " + nextClassName);
                            $('.top-hand,.top-title').css('visibility', 'hidden');
                            $(".top-shell").addClass('open-shell');
                            $('.kudou-box').addClass('show');

                            $('.top-shell').off('touchstart');
                        }

                    }
                });

                if (e && e.preventDefault) {
                    e.preventDefault();
                } else {
                    window.event.returnValue = false;
                }
                return false;
                </c:otherwise>
                </c:choose>
            });
        }
        </c:when>
        <c:otherwise>
        $(document).ready(function () {
            initSignComplete();

            touchSign();



        });



        function initSignComplete() {
            window.nextClassName = 'coral';
            var siprocessFlagInt = -100;
            try {
                var processFlag = '${tasklist[1].taskid}';
                processFlag = processFlag.charAt(processFlag.length - 1);
                processFlagInt = parseInt(processFlag);
                if (isNaN(processFlagInt) || !isFinite(processFlagInt)) {
                    processFlagInt = -100;
                }
            } catch (e) {
            }

            if (processFlagInt == -100) {
                window.nextClassName = 'coral';
            } else {
                window.nextClassName = 'coral' + (processFlagInt + 1);
            }

            $('#processBar').removeClass('coral');
            $("#processBar").addClass(window.nextClassName);
        }

        function touchSign() {
            $('.top-shell').on('touchstart', function (e) {
                if (userAgentInfo.indexOf('youku hd') == -1 && userAgentInfo.indexOf('youku') == -1) {
                    alert("请先登录");
                    return false;
                }
                if (e && e.preventDefault) {
                    e.preventDefault();
                } else {
                    window.event.returnValue = false;
                }
                return false;
            });
        }

        </c:otherwise>
        </c:choose>
    </script>
<body>
<div id="ck">

</div>
<div id="wrapper">
    <div class="wrapper loads">
        <div class="top-box">
            <!--云-->
            <span class="yun yun1"></span>
            <span class="yun yun2"></span>
            <span class="yun yun3"></span>
            <span class="sun"></span>
            <!--点我签到-->
            <div class="top-title">
                <c:if test="${!signcomplete}"><span></span></c:if>
            </div>
            <!--贝壳-->
            <a href="<c:choose><c:when test="${isLogin}">javascript:void(0);</c:when><c:otherwise>${ykt:ykLogin(pageContext.request,url,'')}</c:otherwise></c:choose>"
            class="top-shell <c:if test="${signcomplete}">open-shell1</c:if>">
            <c:choose>
                <c:when test="${signcomplete}">
                    <div class="yiqian"><img src="${URL_LIB}/static/theme/youku/images/yiqian.png" alt=""></div>
                </c:when>
                <c:otherwise>
                    <div class="top-hand"></div>
                    <div class="kudou-box">
                        <div class="guang"></div>
                        <div class="kudou"><span>300</span></div>
                    </div>
                </c:otherwise>
            </c:choose>
            </a>
            <!--鱼-->
            <div class="yuqun"></div>
        </div>
        <!--珊瑚-->
        <div id="processBar" class="bottom-box"></div>
    </div>
    <footer class="footer">
        活动由着迷网提供，与设备生产商Apple lnc.公司无关
    </footer>
</div>
<%@ include file="/views/jsp/youku_pwiki.jsp" %>
</body>
</html>