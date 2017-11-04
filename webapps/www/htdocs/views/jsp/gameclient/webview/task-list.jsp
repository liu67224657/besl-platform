<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1x.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/jstllibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <title>任务列表</title>
    <link href="${URL_LIB}/static/theme/wap/css/wap_common.css" rel="stylesheet" type="text/css">
    <link href="${URL_LIB}/static/theme/wap/css/rwqd.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.11.0.min.js"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/common/gameclient-common.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            window.addEventListener('DOMContentLoaded', function () {
                document.addEventListener('touchstart', function () {
                    return false;
                }, true);
            }, true);

            $('a[name=link_jump]').on("click", function () {
                var jt = $(this).attr("data-jt");
                var ji = $(this).attr("data-ji");
                _jclient.jump('jt=' + jt + '&ji=' + encodeURIComponent(ji));
            });

            $('#jump_gift').on('click', function () {
                var ji = 'http://api.${DOMAIN}/joymeapp/gameclient/webview/giftmarket/list?uno=${profile.uno}&appkey=${appkey}&retype=midou';
                  ji = encodeURIComponent(ji);
                _jclient.jump('jt=-2&ji=' + ji);
                //window.location.herf=ji;
                //   http://api.joyme.com/joymeapp/gameclient/webview/giftmarket/list?retype=midou
            });

            $('#jump_mdintro').on('click', function () {
                _jclient.jump('jt=-2&ji=http://html.joyme.com/gameclient/pointintro.html');
            });

            scrollLock = false;
            $('cite[name=getaward]').on('click', function (event) {
                var linkObj = $(this);
                var taskid = linkObj.attr("data-taskid");
                if (taskid == null || taskid.length <= 0) {
                    return;
                }

                if (scrollLock) {
                    return;
                }
                scrollLock = true;
                $.ajax({
                    url: 'http://api.${DOMAIN}/joymeapp/gameclient/json/task/getaward',
                    data: {
                        profileid: '${profile.profileId}',
                        taskid: taskid,
                        platform: "${platform}",
                        appkey: "${appkey}"
                    },
                    type: 'POST',
                    beforeSend: function () {
                    },
                    success: function (req) {
                        var result = eval('(' + req + ')');
                        if (result.rs != '1') {
                            return;
                        } else {
                            linkObj.parent().attr("class", "ylq");
                            linkObj.replaceWith('<cite>已领取</cite>');

                            try {
                                var point = linkObj.attr('data-point');
                                if (point != null) {
                                    var p = parseInt(point);
                                    if (!isNaN(p)) {
                                        var pointCount = parseInt($("#_point").html());
                                        $("#_point").html(pointCount + p);
                                        _jclient.jump('jt=33&ji=' + p);
                                    }
                                }
                            } catch (e) {
                            }
                        }
                    },
                    complete: function () {
                        scrollLock = false;
                    }
                });
                event.stopPropagation();
            });

        });
    </script>
<body>
<div id="wrapper">
    <div class="rwqd-top">
        <div class="rwqd-top-box">
            <a href="javascript:void(0);" id="header_link"><img src="${icon:parseIcon(profile.icon, profile.sex, "")}"
                                                                dayGetAward alt=""></a>

            <div class="gift-box">
                <a id="jump_gift" href="javascript:void(0);">
                    <div class="gift-box-icon">
                        <div class="star star1"></div>
                        <div class="star star2"></div>
                        <div class="star star3"></div>
                        <div class="star star4"></div>
                        <div class="star star5"></div>
                        <div class="star star6"></div>
                    </div>
                </a>
            </div>

            <h1>${profile.nick}</h1>
            <a class="gr_information"
               href="javascript:void(0);"><b><em
                    id="_point">${point}</em>迷豆</b><b><em>${allcount-uncomplete}/${allcount}个任务</em>已完成</b></a>
            <a href="javascript:void(0);" class="strategy" id="jump_mdintro">迷豆是什么&gt;</a>
        </div>
    </div>
    <div style="margin-top: 181px;">
        <div class="rwqd_box">
            <c:forEach var="item" items="${list}" varStatus="st">
            <c:choose>
                <c:when test="${allcount > 0 }">
                    <c:if test="${not empty item.taskList}">
                        <div>
                            <h1 class="mrrw">
                                <span>${item.groupName}<em>&nbsp;&nbsp;&nbsp;${item.completedNum+item.getAwardNum}</em>/<b>${fn:length(item.taskList)}</b></span>
                            </h1>
                            <c:forEach var="task" items="${item.taskList}">
                                <a href="javascript:void(0);" name="link_jump" data-jt="${task.redirectType.code}"
                                   data-ji="${task.redirectUri}"
                                   class="<c:choose>
                           <c:when test="${completeMap.containsKey(task.taskId)}">
                                <c:choose>
                                    <c:when test="${completeMap.get(task.taskId).overStatus.code eq 'y'}">ylq</c:when>
                                    <c:when test="${completeMap.get(task.taskId).overStatus.code eq 'rj'}">lq</c:when>
                                    <c:otherwise>wwc</c:otherwise>
                                </c:choose>
                               </c:when>
                               <c:otherwise>wwc</c:otherwise>
                           </c:choose>">
                                    <h2><span>${task.taskName}</span></h2>

                                    <h3><span>+&nbsp;${task.taskAward.value}</span></h3>
                                    <c:choose>
                                        <c:when test="${completeMap.containsKey(task.taskId)}">
                                            <c:choose>
                                                <c:when test="${completeMap.get(task.taskId).overStatus.code eq 'y'}">
                                                    <cite>已领取</cite></c:when>
                                                <c:when test="${completeMap.get(task.taskId).overStatus.code eq 'rj'}">
                                                    <cite name="getaward"
                                                          data-taskid="${task.taskId}"
                                                          data-point="${task.taskAward.value}">领取</cite></c:when>
                                                <c:otherwise><cite>未完成</cite></c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>
                                            <cite>未完成</cite>
                                        </c:otherwise>
                                    </c:choose>
                                </a>
                            </c:forEach>
                        </div>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <div class="phb_none">
                        <cite><img src="${URL_LIB}/static/theme/wap/images/rwq.png" alt=""></cite>
                        <span>任务君正在赶来...</span>
                    </div>
                </c:otherwise>
            </c:choose>
            </c:forEach>
        </div>
        <div class="rwqd_flow">
            <h6>做任务有什么用？</h6>

            <div class="flow_box display_box">
                <div class="flow_icon display_box">
                    <a href="javascript:void(0);" class="display_box">
                        <cite class="flow_icon_rw display_box"></cite>
                    </a>
                    <b>做任务</b>
                </div>
                <div class="flow_boult display_box">
                    <span></span>
                </div>
                <div class="flow_icon display_box">
                    <a href="javascript:void(0);" class="display_box">
                        <cite class="flow_icon_md display_box"></cite>
                    </a>
                    <b>赚迷豆</b>
                </div>
                <div class="flow_boult display_box">
                    <span></span>
                </div>
                <div class="flow_icon display_box">
                    <a href="javascript:void(0);" class="display_box">
                        <cite class="flow_icon_lp display_box"></cite>
                    </a>
                    <b>换礼品</b>
                </div>
            </div>
        </div>
    </div>
</div>
</div>
</body>
</html>