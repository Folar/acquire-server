<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ page import="ackgames.acquire.*"%>
<!-- 
-->
<HTML>
<HEAD>
  <TITLE>Choose Hotel</TITLE>
<script language="JavaScript">
function startH(h) {
   	top.frames[2].chooseHotel(h);
}
</script>
</HEAD>
<BODY  BGCOLOR=#b0b0ff><BR<BR>
<H3 ALIGN="CENTER" STYLE="color:white;font-family:sanserif">Choose Hotel</H3>

<FORM name="xxx" >
	<center>
	<%=bg.generateChooseHotel((String)(session.getAttribute("gameId")))%>
	</center>
</FORM>

</BODY>
<%!
	private BoardGenerator bg= new BoardGenerator();
%>
</HTML>
