<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>

    <meta name="Keywords" content="全民奇迹版活动专题">
    <meta name="description" content="全民奇迹版活动专题">
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>全民奇迹版活动专题</title>
    <link href="${URL_LIB}/static/theme/default/css/header_simple.css?${version}" rel="stylesheet" type="text/css"/>
    <script src="http://static.joyme.com/js/jquery-1.9.1.min.js" language="javascript"></script>
    <link href="${URL_LIB}/static/css/activity/2015shuang12/style.css?${version}" rel="stylesheet" type="text/css"/>
</head>

<body>
<!--topbar-->
<div id="joyme-head-2015">
    <div class="joyme-head-nav">
        <div class="joyme-left-nav"><a href="http://www.joyme.com">返回着迷首页 &gt;&gt;</a> <a
                href="http://www.joyme.com/news/official/">手游资讯</a> <a href="http://v.joyme.com/">着迷视频</a> <a
                href="http://wiki.joyme.com/">着迷WIKI</a> <a href="http://www.joyme.com/gift">礼包中心</a> <a
                href="http://bbs.joyme.com">论 坛</a> <a href="http://html.joyme.com/mobile/gameguides.html">应用下载</a>
        </div>
        <script>
            document.write(unescape("%3Cscript src='http://passport.${DOMAIN}/auth/header/userinfo?t=simple%26v=" + Math.random() + "' type='text/javascript'%3E%3C/script%3E"));
        </script>
    </div>
