<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ page import="ackgames.acquire.*"%>
 <%response.addHeader("Cache-Control","no-cache");%>
	
<HTML>
<HEAD>
  <TITLE>acquire</TITLE>

<script language="JavaScript">
var counter=30;
var refresh30=false;

var req=getHTTPObject() ;
var sreq=getHTTPObject() ;
function sub() {
	req.abort();
}

function getHTTPObject() {
  var xmlhttp;
  /*@cc_on
  @if (@_jscript_version >= 5)
    try {
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    } catch (e) {
      try {
      xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
      } catch (E) {
        xmlhttp = false;
      }
    }
  @else
  xmlhttp = false;
  @end @*/
  if (!xmlhttp && typeof XMLHttpRequest != 'undefined') {
    try {
      xmlhttp = new XMLHttpRequest();
    } catch (e) {
      xmlhttp = false;
    }
  }
  return xmlhttp;
}
function callback() {
    if (req.readyState == 4) {
        if (req.status == 200) {
            str=req.responseText;
           // req.abort();
            str= str.substring(str.indexOf("!")+1,str.lastIndexOf("!"));
            parseStates(str);
            asyncCom();
        }
	else{
            //    req.abort(); 
		timeID=setTimeout("asyncCom();",1000);
	}
    }
}
var gamestate="6";
function getGameState(){
	return gamestate;
}
function parseStates(str)
{
    h=document.getElementById("hack");
	states =str.split("~");
        for(i=0;i<states.length;i++){
		//alert(states[i]);
        } 
        val=states[0].split(":");
        gamestate=val[1];
        val=states[1].split(":");
        if(val.length>1){
  		tileState=val[1];
	}else{
  		tileState="";
        }

        val=states[2].split(":");
        rack=val[1];
        h.value=rack;

        val=states[3].split(":");
        stats=val[1];

        val=states[4].split(":");
        txt=val[1];

        val=states[5].split(":");
        if(val.length>1){
  		arg=val[1];
	}else{
  		arg="";
        }

        val=states[6].split("|");
        if(val.length>1){
  		chat=val[1];
	}else{
  		chat="";
        }

        setState2( txt,tileState,stats,rack,arg,gamestate,chat);
}
var rand=8;
function asyncCom() {
    var url = "acquireListener.jsp?id=" + rand++;
  	if (typeof XMLHttpRequest != 'undefined') {
			req=getHTTPObject() ;
  	}
    req.open("GET", url, true);
    req.onreadystatechange = callback;
    req.send(null);
}
function sendComment(cmd,arg) {
        var surl = "acquireSender.jsp?cmd="+cmd+"&arg="+arg+"&id=" + rand++;
  		if (typeof XMLHttpRequest != 'undefined') {
			sreq=getHTTPObject() ;
  		}
		sreq.open("GET", surl, true);
        sreq.onreadystatechange = handlehHttpSendChat;
  		sreq.send(null);
}
function  handlehHttpSendChat()
{
}
function updateTime(){
	counter--;
	if(counter==0){
   		obj = document.getElementById("command");	
   		obj.value="700";	
   		obj = document.getElementById("cmdForm");	
   		obj.submit();	
	} else{
		obj =top.frames[0].frames[1].document.getElementById("ref_sec");
		obj.value=counter + " Seconds";
		timeID=setTimeout("updateTime();",1000);
	}
}
</script>

