<%@ page import="ackgames.acquire.*"%>
<%!
%>
<%
System.out.println(" in chatsender "+ session.getAttribute("name"));
System.out.println(" in chatsender "+ request.getParameter("inmsg"));
%>
   <% 
	        BoardGenerator bg= new BoardGenerator();  
                String msgStr="";
	        System.out.println(" hfdg chat name="+session.getAttribute("name"));
		String mStr =request.getParameter("inmsg");
		StringBuffer outArg=new StringBuffer();
		StringBuffer outCmd=new StringBuffer();
		StringBuffer msg=new StringBuffer();
		StringBuffer tile=new StringBuffer();
		int s= bg.processCmd(0, (String)(session.getAttribute("name")),
                                  mStr,outCmd,
                                  outArg,tile,msg);
     %>
aa
