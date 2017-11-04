<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta content="width=device-width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta name="Keywords" content="">
    <meta name="description"
          content=""/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>全部评论</title>
    <link href="${URL_STATIC}/mobile/cms/jmsy/css/common.css" rel="stylesheet"
          type="text/css">
    <link href="${URL_STATIC}/mobile/cms/jmsy/css/comment.css" rel="stylesheet"
          type="text/css">
    <script type="text/javascript" src="http://static.joyme.com/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
    </script>
</head>
<body>
<div class="topbar-mod border-b fn-t-c">
    <h1><a href="javascript:history.back(-1);" class="joyme-back">返回</a></h1>
    <c:choose>
        <c:when test="${userSession != null}">
            <span class="fn-right"><a href="http://passport.${DOMAIN}/auth/logout">退出登录</a></span>
        </c:when>
        <c:otherwise>
            <span class="fn-right"><a href="http://passport.${DOMAIN}/auth/loginpage">登录</a></span>
        </c:otherwise>
    </c:choose>
</div>
<section class="wrapper">
    <div class="comment">
        <div class="cmnt-wrap border-b" id="div_post"
             <c:if test="${profile == null && (flag == null || !(flag eq 'post'))}">style="display: none;"</c:if>>
            <h2>发表评论</h2>

            <div class="cmnt-area">
                <textarea class="new-area" id="textarea_body"><c:if test="${profile != null}">@${profile.nick}:</c:if></textarea>
            </div>
            <div class="cmnt-bar">
                <span class="cmnt-btn fn-right" id="span_do_post">评论</span>
                <span style="display:none;" id="span_tips">＊还可以输入<b class="inp-num">300</b>个字</span>
            </div>
            <div class="cmnt-beyond" id="div_beyond"><span>请输入至少2个字</span></div>
        </div>
        <input type="hidden" value="${unikey}" id="input_hidden_unikey"/>
        <input type="hidden" value="${domain}" id="input_hidden_domain"/>
        <input type="hidden" id="input_hidden_oid"
               <c:if test="${parentReply != null}">value="${parentReply.rootId}"</c:if>/>
        <input type="hidden" id="input_hidden_pid"
               <c:if test="${parentReply != null}">value="${parentReply.replyId}"</c:if>/>
        <input type="hidden" id="input_hidden_name"
               <c:if test="${profile != null}">value="@${fn:escapeXml(profile.nick)}:"</c:if>/>

        <div class="fn-stripe" id="div_post_next"
             <c:if test="${profile == null && (flag == null || !(flag eq 'post'))}">style="display: none;"</c:if>></div>
        <!--cmnt-list-->
        <c:choose>
            <c:when test="${list != null && list.size() > 0}">
                <div class="cmnt-list">
                    <c:forEach items="${list}" var="replyDto" varStatus="st">
                        <div class="cmnt-box">
                            <dl>
                                <dt>
                                    <span class="fn-clear">
                                        <a href="javascript:void(0);" class="userhead fn-left">
                                            <img class="lazy" src="${replyDto.user.icon}" alt="" title="">
                                        </a>
                                        <font class="fn-left">
                                            <a href="javascript:void(0);" class="userName">${replyDto.user.name}</a>
                                            <code>${replyDto.reply.post_date}</code>
                                        </font>
                                    </span>
                                    <cite class="cmnt-reply fn-right" data-pid="${replyDto.reply.rid}"
                                          data-oid="${replyDto.reply.oid}" data-uname="${fn:escapeXml(replyDto.user.name)}">回复</cite>
                                </dt>
                                <dd>
                                    <span class="border-b">
                                        <c:if test="${replyDto.puser != null}">
                                            <a href="javascript:void(0);">@${replyDto.puser.name}:</a>
                                        </c:if>
                                        ${replyDto.reply.body.text}
                                    </span>
                                </dd>
                            </dl>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="no-comment">
                    暂时没有评论，快来抢沙发
                </div>
            </c:otherwise>
        </c:choose>

        <!--cmnt-list==end-->
        <c:if test="${page != null && (page.curPage < page.maxPage)}">
            <div class="more-load" id="div_more_list" data-cp="${page.curPage}" data-mp="${page.maxPage}"
                 data-ps="${page.pageSize}">
                <a href="javascript:void(0);">点击加载更多...</a>
            </div>
        </c:if>
    </div>
</section>
<div class="cmnt-column border-t" id="div_float_post"
     <c:if test="${profile != null || (flag != null && flag eq 'post')}">style="display: none;"</c:if>>
    <a href="javascript:void(0);" id="a_to_comment"><cite>发表评论</cite></a>
