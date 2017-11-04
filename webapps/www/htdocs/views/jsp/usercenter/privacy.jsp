<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1"/>
    <title>我的隐私</title>
    <link href="//cdn.bootcss.com/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
    <link href="//cdn.bootcss.com/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${URL_STATIC}/pc/userEncourageSys/css/screen.css">
    <link rel="stylesheet" type="text/css" href="${URL_STATIC}/pc/userEncourageSys/css/common.css">
    <link rel="stylesheet" type="text/css" href="${URL_STATIC}/pc/userEncourageSys/css/edit-select.css">
    <link rel="stylesheet" href="${URL_STATIC}/pc/userEncourageSys/css/setting.css">
    <link rel="stylesheet" href="${URL_STATIC}/pc/userEncourageSys/css/head-skin.css">
    <link rel="stylesheet" type="text/css" href="${URL_STATIC}/pc/userEncourageSys/css/userInfocc.css">
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function (){
            document.addEventListener('touchstart', function (){return false}, true)
        }, true);

    </script>

</head>
<body>
<%@ include file="../usercenter/user-center-header.jsp" %>
<!-- 内容区域 开始 -->
<div class="container">
    <div class="row">
        <div class="setting-con">
            <%@ include file="../customize/customize-general-left.jsp" %>
            <div class="col-md-9 pag-hor-20">
                <div class="setting-r">
                    <form action="/usercenter/customize/privacysave" method="post">
                        <input type="hidden" name="profileid" value="${profile.profileId}">
                    <h3 class="setting-tit web-hide">隐私</h3>
                    <div class="secret-con">
                            <h4>提醒设置</h4>
                            <span>
                                    <input type="checkbox" id="userat" name="userat" value="${privacyAlarm.userat}" onclick="changeStatus(this)" <c:if test="${privacyAlarm.userat eq '1'}">checked</c:if>>
                                    <label for="userat"><i></i>开启@我</label>
                                </span>
                            <span>
                                    <input type="checkbox" id="comment"  name="comment" value="${privacyAlarm.comment}" onclick="changeStatus(this)" <c:if test="${privacyAlarm.comment eq '1'}">checked</c:if>>
                                    <label for="comment"><i></i>开启评论&回复</label>
                                </span>
                            <span>
                                    <input type="checkbox" id="agreement"  name="agreement" value="${privacyAlarm.agreement}" onclick="changeStatus(this)" <c:if test="${privacyAlarm.agreement eq '1'}">checked</c:if>>
                                    <label for="agreement"><i></i>开启点赞</label>
                                </span>
                            <span>
                                    <input type="checkbox" id="follow"  name="follow" value="${privacyAlarm.follow}" onclick="changeStatus(this)" <c:if test="${privacyAlarm.follow eq '1'}">checked</c:if>>
                                    <label for="follow"><i></i>开启关注</label>
                                </span>
                            <span>
                                    <input type="checkbox" id="systeminfo"  name="systeminfo" value="${privacyAlarm.systeminfo}" onclick="changeStatus(this)" <c:if test="${privacyAlarm.systeminfo eq '1'}">checked</c:if>>
                                    <label for="systeminfo"><i></i>开启系统通知</label>
                                </span>
                            <h4>功能</h4>
                            <span>
                                    <input type="checkbox" id="acceptfollow"   name="acceptfollow" value="${privacyFunction.acceptFollow}" onclick="changeStatus(this)" <c:if test="${privacyFunction.acceptFollow eq '1'}">checked</c:if>>
                                    <label for="acceptfollow"><i></i>允许他人关注</label>
                                </span>
                            <span>
                                    <input type="checkbox" id="chat"  name="chat" value="${privacyFunction.chat}" onclick="changeStatus(this)" <c:if test="${privacyFunction.chat eq '1'}">checked</c:if>>
                                    <label for="chat"><i></i>允许他人私信</label>
                                </span>

                    </div>

                    <button class="web-hide btn-sure" type="submit">保存</button>
                    <button class="web-show btn-sure" type="submit">保存</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- 内容区域 结束 -->
<%@ include file="../usercenter/user-center-footer.jsp" %>

<script type="text/javascript" src="http://static.joyme.com/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${URL_STATIC}/pc/userEncourageSys/js/bootstrap.js"></script>
<script src="${URL_LIB}/static/js/usercenter/userWindow.js"></script>
<script type="text/javascript" src="${URL_LIB}/static/js/usercenter/action.js"></script>
<script type="text/javascript">
    function changeStatus(obj) {
        if(obj.checked){
            obj.value = "1";
        }else {
            obj.value = "0";
        }
    }
</script>
</body>
</html>