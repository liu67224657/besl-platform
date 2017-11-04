<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <meta name="Keywords" content="新游开测">
    <meta name="description" content="新游开测"/>
    <title>新游开测</title>
    <link href="${URL_LIB}/static/theme/default/css/wap_common_xykc.css" rel="stylesheet" type="text/css">
    <link href="${URL_LIB}/static/theme/default/css/wap_xykc.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.11.0.min.js"></script>
    <%--   <script type="text/javascript" src="${URL_LIB}/static/js/common/app.js"></script> --%>
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
    </script>
</head>
<body>
<div id="wrapper">
    <div class="popup_box">
        <img src="${URL_LIB}/static/theme/wap/images/popup.jpg" alt=""/>
    </div>
    <div class="mark_box"></div>

    <div class="yxxq_tab_title">
        <a href="javascript:void(0);" class="active"><span>热门开测</span></a>
        <a href="javascript:void(0);"><span>即将开测</span></a>
        <a href="javascript:void(0);"><span>已经开测</span></a>
    </div>
    <div class="timeShaft-tit"><p class="timeShaft-main-time"></p></div>
    <div class="black-dialog" id="popup"></div>
    <div class="wrapper" style="margin-top:35px;">
        <!--热门开测-->
        <div class="timeShaft-main cur_timeBox">
            <div class="timeShaft-main-box" id="wrapper-hot">
                <!--时间轴-->
                <c:if test="${not empty hotblock}">
                    <div class="timeShaft-main-block hot first_box"><span></span>

                        <p class="timeShaft-main-time hot">热门</p>
                        <c:forEach items="${hotblock}" var="item" varStatus="st">
                            <div class="timeShaft-block-one <c:choose><c:when test='${st.index == 0}'>ma</c:when><c:otherwise></c:otherwise></c:choose> ">
                                <a href="javascript:void(0);" onclick="toJumpToGameDetail(this);"
                                   data-jt="${item.jt}" data-ji="${item.ji}">
                                    <div class="timeShaft-block-l fl">
                                        <img src="${item.iconurl}" alt=""/>
                                    </div>
                                    <div class="timeShaft-block-r fl">
                                        <h1>${item.name}</h1>

                                        <h2>${item.customContent}</h2>

                                        <h3><c:choose>
                                            <c:when test="${not empty item.reason&&fn:length(item.reason) > 13}">
                                                <c:out value="${fn:substring(item.reason, 0, 13)}..."/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="${item.reason}"/>
                                            </c:otherwise>
                                        </c:choose></h3>
                                    </div>
                                    <b class="timeShaft_btn"></b> </a>
                            </div>
                        </c:forEach>
                    </div>
                </c:if>

                <c:if test="${ not empty tomorrow}">
                    <div class="timeShaft-main-block tomorrow <c:choose><c:when test='${empty hotblock}'>first_box</c:when><c:otherwise></c:otherwise></c:choose> ">
                        <span></span>

                        <p class="timeShaft-main-time tomorrow">明日</p>
                        <c:forEach items="${tomorrow}" var="item" varStatus="st">
                            <div class="timeShaft-block-one <c:choose><c:when test='${st.index == 0}'>ma</c:when><c:otherwise></c:otherwise></c:choose> ">
                                <a href="javascript:void(0);" onclick="toJumpToGameDetail(this);"
                                   data-jt="${item.jt}" data-ji="${item.ji}">
                                    <div class="timeShaft-block-l fl">
                                        <img src="${item.iconurl}" alt=""/></div>
                                    <div class="timeShaft-block-r fl">
                                        <h1>${item.name}</h1>

                                        <h2>${item.customContent}</h2>

                                        <h3><c:choose>
                                            <c:when test="${not empty item.reason&&fn:length(item.reason) > 13}">
                                                <c:out value="${fn:substring(item.reason, 0, 13)}..."/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="${item.reason}"/>
                                            </c:otherwise>
                                        </c:choose></h3>
                                    </div>
                                    <b class="timeShaft_btn"></b> </a></div>
                        </c:forEach>
                    </div>
                </c:if>

                <c:if test="${ not empty  yesterday}">
                    <div class="timeShaft-main-block yesterday <c:choose><c:when test='${empty hotblock&&empty tomorrow}'>first_box</c:when><c:otherwise></c:otherwise></c:choose> ">
                        <span></span>

                        <p class="timeShaft-main-time yesterday">昨日</p>
                        <c:forEach items="${yesterday}" var="item" varStatus="st">
                            <div class="timeShaft-block-one  <c:choose><c:when test='${st.index == 0}'>ma</c:when><c:otherwise></c:otherwise></c:choose> ">
                                <a href="javascript:void(0);" onclick="toJumpToGameDetail(this);"
                                   data-jt="${item.jt}" data-ji="${item.ji}">
                                    <div class="timeShaft-block-l fl">
                                        <img src="${item.iconurl}" alt=""/></div>
                                    <div class="timeShaft-block-r fl">
                                        <h1>${item.name}</h1>

                                        <h2>${item.customContent}</h2>

                                        <h3><c:choose>
                                            <c:when test="${not empty item.reason&&fn:length(item.reason) > 13}">
                                                <c:out value="${fn:substring(item.reason, 0, 13)}..."/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="${item.reason}"/>
                                            </c:otherwise>
                                        </c:choose></h3>
                                    </div>
                                    <b class="timeShaft_btn"></b> </a></div>
                        </c:forEach>
                    </div>
                </c:if>


                <c:if test="${not empty futureweek}">
                    <div class="timeShaft-main-block future  <c:choose><c:when test='${empty hotblock&&empty tomorrow&&empty yesterday}'>first_box</c:when><c:otherwise></c:otherwise></c:choose> ">
                        <span></span>

                        <p class="timeShaft-main-time future">未来一周</p>
                        <c:forEach items="${futureweek}" var="item" varStatus="st">
                            <div class="timeShaft-block-one  <c:choose><c:when test='${st.index == 0}'>ma</c:when><c:otherwise></c:otherwise></c:choose> ">
                                <a href="javascript:void(0);" onclick="toJumpToGameDetail(this);"
                                   data-jt="${item.jt}" data-ji="${item.ji}">
                                    <div class="timeShaft-block-l fl">
                                        <img src="${item.iconurl}" alt=""/></div>
                                    <div class="timeShaft-block-r fl">
                                        <h1>${item.name}</h1>

                                        <h2>${item.customContent}</h2>

                                        <h3><c:choose>
                                            <c:when test="${not empty item.reason&&fn:length(item.reason) > 13}">
                                                <c:out value="${fn:substring(item.reason, 0, 13)}..."/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="${item.reason}"/>
                                            </c:otherwise>
                                        </c:choose></h3>
                                    </div>
                                    <b class="timeShaft_btn"></b> </a></div>
                        </c:forEach>
                    </div>
                </c:if>
                <c:if test="${not empty pastweek}">
                    <div class="timeShaft-main-block formerly  <c:choose><c:when test='${empty hotblock&&empty tomorrow&&empty yesterday && empty futureweek}'>first_box</c:when><c:otherwise></c:otherwise></c:choose> ">
                        <span></span>

                        <p class="timeShaft-main-time formerly">过去一周</p>
                        <c:forEach items="${pastweek}" var="item" varStatus="st">
                            <div class="timeShaft-block-one  <c:choose><c:when test='${st.index == 0}'>ma</c:when><c:otherwise></c:otherwise></c:choose> ">
                                <a href="javascript:void(0);" onclick="toJumpToGameDetail(this);"
                                   data-jt="${item.jt}" data-ji="${item.ji}">
                                    <div class="timeShaft-block-l fl">
                                        <img src="${item.iconurl}" alt=""/></div>
                                    <div class="timeShaft-block-r fl">
                                        <h1>${item.name}</h1>

                                        <h2>${item.customContent}</h2>

                                        <h3><c:choose>
                                            <c:when test="${not empty item.reason&&fn:length(item.reason) > 13}">
                                                <c:out value="${fn:substring(item.reason, 0, 13)}..."/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="${item.reason}"/>
                                            </c:otherwise>
                                        </c:choose></h3>
                                    </div>
                                    <b class="timeShaft_btn"></b> </a></div>
                        </c:forEach>
                    </div>
                </c:if>


                <c:if test="${empty hotblock&&empty tomorrow&&empty yesterday&&empty futureweek&&empty pastweek}">
                    <div class="tip_text" id="loading-tip-hot" style="display:block">小编正在收集中</div>
                </c:if>
                <div class="loading" id="loading-btn-hot"><a href="javascript:;"><i
                        style="display:none"></i><b></b></a></div>
            </div>
        </div>
        <!--热门开测===end-->


        <!--即将开测-->
        <div class="timeShaft-main">
            <div class="timeShaft-main-box" id="wrapper-future">

                <div class="tip_text" id="loading-tip-future" style="display:none">小编正在收集中</div>
                <div class="loading" id="loading-btn-future"><a href="javascript:;"><i
                        style="display:none"></i><b></b></a></div>
            </div>
        </div>
        <!--已经开测-->
        <div class="timeShaft-main">
            <div class="timeShaft-main-box">

                <div class="tip_text" id="loading-tip-past" style="display:none">小编正在收集中</div>
                <div class="loading" id="loading-btn-past"><a href="javascript:;"><i
                        style="display:none"></i><b></b></a></div>
            </div>
        </div>
        <!--已经开测===end-->
    </div>
