/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 11-7-25
 * Time: 下午10:15
 * add jmh mood js
 */
(function(){
    //Section 1 : 按下自定义按钮时执行的代码
    var a= {
        exec:function(editor){
            upload();
        }
    },
    //Section 2 : 创建自定义按钮、绑定方法
    b='upimage';
    CKEDITOR.plugins.add(b,{
        init:function(editor){
            editor.addCommand(b,a);
            editor.ui.addButton('upimage',{
                icon: this.path + 'images/p_face.gif',
                command:b
            });
        }
    });
})();

function upload(){
    window.parent.selectFile();
}
