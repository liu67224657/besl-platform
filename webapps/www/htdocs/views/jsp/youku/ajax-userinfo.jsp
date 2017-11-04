<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/views/jsp/common/youkulibs.jsp" %>
<script type="text/javascript">
    var nowTime1 = ${nowDate}-1000;
    var nowTime2 = ${nowDate}-1000;
    $(function () {
        var profile = '${profile}';
        if (profile == '') {
            $("#signdays").html("尚未签到");
        } else {
            var signComplete = '${signcomplete}';
            if (signComplete == 'true') {
                $("#signdays").html("已经签到${signnum}天");
            } else {

                $("#signdays").html("尚未签到");
            }
        }

        var uid = '${profile == null ? -1 : profile.uid}'
        $('#input_hidden_uid').val(uid);

        var start1 = $("#startTime1").val();
        var start2 = $("#startTime2").val();
        if (start1 != null && start1 != undefined) {
            timeOut1($('#zg-count1'), start1, 1, nowTime1);//中国北京时间
        }
        if (start2 != null && start2 != undefined) {
            timeOut2($('#zg-count2'), start2, 2, nowTime2);//中国北京时间
        }
    });
    function timeOut1(parent, time, id, nowTime) {
        var parents = parent;
        var timeNum = time;
        var id = id;
        timeNum = timeNum.replace('-', '/').replace('-', '/');
        timeNum = timeNum.replace('.0', '');
        function zero(num) {
            if (num < 10) {
                return '0' + num;
            } else if (num >= 10) {
                return '' + num;
            }
        }

        var time = setInterval(function () {
            checkTime();
        }, 1000);

        function checkTime() {
            var future = Date.parse(timeNum);
            nowTime = nowTime + 1000;
            var mistiming = (future - nowTime) / 1000;
            var d = zero(parseInt(mistiming / 86400));
            var h = zero(parseInt((mistiming) / 3600));
            var f = zero(parseInt((mistiming % 86400 % 3600) / 60));
            var m = zero(parseInt(mistiming % 86400 % 360 % 60));
            var daybox = parents.children('.day'),
                    hourbox = parents.children('.hour'),
                    minutebox = parents.children('.minute'),
                    secondbox = parents.children('.second');
            daybox.children('code').eq(0).text(d.substr(0, 1));
            daybox.children('code').eq(1).text(d.substr(1, 1));
            hourbox.children('code').eq(0).text(h.substr(0, 1));
            hourbox.children('code').eq(1).text(h.substr(1, 1));
            minutebox.children('code').eq(0).text(f.substr(0, 1));
            minutebox.children('code').eq(1).text(f.substr(1, 1));
            secondbox.children('code').eq(0).text(m.substr(0, 1));
            secondbox.children('code').eq(1).text(m.substr(1, 1));
            if (parseInt(future) < parseInt(nowTime)) {
                clearInterval(time);
                var gid = $("#aid" + id).val();
                $("#list-box" + id).attr("href", "/youku/webview/giftmarket/detail?aid=" + gid);
                var msg = $("#msg" + id);
                msg.text("马上抢");
                msg.addClass("msg3").removeClass("msg");
                $("#clearfix" + id).css("display", "none");
                $("#surplus" + id).css("display", "block");

            }
        }
    }

    function timeOut2(parent, time, id, nowTime) {
        var parents = parent;
        var timeNum = time;
        var id = id;
        timeNum = timeNum.replace('-', '/').replace('-', '/');
        timeNum = timeNum.replace('.0', '');
        function zero(num) {
            if (num < 10) {
                return '0' + num;
            } else if (num >= 10) {
                return '' + num;
            }
        }

        var time = setInterval(function () {
            checkTime();
        }, 1000);

        function checkTime() {
            var future = Date.parse(timeNum);
            nowTime = nowTime + 1000;
            var mistiming = (future - nowTime) / 1000;
            var d = zero(parseInt(mistiming / 86400));
            var h = zero(parseInt((mistiming) / 3600));
            var f = zero(parseInt((mistiming % 86400 % 3600) / 60));
            var m = zero(parseInt(mistiming % 86400 % 360 % 60));
            var daybox = parents.children('.day'),
                    hourbox = parents.children('.hour'),
                    minutebox = parents.children('.minute'),
                    secondbox = parents.children('.second');
            daybox.children('code').eq(0).text(d.substr(0, 1));
            daybox.children('code').eq(1).text(d.substr(1, 1));
            hourbox.children('code').eq(0).text(h.substr(0, 1));
            hourbox.children('code').eq(1).text(h.substr(1, 1));
            minutebox.children('code').eq(0).text(f.substr(0, 1));
            minutebox.children('code').eq(1).text(f.substr(1, 1));
            secondbox.children('code').eq(0).text(m.substr(0, 1));
            secondbox.children('code').eq(1).text(m.substr(1, 1));
            if (parseInt(future) < parseInt(nowTime)) {
                clearInterval(time);
                var gid = $("#aid" + id).val();
                $("#list-box" + id).attr("href", "/youku/webview/giftmarket/detail?aid=" + gid);
                var msg = $("#msg" + id);
                msg.text("马上抢");
                msg.addClass("msg3").removeClass("msg");
                $("#clearfix" + id).css("display", "none");
                $("#surplus" + id).css("display", "block");

            }
        }
    }
