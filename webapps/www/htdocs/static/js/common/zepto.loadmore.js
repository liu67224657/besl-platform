;(function($, window) {
	
	'use strict';

	var pluginName = 'loadmore',
		namespace = 'plugin_' + pluginName;

	var	defaults = {
		bottomBuffer: 0,
		debounceInt: 100,
		doneLoading: null,
		interval: 300,
		loadingClass: 'loading',
		loadingClassTarget: null,
		loadMoreDelay: 0,
		loadMore: $.noop,
		startingPageCount: 1,
		triggerInitialLoad: false
	};

	function Loadmore(element, options) {
		this.options = $.extend({}, defaults, options);
		
		this.$element = $(element);
		this.$win = $(window);
		this.$loadingClassTarget = this._getLoadingClassTarget();
		this.$scrollContainer = this._getScrollContainer();
		
		this.loading = false;
		this.doneLoadingInt = null;
		this.pageCount = this.options.triggerInitialLoad ? this.options.startingPageCount - 1 : this.options.startingPageCount;
		this.destroyed = false;

		this.init();
	}

	Loadmore.prototype = {
		
		init : function(){
			this._addListeners();
			if (this.options.triggerInitialLoad) {
				this._beginLoadMore(this.options.loadMoreDelay);
			} else {
				this._handleScroll();
			}
		},
		
		_getLoadingClassTarget : function() {
			return this.options.loadingClassTarget ? $(this.options.loadingClassTarget) : this.$element;
		},
		
		_getScrollContainer : function() {
			var $scrollContainer = null;

			if (this.$element.css('overflow-y') == 'scroll') {
				$scrollContainer = this.$element;
			}
			if (!$scrollContainer) {
				$scrollContainer = this.$element.parents().filter(function() { 
					return $(this).css('overflow-y') == 'scroll';
				});
			}
			$scrollContainer = $scrollContainer.length > 0 ? $scrollContainer : this.$win;
			return $scrollContainer;
		},
		
		_addListeners : function() {
			var self = this;

			this.$scrollContainer.on('scroll.' + pluginName, debounce(function() {
				self._handleScroll();
			}, this.options.debounceInt));
		},
		
		_removeListeners : function() {
			this.$scrollContainer.off('scroll.' + pluginName);
		},
		
		_handleScroll : function(e) {
			var self = this;

			if (this._shouldTriggerLoad()) {
				this._beginLoadMore(this.options.loadMoreDelay);
				if (this.options.doneLoading) {
					this.doneLoadingInt = setInterval( 
						function() {
							if (self.options.doneLoading(self.pageCount)) {
								self._endLoadMore();
							}
						}, 
						this.options.interval
					);
				}
			}
		},
		
		_shouldTriggerLoad : function() {
			var elementBottom = this._getElementHeight(),
				scrollBottom = this.$scrollContainer.scrollTop() + this.$scrollContainer.height() + this.options.bottomBuffer;

			return (!this.loading && scrollBottom >= elementBottom && this.$element.is(':visible'));
		},
		
		_getElementHeight : function() {
			if (this.$element == this.$scrollContainer) {
				return this.$element[0].scrollHeight;
			} else {
				return this.$element.height();
			}
		},
		
		_beginLoadMore : function(delay) {
			delay = delay || 0;

			setTimeout($.proxy(function() {
				this.pageCount++;
				this.options.loadMore(this.pageCount, $.proxy(this._endLoadMore, this));
				this.$loadingClassTarget.addClass(this.options.loadingClass);
			}, this), delay);

			this.loading = true;
			this._removeListeners();
		},
		
		_endLoadMore : function() {
			clearInterval(this.doneLoadingInt);
			this.loading = false;
			this.$loadingClassTarget.removeClass(this.options.loadingClass);
			!this.destroyed && this._addListeners();
		},
		
		destroy : function() {
			this._removeListeners();
			this.options.loadMore = null;
			this.options.doneLoading = null;
			//$.data(this.$element[0], namespace, null);
			clearInterval(this.doneLoadingInt);
			this.destroyed = true;
		}	
		
	};

	function callMethod(instance, method, args) {
		if ( instance && $.isFunction(instance[method]) ) {
			instance[method].apply(instance, args);
		}
	}

	function debounce(func, wait, immediate) {
		var timeout;

		return function() {
			var context = this, args = arguments;
			var later = function() {
				timeout = null;
				if (!immediate) func.apply(context, args);
			};
			var callNow = immediate && !timeout;
			clearTimeout(timeout);
			timeout = setTimeout(later, wait);
			if (callNow) func.apply(context, args);
		};
	}
	
	$.fn[pluginName] = function(options) {
		var method = false,
			methodArgs = arguments;

		if (typeof options == 'string') {
			method = options;
		}

		return this.each(function() {

			var plugin = $.data(this, namespace);

			if (!plugin && !method) {
				$.data(this, namespace, new Loadmore(this, options));
			} else if (method) {
				callMethod(plugin, method, Array.prototype.slice.call(methodArgs, 1));
			}
		});
	};

	window.Loadmore = window.Loadmore || Loadmore;

})(Zepto, window);