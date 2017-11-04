define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var common = require('../common/common');
    var joymeAlert = require('../common/joymealert');

    var gamePrivacy = {
        bindPrivacyOnBlog:function() {
            $('#select_game_privacy').change(function() {
                var gcid = $(this).val();
                var param = {gcid:gcid,cid:cid,cuno:cuno};
                gamePrivacy.biz.getGamePrivacy(param, gamePrivacy.callback.getPrivacyListByBlog);
            });

            $('a[name=add_ess]').live('click', function() {
                var essid = $(this).attr('data-cgid');
                var param = {essid:essid,cid:cid,cuno:cuno};
                gamePrivacy.biz.addEss($(this), param, gamePrivacy.callback.addEssCallbackOnBlog);
            });

            $('a[name=add_talk]').live('click', function() {
                var talkid = $(this).attr('data-cgid');
                var param = {talkid:talkid,cid:cid,cuno:cuno};
                gamePrivacy.biz.addTalk($(this), param, gamePrivacy.callback.addTalkCallbackOnBlog);
            });

            $('a[name=remove_ess]').live('click', function() {

                var essid = $(this).attr('data-cgid');
                var param = {essid:essid,cid:cid,cuno:cuno};
                gamePrivacy.biz.removeEss($(this), param, gamePrivacy.callback.removeEssCallbackOnBlog);
            });
            $('a[name=remove_talk]').live('click', function() {
                var tcgid = $(this).attr('data-cgid');
                var param = {tcgid:tcgid,cid:cid,cuno:cuno};
                gamePrivacy.biz.removeTalk($(this), param, gamePrivacy.callback.removeTalkCallbackOnBlog);
            });
            $('a[name=top_talk]').live('click', function() {
                var tcgid = $(this).attr('data-cgid');
                var param = {tcgid:tcgid,cid:cid,cuno:cuno};
                gamePrivacy.biz.topTalk($(this), param, gamePrivacy.callback.topTalkCallbackOnBlog);
            });
            $('a[name=remove_top_talk]').live('click', function() {
                var tcgid = $(this).attr('data-cgid');
                var param = {tcgid:tcgid,cid:cid,cuno:cuno};
                gamePrivacy.biz.cancleTopTalk($(this), param, gamePrivacy.callback.cancleTopTalkCallbackOnBlog);
            });
        },

        bindAdjustPotinOnBlog:function() {
            $('#but_adjust_point').live('click', function() {
                var point = $('#adjust_point').val();
                var gid = $('#adjust_group_id').val();
                var pointReason = $("#point_reason").val();
                var param = {point:point,cid:cid,cuno:cuno,gid:gid,pointreason:pointReason};

                if (pointReason == '' || pointReason == '请输入加分理由...') {
                    joymeAlert.alert({text:'请正确填写加分理由。',timeOutMills:1000});
                    return false;
                }
                gamePrivacy.biz.preadjustPoint($(this), param, gamePrivacy.callback.preadjustPointCallBak);
            });
        },

        bindSuperGroupPrivacyOnBlog:function() {
            $('#input_search_word').bind('keyup', function(event) {
                var keywords = $(this).val();
                var resultList = $("#search_result_list");
                if (keywords == null || keywords.length == 0) {
                    resultList.remove();
                    $(this).css('color', '#989898');
                    return;
                }

                if (keywords != null && keywords.length > 0
                        && event.keyCode != 38 && event.keyCode != 40 && event.keyCode != 13) {
                    gamePrivacy.biz.searchGame({key:keywords}, gamePrivacy.callback.searchPrivacyByBlog);
                }


                switch (event.keyCode) {
                    case 38:
                        if (resultList.size() > 0 && !resultList.is(":hidden")) {
                            var hoverLink = $("#search_result_list>ul>li>a[class=hover]");
                            var hoverli = hoverLink.parent();

                            if (hoverLink.size() != 0) {
                                hoverLink.removeClass("hover");
                            }
                            if (hoverli.prev().size() != 0) {
                                hoverli.prev().children('a')[0].className = 'hover';
                            } else {
                                $("#search_result_list>ul>li>a").last().addClass("hover");
                            }
                        }
                        break;
                    case 40:
                        if (resultList.size() > 0 && !resultList.is(":hidden")) {
                            var hoverLink = $("#search_result_list>ul>li>a[class=hover]");
                            var hoverli = hoverLink.parent();

                            //清除.libg
                            if (hoverLink.size() != 0) {
                                hoverLink.removeClass("hover");
                            }
                            if (hoverli.next().size() != 0) {
                                hoverli.next().children('a')[0].className = 'hover';
                            } else {
                                $("#search_result_list>ul>li>a").first().addClass("hover");
                            }

                        }
                        break;
                    case 13:
                        $("#search_result_list>ul>li>a[class=hover]").click();
                        break;
                    default:
                        break;
                }

            });

            $('a[name=superPriGame]').live('click', function() {
                var gcid = $(this).attr('data-groupid');
                var param = {gcid:gcid,cid:cid,cuno:cuno};
                var gameName = $(this).text()
                gamePrivacy.biz.getGamePrivacy(param, function(resMsg, param) {

                    gamePrivacy.callback.getPrivacyListByBlog(resMsg, param);
                    $('#search_result_list').remove();
                    $('#input_search_word').val(gameName);
                });
            });

            $('a[name=add_ess]').live('click', function() {
                var essid = $(this).attr('data-cgid');
                var param = {essid:essid,cid:cid,cuno:cuno};
                gamePrivacy.biz.addEss($(this), param, gamePrivacy.callback.addEssCallbackOnBlog);
            });

            $('a[name=add_talk]').live('click', function() {
                var talkid = $(this).attr('data-cgid');
                var param = {talkid:talkid,cid:cid,cuno:cuno};
                gamePrivacy.biz.addTalk($(this), param, gamePrivacy.callback.addTalkCallbackOnBlog);
            });

            $('a[name=remove_ess]').live('click', function() {
                var essid = $(this).attr('data-cgid');
                var param = {essid:essid,cid:cid,cuno:cuno};
                gamePrivacy.biz.removeEss($(this), param, gamePrivacy.callback.removeEssCallbackOnBlog);
            });
            $('a[name=remove_talk]').live('click', function() {
                var tcgid = $(this).attr('data-cgid');
                var param = {tcgid:tcgid,cid:cid,cuno:cuno};
                gamePrivacy.biz.removeTalk($(this), param, gamePrivacy.callback.removeTalkCallbackOnBlog);
            });
            $('a[name=top_talk]').live('click', function() {
                var tcgid = $(this).attr('data-cgid');
                var param = {tcgid:tcgid,cid:cid,cuno:cuno};
                gamePrivacy.biz.topTalk($(this), param, gamePrivacy.callback.topTalkCallbackOnBlog);
            });
            $('a[name=remove_top_talk]').live('click', function() {
                var tcgid = $(this).attr('data-cgid');
                var param = {tcgid:tcgid,cid:cid,cuno:cuno};
                gamePrivacy.biz.cancleTopTalk($(this), param, gamePrivacy.callback.cancleTopTalkCallbackOnBlog);
            });
        },

        bindMagazinePrivacyOnBlog:function() {
            $('#select_magazine_privacy').change(function() {
                var mcid = $(this).val();
                var param = {mcid:mcid,cid:cid,cuno:cuno};
                gamePrivacy.biz.getMagazinePrivacy(param, gamePrivacy.callback.getMagazinePrivacyListByBlog);
            });

            $('a[name=add_magazine]').live('click', function() {
                var mcid = $(this).attr('data-mcid');
                var param = {mcid:mcid,cid:cid,cuno:cuno};
                gamePrivacy.biz.addMagazine($(this), param, gamePrivacy.callback.addMagazineCallbackOnBlog);
            });

            $('a[name=remove_magazine]').live('click', function() {
                var mcid = $(this).attr('data-mcid');
                var param = {mcid:mcid,cid:cid,cuno:cuno};
                gamePrivacy.biz.removeMagazine($(this), param, gamePrivacy.callback.removeMagazineCallbackOnBlog);
            });
        },

        biz:{

            getGamePrivacy:function(param, callback) {
                $.ajax({
                            url: "/json/category/getgameprivacy",
                            type:'post',
                            data:{gcid:param.gcid,cid:param.cid,cuno:param.cuno},
                            success:function(req) {
                                var resMsg = eval('(' + req + ')');
                                if (resMsg.status_code == '1') {
                                    callback(resMsg, param);
                                }
                            }
                        });
            },
            searchGame:function(param, callback) {
                $.ajax({
                            url: "/json/category/search",
                            type:'post',
                            data:{key:param.key},
                            success:function(req) {
                                var resMsg = eval('(' + req + ')');
                                if (resMsg.status_code == '1') {
                                    callback(resMsg, param);
                                }
                            }
                        });
            },
            addEss:function(dom, param, callback) {
                $.ajax({
                            url: "/json/category/addess",
                            type:'post',
                            data:{essid:param.essid,cid:param.cid,cuno:param.cuno},
                            success:function(req) {
                                var resMsg = eval('(' + req + ')');
                                callback(resMsg, dom);
                            }
                        });
            },
            addTalk:function(dom, param, callback) {
                $.ajax({
                            url: "/json/category/addtalk",
                            type:'post',
                            data:{talkid:param.talkid,cid:param.cid,cuno:param.cuno},
                            success:function(req) {
                                var resMsg = eval('(' + req + ')');
                                callback(resMsg, dom);
                            }
                        });
            },
            removeEss:function(dom, param, callback) {
                $.ajax({
                            url: "/json/category/removeess",
                            type:'post',
                            data:{essid:param.essid,cid:param.cid,cuno:param.cuno},
                            success:function(req) {
                                var resMsg = eval('(' + req + ')');
                                callback(resMsg, dom);
                            }
                        });
            },
            removeTalk:function(dom, param, callback) {
                $.ajax({
                            url: "/json/category/removetalk",
                            type:'post',
                            data:{tcgid:param.tcgid,cid:param.cid,cuno:param.cuno},
                            success:function(req) {
                                var resMsg = eval('(' + req + ')');
                                callback(resMsg, dom);
                            }
                        });
            },
            topTalk:function(dom, param, callback) {
                $.ajax({
                            url: "/json/category/toptalk",
                            type:'post',
                            data:{tcgid:param.tcgid,cid:param.cid,cuno:param.cuno},
                            success:function(req) {
                                var resMsg = eval('(' + req + ')');
                                callback(resMsg, dom);
                            }
                        });
            },
            cancleTopTalk:function(dom, param, callback) {
                $.ajax({
                            url: "/json/category/cancletoptalk",
                            type:'post',
                            data:{tcgid:param.tcgid,cid:param.cid,cuno:param.cuno},
                            success:function(req) {
                                var resMsg = eval('(' + req + ')');
                                callback(resMsg, dom);
                            }
                        });
            },
            getMagazinePrivacy:function(param, callback) {
                $.ajax({
                            url: "/json/category/getmagazineprivacy",
                            type:'post',
                            data:{mcid:param.mcid,cid:param.cid,cuno:param.cuno},
                            success:function(req) {
                                var resMsg = eval('(' + req + ')');
                                if (resMsg.status_code == '1') {
                                    callback(resMsg, param);
                                }
                            }
                        });
            },
            addMagazine:function(dom, param, callback) {
                $.ajax({
                            url: "/json/category/addmagazine",
                            type:'post',
                            data:{mcid:param.mcid,cid:param.cid,cuno:param.cuno},
                            success:function(req) {
                                var resMsg = eval('(' + req + ')');
                                callback(resMsg, dom);
                            }
                        });
            },
            removeMagazine:function(dom, param, callback) {
                $.ajax({
                            url: "/json/category/removemagazine",
                            type:'post',
                            data:{mcid:param.mcid,cid:param.cid,cuno:param.cuno},
                            success:function(req) {
                                var resMsg = eval('(' + req + ')');
                                callback(resMsg, dom);
                            }
                        });
            },
            adjustPoint:function(dom, param, callback) {
                $.ajax({
                            url: "/json/note/adjustpoint",
                            type:'post',
                            data:{pointvalue:param.point,cid:param.cid,cuno:param.cuno,gid:param.gid,pointreason:param.pointreason},
                            success:function(req) {
                                var resMsg = eval('(' + req + ')');
                                callback(resMsg, dom);
                            }
                        });
            },
            preadjustPoint:function(dom, param, callback) {
                $.ajax({
                            url:"/json/note/preadjustpoint",
                            type:'post',
                            data:{cuno:param.cuno,pointvalue:param.point,cid:param.cid},
                            success:function(req) {
                                var resMsg = eval('(' + req + ')');
                                callback(resMsg, dom);
                            }
                        });
            }
        },
        callback:{
            getPrivacyListByBlog:function(resMsg, param) {
                var result = resMsg.result[0];
                if (result == null) {
                    return;
                }

                //小组权限
                var privacyHtml = '';
                //精华权限
                if (result.essCategoryId != null && result.essCategoryId > 0) {
                    if (result.essOneLevel) {
                        //从画板屏蔽
                        if (result.essItem == null) {
                            privacyHtml += '<li><a name="add_ess" data-cgid="' + result.essCategoryId + '" href="javascript:void(0);">加为小组精华&nbsp;&gt;&gt;</a></li>';
                        } else {
                            privacyHtml += '<li><a name="remove_ess" data-cgid="' + result.essCategoryId + '" href="javascript:void(0);">从精华版移除&nbsp;&gt;&gt;</a></li>';
                        }
                    }
                }

                if (result.talkCategoryId != null && result.talkCategoryId > 0) {
                    if (result.talkOneLevel && result.talkItem) {
                        //从画板屏蔽
                        privacyHtml += '<li><a name="remove_talk" data-cgid="' + result.talkCategoryId + '" href="javascript:void(0);">从小组屏蔽&nbsp;&gt;&gt;</a></li>';
                    }
                    //有置顶权限，并且在画板没置顶
                    if (result.talkTwoLevel && result.talkItem) {
                        if (!gamePrivacy.itemDisplayType.hasTop(result.talkItem.displayType.value)) {
                            privacyHtml += '<li><a name="top_talk" data-cgid="' + result.talkCategoryId + '" href="javascript:void(0);">置顶到小组&nbsp;&gt;&gt;</a></li>';
                        } else {
                            privacyHtml += '<li><a name="remove_top_talk" data-cgid="' + result.talkCategoryId + '"  href="javascript:void(0);">小组取消置顶&nbsp;&gt;&gt;</a></li>';
                        }
                    }

                    if (result.talkOneLevel && (result.resourceId == null || result.resourceId.length == 0)) {
                        privacyHtml += '<li><a name="add_talk" data-cgid="' + result.talkCategoryId + '" href="javascript:void(0);">移动到小组&nbsp;&gt;&gt;</a></li>';
                    }
                }
                $('#privacy_operate').html(privacyHtml);
            },

            searchPrivacyByBlog:function(resMsg, param) {
                if (resMsg.status_code == '1') {
                    $('#privacy_operate').html('');
                    var list = resMsg.result;
                    var html = '<ul>';
                    $.each(list, function(i, val) {
                        html += '<li id="' + i + '"><a href="javascript:void(0)" data-groupid="' + val.boardId + '" name="superPriGame">' + val.gameName + '</a></li>';
                    });
                    html += '</ul>'

                    var resultListDom = $('#search_result_list');
                    if (resultListDom.size() > 0) {
                        resultListDom.html(html).show();
                    } else {
                        $('#input_search_word').after('<div id="search_result_list">' + html + '</div>');
                    }
                }
            },


            addEssCallbackOnBlog:function(resMsg, dom) {
                if (resMsg.status_code == '1') {
                    var cgid = dom.attr('data-cgid');
                    dom.replaceWith('<a name="remove_ess" data-cgid="' + cgid + '" href="javascript:void(0);">从精华版移除&nbsp;&gt;&gt;</a>');
//                    appendGameEss(resMsg.result[0]);
                    joymeAlert.alert({text:'已成功被添加到' + $('#select_game_privacy option:selected').text() + '小组的精华区'});
                } else {
                    joymeAlert.alert({text:resMsg.msg});
                }
            },
            removeEssCallbackOnBlog:function(resMsg, dom) {
                if (resMsg.status_code == '1') {
                    var cgid = dom.attr('data-cgid')
                    dom.replaceWith('<a name="add_ess" data-cgid="' + cgid + '" href="javascript:void(0);">加为小组精华&nbsp;&gt;&gt;</a>');
//                    removeEss();
                    joymeAlert.alert({text:'本文已经从' + $('#select_game_privacy option:selected').text() + '小组的精华区移除'});
                } else {
                    joymeAlert.alert({text:resMsg.msg});
                }
            },
            addTalkCallbackOnBlog:function(resMsg, dom) {
                if (resMsg.status_code == '1') {
                    var cgid = dom.attr('data-cgid');

                    $('a[name=top_talk]').parent().remove();
                    $('a[name=remove_top_talk]').parent().remove();
                    dom.replaceWith('<a name="remove_talk" data-cgid="' + cgid + '" href="javascript:void(0);">从小组屏蔽&nbsp;&gt;&gt;</a></li><li>' +
                            '<a name="top_talk" data-cgid="' + cgid + '" href="javascript:void(0);">置顶到小组&nbsp;&gt;&gt;</a>');


                    var gameResource = resMsg.result[0];
                    var gameLink = joyconfig.URL_WWW + '/group/' + gameResource.gameCode;
                    $('#content_source').html('<em>来自：<a href="' + gameLink + '" target="_blank">' + gameResource.resourceName + '小组</a></em>');
                    $('#select_game_privacy').trigger("change");

                    joymeAlert.alert({text:'已成功被移动到' + gameResource.resourceName + '小组'});
                } else {
                    joymeAlert.alert({text:resMsg.msg});
                }
            },
            removeTalkCallbackOnBlog:function(resMsg, dom) {
                if (resMsg.status_code == '1') {
                    var cgid = dom.attr('data-cgid');
                    $('a[name=top_talk]').parent().remove();
                    $('a[name=remove_top_talk]').parent().remove();
                    dom.replaceWith('<a name="add_talk" data-cgid="' + cgid + '" href="javascript:void(0);">移动到小组&nbsp;&gt;&gt;</a>');

                    $('#content_source').html('');
                    joymeAlert.alert({text:'本文已经从' + $('#select_game_privacy option:selected').text() + '小组中屏蔽'});
                } else {
                    joymeAlert.alert({text:resMsg.msg});
                }
            },
            topTalkCallbackOnBlog:function(resMsg, dom) {
                if (resMsg.status_code == '1') {
                    var cgid = dom.attr('data-cgid')
                    dom.replaceWith('<a name="remove_top_talk" data-cgid="' + cgid + '" href="javascript:void(0);">小组取消置顶&nbsp;&gt;&gt;</a>');
                    joymeAlert.alert({text:'已成功置顶'});
                } else {
                    joymeAlert.alert({text:resMsg.msg});
                }
            },
            cancleTopTalkCallbackOnBlog:function(resMsg, dom) {
                if (resMsg.status_code == '1') {
                    var cgid = dom.attr('data-cgid')
                    dom.replaceWith('<a name="top_talk" data-cgid="' + cgid + '" href="javascript:void(0);">置顶到小组&nbsp;&gt;&gt;</a>');
                    joymeAlert.alert({text:'已取消置顶'});
                } else {
                    joymeAlert.alert({text:resMsg.msg});
                }
            },
            getMagazinePrivacyListByBlog:function(resMsg, param) {
                if (resMsg.status_code == '1') {
                    var result = resMsg.result[0];
                    if (result == null) {
                        return;
                    }

                    //杂志权限
                    var privacyHtml = '';
                    if (result.talkCategoryId != null && result.talkCategoryId > 0) {
                        if (result.talkOneLevel && result.talkItem) {
                            //从杂志屏蔽
                            privacyHtml += '<li><a name="remove_magazine" data-mcid="' + result.talkCategoryId + '" href="javascript:void(0);">从杂志屏蔽&nbsp;&gt;&gt;</a></li>';
                        } else {
                            privacyHtml += '<li><a name="add_magazine" data-mcid="' + result.talkCategoryId + '" href="javascript:void(0);">移动到杂志&nbsp;&gt;&gt;</a></li>';
                        }
                    }
                    $('#privacy_magazine_operate').html(privacyHtml);
                }
            },
            addMagazineCallbackOnBlog:function(resMsg, dom) {
                if (resMsg.status_code == '1') {
                    var magazineName = $('#select_magazine_privacy option:selected').text();
                    $('#select_magazine_privacy').trigger("change");
                    joymeAlert.alert({text:'已成功被移动到' + magazineName + '杂志'});
                } else {
                    joymeAlert.alert({text:resMsg.msg});
                }
            },
            removeMagazineCallbackOnBlog:function(resMsg, dom) {
                if (resMsg.status_code == '1') {
                    var mcid = dom.attr('data-mcid');
                    dom.replaceWith('<a name="add_magazine" data-mcid="' + mcid + '" href="javascript:void(0);">移动到杂志&nbsp;&gt;&gt;</a>');

                    $('#talk_title').text('');
                    joymeAlert.alert({text:'本文已经从' + $('#select_magazine_privacy option:selected').text() + '杂志中屏蔽'});
                } else {
                    joymeAlert.alert({text:resMsg.msg});
                }
            },
            adjustPointOnBlog:function(resMsg, dom) {
                if (resMsg.status_code == '1') {
                    $("#point_reason").val("");
                    $("#adjust_point").find("option:first").attr("selected", true);
                    joymeAlert.alert({text:resMsg.msg,timeOutMills:null,alertButtonText:'关闭',alertFooter:true});
                } else if (resMsg.status_code == '0') {
                    joymeAlert.alert({text:resMsg.msg,timeOutMills:null,alertButtonText:'关闭',alertFooter:true});
                }
            },
            preadjustPointCallBak:function(resMsg, dom) {
                if (resMsg.status_code == '0') {
                    joymeAlert.alert({text:resMsg.msg,timeOutMills:null,alertButtonText:'关闭',alertFooter:true,width:350});
                } else if (resMsg.status_code == '1') {
                    var point = $('#adjust_point').val();
                    var gid = $('#adjust_group_id').val();
                    var pointReason = $("#point_reason").val();
                    var param = {point:point,cid:cid,cuno:cuno,gid:gid,pointreason:pointReason};
                    var confirmOption = {
                        text:'您将为作者加' + point + '分，理由是:</br></br>' + pointReason + '</br></br>' + resMsg.msg,
                        width:350,
                        submitButtonText:'确定',
                        submitFunction:function() {
                            gamePrivacy.biz.adjustPoint(dom, param, gamePrivacy.callback.adjustPointOnBlog);
                        },
                        cancelButtonText:'取 消',
                        cancelFunction:null
                    };
                    joymeAlert.confirm(confirmOption);
                }
            }
        },

        itemDisplayType:{
            hasTop:function(itemStatus) {
                return (itemStatus & 8) > 0;
            }
        }

    }

