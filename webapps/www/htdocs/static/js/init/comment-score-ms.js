
// JavaScript Document
$(function(){
	function winResize(){
		var h=$(window).height();
		var op=$('#opacity_bg');
		$('#opacity_bg').height(h);
		$('.icon_help').click(function(){
			$(this).parent().siblings('.con_pet_tips').show();
			op.show();
			op.css({'z-index':'28'});
			});
		$('.tips_colse').click(function(){
			$(this).parent().hide();
			op.hide();
			op.css({'z-index':'9'});
			})
	}
	//tab
	function tab(){
		$('.con_pet_menu span').each(function(i) {
			if(!$(".con_pet_main").eq(i).html()){
					$(this).hide();
				};
			$(this).click(function(){
				$(this).addClass("on").siblings('span').removeClass("on");
				$($(".con_pet_main")[i]).show().siblings('.con_pet_main').hide();
			})
		});
	}
	//列表页面
	function sel(){
		$('.select_sel').click(function(){
			$('.option_box').hide();
			$('.sel_tips').hide();
			$(this).siblings('.option_box').show();
			$(this).siblings('.sel_tips').show();
			$(this).siblings('.sel_tips').addClass('on');
			});
		$('.option_box div').click(function(){
			$(this).parent().siblings('.select_sel').text($(this).text());
			$(this).siblings('.sel_tips').show();
			$(this).parent().hide();
		});
		$('.prop_list b').click(function(){
			if($(this).hasClass('on')){
				$(this).removeClass('on');
				}else{
				$(this).addClass('on').parent().siblings().children('b').removeClass('on');	
			}
		});
	function Selopacity(){
		var h=$(window).height();
		var op=$('#opacity_bg');
		$('#opacity_bg').height(h);
		$('.search_more').click(function(){
			$('.select_Box').show();
			op.show();
			op.css({'z-index':'28'});
			});
		$('.sel_submit').click(function(){
			$('.select_Box').hide();
			op.hide();
			op.css({'z-index':'9'});
			});
		$('.sel_close').click(function(){
			window.location.reload();
			});
			var set_= $('.select_sel').width();
			$('.option_box').width(280);
		}
	$(window).resize(function() {
			Selopacity();
		});
	
		Selopacity();
	}
	//素材图鉴
	$('.material_list').click(function(){
		if(!$(this).hasClass('on')){
				$(this).addClass('on').siblings('.material_list').removeClass('on');
			}else{
				$(this).removeClass('on');
			}
	});
	//图鉴页
	$(".open_btn").click(function(){
		$('.pic_wrap,.pic_ban').addClass('on');
		$('.hide_btn').show();
	})
	$(".hide_btn").click(function(){
		$('.pic_wrap,.pic_ban').removeClass('on');
		$(this).hide();
	});
	//点赞字数增加
	$('.zan').click(function(){
		var zanNum = $('.zan span code').text();
		zanNum=parseInt(zanNum)+1;
		$(this).find('code').text(zanNum);
	});
	//拖动条
	$('#slider').css({
		'background-color':'#e3e3e3',
		'height': '5px',
		'border':'none'
		});
	$('#slider').parent().css({
		'float':'none',
		'padding':'15px 0'
		});
	$('#dslider').css({
		'height': '5px',
		'background-color':'#d82a00',
		'width':'0%'
		});
	$('#aslider').css({
		'width':'32px',
		'height':'33px',
		'background':'url(http://reswiki1.joyme.com/css/ms/wxwiki/default/1/images/drag_display.png) no-repeat 0 0',
		'background-size':'32px 33px',
		'border':'none',
		'margin-top':'-8px',
		'margin-left':'-15px',
		'left':'0%'
		});
	//悬浮菜单
	function floatMenu(){
		var h=$(window).height();
		var op=$('#opacity_bg');
		var fm=$('#float_menu');
		var oArr=$('#float_menu_btn');
		$('#opacity_bg').height(h);
		oArr.click(function(){
			if(fm.hasClass('')){
				fm.addClass('ation');
				op.show();
				oArr.html('<b>关</b><b>闭</b>');
			}else{
				fm.removeClass('ation');
				op.hide();	
				oArr.html('<b>菜</b><b>单</b>');
			}
		})
	}
	//重写拖动条
	function bindEvent(func){
		pchange = func;
	}
	function myonpchange() {
		bindEvent(function(){
			var tmpwidth;
			$('.plevelspan').each(function(i,v){
				$(v).attr('data-min')==''?1:$(v).attr('data-min');
				$(v).attr('data-min')==''?maxLevel:$(v).attr('data-max');
				ylv = lv;
				if($(v).attr('data-min')!='' && lv<=$(v).attr('data-min')){
					//$(v).html($(v).attr('data-min'));
					lv = $(v).attr('data-min');
				}else if($(v).attr('data-max')!='' && lv>=$(v).attr('data-max')){
					//$(v).html($(v).attr('data-max'));
					lv = $(v).attr('data-max');
				}
				// 修改此处--重新计算alv
				if($("#mswiki").html() != undefined){
					var ilv = parseInt(98*(parseInt(lv)-1)/(parseInt($("#maxLevel").val())-1)) + 1;
					alv = $('#data-lv'+ilv).val()?$('#data-lv'+ilv).val():ilv;
				}else{
					alv = $('#data-lv'+lv).val()?$('#data-lv'+lv).val():lv;
				}
				html = eval($(v).attr('data-method'));
				$(v).html(html);
				lv = ylv;
				
				if($(v).parent()[0].tagName.toUpperCase() == 'B'){
					tmpwidth=parseInt($(v).parent().attr('data-b-width'))+_x/10;
					tmpwidth=tmpwidth>=90?90:tmpwidth;
					$(v).parent().css({"width":tmpwidth+"%"});
				}
			});
		});
	}
	//20141111新增评分页面
	function grade(){
		var grade=true;
		var plNum=$('.pf_user b').text();
		$('.grade_icon cite').click(function(){
			$(this).addClass('on').prevAll('cite').addClass('on');
				$(this).nextAll('cite').removeClass('on');
			if(!$('.grade_icon cite:first-child').hasClass('on')){
				$('.grade_icon cite:first-child').removeClass('on');
			}else{
				$('.grade_icon cite:first-child').addClass('on');
				}
		});
		$('.pf_btn').click(function(){
//			plNum=parseInt(plNum)+1;
//			$('.pf_user b').text(plNum);
//			if(grade){
//			   $('.pf_btn').text("修改");
//			   $('#opacity_bg,.pf_tips').show();
//			   $('#opacity_bg').css({
//				   'z-index':'20'
//				   });
//			   $('.pf_tips').css({
//				   'z-index':'21'
//				   })
//			   grade=false;
//			}else{
//			   $('.pf_btn').text("评分");
//			   grade=true;
//			};
		});
		$('.pf_close').click(function(){
			 $('#opacity_bg,.pf_tips').hide();
			 $('#opacity_bg,.pf_tips').css({
				   'z-index':'9'
			    });
		});
		var varNum=0;
		$('.pic_comment span').click(function(){
			var countNum=$(this).children('code').text();
			varNum=parseInt(countNum)+1
			$(this).children('code').text(varNum);
		})
	}
	//活动页面
	function course(){
			$('.course_tit').click(function(){
				if(!$(this).parent().hasClass('on')){
					$(this).parent().addClass('on').siblings('.course_Box').removeClass('on');
					}else{
					$(this).parent().removeClass('on');	
					}
				})
			}
		function ShowCountDown() 
		{ 
			var nowDate = new Date(nowtime).getTime();
			$('.data-timer-div').each(function(i,v){
				
				var startDate = new Date($(v).attr('data-timer-start')).getTime();
				var endDate = new Date($(v).attr('data-timer-end')).getTime();
				
				if(nowDate<startDate && (startDate-nowDate)/3600000<24){
					var spanstr = '<b>开始倒计时</b>';
					var leftTime=startDate-nowDate; 
					if($(v).parent().attr('id') !== 'action_off'){
						$(v).appendTo($('#action_off .con_pet_listMain'));
						$(v).addClass('start');
					}
				}else if(startDate<nowDate && nowDate<endDate){
					var spanstr = '<b>结束倒计时</b>';
					var leftTime=endDate-nowDate; 
					if($(v).parent().attr('id') !== 'action_on'){
						$(v).appendTo($('#action_on .con_pet_listMain'));
						$(v).removeClass('start');
					}
				}else{
					$(v).hide();
					$(v).addClass('start');
					return true;
				}
				var leftsecond = leftTime/1000;
				var hour=Math.floor(leftsecond/3600); 
				var minute=Math.floor((leftsecond-hour*3600)/60); 
				var second=Math.floor(leftsecond-hour*3600-minute*60); 
				var bhour = '';
				if(hour>=100){
					bhour = '<i>'+Math.floor(hour/100)+'</i>';
					hour = Math.floor(hour/10);
				}
				$(v).find('.data-timer').html(spanstr+bhour+"<i>"+Math.floor(hour/10)+"</i><i>"+(hour%10)+"</i>:<i>"+Math.floor(minute/10)+"</i><i>"+(minute%10)+"</i>:<i>"+Math.floor(second/10)+"</i><i>"+(second%10)+"</i>");
				$(v).show();
			});
			var tday = new Date((nowDate/1000+1)*1000);
			nowtime = tday.getFullYear()+'/'+(tday.getMonth()+1)+'/'+tday.getDate()+' '+tday.getHours()+':'+tday.getMinutes()+':'+tday.getSeconds();
		}
		var nowtime = '2015/01/01 00:00:00';
		function activeTime(){
			$.ajax({
				url : "http://hezuo.joyme.com/api/time.php",
				type : "get",
				async:false,
				dataType : "jsonp",
				jsonpCallback:"timecallback",
				success : function(req) {
					nowtime = req[0].time;
					window.setInterval(function(){ShowCountDown();}, 1000);
				},
				error:function() {
					alert('获取失败，请刷新');
				}
			});
		}
	$(window).resize(function() {
			winResize();
			floatMenu()
		});
	activeTime();
	winResize();
	floatMenu()
	sel();
	course();
	tab();
	myonpchange();
	grade();
})