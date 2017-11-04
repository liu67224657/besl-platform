/**
 * @author uonenet add by yongmingx
 * @date 2008-6-4
 * @fileoverview 消息提示组件
 * 主要包括：警告框，询问框，可自定义窗口大小，同时窗口大小自适应，可设定点击确定或取消（或关闭）后执行的函数。
 */
//获取对象
function $obj(id){
	return document.getElementById(id);
}
//获取对象的高度
function $height(obj){
	return parseInt(obj.style.height)||obj.offsetHeight;
}
//2007-9-14 判断文件路径，js自动获取并设定css路径
var __ymPrompt_page_scripts=document.getElementsByTagName("script");
var __ymPrompt_jsPath=__ymPrompt_page_scripts[__ymPrompt_page_scripts.length-1].src.substring(0,__ymPrompt_page_scripts[__ymPrompt_page_scripts.length-1].src.lastIndexOf("/")+1);
__ymPrompt_page_scripts=null;
var __ymPrompt_skin;	//保存用户设定皮肤
function ymPrompt(name){
	this._bgAlpha=0.3;	//遮罩透明度
	this._bgAlphaColor="#000000";
	this._insName=name||"(new ymPrompt())";
	this._ok=0;	//点击了确定返回1，默认为0
	this._icon=null;	//消息类型的图标
	this._startDrag=false;
	//高宽
	this._width=280;
	this._height=150;
	//点击确定或取消分别执行的函数
	this._okFunc=null;
	this._cancelFunc=null;
	//确定取消按钮,如果当前的doOK不能执行则还原doOK或doCancel，防止出现错误或意外
	//修改按钮类型为input（原来为a），便于控制且兼容性较好 2007-8-29
	this._okBtn="<input type='button' onclick='"+this._insName+".doOK()' id='okBtn' style='cursor:pointer' class='btnStyle' value=' 确 定 ' />";
	this._cancelBtn="<input type='button'onclick='"+this._insName+".doCancel()' id='cancelBtn' style='cursor:pointer' class='btnStyle' value=' 取 消 ' />";
	//设定button类型，0什么都不显示，1为确定，2为取消，3为两者都显示
	this.setBtnByType=["",this._okBtn,this._cancelBtn,this._okBtn+"&nbsp;&nbsp;"+this._cancelBtn];

	this._path=__ymPrompt_jsPath||""; //2007-9-14 根据js路径计算出css路径
	this._promptSkin=this._path+(__ymPrompt_skin||"default");	//用户设定皮肤或默认皮肤
	// 根据html Doctype获取html根节点，以兼容非xhtml的页面
	this._rootEl=document.compatMode=="CSS1Compat"?document.documentElement:document.body;	//根元素
	//页面的实际宽高
	this._docWidth=0;
	this._docHeight=0;
	//浏览器类型判断
	var browser=function(s){return navigator.userAgent.toLowerCase().indexOf(s)!=-1}; 
	this._isOpera=browser("opera"); 
	this._isIE=browser("msie")!=-1&&(document.all&&!this.isOpera);
	this._addSkin();	//向页面添加样式表
}
//添加样式表
ymPrompt.prototype._addSkin=function(){
	if($obj("promptCss")) return;	//只添加一次
	var __style = document.createElement('link');
	__style.href = this._promptSkin+"/style/ymPrompt.css";
	__style.id="promptCss";
	__style.rel = "stylesheet";
	__style.type = "text/css";
	document.getElementsByTagName('HEAD')[0].appendChild(__style);
	__style=null;
}

//设定消息框的宽度
ymPrompt.prototype.setWidth=function(w){
	if(/\d/.test(w)&&w>=this._width){this._width=w;}
};
//设定消息框的高度
ymPrompt.prototype.setHeight=function(h){
	if(/\d/.test(h)&&h>=this._height){this._height=h;}
};

