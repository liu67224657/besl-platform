<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1x.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/jstllibs.jsp" %>
<%@ include file="/views/jsp/common/jsconfig.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <meta http-equiv="Cache-Control" content="no-Cache"/>
    <meta http-equiv="Cache-Control" content="max-age=0"/>
    <title>详情</title>
    <link href="http://static.joyme.com/app/wanba/css/common.css?${version}" rel="stylesheet"
          type="text/css">
    <link href="http://static.joyme.com/app/wanba/css/details.css?${version}" rel="stylesheet"
          type="text/css">

    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.11.0.min.js"></script>

    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
        var browser = {
            versions: function () {
                var u = navigator.userAgent, app = navigator.appVersion;
                return {//移动终端浏览器版本信息
                    trident: u.indexOf('Trident') > -1, //IE内核
                    presto: u.indexOf('Presto') > -1, //opera内核
                    webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
                    gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
                    mobile: !!u.match(/AppleWebKit.*Mobile.*/), //是否为移动终端
                    ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
                    android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或uc浏览器
                    iPhone: u.indexOf('iPhone') > -1, //是否为iPhone或者QQHD浏览器
                    iPad: u.indexOf('iPad') > -1, //是否iPad
                    webApp: u.indexOf('Safari') == -1 //是否web应该程序，没有头部与底部
                };
            }(),
            language: (navigator.browserLanguage || navigator.language).toLowerCase()
        }
        $(document).ready(function() {
            <c:forEach items="${commentHistories}" var="item">
            var commentId = '${item.objectId}';
            $("[name='" + commentId + "']").each(function() {
                $(this).addClass("active");
            });
            </c:forEach>
            var createLongtime = '${createTimeLong}';
            var newTime = new Date().getTime();
            var time = newTime - createLongtime;
            var oneday = 60 * 60 * 24 * 1000;
            var threeday = 60 * 60 * 24 * 3 * 1000;
            if (time <= 10 * 60 * 1000) {
                $("#time").text("刚刚");
            } else if (time <= oneday) {
                time = Math.round(time / 3600 / 1000);
                time = time == 0 ? 1 : time;
                $("#time").text("最近" + time + "小时前");
            } else if (time > oneday && time <= threeday) {

                $("#time").text(Math.round(time / 86400 / 1000) + "天前");
            } else {
                var time = '${commentBean.createTime}';

                $("#time").text(time.substring(0, time.lastIndexOf(":")));
            }

//            $(".large-load").click(function() {
//                var img = $('.mi-tit-box').find('em').children('img').attr('src');
//                if (browser.versions.ios) {
//                    _jclient.saveImage("src=" + img, function (response) {
//                        if (response) {
//                            $('.Threebox').find("span").text("保存成功");
//                            $('.Threebox').show().delay(3000).hide(0);
//                        }
//                    });
//                }
//                if (browser.versions.android) {
//                    _jclient.saveImage("src=" + img);
//                }
//            });
            $(".call-btn").on("touchstart", function(ev) {
                ev.stopPropagation();
                ev.preventDefault();
                $(".shadow-bg").css("display", "none");
            });

            $(".jumpu").on("touchstart", function(ev) {
                var uid = $("input[name='commentuid']").val();
                var nick = $("input[name='commentnick']").val();
                var u = $("#uid").val();
                var jt = 42;

                if (u == uid) {
                    jt = 41;
                }

                _jclient.jump("jt=" + jt + "&ji=" + uid + "&nick=" + nick);
            });

            <c:forEach items="${pics}" var="pic">
            var wp = $(window).width();
            var width = "${pic.width}";
            if (width > wp) {
                var height = "${pic.height}";
                var b = wp / width;
                var endH = b * height;
                var endW = wp - 20;
                var content = "<cite><img class='lazy' src='${URL_LIB}/static/theme/default/images/data-bg.gif' height=" + endH + " data-src='${pic.pic}?imageMogr2/auto-orient/thumbnail/" + endH * endW + "@' data-img='${pic.pic}'/> </cite>";
                $(".details-cont").append(content);
            } else {
                var height = "${pic.height}";
                var endH = height;
                var endW = width;
                var content = "<cite><img class='lazy' src='${URL_LIB}/static/theme/default/images/data-bg.gif' height=" + endH + " data-src='${pic.pic}?imageMogr2/auto-orient/thumbnail/" + endH * endW + "@' data-img='${pic.pic}'/> </cite>";
                $(".details-cont").append(content);
            }
            </c:forEach>

        });
    </script>

