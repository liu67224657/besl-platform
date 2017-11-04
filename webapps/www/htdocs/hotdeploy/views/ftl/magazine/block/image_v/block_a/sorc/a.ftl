<div class="aver_pic fl">
    <a href="javascript:void(0)" name="preview" cuno="${displayContent.contentUno}"
       cid="${displayContent.contentId}" idx="${displayContent.displayOrder}">
        <div class="aver_box">
            <p><img src="${displayContent.thumbImgLink}" width="${displayContent.width}"
                    height="${displayContent.height}"/></p>
            <div class="zhezhao"></div>
        </div>
    </a>
</div><!--aver_pic-->
<div class="pic_txt pic_ml fl">
    <h3>
        <a href="javascript:void(0)" name="preview" cuno="${displayContent.contentUno}"
           cid="${displayContent.contentId}" idx="${displayContent.displayOrder}">
        ${displayContent.wallSubject}
        </a>
    </h3>
    <div class="pic_sth">
        <span class="by">作者：<em> <a href="${URL_WWW}/people/${displayContent.domainName}"
                                    target="_blank">${displayContent.screenName}</a> </em></span>
        <span class="pic_time">
            <a href="${URL_WWW}/note/${displayContent.contentId}"
               target="_blank">${displayContent.dateStr}</a>
        </span>
    </div>
</div><!--pic_txt-->