//设定点击确定或取消时执行的函数的名称，tp=1：点击确定时执行，tp=2：点击取消或关闭执行
ymPrompt.prototype.setFunc=function(func,tp){
	if(tp==1){
		this._okFunc=func;
	}
	if(tp==2){
		this._cancelFunc=func;
	}
};
//设定图标类型，提示，询问，错误，确认。。
ymPrompt.prototype.setIco=function(icon){
	this._icon=icon;
};
//弹出窗口
ymPrompt.prototype.alert=function(message,title){
	this._init();
	this._createWin(title||"警告提示框",message,1);
};
//确认窗口
ymPrompt.prototype.confirm=function(message,title){
	this._init();
	this._createWin(title||"信息确认框",message,3);
};

//切换皮肤
ymPrompt.prototype.changeSkin=function(value){
	var tempPromptSkin=this._promptSkin.substring(0,this._promptSkin.lastIndexOf("/")+1)+value;
	var reg=new RegExp(this._promptSkin,"ig");
	try{
		$obj("promptWinContainer").innerHTML=$obj("promptWinContainer").innerHTML.replace(reg,tempPromptSkin)
	}catch(e){}
	$obj("promptCss").href=tempPromptSkin+"/style/ymPrompt.css";
	this._promptSkin=tempPromptSkin;
};

//弹出前的初始化
ymPrompt.prototype._init=function(){
	this._showMask();	//显示背景层
};

//关闭弹出框后的处理
ymPrompt.prototype._destory=function(){
	this._removeMask();	//隐藏背景层
	$obj("promptWinContainer").style.display="none";	//隐藏容器
};

/**
 * 拖动部分代码
 */
ymPrompt.prototype._setDrag=function(event){
	this._startDrag=true;
	this._startX=event.x||event.pageX;
	this._startY=event.y||event.pageY;
	this._containX=$obj("promptWinContainer").offsetLeft;
	this._containY=$obj("promptWinContainer").offsetTop;
	$obj("promptWinContainer").style.left=this._containX+"px";
	$obj("promptWinContainer").style.top=this._containY+"px";
	//由于弹出框居中使用了css hacks，所以在设定了其绝对的left和top后，之前的margin属性要清空。
	$obj("promptWinContainer").style.margin="0px";
	var insName=this._insName;
	var _self=this;
	//拖动
	this._addEvent("mousemove",function(){
		var event=window.event||arguments[0];	//修改获取event方法 2007-8-29
		if(_self._startDrag){
			try{	//有时随意拖动还是会保存，尽管不影响使用，先屏蔽掉
				$obj("promptWinContainer").style.left=(_self._containX+(event.x||event.pageX)-_self._startX)+"px";
				$obj("promptWinContainer").style.top=(_self._containY+(event.y||event.pageY)-_self._startY)+"px";
			}catch(e){}
		}
	});
	//取消拖动
	this._addEvent("mouseup",function(){eval(insName+"._startDrag=false")});
};

/**
 * 创建窗体
 * @param {Object} title 标题
 * @param {Object} content 内容
 * @param {Object} btnType 按钮类型，确定为1，取消为2，两者都有为3，都无为0
 */