</script>
<div class="header-box borbottom" id="yk_userinfo">
    <input type="hidden" value="" id="input_hidden_uid"/>
    <dl>
        <a href="${ykt:ykLogin(pageContext.request,'/youku/webview/giftmarket/mygift','/youku/webview/giftmarket/list')}"
           id="a_yk_grzx">
            <dt><img src="${URL_LIB}/static/theme/wap/images/youku/club-1.png" alt=""></dt>
            <dd>
                <p>个人中心</p>
                    <span>
                        <c:choose>
                            <c:when test="${not empty userPoint && profile!=null}">
                                ${userPoint.userPoint}酷豆
                            </c:when>
                            <c:otherwise>
                                请先登录
                            </c:otherwise>
                        </c:choose>

                        </span>
            </dd>
        </a>
    </dl>
    <dl>
        <a href="/youku/webview/task/signpage?platform=0">
            <dt><img src="${URL_LIB}/static/theme/wap/images/youku/club-2.png" alt=""></dt>
            <dd>
                <p>每日签到</p>
                <span id="signdays"></span>
            </dd>
        </a>
    </dl>
    <dl>
        <a href="/youku/webview/giftmarket/novice">
            <dt><img src="${URL_LIB}/static/theme/wap/images/youku/club-3.png" alt=""></dt>
            <dd>
                <p>新手指南</p>
                <span>玩转俱乐部</span>
            </dd>
        </a>
    </dl>
