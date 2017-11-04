<#if gameList?? && gameList?size gt 0>
 <#assign parseOrgImg= "com.enjoyf.platform.util.html.freemarktag.ImageOrgUrl"?new()>
    <#assign joymeSubString= "com.enjoyf.platform.util.html.freemarktag.SubStringChinese"?new()>
<div id="div_findgame" class="find-enteresting-game">
		<h2>一起推荐最好玩的游戏，<br>共同更新最实用攻略资料，<br />和爱玩的你，在一起！</h2>
		<a id="link_findgame" class="findmore" href="javascript:void(0);">试试手气</a>
		<ul class="findedgame">
            <#list gameList as game>
                <li class="findedgame-list-${game_index+1}">
                    <#if (game_index<=2)>
                            <a href="<#if game.extField2?? && game.extField2?length gt 0>${game.extField2}<#else>${URL_WWW}/game/${game.code}</#if>" target="_blank">
                                <#assign thumbimg = parseOrgImg(game.thumbimg)/><img src="${thumbimg}" alt="${game.title}">
                            </a>
                        <#else>
                            <#assign thumbimg = parseOrgImg(game.thumbimg)/><img src="${thumbimg}" alt="${game.title}">
                    </#if>
                    <div><p><#if game.extField1??><span>${joymeSubString(game.extField1,10)}</span></#if></p><p><span>${joymeSubString(game.title,13)}</span></p><p class="goto"><a href="${URL_WWW}/game/${game.code}" class="goto-game" target="_blank" title="${game.title}">去条目</a><#if game.extField2??><a href="${game.extField2}" class="goto-wiki" target="_blank" title="${game.title}">去wiki</a></#if></p></div></li>
            </#list>
		</ul>
	</div>
</#if>
