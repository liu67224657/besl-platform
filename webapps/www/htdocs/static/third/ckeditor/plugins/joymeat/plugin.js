/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 11-10-14
 * Time: 下午3:14
 * To change this template use File | Settings | File Templates.
 */
(function(){
    //Section 1 : 按下自定义按钮时执行的代码
    var a= {
        exec:function(editor){
            displayAtme(editor);
        }
    },
    //Section 2 : 创建自定义按钮、绑定方法
    b='joymeat';
    CKEDITOR.plugins.add(b,{
        init:function(editor){
            editor.addCommand(b,a);
            editor.ui.addButton('joymeat',{
                        title:'@朋友',
                icon: this.path + '/acon.jpg',
                command:b
            });
        }
    });
})();

function displayAtme(editor) {
    $(".reldiv").css("display","none");
    $("#atFriendText").slideToggle("fast",function(){
        $(this).find("#at_input_text").focus();
    });
    setAtFoucsList('Text');
}