<#if list?? && list?size gt 0>
 <#assign parseOrgImg= "com.enjoyf.platform.webapps.common.html.freemarktag.ImageOrgUrl"?new()>
<h4 class="noborder">新增游戏条目</h4>
<ul class="hover-item">
    <#list list as game>
    <li class="<#if game_index==0>current</#if>">
        <em>${game.title}</em>
        <div>
            <#assign thumbimg = parseOrgImg(game.thumbimg)/>
            <a href="${URL_WWW}/game/${game.code}" target="_blank" title="${game.title}"><img src="${thumbimg}" alt="${game.title}" width="50"></a>

            <h2><a href="${URL_WWW}/game/${game.code}" target="_blank" title="${game.title}">${game.title}</a></h2>
            <#if game.device??><p>平台：${game.device}</p></#if>
        </div>
        <span><#if game.createDate??>${game.createDate?string("MM.dd")}</#if></span>
    </li>
    </#list>
</ul>
</#if>