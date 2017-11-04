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
// 动态获取div class="scroll"宽度
function scrollW(className){
     var scrollE =$('.'+className.name),
        huadBoxrightLi = scrollE.find(".huad-boxright li"),
        liLen = huadBoxrightLi.length,
        liW = huadBoxrightLi.outerWidth(true),
        huadBoxleft = scrollE.find('.huad-boxleft').outerWidth(true),
        huadBoxright = scrollE.find('.huad-boxright'),
        huadBoxrightW = liLen *liW,
        huadBoxrightH = huadBoxrightLi.find('a').outerHeight();
        huadBoxright.css({'width':huadBoxrightW,'height':huadBoxrightH});
      scrollE.width(huadBoxleft + huadBoxright.width()+2);
}
scrollW({name:'scroll-1'});
scrollW({name:'scroll-2'});

