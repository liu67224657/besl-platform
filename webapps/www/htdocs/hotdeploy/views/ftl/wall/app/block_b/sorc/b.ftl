<div class="bbankuai fl">
<div class="b_appcon clearfix">
     <div class="bapp">
            <a href="javascript:void(0)" name="preview" cuno="${block.discoveryWallContent.contentUno}"
               cid="${block.discoveryWallContent.contentId}">
                <img src="${block.discoveryWallContent.thumbImgLink}">
                <span class="bmask"></span>
            </a>
     </div>
      <div class="bapptit mt10">
            <h3>
                <a href="javascript:void(0)" name="preview" cuno="${block.discoveryWallContent.contentUno}"
                   cid="${block.discoveryWallContent.contentId}">
                ${block.discoveryWallContent.wallSubject}
                </a>
            </h3>
         <div class="byauthor byposition">
             作者：<a href="${URL_WWW}/people/${block.discoveryWallContent.domainName}" target="_blank">${block.discoveryWallContent.screenName}</a>
             <span class="bytime"><a href="${URL_WWW}/note/${block.discoveryWallContent.contentId}" target="_blank">${block.discoveryWallContent.dateStr}</a></span>
         </div>
      </div>
  </div>
</div>