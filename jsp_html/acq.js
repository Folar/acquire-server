
function clicky(x) {
   index=-1;
   if (!document.getElementById) return;
   sts = getGameState();	
   if(sts=="0" ){
        obj = document.getElementById(x);	
        if( obj.style.backgroundColor =="#c0c0c0"||obj.style.backgroundColor =="rgb(192, 192, 192)"){
	   		str=x.substring(9);
            for(i=0;i<6;i++){
				obj = document.getElementById("tile" +i);
                if(obj.childNodes[0].value==str){
			        tileClick(str,"tile"+i);
			        break;
		        }
	    	}
		}else{
	    	alert("Unable to  play this tile !!!!");
		}
		return;
   }
   if(sts!="3"){
		alert("Unable to buy hotel at this time!!");
		return;
   }
   str="";
   obj = document.getElementById(x);	
   if( obj.style.backgroundColor =="red"){
        index=0;
	str="Luxor";
   } else if( obj.style.backgroundColor =="yellow"){
        index=1;
	str="Tower";
   } else if( obj.style.backgroundColor =="#8787ff"||obj.style.backgroundColor =="rgb(135, 135, 255)"){
        index=2;
        str="American";
   } else if( obj.style.backgroundColor =="#b3af91"||obj.style.backgroundColor =="rgb(179, 175, 145)"){
        index=3;
        str="WorldWide";
   } else if( obj.style.backgroundColor =="#80ff80"||obj.style.backgroundColor =="rgb(128, 255, 128)"){
        index=4;
        str="Festival";
   } else if( obj.style.backgroundColor =="cyan"){
        index=5;
        str="Continental";
   } else if( obj.style.backgroundColor =="pink"){
        index=6;
        str="Imperial";
   }
   if(index!=-1){
        obj = document.getElementById("h"+index);	
  	if(obj==null){
  		alert("Unable to buy "+str+ " at this time");
  		return;
    }
    incrHotel(index);
   }/*else{
	alert("Unable to buy hotel by clicking this tile");
   }*/	
}
function selectDate(x) {
   if (!document.getElementById) return;
	alert(x);
   obj=document.getElementById(x);
    if( obj.style.backgroundColor =="red"){
   		obj.style.backgroundColor="yellow";
	} else {
   		obj.style.backgroundColor="red";
	}
}

function tileClick(x,layer) {
   if (!document.getElementById) return;
   r = document.getElementById("color");	
   if(r.value.length == 0){
		return;
   }	
   sts = getGameState();	
   if(sts!="0"){
		alert("unable to play tile at this time !");
		return;
   }
   obj=document.getElementById(layer);
   index =parseInt(layer.substring(4));	
   if(r.value.substring(index,index+1) == "n"){
		alert("unable to play tile at this time");
		return;
   }	
   obj.style.visibility="hidden";	
   r.value="";
   //obj.submit();
   //alert(x);
   sendComment(102,x);
}
function sub() {
    obj=document.getElementById("inmsg");
    str = obj.value;
   if(str.length == 0){
		alert("must not be blank");
		return false;
   }
   obj.value="";
      sendComment(0,str);
   return false;
}
function startG() {

   if (!document.getElementById) return;
   obj=document.getElementById("Start");
   obj.style.visibility="hidden";	
   sendComment(100,"na");
}
var ts;
function mousein(x) {
   if (!document.getElementById) return;
   obj=document.getElementById("tileLayer"+x);
   ts=obj.style.backgroundColor; 
   obj.style.backgroundColor ="#a0a0a0";
}
function mouseout(x) {
   if (!document.getElementById) return;
   obj=document.getElementById("tileLayer"+x);
   obj.style.backgroundColor =ts;
}

function signIn() {


   sendComment(111,"na");
   asyncCom();
}
function initGame(){
                              
   $.get('game',{command:3},function(responseText) { 
        $('#stats2').html(responseText); 
        $.get('game',{command:4},function(responseText) { 
          $('#stats1').html(responseText);
           $.get('game',{command:5},function(responseText) { 
               $('#toppart').html(responseText); 
                signIn();  
            });       
      });        
    });         
      
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
        var h = document.getElementById("hack");
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
  $.ajax({
      type: "get",
      data: { command:2},
      cache: false,
      url: "game" ,
      dataType: "text",
      error: function(request,error) 
      {
          timeID=setTimeout(asyncCom,1000);
      },
      success: function ( str)
      {
       
        parseStates(str);
        asyncCom();
      }});
}


      


