/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-7
 * Time: 上午10:11
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var postaudio = {
        searchChatAudio:function (event, ctx, page) {
            var strcode = $('#audio_code').val();
            var elementIds = {songList:'songlist',
                songInfo:'songinfo',
                songlinkPrefix:'song_link_',
                audioSearch:'audio_search',
                selectAudioCallBack:this.selectChatAudio,
                pager_prefix:'chat_audio_'};
            searchAudio(strcode, event, ctx, page, elementIds, ajaxResultOper);
        },
        searchTextAudio:function (event, ctx, page) {
            var strcode = $('#audio_oEditor_code').val();
            var elementIds = {songList:'t_songlist',
                songInfo:'t_songinfo',
                songlinkPrefix:'song_t_link_',
                audioSearch:'audio_text_search',
                selectAudioCallBack:this.selectTextAudio,
                pager_prefix:'text_audio_'};
            searchAudio(strcode, event, ctx, page, elementIds, ajaxResultOper);
        },
        selectChatAudio:function (id, album, idx) {
            $('#audio_code').val($('#song_link_' + idx).attr('title'));
            $("#link_upload_audio").die().live('click', function() {
                if ($('#audio_empty').length > 0) {
                    return;
                }
                $(".pop").hide();
                $("#chat_content").focus();
                postChatAudio(id, album, $('#audio_code').val());
            });
        },
        selectTextAudio : function (id, album, idx) {
            $('#audio_oEditor_code').val($('#song_t_link_' + idx).attr('title'));
            $("#link_oEditor_upload_audio").die().live('click', function() {
                if ($('#audio_empty').length > 0) {
                    return;
                }
                $(".pop").hide();
                $("#chat_content").focus();
                postTextAudio(id, album, $('#audio_oEditor_code').val());
            });
        }

    }
    var searchAudio = function(audioCode, event, ctx, page, elementIds, callback2) {
        if (audioCode == null || audioCode.length == 0) {
            resetSearchInfo();
            return;
        }
        if (event.keyCode != 38 && event.keyCode != 40 && event.keyCode != 13) {
            var postbiz = require('../biz/post-biz');
            postbiz.searchAudio(audioCode, event, ctx, page, elementIds, callback2)
        }
    }
    var resetSearchInfo = function () {
        $("#songlist").children().remove();
        $("#songinfo").children().remove();
        $("#audio_search").css("display", "none");
        $("#audio_error").val('');
        $("#audio_code").val('');

    }
    var postChatAudio = function (id, album, text) {
        if (text == null) {
            $("#audio_error").text('歌曲名称不能为空');
            return;
        }
        displayPreviewDiv();
//
        var flashurl = 'http://www.xiami.com/widget/0_' + id + '/singlePlayer.swf';
        $("#ul_preview").append('<li id="li_audio_preview"><a href="javascript:void(0)" class="musicreview" >' + text + '</a><a href="javascript:void(0)" title="取消" class="close"></a>' +
                '<input type="hidden" name="audioUrl" value="' + flashurl + '" />' +
                '<input type="hidden" name="audioAlbum" value="' + album + '" />' +
                '<input type="hidden" name="audioTitle" value="' + text + '" />' +
                '</li>');
        $('#audio_code').val('');
        if ($("#ul_preview li").size() > 0) {
            $("#post_chat_submit").attr('class','publishon');
            $("#edit_chat_submit").attr('class','publishon');
        }
        blogContent.audio = {flash:flashurl,album:album,title:text};
    }
    var dynmicbindClass = function (jq, className) {
        if (!jq.hasClass(className)) {
            jq.attr('class', className);
        } else {
            jq.attr('class', '');
        }
    }
    var displayPreviewDiv = function () {
        $("#rel_preview").show();
    }
    var hidePreviewDiv = function() {
        if ($("#ul_preview li").length == 0) {
            $("#rel_preview").hide();
        }
    }

    var postTextAudio = function (id, album, text) {
        var flashurl = 'http://www.xiami.com/widget/0_' + id + '/singlePlayer.swf';
        //构建第二层音频展示
        var offset = $("#editor_link").offset();
        var config = {
            pointerFlag : true,//是否有指针
            pointdir : 'up',//指针方向
            tipLayer : false,//是否遮罩
            offset:"Custom",
            containTitle : true,//包含title
            containFoot : true,//包含footer
            offsetlocation:[offset.top + 31,offset.left],
            className:"",
            forclosed:true,
            popwidth:445 ,
            allowmultiple:false,
            isfocus:true
        };
        var musicId = "div_text_audio_post_content";
        var pop = require('../common/jmpopup');
        if ($("#" + musicId).length > 0) {
            pop.resetOffset(config, musicId);
        } else {
            var htmlObj = new Object();
            htmlObj['id'] = musicId;
            htmlObj['html'] = '<div class="longmusic clearfix"><div class="longmusicl" id="text_audio_img">' +
                    '<img width="141" height="147" src="' + album + '">' +
                    '</div><div class="longmusicr">' +
                    '<input type="text" id="post_text_audio_title" value="' + text + '" class="musictxt">' +
                    '<span class="mv" id="preview_audio_flash">' +
                    '<object id="text_audio_preview_flash" width="283" height="33" align="middle"' +
                    'classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000"' +
                    'codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,0,0">' +
                    '<param name="allowScriptAccess" value="sameDomain">' +
                    '<param name="movie" value="' + flashurl + '">' +
                    '<param name="wmode" value="Transparent">' +
                    '<embed width="283" height="33" wmode="Transparent" type="application/x-shockwave-flash"' +
                    'src="' + flashurl + '">' +
                    '</object>' +
                    '</span><textarea id="post_text_audio_desc" cols="" rows="" class="musictextarea" style="font-family:Tahoma, \'宋体\';"></textarea></div></div>';
            htmlObj['title'] = '插入音乐'
            htmlObj['input'] = '<a class="submitbtn" id="link_insert_text_audio"><span>加入文章</span></a>';
            pop.popupInit(config, htmlObj);
            $("#closePop_div_text_audio_post_content").die().live('click', function() { //关闭第二层时remove掉第二层
                $("#div_text_audio_post_content").remove();
            });
            $('#link_insert_text_audio').die().live('click', function() {//为确定按钮注册插入事件
                var str = '<br />' + createAudioImage(album, flashurl, $('#post_text_audio_title').val(), $("#post_text_audio_desc").val()) +
                        '<br />' + $('#post_text_audio_title').val() +
                        '<br />' + $("#post_text_audio_desc").val();
                CKEDITOR.instances['text_content'].insertHtml(str);
                resetSearchAudioTextStep2();
                $("#div_text_audio_post_content").remove();
            });
        }
        resetSearchAudioTextStep1();
        $("#div_text_post_audio").css("display", "none");
    }
    var ajaxResultOper = function(audioCode, event, ctx, page, elementIds, resultMsg, callback2) {
        var audioinfo = resultMsg.result[0];
        if (resultMsg.status_code == '1' && audioinfo != null) {
            if (audioinfo.results.length == 0) {
                $("#" + elementIds.songList).html('<li id="audio_empty"><a href="javascript:void(0);">对不起，没有关于‘' + audioCode + '’的音乐<li></a>');
                $("#" + elementIds.audioSearch).slideDown();
                return;
            }

            for (var i = 0; i < audioinfo.results.length; i++) {
//                    var songTitle=audioinfo.results[i].song_name;
                var songName = audioinfo.results[i].song_name;
                // songName=songName.length>22?songName.substring(0,22)+'...':songName;
                var musicName = songName.length > 30 ? songName.substring(0, 30) + '...' : songName;
                $("#" + elementIds.songList).append('<li>' +
                        '<a title="' + songName + '" id="' + elementIds.songlinkPrefix + i + '" href="javascript:void(0);" name="' + audioinfo.results[i].song_id + '||' + audioinfo.results[i].album_logo + '||' + i + '">' + musicName + '</a>' +
                        '</li>');
                $('#' + elementIds.songlinkPrefix + i).live('hover', function() {
                    $("#" + elementIds.songList + ' li').removeClass('on');
                    dynmicbindClass($(this).parent(), 'on');
                }, function() {
                    $("#" + elementIds.songList + ' li').removeClass('on');
                    dynmicbindClass($(this).parent(), 'on');
                });
            }

            //分页
            var pageSize = 5;
            var pre = page - 1 >= 1 ? page - 1 : 1;
            var last = parseInt((audioinfo.total - 1) / pageSize + 1);
            var next = page >= last ? page : page + 1;
            $("#" + elementIds.songInfo).append('<span><a id="' + elementIds.pager_prefix + 'pre" href="javascript:void(0);">上一页</a> | <a id="' + elementIds.pager_prefix + 'next" href="javascript:void(0);" >下一页</a></span>');
            $("#" + elementIds.pager_prefix + 'pre').one('click', function(event) {
                searchAudio(audioCode, event, ctx, pre, elementIds, callback2);
            })
            $("#" + elementIds.pager_prefix + 'next').one('click', function(event) {
                searchAudio(audioCode, event, ctx, next, elementIds, callback2);
            })

            $("#" + elementIds.audioSearch).slideDown();
        }
    }
    var createAudioImage = function (album, flashUrl, title, desc) {
        var str = '<br /><img src="' + album + '" joymet="audio"  title="' + title + '"  joymed="' + desc + '" joymef="' + flashUrl + '"/>';
        return str;
    }
    var autoTextAudioFill = function (event) {
        switch (event.keyCode) {

            case 38:
                if (!$("#audio_text_search").is(':hidden')) {
                    var p_up = $("#audio_text_search .audiohover");
                    if (p_up.length != 0) {
                        p_up.removeClass("audiohover");
                    }
                    if (p_up.prev().length != 0) {
                        p_up.prev().addClass("audiohover");
                    } else {
                        var last = $("#audio_text_search ul li").last();
                        last.addClass("audiohover");
                    }
                }
                break;
            case 40:
                if (!$("#audio_text_search").is(':hidden')) {
                    var p_up = $("#audio_text_search .audiohover");
                    if (p_up.length != 0) {
                        p_up.removeClass("audiohover");
                    }
                    if (p_up.next().length != 0) {
                        p_up.next().addClass("audiohover");
                    } else {
                        var first = $("#audio_text_search ul li").first()
                        first.addClass("audiohover");
                    }
                }
                break;
            case 13:
                var p_up = $("#audio_text_search .audiohover");
                p_up.children('a').click();
                break;
            default:
                break;
        }
    }
    var resetSearchAudioTextStep2 = function () {
        $('#post_text_audio_title').val('');
        $("#post_text_audio_desc").val('');
        $('#preview_audio_flash').children().remove();
    }
    var resetSearchAudioTextStep1 = function () {
        $("#t_songlist").children().remove();
        $("#t_songinfo").children().remove();
        $('#audio_text_search').css('display', 'none');
        $("#audio_text_error").val('');
        $("#audio_ptext_audio").val('');
    }

    return postaudio;
})