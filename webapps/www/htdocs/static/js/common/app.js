$(function(){
    var arr=[],box=$('#box');
    var timeTip=$('#wrapperBox>div');
    timeTip.each(function(){
        arr.push($(this).offset().top-50);
    })
    var h=$('.box').find('ul:last').height();
    $('.box').height(arr[arr.length-1]+h)
    $('#scrollBox').on('scroll',function(){
        var scrollTop_num=$(this).scrollTop();

        for(var i= 0,len=arr.length;i<len;i++){


            if(scrollTop_num>arr[i] && scrollTop_num<arr[i+1]){
                timeTip.eq(i).addClass('fixed')
            }else{
                timeTip.eq(i).removeClass('fixed')
            }

            if(scrollTop_num>arr[len-1]){
                timeTip.eq(len-1).addClass('fixed')
            }else{
                timeTip.eq(len-1).removeClass('fixed')
            }
        }

    })
})