/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 11-7-25
 * Time: 下午10:15
 * add image link
 */
(function() {
    //Section 1 : 按下自定义按钮时执行的代码
    var a = {
        exec:function(editor) {
            displayImageLink(editor);
        }
    },
        //Section 2 : 创建自定义按钮、绑定方法
            b = 'joymeimagelink';
    CKEDITOR.plugins.add(b, {
                init:function(editor) {
                    editor.addCommand(b, a);
                    editor.ui.addButton('joymeimagelink', {
                                title:'上传网络图片',
                                icon: this.path + '/link_icon.jpg',
                                command:b
                            });
                }
            });


})();

function displayImageLink(editor) {
    if(!regImg(editor)){
        alert('只能上传20张图片');
        return false;
    }
    $(".reldiv").css("display", "none");
    $("#div_text_post_imagelink").slideToggle("fast",function(){
         $(this).find('#input_text_img_link').focus();
     });
}
