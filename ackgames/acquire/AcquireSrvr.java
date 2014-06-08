package ackgames.acquire;
import ackgames.acquire.*;
// Import appropriate java classes
import java.applet.*;
import java.awt.*;
import java.lang.*;
import java.net.*;
import java.io.*;
import java.util.*;


class MessageThreadGroup extends ThreadGroup
{
   MessageThreadGroup( String name)
    {
        super( name );
    }


    public void close()    //MessageThreadGroup
    {
	    int count;

        if ( (count = activeCount()) != 0 )
	    {
	       // System.err.println( "in close msggroup thread "+ count );
	        MessageClientThreadHandler[] thrds =
		    new MessageClientThreadHandler[count];

	        enumerate(thrds,false);

	        for ( int i = 0; i < thrds.length; i++ )
	        {
	            if ( thrds[i] != null )
	            {
		            if ( thrds[i] == Thread.currentThread() )
		                System.err.println( "Enumerated the current thread!!!" );
		            System.err.println( "Closing thread " + thrds[i].getName() + " [" + i + "]  to end." );
		            thrds[i].close();
		        }
	        }
	    }
    }
    MessageClientThreadHandler[] getClientThreads()
    {
	    int count = activeCount();
	    MessageClientThreadHandler[] thrds =
		     new MessageClientThreadHandler[count];

	    enumerate(thrds,false);
	    return thrds;
    }

    public void broadcastmsg( AQC msg )
    {
	    int count;

	    if ( (count = activeCount()) != 0 )
	    {
	        MessageClientThreadHandler[] thrds =
		        new MessageClientThreadHandler[count];

	        enumerate(thrds,false);
	        //System.err.println( "found " + new Integer(thrds.length).toString() + " threads" );
	        for ( int i = 0; i < thrds.length; i++ )
	        {
		        if ( thrds[i] != null )
		        {
		            thrds[i].sendmsg( msg );
		        }
	        }
	    }
    }

    public void broadcastmsg( AQC msg,int id )
    {
	    int count;

	    if ( (count = activeCount()) != 0 )
	    {
	        MessageClientThreadHandler[] thrds =
		        new MessageClientThreadHandler[count];

	        enumerate(thrds,false);
	        //System.err.println( "found " + new Integer(thrds.length).toString() + " threads" );
	        for ( int i = 0; i < thrds.length; i++ )
	        {
		        if ( thrds[i] != null && thrds[i].getGameId() == id)
		        {
		            if (!thrds[i].getGameOver()){
		                thrds[i].sendmsg( msg );
		            }else if(msg.getCode() == AQC.BROADCAST_MESSAGE){
		                 thrds[i].sendmsg( msg );
		            }
		        }
	        }
	    }
    }

    public void removeExpiredClients()
    {
        int count;

	    if ( (count = activeCount()) != 0 )
	    {
	        MessageClientThreadHandler[] thrds =
		        new MessageClientThreadHandler[count];

	        enumerate(thrds,false);
	        //System.err.println( "found " + new Integer(thrds.length).toString() + " threads" );
	        for ( int i = 0; i < thrds.length; i++ )
	        {
		        if ( thrds[i] != null )
		        {
		            if(thrds[i].hasClientExpired() && thrds[i].getGameOver() ){
		                thrds[i].sendmsg(
		                   new AQC(-1,"Your session has expired",AQC.ERROR));
		                thrds[i].close();
		            }
		        }
	        }
	    }
    }


    public void removeExpiredGame( int id )
    {
	    int count;

	    if ( (count = activeCount()) != 0 )
	    {
	        MessageClientThreadHandler[] thrds =
		        new MessageClientThreadHandler[count];

	        enumerate(thrds,false);
	        //System.err.println( "found " + new Integer(thrds.length).toString() + " threads" );
	        for ( int i = 0; i < thrds.length; i++ )
	        {
		        if ( thrds[i] != null && thrds[i].getGameId() == id)
		        {
		            if(thrds[i].hasClientExpired()  ){
		                thrds[i].sendmsg(
		                   new AQC(-1,"Your session has expired",AQC.ERROR));
		                thrds[i].close();
		            }else{
		                thrds[i].setGameOver(true);
		            }
		        }
	        }
	    }
	}
    public void setGameOver( int id )
    {
	    int count;

	    if ( (count = activeCount()) != 0 )
	    {
	        MessageClientThreadHandler[] thrds =
		        new MessageClientThreadHandler[count];

	        enumerate(thrds,false);
	        //System.err.println( "found " + new Integer(thrds.length).toString() + " threads" );
	        for ( int i = 0; i < thrds.length; i++ )
	        {
		        if ( thrds[i] != null && thrds[i].getGameId() == id)
		        {
		            thrds[i].setGameOver(true);
		        }
	        }
	    }
    }

    public boolean findName( String name )
    {
	    int count;

	    if ( (count = activeCount()) != 0 )
	    {
	        MessageClientThreadHandler[] thrds =
		        new MessageClientThreadHandler[count];
            enumerate(thrds,false);
	        //System.err.println( "found " + new Integer(thrds.length).toString() + " threads" );
	        for ( int i = 0; i < thrds.length; i++ )
	        {
	            if(thrds[i].fClientName.equals(name)){
	                return true;
	            }
	        }
	    }
	    return false;
    }

}

