/****************************************************
 *	 Author:	Fason(????)[??????]					*
 *	 Version:v1.0									*
 *  You may use this code on a public web site only *
 *  this entire copyright notice appears unchanged  *
 *  and you clearly display a link to				*
 *  http://fason.nease.net/                         *
 *                                                  *
 *                                                  *
 *     Please send questions and bug reports to:    *
 *             pufoxin@hotmail.com					*
****************************************************/
var icon={
		root		:'/static/images/tree/root.gif',
		open		:'/static/images/tree/open.gif',
		close		:'/static/images/tree/close.gif',
		file		:'/static/images/tree/close.gif',
		join		:'/static/images/tree/T.gif',
		joinbottom	:'/static/images/tree/L.gif',
		plus		:'/static/images/tree/Tplus.gif',
		plusbottom	:'/static/images/tree/Lplus.gif',
		minus		:'/static/images/tree/Tminus.gif',
		minusbottom	:'/static/images/tree/Lminus.gif',
		blank		:'/static/images/tree/empty.gif',
		line		:'/static/images/tree/I.gif'
}

function PreLoad(){
	for(i in icon){
	var tem=icon[i]
	icon[i]=new Image()
	icon[i].src=tem
	}
}
PreLoad()

function TreeView(obj,target,ExpandOne){
	this.obj=obj;
	this.Tree=new Node(-1)
	this.Root=null
	this.Nodes=new Array()
	this.target=target?target:"_blank";
	this.ie=document.all?1:0;
	this.ExpandOne=ExpandOne?1:0
}

function Node(id,pid,txt,link,title,target,xicon){
	this.Index=null;
	this.id=id;
	this.pid=pid
	this.parent=null
	this.txt=txt?txt:"New Item"
	this.link=link
	this.title=title?title:this.txt
	this.target=target
	this.xicon=xicon
	this.indent=""
	this.hasChild=false;
	this.lastNode=false;
	this.open=false
}

TreeView.prototype.add=function(id,pid,txt,link,title,target,xicon){
	target=target?target:this.target
	var nNode=new Node(id,pid,txt,link,title,target,xicon);
	if(pid==this.Tree.id)nNode.open=true;
	nNode.Index=this.Nodes.length
	this.Nodes[this.Nodes.length]=nNode
}

TreeView.prototype.InitNode=function(oNode){
	var last;
	for(i=0;i<this.Nodes.length;i++){
		if(this.Nodes[i].pid==oNode.id){this.Nodes[i].parent=oNode;oNode.hasChild=true}
		if(this.Nodes[i].pid==oNode.pid)last=this.Nodes[i].id
	}
	if(last==oNode.id)oNode.lastNode=true
}

TreeView.prototype.DrawLine=function(pNode,oNode){
		oNode.indent=pNode.indent+(oNode.pid!=this.Tree.id&&oNode.pid!=this.Root.id?("<img align='absmiddle' src='"+(pNode.lastNode?icon.blank.src:icon.line.src)+"'>"):'')
}

TreeView.prototype.DrawNode=function(nNode,pNode){
	var str=""
	var indents=""
	var nid=nNode.id
	var nIndex=nNode.Index;
	this.DrawLine(pNode,nNode)
	var togglevar=".Toggle(";
	if(nid == "-11"){
		togglevar = ".AllToggle(";
	}
	if(nNode.hasChild){
    	indents=nNode.indent+"<a href='javascript:void(0)' onclick='"+this.obj+togglevar+nIndex+",this);return false'>"+(this.Tree.id!=nNode.pid?("<img align='absmiddle' src='"+(nNode.lastNode?(nNode.open?icon.minusbottom.src:icon.plusbottom.src):(nNode.open?icon.minus.src:icon.plus.src))+"' id='icon"+nid+"' border=0>"):"")+"</a>"
	}else{
	  indents=nNode.indent+(nNode.pid==this.Tree.id?'':("<img align='absmiddle' src='"+(nNode.lastNode?icon.joinbottom.src:icon.join.src)+"'>"))
	}  
	indents+="<img onclick='"+this.obj+togglevar+nIndex+",this)' id='folder"+nid+"' align='absmiddle' src='"+(nNode.xicon?nNode.xicon:(nNode.hasChild?(nNode.open?icon.open.src:icon.close.src):icon.file.src))+"'>"
	str+="<div class='node'><nobr>"+indents+this.DrawLink(nNode)+"</nobr></div>"
	if(nNode.hasChild){
	  str+="<div id='Child"+nid+"' style='display:"+(nNode.open?"":"none")+"'>"
	  str+=this.DrawTree(nNode)
	  str+="</div>"
	}
	return str;
}

TreeView.prototype.DrawTree=function(pNode){
	var str=""
	for(var i=0;i<this.Nodes.length;i++)
		if(this.Nodes[i].pid==pNode.id)
		str+=this.DrawNode(this.Nodes[i],pNode)
	return str
}

TreeView.prototype.DrawLink=function(oNode){
	var nIndex=oNode.Index;
	var togglevar=".Toggle(";
	if(oNode.id == "-11"){
		togglevar = ".AllToggle(";
	}
	var str=""
	str+=" <span id='NodeItem"+oNode.id+"' tabindex='1' title='"+oNode.title+"' onclick='"+this.obj+togglevar+nIndex+",this);" +this.obj+".Select(this)'>"+(oNode.link?("<a href='"+oNode.link+"' target='"+oNode.target+"'>"+oNode.txt+"</a>"):oNode.txt)+"</span>"
	return str
}


