package ackgames.acquire;

import ackgames.acquire.*;

import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.awt.image.*;
import java.net.*;
import java.util.Hashtable;
import java.util.Enumeration;
import java.awt.Point;
import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.*;
import java.net.*;










class ClientThread extends Thread
{
    Socket          fIncoming = null;
    DataInputStream fInputStream = null;
    DataOutputStream fOutputStream = null;
    TextArea fTextArea = null;
    boolean firstTime = true;
    GameBoard fGameBoard;
    Acquire m_Acquire;
    Lobby m_lobby=null;
    String sync= "sync";

    ClientThread( Socket s, DataInputStream instream, DataOutputStream outstream,
                  TextArea ta, GameBoard gb, Acquire a )
    {
        super( "ClientThread" );
        fInputStream = instream;
        fOutputStream = outstream;
        fIncoming = s;
        fTextArea = ta;
        fGameBoard = gb;
        m_Acquire = a;
        m_lobby = new Lobby(a);
        a.setLobby(m_lobby);
        a.setSync(sync);
    }
    void pp(String s) {
        //debug ack
        //System.err.println( s );
    }

    public void run()
    {
        try
        {
            boolean done = false;

            while ( !done )
            {

                int code = fInputStream.readInt();
                synchronized(sync){
                    AQC arg =AQCFactory.createObject(code, fInputStream);

                    if(arg.getCode()== AQC.BROADCAST_MESSAGE||
                            arg.getMessageTarget()== AQC.ALL||
                            arg.getMessageTarget()== AQC.CURRENT){

                        if(arg.getMessageType() == AQC.CHAT){
                            String playerName = arg.getName();
                            // fGameBoard.m_players[arg.getCurrentPlayerID()].getName();
                            m_Acquire.populateChatArea(
                                    (firstTime ? playerName+": " : "\n"+playerName+": ") +
                                    arg.getMessage() );
                            firstTime = false;
                        } else {
                            if(arg.getMessage()!=null){
                                m_Acquire.populateTextArea( "\n>> " + arg.getMessage() );
                            }
                        }
                        if (arg.getTurnMergeTileOff()){
                            fGameBoard.turnMergeTileOff();
                        }
                        if (arg.getLostConnection()){
                            m_Acquire.lostPlayer(AQCFactory.getAQCHideFrames(arg).getName());
                        }

                    } else if(arg.getCode()== AQC.LOBBY_MESSAGE){
                        m_lobby.populateTextAreas(arg.getMessage(),arg.getName());
                        continue;
                    } else if(arg.getCode()== AQC.ERROR_MESSAGE){
                        m_Acquire.m_loginPanel.setErrorOn(arg.getMessage());
                        m_Acquire.showState("login");
                        m_Acquire.m_lobby.resetLobby();
                        m_Acquire.restart();
                        continue;
                    }
                    m_Acquire.setFocus();
                    System.out.println("ARG code ="+code);
                    switch( arg.getCode()/100)
                    {
                        case 1:
                            fGameBoard.processMsg(arg, fOutputStream, fInputStream );
                            if(arg.getCode() == AQC.GAMEBOARD_END_GAME){
                                m_Acquire.resetButtons();
                            }else{
                                m_Acquire.updateb(true);
                            }
                            break;
                        case 2:
                            m_Acquire.processMsg( arg, fOutputStream, fInputStream );
                            if(arg.getCode() == AQC.CLIENT_ABORT){
                                fGameBoard.hideSwapMergeFrame();
                                m_Acquire.resetButtons();
                            }else{
                                m_Acquire.updateb(code!=AQC.CLIENT_BUY_STATE &&
                                        code!=AQC.CLIENT_PLACE_STATE);

                            }
                            break;
                        case 3:
                            m_lobby.processMsg(arg );
                            break;
                    }


                    m_Acquire.mOK2Press=true;

                }

            }

        } catch ( ThreadDeath te )
        {
            System.err.println( "The current thread caught its death trap." );
            throw te;
        } catch ( Exception e )
        {
            System.err.println( e );
        } finally
        {
            m_Acquire.showState("login");
            m_Acquire.m_lobby.resetLobby();
            stop();
        }
    }
}


public class Acquire extends Applet {
    int m_availTiles = 108;
    Hotel m_hot[];
    int m_cSafe;
    int m_cExamine = 0;
    int m_tradeCnt;
    StockTransaction m_stockTrade[] = new StockTransaction[18];
    int m_playerNum = 0;
    String m_playersName[] = new String[6];
    int m_id = -1;
    int m_row;
    int m_column;

    int m_currentPlayer = 0;
    Player m_player;
    Button doneButton ;
    Button overButton;
    StatsGUI m_GUIStats;
    Wallet m_wallet;
    String m_bonusWinners[] = new String[3];
    Tile dummyTile;
    int m_winner;
    Panel m_stock = new Panel();
    Panel m_stockButtons = new Panel();
    Panel m_visibleStats = new Panel();
    Panel m_playerList = new Panel();
    Panel m_playerStats = new Panel();
    Panel m_topText = new Panel();
    Panel m_topHeader = new Panel();
    Panel m_leftTop = new Panel();
    Panel m_gamePanel = new Panel();
    Panel m_top = new Panel(new BorderLayout());
    Button mLobbyButton = new Button("Lobby");
    Label mGameLabel = new Label("Game #10");

    Label m_chatLabel= new Label("Chat");
    Label m_gameMsgLabel= new Label("Game Messages");