class CheckExpiredGamesThread extends Thread
{
    MessageFrame mMessageFrame=null;
    CheckExpiredGamesThread(MessageFrame mf)
    {
        mMessageFrame = mf;
    }

    public void run()
    {
        while(true){
            try {
                Thread.sleep(60000);
            }catch (Exception e){
            }
            mMessageFrame.checkExpiredGames();
        }
    }
}

class MessageDispatcherThread extends Thread
{
    ServerSocket    fSocket = null;
    MessageFrame    fFrame = null;
    MessageThreadGroup	   fThreadGroup = null;
    GameBoard m_gameBoard;
    static int port = 7779;
    MessageDispatcherThread(  MessageFrame frame )
    {
	    super(  "Dispatcher" );
	    fFrame = frame;
	    fThreadGroup = new MessageThreadGroup( "MyThreadGroup" );
	    frame.setMessageThreadGroup(fThreadGroup);
	    try
	    {
	        fSocket = new ServerSocket( port );
	        System.err.println("host " + fSocket.getInetAddress().getLocalHost());
	    } catch ( Exception e )
	    {
		    System.err.println( e );
	    }
    }


    public void close()  //  MessageDispatcherThread
    {
        stop();
	    try
	    {
	        fSocket.close();
	    } catch ( Exception e )
	    {
	        System.err.println( e );
	    }

    }

    public void run()
    {
	    try
	    {
	        for ( int c = 1; true; c++ )
	        {
		        Socket theSocket = fSocket.accept();
		        new MessageClientThreadHandler( fThreadGroup, fFrame, theSocket, c ).start();
	        }
	    } catch ( ThreadDeath te )
	    {
	        System.err.println( "The dispatcher thread caught its death trap." );
	        throw te;
	    }
	    catch ( Exception e )
	    {
	        System.err.println( "An exception has been thrown to dispatcher: " + e );
	    } finally
	    {
	        System.err.println( "Shutting down dispatcher thread" );

	        if ( fThreadGroup != null )
		    fThreadGroup.close();

	        fThreadGroup = null;
	        close();
	    }
    }
}


class MessageClientThreadHandler extends Thread
{
    Socket          fIncoming = null;
    int 	    fCounter = 0;
    MessageFrame    fFrame = null;
    DataInputStream fInputStream = null;
    DataOutputStream     fOutputStream = null;
    String          fClientName = "UNKNOWN";
    MessageThreadGroup fMsgThreadGroup = null;
    boolean m_gameOver = true;
    GameBoard m_gameBoard;
    int m_gameId =-1;

    long lastAccess;

    boolean hasClientExpired()
    {
        return ( (System.currentTimeMillis() - lastAccess)> Preference.TIME_OUT);
    }
    MessageClientThreadHandler( MessageThreadGroup group , MessageFrame f, Socket s, int c )
    {

	    super( group, new Integer( c ).toString() );
        fMsgThreadGroup = group;
        fFrame = f;
        fIncoming = s;
	    m_gameBoard = null;
	    fCounter = c;

	    try
	    {
	        fOutputStream = new DataOutputStream( fIncoming.getOutputStream() );
	        fInputStream =  new DataInputStream(s.getInputStream());

	    } catch ( Exception e )
	    {
		    System.err.println( e );
	    }
    }
    void setGameOver(boolean b)
    {
        m_gameOver =b;
    }
    boolean getGameOver()
    {
        return m_gameOver;
    }
    synchronized public void close()
    {
	    if(fIncoming == null){
	        return;
	    }
	    try
	    {
		    fInputStream.close();
	   	    fOutputStream.close();
    		fIncoming.close();
    		fIncoming = null;
	    } catch ( java.net.SocketException e ){
	       System.err.println( "The current thread caught its death trap." );
	    }catch ( Exception e )
	    {
	       System.err.println( e );
	    }
	    stop();

    }

    public void sendmsg( AQC msg )
    {
	    synchronized(fOutputStream)
	    {
	        fFrame.printMessage("****OUTGOING MESSAGE***  code= "+msg.getCode()+"  id ="+msg.getCurrentPlayerID());
	        msg.writeAQC(fOutputStream);

        }
    }

    int getGameId()
    {
        return m_gameId;
    }
    int findPlayer(String name)
    {
        return fFrame.findPlayer(name);

    }
    int findObserverAndRemove(String name)
    {
        return fFrame.findObserverAndRemove(name);
    }


    AQCLobbyUpdate createRoom(String fn)
    {
        return fFrame.createRoom(fn);
    }
    AQCLobbyUpdate createRoom(int version,int gid)
    {
        return fFrame.createRoom(version,gid);
    }
    AQCLobbyUpdate getLobby(boolean b)
    {
        return fFrame.getLobby(b);
    }

