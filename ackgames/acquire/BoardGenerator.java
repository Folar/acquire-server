package ackgames.acquire;
import ackgames.acquire.*;

public class BoardGenerator
{
    String letter[]={"A","B","C","D","E","F","G","H","I"};
    public BoardGenerator()
    {
        //System.out.println("in bg");
    }
    public String getHTMLStats()
    {
         //System.out.println("in getHTMLsTATSYY");
        if (hasGameStarted()){
            return AcquireSrvr.getHTMLStats();
        }
        String str="";
        for(int i=0;i<6;i++){
            String c = i%2==0?"white": "\"#5092E4\"";
           /* if(i== m_currentPlayer){
                c = "\"#C0C0C0\"";
            }*/
            str = str + "\n<TR ID=\"StatRow"+i+"\" BGCOLOR=" + c +" >";
            str = str +"\n<TD ID=\"StatName"+i+"\">            </TD>";
            for(int j=0;j<7;j++){
                str = str +"\n<TD ID=\"StatHotel"+i+j+"\">0</TD>";
            }
            str = str +"\n<TD ID=\"StatMoney"+i+"\"> 6000</TD>";
            str = str + "\n</TR>";
        }
        return str;
        
        
    }
    public String getHTMLStocks()
    {
         //System.out.println("in getHTMLsTOCKSYY");
        String c= "";
        String str="";
        String m_hot[] ={"Luxor","Tower","American","Worldwide","Festival","Continental","Imperial"};
        for(int k= 0;k<7;k++){
	        
	        if(k%2==0){
	            c= "\"white\"";
	        }else {
	            c = "\"#5092E4\"";  
	        }
	        
	        String state = "\"white\"";
	        switch(k)
            {
                case Hotel.LUXOR:
                    state="\"red\"";
                    break;
                case Hotel.TOWER:
                    state="\"yellow\"";
                    break;   
                case Hotel.AMERICAN:
                    state = "\"#8787FF\"";
                    break;
                case Hotel.FESTIVAL:
                    state="\"#80FF80\"";
                    break;   
                case Hotel.WORLDWIDE:
                    state="\"#B3AF91\"";
                    break;
                case Hotel.IMPERIAL:
                    state="\"pink\"";
                    break;   
                case Hotel.CONTINENTAL:
                    state="\"cyan\"";
                    break; 
            }
	        str = str + "\n<TR ID=\"StockRow"+k+"\" BGCOLOR=" + c +" >";
	        str = str +"\n<TD BGCOLOR=" + state +"ID=\"HotelName"+k+"\">"+ m_hot[k]+"</TD>";
            str = str +"\n<TD ID=\"HotelAvailable"+k+"\">"+ 25+"</TD>";
            str = str +"\n<TD ID=\"HotelCount"+k+"\">"+ 0+"</TD>";
            str = str +"\n<TD ID=\"HotelPrice"+k+"\">"+ 0+"</TD>";
            str = str + "\n</TR>";
        }
        return str;
        
    }
    public static boolean notPlaying(String n,int id)
    {
        return AcquireSrvr.getPlayerId(n,id) ==  -2;
    }
    public static boolean hasGameStarted()
    {
        return AcquireSrvr.hasGameStarted();
    }
    public String getPlayerId(String name,int id)
    {
        return "\""+ AcquireSrvr.getPlayerId(name,id)+"\"";
        
    }
    public String getStats(String id)
    {
         return AcquireSrvr.getStats(Integer.parseInt(id));
    }


