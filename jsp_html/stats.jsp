<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Translation//EN">
<%@ page import="ackgames.acquire.BoardGenerator"%>
<HTML>
<HEAD><TITLE>JSP Test</TITLE>
</HEAD>
<BODY>
<%!
	private BoardGenerator bg= new BoardGenerator();
 %>
<DIV STYLE="position: absolute; left: 10; top:100">
<TABLE  WIDTH=100% CELLSPACING = 0 CELLPADDING = 1 BORDER=1  BGCOLOR="black">
<TR BGCOLOR="#FDFCD8">
<TH >Hotel</TH>
<TH >Available</TH>
<TH >Size</TH>
<TH >Price</TH>
</TR>

<%=bg.getHTMLStocks()%>
</TABLE>
<BR>
<HR>
<BR>
<TABLE  WIDTH=100% CELLSPACING = 0 CELLPADDING = 3 BORDER=1  BGCOLOR="black">
<TR>
<TH BGCOLOR="#FDFCD8" WIDTH= 22%></TH>
<TH BGCOLOR="red" WIDTH= 7%>L</TH>
<TH BGCOLOR="yellow" WIDTH= 7%>T</TH>
<TH BGCOLOR="#8787FF" WIDTH= 7%>A</TH>
<TH BGCOLOR="#B3AF91" WIDTH= 7%>W</TH>
<TH BGCOLOR="#80FF80" WIDTH= 7%>F</TH>
<TH BGCOLOR="cyan" WIDTH= 7%>C</TH>
<TH BGCOLOR="pink" WIDTH= 7%>I</TH>
<TH BGCOLOR="#FDFCD8" WIDTH= 22%>$$$</TH>
</TR>

<%=bg.getHTMLStats()%>
</TABLE>
</DIV>
</BODY>
</HTML>