</div>
<!--End topbar-->
<div class="wrapper">
    <div class="inner clearfix">
        <!--right-->
        <div class="right">
            <a class="android" href="http://ad.mg3721.com/?aid=4X0jxi">安卓下载</a>
            <a class="ios" href="http://qmqj2.xy.com/idf/Igqzxh">IOS下载</a>
            <cite><img src="${URL_LIB}/static/img/activity/2015shuang12/code.jpg"><br>二维码下载</cite>
            <a class="web" href="http://weibo.com/p/1006065035720969/home?from=page_100606&mod=TAB#place">新浪微博</a>
        </div>
        <!--End right-->
        <!--link-->
        <div class="link" id="link">
            <a href="http://wiki.joyme.com/qmqj/">全民奇迹WIKI</a>
            <a href="http://mu.xy.com/">进入官网</a>
        </div>
        <!--End link-->
        <div class="column clearfix">
            <div class="quick-link">
                <a href="javascript:void(0)" id="a_wyndj">我要拿大奖</a>
                <a href="http://www.joyme.com/gift/21069">老玩家回归礼包</a>
            </div>
        </div>
        <!--articlebox-->
        <div class="articlebox clearfix">
            <!--article-box-->
            <div id="article-box">
                <ul class="article-list">
                    <!--li1-->
                    <c:choose>
                        <c:when test="${list != null && list.size() > 0}">
                            <c:forEach items="${list}" var="dto" varStatus="st">
                                <c:if test="${st.index == 0}">
                                    <li class="show">
                                    <div class="showmain clearfix">
                                </c:if>
                                <div class="article">
                                    <p>${dto.words}</p>
                                    <span>BY：<c:choose><c:when test="${fn:length(dto.nick) > 6}">${fn:substring(dto.nick,0,5)}...</c:when><c:otherwise>${dto.nick}</c:otherwise></c:choose></span>
                                </div>
                                <c:if test="${st.index % 8 == 7 && st.index!=0 && !st.last}">
                                    </div>
                                    </li>
                                    <li>
                                    <div class="showmain">
                                </c:if>
                                <c:if test="${st.last}">
                                    </div>
                                    </li>
                                </c:if>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <li class="show">
                                <div class="showmain clearfix">
                                    <div class="article">
                                        <p>1我是一个忠实的全民奇迹玩家，也预祝全民奇迹越办越好~~</p>
                                        <span>BY：葡提蛋挞</span>
                                    </div>
                                    <div class="article">
                                        <p>我是一个忠实的全民奇迹玩家，也预祝全民奇迹越办越好~~</p>
                                        <span>BY：小皮蛋儿</span>
                                    </div>
                                    <div class="article">
                                        <p>我是一个忠实的全民奇迹玩家，也预祝全民奇迹越办越好~~</p>
                                        <span>BY：最厉害的人</span>
                                    </div>
                                    <div class="article">
                                        <p>我是一个忠实的全民奇迹玩家，也预祝全民奇迹越办越好~~</p>
                                        <span>BY：为MU而来</span>
                                    </div>
                                    <div class="article">
                                        <p>我是一个忠实的全民奇迹玩家，也预祝全民奇迹越办越好~~</p>
                                        <span>BY：最疯狂的石头</span>
                                    </div>
                                    <div class="article">
                                        <p>我是一个忠实的全民奇迹玩家，也预祝全民奇迹越办越好~~</p>
                                        <span>BY：小小的希望</span>
                                    </div>
                                    <div class="article">
                                        <p>我是一个忠实的全民奇迹玩家，也预祝全民奇迹越办越好~~</p>
                                        <span>BY：玉帝老儿</span>
                                    </div>
                                    <div class="article">
                                        <p>我是一个忠实的全民奇迹玩家，也预祝全民奇迹越办越好~~</p>
                                        <span>BY：葡提蛋挞</span>
                                    </div>
                                </div>
                            </li>
                            <li>
                                <div class="showmain">
                                    <div class="article">
                                        <p>2我是一个忠实的全民奇迹玩家，也预祝全民奇迹越办越好~~</p>
                                        <span>BY：小皮蛋儿</span>
                                    </div>
                                    <div class="article">
                                        <p>我是一个忠实的全民奇迹玩家，也预祝全民奇迹越办越好~~</p>
                                        <span>BY：葡提蛋挞</span>
                                    </div>
                                    <div class="article">
                                        <p>我是一个忠实的全民奇迹玩家，也预祝全民奇迹越办越好~~</p>
                                        <span>BY：最厉害的人</span>
                                    </div>
                                    <div class="article">
                                        <p>我是一个忠实的全民奇迹玩家，也预祝全民奇迹越办越好~~</p>
                                        <span>BY：为MU而来</span>
                                    </div>
                                    <div class="article">
                                        <p>我是一个忠实的全民奇迹玩家，也预祝全民奇迹越办越好~~</p>
                                        <span>BY：最疯狂的石头</span>
                                    </div>
                                    <div class="article">
                                        <p>我是一个忠实的全民奇迹玩家，也预祝全民奇迹越办越好~~</p>
                                        <span>BY：小小的希望</span>
                                    </div>
                                    <div class="article">
                                        <p>我是一个忠实的全民奇迹玩家，也预祝全民奇迹越办越好~~</p>
                                        <span>BY：玉帝老儿</span>
                                    </div>
                                    <div class="article">
                                        <p>我是一个忠实的全民奇迹玩家，也预祝全民奇迹越办越好~~</p>
                                        <span>BY：葡提蛋挞</span>
                                    </div>
                                </div>
                            </li>
                        </c:otherwise>
                    </c:choose>
                    <span id="n-btn" title="换一组">换一组</span>
                </ul>
                <ul id="but">
                </ul>
            </div>
            <!--End article-box-->
            <div class="column">
                <div class="comments">
                    <div class="comments-title">你还可以输入<span id="span_words_tips">25</span>字</div>
                    <div class="comments-text"><textarea id="textarea_words" placeholder="#输入话题标题#"></textarea></div>
                    <a href="javascript:void(0);" id="a_post">发布</a>
                </div>
                <div class="comments-info">
                    <p>《全民奇迹》是一款获得韩国网禅十年经典网游《奇迹MU》正版授权的3D 动作类RPG多人在线手游，由北京天马时空与上海恺英网络联合开发，恺英网络独家代理发行。</p>

                    <p>
                        游戏采用Unity3D引擎打造，完美重现了端游的精美3D画面，更集成了装备养成、人物养成、Boss挑战、在线交易、战盟PK等海量内容。让玩家身临其境，仿佛回到那个“奇迹的年代”，带来一次真正的掌上奇迹之旅!</p>
                </div>
            </div>
        </div>
        <!--End articlebox-->
        <!--share-->
        <div class="column share clearfix" id="div_share">
            <h2>微博分享抽大奖</h2>

            <div class="share-dynamic">
                <div class="dynamic-info">
                    <span>#全民奇迹周年庆#</span>
                    <span>12月10日 9:05  转发 </span>
                </div>
                <li>我在全民奇迹等你回来，创全民新时代！还不快来着迷参与抽奖活动</li>
                <a href="javascript:void(0);" id="a_share">微博转发</a>
            </div>
            <div class="rules">
                <h3>参与微博分享抽奖活动规则</h3>
                <li><i>1</i>所有参与活动玩家需要留下QQ，方便联系；</li>
                <li><i>2</i>活动奖品由官方直接发放；</li>
                <li><i>3</i>评选标准以抽取符合活动规则的玩家回复为标准。</li>
            </div>
            <!--rotate-->
            <div class="ly-plate">
                <div class="rotate-bg"></div>
                <div class="lottery-star">
                    <img src="${URL_LIB}/static/img/activity/2015shuang12/rotate-static.png" id="lotteryBtn"
                         style="-webkit-transform: rotate(0deg); -webkit-transform-origin: 50% 50% 0px;">
                </div>
            </div>
        </div>
        <!--End share-->
        <!--activities-->
        <div class="activities clearfix">
            <h2>参与抽奖规则</h2>

            <h3><span>活动时间</span></h3>
            <li class="act-time">2015年12月10日——2015年12月20日</li>
            <h3><span>活动要求</span></h3>

            <div class="explain">
                <p>各位玩家下载体验《全民奇迹》后，根据活动形式参与活动：</p>
                <li><i>1</i>获奖玩家需要留下姓名、手机号、真实地址，方便联系；</li>
                <li><i>2</i>活动结束后奖品由官方直接发放；</li>
                <li><i>3</i>评选标准以抽取符合规则的玩家为标准；</li>
                <li><i>4</i>中奖后未填写联系方式的，视为弃奖；</li>
                <li><i>5</i>活动的解释权归属着迷所有。</li>
            </div>
            <h3><span>活动奖品</span></h3>
            <li>一等奖：500元京东卡/人 （ 1人）</li>
            <li>二等奖：50元京东卡/人 （ 5人）</li>
            <li>三等奖：抱枕、U盘/人 （18人）</li>
            <li>四等奖：美女鼠标垫/人 （20人）</li>
            <li>五等奖：挂件、普通鼠标垫/人 （55人）</li>
        </div>
        <!--End activities-->
    </div>
    <!--End inner-->
    <div class="lottery-box" style="display:none;">
        <div class="lybox-main clearfix">
            <h3><b>恭喜您中奖啦！</b><span>注：为了奖品可以及时的送到您手中，请填写真实有效的信息。</span></h3>

            <div class="user-info clearfix" style="display:none;">           
			 <div class="prize-img"><cite><img src=""></cite></div>

                <div class="item clearfix">
                    <span class="label">姓名</span>

                    <div class="fl"><input type="text" id="name" name="mail" class="text" tabindex="4"
                                           sta="0"><em>*</em></div>
                </div>
                <div class="item clearfix">
                    <span class="label">手机</span>

                    <div class="fl"><input type="text" id="phone" name="mail" class="text" tabindex="4"
                                           sta="0"><em>*</em></div>
                </div>
                <div class="item clearfix">
                    <span class="label">地址</span>

                    <div class="fl"><textarea rows="2" id="address"></textarea><em>*</em></div>
                </div>
                <div class="item">
                    <a class="sub" id="a_save">确认提交</a>
                </div>
            </div>
            <i id="i_hide"></i>
        </div>
    </div>
    <!--footer-->
    <div id="footer_bottom">
        <span>© 2011－2015 joyme.com, all rights reserved</span>
        <a href="http://www.joyme.com/help/aboutus" target="_blank" rel="nofollow">关于着迷</a> |
        <a href="http://www.joyme.com/about/job/zhaopin" target="_blank" rel="nofollow">工作在着迷</a> |
        <a href="http://www.joyme.com/about/contactus" target="_blank" rel="nofollow">商务合作</a>|
        <a href="http://www.joyme.com/help/law/parentsprotect/" target="_blank" rel="nofollow">家长监护</a>|
        <a href="http://www.joyme.com/sitemap.htm" target="_blank">网站地图</a>
        <span><a href="http://www.miibeian.gov.cn/" target="_blank">京ICP备11029291号</a></span>
        <span>京公网安备110108001706号</span>
    </div>
    <!--footer end-->

    <script src="${URL_LIB}/static/js/activity/2015shuang12/jQueryRotate.2.2.js"></script>
    <script type="text/javascript">
        var uno = getCookie('jmuc_uno');
        var uid = getCookie('jmuc_u');
        var token = getCookie('jmuc_token');
        var sign = getCookie('jmuc_s');
        var login = getCookie('jmuc_lgdomain');
        var pid = getCookie('jmuc_pid');

        var postLock = false;
        $(document).ready(function () {
            $('#i_hide').on('click', function () {

                $('.lottery-box').hide();

            });

            $('#a_save').on('click', function () {
                var name = $("#name").val();
                if (name.trim() == "") {
                    alert("姓名不能为空");
                    return false;
                }
                var phoneReg = /^([0-9]+(([-]*)|([+]*))[0-9]+)+$/;
                var phone = $("#phone").val();
                phone = phone.replace(/(\s+)/g, "");
                if (phone.length < 6 || phone.length > 20 || !phoneReg.test(phone)) {
                    alert("手机号码未填写或不正确");
                    return false;
                }
                var address = $("#address").val();
                if (address.trim() == "") {
                    alert("详细地址不能为空");
                    return false;
                }
                var ajaxTimeoutTest = $.ajax({
                    url: "http://www." + joyconfig.DOMAIN + "/joymeapp/my/modifyaddress",
                    type: "POST",
                    timeout: 5000,
                    data: {profileid: pid, name: name, phone: phone, address: address},
                    dataType: "json",
                    success: function (resMsg) {
                        alert("保存成功！");
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        if (textStatus == "timeout") {
                            alert("请求超时！");
                        }
                        return;
                    }
                });
            });


            $('#textarea_words').on('click', function () {
                if (uno == null || uid == null || token == null || login == null || sign == null || login == 'client') {
                    alert('请在页面的右上角进行登录~');
                    return;
                }
            });

            $('#textarea_words').on('input propertychange' || 'keyup', function () {
                numChange();
            });

            $('#a_wyndj').on('click', function () {
                $("html,body").stop(true);
                $("html,body").animate({
                    scrollTop: $('#div_share').offset().top
                }, 0);
            });

            $('#a_share').on('click', function () {
                $.ajax({
                    url: "http://api.${DOMAIN}/activity/share",
                    type: "post",
                    data: {sid: 100000},
                    dataType: "jsonp",
                    jsonpCallback: "sharecallback",
                    success: function (req) {
                        var resMsg = req[0];
                        if (resMsg.rs == '0') {
                            alert('分享失败~');
                            return;
                        } else if (resMsg.rs == '-1001') {
                            alert('您需要用微博登录并进行分享之后才能获得一次抽奖机会~');
                            return;
                        } else if (resMsg.rs == '-10102') {
                            alert('您需要用微博登录并进行分享之后才能获得一次抽奖机会~');
                            return;
                        } else if (resMsg.rs == '-1') {
                            alert('您需要用微博登录并进行分享之后才能获得一次抽奖机会~');
                            return;
                        } else if (resMsg.rs == '-10114') {
                            alert('您需要用微博登录并进行分享之后才能获得一次抽奖机会~');
                            return;
                        } else if (resMsg.rs == '1') {
                            alert('分享成功~');
                            return;
                        } else {
                            alert(resMsg.msg);
                            return;
                        }
                    },
                    error: function () {
                        alert('获取失败，请刷新');
                        return;
                    }
                });
            });

            $('#a_post').on('click', function () {
                if (postLock) {
                    return;
                }
                var words = $('#textarea_words').val();
                words = words.replace(/\ +/g,"").replace(/[\r\n]/g,"");
                //超出文字提示
                if (getStrlen(words) <= 0) {
                    alert('内容不能为空~');
                    return;
                }
                if (getStrlen(words) > 25) {
                    alert('超过了25个字~');
                    return;
                }

                if (uno == null || uid == null || token == null || login == null || sign == null || login == 'client') {
                    alert('请在页面的右上角进行登录~');
                    return;
                }
                postLock = true;
                $.ajax({
                    url: "http://api.${DOMAIN}/activity/postwords",
                    type: "post",
                    data: {words: words},
                    dataType: "jsonp",
                    jsonpCallback: "postcallback",
                    success: function (req) {
                        var resMsg = req[0];
                        if (resMsg.rs == '0') {
                            alert(resMsg.msg);
                            postLock = false;
                            return;
                        } else if (resMsg.rs == '-1001') {
                            alert('请先保存您的内容，登录之后再回来~');
                            postLock = false;
                            return;
                        } else if (resMsg.rs == '-10102') {
                            alert('请先保存您的内容，登录之后再回来~');
                            postLock = false;
                            return;
                        } else if (resMsg.rs == '-40005') {
                            alert('内容不能为空~');
                            postLock = false;
                            return;
                        } else if (resMsg.rs == '-40017') {
                            alert('内容含有敏感词：' + resMsg.result[0]);
                            postLock = false;
                            return;
                        } else if (resMsg.rs == '-1') {
                            alert('请先保存您的内容，登录之后再回来~');
                            postLock = false;
                            return;
                        } else if (resMsg.rs == '1') {
                            var result = resMsg.result;
                            if (result == null) {
                                return;
                            }
                            var nick = result.nick;
                            if(nick.length > 6){
                                nick = nick.substring(0, 5)+'...';
                            }
                            var html = '<div class="article"><p>' + result.words + '</p><span>BY：' + nick + '</span></div>';
                            if ($('li.show div.showmain div.article').length == 8) {
                                $('li.show div.showmain div.article:last').remove();
                            }
                            $('li.show div.showmain div.article:first').before(html);

                            var htm = $('li.show div.showmain').html();
                            $('li.show div.showmain').html(htm);
                            $('#textarea_words').val('');
                            $('#span_words_tips').html(25);
                            postLock = false;
                            return;
                        } else {
                            alert(resMsg.msg);
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
            });
        });

        function numChange() {
            var words = $('#textarea_words').val();
            //超出文字提示
            $('#span_words_tips').html(25 - getStrlen(words));
        }

        function getStrlen(str) {
            return str.length;
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

        function setCookie(key, value, exDate, env) {
            var cookie = "";
            if (!!key)
                cookie += key + "=" + escape(value) + ";path=/;domain=.joyme" + env + ";expires=" + exDate.toUTCString();
            document.cookie = cookie;
        }

        function imgShow() {
            var imgs = document.getElementById('article-box');
            var imgList = imgs.getElementsByTagName('ul')[0].getElementsByTagName('li');
            var n = document.getElementById('n-btn');
            var imgBtn = document.getElementById('but');
            var btnList = imgBtn.getElementsByTagName('li');
            var iNub = 0;
            addBtn();
            //add
            function addBtn() {
                for (var i = 0; i < imgList.length; i++) {
                    var addLi = document.createElement('li');
                    imgBtn.appendChild(addLi);
                }
                ;
            }

            for (var j = 0; j < btnList.length; j++) {
                btnList[0].className = 'sel';
                btnList[j].index = j;
                btnList[j].onclick = show;
            }
            //下一个
            n.onclick = next;
            //tab切换
            function show() {
                for (var j = 0; j < btnList.length; j++) {
                    btnList[j].className = '';
                    imgList[j].className = '';
                    this.className = 'sel';
                    imgList[this.index].className = 'show';
                    iNub = this.index;
                }
            }

            //下一个
            function next() {
                iNub = iNub + 1;
                if (iNub >= btnList.length) {
                    iNub = 0;
                }
                soll();
            }

            //轮播
            function soll() {
                for (var i = 0; i < btnList.length; i++) {
                    btnList[i].className = '';
                    imgList[i].className = '';
                }
                btnList[iNub].className = 'sel';
                imgList[iNub].className = 'show';
            }
        }
        imgShow();
    </script>        
<script>
$(function(){
	/*rotate*/
        /* var timeOut = function(){  //超时函数
         $("#lotteryBtn").rotate({
         angle:0, //起始角度
         duration: 5000, //转动时间
         animateTo: 2160, //这里是设置请求超时后返回的角度，所以应该还是回到最原始的位置，2160是因为我要让它转6圈，就是360*6得来的
         callback:function(){
         alert('网络超时')
         }
         });
         }; */
        var rotateFunc = function (awards, angle, text) {  //awards:奖项，angle:奖项对应的
            $('#lotteryBtn').stopRotate();//停止旋转动画
            $("#lotteryBtn").rotate({
                angle: 0, //起始角度
                duration: 5000, //转动时间
                animateTo: angle + 1440, //angle是图片上各奖项对应的角度，1440是我要让指针旋转4圈。所以最后的结束的角度就是这样子^^
                callback: function () {
                    $(".lottery-box").show("fast", function () {
                        $(".user-info").hide();
                        if (text == '6') {							
							$(".user-info").show();
                            $(".lottery-box h3 b").text(awards.lotteryAwardName);
                            $(".lottery-box img").attr({src: awards.lotteryAwardPic});
                        } else if (text == '4') {						
							$(".user-info").show();
                            $(".lottery-box h3 b").text(awards.lotteryAwardName);
                            $(".lottery-box img").attr({src: awards.lotteryAwardPic});
                        } else if (text == '5') {						
							$(".user-info").show();
                            $(".lottery-box h3 b").text(awards.lotteryAwardName);
                            $(".lottery-box img").attr({src: awards.lotteryAwardPic});
                        } else if (text == '7') {						
							$(".user-info").show();
                            $(".lottery-box h3 b").text(awards.lotteryAwardName);
                            $(".lottery-box img").attr({src: awards.lotteryAwardPic});
                        } else if (text == '2') {						
							$(".user-info").show();
                            $(".lottery-box h3 b").text(awards.lotteryAwardName);
                            $(".lottery-box img").attr({src: awards.lotteryAwardPic});
                        } else if (text == '3') {						
							$(".user-info").show();
                            $(".lottery-box h3 b").text(awards.lotteryAwardName);
                            $(".lottery-box img").attr({src: awards.lotteryAwardPic});
                        } else if (text == '1') {						
							$(".user-info").show();
                            $(".lottery-box h3 b").text(awards.lotteryAwardName);
                            $(".lottery-box img").attr({src: awards.lotteryAwardPic});
                        } else if (text == '8') {
                            $(".user-info").hide();
                            $(".lottery-box h3 b").text('很遗憾，这次您未抽中奖');
                            $(".lottery-box h3 span").hide();
                        }
                    });
                }
            });
        };
        $("#lotteryBtn").rotate({
            bind: {
                click: function () {
                    $.ajax({
                        url: "http://api.${DOMAIN}/activity/lotteryaward",
                        type: "post",
                        data: {lid: 1000},
                        dataType: "jsonp",
                        jsonpCallback: "lotterycallback",
                        success: function (req) {
                            var resMsg = req[0];
                            if (resMsg.rs == '-1001') {
                                alert('您需要用微博登录并进行分享之后才能获得一次抽奖机会~');
                                return;
                            } else if (resMsg.rs == '-10102') {
                                alert('您需要用微博登录并进行分享之后才能获得一次抽奖机会~');
                                return;
                            } else if (resMsg.rs == '-1') {
                                alert('您需要用微博登录并进行分享之后才能获得一次抽奖机会~');
                                return;
                            } else if (resMsg.rs == '-902') {
                                alert('您今天已经抽过一次了，请明天再来~');
                                return;
                            } else if (resMsg.rs == '-904') {
                                alert('您需要用微博登录并进行分享之后才能获得一次抽奖机会~');
                                return;
                            } else if (resMsg.rs == '-10114') {
                                alert('您需要用微博登录并进行分享之后才能获得一次抽奖机会~');
                                return;
                            } else if (resMsg.rs == '1') {
                                var result = resMsg.result;
                                if (result == null) {
                                    rotateFunc(result, 112, '8')
                                }
                                var level = result.lotteryAwardLevel;
                                if (level == 1) {
                                    rotateFunc(result, 67, '1')
                                }
                                if (level == 2) {
                                    rotateFunc(result, 292, '2')
                                }
                                if (level == 3) {
                                    rotateFunc(result, 202, '3')
                                }
                                if (level == 4) {
                                    rotateFunc(result, 247, '4')
                                }
                                if (level == 5) {
                                    rotateFunc(result, 22, '5')
                                }
                                if (level == 6) {
                                    rotateFunc(result, 337, '6')
                                }
                                if (level == 7) {
                                    rotateFunc(result, 157, '7')
                                }
                                if (level == 0) {
                                    rotateFunc(result, 112, '8')
                                }
                            } else {
                                rotateFunc(result, 112, '8')
                            }
                        },
                        error: function () {
                            alert('获取失败，请刷新');
                            return;
                        }
                    });
                }
            }
        })	
})
</script>
</div>
<script type="text/javascript">
    var _paq = _paq || [];
    _paq.push(['trackPageView']);
    _paq.push(['enableLinkTracking']);
    (function() {
        var u="//stat.joyme.com/";
        var n="//static.joyme.com/pc/piwik/";
        _paq.push(['setTrackerUrl', u+'piwik.php']);
        _paq.push(['setSiteId', 901]);
        var d=document, g=d.createElement('script'), s=d.getElementsByTagName('script')[0];
        g.type='text/javascript'; g.async=true; g.defer=true; g.src=n+'piwik.js'; s.parentNode.insertBefore(g,s);
    })();
</script>
<noscript><p><img src="//stat.joyme.com/piwik.php?idsite=901" style="border:0;" alt="" /></p></noscript>
</body>
</html>
