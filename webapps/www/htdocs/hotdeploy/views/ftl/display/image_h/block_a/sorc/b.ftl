<div class="bankuai fl">
    <div class="apcon clearfix">
        <div class="h01_picon">
            <a href="javascript:void(0)" name="preview" cuno="${displayContent.contentUno}"
               cid="${displayContent.contentId}" idx="${displayContent.displayOrder}">
                <div class="h01_img">
                    <p><img src="${displayContent.thumbImgLink}" width="${displayContent.width}"
                            height="${displayContent.height}"></p>
                    <div class="zhezhao"></div>
                </div>
            </a>
        </div>
        <div class="atit fl pt20 clearfix">
            <h3><a href="javascript:void(0)" name="preview" cuno="${displayContent.contentUno}"
                   cid="${displayContent.contentId}" idx="${displayContent.displayOrder}">
            ${displayContent.wallSubject}
            </a></h3>

            <div class="byauthor">
                作者：<a href="${URL_WWW}/people/${displayContent.domainName}" target="_blank">${displayContent.screenName}</a>
                <span class="bytime"><a href="${URL_WWW}/note/${displayContent.contentId}" target="_blank">${displayContent.dateStr}</a></span>
            </div>
        </div>
    </div>
</div>