TreeView.prototype.toString=function(){
	var str=""
	for(var i=0;i<this.Nodes.length;i++){
	if(!this.Root)
	if(this.Nodes[i].pid==this.Tree.id)this.Root=this.Nodes[i]
	this.InitNode(this.Nodes[i])
	}
	str+=this.DrawTree(this.Tree)
	return str
}

TreeView.prototype.toRootMenuString=function(){
	var str=""
	for(var i=0;i<this.Nodes.length;i++){
		str+="<li><nobr><a href='common/leftMenu.jsp?menunoKey="+this.Nodes[i].id +"' target='" 
				 + this.Nodes[i].target +"'>"
				 + this.Nodes[i].txt +"</a></nobr></li>";
	}
	return str
}

TreeView.prototype.clickRootMenu=function(id){
	for(var i=0;i<this.Nodes.length;i++){
		if(this.Nodes[i].id == id ){
			//document.getElementById("rootMenuA_"+this.Nodes[i].id).style.color="#FF0000";
		}else{
			document.getElementById("menu_td_"+this.Nodes[i].id).className="";
			document.getElementById("menu_img_"+this.Nodes[i].id).src="/static/images/B_tri_left.gif";
			document.getElementById("menu_a_"+this.Nodes[i].id).className="menu_title";
		}
	}
}

TreeView.prototype.Toggle=function(nIndex,o){
	var nNode=this.Nodes[nIndex]
	o.blur();
	if(!nNode.hasChild)return;
	if(nNode.open)this.Collapse(nNode)
	else this.Expand(nNode)
}

TreeView.prototype.AllToggle=function(nIndex,o){
	o.blur();
	if(this.Nodes[nIndex].open){
	  for(var i=0;i<this.Nodes.length;i++){
		if(i!=nIndex){
			if(!this.Nodes[i].hasChild)continue;
			this.Expand(this.Nodes[i]);
		}
	  }
	  this.Nodes[nIndex].open=false;
	}else{
	  for(var i=0;i<this.Nodes.length;i++){
		if(i!=nIndex){
		    if(!this.Nodes[i].hasChild)continue;
			this.Collapse(this.Nodes[i]);
		}
	  }
	  this.Nodes[nIndex].open=true;
	}
	
}

TreeView.prototype.Expand=function(nNode){
	var nid=nNode.id
	var node=this.GetElm('Child'+nid)
	var oicon=this.GetElm('icon'+nid)
	node.style.display=''
	var img1=new Image()
	img1.src=(nNode.lastNode?icon.minusbottom.src:icon.minus.src)
	if(oicon)oicon.src=img1.src
	if(!nNode.xicon){
		var img2=new Image()
		img2.src=icon.open.src
		this.GetElm("folder"+nid).src=img2.src
	}
	if(this.ExpandOne)this.CloseOtherItem(nNode);
	nNode.open=true
}

TreeView.prototype.Collapse=function(nNode){
	var nid=nNode.id
	var node=this.GetElm('Child'+nid)
	var oicon=this.GetElm('icon'+nid)
	node.style.display='none'
	var img1=new Image()
	img1.src=(nNode.lastNode?icon.plusbottom.src:icon.plus.src)
	if(oicon)oicon.src=img1.src
	if(!nNode.xicon){
		var img2=new Image()
		img2.src=icon.close.src	
		this.GetElm("folder"+nid).src=img2.src
	}
	nNode.open=false
}

TreeView.prototype.ExpandAll=function(){
	for(i=0;i<this.Nodes.length;i++)
		if(this.Nodes[i].hasChild)this.Expand(this.Nodes[i])
}

TreeView.prototype.CollapseAll=function(){
	for(i=0;i<this.Nodes.length;i++)
		if(this.Nodes[i].hasChild&&this.Nodes[i].pid!=this.Tree.id&&this.Nodes[i]!=this.Root)this.Collapse(this.Nodes[i])
}

TreeView.prototype.CloseOtherItem=function(nNode){
	for(var i=0;i<this.Nodes.length;i++)
		if(this.Nodes[i].pid==nNode.pid&&this.Nodes[i].open){this.Collapse(this.Nodes[i]);break}
}
TreeView.prototype.Select=function(objNode,flag){
	if(!this.current)this.current=objNode
	this.current.className=""
	objNode.className="highlight"
	this.current=objNode
	var a=objNode.getElementsByTagName("A")
	if(a.length!=0&&flag)window.open(a[0].href,a[0].target);
	if(this.ie)objNode.focus()
}

TreeView.prototype.Select2=function(objNode,flag){
	if(!this.current)this.current=objNode
	this.current.className=""
	objNode.className="highlight"
	this.current=objNode
	var a=objNode.getElementsByTagName("A")
	//if(a.length!=0&&flag)window.open(a[0].href,a[0].target);
	if(this.ie)objNode.focus()
}

TreeView.prototype.openTo=function(nIndex){
	if(nIndex<0||nIndex>=this.Nodes.length)return;
	var nNode=this.Nodes[nIndex]
	while(nNode){
		if(!nNode.open&&nNode.hasChild)this.Expand(nNode)
		nNode=nNode.parent
	}
	this.Select2(this.GetElm("NodeItem"+this.Nodes[nIndex].id),true)
}

TreeView.prototype.GetElm=function(uid){
	return document.getElementById(uid)
}