    Button playerButton[] = new Button[6];
    Lobby m_lobby;
    Board m_board ;
    int m_gameId =-1;
    static Color xm_color[] = new Color[7];
    TextField fTextField = null;
    TextArea fTextArea = null;
    TextArea fChatArea = null;
    Socket fSocket = null;
    ClientThread fThread = null;
    DataInputStream fInputStream = null;
    DataOutputStream fOutputStream = null;
    GameBoard m_gameBoard;
    boolean bReady = false;
    boolean m_bStatus = false;
    int lastPlayer = -1;
    Frame m_clientFrame = null;
    String m_playerName = null;
    boolean m_resetPanels =false;   
    String sync;
    AbortDialog m_ab= null;
    void setSync(String s)
    {
        sync=s;
    }

    public void showNoStatus()
    {
        m_bStatus = false;
    }

    void showStatus()
    {
        m_bStatus = true;
    }
    public void sendmsg( AQC msg )
    {
        synchronized(fOutputStream)
        {
            msg.writeAQC(fOutputStream);

        }
    }
    void pp(String s) {
        //System.err.println( s );
    }
    void traceError(String s) {
        //System.err.println("AQ TRACE ["+m_playerName+":"+m_gameBoard.getGameState()+"] "+ s );
        //populateTextArea("\nAQ TRACE ["+m_playerName+":"+m_gameBoard.getGameState()+"] "+ s );
    }
    void traceError2(String s) {
        //System.err.println("AQ TRACE ["+m_playerName+":"+m_gameBoard.getGameState()+"] "+ s );
        //populateTextArea("\nAQ TRACE ["+m_playerName+":"+m_gameBoard.getGameState()+"] "+ s );
    }
    void traceError3(String s) {
        m_gameBoard.appendTextMsg(fChatArea,s);
        //System.err.println(
        //      "AQ TRACE ["+m_playerName+":"+m_gameBoard.getGameState()+"] "+ s );

    }

    String m_host;

    void updateDoneButton()
    {
        if(doneButton.getLabel().equals("Join")){
            return;
        }
        boolean b = doneButton.getLabel().equals("Start") ||
                (m_gameBoard.getGameState() != GameBoard.STARTCHAIN&&
                m_gameBoard.getGameState() != GameBoard.MERGE&&
                m_gameBoard.getGameState() != GameBoard.OTHER &&
                m_gameBoard.getGameState() != GameBoard.PLACETILE) ;
        doneButton.setEnabled(b);
    }
    void close()
    {
        try {
            fInputStream.close();
            fOutputStream.close();
            fSocket.close();
        }catch (Exception e){
        }
    }
    void resetButtons()
    {
        doneButton.setLabel("Join");
        doneButton.setEnabled(true);
        overButton.setLabel("Kibitz");
        overButton.setEnabled(true);
        enableCreate(true);
    }
    void updateHotels()
    {

        boolean b = true;
        for(int i = 0;i<7;i++) {
            if(m_gameBoard.getGameState() != GameBoard.STARTCHAIN){
                m_hotelButton[i].setEnabled(bReady);
            } else {
                if(b){
                    b = false;
                    populateTextArea("\n>> Press a hotel button to start a chain");
                }
                traceError3("in updatehotels can start chain"+i);
                m_hotelButton[i].setEnabled(m_gameBoard.canStartChain(i));
                traceError3("in updatehotels after canstartchain"+i);
            }
        }

    }
    synchronized void populateChatArea(String s)
    {
        fChatArea.append(s);
    }
    synchronized void populateTextArea(String s)
    {
        fTextArea.append(s);
    }
    void updateb(boolean b)
    {
        System.out.println("in update boardZZZZZZZZZZZZZZZZZZZZZ");

        if(b){
            updateTiles();
            updateHotels();
            m_player.fillInStats( m_GUIStats);
        }
        m_board.upd(null);
        updateDoneButton();

        setGameStatus(m_gameBoard.getGameState());
        if(m_gameBoard.checkForEnd()){
            if(!overButton.getLabel().equals("Over")){
                overButton.setLabel("Over");
            }
            if(m_gameBoard.getGameState() == m_gameBoard.PLACETILE ||
                    m_gameBoard.getGameState() == m_gameBoard.BUYSTOCK ){
                overButton.setEnabled(true);
            }else{
                overButton.setEnabled(false);
            }
        }else{
            if(m_id != -1){
                if(!overButton.getLabel().equals("End")){
                    overButton.setLabel("End");
                }
                if(!overButton.isEnabled()){
                    overButton.setEnabled(true);
                }
            }
        }
        if(m_gameBoard.getGameState() == GameBoard.BUYSTOCK){
            canBuyStock();
        }
        traceError("end of updateb");
    }
    void canBuyStock()
    {
        if(m_gameBoard.checkForEnd()) {
            return;
        }
        int i=0;
        for(i=0;i<7;i++){
            if (m_gameBoard.canBuyStock(i)){
                break;
            }
        }
        if(i==7){
            populateTextArea("\n>> Unable to buy stock");
            sendmsg(m_gameBoard.nextPerson());
        }
    }
    void enableJoinButton(boolean b)
    {
        if (doneButton.getLabel().equals("Join")){
            doneButton.setEnabled(false);
        }

    }
    void setLobby(Lobby l)
    {
        m_lobby=l;
    }
    void enableCreate(boolean b)
    {
        m_lobby.enableCreate(b);
    }
    void disableKibitz(int i)
    {
        m_lobby.disableKibitz(i);
    }
    void setGameStatus(int state)
    {
        String str = "Place a tile";
        switch (state)
        {
            case GameBoard.PLACETILE:
                str = "Place a tile";
                break;
            case GameBoard.MERGE:
                str = "Merge two or more chains";
                break;
            case GameBoard.BUYSTOCK:
                str = "Buy Stock";
                break;
            case GameBoard.STARTCHAIN:
                str = "Start a new chain";
                break;
            case GameBoard.GAMEOVER:
                str = "Game is over. ";
                break;
        }
        if (m_bStatus == true)
            showStatus(str);
    }

