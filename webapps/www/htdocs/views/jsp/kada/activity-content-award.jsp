<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <meta content=" initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link href="${URL_LIB}/static/theme/default/css/wca.css?${version}" rel="stylesheet" type="text/css"/>
    <script src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>
    <title>${activity.title}</title>
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);

        var uno = '${uno}';
        var lock = false;
        var aid= '${activity.activityId}'

        $(document).ready(function() {
            $(document).on("click","a[name=agree]",function (e) {
                e.preventDefault();
                if (lock) {
                    return false;
                }

                lock = true;

                var but=$(this);
                var cid = but.attr("data-cid");
                var hasAgree = but.hasClass("vv"); //已经赞过
                var action = (uno != null && uno.length > 0 && hasAgree) ? 0 : 1;


                $.ajax({
                    url: '/json/kada/agree',
                    type: 'post',
                    data: {'uno': uno, 'action': action,'cid':cid},
                    dataType: "json",
                    success: function (data) {
                        if (data.rs == "1") {
                            var numSpan=  $('#agree_num_'+cid);
                            var agreeList=$('#agreelist_'+cid);
                            if (action == 1) {
                                but.attr('class','vv');
                                var num = parseInt(numSpan.text()) + 1;
                                numSpan.text(num+'赞');
                                var agreeHeadImageList=$('img[id^=agree_headicon_'+cid+']');
                                if(agreeHeadImageList.length<4){
                                    agreeList.append('<img src="'+data.result.blog.headIcon+'"   id="agree_headicon_'+cid+'_'+data.result.blog.uno+'"/>');
                                }else{
                                    agreeHeadImageList.last().remove();
                                    agreeList.append('<img src="'+data.result.blog.headIcon+'"   id="agree_headicon_'+cid+'_'+data.result.blog.uno+'"/>');
                                }

                            } else {
                                but.attr('class','');
                                var num = parseInt(numSpan.text()) - 1;
                                numSpan.text(num+'赞');

                                 $('#agree_headicon_'+cid+'_'+data.result.blog.uno).remove();

                            }
                        }
                    },
                    complete: function () {
                        lock = false;
                    }
                })
            });
        });
    </script>
</head>
<body style="background:#242429">
<article class="item1">
    <section>
        <img src="${activity.iosBigPic}">
    </section>
    <section class="title">
        <div class="t1">
            <h2>${activity.title}</h2>
            <span>参与人数：<strong>${activity.useSum}</strong></span>
        </div>
        <div class="t2">${activity.description}</div>
    </section>
</article>

<article class="item2">
    <h2 style="padding-bottom:0">活动排行</h2>

        <c:forEach var="dto" items="${list}">
    <article class="user-list">
            <section>
                <img src="${dto.profile.icon}">
                <span>${dto.profile.username}</span>
                <a href="joymekada://joymeapp?msgtype=4&info=${dto.content.cid}">+关注</a>
            </section>
            <section>
                <img src="${dto.content.pic.pic_s}">
            </section>
            <section>
                <span id="agree_num_${dto.content.cid}">${dto.content.agreenum}赞</span>
                <a href="javascript:;" title="喜欢" name="agree" data-cid="${dto.content.cid}" class="<c:if test="${dto.content.isagree}">vv</c:if>"></a>
                <div id='agreelist_${dto.content.cid}'>
                <c:forEach var="agreeProfile" items="${dto.agress.rows}" begin="0" end="3">
                    <img src="${agreeProfile.icon}" id="agree_headicon_${dto.content.cid}_${agreeProfile.uno}"/>
                </c:forEach>
                </div>
            </section>
    </article>
        </c:forEach>

</article>
</body>
</html>