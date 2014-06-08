package ackgames.acquire;

import ackgames.acquire.AQC;
import ackgames.acquire.AQCRoom;
import ackgames.acquire.AQCSignIn;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Room extends java.awt.Panel
{
	public Room(int slot,Acquire a)
	{
	    Color bg = Color.lightGray;
	    if (slot%2 == 1){
		    bg= new Color(220,220,220);
		}
        mAquire = a;
        mSlot = slot;
		setLayout(new CardLayout());
		Insets ins = getInsets();
		setSize(ins.left + ins.right + 430,ins.top + ins.bottom + 24);
		emptyPanel.setLayout(new BorderLayout(0,0));
		add("empty", emptyPanel);
		emptyPanel.add(BorderLayout.CENTER,emptyLabel);
		
		roomPanel.setBackground(Color.black);
		gameLabel.setForeground(Color.black);
		gameLabel.setBackground(bg);
		kibitzersLabel.setForeground(Color.black);
		kibitzersLabel.setBackground(bg);
		playersLabel.setBackground(bg);
		playersLabel.setForeground(Color.black);
		kibitzButton.setBackground(bg);
		joinButton.setBackground(bg);
		emptyLabel.setBackground(Color.lightGray);
		
		add("room", roomPanel);
		/************************************************************/
		actionPanel.setLayout(new GridLayout(1,3,1,1));
		actionPanel.add(gameLabel);
		actionPanel.add(joinButton);
		actionPanel.add(kibitzButton);
		GridBagLayout gbl = new GridBagLayout();
        roomPanel.setLayout(gbl);
		gc.fill= gc.NONE;
		gc.insets= new Insets(1,1,1,1);
		/**********************************
		gbl.setConstraints(gameLabel, gc);
		roomPanel.add(gameLabel);
		gc.gridx= gc.RELATIVE;
		gbl.setConstraints(joinButton, gc);
		roomPanel.add(joinButton);
		gbl.setConstraints(kibitzButton, gc);
		roomPanel.add(kibitzButton);
		**********************************/
		
		
		gbl.setConstraints(actionPanel, gc);
		roomPanel.add(actionPanel);
		gc.fill = gc.HORIZONTAL;
		gc.weightx=1;
		gbl.setConstraints(playersLabel, gc);
		roomPanel.add(playersLabel);
		gbl.setConstraints(kibitzersLabel, gc);
		roomPanel.add(kibitzersLabel);
		/***********************************
		
		
		
		roomPanel.setLayout(new GridLayout(1,3,1,1));
		actionPanel.setLayout(new GridLayout(1,3,1,1));
		actionPanel.add(gameLabel);
		actionPanel.add(joinButton);
		actionPanel.add(kibitzButton);
		roomPanel.add(actionPanel);
		roomPanel.add(playersLabel);
		roomPanel.add(kibitzersLabel);
		*****************/
		((CardLayout) getLayout()).show(this,"empty");
		joinButton.addActionListener(new JoinActionListener());
		kibitzButton.addActionListener(new KibitzActionListener());
		
	}
	
	void removeRoom()
	{
	    ((CardLayout) getLayout()).show(this,"empty");
	}
	int getGame()
	{
	    return mGame;
	}
	void setRoom(AQCRoom r)
	{
	    mGame = r.getGame();
	    mVersion = r.getVersion();
	    playersLabel.setText(r.getPlayers());
	    kibitzersLabel.setText(r.getKibitzers());
	    String ver = "  ";
	    if(r.getVersion()== AQC.CENTRAL_AMERICA){
	        ver = "*";
	    }
	    gameLabel.setText("Game #"+ r.getGame()+ver);
	    if(r.getPlaying()){
	        enableJoinButton(false);  
	        mAquire.enableJoinButton(false); 
	    }
	    ((CardLayout) getLayout()).show(this,"room");
	}
	
	void enableJoinButton(boolean b)
	{
	    joinButton.setEnabled(b);
	}
	
	void enableKibitzButton(boolean b)
	{
	    kibitzButton.setEnabled(b);
	}
	
    class KibitzActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            mAquire.restart();
            mAquire.sendmsg( new AQCSignIn( mAquire.m_playerName,mGame,true) );
            mAquire.showState("main");
            mAquire.setGameId(mGame,mVersion);
            
        }
    }
     class JoinActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            mAquire.restart();
            mAquire.sendmsg( new AQCSignIn( mAquire.m_playerName,mGame,false) );
            mAquire.showState("main");
            mAquire.setGameId(mGame,mVersion);
            mAquire.enableCreate(false);
        }
    }
    
    private Label emptyLabel = new Label("");
	private Panel emptyPanel = new java.awt.Panel();
	private Panel roomPanel = new java.awt.Panel();
	private Panel actionPanel = new Panel(); 
	private Label gameLabel = new Label("Game #10 *");
	private Label playersLabel = new Label("kk,mm,nh");
	private Label kibitzersLabel = new Label("kk,tt,yy");
	private Button kibitzButton = new Button("Kibitz");
	private Button joinButton = new Button("Join");
	private GridBagConstraints gc = new GridBagConstraints();
	private int mSlot;
	private int mGame;
	int mVersion;
	private Acquire mAquire;

}