    Tile startTiles[];
    boolean mOK2Press=true;
    void startTiles(AQCStartTile arg)
    {

        bReady = true;
        // m_gameBoard.resetStatPanels();
        startTiles = arg.getStartTiles();

        for (int i = 0; i<m_playerNum;i ++) {

            populateTextArea("\n>> "+m_playersName[i]+ " picks tile "+
                    startTiles[i].getLabel());
            if(m_gameBoard.getVersion() == AQC.CENTRAL_AMERICA){
                m_gameBoard.setTileState(startTiles[i].getRow(),
                        startTiles[i].getColumn(),Tile.EMPTY);
            }else {
                m_gameBoard.setTileState(startTiles[i].getRow(),
                        startTiles[i].getColumn(),Tile.ONBOARD);
            }
        }
        //ca
        boolean f=true;

        int sorted[]=m_gameBoard.sortPlayers(startTiles,m_playerNum);

        for (int i = 0; i<m_playerNum;i ++) {
            if(m_id == sorted[i] &&f){
                m_id = i;
                //hey larry zzz
                m_player.setId(i);
                f= false;
            }
            m_playersName[i] = m_gameBoard.m_players[i].getName();
        }

        m_gameBoard.resetStatPanels();
        //m_board.upd(null);

        if (m_id == 0) {
            setGameStatus(GameBoard.PLACETILE);
            m_gameBoard.setGameState (GameBoard.PLACETILE);
        }
        else{
            m_gameBoard.setGameState (GameBoard.OTHER);
        }
    }
    void replaceTiles(AQCReplaceTile arg)
    {
        int col;
        int row;
        int replace;
        replace = arg.getReplaceTiles().length;
        for (int i = 0; i<replace;i++) {
            row = arg.getReplaceTiles()[i].getRow();
            col = arg.getReplaceTiles()[i].getColumn();
            for (int j = 0; j<6;j++) {
                if (m_player.m_tiles[j] == dummyTile) {
                    m_player.m_tiles[j] = m_gameBoard.m_tile[row][col];
                    m_GUIStats.m_tileButton[j].setFont(new Font("Dialog", Font.BOLD, 12));
                    populateTextArea("\n>> Your new tile is "+m_player.m_tiles[j].getLabel() );
                    break;
                } else if (m_gameBoard.isDead(m_player.m_tiles[j].getRow(),
                        m_player.m_tiles[j].getColumn()) == true) {
                    Tile temp =  m_player.m_tiles[j];

                    m_player.m_tiles[j] = m_gameBoard.m_tile[row][col];
                    m_GUIStats.m_tileButton[j].setFont(new Font("Dialog", Font.BOLD, 12));
                    populateTextArea("\n>> Dead tile "+temp.getLabel() +" is replaced by "+
                            m_player.m_tiles[j].getLabel());

                    break;
                }

            }
        }
        //m_player.fillInStats( m_GUIStats);
    }
    void resetGameInfo(AQCGameInfo cmd,DataOutputStream os )
    {
        int id = cmd.getCurrentPlayerID();
        int cnt = cmd.getPlayerCount();
        m_playerNum = cnt;
        for (int i = 0; i <cnt; i++) {
            m_playersName[i] = cmd.getNames()[i];
            if(cmd.getObserver()) {
                doneButton.setLabel("Join");
                overButton.setLabel("Kibitz");
                overButton.setEnabled(false);
                if(!cmd.getGameStarted()){
                    doneButton.setEnabled(true);
                }else{
                    doneButton.setEnabled(false);
                }
                m_gameBoard.otherPlayer (m_playersName[i],i,0);
                m_gameBoard.setPlaying(false);
            } else {
                if (i == id) {
                    m_id = i;
                    m_gameBoard.m_currentPlayer = i;
                    m_player = new Player(m_playersName[i],os,m_gameBoard,i);
                    m_board.setPlayer(m_player);
                    m_gameBoard.m_swapFrame.setPlayer(m_player);
                    m_gameBoard.m_players[i]=m_player;
                }  else {
                    m_gameBoard.otherPlayer (m_playersName[i],i,0);
                }

                doneButton.setEnabled(true);
                if(cmd.getGameStarted()){
                    doneButton.setLabel("Done");
                }else{
                    doneButton.setLabel("Start");
                }
                overButton.setLabel("End");
                overButton.setEnabled(true);
            }
            m_gameBoard.m_stockPanel.add(i,m_playersName[i]);
            bReady = true;
        }
        m_gameBoard.setPlayerNum(cnt);

    }


    void resetTileRack(AQCGameInfo cmd)
    {
        int col;
        int row;
        int cnt;
        if(cmd.getRackTiles()== null){
            disableKibitz(cmd.getGameID());
            return;
        }
        enableCreate(false);
        cnt =cmd.getRackTiles().length;
        for (int i = 0; i<6;i ++) {
            m_GUIStats.m_tileButton[i].setEnabled(true);
            m_player.m_tiles[i] = dummyTile;
        }
        for (int i = 0; i<cnt;i ++) {
            row = cmd.getRackTiles()[i].getRow();
            col = cmd.getRackTiles()[i].getColumn();
            if(row != -1){
                m_player.m_tiles[i] = m_gameBoard.m_tile[row][col];
            }
        }
        m_player.fillInStats( m_GUIStats);
    }

