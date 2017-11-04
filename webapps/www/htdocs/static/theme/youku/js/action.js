//头图轮播
var action={
    'toolsInfo':function(){
        var winWidth=$(window).width();
        if(winWidth<768){
            action.checkTopLoop();//手机
        }else{
            action.checkTopLoop1();//平板
        }
    },
    /* 头图轮播ios */
    'checkTopLoop':function(){
        if ($('#pic-loop .swiper-slide').length > 1){
            var mySwiper = new Swiper('#pic-loop',{
                loop: true,
                pagination: '.pagination',
                paginationClickable: false,
                autoResize:false,
                mode: 'horizontal',  //水平
                cssWidthAndHeight: true
            });
            setInterval(function(){mySwiper.swipeNext()}, 3000);
        }
    },
    /* 头图轮播ipad */
    /*'checkTopLoop1':function(){
     var sideW=$('.swiper-slide').width()/5;

     if ($('#pic-loop .swiper-slide').length > 1){
     var mySwiper = new Swiper('#pic-loop',{
     loop: true,
     pagination: '.pagination',
     paginationClickable: false,
     offsetPxBefore:sideW,
     autoResize:false,
     mode: 'horizontal',  //水平
     cssWidthAndHeight: true
     });
     setInterval(function(){mySwiper.swipeNext()}, 3000);
     };
     }*/
    'checkTopLoop1':function(){
        var sideW=$('.swiper-slide').width()/5;

        if ($('#pic-loop .swiper-slide').length > 1){
            var mySwiper = new Swiper('#pic-loop',{
                loop: true,
                pagination: '.pagination',
                paginationClickable: false,
                //offsetPxBefore:sideW,
                visiblilityFullfit:true,
                autoResize:false,
                mode: 'horizontal',  //水平
                cssWidthAndHeight: true
            });
            setInterval(function(){mySwiper.swipeNext()}, 3000);
        };
        $('.pic-loop-box').css('padding-left','0px');
    }
}
action.toolsInfo();
//头图轮播end

//ipad端倒计时
function timeOut(parent,time){
    var parents=parent;
    var timeNum=time;
    var times=null;
    var daybox=parents.children('.day'),
        hourbox=parents.children('.hour'),
        minutebox=parents.children('.minute'),
        secondbox=parents.children('.second');
    function zero(num){
        if(num<10){
            return '0'+num;
        }else if(num>=10){
            return ''+num;
        }
    };
    times=setInterval(function(){
        checkTime();
    },1000);
    function checkTime(){
        var future= Date.parse(timeNum);
        var now=new Date();
        var nowTime=now.getTime();
        var mistiming=(future-nowTime)/1000,
            d=zero(parseInt(mistiming/86400)),
            h=zero(parseInt((mistiming)/3600)),
            f=zero(parseInt((mistiming%86400%3600)/60)),
            m=zero(parseInt(mistiming%86400%360%60));
        getTimes(d,h,f,m);
        if(future<=nowTime){
            clearInterval(times);
            setTimes();
            return false;
        };
    };
    function getTimes(d,h,f,m){
        daybox.children('code').eq(0).text(d.substr(0,1));
        daybox.children('code').eq(1).text(d.substr(1,1));
        hourbox.children('code').eq(0).text(h.substr(0,1));
        hourbox.children('code').eq(1).text(h.substr(1,1));
        minutebox.children('code').eq(0).text(f.substr(0,1));
        minutebox.children('code').eq(1).text(f.substr(1,1));
        secondbox.children('code').eq(0).text(m.substr(0,1));
        secondbox.children('code').eq(1).text(m.substr(1,1));
    };
    function setTimes(){
        daybox.children('code').eq(0).text(0);
        daybox.children('code').eq(1).text(0);
        hourbox.children('code').eq(0).text(0);
        hourbox.children('code').eq(1).text(0);
        minutebox.children('code').eq(0).text(0);
        minutebox.children('code').eq(1).text(0);
        secondbox.children('code').eq(0).text(0);
        secondbox.children('code').eq(1).text(0);
    }
};

/*倒计时end*/
// 手机端倒计时 开始
function countDown(time,id){
    var hour_elem = $(id).find('.hour'),
        minute_elem = $(id).find('.minute'),
        second_elem = $(id).find('.second'),
        end_time = new Date(time).getTime();
    var timer = setInterval(function(){
        var sys_second = (end_time-new Date().getTime()) / 1000;
        if (sys_second > 0) {
            var day = Math.floor((sys_second / 3600) / 24);//计算天
            var hour = Math.floor(sys_second / 3600 %24);//计算小时
            var minute = Math.floor(sys_second / 60 %60);//计算分
            var second = Math.floor(sys_second % 60);// 计算秒
            // $(day_elem).text(day<10?"0"+day:day);
            $(hour_elem).text(hour<10?"0"+hour:hour);
            $(minute_elem).text(minute<10?"0"+minute:minute);
            $(second_elem).text(second<10?"0"+second:second);
        } else {
            clearInterval(timer);
            $(hour_elem).text("00");
            $(minute_elem).text("00");
            $(second_elem).text("00");
        }
    }, 1000);
}
// 手机端倒计时 结束

// 手机端tab切换 开始
function tabCon(){
    var newTab = $('.newTab-box'),
        newTabSpan = newTab.find('span'),
        newTabCon = newTab.find('div.newTab-con>div');
    newTabSpan.on('touchstart',function(){
        var ind = $(this).index();
        $(this).addClass('active').siblings('span').removeClass('active');
        newTabCon.eq(ind).addClass('active').siblings('div').removeClass('active');

    })
}
tabCon();
// tab切换 结束