    public void run()
    {
	    try
	    {
	        boolean done = false;

	        while ( !done )
	        {

		        int code = fInputStream.readInt();

                lastAccess = System.currentTimeMillis();
		        AQC arg = AQCFactory.createObject(code, fInputStream);
		        fFrame.printMessage("****INCOMMING MESSAGE*** code= "+arg.getCode()+"  id ="+arg.getCurrentPlayerID());
		        synchronized(fFrame.sync)
		        {
		            if (code == AQC.CLIENT_PING){
		                fFrame.pingPlayers(arg.getGameID());
		                continue;
		            }else if (code == AQC.CLIENT_ABORT){
		                fMsgThreadGroup.broadcastmsg(arg,m_gameId);
		                fMsgThreadGroup.broadcastmsg(new AQCLobbyDelete(m_gameId));
		                fFrame.removeGame(m_gameBoard.m_gameId);
		                continue;
		            }else if (code == AQC.LOBBY_TIMEOUT){
		                AQCTimeout ato =AQCFactory.getAQCTimeout(arg);
		                Preference.TIME_OUT = ato.getTime()*60000;
		                continue;
		            }else if (code == AQC.BROADCAST_MESSAGE){
                        //System.out.println("in broadcastMsg fClientName = " + fClientName+"id="+m_gameId);
		                arg.setName(fClientName);
                        fMsgThreadGroup.broadcastmsg(arg,m_gameId);
                         m_gameBoard.populateMessage2(arg.getMessage(),fClientName);
		                continue;
		            }else if (code == AQC.LOBBY_MESSAGE){
		                fMsgThreadGroup.broadcastmsg(arg);
		                continue;
		            }else if (code == AQC.LOBBY_FILE){
		                AQCLobbyFile alf =AQCFactory.getAQCLobbyFile(arg);
		                if(alf.getSave()){
		                    fFrame.saveGame(alf.getGameID(),alf.getFileName());
		                    continue;
		                }
		                AQC aqc = new AQC(-1,arg.getMessage() ,AQC.LOBBY);
	                    aqc.setName(alf.getName());
		                fMsgThreadGroup.broadcastmsg(aqc);
		                AQCLobbyUpdate alu=createRoom(alf.getFileName());
		                fMsgThreadGroup.broadcastmsg(alu);
		                int id= alu.getRooms()[0].getGame();
		                arg = new AQCSignIn( arg.getName(),id,false) ;
		                code = AQC.GAMEBOARD_SIGN_IN;
		            }else if(code == AQC.LOBBY_FETCH){
		                if(fMsgThreadGroup.findName(arg.getName())){
		                    AQC aqc = new AQC(-1,arg.getName() +
		                                      " is already playing"  ,AQC.ERROR);
		                    sendmsg(aqc);
		                    close();
		                    continue;
		                }else{
		                    AQC aqc = new AQC(-1,arg.getMessage() ,AQC.LOBBY);
	                        aqc.setName(arg.getName());
				            fClientName = arg.getName();
		                    fMsgThreadGroup.broadcastmsg(aqc);
		                    int gid = findPlayer(arg.getName());
		                    if(gid == -1){
		                        sendmsg(getLobby(true));
		                        continue;
		                    }else{
		                        sendmsg(getLobby(false));
		                        arg = new AQCSignIn( arg.getName(),gid,false) ;
		                        code = AQC.GAMEBOARD_SIGN_IN;
		                    }
		                }
		            }else if(code == AQC.LOBBY_CREATE){
		                setGameOver(false);
		                AQC aqc = new AQC(-1,arg.getMessage() ,AQC.LOBBY);
	                    aqc.setName(arg.getName());
		                fMsgThreadGroup.broadcastmsg(aqc);
		                AQCLobbyUpdate alu=createRoom(arg.getVersion(),-1);
		                fMsgThreadGroup.broadcastmsg(alu);
		                int id= alu.getRooms()[0].getGame();
		                arg = new AQCSignIn( arg.getName(),id,false) ;
		                code = AQC.GAMEBOARD_SIGN_IN;
		            }
			        if(code == AQC.GAMEBOARD_SIGN_IN) {
			            setGameOver(false);
				        AQCSignIn asi= AQCFactory.getAQCSignIn(arg);
				        GameBoard gb = fFrame.m_gameBoards[asi.getGameID()];
				        if(gb == null){
				            AQC aqc =
				                new AQC(-1,arg.getName()+ " restarted game "+ asi.getGameID(),AQC.LOBBY);
	                        aqc.setName(arg.getName());
		                    fMsgThreadGroup.broadcastmsg(aqc);
		                    AQCLobbyUpdate alu=createRoom(arg.getVersion(),asi.getGameID());
		                    fMsgThreadGroup.broadcastmsg(alu);
		                    m_gameBoard = fFrame.m_gameBoards[asi.getGameID()];
				        }else {
				            int id =findObserverAndRemove(arg.getName());
				            if(id!=-1){
				                fMsgThreadGroup.broadcastmsg(m_gameBoard.updateRoom(id));
				            }
				            m_gameBoard = gb;
				        }

				        m_gameId = asi.getGameID();

			        }else if (m_gameBoard.isConnectionLost()&&code != AQC.CLIENT_ACK) {
				        return;
			        }
			        if(fFrame.m_gameBoards[m_gameId]==null){
			            continue;
			        }


			        AQC cmds[] = m_gameBoard.processMsg( arg,fOutputStream,
			                                            fInputStream );
			        if (cmds == null){
			            continue;
			        }
			        for(int i=0; i< cmds.length;i++){
			            AQC cmd = cmds[i];
			            if(cmd.getCode()>=AQC.LOBBY_MESSAGES){
			                fMsgThreadGroup.broadcastmsg(cmd);
			            } else if (cmd.getTarget() == AQC.ALL){
			                fMsgThreadGroup.broadcastmsg(cmd,m_gameId);
                            m_gameBoard.populateMessage(cmd.getMessage());
			            } else  {
			                if(cmd.getMessageTarget() == AQC.ALL){
			                    fMsgThreadGroup.broadcastmsg(new AQC(cmd.getMessage()),m_gameId);
			                    m_gameBoard.populateMessage(cmd.getMessage());
			                    cmd.setMessageTarget(AQC.NO_MESSAGE);
			                }
			                fFrame.printMessage("+++OUTGOING MESSAGE+++  code= "+cmd.getCode()+
			                                    "  id ="+cmd.getCurrentPlayerID());
			                m_gameBoard.getPlayer(cmd).sendmsg(cmd);

			            }

			        }
			        if(m_gameBoard.getGameState() == GameBoard.GAMEOVER&&
			            !m_gameBoard.anyDHTMLPlayers()){
			            fFrame.removeGame(m_gameBoard.m_gameId);
			        }
			        cmds = null;

		        }
	        }

	    } catch ( java.net.SocketException e ){
	    System.err.println( "The current thread caught its death trap." );
	    } catch ( ThreadDeath te )
	    {
	        System.err.println( "The current thread caught its death trap." );
	        throw te;

	    }catch ( Exception e )
	    {
	        System.err.println( e );
	    } finally
	    {

	        System.err.println( "About to stop this thread." );
	        System.err.println( "Shutting down thread " + fCounter  );
	        if( m_gameBoard!=null ){
	            if(m_gameBoard.lostConnect(fClientName))
	            {
		            fMsgThreadGroup.broadcastmsg( new AQCHideFrames(-1,fClientName),m_gameId);
		            System.err.println(" lose connection");
		        }else{
		            fMsgThreadGroup.broadcastmsg(m_gameBoard.updateRoom(m_gameId));
		        }

	        }
	        close();
	    }
    }
}