function sendComment(cmd,arg) {
  $.ajax({
    type: "get",
    data: { command:1, boardCommand:cmd,arg:arg},
    cache: false,
    url: "game" ,
    dataType: "text",
    error: function(request,error) 
    {
      debugger;
     
    },
    success: function ( )
    {
 
    }});
     
}

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
function startH(h) {
   chooseHotel(h);
}
var v=1;
function setRack(rack,arg) {
  if (rack.length ==0) return;
  obj = document.getElementById("Start");
  if(obj.style.visibility!="hidden"){
       return;
  }
  tok =rack.split(" ");
    if(arg.length!=0 ){
    for(i=0;i<6;i++){
      obj = document.getElementById("tile" +i);
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
    objv =document.getElementById("tile" +j);
    if(objv.style.visibility=="visible"){
                  obj=document.getElementById("tileLayer"+tok[i]);
                  obj.style.backgroundColor ="#c0c0c0";
    }
    obj =document.getElementById("color");
    obj.value=tok[0];
    tc=getTileColor(tok[0].charAt(j));
    obj =document.getElementById("tile" +j);
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
           
  var tok =stats.split(" ");
  for(i=0;i<7;i++) {
    t = i*3;
    obj = document.getElementById("HotelAvailable"+i);
    obj.innerHTML = "<TD>" +tok[t]+"</TD>";
    obj = document.getElementById("HotelCount"+i);
    obj.innerHTML = "<TD>" +tok[t+1]+"</TD>";
    obj.value = tok[t+1];
    obj = document.getElementById("HotelPrice"+i);
    obj.innerHTML = "<TD>" +tok[t+2]+"</TD>";
    cnt = tok[t+1];
    price[i]=tok[t+2];
    avail[i]=tok[t];
    v= parseInt(cnt);
    if(v>10){
      cell = document.getElementById("StockRow"+i);
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
    cell = document.getElementById("StatRow"+i);
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
    obj = document.getElementById("StatName"+i);
    obj.innerHTML = "<TD>" +tok[t]+"</TD>";
    obj = document.getElementById("StatMoney"+i);
    obj.innerHTML = "<TD>" +tok[t+8]+"</TD>";
    if(curPlayer == i){
      money = parseInt(tok[t+8]);
      for(k=0;k<7;k++){
        holdings[k] = parseInt(tok[t+k+1]);
      }
    }
    for(j=0;j<7;j++){
      r = t+j+1;
      obj = document.getElementById("StatHotel"+i+j);
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
      var str="";
      if(st == "9"){
        if(switchHeader !=9){
         $.get('game',{command:9},function(responseText) { 
               $('#toppart').html(responseText); 
            });
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
        myObject.doc =document;
              $.get('game',{command:8},function(responseText) { 
               $('#toppart').html(responseText); 
            });
        switchHeader=8;
      }
      else if(st == "3"){
        myObject = new Object();
        myObject.price = price;
        myObject.money = money;
        myObject.holdings = holdings;
        myObject.avail = avail;
        myObject.currentPlayer = currentPlayer;
        myObject.doc = document;
        myId=currentPlayer;
        if(switchHeader !=3){
          $.get('game',{command:7},function(responseText) { 
               $('#toppart').html(responseText); 
            });
          
        }
        switchHeader=3;
      }
      else if(st == "1"){
        if(switchHeader !=1){
           $.get('game',{command:6},function(responseText) { 
               $('#toppart').html(responseText); 
            });
        }
        switchHeader=1;
      }else{
        if(switchHeader !=0){
            $.get('game',{command:5},function(responseText) { 
               $('#toppart').html(responseText);   
            });
        } 
        switchHeader=0;
      }
}



function setGameText(str,obj,info) {
  if(str.length != 0){  
    var ma=str.split("#");
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
    cell = document.getElementById(s);
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
  var obj = document.getElementById("info");
  setGameText(txt,obj,true);
  if (txt.indexOf("will start from")!=-1){
      obj = document.getElementById("Start");
      obj.style.visibility="hidden";
  }
  if(tileState.length ==0) return;
  setStats(stats);
  setTiles(tileState);
  setRack(rack,arg);
  gameState(gs);
  if(chat.length>0){
          obj =
          document.getElementById("chatinfo");
    setGameText(chat,obj,true);
  }
    obj = document.getElementById("info3");
    if(obj!=null && obj!='undefined'){
  
      setGameText(txt,obj,false);
    }
}
var arg;

function getCost()
{
  var cost=0;
    for(i=0;i<7;i++){
    obj =document.getElementById("h" +i);
    if(obj !=null){
      var amt = obj.selectedIndex;
        if(amt >0) {
        cost += amt * parseInt(myObject.price[i]);
      }
    }
  }
  return cost;
}
function incrHotel(i) {
  obj =document.getElementById("h"+i);
  sel = obj.selectedIndex;
  len= obj.length;
  if(sel<len-1){
     obj.selectedIndex++;
     calc(i);
  }
}
function calc(h) {
  var cnt =0;
  var m_total=0;
  for(i=0;i<7;i++){
    var obj =document.getElementById("h" +i);
    if(obj !=null){
      var amt = obj.selectedIndex;
        if(amt >0) {
            cnt +=amt;
      }
        }
  }
  if(cnt>3 ){
    for( var i=0;i<7;i++){
      if(i!=h){
        obj =document.getElementById("h" +i);
        if(obj !=null){
          amt = obj.selectedIndex;
          if(amt >0) {
            x= cnt -3;
            if (x>=amt){
              cnt = cnt-amt;
              obj.selectedIndex=0;
            }else{
              cnt =cnt-x;
              obj.selectedIndex=amt-x;
            }
          }
        }
      }
        }
    }
  var x=getCost();
  while(x> myObject.money){
      for(i=0;i<7;i++){
      if(i!=h){
        obj =document.getElementById("h" +i);
        if(obj !=null){
          amt = obj.selectedIndex;
          if(amt >0) {
            obj.selectedIndex=amt-1;
            break;
          }
        }
      }
        }
      x=getCost();
  }
  m= myObject.money-x;
    obj = myObject.doc.getElementById("StatMoney"+myObject.currentPlayer);  
    obj.innerHTML = "<TD>" +m+"</TD>";
    for(i=0;i<7;i++){
    obj =document.getElementById("h" +i);
    if(obj !=null){
      amt = obj.selectedIndex;
      av = myObject.avail[i] - amt;
        obj = myObject.doc.getElementById("HotelAvailable"+i); 
      obj.innerHTML = "<TD>" +av+"</TD>";
      av = myObject.holdings[i] + amt;
        str = "StatHotel"+myObject.currentPlayer;  
        obj = myObject.doc.getElementById(str+i);  
      obj.innerHTML = "<TD>" +av+"</TD>";
      
    }
  }
}


function buy(h) {
  debugger;
  cnt =0;
  for(i=0;i<7;i++){
    obj =document.getElementById("h" +i);
    if(obj !=null){
      amt = obj.selectedIndex;
        if(amt >0) {
        cnt++;
      }
    }
  }
  str = ""+h;
  cost =0;
  a=0;
  for(i=0;i<7;i++){
    obj =document.getElementById("h" +i);
    if(obj !=null){
      amt = obj.selectedIndex;
        if(amt >0) {
        str += i;
        str +=amt;
        cost += amt * parseInt(myObject.price[i]);
        a+=amt;
      }
    }
  }
  if(myObject.money<cost || a>3){
    alert("Unreasonable purchase");
    return;
  }
    buyHotels(str);
}

function calcTrade() {
    keep =document.getElementById("keep");
    sell =document.getElementById("sell");
    trade =document.getElementById("trade");
  total =keep.length-1;
    x=calcTotal();
  if(x> total){
    if(keep.selectedIndex>=(x-total)){
      keep.selectedIndex=keep.selectedIndex - (x-total);
    }else{
      keep.selectedIndex=0;
    }
    x=calcTotal();
    if(x> total){
      sell.selectedIndex=sell.selectedIndex - (x-total);
    }
  }else{
    keep.selectedIndex=keep.selectedIndex + (total-x);
  }
  updateStats();

}
function calcSell() {
    keep =document.getElementById("keep");
    sell =document.getElementById("sell");
    trade =document.getElementById("trade");
  total =keep.length-1;
    x=calcTotal();
  if(x> total){
    if(keep.selectedIndex>=(x-total)){
      keep.selectedIndex=keep.selectedIndex - (x-total);
    }else{
      keep.selectedIndex=0;
    }
    x=calcTotal();
    if(x> total){
      if((2*trade.selectedIndex - (x-total))%2==1){
        keep.selectedIndex=1;
      }
      trade.selectedIndex=(2*trade.selectedIndex - (x-total))/2;
    }
  }else{
    keep.selectedIndex=keep.selectedIndex + (total-x);
  }
  updateStats();
}
function calcKeep() {
    keep =document.getElementById("keep");
    sell =document.getElementById("sell");
    trade =document.getElementById("trade");
  total =keep.length-1;
  x=calcTotal();
  if(x> total){
    if(sell.selectedIndex>=(x-total)){
      sell.selectedIndex=sell.selectedIndex - (x-total);
    }else{
      sell.selectedIndex=0;
    }
    x=calcTotal();
    if(x> total){
      if((2*trade.selectedIndex - (x-total))%2==1){
        sell.selectedIndex=1;
      }
      trade.selectedIndex=(2*trade.selectedIndex - (x-total))/2;
    }
  }else{
    sell.selectedIndex=sell.selectedIndex + (total-x);
  }
  updateStats();
}
function updateStats(){
  trader =document.getElementById("trader");
  t = trader.value;
        //alert("trader ="+trader.value);

  sur =document.getElementById("survivor");
  surValue = sur.value;
  def =document.getElementById("defunct");
  defValue = def.value;

        //set the defunct hotel
    str = "StatHotel"+t
    obj = myObject.doc.getElementById(str+defValue); 
      keep =document.getElementById("keep");
  obj.innerHTML = "<TD>" +keep.selectedIndex+"</TD>";

        //set the suvivor hotel
    obj = myObject.doc.getElementById(str+surValue); 
        trade =document.getElementById("trade");
  v=trade.selectedIndex+ myObject.holdings[surValue];
  obj.innerHTML = "<TD>" +v+"</TD>";

  // set the money
        sell =document.getElementById("sell");
  w =document.getElementById("worth");
  cost = sell.selectedIndex * parseInt(w.value);
  m= myObject.money+cost;
        obj = myObject.doc.getElementById("StatMoney"+t);  
        obj.innerHTML = "<TD>" +m+"</TD>";
  av = myObject.avail[surValue] - trade.selectedIndex;
    obj = myObject.doc.getElementById("HotelAvailable"+surValue);  
  obj.innerHTML = "<TD>" +av+"</TD>";
  total =keep.length-1;
  av2 = parseInt(myObject.avail[defValue]) + (total -keep.selectedIndex);
    obj = myObject.doc.getElementById("HotelAvailable"+defValue);  
  obj.innerHTML = "<TD>" +av2+"</TD>";
  
}
function calcTotal() {
    keep =document.getElementById("keep");
    sell =document.getElementById("sell");
    trade =document.getElementById("trade");
  total =keep.length-1;
    return sell.selectedIndex+2*trade.selectedIndex+keep.selectedIndex;
}
function trade22() {
  debugger;
    keep =document.getElementById("keep");
    sell =document.getElementById("sell");
    trade =document.getElementById("trade");
  sur =document.getElementById("survivor");
  def =document.getElementById("defunct");
  total =keep.length-1;
  str="";
    if(total == sell.selectedIndex+2*trade.selectedIndex+
                keep.selectedIndex){
    str=str+sell.selectedIndex +" ";
    str = str +2*trade.selectedIndex +" ";
    str = str+sur.value+ " ";
    str = str+def.value;
        close();
  } else {
    alert("Invalid trade");
    str="";
  }
  window.returnValue = str;
    
  swapStocks(str);
}


function choose(h) {
  var hotel = new Array(7);
  hotel[0]="Luxor";
  hotel[1]="Tower";
  hotel[2]="American";
  hotel[3]="WorldWide";
  hotel[4]="Festival";
  hotel[5]="Continental";
  hotel[6]="Imperial";
  var sur =document.getElementById("survivor");
  var  d1 =document.getElementById("defunct1");
  var d2 =document.getElementById("defunct2");
  var  d3 =document.getElementById("defunct3");
  
  var cnt=2;
  if(d3!=null){
    cnt=4;
  }else if(d2!=null){
    cnt=3;
  }
  var picks=new Array(cnt);
  for(i=0;i<7;i++){
    if(sur.value == hotel[i]){
      picks[0]=i;
      break;
    }
  }
  for(i=0;i<7;i++){
    if(d1.value == hotel[i]){
      picks[1]=i;
      break;
    }
  }
  if(cnt>2){
    for(i=0;i<7;i++){
      if(d2.value == hotel[i]){
        picks[2]=i;
        break;
      }
    }
  }
  if(cnt==4){
    for(i=0;i<7;i++){
      if(d3.value == hotel[i]){
        picks[3]=i;
        break;
      }
    }
  }
  flag=0;
  for(i=0;i<cnt-1;i++){
    for(j=i+1;j<cnt;j++){
      if(picks[i]== picks[j]){
        alert("hotels must be differrent");
        flag=1;
      }
    }
  }
  str="";
  if(flag == 0){
      mn =document.getElementById("mergenum");
    str = str+mn.value;
    for(j=0;j<cnt;j++){
      str = str+picks[j];
    }
        but = document.getElementById("buybut");
    but.style.visibility="hidden";
        chooseOrder(str);
  }
}
