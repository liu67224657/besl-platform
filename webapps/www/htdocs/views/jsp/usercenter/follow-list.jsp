<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1"/>
    <title>关注</title>
    <link href="//cdn.bootcss.com/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
    <link href="//cdn.bootcss.com/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${URL_STATIC}/pc/userEncourageSys/css/screen.css">
    <link rel="stylesheet" href="${URL_STATIC}/pc/userEncourageSys/css/common.css">
    <link rel="stylesheet" href="${URL_STATIC}/pc/userEncourageSys/css/joymedialog.css">
    <link rel="stylesheet" href="${URL_STATIC}/pc/userEncourageSys/css/head-skin.css">
    <link rel="stylesheet" type="text/css"
          href="${URL_STATIC}/pc/userEncourageSys/css/userInfocc.css">
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);

    </script>
</head>
<body>
<%@ include file="user-center-header.jsp" %>
<!-- 内容区  开始 -->
<div class="container">
    <div class="row">
        <!-- 左侧区域 开始 -->
        <div class="col-md-9">
            <div id="main">
                <div class="follow-list-box ">
                    <h1 class="page-h1 pag-hor-20 fn-clear">${profile.nick}已经关注了${followCount}位好友</h1>
                    <div class="letter-tit  web-common-tit web-show ">
                        <a href="${URL_WWW}/usercenter/follow/list?profileid=${profileId}" class="follow on">他的关注</a>
                        <a href="${URL_WWW}/usercenter/fans/list?profileid=${profileId}" class="fans">他的粉丝</a>
                    </div>
                    <c:if test="${empty rows}">
                        <ul class="list-item ">
                            <div class="no-data"><cite class="no-data-img"></cite>
                                <p></p></div>
                        </ul>
                    </c:if>
                    <c:if test="${not empty rows}">
                        <ul class="list-item ">
                            <c:forEach items="${rows}" var="userinfo">
                                <li id="li_row_user${userinfo.profileId}">
                                    <div class="list-item-l guzhu-l">
                                        <a href="${URL_UC}/usercenter/page?pid=${userinfo.profileId}" target="_blank">
                                            <cite class="user-head-img" data-id="${userinfo.profileId}">
                                                <c:if test="${userinfo.vtype > 0}">
                                                    <span class="user-vip" title="${userinfo.vdesc}"></span>
                                                </c:if>
                                                <img src="${userinfo.icon}">
                                                <span class="fensi-def fensi-dec-0${userinfo.headskin}"></span>
                                            </cite>
                                        </a>
                                    </div>
                                    <div class="list-item-r guzhu-r">
                                        <div class="item-r-name fn-clear">
                                            <a href="${URL_UC}/usercenter/page?pid=${userinfo.profileId}"
                                               target="_blank"
                                               class="r-user-name">${userinfo.nick}</a>
                                                <%--<c:if test="${userinfo.followStatus eq '1' || userinfo.followStatus eq '2'}">--%>
                                                <%--<a href="javascript:postMessge(${userinfo.uid})" class="fn-right fol-style sixin-link">私信</a>--%>
                                                <%--<div class="dropdown web-hide fn-right">--%>
                                                <%--<button type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">--%>
                                                <%--更多--%>
                                                <%--<span class="caret"></span>--%>
                                                <%--</button>--%>
                                                <%--<ul class="dropdown-menu" aria-labelledby="dLabel" id="dropdown-menu_${userinfo.profileId}">--%>
                                                <%--<li><a href="javascript:postMessge(${userinfo.uid})" >私信</a></li>--%>
                                                <%--<c:if test="${userinfo.followStatus eq '1' || userinfo.followStatus eq '2'}">--%>
                                                <%--<li><a href="javascript:;" class="more_action_unfollow" data-destpid="${userinfo.profileId}">取消关注</a></li>--%>
                                                <%--</c:if>--%>
                                                <%--</ul>--%>
                                                <%--</div>--%>
                                                <%--</c:if>--%>
                                            <c:if test="${userinfo.followStatus eq '0'}">
                                                <span id="fans${userinfo.profileId}"
                                                      data-destpid="${userinfo.profileId}"
                                                      class="fol-style follow fn-right"><i class="fa fa-plus"
                                                                                           aria-hidden="true"></i> 关注</span>
                                            </c:if>
                                            <c:if test="${userinfo.followStatus eq '1'}">
                                                <span id="fans${userinfo.profileId}"
                                                      data-destpid="${userinfo.profileId}"
                                                      class="each-follow fol-style fn-right"><i class="fa fa-check"
                                                                                                aria-hidden="true"></i> 已关注</span>
                                            </c:if>
                                            <c:if test="${userinfo.followStatus eq '2'}">
                                                <span id="fans${userinfo.profileId}"
                                                      data-destpid="${userinfo.profileId}"
                                                      class="each-follow fol-style fn-right"><i class="fa fa-exchange"
                                                                                                aria-hidden="true"></i> 互相关注</span>
                                            </c:if>
                                        </div>
                                        <div class="item-r-text">
                                            <font>个性签名</font>${userinfo.desc}
                                        </div>
                                        <div class="item-r-other fn-clear">
                                            <span>关注&nbsp;<a
                                                    href="/usercenter/follow/list?profileid=${userinfo.profileId}">${userinfo.follows}</a></span>
                                            <span>粉丝&nbsp;<a
                                                    href="/usercenter/fans/list?profileid=${userinfo.profileId}">${userinfo.fans}</a></span>
                                            <span>编辑&nbsp;<a
                                                    href="http://wiki.${DOMAIN}/home/index.php?title=%E7%89%B9%E6%AE%8A:%E7%9D%80%E8%BF%B7%E8%B4%A1%E7%8C%AE&userid=${userinfo.uid}">${userinfo.edits}</a></span>
                                        </div>
                                    </div>
                                </li>
                            </c:forEach>
                        </ul>
                    </c:if>
                </div>
                <c:if test="${not empty page&&page.maxPage>1}">
                    <div class="pagecon clearfix">
                        <c:set var="pageurl"
                               value="${URL_UC}/usercenter/follow/list"></c:set>
                        <c:set var="pageparam" value="profileid=${profile.profileId}"/>
                        <%@ include file="/views/jsp/page/wiki-page.jsp" %>
                    </div>
                </c:if>
            </div>
        </div>
        <!-- 左侧区域 结束 -->
        <!-- 右侧区域  开始 -->
        <c:import url="include-follow-right.jsp"></c:import>
        <input type="hidden" value="${page.curPage}" id="curpage"/>
        <input type="hidden" value="${page.maxPage}" id="maxpage"/>
        <!-- 右侧区域  开始 -->
    </div>
