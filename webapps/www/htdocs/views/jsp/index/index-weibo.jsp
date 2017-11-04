<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!-- 官方微博 -->
<h2 class="newIndex-item-title"><span>官方微博</span></h2>

<div class="newIndex-right-shadow-top"></div>
<div class="newIndexContent-right">
    <div class="tab01-hd">
        <ul class="tab01_hd clearfix">
            <li><a href="javascript:void(0);" data-openid="weixin" class="active" name="link_showopen">官方微信</a></li>
            <li><a href="javascript:void(0);" data-openid="sinaweibo" name="link_showopen">新浪微博</a></li>
            <%--<li><a name="link_showopen" data-openid="qweibo" href="javascript:void(0);">腾讯微博</a></li>--%>
            <%--<li><a name="link_showopen" data-openid="renren" href="javascript:void(0);">人人小站</a></li>--%>
        </ul>
    </div>

    <ul id="iframeWeibo">
        <li>
            <div id="frame_show_weixin" class="index-weixin">
                <a href="http://www.joyme.com/note/2ImZdCCGddbbUe0XkZ_Hh-a" target="_blank">
                    <img src="${URL_LIB}/static/theme/default/img/weixin_logo.jpg" src="498"/>
                </a>
            </div>
        </li>
    </ul>

</div>
<div class="newIndex-right-shadow-bottom"></div>