<div class="cbankuai fl">
<div class="ccon clearfix">
        <div class="music  mt15 clearfix">
                <div class="music_con">
                    <a href="javascript:void(0)" name="preview" cuno="${block.discoveryWallContent.contentUno}"
                       cid="${block.discoveryWallContent.contentId}">
                        <img src="${block.discoveryWallContent.thumbImgLink}" width="${block.styleConfig.width}"
                             height="${block.styleConfig.height}">
                    </a>
                </div>
             <div class="c_byauthor">
                 作者：<a href="${URL_WWW}/people/${block.discoveryWallContent.domainName}" target="_blank">${block.discoveryWallContent.screenName}</a>
                 <span class="bytime"><a href="${URL_WWW}/note/${block.discoveryWallContent.contentId}" target="_blank">${block.discoveryWallContent.dateStr}</a></span>
             </div>
             <a class="music_btn" href="javascript:void(0)" url="${URL_WWW}/discovery/${block.discoveryWallContent.domainName}/${block.discoveryWallContent.contentId}"></a>
          </div>
  </div>
</div>