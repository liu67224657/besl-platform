<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<%@ include file="/views/jsp/common/jsconfig.jsp" %>
<html lang="en">
<meta charset="utf-8">
<meta content="width=device-width, initial-scale=1.0, user-scalable=no" name="viewport">
<meta content="yes" name="apple-mobile-web-app-capable">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<meta content="telephone=no" name="format-detection">
<meta name="description" content="${gameDB.recommendReason}" />
<title>${gameDB.gameName}</title>
<link href="${URL_LIB}/static/theme/wap/css/wap_common.css" rel="stylesheet" type="text/css">
<link href="${URL_LIB}/static/theme/wap/css/yxfm.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>
<script type="text/javascript">
    window.addEventListener('DOMContentLoaded', function (){
        document.addEventListener('touchstart', function (){return false}, true)
    }, true);
</script>
<style>
    .jp-right-box-now{
        transition:width 1s linear;
        -webkit-transition:width 1s linear;
        -moz-transition:width 1s linear;
    }
    .box,.box-right,.bottom_ad{ transition:opacity 1s; opacity:0;}
    #loadTips1{ position:fixed; top:0; left:0; right:0; bottom:0; text-align:center;}
    .loadTips{ position:absolute; width:100px; height:20px; left:50%; top:50%; margin:-10px 0 0 -50px; z-index:999;}
</style>
<body>
<div id="_sharebtn_status" style="display: none;">yes</div>
<div id="_title" style="display: none;">${gameDB.gameName}</div>
<div id="_desc" style="display: none;">${gameDB.gameName}&nbsp;&nbsp;${gameDB.gameDBCover.coverComment}</div>
<div id="_clientpic" style="display: none;">${gameDB.gameIcon}</div>
<div id="_share_task" style="display: none;">sharehotgame</div>
<div id="_share_url" style="display: none;">${short_url}</div>
<div id="_directid" style="display: none;">${gameDB.gameDbId}</div>


