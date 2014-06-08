<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ page import="ackgames.acquire.*"%>
<!-- 
-->
<HTML>
<HEAD>
  <TITLE>Swap Stocks</TITLE>
<script language="JavaScript">
var arg;
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
   	obj = arg.doc.getElementById(str+defValue);	
    	keep =document.getElementById("keep");
	obj.innerHTML = "<TD>" +keep.selectedIndex+"</TD>";

        //set the suvivor hotel
   	obj = arg.doc.getElementById(str+surValue);	
        trade =document.getElementById("trade");
	v=trade.selectedIndex+ arg.holdings[surValue];
	obj.innerHTML = "<TD>" +v+"</TD>";

	// set the money
        sell =document.getElementById("sell");
	w =document.getElementById("worth");
	cost = sell.selectedIndex * parseInt(w.value);
	m= arg.money+cost;
        obj = arg.doc.getElementById("StatMoney"+t);	
        obj.innerHTML = "<TD>" +m+"</TD>";
	av = arg.avail[surValue] - trade.selectedIndex;
  	obj = arg.doc.getElementById("HotelAvailable"+surValue);	
	obj.innerHTML = "<TD>" +av+"</TD>";
	total =keep.length-1;
	av2 = parseInt(arg.avail[defValue]) + (total -keep.selectedIndex);
  	obj = arg.doc.getElementById("HotelAvailable"+defValue);	
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
   	top.frames[2].swapStocks(str);
}
function load() {
	arg= top.frames[2].myObject;
}

</script>
</HEAD>
<BODY onLoad="load();" BGCOLOR=#b0b0ff><BR<BR>

	<center>
	<%=bg.generateSwapStock((String)(session.getAttribute("gameId")))%>&nbsp &nbsp
	<!--input type="button" value="Trade" onClick="trade22();"-->
	</center>

</BODY>
<%!
	private BoardGenerator bg= new BoardGenerator();
%>
</HTML>
