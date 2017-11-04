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
            displayIOS(editor);
        }
    },
        //Section 2 : 创建自定义按钮、绑定方法
            b = 'joymeios';
    CKEDITOR.plugins.add(b, {
                init:function(editor) {
                    editor.addCommand(b, a);
                    editor.ui.addButton('joymeiosBtn', {
                                title:'添加苹果应用',
                                icon: CKEDITOR.plugins.getPath('joymeios') + 'app.jpg',
                                command:b
                            });
                }
            });
})();


function displayIOS(oEditor) {
    if (oEditor.getData() != null && oEditor.getData().length != 0) {
        if (!regIOS(oEditor)) {
            jmAlert('一篇文章只允许上传一个苹果应用', false);
            return false;
        }
    }
    $(".reldiv").css("display", "none");
    $("#div_text_post_ios").slideToggle("fast", function() {
        $(this).find("#text_ios_url").focus();
    });
}

function regIOS(editor) {
    var editorValue = editor.getData();
    blogContent.ios = null;
    var videoRegex = /<img\s+joymeappt=\"ios\"\s+joymec=\"([^>\"]+)\"\s+joymed=\"([^>\"]+)\"\s+joymei=\"([^>\"]+)\"\s+joymer=\"([^>\"]+)\"\s+joymet=\"app\"\s+src=\"([^>\"]+)\"\s+title="([^>\"]*)"\s*\/?>/gi;
    var joymeImgEle = videoRegex.exec(editorValue);
    if (joymeImgEle != null) {
        blogContent.ios={icon:joymeImgEle[3],card:joymeImgEle[1],url:joymeImgEle[4],desc:joymeImgEle[2]};
        return false;
    } else {
        return true;
    }
}