<script language="JavaScript">
var state = new Array(10);
var hotel = new Array(7);
function getTileColor(c)
{
	switch(c)
	{
		case 'R':
			return "lightgray";
		case 'w':
			return "white";
		case 's':
			return "green";
		case 'd':
			return "black";
		case 'n':
			return "darkgray";
		case 'm':
			return "orange";
	}
	return state[c-'0'];
}
var v=1;
function setRack(rack,arg) {
	if (rack.length ==0) return;
	obj =top.frames[0].frames[1].document.getElementById("Start");
	if(obj.style.visibility!="hidden"){
	     return;
	}
	tok =rack.split(" ");
    if(arg.length!=0 ){
		for(i=0;i<6;i++){
			obj =top.frames[0].frames[1].document.getElementById("tile" +i);
			if(i<(tok.length-1) ){
				obj.style.visibility="visible";
			} else{
				obj.style.visibility="hidden";
			}
		}
		v++;
	}

 	for(i=1;i<tok.length;i++){ 
		j=i-1;
		objv =top.frames[0].frames[1].document.getElementById("tile" +j);
		if(objv.style.visibility=="visible"){
                	obj=top.frames[0].frames[1].document.getElementById("tileLayer"+tok[i]);
                	obj.style.backgroundColor ="#c0c0c0";
		}
		obj =top.frames[0].frames[1].document.getElementById("color");
		obj.value=tok[0];
		tc=getTileColor(tok[0].charAt(j));
		obj =top.frames[0].frames[1].document.getElementById("tile" +j);
		obj.innerHTML = 
		"<input type=\"button\" value=\""+tok[i]+"\" "+
		"STYLE =\"background-color:"+tc+"\""+
                " onMouseOver=\"mousein('"+tok[i]+"');\" "+
                " onClick=\"tileClick('"+tok[i]+"','tile"+j+"');\" "+
		"onMouseOut=\"mouseout('"+tok[i]+"');\" >";
	}
}
var price = new Array(7);
var holdings = new Array(7);
var avail = new Array(7);
var money = 0;
var currentPlayer=0;

function setStats(stats) {
           
	tok =stats.split(" ");
	for(i=0;i<7;i++) {
		t = i*3;
		obj =top.frames[1].document.getElementById("HotelAvailable"+i);
		obj.innerHTML = "<TD>" +tok[t]+"</TD>";
		obj =top.frames[1].document.getElementById("HotelCount"+i);
		obj.innerHTML = "<TD>" +tok[t+1]+"</TD>";
		obj.value = tok[t+1];
		obj =top.frames[1].document.getElementById("HotelPrice"+i);
		obj.innerHTML = "<TD>" +tok[t+2]+"</TD>";
		cnt = tok[t+1];
		price[i]=tok[t+2];
		avail[i]=tok[t];
		v= parseInt(cnt);
		if(v>10){
			cell =top.frames[1].document.getElementById("StockRow"+i);
			cell.style.backgroundColor = "orange";
		}
	}
	currentPlayer = tok[tok.length -1];
	curPlayer=currentPlayer;
	if(myId!=-1){
		curPlayer=myId;
	}
	playerCount= (tok.length-22)/9;
	for(i=0;i<playerCount;i++) {
		t = 21 + i*9;
		cell =top.frames[1].document.getElementById("StatRow"+i);
		if(i == currentPlayer){
			cell.style.backgroundColor = "#D0D0D0";
			cell.style.fontWeight="bold";
		} else {
			cell.style.fontWeight="normal";
			if(i%2 == 0){
				cell.style.backgroundColor = "white";
			} else {
				cell.style.backgroundColor = "#5092E4";

			}
		}
		obj =top.frames[1].document.getElementById("StatName"+i);
		obj.innerHTML = "<TD>" +tok[t]+"</TD>";
		obj =top.frames[1].document.getElementById("StatMoney"+i);
		obj.innerHTML = "<TD>" +tok[t+8]+"</TD>";
		if(curPlayer == i){
			money = parseInt(tok[t+8]);
			for(k=0;k<7;k++){
				holdings[k] = parseInt(tok[t+k+1]);
			}
		}
		for(j=0;j<7;j++){
			r = t+j+1;
			obj =top.frames[1].document.getElementById("StatHotel"+i+j);
			obj.innerHTML = "<TD>" +tok[r]+"</TD>";
		}

	}
}

function chooseHotel(h) {
                sendComment(110,h);
}
function buyHotels(h) {
                sendComment(103,h);
}
function swapStocks(h) {
                sendComment(107,h);
}
function chooseOrder(h) {
                sendComment(104,h);
}
var myObject= null;   
var switchHeader=0;
var myId=-1;
function gameState(st) {
	str="";
    	if(st == "9"){
                if(switchHeader !=9){
		    top.frames[0].frames[0].location= "chooseOrder2.jsp?x="+rand++;
		}
		switchHeader=9;
	}
        else if(st == "8"){
                myObject = new Object();
                myObject.price = price;
                myObject.money = money;
                myObject.holdings = holdings;
                myObject.avail = avail;
                myObject.currentPlayer = currentPlayer;
		myObject.doc =top.frames[1].document;
		top.frames[0].frames[0].location= "swap2.jsp?x="+rand++;
		switchHeader=8;
	}
	else if(st == "3"){
                myObject = new Object();
                myObject.price = price;
                myObject.money = money;
                myObject.holdings = holdings;
                myObject.avail = avail;
                myObject.currentPlayer = currentPlayer;
		myObject.doc =top.frames[1].document;
		myId=currentPlayer;
                if(switchHeader !=3){
		    top.frames[0].frames[0].location= "buyHotel2.jsp?x="+rand++;
		}
		switchHeader=3;
	}
	else if(st == "1"){
                if(switchHeader !=1){
			top.frames[0].frames[0].location= "pickHotel2.jsp?x="+rand++;
		}
		switchHeader=1;
	}else{
                if(switchHeader !=0){
		    top.frames[0].frames[0].document.location= "header.jsp?x="+rand++;
                } 
		switchHeader=0;
	}
}
function updateTop() {
		<%System.out.println("in gameState header");%>
}


