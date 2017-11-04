<div class="picsee_bg01">
<#list layout.wallBlockDTOList as block>
    <#if block_index == 0>
        <div class="a_vercon fl">
            <div class="pic_acon pic_right">
            ${block.html}
            </div>
        </div>
        <!--a_vercon-->
    </#if>
    <#if block_index == 1>
        <div class="b_vercon ml01 fl">
            <div class="pic_bcon pic_left">
            ${block.html}
            </div>
        </div>
        <!--b_vercon-->
    </#if>
    <#if block_index == 2>
        <div class="c_vercon mt01 fl">
            <div class="pic_ccon pic_c_left">
            ${block.html}
            </div>
        </div>
        <!--c_vercon-->
    </#if>
    <#if block_index == 3>
        <div class="c_vercon c_vercon01 mt01 ml01 fl">
            <div class="pic_ccon pic_c_middle">
            ${block.html}
            </div>
        </div>
        <!--c_vercon-->
    </#if>
    <#if block_index == 4>
        <div class="c_vercon mt01 ml01 fl">
            <div class="pic_ccon pic_c_right">
            ${block.html}
            </div>
        </div>
        <!--c_vercon-->
    </#if>
</#list>
</div>