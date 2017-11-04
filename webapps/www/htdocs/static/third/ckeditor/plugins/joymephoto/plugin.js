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
            displayPhoto(editor);
        }
    },
        //Section 2 : 创建自定义按钮、绑定方法
            b = 'joymephoto';
    CKEDITOR.plugins.add(b, {
                init:function(editor) {
                    editor.addCommand(b, a);
                    editor.ui.addButton('joymephotoBtn', {
                                title:'上传本地图片',
                                icon: this.path + '/pic_icon.jpg',
                                command:b
                            });
                }
            });


})();

function displayPhoto(editor) {
    if(!regImg(editor)){
        // editor.getCommand('joymephoto').setState(0);
        jmAlert('只能上传20张图片',false);
        return false;
    }
    $(".reldiv").css("display", "none");
    $.JMDialog({
                ___title:"上传图片",
                ___content:"iframe:/mask/content/post/photo",
                ___showbg:true,
                ___width:"690",
                ___height:"400",
                ___drag:"___boxTitle",
                ___windowBgOpacity :"0" ,
                ___boxBdOpacity :"1",
                ___boxWrapBdColor :"#fff",
                ___boxBdColor:"none"
            });
}
function hideTextPhoto() {
    $.JMDialog.removeBox();
}

function regImg(editor) {
    var editorValue = editor.getData();

    //将数量反解析成blogContent
    blogContent.image = new Array();
    //正则得到编辑器中所有的图片元素的数量
    var imgRegex = /<img\s+joymed=\"([^>]*)\"\s+joymet=\"img\"\s+joymeu=\"([^>]+)\"\s+(?:style="[^>"]*"\s+)?src=\"([^>]+)\"\s*(\/>)/g;
    var matches = editorValue.match(imgRegex);
    if (matches != null) {
        for (var i = 0; i < matches.length; i++) {
            var imgHtml = matches[i];
            var parternElement = new RegExp(imgRegex.source);
            var joymeImgEle = parternElement.exec(imgHtml);
            blogContent.image.push({key:i,value:{url:joymeImgEle[2],src:joymeImgEle[3],desc:''}});
        }
    }
    if(blogContent.image.length>20){
        return false;
    }else{
        return true;
    }
}