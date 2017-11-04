/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-12
 * Time: 下午5:05
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var postoeditoraudio = {
        postTextAudio:function (id, album, text) {
            var flashurl = 'http://www.xiami.com/widget/0_' + id + '/singlePlayer.swf';
            $('#div_text_audio_post_content').css('display', 'block');
            $("#text_audio_img").html('<img src="' + album + '" width="147"/>');
            $("#post_text_audio_title").val(text);
            $("#preview_audio_flash").html('<object id="text_audio_preview_flash" width="283" height="33" align="middle"' +
                    'classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000"' +
                    'codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,0,0">' +
                    '<param name="allowScriptAccess" value="sameDomain">' +
                    '<param name="movie" value="' + flashurl + '">' +
                    '<param name="wmode" value="Transparent">' +
                    '<embed width="283" height="33" wmode="Transparent" type="application/x-shockwave-flash"' +
                    'src="' + flashurl + '">' +
                    '</object>');

            $('#link_insert_text_audio').unbind('click').bind('click', function() {
                var str = '<br />' + createAudioImage(album, flashurl, $('#post_text_audio_title').val(), $("#post_text_audio_desc").val()) +
                        '<br />' + $('#post_text_audio_title').val() +
                        '<br />' + $("#post_text_audio_desc").val();
                CKEDITOR.instances['text_content'].insertHtml(str);
                resetSearchAudioTextStep2();
                $("#div_text_audio_post_content").css('display', 'none');
            });
            resetSearchAudioTextStep1();
            $("#div_text_post_audio").css("display", "none");
        }
        //取消音乐发布层第一步，第二步

    }
    var resetSearchAudioTextStep2 = function() {
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
    return postoeditoraudio;
});