</head>
<body>
<div class="Threebox"><span></span></div>
<div id="_report" style="display: none;">report</div>
<!--wrapper-->
<div id="wrapper" class="pb ">
    <div class="cont inner" id="cont ">
        <div class="loading">

        </div>
        <!--tz-cont-->
        <section class="details">
            <header class="details-tit">
                <c:forEach items="${profiles}" var="profile">
                    <c:if test="${commentBean.uri eq profile.profileId}">
                        <cite class="jumpu"><img
                                src="${icon:parseIcon(profile.icon,profile.sex, "")}?imageView2/1/w/100/h/100"
                                alt=""></cite>
                        <input type="hidden" name="commentuid" value="${profile.uid}"/>
                        <input type="hidden" name="commentnick" value="${profile.nick}"/>
                        <span class="jumpu">${profile.nick}</span>
                    </c:if>
                </c:forEach>
                <em id="time"></em>
            </header>
            <article class="details-cont">
                <p>${commentBean.description}</p>
                <%--<c:forEach items="${pics}" var="pic" varStatus="index">--%>
                <%--<cite><img alt="" class="lazy" src="${URL_LIB}/static/theme/default/images/data-bg.gif"--%>
                <%--data-src="${pic}" data-img="${pic}"/> </cite>--%>
                <%--</c:forEach>--%>

            </article>
            <nav class="details-nav">
                <a href="javascript:nativeShare('${sharedesc}','${sharedesc}','${shareimg}','${commentBean.commentId}')"
                   class="share-btn"><span>分享</span></a>
                <input type="hidden"/>
                <a href="javascript:void(0);" class="pl-btn"><span class="ll-num"> <c:choose>
                    <c:when test="${commentBean.commentSum==0}">
                        评论
                    </c:when>
                    <c:otherwise>
                        ${commentBean.commentSum}
                    </c:otherwise>
                </c:choose></span></a>
                <a href="javascript:agreeMiyou('${commentBean.commentId}','')" class="zan-btn"
                   name="${commentBean.commentId}">
            <span class="ll-num" id="agreesum${commentBean.commentId}"
                  name="agreesum${commentBean.commentId}">
            <c:choose>
                <c:when test="${commentBean.scoreCommentSum==0}">
                    赞
                </c:when>
                <c:otherwise>
                    ${commentBean.scoreCommentSum}
                </c:otherwise>
            </c:choose>
            </span>
                </a>
            </nav>
        </section>
        <!--ui-scroller-->
        <section class="comment">

            <!--module-comment-->

            <%--<c:if test="${not empty hotList}">--%>
            <%--<!--hot-tit-->--%>
            <%--<header class="comment-tit"><span>神评论</span></header>--%>
            <%--<div class='comment-cont clearfix' id='hot-comment'>--%>
            <%--<!--module-list-->--%>
            <%--<c:forEach items="${hotList}" var="item">--%>
            <%--<div class='module-list'>--%>
            <%--<div class='module-infobox'>--%>
            <%--<cite class='user-headImg'><img src='${item.reply.user.icon}' alt=''--%>
            <%--title=''/></cite>--%>

            <%--<div class='module-shop'>--%>
            <%--<div><em>${item.reply.user.name}</em><span class='module-reply fr'>回复</span><a--%>
            <%--href="javascript:agreeMiyou('${commentBean.commentId}','${item.reply.reply.rid}')"--%>
            <%--class='module-zan fr ll-num'--%>
            <%--name="${item.reply.reply.rid}">${item.reply.reply.agree_sum}</a>--%>
            <%--</div>--%>
            <%--<div class='module-date'><span>${item.reply.reply.post_date}</span></div>--%>
            <%--</div>--%>
            <%--</div>--%>
            <%--<div class='module-txt'>--%>
            <%--${item.reply.reply.body.text}--%>
            <%--</div>--%>
            <%--<c:if test="${uid==item.reply.user.uid}">--%>
            <%--<div class="remove-btn">--%>
            <%--<a href="javascript:deletereply('${item.reply.reply.rid}')">删除</a>--%>
            <%--</div>--%>
            <%--</c:if>--%>
            <%--</div>--%>
            <%--</c:forEach>--%>
            <%--<!--module-list==end-->--%>
            <%--</div>--%>
            <%--</c:if>--%>
            <!--hot-tit==end-->
            <!--all-tit-->
            <c:if test="${not empty mainReplyRows&& not empty mainReplyRows.rows}">
                <c:choose>
                    <c:when test="${not empty hotList}">
                        <header class="comment-tit"><span>更多评论</span></header>
                    </c:when>
                    <c:otherwise>
                        <header class="comment-tit"><span>全部评论</span></header>
                    </c:otherwise>
                </c:choose>

                <div class='comment-cont clearfix' id='all-comment'>
                    <!--module-list-->
                    <c:forEach items="${mainReplyRows.rows}" var="item">
                        <c:if test="${empty hotMap[item.reply.reply.rid]}">
                            <div class='module-list'>
                                <div class='module-infobox'>
                                    <cite class='user-headImg'
                                          onclick="sendMiyou('${item.reply.user.uid}','${item.reply.user.name}')"><img
                                            src='${item.reply.user.icon}' alt=''
                                            title=''/></cite>

                                    <div class='module-shop'>
                                        <div><em
                                                onclick="sendMiyou('${item.reply.user.uid}','${item.reply.user.name}')">${item.reply.user.name}</em><span
                                                class='module-reply fr'>回复</span><a
                                                href="javascript:agreeMiyou('${commentBean.commentId}','${item.reply.reply.rid}')"
                                                class='module-zan fr ll-num'
                                                name="${item.reply.reply.rid}">${item.reply.reply.agree_sum}</a>
                                        </div>
                                        <div class='module-date'><span>${item.reply.reply.post_date}</span>
                                        </div>
                                    </div>
                                </div>
                                <div class='module-txt'> ${item.reply.reply.body.text} </div>
                                <c:if test="${uid==item.reply.user.uid}">
                                    <div class="remove-btn">
                                        <a href="javascript:deletereply('${item.reply.reply.rid}')">删除</a>
                                    </div>
                                </c:if>
                            </div>
                        </c:if>
                    </c:forEach>
                </div>
                <!--module-list==end-->
                            <span style="display:none;" id="hotId">
                                <c:forEach items="${hotMap}" var="entry">
                                    ${entry.value},
                                </c:forEach>
                            </span>


            </c:if>
            <c:if test="${empty mainReplyRows||empty mainReplyRows.rows}">
                <div class="none-comment info-bar">
                    <cite></cite>
                    <span>别让卤煮寂寞太久！</span>
                </div>
            </c:if>
            <!--all-tit==end-->
        </section>

        <input type="hidden" value="${mainReplyRows.page.curPage}" id="miyouCurPage"/>
        <input type="hidden" value="${mainReplyRows.page.maxPage}" id="miyouMaxPage"/>
        <!--ui-scroller-->

        <!--tz-cont-->
        <!--alert-comment-->
        <div class='alert-comment'>
            <div class='info-alert-box'>
                <!--info-beyond-->
                <div class='info-beyond'>
                    <span></span>
                </div>
                <!--info-beyond==end-->
                <div class='info-alert-form'>
                    <textarea class='info-textarea' placeholder='请输入内容，140字以内'></textarea>
                </div>
                <div class='info-alert-shop clearfix'>
                    <span class='fl' style='display:none'>已超出<b class='inp-num'>0</b>个字</span>

                    <div class='fr'>
                        <span class="cancel-btn">取消</span>
                        <span class="reply-btn">来一发</span>
                    </div>
                </div>
            </div>
        </div>
        <!--alert-comment==end-->
        <div class='wp-bg'></div>
    </div>
    <!--评论-->
    <div class="wp-comment">
        <a href="javascript:void(0);">说点什么吧&nbsp;&nbsp;◕‿‿◕</a>
    </div>
    <!--评论-->

