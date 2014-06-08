package ackgames.acquire;

/**
 * Created by larry on 2/28/14.
 */


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet implementation class ActionServlet
 */

public class AcquirePortal extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private  BoardGenerator bg = new BoardGenerator();
    public AcquirePortal() {
        // TODO Auto-generated constructor stub
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cmdStr= request.getParameter("command");
        int cmd = Integer.parseInt(cmdStr);
        String res = null;
        HttpSession session = request.getSession();
        switch (cmd)
        {
            case 1:
                String boardCmdStr =request.getParameter("boardCommand");
                String argStr =request.getParameter("arg");

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
                res =  session.getAttribute("name").toString();
                int boardCmd = Integer.parseInt(boardCmdStr);
                int s= bg.processCmd((String)(session.getAttribute("gameId")), boardCmd,
                        (String)(session.getAttribute("name")),
                        argStr,outCmd,
                        outArg,tile,msg);
//
                break;
            case 2:
             
                argStr ="";

                session.setAttribute("msg","\"\"");
                session.setAttribute("tile","\"\"");
                session.setAttribute("outArg","\"\"");
                outArg=new StringBuffer();
                outCmd=new StringBuffer();
                msg=new StringBuffer();
                tile=new StringBuffer();
                StringBuffer chat=new StringBuffer();

                s= bg.processCmd((String)(session.getAttribute("gameId")),401,
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
                res = result;
                break;
            case 3:

               res =  "<TABLE  WIDTH=100% CELLSPACING = 0 CELLPADDING = 3 BORDER=1  BGCOLOR='black'>" +
               "<TR>" +
                "<TH BGCOLOR='#FDFCD8' WIDTH= 22%>"+"</TH>"+
                "<TH BGCOLOR='red' WIDTH= 7%>L</TH>"+
                "<TH BGCOLOR='yellow' WIDTH= 7%>T</TH>"+
                "<TH BGCOLOR='#8787FF' WIDTH= 7%>A</TH>"+
                "<TH BGCOLOR='#B3AF91' WIDTH= 7%>W</TH>"+
                "<TH BGCOLOR='#80FF80' WIDTH= 7%>F</TH>"+
                "<TH BGCOLOR='cyan' WIDTH= 7%>C</TH>"+
                "<TH BGCOLOR='pink' WIDTH= 7%>I</TH>"+
                "<TH BGCOLOR='#FDFCD8' WIDTH= 22%>$$$</TH>"+
                "</TR>"+
                 bg.getHTMLStats() +
                "</TABLE>";

                break;

            case 4:

                res =  "<TABLE  WIDTH=100% CELLSPACING = 0 CELLPADDING = 1 BORDER=1  BGCOLOR='black'>"+
                "<TR BGCOLOR='#FDFCD8'>"+
                "<TH >Hotel</TH>"+
                "<TH >Available</TH>"+
                "<TH >Size</TH>"+
                "<TH >Price</TH>"+
                "</TR>" +
                bg.getHTMLStocks()+
                "</TABLE>";


                break;
            
            case 5:
               res = 
               " <H2 bgcolor='#b0b0ff'>"+
                "<table> <tr><td>"+
                "Acquire player:"+ session.getAttribute("name").toString()+
                "<INPUT ID='hhh' TYPE='hidden' NAME='inmsg' VALUE = ''  >"+
                "</td><td>"+
                "<TEXTAREA class='xxx'  id='info3' rows='3' cols='50' wrap='hard'></TEXTAREA>"+
                "</td></tr></table>" +
                "</H2>";
                break;

            case 6:
                res =" <H3 ALIGN='CENTER' STYLE='color:white;font-family:sanserif'>Choose Hotel</H3>"+

                "<FORM name='xxx' >"+
                "<center>"+
                bg.generateChooseHotel((String)(session.getAttribute("gameId")))+
                "</center>"+
                "</FORM>";
                break;
            
            case 7:
                res =
                "<H3 ALIGN='CENTER' STYLE='color:white;font-family:sanserif'>Buy Hotels</H3>"+

                "<FORM name='xxx' >"+
                "<center>"+
                bg.generateBuyHotel((String)(session.getAttribute("gameId")))+"&nbsp &nbsp"+
                    "<input type='button' value='Buy' onClick='buy(0);'>"+
                "</center>"+
                "</FORM>";
                break;

            case 8:
                res =
                "<center>"+
                bg.generateSwapStock((String)(session.getAttribute("gameId"))) +
                        "&nbsp &nbsp"+
                "</center>";
                  break;
            case 9:
                res =
                "<H3 ALIGN='CENTER' STYLE='color:white;font-family:sanserif'>Choose Merge Order</H3>"+

                "<FORM name='xxx' >"+
                "<center>"+
                bg.generateChooseOrder((String)(session.getAttribute("gameId")))+
                 "&nbsp; &nbsp;"+
                "<input type='button' id='buybut' value='Choose' onClick='choose(0);'>"+
                "</center>"+
                "</FORM>";
                break;

        }
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(res);
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
// TODO Auto-generated method stub

    }

}
