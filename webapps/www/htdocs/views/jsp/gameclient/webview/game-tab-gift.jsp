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
        var curPage =0;
        var pageSize =0;
        var maxPage =0;
        <c:if test="${page!=null}">
        curPage = ${page.curPage};
        pageSize =  ${page.pageSize};
        maxPage= ${page.maxPage};
        </c:if>




        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);

        $(document).ready(function () {
            $('#page').click(function () {
                scrollPage();
            });
        });

        var sorollLock = false;
        function scrollPage() {
            if (curPage == maxPage) {
                return;
            }

            sorollLock = true;
            $.ajax({
                        url: 'http://api.' + joyconfig.DOMAIN + '/joymeapp/gameclient/json/game/tab/giftlist',
                        data: {gameid: '${gameid}', pnum: curPage + 1, pcount: pageSize},
                        type: 'POST',
                        success: function (req) {
                            var html = '';
                            var result = eval('(' + req + ')');

                            if (result.rs != '1') {
                                return;
                            }

                            if (result.result == null || result.result === undefined) {
                                return;
                            }
                            $.each(result.result.rows, function (idx, val) {
                                var status = '';
                                if (val.reserveType == 0) {
                                    if (val.sn == 0) {
                                        status = '<span class="min_btn th">淘</span>';
                                    }
                                    status = '<span class="lh">领</span>';
                                } else if (val.reserveType == 1) {
                                    status = '<span class="yy">约</span>';
                                }

                                html += '<li>';
                                if (val.reserveType == 1) {
                                    html += '<cite class="yyz">预约中</cite>';
                                } else if (val.weixinExclusive == 2) {
                                    html += ' <cite class="dj">独家</cite>';
                                }
                                html += '<cite class="dj">独家</cite>' +
                                        '<a href="#">' +
                                        '<h1>' + val.title + '</h1>' +
                                        '<h2>' + val.desc + '</h2>' +
                                        status +
                                        '</a>' +
                                        '</li>';
                            });
                            curPage = result.result.page.curPage;
                            $('#wrapperBox').append(html);
                        },
                        complete: function () {
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
            <c:when test="${fn:length(rows)>0}">
                <div class="yxxq_lb_box" style="display:block">
                    <ul id="wrapperBox">
                        <c:forEach var="dto" items="${rows}">
                            <li>
                                <c:choose>
                                    <c:when test="${dto.reserveType==1}">
                                        <cite class="yyz">预约中</cite>
                                    </c:when>
                                    <c:otherwise>
                                        <c:if test="${dto.weixinExclusive==2}">
                                            <cite class="dj">独家</cite>
                                        </c:if>
                                    </c:otherwise>
                                </c:choose>

                                <a href="http://api.${DOMAIN}/joymeapp/gameclient/webview/giftmarket/giftdetail?aid=${dto.gid}&type=1">
                                    <h1>${dto.title}</h1>

                                    <h2>${dto.desc}</h2>
                                    <c:choose>
                                        <c:when test="${dto.reserveType==0}">
                                            <c:choose>
                                                <c:when test="${dto.sn==0}">
                                                    <span class="min_btn th">淘</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="min_btn lh">领</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:when test="${dto.reserveType==1}">
                                            <span class="min_btn yy">约</span>
                                        </c:when>

                                    </c:choose>
                                </a>
                            </li>
                        </c:forEach>
                    </ul>
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