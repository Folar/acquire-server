<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Translation//EN">
<%@ page import="ackgames.acquire.*"%>
 <%response.addHeader("Cache-Control","no-cache");%>
<HTML>
<HEAD><TITLE>JSP Test</TITLE>
<script type="text/javascript" src="jquery-1.3.2.js"></script> 

<script type="text/javascript" src="acq.js"></script> 

</HEAD>
<BODY onLoad="debugger;initGame();" BGCOLOR="#b0b0ff">
<!--Time: <%= new java.util.Date() %>-->
<input type="button" value="Refresh" ID="ref_but" onClick=refbut(); STYLE ="width:50;height:30;visibility:hidden">
<!--input type="text" id="ref_sec" name="zzz" value="30 Seconds" >
<input type="hidden" id="max_sec" name="zzz" value="30" >
<A  id="sec10" style="color:black" HREF="javascript:void setMaxSec(10)">10 </A>
&nbsp
<A  id="sec30" style="color:purple" HREF="javascript:void setMaxSec(30)">30 </A>
&nbsp
<A  id="sec60" style="color:black" HREF="javascript:void setMaxSec(60)">60 </A>
<HR-->
<%!
	private BoardGenerator bg= new BoardGenerator();
	private String loginname;

 %>

<div ID="control" STYLE="position: absolute; left: 5; top: 210 ">
<center>
<table cellpadding="8" bgcolor="black">
<%=bg.generate((String)(session.getAttribute("gameId")))%>
<br>
</table>
<TABLE align="center">
<TR>
<%=bg.generateRack()%>

<TR>
</TABLE>
<%if(!bg.hasGameStarted()){%>
 &nbsp <input type="button" value="Start" ID="Start" onClick=startG(); STYLE ="width:50;height:30">

<%}%>
<center>
<table cellspacing="1" >
<tr><td valign="top">
<TEXTAREA name="info"  id="info" rows="5" cols="50" wrap="hard">
</textarea>
</td><td> &nbsp </td> <td>
<form onSubmit="return sub();">
 <CENTER>
        <TEXTAREA name="info"  id="chatinfo" rows="4" cols="30" wrap="hard"></TEXTAREA><br>
        Type:<INPUT ID="inmsg" TYPE="TEXT" NAME="inmsg" VALUE = ""  >
        <input type="button" value="Enter" ID="ent_but" onClick=sub(); STYLE ="width:50;height:25">
  </CENTER>
</form>

</td></tr>
</table>
<input type="hidden" name="color" id="color"  >
</div>
</BODY>
</HTML>
