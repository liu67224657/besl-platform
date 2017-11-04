<#if wikiList?? && wikiList?size gt 0>
    <#assign parseOrgImg= "com.enjoyf.platform.util.html.freemarktag.ImageOrgUrl"?new()>
<h2 class="index-2013-title">攻略资料wiki</h2>
<ul class="recommendwiki">
    <#list wikiList as wiki>
        <li><a href="<#if wiki.link??>${wiki.link}</#if>" target="_blank" <#if wiki.title??>title="${wiki.title}"</#if>>
            <#if wiki.thumbimg??>
            <#assign thumbimg = parseOrgImg(wiki.thumbimg)/>
            <img width="183" height="90" src="${thumbimg}" <#if wiki.title??>alt="${wiki.title}"</#if>><span></span>
                <#else>
                <img width="183" height="90" src="${URL_LIB}/static/theme/default/img/default.jpg"><span></span>
            </#if>
        </a>
        </li>
    </#list>
</ul>
</#if>