/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-9
 * Time: 下午3:22
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../../js/common/jquery-1.5.2');
   var postimg = {
        postLocalPhoto : function (imgLinkidx, imgSrc, name, imgMap) {
            this.postPhoto(imgLinkidx, imgSrc, name, imgMap);
            this.bindListener();
        } ,

        postPhoto : function (imgLinkidx, imgSrc, name, imgMap) {
            blogContent.image.push({key:imgLinkidx,value:{url:imgMap['b'],src:imgSrc,desc:'',w:imgMap['w'],h:imgMap['h']}});
            $("#preview_photo_" + imgLinkidx).html('<input type="hidden" name="picurl_s" value="' + imgMap['s'] + '"/>' +
                    '<input type="hidden" name="picurl_m" value="' + imgMap['m'] + '"/>' +
                    '<input type="hidden" name="picurl_b" value="' + imgMap['b'] + '"/>' +
                    '<input type="hidden" name="picurl_ss" value="' + imgMap['ss'] + '"/>' +
                    '<input type="hidden" name="picurl" id="picurl" value="' + imgSrc + '"/>' +
                    '<input type="hidden" name="w" id="w" value="' + imgMap['w'] + '"/>' +
                    '<input type="hidden" name="h" id="h" value="' + imgMap['h'] + '"/>' +
                    '<a href="javascript:void(0)" class="picreview" title="' + name + '">' + subfilename(name, 5) + '</a>' +
                    '<a href="javascript:void(0)" title="取消" class="close" ></a>');
        },
        bindListener : function (imgLinkidx) {
            $("a[name=rmlink]").die().click(function() {
                $(this).parent().remove();
                if ($(".rel_preview li").length == 0) {
                    $(".rel_preview").toggle();
                }
            });
        },
        canclePhoto :function (imgLinkidx) {
            $('#preview_photo_' + imgLinkidx).remove();
            $.each(blogContent.image, function(i, val) {
                if (val.key == imgLinkidx) {
                    blogContent.image = $.grep(blogContent.image, function(n) {
                        return n.key != imgLinkidx;
                    });
                }
            });
            this.hidePreviewDiv();
        } ,
        hidePreviewDiv : function () {
            if ($("#ul_preview li").length == 0) {
                $(".rel_preview").css("display", 'none');
            }
        }

    }
    //截取文件名
    var subfilename = function (imgname, l) {
        var splitarr = imgname.split(".");
        var typestr = "." + splitarr[splitarr.length - 1];
        var ps = imgname.indexOf(typestr);
        var namestr = imgname.substr(0, ps);
        if (namestr.length > l) {
            return namestr.substr(0, l - 1) + "..." + namestr.charAt(namestr.length - 1) + typestr;
        }
        return imgname;
    }

    return postimg;
})