//    var appendGameEss = function(gameResource) {
//        if (gameResource == null || $('#essli_' + gameResource.gameRelationSet.boardRelation.value) > 0) {
//            return;
//        }
//
//        var gameIcon = common.genImgDomain(gameResource.icon.images[0].ll, joyconfig.DOMAIN);
//
//        var gameLink = '';
//        var essLink = '';
//        if (gameResource.resourceDomain.code == 'game') {
//            gameLink = joyconfig.URL_WWW + '/game/' + gameResource.gameCode;
//            essLink = joyconfig.URL_WWW + '/game/' + gameResource.gameCode + '/ess';
//        } else {
//            gameLink = joyconfig.URL_WWW + '/board/' + gameResource.gameCode;
//            essLink = joyconfig.URL_WWW + '/board/' + gameResource.gameCode + '/ess';
//        }
//
//        var html = '';
//        if ($('#div_ess').length == 0) {
//            html += ' <div style="display:none" class="side_item" id="div_ess">' +
//                    '<div class="side_hd">本文已被选入</div>' +
//                    '<div class="side_bd side_pd">' +
//                    '<ul class="item_list clearfix" id="ess_list">' +
//                    '<li id="essli_' + gameResource.gameRelationSet.boardRelation.relationValue + '" class="clearfix item_no">' +
//                    '<span class="item_pic">' +
//                    '<img src="' + gameIcon + '" width="69" height="92"/>' +
//                    '</span>' +
//                    '<h3><a href="' + gameLink + '">' + gameResource.resourceName + '</a></h3>' +
//                    '<p><a href="' + essLink + '">精华区</a></p>' +
//                    '</li>' +
//                    '</ul>' +
//                    '</div>' +
//                    '<div class="side_ft"></div>' +
//                    '</div>';
//            $('#ess_flag').after(html);
//            $('#div_ess').fadeIn();
//        } else {
//            html += '<li style="display:none" id="essli_' + gameResource.gameRelationSet.boardRelation.relationValue + '" class="clearfix item_no">' +
//                    '<span class="item_pic">' +
//                    '<img src="' + gameIcon + '" width="69" height="92"/>' +
//                    '</span>' +
//                    '<h3><a href="' + gameLink + '">' + gameResource.resourceName + '</a></h3>' +
//                    '<p><a href="' + essLink + '">精华区</a></p>' +
//                    '</li>';
//            $('#ess_list>li:last').removeClass('item_no');
//            $('#ess_list').append(html);
//            $('#essli_' + gameResource.gameRelationSet.boardRelation.relationValue).fadeIn();
//        }
//    }

//    var removeEss = function() {
//        var gcId = $('#select_game_privacy').val();
//        if ($('#ess_list>li').length <= 1) {
//            $('#div_ess').fadeOut(function() {
//                $(this).remove();
//            })
//        } else {
//            $('#essli_' + gcId).fadeOut(function() {
//                $(this).remove();
//                $('#ess_list>li:last').attr('class', 'clearfix item_no');
//            })
//
//        }
//    }

    return gamePrivacy;
});






