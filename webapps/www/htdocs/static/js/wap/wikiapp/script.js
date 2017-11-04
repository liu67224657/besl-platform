var mod={
    check:false,
    hrefs:[],
    checkAll:false,
    int:function(parents){
      mod.parents=parents;
      mod.gain();
      mod.remind();
      mod.removeLi();
      mod.removeNum(0);
      mod.allBtn();
    },
    //初始化获取a href值
    gain:function(){
      mod.hrefs=[];
      mod.parents.find('a').each(function(){
          mod.hrefs.push($(this).attr('href'));
      });
    },
    //编辑按钮
    remind:function(){
      $('.edit-btn').on('tap',function(){
        if(!mod.check){
          mod.parents.find('a').attr('href','javascript:;');
          mod.parents.find('a').addClass('slide');
          $(this).text('完成');
          $('.wiki-remind-bottom').addClass('slideTop');
          mod.check=true;
          mod.parents.find('li').removeClass('remove');
          $('.all-btn').text('全选');
          mod.checkAll=false;
          mod.parents.css('paddingBottom','4.2rem')
        }else{
          for(var i=0;i<mod.hrefs.length;i++){
            mod.parents.find('a').eq(i).attr('href',mod.hrefs[i]);
          }
          $(this).text('编辑');
          mod.parents.find('a').removeClass('slide');
          $('.wiki-remind-bottom').removeClass('slideTop');
          mod.check=false;
          mod.parents.css('paddingBottom','0')
        }
      });
    },
    //点击li
    removeLi:function(){
      mod.parents.find('li').on('tap',function(){
          if($(this).hasClass('remove')){
            $(this).removeClass('remove');
          }else{
            $(this).addClass('remove');
          }
        mod.removeNum($('.remove').length); 
        if($('.remove').length<mod.parents.find('li').length){
          mod.checkAll=false;
          $('.all-btn').text('全选');
        }
      });
    },
    //改变删除数字
    removeNum:function(lengths){
      $('.delete-btn').text('删除'+'('+lengths+')'); 
    },
    //全选按钮
    allBtn:function(){
      $('.all-btn').on('tap',function(){
        if(!mod.checkAll){
          mod.parents.find('li').addClass('remove');
          $(this).text('取消全选');
          mod.removeNum(mod.parents.find('li').length); 
          mod.checkAll=true;
        }else{
          mod.parents.find('li').removeClass('remove');
          $(this).text('全选');
          mod.removeNum(0); 
          mod.checkAll=false;
        }
      });
    },
    //删除
    deleteBtn:function(){
      $('.delete-btn').on('tap',function(){
        $('.remove').remove();
      })
    }
  };

function tab(){
    $('.wiki-tit>span').on('tap',function(){
      var index=$(this).index();
      $(this).addClass('active').siblings().removeClass('active');
      $('.wiki-cont>div').eq(index).addClass('show').siblings().removeClass('show');
      timeLine();
      $('body').animate({scrollTop:0},30,function(){
        $('.pf').removeClass('show');
      });
    })
  };
  tab();
  function timeLine(){
    var arr=[];
    var timeb=$('.show').find('.wiki-cont-tit')
    timeb.each(function(){
      arr.push(Math.round($(this).offset().top));
    })
    $(window).scroll(function(){
      var topS=$(this).scrollTop()+50;
      for(var i=0;i<arr.length;i++){
        if(topS>arr[i] && topS<arr[i+1]){
          var text=timeb.eq(i).text();
          $('.pf').addClass('show').text(text);
        }else if(topS<60){
          $('.pf').removeClass('show');
        }
      }
    })
  };
timeLine();