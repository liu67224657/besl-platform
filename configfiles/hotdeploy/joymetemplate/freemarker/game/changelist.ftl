<#if list?? && list?size gt 0>
    <#assign parseOrgImg= "com.enjoyf.platform.webapps.common.html.freemarktag.ImageOrgUrl"?new()>
<h4 class="noborder">近期更新条目</h4>
<ul class="hover-item">
    <#list list as game>
        <li class="<#if game_index==0>current</#if>">
            <em>${game.title}</em>

            <div>
                <#assign thumbimg = parseOrgImg(game.thumbimg)/>
                <a href="${URL_WWW}/game/${game.code}" target="_blank" title="${game.title}"><img src="${thumbimg}" alt="${game.title}" width="50"></a>

                <h2><a href="${URL_WWW}/game/${game.code}" target="_blank" title="${game.title}">${game.title}</a></h2>
                <#if game.extField1??>
                    <p>${game.extField1}</p>
                </#if>
            </div>
            <#if game.extField2??>
                <span>${game.extField2}</span>
            </#if>
        </li>
    </#list>
</ul>
</#if>