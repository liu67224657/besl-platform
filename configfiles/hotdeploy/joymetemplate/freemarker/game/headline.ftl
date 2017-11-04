<#if list?? && list?size gt 0>
    <#assign parseOrgImg= "com.enjoyf.platform.webapps.common.html.freemarktag.ImageOrgUrl"?new()>
<div class="carousel">
    <div>
        <#list list as headLine>
            <#assign thumbimg = parseOrgImg(headLine.thumbimg)/>
            <a href="<#if headLine.link?? && headLine.link?length gt 0>${headLine.link}</#if>" id="roll_img_${headLine_index}" style="<#if headLine_index gt 0>disply:none</#if>"><img src="${thumbimg}" width="540" height="260"></a>
        </#list>
    </div>
    <p>
        <#list list as headLine>
        <span <#if headLine_index==0>class="current"</#if> id="roll_button_${headLine_index}"  data-no="${headLine_index}"> ${headLine_index+1}</span>
        </#list>
    </p>
</div>
</#if>