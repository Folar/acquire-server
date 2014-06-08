package ackgames.acquire;


import java.awt.*;
import java.io.*;
import java.util.Vector;

/***********************************************************************

	    How Stock swapping works

in AcquireSrvr, once Tile merge is done gameboard::firstSwap is called
by then swap queue is set up

firstSwap calls   gameboard.swapStockTransaction which generates a
SWAPFRAME msg to Client
once in the Client it calls gameboard.swapHotels  which calls
gameboard.swapStock  which brings up the swap frame

the swap frame calls gamboard.swapper which sends the SWAPSTOCK msg to
server.

the server then calls gameboard.swapPlayerStock. the SWAPSTOCK msg
gets routed to all clients. if Client = current_player then send NEXTTRANS
to server.

the server calls nexttrans which calls gameboard.swapStockTransaction if
more transactions exists else put current Player into buymode


*******************************************************************/

public class GameBoard
{
    private boolean m_newBuyMethod = true;
    public Tile m_tile[][] = new Tile[9][12];
    private Tile m_tileBag[] = new Tile[108];
    private int m_availTiles = 108;
    public Hotel m_hot[];
    private Tile m_tilesSafe[] = new Tile[4];
    private int m_cSafe;
    private Tile m_tilesExamine[] = new Tile[4];
    private int m_cExamine = 0;
    private int m_tradeCnt;
    private int m_tradeIndex;
    private int m_current;
    StockTransaction m_stockTrade[] = new StockTransaction[18];
    int m_playerNum = 0;
    public AcquireStats m_statPanel;
    public DistributionPanel m_stockPanel;
    OrderFrame2 m_orderFrame;
    public SwapFrame2 m_swapFrame;
    int m_gameState2 = 6;
    public static final int PLACETILE = 0;
    public static final int STARTCHAIN = 1;
    public static final int MERGE = 2;
    public static final int BUYSTOCK = 3;
    public static final int GAMEOVER = 4;
    public static final int NEWGAME = 5;
    public static final int OTHER = 6;
    public static final int SERVER = 7;
    public static final int SWAP = 8;
    public static final int CHOOSE_ORDER = 9;
    int m_row;
    int m_column;
    int m_sharesBrought = 0;
    int m_bhits = 0;
    public int m_currentPlayer = 0;
    boolean m_okToBuy= true;
    Vector m_observers = new Vector();
    boolean m_bObserving = false;
    boolean m_bPlaying = true;
    public boolean m_GameStarted = false;
    int m_gameState22 = 6;
    public Player m_players[] = new Player[6];
    String m_bonusWinners[] = new String[3];
    Tile dummyTile;
    int m_winner;
    Tile m_starterTile[] = new Tile[6];
    Tile m_startTile;
    int m_startPlayer;
    int m_startNum = 0;
    int m_version =0;
    Board m_board = null;
    public int m_gameId = -1;
    String m_gameInfo = null;
    String m_tileStr[] = new String[6];
    boolean m_connectionLost = false;
    boolean m_autoSave = false;
    MessageFrameI m_msgFrame = null;
    AQCGameBoard m_gameObject = new AQCGameBoard();
    long lastAccess;

    public boolean hasGameExpired()
    {
        return ( (System.currentTimeMillis() - lastAccess)> Preference.TIME_OUT);
    }
    public synchronized AQC[] processMsg( AQC cmd, DataOutputStream os,DataInputStream dis)
    {
        lastAccess = System.currentTimeMillis();
        switch(cmd.getCode()){
            case AQC.GAMEBOARD_START:
                 //System.out.println("gb:GAMEBOARD_START name = " + AQCFactory.getAQCStart(cmd).getName());
               return startPlayer(AQCFactory.getAQCStart(cmd));
            case AQC.GAMEBOARD_PLAY_TILE:
               return playTile(AQCFactory.getAQCPlayTile(cmd));
            case AQC.GAMEBOARD_BUY_HOTEL:
               return nextPlayer(AQCFactory.getAQCBuyHotel(cmd));
            case AQC.GAMEBOARD_START_HOTEL:
               return startHotel(AQCFactory.getAQCStartHotel(cmd));
            case AQC.GAMEBOARD_SIGN_IN:
                //System.out.println("gb:GAMEBOARD_SIGN_IN name = " + AQCFactory.getAQCSignIn(cmd).getName());
               return newPlayer(AQCFactory.getAQCSignIn(cmd),os);
            case AQC.GAMEBOARD_NEXT_TRANSACTION:
               return nextTrans();
            case AQC.GAMEBOARD_MERGE_HOTEL:
               return mergeHotels(AQCFactory.getAQCMergeHotel(cmd));
            case AQC.GAMEBOARD_SWAP_FRAME:
               swapHotels(AQCFactory.getAQCSwapFrame(cmd));
               break;
            case AQC.GAMEBOARD_SWAP_STOCK:
               return trade(AQCFactory.getAQCSwapStock(cmd));
            case AQC.GAMEBOARD_END_GAME:
               return chooseWinner(AQCFactory.getAQCBuyHotel(cmd));
            case AQC.CLIENT_ACK:
               return ackPlayer(AQCFactory.getAQCACKClient(cmd));
            case AQC.GAMEBOARD_HIDE_FRAMES:
               hideSwapMergeFrame();
               break;

        }
        return null;
    }

    public void setMessageFrame(MessageFrameI m)
    {
        m_msgFrame=m;
    }
    public void setVersion(int v)
    {
        m_version =v;
    }
    public int getVersion()
    {
        return m_version;
    }
    public void setGameStarted( boolean b)
    {
	    m_GameStarted=true;
    }
     public boolean getGameStarted()
    {
	    return m_GameStarted;
    }
    public void setBoard(Board  b)
    {
	m_board = b;
    }

    public void setAutoSave( boolean b)
    {
	    m_autoSave = b;
    }

