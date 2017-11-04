<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <title>${line.lineName}</title>
    <link href="${URL_LIB}/static/theme/default/css/wap_common_rmlb.css" rel="stylesheet" type="text/css">
    <link href="${URL_LIB}/static/theme/default/css/rmlb.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.11.0.min.js"></script>
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false;
            }, true);
        }, true);
        $(function () {
            window.mainObj = {
                "preKey": "default",       //记录上一个条目的日期
                "preId": "defaultId"      //记录上一个日期的div的id
            };
            mainObj.queryData = {"pnum": 1, "pcount": 16, lineCode: "${lineCode}", platform: "${platform}"};
            window.loadFlag = {
                "loadTime": null,
                "isLoading": false,
                "maxFlag": false,
                "loadComment": function () {
                    $('#loadTips').remove();
                    getData();
                }
            };

            window.dateToStr = (function () {
                var todayBegin = new Date();
                todayBegin.setHours(0);
                todayBegin.setMinutes(0);
                todayBegin.setSeconds(0);
                todayBegin.setMilliseconds(0);
                var oneday = 1000 * 60 * 60 * 24;
                var today = todayBegin.getTime();
                var yesterday = todayBegin.getTime() - oneday;
                var tomorrow = todayBegin.getTime() + oneday;
                return function (createtime) {
                    if (createtime >= today && createtime < tomorrow) {
                        return "today";
                    } else if (createtime >= yesterday && createtime < today) {
                        return "yesterday";
                    } else {
                        var time = new Date(createtime);
                        return time.getFullYear() + '-' + (time.getMonth() + 1) + '-' + time.getDate();

                    }
                };
            })();

            getData();

            var box = $('.wrapper');
            box.scroll(toLoadMore);

        });

        function getData() {

            mainObj.queryData.qnum = new Date().getTime();
            $.ajax({
                url: "http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/api/game/todayrec",
                data: mainObj.queryData,
                type: "POST",
                dataType: "json",
                success: function (data, textStatus) {
                    if (data.errorStatus == 1) {
                        $("#popup").text(data.errorMessage);
                        $("#popup").addClass("close");
                        var t = setTimeout(function () {
                            $("#popup").removeClass("close");
                            clearTimeout(t);
                        }, 3000);
                        return;
                    }

                    var toInsert;
                    $.each(data.list, function (index, item) {

                        var key = dateToStr(item.createtime);

                        toInsert = '';
                        if (mainObj.preKey != key) {
                            mainObj.preId = key;
                            toInsert += '<div class="rm-box">';
                            var dataStr;
                            if (key === 'today') {
                                dataStr = '今天';
                            } else if (key === 'yesterday') {
                                dataStr = '昨天';
                            } else {
                                dataStr = key;
                            }
                            toInsert += '<p>' + dataStr + '&nbsp;&nbsp;<span></span>个游戏</p>';
                            toInsert += '<div class="rm-cont clearfix" ' + 'id="' + key + '">';
                        }


                        toInsert += '<div class="rm-block">';
                        toInsert += '<a href="javascript:void(0);" onclick="toJump(this);"  data-jt="' + item.jt + '" data-ji="' + item.ji + '" >';

                        var tagStr = "";
                        if (item.tag != '') {
                            tagStr = '<img src="' + item.tag + '" alt="" />';
                        }

                        toInsert += '<cite>' + tagStr + '</cite>';
                        var nameStr = item.name;
                        if (item.name != null && item.name.length > 7) {
                            nameStr = item.name.substr(0, 7);
                        }
                        toInsert += '<b>' + nameStr + '</b>';
                        toInsert += '<div class="rm-icon">';

                        var flagStr = "";
                        if (item.flag != '') {
                            flagStr = '<b>' + item.flag + '</b>';
                        }

                        toInsert += '<cite>' + flagStr + '<img src="' + item.icon + '" alt="" /></cite>'
                        toInsert += '<p>';
                        var typeStr = item.type;
                        if (item.type != null && item.type.length > 5) {
                            typeStr = item.type.substr(0, 5);
                        }
                        toInsert += '<span>' + typeStr + '</span>';
                        if (item.showtype === 'likes') {
                            toInsert += '<span>' + item.likes + '人期待</span>';
                        } else {
                            var xingStr = '';
                            switch (true) {
                                case item.rate > 0 && item.rate <= 1 :
                                    xingStr = '<em class="xing-half"></em>';
                                    break;
                                case item.rate > 1 && item.rate <= 2 :
                                    xingStr = '<em></em>';
                                    break;
                                case item.rate > 2 && item.rate <= 3 :
                                    xingStr = '<em></em><em class="xing-half"></em>';
                                    break;
                                case item.rate > 3 && item.rate <= 4 :
                                    xingStr = '<em></em><em></em>';
                                    break;
                                case item.rate > 4 && item.rate <= 5 :
                                    xingStr = '<em></em><em></em><em class="xing-half"></em>';
                                    break;
                                case item.rate > 5 && item.rate <= 6 :
                                    xingStr = '<em></em><em></em><em></em>';
                                    break;
                                case item.rate > 6 && item.rate <= 7 :
                                    xingStr = '<em></em><em></em><em></em><em class="xing-half"></em>';
                                    break;
                                case item.rate > 7 && item.rate <= 8 :
                                    xingStr = '<em></em><em></em><em></em><em></em>';
                                    break;
                                case item.rate > 8 && item.rate <= 9 :
                                    xingStr = '<em></em><em></em><em></em><em></em><em class="xing-half"></em>';
                                    break;
                                case item.rate > 9 && item.rate <= 10 :
                                    xingStr = '<em></em><em></em><em></em><em></em><em></em>';
                                    break;
                                default:
                                    break;
                            }
                            toInsert += '<span>' + xingStr + '</span>';
                        }
                        var referralStr = item.referral;
                        if (item.referral != null && item.referral.length > 7) {
                            referralStr = item.referral.substr(0, 7);
                        }
                        toInsert += '<span>' + referralStr + '</span>';
                        toInsert += '</p></div></a></div>';

                        if (mainObj.preKey != key) {
                            toInsert += '</div>';
                            $(".wrapper").append(toInsert);
                        } else {
                            $("#" + mainObj.preId).append(toInsert);
                        }

                        mainObj.preKey = key;
                    });

                    if (data.maxPage <= mainObj.queryData.pnum) {
                        loadFlag.maxFlag = true;
                    }
                    mainObj.queryData.pnum += 1;
                },
                complete: function (XMLHttpRequest, textStatus) {
                    loadFlag.isLoading = false;
                    $('#loadTips').remove();   //确保删除
                    toCount();
                }
            });
        }
        //查每个日期下有多少个子元素
        function toCount() {
            $.each($(".rm-cont"), function (index, item) {
                var block = $(this).find("div.rm-block");
                if (block.length > 0) {
                    $(this).prev().find("span").html(block.length);
                }
            });
        }
        function toJump(obj) {
            var ji = $(obj).attr("data-ji");
            var jt = $(obj).attr("data-jt");
            if (jt == '' && ji == '') {
                return false;
            }
            //  ji = ji.replace(/\?/g, encodeURIComponent("?")).replace(/[&]/g, encodeURIComponent("&"));
            ji = encodeURIComponent(ji);
            _jclient.jump('jt=' + jt + '&ji=' + ji);
            return false;
        }


        function toLoadMore() {
            if (loadFlag.maxFlag) {
                return false;
            }

//            var sTop = $(window).scrollTop();
//            var sHeight = $(document).height();
//            var sMainHeight = $(window).height();
//            var sNum = sHeight - sMainHeight;

            var sTop = $('.wrapper')[0].scrollTop + 45;
            var sHeight = $('.wrapper')[0].scrollHeight;
            var sMainHeight = $('.wrapper').height();
            var sNum = sHeight - sMainHeight;


            //console.log("sTop-->" + sTop + "sHeight->" + sHeight + "sMainHeight-->" + sMainHeight + "sNum-->" + (sHeight - sMainHeight));

            if (sTop >= sNum && !loadFlag.isLoading) {
                if (mainObj.queryData.pnum > 1) {
                    loadFlag.isLoading = true;

                    var loadTips = "<div id='loadTips'><cite></cite><b>加载更多</b></div>";
                    $(".wrapper").append(loadTips);
                    loadFlag.loadTime = setTimeout(function () {
                        loadFlag.loadComment();
                    }, 1000);
                }
            }
        }


    </script>
<body>
<div class="black-dialog" id="popup"></div>
<div class="wrapper">

</div>
</body>
</html>