    void initTheTiles(AQCInitTiles cmd)
    {
        int col;
        int row;
        //m_gameBoard.setTileState(row,col,tile.ONBOARD);
        for (int i = 0; i<6;i ++) {
            m_GUIStats.m_tileButton[i].setEnabled(true);
            row = cmd.getInitTiles()[i].getRow();
            col = cmd.getInitTiles()[i].getColumn();
            m_player.m_tiles[i] = m_gameBoard.m_tile[row][col];
        }
        int cnt = cmd.getStartTiles().length;
        for(int k=0;k<cnt;k++){
            row= cmd.getStartTiles()[k].getRow();
            col =  cmd.getStartTiles()[k].getColumn();
            m_gameBoard.setTileState(row,col,Tile.ONBOARD);
        }


        //m_player.fillInStats( m_GUIStats);
        //m_board.upd(null);

        doneButton.setLabel("Done");
        overButton.setLabel("End");
        overButton.setEnabled(true);
    }
    void addPlayer(AQCAddPlayer cmd)
    {
        setGameId(cmd.getGameID(),cmd.getVersion());
        showState("main");
        enableCreate(false);

        int money = 6000;
        if(cmd.getVersion() ==  AQC.CENTRAL_AMERICA){
            money = 8000;
        }
        m_playerNum = cmd.getNames().length;

        for (int i = 0; i<m_playerNum;i++) {
            String str = cmd.getNames()[i] ;
            if (m_id == -1 && str.equals(m_player.getName())) {
                m_id = i;
                m_gameBoard.setClientPlayer(m_id);
                m_board.setPlayer(m_player);
                m_player.setId(m_id);
                doneButton.setLabel("Start");
                overButton.setLabel("End");
                m_player.setMoney(money);
            }
        }

        for (int i = 0; i<m_playerNum;i++) {
            String str = cmd.getNames()[i] ;
            m_playersName[i] = str;
            m_gameBoard.m_stockPanel.add(i,str);
            m_gameBoard.m_statPanel.addPlayer(i,str);
            if (m_id != i)	{
                if (lastPlayer <i) {
                    lastPlayer = i;
                    m_gameBoard.otherPlayer (str,i,money);
                }
            }
        }

        doneButton.setEnabled(true);
    }
    synchronized void processMsg( AQC cmd, DataOutputStream os,DataInputStream dis )
    {
        switch(cmd.getCode()){
            case AQC.CLIENT_START_TILE:
                startTiles(AQCFactory.getAQCStartTile(cmd));
                break;
            case AQC.CLIENT_REPLACE_TILE:
                replaceTiles(AQCFactory.getAQCReplaceTile(cmd));
                break;
            case AQC.CLIENT_PING:
                if (m_id !=-1){
                    pingPlayer();
                    sendmsg(new AQCACKClient (m_id,0) );
                }
                break;
            case AQC.CLIENT_ACK:
                ackPlayer(cmd.getCurrentPlayerID());
                break;
            case AQC.CLIENT_INFO:
                showState("main");
                AQCGameInfo agi = AQCFactory.getGameInfo(cmd);
                setGameId(cmd.getGameID(),cmd.getVersion());
                resetGameInfo(agi,os);
                resetTileRack(agi);
                m_gameBoard.reproduceGameInfo(agi); 
                m_gameBoard.resetStatPanels();
                break;
            case AQC.CLIENT_INIT_TILES:
                initTheTiles(AQCFactory.getAQCInitTiles(cmd));
                break; 
            case AQC.CLIENT_ADD_PLAYER:
                addPlayer(AQCFactory.getAQCAddPlayer(cmd));
                break;
            case AQC.CLIENT_BUY_STATE:

                hilitePlayer(cmd.getCurrentPlayerID());
                if(m_id == cmd.getCurrentPlayerID()){
                    if (Preference.BEEP){
                        Toolkit.getDefaultToolkit().beep();
                    }
                    m_gameBoard.setGameState(m_gameBoard.BUYSTOCK);
                }
                break;
            case AQC.CLIENT_PLACE_STATE:
                hilitePlayer(cmd.getCurrentPlayerID());
                if(m_id == cmd.getCurrentPlayerID()){
                    if (Preference.BEEP){
                        Toolkit.getDefaultToolkit().beep();
                    }
                    m_gameBoard.setGameState(m_gameBoard.PLACETILE);
                }
                break;
        }

    }





    Button m_hotelButton[] = new Button[7];
    int m_port;
    void doit(String name,String Server,String port)
    {
        try
        {
            InetAddress ad = InetAddress.getLocalHost();
            m_host = Server;
            if(m_host.equals("ackplay")){
                m_host = "24.221.164.49";
            }
            m_port = Integer.parseInt(port);
            if(fSocket != null){
                close();
            }
            fSocket = new Socket( m_host, m_port);
            fInputStream =  new DataInputStream(fSocket.getInputStream());
            fOutputStream = new DataOutputStream( fSocket.getOutputStream() );
        }
        catch ( IOException e )
        {
            System.out.println( "Error: " + e );
            System.out.println( "Cannot find server\n" );
            System.exit(1);
        }
        finally
        {
            m_player = m_gameBoard.initClientPlayer(name,fOutputStream);
            m_player.fillInStats( m_GUIStats);
            fThread = new ClientThread( fSocket, fInputStream,
                    fOutputStream, fTextArea,
                    m_gameBoard,this );
            fThread.start();
            // lma switch
            m_playerName =  name;
            System.out.println("name =="+name);
            //sendmsg( new AQCSignIn( name,0,false) );
            sendmsg( new AQCLobbyFetch(name));
            //populateTextArea("\n>> Press the Start button,when all the players have signed on"); 
        }
    }

