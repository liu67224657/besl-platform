<#if logsList?? && logsList?size gt 0>
 <#assign joymeDate= "com.enjoyf.platform.util.html.freemarktag.JoymeDate"?new()>
<div class="gift-center-title clearfix">
    <h2 class="sp3">礼包上架日志</h2>
</div>
<div class="top-bg putaway-log">
    <ul style="overflow: hidden;white-space: normal;">
        <#list logsList as log>
            <#if log.desc?? && log.desc?length gt 0>
               <li>${joymeDate(log.createDate?string("yyyy-MM-dd HH:mm:ss"))}<span>&nbsp;&nbsp;</span><a target="_blank" href="<#if log.link?? && log.link?length gt 0>${log.link}</#if>">${log.desc}</a></li>
            </#if>
        </#list>
    </ul>
</div>
</#if>
