
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta name="applicable-device" content="pc">
    <meta name="mobile-agent" content="format=xhtml;url=http://m.joyme.com/">
    <meta name="mobile-agent" content="format=html5;url=http://m.joyme.com">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="description"
          content="着迷网礼包中心为手游玩家提供各种手游礼包,其中包括手机游戏激活码,特权码,测试礼包,新手礼包,兑换码，节假日礼包等,还有多种着迷专属礼包等你拿,多种手游礼包尽在着迷网!"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <link href="//cdn.bootcss.com/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
    <link href="//cdn.bootcss.com/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${URL_STATIC}/pc/userEncourageSys/css/screen.css">
    <link rel="stylesheet" type="text/css" href="${URL_STATIC}/pc/userEncourageSys/css/common.css">
    <link rel="stylesheet"
          href="${URL_STATIC}/pc/userEncourageSys/css/dropload.css">

    <link rel="stylesheet" href="${URL_STATIC}/pc/userEncourageSys/css/jifen.css">

    <title>我的积分</title>
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded',
                function () {
                    document.addEventListener('touchstart',
                            function () {
                                return false
                            },
                            true)
                },
                true);


    </script>

</head>
<body>
<!-- 导航 开始 -->
<c:import url="/views/jsp/usercenter/user-center-header.jsp"/>
<!-- 导航 结束 -->
<div class="jifen-box">

    <h1 class="jifen-head">我的积分</h1>
    <p class="jifen-title">您在着迷的总积分是：<span class="color-font-blue">${userPoint.userPoint}</span></p>
    <div class="jifen-title-box">
        <p class="jibox-title"><span class="jibox-num">${userPoint.userPoint}</span></p>
        <p class="font-middle-p">您在着迷的积分总数</p>
    </div>
    <div class="jifen-btn-box">
        <span class="jifen-btn get-btn <c:if test="${empty type}">on</c:if>" id="getpoint">获取</span>
        <span class="jifen-btn consume-btn <c:if test="${not empty type}">on</c:if>" id="consumepoint">消耗</span>
    </div>
    <ul class="tab-change-box">
        <c:set var="typevalue" value="获得积分"/>
        <c:if test="${not empty type}"><c:set var="typevalue" value="消耗积分"></c:set></c:if>
        <li class="tab-change-li on">
            <c:choose>
                <c:when test="${empty monthHistoryList&&empty allHistoryList}">
                    暂无详细积分
                </c:when>
                <c:otherwise>
                    <c:if test="${not empty monthHistoryList}">
                        <div class="jifen-cur-month" id="monthDiv">
                            <h3 class="curmonth">本月</h3>
                            <table class="datas-table" id="monthTable">
                                <tr>
                                    <th>时间</th>
                                    <th>操作</th>
                                    <th>渠道</th>
                                    <th>${typevalue}</th>
                                </tr>
                                <c:forEach items="${monthHistoryList}" var="his">
                                    <tr>
                                        <td>
                                                ${his.createTime}
                                        </td>
                                        <td>${his.actionType}</td>
                                        <td><c:if test="${not empty his.appkey}">
                                            ${his.appkey}
                                        </c:if></td>
                                        <td>${his.pointValue}</td>
                                    </tr>
                                </c:forEach>

                            </table>
                        </div>
                    </c:if>
                    <c:if test="${not empty allHistoryList}">
                        <div class="jifen-cur-month">
                            <h3 class="curmonth">历史</h3>
                            <table class="datas-table" id="historyTable">
                                <tr>
                                    <th>时间</th>
                                    <th>操作</th>
                                    <th>渠道</th>
                                    <th>${typevalue}</th>
                                </tr>
                                <c:forEach items="${allHistoryList}" var="his">
                                    <tr>
                                        <td>${his.createTime}</td>
                                        <td>${his.actionType}</td>
                                        <td>
                                            <c:if test="${not empty his.appkey}">
                                                ${his.appkey}
                                            </c:if>
                                        </td>
                                        <td>${his.pointValue}</td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </div>
                    </c:if>
                </c:otherwise>
            </c:choose>


            <c:if test="${not empty page&&page.maxPage>1}">
                <div class="pagecon clearfix">
                    <c:set var="pageurl" value="${URL_UC}/usercenter/mypoint/list"/>
                    <c:set var="pageparam"
                           value="type=${type}"/>
                    <%@ include file="/views/jsp/page/wiki-page.jsp" %>
                </div>
            </c:if>
        </li>
    </ul>