ymPrompt.prototype._createWin=function(title,content,btnType){
	title=typeof(title)=="string"?title:"";	//2008-06-04 过滤非字符串标题
	//转换换行符和html标记
	//content=typeof(content)=="string"?content.replace(/\n/g,"<br>").replace(/<([^>]*)>/g,"&lt;$1&gt;"):""; haixiao 注释 解决换行	//2008-06-04 过滤非字符串标题，转换换行符
	//第一次需要创建一个容器
	//总容器的样式
	outerStyle="position:absolute;top:50%;left:50%;margin-left:"+(-this._width/2+this._rootEl.scrollLeft)+"px;width:"+this._width+"px;margin-top:"+(-this._height/2+this._rootEl.scrollTop)+"px;height:"+this._height+"px;z-index:10001";
	if(!$obj("promptWinContainer")){
		//标题容器层
		var title_div="<div style=\"cursor:move;width:100%;overflow:hidden\" onmousedown='"+this._insName+"._setDrag(event)' id=\"titleContainer\"><div style=\"float:left\" id=\"titleText\">&nbsp;</div><div style=\"float:right\" id=\"titleCtrl\"><img src='"+this._promptSkin+"/images/close.gif' border='0' valign='absmiddle' style='cursor:pointer' onclick='"+this._insName+".doCancel()' /></div></div>";
		//内容容器层
		var content_div="<div style='width:100%;overflow:visible' id='promptContent'><table cellpadding=0 cellspacing=0 border=0 style='margin:0px;padding:0px' align=center width='100%' height='100%' id='promptContentTable'><tr><td id='winMiddleLeft' width='3'>&nbsp;</td><td id='winMiddleCenter' style='background:url("+this._promptSkin+"/images/"+this._icon+") no-repeat 20px 50%'>&nbsp;</td><td id='winMiddleRight' width='3'>&nbsp;</td></tr>";
		if(btnType){	//显示的按钮的类型
			content_div+="<tr><td height='30' id='winBtnLineLeft'>&nbsp;</td><td align='center' id='winBtnLineCenter'>&nbsp;</td><td id='winBtnLineRight'>&nbsp;</td></tr>";
		}
		content_div+="<tr><td id='winBottomLeft' width='3'></td><td id='winBottomCenter'></td><td id='winBottomRight'></td></tr></table></div>";
		var outContainer=document.createElement("div");
		this._addCSS(outContainer,outerStyle);	//添加样式
		outContainer.id="promptWinContainer";
		outContainer.innerHTML=title_div+content_div;
		document.body.appendChild(outContainer);
		outContainer=null;	//清空不使用的内存

		//设定内容区的高度
		$obj("promptContent").style.height=(this._height-$obj("titleContainer").offsetHeight)+"px";
		//内容区的高度,对于xhtml页面必须有下面三行
		var contentHeight=$height($obj("promptContent"))-$height($obj("winBottomLeft"));
		if($obj("winBtnLineLeft")){contentHeight-=$height($obj("winBtnLineLeft"))}
		$obj("winMiddleCenter").style.height=contentHeight+"px";
	}
	//传入标题和内容
	$obj("titleText").innerHTML=title;	//标题
	$obj("winMiddleCenter").innerHTML=content;	//内容
	$obj("winBtnLineCenter").innerHTML=this.setBtnByType[btnType];	//更新按钮类型
	$obj("winMiddleCenter").style.backgroundImage="url("+this._promptSkin+"/images/"+this._icon+")";	//图标类型
	
	//显示消息容器
	this._addCSS($obj("promptWinContainer"),outerStyle);	//居中定位消息框
	$obj("promptWinContainer").style.display="";	//显示容器
	
	if(navigator.userAgent.indexOf("MSIE 7.0")>-1){	//For Only IE7
		$obj("titleContainer").style.width=$obj("promptContent").style.width=$obj("promptContent").style.height="0px";
		var _w=$obj("promptContentTable").offsetWidth;
		_w=(_w>parseInt($obj("promptWinContainer").style.width)?(_w+"px"):$obj("promptWinContainer").style.width);
		var _tHeight=$obj("titleContainer").offsetHeight;
		var _h=$obj("promptContentTable").offsetHeight+_tHeight;
		_h=(_h>parseInt($obj("promptWinContainer").style.height)?(_h+"px"):$obj("promptWinContainer").style.height);
		$obj("promptWinContainer").style.width=$obj("titleContainer").style.width=$obj("promptContent").style.width=_w;
		//$obj("promptWinContainer").style.height=_h; haixiao 注释 因为在IE7下弹出的提示窗口比提示内容高出一块.
		$obj("promptWinContainer").style.height=$obj("promptContentTable").offsetHeight;
		$obj("promptContent").style.height=parseInt(_h)-_tHeight+"px";
	}else{
		//2007-10-8 先清空原来的高宽值，根据内容设定内容区高宽。保持标题永远和内容同宽(内容可能会根据内容自动变宽)
		$obj("titleContainer").style.width=$obj("promptContent").style.width=$obj("promptContent").style.height="auto";
		$obj("titleContainer").style.width=$obj("promptContent").style.width=$obj("promptContentTable").offsetWidth+"px";
		$obj("promptContent").style.height=$obj("promptContentTable").offsetHeight+"px";
	}
	this.okTab=1;
	$obj("okBtn").focus();	//确定按钮获取焦点
	//第一次创建弹出框增加以下监听事件 
	if(!this.hasAddKeyEvent){	
		this._addEvent("keydown",this._listenKeydown);	//键盘按下事件
		var s=this;
		//重新计算遮罩大小，只有在遮罩显示的条件下才能执行此操作
		function resizeMask(){if($obj("promptShield")&&$obj("promptShield").style.display!="none") s._showMask()}
		this._addEvent("resize",resizeMask,window);
		this._addEvent("scroll",resizeMask,window);
		this.hasAddKeyEvent=1;
	}
};