function setGameText(str,obj,info) {
	if(str.length != 0){  
		ma=str.split("#");
		str="";
  		if (typeof XMLHttpRequest != 'undefined' &&info) {
           for(i=ma.length-1;i>=0;i--){
				if(i==ma.length-1){
					str=ma[i];
				}else{
					str=str+"\n"+ma[i];
				}
			}
  		}else{
			for(i=0;i<ma.length;i++){
				if(i==0){
					str=ma[i];
				}else{
					str=str+"\n"+ma[i];
				}
			}
		}
		if(obj.value.length == 0|| !info){  
			obj.value=str;
		} else{
  			if (typeof XMLHttpRequest != 'undefined') {
				obj.value= str +"\n" +obj.value ;
			}else{
				obj.value= obj.value +"\n"+ str;
				rng=obj.createTextRange();
				rng.scrollIntoView(false);
			}
		}
	}

}

function setTiles(str) {
	state[0]="red";
	state[1]="yellow";
	state[2]="#8787FF";
	state[3]="#B3AF91";
	state[4]="#80FF80";
	state[5]="cyan";
	state[6]="pink";
	state[7]="gray";
	state[8]="FDFCD8";
	state[9]="black";
	hotel[0]="Luxor";
	hotel[1]="Tower";
	hotel[2]="American";
	hotel[3]="WorldWide";
	hotel[4]="Festival";
	hotel[5]="Continetal";
	hotel[6]="Imperial";
	for(i =0;i<108;i++){
		r =Math.floor( i/12) +1;
		c = i%12 +1;
		s= "tileLayer"+c+"-";
		switch(r){
			case 1:
				s=s+"A";
				break;
			case 2:
				s=s+"B";
				break;
			case 3:
				s=s+"C";
				break;
			case 4:
				s=s+"D";
				break;
			case 5:
				s=s+"E";
				break;
			case 6:
				s=s+"F";
				break;
			case 7:
				s=s+"G";
				break;
			case 8:
				s=s+"H";
				break;
			case 9:
				s=s+"I";
				break;
		}
		st =str.substring(i,i+1); 	
		ist = parseInt(st);
		cell = top.frames[0].frames[1].document.getElementById(s);
		if (cell != null){
			cell.style.backgroundColor = state[st];
			if(st == 9){
				cell.style.color = "white";
			} else {
				cell.style.color = "black";
			}
		}
	}

}
function setState2( txt,tileState,stats,rack,arg,gs,chat) {
	debugger;
        obj =top.frames[0].frames[1].document.getElementById("info");
	setGameText(txt,obj,true);
	if (txt.indexOf("will start from")!=-1){
	    obj =top.frames[0].frames[1].document.getElementById("Start");
	    obj.style.visibility="hidden";
	}
	if(tileState.length ==0) return;
	setStats(stats);
	setTiles(tileState);
	setRack(rack,arg);
	gameState(gs);
	if(chat.length>0){
        	obj =top.frames[0].frames[1].document.getElementById("chatinfo");
		setGameText(chat,obj,true);
	}
    obj =top.frames[0].frames[0].document.getElementById("info3");
    if(obj!=null && obj!='undefined'){
		setGameText(txt,obj,false);
    }
}

</script>
</HEAD>
<BODY BGCOLOR="#5092E4" >
hi
        Type:<INPUT ID="hack" TYPE="TEXT" NAME="inmsg" VALUE = ""  >
<%!
	private BoardGenerator bg= new BoardGenerator();
	

	public void jspInit()
	{
		BoardGenerator.hackMain();
	}

 %>
</BODY>
</HTML>
