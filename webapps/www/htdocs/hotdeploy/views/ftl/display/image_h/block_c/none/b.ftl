<div class="cbankuai fl">
    <div class="ccon clearfix">
        <div class="ch01_picon">
            <a href="javascript:void(0)" name="preview" cuno="${displayContent.contentUno}"
               cid="${displayContent.contentId}" idx="${displayContent.displayOrder}">
                <div class="ch01_img">
                    <p><img src="${displayContent.thumbImgLink}" width="${displayContent.width}"
                            height="${displayContent.height}"></p>
                    <div class="zhezhao"></div>
                </div>
            </a>
        </div>
    </div>
    <div class="byauthor byposition">
        作者：<a href="${URL_WWW}/people/${displayContent.domainName}" target="_blank">${displayContent.screenName}</a>
        <span class="bytime"><a href="${URL_WWW}/note/${displayContent.contentId}" target="_blank">${displayContent.dateStr}</a></span>
    </div>
</div>