<div id="wrapper">
    <%--<div class="bg" style="background-color:#FFF"><!--修改游戏封面切换图片背景-->--%>
        <div class="bg" style="background-color:#FFF;background-size:100% auto;">
        <img src="${gameDB.gameDBCover.coverPicUrl}" id="loadImg" style="display: none;">



    <%--<div class="btn_icon back_icon">--%>
            <%--<a href="#"></a>--%>
        <%--</div>--%>
        <%--<div class="btn_icon share_icon">--%>
            <%--<a href="#"></a>--%>
        <%--</div>--%>
        <div>
            <div class="box yx show">
                <div>
                    <cite class="yx-icon"></cite>
                </div>
            </div>
            <div class="box jp">
                <div>
                    <div class="jp_cont">
                        <span>
                            ${averageValue}
                        </span>
                        <b>${average}</b>
                        <cite></cite>
                    </div>
                </div>
            </div>
            <div class="box zan">
                <div>
                    <div class="zan_cont">
                        <cite></cite>
                        <span class="zan_num" id="agreeNum">${gameDB.gameDBCover.coverAgreeNum}</span>
                    </div>
                    <font>已赞</font>
                </div>
            </div>
        </div>
        <div class="box-right active">
            <div>
                <c:if test="${existgift == true && (from ==null || from!='shake') }">
                    <div class="btn lb-btn" id="libao"><cite></cite><span>礼包</span><b style="display: none;"></b></div>
                </c:if>
                <c:if test="${existgift == false || (from !=null && from=='shake' )}">
                    <div class="btn lb-btn" id="libao" style="display: none"><cite></cite><span>礼包</span><b style="display: none;"></b></div>
                </c:if>
                <c:if test="${existnews == true  && (from ==null || from!='shake') }">
                    <div class="btn gl-btn" id="gongl"><cite></cite><span>攻略</span><b style="display: none;"></b></div>
                </c:if>
                <c:if test="${existnews == false || (from !=null && from=='shake')}">
                    <div class="btn gl-btn" id="gongl" style="display: none"><cite></cite><span>攻略</span><b style="display: none;"></b></div>
                </c:if>
                <div class="box-right-main">
                    <div class="yx-right-box">
                        <b class="yx-title-bg"></b>
                        <div class="yx-right-box-ttile"><span>${gameDB.gameDBCover.coverTitle}</span></div>
                        <div class="yx-right-box-main">
                            <h1>${gameDB.gameDBCover.coverComment}</h1>
                            <h2>玩霸点评：</h2>
                            <p class="string_cut">${gameDB.gameDBCover.coverDesc}</p>
                        </div>
                    </div>
                    <!--游戏右侧内容==end-->
                    <!--精品右侧内容-->
                    <div class="jp-right-box" style="visibility:hidden">
                        <h1><span>${gameDB.gameDBCoverFieldJson.key1}</span><p class="rate"><b>${gameDB.gameDBCoverFieldJson.value1}</b>/<em>10</em></p></h1>
                        <div class="jp-right-box-overall"><div class="jp-right-box-now"></div></div>
                        <h1><span>${gameDB.gameDBCoverFieldJson.key2}</span><p class="rate"><b>${gameDB.gameDBCoverFieldJson.value2}</b>/<em>10</em></p></h1>
                        <div class="jp-right-box-overall"><div class="jp-right-box-now"></div></div>
                        <h1><span>${gameDB.gameDBCoverFieldJson.key3}</span><p class="rate"><b>${gameDB.gameDBCoverFieldJson.value3}</b>/<em>10</em></p></h1>
                        <div class="jp-right-box-overall"><div class="jp-right-box-now"></div></div>
                        <h1><span>${gameDB.gameDBCoverFieldJson.key4}</span><p class="rate"><b>${gameDB.gameDBCoverFieldJson.value4}</b>/<em>10</em></p></h1>
                        <div class="jp-right-box-overall"><div class="jp-right-box-now"></div></div>
                        <h1><span>${gameDB.gameDBCoverFieldJson.key5}</span><p class="rate"><b>${gameDB.gameDBCoverFieldJson.value5}</b>/<em>10</em></p></h1>
                        <div class="jp-right-box-overall"><div class="jp-right-box-now"></div></div>
                    </div>
                    <!--精品右侧内容==end-->
                </div>
            </div>
        </div>
        <div class="bottom_ad">

            <c:if test="${downLoadUrl!=''}">
                <a href="javascript:void(0);" data-link="${downLoadUrl}" class="bottom_ad_box" id="bottom_downloadlink">
                </c:if>
            <c:if test="${downLoadUrl==''}">
                <a href="javascript:void(0);" class="bottom_ad_box no-loadBtn">
            </c:if>
                <cite><img src="${gameDB.gameIcon}" alt=""></cite>
                <h1>${gameDB.gameName}</h1>
                <h2 class="string_cut1">${gameDB.downloadRecommend}</h2>
                <span>立即下载</span>

            </a>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function(){

        $("#gongl").on("touchstart",function(){
            try{
                if(${gameDB.modifyTime!=null} && ${gameDB.modifyTime.lastModifyTime>0}){
                    window.localStorage.setItem("gameid_lastModifyTime_${gameDB.gameDbId}","${gameDB.modifyTime.lastModifyTime}");
                }else{
                    window.localStorage.setItem("gameid_lastModifyTime_${gameDB.gameDbId}","0");
                }
                <c:if test="${isNewVersion == false}">
                _jclient.jump('jt=24&ji=${gameDB.gameDbId}');
                </c:if>
                <c:if test="${isNewVersion == true}">
                _jclient.jump('jt=24&ji=${gameDB.gameDbId}_${newsrelationid}');
                </c:if>
            }catch(e){}
        });

        $("#libao").on("touchstart",function(){
            try{
                if(${gameDB.modifyTime!=null} && ${gameDB.modifyTime.giftlastModifyTime>0}){
                    window.localStorage.setItem("gameid_giftModifyTime_${gameDB.gameDbId}","${gameDB.modifyTime.giftlastModifyTime}");
                }else{
                    window.localStorage.setItem("gameid_giftModifyTime_${gameDB.gameDbId}","0");
                }
                <c:if test="${isNewVersion == false}">
                _jclient.jump('jt=24&ji=${gameDB.gameDbId}');
                </c:if>
                <c:if test="${isNewVersion == true}">
                _jclient.jump('jt=24&ji=${gameDB.gameDbId}_${giftrelationid}');
                </c:if>
            }catch(e){}
        });
        var gameDbId =window.localStorage.getItem("gameid_${gameDB.gameDbId}");
        //存在
        if(typeof (gameDbId)!="undefined" && gameDbId!=null ){
            $(".zan_cont").find("cite").addClass("active");
        }
        //加载图片
        getImageURL();
        //截字
        stringCut('string_cut',56);
        stringCut('string_cut1',12);

        var lastModifyTime =window.localStorage.getItem("gameid_lastModifyTime_${gameDB.gameDbId}");
        var giftModifyTime =window.localStorage.getItem("gameid_giftModifyTime_${gameDB.gameDbId}");
        if((lastModifyTime==null)|| (typeof (lastModifyTime)=="undefined" || lastModifyTime!=null && ${gameDB.modifyTime!=null} && ${gameDB.modifyTime.lastModifyTime>0} && "${gameDB.modifyTime.lastModifyTime}">lastModifyTime)){
            $("#gongl b").show();
        }
        if((giftModifyTime==null) || (typeof (giftModifyTime)=="undefined" || giftModifyTime!=null && ${gameDB.modifyTime!=null} && ${gameDB.modifyTime.giftlastModifyTime>0} && "${gameDB.modifyTime.giftlastModifyTime}">giftModifyTime)){
            $("#libao b").show();
        }

        $('#bottom_downloadlink').on("touchstart", function () {
            var downloadlink = $(this).attr('data-link');
            if (downloadlink != null && downloadlink.length > 0) {
                downloadlink = encodeURIComponent(downloadlink)
                window._jclient.download('src=' + downloadlink);
            }
        });

    });
    var h=$(window).height();
    $('.bg').height(h)
    function bili(){
        $('.jp-right-box-now').each(function(i){
            var now=parseInt($(this).parent().prev('h1').find('b').text());
            var total=parseInt($(this).parent().prev('h1').find('em').text());
            var w=$('.jp-right-box-overall:first').width();
            $(this).width(w/total*now)
        })
    }
    function bili2(){
        $('.jp-right-box-now').each(function(i){
            $(this).width(0)
        })
    }

    $('.jp').on('touchstart',function(){
        $('.box-right,.box-right>div,.box-right-main,.jp-right-box').css({'visibility':'initial'});
        $('.yx').removeClass('show')
        $(this).addClass('show')
        $('.jp-right-box').addClass('a')
        bili();
        $('.box-right').removeClass('active');
        $('.yx-right-box').css({'visibility':'hidden'});
        $('.zan').removeClass('now')
    })
    $('.yx').on('touchstart',function(){
        $('.box-right,.box-right>div,.box-right-main,.yx-right-box').css({'visibility':'initial'});
        $('.jp').removeClass('show')
        $(this).addClass('show');
        $('.jp-right-box').removeClass('a')
        $('.zan').removeClass('now')
        bili2();
        $('.box-right').addClass('active');
        $('.jp-right-box').css({'visibility':'hidden'});
    })
    $('.zan').on('touchstart',function(){
        var gameDbId =window.localStorage.getItem("gameid_${gameDB.gameDbId}");
        //存在
        if(typeof (gameDbId)!="undefined" && gameDbId!=null ){
            $('.box-right,.box-right>div,.box-right-main,.jp-right-box,.yx-right-box').css({'visibility':'hidden'});
            $('.jp,.yx').removeClass('show');
            $('.jp-right-box').removeClass('a');
            bili();
            bili2();
            $(this).addClass('now');
            return;
        }


        $.ajax({
            url: 'http://api.' + joyconfig.DOMAIN + "/joymeapp/gameclient/json/gamedb/agree?gameId=${gameDB.gameDbId}",
            type: "get",
            dataType: "json",
            success: function (req) {
                if(req.rs==1){
                    window.localStorage.setItem("gameid_${gameDB.gameDbId}","${gameDB.gameDbId}");


                    $('.box-right,.box-right>div,.box-right-main,.jp-right-box,.yx-right-box').css({'visibility':'hidden'});
                    $('.jp,.yx').removeClass('show');
                    $('.jp-right-box').removeClass('a');
                    bili();
                    bili2();
                    $(".zan").addClass('now');

                    $(".zan_cont").find("cite").addClass("active");

                    var agreeNum = $("#agreeNum").html();
                    if(!isNaN(agreeNum)){
                        $("#agreeNum").html(parseInt(agreeNum)+1);
                    }
                }
            }
        });
    })

    function getImageURL(){
        var boxBg=$('.bg');
        var loadImg= document.getElementById("loadImg");
        var imgs =$("#loadImg").attr("src");
        var t=false;
        loadImg.onload=function(){
            $('.box,.box-right,.bottom_ad').css({'opacity':'1'});
            boxBg.css('background-image', 'url('+imgs+')');
            t=true;
        }
        if(!t){
            var timePic =setInterval(function(){
                if(!t){
                    $('.box,.box-right,.bottom_ad').css({'opacity':'1'});
                    boxBg.css('background-image', 'url('+imgs+')');
                    t=true;
                    clearInterval(timePic);
                }
            },50);
        }
    }

    function stringCut(tar,len){
        var target=$('.'+tar)
        var text=target.text();
        var text_len=text.length;
        if(text_len>len){
            target.text(text.substr(0,len));
        }
    }
</script>
<%--<script src="${URL_LIB}/static/js/pwiki/wanba_game.js" type="text/javascript"></script>--%>
</body>
</html>
