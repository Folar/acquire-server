<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ page import="ackgames.acquire.*"%>
<!-- 
-->
<HTML>
<HEAD>
  <TITLE>Buy Hotels</TITLE>
<script language="JavaScript">
var arg;

function getCost()
{
	cost=0;
    for(i=0;i<7;i++){
		obj =document.getElementById("h" +i);
		if(obj !=null){
			amt = obj.selectedIndex;
		    if(amt >0) {
				cost += amt * parseInt(arg.price[i]);
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

	cnt =0;
	m_total=0;
	for(i=0;i<7;i++){
		obj =document.getElementById("h" +i);
		if(obj !=null){
			amt = obj.selectedIndex;
		    if(amt >0) {
        		cnt +=amt;
			}
        }
	}
	if(cnt>3 ){
		for( i=0;i<7;i++){
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
	x=getCost();
	while(x> arg.money){
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
	m= arg.money-x;
    obj = arg.doc.getElementById("StatMoney"+arg.currentPlayer);	
    obj.innerHTML = "<TD>" +m+"</TD>";
    for(i=0;i<7;i++){
		obj =document.getElementById("h" +i);
		if(obj !=null){
			amt = obj.selectedIndex;
			av = arg.avail[i] - amt;
   			obj = arg.doc.getElementById("HotelAvailable"+i);	
			obj.innerHTML = "<TD>" +av+"</TD>";
			av = arg.holdings[i] + amt;
   			str = "StatHotel"+arg.currentPlayer;	
   			obj = arg.doc.getElementById(str+i);	
			obj.innerHTML = "<TD>" +av+"</TD>";
			
		}
	}
}


function buy(h) {
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
				cost += amt * parseInt(arg.price[i]);
				a+=amt;
			}
		}
	}
	if(arg.money<cost || a>3){
		alert("Unreasonable purchase");
		return;
	}
   	top.frames[2].buyHotels(str);
}
function x() {
	arg= top.frames[2].myObject;
}
</script>
<style>
.Luxor{color:red; font-family:"franklin gothic medium"; font-weight:bold}
.Tower{color:yellow; font-family:"franklin gothic medium"; font-weight:bold}
.American{color:#8787FF; font-family:"franklin gothic medium"; font-weight:bold}
.WorldWide{color:#B3AF91; font-family:"franklin gothic medium"; font-weight:bold}
.Festival{color:#80FF80; font-family:"franklin gothic medium"; font-weight:bold}
.Continental{color:cyan; font-family:"franklin gothic medium"; font-weight:bold}
.Imperial{color:pink; font-family:"franklin gothic medium"; font-weight:bold}
</style>

</HEAD>
<BODY onLoad="x();"  BGCOLOR=#a0a0ff><BR<BR>
<H3 ALIGN="CENTER" STYLE="color:white;font-family:sanserif">Buy Hotels</H3>

<FORM name="xxx" >
	<center>
	<%=bg.generateBuyHotel((String)(session.getAttribute("gameId")))%>&nbsp &nbsp
	<input type="button" value="Buy" onClick="buy(0);">
	</center>
</FORM>

</BODY>
<%!
	private BoardGenerator bg= new BoardGenerator();
%>
</HTML>
