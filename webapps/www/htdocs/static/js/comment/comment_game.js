var uno = getCookie('jmuc_uno');
var uid = getCookie('jmuc_u');
var token = getCookie('jmuc_token');
var sign = getCookie('jmuc_s');
var login = getCookie('jmuc_lgdomain');
var pid = getCookie('jmuc_pid');


//uno uid token sign login pid

var hostname = window.location.hostname;
var wwwHost = 'http://www.joyme.' + hostname.substring(hostname.lastIndexOf('.') + 1, hostname.length) + '/';
var apiHost = 'http://api.joyme.' + hostname.substring(hostname.lastIndexOf('.') + 1, hostname.length) + '/';
var passportHost = 'http://passport.joyme.' + hostname.substring(hostname.lastIndexOf('.') + 1, hostname.length) + '/';

var isReplyBlock = false;
var isGetReplyListBlock = false;
var isAgreeBlock = false;

var bs = {
    versions: function () {
        var u = navigator.userAgent, app = navigator.appVersion;
        return {//移动终端浏览器版本信息
            trident: u.indexOf('Trident') > -1, //IE内核
            presto: u.indexOf('Presto') > -1, //opera内核
            webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
            gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
            mobile: !!u.match(/AppleWebKit.*Mobile.*/) || !!u.match(/AppleWebKit/), //是否为移动终端
            ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
            android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或者uc浏览器
            iPhone: u.indexOf('iPhone') > -1, //是否为iPhone或者QQHD浏览器
            iPad: u.indexOf('iPad') > -1, //是否iPad
            webApp: u.indexOf('Safari') == -1 //是否web应该程序，没有头部与底部
        };
    }(),
    language: (navigator.browserLanguage || navigator.language).toLowerCase()
}

var uri = window.location.href;
if (uri.indexOf("?") > 0) {
    uri = uri.substring(0, uri.indexOf("?"));
}
if (uri.indexOf("#") > 0) {
    uri = uri.substring(0, uri.indexOf("#"));
}

var domain = 13;

var uniKey = '';
var path = window.location.pathname;
if (uri.indexOf('http://') == 0 || uri.indexOf('https://') == 0) {
    if (endWith(path, '/index.html')) {  //首页
        uniKey = path.substring(0, path.indexOf('index.html'));
    } else if (endWith(path, '.html')) {   //数字ID页
        var number = '';
        var arr = path.split('/');
        for (var i = 0; i < arr.length; i++) {
            var item = arr[i];
            if (item.indexOf(".html") > 0) {
                item = item.replace(".html", "");
                var position = item.indexOf("_");
                if (position >= 0) {
                    item = item.substring(0, position);
                }
            }

            if ($.isNumeric(item)) {
                number += item;
            }
        }
        if (number.length > 8) {
            uniKey = number.substring(8, number.length);
        } else {
            uniKey = path;
        }
    } else if (endWith(path, '/')) {
        uniKey = path;
    } else {
        uniKey = path + '/';
    }
}

var title = $('title').eq(0).text();
if (title.indexOf("_") > 0) {
    title = title.substring(0, title.indexOf("_"));
}

if (uniKey.length > 0) {
    if (bs.versions.android || bs.versions.iPhone || bs.versions.iPad) {

        $(document).ready(function () {
            $('body').append('<input type="hidden" id="input_oid" value="0"/><input type="hidden" id="input_pid" value="0"/>');
            var jsonParam = {
                title: title,
                pic: "",
                description: "",
                uri: uri
            }
            //拉取评论列表
            getMCommentList(uniKey, domain, jsonParam, 1, 5, (uid == null || uid == undefined) ? 0 : uid);

            //点击 我要评论 判断登录
            $('span.comment').on('touchstart', function () {
                if (uno == null || uid == null) {
                    $('.new-area').blur();
                    $('.pl-box').hide();
                    $('.mask').hide();
                    loginDiv();

                    return false;
                }
            });
            //监听输入的字符数
            $('.new-area').on('input propertychange' || 'keyup', function () {
                numChange();
            });
            //取消 清空框里面的内容
            $('.cancel').on('touchstart', function () {
                $('.new-area').val('');
                $('.cmnt-num').html('还可以输入<b class="inp-num">140</b>个字');
                $('.cmnt-num').hide();

            });
            //发布
            $('.fabu').on('touchstart', function () {
                if (uno == null || uid == null) {
                    loginDiv();
                    return false;
                }

                var oid = $('#input_oid').val();
                var pid = $('#input_pid').val();
                var text = $('.new-area').val();

                text = Trim(text, 'g');
                console.log(getStrlen(text));

                if (getStrlen(text) <= 0) {
                    $('.cmnt-beyond').html('<span>评论内容不能为空</span>');
                    $('.cmnt-beyond').show();
                    setTimeout(function () {
                        $('.cmnt-beyond').hide()
                    }, 2000);
                    return;
                }
                if (getStrlen(text) > 140) {
                    $('.cmnt-beyond').html('<span>评论内容不能超过140字符</span>');
                    $('.cmnt-beyond').show();
                    setTimeout(function () {
                        $('.cmnt-beyond').hide()
                    }, 2000);
                    return;
                }
                var body = {
                    text: text,
                    pic: ""
                }
                postMComment(uniKey, domain, body, oid, pid);
            });

        });
    } else {
        $(document).ready(function () {
            initPostMask();
            var jsonParam = {
                title: title,
                pic: "",
                description: "",
                uri: uri
            }
            getCommentList(uniKey, domain, JSON.stringify(jsonParam), 1, 10);
        });
    }
}

//M跳转登录提示框
function loginRedirect() {
    $('#wp_comment_alert').attr('class', 'wp_comment_alert active');
    $('#wp_comment_tips').text('您还未登录，点确定跳转至登录页');
    $('#wp_confirm').on('click', function () {
        loginDiv();
    });
}

function numChange() {
    var text = $('.new-area').val();
    //超出文字提示
    if (getStrlen(text) > 140) {
        $('.cmnt-num').show();
        $('.cmnt-num').html('已超出<b class="inp-num">' + (getStrlen(text) - 140) + '</b>个字符');
    } else if (getStrlen(text) > 130 && getStrlen(text) <= 140) {
        $('.cmnt-num').show();
        $('.cmnt-num').html('还可以输入<b class="inp-num">' + (140 - getStrlen(text)) + '</b>个字');
    } else {
        if ($('.inp-num') != null) {
            $('.cmnt-num').show();
            $('.inp-num').html(140 - getStrlen(text));
        }
    }
}

function Trim(str, is_global) {
    var result;
    result = str.replace(/(^\s+)|(\s+$)/g, "");
    return result;
}

// PC 翻页
function getCommentListPage(cp, psize) {
    var jsonParam = {
        title: title,
        pic: "",
        description: "",
        uri: uri
    }
    getCommentList(uniKey, domain, JSON.stringify(jsonParam), cp, psize);
}

function endWith(str, flag) {
    if (flag == null || flag == "" || str.length == 0 || flag.length > str.length)
        return false;
    if (str.substring(str.length - flag.length, str.length) == flag)
        return true;
    else
        return false;
    return true;
}

function startWith(str, flag) {
    if (flag == null || flag == "" || str.length == 0 || flag.length > str.length)
        return false;
    if (str.substring(0, flag.length) == flag)
        return true;
    else
        return false;
    return true;
}

function focusin(dom) {
    var val = $(dom).val();
    if (val == '我来说两句…') {
        $(dom).val('').css('color', '#333');
    }
}

function focusout(dom) {
    var val = $(dom).val();
    if (val == null || val == '') {
        $(dom).val('我来说两句…').css('color', '#ccc');
    }
}

//删除
function removeClick(dom, type) {
    var rid = $(dom).attr('data-rid');
    var oid = $(dom).attr('data-oid');
    if (oid == null || oid.length <= 0) {
        oid = 0;
    }

    if (type == '1') {
        if (uno == null || uid == null || token == null || login == null || sign == null || login == 'client') {
            loginDiv();
            return;
        }
        if (confirm('确定要删除吗？')) {
            removeComment(uniKey, domain, rid, oid);
        }

    } else if (type == '2') {
        if (uno == null || uid == null || token == null || login == null || sign == null || login == 'client') {
            loginDiv();
            return;
        }
        $('#wp_comment_alert').attr('class', 'wp_comment_alert active');
        $('#wp_comment_tips').text('确定删除评论吗？');
        $('#wp_confirm').on('click', function () {
            removeMComment(uniKey, domain, rid, oid);
        });
    }
}

//pc 楼中楼回复
function reCommentSubmit(dom) {
    if (uno == null || uid == null || token == null || login == null || sign == null || login == 'client') {
        loginDiv();
        return;
    }

    var oid = $(dom).attr('data-oid');
    var text = $('#textarea_recomment_body_' + oid).val();
    var submitReComment = $('#post_recomment_area_' + oid).find('a[name=submit_recomment]');
    if (submitReComment.length > 0) {
        var pname = submitReComment.attr('data-nick');
        text = text.replace('@' + pname + ":", '');
    }
    if (text == null || text.length == 0 || text == '我来说两句…') {
        $('#reply_error').html('评论内容不能为空');
        return false;
    }
    if (getStrlen(text) > 300) {
        $('#reply_error').html('评论内容长度不能超过300字符!');
        return false;
    }

    //全角
    //text = ToDBC(text);

    var body = {
        text: text,
        pic: ""
    }
    var pid = $(dom).attr('data-pid');
    if (pid == null || pid.length <= 0) {
        pid = 0;
    }
    postSubComment(uniKey, domain, body, oid, pid, $(dom));
}


