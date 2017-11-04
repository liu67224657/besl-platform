<#if customList?? && customList?size gt 0>
    <#assign joymeSubString= "com.enjoyf.platform.util.html.freemarktag.SubStringChinese"?new()>
<h2 class="index-2013-title">着迷热点</h2>
<ul class="index-2013-hot-news">
    <#list customList as custom>
        <#if custom_index==0>
            <li class="<#if !custom_has_next> noborder</#if>"><a href="<#if custom.link??>${custom.link}</#if>"  class="top" target="_blank" title="${custom.title}">${joymeSubString(custom.title,20,"…")}</a></li>
            <#else>
                <li class="<#if !custom_has_next> noborder</#if>">
                    <a href="<#if custom.link??>${custom.link}</#if>" target="_blank" title="${custom.title}"><#if custom.extField1??>${custom.extField1}</#if>${joymeSubString(custom.title,20,"…")}</a>
                </li>
        </#if>
    </#list>
</ul>
<div class="dotted-line" id="lastdotted-line" style="display:none"></div>
</#if>