<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ page import="ackgames.acquire.*"%>
<!-- 
-->
<HTML>
<HEAD>
  <TITLE>Choose Hotel Order</TITLE>
<script language="JavaScript">
var arg;
function choose(h) {
	hotel = new Array(7);
	hotel[0]="Luxor";
	hotel[1]="Tower";
	hotel[2]="American";
	hotel[3]="WorldWide";
	hotel[4]="Festival";
	hotel[5]="Continental";
	hotel[6]="Imperial";
    sur =document.getElementById("survivor");
    d1 =document.getElementById("defunct1");
    d2 =document.getElementById("defunct2");
    d3 =document.getElementById("defunct3");
	
	cnt=2;
	if(d3!=null){
		cnt=4;
	}else if(d2!=null){
		cnt=3;
	}
	picks=new Array(cnt);
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
		window.returnValue = str;
		close();
	}
}

</script>
</HEAD>
<BODY  BGCOLOR=#a0a0a0><BR<BR>
<H3 ALIGN="CENTER" STYLE="color:white;font-family:sanserif">Buy Hotels</H1>

<FORM name="xxx" >
	<center>
	<%=bg.generateChooseOrder()%>&nbsp &nbsp
	<input type="button" value="Choose" onClick="choose(0);">
	</center>
</FORM>

</BODY>
<%!
	private BoardGenerator bg= new BoardGenerator();
%>
</HTML>