function parentMask(dom) {
    var oid = $(dom).attr('data-oid');
    var pid = $(dom).attr('data-pid');
    var nickname = $(dom).attr('data-nick');
    var postCommentArea = $('#post_recomment_area_' + oid);
    var replyMaskLink = postCommentArea.find('a[name=replypost_mask]');
    var submitReComment = postCommentArea.find('a[name=submit_recomment]');
    submitReComment.attr("data-pid", pid);
    submitReComment.attr("data-nick", nickname);
    $('#textarea_recomment_body_' + oid).val('@' + nickname + ':');
    replyMaskLink.click();
}

//回复
function replyMask(dom) {
    $(dom).hide();
    var obj = $(dom).next();
    obj.show();
    var textAreaObj = obj.find('textarea[id^=textarea_recomment_body_]');
    var body = textAreaObj.val();
    textAreaObj.val("").focus().val(body);
    textAreaObj.AutoHeight({maxHeight: 200});
}

//回复遮罩
function toggle(dom) {
    if ($(dom).hasClass('putaway')) {
        $(dom).parent().next().fadeOut();
        replyNum = parseInt($(dom).attr('data-replynum'));
        var html = '回复';
        if (replyNum > 0) {
            html += '(' + replyNum + ')'
        }
        $(dom).html(html).removeClass();
    } else {
        $(dom).parent().next().fadeIn();
        $(dom).html('收起回复').attr('class', 'putaway');
    }
}

//发表评论
function commentSubmit() {
    if (uno == null || uid == null || token == null || login == null || sign == null || login == 'client') {
        loginDiv();
        return;
    }

    var text = $('#textarea_body').val();
    if (getStrlen(text) < 0 || text == '我来说两句…') {
        $('#reply_error').html('评论内容不能为空');
        return false;
    }
    if (getStrlen(text) > 300) {
        $('#reply_error').html('评论内容长度不能超过300字符!');
        return false;
    }
    //全角
    //text = ToDBC(text);

    var body = {
        text: text,
        pic: ""
    }
    postComment(uniKey, domain, body, 0, 0);
}

//点赞
function agreeClick(dom) {
    if (uno == null || uid == null || token == null || login == null || sign == null || login == 'client') {
        loginDiv();
        return;
    }

    var rid = $(dom).attr('data-commentid');
    agreeComment(uniKey, domain, rid);
}

//M点赞
function agreeMClick(dom) {
    $('#wp_confirm').on('click', function () {
        loginDiv();
    });
    if (uno == null || uid == null || token == null || login == null || sign == null || login == 'client') {
        $('#wp_comment_alert').attr('class', 'wp_comment_alert active');
        $('#wp_comment_tips').text('请先保存您的内容，登录之后再回来~');
        return;
    }

    var rid = $(dom).attr('data-commentid');
    agreeComment(uniKey, domain, rid);
}
//PC 拉取评论列表
function getCommentList(unikey, domain, jsonparam, cp, psize) {
    if (isGetReplyListBlock) {
        return;
    }
    isGetReplyListBlock = true;
    $.ajax({
        url: apiHost + "jsoncomment/reply/query",
        type: "post",
        data: {unikey: unikey, domain: domain, jsonparam: jsonparam, pnum: cp, psize: psize},
        dataType: "jsonp",
        jsonpCallback: "getcommentlistcallback",
        success: function (req) {
            var resMsg = req[0];
            if (resMsg.rs == '1') {
                var result = resMsg.result;
                if (result == null) {
                    $('#comment_list_area').html('<p class="null-pl">目前没有评论，欢迎你发表评论</p>');
                    return;
                }

                var replyNum = '<span>' + result.comment_sum + '条评论</span>'
                //$('#reply_num').html(replyNum);

                //没有评论
                if (result.mainreplys == null || result.mainreplys.rows == null || result.mainreplys.rows.length == 0) {
                    if (result.mainreplys.page != null && result.mainreplys.page.maxPage > 1) {
                        //$('#comment_list_area').html('<p class="null-pl">当前页的评论已经被删除~</p><div class="more-comment"><a href="' + wwwHost + 'comment/reply/page?unikey=' + unikey + '&domain=' + domain + '&pnum=1&psize=10">更多评论&gt;&gt;</a></div>');
                        $('#comment_list_area').html('<p class="null-pl">当前页的评论已经被删除~</p>');
                    } else {
                        $('#comment_list_area').html('<p class="null-pl">目前没有评论，欢迎你发表评论</p>');
                    }
                } else {
                    //有评论
                    var html = '';
                    for (var i = 0; i < result.mainreplys.rows.length; i++) {
                        html += getCommentListCallBack(result.mainreplys.rows[i]);
                    }

                    //分页的html
                    if (result.mainreplys.page != null && result.mainreplys.page.maxPage > 1) {
                        var pageHtml = '<div class="joyme-paging-box">' +
                            '<div class="pagecon">' +
                            '<div class="page">' +
                            '<ul>';

                        if (result.mainreplys.page.curPage > 1) {
                            pageHtml += '<li id="first_page" onclick="getCommentListPage(1, 10)">首页</li>'
                            pageHtml += '<li><a onclick="getCommentListPage(' + (result.mainreplys.page.curPage - 1) + ', 10)" href="javascript:void(0);">上一页</a></li>';
                        }
                        if (result.mainreplys.page.curPage <= 4) {
                            if (result.mainreplys.page.maxPage <= 4) {
                                for (var cp = 1; cp <= result.mainreplys.page.maxPage; cp++) {
                                    if (cp == result.mainreplys.page.curPage) {
                                        pageHtml += '<li class="thisclass">' + cp + '</li>';
                                    } else {
                                        pageHtml += '<li><a href="javascript:void(0);" onclick="getCommentListPage(' + cp + ', 10)">' + cp + '</a></li>'
                                    }
                                }
                            } else {
                                for (var cp = 1; cp <= (result.mainreplys.page.maxPage > 7 ? 7 : result.mainreplys.page.maxPage); cp++) {
                                    if (cp == result.mainreplys.page.curPage) {
                                        pageHtml += '<li class="thisclass">' + cp + '</li>';
                                    } else {
                                        pageHtml += '<li><a href="javascript:void(0);" onclick="getCommentListPage(' + cp + ', 10)">' + cp + '</a></li>'
                                    }
                                }
                                /*                                if (result.mainreplys.page.curPage >= 6) {
                                 if (result.mainreplys.page.curPage > 6) {
                                 pageHtml += '<li><a href="javascript:void(0);" onclick="getCommentListPage(' + (result.mainreplys.page.curPage + 1) + ', 10)">' + (result.mainreplys.page.curPage + 1) + '</a></li>'
                                 }
                                 if (result.mainreplys.page.maxPage >= (result.mainreplys.page.curPage + 2)) {
                                 pageHtml += '<li><a href="javascript:void(0);" onclick="getCommentListPage(' + (result.mainreplys.page.curPage + 2) + ', 10)">' + (result.mainreplys.page.curPage + 2) + '</a></li>'
                                 }
                                 }*/
                            }
                        } else {

                            if (result.mainreplys.page.curPage < result.mainreplys.page.maxPage - 2) {

                                for (var cp = (result.mainreplys.page.curPage - 3); cp <= (result.mainreplys.page.curPage + 3); cp++) {
                                    if (cp == result.mainreplys.page.curPage) {
                                        pageHtml += '<li class="thisclass">' + cp + '</li>';
                                    } else {
                                        pageHtml += '<li><a href="javascript:void(0);" onclick="getCommentListPage(' + cp + ', 10)">' + cp + '</a></li>'
                                    }
                                }

                            } else {
                                if (result.mainreplys.page.maxPage > 6) {
                                    for (var cp = (result.mainreplys.page.maxPage - 6); cp <= result.mainreplys.page.maxPage; cp++) {
                                        if (cp == result.mainreplys.page.curPage) {
                                            pageHtml += '<li class="thisclass">' + cp + '</li>';
                                        } else {
                                            pageHtml += '<li><a href="javascript:void(0);" onclick="getCommentListPage(' + cp + ', 10)">' + cp + '</a></li>'
                                        }
                                    }
                                } else {
                                    for (var cp = 1; cp <= result.mainreplys.page.maxPage; cp++) {
                                        if (cp == result.mainreplys.page.curPage) {
                                            pageHtml += '<li class="thisclass">' + cp + '</li>';
                                        } else {
                                            pageHtml += '<li><a href="javascript:void(0);" onclick="getCommentListPage(' + cp + ', 10)">' + cp + '</a></li>'
                                        }
                                    }
                                }

                            }
                        }

                        if (result.mainreplys.page.curPage < result.mainreplys.page.maxPage) {
                            pageHtml += '<li><a href="javascript:void(0);" onclick="getCommentListPage(' + (result.mainreplys.page.curPage + 1) + ', 10)">下一页</a></li>';
                            pageHtml += '<li id="last_page"><a href="javascript:void(0);" onclick="getCommentListPage(' + result.mainreplys.page.maxPage + ', 10)">末页</a></li>';
                        }

                        pageHtml += '<li><span class="pageinfo">共<strong>' + result.mainreplys.page.maxPage + '</strong>页<strong>' +
                            '</ul>' +
                            '</div>' +
                            '</div>' +
                            '</div>';
                        html += pageHtml;
                    }
                    $('#comment_list_area').html(html);
                }
            } else {
                $('#reply_error').html(resMsg.msg);
                return;
            }
        },
        error: function () {
            isGetReplyListBlock = false;
            return;
        },
        complete: function () {
            isGetReplyListBlock = false;
            return;
        }
    });
}