</div>
<script>
    document.write('<script src="http://passport.${DOMAIN}/auth/footer/m?v=' + Math.random() + '"><\/script>');
</script>
<div class="goTop" style="display: none;">返回顶部</div>
<script type="text/javascript"
        src="${URL_STATIC}/mobile/cms/jmsy/js/common.js"></script>
<script type="text/javascript">
    var postLock = false;
    var uniKey = $('#input_hidden_unikey').val();
    var domain = $('#input_hidden_domain').val();
    $(document).ready(function () {
        var uno = getCookie('jmuc_uno');
        var uid = getCookie('jmuc_u');
        var token = getCookie('jmuc_token');
        var sign = getCookie('jmuc_s');
        var login = getCookie('jmuc_lgdomain');

        var name = $('#input_hidden_name').val();
        var flag = '${flag}';
        if (name.length > 0 || flag == 'post') {
            $('body').attr('style', 'padding-bottom:3.9rem;');
            $('.footer').attr('style', 'bottom:0;');
            $('#textarea_body').focus().val(name);
            $('#textarea_body').attr('contentEditable',true);
            mScroll('textarea_body');
        }

        $('#a_to_comment').on('click', function () {
            mScroll('textarea_body');
            $('.inp-num').html(300);
            $('#div_post').show();
            $('#div_post_next').show();
            $('#textarea_body').focus();
            $('#div_float_post').hide();
            $('body').attr('style', 'padding-bottom:3.9rem;');
            $('.footer').attr('style', 'bottom:0;');
        });
        $('#span_do_post').on('click', function () {
            if (uno == null || uid == null || token == null || sign == null || login == null || login == 'client') {
                textTips('请先保存您的内容，登录之后再回来~');
                return false;
            }

            var text = $('#textarea_body').val();
            var atName = $('#input_hidden_name').val();
            text = text.replace(atName, '');
            if (text == null || getStrlen(text) < 2) {
                textTips('请输入至少2个字符');
                return;
            }
            if (getStrlen(text) > 300) {
                textTips('不能超过300字符');
                return;
            }

            var body = {
                text: text,
                pic: ""
            }
            var oid = $('#input_hidden_oid').val();
            if (oid.length == 0) {
                oid = '0';
            }
            var pid = $('#input_hidden_pid').val();
            if (pid.length == 0) {
                pid = '0';
            }
            if (uniKey.length > 0 && domain.length > 0) {
                postComment(uniKey, domain, body, parseInt(oid), parseInt(pid));
            }
        });

        $('.cmnt-reply').on('click', function () {
            $('.inp-num').html(300);
            $('#div_post').show();
            $('#div_post_next').show();
            var name = '@' + $(this).attr('data-uname') + ':';
            $('#textarea_body').focus().val(name);
            $('#input_hidden_oid').val($(this).attr('data-oid'));
            $('#input_hidden_pid').val($(this).attr('data-pid'));
            $('#input_hidden_name').val(name);
            mScroll('textarea_body');
        });

        $('#div_more_list').on('click', function () {
            var pnum = $(this).attr('data-cp');
            var psize = $(this).attr('data-ps');
            var maxpage = $(this).attr('data-mp');
            if (parseInt(pnum) < parseInt(maxpage)) {
                getMCommentList(uniKey, domain, parseInt(pnum) + 1, psize);
            }
        });

        $('#textarea_body').on('input propertychange' || 'keyup', function () {
            numChange();
        });
    });

    function mScroll(id){
        $("html,body").stop(true);
        $("html,body").animate({
            scrollTop:0
        }, 0);
        $('.goTop').hide();
    }

    function numChange() {
        var text = $('#textarea_body').val();
        var atName = $('#input_hidden_name').val();
        text = text.replace(atName, '');

        //超出文字提示
        if (getStrlen(text) > 300) {
            $('#span_tips').show();
            $('#span_tips').html('已超出<b class="inp-num">' + (getStrlen(text) - 300) + '</b>个字符');
        } else if (getStrlen(text) > 290 && getStrlen(text) <= 300) {
            $('#span_tips').show();
            $('#span_tips').html('还可以输入<b class="inp-num">' + (300 - getStrlen(text)) + '</b>个字');
        } else {
            if ($('.inp-num') != null) {
                $('#span_tips').show();
                $('.inp-num').html(300 - getStrlen(text));
            }
        }
    }

    //提示3秒后关闭
    function textTips(msg) {
        $('#div_beyond').show();
        $('#div_beyond').html('<span>' + msg + '</span>');

        var ibTime = setInterval(function () {
            clearInterval(ibTime);
            $('#div_beyond').hide();
        }, 2000);
    }

    function openPostDiv(dom) {
        $('.inp-num').html(300);
        $('#div_post').show();
        $('#div_post_next').show();
        var name = '@' + $(dom).attr('data-uname') + ':';
        $('#textarea_body').focus().val(name);
        $('#input_hidden_oid').val($(dom).attr('data-oid'));
        $('#input_hidden_pid').val($(dom).attr('data-pid'));
        $('#input_hidden_name').val(name);
    }

    function getMCommentList(unikey, domain, pnum, psize) {
        $.ajax({
            url: "http://api.${DOMAIN}/jsoncomment/reply/mlist",
            type: "post",
            data: {unikey: unikey, domain: domain, jsonparam: "", pnum: pnum, psize: psize},
            dataType: "jsonp",
            jsonpCallback: "getmcommentlistcallback",
            success: function (req) {
                var resMsg = req[0];
                if (resMsg.rs == '1') {
                    var result = resMsg.result;
                    if (result == null || result == undefined) {
                        return;
                    }
                    var rows = result.rows;
                    if (rows != null && rows.length > 0) {
                        var html = '';
                        for (var i = 0; i < rows.length; i++) {
                            var atHtml = '';
                            if (rows[i].puser != null) {
                                atHtml += '<a href="javascript:void(0);">@' + rows[i].puser.name + ':</a>';
                            }

                            var commentHtml = '<div class="cmnt-box"><dl><dt><span class="fn-clear">' +
                                    '<a href="javascript:void(0);" class="userhead fn-left">' +
                                    '<img class="lazy" src="' + rows[i].user.icon + '" alt="" title="">' +
                                    '</a><font class="fn-left">' +
                                    '<a href="javascript:void(0);" class="userName">' + rows[i].user.name + '</a>' +
                                    '<code>' + rows[i].reply.post_date + '</code></font></span>' +
                                    '<cite class="cmnt-reply fn-right" data-pid="' + rows[i].reply.rid + '" data-oid="' + rows[i].reply.oid + '" data-uname="' + escapeXml(rows[i].user.name) + '" onclick="openPostDiv(this)">回复</cite>' +
                                    '</dt><dd><span class="border-b">' +
                                    atHtml + rows[i].reply.body.text +
                                    '</span></dd></dl></div>';
                            html += commentHtml;
                        }
                        $('div.cmnt-list').append(html);
                    }
                    var page = result.page;
                    if (page != null) {
                        if (page.curPage >= page.maxPage) {
                            $('#div_more_list').remove();
                        } else {
                            $('#div_more_list').attr('data-cp', page.curPage);
                            $('#div_more_list').attr('data-mp', page.maxPage);
                            $('#div_more_list').attr('data-ps', page.pageSize);
                        }
                    }
                }

            }
        });
    }

    function postComment(unikey, domain, body, oid, pid) {
        if (postLock) {
            return;
        }
        postLock = true;
        $.ajax({
            url: "http://api.${DOMAIN}/jsoncomment/reply/post",
            type: "post",
            data: {unikey: unikey, domain: domain, body: JSON.stringify(body), oid: oid, pid: pid},
            dataType: "jsonp",
            jsonpCallback: "postcallback",
            success: function (req) {
                var resMsg = req[0];
                if (resMsg.rs == '0') {
                    textTips(resMsg.msg);
                    postLock = false;
                    return;
                } else if (resMsg.rs == '-1001') {
                    textTips('请先保存您的内容，登录之后再回来~');
                    postLock = false;
                    return;
                } else if (resMsg.rs == '-10102') {
                    textTips('请先保存您的内容，登录之后再回来~');
                    postLock = false;
                    return;
                } else if (resMsg.rs == '-100') {
                    textTips('app不存在~');
                    postLock = false;
                    return;
                } else if (resMsg.rs == '-10104') {
                    textTips('用户不存在~');
                    postLock = false;
                    return;
                } else if (resMsg.rs == '-40011') {
                    textTips('评论不存在~');
                    postLock = false;
                    return;
                } else if (resMsg.rs == '-40012') {
                    textTips('评论不存在~');
                    postLock = false;
                    return;
                } else if (resMsg.rs == '-40000') {
                    textTips('您的请求缺少unikey参数~');
                    postLock = false;
                    return;
                } else if (resMsg.rs == '-40001') {
                    textTips('您的请求缺少domain参数~');
                    postLock = false;
                    return;
                } else if (resMsg.rs == '-40002') {
                    textTips('您的请求缺少jsonparam参数~');
                    postLock = false;
                    return;
                } else if (resMsg.rs == '-40003') {
                    textTips('您的请求中jsonparam参数格式错误~');
                    postLock = false;
                    return;
                } else if (resMsg.rs == '-40013') {
                    textTips('您的请求中domain参数错误~');
                    postLock = false;
                    return;
                } else if (resMsg.rs == '-40005') {
                    textTips('评论内容未填写~');
                    postLock = false;
                    return;
                } else if (resMsg.rs == '-40008') {
                    textTips('评论对象不存在~');
                    postLock = false;
                    return;
                } else if (resMsg.rs == '-40006') {
                    textTips('oid参数错误~');
                    postLock = false;
                    return;
                } else if (resMsg.rs == '-40009') {
                    textTips('主楼评论已删除~');
                    postLock = false;
                    return;
                } else if (resMsg.rs == '-40007') {
                    textTips('pid参数错误~');
                    postLock = false;
                    return;
                } else if (resMsg.rs == '-40010') {
                    textTips('上级回复已删除~');
                    postLock = false;
                    return;
                } else if (resMsg.rs == '-40016') {
                    textTips('您已经赞过了~');
                    postLock = false;
                    return;
                } else if (resMsg.rs == '-40017') {
                    textTips('内容含有敏感词：' + resMsg.result[0]);
                    postLock = false;
                    return;
                } else if (resMsg.rs == '-40018') {
                    textTips('评论不存在~');
                    postLock = false;
                    return;
                } else if (resMsg.rs == '-40019') {
                    textTips('您已被禁言~');
                    postLock = false;
                    return;
                } else if (resMsg.rs == '-40020') {
                    textTips('一分钟内不能重复发送相同的内容~');
                    postLock = false;
                    return;
                } else if (resMsg.rs == '-1') {
                    textTips('请先保存您的内容，登录之后再回来~');
                    postLock = false;
                    return;
                } else if (resMsg.rs == '1') {
                    var result = resMsg.result;
                    if (result == null) {
                        return;
                    }
                    var oid = result.reply.oid;
                    var rid = result.reply.rid;
                    var atHtml = '';
                    if (result.puser != null) {
                        atHtml += '<a href="javascript:void(0);">@' + result.puser.name + ':</a>';
                    }

                    var html = '<div class="cmnt-box"><dl><dt><span class="fn-clear">' +
                            '<a href="javascript:void(0);" class="userhead fn-left">' +
                            '<img class="lazy" src="' + result.user.icon + '" alt="" title="">' +
                            '</a><font class="fn-left">' +
                            '<a href="javascript:void(0);" class="userName">' + result.user.name + '</a>' +
                            '<code>' + result.reply.post_date + '</code></font></span>' +
                            '<cite class="cmnt-reply fn-right" data-pid="' + rid + '" data-oid="' + oid + '" data-uname="' + escapeXml(result.user.name) + '" onclick="openPostDiv(this)">回复</cite>' +
                            '</dt><dd><span class="border-b">' +
                            atHtml + result.reply.body.text +
                            '</span></dd></dl></div>';
                    if($('div.cmnt-list').length == 0){
                        $('div.no-comment').html('').removeClass('no-comment').addClass('cmnt-list');
                    }

                    if ($('div.cmnt-list').find('div.cmnt-box').length == 0) {
                        $('div.cmnt-list').append(html);
                    } else {
                        $('div.cmnt-list').find('div.cmnt-box:first').before(html);
                    }
                    $('#textarea_body').val('');
                    $('#div_post').hide();
                    $('#div_post_next').hide();
                    $('#div_float_post').show();
                    $('body').attr('style', '');
                    $('.footer').attr('style', '');
                    $('#input_hidden_oid').val('');
                    $('#input_hidden_pid').val('');
                    $('#input_hidden_name').val('');
                    postLock = false;
                    return;
                } else {
                    textTips(resMsg.msg);
                    postLock = false;
                    return;
                }
            },
            error: function () {
                alert('获取失败，请刷新');
                postLock = false;
                return;
            }
        });
    }

    function getCookie(objName) {
        var arrStr = document.cookie.split("; ");
        for (var i = 0; i < arrStr.length; i++) {
            var temp = arrStr[i].split("=");
            if (temp[0] == objName && temp[1] != '\'\'' && temp[1] != "\"\"") {
                return unescape(temp[1]);
            }
        }
        return null;
    }

    function getStrlen(str) {
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

    function escapeXml(string) {
        return string.replace(/[<>&'"]/g, function (c) {
            switch (c) {
                case '<': return '&lt;';
                case '>': return '&gt;';
                case '&': return '&amp;';
                case '\'': return '&apos;';
                case '"': return '&quot;';
            }
        });
    }
</script>
</body>
</html>