<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!--[if IE]><p style="line-height:40px;width:100%;text-align:center;color:#f00;height:40px;margin:0 auto;">为了您的浏览体验，请使用谷歌打开！</p><![endif]-->
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <title>粽子甜咸大战_端午节活动_着迷玩霸</title>
    <meta name="description" content="粽子到底是甜的好吃还是咸的好吃？" />
    <link href="${URL_LIB}/static/theme/wap/css/activity/2015dw/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {return false}, true)
        }, true);
    </script>
</head>
<body>

<div id="_sharebtn_status" style="display: none;">yes</div>
<div id="_title" style="display: none;">自古甜咸不两立</div>
<div id="_desc" style="display: none;">所以粽子到底是甜的还是咸的？</div>
<div id="_clientpic" style="display: none;">${URL_LIB}/static/theme/wap/images/activity/2015dw/2015dw-share.jpg</div>
<div id="_share_task" style="display: none;">2015_dw</div>
<div id="_share_url" style="display: none;">${short_url}</div>


<div id="wrapper">
    <header class="header">
        <div class="top-Banner">
            <span class="top-bg"><img src="${URL_LIB}/static/theme/wap/images/activity/2015dw/topBanner.jpg" alt="" title="" /></span>
        </div>
    </header>
    <section>
        <div class="vote-box">
            <h2><img src="${URL_LIB}/static/theme/wap/images/activity/2015dw/title-bg.png" alt="甜粽子VS咸粽子，你支持哪一方？" title="甜粽子VS咸粽子，你支持哪一方？" /></h2>
            <div class="progressBar clearfix">
                <p id="pg-bar" class="pg-bar">
                    <b id="l-bar" style="width:${tianPer}%;">${tianIn}</b>
                    <b id="r-bar" style="width:${xianPer}%;">${xianIn}</b>
                </p>
                <span id="vote-l-btn" class="vote-l-btn"><img src="${URL_LIB}/static/theme/wap/images/activity/2015dw/vote-l-btn.png"  /></span>
                <span id="vote-r-btn" class="vote-r-btn"><img src="${URL_LIB}/static/theme/wap/images/activity/2015dw/vote-r-btn.png" /></span>
            </div>
            <div class="popup">O(∩_∩)O~<br />您已经支持过了</div>
        </div>
        <div class="vote-state">
            <h3>支持说明：每天可投票一次，别忘了拉上你的盟友。</h3>
            <p>是夜。<br />
                大街小巷，灯火通明，老老少少沉浸在节日的气氛中。<br />
                然而有两个身影，伫立在紫禁之巅，手住在腰间的剑柄，一动不动，仿佛连空气都凝固了。<br />
                “甜。”<br />
                “咸。”<br />
                ……<br />
                双方坚守自己的立场，并试图用语言让对方动摇。<br />
                高手间的对决，一瞬间的破绽，就能决定胜负。<br />
                月色渐沉，夜已深，已静。二人对峙依旧。<br />
                这场决斗的胜利，由你而定。<br />
                ……<br />
                然而，<br />
                这并没有卵用。</p>
        </div>
    </section>
</div>
<script>
    var cur_date = new Date().toLocaleDateString();
    var mod={
            int:function(){
                $("#vote-l-btn").on('click',function () {
                    agree('tian');
                });
                $("#vote-r-btn").on('click',function () {
                    agree('xian');
                });
            },
            windowFn:function(){
                var popup=$('.popup');
                popup.addClass("show");
                setTimeout(function(){
                    popup.attr("class","popup");
                },3000);
            }
    };
    function agree(index){
        var curdate_var =window.localStorage.getItem("2015_dw_"+cur_date);
        if(typeof (curdate_var)!="undefined" && curdate_var!=null ){
            mod.windowFn();
            return false;
        }
        $.ajax({
            url: 'http://api.' + joyconfig.DOMAIN + "/joymeapp/gameclient/webview/task/taskdownload?type=2015dw&support="+index,
            type: "get",
            dataType: "jsonp",
            jsonpCallback: "taskdownload",
            success: function (req) {
                window.localStorage.setItem("2015_dw_"+cur_date ,index);
                var tian = $("#l-bar").html();
                var xian = $("#r-bar").html();
                if(index=='tian'){
                    $("#l-bar").html(parseInt(tian)+1);
                    tian = parseInt(tian)+1;

                    $("#vote-l-btn").addClass("myAnim");
                    $("#vote-l-btn").html('<img src="${URL_LIB}/static/theme/wap/images/activity/2015dw/vote-l-btn1.png"/>');
                }else if(index=='xian'){
                    $("#r-bar").html(parseInt(xian)+1);
                    xian = parseInt(xian)+1;

                    $("#vote-r-btn").addClass("myAnim");
                    $("#vote-r-btn").html('<img src="${URL_LIB}/static/theme/wap/images/activity/2015dw/vote-r-btn1.png"/>');
                }
                var total = parseInt(tian)+parseInt(xian);
                var tianPer = tian * 100 /total;
                var xianPer = xian * 100 /total;
                if(tianPer>70){
                    $("#l-bar").css("width","70%");
                    $("#r-bar").css("width","30%");
                }else if(xianPer>70){
                    $("#l-bar").css("width","30%");
                    $("#r-bar").css("width","70%");
                }else{
                    $("#l-bar").css("width",tianPer+"%");
                    $("#r-bar").css("width",xianPer+"%");
                }

            }
        });
    }

    $(document).ready(function(){
        mod.int();
        var curdate_var =window.localStorage.getItem("2015_dw_"+cur_date);
        if(typeof (curdate_var)!="undefined" && curdate_var!=null ){
            if(curdate_var=="tian"){
                $("#vote-l-btn").html('<img src="${URL_LIB}/static/theme/wap/images/activity/2015dw/vote-l-btn1.png"/>');
            }else{
                $("#vote-r-btn").html('<img src="${URL_LIB}/static/theme/wap/images/activity/2015dw/vote-r-btn1.png"/>');
            }
        }
    });

</script>
</body>
</html>
