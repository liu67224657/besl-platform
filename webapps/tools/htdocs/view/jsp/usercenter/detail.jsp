<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>

<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="/static/include/js/jquery.js"></script>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>用户信息</title>

    <style>
        * {
            margin: 0;
            padding: 0;
        }

        body {
            font-size: 14px;
            font-family: "Microsoft YaHei";
        }

        ul, li {
            list-style: none;
        }

        #tab {
            position: relative;
        }

        #tab .tabList ul li {
            float: left;
            background: #fefefe;
            background: -moz-linear-gradient(top, #fefefe, #ededed);
            background: -o-linear-gradient(left top, left bottom, from(#fefefe), to(#ededed));
            background: -webkit-gradient(linear, left top, left bottom, from(#fefefe), to(#ededed));
            border: 1px solid #ccc;
            padding: 5px 0;
            width: 200px;
            text-align: center;
            margin-left: -1px;
            position: relative;
            cursor: pointer;
        }

        #tab .tabCon {
            position: absolute;
            left: -1px;
            top: 32px;
            border: 1px solid #ccc;
            border-top: none;
        }

        #tab .tabCon div {
            padding: 10px;
            position: absolute;
            opacity: 0;
            filter: alpha(opacity=0);
        }

        #tab .tabList li.cur {
            border-bottom: none;
            background: #fff;
        }

        #tab .tabCon div.cur {
            opacity: 1;
            filter: alpha(opacity=100);
        }
    </style>
</head>
<body>
<table width="100%" height="50px" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 用户信息>>详情</td>
    </tr>
</table>
<div id="tab" style="">
    <div class="tabList">
        <ul>
            <li class="cur">基本资料</li>
            <li>订阅信息</li>
        </ul>
    </div>
    <div class="tabCon">
        <div class="cur">
            <table width="300px;" height="300px;" border="0" cellspacing="1" cellpadding="0">
                <tr class="">
                    <td nowrap align="center">昵称:</td>
                    <td nowrap align="center">${profile.nick}</td>
                </tr>
                <tr class="">
                    <td nowrap align="center">性别:</td>
                    <td nowrap align="center">
                        <c:if test="${empty profile.sex}">未选择</c:if>
                        <c:if test="${profile.sex eq 0}">女</c:if>
                        <c:if test="${profile.sex eq 1}">男</c:if>
                    </td>
                </tr>
                <tr class="">
                    <td nowrap align="center">地点</td>
                    <td nowrap align="center">
                        <c:if test="${profile.provinceId ne 0 }">
                            ${regionMap[profile.provinceId].regionName}
                        </c:if>
                        <c:if test="${profile.cityId ne 0 }">
                            -${regionMap[profile.cityId].regionName}
                        </c:if>

                    </td>
                </tr>
                <tr class="">
                    <td nowrap align="center">简介</td>
                    <td nowrap align="center">${profile.description}</td>
                </tr>
                <tr class="">
                    <td nowrap align="center">手机号:</td>
                    <td nowrap align="center" id="phone">
                        <c:if test="${not empty profile.mobile}">
                            <script>
                                $(function () {
                                    var phone = '${profile.mobile}';
                                    var mphone = phone.substr(0, 3) + '****' + phone.substr(7);
                                    $("#phone").text(mphone);
                                });
                            </script>

                        </c:if>
                    </td>
                </tr>
            </table>

        </div>
        <div style="width:900px;">
            <c:choose>
                <c:when test="${empty followlist}">
                    暂无数据～
                </c:when>
                <c:otherwise>
                    <table border="1" cellspacing="1" cellpadding="0">

                        <c:forEach items="${followlist}" var="item">

                            <dl style="float:left ;width:300px; margin-top:30px;">
                                <c:choose>
                                    <c:when test="${empty item.icon}">
                                        <dt><img src="/static/images/default.jpg" width="100" height="100"/></dt>
                                    </c:when>
                                    <c:otherwise>
                                        <dt><img src="${item.icon}" width="100" height="100"/></dt>

                                    </c:otherwise>
                                </c:choose>
                                <dd> ${item.gameName}</dd>
                                <dd>订阅日期： <fmt:formatDate value="${item.createTime}"
                                                          pattern="yyyy-MM-dd HH:mm:ss"/></dd>
                            </dl>
                        </c:forEach>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
<script>
    window.onload = function () {
        var oDiv = document.getElementById("tab");
        var oLi = oDiv.getElementsByTagName("div")[0].getElementsByTagName("li");
        var aCon = oDiv.getElementsByTagName("div")[1].getElementsByTagName("div");
        var timer = null;
        for (var i = 0; i < oLi.length; i++) {
            oLi[i].index = i;
            oLi[i].onmouseover = function () {
                show(this.index);
            }
        }
        function show(a) {
            index = a;
            var alpha = 0;
            for (var j = 0; j < oLi.length; j++) {
                oLi[j].className = "";
                aCon[j].className = "";
                aCon[j].style.opacity = 0;
                aCon[j].style.filter = "alpha(opacity=0)";
            }
            oLi[index].className = "cur";
            clearInterval(timer);
            timer = setInterval(function () {
                        alpha += 2;
                        alpha > 100 && (alpha = 100);
                        aCon[index].style.opacity = alpha / 100;
                        aCon[index].style.filter = "alpha(opacity=" + alpha + ")";
                        alpha == 100 && clearInterval(timer);
                    },
                    5)
        }
    }
</script>
</body>
</html>