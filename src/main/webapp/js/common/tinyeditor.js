TINY={};

function T$(i){return document.getElementById(i)}
function T$$$(){return document.all?1:0}

var init_num = 1;
var exportWordName;
TINY.editor=function(){
	var c=[], offset=-30;
	c['cut']=[1,'剪切','a','cut',1];
	c['copy']=[2,'复制','a','copy',1];
	c['paste']=[3,'粘贴','a','paste',1];
	c['bold']=[4,'粗体','a','bold'];
	c['italic']=[5,'斜体','a','italic'];
	c['underline']=[6,'下划线','a','underline'];
	c['strikethrough']=[7,'删除线','a','strikethrough'];
	c['subscript']=[8,'下标','a','subscript'];
	c['superscript']=[9,'上标','a','superscript'];
	c['orderedlist']=[10,'插入有序列表','a','insertorderedlist'];
	c['unorderedlist']=[11,'插入无序列表','a','insertunorderedlist'];
	c['outdent']=[12,'减少缩进','a','outdent'];
	c['indent']=[13,'缩进','a','indent'];
	c['leftalign']=[14,'左对齐','a','justifyleft'];
	c['centeralign']=[15,'居中','a','justifycenter'];
	c['rightalign']=[16,'右对齐','a','justifyright'];
	c['blockjustify']=[17,'垂直居中','a','justifyfull'];
	c['undo']=[18,'回退','a','undo'];
	c['redo']=[19,'前进','a','redo'];
	c['image']=[20,'插入图片','i','insertimage','输入图片链接:','http://'];
	c['hr']=[21,'插入水平线','a','inserthorizontalrule'];
	c['link']=[22,'插入链接','i','createlink','输入链接:','http://'];
	c['unlink']=[23,'移除链接','a','unlink'];
	c['unformat']=[24,'移除格式化','a','removeformat'];
	c['print']=[25,'打印','a','print'];
	c['export']=[2,'导出word文档','a','export'];
	function edit(n,obj){
		this.n=n; window[n]=this; this.t=T$(obj.id); this.obj=obj; this.xhtml=obj.xhtml;
		var p=document.createElement('div'), w=document.createElement('div'), h=document.createElement('div'),
		l=obj.controls.length, i=0;
		this.i=document.createElement('iframe'); this.i.frameBorder=0;this.i.id='iframe-content'+init_num++;
		this.i.width=obj.width||'900'; this.i.height=obj.height||'600'; this.ie=T$$$();
		h.className=obj.rowclass||'teheader'; p.className=obj.cssclass||'te'; p.style.width=this.i.width+'px'; p.appendChild(h);
		for(i;i<l;i++){
			var id=obj.controls[i];
			if(id=='n'){
				h=document.createElement('div'); h.className=obj.rowclass||'teheader'; p.appendChild(h)
			}else if(id=='|'){
				var d=document.createElement('div'); d.className=obj.dividerclass||'tedivider'; h.appendChild(d)
			}else if(id=='font'){
				var sel=document.createElement('select'), fonts=obj.fonts||['Verdana','Arial','Georgia'], fl=fonts.length, x=0;
				sel.className='tefont'; sel.onchange=new Function(this.n+'.ddaction(this,"fontname")');
				sel.options[0]=new Option('字体','');
				for(x;x<fl;x++){
					var font=fonts[x];
					sel.options[x+1]=new Option(font,font)
				}
				h.appendChild(sel)
			}else if(id=='size'){
				var sel=document.createElement('select'), sizes=obj.sizes||[1,2,3,4,5,6,7], sl=sizes.length, x=0;
				sel.className='tesize'; sel.onchange=new Function(this.n+'.ddaction(this,"fontsize")');
				for(x;x<sl;x++){
					var size=sizes[x];
					sel.options[x]=new Option(size,size)
				}
				h.appendChild(sel)
			}else if(id=='style'){
				var sel=document.createElement('select'),
				styles=obj.styles||[['样式',''],['段落','<p>'],['标题 1','<h1>'],['标题 2','<h2>'],['标题 3','<h3>'],['标题 4','<h4>'],['标题 5','<h5>'],['标题 6','<h6>']],
				sl=styles.length, x=0;
				sel.className='testyle'; sel.onchange=new Function(this.n+'.ddaction(this,"formatblock")');
				for(x;x<sl;x++){
					var style=styles[x];
					sel.options[x]=new Option(style[0],style[1])
				}
				h.appendChild(sel)
			}else if(c[id]){
				var div=document.createElement('div'), x=c[id], func=x[2], ex, pos=x[0]*offset;
				div.className=obj.controlclass;
				div.style.backgroundPosition='0px '+pos+'px';
				div.title=x[1];
				ex=func=='a'?'.action("'+x[3]+'",0,'+(x[4]||0)+')':'.insert("'+x[4]+'","'+x[5]+'","'+x[3]+'")';
				div.onclick=new Function(this.n+(id=='export'?'.exportWord()':(id=='print'?'.print()':ex)));
				div.onmouseover=new Function(this.n+'.hover(this,'+pos+',1)');
				div.onmouseout=new Function(this.n+'.hover(this,'+pos+',0)');
				h.appendChild(div);
				if(this.ie){div.unselectable='on'}
			}
		}
		this.t.parentNode.insertBefore(p,this.t); this.t.style.width=this.i.width+'px';
		w.appendChild(this.t); w.appendChild(this.i); p.appendChild(w); this.t.style.display='none';
		if(obj.footer){
			var f=document.createElement('div'); f.className=obj.footerclass||'tefooter';
			if(obj.toggle){
				var to=obj.toggle, ts=document.createElement('div');
				ts.className=to.cssclass||'toggle'; ts.innerHTML=obj.toggletext||'源代码';
				ts.onclick=new Function(this.n+'.toggle(0,this);return false');
				f.appendChild(ts)
			}
			if(obj.resize){
				var ro=obj.resize, rs=document.createElement('div'); rs.className=ro.cssclass||'resize';
				rs.onmousedown=new Function('event',this.n+'.resize(event);return false');
				rs.onselectstart=function(){return false};
				f.appendChild(rs)
			}
			p.appendChild(f)
		}
		var htmlId = 'id="htmlContent"'+ 
					'xmlns:v="urn:schemas-microsoft-com:vml"'+
					'xmlns:o="urn:schemas-microsoft-com:office:office"'+
					'xmlns:w="urn:schemas-microsoft-com:office:word"'+
					'xmlns:m="http://schemas.microsoft.com/office/2004/12/omml"'+
					'xmlns="http://www.w3.org/TR/REC-html40"';
		var headMeta = '<meta charset="utf-8">'+
					'<meta http-equiv=Content-Type content="text/html; charset=unicode">'+
					'<meta name="description" content="Emulating real sheets of paper in web documents (using HTML and CSS)">'+
					'<!--[if gte mso 9]><xml>'+
					 '<w:WordDocument>'+
					  '<w:View>Print</w:View>'+
					  '<w:GrammarState>Clean</w:GrammarState>'+
					  '<w:TrackMoves>false</w:TrackMoves>'+
					  '<w:TrackFormatting/>'+
					 ' <w:ValidateAgainstSchemas/>'+
					  '<w:SaveIfXMLInvalid>false</w:SaveIfXMLInvalid>'+
					  '<w:IgnoreMixedContent>false</w:IgnoreMixedContent>'+
					  '<w:AlwaysShowPlaceholderText>false</w:AlwaysShowPlaceholderText>'+
					  '<w:DoNotPromoteQF/>'+
					  '<w:LidThemeOther>EN-US</w:LidThemeOther>'+
					  '<w:LidThemeAsian>ZH-CN</w:LidThemeAsian>'+
					  '<w:LidThemeComplexScript>X-NONE</w:LidThemeComplexScript>'+
					  '<w:Compatibility>'+
					   '<w:BreakWrappedTables/>'+
					   '<w:SnapToGridInCell/>'+
					   '<w:WrapTextWithPunct/>'+
					   '<w:UseAsianBreakRules/>'+
					   '<w:DontGrowAutofit/>'+
					   '<w:SplitPgBreakAndParaMark/>'+
					   '<w:DontVertAlignCellWithSp/>'+
					   '<w:DontBreakConstrainedForcedTables/>'+
					   '<w:DontVertAlignInTxbx/>'+
					   '<w:Word11KerningPairs/>'+
					   '<w:CachedColBalance/>'+
					   '<w:UseFELayout/>'+
					  '</w:Compatibility>'+
					  '<w:BrowserLevel>MicrosoftInternetExplorer4</w:BrowserLevel>'+
					  '<m:mathPr>'+
					   '<m:mathFont m:val="Cambria Math"/>'+
					   '<m:brkBin m:val="before"/>'+
					   '<m:brkBinSub m:val="--"/>'+
					   '<m:smallFrac m:val="off"/>'+
					  ' <m:dispDef/>'+
					   '<m:lMargin m:val="0"/>'+
					   '<m:rMargin m:val="0"/>'+
					   '<m:defJc m:val="centerGroup"/>'+
					   '<m:wrapIndent m:val="1440"/>'+
					   '<m:intLim m:val="subSup"/>'+
					   '<m:naryLim m:val="undOvr"/>'+
					  '</m:mathPr></w:WordDocument>'+
					'</xml><![endif]-->'+
					'<title>Sheets of Paper</title>';
		this.e=this.i.contentWindow.document; this.e.open();
		var m='<html '+htmlId+'><head>'+headMeta, bodyid=obj.bodyid?" id=\""+obj.bodyid+"\"":"";
		if(obj.cssfile){m+='<link rel="stylesheet" href="'+obj.cssfile+'" />'}
		if(obj.css){m+='<style type="text/css">'+obj.css+'</style>'}
		m+='</head><body'+bodyid+'>'+(obj.content||this.t.value);
		m+='</body></html>';
		this.e.write(m);
		this.e.close(); this.e.designMode='on'; this.d=1;
		if(this.xhtml){
			try{this.e.execCommand("styleWithCSS",0,0)}
			catch(e){try{this.e.execCommand("useCSS",0,1)}catch(e){}}
		};
	};
	edit.prototype.print=function(){
		this.i.contentWindow.print()
	},
	edit.prototype.exportWord=function(){
		var s = this.i.contentDocument.documentElement;
		if (this.obj.exportWordName) { // cjx 20190529 添加if else
			$(s).wordExport(this.obj.exportWordName);
		} else {
			$(s).wordExport(exportWordName);
		}
		
	},
	edit.prototype.hover=function(div,pos,dir){
		div.style.backgroundPosition=(dir?'34px ':'0px ')+(pos)+'px'
	},
	edit.prototype.ddaction=function(dd,a){
		var i=dd.selectedIndex, v=dd.options[i].value;
		this.action(a,v)
	},
	edit.prototype.action=function(cmd,val,ie){
		if(ie&&!this.ie){
			alert('Your browser does not support this function.')
		}else{
			this.e.execCommand(cmd,0,val||null)
		}
	},
	edit.prototype.insert=function(pro,msg,cmd){
		var val=prompt(pro,msg);
		if(val!=null&&val!=''){this.e.execCommand(cmd,0,val)}
	},
	edit.prototype.setfont=function(){
		execCommand('formatblock',0,hType)
	},
	edit.prototype.resize=function(e){
		if(this.mv){this.freeze()}
		this.i.bcs=TINY.cursor.top(e);
		this.mv=new Function('event',this.n+'.move(event)');
		this.sr=new Function(this.n+'.freeze()');
		if(this.ie){
			document.attachEvent('onmousemove',this.mv); document.attachEvent('onmouseup',this.sr)
		}else{
			document.addEventListener('mousemove',this.mv,1); document.addEventListener('mouseup',this.sr,1)
		}
	},
	edit.prototype.move=function(e){
		var pos=TINY.cursor.top(e);
		this.i.height=parseInt(this.i.height)+pos-this.i.bcs;
		this.i.bcs=pos
	},
	edit.prototype.freeze=function(){
		if(this.ie){
			document.detachEvent('onmousemove',this.mv); document.detachEvent('onmouseup',this.sr)
		}else{
			document.removeEventListener('mousemove',this.mv,1); document.removeEventListener('mouseup',this.sr,1)
		}
	},
	edit.prototype.toggle=function(post,div){
		if(!this.d){
			var v=this.t.value;
			if(div){div.innerHTML=this.obj.toggletext||'源代码'}
			if(this.xhtml&&!this.ie){
				v=v.replace(/<strong>(.*)<\/strong>/gi,'<span style="font-weight: bold;">$1</span>');
				v=v.replace(/<em>(.*)<\/em>/gi,'<span style="font-weight: italic;">$1</span>')
			}
			this.e.body.innerHTML=v;
			this.t.style.display='none'; this.i.style.display='block'; this.d=1
		}else{
			var v=this.e.body.innerHTML;
			if(this.xhtml){
				v=v.replace(/<span class="apple-style-span">(.*)<\/span>/gi,'$1');
				v=v.replace(/ class="apple-style-span"/gi,'');
				v=v.replace(/<span style="">/gi,'');
				v=v.replace(/<br>/gi,'<br />');
				v=v.replace(/<br ?\/?>$/gi,'');
				v=v.replace(/^<br ?\/?>/gi,'');
				v=v.replace(/(<img [^>]+[^\/])>/gi,'$1 />');
				v=v.replace(/<b\b[^>]*>(.*?)<\/b[^>]*>/gi,'<strong>$1</strong>');
				v=v.replace(/<i\b[^>]*>(.*?)<\/i[^>]*>/gi,'<em>$1</em>');
				v=v.replace(/<u\b[^>]*>(.*?)<\/u[^>]*>/gi,'<span style="text-decoration:underline">$1</span>');
				v=v.replace(/<(b|strong|em|i|u) style="font-weight: normal;?">(.*)<\/(b|strong|em|i|u)>/gi,'$2');
				v=v.replace(/<(b|strong|em|i|u) style="(.*)">(.*)<\/(b|strong|em|i|u)>/gi,'<span style="$2"><$4>$3</$4></span>');
				v=v.replace(/<span style="font-weight: normal;?">(.*)<\/span>/gi,'$1');
				v=v.replace(/<span style="font-weight: bold;?">(.*)<\/span>/gi,'<strong>$1</strong>');
				v=v.replace(/<span style="font-style: italic;?">(.*)<\/span>/gi,'<em>$1</em>');
				v=v.replace(/<span style="font-weight: bold;?">(.*)<\/span>|<b\b[^>]*>(.*?)<\/b[^>]*>/gi,'<strong>$1</strong>')
			}
			if(div){div.innerHTML=this.obj.toggletext||'可视化'}
			this.t.value=v;
			if(!post){
				this.t.style.height=this.i.height+'px';
				this.i.style.display='none'; this.t.style.display='block'; this.d=0
			}
		}
	},
	edit.prototype.post=function(){
		if(this.d){this.toggle(1)}
	};
	return{edit:edit}
}();

TINY.cursor=function(){
	return{
		top:function(e){
			return T$$$()?window.event.clientY+document.documentElement.scrollTop+document.body.scrollTop:e.clientY+window.scrollY
		}
	}
}();