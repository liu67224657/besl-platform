<#if gameList?? && gameList?size gt 0>
    <#assign parseOrgImg= "com.enjoyf.platform.webapps.common.html.freemarktag.ImageOrgUrl"?new()>
    <#assign joymeDateField= "com.enjoyf.platform.util.html.freemarktag.JoymeDateField"?new()>
<div class="hot-game">
    <h2 class="index-2013-title">热门游戏推荐<a href="${URL_WWW}/moregame" class="normalLink" target="_balnk">&gt;更多</a></h2>

    <ul>
        <#list gameList as game>
        <li <#if !game_has_next>class="noborder"</#if>>
            <a class="game-pic" href="${URL_WWW}/game/${game.code}" title="${game.title}">
                <p><#assign thumbimg = parseOrgImg(game.thumbimg)/><img src="${thumbimg}" alt="${game.title}"></p>
            </a>

            <div>
                <h2><a href="${URL_WWW}/game/${game.code}" title="${game.title}">${game.title}</a></h2>
                <#if game.extField1?? && game?length gt 0>
                  <p>
                    ${game.extField1}
                </p>
                </#if>

            </div>
        </li>
        </#list>
    </ul>
</div>
</#if>