    public String generate(String id)
    {
        // new  JSCalendarYear(4,2005).getHtmlOutput();
        int t[] = AcquireSrvr.getHackGB(Integer.parseInt(id));
        String res = "";
        int k=0;
        for(int i=0;i<9;i++){
            for(int j=0;j<12;j++){
                res+= generate(i,j,t[k]);
                k++;
            }
        }
        return res;
    }
    public String getState(String id)
    {
        int t[]=AcquireSrvr.getHackGB(Integer.parseInt(id));
        if(t==null){
            return "\"\"";
        }
        String s = "";
        for(int i=0;i<108;i++){
            s= s+t[i];
            //System.out.println(i+". t[i] = " + t[i]);
        }

        return s;
    }
    public String generate(int i,int j,int s)
    {
        int xPos=100;
        int yPos=100;
        String state= "#FDFCD8";
        String fg="black";
        switch(s)
        {
            case Hotel.LUXOR:
                state="red";
                break;
            case Hotel.TOWER:
                state="yellow";
                break;   
            case Hotel.AMERICAN:
                state = "#8787FF";
                break;
            case Hotel.FESTIVAL:
                state="#80FF80";
                break;   
            case Hotel.WORLDWIDE:
                state="#B3AF91";
                break;
            case Hotel.IMPERIAL:
                state="pink";
                break;   
            case Hotel.CONTINENTAL:
                state="cyan";
                break;
            case 9/*tile.ONBOARD*/:
                state="black";
                fg="white";
                break;   
           
                  
        }
        String layerName ="tileLayer"+(j+1)+"-"+letter[i];
        String str = "";
        if(j%12 == 0){
            str = str + "<TR>";
        }
		str = str + "\n<TD  ID=\""+layerName+"\" STYLE=\"color:"+fg+
		                 ";font-family:sans-serif;font-size:10pt;font-weight:bold;background-color:"+
		                 state+ "\"  onMouseUp=clicky(\""+layerName+"\");  >\n"+
			            (j+1)+"-"+letter[i]+
		            "</TD>";
		if(j%12 == 11 && i>0){            
	        str = str + "\n</TR>";
        }            
        return str;       
    }
    public String getTextMsg(String name)
    {
                 String s=AcquireSrvr.getTextMsg(name);;
                 while(s.length() == 0){
                      s=AcquireSrvr.getTextMsg(name);
                       try {


                           Thread.sleep(300);
                       } catch (InterruptedException e) {
                           e.printStackTrace();  //To change body of catch statement use Options | File Templates.
                       }
                   }


          System.out.println(" wwwwwwwwwwwwwww bg:gettextmsg ="+s);
        if(s.equals("#abort#")){
            return "\"\"";
        }
        return s;
    }
    public String generateChooseHotel(String gameId)
    {
        return AcquireSrvr.generateChooseHotel(Integer.parseInt(gameId));
    }
    public String generateBuyHotel(String gameId)
    {
        return AcquireSrvr.generateBuyHotel(Integer.parseInt(gameId));
    }
    public String generateSwapStock(String gameId)
    {
        return AcquireSrvr.generateSwapStock(Integer.parseInt(gameId));
    }
    public String generateChooseOrder(String gameId)
    {
        return AcquireSrvr.generateChooseOrder(Integer.parseInt(gameId));
    }
    public String generateRack()
    {
        String str="";
        for(int i=0;i<6;i++){
//            str = str+"\n<TD ID=\"tile"+i+
//                        "\" STYLE =\"width:50;height:30;visibility:hidden\">"+
//                        "<input type=\"button\" value=\"----\"  onMouseOver=mousein(\"1-H\"); "
//                        + "onMouseOut=mouseout(\"1-H\"); ></TD>";
              str = str+"\n<TD ID=\"tile"+i+
                        "\" STYLE =\"width:50;height:30;\">"+
                        "<input  type=\"button\" value=\"----\"  onMouseOver=mousein(\"1-H\"); "
                        + "\" STYLE =\"visibility:hidden\"  ID=\"tile"+i+">"+
                         "onMouseOut=mouseout(\"1-H\"); ></TD>";
        }
        return str;
    }
    private static boolean serverStared = false;
    public static void hackMain()
    {
        if(!serverStared ){
            serverStared = true;
            AcquireSrvr.hackMain();
        }
    }

    public static int processCmd(String gameId,int cmd,String name,String arg,
                                    StringBuffer outCmd,StringBuffer outArg,
                                    StringBuffer tile,StringBuffer msg)
    {
        StringBuffer chatText= new StringBuffer();
        return processCmd(gameId, cmd, name,arg,outCmd,outArg,tile, msg, chatText);

    }



    public static int processCmd(String gameId,int cmd,String name,String arg,
                                    StringBuffer outCmd,StringBuffer outArg,
                                    StringBuffer tile,StringBuffer msg,StringBuffer chatText)
    {
        //System.out.println("bg:proesscmd="+cmd);

        if (cmd == 401 || cmd == 403){
            int s=0;
            while(s == 0){
                //System.out.println(" xx msg = " + msg);
                s=AcquireSrvr.processCmd(Integer.parseInt(gameId),cmd,name,arg,outCmd,outArg,tile,msg,chatText);

                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use Options | File Templates.
                }
            }
           // System.out.println(" ret msg = " + msg);
            return 6;
        }
         //System.out.println("in  processCmd cmd cmd="+cmd+" name "+name + " arg ="+arg);
        return AcquireSrvr.processCmd(Integer.parseInt(gameId),cmd,name,arg,outCmd,outArg,tile,msg,chatText);

    }
}