    private void add( Panel p, Component c,GridBagLayout gbl,
                      GridBagConstraints gbc,
                      int x,int y,int w, int h)
    {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = w;
        gbc.gridheight = h;
        gbl.setConstraints(c,gbc);
        p.add(c);

    }
    static Color bgcolor = Preference.GAME_BOARD_COLOR;
    //new java.awt.Color(253,252,216) new Color(241,233,220);
    static String m_hotelStr[] = {"Luxor","Tower","American","WorldWide",
                                  "Festival","Continental","Imperial"} ;
    void initTiles() {

        int k = 0;

        // init trades;
        /// init stock
        for (int i=0 ;i<18; i++) {
            m_stockTrade[i] = new StockTransaction();
        }



        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc1 = new GridBagConstraints();
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc1.gridx=0;
        gbc1.gridy=0;
        gbc1.gridheight=Preference.TEXTAREA_HEIGHT;
        gbc1.fill = gbc1.BOTH;
        gbc1.weightx=3;
        m_leftTop.setBackground(new java.awt.Color(195,175,145));
        m_topHeader.setLayout(new GridLayout(1,2,2,2));
        m_topText.setLayout(new GridLayout(1,2,2,2));
        gbc2.gridx=0;
        gbc2.gridy=Preference.TEXTAREA_HEIGHT;
        gbc2.gridheight=1;
        gbc2.fill = gbc1.BOTH;
        m_leftTop.setLayout(gbl);
        fTextArea = new TextArea("",Preference.TEXTAREA_HEIGHT,80,TextArea.SCROLLBARS_VERTICAL_ONLY) ;
        fChatArea = new TextArea("",Preference.TEXTAREA_HEIGHT,80,TextArea.SCROLLBARS_VERTICAL_ONLY) ;
        fTextField = new TextField(3) ;
        fTextArea.setEditable(false);
        fChatArea.setEditable(false);
        if(Preference.GREETINGS !=null){
            populateChatArea(Preference.GREETINGS+"\n");
        }
        fChatArea.setFont(Preference.CHAT_FONT);
        fTextArea.setFont(Preference.GAME_MESSAGE_FONT);
        fChatArea.setForeground(Preference.CHAT_COLOR);
        fTextArea.setForeground(Preference.GAME_MESSAGE_COLOR);
        m_topText.setBackground(BORDER_COLOR);
        m_topHeader.setBackground(BORDER_COLOR);
        fChatArea.setBackground(new Color(172,213,255));
        fTextArea.setBackground(new Color(172,213,255));
        fTextField.setBackground(new Color(172,213,255));
        gbl.setConstraints(fChatArea, gbc1);
        gbl.setConstraints(fTextField, gbc2);
        m_leftTop.add(fChatArea);
        m_leftTop.add(fTextField);
        m_topText.add(m_leftTop);
        m_topText.add(fTextArea);
        m_gameMsgLabel.setAlignment(Label.CENTER);
        m_chatLabel.setAlignment(Label.CENTER);
        m_chatLabel.setBackground(new Color(79,148,233));
        m_gameMsgLabel.setBackground(new Color(79,148,233));
        m_topHeader.add(m_chatLabel);
        m_topHeader.add(m_gameMsgLabel);
        m_chatLabel.setFont(Preference.TEXT_HEADER_FONT);
        m_gameMsgLabel.setFont(Preference.TEXT_HEADER_FONT);
        m_top.add("North",m_topHeader);
        m_top.add("Center",m_topText);
        m_stock.setLayout(new GridLayout(2,1,0,0));
        m_visibleStats.setLayout(new GridLayout(1,7,0,0));
        m_stockButtons.setLayout(new GridLayout(1,7,0,0));
        for (int b = 0; b <7; b++) {
            Button but = new Button(m_hotelStr[b]);
            but.setBackground(Preference.m_color[b]);
            m_stockButtons.add(but);
            m_hotelButton[b]=but;
            but.addActionListener(new StockActionListener());
        }
        for ( int t = 0; t< 7; t++) {
            m_GUIStats.m_stockLabel[t].setBackground(Color.lightGray);
            m_visibleStats.add(m_GUIStats.m_stockLabel[t]);
        }
        m_stock.add(m_stockButtons);
        m_stock.add(m_visibleStats);

        dummyTile = new Tile();
        Tile.setDummy(dummyTile);



        // init players
        m_playerList.setLayout(new GridLayout(6,1));
        m_playerStats.setLayout(new GridLayout(1,10));

        fTextField.addActionListener(new  MyTextActionListener());


        m_playerStats.add(m_GUIStats.m_nameLabel);
        for ( int t = 0; t< 6; t++) {
            m_playerStats.add(m_GUIStats.m_tileButton[t]);
            m_GUIStats.m_tileButton[t].addActionListener(new TileActionListener());
            SymMouse aSymMouse = new SymMouse();
            m_GUIStats.m_tileButton[t].addMouseListener(aSymMouse);

        }
        m_playerStats.add(m_GUIStats.m_moneyLabel);
        doneButton = new Button("Start");
        doneButton.addActionListener(new DoneActionListener());
        overButton = new Button("Over");
        overButton.addActionListener(new OverActionListener());
        m_playerStats.add(doneButton);
        m_playerStats.add(overButton);
        doneButton.setEnabled(false);
        overButton.setEnabled(false);

        //m_stats=new AcquireStats() ;
        m_acquireLabel.setForeground(java.awt.Color.white);
        m_acquireLabel.setFont(new Font("Serif", Font.ITALIC, 36));
        m_fill2Panel.setBackground(new java.awt.Color(172,213,255));
        m_fill1Panel.setBackground(new java.awt.Color(172,213,255));

        m_gameBoard.m_stockPanel.setBackground(bgcolor);
        m_gameBoard.m_statPanel.setBackground(bgcolor);

        m_eastPanel.add(m_gameBoard.m_stockPanel );
        m_eastPanel.add(m_gameBoard.m_statPanel );
        GridBagLayout gbl2= new GridBagLayout();
        Panel lobbyPanel = new Panel(gbl2);
        lobbyPanel.setBackground(new java.awt.Color(79,148,233));
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill =gc.HORIZONTAL;
        gc.weightx=1;
        gc.anchor = gc.WEST;
        gc.insets = new Insets(1,25,2,1);
        mGameLabel.setAlignment(Label.LEFT);
        gbl2.setConstraints(mGameLabel, gc);
        mGameLabel.setForeground(Color.white);
        mGameLabel.setFont(new Font("Serif", Font.BOLD, 14));
        gc.anchor = gc.CENTER;
        gc.insets = new Insets(1,1,2,1);
        gbl2.setConstraints(mLobbyButton, gc);
        lobbyPanel.add(mGameLabel);
        lobbyPanel.add(mLobbyButton);
        mLobbyButton.addActionListener(new LobbyActionListener());
        Panel sePanel = new Panel(new BorderLayout(1,1));
        sePanel.add("South", lobbyPanel);
        sePanel.add("Center", m_gameBoard.m_wallet.getPurchase());
        sePanel.setBackground(new java.awt.Color(79,148,233));
        m_eastPanel.add(sePanel);

        m_centerPanel.add("North",m_stock);
        m_centerPanel.add("Center",m_board);
        m_centerPanel.add("South",m_playerStats);
        m_gamePanel.add("East",m_eastPanel);
        m_gamePanel.add("Center",m_centerPanel);
        m_mainPanel.add("North",m_top);
        m_mainPanel.add("Center",m_gamePanel);

        m_mainPanel.setBackground(BORDER_COLOR);
        m_gamePanel.setBackground(BORDER_COLOR);
        m_lobbyPanel = new LobbyPanel(this);
        add("login",m_loginPanel);
        add("main",m_mainPanel);
        add("lobby",m_lobbyPanel);
        //((CardLayout) getLayout()).show(this,"lobby");

    }
    void showState(String state)
    {
        ((CardLayout) getLayout()).show(this,state);
    }
    static final int BORDER_WIDTH=2;
    static final Color BORDER_COLOR=Color.lightGray;
    Panel m_fill2Panel = new Panel(new BorderLayout(0,0));
    Panel m_fill1Panel = new Panel(new BorderLayout(0,0));
    Panel m_centerPanel = new Panel(new BorderLayout(0,0));
    Panel m_eastPanel = new Panel(new GridLayout(3,1,BORDER_WIDTH,3));
    Label m_acquireLabel = new Label("Acquire");
    //AcquireStats m_stats=null;
    public  void login()
    {

    }
    void setFocus(){
        fTextField.requestFocus();
    }
    int m_curindex =0;
    void hilitePlayer(int k)
    {
        m_gameBoard.m_statPanel.hilite(k);
        m_gameBoard.setCurrent(k);

    }

