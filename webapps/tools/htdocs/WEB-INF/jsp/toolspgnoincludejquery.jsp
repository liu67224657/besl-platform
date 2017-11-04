<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ taglib prefix="pg" uri="/pager-taglib.tld" %>
<%//@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%--
<script src="${ctx}/static/include/js/jquery.js" type="text/javascript"></script>
--%>

<script type="text/javascript">
    $(document).ready(function() {
        $("#goPageSet").on('keydown keyup', function(e) {
            if (e.which == 13) {
                gopagecom2($("#goPageUrl").val());
            }
        })
    })
    function gopagecom(pageUrl) {

        if (pageUrl != null && pageUrl != "") {
            var url = pageUrl.substring(0, pageUrl.indexOf("?"));
            var paramstr = pageUrl.substring(pageUrl.indexOf("?") + 1, pageUrl.length); //参数字符串
            var paramarr = paramstr.split("&");//参数数组

            var goPageForm = document.createElement("FORM");
            document.body.appendChild(goPageForm);
            for (var i = 0; i < paramarr.length; i++) {
                var paramandvalue = paramarr[i];
                var paramname = paramandvalue.substring(0, paramandvalue.indexOf("="));
                var paramvalue = paramandvalue.substring(paramandvalue.indexOf("=") + 1, paramandvalue.length);
                var paramHidden = null;
//                if (window.navigator.userAgent.indexOf("MSIE") >= 1) {//ie
//                    paramHidden = document.createElement('<input  name="' + paramname + '" />');
//                    paramHidden.type = "hidden";
//                    paramHidden.value = paramvalue;
//                } else {
//                    if (navigator.userAgent.indexOf("Firefox") > 0) {//fox
                        paramHidden = document.createElement("input");
                        paramHidden.setAttribute("type", "hidden");
                        paramHidden.setAttribute("name", paramname);
                        paramHidden.setAttribute("value", paramvalue);
//                    } else {
//                        alert("本翻页仅支持IE Firefox");
//                    }
//                }
                goPageForm.appendChild(paramHidden);
            }
            goPageForm.method = "post";
            goPageForm.action = url;
            goPageForm.submit();
        }
    }

    function gopagecom2(pageUrl) {
        var goPageSets = document.getElementsByName("goPageSet");
        var goPageSet = null;
        if (goPageSets.length > 1) {
            for (var i = 0; i < goPageSets.length; i++) {
                if (goPageSets[i].value != "" && goPageSets[i].value != null) {
                    goPageSet = goPageSets[i].value;
                }
            }
        } else {
            goPageSet = goPageSets[0].value;
        }

        if (!checkNumberPage(goPageSet)) {
            alert("请输入整页数");
//            document.getElementById("goPageSet").focus();
            var goPageSets = document.getElementsByName("goPageSet");
            for (var i = 0; i < goPageSets.length; i++) {
                if (goPageSets[i].value != "" && goPageSets[i].value != null) {
                    goPageSets[i].focus();
                }
            }

            return;
        }
        var pageMaxItems = document.getElementById("maxPageItems").value;

        if (pageUrl != null && pageUrl != "") {
            var url = pageUrl.substring(0, pageUrl.indexOf("?"));
            var paramstr = pageUrl.substring(pageUrl.indexOf("?") + 1, pageUrl.length); //参数字符串
            var paramarr = paramstr.split("&");//参数数组

            var goPageForm = document.createElement("FORM");
            document.body.appendChild(goPageForm);
            for (var i = 0; i < paramarr.length; i++) {
                var paramandvalue = paramarr[i];
                var paramname = paramandvalue.substring(0, paramandvalue.indexOf("="));
                var paramvalue = paramandvalue.substring(paramandvalue.indexOf("=") + 1, paramandvalue.length);
                if (paramname == "pager.offset") {
                    paramvalue = (goPageSet - 1) * pageMaxItems;
                }

                var paramHidden = null;
//                if (window.navigator.userAgent.indexOf("MSIE") >= 1) {//ie
//                    paramHidden = document.createElement('<input  name="' + paramname + '" />');
//                    paramHidden.type = "hidden";
//                    paramHidden.value = paramvalue;
//                } else {
//                    if (navigator.userAgent.indexOf("Firefox") > 0) {//fox
                        paramHidden = document.createElement("input");
                        paramHidden.setAttribute("type", "hidden");
                        paramHidden.setAttribute("name", paramname);
                        paramHidden.setAttribute("value", paramvalue);
//                    } else {
//                        alert("本翻页仅支持IE Firefox");
//                    }
//                }
                goPageForm.appendChild(paramHidden);
            }
            goPageForm.method = "post";
            goPageForm.action = url;
            goPageForm.submit();
        }
    }

    function checkNumberPage(str) {
        var i;
        var len = str.length;
        if (len <= 0) {
            return false;
        }

        var chkStr = "1234567890";
        if (len == 1) {
            if (chkStr.indexOf(str.charAt(i)) < 0) {
                return false;
            }
        } else {
            if ((chkStr.indexOf(str.charAt(0)) < 0)) {
                return false;
            }
            for (i = 1; i < len; i++) {
                if (chkStr.indexOf(str.charAt(i)) < 0) {
                    return false;
                }
            }
        }
        return true;
    }
</script>

<input type="hidden" id="maxPageItems" name="maxPageItems" value="${page.pageSize}">
<pg:index export="total=itemCount,pages,items">

    <c:choose>
        <c:when test="${1 eq currentPageNumber }">
            首页 前一页
        </c:when>
        <c:otherwise>
            <pg:first>
                <a href="javascript:gopagecom('${pageUrl}')">首页</a>
            </pg:first>
            <pg:prev>
                <a href="javascript:gopagecom('${pageUrl}')">前一页</a>
            </pg:prev>
        </c:otherwise>
    </c:choose>
    <pg:pages>
        <c:choose>
            <c:when test="${pageNumber eq currentPageNumber }">
                <font color="red">${pageNumber }</font>
            </c:when>
            <c:otherwise>
                <a href="javascript:gopagecom('${pageUrl}')">${pageNumber}</a>
            </c:otherwise>
        </c:choose>
    </pg:pages>
    <c:choose>
        <c:when test="${pages eq currentPageNumber }">
            后一页 尾页
        </c:when>
        <c:otherwise>
            <pg:next>
                <a href="javascript:gopagecom('${pageUrl}')">后一页</a>
            </pg:next>
            <pg:last>
                <a href="javascript:gopagecom('${pageUrl}')">尾页</a>
            </pg:last>
        </c:otherwise>
    </c:choose>
    &nbsp;共&nbsp;${pages}&nbsp;页&nbsp;共&nbsp;${items}&nbsp;条&nbsp;
    <pg:page>
        <input type="text" style="height:18px;width:30px;" name="goPageSet" id="goPageSet" />&nbsp;
        <input type="hidden" value="${pageUrl}" name="goPageUrl" id="goPageUrl">
        <a href="javascript:gopagecom2('${pageUrl}')"><img src="${ctx}/static/images/go.gif" align="absmiddle" border="0"/></a>
    </pg:page>
</pg:index>