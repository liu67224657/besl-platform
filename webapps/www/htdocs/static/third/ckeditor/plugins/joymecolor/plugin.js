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
        exec:function(editor,colorNum) {
            addColor(editor,colorNum);
        }
    },
        //Section 2 : 创建自定义按钮、绑定方法
            b = 'joymecolor';
    CKEDITOR.plugins.add(b, {
                init:function(editor) {
                    editor.addCommand(b, a);
                    editor.ui.addButton('joymecolor', {
                                icon: this.path + '/link_icon.jpg',
                                command:b
                            });
                }
            });


})();

function addColor(editor,colorNum) {
    var mySelection = editor.getSelection();
    if(mySelection){
        var text = '';
        nativeSel = selected.getNative();
        text = CKEDITOR.env.ie ? nativeSel.createRange().text : nativeSel.toString();
		text = '<span style="color:#'+colorNum+'">'+text+'</span>';
		editor.insertHtml(text);
    }else{
		jmAlert("请选择内容");
		editor.focus();
	}

}