</div>
<!-- 内容区  结束 -->
<%@ include file="user-center-footer.jsp" %>
<script type="text/javascript" src="http://static.joyme.com/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${URL_STATIC}/pc/userEncourageSys/js/openwindow.js"></script>
<script type="text/javascript" src="${URL_STATIC}/pc/userEncourageSys/js/bootstrap.js"></script>
<script src="${URL_LIB}/static/js/usercenter/userWindow.js"></script>
<script type="text/javascript" src="${URL_STATIC}/pc/userEncourageSys/js/dropload.min.js"></script>

<script type="text/javascript" src="${URL_LIB}/static/js/usercenter/action.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        var srcprofileid = '${userSession.profileId}';
        $('.dropdown-menu').on("click", '.more_action_unfollow', function () {
            //var srcprofileid='${userSession.profileId}';
            var destpId = $(this).attr('data-destpid');
            var _obj = $(this);
            if (srcprofileid != null && srcprofileid != "" && destpId != null && destpId != "") {
                $.ajax({
                    url: "/api/usercenter/relation/unfollow",
                    type: "post",
                    async: false,
                    data: {destprofileid: destpId},
                    dataType: "jsonp",
                    jsonpCallback: "callback",
                    success: function (data) {
                        if (data != "") {
                            var sendResult = data[0];
                            var rs = sendResult.rs;
                            if (rs == "1") {
                                $('#fans' + destpId).empty();
                                var html = '<i class="fa fa-plus" aria-hidden="true"> </i> 关注';
                                $('#fans' + destpId).attr('class', 'fol-style follow fn-right').append(html);
//                               _obj.removeClass('more_action_unfollow').addClass('more_action_follow').text('关注');
                                _obj.parent().remove();
                            } else if (rs == -1) {
                                loginDiv();
                                return;
                            }
                            else if (rs == "2") {
                                alert("取消关注失败");
                            } else if (rs == "-1001") {
                                alert("参数不能为空");
                            } else if (rs == "-1000") {
                                alert("系统错误");
                            } else {
                                alert(sendResult.result);
                            }
                        }
                    }
                });
            }
        });

        appendFansOnClick();

        //////------------
        function appendFansOnClick() {
            $('span[id^=fans]').on("click", function () {
                //var srcprofileid='${userSession.profileId}';
                var destpId = $(this).attr('data-destpid');
                if (!$(this).hasClass("follow")) {
                    return;
                }
                if (srcprofileid == null || srcprofileid == "") {
                    loginDiv();
                    return;
                }
                if (srcprofileid != null && srcprofileid != "" && destpId != null && destpId != "") {
                    $.ajax({
                        url: "/api/usercenter/relation/follow",
                        type: "post",
                        async: false,
                        data: {destprofileid: destpId},
                        dataType: "jsonp",
                        jsonpCallback: "callback",
                        success: function (data) {
                            if (data != "") {
                                var sendResult = data[0];
                                var rs = sendResult.rs;
                                console.log(rs);
                                if (rs == "1") {
                                    $('#fans' + destpId).empty();
                                    if (sendResult.result == 'e') {
                                        var html = '<i class="fa fa-exchange" aria-hidden="true"> </i>相互关注';
                                        $('#fans' + destpId).attr('class', 'each-follow fol-style fn-right').append(html);
                                    } else {
                                        var html = '<i class="fa fa-check" aria-hidden="true"> </i> 已关注';
                                        $('#fans' + destpId).attr('class', 'each-follow fol-style fn-right').append(html);
                                    }
                                    $('#dropdown-menu_' + destpId).append(
                                            '<li><a href="javascript:;" class="more_action_unfollow" data-destpid="' + destpId + '">取消关注</a></li>'
                                    )
                                    mw.ugcwikiutil.msgDialog("关注成功");
                                } else if (rs == '-1') {
                                    loginDiv();
                                    return;
                                }
                                else if (rs == "2") {
                                    mw.ugcwikiutil.msgDialog("关注失败");
                                } else if (rs == "-1001") {
                                    mw.ugcwikiutil.msgDialog("参数不能为空");
                                } else if (rs == "-1000") {
                                    mw.ugcwikiutil.msgDialog("系统错误");
                                } else if (rs == "-10128") {
                                    mw.ugcwikiutil.msgDialog("该用户未开启关注权限");
                                } else if (rs == "-10127") {
                                    mw.ugcwikiutil.msgDialog("不允许自己关注自己");
                                }
                                else {
                                    mw.ugcwikiutil.msgDialog(sendResult.result);
                                }
                            }
                        }
                    });
                }
            });
        }

        ////////
        function refresh() {
            window.location.reload();//刷新当前页面.

        }

        <%--$(function(){--%>
        <%--var loadPageCurpno=${page.curPage};--%>
        <%--var isLastPage=${page.maxPage==page.curPage}--%>
        <%--var profileid='${userSession.profileId}';--%>
        <%--if(!isLastPage){--%>
        <%--$('.follow-list-box').upLoading({--%>
        <%--bindEl:".follow-list-box",// 上拉加载父框--%>
        <%--hasIpadHeng:true,// ipad 是否上拉加载--%>
        <%--lastPageFlag:isLastPage,--%>
        <%--ajaxData:{--%>
        <%--type: 'post',--%>
        <%--url: '/usercenter/follow/list?'+Math.random(),--%>
        <%--data:{profileid: profileid,pno:loadPageCurpno+1,psize:10},--%>
        <%--dataType: 'json',--%>
        <%--success: function(data){--%>
        <%--loadPageCurpno=data.page.curPage+1;--%>
        <%--isLastPage=data.page.maxPage==data.page.curPage;--%>
        <%--if(isLastPage){--%>
        <%--this.me.lock();--%>
        <%--this.me.noData();--%>
        <%--return;--%>
        <%--}--%>

        <%--if(data.rs!=1){--%>
        <%--this.me.lock();--%>
        <%--this.me.noData();--%>
        <%--return;--%>
        <%--}--%>

        <%--if(data.result==null || data.result.length==0){--%>
        <%--this.me.lock();--%>
        <%--this.me.noData();--%>
        <%--return;--%>
        <%--}--%>

        <%--loadPageNo=data.page.curpage+1;--%>
        <%--isLastPage=data.page.isLatstPage;--%>
        <%--var result = '';--%>
        <%--$.each(data.result,function(i,obj){--%>
        <%--var pid=obj.pid;--%>
        <%--var nick=obj.nick;--%>
        <%--var headskin = obj.headskin;--%>
        <%--result+='<li id="li_row_user'+pid+'">';--%>
        <%--result+='<div class="list-item-l guzhu-l">';--%>
        <%--result+='<cite><img src="">';--%>
        <%--result+='<span class="fensi-def fensi-dec-0'+headskin+'"></span>';--%>
        <%--result+='</cite>';--%>
        <%--result+='</div>';--%>
        <%--result+='<div class="list-item-r guzhu-r">';--%>
        <%--result+='<div class="item-r-name fn-clear">';--%>
        <%--result+='<a href="/usercenter/'+pid+'" class="r-user-name">'+nick+'</a>';--%>
        <%--result+='<div class="dropdown web-hide fn-right">';--%>
        <%--result+='<button type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">';--%>
        <%--result+='更多';--%>
        <%--result+='<span class="caret"></span>';--%>
        <%--result+='</button>';--%>
        <%--result+='<ul class="dropdown-menu" aria-labelledby="dLabel" id="dropdown-menu_'+pid+'">';--%>
        <%--result+='<li><a href="javascript:;">私信</a></li>';--%>
        <%--if (obj.followStatus=='1' || obj.followStatus=='2') {--%>
        <%--result+='<li><a href="javascript:;" class="more_action_unfollow" data-destpid="'+pid+'">取消关注</a></li>';--%>
        <%--}--%>
        <%--result+='</ul>';--%>
        <%--result+='</div>';--%>
        <%--if (obj.followStatus=='1') {--%>
        <%--result+='<span id="fans'+pid+'" data-destpid="'+pid+'" class="each-follow fol-style fn-right"><i class="fa fa-check" aria-hidden="true"></i> 已关注</span>';--%>
        <%--} else if(obj.followStatus=='2'){--%>
        <%--result+='<span id="fans'+pid+'" data-destpid="'+pid+'" class="each-follow fol-style fn-right"><i class="fa fa-exchange" aria-hidden="true"></i> 互相关注</span>';--%>
        <%--}else{--%>
        <%--result+='<span id="fans'+pid+'" data-destpid="'+pid+'" class="fol-style follow fn-right"><i class="fa fa-plus" aria-hidden="true"></i> 关注</span>';--%>
        <%--}--%>
        <%--result+='</div>';--%>
        <%--result+='<div class="item-r-text">';--%>
        <%--result+='        <font>个性签名</font></div>';--%>
        <%--result+='<div class="item-r-other fn-clear">';--%>
        <%--result+='        <span>关注&nbsp;<a href="/usercenter/follow/list?profileid='+pid+'">'+obj.follows+'</a></span>';--%>
        <%--result+='<span>粉丝&nbsp;<a href="/usercenter/fans/list?profileid='+pid+'">'+obj.fans+'</a></span>';--%>
        <%--result+='<span>编辑&nbsp;<a href="javascript:;">'+obj.edits+'</a></span>';--%>
        <%--result+='</div>';--%>
        <%--result+=' </div>';--%>
        <%--result+='</li>';--%>
        <%--});--%>
        <%--$('.list-item').append(result);--%>
        <%--this.me.resetload();--%>
        <%--}--%>
        <%--}--%>
        <%--});--%>
        <%--}--%>
        <%--});--%>
        $(function () {
            var profileid = '${profileId}';
            if (!commonFn.isPC()) {
                <c:if test="${not empty page && page.maxPage>1}">
                dropLoad();
                </c:if>
            }
            <c:if test="${not empty page && page.maxPage>1}">
            function dropLoad(argument) {
                $('.follow-list-box ').dropload({
                    scrollArea: window,
                    loadDownFn: function (me) {
                        var curpage = $("#curpage").val();
                        var maxpage = $("#maxpage").val();
                        if (parseInt(curpage) >= parseInt(maxpage)) {
                            me.lock();
                            me.noData();
                            me.resetload();
                            return;
                        }
                        curpage = parseInt(curpage) + 1;
                        $.ajax({
                            type: 'get',
                            url: '/api/usercenter/relation/follow/list',
                            data: {profileid: profileid, pno: curpage, psize: 10},
                            dataType: 'json',
                            success: function (data) {
                                var result = '';
                                $.each(data.result, function (i, obj) {
                                    var pid = obj.profileId;
                                    var uid = obj.uid;
                                    var nick = obj.nick;
                                    var icon = obj.icon;
                                    var headskin = obj.headskin;
                                    result += '<li id="li_row_user' + pid + '">';
                                    result += '<div class="list-item-l guzhu-l">';
                                    result += '<a href="/usercenter/page?pid=' + pid + '" target="_blank">'
                                    result += '<cite><img src=' + icon + '>';
                                    result += '<span class="fensi-def fensi-dec-0' + headskin + '"></span>';
                                    result += '</cite></a>';
                                    result += '</div>';
                                    result += '<div class="list-item-r guzhu-r">';
                                    result += '<div class="item-r-name fn-clear">';
                                    result += '<a href="/usercenter/page?pid=' + pid + '" class="r-user-name" target="_blank">' + nick + '</a>';
//                                    result += '<div class="dropdown web-hide fn-right">';
//                                    result += '<button type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">';
//                                    result += '更多';
//                                    result += '<span class="caret"></span>';
//                                    result += '</button>';
//                                    result += '<ul class="dropdown-menu" aria-labelledby="dLabel" id="dropdown-menu_' + pid + '">';
//                                    result += '<li><a href="javascript:postMessge(uid)" >私信</a></li>';
//                                    result += '<li><a href="javascript:;" class="more_action_unfollow" data-destpid="' + pid + '">取消关注</a></li>';
//                                    result += '</ul>';
//                                    result += '</div>';
                                    if (srcprofileid != pid) {
                                        if (obj.followStatus == '1') {
                                            result += '<span id="fans' + pid + '" data-destpid="' + pid + '" class="each-follow fol-style fn-right"><i class="fa fa-check" aria-hidden="true"></i> 已关注</span>';
                                        } else if (obj.followStatus == '2') {
                                            result += '<span id="fans' + pid + '" data-destpid="' + pid + '" class="each-follow fol-style fn-right"><i class="fa fa-exchange" aria-hidden="true"></i> 互相关注</span>';
                                        } else {

                                            result += '<span id="fans' + pid + '" data-destpid="' + pid + '" class="fol-style follow fn-right"><i class="fa fa-plus" aria-hidden="true"></i> 关注</span>';
                                        }
                                    }
                                    result += '</div>';
                                    result += '<div class="item-r-text">';
                                    result += '        <font>个性签名</font></div>';
                                    result += '<div class="item-r-other fn-clear">';
                                    result += '        <span>关注&nbsp;<a href="/usercenter/follow/list?profileid=' + pid + '">' + obj.follows + '</a></span>';
                                    result += '<span>粉丝&nbsp;<a href="/usercenter/fans/list?profileid=' + pid + '">' + obj.fans + '</a></span>';
                                    result += '<span>编辑&nbsp;<a href="http://wiki.${DOMAIN}/home/index.php?title=%E7%89%B9%E6%AE%8A:%E7%9D%80%E8%BF%B7%E8%B4%A1%E7%8C%AE&userid=' + uid + '">' + obj.edits + '</a></span>';
                                    result += '</div>';
                                    result += ' </div>';
                                    result += '</li>';
                                });
                                $('.list-item').append(result);
                                $("#curpage").val(data.page.curPage);
                                me.resetload();
                                appendFansOnClick();
                            },
                            error: function (ex) {
                                console.log(ex);
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
    });
</script>
</body>
</html>