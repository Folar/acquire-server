<%@ page import="ackgames.acquire.*"%>
<%!
%>
<%
System.out.println(" in chatlistener "+ session.getAttribute("name"));
%>
   <% 
	 BoardGenerator bg= new BoardGenerator();
        String str=bg.getTextMsg((String)(session.getAttribute("name")));
       System.out.println("OOOOOOOOO name="+(String)(session.getAttribute("name"))+" text ="+str);%>
<%=str%>
