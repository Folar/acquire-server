package ackgames.acquire;


import java.awt.*;
import java.util.StringTokenizer;

public  class Preference
{
    public static String m_hotelStr[] = {"Luxor","Tower","American","WorldWide",
			 "Festival","Continental","Imperial"} ;
     public static Color m_color[] = new Color[7];
     public static Font CHAT_FONT =new Font("Serif", Font.BOLD, 14);
     public static Font GAME_MESSAGE_FONT =new Font("Serif", Font.BOLD, 14);
     public static Color GAME_MESSAGE_COLOR = Color.black;
     public static Color CHAT_COLOR = Color.blue;
     public static Font TEXT_HEADER_FONT =new Font("Serif", Font.BOLD, 12);
     public static boolean THREE_D = false;
     public static boolean BEEP = true;
     public static int TEXTAREA_HEIGHT = 7;
     public static String GREETINGS = null;
     public static Font STOCK_STATS_FONT_LARGE =new Font("Serif", Font.PLAIN, 12);
     public static Font STOCK_STATS_FONT_SMALL =new Font("Serif", Font.PLAIN, 10);
     public static Font STOCK_STATS_FONT = STOCK_STATS_FONT_LARGE;
     public static Color MERGE_TILE_BACKGROUND = new Color(35,100,35);
     public static Color MERGE_TILE_FOREGROUND = Color.white;
     public static int MAX_GAMES = 10;
     public static long TIME_OUT = 60000 * 30;
     public static Color GAME_BOARD_COLOR = new Color(253,252,216);
     public static Color TILE_IN_RACK = new Color(236,236,166);
     public static Color TILE_FOREGROUND = Color.black;
     public static String VERSION ="ORIGINAL";
     Preference(String v)
     {
        VERSION = v;
     }
     public Preference()
     {
        m_color[0]= Color.red;
	    m_color[1]= Color.yellow;
	    m_color[2]= new Color(135,135,255);
	    m_color[3] = new Color(195,175,145);
	    m_color[4]= Color.green;
	    m_color[5]= Color.cyan;
	    m_color[6]= Color.pink;
	 }
	 
	 public Preference(Acquire aq)
     {
        this();
        for(int i=0;i<7;i++) {
            String str = aq.getParameter(m_hotelStr[i]);
            if (str != null){
                color(str,i);
            }
        }
        String str = aq.getParameter("BEEP");
        if (str != null){
            if (str.equalsIgnoreCase("no")){
                BEEP=false;
            }
        }
        str = aq.getParameter("3D");
        if (str != null){
            if (str.equalsIgnoreCase("no")){
                THREE_D=false;
            }
        }
        str = aq.getParameter("TEXTAREA_HEIGHT");
        if (str != null){
            TEXTAREA_HEIGHT = Integer.parseInt(str);
            //System.err.println("TEXTAREA_HEIGHT = "+TEXTAREA_HEIGHT );
        }
        str = aq.getParameter("SCREEN_SIZE");
        if (str != null){
            if (str.equalsIgnoreCase("small")){
                STOCK_STATS_FONT = STOCK_STATS_FONT_SMALL;
            }
        }
        
        str = aq.getParameter("TILE_FOREGROUND");
        if (str != null){
            TILE_FOREGROUND = color(str);
        } 
        str = aq.getParameter("GAME_BOARD_COLOR");
        if (str != null){
            GAME_BOARD_COLOR = color(str);
        } 
        str = aq.getParameter("VERSION");
        if (str != null){
             VERSION = str;
        } 
        System.out.println("version = "+VERSION);
        GREETINGS = aq.getParameter("GREETINGS");
	 }
	 
	 void color(String rgb,int index)
	 {
	    
	    m_color[index] = color(rgb);
	 }
	 
	 Color color(String rgb)
	 {
	    StringTokenizer tokenizer =
	        new StringTokenizer( rgb, "," );
	        int r = Integer.parseInt(tokenizer.nextToken());
	        int g = Integer.parseInt(tokenizer.nextToken());
	        int b = Integer.parseInt(tokenizer.nextToken());
	    return new Color(r,g,b);
	 }
}