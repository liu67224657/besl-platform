<#if replyTop?? && (replyTop?size>0)>
    <#assign chineseSubString= "com.enjoyf.platform.webapps.common.html.freemarktag.SubStringChinese"?new()>
    <#list replyTop as reply>
    <div class="hotnlist">
        <div class="sicon">${reply.replyTimes}</div>
        <h4>
            <a href="${URL_WWW}/note/${reply.elementId}" target="_blank">
                <#if reply.title?length gt 16 >
                                ${reply.title?substring(0,15)}…
                 <#else>
                ${reply.title}
                </#if>
            </a>
        </h4>
        <p>作者：<a href="http://${reply.domain}.${DOMAIN}" target="_blank">${chineseSubString(reply.screenName,8)}</a></p>
    </div>
    </#list>
</#if>
</div>
<div class="newIndex-right-shadow-bottom"></div>