// extend the Frame class
class MessageFrame extends Frame implements MessageFrameI
{
	TextField fCommandText = null;  // Command text field
	TextField fNameText = null;     // Name text field
	GridBagLayout fLayout = null;    // The overall layout
	GridBagConstraints fConstraints = null;
	TextArea fTextArea = null;	// The output text pane
	static GameBoard m_gameBoards[] = new GameBoard[Preference.MAX_GAMES];
	static MessageThreadGroup fMsgThreadGroup;
	Menu m_fileMenu= null;
	MessageDispatcherThread  fDispatchThread = null;

	ServerSocket fSocket = null;
	int fTextSize = 0;
	boolean m_autoSave = false;
	boolean m_canSave = false;
	String sync="sync";

	static public String getHTMLStats()
	{
	    return m_gameBoards[0].getHTMLStats();
	}
	static public String getHTMLStocks()
	{
	    return m_gameBoards[0].getHTMLStocks();
	}
   static boolean canBuyStock(int id)
   {
        if(m_gameBoards[id].checkForEnd()) {
            return true;
        }
        int i=0;
        for(i=0;i<7;i++){
            if (m_gameBoards[id].canBuyStock(i)){
                break;
            }
        }
        if(i==7){
            return false;
        }
        return true;
   }

   public static String getTextMsg(String name)
   {
       //System.out.println("as:gettextmsg ="+name);
        Player p=m_gameBoards[0].findPlayer(name);
         if (p==null)return "";

        if (p.getChatMsgStr()==null ||p.getChatMsgStr().length()==0)return "";
        String m=p.getChatMsgStr().replace('\n','#');
        if(m.length()>0){
              System.out.println("AqSSrvr:gettextmsg mmmmmm="+m);
        }
	    m= "\""+m+"\"";

	    p.resetChatMsgStr();
       //System.out.println("in getTextMsg= "+m);
        return m;
   }
   public static String generateChooseHotel(int id)
   {
        return m_gameBoards[id].generateChooseHotel();
   }
   public static String generateBuyHotel(int id)
   {
     //  System.out.println("ZZZZZZZZZZZZZZZZZZ id = " + id);
        return m_gameBoards[id].generateBuyHotel();
   }
   public static String generateSwapStock(int id)
   {
        return m_gameBoards[id].generateSwapStock();
   }
   public static String generateChooseOrder(int id)
   {
        return m_gameBoards[id].generateChooseOrder();
   }
   static void trace(String s)
   {
        System.out.println(s);
   }

