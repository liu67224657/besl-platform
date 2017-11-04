<%@ page import="java.util.Date" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1x.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/jstllibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <meta content="width=device.width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <title>游戏详情</title>
    <link href="${URL_LIB}/static/theme/wap/css/wap_common.css" rel="stylesheet" type="text/css">
    <link href="${URL_LIB}/static/theme/wap/css/yxxq.css" rel="stylesheet" type="text/css">
    <%@ include file="/views/jsp/common/jsconfig.jsp" %>
    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.11.0.min.js"></script>
    <script type="text/javascript">
        var curPage =${page.curPage};
        var pageSize =${page.pageSize};
        var maxPage =${page.maxPage};
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);

        $(document).ready(function () {
            $('#wrapper').scroll(function () {
                scrollPage();
            });
        });

        var sorollLock = false;
        function scrollPage() {
            if (curPage == maxPage) {
                return;
            }

            if (sorollLock) {
                return;
            }

            sorollLock = true;
            $.ajax({
                url: 'http://api.' + joyconfig.DOMAIN + '/joymeapp/gameclient/json/game/tab/news/list',
                data: {gameid: '${gameid}', pnum: curPage + 1, pcount: pageSize},
                type: 'POST',
                beforesend: function () {
                    var loadTips = "<div id='loadTips'><div class='loadTips txt-c'><div class='load-bar1'></div><div class='load-bar2'></div><div class='load-bar1'></div></div>";
                    $('#wrapperBox').append(loadTips);
                },
                success: function (req) {
                    $('#loadTips').remove();
                    var html = '';
                    var result = eval('(' + req + ')');

                    if (result.rs != '1') {
                        return;
                    }

                    if (result.result == null || result.result === undefined) {
                        return;
                    }

                    for (var i in result.result.rows) {
                        var date = new Date(parseInt(i));
                        var now = new Date();

                        var month = date.getMonth() + 1;
                        var day = date.getDate();
                        var year = now.getYear();

                        var nowMonth = now.getMonth() + 1;
                        var nowDay = now.getDate();
                        var nowYear = now.getYear();

                        var iDays = parseInt(Math.abs(now - date) / 1000 / 60 / 60 / 24)

                        var css = 'thirdTip';
                        if (year == nowYear && nowMonth == month && day == nowDay) {
                            css = 'firstTip';
                            html += '<div class="timeTip ' + css + '">今天</div>';
                        } else if (year == nowYear && nowMonth == month && iDays == 1) {
                            css = 'secondTip';
                            html += '<div class="timeTip ' + css + '">昨天</div>';
                        } else if (year == nowYear && nowMonth == month && iDays >= 2 && iDays < 6) {
                            css = 'secondTip';
                            html += '<div class="timeTip ' + css + '">' + iDays + '天前</div>';
                        } else {
                            month = month + '';
                            html += '<div class="timeTip ' + css + '">' + (month.length == 1 ? '0' + month : month) + '-' + day + '</div>';
                        }

                        $.each(result.result.rows[i], function (idx, val) {
                            html += '<ul><li><a href="' + val.ji + '"><cite class="yxxq_zx_icon" style="border-color:' + val.categoryColor + ';color:' + val.categoryColor + '">' + val.category + '</cite><h1>' + val.title + '</h1><p>' + val.title + '</p></a></li></ul>';
                        });
                    }


                    curPage = result.result.page.curPage;
                    $('#wrapperBox').append(html);
                },
                complete: function () {
                    $('#loadTips').remove();
                    sorollLock = false;
                }
            })

        }

    </script>
<body>
<div id="wrapper">
    <div class="yxxq_tab_main">
        <!--最新-->
        <c:choose>
            <c:when test="${fn:length(data)>0}">
                <div class="yxxq-zx-box" style="display:block">
                    <div id="scrollBox">
                        <c:set var="nowDate" value="<%=new Date()%>"></c:set>
                        <div class="box" id="wrapperBox">
                            <c:forEach var="d" items="${data}">
                                <c:choose>
                                    <c:when test="${dateutil:isToDay(d.key)}">
                                        <div class="timeTip firstTip ">今天</div>
                                    </c:when>
                                    <c:when test="${dateutil:isYesterDay(d.key)}">
                                        <div class="timeTip secondTip">昨天</div>
                                    </c:when>
                                    <c:when test="${dateutil:getDayList(d.key,nowDate)>2 && dateutil:getDayList(d.key,nowDate)<7}">
                                        <div class="timeTip secondTip">${dateutil:getDayList(d.key,nowDate)}天前</div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="timeTip thirdTip"><fmt:formatDate value="${d.key}"
                                                                                      pattern="MM-dd"/></div>
                                    </c:otherwise>
                                </c:choose>
                                <ul>
                                    <c:forEach var="item" items="${d.value}">
                                        <li>
                                            <a href="${item.ji}">
                                                <cite class="yxxq_zx_icon"
                                                      style="border-color:${item.categoryColor};color:${item.categoryColor}">${item.category}</cite>

                                                <h1>${item.title}</h1>

                                                <p>${item.desc}</p>
                                            </a>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="tip_text" style="display:block">
                    小编正在收集中
                </div>
            </c:otherwise>
        </c:choose>


        <!--最新==end-->
    </div>
</div>
</body>
</html>