//显示遮罩层
ymPrompt.prototype._showMask=function(){
	//第一次需要创建一个蒙板层
	if(!$obj("promptShield")){
		var shieldStyle="position:absolute;top:0px;left:0px;width:0;height:0;background:"+this._bgAlphaColor+";text-align:center;z-index:10000;filter:alpha(opacity="+(this._bgAlpha*100)+");opacity:"+this._bgAlpha+";";
		try{	//IE
			document.body.appendChild(document.createElement("<div id='promptShield' style=\""+shieldStyle+"\"></div>"));
			//为IE创建Iframe遮罩
			document.body.appendChild(document.createElement("<iframe id='promptShieldIframe'></iframe>"));
		}catch(e){
			var promptShield=document.createElement("div");
			promptShield.id="promptShield";
			promptShield.setAttribute("style",shieldStyle);
			document.body.appendChild(promptShield);
			promptShield=null;
		}
	}
	
	//计算蒙板的高宽，因为页面内容可能变化，所以每次弹出都应该更新宽高 
	$obj("promptShield").style.display="none";	// 如果显示则先隐藏便于后面计算页面的高宽
	var rootEl=this._rootEl;
	//使用scrollTop和scrollWidth判断是否有滚动条更加准确，但需要加上onscroll监听,一旦发现有scrollTop或scrollLeft则使用scrollWidth/Height
	this._docHeight=((rootEl.scrollTop==0)?rootEl.clientHeight:rootEl.scrollHeight)+"px";
	this._docWidth=((rootEl.scrollLeft==0)?rootEl.clientWidth:rootEl.scrollWidth)+"px";
	$obj("promptShield").style.width=this._docWidth;
	$obj("promptShield").style.height=this._docHeight;

	//2007-11-15 添加Iframe遮罩，仅在IE下才会存在Iframe遮罩
	var psIframe=$obj("promptShieldIframe");
	if(psIframe){
		this._addCSS(psIframe,$obj("promptShield").style.cssText+";z-index:9999;filter:alpha(opacity=0);opacity:0");
		psIframe.style.display="";
	}
	//显示蒙板
	$obj("promptShield").style.display="";			
	rootEl=null;	//清空引用，释放内存
	//禁止对页面的任何操作
	document.body.onselectstart = function(){return false};
	document.body.oncontextmenu = function(){return false};
};

//移除遮罩层
ymPrompt.prototype._removeMask=function(){
	//隐藏蒙板
	$obj("promptShield").style.display="none";
	if($obj("promptShieldIframe")){	//隐藏IE下创建的Iframe遮罩
		$obj("promptShieldIframe").style.display="none";
	}
	document.body.onselectstart = function(){return true};
	document.body.oncontextmenu = function(){return true};
};
//点击确定
ymPrompt.prototype.doOK=function(){
	this._ok=1;
	this._destory();
	//点击确定时执行的函数
	if(this._okFunc){
		if(typeof this._okFunc=="function"){
			this._okFunc();
		}else{
			eval(this._okFunc+"()");
		}
	}
};
//点击取消
ymPrompt.prototype.doCancel=function(){
	this._ok=0;
	this._destory();
	//点击取消或关闭时执行的函数
	if(this._cancelFunc){
		if(typeof this._cancelFunc=="function"){
			this._cancelFunc();
		}else{
			eval(this._cancelFunc+"()");
		}
	}
};