     public static int processCmd(int gameId,int cmd,String name,String arg,
                                    StringBuffer outCmd,StringBuffer outArg,
                                    StringBuffer tile,StringBuffer msg)
    {
        StringBuffer chatText= new StringBuffer();
        return processCmd( gameId, cmd, name,arg,outCmd,outArg,tile, msg, chatText);

    }
   public static int processCmd(int gameId,int cmd,String name,String arg,
	                                StringBuffer outCmd,StringBuffer outArg,
	                                StringBuffer tile,StringBuffer msg,StringBuffer chatText)
    {
        AQC cmds[]=null;

        switch(cmd)
        {
              case 403:

                Player p3=m_gameBoards[gameId].findPlayer(name);
                if(p3==null){
                   return 0;
                }

                String m3=p3.getChatMsgStr();
	            if( m3.length()!=0){
                    System.out.println("in  processCmd 403 m = "+name+":" + m3);
                    return 1;
                }
                return 0;
              case 401:
                Player p2=m_gameBoards[gameId].findPlayer(name);
                String m2=p2.getMsgStr();
	            if( m2.length()!=0 || p2.getState()!=p2.getOldState() ||p2.getChatMsgStr().length()!=0){
                    p2.setOldState(p2.getState());
                    //System.out.println(" xxxxmsg = " + m2);
                    return 1;
                }
                return 0;




            case 400:
                Player p=m_gameBoards[gameId].findPlayer(name);
                String m=p.getMsgStr().replace('\n','#');
	            msg.append(m);
               // System.out.println(" xxxxxxxxxxxxxxxxxxxxxmsg = " + msg);
                 m=p.getChatMsgStr().replace('\n','#');
	            chatText.append(m);
                //.out.println(" xxxxxxxxxxxxxxxxxxxxxmsg = " + msg);
	            p.resetMsgStr();
                p.resetChatMsgStr();
	            tile.append(m_gameBoards[gameId].findPlayer(name).getRack());
                if(p.isRedrawRack()){
                    m_gameBoards[gameId].findPlayer(name).setRedrawRack(false);
                    outArg.append("R");
                }
                return p.getState();

           case AQC.BROADCAST_MESSAGE:
                 Player plyer=m_gameBoards[gameId].findPlayer(name);
                 //System.out.println("in broadcast  newz");

                 AQC ar =new AQC(plyer.m_id,arg,AQC.CHAT);
                 ar.setName(name);
                 fMsgThreadGroup.broadcastmsg(ar,gameId);
                 m_gameBoards[gameId].populateMessage2(ar.getMessage(),name);
                return 6;

            case AQC.GAMEBOARD_SWAP_STOCK:
                StringTokenizer s = new StringTokenizer(arg);
                int sell = Integer.parseInt(s.nextToken());
                int trade = Integer.parseInt(s.nextToken());
                int sur = Integer.parseInt(s.nextToken());
                int def = Integer.parseInt(s.nextToken());
                cmds = m_gameBoards[gameId].processMsg( new AQCSwapStock(m_gameBoards[gameId].findPlayer(name).getId(),
                                                                    gameId,sur,def,trade,sell),null,null);

                break;
            case AQC.GAMEBOARD_NEXT_TRANSACTION:
                 cmds = m_gameBoards[gameId].processMsg( new AQCNextTransaction(m_gameBoards[gameId].findPlayer(name).getId(),gameId),null,null);
                break;

            case AQC.GAMEBOARD_SIGN_IN:
                cmds = m_gameBoards[gameId].processMsg( new AQCSignIn( name,gameId,false),null,null);
                break;
             case AQC.GAMEBOARD_START:
               // System.out.println("GAMEBOARD_START name = " + name);
                cmds = m_gameBoards[gameId].processMsg( new AQCStart(
                                      m_gameBoards[gameId].findPlayer(name).getId(),gameId),null,null);
                break;
             case AQC.GAMEBOARD_PLAY_TILE:
                cmds = m_gameBoards[gameId].processMsg( new AQCPlayTile(
                                      m_gameBoards[gameId].findPlayer(name).getId(),gameId,new Tile(arg),name),
                                      null,null);
                m_gameBoards[gameId].findPlayer(name).setRedrawRack(true);
                break;
             case AQC.GAMEBOARD_BUY_HOTEL:
                outArg.append("R");
                m_gameBoards[gameId].findPlayer(name).setRedrawRack(true);
                int cnt= Integer.parseInt(arg.substring(0,1));
                AQCBuyHotel bh=new AQCBuyHotel( m_gameBoards[gameId].findPlayer(name).getId(),gameId,cnt==1);

                String str = "";
                for(int i=1;i<arg.length();i=i+2){
                    if(i==1){
                        str = name + " buys ";
                    }

                    int h= Integer.parseInt(arg.substring(i,i+1));
                    int a= Integer.parseInt(arg.substring(i+1,i+2));
                    str = str + a +" "+m_gameBoards[gameId].m_hot[h].getName()+" ";
                    bh.setHotelPurchase(h, a);
                }
                bh.setMessage(str);
                cmds = m_gameBoards[gameId].processMsg( bh,null,null);
                break;
             case AQC.GAMEBOARD_START_HOTEL:
                  int index =Integer.parseInt(arg);
                  String hName=m_gameBoards[gameId].m_hot[index].getName();
                  cmds = m_gameBoards[gameId].processMsg( new AQCStartHotel(
                                                     m_gameBoards[gameId].findPlayer(name).getId(),gameId,
                                                     index , hName,name),null,null);
                m_gameBoards[gameId].findPlayer(name).setRedrawRack(true);
                  break;
             case AQC.GAMEBOARD_MERGE_HOTEL:
                  int cnt2 =Integer.parseInt(arg.substring(0, 1));
                  int a[] = new int[cnt2];
                  for(int i=0;i<cnt2;i++){
                     a[i]=Integer.parseInt(arg.substring(i+1, i+2));
                  }
                  cmds = m_gameBoards[gameId].processMsg( new AQCMergeHotel(
                                                     m_gameBoards[gameId].findPlayer(name).getId(),gameId,
                                                     cnt2 , a),null,null);
                  break;
        }
       //System.out.println("nnnnnnnnnnn  nnnnnnnnnnn  namee = " + name);
        Player p= m_gameBoards[gameId].findPlayer(name);
        for(int i=0; i< cmds.length;i++){
            if(cmds[i].getMessage() !=null){
                m_gameBoards[gameId].populateMessage(cmds[i].getMessage());

		    }
		    if (cmds[i].getTarget() == AQC.ALL){
			    fMsgThreadGroup.broadcastmsg(cmds[i],gameId);
		    } else  {
			    if(cmds[i].getMessageTarget() == AQC.ALL){
			        fMsgThreadGroup.broadcastmsg(new AQC(cmds[i].getMessage()),gameId);
			        //m_gameBoards[0].populateMessage(cmds[i].getMessage());
			    }
			    m_gameBoards[gameId].getPlayer(cmds[i]).sendmsg(cmds[i]);

		    }
		    switch(cmds[i].getCode())
		    {
		        case AQC.CLIENT_INIT_TILES:
		        case AQC.CLIENT_REPLACE_TILE:
		        case AQC.CLIENT_INFO:
		            //tile.append(m_gameBoards[0].findPlayer(name).getRack());
		            outArg.append("R");
                    m_gameBoards[gameId].findPlayer(name).setRedrawRack(true);
		    }


	    }
	    //String m=p.getMsgStr().replace('\n','#');
	    //msg.append(m);
	    //p.resetMsgStr();
	    boolean getRack=true;
        if(cmds[0].getCode()== AQC.GAMEBOARD_SWAP_STOCK){
            getRack=false;
            processCmd(gameId,AQC.GAMEBOARD_NEXT_TRANSACTION,name,"",outCmd, outArg,tile, msg);

        }
	    int st=m_gameBoards[gameId].findPlayer(name).getState();
	   // trace("State ========= "+st);
	    if(st==3){
	        if(!canBuyStock(gameId)){
	            getRack=false;
	            st =processCmd(gameId,AQC.GAMEBOARD_BUY_HOTEL,name,"0",outCmd, outArg,tile, msg);
	        }
	    } else if(st==2){
	        String str =""+ m_gameBoards[gameId].findPlayer(name).getMergeNum();
	        int cnt = Integer.parseInt(str);
	        for(int i=0;i<cnt;i++){
	            str = str + m_gameBoards[gameId].findPlayer(name).getMergeList()[i];

	        }
	        getRack=false;
	        st =processCmd(gameId,AQC.GAMEBOARD_MERGE_HOTEL,name,str,outCmd, outArg,tile, msg);
	    }
	    if(getRack){
	        tile.append(m_gameBoards[gameId].findPlayer(name).getRack());
	    }
        return st;
    }
	public static String getStats(int id)
    {
        return m_gameBoards[id].getStats();
	}
	static public int getPlayerId(String name,int id)
    {
        return m_gameBoards[id].getPlayerId(name);

    }

