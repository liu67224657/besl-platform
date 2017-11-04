// JavaScript Document
var giftcenter={
	//调用	
	int:function(){
		//搜索
		giftcenter.jsearch();
		giftcenter.lb_slider({
				oUl:'.max-pic-list',
				oMenu:'.min-pic-list',
				direction:'top',//方向
				isNum:330,
				autoTime:3000  //自动播放的时间	
			});
		giftcenter.controlImg({
			parentBox:$('.games-jt-cont'),
			btnLeft:$('.games-jt-left'),
			btnRight:$('.games-jt-right'),
			num:1
		});
		var len=$('.ele-lb-slider .lb-ul').find('li').length;
		if(len>1){
			giftcenter.controlImg({
				parentBox:$('.ele-lb-list'),
				btnLeft:$('.ele-next'),
				btnRight:$('.ele-prev'),
				num:1
			});
		};
		giftcenter.clickBig({
				parentUl:$('.games-jt-cont-ul'),
				popupImg:$('.popupImg')
			});
		giftcenter.liHeight({
			ulc:'joyme-lbzx-box',
			showC:'show'
		});
		giftcenter.getTop({returnBtn:'.retuen-top'});
		giftcenter.lazyImg({img:'img.lazy'});
		giftcenter.bigNav({parentBox:'.joyme-header',navbtnParent:'.joyme-nav-ul',navbtn:'li'});
	},
	//搜索
	jsearch:function(){
		var txt_search=$("#txt_search");
		var inp_search_btn=$('.inp-search-btn');
		var val =null;
		txt_search.focus(function(){
				$(this).val('');	
			});
			txt_search.blur(function(){
				if($(this).val()==''){
					$(this).val('输入您想要的内容');
				}
			});
		function search(){
			val = txt_search.val();
			val = $.trim(val);
			if (val != null && val.length > 0 && val != '找找你感兴趣的') {
				window.open("http://www.baidu.com/s?wd=intitle:" + val + " 着迷网 inurl:joyme", "_blank");
			} else {
				 //todo error 
			};
		};
		txt_search.on("keydown",function(e){
			var keyCode=0,e=e||event; 
			if(txt_search.val()==='输入您想要的内容'){
			}else{
				if (e.keyCode == 13) {
					search();
				}
			}
		});
		inp_search_btn.on('click',function(){
			if(txt_search.val()==='输入您想要的内容'){
			}else{
				search();
			}
		});
	},
	//轮播图
	lb_slider:function(config){
		var direction=config.direction;
		var isNum=config.isNum;
		var oLi=$(config.oMenu).find('li');
		var iNow=0;
		var defaultTit=oLi.first().find('a').attr('title');
		$('.max-title').find('b').text(defaultTit);
		function movePic(index){
			var tit=oLi.eq(index).find('a').attr('title');
			iNow=index;
			if(direction=='top'){
				$(config.oUl).stop().animate({top:-(index*isNum)+'px'},500);
				oLi.eq(index).addClass('on').siblings().removeClass();
				$('.max-title').find('b').text(tit);
			};
		};
		function autoSlider(){
			iNow++;
			if(iNow==3){
				iNow=0;
			}
			movePic(iNow);
		};
		oLi.each(function(index){
			$(this).mouseover(function(){
				movePic(index);
			});
		});
		var times=setInterval(autoSlider,config.autoTime);
		$(config.oUl,config.oMenu).on('mouseenter',function(){
			clearInterval(times);	
		});
		$(config.oUl).on('mouseenter',function(){
			clearInterval(times);	
		});
		$(config.oUl).on('mouseleave',function(){
			times=setInterval(autoSlider,config.autoTime);
		});
		$(config.oUl,config.oMenu).on('mouseleave',function(){
			times=setInterval(autoSlider,config.autoTime);
		});
	},
	//礼包内页
	controlImg:function(conf){
		var parentBox=conf.parentBox;
		parentBox.on('mouseenter',function(){
			$(this).addClass('hover')
		});
		parentBox.on('mouseleave',function(){
			$(this).removeClass('hover')
		});
		parentBox.find('li').on('mouseenter',function(){
			$(this).addClass('hover')
		});
		parentBox.find('li').on('mouseleave',function(){
			$(this).removeClass('hover')
		});
		var width=parentBox.find('li:first').outerWidth(true),
			length=parentBox.find('li').size(),
			num=conf.num,
			index=0;
			move();
		parentBox.find('ul').width(width*length);
		var len=parseInt(parentBox.find('ul').width()/(width*num));
		conf.btnRight.on('click',nextBox);
		function nextBox(){
			index++;
			if(index>=len){
				index=len-1;
			}else{
				move(index)
			}
		};
		conf.btnLeft.on('click',prevBox);
		function prevBox(){
			index--;
			if(index<=0){
				index=0;
			}
			move(index)
		};
		function move(){
			if(index===0){
				conf.btnRight.addClass('show');
				conf.btnLeft.removeClass('show');
			}else if(index===len-1){
				conf.btnRight.removeClass('show');
				conf.btnLeft.addClass('show');
			}else{
				conf.btnLeft.addClass('show');
				conf.btnRight.addClass('show');
			}
			parentBox.find('ul').stop().animate({'marginLeft':(-width*num)*index},500)
		};
		return this;
	},
	//点击大图
	clickBig:function(conf){
      var parentUl=conf.parentUl;
      var popupImg=conf.popupImg;
      parentUl.on('click','li',function(){
      	popupImg.children('cite').html('');
      	$(this).find('img').clone().appendTo(popupImg.children('cite'));
      	var w=$(this).find('img').width();
      	var h=$(this).find('img').height();
      	console.log(w+'+'+h)
      	popupImg.show();
      	popupImg.children('cite').css({'width':w,'height':h,'margin-top':-h/2+'px','margin-left':-w/2+'px'});
      });
      popupImg.on('click',function(){
      	$(this).hide();
      })
      return this;
	},
	//返回顶部
	getTop:function(conf){
		var w=$(window).width();
		var topBtn=$(conf.returnBtn);
		var innerW=$('.joyme-center').width();
		var _left=null;
		var resizeTimer = null;
		$(window).on('resize', function () {
				if (resizeTimer) {
					clearTimeout(resizeTimer)
				}
				resizeTimer = setTimeout(function(){
					w=$(window).width();
					if(w>innerW){
						_left=(w-innerW)/2+innerW;
						topBtn.css({'left':_left+20});
					}
				},500);
			}
		);
		_left=(w-innerW)/2+innerW;
		topBtn.css({'left':_left+20});
		intFun();
		function intFun(){
			var wTop=$(window).scrollTop();
			if(wTop>=250){
				topBtn.show();
			}else{
				topBtn.hide();
			}
		};	
		$(window).scroll(function(){
			intFun();
		});
		topBtn.find('.rt-btn').on('click',function(){
			$('body,html').animate({scrollTop:0},500);
		});
	},
	//礼包
	liHeight: function(config){
		$('.'+config.ulc).find('li').hover(function(){
			$(this).addClass(config.showC).siblings().removeClass(config.showC);
		})
	},
	//图片预加载
	lazyImg:function(config) {
		$.fn.lazyImg = function(options) {
			var defaults = {
				attr: "data-url",
				container: $(window),
				callback: $.noop
			};
			var params = $.extend({}, defaults, options || {});
			params.cache = [];
			$(this).each(function() {
				var node = this.nodeName.toLowerCase(), url = $(this).attr(params["attr"]);
				//重组
				var data = {
					obj: $(this),
					tag: node,
					url: url
				};
				params.cache.push(data);
			});
			
			var callback = function(call) {
				if ($.isFunction(params.callback)) {
					params.callback.call(call.get(0));
				}
		};
		//动态显示数据
		var loading = function() {
			var contHeight = params.container.height();
			if (params.container.get(0) === window) {
				contop = $(window).scrollTop();
			} else {
				contop = params.container.offset().top;
			}		
			
			$.each(params.cache, function(i, data) {
				var o = data.obj, tag = data.tag, url = data.url, post, posb;
				
				if (o) {
					post = o.offset().top - contop, posb = post + o.height();
					if ((post >= 0 && post < contHeight) || (posb > 0 && posb <= contHeight)) {
						if (url) {
							//在浏览器窗口内
							if (tag === "img") {
								//图片，改变src
								callback(o.attr("src", url));		
							} else {
								o.load(url, {}, function() {
									callback(o);
								});
							}		
						} else {
							// 无地址，直接触发回调
							callback(o);
						}
						data.obj = null;	
					}
				}
			});	
		};
		loading();
		params.container.on("scroll", loading);
		};
		$(config.img).lazyImg();
	},
	//导航
	bigNav:function(config){
		var $parentBox=$(config.parentBox);
		var $navbtnParent=$(config.navbtnParent);
		var $lis=$navbtnParent.children(config.navbtn);
		$lis.each(function(){
		   if($(this).find('div').length){
		   		$(this).on('mouseover',function(){
		   			$(this).addClass('on');
		   			$parentBox.stop().animate({'height':100},300);
		   			$(this).children('a').addClass('on');
		   		});
		   		$(this).on('mouseout',function(){
		   		   	$(this).removeClass('on');
		   			$parentBox.stop().animate({'height':60},300);
		   			$(this).children('a').removeClass('on');
		   		});
		   }
		});
		if($('.joyme-loginbar').children('.after').length){
			$('.joyme-search-bar').width(200);
		}
     }
};
giftcenter.int();