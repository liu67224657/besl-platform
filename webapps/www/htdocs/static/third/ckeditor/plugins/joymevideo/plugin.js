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
            displayVideo(editor);
        }
    },
        //Section 2 : 创建自定义按钮、绑定方法
            b = 'joymevideo';
    CKEDITOR.plugins.add(b, {
                init:function(editor) {
                    editor.addCommand(b, a);
                    editor.ui.addButton('joymevideoBtn', {
                                title:'添加视频',
                                icon: CKEDITOR.plugins.getPath('joymevideo') + 'video_icon.jpg',
                                command:b
                            });
                }
            });
})();


function displayVideo(oEditor) {
    $('#video_text_error').text('');
    if (oEditor.getData() != null && oEditor.getData().length != 0) {
        if (!regVideo(oEditor)) {
            //oEditor.getCommand('joymevideo').setState(0);
            jmAlert('一篇文章只允许上传一个视频', false);
            return false;
        }
    }
    $(".reldiv").css("display", "none");
    $("#div_text_post_video").slideToggle("fast", function() {
        $(this).find("#video_ptext_url").focus();
    });
}

function regVideo(editor) {
    var editorValue = editor.getData();

    blogContent.video = null;
    var videoRegex = /<img\s+joymed=\"([^>]*)\"\s+joymef=\"([^>]+)\"\s+joymet=\"video\"\s+src=\"([^>]+)\"\s+title=\"([^>]*)\"\s*(\/>)/g;
    var joymeImgEle = videoRegex.exec(editorValue);
    if (joymeImgEle != null) {
        blogContent.video = {album:joymeImgEle[3],flash:joymeImgEle[2],title:joymeImgEle[1]};
        return false;
    } else {
        return true;
    }
}
