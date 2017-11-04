var WS = function (opt) {
    var holderVal = opt.data;//{text:value}
    var regexp = opt.regexp || /\S/,
        el = $(opt.el),
        list = el.val().split(','),
        holder = $('<span class="words-split"></span>'),
        add = $('<a href="javascript:void(0)" class="words-split-add">+</a>');

    fillData();

    el.hide().after(holder);
    holder.after(add);

    holder.on('click', 'a>em', function () {	//刪除

        var name =$(this).parent().text().trim();

        delete holderVal[name]
        $(this).parent().remove();
        fillData();
    });

    add.on('click', function () {				//添加预处理
        $(this).hide();
        holder.append($('<span class="lbl-input" contenteditable="true"/>'))
    });

    holder.on('blur', '.lbl-input', function () {	//验证添加字段
        var t = $(this), v = t.text();
        if (v!=null && v.length>0 && !regexp.test(v)) {
            alert('输入不合法');
        } else if (!v) {
            t.remove();
            add.show();
        } else {
            t.remove();
            add.show();
            $.get('/gamev2/tag/getbyname', {tagName: v}, function (req) {

                if (req == 'ban') {
                    alert('标签被禁用了');
                    return;
                }
                if (req == 404) {
                    alert('标签不存在');
                    return;
                }
                if (req == 500) {
                    alert('内部错误');
                    return;
                }
                var resObj = eval('(' + req + ')');
                if (!resObj.rs) {
                    holderVal[resObj.tagName]=resObj.id;
                    fillData();
                }
            });
        }
    });

    holder.on('keydown', '.lbl-input', function (e) {
        switch (e.keyCode) {
            case 13:
            case 27:
                $(this).trigger('blur');
        }
    });

    function fillData(){
        holder.html('');
        var val='';
        $.each(holderVal,function(i,v){
            if (v!=null) {
                holder.append($('<a href="javascript:void(0)" class="fm-button">' + i + '<em> </em></a>'));
                val+=v + ',';
            }
        })
        el.val(val);
    }

}
//demo
// WS({el:'#staticPath',regexp:/\w+\.\w+/});
