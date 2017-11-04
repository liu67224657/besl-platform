<#assign parseOrgImg= "com.enjoyf.platform.webapps.common.html.freemarktag.ImageOrgUrl"?new()>
<#if recommend?? && (recommend?size > 0)>
<div class="conbanner_pd">
    <a href="${recommend[0].displayInfo.linkUrl}" target="_blank" title="${recommend[0].displayInfo.subject}">
        <#if recommend[0].displayInfo?? &&  recommend[0].displayInfo.iconUrl?? && (recommend[0].displayInfo.iconUrl?length>0)>
            <#assign thumbimg = parseOrgImg(recommend[0].displayInfo.iconUrl)/>
            <img src="${thumbimg}" width="730" height="222"/>
            <#else>
                <img src="${URL_LIB}/static/theme/default/img/default.jpg" width="730" height="222"/>
        </#if>
    </a>
</div>
</#if>