<#if bulletinList?? && (bulletinList?size > 0)>
<div class="side_item" id="viewAlsoViewId" style="">
    <div class="side_hd">活动公告</div>
    <div class="side_bd side_pd">
        <ul class="item_list item_list04 clearfix" id="bodyViewAlsoViewId">
            <#list bulletinList as bulletin>
                <#if bulletin.displayInfo?? && bulletin.displayInfo.subject?? && (bulletin.displayInfo.subject?length>0)>
                    <li class="clearfix <#if !bulletin_has_next>item_no</#if>">
                        <a href="${bulletin.displayInfo.linkUrl}" target="_blank">${bulletin.displayInfo.subject}</a>
                    </li>
                </#if>
            </#list>
        </ul>
    </div>
    <div class="side_ft"></div>
</div>
</#if>