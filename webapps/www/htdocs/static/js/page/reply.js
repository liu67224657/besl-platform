define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var common = require('../common/common');
    var moodBiz = require('../biz/mood-biz');
    var replyBiz = require('../biz/reply-biz');
    var ajaxverify = require('../biz/ajaxverify');
    require('../common/tips');
    var joymealert = require('../common/joymealert');
    var login = require('../biz/login-biz');
    require('../common/jquery.autotextarea')($);
    require('../common/tips');

    var reply = {
        bindMood:function() {
            var te = new TextareaEditor(document.getElementById('textarea_messagebody'));
            $('#message_mood').live('click', function() {
                var config = {
                    allowmultiple:true,
                    isremovepop:true,
                    isfocus:false
                };
                moodBiz.docFaceBind($(this), 'textarea_messagebody', config, te);
            });
        },
        replyInit:function() {
            $("a[name='replyLink']").live('click', function() {
                if (login.checkLogin($(this))) {
                    var replyid = $(this).attr("reply-id");
                    var replyuno = $(this).attr("reply-uno");

                    var replayArea = $('#cont_cmt_' + replyid);
                    if (replayArea.length == 0 || !replayArea.is(':visible')) {
                        var privilegeObj = ajaxverify.verifyPrivilege(true);
                        if (privilegeObj.status_code == '-1') {
                            login.maskLoginByJsonObj(privilegeObj);
                            return;
                        } else if (privilegeObj.status_code != '1') {
                            var alertOption = {text:privilegeObj.msg,tipLayer:true,textClass:"tipstext"};
                            joymealert.alert(alertOption);
                            return;
                        }
                    }

                    if (replayArea.length == 0) {
                        replyBiz.getreplyobject($(this), replyid, replyuno, generatorLoadingArea, loadReplyLayer);
                    } else {
                        showreplyhtml(replyid, 'cont_cmt_', $(this));
                    }
                }
            })
        },
        reReplyOnReceiveInit:function() {
            $("a[name=reReply]").live("click", function() {
                var replyPostDom = $(this).parent().parent().parent().find(".mycomment_c");
                if (replyPostDom.is(':visible')) {
                    var privilegeObj = ajaxverify.verifyPrivilege(true);
                    if (privilegeObj.status_code == '-1') {
                        login.maskLoginByJsonObj(privilegeObj);
                        return;
                    } else if (privilegeObj.status_code != '1') {
                        var alertOption = {text:privilegeObj.msg,tipLayer:true,textClass:"tipstext"};
                        joymealert.alert(alertOption);
                        return;
                    }
                }
                if (replyPostDom.size() > 0) {
                    $(".mycomment_c").slideUp();
                    slideToggleFocus(replyPostDom, replyPostDom.children().children().children());
                } else {
                    $(".mycomment_c").slideUp();
                    loadReReplyLayerOnReceiveList($(this));
                }
            });
        },

        reReplyOnAtme:function() {
            $("a[name=reAtmeReply]").live("click", function() {
                if ($(this).parent().parent().parent().find(".mycomment_c").size() > 0) {
                    var replyPostDom = $(this).parent().parent().parent().find(".mycomment_c");
                    slideToggleFocus(replyPostDom, replyPostDom.children().children().children());
                } else {
                    loadReReplyLayerOnReceiveList($(this));
                }
            });
        },
        removeReplyOnReceiveInit:function() {
            $("a[name=removeReply]").live("click", function() {
                var replyObj = $(this);
                joymealert.confirm({offset:'',tipLayer:true,text:'确定删除该评论？',submitFunction:function() {
                            var paramObj = {
                                replyid:replyObj.attr("replyId"),
                                cid:replyObj.attr("cid"),
                                cuno:replyObj.attr("cuno")
                            }
                            replyBiz.deleteReply(paramObj, removeReply);
                        }});
            });
        },
        postReplyOnBlog:function() {
            $('#reply_submit').live('click', function() {
                var replyContent = $('#reply_content').val();
                $('#reply_content').blur();
                var cid = $('#hidden_cid').val();
                var cuno = $('#hidden_cuno').val();
                var forwardroot = $('#check_forwardroot').attr('checked');
                var replyroot = false;
                var rcid = '';
                var rdomain = '';
                var rname = ''
                if ($('#check_replyroot').size() > 0) {
                    replyroot = $('#check_replyroot').attr('checked');
                    rcid = $('#hidden_rcid').val();
                    rdomain = $('#hidden_rdomain').val();
                    rname = $('#hidden_rname').val();
                }

                var alertText = ''
                if (replyContent == null || $.trim(replyContent).length == 0) {
                    alertText = tipsText.comment.blog_pl_content_notnull;
                } else if (common.getInputLength(replyContent) > tipsText.comment.blog_reply_length) {
                    alertText = tipsText.comment.blog_pl_content_maxlength;
                } else if (!ajaxverify.verifyPost(replyContent)) {
                    alertText = tipsText.userSet.user_word_illegl;
                }
                var privilegeObj = ajaxverify.verifyPrivilege(true);
                if (privilegeObj.status_code == '-1') {
                    login.maskLoginByJsonObj(privilegeObj);
                    return;
                } else if (privilegeObj.status_code != '1') {
                    alertText = privilegeObj.msg;
                }

                if (alertText.length > 0) {
                    joymealert.alert({text:alertText,tipLayer:true,textClass:"tipstext"});
                    return false;
                }
                var paramObj = {cid:cid,
                    cuno:cuno,
                    replycontent:replyContent,
                    forwardroot:forwardroot,
                    replyroot:replyroot,pid:rcid,puno:''};

                checkContextAt(paramObj, function(paramObj, replyDom) {
                    replyBiz.commonreply(paramObj, loadreplyOnBlogCallback, publishReplyBlogCallback, replyDom);
                }, $(this));
            });
        },
        reReplyOnBlog:function() {
            $('a[name="reReplyLink"]').live('click', function() {
                var pid = $(this).attr('data-pid');
                var replyArea = $('#rereply_area_' + pid);
                if (replyArea.size() == 0) {
                    var puno = $(this).attr('data-puno');
                    var pscreenName = $(this).attr('data-pname');
                    var cid = $(this).attr('data-cid');
                    var cuno = $(this).attr('data-cuno');
                    loadBLogReReplyLayer($(this), pid, puno, cid, cuno, pscreenName);
                    replyArea = $('#rereply_area_' + pid);
                }

                if (replyArea.is(':hidden')) {
                    $('.blog_discusson').slideUp();
                    replyArea.slideDown();
                } else {
                    replyArea.slideUp();
                }
            });
        },
        deleteReplyOnBlog:function() {
            $('a[id^=del_reply_]').live('click', function() {
                var linkObj = $(this);
                var id = linkObj.attr('id');
                var replyId = id.substr('del_reply_'.length, id.length - 'del_reply_'.length);
                var cid = linkObj.attr('data-cid');
                var cuno = linkObj.attr('data-cuno');
                var deleteBlog = function() {
                    replyBiz.deleteReply({replyid:replyId,cid:cid,cuno:cuno}, delReplyBlogCallback);
                }

                var confirmOption = {
                    text:'确定要删除该评论？',
                    width:229,
                    submitButtonText:'删 除',
                    submitFunction:deleteBlog,
                    cancelButtonText:'取 消',
                    cancelFunction:null};
                joymealert.confirm(confirmOption);
            });
        },
        replyOnWallInit:function() {
            $("#reply_submit").die().live('click', function() {
                if (joyconfig.joyuserno == '') {
                    var discovery = require('./discovery');
                    discovery.appendLogin(showReplyOnWall);
                    return;
                } else {
                    showReplyOnWall();
                }

            });
            //表情

            $("#reply_mood").die().live('click', function() {
                var te = new TextareaEditor(document.getElementById('reply_content'));
                var config = {
                    allowmultiple:true,
                    isremovepop:true,
                    isfocus:false
                };
                moodBiz.docFaceBindOnWall($(this), 'reply_content', config, te);
            });
            $('#reply_content').die().live('keyup keydown', function(event) {
                common.checkInputLength(tipsText.comment.blog_reply_length, 'reply_content', 'reply_num');
                common.joymeCtrlEnter(event, $('#reply_submit'))
            });

            $('a[name="reReplyLink"]').die().live('click', function() {
                if (joyconfig.joyuserno == '') {
                    var discovery = require('./discovery');
                    discovery.appendLogin(showReReplyOnWall, [$(this)]);
                    return;
                } else {
                    showReReplyOnWall($(this));
                }
            });

            $('a[id^=del_reply_]').live('click', function() {
                var linkObj = $(this);
                var id = linkObj.attr('id');
                var replyId = id.substr('del_reply_'.length, id.length - 'del_reply_'.length);
                var cid = linkObj.attr('data-cid');
                var cuno = linkObj.attr('data-cuno');
                var deleteBlog = function() {
                    replyBiz.deleteReply({replyid:replyId,cid:cid,cuno:cuno}, delReplyBlogCallback);
                }

                var confirmOption = {
                    text:'确定要删除该评论？',
                    width:229,
                    submitButtonText:'删 除',
                    submitFunction:deleteBlog,
                    cancelButtonText:'取 消',
                    cancelFunction:null,
                    confirmid:'wallconfirm'};
                joymealert.confirm(confirmOption);

                var moodstyle = $("#joymeconfirm_wallconfirm").attr('style');
                $("#joymeconfirm_wallconfirm").removeAttr('style').attr('style', moodstyle.replace('absolute', 'fixed'));
            });
        }

    }
    var loadReplyLayer = function(dom, cid, cuno, resultMsg) {
//        resultMsg.blogContent.content.contentId
        if (resultMsg.status_code == '-1') {
            login.maskLoginByJsonObj(resultMsg);
            return;
        } else if (resultMsg.status_code != '1') {
            $('#reply_loading_' + cid).replaceWith(generatorErrorArea(cid, cuno));

            $('#reloadreply_but_' + cid).die().live('click', function() {
                replyBiz.getreplyobject($(this), cid, cuno, null, loadReplyLayer);
            })
            return;
        }
        var result = resultMsg.result[0];

        //向该文章插入html
        var replyhtml = loadtextarea(result.blogContent, result.interactionInfoList);

        //显示评论层
        $('#reply_loading_' + result.blogContent.content.contentId).replaceWith(replyhtml);
        replylivemood(result.blogContent.content.contentId, "replyCont_" + result.blogContent.content.contentId);


        var replyid = dom.attr("reply-id");
        //为评论框绑定自适应高度方法
        $('#replyCont_' + replyid).AutoHeight({maxHeight:200});

        //注册点击事件
        replylivesubfun(result.blogContent.content.contentId, result.blogContent.content.uno, '', '');
        replyCenterfun(result.blogContent.content.contentId, 'replyCont_');
        showReplyLayerFocus(cid, 'replyCont_', dom);
    }

    //加载评论层内容
    var loadtextarea = function(blogContent, replyInfoList) {
        var replyhtml = '<div id="replypostarea_' + blogContent.content.contentId + '">' +
                '<div class="commentbox clearfix">' +
                '<div class="commenttextDiv"><textarea class="commenttext" style="heigth:28px; font-family:Tahoma, \'宋体\';" id="replyCont_' + blogContent.content.contentId + '"></textarea></div>' +
                '<a name="subreplybtn" class="submitbtn" id="subreply_' + blogContent.content.contentId + '"><span>评论</span></a></div>' +
                '<div class="related clearfix"><div class="commenface"><a href="javascript:void(0)" id="replyContmood_' + blogContent.content.contentId + '" title="表情"></a></div></div>';
        if (blogContent.content.publishType.code == 'org') {
            replyhtml += '<p><input class="checktext" type="checkbox" name="forwardRoot"><label for="checkbox">同时转发到我的博客</label></p>';
        } else if (blogContent.content.publishType.code == 'fwd' && blogContent.rootContent != undefined && blogContent.rootContent.removeStatus.code == 'n') {
            replyhtml += '<p><input class="checktext" type="checkbox" name="forwardRoot"><label for="checkbox">同时转发到我的博客</label></p>';
            replyhtml += '<p><input class="checktext" type="checkbox" name="replayRoot"><label for="checkbox01">同时评论给原文作者</label> <a name="atLink" title="' + blogContent.rootProfile.blog.screenName + '" class="author" href="#">' + blogContent.rootProfile.blog.screenName + '</a></p>';
        }
        replyhtml += '</div>';

        //追加评论内容层
        replyhtml += loadreplyInfoList(replyInfoList, blogContent.content.contentId, blogContent.content.contentuno, blogContent.content.replyTimes);

        return replyhtml
    }

    var generatorErrorArea = function(cid, cuno) {
        return '<div id="reply_loading_' + cid + '" class="discuss_load">' +
                '<p>读取失败，请<a href="javascript:void(0);" id="reloadreply_but_' + cid + '" title="评论" reply-id="' + cid + '" reply-uno="' + cuno + '">重试</a></p>' +
                '</div>'
    }

    var generatorLoadingArea = function(dom, cid) {
        var loadingHtml = '<div class="disarea replytextarea" id="cont_cmt_' + cid + '" style="display:none">' +
                '<div class="discussoncorner"><span class="discorner"></span></div>' +
                '<div class="discusson">' +
                '<div id="reply_loading_' + cid + '" class="discuss_load">' +
                '<p>读取中，请稍候... </p>' +
                '</div>' +
                '</div></div>';
        //向页面插入评论层
        dom.parent().parent().after(loadingHtml);
        //显示评论层
        showreplyhtml(cid, 'cont_cmt_', dom);
    }

    var replylivemood = function(cid, textareaID) {
        var te = new TextareaEditor(document.getElementById(textareaID));
        $("#replyContmood_" + cid).live('click', function() {
            var config = {
                allowmultiple:true,
                isremovepop:true,
                isfocus:true
            };
            moodBiz.docFaceBindOnReply($(this), textareaID, config, te);
        });
    }

    //加载评论回复内容
    var loadreplyInfoList = function(replyInfoList, contentId, uno, replyTimes) {
        var replyInfoHtml = "";
        if (replyInfoList.length > 0) {
            $.each(replyInfoList, function(i, val) {
                replyInfoHtml += assemparenthtml(val);
                if (i == 9) {
                    replyInfoHtml += '<div class="conmenttx clearfix">' +
                            '<div class="conmentface"></div><div class="conmentcon" style="text-align: right;">后面还有' +
                            (replyTimes - 10) +
                            '条评论 <a href="' + joyconfig.URL_WWW + '/note/' + contentId + '" style="margin:0">点击查看&gt;</a></div></div>';

                    return;
                }
            })
        }
        return replyInfoHtml;
    }
    //拼装每一条评论回复内容  参数为每条回复记录对象
    var assemparenthtml = function(val) {
        var replyInfoHtml = "";
        var headimg = "";//左侧头像内容
        var delStr = '';

        if (val.replyProfile.blog.headIconSet == null || val.replyProfile.blog.headIconSet.iconSet == null
                || val.replyProfile.blog.headIconSet.iconSet.length <= 0) {
            headimg = joyconfig.URL_LIB + '/static/theme/default/img/default.jpg';
        } else {
            for (var i = 0; i < val.interactionProfile.blog.headIconSet.iconSet.length; i++) {
                if (val.interactionProfile.blog.headIconSet.iconSet[i].validStatus) {
                    headimg = common.parseSimg(val.interactionProfile.blog.headIconSet.iconSet[i].headIcon, joyconfig.DOMAIN);
                    break;
                }
            }
        }

        var rightStr = '';//右侧文本内容
        if (val.interaction.contentUno == joyconfig.joyuserno || val.interaction.replyUno == joyconfig.joyuserno) {
            delStr = '<a href="javascript:void(0);" id="del_reply_' + val.interaction.interactionId + '">删除</a> |  ';
            //为该段代码注册live事件
            replyProfileLivedel(val);
        }
        var contentId = val.interaction.contentId;
        var contentUno = val.interaction.contentUno;
        var rootContent = val.content.publishType.code;
        var replyId = val.interaction.interactionId;
        var replyUno = val.interaction.interactionUno;
        var replyScreenName = val.interactionProfile.blog.screenName;
        if (val.content.publishType.code == 'fwd') {
            rightStr = '<span class="delete" >' + delStr +
                    ' <a href="javascript:void(0);"  id="sub_reply_' + val.interaction.interactionId + '">回复</a>' +
                    '</span>';
            var contentScreenName = val.rootProfileBlog != null ? val.rootProfileBlog.screenName : '';
            var hrefUser = val.rootProfileBlog != null ? val.rootProfileBlog.domain + '.' + joyconfig.DOMAIN : '';

            //为该段代码注册live事件
            replyProfileLiveforward(contentId, contentUno, rootContent, contentScreenName, hrefUser, replyId, replyUno, replyScreenName, val);
        } else {
            rightStr = '<span class="delete">' + delStr +
                    ' <a id="sub_reply_' + val.interaction.interactionId + '" href="javascript:void(0)">回复</a>' +
                    '</span>';
            //为该段代码注册live事件
            replyProfileLiveforward(contentId, contentUno, rootContent, '', '', replyId, replyUno, replyScreenName, val);
        }
        var verifyType = val.interactionProfile.detail.verifyType;
        var vipStr = '';
        if (verifyType != null) {
            if (verifyType.code == 'p') {
                vipStr = '<a href="http://' + val.interactionProfile.blog.domain + '.' + joyconfig.DOMAIN + '" class="vip" title="' + joyconfig.vip_title + '"></a>';
            }
            if (verifyType.code == 'c') {
                vipStr = '<a href="http://' + val.interactionProfile.blog.domain + '.' + joyconfig.DOMAIN + '" class="tvip" title="' + joyconfig.vipc_title + '"></a>';
            }
        }
        replyInfoHtml += '<div class="conmenttx clearfix" id="cont_cmt_list_' + val.interaction.interactionId + '">' +
                '<div class="conmentface" id="cont_cmt_list_' + val.interaction.interactionId + '">' +
                '<div class="commenfacecon">' +
                '<a class="cont_cl_left" name="atLink" title="' + val.interactionProfile.blog.screenName + '" href="http://' + val.interactionProfile.blog.domain + '.' + joyconfig.DOMAIN + '">' +
                '<img src="' + headimg + '" width="33px" height="33px"/>' +
                '</a>' +
                '</div>' +
                '</div>' +
                '<div class="conmentcon">' +
                '<a href="http://' + val.interactionProfile.blog.domain + '.' + joyconfig.DOMAIN + '" name="atLink" title="' + val.interactionProfile.blog.screenName + '">' + val.interactionProfile.blog.screenName + '</a>' + vipStr + '：' + val.interaction.interactionContent +
                '<div class="commontft clearfix"><span class="reply_time">' + val.interaction.createDateStr + '</span>' + rightStr + '</div>' +
                '</div>' +
                '</div>';
        return replyInfoHtml;
    }
    var replyProfileLivedel = function(val) {
        $("#del_reply_" + val.interaction.interactionId).live('click', function() {
            joymealert.confirm({offset:'',tipLayer:true,text:'确定删除该评论？',submitFunction:function() {
                        var paramObj = {replyid:val.interaction.interactionId ,cid:val.interaction.contentId,cuno: val.interactionProfile.blog.uno}
                        replyBiz.deleteReply(paramObj, removeReply);
                    }});
        });
    }
    //注册评论的回复事件
    var replyProfileLiveforward = function(contentId, contentUno, rootContent, contentScreenName, hrefUser, replyId, replyUno, replyScreenName, val) {
        $("#sub_reply_" + replyId).live('click', function() {
            showReComment(contentId, contentUno, rootContent, contentScreenName, hrefUser, replyId, replyUno, replyScreenName, val, $(this));
        })

    }
    var showReComment = function(contentId, contentUno, rootContent, contentScreenName, hrefUser, replyId, replyUno, replyScreenName, val, rDom) {
        if ($('#reply_comment_' + replyId).length == 0) {
            var showReHtml = '<div style="display:none" id="reply_comment_' + replyId + '">' +
                    '<div class="commentbox clearfix">' +
                    '<div class="commenttextDiv"><textarea class="commenttext" style="font-family:Tahoma, \'宋体\'; heigth:20px" id="Rereply_textarea_' + replyId + '">回复@' + replyScreenName + ':</textarea></div>' +
                    '<a name="subreplybtn" class="submitbtn" id="subreply_' + contentId + '"><span>评论</span></a></div>' +
                    '<div class="related clearfix">' +
                    '<div class="commenface"><a href="javascript:void(0)" title="表情" id="replyContmood_Re_' + replyId + '"></a></div>' +
                    '</div>' +
                    '<p><input type="checkbox" name="forwardRoot" class="checktext"><label for="checkbox">同时转发到我的博客</label></p>'
            if (val.content.publishType.code == 'fwd') {
                showReHtml += '<p><input type="checkbox" name="replayRoot" class="checktext"><label for="checkbox01">同时评论给原文作者</label>' +
                        '<a class="author" href="javascript:void(0)" name="atLink" title="' + contentScreenName + '">' + contentScreenName + '</a></p>';
            }
            showReHtml += '</div>';
            rDom.parent().parent().after(showReHtml);
            replyCenterfun(replyId, 'Rereply_textarea_')
            replylivesubfun(contentId, contentUno, replyId, replyUno, replyScreenName);
            replylivemood("Re_" + replyId, "Rereply_textarea_" + replyId);
            setTimeout(function() {
                focusreplyhtml(replyId, 'Rereply_textarea_');
                $("#Rereply_textarea_" + replyId).AutoHeight({maxHeight:200})
            }, 200)
        }
//        $('#reply_comment_'+replyId).slideToggle();
        toggolreplyhtml(replyId, 'reply_comment_', rDom, 'Rereply_textarea_');

    }
    var removeReply = function(data, replyId, contentId) {
        if (data.status_code == '1') {
            $("#cont_cmt_list_" + replyId).slideToggle(function() {
                $("#cont_cmt_list_" + replyId).remove();
            });
            common.increaseCount('feedbacknum_' + contentId, -1)
        } else {
            var alertOption = {text:'删除失败',tipLayer:true,textClass:"tipstext"};
            joymealert.alert(alertOption);
        }
    }
    //显示层方法
    var showreplyhtml = function(id, Prefix, dom) {
        var isIE7 = window.navigator.userAgent.indexOf('MSIE 7.0') > 0;
        if (isIE7) {
            $("#" + Prefix + id).fadeToggle(function() {
                showReplyLayerFocus(id, 'replyCont_', dom)
            });
        } else {
            $("#" + Prefix + id).slideToggle(function() {
                showReplyLayerFocus(id, 'replyCont_', dom)
            });
        }
    }
    var toggolreplyhtml = function(id, Prefix, rDom, Prefix1) {
        var isIE7 = window.navigator.userAgent.indexOf('MSIE 7.0') > 0;
        if (isIE7) {
            $("#" + Prefix + id).fadeToggle('fast', function() {
                showReplyLayerFocus(id, Prefix1, rDom)
            });
        } else {
            $("#" + Prefix + id).slideToggle('fast', function() {
                showReplyLayerFocus(id, Prefix1, rDom)
            });
        }
    }

    //注册事件
    var replylivesubfun = function(id, uno, rid, replyUno, screenName) {
        $("#subreply_" + id).die().live('click', function() {
            ajaxSaveReply(id, uno, rid, replyUno, screenName, $(this));
        })
    }
    //参数一,基本ID,参数二,textareaID前缀,参数三:按钮ID前缀
    var replyCenterfun = function(id, CtrlTextId) {
        $("#" + CtrlTextId + id).die().live('keydown', function(event) {
            common.joymeCtrlEnter(event, $(this).next('a'));
        });
    }
    //点击提交事件
    var ajaxSaveReply = function(cid, cuno, pid, replyUno, screenName, replyDom) {
        var content = replyDom.prev('div').find('textarea').val();
        var trimContent = content.replace(/\s+/g, " ");
        //同时转帖原文
        var forwardRoot = false;
        //发送原作者
        var replayRoot = false;
        //判断是否为选中状态
        replyDom.parent().parent().children('p').each(function(i, val) {
            var checkbox = $(this).find('input');
            if (checkbox.attr("checked")) {
                if (checkbox.attr('name') == "forwardRoot") {
                    forwardRoot = true;
                }
                if (checkbox.attr('name') == "replayRoot") {
                    replayRoot = true;
                }
            }
        })
        //验证文本
        if (checkContext(trimContent)) {
            var paramObj = {cid:cid,
                cuno:cuno,
                pid:pid,
                puno:replyUno,
                replycontent:trimContent,
                forwardroot:forwardRoot,
                screenName:screenName,
                replyroot:replayRoot};
            //验证方法
            if (!checkContextAt(paramObj, subreply, replyDom)) {
                replylivesubfun(paramObj.cid, paramObj.cuno);
            }
        } else {
            replylivesubfun(cid, cuno);
        }

    }
    //提交reply
    var subreply = function(paramObj, replyDom) {
        replyBiz.commonreply(paramObj, loadreplyCallback, subreplyCallback, replyDom);
    }

    //插入需要显示的评论内容
    var loadreplyCallback = function(replyDom) {
        replyDom.attr('class', 'loadbtn').html('<span><em class="loadings"></em>发布中…</span>');
    }

    var loadreplyOnBlogCallback = function(replyDom) {
        replyDom.attr('class', 'loadbtn fr').html('<span><em class="loadings"></em>发布中…</span>');
    }

    //插入需要显示的评论内容
    var subreplyCallback = function(jsonObj, paramObj, replyDom) {
        replyDom.attr('class', 'submitbtn').html('<span>评论</span>');

        if (jsonObj.status_code == '1') {
            var reReplyAlertText = '';
            if (jsonObj.billingStatus) {
                reReplyAlertText = "评论成功，" + jsonObj.billingMsg;
            } else {
                reReplyAlertText = "评论成功";
            }
            var alertOption = {text:reReplyAlertText,tipLayer:true,callbackFunction:moodBiz.hideFace};
            joymealert.alert(alertOption);

            //增加评论数
            common.increaseCount('feedbacknum_' + paramObj.cid, 1);
            //同时回复原作者
            if (paramObj.replyroot) {
                common.increaseCount('feedbacknum_' + jsonObj.result[0].content.rootContentId, 1);
            }

            if (paramObj.forwardroot) {
                if (jsonObj.result[0].content.rootContentId != null && jsonObj.result[0].content.rootContentId.length > 0) {
                    common.increaseCount('forward_root_num_' + jsonObj.result[0].content.rootContentId, 1);
                    common.increaseCount('forward_num_' + jsonObj.result[0].content.contentId, 1);
                } else {
                    common.increaseCount('forward_num_' + jsonObj.result[0].content.contentId, 1);
                }

            }

            //拼装返回的html
            var subreplyhtml = assemparenthtml(jsonObj.result[0]);

            $('#replypostarea_' + paramObj.cid).after(subreplyhtml);

            var screenText = "";
            if (paramObj.screenName != undefined) {
                screenText = "回复@" + paramObj.screenName + ":";
            }
            replyDom.prev().find('textarea').val(screenText).css('height', '24px').css('font-size', '12px');
            $("#reply_comment_" + paramObj.pid).slideUp();
        } else if (jsonObj.status_code == '-4') {
            var alertOption = {text:jsonObj.msg,tipLayer:true,textClass:"tipstext"};
            joymealert.alert(alertOption);
        } else if (jsonObj.status_code == '-5') {
            var alertOption = {text:tipsText.profile.user_ipforbidden_forbidlogin,
                tipLayer:true,
                textClass:"tipstext",
                callbackFunction:function() {
                    if (jsonObj.result != null && jsonObj.result.length > 0) {
                        window.location.href = jsonObj.result[0];
                    }
                }};
            joymealert.alert(alertOption);
        } else if (jsonObj.status_code == '-1') {
            login.maskLoginByJsonObj(jsonObj);
        } else {
            var alertOption = {text:"回复失败",tipLayer:true,textClass:"tipstext"};
            joymealert.alert(alertOption);
        }

        //成功后重新绑定提交事件
//        replylivesubfun(paramObj.cid, paramObj.cuno);

    }

    //验证文本
    var checkContext = function(replycontent) {
        var alertOption = {};
        if (replycontent == null || $.trim(replycontent).length == 0) {
            alertOption = {text:tipsText.comment.blog_pl_content_notnull,tipLayer:false,textClass:"tipstext"};
            joymealert.alert(alertOption);
            return false;
        } else if (common.getInputLength(replycontent) > tipsText.comment.blog_reply_length) {
            alertOption = {text:tipsText.comment.blog_pl_content_maxlength,tipLayer:false,textClass:"tipstext"};
            joymealert.alert(alertOption);
            return false;
        } else if (! ajaxverify.verifyPost(replycontent)) {
            alertOption = {text:tipsText.comment.blog_pl_illegl,tipLayer:false,textClass:"tipstext"};
            joymealert.alert(alertOption);
            return false;
        }
        return true;
    }

    //验证at,同时验证字数
    var checkContextAt = function (paramObj, callback, replyDom, focusDom) {
        var alertOption = {};
        if (paramObj.replycontent == null || $.trim(paramObj.replycontent).length == 0) {
            alertOption = {text:tipsText.comment.blog_pl_content_notnull,tipLayer:true,textClass:"tipstext"};
            joymealert.alert(alertOption);
            return;
        } else if (common.getInputLength(paramObj.replycontent) > tipsText.comment.blog_reply_length) {
            alertOption = {text:tipsText.comment.blog_pl_content_maxlength,tipLayer:true,textClass:"tipstext"};
            joymealert.alert(alertOption);
            return;
        } else if (! ajaxverify.verifyPost(paramObj.replycontent)) {
            alertOption = {text:tipsText.comment.blog_pl_illegl,tipLayer:true,textClass:"tipstext"};
            joymealert.alert(alertOption);
            return;
        }
        replyBiz.checkAtSize(paramObj, checkAtNicks, callback, replyDom, showAtSizeTips, focusDom);
    }
    //验证at昵称
    var checkAtNicks = function(paramObj, callback, replyDom, focusDom) {
        replyBiz.checkAtNicks(paramObj, callback, replyDom, showAtNicksTips, focusDom);
    }
    var focusreplyhtml = function(id, prefix) {
        var obj = document.getElementById(prefix + id);
        if (obj == null) {
            return;
        }
        obj.focus();
        var len = obj.value.length;
        if (document.selection) {
            var sel = obj.createTextRange();
            sel.moveStart('character', len);
            sel.collapse();
            sel.select();
        } else if (typeof  obj.selectionStart == 'number' && typeof  obj.selectionEnd == 'number') {
            obj.selectionStart = obj.selectionEnd = len;
        }
    }

    //收到的评论-->回复 初始html
    var loadReReplyLayerOnReceiveList = function(rDom) {
        var cid = rDom.attr("data-rid");
        var showReHtml = '<dd class="mycomment_c" style="display: none">' +
                '<div class="discusson">' +
                '<span class="corner"></span>' +
                '<div class="commentbox clearfix">' +
                '<textarea class="commenttext" style="heigth:20px;font-size: 12px; font-family:Tahoma, \'宋体\';" id="reply_textarea_' + rDom.attr("data-rid") + '">回复@' + rDom.attr("data-nick") + ':</textarea>' +
                '<a class="submitbtn" id="subreply_' + rDom.attr("data-rid") + '" data-cid="' + rDom.attr("data-cid") + '" data-cuno="' + rDom.attr("data-cuno") + '" data-rid="' + rDom.attr("data-rid") + '" data-runo="' + rDom.attr("data-runo") + '" data-pname="' + rDom.attr("data-nick") + '"><span>回复</span></a>' +
                '</div>' +
                '<div class="related clearfix">' +
                    '<div class="transmit_pic clearfix" id="rereply_image_' + rDom.attr("data-rid") + '">' +
                        '<a class="commenface" id="reply_mood_' + rDom.attr("data-rid") + '" href="javascript:void(0)" title="表情"></a> ' +
                        '<div class="t_pic" name="reply_image_icon">' +
                            '<a class="t_pic1" href="javascript:void(0)">图片</a>' +
                        '</div>' +
                        '<div style="display:none;" name="reply_image_icon_more" class="t_pic_more">' +
                            '<a title="图片" class="t_pic1" href="javascript:void(0)" name="reply_upload_img" data-cid="rereply_image_' + rDom.attr("data-rid") + '">图片</a>' +
                            '<a title="链接" class="t_more" href="javascript:void(0)" name="reply_upload_img_link" data-cid="rereply_image_' + rDom.attr("data-rid") + '">链接</a>' +
                        '</div>' +
                    '</div>'+
                '</div>' +
                '</div>' +
                '</dd>';
        rDom.parent().parent().append(showReHtml);
//        $("#reply_textarea_" + cid).parent().css("background", "#fff");
        var replyPostDom = rDom.parent().parent().parent().find(".mycomment_c");
        slideToggleFocus(replyPostDom, replyPostDom.children().children().children());
        var textareaDom = $("#reply_textarea_" + cid);
        var len = textareaDom.val().length;
        setTimeout(function() {
            if (len != 0) {
                var te = new TextareaEditor(document.getElementById("reply_textarea_" + cid));
                te.setSelectionRange(len, len);
                te = null;
            }
        }, 400);
        replyCenterfun(rDom.attr('data-rid'), 'reply_textarea_');
        reReplyLiveSubFun($('#subreply_' + rDom.attr("data-rid")));
        bindReplyMood(rDom.attr("data-rid"));
        //为评论框绑定自适应高度方法
        $('#reply_textarea_' + rDom.attr("data-rid")).AutoHeight({maxHeight:200});
//        showReplyLayerFocus(rDom.attr("data-rid"), 'reply_textarea_', rDom)
        window.autoAt({id:"reply_textarea_" + rDom.attr("data-rid"),ohterStyle:'line-height:22px'});
    }

    //表情事件
    var bindReplyMood = function(replyId) {
        var te = new TextareaEditor(document.getElementById('reply_textarea_' + replyId));
        $('#reply_mood_' + replyId).live('click', function() {
            var config = {
                allowmultiple:true,
                isremovepop:true,
                isfocus:true
            };
            moodBiz.docFaceBindOnReply($(this), 'reply_textarea_' + replyId, config, te);
        });
    }

    //注册回复事件
    var reReplyLiveSubFun = function(jqObj) {
        $("#subreply_" + jqObj.attr("data-rid")).die().live('click', function() {
            ajaxSaveReReply($(this));
        })
    }

    //点击回复事件
    var ajaxSaveReReply = function(jqObj) {
        var content = jqObj.prev().val();
        var trimContent = content.replace(/\s+/g, " ");
        //验证方法

        if($.trim(trimContent).length == 0){
            if(jqObj.attr("data-rid") != null && jqObj.attr("data-rid") !=''){
                //rereply_image_
                if($("#rereply_image_"+jqObj.attr("data-rid")).find("input[type=hidden]").length>0){
                    trimContent = tipsText.comment.image_reply;
                }
            } else {
                //reply_image_
                if($("#reply_image_"+jqObj.attr("data-cid")).find("input[type=hidden]").length>0){
                    trimContent = tipsText.comment.image_reply;
                }
            }

        }

        if (checkContext(trimContent)) {
            var paramObj = {cid:jqObj.attr("data-cid"),
                cuno:jqObj.attr("data-cuno"),
                pid:jqObj.attr("data-rid"),
                puno:jqObj.attr("data-runo"),
                replycontent:trimContent,
                forwardroot:false,
                screenName:jqObj.attr("data-pname"),
                replyroot:false};
            paramObj = parseReplyParams(paramObj);
            if (!checkContextAt(paramObj, subReReply, jqObj)) {
                reReplyLiveSubFun(jqObj);
            }
        } else {
            reReplyLiveSubFun(jqObj);
        }

    }

    //提交reReply
    var subReReply = function(paramObj, replyDom) {
        replyBiz.commonreply(paramObj, loadreplyCallback, subReReplyCallback, replyDom);
    }

    //reReply回调方法
    var subReReplyCallback = function(jsonObj, paramObj, replyDom) {
        replyDom.attr('class', 'submitbtn').html('<span>评论</span>');
        replyDom.prev().find('textarea').val("回复@" + replyDom.attr('data-pname') + ":").css('height', '24px').css('font-size', '12px');
        login.maskLoginByJsonObj(jsonObj);
        if (jsonObj.status_code == '1') {
            var reReplyAlertText = '';
            if (jsonObj.billingStatus) {
                reReplyAlertText = "回复成功" + jsonObj.billingMsg;
            } else {
                reReplyAlertText = "回复成功";
            }
            var alertOption = {text:reReplyAlertText,tipLayer:true,callbackFunction:moodBiz.hideFace};
            joymealert.alert(alertOption);

            //增加评论数
            common.increaseCount('feedbacknum_' + paramObj.cid, 1)
            replyDom.parent().parent().parent().slideToggle('fast');
            replyDom.prev().val("回复@" + paramObj.screenName + "：");
        } else {
            var alertOption = {text:"回复失败",tipLayer:true,textClass:"tipstext"};
            joymealert.alert(alertOption);
        }
        //成功后重新绑定提交事件
        reReplyLiveSubFun(replyDom);
    }

    //超过@次数提示层
    var showAtSizeTips = function (paramObj, callback1, replyDom, resMsg, focusDom) {
        var offSet = replyDom.parent().offset();
        var tips = '您在评论中使用@的次数超过15次超出的部分不能传达给对方信息。'

        var submitFunction = function() {
            replyBiz.checkAtNicks(paramObj, callback1, replyDom, showAtNicksTips);
        }

        var confirmOption = {
            confirmid:"atSizeTips",
            offset:"Custom",
            offsetlocation:[offSet.top,offSet.left + Math.floor((replyDom.parent().width() - 229) / 2)],
            text:tips,
            width:229,
            submitButtonText:'继续发送',
            submitFunction:submitFunction,
            cancelButtonText:'返回修改',
            cancelFunction:null};
        joymealert.confirm(confirmOption);
        focusDom.focus();
    }

    var showAtNicksTips = function (paramObj, replyDom, resMsg, transfercallback, focusDom) {
        var list = resMsg.result;
        var str = '';
        $.each(list, function(i, val) {
            str = str + '“' + val + '”、';
        });

        var offSet = replyDom.parent().offset();
        var tips = '您在评论@到的用户' + str.substring(0, str.length - 1) + '，不存在。';

        var confirmOption = {
            confirmid:"atNicksTips",
            offset:"Custom",
            offsetlocation:[offSet.top,offSet.left + Math.floor((replyDom.parent().width() - 229) / 2)],
            title:"确认继续发送",
            text:tips,
            width:229,
            submitButtonText:'继续发送',
            submitFunction:function() {
                transfercallback(paramObj, replyDom);
            },
            cancelButtonText:'返回修改',
            cancelFunction:function() {
                focusDom.focus();
            }};
        joymealert.confirm(confirmOption);

    }

    var publishReplyBlogCallback = function(resultMsg, param, replyDom) {
        replyDom.attr('class', 'submitbtn fr').html('<span>评论</span>');
        if (resultMsg.status_code != '1') {
            var alertOption = {text:resultMsg.msg,tipLayer:false,textClass:"tipstext"};
            joymealert.alert(alertOption);
            return false;
        }
        $("#rereply_area_" + param.pid).slideUp();
        $("#rereply_content_" + param.pid).val("回复@" + param.screenName + "：");
        var insertBlogReplyList = function() {
            var replyInfo = resultMsg.result[0];
            var id = 'cont_reply_' + replyInfo.reply.interactionId;

            var reReplyHtml = '<a href="javascript:void(0)"  name="reReplyLink"  data-pid="' + replyInfo.reply.interactionId + '" data-puno="' + replyInfo.reply.interactionUno + '" data-pname="' + replyInfo.interactionProfile.blog.screenName + '" data-cid="' + replyInfo.reply.contentId + '" data-cuno="' + replyInfo.reply.contentUno + '">回复</a>';

            var iconSet = replyInfo.interactionProfile.blog.headIconSet.iconSet;

            var headIcon;
            for (var i = 0; i < iconSet.length; i++) {
                if (iconSet[i].validStatus) {
                    headIcon = iconSet[i].headIcon;
                    break;
                }
            }

            var html = '<div class="area blogopinion" id="' + id + '" style="display:none">' +
                    '<dl>' +
                    '<dt class="personface">' +
                    '<a class="tag_cl_left" href="http://' + replyInfo.interactionProfile.blog.domain + '.' + joyconfig.DOMAIN + '" name="atLink" title="' + replyInfo.interactionProfile.blog.screenName + '">' +
                    '<img width="58" height="58" src="' + common.parseSimg(headIcon, joyconfig.DOMAIN) + '"></a>' +
                    '</dt>' +
                    '<dd class="textcon">' +
                    '<a class="author" href="http://' + replyInfo.interactionProfile.blog.domain + '.' + joyconfig.DOMAIN + '" name="atLink" title="' + replyInfo.interactionProfile.blog.screenName + '">' +
                    replyInfo.interactionProfile.blog.screenName +
                    '</a>' +
                    '<p> ' + replyInfo.reply.interactionContent + ' </p>' +
                    '<div class="commontft clearfix">今天 ' + replyInfo.reply.createDate +
                    '<span class="delete">' +
                    '<a href="javascript:void(0);" id="del_reply_' + replyInfo.reply.interactionId + '" data-cid="' + replyInfo.reply.contentId + '" data-cuno="' + replyInfo.reply.contentUno + '">删除</a>|' +
                    reReplyHtml +
                    '</span>' +
                    '</div>' +
                    '</dd>' +
                    '</dl>' +
                    '</div>';
            $('#post_reply').after(html);
            var isIE7 = window.navigator.userAgent.indexOf('MSIE 7.0') > 0;
            if (isIE7) {
                $('#' + id).fadeIn();
            } else {
                $('#' + id).slideDown();
            }

            $('#reply_content').val('');
            common.increaseCount('replynum_' + replyInfo.content.contentId, 1);
            if (param.replyroot) {
                common.increaseCount('replynum_' + param.pid, 1);
            }
            if (param.forwardroot) {
                common.increaseCount('forward_num_' + param.cid, 1);
                if (param.pid != null && param.pid.length > 0) {
                    common.increaseCount('forward_root_num_' + param.pid, 1);
                }
            }
        }
        var alertText = '';
        if (resultMsg.billingMsg != undefined) {
            alertText = '评论成功,' + resultMsg.billingMsg + resultMsg.msg;
        } else {
            alertText = '评论成功' + resultMsg.msg;
        }
        var alertOption = {text:alertText,tipLayer:true,callbackFunction:insertBlogReplyList,forclosed:false};
        joymealert.alert(alertOption);
    }

    var loadBLogReReplyLayer = function(dom, pid, puno, cid, cuno, pscreenName) {
        var rcid = $('#hidden_rcid').val();
        var rdomain = $('#hidden_rdomain').val();
        var rname = $('#hidden_rname').val();

        var isForward = rcid != null && rcid.length > 0;
        var forwardCheckHtml = '';
        if (isForward) {
            forwardCheckHtml = '<p><input id="rereply_replyroot_' + pid + '" class="checktext" type="checkbox"  ><label for="checkbox">同时评论给原文作者' +
                    '<a href="http://' + rdomain + '.' + joyconfig.DOMAIN + '" name="atLink" title="' + rname + '">' + rname + '</a>' +
                    '</label></p>';
        }

        var html = '<div class="blog_discusson" id="rereply_area_' + pid + '" style="display:none">' +
                '<div class="commentbox clearfix">' +
                '<div class="commenttextDiv"><textarea class="commenttext" style="font-family:Tahoma, \'宋体\';" id="rereply_content_' + pid + '" type="text">回复@' + pscreenName + '：</textarea></div>' +
                '<a class="submitbtn" id="submit_rereply_' + pid + '"><span>回复</span></a>' +
                '</div>' +
                '<div class="related clearfix">' +
                '<div class="commenface"><a href="javascript:void(0);" title="表情" id="replyContmood_re_' + pid + '"></a></div>' +
                '</div>' +
                '<p><input id="rereply_forwardroot_' + pid + '" class="checktext" type="checkbox"><label for="checkbox">同时转发到我的博客</label></p>' +
                forwardCheckHtml +
                '</div>';
        dom.parent().parent().after(html);
        replylivemood("re_" + pid, "rereply_content_" + pid);
//        $("#rereply_content_" + pid).parent().css("background", "#fff");
        $('#submit_rereply_' + pid).die().live('click', function() {

            var replyContent = $('#rereply_content_' + pid).val();
            var forwardroot = $('#rereply_forwardroot_' + pid).attr('checked');

            var replyroot = false;
            if ($('#rereply_replyroot_' + pid).size()) {
                replyroot = $('#rereply_replyroot_' + pid).attr('checked');
            }

            var alertText = ''
            if (replyContent == null || $.trim(replyContent).length == 0) {
                alertText = tipsText.comment.blog_pl_content_notnull;
            } else if (common.getInputLength(replyContent) > tipsText.comment.blog_reply_length) {
                alertText = tipsText.comment.blog_pl_content_maxlength;
            } else if (!ajaxverify.verifyPost(replyContent)) {
                alertText = tipsText.userSet.user_word_illegl;
            }

            if (alertText.length > 0) {
                joymealert.alert({text:alertText,tipLayer:true,textClass:"tipstext"});
                return false;
            }

            var paramObj = {cid:cid,
                cuno:cuno,
                replycontent:replyContent,
                forwardroot:forwardroot,
                replyroot:replyroot,pid:pid,puno:puno,
                screenName:pscreenName};

            checkContextAt(paramObj, function(paramObj, replyDom) {
                replyBiz.commonreply(paramObj, loadreplyCallback, publishReplyBlogCallback, replyDom);
            }, $(this));

        });

        $("#rereply_content_" + pid).die().live("keydown keyup", function(event) {
            common.joymeCtrlEnter(event, $('#submit_rereply_' + pid));
        })
        $('#rereply_content_' + pid).AutoHeight({maxHeight:200,minHeight:24}).css('font-size', '12px');
    }

    var delReplyBlogCallback = function(resultMsg, replyid, cid) {
        if (resultMsg.status_code == 0) {
            var alertOption = {text:resultMsg.msg,tipLayer:false,textClass:"tipstext"};
            joymealert.alert(alertOption);
            return false;
        }

        $('#cont_reply_' + replyid).slideUp(function() {
            $(this).remove();
            common.increaseCount('replynum_' + cid, -1);
        })
    }

    //随便看看页面评论提交事件
    var ajaxSaveReplyOnWall = function(cid, cuno, pid, replyUno, screenName, replyDom) {
        var content = $("#reply_content").val();
        var focusDom = $("#reply_content");
        var trimContent = content.replace(/\s+/g, " ");
        //同时转帖原文
        var forwardRoot = $('#check_forwardroot').attr('checked');

        //发送原作者
        var replayRoot = false;

        if (checkContext(trimContent)) {
            var paramObj = {cid:cid,
                cuno:cuno,
                pid:pid,
                puno:replyUno,
                replycontent:trimContent,
                forwardroot:forwardRoot,
                screenName:screenName,
                replyroot:replayRoot};

            //验证方法
            if (!checkContextAt(paramObj, subreplyOnWall, replyDom, focusDom)) {
                joymealert.discoveryAlertConfirm();
                replylivesubfunonwall();
            }
        } else {
            var styleStr = $("#joymealert_").attr('style');
            $("#joymealert_").attr('style', styleStr.replace('absolute', 'fixed'));
            replylivesubfunonwall();
        }
    }

    var showReplyOnWall = function() {
//        common.checkInputLength(140, 'reply_content', 'reply_num');
//        if ($("#post_reply").is(':visible')) {
//            $("#post_reply").slideUp();
//        } else {
//            $("#post_reply").slideDown(function() {
//                common.cursorPosition._setCurPos($("#reply_content").val().length, $("#reply_content")[0]);
//                replylivesubfunonwall();
//            });
//        }
        common.cursorPosition._setCurPos($("#reply_content").val().length, $("#reply_content")[0]);
        replylivesubfunonwall();
    }

    var showReReplyOnWall = function(jqDom) {
        var pid = jqDom.attr('data-pid');
        var replyArea = $('#rereply_area_' + pid);
        if (replyArea.size() == 0) {
            loadReReplyLayerOnWall(jqDom);
            replyArea = $('#rereply_area_' + pid);
        }

        if (replyArea.is(':hidden')) {
            $('.blog_discusson').slideUp();
            replyArea.slideDown();
            common.cursorPosition._setCurPos($('#rereply_content_' + pid).val().length, $('#rereply_content_' + pid)[0]);
        } else {
            replyArea.slideUp();
        }
    }

    //随便看看页面提交reply
    var subreplyOnWall = function(paramObj, replyDom) {
        replyBiz.commonreply(paramObj, loadreplyOnBlogCallback, publishReplyWallCallback, replyDom);

    }


    var replylivesubfunonwall = function() {
        $("#reply_submit").die().live('click', function() {
            ajaxSaveReplyOnWall($("#hidden_cid").val(), $("#hidden_cuno").val(), '', '', '', $(this));
        });
    }

    var publishReplyWallCallback = function(resultMsg, param, replyDom) {
        replyDom.attr('class', 'submitbtn fr').html('<span>评论</span>');

        if (resultMsg.status_code != '1') {
            var alertOption = {text:resultMsg.msg,tipLayer:false,textClass:"tipstext"};
            joymealert.alert(alertOption);
            return false;
        }
        $("#rereply_area_" + param.pid).slideUp();
        $("#rereply_content_" + param.pid).val("回复@" + param.screenName + "：");
        var insertBlogReplyList = function() {
            var replyInfo = resultMsg.result[0];
            var id = 'cont_reply_' + replyInfo.reply.interactionId;

            var reReplyHtml = '<a href="javascript:void(0)"  name="reReplyLink"  data-pid="' + replyInfo.reply.interactionId + '" data-puno="' + replyInfo.reply.interactionUno + '" data-pname="' + replyInfo.interactionProfile.blog.screenName + '" data-cid="' + replyInfo.reply.contentId + '" data-cuno="' + replyInfo.reply.contentUno + '">回复</a>';

            var iconSet = replyInfo.interactionProfile.blog.headIconSet.iconSet;

            var headIcon;
            for (var i = 0; i < iconSet.length; i++) {
                if (iconSet[i].validStatus) {
                    headIcon = iconSet[i].headIcon;
                    break;
                }
            }

            var html = '<div class="area blogopinion" id="' + id + '" style="display:none">' +
                    '<dl>' +
                    '<dt class="personface">' +
                    '<a class="tag_cl_left" href="http://' + replyInfo.interactionProfile.blog.domain + '.' + joyconfig.DOMAIN + '" target="_blank" title="' + replyInfo.interactionProfile.blog.screenName + '">' +
                    '<img width="58" height="58" src="' + common.parseSimg(headIcon, joyconfig.DOMAIN) + '"></a>' +
                    '</dt>' +
                    '<dd class="textcon">' +
                    '<a class="author" href="http://' + replyInfo.interactionProfile.blog.domain + '.' + joyconfig.DOMAIN + '" target="_blank" title="' + replyInfo.interactionProfile.blog.screenName + '">' +
                    replyInfo.interactionProfile.blog.screenName +
                    '</a>' +
                    '<p> ' + replyInfo.reply.interactionContent + ' </p>' +
                    '<div class="commontft clearfix">今天 ' + replyInfo.reply.createDate +
                    '<span class="delete">' +
                    '<a href="javascript:void(0);" id="del_reply_' + replyInfo.reply.interactionId + '" data-cid="' + replyInfo.reply.contentId + '" data-cuno="' + replyInfo.reply.contentUno + '">删除</a>|' +
                    reReplyHtml +
                    '</span>' +
                    '</div>' +
                    '</dd>' +
                    '</dl>' +
                    '</div>';
            $('#post_reply').after(html);
            var isIE7 = window.navigator.userAgent.indexOf('MSIE 7.0') > 0;
            if (isIE7) {
                $('#' + id).fadeIn();
            } else {
                $('#' + id).slideDown();
            }

            $('#reply_content').val('');
            common.increaseCount('replynum_' + replyInfo.content.contentId, 1);
            if (param.replyroot) {
                common.increaseCount('replynum_' + param.pid, 1);
            }
            if (param.forwardroot) {
                common.increaseCount('forward_num_' + param.cid, 1);
            }
        }
        var alertText = '';
        if (resultMsg.billingMsg != undefined) {
            alertText = '评论成功,' + resultMsg.billingMsg + resultMsg.msg;
        } else {
            alertText = '评论成功' + resultMsg.msg;
        }
        var alertOption = {text:alertText,tipLayer:false,callbackFunction:insertBlogReplyList,forclosed:false};
        joymealert.alert(alertOption);
        var styleStr = $("#joymealert_").attr('style');
        $("#joymealert_").attr('style', styleStr.replace('absolute', 'fixed'));
        if ($("#post_reply").is(':visible')) {
            $("#post_reply").slideUp();
        }
    }

    var publishReReplyWallCallback = function(resultMsg, param, replyDom) {
        var pid = replyDom.attr("data-pid");
        $("#submit_rereply_" + pid).attr('class', 'submitbtn fr').html('<span>评论</span>');

        if (resultMsg.status_code != '1') {
            var alertOption = {text:resultMsg.msg,tipLayer:false,textClass:"tipstext"};
            joymealert.alert(alertOption);
            return false;
        }
        $("#rereply_area_" + param.pid).slideUp();
        $("#rereply_content_" + param.pid).val("回复@" + param.screenName + "：");
        var insertBlogReplyList = function() {
            var replyInfo = resultMsg.result[0];
            var id = 'cont_reply_' + replyInfo.reply.interactionId;

            var reReplyHtml = '<a href="javascript:void(0)"  name="reReplyLink"  data-pid="' + replyInfo.reply.interactionId + '" data-puno="' + replyInfo.reply.interactionUno + '" data-pname="' + replyInfo.interactionProfile.blog.screenName + '" data-cid="' + replyInfo.reply.contentId + '" data-cuno="' + replyInfo.reply.contentUno + '">回复</a>';

            var iconSet = replyInfo.interactionProfile.blog.headIconSet.iconSet;

            var headIcon;
            for (var i = 0; i < iconSet.length; i++) {
                if (iconSet[i].validStatus) {
                    headIcon = iconSet[i].headIcon;
                    break;
                }
            }

            var html = '<div class="area blogopinion" id="' + id + '" style="display:none">' +
                    '<dl>' +
                    '<dt class="personface">' +
                    '<a class="tag_cl_left" href="http://' + replyInfo.interactionProfile.blog.domain + '.' + joyconfig.DOMAIN + '" target="_blank" title="' + replyInfo.interactionProfile.blog.screenName + '">' +
                    '<img width="58" height="58" src="' + common.parseSimg(headIcon, joyconfig.DOMAIN) + '"></a>' +
                    '</dt>' +
                    '<dd class="textcon">' +
                    '<a class="author" href="http://' + replyInfo.interactionProfile.blog.domain + '.' + joyconfig.DOMAIN + '" target="_blank" title="' + replyInfo.interactionProfile.blog.screenName + '">' +
                    replyInfo.interactionProfile.blog.screenName +
                    '</a>' +
                    '<p> ' + replyInfo.reply.interactionContent + ' </p>' +
                    '<div class="commontft clearfix">今天 ' + replyInfo.reply.createDate +
                    '<span class="delete">' +
                    '<a href="javascript:void(0);" id="del_reply_' + replyInfo.reply.interactionId + '" data-cid="' + replyInfo.reply.contentId + '" data-cuno="' + replyInfo.reply.contentUno + '">删除</a>|' +
                    reReplyHtml +
                    '</span>' +
                    '</div>' +
                    '</dd>' +
                    '</dl>' +
                    '</div>';
            $('#post_reply').after(html);
            var isIE7 = window.navigator.userAgent.indexOf('MSIE 7.0') > 0;
            if (isIE7) {
                $('#' + id).fadeIn();
            } else {
                $('#' + id).slideDown();
            }

            $('#reply_content').val('');
            common.increaseCount('replynum_' + replyInfo.content.contentId, 1);
            if (param.replyroot) {
                common.increaseCount('replynum_' + param.pid, 1);
            }
            if (param.forwardroot) {
                common.increaseCount('forward_num_' + param.cid, 1);
            }
        }
        var alertText = '';
        if (resultMsg.billingMsg != undefined) {
            alertText = '评论成功,' + resultMsg.billingMsg + resultMsg.msg;
        } else {
            alertText = '评论成功' + resultMsg.msg;
        }
        var alertOption = {text:alertText,tipLayer:false,callbackFunction:insertBlogReplyList,forclosed:false};
        joymealert.alert(alertOption);
        var styleStr = $("#joymealert_").attr('style');
        $("#joymealert_").attr('style', styleStr.replace('absolute', 'fixed'));

        if ($("#post_reply").is(':visible')) {
            $("#post_reply").slideUp();
        }
    }


    var loadReReplyLayerOnWall = function(dom, pid) {
        var rcid = $('#hidden_rcid').val();
        var rdomain = $('#hidden_rdomain').val();
        var rname = $('#hidden_rname').val();
        var pid = dom.attr('data-pid');
        var puno = dom.attr('data-puno');
        var pscreenName = dom.attr('data-pname');
        var cid = dom.attr('data-cid');
        var cuno = dom.attr('data-cuno');

        var isForward = rcid != null && rcid.length > 0;
        var forwardCheckHtml = '';
        if (isForward) {
            forwardCheckHtml = '<p><input id="rereply_replyroot_' + pid + '" class="checktext" type="checkbox"  ><label for="checkbox">同时评论给原文作者' +
                    '<a href="http://' + rdomain + '.' + joyconfig.DOMAIN + '" name="atLink" title="' + rname + '">' + rname + '</a>' +
                    '</label></p>';
        }

        var html = '<div class="blog_discusson" id="rereply_area_' + pid + '" style="display:none">' +
                '<div class="commentbox clearfix">' +
                '<div class="commenttextDiv"><textarea class="commenttext" style="font-family:Tahoma, \'宋体\';" id="rereply_content_' + pid + '" type="text">回复@' + pscreenName + '：</textarea></div>' +
                '<a class="submitbtn" id="submit_rereply_' + pid + '"><span>回复</span></a>' +
                '</div>' +
                '<div class="related clearfix">' +
                '<div class="commenface"><a href="javascript:void(0);" title="表情" id="replyContmood_re_' + pid + '"></a></div>' +
                '</div>' +
                '<p><input id="rereply_forwardroot_' + pid + '" class="checktext" type="checkbox"><label for="checkbox">同时转发到我的博客</label></p>' +
                forwardCheckHtml +
                '</div>';
        dom.parent().parent().after(html);
//        $("#`rereply_content_" + pid).parent().css("background", "#fff");
        replylivemoodOnWall("re_" + pid, "rereply_content_" + pid);
        reBindReReplySubmitOnWall(dom);

        $("#rereply_content_" + pid).die().live("keydown keyup", function(event) {
            common.joymeCtrlEnter(event, $('#submit_rereply_' + pid));
        })
        $('#rereply_content_' + pid).AutoHeight({maxHeight:200,minHeight:24}).css('font-size', '12px');

    }

    var submitReReplyOnWall = function(dom) {
        var rcid = $('#hidden_rcid').val();
        var rdomain = $('#hidden_rdomain').val();
        var rname = $('#hidden_rname').val();
        var pid = dom.attr('data-pid');
        var puno = dom.attr('data-puno');
        var pscreenName = dom.attr('data-pname');
        var cid = dom.attr('data-cid');
        var cuno = dom.attr('data-cuno');

        var replyContent = $('#rereply_content_' + pid).val();
        var trimContent = replyContent.replace(/\s+/g, " ");
        var forwardroot = $('#rereply_forwardroot_' + pid).attr('checked');

        var replyroot = false;
        if ($('#rereply_replyroot_' + pid).size()) {
            replyroot = $('#rereply_replyroot_' + pid).attr('checked');
        }

        //验证文本
        if (checkContext(trimContent)) {
            var paramObj = {cid:cid,
                cuno:cuno,
                pid:pid,
                puno:puno,
                replycontent:trimContent,
                forwardroot:forwardroot,
                screenName:pscreenName,
                replyroot:forwardroot};

            //验证方法
            if (!checkContextAt(paramObj, subreReplyOnWall, dom)) {
                reBindReReplySubmitOnWall(dom);
            } else {
                reBindReReplySubmitOnWall(dom);
            }
        } else {
            reBindReReplySubmitOnWall(dom);
        }

    }

    var subreReplyOnWall = function(paramObj, replyDom) {
        replyBiz.commonreply(paramObj, loadreplyOnWallCallback, publishReReplyWallCallback, replyDom);
    }

    var reBindReReplySubmitOnWall = function(dom) {
        $('#submit_rereply_' + dom.attr('data-pid')).one('click', function() {
            submitReReplyOnWall(dom);
        });
    }

    var loadreplyOnWallCallback = function(replyDom) {
        var pid = replyDom.attr("data-pid");
        $("#submit_rereply_" + pid).attr('class', 'loadbtn fr').html('<span><em class="loadings"></em>发布中…</span>');
    }

    var replylivemoodOnWall = function(cid, textareaID) {
        var te = new TextareaEditor(document.getElementById(textareaID));
        $("#replyContmood_" + cid).live('click', function() {
            var config = {
                allowmultiple:true,
                isremovepop:true,
                isfocus:true
            };
            moodBiz.docFaceBindOnWall($(this), textareaID, config, te);
        });
    }
    //
    var showReplyLayerFocus = function(id, Prefix, dom) {
        $("html,body").animate({scrollTop:dom.offset().top - parseInt($(window).height() / 2 + 100)}, function() {
            var ele = $("#" + Prefix + id).parent('div');
            common.joymeShake(ele, 'red', 2);
            //获得焦点
            focusreplyhtml(id, Prefix);
        });

    }

    var parseReplyParams = function(paramObj) {
        if (paramObj.pid != null && paramObj.pid != '') {
            //rereply_image_
            if ($("#rereply_image_" + paramObj.pid).find("input[type=hidden]").length > 0) {
                var imageB = $("#rereply_image_" + paramObj.pid).find("input[name=picurl_b]").val();
                var imageM = $("#rereply_image_" + paramObj.pid).find("input[name=picurl_m]").val();
                var imageS = $("#rereply_image_" + paramObj.pid).find("input[name=picurl_s]").val();
                var imageSS = $("#rereply_image_" + paramObj.pid).find("input[name=picurl_ss]").val();
                var w = $("#rereply_image_" + paramObj.pid).find("input[name=w]").val();
                var h = $("#rereply_image_" + paramObj.pid).find("input[name=h]").val();
                paramObj["imgs"] = imageS;
                paramObj["imgss"] = imageSS;
                paramObj["imgb"] = imageB;
                paramObj["imgm"] = imageM;
                paramObj["w"] = w;
                paramObj["h"] = h;
            }
        } else {
            //reply_image_
            if ($("#reply_image_" + paramObj.cid).find("input[type=hidden]").length > 0) {
                var imageB = $("#reply_image_" + paramObj.cid).find("input[name=picurl_b]").val();
                var imageM = $("#reply_image_" + paramObj.cid).find("input[name=picurl_m]").val();
                var imageS = $("#reply_image_" + paramObj.cid).find("input[name=picurl_s]").val();
                var imageSS = $("#reply_image_" + paramObj.cid).find("input[name=picurl_ss]").val();
                var w = $("#reply_image_" + paramObj.cid).find("input[name=w]").val();
                var h = $("#reply_image_" + paramObj.cid).find("input[name=h]").val();
                paramObj["imgs"] = imageS;
                paramObj["imgss"] = imageSS;
                paramObj["imgb"] = imageB;
                paramObj["imgm"] = imageM;
                paramObj["w"] = w;
                paramObj["h"] = h;
            }
        }

        return paramObj;
    }

    return reply;

    function slideToggleFocus(jqdom, focusDom) {
        if (jqdom.is(':visible')) {
            jqdom.slideUp();
        } else {
            jqdom.slideDown(function() {
                $(this).children().show();
//                common.cursorPosition._setCurPos(focusDom.val().length, focusDom[0]);
            });
        }
    }
})
        ;












