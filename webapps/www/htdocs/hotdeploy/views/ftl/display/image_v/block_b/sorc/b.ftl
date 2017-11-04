<div class="bbankuai fl">
    <div class="bcon clearfix">
        <div class="bs_picon">
            <a href="javascript:void(0)" name="preview" cuno="${displayContent.contentUno}"
               cid="${displayContent.contentId}" idx="${displayContent.displayOrder}">
                <div class="bs_pimg">
                    <p><img src="${displayContent.thumbImgLink}" width="${displayContent.width}"
                            height="${displayContent.height}"></p>
                    <div class="zhezhao"></div>
                </div>
            </a>
        </div>
        <h3 class="mt10">
            <a href="javascript:void(0)" name="preview" cuno="${displayContent.contentUno}"
               cid="${displayContent.contentId}" idx="${displayContent.displayOrder}">
            ${displayContent.wallSubject}
            </a>
        </h3>

        <div class="byauthor byposition01">
            作者：<a href="${URL_WWW}/people/${displayContent.domainName}" target="_blank">${displayContent.screenName}</a>
            <span class="bytime"><a href="${URL_WWW}/note/${displayContent.contentId}" target="_blank">${displayContent.dateStr}</a></span>
        </div>
    </div>
</div>