</div>
<!-- header-box-end -->
<c:if test="${not empty seckillDTOs}">
    <div class="page-box1 padd10 borbottom">
        <h2 class="title no-xian"><span>极速秒杀</span></h2>
        <c:forEach items="${seckillDTOs}" var="seckillDTO" varStatus="index">
            <c:choose>
                <c:when test="${seckillDTO.startTime.time>nowDate}">
                    <dl>
                        <a href="/youku/webview/giftmarket/detail?aid=${seckillDTO.goodsId}&secid=${seckillDTO.seckillId}" class="list-box"
                           id="list-box${index.index+1}">
                            <dt>
                                <input type="hidden" id="aid${index.index+1}" value="${seckillDTO.goodsId}"/>
                                <img src="${seckillDTO.activityDTO.gipic}" alt="">
                                <c:if test="${not empty seckillDTO.activityDTO.supscript}">
                                    <b class="blud-hot"
                                    <c:if test="${not empty seckillDTO.activityDTO.color}">style='background-color:${seckillDTO.activityDTO.color}'</c:if>>${seckillDTO.activityDTO.supscript}</b>
                                </c:if>
                            </dt>
                            <dd>
                                <p>${seckillDTO.activityDTO.title}</p>
                            <span id="surplus${index.index+1}" style="display:none;">
                               <em>剩余</em>
                               <i class='surplus'>
                                   <cite class="sur-top" <c:choose><c:when test="${seckillDTO.restsum == seckillDTO.totals}">style="width:${seckillDTO.restsum/seckillDTO.totals*100}%;border-radius: 1.2rem;"</c:when><c:otherwise>style="width:${seckillDTO.restsum/seckillDTO.totals*100}%;"</c:otherwise></c:choose>></cite>
                                   <b <c:choose><c:when test="${seckillDTO.restsum == seckillDTO.totals}">class="sur-top-val b-full"</c:when><c:otherwise>class="sur-top-val"</c:otherwise></c:choose>>
                                   <fmt:formatNumber type='number' value='${seckillDTO.restsum/seckillDTO.totals*100}' maxFractionDigits='0'></fmt:formatNumber>%</b>
                               </i>
                           </span>
                                <em class="clearfix" id="clearfix${index.index+1}">
                                    <span>还有</span>

                                    <div class="count-down">
                                        <div class="zg-count" id="zg-count${index.index+1}">
                                            <input type="hidden" value="${seckillDTO.startTime}"
                                                   id="startTime${index.index+1}"/>

                                            <div class="hour">
                                                <code></code>
                                                <code></code>
                                            </div>
                                            <div class="minute">
                                                <code></code>
                                                <code></code>
                                            </div>
                                            <div class="second">
                                                <code></code>
                                                <code></code>
                                            </div>
                                        </div>
                                    </div>

                                    <em>${item.ykdesc}</em>
                                </em>

                            </dd>
                            <p class="msg" id="msg${index.index+1}">即将开抢</p>
                        </a>
                    </dl>
                </c:when>
                <c:otherwise>
                    <dl>
                        <a href="/youku/webview/giftmarket/detail?aid=${seckillDTO.goodsId}&secid=${seckillDTO.seckillId}" class="list-box">
                            <dt>
                                <img src="${seckillDTO.activityDTO.gipic}" alt="">
                                <c:if test="${not empty seckillDTO.activityDTO.supscript}">
                                    <b class="blud-hot"
                                    <c:if test="${not empty seckillDTO.activityDTO.color}">style='background-color:${seckillDTO.activityDTO.color}' </c:if>
                                    >${seckillDTO.activityDTO.supscript}</b>
                                </c:if>
                            </dt>
                            <dd>
                                <p>${seckillDTO.activityDTO.title}</p>
                           <span>
                               <em>剩余</em>
                               <i class='surplus'>
                                   <cite class="sur-top" <c:choose><c:when test="${seckillDTO.restsum == seckillDTO.totals}">style="width:${seckillDTO.restsum/seckillDTO.totals*100}%;border-radius: 1.2rem;"</c:when><c:otherwise>style="width:${seckillDTO.restsum/seckillDTO.totals*100}%;"</c:otherwise></c:choose>></cite>
                                   <b <c:choose><c:when test="${seckillDTO.restsum == seckillDTO.totals}">class="sur-top-val b-full"</c:when><c:otherwise>class="sur-top-val"</c:otherwise></c:choose>>
                                   <fmt:formatNumber type='number' value='${seckillDTO.restsum/seckillDTO.totals*100}' maxFractionDigits='0'></fmt:formatNumber>%</b>
                               </i>
                           </span>
                            </dd>
                            <c:choose>
                                <c:when test="${seckillDTO.restsum>0}">
                                    <p class="msg3">马上抢</p>
                                </c:when>
                                <c:otherwise>
                                    <p class="msg">抢光了</p>
                                </c:otherwise>
                            </c:choose>
                        </a>
                    </dl>
                </c:otherwise>
            </c:choose>

        </c:forEach>
    </div>
</c:if>

<!-- end -->
<div class="page-box1 padd10">
    <c:choose>
        <c:when test="${not empty activityDTOs}">
            <h2 class="title"><span>酷豆精选</span></h2>
            <c:forEach items="${activityDTOs}" var="item">
                <dl>
                    <a href="/youku/webview/giftmarket/detail?aid=${item.gid}" class="list-box">
                        <dt>
                            <img src="${item.gipic}" alt="">
                            <c:if test="${not empty item.supscript}">
                                <b class="blud-hot"
                                <c:if test="${not empty item.color}">style='background-color:${item.color}' </c:if>
                                >${item.supscript}</b>
                            </c:if>

                        </dt>
                        <dd>
                            <p>${item.title}</p>
                            <cite>市场价${item.price}元</cite>

                            <h3><b>${item.point}</b>酷豆</h3>
                        </dd>
                        <c:choose>
                            <c:when test="${item.sn>0}">
                                <p class="msg2">兑换</p>
                            </c:when>
                            <c:otherwise>
                                <p class="msg">抢光了</p>
                            </c:otherwise>
                        </c:choose>
                    </a>
                </dl>
            </c:forEach>
        </c:when>
        <c:otherwise>

        </c:otherwise>
    </c:choose>


</div>
<script>
    $(document).ready(function () {
        $('.surplus').each(function () {
            var sw = $(this).width();
            var oCite = $(this).find('.sur-top').width();
            var oNin = $(this).find('.sur-top-val');
            var len = sw * 0.65;
            if (oCite > len) {
                oNin.addClass('progress-tiao');
            } else {
                oNin.removeClass('progress-tiao');
            }
        });

        $('#a_yk_grzx').on('touchstart', function () {
            var userAgentInfo = navigator.userAgent.toLowerCase();
            if (userAgentInfo.indexOf('youku hd') == -1 && userAgentInfo.indexOf('youku') == -1) {
                alert("请先登录");
                return false;
            }
        });
    });
</script>