</div>

<%@ include file="/views/jsp/usercenter/user-center-footer.jsp" %>
<input type="hidden" value="${page.curPage}" id="curpage"/>
<input type="hidden" value="${page.maxPage}" id="maxpage"/>
<input type="hidden" value="${type}" id="type"/>
<!-- 页脚 结束 -->
<script type="text/javascript" src="http://static.joyme.com/js/jquery-1.9.1.min.js">
</script>

<script type="text/javascript"
        src="${URL_STATIC}/pc/userEncourageSys/js/bootstrap.js"></script>
<script type="text/javascript"
        src="${URL_STATIC}/pc/userEncourageSys/js/dropload.min.js"></script>

<script type="text/javascript"
        src="${URL_LIB}/static/js/usercenter/action.js"></script>

<script type="text/javascript">
    $(document).ready(function () {
        $("#getpoint").click(function () {
            window.location = "${URL_UC}/usercenter/mypoint/list";
        });

        $("#consumepoint").click(function () {
            window.location = "${URL_UC}/usercenter/mypoint/list?type=-1"
        });

        var login = '${isnotlogin}'; //如果没有登录弹登录框
        if (login == 'true') {
            loginDiv();
        }

        if (!commonFn.isPC()) {
            <c:if test="${not empty page && page.maxPage>1}">
               dropLoad();
            </c:if>
        }
        <c:if test="${not empty page && page.maxPage>1}">
        function dropLoad(argument) {
            // dropload
            $('.tab-change-li ').dropload({
                scrollArea: window,
                loadDownFn: function (me) {
                    var curpage = $("#curpage").val();
                    var maxpage = $("#maxpage").val();
                    var type = $("#type").val();

                    if (parseInt(curpage) >= parseInt(maxpage)) {
                        me.lock();
                        me.noData();
                        me.resetload();
                        return;
                    }

                    curpage = parseInt(curpage) + 1;
                    $.ajax({
                        type: 'post',
                        url: '/joyme/api/point/list',
                        data: {p: curpage, type: type},
                        dataType: 'json',
                        success: function (data) {
                            var result = data.result;
                            var allHistoryList = result.allHistoryList;
                            var monthHistoryList = result.monthHistoryList;
                            var type = result.type;
                            if (type == null) {
                                type = "";
                            }
                            var html = "";
                            for (var i = 0; i < monthHistoryList.length; i++) {
                                html += "<tr>" +
                                        "<td>" + monthHistoryList[i].createTime + "</td>" +
                                        "<td>" + monthHistoryList[i].actionType + "</td>" +
                                        "<td>" + monthHistoryList[i].appkey + "</td>" +
                                        "<td>" + monthHistoryList[i].pointValue + "</td>" +
                                        "</tr>";
                            }
                            $("#monthTable").append(html);
                            html = "";
                            for (var i = 0; i < allHistoryList.length; i++) {
                                html += "<tr>" +
                                        "<td>" + allHistoryList[i].createTime + "</td>" +
                                        "<td>" + allHistoryList[i].actionType + "</td>" +
                                        "<td>" + allHistoryList[i].appkey + "</td>" +
                                        "<td>" + allHistoryList[i].pointValue + "</td>" +
                                        "</tr>";
                            }

                            if (html != "") {
                                var historyTable = $("#historyTable");
                                if (historyTable.length > 0) {
                                    $(historyTable).append(html);
                                } else {
                                    var div = "<div class='jifen-cur-month'><h3 class='curmonth'>历史</h3><table class='datas-table' id='historyTable'><table></div>";
                                    $("#monthDiv").after(div);
                                    $("#historyTable").append(html);
                                }
                            }

                            $("#type").val(type);
                            $("#curpage").val(result.page.curPage);

                            me.resetload();
                        },
                        error: function (xhr, type) {
                            alert('Ajax error!');
                            // 即使加载出错，也得重置
                            me.resetload();
                        }
                    });
                }
            });
        }

        </c:if>
    });

</script>
</body>
</html>

