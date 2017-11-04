<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html>
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1"/>
    <title>个性装扮</title>
    <link href="//cdn.bootcss.com/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
    <link href="//cdn.bootcss.com/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${URL_STATIC}/pc/userEncourageSys/css/screen.css">
    <link rel="stylesheet" type="text/css" href="${URL_STATIC}/pc/userEncourageSys/css/common.css">
    <link href="${URL_STATIC}/pc/userEncourageSys/css/bootstrap-datepicker.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${URL_STATIC}/pc/userEncourageSys/css/edit-select.css">
    <link rel="stylesheet" href="${URL_STATIC}/pc/userEncourageSys/css/setting.css">
    <link rel="stylesheet" href="${URL_STATIC}/pc/userEncourageSys/css/head-skin.css">
    <link rel="stylesheet" type="text/css" href="${URL_STATIC}/pc/userEncourageSys/css/userInfocc.css">
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);

    </script>

</head>
<body>
<c:import url="/views/jsp/usercenter/user-center-header.jsp"/>

<!-- 内容区域 开始 -->
<div class="container">
    <div class="row">
        <div class="setting-con">
            <%@ include file="../customize/customize-general-left.jsp" %>

            <div class="col-md-9 pag-hor-20">
                <div class="setting-r">
                    <h3 class="setting-tit web-hide">个性装扮</h3>
                    <div class="get-box">
                        <h4 class="get-title web-hide">获取宝箱</h4>
                        <div class="bbx-box">
                            <span class="red-box"></span>
                            <cite class="num" id="giftBoxNum">${giftBoxNum}</cite>
                        </div>
                        <p class="click web-hide"><a href="javascript:;" class="duihuan">点击此处兑换宝箱 ></a></p>
                        <p class="warn web-show">可以点击宝箱打开</p>
                        <p class="dhbx web-show duihuan"><a href="javascript:;">兑换宝箱</a></p>
                        <div class="box-pop">
                            <p class="pop-con">
                                <img src=""
                                     alt=""><br/>
                                <!--<span>头骨3号</span>-->
                            </p>
                        </div>
                        <div class="box-pop-02-wrap">
                            <div class="box-pop-02">
                                <div class="joyme-dialog-warning">
                                    <p><span>!</span><b>提示</b></p></div>
                                <p class="wain-dec"></p>
                                <p><a class="make-sure">确定</a></p>
                            </div>
                        </div>
                        <div class="joyme-dialog-popup-mc">
                            <div class="joyme-dialog-wiki-popup commonwindow">
                                <div class="joyme-dialog-warning">
                                    <p><span>!</span><b>提示</b></p></div>
                                <div class="joyme-dialog-warn-con">
                                    <div class="new-window-content">
                                        <div class="new-window-line">
                                            <span class="label-word">宝箱个数:</span>
                                            <div class="label-input"><span class="fuhao fuhao-unplus noclick">-</span>
                                                <input class="fuhao fuhao-times" readonly="readonly" name="times"
                                                       type="text" value="1"><span class="fuhao fuhao-plus">+</span>
                                            </div>
                                        </div>
                                        <div class="new-window-line"><span class="label-word">消耗积分:</span>
                                            <div class="label-input"><span class="showPoint">500</span>
                                                <span class="surplus">(可用积分：<i
                                                        class="sur-num">${userPoint.userPoint}</i>)</span></div>
                                        </div>
                                    </div>
                                    <div class="joyme-dialog-warn-time">
                                        <span class="joyme-dialog-close-time"></span></div>
                                    <div class="joyme-dialog-warn-close">
                                        <p class="joyme-dialog-close-btn"><a class="joyme-dialog-confirm">确定</a><a
                                                class="joyme-dialog-cancel">取消</a></p></div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="can-choose">
                        <h4 class="choose-title web-hide">可选内容</h4>
                        <div class="choose-con">
                            <ul class="tab">
                                <li class="txbk on"><span><i></i>头像边框</span></li>
                                <li class="mpbj"><span><i></i>名片背景</span></li>
                                <li class="ltqp"><span><i></i>聊天气泡</span></li>
                                <li class="plzs"><span><i></i>评论装饰</span></li>
                            </ul>
                            <div class="decorate-con">
                                <div class="decorate1 on">
                                    <ul class="dec-list fn-clear">
                                        <c:forEach items="${giftLotterys}" var="lottery">
                                            <c:if test="${lottery.lotteryType.code==1}">
                                                <c:choose>
                                                    <c:when test="${not empty userLotteryLogMap[lottery.giftLotteryId]}">
                                                        <li>
                                                            <div class="dec-con owner <c:if test="${not empty chooseMap['lotteryType_1']&&chooseMap['lotteryType_1'] eq lottery.picKey}">choose</c:if>">
                                                                <cite class="selected"></cite>
                                                                <img src="${URL_LIB}/hotdeploy/static/img/skinpage/${lottery.picName}"
                                                                     alt="${lottery.giftLotteryName}"
                                                                     data-id="${lottery.giftLotteryId}" class="tx-img">
                                                                <p class="tx-star fn-clear">
                                                                    <c:forEach begin="1" end="${lottery.starRating}">
                                                                        <img src="${URL_STATIC}/pc/userEncourageSys/images/new-img/images/gxzb_yellow_star.png"
                                                                             alt="">
                                                                    </c:forEach>
                                                                <input type="hidden" value="${URL_LIB}/hotdeploy/static/img/skinpage/${lottery.picName}" id="skin${lottery.giftLotteryId}"/>
                                                                </p>

                                                            </div>
                                                        </li>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <li id="dis${lottery.giftLotteryId}">
                                                            <div class="dec-con">
                                                                <img src="${URL_LIB}/hotdeploy/static/img/skinpage/${lottery.disPicName}"
                                                                     alt="${lottery.giftLotteryName}" class="tx-img">
                                                                <p class="tx-star fn-clear">
                                                                    <c:forEach begin="1" end="${lottery.starRating}">
                                                                        <img src="${URL_STATIC}/pc/userEncourageSys/images/new-img/images/gxzb_grey_star.png"
                                                                             alt="">
                                                                    </c:forEach>
                                                                </p>
                                                            </div>
                                                        </li>
                                                        <li id="none${lottery.giftLotteryId}" style="display:none;">
                                                            <div class="dec-con owner">
                                                                <cite class="selected"></cite>
                                                                <img src="${URL_LIB}/hotdeploy/static/img/skinpage/${lottery.picName}"
                                                                     alt="${lottery.giftLotteryName}"
                                                                     data-id="${lottery.giftLotteryId}" class="tx-img">
                                                                <p class="tx-star fn-clear">
                                                                    <c:forEach begin="1" end="${lottery.starRating}">
                                                                        <img src="${URL_STATIC}/pc/userEncourageSys/images/new-img/images/gxzb_yellow_star.png"
                                                                             alt="">
                                                                    </c:forEach>
                                                                </p>
                                                            </div>
                                                        </li>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:if>
                                        </c:forEach>
                                    </ul>
                                </div>
                                <div class="decorate1">
                                    <ul class="dec-list fn-clear">
                                        <c:forEach items="${giftLotterys}" var="lottery">
                                            <c:if test="${lottery.lotteryType.code==2}">
                                                <c:choose>
                                                    <c:when test="${not empty userLotteryLogMap[lottery.giftLotteryId]}">
                                                        <li>
                                                            <div class="dec-con owner <c:if test="${not empty chooseMap['lotteryType_2']&&chooseMap['lotteryType_2'] eq lottery.picKey}">choose</c:if>">
                                                                <cite class="selected"></cite>
                                                                <img src="${URL_LIB}/hotdeploy/static/img/skinpage/${lottery.picName}"
                                                                     alt="${lottery.giftLotteryName}"
                                                                     data-id="${lottery.giftLotteryId}" class="tx-img">
                                                                <p class="tx-star fn-clear">
                                                                    <c:forEach begin="1" end="${lottery.starRating}">
                                                                        <img src="${URL_STATIC}/pc/userEncourageSys/images/new-img/images/gxzb_yellow_star.png"
                                                                             alt="">
                                                                    </c:forEach>
                                                                    <input type="hidden" value="${URL_LIB}/hotdeploy/static/img/skinpage/${lottery.picName}" id="skin${lottery.giftLotteryId}"/>

                                                                </p>
                                                            </div>
                                                        </li>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <li id="dis${lottery.giftLotteryId}">
                                                            <div class="dec-con">
                                                                <img src="${URL_LIB}/hotdeploy/static/img/skinpage/${lottery.disPicName}"
                                                                     alt="${lottery.giftLotteryName}" class="tx-img">
                                                                <p class="tx-star fn-clear">
                                                                    <c:forEach begin="1" end="${lottery.starRating}">
                                                                        <img src="${URL_STATIC}/pc/userEncourageSys/images/new-img/images/gxzb_grey_star.png"
                                                                             alt="">
                                                                    </c:forEach>
                                                                </p>
                                                            </div>
                                                        </li>
                                                        <li id="none${lottery.giftLotteryId}" style="display:none;">
                                                            <div class="dec-con owner">
                                                                <cite class="selected"></cite>
                                                                <img src="${URL_LIB}/hotdeploy/static/img/skinpage/${lottery.picName}"
                                                                     alt="${lottery.giftLotteryName}"
                                                                     data-id="${lottery.giftLotteryId}" class="tx-img">
                                                                <p class="tx-star fn-clear">
                                                                    <c:forEach begin="1" end="${lottery.starRating}">
                                                                        <img src="${URL_STATIC}/pc/userEncourageSys/images/new-img/images/gxzb_yellow_star.png"
                                                                             alt="">
                                                                    </c:forEach>
                                                                </p>
                                                            </div>
                                                        </li>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:if>
                                        </c:forEach>
                                        <!--灰色名片背景结束-->
                                    </ul>
                                </div>
                                <div class="decorate1">
                                    <ul class="dec-list fn-clear">
                                        <c:forEach items="${giftLotterys}" var="lottery">
                                            <c:if test="${lottery.lotteryType.code==4}">
                                                <c:choose>
                                                    <c:when test="${not empty userLotteryLogMap[lottery.giftLotteryId]}">
                                                        <li>
                                                            <div class="dec-con owner <c:if test="${not empty chooseMap['lotteryType_4']&&chooseMap['lotteryType_4'] eq lottery.picKey}">choose</c:if>">
                                                                <cite class="selected"></cite>
                                                                <img src="${URL_LIB}/hotdeploy/static/img/skinpage/${lottery.picName}"
                                                                     alt="${lottery.giftLotteryName}"
                                                                     data-id="${lottery.giftLotteryId}" class="tx-img">
                                                                <p class="tx-star fn-clear">
                                                                    <c:forEach begin="1" end="${lottery.starRating}">
                                                                        <img src="${URL_STATIC}/pc/userEncourageSys/images/new-img/images/gxzb_yellow_star.png"
                                                                             alt="">
                                                                    </c:forEach>
                                                                    <input type="hidden" value="${URL_LIB}/hotdeploy/static/img/skinpage/${lottery.picName}" id="skin${lottery.giftLotteryId}"/>

                                                                </p>
                                                            </div>
                                                        </li>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <li id="dis${lottery.giftLotteryId}">
                                                            <div class="dec-con">
                                                                <img src="${URL_LIB}/hotdeploy/static/img/skinpage/${lottery.disPicName}"
                                                                     alt="${lottery.giftLotteryName}" class="tx-img">
                                                                <p class="tx-star fn-clear">
                                                                    <c:forEach begin="1" end="${lottery.starRating}">
                                                                        <img src="${URL_STATIC}/pc/userEncourageSys/images/new-img/images/gxzb_grey_star.png"
                                                                             alt="">
                                                                    </c:forEach>
                                                                </p>
                                                            </div>
                                                        </li>
                                                        <li id="none${lottery.giftLotteryId}" style="display:none;">
                                                            <div class="dec-con owner">
                                                                <cite class="selected"></cite>
                                                                <img src="${URL_LIB}/hotdeploy/static/img/skinpage/${lottery.picName}"
                                                                     alt="${lottery.giftLotteryName}"
                                                                     data-id="${lottery.giftLotteryId}" class="tx-img">
                                                                <p class="tx-star fn-clear">
                                                                    <c:forEach begin="1" end="${lottery.starRating}">
                                                                        <img src="${URL_STATIC}/pc/userEncourageSys/images/new-img/images/gxzb_yellow_star.png"
                                                                             alt="">
                                                                    </c:forEach>
                                                                </p>
                                                            </div>
                                                        </li>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:if>
                                        </c:forEach>
                                        <!--灰色聊天气泡结束-->
                                    </ul>
                                </div>
                                <div class="decorate1">
                                    <ul class="dec-list fn-clear">
                                        <c:forEach items="${giftLotterys}" var="lottery">
                                            <c:if test="${lottery.lotteryType.code==3}">
                                                <c:choose>
                                                    <c:when test="${not empty userLotteryLogMap[lottery.giftLotteryId]}">
                                                        <li>
                                                            <div class="dec-con owner <c:if test="${not empty chooseMap['lotteryType_3']&&chooseMap['lotteryType_3'] eq lottery.picKey}">choose</c:if>">
                                                                <cite class="selected"></cite>
                                                                <img src="${URL_LIB}/hotdeploy/static/img/skinpage/${lottery.picName}"
                                                                     alt="${lottery.giftLotteryName}"
                                                                     data-id="${lottery.giftLotteryId}" class="tx-img">
                                                                <p class="tx-star fn-clear">
                                                                    <c:forEach begin="1" end="${lottery.starRating}">
                                                                        <img src="${URL_STATIC}/pc/userEncourageSys/images/new-img/images/gxzb_yellow_star.png"
                                                                             alt="">
                                                                    </c:forEach>
                                                                    <input type="hidden" value="${URL_LIB}/hotdeploy/static/img/skinpage/${lottery.picName}" id="skin${lottery.giftLotteryId}"/>

                                                                </p>
                                                            </div>
                                                        </li>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <li id="dis${lottery.giftLotteryId}">
                                                            <div class="dec-con">
                                                                <img src="${URL_LIB}/hotdeploy/static/img/skinpage/${lottery.disPicName}"
                                                                     alt="${lottery.giftLotteryName}" class="tx-img">
                                                                <p class="tx-star fn-clear">
                                                                    <c:forEach begin="1" end="${lottery.starRating}">
                                                                        <img src="${URL_STATIC}/pc/userEncourageSys/images/new-img/images/gxzb_grey_star.png"
                                                                             alt="">
                                                                    </c:forEach>
                                                                </p>
                                                            </div>
                                                        </li>
                                                        <li id="none${lottery.giftLotteryId}" style="display:none;">
                                                            <div class="dec-con owner">
                                                                <cite class="selected"></cite>
                                                                <img src="${URL_LIB}/hotdeploy/static/img/skinpage/${lottery.picName}"
                                                                     alt="${lottery.giftLotteryName}"
                                                                     data-id="${lottery.giftLotteryId}" class="tx-img">
                                                                <p class="tx-star fn-clear">
                                                                    <c:forEach begin="1" end="${lottery.starRating}">
                                                                        <img src="${URL_STATIC}/pc/userEncourageSys/images/new-img/images/gxzb_yellow_star.png"
                                                                             alt="">
                                                                    </c:forEach>
                                                                </p>
                                                            </div>
                                                        </li>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:if>
                                        </c:forEach>
                                        <!--灰色评论装饰结束-->
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<input type="hidden" value="${giftBoxNum}" name="giftnum"/>
<input type="hidden" value="${profile.profileId}" name="profileid"/>
<input type="hidden" value="${userPoint.userPoint}" name="point"/>
<!-- 内容区域 结束 -->
<!-- 页脚 开始 -->
<%@ include file="/views/jsp/usercenter/user-center-footer.jsp" %>
<!-- 页脚 结束 -->
<script type="text/javascript" src="http://static.joyme.com/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${URL_STATIC}/pc/userEncourageSys/js/bootstrap.js"></script>
<script type="text/javascript" src="${URL_STATIC}/pc/userEncourageSys/js/bootstrap-datepicker.js"></script>
<script src="${URL_LIB}/static/js/usercenter/userWindow.js"></script>
<script type="text/javascript" src="${URL_LIB}/static/js/usercenter/action.js"></script>
<script type="text/javascript" src="${URL_LIB}/static/js/common/personal_dec.js"></script>
<script type="text/javascript" src="${URL_LIB}/static/js/common/joyme_general.js"></script>
<script type="text/javascript">
    $(function () {
        $('#datetimepicker10').datepicker({
            language: "ch",           //语言选择中文
            format: 'yyyy/mm/dd',      //格式化日期
            orientation: "bottom left"
        });

        var pid = getCookie('jmuc_pid');
        //如果没有登录弹登录框
        if (pid == null) {
            loginDiv();
            return;
        }
    });
</script>
</body>
</html>