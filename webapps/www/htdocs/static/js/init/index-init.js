define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    require('../common/jquery.validate.min');
    var card = require('../page/card');
    var follow = require('../page/follow');
    var followCallback = require('../page/followcallback');
    var common = require('../common/common.js');
    var ajaxverify = require('../biz/ajaxverify.js');
    var header = require('../page/header');
    require('../common/tips');
    require.async('../common/google-statistics');
    require.async('../common/bdhm');
//    var rollanimate = require('../page/rollanimate')
    $(document).ready(function() {
        //小纸条 搜索 返回
        header.noticeSearchReTopInit();
//        rollanimate.rollanimate();

        follow.ajaxFollowSimpleBind(followCallback.followSimpleCallBack);

//        $('a[id^=rgamelink_]').mouseenter(function() {
//            var no = $(this).attr('data-no');
//            var li = $(this).parent();
//            li.attr('class', 'current').siblings().removeClass('current');
//
//            $('[id^=indexgame_tag_]').removeAttr('style');
//            $('#indexgame_tag_' + no).show();
//        });

        $('a[name=link_showopen]').click(function() {
            var openid = $(this).attr('data-openid');
            var frame = $('#frame_show_' + openid);
            if (frame.length > 0 && frame.is(':hidden')) {
                $('a[name=link_showopen]').removeClass('active');
                $(this).attr('class', 'active');
                $('[id^=frame_show_]').hide();
                frame.show();
            }
        });

        $("#iframeWeibo>li").append('<iframe id="frame_show_sinaweibo" style="margin: 0px 4px;width: 180px;" height="346" class="share_self" frameborder="0"' +
                'scrolling="no"' +
                'src="http://widget.weibo.com/weiboshow/index.php?language=&width=0&height=366&fansRow=2&ptype=1&speed=0&skin=1&isTitle=0&noborder=0&isWeibo=1&isFans=0&uid=2253167477&verifier=4d2ced93&colors=ccc,ffffff,666666,0082cb,ecfbfd&dpc=1">' +
                '</iframe>');
//                '<iframe id="frame_show_qweibo" style="display:none;margin: 0px 4px;width: 288px" height="498" class="share_self" frameborder="0" scrolling="no"' +
//                'src="http://v.t.qq.com/pendant/show.php?n=joymeweb&w=288&h=498&fl=2&l=8&o=17&c=4&si=d8bca5c70fee99f5145f432a8f1abd070a9b363b&cs=CCCCCC_FFFFFF_000000_C0C0C0"></iframe>'

        var nextPageNo = 2;
        $("#reload_talent").live("click", function() {
            $.post("/json/index/talent", {p:nextPageNo}, function(data) {
                var jsonobj = eval("(" + data + ")");
                if (jsonobj.result == null || jsonobj.msg == null) {
                    return;
                }

                var result = jsonobj.result[0];
                var pageObj = jsonobj.result[1];
                var htmlStr = "";
                for (var i = 0; i < result.length; i++) {
                    var head = common.parseSimg(result[i].thumbimg, joyconfig.DOMAIN);

                    htmlStr += '<li ' + ((i == result.length - 1) ? 'class="noborder"' : '') + '>';

                    var screenName = result[i].screenName;
                    if (screenName.length > 7) {
                        screenName = screenName.substring(0, 7) + '…';
                    }

                    var verifyType = '';
                    if (result[i].verifyType != null && result[i].verifyType != 'n') {
                        verifyType = '<a href="javascript:void(0);" class="' + result[i].verifyType + 'vip" title="' + joyconfig.viptitle[result[i].verifyType] + '"></a>';
                    }

                    htmlStr = htmlStr + '<div class="user_info">' +
                            '<a class="user_img" href="' + joyconfig.URL_WWW + '/people/' + result[i].domain + '" target="_blank">' +
                            '<img width="33" height="33" src="' + head + '"></a>' +
                            '<a class="user_name" href="'+ joyconfig.URL_WWW + '/people/' + result[i].domain + '" target="_blank">' + screenName + '</a>' +
                            verifyType +
                            '<p>';
                    var profileDesc = result[i].desc;
                    if (profileDesc == null) {
                        profileDesc = '';
                    }
                    if (profileDesc.length > 10) {
                        profileDesc = common.subStr(profileDesc, 9) + '…';
                    }
                    htmlStr += profileDesc + '</p></div>';

                    for (var j = 0; j < result[i].lastContentList.length; j++) {
                        htmlStr = htmlStr + '<p>• <a href="' + joyconfig.URL_WWW + '/note/' + result[i].lastContentList[j].contentId + '/" target="_blank">'

                        var contentText = '';
                        contentText = (result[i].lastContentList[j].subject != null && result[i].lastContentList[j].subject.length > 0) ? result[i].lastContentList[j].subject : result[i].lastContentList[j].content;
                        if (contentText == null || contentText.length == 0) {
                            contentText = '';
                        }

                        if (contentText.length > 18) {
                            contentText = contentText.substring(0, 18) + '…';
                        }
                        htmlStr = htmlStr + contentText + '</a></p>';

                    }

                    htmlStr = htmlStr + "</div>"
                    if (i % 2 == 1 || i == result.length - 1) {
                        htmlStr = htmlStr + '</li> ';
                    }
                }

                $("#daren-tj").html(htmlStr);
                if (pageObj.curPage == 1) {
                    $('#reload_talent').html('后排还有人');
                    nextPageNo = pageObj.curPage + 1;
                } else if (pageObj.lastPage) {
                    $('#reload_talent').html('回到第一排');
                    nextPageNo = 1;
                } else {
                    $('#reload_talent').html('下一排还有人');
                    nextPageNo = pageObj.curPage + 1;
                }
            })
        })

        var fpageNo = 1;
        var ulDom = $('ul[id^=ul_famous]');
        var downDom = $('#famous_pagedown');
        var upDom = $('#famous_pageup');
        $('#famous_pageup').click(function() {
            if ($(this).attr('class') == 'pagebtn_left_2') {
                return
            }

            fpageNo--;
            famousPageLogic(upDom, downDom);
        })
        $('#famous_pagedown').click(function() {
            if ($(this).attr('class') == 'pagebtn_right_2') {
                return
            }

            fpageNo++;
            famousPageLogic(upDom, downDom);
        });
        var famousPageLogic = function(upDom, downDom) {
            if (fpageNo <= 1) {
                fpageNo = 1;
                upDom.attr('class', 'pagebtn_left_2');
            } else {
                upDom.attr('class', 'pagebtn_left_2_current');
            }
            if (fpageNo >= fMaxPage) {
                fpageNo = fMaxPage;
                downDom.attr('class', 'pagebtn_right_2');
            } else {
                downDom.attr('class', 'pagebtn_right_2_current');
            }

            $.post(joyconfig.URL_WWW + "/json/index/famous", {p:fpageNo}, function(req) {
                var resultMsg = eval('(' + req + ')');
                if (resultMsg.status_code != '1') {
                    return;
                }

                var result = resultMsg.result[0];

                var htmlStr = "";
                for (var i = 0; i < result.length; i++) {
                    var head = common.genImgDomain(result[i].thumbimg, joyconfig.DOMAIN);
                    var domain = joyconfig.URL_WWW + '/people/' + result[i].domain ;
                    var follow = '';
                    if (joyconfig.joyuserno != null && joyconfig.joyuserno == result[i].uno) {
                    } else if (joyconfig.joyuserno == null || result[i].relationFlag == null || result[i].relationFlag == "-1" || result[i].relationFlag == "0") {
                        follow = '<a class="add_attention" name="follow-simple" href="javascript:void(0)"  data-uno="' + result[i].uno + '"></a>';
                    } else {
                        follow = '<a class="attentioned_nocancel" href="javascript:void(0);"></a>';
                    }
                    var verifyType = '';
                    if (result[i].verifyType != null && result[i].verifyType != 'n') {
                        verifyType = '<a href="javascript:void(0);" class="' + result[i].verifyType + 'vip" title="' + joyconfig.viptitle[result[i].verifyType] + '"></a>';
                    }
                    var desc = (result[i].desc != null && result[i].desc.length > 0) ? common.subStr(result[i].desc, 30) : '';
                    var screenName = common.subStr(result[i].screenName, 5)

                    htmlStr += '<li ' + ((i > 1 || result.length < 2 ) ? 'class="noborder"' : '') + '>' +
                            '<div class="joymestar-left">' +
                            '<a class="joymestar-pic" href="' + domain + '" target="_blank">' +
                            '<em>' +
                            '<img width="101" src="' + head + '"></em>' +
                            '<span></span>' +
                            '</a>' +
                            follow +
                            '</div>' +
                            '<div class="joymestar-right">' +
                            ' <p class="joymestar-name">' +
                            '<a href="' + domain + '" target="_blank">' + screenName + '</a>' +
                            verifyType +
                            '</p>' +
                            '<p class="joymestar-discription">' + desc + '</p>' +
                            '</div>' +
                            '</li>';
                }
                ulDom.html(htmlStr);
            })
        }

        //找游戏
        $('#link_findgame').live('click', function() {
            fgamePage++;
            $.post(joyconfig.URL_WWW + '/json/index/findgame', {p:fgamePage}, function(req) {
                var resultMsg = eval('(' + req + ')');
                if (resultMsg.status_code != '1') {
                    return;
                }
                var result = resultMsg.result[0];
                $('#div_findgame').replaceWith(result);
                fgamePage = resultMsg.result[1];
            });
        });
        $('.findedgame li').live('mouseenter',
                function() {
                    $(this).find('p.goto').show()
                }).live('mouseleave', function() {
                    $(this).find('p.goto').hide()
                })
    });
});