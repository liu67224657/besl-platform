 if($('.details-btn>a').hasClass('a1')){
    $('.details-btn>a').on('touchstart',function(){
      $('.mark-box').show();
      $('.dialog').addClass('close');
    })
  }
    $('#Forno').on('touchstart',function(){
      $('.mark-box').show();
      $('.dialog').addClass('close');
    })
    $('#reserve').on('touchstart',function(){
      $('.mark-box').show();
      $('.dialog').addClass('close');
    })
  //header
 /* function headerShow(){
    $(document).scroll(function(){
      var top=$(this).scrollTop();
      if(top>0){
        $('#header').addClass('po')
      }else{
        $('#header').removeClass('po')
      }
    })
  }
  headerShow()*/
  //tab
  function tabShow(){
    $('.tab-tit').on('touchstart','a',function(){
      var index=$(this).index();
      $(this).addClass('cur').siblings().removeClass('cur');
      $('.tab-main>div').eq(index).addClass('cur-box').siblings().removeClass('cur-box')
    })
  }
  tabShow();
  //关注
  function checkAttention(num){
    var number=num;
    var initial_num=0;
    var timer=null;
    $('.add-attention-block').on('touchstart',function(){
      if($(this).find('span').hasClass('att2')){
          $(this).find('span').removeClass().addClass('att1');
            $(this).find('em').html('关注');
        initial_num--;
      }else{
        initial_num++;
        if(initial_num>number){
          $('.tip').text('亲，请先关注6个游戏，稍后随时可以修改').addClass('show');
           timer=setTimeout(function(){
           clearTimeout(timer);
           $('.tip').removeClass('show');
          },1000)
          initial_num=number
        }else{
          $(this).find('span').removeClass().addClass('att2');
            $(this).find('em').html('已关注');
        }
      }
       $('.interest-tit-num').html(initial_num)
      })
      if(initial_num===0){
        $('.finishBtn').on('touchstart',function(){
          $('.tip').text('亲，请至少关注1个游戏').addClass('show');
          timer=setTimeout(function(){
           clearTimeout(timer);
           $('.tip').removeClass('show');
          },1000)
        })
      }
      /*timer=setTimeout(function(){
       clearTimeout(timer);
       $('.tip').removeClass('show');
      },100)*/
  }
  checkAttention(6)
/*function textInput(){
  $('input').
}*/
 //新游开测 时间轴
 function timeBar(){
  var arr=[];
    var timeTip=$('.timeShaft-main-time');
    timeTip.each(function(){
        arr.push(parseInt($(this).offset().top)-30);
    })
  $(document).scroll(function(){
    var oTop=$(this).scrollTop();
    if(oTop>0){
      $('.timeShaft-tit').addClass('po1')
    }else{
      $('.timeShaft-tit').removeClass('po1')
    }
    for(var i= 1,len=arr.length;i<len;i++){
        if(oTop>arr[i] && oTop<arr[i+1]){
          var html=$('.timeShaft-main-block').eq(i-1).children('p').text();
          var classNames=$('.timeShaft-main-block').eq(i-1).children('p').css('background');
          $('.timeShaft-tit').children('p').text(html);
          $('.timeShaft-tit').children('p').css('background',classNames);
        }
    }
  }) 


 }
  timeBar()
 //轮播
  function checkTopLoop(){
    if ($('#pic-loop .swiper-slide').length > 2){
        var mySwiper = new Swiper('#pic-loop',{
            loop: true,
            pagination: '.pagination',
            paginationClickable: false,
            mode: 'horizontal',  //水平
            cssWidthAndHeight: true
        });
        setInterval(function(){mySwiper.swipeNext()}, 3000);
    }
    if ($('#hotNews-loop .swiper-slide').length > 2){
        var mySwiper2 = new Swiper('#hotNews-loop',{
            loop: false,
            paginationClickable: false,
            mode: 'horizontal',  //水平
            cssWidthAndHeight: true
        });
		function pageBtn(){
			/* 图片展示翻页 */
			document.getElementById('l-btn').addEventListener('touchstart', function(ev){
				ev.stopPropagation();
				ev.preventDefault();
				mySwiper2.swipePrev();
			}, false);
	
			/* 图片展示翻页 */
			document.getElementById('r-btn').addEventListener('touchstart', function(ev){
				ev.stopPropagation();
				ev.preventDefault();
				mySwiper2.swipeNext();
			}, false);
		}
    }
  }
  checkTopLoop();
//返回，收藏，分享
function handleToole(){
  var oTop=null;
  var w=$(window).width();
  var ImgHeight=w/(640/360);
  var sHeight=parseInt(ImgHeight-43);
  var allHandle={
	  'int':function(){
		  allHandle.hbCollect();
		  allHandle.hbShare();
		  },
	  //收藏
	  'hbCollect':function(hbReturn){
		var hbCollect=$('.hb-collect-btn');
		$(document).scroll(function(){
			oTop=$(window).scrollTop();
			if(oTop>=sHeight){
				hbCollect.addClass('on');
				hbCollect.css({
					'right':w-95+'px',
					'top':8+'px',
				})
				}else{
				hbCollect.removeClass('on');	
				hbCollect.removeAttr('style');
				}
			})
		},
	  //分享	
	  'hbShare':function(){
		var hbShare=$('.hb-share-btn'); 
		$(document).scroll(function(){
			oTop=$(window).scrollTop();
			if(oTop>=sHeight){
				hbShare.addClass('on');
				hbShare.css({
					'top':8+'px',
					'right':w-142+'px',
				});
				}else{
				hbShare.removeClass('on');	
				hbShare.removeAttr('style');
				}
			})
		  }
	}
  allHandle.int();
}
handleToole();