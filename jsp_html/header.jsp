<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Translation//EN">
<HEAD><TITLE>JSP Test</TITLE>

<style>
	.xxx {
			background-color:#b0b0ff;
			font-family:garamond;
			font-size:16pt;
			font-weight:bold;
			font-style:italic;
			color:purple;
	     }
</style>
</HEAD>
<BODY BGCOLOR=#b0b0ff >
<H2>
         <table> <tr><td>
    <%="Acquire player: "+ session.getAttribute("name")%>
        <INPUT ID="hhh" TYPE="hidden" NAME="inmsg" VALUE = ""  >
        </td><td>
        <TEXTAREA class="xxx"  id="info3" rows="3" cols="50" wrap="hard"></TEXTAREA>
        </td></tr></table>
</H2>
</body>
</html>