    static boolean  hasGameStarted()
    {
        if(m_gameBoards[0] == null){
            return false;
        }
        return m_gameBoards[0].m_GameStarted;

    }
	public static int[] getHackGB(MessageFrame f, int gid)
	{
	    int t[]= new int[108];
	    if(m_gameBoards[gid] == null){
	         f.createRoom(0,gid);
	    }

	    int h=0;
	    for (int i = 0; i <9; i++) {

	        for (int j = 0; j < 12; j++) {
	            t[h++]=m_gameBoards[gid].m_tile[i][j].getState();
	        }
	    }
	    return t;
	}

    void checkExpiredGames()
    {
         synchronized (sync)
         {
             for ( int i=0;i<Preference.MAX_GAMES;i++){
                 if (m_gameBoards[i]!=null){
                     if(m_gameBoards[i].hasGameExpired()){
                          m_gameBoards[i] =null;
                          fMsgThreadGroup.broadcastmsg(new AQCLobbyDelete(i));
                          fMsgThreadGroup.broadcastmsg(new AQCGameAbort(i),i);
                          fMsgThreadGroup.removeExpiredGame(i);

                      }
                 }
            }
            fMsgThreadGroup.removeExpiredClients();
        }
    }
    int findPlayer(String name)
    {
        int i=0;
        for ( i=0;i<Preference.MAX_GAMES;i++){
            if (m_gameBoards[i]==null){
                continue;
            }
            if(m_gameBoards[i].findPlayer(name) !=null){
                return i;
            }
        }
        return -1;
    }
    int findObserverAndRemove(String name)
    {
        int i=0;
        for ( i=0;i<Preference.MAX_GAMES;i++){
            if (m_gameBoards[i]==null){
                continue;
            }
            if(m_gameBoards[i].findObserverAndRemove(name)){
                return i;
            }
        }
        return -1;
    }