//为元素添加css文本样式的方法
//obj:要添加css的对象，css:css文本,append:追加还是覆盖，默认覆盖
ymPrompt.prototype._addCSS=function(obj,css,append){
	if(!append){
		this._isOpera?obj.setAttribute("style",css):obj.style.cssText=css;
	}else{
		this._isOpera?obj.setAttribute("style",obj.getAttribute("style")+css):obj.style.cssText+=css;
	}
}

//绑定事件的函数 2007-11-16封装到组件内部，防止与业务函数冲突
ymPrompt.prototype._addEvent=function(env,fn,obj){
	obj=obj||document;	//默认是document对象
	if(this._isIE){
		obj.attachEvent("on"+env,fn);
	}else{
		obj.addEventListener(env,fn,false);
	}
};

//弹出消息框时监听键盘事件 2007-11-16 封装到组件内部
ymPrompt.prototype._listenKeydown=function(){
	//无弹出框或弹出框隐藏则不屏蔽操作
	if(!$obj("promptWinContainer")||$obj("promptWinContainer").style.display=="none") return true;
	var ev=window.event||arguments[0];
	//2007-11-12 tab键切换选中按钮
	if(ev.keyCode==9){
		if(this.okTab&&$obj("cancelBtn")){
			$obj("cancelBtn").focus();
			this.okTab=0;
		}else{
			$obj("okBtn").focus();
			this.okTab=1;
		}
	}
	//2007-11-15 左右方向键切换焦点
	if(ev.keyCode==37){$obj("okBtn").focus();this.okTab=1};
	if(ev.keyCode==39&&$obj("cancelBtn")){$obj("cancelBtn").focus();this.okTab=0};
	if(ev.keyCode==13)return true;	//允许回车键
	//屏蔽所有键盘操作包括刷新等
	try{
		ev.keyCode=0;
		ev.cancelBubble=true;
		ev.returnValue=false;
	}catch(e){
		try{	//2007-11-13 避免IE下event.keycode=0执行出错后转向此处而报错，先暂时用try..catch解决吧
			ev.stopPropagation();
			ev.preventDefault();
		}catch(e){}
	}
}

/**
 * 提供简易的操作接口
 */
var tempPrompt;
//各消息框的相同操作
function initPromptInfo(a){
	// 页面加载完成才允许弹出，否则会报错
	if(!document.body){setTimeout(function(){initPromptInfo(a)},1);return}
	tempPrompt=tempPrompt||new ymPrompt("tempPrompt");
	tempPrompt.setWidth(a[0]);
	tempPrompt.setHeight(a[1]);
	tempPrompt.setFunc(a[2],1);
	tempPrompt.setFunc(a[3],2);
	tempPrompt.setIco(a[4]);
	a[7]?tempPrompt.confirm(a[5],a[6]):tempPrompt.alert(a[5],a[6]);
}
function Alert(message, width, height, title,func1,func2) {
	initPromptInfo([width,height,func1,func2,"info.gif",message,title||"消息提示框"]);
}
function SucceedInfo(message, width, height, title,func1,func2) {
	initPromptInfo([width,height,func1,func2,"right.gif",message,title||"成功信息框"]);
}
function ErrorInfo(errorMessage, width, height, title,func1,func2) {
	initPromptInfo([width,height,func1,func2,"err.gif",errorMessage,title||"错误信息框"]);
}
function ConfirmInfo(message, width, height, title,func1,func2) {
	initPromptInfo([width,height,func1,func2,"ask.gif",message,title||"信息确认框",1]);
}