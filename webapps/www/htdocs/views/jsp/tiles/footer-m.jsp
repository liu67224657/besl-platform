<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<footer class="footer">
    <p><span><a href="${URL_WWW}?tab_device=wap_pc" class="joyme-pc">访问着迷网电脑版</a></span><span><a href="${URL_M}" class="joyme-phone">访问着迷网手机版</a></span></p>
    <p>2011－2017 joyme.com, all rights reserved</p>
</footer>
<script type="text/javascript">
    $(document).ready(function(){
        var url = window.location.href;
        var pcUrl = url;
        var flag = "";
        if(url.indexOf("#") > 0){
            url = url.substring(0, url.indexOf("#"));
            flag = window.location.href.replace(url, "");
        }
        if(url.indexOf("?") > 0){
            pcUrl += "&tab_device=wap_pc" + flag;
        }else{
            pcUrl += "?tab_device=wap_pc" + flag;
        }
        $('.joyme-pc').attr('href', pcUrl);
        $('.joyme-phone').attr('href', window.location.href);
    });
</script>
