<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Translation//EN">
<HEAD><TITLE>JSP Test</TITLE>
<script language="JavaScript" src="menu.js">
</script>
</HEAD>
<BODY BGCOLOR=#b0b0ff>
<table border="0" cellpadding="4">
<tr>
   <td ID="menu-file" width="100" bgcolor="Silver"
      onMouseOver="Menu('menu-file','file');" onMouseOut="Timeout('menu-file','file');">
     <b>File</b>
   </td>
   <td ID="menu-game" width="100" bgcolor="Silver"
      onMouseOver="Menu('menu-game','game');" onMouseOut="Timeout('menu-game','game');">
     <b>Game</b>
   </td>
   <td ID="menu-preferences" width="100" bgcolor="Silver"
      onMouseOver="Menu('menu-preferences','preferences');" onMouseOut="Timeout('menu-preferences','preferences');">
      <b>Preferences</b>
   </td>
</tr>
</table>
<div ID="file" STYLE="position:absolute; visibility: hidden">
  <table width="100%" border="0" cellpadding="4" cellspacing="0">
  <tr> <td width="100%" ID="p1" 
     onMouseOver="Highlight('file','p1');"
     onMouseOut="UnHighlight('menu-file','file','p1');">
     <b>Open...</b></td></tr>
  <tr> <td width="100%" ID="p2"
     onMouseOver="Highlight('file','p2');"
     onMouseOut="UnHighlight('menu-file','file','p2');">
     <b>Save...</b></td></tr>
  </table>
</div>
<div ID="game" STYLE="position:absolute; visibility: hidden">
  <table width="100%" border="0" cellpadding="4" cellspacing="0">
  <tr> <td width="100%" ID="s1"
     onMouseOver="Highlight('game','s1','rack');"
     onMouseOut="UnHighlight('menu-game','game','s1');">
     <b>Start Game</b></td></tr>
  <tr> <td width="100%" ID="s4"
     onMouseOver="Highlight('game','s4','rack');"
     onMouseOut="UnHighlight('menu-game','game','s4');">
     <b>xxx</b></td></tr>
  <tr> <td width="100%" ID="s2"
     onMouseOver="subMenu('game','s2','rack');"
     onMouseOut="UnHighlight('menu-game','game','s2');">
     <b>Play Tile</b></td></tr>
  <tr> <td width="100%" ID="p3"
     onMouseOver="Highlight('game','s3');"
     onMouseOut="UnHighlight('menu-game','game','s3');">
     <b>End Game</b></td></tr>
  </table>
</div>
<div ID="preferences" STYLE="position:absolute; visibility: hidden">
  <table width="100%" border="0" cellpadding="4" cellspacing="0">
  <tr> <td width="100%" ID="c1"
     onMouseOver="Highlight('preferences','c1');"
     onMouseOut="UnHighlight('menu-preferences','preferences','c1');">
     <b>Set Timeout</b></td></tr>
  </table>
</div>
<div ID="rack" STYLE="position:absolute; visibility: hidden">
  <table width="100%" border="0" cellpadding="4" cellspacing="0">
  <tr> <td width="100%" ID="r1"
     onMouseOver="Highlight('rack','r1');"
     onMouseOut="UnHighlight('rack','rack','r1');">
     <b>1-A</b></td></tr>
  <tr> <td width="100%" ID="r2"
     onMouseOver="Highlight('rack','r2');"
     onMouseOut="UnHighlight('rack','rack','r2');">
     <b>2-C</b></td></tr>
  </table>
</div>
<H1>
    <%="Acquire player: "+ session.getAttribute("name")%>
        Type:<INPUT ID="hhh" TYPE="TEXT" NAME="inmsg" VALUE = ""  >
</H1>
</body>
</html>