//M 评论列表
function getMCommentList(unikey, domain, jsonparam, cp, psize, uid) {
    if (isGetReplyListBlock) {
        return;
    }
    isGetReplyListBlock = true;
    $.ajax({
        url: apiHost + "jsoncomment/reply/query",
        type: "post",
        data: {unikey: unikey, domain: domain, jsonparam: JSON.stringify(jsonparam), pnum: cp, psize: psize, uid: uid},
        dataType: "jsonp",
        //jsonpCallback: "getmcommentlistcallback",
        success: function (req) {
            var resMsg = req[0];
            var result = resMsg.result;
            var commentListHtml = '';

            if (resMsg.rs == '1') {
                //无 评论
                if (result == null || result.mainreplys == null || result.mainreplys.rows == null || result.mainreplys.rows.length == 0) {
                    commentListHtml = '<p id="no-reply" class="null-pl">目前没有评论，欢迎你发表评论</p>';
                } else {
                    //有 评论
                    for (var i = 0; i < result.mainreplys.rows.length; i++) {
                        commentListHtml += getMCommentListCallback(result.mainreplys.rows[i]);
                    }
                }
            } else {
                commentListHtml = '<p class="null-pl">' + resMsg.msg + '</p>';
            }

            //加载更多
            var moreHtml = '';

            if (result.mainreplys.page != null && result.mainreplys.page.maxPage > 1) {
                moreHtml = '<div class="more-load"><a href="javascript:void(0);" onclick="getMCommentListMore(' + (result.mainreplys.page.curPage + 1) + ', 5)">查看更多...</a></div>';
            }

            //用户 登录状态
            var userHtml = '';
            if (result.user != null) {
                userHtml += '<cite class="user">' + result.user.name + '</cite><cite class="quit"><a href="' + passportHost + 'auth/logout">退出</a></cite>';
            } else {
                userHtml += '<cite class="user"></cite><cite class="quit"><a href="javascript:void(0);" onclick="loginDiv();">登录</a></cite>';
            }

            var comment_sum = (result.comment_sum == null || result.comment_sum == 0) ? "<i>评论</i>评论" : "<i>评论</i>" + result.comment_sum;
            $('#comment_sum').html(comment_sum);
            //评论列表的 html
            var html = '<div id="comment_box" class="tj-pl pag-hor-20">' +
                '<div class="fn-clear tit-pl">' +
                '<span class="count-pl fn-left">全部评论&nbsp;<code>(' + result.comment_sum + ')</code></span>' +
                '<span class="user-quit fn-clear">' +
                userHtml +
                '</span>' +
                '</div>' +
                '<div class="cmnt-list" id="cmnt-list">' +
                commentListHtml +
                '</div>' +
                moreHtml +
                '</div>';
            //$('#comment_box').html('');
            $('#comment_area').html(html);
        },
        error: function () {
            isGetReplyListBlock = false;
            return;
        },
        complete: function () {
            isGetReplyListBlock = false;
            return;
        }
    });
}

//M 加载更多
function getMCommentListMore(cp, psize) {
    var jsonParam = {
        title: title,
        pic: "",
        description: "",
        uri: uri
    }
    $.ajax({
        url: apiHost + "jsoncomment/reply/query",
        type: "post",
        data: {unikey: uniKey, domain: domain, jsonparam: JSON.stringify(jsonParam), pnum: cp, psize: psize},
        dataType: "jsonp",
        //jsonpCallback: "getmcommentlistcallback",
        success: function (req) {
            var resMsg = req[0];
            var commentListHtml = '';
            if (resMsg.rs == '1') {
                var result = resMsg.result;
                if (result != null && result.mainreplys != null && result.mainreplys.rows != null && result.mainreplys.rows.length > 0) {
                    for (var i = 0; i < result.mainreplys.rows.length; i++) {
                        commentListHtml += getMCommentListCallback(result.mainreplys.rows[i]);
                    }
                }
            }
            $('.cmnt-list').append(commentListHtml);

            var moreHtml = '';
            if (result.mainreplys.page != null && result.mainreplys.page.maxPage > result.mainreplys.page.curPage) {
                moreHtml = '<a href="javascript:void(0);" onclick="getMCommentListMore(' + (result.mainreplys.page.curPage + 1) + ', 5)">查看更多...</a>';
            }
            $('.more-load').html(moreHtml);
        }
    });
}

