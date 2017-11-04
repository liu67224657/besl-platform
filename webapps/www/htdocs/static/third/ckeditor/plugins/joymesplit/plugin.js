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
            addSplitLine();
        }
    },
    //Section 2 : 创建自定义按钮、绑定方法
    b='joymesplit';
    CKEDITOR.plugins.add(b,{
        init:function(editor){
            editor.addCommand(b,a);
            editor.ui.addButton('joymesplit',{
                title:'插入分割线',
                icon: this.path + '/fgx_icon.jpg',
                command:b
            });
        }
    });
})();

function addSplitLine(){
    CKEDITOR.instances['text_content'].insertHtml('<br /><br />---------------------华丽的分割线----------------------------<br /><br />');
}
