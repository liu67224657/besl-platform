$(document).ready(function() {
						   //perform actions when DOM is ready
  var z = 0; //for setting the initial z-index's
  var inAnimation = false; //flag for testing if we are in a animation
  
  $('#pictures img').each(function() { //set the initial z-index's
    z++; //at the end we have the highest z-index value stored in the z variable
    $(this).css('z-index', z); //apply increased z-index to <img>
  });
  var current = "p3";

  function swapFirstLast(isFirst) {
    if(inAnimation) return false; //if already swapping pictures just return
    else inAnimation = true; //set the flag that we process a image
    
    var processZindex, direction, newZindex, inDeCrease; //change for previous or next image
    
    if(isFirst) { processZindex = z; direction = '-'; newZindex = 1; inDeCrease = 1; } //set variables for "next" action
    else { processZindex = 1; direction = ''; newZindex = z; inDeCrease = -1; } //set variables for "previous" action
    
    $('#pictures img').each(function() { //process each image
      if($(this).css('z-index') == processZindex) { //if its the image we need to process
	  
        $(this).animate({ 'left' : direction + $(this).width() + 'px' }, 'slow', function() { //animate the img above/under the gallery (assuming all pictures are equal height)
          $(this).css('z-index', newZindex) //set new z-index
            .animate({ 'left' : '0' }, 'slow', function() { //animate the image back to its original position
              inAnimation = false; //reset the flag
            });
        });
      } else { //not the image we need to process, only in/de-crease z-index
        $(this).animate({ 'left' : '0' }, 'slow', function() { //make sure to wait swapping the z-index when image is above/under the gallery
          $(this).css('z-index', parseInt($(this).css('z-index')) + inDeCrease); //in/de-crease the z-index by one
        });
      }
    
	});
    
    return false; //don't follow the clicked link
  }
  
  var int;
  function go(){
  	int=window.setInterval(function(){
     swapFirstLast(true);
	},6000);
  }
  go();
  $('#next').click(function() {
	window.clearInterval(int);
    swapFirstLast(true); //swap first image to last position
    int=window.setInterval(function(){
     swapFirstLast(true);
	},6000);
  });
  
  $('#prev').click(function() {
	  window.clearInterval(int);
    swapFirstLast(false); //swap last image to first position
	int=window.setInterval(function(){
     swapFirstLast(true);
	},6000);
  });
  
  
	

});