<#if gameList?? && gameList?size gt 0>
 <#assign parseOrgImg= "com.enjoyf.platform.util.html.freemarktag.ImageOrgUrl"?new()>
    <#assign joymeSubString= "com.enjoyf.platform.util.html.freemarktag.SubStringChinese"?new()>
<div id="div_findgame" class="find-enteresting-game">
		<h2>热门游戏，全面集结，<br>达人驻守，每日更新，<br />和爱玩的你，在一起。</h2>
		<a id="link_findgame" class="findmore" href="javascript:void(0);">翻翻热点</a>
		<ul class="findedgame">
            <#list gameList as game>
                <li class="findedgame-list-${game_index+1}">
                    <a href="<#if game.extField2?? && game.extField2?length gt 0>${game.extField2}<#else>${URL_WWW}/game/${game.code}</#if>" target="_blank">
                    <#if (game_index<=2)>
                                <#assign thumbimg = parseOrgImg(game.thumbimg)/><img src="${thumbimg}" alt="${game.title}?html">
                        <#else>
                            <#assign thumbimg = parseOrgImg(game.thumbimg)/><img src="${thumbimg}" alt="${game.title}?html">
                    </#if>
                    </a>
                    <#if game.extField1??><span class="tag-type">${joymeSubString(game.extField1,10)?html}</span></#if>
                    <span class="tag-name">${joymeSubString(game.title,13)?html}</span>
                </li>
            </#list>
		</ul>
	</div>
</#if>
