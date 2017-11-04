<#assign parseOrgImg= "com.enjoyf.platform.util.html.freemarktag.ImageOrgUrl"?new()>
<#list game.icon.images as icon>
    <#assign thumbimg = parseOrgImg(icon.ll)/>
    <#break>
</#list>
<div class="article-recommendgame clearfix">
	<#if game.logoSize?? && game.logoSize=='3:3'>
	     <a href="${URL_WWW}/game/${game.gameCode}" class="game-pic-app"><img width="100" height="100" src="${thumbimg}"><em></em></a>
	    <#else>
          <a href="${URL_WWW}/game/${game.gameCode}" class="game-pic-normal"><p><img width="102" src="${thumbimg}"></p></a>
    </#if>
	<div>
		<h2><a href="${URL_WWW}/game/${game.gameCode}">${game.resourceName}</a></h2>
		<p><#if game.deviceSet?? && game.deviceSet.deviceSet??>平台：<#list game.deviceSet.deviceSet as device>${device.code}<#if device_has_next && device_index lt 2>/</#if><#if (device_index>=2)><#break></#if></#list></#if></p>
		<p><#if game.categorySet?? && game.categorySet.categorySet??>类型：<#list game.categorySet.categorySet as category>${category.code}<#if category_has_next && category_index lt 2>/</#if><#if (category_index>=2)><#break></#if></#list></#if></p>
		<p><#if game.publishCompany?? &&game.publishCompany?length gt 0>发行商： <#if game.publishCompany?length gt 8 >${game.publishCompany?substring(0,8)}<#else>${game.publishCompany}</#if></#if></p>
	</div>
    <#if game.gameProperties??>
        <#list game.gameProperties.channels as channel>
            <#if channel_index gt 1><#break></#if>
            <p class="down-box">
                <#if channel.value2??><span>${channel.value2}</span><#else><span>免费</span></#if>
                <a href="${channel.value}" class="download_${channel.propertyType}"></a>
            </p>
        </#list>
    </#if>
	<a href="${URL_WWW}/game/${game.gameCode}" class="goto">点击进入条目页&gt;&gt;</a>
</div>
<div class="clear"></div>