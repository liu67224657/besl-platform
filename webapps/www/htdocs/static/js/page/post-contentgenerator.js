define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var common = require('../common/common');
    var contenttypeBiz = require('../biz/contenttype-biz');
    var forwardIndex = 0;
    var contentGenerator = {
        generator:function(blogContent,isHidden) {
            if (blogContent == null || blogContent.content == null || blogContent.profile == null) {
                return '';
            }

            var pVerifyType = '';
            //todo
            if (blogContent.profile.detail != null &&
                    blogContent.profile.detail.verifyType != null
                    && blogContent.profile.detail.verifyType.code != 'n') {
                  pVerifyType = '<a href="'+joyconfig.URL_WWW + '/people/' + blogContent.profile.blog.domain +  '" class="'+blogContent.profile.detail.verifyType.code+'vip" title="'+joyconfig.viptitle[blogContent.profile.detail.verifyType.code]+'"></a>';
            }

            var headIcon = '';
            if (blogContent.profile.blog.headIconSet != null && blogContent.profile.blog.headIconSet.iconSet != null
                    && blogContent.profile.blog.headIconSet.iconSet.length > 0) {
                for (var i = 0; i < blogContent.profile.blog.headIconSet.iconSet.length; i++) {
                    var icon = blogContent.profile.blog.headIconSet.iconSet[i];
                    if (icon.validStatus) {
                        headIcon = icon.headIcon;
                        break;
                    }
                }
            }

            var favText = '';
            if (blogContent.blogFavProfiles.length > 0) {
                favText += '<div class="attention_tips"> 你关注的';
                for (var i = 0; i < blogContent.blogFavProfiles.length; i++) {
                    favText += '<a href="'+joyconfig.URL_WWW + '/people/' + blogContent.blogFavProfiles[i].blog.domain + '" target="_blank">' + blogContent.blogFavProfiles[i].blog.screenName + '</a> ';
                    if (i != blogContent.blogFavProfiles.length - 1) {
                        favText += '、'
                    }
                }
                if (blogContent.followFavSums > 3) {
                    favText += '等' + blogContent.followFavSums + '人';
                }
                favText += '喜欢此篇文章</div>';
            }
            var hideStyle=(isHidden!=undefined && isHidden)?'display:none':'';

            var contentHtml = favText + '<div id="conent_' + blogContent.content.contentId + '" class="area clearfix" style="'+hideStyle+'">' +
                    '<dl>' +
                    '<dt class="personface">' +
                    '<a class="tag_cl_left" href="'+joyconfig.URL_WWW + '/people/' + blogContent.profile.blog.domain + '" name="atLink" title="' + blogContent.profile.blog.screenName + '">' +
                    '<img class="lazy" src="' + common.parseSimg(headIcon, joyconfig.DOMAIN) + '" original="' + common.parseSimg(headIcon, joyconfig.DOMAIN) + '" width="58px" height="58px"/>' +
                    '</a>' +
                    '</dt>' +
                    '<dd class="textcon">' +
                    '<a  href="'+joyconfig.URL_WWW + '/people/' + blogContent.profile.blog.domain + '" name="atLink"   class="author" title="' + blogContent.profile.blog.screenName + '">' +
                    blogContent.profile.blog.screenName +
                    '</a>' + pVerifyType + '<a class="chakan" href="/note/' + blogContent.content.contentId + '" target="_blank">查看全文</a>';
            contentHtml += initContent(blogContent);
            contentHtml += '</dd>' +
                    '</dl>' +
                    '<div style="clear:both"></div></div>';

            return {htmlId:'conent_' + blogContent.content.contentId,contentHtml:contentHtml};
        },
        generatorOnBlogList:function(blogContent) {
            if (blogContent == null || blogContent.content == null || blogContent.profile == null) {
                return '';
            }

            var pVerifyType = '';
            //todo
            if (blogContent.profile.detail != null &&
                    blogContent.profile.detail.verifyType != null
                    && blogContent.profile.detail.verifyType.code != 'n') {
                  pVerifyType = '<a href="'+joyconfig.URL_WWW + '/people/' + blogContent.profile.blog.domain + '" class="'+blogContent.profile.detail.verifyType.code+'vip" title="'+joyconfig.viptitle[blogContent.profile.detail.verifyType.code]+'"></a>';
            }

            var headIcon = '';
            if (blogContent.profile.blog.headIconSet != null && blogContent.profile.blog.headIconSet.iconSet != null
                    && blogContent.profile.blog.headIconSet.iconSet.length > 0) {
                for (var i = 0; i < blogContent.profile.blog.headIconSet.iconSet.length; i++) {
                    var icon = blogContent.profile.blog.headIconSet.iconSet[i];
                    if (icon.validStatus) {
                        headIcon = icon.headIcon;
                        break;
                    }
                }
            }

            var favText = '';
            if (blogContent.blogFavProfiles.length > 0) {
                favText += '<div class="attention_tips"> 你关注的';
                for (var i = 0; i < blogContent.blogFavProfiles.length; i++) {
                    favText += '<a href="'+joyconfig.URL_WWW + '/people/' + blogContent.blogFavProfiles[i].blog.domain + '" target="_blank">' + blogContent.blogFavProfiles[i].blog.screenName + '</a> ';
                    if (i != blogContent.blogFavProfiles.length - 1) {
                        favText += '、'
                    }
                }
                if (blogContent.followFavSums > 3) {
                    favText += '等' + blogContent.followFavSums + '人';
                }
                favText += '喜欢此篇文章</div>';
            }

            var contentHtml = favText + '<div id="conent_' + blogContent.content.contentId + '" class="area blogarea" style="display:none">' +
                    '<dl>' +
                    '<dd class="textcon">' +
                    '<a  href="'+joyconfig.URL_WWW + '/people/' + blogContent.profile.blog.domain + '" name="atLink"   class="author" title="' + blogContent.profile.blog.screenName + '">' +
                    blogContent.profile.blog.screenName +
                    '</a>' + pVerifyType + '<a class="chakan" href="/note/' + blogContent.content.contentId + '" target="_blank">查看全文</a>';
            contentHtml += initContent(blogContent);
            contentHtml += '</dd>' +
                    '</dl>' +
                    '<div style="clear:both"></div></div>';

            return {htmlId:'conent_' + blogContent.content.contentId,contentHtml:contentHtml};
        }
    }
    return contentGenerator;

    //根据转贴原贴生成帖子
    function initContent(blogContent) {
        var contentHtml = '';
        if (blogContent.content.publishType.code == 'org') {
            contentHtml = generatorByContentType(blogContent.profile, blogContent.content, false);
        } else {
            contentHtml = generatorForwardContent(blogContent);
        }
//        contentHtml += generatorTag(blogContent.content);
        contentHtml += generatorFooter(blogContent);
        return contentHtml;
    }

    //生成标签
