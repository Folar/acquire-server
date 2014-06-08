<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<!-- 
-->
<HTML>
<HEAD>
  <TITLE>eAcquire</TITLE> 
    <% String loginname = request.getParameter("name");
		System.out.println("start loginname= "+loginname);
      session.setAttribute("name",loginname);
      session.setAttribute("gameId","3");
    %>
</HEAD>
<FRAMESET ROWS="20%,80%"  marginwidth="5"  topmargin="5" framespacing="0">
<FRAME NAME="header" SRC="header.jsp" marginheight="8" marginwidth="5"  topmargin="8" frameborder="no">
<FRAME NAME="stats" SRC="board.jsp" noresize>
</FRAMESET>
</HTML>
