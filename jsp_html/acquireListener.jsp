<%@ page import="ackgames.acquire.*"%>
<%!
	private BoardGenerator bg= new BoardGenerator();
%>
	
<%
        String cmdStr =request.getParameter("cmd");
		String argStr =request.getParameter("arg");
	
		session.setAttribute("msg","\"\"");
		session.setAttribute("tile","\"\"");
		session.setAttribute("outArg","\"\"");
		StringBuffer outArg=new StringBuffer();
		StringBuffer outCmd=new StringBuffer();
		StringBuffer msg=new StringBuffer();
		StringBuffer tile=new StringBuffer();
		StringBuffer chat=new StringBuffer();
	        BoardGenerator bg= new BoardGenerator();
	        int s= bg.processCmd((String)(session.getAttribute("gameId")),401, 
                                  (String)(session.getAttribute("name")),
                                  argStr,outCmd,
                                  outArg,tile,msg);
	        s= bg.processCmd((String)(session.getAttribute("gameId")),400, (String)(session.getAttribute("name")),
                                  argStr,outCmd,
                                  outArg,tile,msg,chat);

                System.out.println("\n----------------------");
                System.out.println(" in acquireListener name="+ session.getAttribute("name"));
                if(argStr!=null)
                System.out.println(" in acquireListener argStr="+ argStr);
                if(outCmd!=null)
                System.out.println(" in acquireListener outCmd="+ outCmd);
                if(outArg!=null)
                System.out.println(" in acquireListener outArg="+ outArg);
                if(msg!=null)
                System.out.println(" in acquireListener msg="+ msg);
                System.out.println(" in acquireListener state="+ s);
                System.out.println("----------------------\n");
                String result="state:"+s+"~";
                result+="board_tiles:"+bg.getState((String)(session.getAttribute("gameId")))+"~";
                result+="tiles:"+tile+"~";
                result+="stats:"+bg.getStats((String)(session.getAttribute("gameId")))+"~";
                result+="msg:"+msg+"~";
                result+="arg:"+outArg+"~";
                result+="chat|"+chat;
%>
!<%=result%>!