    void lostPlayer(String s)
    {
        for( int k=0;k<m_playerNum;k++){
            if(m_gameBoard.m_players[k].getName().equals(s)){
                m_gameBoard.m_statPanel.lostConnection(k);
            }
        }

    }
    void ackPlayer(int id)
    {
        populateTextArea("\n>> Player " +
                m_gameBoard.m_players[id].getName() + " is alive");
        m_gameBoard.m_statPanel.ackPlayer(id);
    }
    void pingPlayer()
    {
        populateTextArea("\n>> Being pinged from the server");
        m_gameBoard.m_statPanel.pingPlayer(m_id);
    }

    public void startIt(Frame clientFrame,String host,String port,String name,String ver)
    {
        m_clientFrame= clientFrame;
        clientFrame.addWindowListener( new MyWindowListener());
        initGame(host,port,name,ver);
        //initGame("ackplay","7779","ack","CA");
        setSize(575  ,490);

    }

    void restart()
    {
        int i;
        for (i = 0; i<6;i ++) {
            m_GUIStats.m_tileButton[i].setEnabled(false);
            m_GUIStats.m_tileButton[i].setBackground(BORDER_COLOR);
            m_player.m_tiles[i] = dummyTile;
        }
        bReady = false;
        m_gameBoard.resetGame();
        m_GUIStats.reset();
        m_id = -1;
        lastPlayer=-1;
        m_playerNum = 0;
        m_gameBoard.resetDistributionPanel();
        m_player= m_gameBoard.initClientPlayer(m_player.getName(),fOutputStream);
        m_player.fillInStats( m_GUIStats);


    }


    class MyMouseListener implements MouseListener
    {
        public void mouseExited(MouseEvent e)
        {
            pp("exit button");
        }
        public void mousePressed(MouseEvent e)
        {
        }
        public void mouseClicked(MouseEvent e)
        {
        }
        public void mouseReleased(MouseEvent e)
        {
        }
        public void mouseEntered(MouseEvent e)
        {
            pp("enter button");

        }

    }

    Panel m_mainPanel=new Panel(new BorderLayout(BORDER_WIDTH,BORDER_WIDTH));
    LoginPanel m_loginPanel = null;
    LobbyPanel m_lobbyPanel = null;

    void initGame(String host,String port,String name,String ver)
    {
        if (m_bStatus){
            new Preference(this);
        } else {
            new Preference();
        }
        if (ver != null){
            Preference.VERSION=ver;
        }
        //System.out.println("ver = "+Preference.VERSION);
        m_loginPanel = new LoginPanel(this,host,port,name);
        setLayout(new CardLayout(0,0));
        m_gamePanel.setLayout(new BorderLayout(BORDER_WIDTH,0));
        m_gameBoard = new GameBoard();
        m_gameBoard.initHotels();
        m_wallet = new Wallet(m_gameBoard);
        m_GUIStats = new StatsGUI(m_wallet);
        m_gameBoard.setWallet(m_wallet);
        m_board = new Board(m_gameBoard);
        m_gameBoard.setBoard(m_board);
        initTiles();
        if (name !=null){
            m_loginPanel.mLoginPanel_ActionPerformed(null);
        }
    }
    public void stop() {
        close();
    }
    public void init() {
        super.init();
        this.showStatus();
        String name = getParameter("acquireServer");
        String port = getParameter("port");
        if (name == null) name = "ackplay";
        if (port == null) port = "7779";
        initGame(name,port,null,null);
        setSize(575  ,490);

        //{{REGISTER_LISTENERS
        //SymComponent aSymComponent = new SymComponent();
        //this.addComponentListener(aSymComponent);
        SymMouse aSymMouse = new SymMouse();
        this.addMouseListener(aSymMouse);
        //}}
    }