//    function generatorTag(content) {
//        var tagHtml = '';
//        if (content.contentTag.tags != null && content.contentTag.tags.length > 0) {
//            tagHtml += '<div class="cont_tags"><p>';
//            $.each(content.contentTag.tags, function(i, val) {
//                tagHtml += '<a href="' + joyconfig.URL_WWW + '/search/s/' + val + '/">#' + val + '</a> ';
//            });
//            tagHtml += '</p></div>';
//        }
//        return tagHtml;
//    }

    function generatorForwardFooter(blogContent) {
        var fromStr = '来自：着迷网';
        if (blogContent.rootBoard != null) {
            fromStr = '来自：<a href="' + joyconfig.URL_WWW + '/group/' + blogContent.rootBoard.gameCode + '" target="_blank">' + blogContent.rootBoard.gameName + '小组</a>';
        }
        var forwardFooterHtml = '<div class="area_ft"><span class="time">' + generateDate(blogContent.rootContent.publishDate) + '</span><span class="from">' + fromStr + '</span><div class="operate"><ul>'
        if (blogContent.rootContent.uno == joyconfig.joyuserno && blogContent.rootContent.voteSubjectId == null) {
            forwardFooterHtml += '<li><a href="' + joyconfig.URL_WWW + '/content/edit/' + blogContent.rootContent.uno + '/' + blogContent.rootContent.contentId + '" class="listedit" title="编辑">编辑</a><em>|</em></li>';
        }
//        forwardFooterHtml += '<li><a href="' + joyconfig.URL_WWW + '/home/' + blogContent.rootProfile.blog.domain + '/' + blogContent.rootContent.contentId + '"  class="share" title="热度">热度(<span name="hot_num_' + blogContent.rootContent.contentId + '">' + (blogContent.rootContent.replyTimes + blogContent.rootContent.favorTimes) + '</span>)</a><em>|</em></li>'
        forwardFooterHtml += '<li><a href="' + joyconfig.URL_WWW + '/note/' + blogContent.rootContent.contentId + '"  class="share" title="评论">评论'
        if (blogContent.rootContent.replyTimes > 0) {
            forwardFooterHtml += '(' + blogContent.rootContent.replyTimes + ')'
        }
        forwardFooterHtml += '</a></li>'
        forwardFooterHtml += '<li><a href="javascript:void(0);" name="favLink" data-cid="' + blogContent.rootContent.contentId + '" data-cuno="' + blogContent.rootContent.uno + '" class="favlink" title="' + (blogContent.rootFavorite ? '取消喜欢' : '喜欢') + '">' + (blogContent.rootFavorite ? '<i class="step7"></i>' : '<i class="step1"></i>') + '<span>' + (blogContent.rootContent.favorTimes <= 0 ? '喜欢' : blogContent.rootContent.favorTimes) + '</span></a></li>'
        forwardFooterHtml += '</ul></div></div>';
        return forwardFooterHtml;
    }

    function generatorFooter(blogContent) {
        var fromStr = '来自：着迷网';
        if (blogContent.board != null) {
            fromStr = '来自：<a href="' + joyconfig.URL_WWW + '/group/' + blogContent.board.gameCode + '" target="_blank">' + blogContent.board.gameName + '小组</a>';
        }

        var footerHtml = '<div class="area_ft"><span class="time">' + generateDate(blogContent.content.publishDate) + '</span><span class="from">' + fromStr + '</span><div class="operate"><ul>'
        if (blogContent.content.uno == joyconfig.joyuserno) {
            if (blogContent.content.voteSubjectId == null) {
                if (blogContent.content.publishType.code == 'fwd') {
                    //todo
                    footerHtml += '<li><a href="javascript:void(0);" id="edit_forward_' + blogContent.content.contentId + '" data-cid="' + blogContent.content.contentId + '" data-cuno="' + blogContent.content.uno + '" class="listedit" title="编辑">编辑</a><em>|</em></li>';
                } else {
                    footerHtml += '<li><a href="' + joyconfig.URL_WWW + '/content/edit/' + blogContent.content.uno + '/' + blogContent.content.contentId + '" class="listedit" title="编辑">编辑</a><em>|</em></li>';
                }
            }

            footerHtml += '<li><a href="javascript:void(0);" id="link_del_' + blogContent.content.contentId + '" data-cid="' + blogContent.content.contentId + '" data-uno="' + blogContent.content.uno + '" class="remove" title="删除">删除</a><em>|</em></li>'
        }

//        footerHtml += '<li><a href="javascript:void(0);" data-itype="hot" name="hotLink" data-cid="' + blogContent.content.contentId + '" data-cuno="' + blogContent.content.uno + '" class="share" title="热度">热度(<span name="hot_num_' + blogContent.content.contentId + '">' + (blogContent.content.replyTimes + blogContent.content.favorTimes) + '</span>)</a><em>|</em></li>'
        footerHtml += '<li><a href="javascript:void(0);" data-itype="reply" name="replyLink" data-cid="' + blogContent.content.contentId + '" data-cuno="' + blogContent.content.uno + '" class="share" title="评论">评论';
        if (blogContent.content.replyTimes > 0) {
            footerHtml += '(' + blogContent.content.replyTimes + ')'
        }
        footerHtml += '</a></li>'
        footerHtml += '<li><a href="javascript:void(0);" name="favLink" class="favlink" title="' + (blogContent.favorite ? '取消喜欢' : '喜欢') + '" data-cid="' + blogContent.content.contentId + '" data-cuno="' + blogContent.content.uno + '">' + (blogContent.favorite ? '<i class="step7"></i>' : '<i class="step1"></i>') + '<span>' + (blogContent.content.favorTimes <= 0 ? '喜欢' : blogContent.content.favorTimes) + '</span></a></li>'
        footerHtml += '</ul></div></div>';
        return footerHtml;
    }

    //生成转贴
    function generatorForwardContent(blogContent) {

        if (blogContent.rootContent.removeStatus.code == 'y') {
            return generatorRemove(blogContent);
        }

        var pVerifyType = '';
        //todo
        if (blogContent.rootProfile.detail != null &&
                blogContent.rootProfile.detail.verifyType != null
                && blogContent.rootProfile.detail.verifyType.code != 'n') {
            pVerifyType = '<a href="'+joyconfig.URL_WWW + '/people/' + blogContent.rootProfile.blog.domain + '" class="' + blogContent.rootProfile.detail.verifyType.code + 'vip" title="' + joyconfig.viptitle[blogContent.rootProfile.detail.verifyType.code] + '"></a>';
        }
        var imageHtml = '';
        if (blogContent.content.images != null && blogContent.content.images.images.length > 0) {
            if (blogContent.content.images.images.length == 1) {
                imageHtml += '<div class="single_pic" id="img_' + blogContent.content.contentId + '" data-cuno="' + blogContent.rootProfile.blog.uno + '" data-cid="' + blogContent.content.contentId + '">';
                imageHtml += '<ul><li><p><img src="' + common.parseSSimg(blogContent.content.images.images[0].ss, joyconfig.DOMAIN) + '" original="' + common.parseSSimg(blogContent.content.images.images[0].ss, joyconfig.DOMAIN) + '" data-jw="' + blogContent.content.images.images[0].w + '" data-jh="' + blogContent.content.images.images[0].h + '"/></p></li></ul></div>';
            } else {
                imageHtml += '<ul class="multipic clearfix">';
                $.each(blogContent.content.images.images, function(i, val) {
                    if (i > 2) {
                        imageHtml += '<li style="display:none"><p><img class="lazy" src="' + common.parseMimg(val.ss, joyconfig.DOMAIN) + '" original="' + common.parseMimg(val.ss, joyconfig.DOMAIN) + '" data-jw="' + val.w + '" data-jh="' + val.h + '"/></p></li>';
                    } else {
                        imageHtml += '<li><p><img class="lazy" src="' + common.parseSSimg(val.ss, joyconfig.DOMAIN) + '"  original="' + common.parseSSimg(val.ss, joyconfig.DOMAIN) + '" data-jw="' + val.w + '" data-jh="' + val.h + '"/></p></li>';
                    }
                    imageHtml += '';
                });
                imageHtml += '</ul>';
                imageHtml += '<p>（共' + blogContent.content.images.images.length + '张，点击图片查看更多）</p>'
            }

        }

        var forwardContent = '<p class="discuss_txt">' + blogContent.content.content + '</p>' + imageHtml +
                '<div class="disarea clearfix"><div class="discuss_corner"><span class="discorner"></span></div>' +
                '<div class="discuss_area clearfix">' +
//                '<div class="shadow"></div>' +
                '转发自<a class="author" href="http://' + blogContent.rootProfile.blog.domain + '.' + joyconfig.DOMAIN + '"' +
                ' name="atLink" title="' + blogContent.rootProfile.blog.screenName + '"> @' + blogContent.rootProfile.blog.screenName + '</a>' +
                pVerifyType +
                '<a class="chakan" href="/note/' + blogContent.rootContent.contentId + '" target="_blank">查看全文</a>';

        forwardContent += generatorByContentType(blogContent.rootProfile, blogContent.rootContent, true);
        forwardContent += generatorForwardFooter(blogContent);
        forwardContent += '</div></div>';
        return forwardContent;
    }

    //生成原贴
    function generatorByContentType(profile, content, isForward) {
        var contentByType = '';
        if (contenttypeBiz.hasPhrase(content.contentType.value)) {
            contentByType = generatorPhrase(profile, content, isForward, true);
        } else {
            contentByType = generatorText(profile, content, isForward);
        }
        return contentByType;
    }

    function generatorRemove(blogContent) {
        var contentText = '<p class="discuss_txt">' + blogContent.content.content + '</p>' +
                '<div class="disarea clearfix">' +
                '<div class="discuss_corner">' +
                '<span class="discorner"></span>' +
                '</div>' +
                '<div class="discuss_area clearfix">' +
//                '<div class="shadow"></div>' +
                '<div class="contentremove">' +
                '<p>文章已删除或者不存在！</p>' +
                '</div>' +
                '</div>' +
                '</div>';
        return contentText;
    }

    //一句话
    /**
     *
     * @param profile 作者
     * @param content 文章
     * @param isForward 是否是转贴
     * @param ignoreImageRules 是否忽略文章规则，false，值显示大于100的图，true，无论尺寸多少都显示
     */
    function generatorPhrase(profile, content, isForward, ignoreImageRules) {
        var phraseHtml = '<div><p>' + content.content;
        if (contenttypeBiz.hasText(content.contentType.value) && common.endsWith(content.content, '...')) {
            phraseHtml += '<br/><a href="'+joyconfig.URL_WWW+'/note/' + content.contentId + '" target="_blank">' +
                    ' 未完，阅读全文... ' +
                    '</a>';
        }
        phraseHtml += '</p></div>';

        var imageHtml = '';
        if (content.images != null && content.images.images.length > 0) {
            if (content.images.images.length == 1) {
                if (ignoreImageRules || (content.images.images[0].w >100 || content.images.images[0].h > 100)) {
                    imageHtml += '<div class="single_pic" id="img_' + content.contentId + '">';
                    imageHtml += '<ul><li><p><img src="' + common.parseSSimg(content.images.images[0].ss, joyconfig.DOMAIN) + '" original="' + common.parseSSimg(content.images.images[0].ss, joyconfig.DOMAIN) + '" data-jw="' + content.images.images[0].w + '" data-jh="' + content.images.images[0].h + '"/></p></li></ul></div>';
                }
            } else {
                imageHtml += '<ul class="multipic clearfix">';
                var showNo = 0;
                for (var imgIdx = 0; imgIdx < content.images.images.length; imgIdx++) {
                    var val = content.images.images[imgIdx];
                    if (ignoreImageRules || (val.w > 100 || val.h > 100)) {
                        if (showNo > 2) {
                            imageHtml += '<li style="display:none"><p><img class="lazy" src="' + common.parseMimg(val.ss, joyconfig.DOMAIN) + '"  original="' + common.parseMimg(val.ss, joyconfig.DOMAIN) + '" data-jw="' + val.w + '" data-jh="' + val.h + '"/></p></li>';
                        } else {
                            imageHtml += '<li><p><img class="lazy" src="' + common.parseSSimg(val.ss, joyconfig.DOMAIN) + '"  original="' + common.parseSSimg(val.ss, joyconfig.DOMAIN) + '" data-jw="' + val.w + '" data-jh="' + val.h + '"/></p></li>';
                            showNo++;
                        }
                    }
                }

                imageHtml += '</ul>';
                imageHtml += '<p>（共' + content.images.images.length + '张，点击图片查看更多）</p>'
            }

        }
        var videoHtml = '';
        if (content.videos != null && content.videos.videos.length > 0) {
            $.each(content.videos.videos, function(i, val) {
                videoHtml += '<div class="single_video">' +
                        '<a href="javascript:void(0);" data-flashurl="' + val.flashUrl + '" name="preivewvideo">' +
                        '<img class="lazy" width="188" height="141" src="' + val.url + '" original="' + val.url + '">' +
                        '</a>' +
                        '<a class="video_btn" title="播放" href="javascript:void(0)" data-flashurl="' + val.flashUrl + '" name="preivewvideo"></a>' +
                        '</div>'
            });
        }
        var audioHtml = '';
        if (content.audios != null && content.audios.audios.length > 0) {
            if (content.content == '' &&
                    (content.subject == null || content.subject.length == 0) &&
                    (content.videos == null || content.videos.videos.length == 0) &&
                    (content.images == null || content.images.images.length == 0)) {
                $.each(content.audios.audios, function(i, val) {
                    audioHtml += '<div class="single_music">' +
                            '<object align="middle" width="283" height="33" ' +
                            'classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" ' +
                            'codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,0,0">' +
                            '<param name="allowScriptAccess" value="sameDomain">' +
                            '<param name="movie" value="' + val.flashUrl + '">' +
                            '<param name="wmode" value="Transparent">' +
                            '<embed width="283" height="33" wmode="Transparent" type="application/x-shockwave-flash" src="' + val.flashUrl + '">' +
                            '</object>' +
                            '</div>' +
                            '<div class="single_music_view clearfix">' +
                            '<img width="141" height="147" src="' + common.genImgUrlBySuffix(val.url, '2') + '">' +
                            '</div>';
                });
            } else {
                $.each(content.audios.audios, function(i, val) {
                    audioHtml += '<div class="single_music">' +
                            '<object align="middle" width="283" height="33" ' +
                            'classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" ' +
                            'codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,0,0"> ' +
                            '<param name="allowScriptAccess" value="sameDomain"> ' +
                            '<param name="movie" value="' + val.flashUrl + '"> ' +
                            '<param name="wmode" value="Transparent">' +
                            '<embed width="283" height="33" wmode="Transparent" type="application/x-shockwave-flash" ' +
                            'src="' + val.flashUrl + '">' +
                            '</object>' +
                            '</div>';
                });
            }

        }
        var voteHtml = '';
        if (content.vote != null) {
            var voteImg = '';
            if (content.vote.voteSubject.imageSet != null && content.vote.voteSubject.imageSet.images.length > 0) {
                $.each(content.vote.voteSubject.imageSet.images, function(i, val) {
                    voteImg = '<img src="' + common.parseSSimg(val.ss, joyconfig.DOMAIN) + '" data-jw="' + val.w + '" data-jh="' + val.h + '" width="102" height="101">';
                });
            } else {
                voteImg = '<img src="' + joyconfig.URL_LIB + '/static/theme/default/img/poll_box_normal_ico.jpg">';
            }

            var optionStr = '';
            var index = 0;

            $.each(content.vote.voteOptionMap, function(i, val) {
                if (index < 2) {
                    optionStr += '<p>' + common.subStr(val.description, 12) + '</p>';
                }
                index++;
            });
            if (index > 2) {
                optionStr += '<p>...</p>';
            }

            if (isForward) {
                forwardIndex++;
                voteHtml = '<div id="' + forwardIndex + '_forward_' + content.contentId + '" class="poll_box_normal">' + voteImg +
                        '<h3>' + common.subStr(content.vote.voteSubject.subject, 15) + '</h3>' +
                        optionStr +
                        '<a data-sid="' + content.vote.voteSubject.subjectId + '" data-duno="' + profile.blog.uno + '" data-did="' + content.contentId + '" data-domid="' + forwardIndex + '_forward_' + content.contentId + '" name="prePartVote" class="submit_poll"><span>投 票</span></a>' +
                        '</div>';
            } else {
                voteHtml = '<div id="vote_' + content.contentId + '" class="poll_box_normal">' + voteImg +
                        '<h3>' + common.subStr(content.vote.voteSubject.subject, 15) + '</h3>' +
                        optionStr +
                        '<a data-sid="' + content.vote.voteSubject.subjectId + '" data-duno="' + profile.blog.uno + '" data-did="' + content.contentId + '" data-domid="vote_' + content.contentId + '" name="prePartVote" class="submit_poll"><span>投 票</span></a>' +
                        '</div>';
            }

        }
        return phraseHtml + imageHtml + videoHtml + audioHtml + voteHtml;
    }

    //长文
    function generatorText(profile, content, isForward) {
        var textHtml = ''
        if (content.subject.length > 0) {
            textHtml += '<h3>';
            var subject = content.subject.length > 30 ? content.subject.substring(0, 29) + "…" : content.subject;
            textHtml += '<a href="'+joyconfig.URL_WWW+'/note/' + content.contentId + '" target="_blank" title="' + content.subject + '">'
                    + (subject || "").replace(/</g, "&lt;").replace(/>/g, "&gt;")
                    + '</a>';
            textHtml += '</h3>';
        }

        if (contenttypeBiz.hasApp(content.contentType.value) &&  content.apps!=null && content.apps.apps.length>0) {
            textHtml += '<div class="longmode clearfix"><div class="tappic">';
            var appMaskClass = isForward ? 'pmask' : 'mask';
            $.each(content.apps.apps, function(i, val) {
                textHtml += '<a href="' + val.resourceUrl + '" target="_blank">' +
                        '<img class="lazy" src="' + common.parseBimg(val.appSrc, joyconfig.DOMAIN) + '" original="' + common.parseBimg(val.appSrc, joyconfig.DOMAIN) + '">' +
                        '<span class="' + appMaskClass + '"></span>' +
                        '</a>'
            })
            textHtml += '</div>';
            textHtml += content.content;
            if (common.endsWith(content.content, '...')) {
                textHtml += '<br/><a href="'+joyconfig.URL_WWW+'/note/' + content.contentId + '" target="_blank">' +
                        ' 未完，阅读全文... ' +
                        '</a>';
            }
            textHtml += '</div>';
            return textHtml;
        }

        textHtml += generatorPhrase(profile, content, isForward, false);
        return textHtml;
    }

    function generateDate(date) {
        var dateArray = date.split('-');
        var year = dateArray[0]
        var month = dateArray[1]
        var dayArray = dateArray[2].split(' ');
        var day = dayArray[0];
        var hhmm = dayArray[1];
        if (common.isCurrentDay(year, month, day)) {
            return '今天 ' + hhmm;
        } else {
            if (common.isCurrentYear(year)) {
                return  month + "月" + day + "日";
            }

            return year + "-" + month + "-" + day + "-";
        }


    }
});