    public String generateSwapStock()
    {
        int survivor = m_st.getSurvivor();
	    int defunct = m_st.getDefunct();
        int cntSurvivor = m_hot[survivor].getAvailShares();
	    int cntDefunct = m_players[ m_st.getPlayer()].m_hotels[defunct];
        String state1 =getHotelColor(survivor);
        String state2 =getHotelColor(defunct);
        String str= "<TABLE  bgcolor="+state1+" cellspacing=\"8\" ><tr><td  bgcolor="+state2+" id=\"LFrame\" valign=\"top\"><FORM name=\"xxx\">";

        str+="<h5 ALIGN=\"CENTER\" STYLE=\"color:black;font-family:sanserif\">Swap Stocks</h5>";

		str += "keep <SELECT NAME =\"keep\" ID=\"keep\" onChange=\"calcKeep();\">" ;
            for(int j=0;j<=cntDefunct;j++){
                if(j== cntDefunct){
                    str += "<OPTION VALUE=\"keep"+j+"\" SELECTED>"+j;
                }else{
                    str += "<OPTION VALUE=\"keep"+j+"\">"+j;
                }
            }
        str += "</SELECT> ";

        int n = cntDefunct;
	    if (cntDefunct/2 > cntSurvivor)
	    {
		    n = 2* cntSurvivor;
	    }
	    str += "trade <SELECT NAME =\"trade\" ID=\"trade\"  onChange=\"calcTrade();\">" ;
	    for (int i = 0; i<= n; i++){
		    if (i%2 == 0)
		        str += "<OPTION VALUE=\"trade"+i+"\">"+i;
	    }
	    str += "</SELECT> ";
        str += "sell <SELECT NAME =\"sell\" ID=\"sell\"  onChange=\"calcSell();\">" ;
            for(int j=0;j<=cntDefunct;j++){
                str += "<OPTION VALUE=\"sell"+j+"\">"+j;
            }
        str += "</SELECT> &nbsp &nbsp";
        str+= "<input type=\"button\" value=\"Trade\" onClick=\"trade22();\"> </td><td>&nbsp &nbsp</td><td id=\"RFrame\">";

        str +=m_st.getBonusStr()+ "<br>";
        Integer I1 = new Integer(m_hot[survivor].price());
	    Integer I2 = new Integer(m_hot[defunct].firstBonus()/10);
	    Integer A1 = new Integer(m_hot[survivor].getAvailShares());
	    Integer A2 = new Integer(m_players[m_st.getPlayer()].m_hotels[defunct]);

	    String stockWorth = "a share of " +
				m_hot[survivor].getName() +
				"(survivor) is now worth " + I1.toString()+
				".<BR> There are now " + A1.toString() +
				" shares available. ";
	    str+= stockWorth+"<BR>";
	    int defunctPrice = I2.intValue();
	    stockWorth = "a share of " +
			   m_hot[defunct].getName() +
				"(defunct) was worth " + I2.toString() +
				". " +m_players[ m_st.getPlayer()].getName() +
				" you have " + A2.toString() + " shares";
		str= str +stockWorth;

         str= str +"<input type=\"HIDDEN\" name=\"traders\" id=\"trader\" value=\""+m_st.getPlayer() +"\" > &nbsp";
        str= str +"<input type=\"HIDDEN\" name=\"survivor\" id=\"survivor\" value=\""+survivor+"\" > &nbsp";
        str= str +"<input type=\"HIDDEN\" name=\"defunct\" id=\"defunct\" value=\""+defunct+"\" > &nbsp";
        str= str +"<input type=\"HIDDEN\" name=\"defunctPrice\" id=\"worth\" value=\""+defunctPrice+"\" > &nbsp";
		str= str +"</FORM></td></tr></tabel>";
        return str;
    }
    public String generateChooseHotel()
    {
        String str="<TABLE><tr>";
         for(int i=0;i<7;i++){
            if(m_hot[i].price()!=0){
                continue;
            }
              String state = "\"white\"";
	        switch(i)
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
            }
             str+="<TD>";
             str+="<input type=\"button\" value=\""+m_hot[i].getName() +"\" STYLE=\"background-color:"+state+"\" onClick=\"startH("+i+");\">";

             str+="</TD>";
         }
        str+="</TR></TABLE>";
        return str;

    }
    public String generateBuyHotel()
    {
        String str="";
        Player m_player= m_players[m_currentPlayer];

        for(int i=0;i<7;i++){
            if(!canBuyStock(i)){
                continue;
            }
            int m_max =  m_player.getMoney()/m_hot[i].price();
            m_max = m_max>= 3?3:m_max;
            m_max = m_max>= m_hot[i].getAvailShares()? m_hot[i].getAvailShares():m_max;
            if(m_max ==0){
                continue;
            }
            String h=m_hot[i].getName();
            str += "<span class=\""+h+"\">"+h+"<SELECT class=\""+h+"\" NAME =\""+h+"\" ID=\"h"+i + "\" size=1 onChange=\"calc("+i+");\">" ;
            for(int j=0;j<m_max+1;j++){
                str += "<OPTION VALUE=\""+h+j+"\">"+j;
            }
            str += "</SELECT></span>&nbsp ";

        }
        if(checkForEnd()){

            str += "&nbsp <input type=\"button\" value=\"End Game\" onClick=\"buy(1);\">&nbsp";
        }

        return str;
    }

    public AQC nextPerson()
    {
        AQCBuyHotel bh = new AQCBuyHotel(m_currentPlayer,0,false);
		m_wallet.setAQCBuyHotel(bh,m_players[m_currentPlayer].getName());
		m_wallet.reset();
		setGameState(OTHER);
		return bh;
    }

    public void setPlayerNum (int i)
    {
	    m_playerNum = i;
    }

    public Player getPlayer(AQC cmd)
    {
        int id = cmd.getCurrentPlayerID();
        if (cmd.getObserver()){
            return (Player)m_observers.elementAt(id);
        }
        return m_players[id];
    }

    public Player otherPlayer(String n,int id,int m)
    {
        if (m_playerNum<(id +1))
	        m_playerNum = id+1;
	    m_players[id] = new Player(n,id,this);
	    m_players[id].setMoney(m);
	    return m_players[id];
    }
    public void setClientPlayer(int id)
    {
	    m_playerNum = id+1;
	    //System.out.println("ID=============="+id);
	    m_currentPlayer = id;
	    m_players[m_currentPlayer] =m_players[0] ;
	    m_swapFrame.setPlayer(m_players[id]);
    }
    public Player initClientPlayer(String n,DataOutputStream os)
    {
		m_currentPlayer = 0;
		m_players[m_currentPlayer] = new Player(n,os,this,0);
		m_playerNum = 1;
		return m_players[m_currentPlayer];
    }

    public void swapper(int id,int survivor, int defunct, int swap, int sell)
    {
        AQCSwapStock ass= new AQCSwapStock(id,0,survivor,defunct,swap,sell);
		m_players[m_currentPlayer].sendmsg( ass);
    }

    public boolean isConnectionLost()
    {
        return m_connectionLost;
    }
    public void setConnectionLost(boolean b)
    {
        m_connectionLost=b;
    }
    public void setAllConnectionLost()
    {
        for (int i = 0; i< m_playerNum; i++) {
            m_players[i].setLostConnection(true);
        }
        setConnectionLost(true);
    }

    public int getPlayerId(String name)
    {
        if(!m_GameStarted){
            return -1;
        }
         for (int i = 0; i< m_playerNum; i++)
	    {
	        if (name.equals(m_players[i].m_name))
	        {
		        return i;
	        }
	    }
        return -2;

    }

    public AQC[] ackPlayer(AQCACKClient arg)
    {
         int ii=0;
         m_players[arg.getCurrentPlayerID()].setLostConnection(false);
	     for (ii = 0; ii< m_playerNum; ii++) {
		    if (m_players[ii].isLostConnection()) {
		        break;
		    }
	     }
	     if (ii == m_playerNum) {
		     setConnectionLost(false);
	     }
	     AQC msgs[] = {arg};
	     return msgs;
    }
    public AQCRoom getGameRoom(int gid)
    {
        boolean b=  m_GameStarted || (m_playerNum == 6);
        String pList = "";
        String kList = "";
        for(int i=0; i<m_playerNum;i++){
            pList += m_players[i].getName();
            if (i < (m_playerNum -1)){
                pList +=",";
            }
        }
        for(int i=0,sz =m_observers.size();i<sz;i++)
        {
            kList += ((Player)(m_observers.elementAt(i))).getName();
            if (i < (sz-1)){
                kList +=",";
            }
        }
        return new AQCRoom(gid,b,pList,kList,getVersion());
    }
    public AQC[] newPlayer(AQCSignIn arg,DataOutputStream oStream)
    {
		boolean bObserving = false;
	    AQC msgs[]=null;
	    if(m_playerNum == 0)
	        setConnectionLost(false);
	    if ( m_GameStarted || findPlayer(arg.getName()) !=null|| m_playerNum == 6 || arg.getObserver()) {
	        bObserving = true;
	    } else {
	        m_players[m_playerNum]= new Player(arg.getName() ,oStream,this, m_playerNum);
	        if(getVersion() ==  AQC.CENTRAL_AMERICA){
                m_players[m_playerNum].setMoney(8000);
            }
	        m_players[m_playerNum++].pickFirstTiles();
	        gameInfo(m_gameObject);
	    }
	    if (bObserving) {


	        String info = null;
	        int id = -1;
	        for (int i = 0; i< m_playerNum; i++) {
		        if (arg.getName().equals(m_players[i].getName()) &&
		            (m_players[i].isLostConnection()||m_players[i].isDHTMLClient() )) {
		            id = i;
		            bObserving = false;
		            m_players[i]= new Player(arg.getName(),oStream,this, i);
		            m_players[i].setLostConnection(false);
		            break;
		        }
	        }
	        if (bObserving){
	            m_observers.addElement (new Player(arg.getName(),oStream,this, 100));
	            msgs= new AQC[2];
	            AQCGameBoard go = new AQCGameBoard();
		        gameInfo(go);
		        msgs[0] = new AQCGameInfo(go);
		        msgs[0].setObserver(true);
		        msgs[0].setCurrentPlayerID(m_observers.size()-1);
		        msgs[1] = updateRoom(arg.getGameID());
	        } else {
	            int ii=0;
	            for (ii = 0; ii< m_playerNum; ii++) {
		            if (m_players[ii].isLostConnection()) {
		                break;
		            }
	            }
	            if (ii == m_playerNum) {

	                System.out.println("RESET BOARD INFO");
		            msgs = resetBoardInfo();
	            }

	        }

	    } else {
	        String names[]= new String[m_playerNum];
	        for (int i = 0; i< m_playerNum; i++) {
		        names[i]= m_players[i].getName();
		    }
		    msgs= new AQC[2];
		    msgs[0] = new AQCAddPlayer(m_playerNum,names,arg.getMessage(),arg.getGameID(),getVersion() );
		    msgs[1] = updateRoom(arg.getGameID());
	    }
	    return msgs;
    }

    public AQCLobbyUpdate updateRoom(int gid)
    {
         AQCRoom rooms[] = new AQCRoom[1];
		 rooms[0]= getGameRoom(gid);
		 return new AQCLobbyUpdate(rooms);
    }


    public int getGameId()
    {
        return m_gameId;
    }
    public void setGameId(int id)
    {
        m_gameId = id;
    }

    AQC[] resetBoardInfo()
    {
        System.out.println("in resetBoardInfo");
        //m_gameObject.setGameID(m_gameId);
        AQCGameInfo agi = new AQCGameInfo(m_gameObject);
		agi.setMessage(m_players[m_currentPlayer].m_name+ " will start from the beginning of his/her turn" );
		agi.setMessageTarget(AQC.CURRENT);
        agi.setMessageType(AQC.GAME);
		AQC msgs[] = new AQC[m_playerNum +m_observers.size()+1];
	    for (int i=0; i<m_playerNum;i++) {
	        AQCGameInfo gi = new AQCGameInfo(agi);
	        gi.setCurrentPlayerID(i);
	        gi.setRackTiles(m_gameObject.getPlayerTiles()[i]);
	        Tile t[]=gi.getRackTiles();
	        for(int j=0;j<6;j++){
	             m_players[i].getM_tiles()[j] = dummyTile;
	        }
	        for(int j=0;j<t.length;j++){
	             m_players[i].getM_tiles()[j] = m_tile[t[j].getRow()][t[j].getColumn()];
	        }
	        for(int j=0;j<7;j++){
	             m_players[i].m_hotels[j]=m_gameObject.getOwnShares()[i][j];
	        }
	        m_players[i].setMoney(m_gameObject.getMoney()[i]);
	        msgs[i]=gi;
	    }
	    int k = m_playerNum;
	    for (int i=0; i<m_observers.size();i++) {
	        msgs[k] = new AQCGameInfo(agi);
	        msgs[k].setObserver(true);
		    msgs[k++].setCurrentPlayerID(i);
	    }
	    if (allNonPlayable ( m_players[m_currentPlayer].getM_tiles())){
	         msgs[msgs.length-1]= new AQCBuyState(m_currentPlayer,m_players[m_currentPlayer].getName());
	         m_players[m_currentPlayer].setState(BUYSTOCK);
	    } else {
	         msgs[msgs.length-1]= new AQCPlaceState(m_currentPlayer,m_players[m_currentPlayer].getName());
	          m_players[m_currentPlayer].setState(PLACETILE);
	    }

	    setConnectionLost(false);
        return msgs;
    }



    boolean IsObserving()
    {
	if (m_bObserving) {
	    m_bObserving = false;
	    return true;
	}
	return false;
    }

    public void gameInfo(AQCGameBoard gameObject)
    {
        gameObject.setGameID(m_gameId);
        gameObject.setGameStarted(m_GameStarted);
        gameObject.setVersion(getVersion());
        gameObject.setTileState(m_tile);
        gameObject.setCurrentPlayer(m_currentPlayer);
        Tile availableTiles[] = new Tile[m_availTiles];
	    int availableShares[] = new int[7];
	    int money[] = new int[m_playerNum];
	    int ownShares[][] = new int[m_playerNum][7];
	    String names[] = new String[m_playerNum];
	    Tile playerTiles[][] = new Tile[m_playerNum][6];
	    // avail Hotel shares
	    for (int i = 0; i <7; i++) {
	        availableShares[i] = m_hot[i].getAvailShares();
	    }
        gameObject.setAvailabeShares(availableShares);

	    for (int i = 0; i <m_playerNum; i++) {
	        names[i]= m_players[i].m_name;
	        money[i]=m_players[i].m_money;
	        for (int j = 0; j<7; j++)
	        {
		        ownShares[i][j] =m_players[i].m_hotels[j];
	        }
	        for (int k = 0; k<6; k++)
	        {
	            Tile t = m_players[i].getM_tiles()[k];
	            playerTiles[i][k]= new Tile(t.getRow(),t.getColumn());
	        }
	        gameObject.setNames(names);
	        gameObject.setMoney(money);
	        gameObject.setOwnShares(ownShares);
	        gameObject.setPlayerTiles(playerTiles);
	    }
	    for (int i = 0; i <availableTiles.length; i++) {
	        availableTiles[i] = new Tile(m_tileBag[i].getRow(),m_tileBag[i].getColumn());
	    }
	    gameObject.setAvailableTiles(availableTiles);

    }

    public void resetGame()
    {
	    for (int i = 0; i <9; i++) {

	        for (int j = 0; j < 12; j++) {
	        m_tile[i][j].setState(Tile.EMPTY);
                 m_tile[i][j].setM_inRack(false);
	        }
	    }
	    for (int i = 0; i <7; i++) {
	        m_hot[i].setAvailShares(25);
	    }
	    m_playerNum=0;


    }

    public void reproduceGameInfo(AQCGameInfo agb)
    {
	    //m_tile= agb.getTileState();
	    Tile t[][]= agb.getTileState();
	    for (int i = 0; i <9; i++) {
	        for (int j = 0; j < 12; j++) {
		        m_tile[i][j].m_mergeTile=false;
		        m_tile[i][j].setState(t[i][j].getState());
	        }
	    }
	    for (int i = 0; i <7; i++) {

	        m_hot[i].setAvailShares(agb.getAvailabeShares()[i]);
	    }
	    int cnt = agb.getPlayerCount();
	    for (int i = 0; i <cnt; i++) {

	        m_players[i].m_name = agb.getNames()[i];
            m_players[i].m_money = agb.getMoney()[i];
	        for (int j = 0; j<7; j++)
	        {
		        m_players[i].m_hotels[j] = agb.getOwnShares()[i][j];
	        }

	    }

    }
    public boolean m_playingTile=false;
    AQC[] playTile(AQCPlayTile msg)
    {
	   traceError("start of playTile");
	    int col;
	    int row;
	    int x;
	    Tile t = msg.getTile();
	    row = t.getRow();
	    col=t.getColumn();
	    placeTile(row,col,msg);
	    x = m_stockPanel.viewing();
	    if (x != -1) {
	        showStock(x);
	    }
	    m_statPanel.updateSize();

	    if( getGameState() == SERVER) {
	        for ( int i = 0;i<6; i++) {
		        if(m_players[m_currentPlayer].getM_tiles()[i].getRow() == row &&
		            m_players[m_currentPlayer].getM_tiles()[i].getColumn() == col) {
		            m_players[m_currentPlayer].getM_tiles()[i] = dummyTile;
		            break;
		        }
	        }
	    }
	    traceError("end of playTile");
	    m_playingTile=false;
	    AQC msgs[] = {msg};
	    return msgs;
    }


    public Player findPlayer(String name)
    {
       // System.out.println("findPlayer ========= name = " + name);
        for (int i = 0; i< m_playerNum; i++)
	    {
	        if (name.equals(m_players[i].m_name))
	        {
		        return m_players[i];
	        }
	    }
	    return null;
    }
    public boolean findObserverAndRemove(String name)
    {
        for (int i = 0; i<m_observers.size(); i++)
	    {
	        Player p=(Player)(m_observers.elementAt(i));
	        if (name.equals(p.getName()))
	        {
	            m_observers.removeElementAt(i);
		        return true;
	        }
	    }
	    return false;
    }

    public boolean lostConnect(String name)
    {
	    for (int i = 0; i< m_playerNum; i++)
	    {
	        if (name.equals(m_players[i].m_name))
	        {
	            setConnectionLost(true);
		        m_players[i].setLostConnection(true);
		        return true;
	        }
	    }
	    for (int i = 0; i<m_observers.size(); i++)
	    {
	        Player p=(Player)(m_observers.elementAt(i));
	        if (name.equals(p.getName()))
	        {
	            m_observers.removeElementAt(i);
		        return false;
	        }
	    }
	    return false;
    }




    public void showStock(int h)
    {
	    int hold[] = new int[m_playerNum];
	    for (int i = 0; i< m_playerNum; i++)
	    {
	        hold[i] = m_players[i].m_hotels[h];
	    }
	    m_stockPanel.setFrame(h,hold);


    }





    public void setStatsDuringSwap(int survivor,int defunct,int keep,
                            int swap,int sell,int defunctPrice)
    {
        int money= m_players[m_currentPlayer].getMoney() + sell * defunctPrice;
        m_statPanel.setStatsDuringSwap(survivor,defunct,keep,
	                   swap/2 + m_players[m_currentPlayer].m_hotels[survivor],
	                   sell,m_currentPlayer,money);

	    m_wallet.m_moneyLabel.setText("$"+Integer.toString(money));
	    int hold[] = new int[m_playerNum];

	    if (m_stockPanel.viewing() == survivor){
	        for (int i = 0; i< m_playerNum; i++)
	        {
	            hold[i] = m_players[i].m_hotels[survivor];
	        }
	        hold[m_currentPlayer] = swap/2 +
	                                m_players[m_currentPlayer].m_hotels[survivor];
	        m_stockPanel.setFrame(survivor,hold);
	    }
	    if (m_stockPanel.viewing() == defunct){
	        for (int i = 0; i< m_playerNum; i++)
	        {
	            hold[i] = m_players[i].m_hotels[defunct];
	        }
	        hold[m_currentPlayer] = keep;
	        m_stockPanel.setFrame(defunct,hold);
	    }

    }
    public void showStat()
    {
	    m_statPanel.updateStats();
	}






   public void swapHotels(AQCSwapFrame arg)
   {
	    StockTransaction trans = arg.getStockTransaction();

	    if (Preference.BEEP && getGameState() == OTHER ){
			Toolkit.getDefaultToolkit().beep();
		}
	    swapStock(trans);

    }


    public void setMoney(AQCSetMoney cmd)
    {
	    m_players[cmd.getPlayerID()].setMoney(cmd.getAmount());
	    showStat();
    }
    public AQC[] startHotel(AQCStartHotel arg )
    {
	    startChain(arg.getHotelIndex(),arg.getCurrentPlayerID());
	    AQC x[]={arg};
	    return x;
    }

    public int[] sortPlayers(Tile startTiles[])
    {
        return sortPlayers(startTiles,startTiles.length);
    }

    public int[] sortPlayers(Tile startTiles[],int playerNum)
    {
        Player players2[] = new Player[6];
        m_stockPanel.initPlayer2();
        int sorted[]= new int[playerNum];
        Tile temp;
        int iTemp;

        for (int i = 0; i <playerNum ;i++) {
            sorted[i]=i;
        }
        for (int i = 0; i <playerNum - 1;i++) {
	        for ( int j = i+1; j< playerNum ;j++) {
	            if(startTiles[i].compare(startTiles[j])== 1){
	                iTemp=sorted[i];
	                sorted[i] = sorted[j];
	                sorted[j] = iTemp;
			        temp = startTiles[i];
			        startTiles[i] = startTiles[j];
			        startTiles[j] = temp;
		        }
	        }
	    }

	    boolean f = true;
	    for (int i = 0; i <playerNum ;i++) {
	         players2[i]= m_players[sorted[i]];

	         m_stockPanel.m_players2[i]= m_stockPanel.m_players[sorted[i]];

	         players2[i].setM_id(i);
	         if(m_currentPlayer == sorted[i]&& f){
	            m_currentPlayer = i;
	            f= false;

	         }
	         m_statPanel.setName(i,players2[i].getName());

        }
        m_players = players2;
        for (int i = 0; i <playerNum ;i++) {
            if(i== 0){
                m_players[0].setState(PLACETILE);
            }else {
                m_players[i].setState(OTHER);
            }
        }
        m_swapFrame.setPlayer(m_players[m_currentPlayer]);
        m_stockPanel.m_players= m_stockPanel.m_players2;
	    return sorted;
    }

    AQC[] startPlayer(AQCStart cmd)
    {
	    String bstr;
	    m_startNum++;
	    int id = cmd.getCurrentPlayerID();
	    String name = m_players[id].getName();
	    Tile t =null;
	    if(getVersion() == AQC.CENTRAL_AMERICA){
	        t = pickATile2();
	    }else{
	        t = pickATile();
	    }
	    m_starterTile[id]=t;
	    AQC msg[]= null;
	    if (m_startNum == m_playerNum) {
	        msg = new AQC[4];
	    }else {
	        msg = new AQC[2];
	    }
	    msg[0]= new AQCInitTiles(id,m_players[id].getM_tiles(),m_starterTile); // nulls included
         msg[1]= new AQC(id,m_players[id].getName() + " presses the start button and draws tile "+t.getLabel(),AQC.BROADCAST_MESSAGE);
        msg[1].setTarget(AQC.ALL);
	    if (m_startNum == m_playerNum) {
	         for (int i = 0; i< m_playerNum; i++) {
	            if(getVersion() != AQC.CENTRAL_AMERICA){
		            setTileState(m_starterTile[i].getRow(),
			            m_starterTile[i].getColumn(),
			            Tile.ONBOARD );
                }

	        }

	        msg[2]= new AQCStartTile( m_players[0].getId(),m_starterTile, m_players[0].getName());
	        sortPlayers(m_starterTile,m_playerNum);
	        msg[2].setMessage(m_players[0].getName() + " goes first");
	        msg[2].setCurrentPlayerID(m_players[0].getId());
	        for (int i = 0; i< m_playerNum; i++) {
	            if (name.equals(m_players[i].getName())){
	                msg[0].setCurrentPlayerID(i);
	                break;
	            }
	        }
	        setGameStarted(true);
	        gameInfo(m_gameObject);
	        m_currentPlayer =0;
	        msg[3]= updateRoom(cmd.getGameID());
	    }

	    return msg;


    }
    /*
    boolean isReadyToStart()
    {
	    if (m_startNum == m_playerNum) {
	        setGameStarted(true);
	        return true;
	    }
	    return false;
    }
    */
    public void pp(String s) {
	   // System.err.println( s );
    }
    public Wallet m_wallet;
    public void setWallet(Wallet w)
    {
        m_wallet = w;
    }


    public void setTileState(int row,int col,int state)
    {
	    m_row = row;
	    m_column = col;
	    m_tile[row][col].setState(state);
    }

    public void setPlaying ( boolean s)
    {
        m_bPlaying = s;
    }
    synchronized int getGameState22 ( )
    {

        return m_gameState22 ;
    }
    public synchronized int getGameState ( )
    {

        return m_gameState2 ;
    }
    synchronized public void setGameState ( int s)
    {
        traceError("*** Setting Game State "+s);
        if (s == BUYSTOCK && m_newBuyMethod){
            m_wallet.setup();
        }
        m_gameState2 = s;
        m_gameState22=2;
    }
    Tile pickATile()
    {
	    Integer I = new Integer(m_availTiles);
	    if (m_availTiles <= 0) return null;
	    int p = (int)Math.floor(Math.random() * m_availTiles);
	    if (p == m_availTiles) return pickATile();
            m_availTiles--;
	    Tile x = m_tileBag[p];
	    for ( int i = p;i<m_availTiles; i++)
	        m_tileBag[i] =m_tileBag[i + 1];
        return x;
    }
    int xxx=0;
    Tile pickATile2()
    {

	    Integer I = new Integer(m_availTiles2);
	    if (m_availTiles2 <= 0) return null;
	    int p = (int)Math.floor(Math.random() * m_availTiles2);
	    if (p == m_availTiles2) return pickATile2();
            m_availTiles2--;
	    Tile x = m_tileBag2[p];
	    for ( int i = p;i<m_availTiles2; i++)
	        m_tileBag2[i] =m_tileBag2[i + 1];
        return x;

    }

    public GameBoard()
    {
	    m_startTile = new Tile();
	    m_startTile.setTile(14,14);


	    m_orderFrame = new OrderFrame2 (this);
	    m_swapFrame = new SwapFrame2(this);


	    /// init stock
	    for (int i=0 ;i<18; i++) {
	        m_stockTrade[i] = new StockTransaction();
	    }
	    initTiles();
    }

    public void initHotels() {
        m_hot = new Hotel[7];
        m_hot[Hotel.LUXOR] = new Hotel(Hotel.LUXOR,"Luxor",this);
        m_hot[Hotel.TOWER] = new Hotel(Hotel.TOWER,"Tower",this);
        m_hot[Hotel.AMERICAN] = new Hotel(Hotel.AMERICAN,"American",this);
        m_hot[Hotel.FESTIVAL] = new Hotel(Hotel.FESTIVAL,"Festival",this);
        m_hot[Hotel.WORLDWIDE] = new Hotel(Hotel.WORLDWIDE,"WorldWide",this);
        m_hot[Hotel.CONTINENTAL] = new Hotel(Hotel.CONTINENTAL,"Continental",this);
        m_hot[Hotel.IMPERIAL] = new Hotel(Hotel.IMPERIAL,"Imperial",this);
	    m_statPanel = new AcquireStats (this);
	    m_stockPanel = new DistributionPanel (this);
    }

   /* void newStockFrame() {
	    m_stockPanel.buildFrame(this);
    }*/

    public void resetDistributionPanel()
    {
        m_stockPanel.resetDistributionPanel();
        m_statPanel.setPlayerCount(this);
        m_statPanel.updateStats();
    }
    public void resetStatPanels()
    {
        m_stockPanel.buildFrame(this);
        showStock(0);
        m_statPanel.setPlayerCount(this);
        m_statPanel.updateStats();
        m_statPanel.hilite(0);
        m_wallet.reset();
    }


    private Tile m_tile2[][] = new Tile[9][12];
    private Tile m_tileBag2[] = new Tile[108];
    private int m_availTiles2 = 108;
    public void initTiles() {

	    int k = 0;


	    for (int i = 0; i <9; i++) {
	        for (int j = 0; j < 12; j++) {
		        m_tile[i][j]= new Tile();
		        m_tile[i][j].setTile(i,j);
		        m_tileBag[k++]=m_tile[i][j];
	        }
	    }
	    dummyTile = new Tile();
	    Tile.setDummy(dummyTile);

        // for ca acquire
        k=0;
         for (int i = 0; i <9; i++) {
	        for (int j = 0; j < 12; j++) {
		        m_tile2[i][j]= new Tile();
		        m_tile2[i][j].setTile(i,j);
		        m_tileBag2[k++]=m_tile2[i][j];
	        }
	    }
    }




    boolean allNonPlayable( Tile ti[] )
    {
        int i;
	    for ( i = 0; i< 6; i++) {
	        if ( ti[i] == dummyTile)
	        {
		        continue;
	        }
	        if (isDead(ti[i].getRow(),ti[i].getColumn()) == true) {
		        continue;
	        }
	        if (isNonPlayable(ti[i].getRow(),ti[i].getColumn()) == false) {
		        break;
	        }
	    }
	    if (i == 6) return true;
	    return false;
    }


    public boolean isNonPlayable(int row, int col)
    {

	//pp("is non playable "	+ r.toString() + "-" + c.toString());

	if (isDead(row,col) == true) return true;


	if ( m_tile[row][col].getState() != Tile.EMPTY) return true;

	if (isHotelsToBuy() == true) return false;

	m_cSafe = 0;
	int dup[] = new int[4];
	int dupCnt = 0;
	int j;
	boolean bStart = false;
	if (row != 0) {
	    if ( m_tile[row -1][col].getState() != Tile.EMPTY &&
		 m_tile[row -1][col].getState() != Tile.ONBOARD) {
		    return false;
	    } else if (m_tile[row -1][col].getState() == Tile.ONBOARD) {
		    bStart = true;
	    }
	}
	if (row != 8) {
	    if ( m_tile[row + 1][col].getState() != Tile.EMPTY &&
		    m_tile[row + 1][col].getState() != Tile.ONBOARD) {
		    return false;
	    } else if (m_tile[row +1][col].getState() == Tile.ONBOARD) {
		    bStart = true;
	    }
	}
	if (col != 0) {
	    if ( m_tile[row ][col -1].getState() != Tile.EMPTY &&
		     m_tile[row ][col - 1].getState() != Tile.ONBOARD) {
		    return false;
	    } else if (m_tile[row ][col-1].getState() == Tile.ONBOARD) {
		    bStart = true;
	    }
	}
	if (col != 11) {
	    if ( m_tile[row ][col + 1].getState() != Tile.EMPTY &&
		    m_tile[row][col + 1].getState() != Tile.ONBOARD) {
		    return false;
	    } else if (m_tile[row ][col+1].getState() == Tile.ONBOARD) {
		    bStart = true;
	    }
	}
	if ( bStart == true) {

	    return true;
	}

	return false;
    }


    public boolean isDead(int row, int col)
    {
	    if ( m_tile[row][col].getState() != Tile.EMPTY) return false;
	    m_cSafe = 0;
	    int dup[] = new int[4];
	    int dupCnt = 0;
	    int j;
	    if (row != 0) {
	        if ( m_tile[row -1][col].getState() != Tile.EMPTY &&
		         m_tile[row -1][col].getState() != Tile.ONBOARD) {
		         dup[dupCnt++] = m_tile[row -1][col].getState() ;
		         m_tilesSafe[m_cSafe++] = m_tile[row -1][col];
	        }
	    }
	    if (row != 8) {
	        if ( m_tile[row + 1][col].getState() != Tile.EMPTY &&
		         m_tile[row + 1][col].getState() != Tile.ONBOARD) {
		        for (j = 0; j<dupCnt; j++) {
		            if (m_tile[row + 1][col].getState() == dup[j]) break;
		        }
		        if (j == dupCnt) {
		            dup[dupCnt++] = m_tile[row +1][col].getState() ;
		            m_tilesSafe[m_cSafe++] = m_tile[row +1][col];
		        }
	        }
	    }
	    if (col != 0) {
	        if ( m_tile[row ][col -1].getState() != Tile.EMPTY &&
		         m_tile[row ][col - 1].getState() != Tile.ONBOARD) {
		        for (j = 0; j<dupCnt; j++) {
		            if (m_tile[row ][col -1].getState() == dup[j]) break;
		        }
		        if (j == dupCnt) {
		            dup[dupCnt++] = m_tile[row][col -1].getState() ;
		            m_tilesSafe[m_cSafe++] = m_tile[row ][col - 1];
		        }
	        }
	    }
	    if (col != 11) {
	        if ( m_tile[row ][col + 1].getState() != Tile.EMPTY &&
		         m_tile[row][col + 1].getState() != Tile.ONBOARD) {
		        for (j = 0; j<dupCnt; j++) {
		            if (m_tile[row ][col+1].getState() == dup[j]) break;
		        }
		        if (j == dupCnt) {
		            m_tilesSafe[m_cSafe++] = m_tile[row ][col + 1];
		        }
	        }
	    }
	    if (m_cSafe == 0 || m_cSafe == 1) {
	        return false;
	    }
	    int cnt =0;
	    for ( int i =0;i<m_cSafe;i++) {
	        if (m_hot[m_tilesSafe[i].getState()].isSafe()) {
		        cnt++;
	        }
	    }
	    if (cnt > 1) return true;
	    return false;
    }

    public void printMessage(String str)
    {
        //if (m_msgFrame !=null)
        //    m_msgFrame.printMessage( str);
    }
    boolean placeTile(int row, int col,AQC msg)
    {
        printMessage( "in placeTile");

	    if ( m_tile[row][col].getState() != Tile.EMPTY){
	        traceError("in placeTile TILE NOT empty state = "+
	                     m_tile[row][col].getState());
	        printMessage( "out of placeTile 1");
	        return false;
	    }
	    if (getGameState() != PLACETILE && getGameState() != OTHER &&
	        getGameState() !=SERVER){
	            traceError("in placeTile Game state = "+
	                     getGameState());
	            printMessage( "out of placeTile 2");
	            return false;
	    }

	    m_row = row;
	    m_column = col;


	    //traceError("POINT A  placeTile");

	    m_cExamine = 0;
	    if (row != 0) {
	        if ( m_tile[row -1][col].getState() != Tile.EMPTY) {
		        m_tilesExamine[m_cExamine++] = m_tile[row -1][col];
	        }
	    }

	    //traceError("POINT B  placeTile");

	    if (row != 8) {
	        if ( m_tile[row + 1][col].getState() != Tile.EMPTY) {
		        m_tilesExamine[m_cExamine++] = m_tile[row +1][col];
	        }
	    }

	    //traceError("POINT C  placeTile");

	    if (col != 0) {
	        if ( m_tile[row ][col -1].getState() != Tile.EMPTY) {
		        m_tilesExamine[m_cExamine++] = m_tile[row ][col - 1];
	        }
	    }

	    //traceError("POINT D  placeTile");

	    if (col != 11) {
	        if ( m_tile[row ][col + 1].getState() != Tile.EMPTY) {
		        m_tilesExamine[m_cExamine++] = m_tile[row ][col + 1];
	        }
	    }

	    //traceError("POINT E  placeTile");

	    if (m_cExamine == 0) {
	        m_players[m_currentPlayer].setState(BUYSTOCK);
	        if(getGameState() == PLACETILE){
	            traceError("SET state to BUYSTOCK(placestate A)");
		        setGameState(BUYSTOCK);
		    }
	        m_tile[row][col].setState(Tile.ONBOARD);
	        printMessage( "out of placeTile 3");
	        return true;
	    }

	    //traceError("POINT F  placeTile");

	    int chain =Tile.EMPTY;
	    Integer I1 = new Integer(m_cExamine);
	    for ( int i =0;i<m_cExamine;i++) {
	        if ( m_tilesExamine[i].getState() != Tile.ONBOARD) {
		        if ( chain != Tile.EMPTY) {
		            if ( chain != m_tilesExamine[i].getState()	) {
			            m_tile[row][col].setState(Tile.ONBOARD);
			            m_players[m_currentPlayer].setState(MERGE);

			            if(getGameState() == PLACETILE|| m_players[m_currentPlayer].isDHTMLClient()){
			                if(getGameState() == PLACETILE){
			                    setGameState(MERGE);
			                }
			                return mergeChain();
			            }
			            printMessage( "out of placeTile 5");
			            return true;
		            }
		        } else {
		            chain = m_tilesExamine[i].getState();
		        }
	        }
	    }

	    //traceError("POINT G  placeTile");

	    if (chain == Tile.EMPTY ) {
	        if (isHotelsToBuy() == false) return false;
	        m_tile[row][col].setState(Tile.ONBOARD);
	        int h=isOneHotelLeft();
	        if(h !=-1){
	            startChain(h,msg.getCurrentPlayerID());
		        msg.appendMessage( m_players[m_currentPlayer].getName() +
	                " starts "+ m_hot[h].getName());
	        }
	        if(getGameState() == PLACETILE){
	            traceError("POINT Startchain  placeTile");
	            if(h ==-1){
		            setGameState(STARTCHAIN);
		        }else{
		            setGameState(BUYSTOCK);
		        }
		    }
		    if(h ==-1){
		        m_players[m_currentPlayer].setState(STARTCHAIN);
		    }else{
		        m_players[m_currentPlayer].setState(BUYSTOCK);
		    }

	    } else {

	        traceError("GROWCHAIN  placeTile");

	        growChain(chain,msg);
	        m_players[m_currentPlayer].setState(BUYSTOCK);
	        if(getGameState() == PLACETILE){
		        setGameState(BUYSTOCK);
		    }
	    }
	    printMessage( "end of placeTile");
	    return true;
    }

    public void setMerge(int mergeNum, int mergeList[])
    {
	    m_players[m_currentPlayer].sendmsg(
	          new AQCMergeHotel(m_currentPlayer,0,mergeNum,mergeList));
    }

    public void turnMergeTileOff()
    {
        m_tile[m_row][m_column].setM_dirty(true);
        m_tile[m_row][m_column].m_mergeTile = false;
    }

    AQC[] mergeHotels(AQCMergeHotel arg)
    {
        AQC msgs[]= new AQC[2];
        int mergeNum =arg.getHotelCount();
        int mergeList[]=arg.getMergeList();
        m_tile[m_row][m_column].m_mergeTile = true;
	    m_tile[m_row][m_column].setState(m_hot[mergeList[0]].getHotel());
	    int p = m_hot[mergeList[1]].count();
	    for ( int i=0;i<m_cExamine;i++) {
	        walkChain(m_tilesExamine[i], m_hot[mergeList[0]].getHotel());
	    }

	    if (getGameState() ==SERVER) {
	        String dstr = m_hot[mergeList[1]].getName();
	        for ( int i=2 ;i<mergeNum;i++) {
		        dstr = dstr + " and " + m_hot[mergeList[i]].getName();
	        }

	        arg.setMessage(m_players[m_currentPlayer].getName()  + " merges " + dstr +
	        " into " + m_hot[mergeList[0]].getName()+".");
	    }
	    for ( int i=1;i<mergeNum;i++) {
	        m_hot[mergeList[i]].calcBonus();
	        takeOver(m_hot[mergeList[0]].getHotel(),
		        m_hot[mergeList[i]].getHotel());
	    }

	    m_cExamine = 0;

	    for ( int i=1;i<mergeNum; i++) {
		    bonusPayout(m_hot[mergeList[i]].getHotel(),i,arg);
	    }
	    if (getGameState() !=SERVER)
	    {
	        int x = m_stockPanel.viewing();
	        if (x != -1) {
		        showStock(x);
	        }
	        m_statPanel.updateSize();
	        showStat();
	    }
	    msgs[0]=arg;
	    m_tradeIndex = 0;
	    m_tradeCnt = 0;
	    if (getGameState() ==SERVER)
	    {
	        for ( int i=1;i<mergeNum; i++) {
		        setSwapQueue(mergeList[0],
			            mergeList[i],i);
			}
			msgs[1] = firstSwap();
	    }
	    return msgs;
    }

    AQC firstSwap()
    {
        m_players[m_currentPlayer].setState(OTHER);
        m_players[m_stockTrade[0].getPlayer()].setState(SWAP);
	    AQCSwapFrame sst = swapStockTransaction(m_stockTrade[0]);
	    sst.setMessage( m_players[m_stockTrade[0].getPlayer()].getName() +
	        " is deciding what to do with his/her shares of " +
	        m_hot[m_stockTrade[0].getDefunct()].getName()+".");
	    setSwap(sst.getStockTransaction());
	    return sst;
    }

    AQC[] trade(AQCSwapStock ass)
    {
	    if (getGameState() ==SERVER) {
	        String str=
	        m_players[ass.getCurrentPlayerID() ].getName() + " swaps " + ass.getSwap() + " shares of "
		    + m_hot[ass.getDefunct()].getName() + " for " +
		    m_hot[ass.getSurvivor()].getName() + ".\n" +
	        m_players[ass.getCurrentPlayerID() ].getName() + " sells " + ass.getSell() + " shares of "
		    + m_hot[ass.getDefunct()].getName() + ".";
		    ass.setMessage(str);

	    }
	    m_players[ass.getCurrentPlayerID()].setState(OTHER);

	    m_players[ass.getCurrentPlayerID()].swapStock(
			    m_hot[ass.getSurvivor()],
			    m_hot[ass.getDefunct()],
			    ass.getSwap(),
			    ass.getSell());

	    if (m_stockPanel.viewing() == ass.getDefunct()) {
	        showStock(ass.getDefunct());
	    }
	    if (m_stockPanel.viewing() == ass.getSurvivor()) {
	        showStock(ass.getSurvivor());
	    }

	    showStat();
    	//System.out.println("m_currentPlayer = "+m_currentPlayer+
    	  //                  " ass.getCurrentPlayerID()= "+ass.getCurrentPlayerID());
	    if (getGameState() !=SERVER  && m_bPlaying &&
	        //m_current ==m_currentPlayer) {
	        m_currentPlayer == ass.getCurrentPlayerID()) {
	            m_players[m_currentPlayer].sendmsg(
	                  new AQCNextTransaction(ass.getCurrentPlayerID(),0));
	     }
	     AQC msg[]= {ass};
	     return msg;
    }
    AQC[] nextTrans()
    {
	    m_tradeIndex++;
	    AQC msg[]= new AQC[1];
	    if (m_tradeIndex < m_tradeCnt) {
	        AQCSwapFrame sst = swapStockTransaction(m_stockTrade[m_tradeIndex]);
	        sst.setMessage( m_players[m_stockTrade[m_tradeIndex].getPlayer()].getName() +
	        " is deciding what to do with his/her shares of " +
	        m_hot[m_stockTrade[m_tradeIndex].getDefunct()].getName()+".");
	        m_players[m_stockTrade[m_tradeIndex].getPlayer()].setState(SWAP);
	        setSwap(sst.getStockTransaction());
	        msg[0]= sst;
	    } else {
	        m_players[m_currentPlayer].setState(BUYSTOCK);
	        msg[0]= new AQCBuyState(m_currentPlayer,m_players[m_currentPlayer].getName());
	        msg[0].setMessage(m_players[m_currentPlayer].getName()  + " you can buy stock now.");
	        msg[0].setTurnMergeTileOff(true);
	    }
	    return msg;
    }

    private StockTransaction m_st;
    public void setSwap(StockTransaction st)
    {
        m_st=st;
    }
    StockTransaction getSwap()
    {
        return m_st;
    }

    AQCSwapFrame swapStockTransaction(StockTransaction trans)
    {
        return new AQCSwapFrame(trans.getPlayer(),trans);
    }

    int round(int x)
    {
        if((x%100)==0){
            return x;
        }
        int q = x/100;
        x = q * 100 +100;
        return x;
    }
    public void bonusPayout(int defunct,int mergeIndex, AQC aqc)
    {
	    int cnt = 0;
	    int bonusAmt;
	    String bonusWinners;
	    Player partners[] = new Player[6];
	    int i;
	    // figure partners
	    for ( i = 0; i <m_playerNum; i++) {
	        if (m_players[i].m_hotels[defunct] != 0) {
		        partners[cnt] = m_players[i];
		        cnt++;
	        }
	    }
	    Integer I1;
	    if ( cnt == 1) {
	        // only one owner
	        int amt = partners[0].getMoney();
	        bonusAmt = m_hot[defunct].firstBonus() +
			    m_hot[defunct].secondBonus();
	        amt = amt  + m_hot[defunct].firstBonus() +
		    m_hot[defunct].secondBonus();
	        partners[0].setMoney(amt);


	        if (mergeIndex != -1)
	        {
		        //setAllMoney(partners[0],amt);
	        }
	        bonusWinners = partners[0].getName() +
		    " Wins both first and second bonus for "+ bonusAmt;


	    } else {
	        // more then one owner
	        Player temp;

	        //sort
	        int j;
	        for (i = 0; i <cnt - 1;i++) {
		        for ( j = i+1; j< cnt ;j++) {
		            if ( partners[i].m_hotels[defunct] <
			        partners[j].m_hotels[defunct]	) {
			            temp = partners[i];
			            partners[i] = partners[j];
			            partners[j] = temp;
		            }
		        }
	        }

	        // partition
	        int nParts= 0;
	        int part[]=new int[6];
	        int shareCnt = partners[0].m_hotels[defunct] ;
	        for ( i = 0;i<cnt; i++) {
		        if ( partners[i].m_hotels[defunct] == shareCnt){
		            part[nParts]++;
		        } else {
		            shareCnt = partners[i].m_hotels[defunct] ;
		            nParts++;
		            part[nParts]++;
		        }
	        }
	        if (part[0] == 1) {
		        bonusAmt = m_hot[defunct].firstBonus();
		        bonusWinners = partners[0].getName() +
		        " Wins first bonus for "+ bonusAmt;

		        int amt = partners[0].getMoney();
		        amt = amt  + m_hot[defunct].firstBonus();
		        partners[0].setMoney(amt);
		        if (mergeIndex != -1){
		           // setAllMoney(partners[0],amt);
		        }
		        if  (part[1] == 1) {

		            bonusAmt = m_hot[defunct].secondBonus();
		            bonusWinners = bonusWinners + ". " +
				        partners[1].getName() +
				        " Wins second bonus for "+bonusAmt;

		            amt = partners[1].getMoney();
		            amt = amt  + m_hot[defunct].secondBonus();
		            partners[1].setMoney(amt);
		            if (mergeIndex != -1) {
			            //setAllMoney(partners[1],amt);
		            }
		        } else {
		            bonusAmt = m_hot[defunct].secondBonus();
		            bonusWinners = bonusWinners + ". ";

		            int evenShare = m_hot[defunct].secondBonus();
		            evenShare /= part[1];
		            evenShare = round(evenShare);
		            for ( j = 0; j<part[1];j++)
		            {
			            bonusWinners = bonusWinners +
				                partners[j+1].getName() + " ";

			            amt = partners[j + 1].getMoney() + evenShare;
			            partners[j+1].setMoney(amt );
			            if (mergeIndex != -1) {
			                //setAllMoney(partners[j+1],amt);
			            }
			            if (j+1 < part[1]) {
			                bonusWinners = bonusWinners + "and ";
			            }
		            }
		            bonusWinners = bonusWinners +
				        " split second bonus of "+bonusAmt;



		        }
	        } else {
		        int evenShare = m_hot[defunct].firstBonus() +
				        m_hot[defunct].secondBonus();

		        bonusAmt = evenShare;

		        evenShare /= part[0];
		        evenShare = round(evenShare);
		        bonusWinners = "";
		        for ( j = 0; j<part[0];j++)
		        {
		            bonusWinners = bonusWinners +
				        partners[j].getName() + " ";
		            if (j+1 < part[0]) {
			            bonusWinners = bonusWinners + "& ";
		            }
		            int amt = partners[j].getMoney() + evenShare;
		            partners[j].setMoney(amt );
		            if (mergeIndex != -1) {
			            //setAllMoney(partners[j],amt);
		            }
		        }
		        bonusWinners = bonusWinners +
			            " split first and second bonus of "+bonusAmt;

	        }

	    }
	    aqc.appendMessage("For " + m_hot[defunct].m_name+  ", "+ bonusWinners);

	    if (mergeIndex != -1)
	        m_bonusWinners[mergeIndex - 1] = bonusWinners;

    }



    AQC[] nextPlayer (AQCBuyHotel arg)
    {
        boolean b=buyStock(arg);
        if( getGameState() != SERVER) {
            AQC x[]= {arg};
            return x;
        }

	    int replace = 0;
	    Tile rt[]= new Tile[6];
	    for ( int t = 0; t< 6; t++) {
	        if ( m_players[m_currentPlayer].getM_tiles()[t] == dummyTile)
	        {
		        while (isTile() == true)
		        {
		            Tile ti = pickATile();
		            if (isDead(ti.getRow(),ti.getColumn()) == false) {
			            rt[replace++] = ti;
			            m_players[m_currentPlayer].getM_tiles()[t] = ti;
		            	break;
		            }
		        }
	        } else if (isDead(m_players[m_currentPlayer].getM_tiles()[t].getRow(),
		        m_players[m_currentPlayer].getM_tiles()[t].getColumn()) == true) {
		        while (isTile() == true)
		        {
		            Tile ti = pickATile();
		            if (isDead(ti.getRow(),ti.getColumn()) == false) {
			            rt[replace++] = ti;
			            m_players[m_currentPlayer].getM_tiles()[t] = ti;
			            break;
		            }
		        }
	        }
	    }
	    AQC msgs[] =null;
	    if (replace > 0) {
	        if(b){
	            msgs = new AQC[3];
	        }else{
	            msgs = new AQC[2];
	        }
	        Tile t[] = new Tile[replace];
	        for (int j = 0;j<replace;j++) {
		        t[j]= rt[j];
		    }
		    if(b){
	            msgs[1]= new AQCReplaceTile(arg.getCurrentPlayerID(),t);
	        }else{
	            msgs[0]= new AQCReplaceTile(arg.getCurrentPlayerID(),t);
	        }
	    } else {
	        if(b){
	            msgs = new AQC[2];
	        }else{
	            msgs = new AQC[1];
	        }
	    }
	    if(b){
            msgs[0]=arg;
        }

        m_players[m_currentPlayer].setState(OTHER);
	    if (m_currentPlayer == m_playerNum - 1)
	        m_currentPlayer = 0;
	    else
	        m_currentPlayer++;
        //System.out.println("m_currentPlayer =zzz " +  m_players[m_currentPlayer].getName());
	     m_players[m_currentPlayer].setState(PLACETILE);
	    gameInfo(m_gameObject);
	    /*if(m_autoSave) {
	        saveGame();
	    }*/

	    if (allNonPlayable ( m_players[m_currentPlayer].getM_tiles())){
	         msgs[msgs.length-1]= new AQCBuyState(m_currentPlayer,m_players[m_currentPlayer].getName());
	    } else {
	         msgs[msgs.length-1]= new AQCPlaceState(m_currentPlayer,m_players[m_currentPlayer].getName());
	    }
	    return msgs;
    }

    public void traceBug(String s) {
       //System.err.println("GB TRACE ["+m_players[m_currentPlayer].getName()+":"+
          //                            getGameState()+":"+getGameState22()+"] " + s );
    }
    public void traceError2(String s) {
        appendTextMsg(m_text, s);
        if(m_currentPlayer <0||m_players[m_currentPlayer]== null||
           m_players[m_currentPlayer].getName()==null)return;
	    //System.err.println("GB TRACE ["+m_players[m_currentPlayer].getName()+":"+getGameState()+"] " + s );
    }
    public void traceError(String s) {
        if(m_currentPlayer <0||m_players[m_currentPlayer]== null||
           m_players[m_currentPlayer].getName()==null)return;
	    //System.err.println("GB TRACE ["+m_players[m_currentPlayer].getName()+":"+getGameState()+"] " + s );
    }
    public void saveGame(String fn)
    {
	    FileOutputStream out = null;
	    try {
	        out = new  FileOutputStream (fn+".aq");
	        ObjectOutputStream oos = new ObjectOutputStream(out);
	        oos.writeObject(m_gameObject);
	        oos.close();
	        out.close();
	    } catch (IOException e) {
	        System.err.println( "Can't open acquire.Game" );
	        return;
	    }
    }


    public void loadGame(String fn,int gid)
    {
	    FileInputStream in = null;

	    try {
	        in = new  FileInputStream (fn+".aq");
	        ObjectInputStream ois = new ObjectInputStream(in);
	        m_gameObject = (AQCGameBoard) ois.readObject();
	        m_gameObject.setGameID(gid);
	        ois.close();
	        in.close();
	    } catch (Exception e) {
	        System.err.println( "Can't open acquire.Game" );
	        return;

	    }

	    setVersion(m_gameObject.getVersion());
	    setGameStarted(true);
	    setConnectionLost(false);

	    m_currentPlayer = m_gameObject.getCurrentPlayer();
	    m_playerNum = m_gameObject.getPlayerCount();
	    for (int i =0; i<m_playerNum;i++)
	    {
	        m_players[i]=new Player ("blank",i,this);
	        m_players[i].setLostConnection(true);
	    }

        m_availTiles  = m_gameObject.getAvailableTiles().length;
        for (int i = 0; i <m_availTiles; i++) {
	        m_tileBag[i] = m_gameObject.getAvailableTiles()[i];
	    }

	    for (int i =0; i<m_playerNum;i++)
	    {
	        m_players[i].setM_tiles(m_gameObject.getPlayerTiles()[i]);
	    }

	    reproduceGameInfo(m_gameObject);

    }

    public void setSwapQueue(  int survivor, int defunct, int mergeIndex)
    {
	    int playIndex = m_currentPlayer;
	    String str = m_hot[survivor].getName() + " takeover of " +
		        m_hot[defunct].getName();
	    for ( int i = 0; i<m_playerNum; i++) {

	        if (m_players[playIndex].m_hotels[defunct] != 0) {
		        m_stockTrade[m_tradeCnt].setIndex(m_tradeCnt);
		        m_stockTrade[m_tradeCnt].setPlayer(playIndex);
		        m_stockTrade[m_tradeCnt].setSurvivor(survivor);
		        m_stockTrade[m_tradeCnt].setDefunct(defunct);
		        m_stockTrade[m_tradeCnt].setTitle(str);
		        m_stockTrade[m_tradeCnt++].setBonusStr(m_bonusWinners[mergeIndex - 1]);
	        }
	        if (playIndex == (m_playerNum -1)) {
		        playIndex = 0;
	        } else {
		        playIndex++;
	        }
	    }
    }

    public void hideSwapMergeFrame()
    {
	    m_swapFrame.setVisible(false);
	    m_orderFrame.setVisible(false);
    }
    private boolean m_swappingStock = false;
    public void swapStock( StockTransaction trade)
    {
        m_swappingStock = true;
	    m_swapFrame.setFrame(trade);
	    m_swapFrame.setVisible(true);
    }

    boolean isHotelsToBuy()
    {
        int i;
	    for ( i = 0; i <7; i++) {
	        if (m_hot[i].count() == 0)
		    break;
	    }
	    if ( i != 7) return true;
	    return false;

    }
    public void takeOver(int survivor, int defunct)
    {
	for (int i = 0; i <9; i++) {
	    for (int j = 0; j < 12; j++) {
		if (m_tile[i][j].getState() == defunct){
		   m_tile[i][j].setState(survivor);
		}
	    }
	}
    }





    boolean  mergeChain()
    {

	    int mergeNum = 0;
	    int j;
	    int i;
	    Tile order[] = new Tile[4];
	    for (i = 0 ;i< m_cExamine; i++) {
	        if (m_tilesExamine[i].getState() != Tile.ONBOARD) {
		        if (mergeNum == 0) {
		            order[0] = m_tilesExamine[i];
		            mergeNum++;
		        } else {
		            for (j = 0; j <mergeNum; j++) {
			        if (order[j].getState() == m_tilesExamine[i].getState()){
			            break;
			        }
		            }
		            if (mergeNum == j) {
			        order[mergeNum++] = m_tilesExamine[i];
		            }
		        }
	        }
	    }


	    int x;
	    int y;
	    Tile temp;
	    for (i = 0; i <mergeNum - 1;i++) {
	        for ( j = i+1; j< mergeNum ;j++) {
		        if ( m_hot[order[i].getState()].count() <
			        m_hot[order[j].getState()].count() ) {
			        temp = order[i];
			        order[i] = order[j];
			        order[j] = temp;
			    // during sort
		        }
	        }
	    }

	    int nParts= 0;
	    int part[]=new int[4];
	    int cnt = m_hot[order[0].getState()].count();
	    int mergeList[]= new int[4];
	    for ( i = 0;i<mergeNum; i++) {
	        mergeList[i] = order[i].getState();
	        if ( m_hot[order[i].getState()].count() == cnt){
		        part[nParts]++;
	        } else {
		        cnt = m_hot[order[i].getState()].count();
		        nParts++;
		        part[nParts]++;
	        }
	    }
	    nParts++;
	    if (nParts < mergeNum) {
	        int split[][] = new int[nParts][];
	        int pos = 0;
	        for ( i = 0;i<nParts  ; i++) {
		        split[i] = new int [part[i]];
		        for (j =0;j<part[i];j++)
		        {
		            split[i][j] =  order[j + pos].getState();
		            xxx = split[i][j];
		        }
		        pos += part[i];
	        }
	        if( m_players[m_currentPlayer].isDHTMLClient()){
	            DHTMLMergeList = split;
	            DHTMLMergeNum = mergeNum;
	            m_players[m_currentPlayer].setState(CHOOSE_ORDER);
	        }else{
	            m_orderFrame.setFrame("Merge Order",split,mergeNum);
	            m_orderFrame.setVisible(true);
	        }
	    } else {
	        if( m_players[m_currentPlayer].isDHTMLClient()){
	            m_players[m_currentPlayer].setMerge(mergeNum,mergeList);
	        }else{
	            setMerge(mergeNum,mergeList);
	        }
	    }
	    return true;
    }
    private int DHTMLMergeNum=0;
    private int DHTMLMergeList[][]=null;
    public String generateChooseOrder()
    {
        int pos = 0;
	    int nParts = DHTMLMergeList.length;
	    String str ="";
	    int x=0;
	    for ( int i = 0;i<nParts ; i++) {
		    for (int j =0;j<DHTMLMergeList[i].length;j++)
		    {
		        String id=(x==0?"survivor":"defunct"+x);
		        x++;
		        str += id+"<SELECT NAME =\""+id+"\" ID=\""+id + "\" >" ;
		        for (int m =0;m<DHTMLMergeList[i].length;m++)
		        {
		            String h =m_hot[DHTMLMergeList[i][m]].getName();
			        str += "<OPTION VALUE=\""+h+"\">"+h;
		        }
		        str += "</SELECT> &nbsp";
		    }
		    str= str+"<input type=\"HIDDEN\" name=\"mergenum\" id=\"mergenum\" value=\""+DHTMLMergeNum+"\">";

	    }
        return str;


    }
    public void setCurrent(int k)
    {
        m_current=k;
    }
    public void growChain(int state,AQC msg)
    {
	    m_tile[m_row][m_column].setState(state);
	    for ( int i=0;i<m_cExamine;i++) {
	        walkChain(m_tilesExamine[i], state);
	    }
	    m_cExamine = 0;
	    if(getVersion() == AQC.CENTRAL_AMERICA){
	        if (getGameState() ==SERVER){
	            m_current= m_currentPlayer;
	        }
	        if(m_players[m_current].bonusShare(m_hot[state])){
	            msg.appendMessage( m_players[m_current].getName() +
	                " is awarded a share for expanding "+ m_hot[state].getName());
	        }
	        showStat();
	    }
    }

   public int isOneHotelLeft()
   {
        int j = 0;
        int h=-1;
        for(int i=0;i<7;i++){
            if(canStartChain(i)){
                j++;
                h=i;
            }
        }
        if(j>1){
            h=-1;
        }
        return h;
   }

    public boolean canStartChain(int state)
    {
	    if( m_hot[state].price() != 0) return false;
	    return true;
    }

    public boolean startChain(int state, int cur)
    {

        printMessage( " in start chain");
	    if( m_hot[state].price() != 0) return false;

	    m_tile[m_row][m_column].setState(state);
	    for ( int i=0;i<m_cExamine;i++) {
	        walkChain(m_tilesExamine[i], state);
	    }
	    m_cExamine = 0;
	    m_players[cur].bonusShare(m_hot[state]);
	    if(getGameState() == GameBoard.STARTCHAIN) {
	        traceError("SET state to BUYSTOCK(startchain A)");
	        setGameState(BUYSTOCK);
	    }
	     m_players[m_currentPlayer].setState(BUYSTOCK);
	    if (m_stockPanel.viewing() == state) {
	        showStock(state);
	    }

	    showStat();
	    printMessage( " end of start chain");
	    return true;
    }
    public void walkChain(Tile t, int state)
    {
	int row = t.getRow();
	int col = t.getColumn();
	if ( m_tile[row ][col].getState() == Tile.ONBOARD) {
	    t.setState(state);
	}
	if (row != 0) {
	    if ( m_tile[row -1][col].getState() == Tile.ONBOARD) {
		walkChain(m_tile[row -1][col],state);
	    }
	}
	if (row != 8) {
	    if ( m_tile[row + 1][col].getState() == Tile.ONBOARD) {
		walkChain(m_tile[row+1][col],state);
	    }
	}
	if (col != 0) {
	    if ( m_tile[row ][col -1].getState() == Tile.ONBOARD) {
		walkChain(m_tile[row][col-1],state);
	    }
	}
	if (col != 11) {
	    if ( m_tile[row ][col + 1].getState() == Tile.ONBOARD) {
		walkChain(m_tile[row][col+1],state);
	    }
	}
    }

    public boolean canBuyStock(int i)
    {
	    if (m_hot[i].price() == 0) return false;
	    if (m_players[m_currentPlayer].getMoney() < m_hot[i].price())
	        return false;
	    int av = m_hot[i].getAvailShares();
	    if (av == 0) return false;
	    return true;
    }

    public boolean buyStock(AQCBuyHotel bh)
    {
        boolean b =false;
        for (int i=0;i< bh.getCount();i++){
	        for (int j=0;j<bh.getAmount(i);j++){
	            b=true;
	            m_players[bh.getCurrentPlayerID()].purchaseStock(m_hot[bh.getHotel(i)]);
	        }
	         if (m_stockPanel.viewing() == bh.getHotel(i)) {
	            showStock(bh.getHotel(i));
	        }
		}


	    showStat();
	    return b;
    }



    public boolean isAllSafe()
    {
	    boolean b = false;
	    for ( int i=0; i<7;i++) {
	        if (m_hot[i].count() == 0)
		    continue;
	        if (m_hot[i].count() < 11)
		    return false;
	        b = true;
	    }
	    return b;
    }
    public boolean checkForEnd()
    {
	    if(over40() || isAllSafe())
	        return true;
	    else
	        return false;
    }

    public boolean over40()
    {
        int i;
	    for ( i=0; i<7;i++) {
	        if (m_hot[i].count() > 40)
		    break;
	    }
	    if ( i == 7) return false;
	    return true;
    }

    boolean isTile()
    {
        if(m_availTiles > 0) return true;
        return false;
    }

    AQC[] chooseWinner(AQCBuyHotel bh)
    {
        buyStock(bh);
        AQC m[] = null;
	    for ( int i=0;i<7;i++) {
	        if (m_hot[i].count() != 0)
	        {
		        m_hot[i].calcBonus();
		        bonusPayout (i,-1,bh);
	        }
	    }
	    int amt = 0;
	    int winner = 0;
        for ( int i=0;i<m_playerNum;i++) {
	        int w = m_players[i].worth();
	        m_players[i].setMoney(w );
	        if (amt< w) {
		        amt = w;
		        winner = i;
	        }


	    }
	    boolean dl=true;
	    for (int i = 0; i <m_playerNum ;i++) {
            m_players[i].setState(GAMEOVER);
            if(m_players[i].isDHTMLClient()){
                dl=false;
            }
        }
	    AQC res[]= null;
	    if (dl){
	        res = new AQC[2];
	        res[1]= new AQCLobbyDelete(m_gameId);
	    } else{
	        res = new AQC[1];
	    }
	    res[0]=bh;


	    String str =  m_players[bh.getCurrentPlayerID()].getName() + " ends the Game.\n>> " +
		        m_players[winner].getName() + " is the WINNER!!.";
		bh.appendMessage(str);

	    if (getGameState() !=SERVER)
	    {
	        showStat();
	    }
	    setGameState(GAMEOVER);

	    return res;
    }
    public boolean anyDHTMLPlayers()
    {
         for (int i = 0; i <m_playerNum ;i++) {
            if(m_players[i].isDHTMLClient()){
                return true;
            }
        }
        return false;
    }
     public void populateMessage2(String m,String name)
    {
         for (int i = 0; i <m_playerNum ;i++) {
            if( m_players[i].isDHTMLClient()){
                if(true|| !m_players[i].getName().equals(name) ){
                    System.out.println(m_players[i].getName()+":in  populateMessage2 m = "+name+":" + m);
                    m_players[i].setChatMsgStr(name+":"+m);
                }else{
                    m_players[i].setChatMsgStr("#abort#");
                }

            }
        }
    }
    public void populateMessage(String m)
    {
         for (int i = 0; i <m_playerNum ;i++) {
            if(m_players[i].isDHTMLClient()){
                m_players[i].setMsgStr(m);
            }
        }
    }

    public AboutNeighbors surroundingTiles(Tile ti)
    {
        Tile x;
        int row=ti.getRow();
        int col = ti.getColumn();
	    if ( ti.getState() != Tile.EMPTY){
	        appendTextMsg(m_text, "NEVER GET HERE " + ti.getLabel());
	        return null;
	    }
	    AboutNeighbors an = new AboutNeighbors();
	    if (isNonPlayable(row,col)){
	        an.setType(Tile.NONPLAYBLE);
	        return an;
	    }
	    an.setType(Tile.ONBOARD);
	    m_cSafe = 0;
	    int dup[] = new int[4];
	    int dupCnt = 0;
	    int j;
	    if (row != 0) {
	        appendTextMsg(m_text, "NORTH");
	        if ( m_tile[row -1][col].getState() != Tile.EMPTY &&
		         m_tile[row -1][col].getState() != Tile.ONBOARD) {
		         dup[dupCnt++] = m_tile[row -1][col].getState() ;
		         m_tilesSafe[m_cSafe++] = m_tile[row -1][col];

		         //north
		         an.getNeighors()[0] = m_tile[row -1][col].getState();
		         an.getHotels()[m_tile[row -1][col].getState()] =
		         m_hot[m_tile[row -1][col].getState()].count();
	        } else {
	            an.getNeighors()[0] = m_tile[row -1][col].getState() ;
	            if(an.getNeighors()[0] == Tile.ONBOARD) {
	                an.setType(Tile.START);
	            }
	        }
	    } else {
	        an.getNeighors()[0] = Tile.OUTOFBOUNDRY;
	    }

	    if (row != 8) {
	        appendTextMsg(m_text, "SOUTH");
	        if ( m_tile[row + 1][col].getState() != Tile.EMPTY &&
		         m_tile[row + 1][col].getState() != Tile.ONBOARD) {

		        // south Tile
		        an.getNeighors()[2] = m_tile[row +1][col].getState();
		        an.getHotels()[m_tile[row +1][col].getState()] =
		              m_hot[m_tile[row +1][col].getState()].count();
		        for (j = 0; j<dupCnt; j++) {
		            if (m_tile[row + 1][col].getState() == dup[j]) break;
		        }
		        if (j == dupCnt) {
		            dup[dupCnt++] = m_tile[row +1][col].getState() ;
		            m_tilesSafe[m_cSafe++] = m_tile[row +1][col];

		        }
	        }else {
	            an.getNeighors()[2] = m_tile[row +1][col].getState() ;
	            if(an.getNeighors()[2] == Tile.ONBOARD) {
	                an.setType(Tile.START);
	            }
	        }
	    } else {
	        an.getNeighors()[2] = Tile.OUTOFBOUNDRY;
	    }
	    if (col != 0) {
	        appendTextMsg(m_text, "WEST");
	        if ( m_tile[row ][col -1].getState() != Tile.EMPTY &&
		         m_tile[row ][col - 1].getState() != Tile.ONBOARD) {

		        // west Tile
		        an.getNeighors()[3] = m_tile[row ][col-1].getState();
		        an.getHotels()[m_tile[row ][col-1].getState()] =
		              m_hot[m_tile[row][col-1].getState()].count();
		        for (j = 0; j<dupCnt; j++) {
		            if (m_tile[row ][col -1].getState() == dup[j]) break;
		        }
		        if (j == dupCnt) {
		            dup[dupCnt++] = m_tile[row][col -1].getState() ;
		            m_tilesSafe[m_cSafe++] = m_tile[row ][col - 1];
		        }
	        }else {
	            an.getNeighors()[3] = m_tile[row][col-1].getState() ;
	            if(an.getNeighors()[3] == Tile.ONBOARD) {
	                an.setType(Tile.START);
	            }
	        }

	    }else {
	        an.getNeighors()[3] = Tile.OUTOFBOUNDRY;
	    }
	    if (col != 11) {
	        appendTextMsg(m_text, "EAST");
	        if ( m_tile[row ][col + 1].getState() != Tile.EMPTY &&
		         m_tile[row][col + 1].getState() != Tile.ONBOARD) {

		        //east
		        an.getNeighors()[1] = m_tile[row][col+1].getState();
		        an.getHotels()[m_tile[row ][col+1].getState()] =
		              m_hot[m_tile[row][col+1].getState()].count();
		        for (j = 0; j<dupCnt; j++) {
		            if (m_tile[row ][col+1].getState() == dup[j]) break;
		        }
		        if (j == dupCnt) {
		            m_tilesSafe[m_cSafe++] = m_tile[row ][col + 1];
		        }
	        }else {
	            an.getNeighors()[1] = m_tile[row ][col+1].getState() ;
	            if(an.getNeighors()[1] == Tile.ONBOARD) {
	                an.setType(Tile.START);
	            }
	        }

	    }else {
	        an.getNeighors()[1] = Tile.OUTOFBOUNDRY;
	    }
	    if( m_cSafe == 1) {
	        an.setType(Tile.GROW);
	        an.setGrower(m_tilesSafe[0].getState());
	        return an;
	    }
	    int cnt =0;
	    for ( int i =0;i<m_cSafe;i++) {
	        if (m_hot[m_tilesSafe[i].getState()].isSafe()) {
		        cnt++;
	        }
	    }
	    if (cnt > 1){
	         an.setType(Tile.DEAD);
	         return an;
	    }
	    if( m_cSafe > 1) {
	        an.setType(Tile.MERGE);
	        return an;
	    }
	    return an;
    }
    private TextArea m_text;
    private String m_lastMsg;
    public void setTextMsg(TextArea ta, String lm)
    {
        m_text=ta;
        m_lastMsg=lm;
    }
    public void appendTextMsg(TextArea ta, String lm)
    {
        m_text=ta;
        m_lastMsg= m_lastMsg+"\n"+lm;
    }
    public void showLastMsg()
    {
        m_text.append(m_lastMsg);
    }


    public String getHTMLStats()
    {
        String str="";
        for(int i=0;i<m_playerNum;i++){
            String c = i%2==0?"white": "\"#5092E4\"";
            if(i== m_currentPlayer){
                c = "\"#C0C0C0\"";
            }
            str = str + "\n<TR ID=\"StatRow"+i+"\" BGCOLOR=" + c +" >";
            str = str +"\n<TD ID=\"StatName"+i+"\">"+m_players[i].getName() +"</TD>";
            for(int j=0;j<7;j++){
                str = str +"\n<TD ID=\"StatHotel"+i+j+"\">"+m_players[i].m_hotels[j] +"</TD>";
            }
            str = str +"\n<TD ID=\"StatMoney"+i+"\">"+m_players[i].getMoney() +"</TD>";
            str = str + "\n</TR>";
        }
        return str;

    }

    public String getHTMLStocks()
    {
        String c= "";
        String str="";
        for(int k= 0;k<7;k++){
	        if (m_hot[k].count()>10){
	            c ="\"orange\"";
	        }else {
	            if(k%2==0){
	                c= "\"white\"";
	            }else {
	                c = "\"#5092E4\"";
	            }
	        }

            String state =getHotelColor(k);

	        str = str + "\n<TR BGCOLOR=" + c +" >";
	        str = str +"\n<TD BGCOLOR=" + state +">"+ m_hot[k].getName()+"</TD>";
            str = str +"\n<TD>"+ m_hot[k].getAvailShares()+"</TD>";
            str = str +"\n<TD>"+ m_hot[k].count()+"</TD>";
            str = str +"\n<TD>"+ m_hot[k].price()+"</TD>";
            str = str + "\n</TR>";
        }
        return str;
    }

    String getHotelColor(int k) {
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
            return state;
     }

    public String getStats()
    {
        String str = "";
        for(int k =0; k< 7;k++){
          str = str +  m_hot[k].getAvailShares()+ " ";
          str = str +  m_hot[k].count()+ " ";
          str = str +  m_hot[k].price();
          //if (k<6 || m_playerNum>0){
              str = str +" ";
          //}
        }
        for(int i =0; i< m_playerNum;i++){
            str = str +m_players[i].getName() +" ";
            for(int j=0;j<7;j++){
                str = str +m_players[i].m_hotels[j] +" ";
            }
            str = str + m_players[i].getMoney();
           // if(i<(m_playerNum-1)){
                str = str +" ";
           // }
        }
        str = str +(m_GameStarted?m_currentPlayer:-1);
        //str = "\""+str+"\"";
        return str;
    }

    public boolean isM_newBuyMethod() {
        return m_newBuyMethod;
    }

    public void setM_newBuyMethod(boolean m_newBuyMethod) {
        this.m_newBuyMethod = m_newBuyMethod;
    }

    public Tile[][] getM_tile() {
        return m_tile;
    }

    public void setM_tile(Tile[][] m_tile) {
        this.m_tile = m_tile;
    }

    public Tile[] getM_tileBag() {
        return m_tileBag;
    }

    public void setM_tileBag(Tile[] m_tileBag) {
        this.m_tileBag = m_tileBag;
    }

    public int getM_availTiles() {
        return m_availTiles;
    }

    public void setM_availTiles(int m_availTiles) {
        this.m_availTiles = m_availTiles;
    }

    public Hotel[] getM_hot() {
        return m_hot;
    }

    public void setM_hot(Hotel[] m_hot) {
        this.m_hot = m_hot;
    }

    public Tile[] getM_tilesSafe() {
        return m_tilesSafe;
    }

    public void setM_tilesSafe(Tile[] m_tilesSafe) {
        this.m_tilesSafe = m_tilesSafe;
    }

    public int getM_cSafe() {
        return m_cSafe;
    }

    public void setM_cSafe(int m_cSafe) {
        this.m_cSafe = m_cSafe;
    }

    public int getM_cExamine() {
        return m_cExamine;
    }

    public void setM_cExamine(int m_cExamine) {
        this.m_cExamine = m_cExamine;
    }

    public boolean isM_playingTile() {
        return m_playingTile;
    }

    public void setM_playingTile(boolean m_playingTile) {
        this.m_playingTile = m_playingTile;
    }

    public Wallet getM_wallet() {
        return m_wallet;
    }

    public void setM_wallet(Wallet m_wallet) {
        this.m_wallet = m_wallet;
    }

    public Tile[][] getM_tile2() {
        return m_tile2;
    }

    public void setM_tile2(Tile[][] m_tile2) {
        this.m_tile2 = m_tile2;
    }

    public Tile[] getM_tileBag2() {
        return m_tileBag2;
    }

    public void setM_tileBag2(Tile[] m_tileBag2) {
        this.m_tileBag2 = m_tileBag2;
    }

    public int getM_availTiles2() {
        return m_availTiles2;
    }

    public void setM_availTiles2(int m_availTiles2) {
        this.m_availTiles2 = m_availTiles2;
    }

    public StockTransaction getM_st() {
        return m_st;
    }

    public void setM_st(StockTransaction m_st) {
        this.m_st = m_st;
    }

    public boolean isM_swappingStock() {
        return m_swappingStock;
    }

    public void setM_swappingStock(boolean m_swappingStock) {
        this.m_swappingStock = m_swappingStock;
    }

    public int getDHTMLMergeNum() {
        return DHTMLMergeNum;
    }

    public void setDHTMLMergeNum(int DHTMLMergeNum) {
        this.DHTMLMergeNum = DHTMLMergeNum;
    }

    public int[][] getDHTMLMergeList() {
        return DHTMLMergeList;
    }

    public void setDHTMLMergeList(int[][] DHTMLMergeList) {
        this.DHTMLMergeList = DHTMLMergeList;
    }

    public TextArea getM_text() {
        return m_text;
    }

    public void setM_text(TextArea m_text) {
        this.m_text = m_text;
    }

    public String getM_lastMsg() {
        return m_lastMsg;
    }

    public void setM_lastMsg(String m_lastMsg) {
        this.m_lastMsg = m_lastMsg;
    }

    public Tile[] getM_tilesExamine() {
        return m_tilesExamine;
    }

    public void setM_tilesExamine(Tile[] m_tilesExamine) {
        this.m_tilesExamine = m_tilesExamine;
    }

    public int getM_tradeCnt() {
        return m_tradeCnt;
    }

    public void setM_tradeCnt(int m_tradeCnt) {
        this.m_tradeCnt = m_tradeCnt;
    }

    public int getM_tradeIndex() {
        return m_tradeIndex;
    }

    public void setM_tradeIndex(int m_tradeIndex) {
        this.m_tradeIndex = m_tradeIndex;
    }

    public int getM_current() {
        return m_current;
    }

    public void setM_current(int m_current) {
        this.m_current = m_current;
    }

    public Player[] getM_players() {
        return m_players;
    }

    public void setM_players(Player[] m_players) {
        this.m_players = m_players;
    }

    public AcquireStats getM_statPanel() {
        return m_statPanel;
    }

    public void setM_statPanel(AcquireStats m_statPanel) {
        this.m_statPanel = m_statPanel;
    }

    public DistributionPanel getM_stockPanel() {
        return m_stockPanel;
    }

    public void setM_stockPanel(DistributionPanel m_stockPanel) {
        this.m_stockPanel = m_stockPanel;
    }

    public int getM_currentPlayer() {
        return m_currentPlayer;
    }

    public void setM_currentPlayer(int m_currentPlayer) {
        this.m_currentPlayer = m_currentPlayer;
    }

    public SwapFrame2 getM_swapFrame() {
        return m_swapFrame;
    }

    public void setM_swapFrame(SwapFrame2 m_swapFrame) {
        this.m_swapFrame = m_swapFrame;
    }

}