//M 评论框评论
function postMComment(unikey, domain, body, oid, pid) {
    if (isReplyBlock) {
        return;
    }
    isReplyBlock = true;
    var jsonParam = {
        title: title,
        pic: "",
        description: "",
        uri: uri
    }
    $.ajax({
        url: apiHost + "jsoncomment/reply/post",
        type: "post",
        data: {unikey: unikey, domain: domain, body: JSON.stringify(body), oid: oid, pid: pid},
        dataType: "jsonp",
        //jsonpCallback: "postcallback",
        success: function (req) {
            var resMsg = req[0];
            if (resMsg.rs == '0') {
                $('.cmnt-beyond').html('<span>' + resMsg.msg + '</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-1001') {
                $('.cmnt-beyond').html('<span>请先保存您的内容，登录之后再回来~</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-10102') {
                $('.cmnt-beyond').html('<span>请先保存您的内容，登录之后再回来~</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-100') {
                $('.cmnt-beyond').html('<span>app不存在~</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-10104') {
                $('.cmnt-beyond').html('<span>用户不存在~</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-40011') {
                $('.cmnt-beyond').html('<span>评论不存在~</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-40012') {
                $('.cmnt-beyond').html('<span>评论不存在~</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-40000') {
                $('.cmnt-beyond').html('<span>您的请求缺少unikey参数</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-40001') {
                $('.cmnt-beyond').html('<span>您的请求缺少domain参数</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-40002') {
                $('.cmnt-beyond').html('<span>您的请求缺少jsonparam参数</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-40003') {
                $('.cmnt-beyond').html('<span>您的请求中jsonparam参数格式错误</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-40013') {
                $('.cmnt-beyond').html('<span>您的请求中domain参数错误</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-40005') {
                $('.cmnt-beyond').html('<span>评论内容未填写</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-40008') {
                $('.cmnt-beyond').html('<span>评论对象不存在</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-40006') {
                $('.cmnt-beyond').html('<span>oid参数错误~</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-40009') {
                $('.cmnt-beyond').html('<span>主楼评论已删除</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-40007') {
                $('.cmnt-beyond').html('<span>pid参数错误~</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-40010') {
                $('.cmnt-beyond').html('<span>上级回复已删除~</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-40016') {
                $('.cmnt-beyond').html('<span>您已经赞过了~</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-40017') {
                $('.cmnt-beyond').html('<span>内容含有敏感词：' + resMsg.result[0] + '</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-40018') {
                $('.cmnt-beyond').html('<span>评论不存在~</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-40019') {
                $('.cmnt-beyond').html('<span>您已被禁言~</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-40020') {
                $('.cmnt-beyond').html('<span>一分钟内不能重复发送相同的内容~</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-40022') {
                $('.cmnt-beyond').html('<span>两次评论间隔不能少于15秒，请稍后再试~</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-1') {
                $('.cmnt-beyond').html('<span>请先保存您的内容，登录之后再回来~</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '1') {
                var result = resMsg.result;
                if (result == null) {
                    return;
                }
                var numHtml = $('.count-pl code').text();
                var numStr = numHtml.replace("(", '').replace(")", "");
                var num = parseInt(numStr) + 1;
                $('.count-pl code').text("(" + num + ")");

                //底部评论数加1
                var cs = $('#comment_sum').text().replace('评论', '');
                var num = 0;
                if (cs != '评论') {
                    num = parseInt(cs) + 1;
                }
                $('#comment_sum').html('<i>评论</i>' + num);

                //$('#cmnt-list').html('');
                getMCommentList(uniKey, domain, jsonParam, 1, 5, (uid == null || uid == undefined) ? 0 : uid);

                $('#input_oid').val(0);
                $('#input_pid').val(0);
                $('.new-area').val('');

                $('.new-area').blur();
                $('.pl-box').hide();
                $('.mask').hide()
                $('.cmnt-num').html('还可以输入<b class="inp-num">140</b>个字');
                $('.cmnt-num').hide();

                $('#wp_comment_alert_no_button').attr('class', 'wp_comment_alert active');
                $('#wp_comment_tips_no_button').text('发布成功');
                setTimeout(function () {
                    $('#wp_comment_alert_no_button').attr('class', 'wp_comment_alert');
                }, 1500);

                var noReply = $('#no-reply').text();
                if (getStrlen(noReply) > 0) {
                    $('#no-reply').remove();
                }
                scrollOffset();
            } else {
                $('.cmnt-beyond').html('<span>' + resMsg.msg + '</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            }
        },
        error: function () {
            $('.cmnt-beyond').html('<span>获取失败，请刷新</span>');
            $('.cmnt-beyond').show();
            isReplyBlock = false;
            setTimeout(function () {
                $('.cmnt-beyond').hide()
            }, 2000);
            return;
        },
        complete: function () {
            isReplyBlock = false;
            return;
        }
    });
}

function scrollOffset() {

//    var newHeight=$('#comment_box').offset().top;
//	$("#wrapper").scrollTop(newHeight);
    location.href = "#_pinglun";      // firstAnchor为锚点名称
    window.location.hash = "#_pinglun"; // firstAnchor为锚点名称

}

//PC 评论框 评论
function postComment(unikey, domain, body, oid, pid) {
    $.ajax({
        url: apiHost + "jsoncomment/reply/post",
        type: "post",
        data: {unikey: unikey, domain: domain, body: JSON.stringify(body), oid: oid, pid: pid},
        dataType: "jsonp",
        jsonpCallback: "postcallback",
        success: function (req) {
            var resMsg = req[0];
            if (resMsg.rs == '0') {
                $('#reply_error').html(resMsg.msg);
                return;
            } else if (resMsg.rs == '-1001') {
                loginDiv();
                return;
            } else if (resMsg.rs == '-10102') {
                loginDiv();
                return;
            } else if (resMsg.rs == '-100') {
                $('#reply_error').html('app不存在~');
                return;
            } else if (resMsg.rs == '-10104') {
                $('#reply_error').html('用户不存在~');
                return;
            } else if (resMsg.rs == '-40011') {
                $('#reply_error').html('评论不存在~');
                return;
            } else if (resMsg.rs == '-40012') {
                $('#reply_error').html('评论不存在~');
                return;
            } else if (resMsg.rs == '-40000') {
                $('#reply_error').html('您的请求缺少unikey参数~');
                return;
            } else if (resMsg.rs == '-40001') {
                $('#reply_error').html('您的请求缺少domain参数~');
                return;
            } else if (resMsg.rs == '-40002') {
                $('#reply_error').html('您的请求缺少jsonparam参数~');
                return;
            } else if (resMsg.rs == '-40003') {
                $('#reply_error').html('您的请求中jsonparam参数格式错误~');
                return;
            } else if (resMsg.rs == '-40013') {
                $('#reply_error').html('您的请求中domain参数错误~');
                return;
            } else if (resMsg.rs == '-40005') {
                $('#reply_error').html('评论内容未填写~');
                return;
            } else if (resMsg.rs == '-40008') {
                $('#reply_error').html('评论对象不存在~');
                return;
            } else if (resMsg.rs == '-40006') {
                $('#reply_error').html('oid参数错误~');
                return;
            } else if (resMsg.rs == '-40009') {
                $('#reply_error').html('主楼评论已删除~');
                return;
            } else if (resMsg.rs == '-40007') {
                $('#reply_error').html('pid参数错误~');
                return;
            } else if (resMsg.rs == '-40010') {
                $('#reply_error').html('上级回复已删除~');
                return;
            } else if (resMsg.rs == '-40016') {
                $('#reply_error').html('您已经赞过了~');
                return;
            } else if (resMsg.rs == '-40017') {
                $('#reply_error').html('内容含有敏感词：' + resMsg.result[0]);
                return;
            } else if (resMsg.rs == '-40018') {
                $('#reply_error').html('评论不存在~');
                return;
            } else if (resMsg.rs == '-40019') {
                $('#reply_error').html('您已被禁言~');
                return;
            } else if (resMsg.rs == '-40020') {
                $('#reply_error').html('一分钟内不能重复发送相同的内容~');
                return;
            } else if (resMsg.rs == '-40022') {
                $('#reply_error').html('两次评论间隔不能少于15秒，请稍后再试~');
                return;
            } else if (resMsg.rs == '-1') {
                loginDiv();
                return;
            } else if (resMsg.rs == '1') {
                var result = resMsg.result;
                if (result == null) {
                    return;
                }

                var oid = result.reply.oid;
                var rid = result.reply.rid;
                if (oid == 0) {
                    var spanHtml = '条评论';
                    var numHtml = $('#reply_num').text();
                    var numStr = numHtml.replace(spanHtml, '');
                    var num = parseInt(numStr) + 1;
                    // $('#reply_num').html('<span>' + num + spanHtml + '</span>');
                    $('#cont_cmt_list_' + rid).remove();
                } else {
                    var numStr = $('#togglechildrenreply_area_' + oid).attr('data-replynum');
                    $('#togglechildrenreply_area_' + oid).attr('data-replynum', parseInt(numStr) + 1);
                    $('#cont_recmt_list_' + rid).remove();
                }

                var commentHtml = '<div id="cont_cmt_list_' + result.reply.rid + '" class="area blogopinion clearfix ">' +
                    '<dl>' +
                    '<dt class="personface">' +
                    '<a title="' + result.user.name + '" name="atLink" href="http://uc.' + joyconfig.DOMAIN + '/usercenter/page?pid=' + result.user.pid + '" target="_blank">' +
                    '<img width="58" height="58" class="user" src="' + result.user.icon + '"></a>' +
                    '</dt>' +
                    '<dd class="textcon discuss_building">' +
                    '<a title="' + result.user.name + '" class="author" name="atLink" target="_blank" href="http://uc.' + joyconfig.DOMAIN + '/usercenter/page?pid=' + result.user.pid + '">' + result.user.name + '</a><code>' + result.reply.floor_num + '楼</code>' +
                    '<p>' + result.reply.body.text + '</p>' +
                    '<div class="discuss_bdfoot">' + result.reply.post_date + '&nbsp;' +
                    '<a href="javascript:void(0);" id="agreelink_' + result.reply.rid + '" data-commentid="' + result.reply.rid + '" class="dianzan" onclick="agreeClick(this)"></a>' +
                    '<span id="agreenum_' + result.reply.rid + '">' +
                    '<a href="javascript:void(0);" name="agree_num" data-commentid="' + result.reply.rid + '" onclick="agreeClick(this)"></a>'
                if (uno != null && uid != null && token != null && sign != null && login != null && login != 'client' && uno == result.user.uno && uid == result.user.uid) {
                    commentHtml += '</span><a onclick="removeClick(this,1)" name="remove_comment" href="javascript:void(0);" data-oid="0" data-rid="' + result.reply.rid + '">删除</a>'
                }
                commentHtml += '<a name="togglechildrenreply_area" href="javascript:void(0);" onclick="toggle(this)" id="togglechildrenreply_area_' + result.reply.rid + '" data-replynum="0">回复</a>' +
                    '</div>' +
                    '<div class="discuss_bd_list discuss_border" style="display:none">' +
                    '<div id="children_reply_list_' + result.reply.rid + '"></div>' +
                    '<div id="post_recomment_area_' + result.reply.rid + '" class="discuss_reply">' +
                    '<a class="discuss_text01" href="javascript:void(0);" name="replypost_mask" onclick="replyMask(this)">我也说一句</a>' +
                    '<div style="display:none" class="discuss_reply reply_box01">' +
                    '<textarea warp="off" style="font-family:Tahoma, ' + "宋体" + ';" id="textarea_recomment_body_' + result.reply.rid + '" class="discuss_text focus" rows="" cols="" name="content"></textarea>' +
                    '<div class="related clearfix">' +
                    '<div class="transmit clearfix">' +
                    '<a class="submitbtn fr" onclick="reCommentSubmit(this)" name="submit_recomment" data-oid="' + result.reply.rid + '">' +
                    '<span name="span_pinglun">评 论</span></a></div></div></div></div></div></dd></dl></div>';

                if ($('#comment_list_area div.area').length > 0) {
                    $('#comment_list_area div:first').before(commentHtml);
                } else {
                    //var moreHtml = '<div class="more-comment"><a href="' + wwwHost + 'comment/reply/page?unikey=' + unikey + '&domain=' + domain + '&pnum=1&psize=10">更多评论&gt;&gt;</a></div>';
                    //$('#comment_list_area').html(commentHtml + moreHtml);
                    $('#comment_list_area').html(commentHtml);
                }
                $("#textarea_body").val('我来说两句…').css('color', '#ccc');
                $('#reply_error').html('');
            } else {
                $('#reply_error').html(resMsg.msg);
                return;
            }
        },
        error: function () {
            alert('获取失败，请刷新');
        }
    });
}

//PC 楼中楼回复
function postSubComment(unikey, domain, body, oid, pid, reCommentDom) {
    $.ajax({
        url: apiHost + "jsoncomment/reply/post",
        type: "post",
        data: {unikey: unikey, domain: domain, body: JSON.stringify(body), oid: oid, pid: pid},
        dataType: "jsonp",
        jsonpCallback: "postcallback",
        success: function (req) {
            var resMsg = req[0];
            if (resMsg.rs == '0') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">' + resMsg.msg + '</span>');
                return;
            } else if (resMsg.rs == '-1001') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">请先保存您的内容，登录之后再回来~</span>');
                return;
            } else if (resMsg.rs == '-10102') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">请先保存您的内容，登录之后再回来~</span>');
                return;
            } else if (resMsg.rs == '-100') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">app不存在~</span>');
                return;
            } else if (resMsg.rs == '-10104') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">用户不存在~</span>');
                return;
            } else if (resMsg.rs == '-40011') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">评论不存在~</span>');
                return;
            } else if (resMsg.rs == '-40012') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">评论不存在~</span>');
                return;
            } else if (resMsg.rs == '-40000') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">您的请求缺少unikey参数~</span>');
                return;
            } else if (resMsg.rs == '-40001') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">您的请求缺少domain参数~</span>');
                return;
            } else if (resMsg.rs == '-40002') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">您的请求缺少jsonparam参数~</span>');
                return;
            } else if (resMsg.rs == '-40003') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">您的请求中jsonparam参数格式错误~</span>');
                return;
            } else if (resMsg.rs == '-40013') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">您的请求中domain参数错误~</span>');
                return;
            } else if (resMsg.rs == '-40005') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">评论内容未填写~</span>');
                return;
            } else if (resMsg.rs == '-40008') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">评论对象不存在~</span>');
                return;
            } else if (resMsg.rs == '-40006') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">oid参数错误~</span>');
                return;
            } else if (resMsg.rs == '-40009') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">主楼评论已删除~</span>');
                return;
            } else if (resMsg.rs == '-40007') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">pid参数错误~</span>');
                return;
            } else if (resMsg.rs == '-40010') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">上级回复已删除~</span>');
                return;
            } else if (resMsg.rs == '-40016') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">您已经赞过了~</span>');
                return;
            } else if (resMsg.rs == '-40017') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">内容含有敏感词：' + resMsg.result[0] + '</span>');
                return;
            } else if (resMsg.rs == '-40018') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">评论不存在~</span>');
                return;
            } else if (resMsg.rs == '-40019') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">您已被禁言~</span>');
                return;
            } else if (resMsg.rs == '-40020') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">一分钟内不能重复发送相同的内容~</span>');
                return;
            } else if (resMsg.rs == '-40022') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">两次评论间隔不能少于15秒，请稍后再试~</span>');
                return;
            } else if (resMsg.rs == '-1') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">请先保存您的内容，登录之后再回来~</span>');
                return;
            } else if (resMsg.rs == '1') {
                var result = resMsg.result;
                if (result == null) {
                    return;
                }
                var html = getReCommentHtml(result);
                var spanHtml = '条评论';
                var numHtml = $('#reply_num').text();
                var numStr = numHtml.replace(spanHtml, '');
                var num = parseInt(numStr) + 1;
                // $('#reply_num').html('<span>' + num + spanHtml + '</span>');

                $('#children_reply_list_' + oid).find('p').remove();
                var moreDom = $('#children_reply_list_' + oid).find('div.more-comment');
                if (moreDom == null || moreDom.length == 0 || moreDom.html().length == 0) {
                    $('#children_reply_list_' + oid).append(html);
                } else {
                    moreDom.before(html);
                }


                var num = parseInt($('#togglechildrenreply_area_' + oid).attr('data-replynum'));
                $('#togglechildrenreply_area_' + oid).attr('data-replynum', (num + 1));
                $('#textarea_recomment_body_' + oid).val("");
                $('#post_callback_msg_' + oid).remove();
            } else {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">' + resMsg.msg + '</span>');
                return;
            }
        },
        error: function () {
            alert('获取失败，请刷新');
        }
    });
}

//PC 点赞
function agreeComment(unikey, domain, rid) {
    if (isAgreeBlock) {
        return;
    }
    isAgreeBlock = true;
    $.ajax({
        url: apiHost + "jsoncomment/reply/agree",
        type: "post",
        data: {unikey: unikey, domain: domain, rid: rid},
        dataType: "jsonp",
        jsonpCallback: "agreecallback",
        success: function (req) {
            var resMsg = req[0];
            if (resMsg.rs == '0') {
                alert(resMsg.msg);
                return;
            } else if (resMsg.rs == '-1001') {
                loginDiv();
                return;
            } else if (resMsg.rs == '-10102') {
                loginDiv();
                return;
            } else if (resMsg.rs == '-100') {
                alert('app不存在~');
                return;
            } else if (resMsg.rs == '-10104') {
                alert('用户不存在~');
                return;
            } else if (resMsg.rs == '-40011') {
                alert('评论不存在~~');
                return;
            } else if (resMsg.rs == '-40012') {
                alert('评论不存在~~');
                return;
            } else if (resMsg.rs == '-40000') {
                alert('您的请求缺少unikey参数~');
                return;
            } else if (resMsg.rs == '-40001') {
                alert('您的请求缺少domain参数~');
                return;
            } else if (resMsg.rs == '-40002') {
                alert('您的请求缺少jsonparam参数~');
                return;
            } else if (resMsg.rs == '-40003') {
                alert('您的请求中jsonparam参数格式错误~')
                return;
            } else if (resMsg.rs == '-40013') {
                alert('您的请求中domain参数错误~')
                return;
            } else if (resMsg.rs == '-40005') {
                alert('评论内容未填写~')
                return;
            } else if (resMsg.rs == '-40008') {
                alert('评论对象不存在~');
                return;
            } else if (resMsg.rs == '-40006') {
                alert('oid参数错误~');
                return;
            } else if (resMsg.rs == '-40009') {
                alert('主楼评论已删除~');
                return;
            } else if (resMsg.rs == '-40007') {
                alert('pid参数错误~');
                return;
            } else if (resMsg.rs == '-40010') {
                alert('上级回复已删除~');
                return;
            } else if (resMsg.rs == '-40016') {
                alert('您已经赞过了~');
                return;
            } else if (resMsg.rs == '-40017') {
                alert('内容含有敏感词：' + resMsg.result[0]);
                return;
            } else if (resMsg.rs == '-40018') {
                alert('评论不存在~');
                return;
            } else if (resMsg.rs == '-40019') {
                alert('您已被禁言~');
                return;
            } else if (resMsg.rs == '-1') {
                loginDiv();
                return;
            } else if (resMsg.rs == '1') {
                var numObj = $('a[name=agree_num][data-commentid=' + rid + ']');
                if (bs.versions.android || bs.versions.iPhone || bs.versions.iPad) {
                    numObj = $('cite[data-commentid=' + rid + ']')
                }
                var objStr = numObj.html();
                var num;
                if (numObj.length == 0 || objStr == null || objStr.length == 0) {
                    num = parseInt(1);
                } else {
                    var num = numObj.html().replace("(", '').replace(')', '');
                    num = parseInt(num);
                    num = num + 1;
                }
                numObj.html('(' + num + ')');
            } else {
                alert(resMsg.msg);
                return;
            }
        },
        error: function () {
            isAgreeBlock = false;
            alert('获取失败，请刷新');
        },
        complete: function () {
            isAgreeBlock = false;
            return;
        }
    });
}

//PC 评论框
function initPostMask() {
    var textareaHtml = '';
    //uno uid token sign login pid
    if (uno == null || uno.length == 0 || uid == null || uid.length == 0 || token == null || token.length == 0 || sign == null || sign.length == 0 || login == null || login.length == 0 || pid == null || pid.length == 0 || login == 'client') {
        textareaHtml += '<textarea warp="off" style="font-family:Tahoma, 宋体;" class="talk_text" onfocus="focusin(this)" onblur="focusout(this)" id="textarea_body" name="body"></textarea>' +
            '<div class="wrapper_unlogin" style="text-align:center; margin:-65px 0 65px">您需要<a id="login_link" href="javascript:loginDiv();">登录</a>后才能评论</div>';

    } else {
        textareaHtml += '<textarea warp="off" style="font-family:Tahoma, 宋体;color: #ccc" class="talk_text" onfocus="focusin(this)" onblur="focusout(this)" id="textarea_body" name="body">我来说两句…</textarea>'
    }

    var html = '<div id="post_reply" class="blog_comment">' +
        '<div class="clearfix" style="padding:10px 6px 4px">' +
        '<span class="fl">评论</span>' +
        //     '<span class="fr" id="reply_num"></span>' +
        '</div>' +
        '<form action="" method="post" id="post_comment">' +
        '<div class="talk_wrapper clearfix">' +
        '<input type="hidden" id="hidden_unikey" value="' + uniKey + '"/>' +
        '<input type="hidden" id="hidden_domain" value="' + domain + '"/>' +
        '<input type="hidden" id="hidden_uri" value="' + uri + '"/>' +
        '<input type="hidden" id="hidden_title" value="' + title + '"/>' +
        textareaHtml +
        '</div>' +
        '<div class="related clearfix">' +
        '<div class="transmit_pic clearfix">' +
        '<a class="submitbtn fr" id="comment_submit" href="javascript:void(0);" onclick="commentSubmit()"><span>评 论</span></a>' +
        '<span style="color: #f00; padding-top: 2px; float: right;margin-top: 6px;margin-right: 4px;" id="reply_error"></span>' +
        '</div>' +
        '</div>' +
        '</form>' +
        '</div>' +
        '<a id="ap_comment" name="ap_comment"></a>' +
        '<div id="comment_list_area">' +
        '</div>';
    $('#comment_area').after(html);
    $('#comment_area').remove();
}

function loginDiv() {
    $('.login-box').show();
    $('.mask-box').show();
    $('.loginbarBox').show();
}
//M删除
function removeMComment(unikey, domain, rid, oid) {
    $.ajax({
        url: apiHost + "jsoncomment/reply/remove",
        type: "post",
        data: {unikey: unikey, domain: domain, rid: rid},
        dataType: "jsonp",
        //jsonpCallback: "removecallback",
        success: function (req) {
            var resMsg = req[0];
            if (resMsg.rs == '0') {
                $('.cmnt-beyond').html('<span>' + resMsg.msg + '</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-1001') {
                $('.cmnt-beyond').html('<span>请先保存您的内容，登录之后再回来~</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-10102') {
                $('.cmnt-beyond').html('<span>请先保存您的内容，登录之后再回来~</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-100') {
                $('.cmnt-beyond').html('<span>app不存在~</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-10104') {
                $('.cmnt-beyond').html('<span>用户不存在~</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-40011') {
                $('.cmnt-beyond').html('<span>评论不存在~</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-40012') {
                $('.cmnt-beyond').html('<span>评论不存在~</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-40000') {
                $('.cmnt-beyond').html('<span>您的请求缺少unikey参数</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-40001') {
                $('.cmnt-beyond').html('<span>您的请求缺少domain参数</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-40002') {
                $('.cmnt-beyond').html('<span>您的请求缺少jsonparam参数</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-40003') {
                $('.cmnt-beyond').html('<span>您的请求中jsonparam参数格式错误</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-40013') {
                $('.cmnt-beyond').html('<span>您的请求中domain参数错误</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-40005') {
                $('.cmnt-beyond').html('<span>评论内容未填写</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-40008') {
                $('.cmnt-beyond').html('<span>评论对象不存在</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-40006') {
                $('.cmnt-beyond').html('<span>oid参数错误~</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-40009') {
                $('.cmnt-beyond').html('<span>主楼评论已删除</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-40007') {
                $('.cmnt-beyond').html('<span>pid参数错误~</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-40010') {
                $('.cmnt-beyond').html('<span>上级回复已删除~</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-40016') {
                $('.cmnt-beyond').html('<span>您已经赞过了~</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-40017') {
                $('.cmnt-beyond').html('<span>内容含有敏感词：' + resMsg.result[0] + '</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-40018') {
                $('.cmnt-beyond').html('<span>评论不存在~</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-40019') {
                $('.cmnt-beyond').html('<span>您已被禁言~</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-40020') {
                $('.cmnt-beyond').html('<span>一分钟内不能重复发送相同的内容~</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-40022') {
                $('.cmnt-beyond').html('<span>两次评论间隔不能少于15秒，请稍后再试~</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '-1') {
                $('.cmnt-beyond').html('<span>请先保存您的内容，登录之后再回来~</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            } else if (resMsg.rs == '1') {
                var numHtml = $('.count-pl code').text();
                var numStr = numHtml.replace("(", '').replace(")", "");
                var num = parseInt(numStr) - 1;
                $('.count-pl code').text("(" + num + ")");

                var jsonParam = {
                    title: title,
                    pic: "",
                    description: "",
                    uri: uri
                }
                //拉取评论列表
                getMCommentList(uniKey, domain, jsonParam, 1, 5, (uid == null || uid == undefined) ? 0 : uid);

            } else {
                $('.cmnt-beyond').html('<span>' + resMsg.msg + '</span>');
                $('.cmnt-beyond').show();
                setTimeout(function () {
                    $('.cmnt-beyond').hide()
                }, 2000);
                return;
            }
        },
        error: function () {
            $('.cmnt-beyond').html('<span>获取失败，请刷新</span>');
            $('.cmnt-beyond').show();
            setTimeout(function () {
                $('.cmnt-beyond').hide()
            }, 2000);
            return;
        }
    });
}

//PC 删除
function removeComment(unikey, domain, rid, oid) {
    $.ajax({
        url: apiHost + "jsoncomment/reply/remove",
        type: "post",
        data: {unikey: unikey, domain: domain, rid: rid},
        dataType: "jsonp",
        //jsonpCallback: "removecallback",
        success: function (req) {
            var resMsg = req[0];
            if (resMsg.rs == '0') {
                alert(resMsg.msg);
                return;
            } else if (resMsg.rs == '-1001') {
                loginDiv();
                return;
            } else if (resMsg.rs == '-100') {
                alert('app不存在~');
                return;
            } else if (resMsg.rs == '-10104') {
                alert('用户不存在~');
                return;
            } else if (resMsg.rs == '-40011') {
                alert('评论不存在~~');
                return;
            } else if (resMsg.rs == '-40012') {
                alert('评论不存在~~');
                return;
            } else if (resMsg.rs == '-10102') {
                loginDiv();
                return;
            } else if (resMsg.rs == '-40000') {
                alert('您的请求缺少unikey参数~');
                return;
            } else if (resMsg.rs == '-40001') {
                alert('您的请求缺少domain参数~');
                return;
            } else if (resMsg.rs == '-40002') {
                alert('您的请求缺少jsonparam参数~');
                return;
            } else if (resMsg.rs == '-40003') {
                alert('您的请求中jsonparam参数格式错误~')
                return;
            } else if (resMsg.rs == '-40013') {
                alert('您的请求中domain参数错误~')
                return;
            } else if (resMsg.rs == '-40005') {
                alert('评论内容未填写~')
                return;
            } else if (resMsg.rs == '-40008') {
                alert('评论对象不存在~');
                return;
            } else if (resMsg.rs == '-40006') {
                alert('oid参数错误~');
                return;
            } else if (resMsg.rs == '-40009') {
                alert('主楼评论已删除~');
                return;
            } else if (resMsg.rs == '-40007') {
                alert('pid参数错误~');
                return;
            } else if (resMsg.rs == '-40010') {
                alert('上级回复已删除~');
                return;
            } else if (resMsg.rs == '-40016') {
                alert('您已经赞过了~');
                return;
            } else if (resMsg.rs == '-40017') {
                alert('内容含有敏感词：' + resMsg.result[0]);
                return;
            } else if (resMsg.rs == '-40018') {
                alert('评论不存在~');
                return;
            } else if (resMsg.rs == '-40019') {
                alert('您已被禁言~');
                return;
            } else if (resMsg.rs == '-1') {
                loginDiv();
                return;
            } else if (resMsg.rs == '1') {
                var spanHtml = '条评论';
                var numHtml = $('#reply_num').text();
                var numStr_head = numHtml.replace(spanHtml, '');
                var numStr = $('#togglechildrenreply_area_' + rid).attr('data-replynum');
                var num = parseInt(numStr_head) - (1 + parseInt(numStr));
                // $('#reply_num').html('<span>' + num + spanHtml + '</span>');
                if (oid == 0) {
                    $('#cont_cmt_list_' + rid).remove();
                    if (num <= 0) {
                        $('#comment_list_area').html('<p class="null-pl">目前没有评论，欢迎你发表评论</p>');
                    }
                } else {
                    $('#togglechildrenreply_area_' + oid).attr('data-replynum', parseInt(numStr) - 1);
                    $('#cont_recmt_list_' + rid).remove();
                }
            } else {
                alert(resMsg.msg);
                return;
            }
        },
        error: function () {
            alert('获取失败，请刷新');
        }
    });
}

//PC 组装评论列表的html
function getCommentListCallBack(commentObj) {
    var reply = commentObj.reply;
    var reReplyArray = null;
    if (commentObj.subreplys != null) {
        reReplyArray = commentObj.subreplys.rows;
    }

    var hasRe = (reReplyArray != null && reReplyArray.length > 0);

    //楼中楼 列表
    var reCommentListHtml = '<div id="children_reply_list_' + reply.reply.rid + '">';
    if (hasRe) {
        for (var i = 0; i < reReplyArray.length; i++) {
            var reCommentObj = reReplyArray[i];
            //楼中楼的html
            reCommentListHtml += getReCommentHtml(reCommentObj);
        }
    }
    reCommentListHtml += '</div>';

    var removeHtml = '';
    if (uno != null && uid != null && token != null && sign != null && login != null && login != 'client' && uno == reply.user.uno && uid == reply.user.uid) {
        removeHtml = '<a onclick="removeClick(this,1)" name="remove_comment" href="javascript:void(0);" data-oid="' + reply.reply.oid + '" data-rid="' + reply.reply.rid + '" >删除</a>'
    }

    var toogleReHtml = '<a name="togglechildrenreply_area" href="javascript:void(0);" onclick="toggle(this)" id="togglechildrenreply_area_' + reply.reply.rid + '" data-replynum="' + reply.reply.sub_reply_sum + '">回复' + (reply.reply.sub_reply_sum > 0 ? ('(' + reply.reply.sub_reply_sum + ')') : '') + '</a>';

    var postReCommentHtml = '<div id="post_recomment_area_' + reply.reply.rid + '" class="discuss_reply">' +
        ' <a class="discuss_text01" href="javascript:void(0);" name="replypost_mask" onclick="replyMask(this)">我也说一句</a>' +
        '<div style="display:none" class="discuss_reply reply_box01">' +
        '<textarea warp="off" style="font-family:Tahoma, \'宋体\';" id="textarea_recomment_body_' + reply.reply.rid + '" class="discuss_text focus" rows="" cols="" name="content"></textarea>' +
        '<div class="related clearfix">' +
        '<div class="transmit clearfix">' +
        ' <a class="submitbtn fr" onclick="reCommentSubmit(this)" name="submit_recomment"  data-oid="' + reply.reply.rid + '" name="childreply_submit"><span name="span_pinglun">评 论</span></a>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '</div>';

    var puserHtml = '';
    if (reply.puser != null && reply.puser.name != null) {
        puserHtml = '@' + reply.puser.name + ":";
    }

    var rePageHtml = '<div id="chidren_page_area_' + reply.reply.rid + '" class="discuss_page">';
    if (commentObj.subreplys != null && commentObj.subreplys != undefined && commentObj.subreplys.page != null && commentObj.subreplys.page.maxPage > 1) {
        rePageHtml += getRePageHtml(commentObj.subreplys.page, reply.reply.rid);
    }
    rePageHtml += '</div>';

    var html = '<div id="cont_cmt_list_' + reply.reply.rid + '" class="area blogopinion clearfix ">' +
        '<dl>' +
        '<dt class="personface">' +
        '<a title="' + reply.user.name + '" name="atLink" target="_blank"  href="http://uc.' + joyconfig.DOMAIN + '/usercenter/page?pid=' + reply.user.pid + '">' +
        '<img width="58" height="58" class="user" src="' + reply.user.icon + '">' +
        '</a>' +
        '</dt>' +
        '<dd class="textcon discuss_building">' +
        '<a title="' + reply.user.name + '" class="author" name="atLink" target="_blank" href="http://uc.' + joyconfig.DOMAIN + '/usercenter/page?pid=' + reply.user.pid + '">' + reply.user.name + '</a><code>' + reply.reply.floor_num + '楼</code>' +
        '<p>' + puserHtml + reply.reply.body.text + '</p>' +
        '<div class="discuss_bdfoot">' + reply.reply.post_date + '&nbsp;<a href="javascript:void(0);" id="agreelink_' + reply.reply.rid + '" data-commentid="' + reply.reply.rid + '" class="dianzan" onclick="agreeClick(this)"></a><span id="agreenum_' + reply.reply.rid + '"><a href="javascript:void(0);" name="agree_num" data-commentid="' + reply.reply.rid + '" onclick="agreeClick(this)">' + ((reply.reply.agree_sum == null || reply.reply.agree_sum == 0) ? '' : ('(' + reply.reply.agree_sum + ')')) + '</a></span>' + removeHtml + toogleReHtml + '</div>' +
        '<div class="discuss_bd_list discuss_border" style="display:none"> ' +
        reCommentListHtml +
        rePageHtml +
        postReCommentHtml +
        '</div> ' +
        '</dd>' +
        '</dl>' +
        '</div>';
    return html;
}

//PC 楼中楼的 分页 html
function getRePageHtml(page, oid) {
    var rePageHtml = '<a href="javascript:void(0)" onclick="getReCommentListPage(1, 10, ' + oid + ')">首页</a>&nbsp;';
    if (page.curPage > 1) {
        rePageHtml += '<a onclick="getReCommentListPage(' + (page.curPage - 1) + ', 10, ' + oid + ')" href="javascript:void(0);">上一页</a>&nbsp;';
    }
    if (page.curPage <= 4) {
        if (page.maxPage <= 4) {
            for (var cp = 1; cp <= page.maxPage; cp++) {
                if (cp == page.curPage) {
                    rePageHtml += '<b>' + cp + '</b>&nbsp;';
                } else {
                    rePageHtml += '<a href="javascript:void(0);" onclick="getReCommentListPage(' + cp + ', 10, ' + oid + ')">' + cp + '</a>&nbsp;'
                }
            }
        } else {
            for (var cp = 1; cp <= 4; cp++) {
                if (cp == page.curPage) {
                    rePageHtml += '<b>' + cp + '</b>&nbsp;';
                } else {
                    rePageHtml += '<a href="javascript:void(0);" onclick="getReCommentListPage(' + cp + ', 10, ' + oid + ')">' + cp + '</a>&nbsp;'
                }
            }
            if (page.curPage >= 3) {
                if (page.curPage > 3) {
                    rePageHtml += '<a href="javascript:void(0);" onclick="getReCommentListPage(' + (page.curPage + 1) + ', 10, ' + oid + ')">' + (page.curPage + 1) + '</a>&nbsp;'
                }
                if (page.maxPage >= (page.curPage + 2)) {
                    rePageHtml += '<a href="javascript:void(0);" onclick="getReCommentListPage(' + (page.curPage + 2) + ', 10, ' + oid + ')">' + (page.curPage + 2) + '</a>&nbsp;'
                }
            }
        }
    } else {
        if (page.curPage < page.maxPage - 2) {
            for (var cp = (page.curPage - 2); cp <= (page.curPage + 2); cp++) {
                if (cp == page.curPage) {
                    rePageHtml += '<b>' + cp + '</b>&nbsp;';
                } else {
                    rePageHtml += '<a href="javascript:void(0);" onclick="getReCommentListPage(' + cp + ', 10, ' + oid + ')">' + cp + '</a>&nbsp;'
                }
            }
        } else {
            for (var cp = (page.curPage - 2); cp <= page.maxPage; cp++) {
                if (cp == page.curPage) {
                    rePageHtml += '<b>' + cp + '</b>&nbsp;';
                } else {
                    rePageHtml += '<a href="javascript:void(0);" onclick="getReCommentListPage(' + cp + ', 10, ' + oid + ')">' + cp + '</a>&nbsp;'
                }
            }
        }
    }
    if (page.curPage < page.maxPage) {
        rePageHtml += '<a href="javascript:void(0);" onclick="getReCommentListPage(' + (page.curPage + 1) + ', 10, ' + oid + ')">下一页</a>&nbsp;';
    }
    rePageHtml += '<a href="javascript:void(0)" onclick="getReCommentListPage(' + page.maxPage + ', 10, ' + oid + ')">末页</a>';
    return rePageHtml;
}

//PC 楼中楼列表 翻页
function getReCommentListPage(cp, psize, oid) {
    getReCommentList(uniKey, domain, oid, cp, psize);
}

//PC 楼中楼列表
function getReCommentList(unikey, domain, oid, cp, psize) {
    $.ajax({
        url: apiHost + "jsoncomment/reply/sublist",
        type: "post",
        data: {unikey: unikey, domain: domain, oid: oid, pnum: cp, psize: psize},
        dataType: "jsonp",
        jsonpCallback: "sublistcallback",
        success: function (req) {
            var resMsg = req[0];
            if (resMsg.rs == '0') {
                return;
            } else if (resMsg.rs == '-1001') {
                alert('请先保存您的内容，登录之后再回来~');
                return;
            } else if (resMsg.rs == '-10102') {
                alert('请先保存您的内容，登录之后再回来~');
                return;
            } else if (resMsg.rs == '-40000') {
                alert('您的请求缺少unikey参数~');
                return;
            } else if (resMsg.rs == '-40001') {
                alert('您的请求缺少domain参数~');
                return;
            } else if (resMsg.rs == '-40002') {
                alert('您的请求缺少jsonparam参数~');
                return;
            } else if (resMsg.rs == '-40003') {
                alert('您的请求中jsonparam参数格式错误~');
                return;
            } else if (resMsg.rs == '-40013') {
                alert('您的请求中domain参数错误~');
                return;
            } else if (resMsg.rs == '-40005') {
                alert('评论内容未填写~');
                return;
            } else if (resMsg.rs == '-40008') {
                alert('评论对象不存在~');
                return;
            } else if (resMsg.rs == '-40006') {
                alert('oid参数错误~');
                return;
            } else if (resMsg.rs == '-40009') {
                alert('主楼评论已删除~');
                return;
            } else if (resMsg.rs == '-40007') {
                alert('pid参数错误~');
                return;
            } else if (resMsg.rs == '-40010') {
                alert('上级回复已删除~');
                return;
            } else if (resMsg.rs == '-40016') {
                alert('您已经赞过了~');
                return;
            } else if (resMsg.rs == '-40017') {
                alert('内容含有敏感词~');
                return;
            } else if (resMsg.rs == '-40018') {
                alert('评论不存在~');
                return;
            } else if (resMsg.rs == '-40019') {
                alert('您已被禁言~');
                return;
            } else if (resMsg.rs == '-40020') {
                alert('一分钟内不能重复发送相同的内容~');
                return;
            } else if (resMsg.rs == '-40022') {
                alert('两次评论间隔不能少于15秒，请稍后再试~');
                return;
            } else if (resMsg.rs == '-99999') {
                alert('系统维护~');
                return;
            } else if (resMsg.rs == '-1') {
                alert('请先保存您的内容，登录之后再回来~');
                return;
            } else if (resMsg.rs == '1') {
                var result = resMsg.result;
                if (result == null) {
                    return;
                } else {
                    var html = getReCommentListCallBack(result);
                    $('#children_reply_list_' + oid).html(html);
                    var rePageHtml = getRePageHtml(result.page, oid);
                    $('#chidren_page_area_' + oid).html(rePageHtml);
                }
            } else {
                alert(resMsg.msg);
                return;
            }
        },
        error: function () {
            alert('获取失败，请刷新');
        }
    });
}

//PC 楼中楼列表的html
function getReCommentListCallBack(commentObj) {
    var reCommentArray = null;
    if (commentObj != null && commentObj.rows != null && commentObj.rows.length > 0) {
        reCommentArray = commentObj.rows;
    }

    var hasRe = (reCommentArray != null && reCommentArray.length > 0);

    var reCommentListHtml = '';
    if (hasRe) {
        for (var i = 0; i < reCommentArray.length; i++) {
            var reCommentObj = reCommentArray[i];
            reCommentListHtml += getReCommentHtml(reCommentObj);
        }
    }
    return reCommentListHtml;
}

//M 楼中楼列表的html
function getReMCommentListCallBack(commentObj) {
    var reMCommentArray = null;
    if (commentObj != null && commentObj.rows != null && commentObj.rows.length > 0) {
        reMCommentArray = commentObj.rows;
    }

    var hasRe = (reMCommentArray != null && reMCommentArray.length > 0);

    var reMCommentListHtml = '';
    if (hasRe) {
        for (var i = 0; i < reMCommentArray.length; i++) {
            var reCommentObj = reMCommentArray[i];
            reMCommentListHtml += getReMCommentHtml(reCommentObj);
        }
    }
    return reMCommentListHtml;
}

//M 组装评论列表的html
function getMCommentListCallback(commentObj) {
    var reReplyArray = null;
    if (commentObj.subreplys != null) {
        reReplyArray = commentObj.subreplys.rows;
    }

    var hasRe = (reReplyArray != null && reReplyArray.length > 0);

    var reCommentListHtml = '';
    if (hasRe) {
        for (var i = 0; i < reReplyArray.length; i++) {
            var reCommentObj = reReplyArray[i];
            reCommentListHtml += getReMCommentHtml(reCommentObj);
        }
    }

    var atHtml = '';
    if (commentObj.reply.puser != null) {
        atHtml += '<a href="javascript:void(0);">回复' + commentObj.reply.puser.name + ':</a>';
    }

    var moreReHtml = '';
    if (commentObj.subreplys != null && commentObj.subreplys.page != null && commentObj.subreplys.page.maxPage > 1) {
        moreReHtml += '<a id="more_re_mcoment_' + commentObj.reply.reply.rid + '" href="javascript:void(0);" class="level-more" onclick="getReMCommentListPage(' + (commentObj.subreplys.page.curPage + 1) + ', 5, ' + commentObj.reply.reply.rid + ')">更多评论...</a>';
    }

    var html = '<div id="cmnt-box-"' + commentObj.reply.reply.rid + ' class="cmnt-box">' +
        '<dl>' +
        '<dt>' +
        '<span class="fn-clear">' +
        '<a href="http://uc.' + joyconfig.DOMAIN + '/usercenter/page?pid=' + commentObj.reply.user.pid + '" class="userhead fn-left">' +
        '<img class="lazy" src="' + commentObj.reply.user.icon + '" alt="" title="">' +
        '</a>' +
        '<font class="fn-left">' +
        '<a href="http://uc.' + joyconfig.DOMAIN + '/usercenter/page?pid=' + commentObj.reply.user.pid + '" class="userName">' + commentObj.reply.user.name + '</a>' +
        '</font>' +
        '</span>' +
        '<cite class="cmnt-reply fn-right" onclick="toJump(' + commentObj.reply.reply.rid + ', ' + commentObj.reply.reply.rid + ')">回复</cite>' +
        '<cite class="zan-num fn-right" data-commentid="' + commentObj.reply.reply.rid + '"onclick="agreeMClick(this)">' + ((commentObj.reply.reply.agree_sum == null || commentObj.reply.reply.agree_sum == 0) ? '' : ('(' + commentObj.reply.reply.agree_sum + ')')) + '</cite>' +
        '</dt>' +
        '<dd class="border-b">' +
        '<span>' +
        atHtml + commentObj.reply.reply.body.text +
        '</span>' +
        '<div class="fn-clear cmnt-time-del">' +
        '<code class="cmnt-time fn-clear">' + commentObj.reply.reply.post_date + '</code>'
    if (uno != null && uid != null && token != null && sign != null && login != null && login != 'client' && uno == commentObj.reply.user.uno && uid == commentObj.reply.user.uid) {
        html += '<span class="cmnt-del fn-right"><a onclick="removeClick(this,2)" name="remove_comment" href="javascript:void(0);" data-oid="' + commentObj.reply.reply.oid + '" data-rid="' + commentObj.reply.reply.rid + '" >删除</a></span>'
    }
    html += '<code class="floor">' + commentObj.reply.reply.floor_num + '楼</code></div>' +
        '<div class="cmnt-level">' +
        '<div class="level-con" id="re_mcomment_list_' + commentObj.reply.reply.rid + '">' +
        reCommentListHtml +
        '</div>' +
        moreReHtml +
        '</div>' +
        '</dd>' +
        '</dl>' +
        '</div>';
    return html;
}

//M 楼中楼 加载更多
function getReMCommentListPage(cp, psize, oid) {
    getReMCommentList(uniKey, domain, oid, cp, psize);
}

//M 楼中楼
function getReMCommentList(unikey, domain, oid, cp, psize) {
    $.ajax({
        url: apiHost + "jsoncomment/reply/sublist",
        type: "post",
        data: {unikey: unikey, domain: domain, oid: oid, pnum: cp, psize: psize},
        dataType: "jsonp",
        jsonpCallback: "sublistcallback",
        success: function (req) {
            var resMsg = req[0];
            if (resMsg.rs == '1') {
                var result = resMsg.result;
                if (result == null) {
                    return;
                } else {
                    var html = getReMCommentListCallBack(result);
                    $('#re_mcomment_list_' + oid).append(html);
                    if (result.page != null && result.page.maxPage > result.page.curPage) {
                        $('#more_re_mcoment_' + oid).html('查看更多...');
                        $('#more_re_mcoment_' + oid).attr('onclick', 'getReMCommentListPage(' + (result.page.curPage + 1) + ', 5, ' + oid + ')');
                    } else {
                        $('#more_re_mcoment_' + oid).remove();
                    }
                }
            } else {
                alert(resMsg.msg);
                return;
            }
        },
        error: function () {
            alert('获取失败，请刷新');
        }
    });
}

//M 打开评论框
function toJump(oid, pid) {
    if (uno == null || uid == null) {
        $('.new-area').blur();
        $('.pl-box').hide();
        $('.mask').hide();
        loginDiv();
        return false;
    }
    $('#input_oid').val(oid);
    $('#input_pid').val(pid);
    $('.pl-box').show();
    $('.mask').show();
    $('.new-area').focus();

}

//M 组装楼中楼列表的html
function getReMCommentHtml(reCommentObj) {
    var puserHtml = '';
    if (reCommentObj.puser != null && reCommentObj.puser.name != null) {
        puserHtml = '<i>回复</i><b>' + reCommentObj.puser.name + '：</b>';
    }
    var html = '<p name="m_sub_reply" onclick="toJump(' + reCommentObj.reply.oid + ',' + reCommentObj.reply.rid + ')" data-nick="' + reCommentObj.user.name + '" data-oid="' + reCommentObj.reply.oid + '" data-pid="' + reCommentObj.reply.rid + '"><b>' + reCommentObj.user.name;
    if (puserHtml == '') {
        html += ':';
    }
    html += '</b>' + puserHtml + reCommentObj.reply.body.text + '</p>';
    //var html = '<p name="m_sub_reply"><b>'+reCommentObj.user.name+'</b>'+puserHtml + reCommentObj.reply.body.text +'</p>';
    return html;
}

//PC 组装楼中楼列表的html
function getReCommentHtml(reCommentObj) {
    var removeHtml = '';
    if (uno != null && uid != null && token != null && sign != null && login != null && login != 'client' && uno == reCommentObj.user.uno && uid == reCommentObj.user.uid) {
        removeHtml = '<a onclick="removeClick(this,1)" name="remove_comment" href="javascript:void(0);" data-rid="' + reCommentObj.reply.rid + '" data-oid="' + reCommentObj.reply.oid + '">删除</a>'
    }

    var puserHtml = '';
    if (reCommentObj.puser != null && reCommentObj.puser.name != null) {
        puserHtml = '@' + reCommentObj.puser.name + ":";
    }

    var reCommentHtml = '<div style="" id="cont_recmt_list_' + reCommentObj.reply.rid + '" class="conmenttx clearfix">' +
        '<div class="conmentface">' +
        '<div class="commenfacecon">' +
        '<a href="http://uc.' + joyconfig.DOMAIN + '/usercenter/page?pid=' + reCommentObj.user.pid + '" title="' + reCommentObj.user.name + '" name="atLink" target="_blank" class="cont_cl_left">' +
        '<img width="33px" height="33px" src="' + reCommentObj.user.icon + '">' +
        '</a>' +
        '</div>' +
        '</div>' +
        '<div class="conmentcon">' +
        '<a title="' + reCommentObj.user.name + '" name="atLink" target="_blank" href="http://uc.' + joyconfig.DOMAIN + '/usercenter/page?pid=' + reCommentObj.user.pid + '">' + reCommentObj.user.name + '</a>' +
        ':' + puserHtml + reCommentObj.reply.body.text + '<div class="commontft clearfix"><span class="reply_time">' + reCommentObj.reply.post_date + '</span>' +
        '<span class="delete">' +
        '<a href="javascript:void(0);" id="agreelink_' + reCommentObj.reply.rid + '" data-commentid="' + reCommentObj.reply.rid + '" class="dianzan" onclick="agreeClick(this)"></a>' +
        '<span id="agreenum_' + reCommentObj.reply.rid + '"><a href="javascript:void(0);" name="agree_num" data-commentid="' + reCommentObj.reply.rid + '" onclick="agreeClick(this)">' + ((reCommentObj.reply.agree_sum == null || reCommentObj.reply.agree_sum == 0) ? '' : ('(' + reCommentObj.reply.agree_sum + ')')) + '</a></span>' +
        removeHtml +
        '<a href="javascript:void(0);" onclick="parentMask(this)" name="link_recommentparent_mask" data-nick="' + reCommentObj.user.name + '" data-oid="' + reCommentObj.reply.oid + '" data-pid="' + reCommentObj.reply.rid + '">回复</a>' +
        '</span>' +
        '</div>' +
        '</div>' +
        '</div>';
    return reCommentHtml;
}

var errorArray = {
    'comment.post.limit': '您说话的频率太快了，请歇一歇哦~',
    'user.has.agree': '你已经支持过了',
    'comment.body.empty': '评论内容不能为空',
    'content.isnot.exists': '文章不存在！',
    'reply.not.exist': '该评论已经被删除，无法回复！',
    'comment.body.illegl': '评论中含有不适当的内容！'
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

function ToDBC(txtstring) {
    var tmp = "";
    for (var i = 0; i < txtstring.length; i++) {
        if (txtstring.charCodeAt(i) == 32) {
            tmp = tmp + String.fromCharCode(12288);
        }
        if (txtstring.charCodeAt(i) < 127) {
            tmp = tmp + String.fromCharCode(txtstring.charCodeAt(i) + 65248);
        }
    }
    return tmp;
}

$.fn.AutoHeight = function (options) {
    var defaults = {
        maxHeight: null,
        minHeight: $(this).height()
    };
    var opts = (typeof (options) === 'object') ? $.extend({}, defaults, options) : {};
    this.each(function () {
        $(this).bind("paste cut keydown keyup focus blur", function () {
            var height, style = this.style;
            this.style.height = opts.minHeight + 'px';
            if (this.scrollHeight > opts.minHeight) {
                if (opts.maxHeight && this.scrollHeight > opts.maxHeight) {
                    height = opts.maxHeight;
                    style.overflowY = 'scroll';
                } else {
                    if ('\v' == 'v') {
                        height = this.scrollHeight - 2;
                    } else {
                        height = this.scrollHeight;
                    }
                    style.overflowY = 'hidden';
                }
                style.height = height + 'px';
            }
        });
    });
    return this;
};

