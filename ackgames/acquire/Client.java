package ackgames.acquire;
import ackgames.acquire.*;


import java.awt.*;

class Game extends Frame
{
    public synchronized void setVisible(boolean b)
    {
	if (!b) {
	    super.setVisible(false);
	    return ;
	}
	super.setVisible(true);

	Dimension size = new Dimension( 600, 600 );
	setSize( size );
    }

    Game(Acquire A,String host,String port,String name,String ver )
    {
	    A.startIt(this,host,port,name,ver);
	    A.showNoStatus();
	    setLayout(new BorderLayout(2,2));
	    add("Center",A);
		setSize(600,600);
		setTitle("The Game of Acquire");
}
	//{{DECLARE_CONTROLS
	//}}
	//{{DECLARE_MENUS
	//}}
}

public class Client
{
    static Acquire A = new Acquire();
    static Game G;
    //Client m_c = new Client();
    public static void main( String[] args )
    {
        
        String host = null;
        String port = null;
        String name = null;
        String ver = null;
        if(args.length==1){
            host=args[0];
        }
        if(args.length>1){
            port=args[1];
        }
        if(args.length>2){
            ver=args[2];
        }
        if(args.length>3){
            name=args[3];
        }
        if (host == null) host = "localhost";
        if (port == null) port = "7779";
        if (ver == null) ver = "ORIGINAL";
        G = new Game(A,host,port,name,ver);
	    G.setVisible(true);
        
	 }

    
}
