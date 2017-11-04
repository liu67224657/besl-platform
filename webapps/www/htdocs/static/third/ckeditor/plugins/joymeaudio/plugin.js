/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 11-7-25
 * Time: 下午10:15
 * add jmh mood js
 */
(function() {
    //Section 1 : 按下自定义按钮时执行的代码
    var a = {
        exec:function(editor) {
            displayAudio(editor);
        }
    },
        //Section 2 : 创建自定义按钮、绑定方法
            b = 'joymeaudio';
    CKEDITOR.plugins.add(b, {
                init:function(editor) {
                    editor.addCommand(b, a);
                    editor.ui.addButton('joymeaudioBtn', {
                                title:'添加音乐',
                                icon: CKEDITOR.plugins.getPath('joymeaudio') + '/music_icon.jpg',
                                command:b

                            });
                }
            });
})();

function displayAudio(oEditor) {
    if (oEditor.getData() != null && oEditor.getData().length != 0) {
        if (!regAudio(oEditor)) {
            //oEditor.getCommand('joymeaudio').setState(0);
            jmAlert('一篇文章只允许上传一个音乐',false);
            return;
        }
    }

    $(".reldiv").css("display", "none");
    $("#div_text_post_audio").slideToggle("fast", function() {
        $(this).find("#audio_ptext_audio").focus();
    });


}

function regAudio(editor) {
    var editorValue = editor.getData();

    blogContent.audio = null;
    var audioRegex = /<img\s+joymed=\"([^>]*)\"\s+joymef=\"([^>]+)\"\s+joymet=\"audio\"\s+src=\"([^>]+)\"\s+title=\"([^>]*)\"\s*(\/>)/g;
    var joymeImgEle = audioRegex.exec(editorValue);
    if (joymeImgEle != null) {
        blogContent.audio = {album:joymeImgEle[3],flash:joymeImgEle[2],title:joymeImgEle[1]};
        return false;
    } else {
        return true;
    }
}
