<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/views/jsp/common/youkulibs.jsp" %>
<dl>
    <dt><cite><img
            src="${URL_LIB}/static/theme/youku/images/jlb-logo.png"
            alt=""></cite></dt>
    <dd>
        <cite><code id="seckill_script"></code></cite>

        <h2>
            <b id="seckill_tips"></b>

            <p id="seckill_desc"></p>
        </h2>
    </dd>
</dl>
<script>
    var nowTimeLong = '';
    Date.prototype.format = function (format) {
        console.log(this.getDate())
        var o = {
            "M+": this.getMonth() + 1, //month
            "d+": this.getDate(), //day
            "h+": this.getHours(), //hour
            "m+": this.getMinutes(), //minute
            "s+": this.getSeconds(), //second
            "q+": Math.floor((this.getMonth() + 3) / 3), //quarter
            "S": this.getMilliseconds() //millisecond
        }
        if (/(y+)/.test(format)) format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(format))
                format = format.replace(RegExp.$1,
                        RegExp.$1.length == 1 ? o[k] :
                                ("00" + o[k]).substr(("" + o[k]).length));
        return format;
    }
    //倒计时
    function zero(num) {
        if (num < 10) {
            return '0' + num;
        } else if (num >= 10) {
            return '' + num;
        }
    }

    function timeOutIndex(time) {
        var timeNum = time;
        var times = null;

        times = setInterval(function () {
            checkTime();
        }, 1000);

        function checkTime() {
            var future = Date.parse(timeNum);
            var nowTime = nowTimeLong;
            if (nowTimeLong == "") {
                nowTime = new Date().getTime();
            } else {
                nowTimeLong += 1000;
            }
            var mistiming = (future - nowTime) / 1000,
                    h = zero(parseInt((mistiming) / 3600)),
                    f = zero(parseInt((mistiming % 86400 % 3600) / 60)),
                    m = zero(parseInt(mistiming % 86400 % 360 % 60));
            getTimes(h, f, m);
            if (future <= nowTime) {
                clearInterval(times);
                setTimes();
                return false;
            }
        }

        function getTimes(h, f, m) {
            $('#seckill_desc').html('<span>' + h + '</span>:<span>' + f + '</span>:<span>' + m + '</span>后开抢!');
        }
    }

    function setTimes() {
        var goodsSeckill = '${goodsSeckill}';
        var end = '';
        if (goodsSeckill != null && goodsSeckill != 'null' && goodsSeckill != '' && goodsSeckill != undefined) {
            $('#seckill_tips').html('${goodsSeckill.inTips}');
            $('#seckill_desc').html('正在秒杀中！快来免费抢');
            var times = null;
            times = setInterval(function () {
                end = new Date(${goodsSeckill.endTime.getTime()});
                if (end.getTime() <= nowTimeLong) {
                    clearInterval(times);
                    $('#seckill_tips').html('${goodsSeckill.afterTips}');
                    $('#seckill_desc').html('敬请期待下一次秒杀');
                }
                nowTimeLong += 1000;
            }, 1000);
        }
    }
    //把下面代码copy下
    $(document).ready(function () {
        var goodsSeckill = '${goodsSeckill}';
        if (goodsSeckill != null && goodsSeckill != 'null' && goodsSeckill != '' && goodsSeckill != undefined) {
            $('#seckill_script').html('秒杀');
            if ($('#a_jlb').hasClass('dh')) {
                $('#a_jlb').removeClass('dh');
            }
            if (!$('#a_jlb').hasClass('ms')) {
                $('#a_jlb').addClass('ms');
            }
            var nowTime = new Date(${currentTime});
            nowTimeLong = new Date(${currentTime}).getTime();
            var start = new Date(${goodsSeckill.startTime.getTime()});

            var end = new Date(${goodsSeckill.endTime.getTime()});
            if (nowTime.getTime() < start.getTime()) {
                $('#seckill_tips').html('${goodsSeckill.beforeTips}');
                var mistiming = (start.getTime() - nowTimeLong) / 1000;
                var h = zero(parseInt((mistiming) / 3600));
                var f = zero(parseInt((mistiming % 86400 % 3600) / 60));
                var m = zero(parseInt(mistiming % 86400 % 360 % 60));
                $('#seckill_desc').html('<span>' + h + '</span>:<span>' + f + '</span>:<span>' + m + '</span>后开抢!');
                timeOutIndex(start.format('yyyy/MM/dd hh:mm:ss')); //中国北京时间
            } else if (nowTime.getTime() > end.getTime()) {
                $('#seckill_tips').html('${goodsSeckill.afterTips}');
                $('#seckill_desc').html('敬请期待下一次秒杀');
            } else {
                $('#seckill_tips').html('${goodsSeckill.inTips}');
                $('#seckill_desc').html('正在秒杀中！快来免费抢');
                var times = null;
                times = setInterval(function () {
                    if (end.getTime() <= nowTimeLong) {
                        clearInterval(times);
                        $('#seckill_tips').html('${goodsSeckill.afterTips}');
                        $('#seckill_desc').html('敬请期待下一次秒杀');
                    }
                    nowTimeLong += 1000;
                }, 1000);
            }
        } else {
            var goodsId = '${activityGoods == null ? null : activityGoods.geid}';
            if (goodsId != null && goodsId != 'null' && goodsId != '' && goodsId != undefined) {
                if ($('#a_jlb').hasClass('ms')) {
                    $('#a_jlb').removeClass('ms');
                }
                if (!$('#a_jlb').hasClass('dh')) {
                    $('#a_jlb').addClass('dh');
                }
                $('#seckill_script').html('兑换');
                $('#seckill_tips').html('${activityGoods.yktitle}');
                $('#seckill_desc').html('${activityGoods.ykdesc}');
            } else {
                if ($('#a_jlb').hasClass('ms')) {
                    $('#a_jlb').removeClass('ms');
                }
                if (!$('#a_jlb').hasClass('dh')) {
                    $('#a_jlb').addClass('dh');
                }
                $('#seckill_script').html('兑换');
                $('#seckill_tips').html('智能产品、优酷会员卡');
                $('#seckill_desc').html('多种宝贝，正在火热兑换中');
            }
        }
    });
</script>