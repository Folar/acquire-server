package ackgames.acquire;

import ackgames.acquire.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class LobbyPanel extends java.awt.Panel
{
	public LobbyPanel(Acquire a)
	{
	    setLayout(new BorderLayout());
	    mAquire = a;
        GridBagLayout gbl = new GridBagLayout();
		roomPanel.setLayout(gbl);
		GridBagLayout gbl2 = new GridBagLayout();
		gc.gridwidth= gc.REMAINDER;
	    gc.weightx =1;
		gc.fill=gc.HORIZONTAL;
		for(int i=0;i<Preference.MAX_GAMES;i++){
	        rooms[i] = new Room(i,a);
		    gbl.setConstraints(rooms[i], gc);
		    roomPanel.add(rooms[i]);
		}
		chatPanel.setLayout(gbl2);
		
		ta = new TextArea("",Preference.TEXTAREA_HEIGHT,80,TextArea.SCROLLBARS_VERTICAL_ONLY) ;
	    tf = new TextField(80) ;
	    ta.setEditable(false);
	    ta.setBackground(new Color(172,213,255));
	    tf.setBackground(new Color(172,213,255));
	    ta.setFont(Preference.CHAT_FONT);
	    ta.setForeground(Preference.CHAT_COLOR);
	    
	    
	    GridBagConstraints gbc1 = new GridBagConstraints();
	    GridBagConstraints gbc2 = new GridBagConstraints();
	    GridBagConstraints gbc3 = new GridBagConstraints();
	    GridBagConstraints gbc4 = new GridBagConstraints();
	    GridBagConstraints gbc5 = new GridBagConstraints();
	    gbc1.gridx=0;
	    gbc1.gridy=0;
	    gbc1.fill = gbc1.BOTH;
	    gbc1.weightx=1;
	    gbc1.gridwidth= gc.REMAINDER;
	    
	    gbc2.gridy=gbc2.RELATIVE;
	    gbc2.gridwidth= gc.REMAINDER;
        gbc2.fill = gbc2.HORIZONTAL;
		gbl2.setConstraints(ta, gbc1);
        gbl2.setConstraints(tf, gbc2);
        gbl2.setConstraints(roomLabel, gbc2);
        gbl2.setConstraints(roomPanel, gbc2);
        
        GridBagLayout gbl5 = new GridBagLayout();
        Panel xx= new Panel(gbl5);
        
        gbc5.insets= new Insets(4,5,5,0);
        gbl5.setConstraints(createButton, gbc5);
        gbc5.insets= new Insets(4,75,5,0);
        gbl5.setConstraints(CA, gbc5);
        chatPanel.add(ta);
        chatPanel.add(tf);
        chatPanel.add(roomLabel);
        chatPanel.add(roomPanel);
        xx.add(createButton);
        xx.add(CA);
		chatPanel.add(xx);
		GridBagLayout gbl3 = new GridBagLayout();
		adminPanel.setLayout(gbl3);
		gbc3.insets= new Insets(4,5,0,0);
		gbl3.setConstraints(pingButton, gbc3);
		gbl3.setConstraints(saveButton, gbc3);
		gbl3.setConstraints(loadButton, gbc3);
		Label l = new Label("File:");
		gbc3.insets= new Insets(4,20,0,0);
		gbl3.setConstraints(l, gbc3);
		gbc3.insets= new Insets(4,0,0,0);
		gbl3.setConstraints(fileName, gbc3);
		gbc3.insets= new Insets(4,20,0,0);
		gbl3.setConstraints(timeoutButton, gbc3);
		gbc3.insets= new Insets(4,3,0,0);
		gbl3.setConstraints(to, gbc3);
		adminPanel.add(pingButton);
		adminPanel.add(saveButton);
		adminPanel.add(loadButton);
		adminPanel.add(l);
		adminPanel.add(fileName);
		adminPanel.add(timeoutButton);
		adminPanel.add(to);
		gbc4.gridx= 0;
        gbc4.insets= new Insets(4,10,0,0);
		
		gbl2.setConstraints(adminPanel, gbc4);
		chatPanel.add(adminPanel);
		
		add(chatPanel,BorderLayout.NORTH);
		LobbyActionListener x= new LobbyActionListener();
		createButton.addActionListener(x);
		loadButton.addActionListener(x);
		saveButton.addActionListener(x);
		pingButton.addActionListener(x);
		timeoutButton.addActionListener(x);
	    tf.addActionListener(new  MyTextActionListener());
	    setBackground(new Color(79,148,233));
	    
	    //if(false)
	        showAdminPanel(false);
	}
   void showAdminPanel(boolean b)
   {
        adminPanel.setVisible(b);
   }
  
   void enableCreateButton(boolean b)
   {  
        createButton.setEnabled(b);
   }
   void enableCreate(boolean b)
   {    
        if(b && !mAquire.m_lobby.isLobbyFull()){
            createButton.setEnabled(true);
        }else{
            createButton.setEnabled(false);
        }
        for(int i=0;i<Preference.MAX_GAMES;i++){
	        rooms[i].enableKibitzButton(b);
	        rooms[i].enableJoinButton(b);
	    }
   }
   void disableKibitz(int id)
   {
        for(int i=0;i<Preference.MAX_GAMES;i++){
            if(rooms[i].getGame() == id){
	            rooms[i].enableKibitzButton(false);
	        }else{
	            rooms[i].enableKibitzButton(true);
	        }
	    }
   }
   void resetLobby()
   {
        for(int i=0;i<Preference.MAX_GAMES;i++){
            rooms[i].removeRoom();  
            rooms[i].enableKibitzButton(true);
            rooms[i].enableJoinButton(true);
        }
          
        createButton.setEnabled(true);
          
   }
	boolean firstTime = true;
	void populateTextArea(String text,String name)
	{
	    ta.append((firstTime ? name+": " : "\n"+name+": ") + text);
		firstTime = false;   
    }
	Panel roomPanel = new Panel();
	Panel chatPanel = new Panel();
	Acquire mAquire = null;
	private Button createButton = new Button("Create");
	private Checkbox CA= new Checkbox("Central American Version *");
	GridBagConstraints gc = new GridBagConstraints();
    Room rooms[]= new Room[Preference.MAX_GAMES];
    TextArea ta = null;
    TextField tf = null;
    RoomLabel roomLabel = new RoomLabel();
    Panel adminPanel = new Panel();
	TextField fileName = new TextField(20);
	TextField to = new TextField(5);
	Button loadButton = new Button("Load");
	Button saveButton = new Button("Save");
	Button pingButton = new Button("Ping");
	Button timeoutButton = new Button("Timeout");
	    
	class LobbyActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if (e.getActionCommand().equals("Create")) {
                mAquire.restart();
                mAquire.sendmsg( new AQCLobbyCreate( mAquire.m_playerName,CA.getState()) );
            }else if (e.getActionCommand().equals("Load")) {
                if (fileName.getText() != null && fileName.getText().length()>0){
                    mAquire.sendmsg( new AQCLobbyFile(mAquire.m_playerName,fileName.getText()));
                    fileName.setText("");
                    mAquire.showState("main");
                }
            }else if (e.getActionCommand().equals("Save")) {
                 if (fileName.getText() != null && fileName.getText().length()>0){
                    mAquire.sendmsg( new AQCLobbyFile(mAquire.m_gameId,fileName.getText()));
                    fileName.setText("");
                }
            }else if (e.getActionCommand().equals("Timeout")) {
                 if (to.getText() != null && to.getText().length()>0){
                    int t;
                    try {
                        t = Integer.parseInt(to.getText());
                        to.setText("");
                        mAquire.sendmsg( new AQCTimeout(t));
                    }catch (Exception ex){
                    }
                }
            }else{
                if(mAquire.m_gameId!=-1){
                    mAquire.sendmsg( new AQCPing(mAquire.m_gameId));
                }
            }
        }
    }
    class MyTextActionListener implements ActionListener
    {
       public void actionPerformed(ActionEvent e)
       {
	        String text = tf.getText();
	        if (text.length()==0){
	            return;
	        }
	        if(text.equals("//")){
	            tf.setText("");
	            showAdminPanel(!adminPanel.isVisible());
	            return;
	        }
	        AQC aqc = new AQC(mAquire.m_player.getM_id(),text,AQC.LOBBY);
	        aqc.setName(mAquire.m_playerName);
	        mAquire.sendmsg( aqc );
	        tf.setText("");     
       }	    
	}
	class RoomLabel extends java.awt.Panel
    {
	    public RoomLabel()
	    {

		    kibitzersLabel.setAlignment(Label.CENTER);
		    playersLabel.setAlignment(Label.CENTER);
		    GridBagLayout gbl = new GridBagLayout();
		    setBackground(new Color(79,148,233));
            setLayout(gbl);
		    gc.gridx= gc.RELATIVE;
		    gc.fill = gc.HORIZONTAL;
		    gc.weightx=30;
		   
		    gbl.setConstraints(gameLabel, gc);
		    add(gameLabel);
		    gc.weightx=50;
		    gbl.setConstraints(playersLabel, gc);
		    add(playersLabel);
		    gbl.setConstraints(kibitzersLabel, gc);
		    add(kibitzersLabel);
	    }
	    
	    Label gameLabel = new Label("Games");
	    Label playersLabel = new Label("Players");
	    Label kibitzersLabel = new Label("Kibitzers");
	    GridBagConstraints gc = new GridBagConstraints();
	    

    }
}