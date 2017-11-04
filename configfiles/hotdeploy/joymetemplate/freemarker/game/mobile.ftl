<#if majorList?? && majorList?size gt 0>
    <#assign parseOrgImg= "com.enjoyf.platform.webapps.common.html.freemarktag.ImageOrgUrl"?new()>
<h4 class="game-phone-icon">移动游戏</h4>
<ul class="tj-game-phone clearfix">
    <#list majorList as game>
        <li>
            <a href="${URL_WWW}/game/${game.code}" target="_blank" title="${game.title}"><p>
                <#assign thumbimg = parseOrgImg(game.thumbimg)/>
                <img src="${thumbimg}" width="100" height="100"><em></em></p>
                <#if game.extField1??>
                    <span>${game.extField1}</span>
                </#if>
            ${game.title}</a>
        </li>
    </#list>
</ul>
    <#if list?? && list?size gt 0>
    <ul class="tj-game-tag clearfix">
        <#list list as game><li><#if game.extField1??><span class="week-game-tag">${game.extField1}</span></#if><a href="http://www.joyme.com/game/${game.code}" target="_blank"title=" ${game.title}"> ${game.title}</a></li></#list>
    </ul>
    </#if>
</#if>