/*进度条*/
function progress(){
    $('.surplus').each(function(){
        var sw=$(this).width();
        var newCite = $(this).find('.sur-top');
        var oCite=$(this).find('.sur-top').width();
        var oNin=$(this).find('.sur-top-val');
        var len=sw*0.65;
        if(oCite >= 100){
            newCite.css({'border-radius':'1.2rem'});
        }
        if(oCite>len){
            oNin.addClass('progress-tiao');
        }else{
            oNin.removeClass('progress-tiao');
        }
    });
};
progress();
/*进度条end*/

//换一换1
function addclass(){
    var wp=$(window).width(),
        oUl=$('.big-box1').find('.tab-box1'),
        oli=$('.big-box1').find('.com-tab1'),
        len = oli.length,
        iNow=0,
        index=1;

    $.fn.imgFade = function (idx) {
        return this.each(function () {
            var _this=$(this);
            var iNow=0;
            var times=null;
            var comTab1=null;
            //换一换点击
            _this.on('touchstart', function(e){
                iNow++;
                if(iNow>2){
                    iNow=0;
                };
                //新
                _this.find('b').addClass('resh');
                var resh = setTimeout(function(){
                    _this.find('b').removeClass('resh');
                    showFn();
                }, 500);

                comTab1=$(this).parents('h2').siblings('.big-box1').find('.com-tab1');
                clearTimeout(times);
                function showFn(){
                    comTab1.eq(iNow).addClass('tabDis').siblings('.com-tab1').removeClass('tabDis');
                    times=setTimeout(function(){
                        comTab1.eq(iNow).addClass('tab-block').siblings('.com-tab1').removeClass('tab-block');
                    },20);
                }

                e.preventDefault();
            });
        });
    }
    $('.next-group1').imgFade();
}
addclass();
/*换一换end*/

/*关闭下载*/
function clsoeDownload(){
    $('.closeImg').on('touchstart',function(){
        $('.download').hide();
    })
}
clsoeDownload();
/*关闭下载end*/
/*function num(){
 var picListWidth=$('.picList').width()/2;
 $('.picList').css("margin-left",-picListWidth);
 alert(picListWidth)
 }
 num();
 轮播图点的位置*/
/*var slider=TouchSlide({ 
 slideCell:"#header",
 titCell:".picList ul", //开启自动分页 autoPage:true ，此时设置 titCell 为导航元素包裹层
 mainCell:".picBox ul",
 effect:"leftLoop",
 autoPage:true,//自动分页
 autoPlay:true,//自动播放
 endFun:function(i,c){
 //console.log(i);
 //$("#textarea").val( $("#textarea").val()+ "第"+(i+1)+"个效果结束，开始执行endFun函数。当前分页状态："+(i+1)+"/"+c+"\r\n")
 //$(i).addClass('noMb');
 }
 });新轮播*/

//详情页结构换位,首页俱乐部位置
function clubAppend(){
    var winW=$(window).width();
    if(winW>768){
        $(".sp-details").append($('.appTo'));
        $(".sp-details").append($('.details-btn'));
        $(".ipad-clubBox").show();
        $(".ipad-clubBox").append($('.page-top'));
        $(".huad-bg").css('background-size','55% 100%');
    }else{
        $(".ipad-clubBox").hide();
        $(".huad-bg").css('background-size','100% 100%');
    }
}
clubAppend();
//弹窗位置
function Alert(){
    var alertW=$('.dialog').width()/2;
    $('.dialog').css('margin-left',-alertW);

}
Alert();


//轮播图前后
var timer=setInterval(function loopEnd(){
    //判断最后一个是否是选中
    if($('.pic-loop-box>div:last').hasClass('swiper-slide-active')){
        $('.pic-loop-box>div:last').append("<dl></dl>");
    }else{
        $('.pic-loop-box>div:last dl').remove();
    }

    //判断第一个是否是选中
    if($('.pic-loop-box>div:first').hasClass('swiper-slide-active')){
        $('.pic-loop-box>div:first').append("<dl></dl>");
    }else{
        $('.pic-loop-box>div:first dl').remove();
    }


    var firstImg=$('.pic-loop-box>div:nth-child(3)').find('img').attr('src');
    $('.pic-loop-box>div:last-child dl').css({'background':'url('+firstImg+') no-repeat','background-size':'100% 100%'});
    var lastImg=$('.pic-loop-box>div:nth-last-child(3)').find('img').attr('src');
    $('.pic-loop-box>div:first-child dl').css({'background':'url('+lastImg+') no-repeat','background-size':'100% 100%'});
},0);


// 动态获取div class="scroll"宽度
function scrollW(){
    var scrollE = $(".scroll"),
        huadBoxrightLi = scrollE.find(".huad-boxright li"),
        liLen = huadBoxrightLi.length,
        liW = huadBoxrightLi.outerWidth(true),
        huadBoxleft = scrollE.find('.huad-boxleft').outerWidth() + 2,
        huadBoxright = scrollE.find('.huad-boxright'),
        huadBoxrightW = liLen *liW,
        huadBoxrightH = huadBoxrightLi.outerHeight();
    huadBoxright.css({'width':huadBoxrightW,'height':huadBoxrightH});
    scrollE.width(huadBoxleft + huadBoxright.width());

}
scrollW();