</div>
<div class="floatBanner" style="z-index:99;">
    <div class="box-sizing display">
        <div class="hb-dw-box">
            <i class="fl"><img src="${URL_LIB}/static/theme/wap/images/syhbcon.png" alt="" title=""/></i>

            <p class="fl">
                <em>着迷玩霸</em>
                <span>只要你着迷 陪你玩到底</span>
            </p>
        </div>
        <a href="javascript:void(0);" class="hn-dw-btn" id="close_btn">免费下载</a>
    </div>
</div>


<script type="text/javascript">
    //<![CDATA[
    $(document).ready(function (e) {
        window.arr = [];
        window.future = {
            "indexToShow": 0,
            "preKey": "default",
            "preId": "defaultId",
            "total": 0,
            "maxFlag": false,
            "postLock": false
        };
        future.queryData = {"pnum": 1, "pcount": 10, linecode: "${linecode}"};

        window.past = {
            "indexToShow": 0,
            "preKey": "default",
            "preId": "defaultId",
            "total": 0,
            "maxFlag": false,
            "postLock": false
        };
        past.queryData = {"pnum": 1, "pcount": 10, linecode: "${linecode}"};

        window.downlink = 'http://api.' + joyconfig.DOMAIN + '/joymeapp/gameclient/webview/game/cover?gameid=';
        window.loadFlag = {"loadTime": null, "isLoading": false};

        window.dateLong = {};
        (function () {
            var today = new Date();
            today.setHours(0);
            today.setMinutes(0);
            today.setSeconds(0);
            today.setMilliseconds(0);

            var oneday = 1000 * 60 * 60 * 24;
            dateLong.yesterday = today.getTime() - oneday;
            dateLong.tomorrow = today.getTime() + oneday;
            dateLong.houtian = today.getTime() + oneday * 2;
            dateLong.futureWeek = today.getTime() + oneday * 8;
            dateLong.pastWeek = today.getTime() - oneday * 7;
            dateLong.today = today.getTime();
        })();


        $("#loading-btn-future").on('click', function () {
            $("#loading-btn-future i").css('display', 'inline-block');
            $("#loading-btn-future b").html('加载中');
            getMoreListForFuture();
        });

        $("#loading-btn-past").on('click', function () {
            $("#loading-btn-past i").css('display', 'inline-block');
            $("#loading-btn-past b").html('加载中');
            getMoreListForPast();
        });
        timeBar();

//        var longTimes = $("#wrapper-hot h2");
//        $.each(longTimes, function (index, item) {
//            var longStr = $(this).html().trim();
//            var time = new Date(Number(longStr));
//            if (time != null) {
//                var year = "" + time.getFullYear();
//                year = year.substr(year.length - 2, 2);
//                var timeStr = year + '年' + (time.getMonth() + 1) + '月' + time.getDate() + '日';
//                $(this).html(timeStr);
//            }
//        });

        var linkBtn = $("#wrapper-hot a");
        $.each(linkBtn, function (index, item) {

            var ji = $(this).attr("data-ji");
            var jt = $(this).attr("data-jt");
            if (jt == '' || jt == '24') {
                $(this).children(".timeShaft_btn").attr("style", "display:none;");
            }

        });

        $('.hn-dw-btn').on('touchstart', function () {
            if (is_weixn()) {
                $('.mark_box').show();
                $('.popup_box').show();
            } else {
                window.location.href = "http://www.joyme.com/appclick/rli4niip";
            }
        });
        $('.mark_box').on('touchstart', function (ev) {
            ev.preventDefault();
            ev.stopPropagation();
            if (is_weixn()) {
                $('.mark_box').hide();
                $('.popup_box').hide();
            }
        });

    });

    function is_weixn() {
        var ua = navigator.userAgent.toLowerCase();
        var pattern = /MicroMessenger/i;
        if (ua.indexOf("micromessenger") > -1) {
            return true;
        } else {
            return false;
        }
    }

    function formateDateLongForFuture(obj) {
        if (obj >= dateLong.tomorrow && obj < dateLong.houtian) {
            return "tomorrow";
        } else if (obj >= dateLong.houtian && obj < dateLong.futureWeek) {
            return "futureweek"
        }
        var time = new Date(obj);
        var month = time.getMonth() + 1
        if (month < 10) {
            month = '0' + month;
        }
        var day = time.getDate();
        if (day < 10) {
            day = '0' + day;
        }
        return time.getFullYear() + '-' + month + '-' + day;
    }

    function formateDateLongForPast(obj) {
        if (obj >= dateLong.yesterday && obj < dateLong.today) {
            return "yesterday";
        } else if (obj >= dateLong.pastWeek && obj < dateLong.yesterday) {
            return "formerweek"
        }
        var time = new Date(obj);
        var month = time.getMonth() + 1
        if (month < 10) {
            month = '0' + month;
        }
        var day = time.getDate();
        if (day < 10) {
            day = '0' + day;
        }
        return time.getFullYear() + '-' + month + '-' + day;
    }


    function toJumpToGameDetail(obj) {
        var ji = $(obj).attr("data-ji");
        var jt = $(obj).attr("data-jt");
        if (jt == '' && ji == '') {
            return false;
        }
        //  ji = ji.replace(/\?/g, encodeURIComponent("?")).replace(/[&]/g, encodeURIComponent("&"));
        //  ji = encodeURIComponent(ji);
        // _jclient.jump('jt=' + jt + '&ji=' + ji);

        if (jt == '23') {
            ji = ji.replace("cover", "covershare");
            ji = ji.replace("poster", "postershare");
            window.location.href = ji;
        }
        return false;
    }

    function getMoreListForFuture() {

        if (future.postLock) {
            return;
        }
        future.postLock = true;
        future.queryData.qnum = new Date().getTime();
        $.ajax({
            url: "http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/api/game/newgamemorefuture",
            data: future.queryData,
            type: "POST",
            dataType: "json",
            success: function (data, textStatus) {
                if (data.errorStatus == 1) {
                    $("#popup").text(data.errorMessage);
                    $("#popup").addClass("close");
                    var t = setInterval(function () {
                        $("#popup").removeClass("close");
                        clearTimeout(t);
                    }, 3000);
                    return;
                }
                var toInsert;
                $.each(data.smallList, function (index, item) {

                    var key = formateDateLongForFuture(item.publishtime);

                    toInsert = '';
                    if (future.preKey != key) {
                        future.indexToShow = future.indexToShow + 1;
                        future.preId = "future" + future.indexToShow;
                        if (key == 'tomorrow') {
                            toInsert += '<div id="' + future.preId + '" class="timeShaft-main-block tomorrow ';
                            if (future.indexToShow == 1) {
                                toInsert += '  first_box';
                            }
                            toInsert += '"><span></span>';
                            toInsert += '<p class="timeShaft-main-time tomorrow">明日</p>';
                        } else if (key == 'futureweek') {
                            toInsert += '<div id="' + future.preId + '" class="timeShaft-main-block future ';
                            if (future.indexToShow == 1) {
                                toInsert += '  first_box';
                            }
                            toInsert += '"><span></span>';
                            toInsert += '<p class="timeShaft-main-time future">未来一周</p>';
                        } else {
                            toInsert += '<div id="' + future.preId + '" class="timeShaft-main-block date';
                            if (future.indexToShow == 1) {
                                toInsert += '  first_box';
                            }
                            toInsert += '"><span></span>';
                            toInsert += '<p class="timeShaft-main-time date">' + key.substr(5, 5) + '</p>';
                        }

                    }


                    future.total++;
                    if (future.preKey != key) {
                        toInsert += '<div class="timeShaft-block-one ma">';
                    } else {
                        toInsert += '<div class="timeShaft-block-one">';
                    }
                    toInsert += '<a href="javascript:void(0);" onclick="toJumpToGameDetail(this);"  data-jt="' + item.jt + '" data-ji="' + item.ji + '" >';
                    toInsert += '<div class="timeShaft-block-l fl">';
                    toInsert += '<img src="' + item.iconurl + '" alt="" /></div>';
                    toInsert += '<div class="timeShaft-block-r fl">';
                    toInsert += '<h1>' + item.name + '</h1>';

//                    var time = new Date(item.publishtime);
//                    var year = "" + time.getFullYear();
//                    year = year.substr(year.length - 2, 2);
//                    var timeStr = year + '年' + (time.getMonth() + 1) + '月' + time.getDate() + '日';

                    toInsert += '<h2>' + item.customContent + '</h2>';
                    var desc = item.reason;
                    if (desc != undefined && desc.length > 13) {
                        desc = desc.substr(0, 13);
                    }
                    toInsert += '<h3>' + desc + '</h3>'
                    if (item.jt == '' || item.jt == '24') {
                        toInsert += '</div> <b class="timeShaft_btn" style="display:none;"></b> </a> </div>';
                    } else {
                        toInsert += '</div> <b class="timeShaft_btn"></b> </a> </div>';
                    }

                    if (future.preKey != key) {
                        toInsert += '</div>';
                        $("#loading-btn-future").before(toInsert);
                    } else {
                        $("#" + future.preId).append(toInsert);
                    }

                    future.preKey = key;
                });

                setTimeout(function () {
                    $("#loading-btn-future i").css('display', 'none');
                    $("#loading-btn-future b").html("加载更多");

                    if (data.maxPage <= future.queryData.pnum) {
                        if (future.total == 0) {
                            $("#loading-tip-future").css('display', 'block');
                            $("#loading-btn-future b").html("");
                        } else if (future.total > 10) {
                           // $("#loading-btn-future b").html("木有了....（＞﹏＜");
                            $("#loading-btn-future b").html("");
                        } else {
                            $("#loading-btn-future b").html("");
                        }
                        future.maxFlag = true;
                        $("#loading-btn-future").off();
                    }
                }, 1000);
                future.queryData.pnum += 1;
                arr.length = 0;
                arr.top = $(".wrapper").scrollTop();
                $('.cur_timeBox').find('.timeShaft-main-time').each(function () {
                    arr.push(parseInt($(this).offset().top) - 80 + arr.top);
                });
                arr.bottom = $('.cur_timeBox').find('.loading').offset().top;
            },
            complete: function (XMLHttpRequest, textStatus) {
                future.postLock = false;
            }

        });
    }

    function getMoreListForPast() {

        if (past.postLock) {
            return;
        }
        past.postLock = true;
        past.queryData.qnum = new Date().getTime();
        $.ajax({
            url: "http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/api/game/newgamemorepast",
            data: past.queryData,
            type: "POST",
            dataType: "json",
            success: function (data, textStatus) {
                if (data.errorStatus == 1) {
                    $("#popup").text(data.errorMessage);
                    $("#popup").addClass("close");
                    var t = setInterval(function () {
                        $("#popup").removeClass("close");
                        clearTimeout(t);
                    }, 3000);
                    return;
                }
                var toInsert;
                $.each(data.smallList, function (index, item) {
                    var key = formateDateLongForPast(item.publishtime);

                    toInsert = '';
                    if (past.preKey != key) {
                        past.indexToShow = past.indexToShow + 1;
                        past.preId = "past" + past.indexToShow;
                        if (key == 'yesterday') {
                            toInsert += '<div id="' + past.preId + '" class="timeShaft-main-block yesterday ';
                            if (past.indexToShow == 1) {
                                toInsert += '  first_box';
                            }
                            toInsert += '"><span></span>';
                            toInsert += '<p class="timeShaft-main-time yesterday">昨日</p>';
                        } else if (key == 'formerweek') {
                            toInsert += '<div id="' + past.preId + '" class="timeShaft-main-block formerly ';
                            if (past.indexToShow == 1) {
                                toInsert += '  first_box';
                            }
                            toInsert += '"><span></span>';
                            toInsert += '<p class="timeShaft-main-time formerly">过去一周</p>';
                        } else {
                            toInsert += '<div id="' + past.preId + '" class="timeShaft-main-block date ';
                            if (past.indexToShow == 1) {
                                toInsert += '  first_box';
                            }
                            toInsert += '"><span></span>';
                            toInsert += '<p class="timeShaft-main-time date">' + key.substr(5, 5) + '</p>';
                        }
                    }

                    past.total++;
                    if (past.preKey != key) {
                        toInsert += '<div class="timeShaft-block-one ma">';
                    } else {
                        toInsert += '<div class="timeShaft-block-one">';
                    }
                    toInsert += '<a href="javascript:void(0);"  onclick="toJumpToGameDetail(this);"   data-jt="' + item.jt + '" data-ji="' + item.ji + '" >';
                    toInsert += '<div class="timeShaft-block-l fl">';
                    toInsert += '<img src="' + item.iconurl + '" alt="" /></div>';
                    toInsert += '<div class="timeShaft-block-r fl">';
                    toInsert += '<h1>' + item.name + '</h1>';

//                    var time = new Date(item.publishtime);
//                    var year = "" + time.getFullYear();
//                    year = year.substr(year.length - 2, 2);
//                    var timeStr = year + '年' + (time.getMonth() + 1) + '月' + time.getDate() + '日';
                    toInsert += '<h2>' + item.customContent + '</h2>';

                    var desc = item.reason;
                    if (desc != undefined && desc.length > 13) {
                        desc = desc.substr(0, 13);
                    }
                    toInsert += '<h3>' + desc + '</h3>'
                    if (item.jt == '' || item.jt == '24') {
                        toInsert += '</div> <b class="timeShaft_btn" style="display:none;"></b> </a> </div>';
                    } else {
                        toInsert += '</div> <b class="timeShaft_btn"></b> </a> </div>';
                    }

                    if (past.preKey != key) {
                        toInsert += '</div>';
                        $("#loading-btn-past").before(toInsert);
                    } else {
                        $("#" + past.preId).append(toInsert);
                    }
                    past.preKey = key;
                });

                setTimeout(function () {
                    $("#loading-btn-past i").css('display', 'none');
                    $("#loading-btn-past b").html("加载更多");

                    if (data.maxPage <= past.queryData.pnum) {

                        if (past.total == 0) {
                            $("#loading-tip-past").css('display', 'block');
                            $("#loading-btn-past b").html("");
                        } else if (past.total > 10) {
                           // $("#loading-btn-past b").html("木有了....（＞﹏＜");
                            $("#loading-btn-past b").html("");
                        } else {
                            $("#loading-btn-past b").html("");
                        }
                        past.maxFlag = true;
                        $("#loading-btn-past").off();
                    }
                }, 1000);
                past.queryData.pnum += 1;
                arr.length = 0;
                arr.top = $(".wrapper").scrollTop();
                $('.cur_timeBox').find('.timeShaft-main-time').each(function () {
                    arr.push(parseInt($(this).offset().top) - 80 + arr.top);
                });
                arr.bottom = $('.cur_timeBox').find('.loading').offset().top;
            },
            complete: function (XMLHttpRequest, textStatus) {
                past.postLock = false;
            }
        });
    }

    function timeBar() {
        var box = $('.wrapper');
        $('.cur_timeBox').find('.timeShaft-main-time').each(function () {
            arr.push(parseInt($(this).offset().top) - 80);
        });
        $('.yxxq_tab_title>a').on("touchstart", function () {
            $(this).addClass('active').siblings().removeClass('active');
            var index = $(this).index();
            arr.index = index;
            $('.wrapper>div').eq(index).addClass('cur_timeBox').siblings().removeClass('cur_timeBox');
            box.animate({scrollTop: 0}, 5);
            box.scrollTop(0);
            if (index == 1 && future.indexToShow == 0) {
                getMoreListForFuture();
            } else if (index == 2 && past.indexToShow == 0) {
                getMoreListForPast();
            }
            arr.length = 0;
            $('.cur_timeBox').find('.timeShaft-main-time').each(function () {
                arr.push(parseInt($(this).offset().top) - 80);
            });
            arr.bottom = $('.cur_timeBox').find('.loading').offset().top;
            $('.timeShaft-tit').children('p').text('');
            $('.timeShaft-tit').children('p').css('background', '');
        });

        function toShowTip() {
            // console.log(arr);
            var oTop = $(this).scrollTop();
            if (oTop > 0) {
                $('.timeShaft-tit').addClass('po1');
            } else {
                $('.timeShaft-tit').removeClass('po1');
            }
            for (var i = 0, len = arr.length; i < len; i++) {
                if (oTop > arr[len - 1]) {
                    var html = $('.cur_timeBox').find('.timeShaft-main-time').eq(len - 1).text();
                    var classNames = $('.cur_timeBox').find('.timeShaft-main-time').eq(len - 1).css('background');
                    $('.timeShaft-tit').children('p').text(html);
                    $('.timeShaft-tit').children('p').css('background', classNames);
                    break; //符合一个即可退出
                } else if (oTop > arr[i] && oTop < arr[i + 1]) {
                    var html = $('.cur_timeBox').find('.timeShaft-main-time').eq(i).text();
                    var classNames = $('.cur_timeBox').find('.timeShaft-main-time').eq(i).css('background');
                    $('.timeShaft-tit').children('p').text(html);
                    $('.timeShaft-tit').children('p').css('background', classNames);
                    break;   //符合一个即可退出
                }
            }
        }

        box.scroll(toShowTip);
        box.on("touchmove", toShowTip);
        box.scroll(toLoadMore);
    }
    function toLoadMore() {
        if (arr.index == 2 && past.maxFlag) {
            return;
        } else if (arr.index == 1 && future.maxFlag) {
            return;
        }
        var sTop = $('.wrapper')[0].scrollTop + 45;
        var sHeight = $('.wrapper')[0].scrollHeight;
        var sMainHeight = $('.wrapper').height();
        var sNum = sHeight - sMainHeight;
        if (sTop >= sNum && !loadFlag.isLoading) {
            loadFlag.isLoading = true;
            if (arr.index == 1 && !future.maxFlag) {
                $("#loading-btn-future i").css('display', 'inline-block');
                $("#loading-btn-future b").html('加载中');
                getMoreListForFuture();
            } else if (arr.index == 2 && !past.maxFlag) {
                $("#loading-btn-past i").css('display', 'inline-block');
                $("#loading-btn-past b").html('加载中');
                getMoreListForPast();
            }
            loadFlag.loadTime = setTimeout(function () {
                loadFlag.isLoading = false;
            }, 1000);
        }
    }
    //]]>
</script>

</body>
</html>