</div>

<div class="shadow-bg" style="display:none;">
    <div class="dialog">
        <p>确定要删除吗？</p>
           <span>
               <cite class="call-btn">取消</cite>
               <cite class="sure-btn">确定</cite>
           </span>
    </div>
</div>

<input type="hidden" id="uid" value="${uid}"/>
<input type="hidden" id="logindomain" value="${logindomain}"/>
<input type="hidden" id="appkey" value="${appkey}"/>
<input type="hidden" id="cid" value="${commentBean.commentId}"/>
<!--wrapper-->
<script type="text/javascript" src="http://static.joyme.com/app/wanba/js/lazyImg.js"></script>

<script>
window.onload = function() {
    var imgs = document.querySelectorAll('img.lazy');
    var imgArr = [];
    for (var i = 0; i < imgs.length; i++) {
        var idx = imgs[i].indexs;
        imgs[i].idx = i;
        imgArr.push(imgs[i].getAttribute('data-img'));
        imgs[i].onclick = function() {
            var oSrc = this.getAttribute('data-img');
            _jclient.openImg("img=" + oSrc + "&position=" + this.idx + "&imgs=" + imgArr);
            // console.log(imgArr);
        }
    }

}


function deletereply(rid) {
    $(".shadow-bg").css("display", "block");
    $(".sure-btn").bind('touchstart', function(ev) {
        ev.stopPropagation();
        ev.preventDefault();
        var cid = $("#cid").val();
        location.href = "/joymeapp/gameclient/webview/miyou/deletereply?rid=" + rid + "&commentid=" + cid + "&uid=${uid}";
    })

}
function browseNum() {
    $('.ll-num').each(function() {
        var num = $(this).text();
        if (isNaN(num)) {
            return;
        }
        num = parseInt(num);
        if (num >= 10000) {
            num = ($(this).text() / 10000).toFixed(1)
            var ind = num.indexOf('.');
            var lastNum = num.substr(ind + 1, 1);
            if (lastNum == '0') {
                num = num.substr(0, ind)
            }
            $(this).text(num + '万');
        } else {
            $(this).text(num);
        }
    })
}
browseNum();
function agreeMiyou(commentId, rid) {
    var uid = $("#uid").val();
    $.post("/joymeapp/gameclient/json/miyou/agree", {uid:uid,cid:commentId,rid:rid}, function(req) {
        var resMsg = eval('(' + req + ')');
        if (resMsg.rs == '1') {
            var agreeSum = $("#agreesum" + commentId).text();
            if (isNaN(agreeSum)) {
                agreeSum = 0;
            }
            agreeSum = parseInt(agreeSum) + 1;

            if (rid == '') {
                $("[name='" + commentId + "']").each(function() {
                    $(this).addClass("active active-end");
                });
                var sum = $("#agreesum" + commentId).text().trim();
                if (sum == '赞' || !isNaN(sum)) {
                    $("[name='agreesum" + commentId + "']").each(function() {
                        $(this).text(agreeSum);
                    });
                }
            } else {
                $("[name=" + rid + "]").each(function() {
                    $(this).addClass("active");
                    var sum = $(this).text().trim();
                    if (!isNaN(sum)) {
                        $(this).text(parseInt(sum) + 1);
                    }
                });
            }

            return;
        } else if (resMsg.rs == '-40016') {
            $('.Threebox').find("span").text("已经点过赞了");
            $('.Threebox').show().delay(3000).hide(0);
            return;
        } else if (resMsg.rs == '-10104') {
            $('.Threebox').find("span").text("参数为空");
            $('.Threebox').show().delay(3000).hide(0);
            return;
        } else if (resMsg.rs == '-1000') {
            $('.Threebox').find("span").text("点赞失败");
            $('.Threebox').show().delay(3000).hide(0);
            return;
        } else {
            $('.Threebox').find("span").text("点赞失败");
            $('.Threebox').show().delay(3000).hide(0);
            return;
        }
    });
}