    Font fff;

    void hiLiteTile(Button b,boolean in)
    {
        synchronized(sync){
            if (b.getLabel().equals("")) return;
            for ( int t = 0; t< 6; t++) {
                if ( b == m_GUIStats.m_tileButton[t]){
                    Tile ti = m_player.m_tiles[t];
                    int row= ti.getRow();
                    int col = ti.getColumn();
                    m_gameBoard.m_tile[row][col].m_dirty=true;
                    m_gameBoard.m_tile[row][col].m_mouseOver=in;
                    m_board.upd(null);
                }

            }
        }
    }
    int plainCnt;
    int plain[] = new int[6];

    void formChain(int i,int j)
    {
        Button b1= m_GUIStats.m_tileButton[i];
        Button b2= m_GUIStats.m_tileButton[j];
        Tile t1 = m_player.m_tiles[i];
        Tile t2 = m_player.m_tiles[j];
        if (t1.getRow() == t2.getRow()){
            if ( t1.getColumn() == (t2.getColumn()+1)  ||
                    t1.getColumn() == (t2.getColumn()-1)){
                b1.setBackground(Color.white);
                b2.setBackground(Color.white);

            }
            return;
        }
        if (t1.getColumn() == t2.getColumn()){
            if ( t1.getRow() == (t2.getRow()+1)  ||
                    t1.getRow() == (t2.getRow()-1)){
                b1.setBackground(Color.white);
                b2.setBackground(Color.white);

            }
            return;
        }
    }
    void  updateTiles()
    {
        if (!bReady) {
            return;
        }
        plainCnt = 0;
        Button b;
        for ( int t = 0; t< 6; t++) {
            b = m_GUIStats.m_tileButton[t];
            b.setForeground(Color.black);
            Tile ti = m_player.m_tiles[t];
            if(ti == dummyTile){
                continue;
            }
            m_gameBoard.m_tile[ti.getRow()][ti.getColumn()].setM_inRack(true);
            traceError3("in updatetiles surrounding tiles " + ti.getRow()+ " "+ti.getColumn() );
            AboutNeighbors an =
                    m_gameBoard.surroundingTiles(ti);
            traceError3("in updatetiles after surrounding tiles");
            switch(an.getType())
            {
                case Tile.GROW:
                    traceError3("GROW " + an.getGrower());
                    b.setBackground(Preference.m_color[an.getGrower()]);
                    break;
                case Tile.START:
                    traceError3("START");
                    b.setBackground(Color.magenta);
                    break;
                case Tile.DEAD:
                    traceError3("DEAD");
                    m_gameBoard.m_tile[ti.getRow()][ti.getColumn()].setM_inRack(false);
                    b.setBackground(Color.black);
                    break;
                case Tile.NONPLAYBLE:
                    traceError3("NONPLAYABLE");
                    if(m_gameBoard.isDead(ti.getRow(),ti.getColumn())){
                        m_gameBoard.m_tile[ti.getRow()][ti.getColumn()].setM_inRack(false);
                    }
                    b.setBackground(Color.darkGray);
                    break;
                case Tile.MERGE:
                    traceError3("MERGE");
                    b.setBackground(new Color(35,100,35));
                    b.setForeground(Color.white);
                    break;
                default:
                    traceError3("DEFAULT "+ plainCnt);
                    b.setBackground(Color.lightGray);
                    plain[plainCnt]=t;
                    if (plainCnt>0) {
                        traceError3("in updatetiles formchain");
                        for(int j= plainCnt-1;j>=0;j--){
                            traceError3("in updatetiles formchain"+j);
                            formChain(t,plain[j]);
                        }
                        traceError3("in updatetiles after formchain");

                    }
                    plainCnt++;
            }
        }

    }

    void setGameId (int id, int version)
    {
        String ver ="";
        if(version == AQC.CENTRAL_AMERICA){
            ver= "(ca)";
        }
        mGameLabel.setText("Game #"+id+ver);
        m_gameId = id;
        m_gameBoard.setVersion(version);
    }
    void playTile( Button b)
    {
        if (b.getLabel().equals("")) return;
        for ( int i = 0; i< 6; i++)
            m_GUIStats.m_tileButton[i].setFont(new Font("Dialog", Font.PLAIN, 12));
        for ( int t = 0; t< 6; t++) {
            if ( b == m_GUIStats.m_tileButton[t])
            {
                Tile ti = m_player.m_tiles[t];
                if (m_gameBoard.isDead(ti.getRow(), ti.getColumn() ) == true) {
                    pp("play a dead tile larry");
                    m_player.m_tiles[t] = dummyTile;
                    b.setLabel("");
                    return ;
                }
                if (m_gameBoard.isNonPlayable(ti.getRow(), ti.getColumn()) ==false) {
                    b.setBackground(Color.lightGray);
                    b.setLabel("");
                    m_player.m_tiles[t] = dummyTile;
                    m_gameBoard.m_playingTile= true;
                    sendmsg(
                            new AQCPlayTile(m_player.getId(),0,ti,m_player.getName()));
                    break;
                }
                //ti.m_state=Tile.EMPTY;

            }

        }
    }

