<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Translation//EN">
<%@ page import="ackgames.acquire.*"%>
<%!
	private BoardGenerator bg= new BoardGenerator();
%>
	
<%
//System.out.println(" in acktest `kkkkkkkkkkhhhhhhjjjjjjjjjjjjjjjjjjjjjjjjjjjjjh"+ session.getAttribute("name"));
String cmdStr =request.getParameter("cmd");
		String argStr =request.getParameter("arg");
	
		session.setAttribute("msg","\"\"");
		session.setAttribute("tile","\"\"");
		session.setAttribute("outArg","\"\"");
		StringBuffer outArg=new StringBuffer();
		StringBuffer outCmd=new StringBuffer();
		StringBuffer msg=new StringBuffer();
		StringBuffer tile=new StringBuffer();
	        int s= bg.processCmd(401, (String)(session.getAttribute("name")),
                                  argStr,outCmd,
                                  outArg,tile,msg);

//System.out.println("hhhhhhhhhhhhhhhhh");
%>
<HTML>
<HEAD><TITLE>JSP Test</TITLE>
</HEAD>
<BODY>
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

</TABLE>
</DIV>
</BODY>
</HTML>