function nativeShare(title, content, pic, commentId) {
    var url = '${share_url}';
    var pic = '${shareimg}';
    _jclient.share("title=" + title + "&content=" + content + "&picurl=" + encodeURIComponent(pic) + "&url=" + encodeURIComponent(url));
}

function sendMiyou(uid, nick) {
    var u = $('#uid').val();
    var jt = 42;
    if (u == uid) {
        jt = 41;
    }
    _jclient.jump("jt=" + jt + "&ji=" + uid + "&nick=" + nick);
}


window.addEventListener('load', function() {
    console.log($('.inner').height());
    //document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);
    var postbool = true;
    var commentTools = {
        'int':function() {
            commentTools.huifu();
            commentTools.txtTools();
            $(window).resize(function(e) {
                commentTools.windowHeight();
            });
        },
        //回复
        'huifu':function() {
            var wp_commentLink = $('.wp-comment a');

            var wp_bg = $('.wp-bg');
            var alert_comment = $('.alert-comment');
            var $textarea = $('.info-textarea');
            var $cancelBtn = $('.cancel-btn');
            var times = null;
            wp_commentLink.on('click', function(e) {
                e.preventDefault();
                var logindomain = $("#logindomain").val();
                if (logindomain == '' || logindomain == 'client') {
                    _jclient.showLogin();
                    return;
                }


                clearInterval(times);
                wp_bg.show();
                alert_comment.show();
                $textarea.focus();
            });
            $cancelBtn.on('click', function(ev) {
                ev.stopPropagation();
                ev.preventDefault();
                wp_bg.hide();
                alert_comment.hide();
            });
            wp_bg.on('touchstart', function(ev) {
                ev.stopPropagation();
                ev.preventDefault()
            });
            wp_bg.on('touchmoveon', function(ev) {
                ev.stopPropagation();
                ev.preventDefault()
            });
        },
        //字数限制
//评论字数限制
        'txtTools':function() {
            var txtobj = {
                divName:'info-alert-box', //外层容器的class
                textareaName:'info-textarea',
                numName:'inp-num',
                num:140 //最大字数
            }
            var textareaFn = function() {
                var $onthis;
                var $divname = txtobj.divName;
                var $textareaName = txtobj.textareaName;
                var $numName = txtobj.numName;
                var $num = txtobj.num;
                var infotext = $('.' + $textareaName);
                var strlen = 0;
                var $replyBtn = $('.reply-btn');
                var $infoBeyond = $('.info-beyond');
                var ibTime = null;
                var $par;
                var $b;

                function isChinese(str) { //判断是不是中文
                    var oVal = str.val();
                    var oValLength = 0;
                    oVal.replace(/n*s*/, '') == '' ? oValLength = 0 : oValLength = oVal.match(/[^ -~]/g) == null ? oVal.length : oVal.length + oVal.match(/[^ -~]/g).length;
                    return oValLength;

                }

                //提示2秒后关闭
                function textTips() {
                    $infoBeyond.show();
                    clearInterval(ibTime);
                    ibTime = setInterval(function() {
                        $infoBeyond.hide();
                    }, 3000);
                }

                ;
                function numChange() {
                    var setNum = '';
                    var txtval = $.trim(infotext.val());
                    strlen = isChinese(infotext);

                    strlen = Math.ceil(strlen / 2);
                    //超出文字提示
                    if ($num - strlen < 0) {
                        $par.show();
                        $par.html('已超出<b class=' + $numName + '>' + Math.abs($num - strlen) + '</b>个字');
                    } else {
                        if (strlen >= 130) {//剩余最后10个字时提示
                            $par.show();
                            $par.html('还可以输入<b class=' + $numName + '>' + ($num - strlen) + '</b>个字');
                        } else {
                            if ($par != null) {
                                $par.hide();
                            }
                        }
                    }
                    if ($b != null) {
                        $b.html($num - strlen);
                    }
                }

                ;
                //当字数小于2或大于140
                $replyBtn.on('touchstart', function(ev) {
                    ev.stopPropagation();
                    ev.preventDefault();
                    numChange();
                    var txtval = $.trim(infotext.val());
                    if (txtval.length <= 1) {
                        textTips();
                        $infoBeyond.html('<span>请输入至少2个字</span>');
                        return;
                    }

                    if (strlen > 140) {
                        textTips();
                        $infoBeyond.html('<span>请删减后再进行评论</span>');
                        return;
                    } else {
                        if (postbool) {
                            $replyBtn.css("background", "#ccc");
                            postbool = false;
                            var textare = infotext.val();
                            var uid = $("#uid").val();
                            var commentid = $("#cid").val();
                            var appkey = $("#appkey").val();
                            textare = {text:textare};
                            var text = JSON.stringify(textare);
                            $.ajax({
                                        type : "post",
                                        url:"/joymeapp/gameclient/json/miyou/comment",
                                        data:{uid:uid,cid:commentid,appkey: appkey,text:text},
                                        success : function(req) {
                                            var result = eval('(' + req + ')');
                                            if (result.rs == '1') {
                                                location.reload();
                                            } else if (result.rs == '-40005') {

                                                $replyBtn.css("background", "#3BCB09");
                                                textTips();
                                                $infoBeyond.html('<span>文字不能为空</span>');
                                                postbool = true;
                                                return;
                                            } else if (result.rs = '-40017') {
                                                $replyBtn.css("background", "#3BCB09");
                                                var keyword = result.result;
                                                textTips();
                                                $infoBeyond.html('<span>不要使用" ' + keyword + ' "等词汇，请修改</span>');
                                                postbool = true;
                                            } else {
                                                $replyBtn.css("background", "#3BCB09");
                                                textTips();
                                                $infoBeyond.html('<span>发布失败</span>');
                                                postbool = true;
                                            }
                                        },error:function(req) {
                                            postbool = true;
                                        }
                                    });
                        }
                    }

                });
                var inp = 'input propertychange' || 'keyup';
                infotext.on(inp, function() {
                    $b = infotext.parents('.' + $divname).find('.' + $numName); //获取当前的数字
                    $par = $b.parent();
                    $onthis = $(this);
                    numChange();
                });
            }
            textareaFn();
        }

    }
    commentTools.int();
    pulldownLoad('cont', 'comment-cont');
});
var check = false;
function pulldownLoad(parentBox, parentCont) {


    var bottomnum = 50,//距离底部距离
            num = 5;//一次加载几条
    $parentBox = $('.' + parentBox),
            $parentCont = $('.' + parentCont),
            load = $('<div>').addClass('load-box show').html('<span>加载更多</span>'),
            timer = null,
            $parentBox.scroll(function() {
                var srollPos = $parentBox.scrollTop(),
                        docuH = $parentBox[0].scrollHeight,
                        totalheight = parseFloat($(window).height()) + parseFloat(srollPos);
                if (docuH - bottomnum <= totalheight && !check) {
                    var cid = $("#cid").val();
                    var uid = $("#uid").val();
                    var curNum = $("#miyouCurPage").val();
                    var maxNum = $("#miyouMaxPage").val();
                    if (curNum == '') {
                        return;
                    }
                    if (parseInt(maxNum) <= parseInt(curNum)) {
                        return;
                    }
                    $('.cont').append(load);
                    curNum = parseInt(curNum) + parseInt(1);
                    check = true;
                    $.ajax({
                                type : "post",
                                url:"/joymeapp/gameclient/json/miyou/replylist",
                                data:{cid:cid,count:15,pnum: curNum,uid:uid},
                                success : function(req) {
                                    var result = eval('(' + req + ')');
                                    if (result.rs == '1') {
                                        var hotId = $("#hotId").html().split(",");
                                        var replylist = result.result.replylist;
                                        for (var i = 0; i < replylist.length; i++) {
                                            var bool = true;
                                            for (var j = 0; j < hotId.length; j++) {
                                                if (replylist[i].reply.reply.rid == hotId[j]) {
                                                    bool = false;
                                                }
                                            }
                                            if (bool) {
                                                var content = "<div class='module-list'><div class='module-infobox'> <cite class='user-headImg' onclick='sendMiyou(" + '"' + replylist[i].reply.user.uid + '"' + "," + '"' + replylist[i].reply.user.name + '"' + ")'>" +
                                                        "<img src=" + replylist[i].reply.user.icon + " > </cite><div class='module-shop'>" +
                                                        "<div><em onclick='sendMiyou(" + '"' + replylist[i].reply.user.uid + '"' + "," + '"' + replylist[i].reply.user.name + '"' + ")'>" + replylist[i].reply.user.name + "</em><span class='module-reply fr'>回复</span>" +
                                                        " <a href='javascript:agreeMiyou(" + '"' + cid + '"' + "," + '"' + replylist[i].reply.reply.rid + '"' + ")' class='module-zan fr ll-num' name='" + replylist[i].reply.reply.rid + "' >" + replylist[i].reply.reply.agree_sum + "</a></div><div class='module-date'><span>" + replylist[i].reply.reply.post_date + "</span>" +
                                                        "</div></div></div><div class='module-txt'>" + replylist[i].reply.reply.body.text + "</div>";
                                                if (uid == replylist[i].reply.user.uid) {
                                                    content += "<div class='remove-btn'> <a href=\"javascript:deletereply('" + replylist[i].reply.reply.rid + "')\">删除</a></div>"
                                                }

                                                content += "</div>";

                                                "</div>";
                                                $("#all-comment").append(content);
                                            }
                                        }
                                        $("#miyouCurPage").val(result.page.curPage);
                                        $("#miyouMaxPage").val(result.page.maxPage);

                                        if (result.result.commentHistories != null) {
                                            for (var i = 0; i < result.result.commentHistories.length; i++) {
                                                var commentId = result.result.commentHistories[i].objectId;
                                                $("[name='" + commentId + "']").each(function() {
                                                    $(this).addClass("active");
                                                });
                                            }
                                        }
                                        browseNum();
                                        load.remove();
                                        check = false;
                                    }
                                }});
                }
            });
    function fillCont() {
        clearTimeout(timer);
    }
}


var _paq = _paq || [];
_paq.push(['trackPageView']);
_paq.push(['enableLinkTracking']);
(function () {
    var u = "//stat.joyme.com/";
    _paq.push(['setTrackerUrl', u + 'piwik.php']);
    _paq.push(['setSiteId', 222]);
    var d = document, g = d.createElement('script'), s = d.getElementsByTagName('script')[0];
    g.type = 'text/javascript';
    g.async = true;
    g.defer = true;
    g.src = u + 'piwik.js';
    s.parentNode.insertBefore(g, s);
})();
</script>
</body>
</html>