/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-16
 * Time: 下午4:07
 * To change this template use File | Settings | File Templates.
 */
define(function(){return function($){
(function($) {
    $.fn.AutoHeight = function(options) {
                    var defaults = {
                maxHeight:null,
                minHeight:$(this).height()
            };
        var opts = (typeof (options) === 'object') ? $.extend({}, defaults, options) : {};
		this.each(function() {
                $(this).bind("paste cut keydown keyup focus blur", function() {
                    var height,style = this.style;
                    this.style.height = opts.minHeight + 'px';
                    if (this.scrollHeight > opts.minHeight) {
                        if (opts.maxHeight && this.scrollHeight > opts.maxHeight) {
                            height = opts.maxHeight;
                            style.overflowY = 'scroll';
                        } else {
                            if ('\v' == 'v') {
                                height = this.scrollHeight - 2;
                            } else {
                                height = this.scrollHeight;
                            }
                            style.overflowY = 'hidden';
                        }
                        style.height = height + 'px';
                    }
                });
		});
		return this;
	};
}(jQuery));
}});