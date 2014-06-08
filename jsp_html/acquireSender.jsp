<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Translation//EN">
<%@ page import="ackgames.acquire.*"%>
<%!
	private BoardGenerator bg= new BoardGenerator();
%>
	
<%
        String cmdStr =request.getParameter("cmd");
		String argStr =request.getParameter("arg");
        System.out.println("\n---------------------- ");
        System.out.println(" in acquireSender name="+ session.getAttribute("name"));
                if(cmdStr!=null)
        System.out.println(" in acquireSender cmd="+ cmdStr);
                if(argStr!=null)
        System.out.println(" in acquireSender arg="+ argStr);
        System.out.println("----------------------\n ");
	
		session.setAttribute("msg","\"\"");
		session.setAttribute("tile","\"\"");
		session.setAttribute("outArg","\"\"");
		StringBuffer outArg=new StringBuffer();
		StringBuffer outCmd=new StringBuffer();
		StringBuffer msg=new StringBuffer();
		StringBuffer tile=new StringBuffer();
	        BoardGenerator bg= new BoardGenerator();
		int cmd = Integer.parseInt(cmdStr);
	        int s= bg.processCmd((String)(session.getAttribute("gameId")),cmd, 
                                  (String)(session.getAttribute("name")),
                                  argStr,outCmd,
                                  outArg,tile,msg);
%>
Xx

