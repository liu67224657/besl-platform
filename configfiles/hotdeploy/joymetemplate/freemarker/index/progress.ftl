<#if progressList?? && (progressList?size>0) >
<h2 class="index-2013-title">着迷在进步</h2>
<div class="joymestep">
    <#list progressList as progress>
           <#if progress.desc?? && (progress.desc?length>0)>
             ${progress.desc}
            </#if>
    </#list>
</div>
</#if>
