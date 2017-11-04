<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1"/>
    <title>我的信息</title>
    <link href="//cdn.bootcss.com/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
    <link href="//cdn.bootcss.com/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">
    <link href="${URL_STATIC}/pc/userEncourageSys/css/bootstrap-datepicker.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${URL_STATIC}/pc/userEncourageSys/css/screen.css">
    <link rel="stylesheet" type="text/css" href="${URL_STATIC}/pc/userEncourageSys/css/common.css">
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
<%@ include file="../usercenter/user-center-header.jsp" %>
<!-- 内容区域 开始 -->
<div class="container">
    <div class="row">
        <div class="setting-con">
            <%@ include file="../customize/customize-general-left.jsp" %>
            <div class="col-md-9 pag-hor-20">
                <div class="setting-r">
                    <h3 class="setting-tit web-hide">我的信息</h3>
                    <form class="message-con" name="myform" action="/usercenter/customize/person/save" method="post" id="myform">
                        <input type="hidden" name="profileId" value="${profile.profileId}">
                        <div>
                            <span>昵称：</span>
                            <div>
                                <c:choose>
                                    <c:when test="${not empty modifynick}">
                                        <input type="text" name="nick" value="${profile.nick}" readonly onfocus="this.blur()">
                                    </c:when>
                                    <c:otherwise>
                                        <input type="text" name="nick" value="${profile.nick}">
                                    </c:otherwise>
                                </c:choose>
                                <i class="warning">*昵称只能修改一次</i>
                            </div>
                        </div>
                        <div>
                            <span class="sign">我的签名：</span>
                            <div>
                                <textarea name="description" maxlength="16">${profile.description}</textarea>
                            </div>
                        </div>
                        <div>
                            <span>性别：</span>
                            <div>
                                <div class="select-area">
                                    <div class="select-ele">
                                        <span class="select-value">请选择</span>
                                        <i class="fa fa-angle-down"></i>
                                    </div>
                                    <select name="sex">
                                        <option value="1" <c:if test="${profile.sex eq '1'}">selected</c:if>>男</option>
                                        <option value="0" <c:if test="${profile.sex eq '0'}">selected</c:if>>女</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div>
                            <span>出生年月：</span>

                            <div>
                                <div class="form-group">
                                    <div class='input-group input-append date' id='datetimepicker10'>
                                        <input type='text' class="form-control" name="birthday"
                                               value="${profile.birthday}"/>
                                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div>
                            <span>兴趣：</span>
                            <div>
                                <input  class="send_info" type="text" name="hobby" value="${profile.hobby}" maxlength="100">
                            </div>
                        </div>
                        <div>
                            <span>所在地：</span>
                            <div>
                                <div class="select-area">
                                    <div class="select-ele">
                                        <span class="select-value">请选择</span>
                                        <i class="fa fa-angle-down"></i>
                                    </div>
                                    <select name="provinceId">
                                        <c:forEach items="${regionList}" var="region">
                                            <option value="${region.regionId}"
                                                    <c:if test="${profile.provinceId == region.regionId}">selected</c:if>>${region.regionName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div>
                            <span>收货地址：</span>
                            <div>
                                <c:if test="${not empty userAccount}">
                                    <c:if test="${not empty userAccount.address}">
                                        <c:set value="${userAccount.address.address}" var="address"></c:set>
                                    </c:if>
                                    <c:if test="${not empty userAccount.address}">
                                        <c:set value="${userAccount.address.phone}" var="phone"></c:set>
                                    </c:if>
                                </c:if>
                                <input type="text" class="send_info" value="<c:out value="${address}"/>" name="address" maxlength="200">
                            </div>
                        </div>
                        <div>
                            <span>收货人：</span>
                            <div>
                                <input type="text" class="send_info" value="${profile.realName}" name="realName" id="realName" maxlength="32">
                            </div>
                        </div>
                        <div>
                            <span>联系方式：</span>
                            <div>
                                <input type="text" class="send_info" value="${phone}" name="mobile" maxlength="32" onkeyup="value=value.replace(/[^\d（()）\s-]|_/ig,'')" onblur="value=value.replace(/[^\d（()）\s-]|_/ig,'')" placeholder="限数字、中横线、括号、空格">
                            </div>
                        </div>
                        <button class="web-hide btn-sure"  type="submit">保存</button>
                        <button class="web-show btn-sure"  type="submit">保存</button>
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
<script type="text/javascript" src="${URL_STATIC}/pc/userEncourageSys/js/bootstrap-datepicker.js"></script>
<script src="${URL_LIB}/static/js/usercenter/userWindow.js"></script>
<script type="text/javascript" src="${URL_LIB}/static/js/usercenter/action.js"></script>
<script type="text/javascript" >
    $(function () {
        $('#datetimepicker10').datepicker({
            language: "ch",           //语言选择中文
            format: 'yyyy/mm/dd',      //格式化日期
            orientation: "bottom left"
        });
    });

</script>
</body>
</html>