    AQCLobbyUpdate createRoom(String fn)
    {

        int i=0;
        for ( i=0;i<Preference.MAX_GAMES;i++){
            if (m_gameBoards[i]==null){
                break;
            }
        }
        m_gameBoards[i]= startGame(fn,i);
        m_gameBoards[i].setGameId(i);
        return m_gameBoards[i].updateRoom(i);

    }
    AQCLobbyUpdate createRoom(int version,int gid)
    {
        int i=0;
        if(gid== -1){
            for ( i=0;i<Preference.MAX_GAMES;i++){
                if (m_gameBoards[i]==null){
                    break;
                }
            }
        } else {
            i = gid;
        }
        m_gameBoards[i]= startGame(version);
        m_gameBoards[i].setGameId(i);
        return m_gameBoards[i].updateRoom(i);

    }
    AQCLobbyUpdate getLobby(boolean b)
    {
        int cnt=0;
        for ( int i=0;i<Preference.MAX_GAMES;i++){
            if (m_gameBoards[i]!=null){
                cnt++;
            }
        }
        AQCRoom rooms[] = new AQCRoom[cnt];
        cnt=0;
        for ( int i=0;i<Preference.MAX_GAMES;i++){
            if (m_gameBoards[i]!=null){
                GameBoard gb  = m_gameBoards[i];
                rooms[cnt]=  gb.getGameRoom(i);
                cnt++;
            }

        }
        return new AQCLobbyUpdate(rooms,b);

    }

    public void removeGame(int gid){
        fMsgThreadGroup.setGameOver(gid);
        m_gameBoards[gid] =null;
    }

	// Allow the frame to send a message to its output pane
	public void printMessage( String message )
	{
	     int len = message.length();
//	     if( fTextSize > 4096 )
//		    fTextArea.replaceRange( "", 0, len-1 );
//	     else
//		    fTextSize += len;
//	     fTextArea.append( message + "\n" );
	}

    // non widget-targetted even handling
    public boolean handleEvent( Event evt )
    {
        if ( evt.id == Event.WINDOW_DESTROY )
        { // Window closed
		    dispose();
        }

        // pass event handling up to superclass for default behaviour
	    return super.handleEvent( evt );
    }

    // widget-based (menu-based) event handling
    public boolean action( Event evt, Object widget )
    {
	    // Demonstrates use of menu
	    if ( evt.target instanceof MenuItem )
	    {
	        if ( (String)widget == "Ping" ){
		        pingPlayers();
	        }
	    }

	    // pass action event handling to superclass for default behavior
	    return( super.action( evt, widget ) );
    }
    void setMessageThreadGroup( MessageThreadGroup m)
    {
	    fMsgThreadGroup = m;
    }
    void pingPlayers(int gid)
    {

        m_gameBoards[gid].setAllConnectionLost();
        fMsgThreadGroup.broadcastmsg( new AQCPing(),gid);
    }
    void pingPlayers()
    {
        for(int i=0;i<Preference.MAX_GAMES;i++){
            if( m_gameBoards[i] != null){
                m_gameBoards[i].setAllConnectionLost();
                fMsgThreadGroup.broadcastmsg( new AQCPing(),i);
            }
        }
    }
    void saveGame(int gid,String fn)
    {
        m_gameBoards[gid].saveGame(fn);
    }
    private void closeThreads()
    {
    	fDispatchThread.close();
	    try
	    {
            fDispatchThread.join();
	    } catch ( Exception e )
	    {
	        System.err.println( "Error closing Message thread: " + e );
	    }
    }

    // Make sure the thread owned by the frame is stopped
    public void dispose()
    {
        closeThreads();
		System.err.println( "Done closing threads." );
        super.dispose();
		System.exit(0);
    }

    // When showing the frame, start the thread and resize the frame
    public synchronized void setVisible(boolean b)
    {
	    if (!b){
		    super.setVisible(false);
		    return;
	    }
	    super.setVisible(true);
	    fDispatchThread = new MessageDispatcherThread(this);
	    Dimension size = fLayout.preferredLayoutSize( this );
	    setSize( size );
	    fDispatchThread.start();
    }
    GameBoard startGame(String fn,int gid)
    {
	    GameBoard gb= new GameBoard();
	    gb.initHotels();
	    gb.setMessageFrame(this);
	    gb.loadGame(fn,gid);
	    gb.setGameState(gb.SERVER);
	    return gb;
    }
    GameBoard startGame(int version)
    {

	    GameBoard gb= new GameBoard();
	    gb.setMessageFrame(this);
	    gb.setVersion(version);
	    gb.setGameState(gb.SERVER);
	    gb.initHotels();
	    gb.setAutoSave(m_autoSave);
	    return gb;
    }
    public void update( Graphics g )
    {

    }

    private void add( Component c, int x, int y, int w, int h, int fill, int anchor, double wx, double wy )
    {
	    fConstraints.gridx = x;
	    fConstraints.gridy = y;
	    fConstraints.gridwidth = w;
	    fConstraints.gridheight = h;
	    fConstraints.fill = fill;
	    fConstraints.anchor = anchor;
	    fConstraints.weightx = wx;
	    fConstraints.weighty = wy;

	    fLayout.setConstraints( c, fConstraints );
	    add( c );
    }