    public boolean press(int i) {
        synchronized (sync){
            if (mOK2Press && m_gameBoard.getGameState() == GameBoard.STARTCHAIN) {
                mOK2Press=false;
                if (m_gameBoard.canStartChain(i) == true){
                    sendmsg(new AQCStartHotel(m_player.m_id,0,i,m_hotelStr[i],
                            m_player.getName()));
                }
            } else {
                if (bReady) {
                    m_gameBoard.showStock(i);
                    m_gameBoard.m_stockPanel.showDetail();
                }
            }
        }
        return true;
    }

    class SymMouse extends java.awt.event.MouseAdapter
   {
        public void mouseExited(java.awt.event.MouseEvent e)
        {
            traceError3("before hiLiteTile");
            hiLiteTile((Button)e.getComponent(),false);
            traceError3("after hiLiteTile");


        }

        public void mouseEntered(java.awt.event.MouseEvent e)
        {
            try{
                hiLiteTile((Button)e.getComponent(),true);
            }catch(Exception ee){
                System.out.println("e.getSource() = " + e.getComponent().toString());
            }

        }
    }
    class MyTextActionListener implements ActionListener
   {
        public void actionPerformed(ActionEvent e)
        {
            String text = fTextField.getText();
            if (text.length()==0){
                return;
            }
            if (text.equalsIgnoreCase("clr")){
                fChatArea.setText("");
                fTextField.setText("");
                return;
            }
            if (text.equalsIgnoreCase("beep")){
                Preference.BEEP = !Preference.BEEP;
                fTextField.setText("");
                return;
            }

            sendmsg( new AQC(m_player.m_id,text,AQC.CHAT) );
            fTextField.setText("");
        }
    }
    class PlayerActionListener implements ActionListener
   {
        public void actionPerformed(ActionEvent e)
        {
            for ( int i = 0; i< m_playerNum;i++) {
                if (( e.getActionCommand()).equals(m_playersName[i])) {
                    if (bReady) {
                        m_gameBoard.showStat();
                    }
                    break;
                }
            }

        }
    }
    class StockActionListener implements ActionListener
   {
        public void actionPerformed(ActionEvent e)
        {
            for ( int i = 0; i< 7;i++) {
                if (( e.getActionCommand()).equals(m_hotelStr[i])) {
                    if (bReady) {
                        press(i);
                    }
                    break;
                }
            }

        }
    }

    class LobbyActionListener implements ActionListener
   {
        public void actionPerformed(ActionEvent e)
        {
            m_lobby.setVisible();
        }
    }
    class OverActionListener implements ActionListener
   {
        public void actionPerformed(ActionEvent e)
        {
            if (e.getActionCommand().equals("Kibitz")) {
                restart();
                sendmsg( new AQCSignIn( m_playerName,m_gameId,true,
                        m_gameBoard.getVersion()) );
                overButton.setEnabled(false);
            }else if(e.getActionCommand().equals("End")) {
                if(m_ab== null){
                    Object f= getParent();
                    if( f instanceof Frame){
                        f= getParent();
                    }else{
                        f= getParent().getParent();
                    }
                    m_ab = new AbortDialog((Frame)f,true);
                }
                m_ab.setVisible(true);
                if(m_ab.quit){
                    sendmsg( new AQCGameAbort(m_playerName,m_id,m_gameId));
                }
            }else if ( m_gameBoard.getGameState() != GameBoard.OTHER &&
                    m_gameBoard.getGameState() != GameBoard.MERGE) {
                AQCBuyHotel bh = new AQCBuyHotel(m_id,0,true);
                m_wallet.setAQCBuyHotel(bh,m_player.getName());
                sendmsg( bh );
            }
        }
    }
    String m_junk = null;
    class DoneActionListener implements ActionListener
   {
        public void actionPerformed(ActionEvent e)
        {

            if (e.getActionCommand().equals("Join")) {
                restart();
                sendmsg( new AQCSignIn( m_playerName,m_gameId,false,m_gameBoard.getVersion()) );
                enableCreate(false);
            }
            if (e.getActionCommand().equals("Start")) {
                sendmsg( new AQCStart(m_id,m_gameId) );
            }
            else if (e.getActionCommand().equals("Done")) {
                if ( m_gameBoard.getGameState() != GameBoard.OTHER &&
                        m_gameBoard.getGameState() != GameBoard.STARTCHAIN&&
                        m_gameBoard.getGameState() != GameBoard.MERGE&&
                        m_gameBoard.getGameState() != GameBoard.PLACETILE) {
                    setGameStatus(m_gameBoard.getGameState());
                    sendmsg(m_gameBoard.nextPerson());
                }
            }

        }
    }

    class TileActionListener implements ActionListener
   {
        public void actionPerformed(ActionEvent e)
        {

            if( m_gameBoard.m_playingTile) return;

            if ( m_gameBoard.getGameState() == GameBoard.PLACETILE){
                synchronized(sync){
                    traceError3("before playtile");
                    playTile((Button)e.getSource());
                    traceError3("after playtile");
                }
            }
        }
    }

    class MyWindowListener implements WindowListener
    {
        public	void windowActivated(WindowEvent e)
        {
        }
        public	void windowClosed(WindowEvent e)
        {
        }

        public  void windowClosing(WindowEvent e)
        {
            m_clientFrame.dispose();
            System.exit(0);
        }


        public	void windowDeactivated(WindowEvent e)
        {
        }

        public void windowDeiconified( WindowEvent e)
        {
        }

        public void windowIconified( WindowEvent e)
        {
        }

        public void windowOpened(WindowEvent e)
        {
        }

    }


}