    void setSaveLabel(boolean b)
    {
	    m_canSave = b;
	    MenuItem x = m_fileMenu.getItem(2);
	    if (!b) {
	        x.setEnabled(b);
	    } else if(!m_autoSave) {
	        x.setEnabled(b);
	    }
    }
    void setLoadLabel(boolean b)
    {
	    MenuItem x = m_fileMenu.getItem(1);
	    x.setEnabled(b);
    }


    // The frame constructor sets the appropriate layout
    public MessageFrame( ) {
	    super( "Acquire Server" );

            // Add menu bar
	    MenuBar menuBar = new MenuBar();
	    setMenuBar( menuBar );

            // and a pair of related menu items, grouped under "File"
	    m_fileMenu = new Menu("File");
	    menuBar.add( m_fileMenu );
	    m_fileMenu.add( "Restart" );
	    m_fileMenu.add( "Load" );
	    m_fileMenu.add( "Save" );
	    m_fileMenu.add( "No Auto Save" );
	    m_fileMenu.add( "Ping" );
	    setSaveLabel(false);
	    //fileMenu.add( "Close" );


	// Overall layout is North - South
        fLayout = new GridBagLayout();
		fConstraints = new GridBagConstraints();
        setLayout( fLayout );



        // The scrollable text field goes at the bottom
        fTextArea = new TextArea();

	    add( fTextArea, 0, 8, GridBagConstraints.REMAINDER, 1,
	     GridBagConstraints.HORIZONTAL,
		 GridBagConstraints.CENTER,
		 1.0, 1.0 );
		 CheckExpiredGamesThread cegt= new CheckExpiredGamesThread(this);
		 cegt.start();

    }
}

// Extends applet
public class AcquireSrvr extends Panel {
    static MessageFrame fFrame=null;
    public static int[] getHackGB(int gid)
    {
        if(fFrame == null) return null;
        return fFrame.getHackGB(fFrame, gid);
    }
    public static boolean  hasGameStarted()
    {
         if(fFrame == null) return false;
         return fFrame.hasGameStarted();
    }

    public static int getPlayerId(String name,int id)
    {
        return fFrame.getPlayerId(name,id);
    }
    public static String getHTMLStocks()
    {
        if(fFrame == null) return "";
        return fFrame.getHTMLStocks();
    }
    public static String generateSwapStock(int id)
    {
        if(fFrame == null) return null ;
        String s =fFrame.generateSwapStock(id);
        return s;

    }
    public static String generateChooseOrder(int id)
    {
        if(fFrame == null) return null ;
        String s =fFrame.generateChooseOrder(id);
        return s;

    }
    public static String generateBuyHotel(int id)
    {
        if(fFrame == null) return null ;
        String s =fFrame.generateBuyHotel(id);
        return s;

    }

    public static String getTextMsg(String name)
    {
        if(fFrame == null) return null ;
        String s="";
        synchronized(fFrame.sync)
        {
            s =fFrame.getTextMsg(name);
        }
        return s;

    }

    public static String generateChooseHotel(int id)
    {
        if(fFrame == null) return null ;
        String s =fFrame.generateChooseHotel(id);
        return s;

    }
    public static int processCmd(int gameId,int cmd,String name,String arg,
                                    StringBuffer outCmd,StringBuffer outArg,
                                    StringBuffer tile, StringBuffer msg, StringBuffer chatText)
    {
        int r=6;
        if(fFrame == null) {
            System.out.println("fFrame==null");
            return 0 ;
        }
        //if(cmd==403) System.out.println("in processcmd ************ 403");
        synchronized(fFrame.sync)
        {

            r =fFrame.processCmd( gameId,cmd ,name,arg,outCmd,outArg,tile,msg,chatText);

        }
        return r;

    }

    public static String getStats(int id)
    {
        if(fFrame == null) return "";
        return fFrame.getStats(id);
    }
    public static String getHTMLStats()
    {
        if(fFrame == null) return "";
        return fFrame.getHTMLStats();
    }
    public static void hackMain( )
    {
        MessageFrame frame = new MessageFrame( );
		frame.setResizable( true );
		frame.setVisible(true);
		fFrame=frame;
    }
    public static void main( String[] args )
    {
        if (args.length == 1){
            MessageDispatcherThread.port = Integer.parseInt(args[0]);
        }
        //new Preference();
        if (args.length >= 2){
            Preference.VERSION= args[1];
        }
        MessageFrame frame = new MessageFrame( );
		frame.setResizable( true );
		frame.setVisible(true);
		fFrame=frame;
    }



    // just need to create the frame and show it
    public void init()
    {
	fFrame = new MessageFrame( );
	fFrame.setResizable( true );
	fFrame.setVisible(true);
    }

    // and, of course, dispose of it
    public void destroy()
    {
	fFrame.dispose();
    }


	class SymContainer extends java.awt.event.ContainerAdapter
	{
		public void componentAdded(java.awt.event.ContainerEvent event)
		{
			Object object = event.getSource();
			if (object == AcquireSrvr.this)
				AcquireSrvr_ComponentAdded(event);
		}
	}

	void AcquireSrvr_ComponentAdded(java.awt.event.ContainerEvent event)
	{
		// to do: code goes here.
	}
}
