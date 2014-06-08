package ackgames.acquire;

import java.awt.*;

public class AcquireStats extends java.awt.Panel
{
    //Color wbg= new java.awt.Color(110,210,255);
    static Color wbg= Preference.GAME_BOARD_COLOR;
    public Dimension getPreferredSize()
    {
        return new Dimension(240,85);
    }
    String m_hLabel[]={"L","T","A","W","F","C","I"};
	public AcquireStats(GameBoard gb)
	{
		m_gb =gb;
		setLayout(new BorderLayout(0,0));
		setBackground(wbg);
		setSize(255,176);
		m_hotelPanel.setLayout(new GridLayout(7,7,0,0));
		add(BorderLayout.CENTER, m_hotelPanel);
		m_hotelPanel.setBounds(56,0,138,176);
        for(int i=0;i<7;i++ )
        {
            m_header[i]=new Label(m_hLabel[i]);
            m_header[i].setBackground(Preference.m_color[i]);
		    m_header[i].setForeground(java.awt.Color.black);
		    m_header[i].setFont(new Font("Dialog", Font.PLAIN, 12));
            m_hotelPanel.add(m_header[i]);
        }
        for(int i= 0;i<6;i++){
            for (int j=1;j<8;j++){
                 m_stats[i][j]=new Label("0");
                 m_stats[i][j].setBackground(wbg);
                 //m_stats[i][j].setForeground(Acquire.bgcolor);
                 m_stats[i][j].setForeground(wbg);
                m_hotelPanel.add(m_stats[i][j]);
            }
        }


		playerPanel.setLayout(new GridLayout(7,1,0,0));
		add(BorderLayout.WEST, playerPanel);

        playerPanel.add(playername);
		playerPanel.setBounds(0,0,54,176);
		playername.setText("             ");
		playerPanel.add(playername);
        for(int i= 0;i<6;i++){
            m_stats[i][0]=new Label("player"+i);
            m_stats[i][0].setBackground(wbg);
            playerPanel.add(m_stats[i][0]);
        }



		moneyPanel.setLayout(new GridLayout(7,1,0,0));
		add(BorderLayout.EAST, moneyPanel);
		moneyPanel.setFont(new Font("Dialog", Font.PLAIN, 12));
		moneyPanel.setBounds(196,0,59,176);

		moneyPanel.add(money);


        for(int i= 0;i<6;i++){
            m_stats[i][8]=new Label("money"+i);
            m_stats[i][8].setBackground(wbg);
            moneyPanel.add(m_stats[i][8]);
        }

	
        setup();
		
	}
    
	//{{DECLARE_CONTROLS
	java.awt.Panel m_hotelPanel = new java.awt.Panel();

	java.awt.Panel playerPanel = new java.awt.Panel();

    java.awt.Label playername = new java.awt.Label();
    java.awt.Label money = new java.awt.Label("$$$");
	java.awt.Panel moneyPanel = new java.awt.Panel();



    Label m_stats[][]=new Label[6][9];
    Label m_header[] =new Label[7];
    //Label m_size[] =new Label[7];
    void setup()
    {

        /*m_size[0]=lsz;    
        m_size[1]=tsz;      
        m_size[2]=asz;  
        m_size[3]=wsz;  
        m_size[4]=fsz;      
        m_size[5]=csz;  
        m_size[6]=isz;*/  
          

        for(int k= 0;k<7;k++){
            m_header[k].setBackground(Preference.m_color[k]);
            //m_size[k].setBackground(Preference.m_color[k]);
            m_header[k].setFont(new Font("Dialog", 
		                                 Font.PLAIN , 12));
        }    
        for(int i= 0;i<6;i++){
            for (int j=0;j<9;j++){
                 m_stats[i][j].setBackground(wbg);
                 //m_stats[i][j].setForeground(Acquire.bgcolor);
                 m_stats[i][j].setForeground(wbg);
            }
        }   
        money.setBackground(Color.white);
        playername.setBackground(Color.white);
        
    }
    int m_playerCount = 0;
    void setName(int index,String name)
    {
        m_stats[index][0].setText(name);
    }
    void setMoney(int index,String name)
    {
        m_stats[index][8].setText(name);
    }
    void setHotel(int index,int hotel,String name)
    {
        m_stats[index][hotel].setText(name);
    }
    public void addPlayer(int i, String str)
    {
        
        for (int j=0;j<9;j++){
            if (i%2 == 0){
                m_stats[i][j].setBackground(Color.white);
            }else{
                m_stats[i][j].setBackground(blue);
            }
            m_stats[i][j].setForeground(Color.black);
            m_stats[i][j].setText("0");
            m_stats[i][j].setFont(new Font("Dialog", 
		                                Font.PLAIN , 12));
        }
        setName(i, str);
        if(m_gb.getVersion()==AQC.CENTRAL_AMERICA){
            setMoney(i,"8000");
        }else{
            setMoney(i,"6000");
        }
    }
    public void setPlayerCount(GameBoard gb)
    {
        m_playerCount = gb.m_playerNum;
        m_gb =gb;
        for(int i= 0;i<6;i++){
            for (int j=0;j<9;j++){
                 if(i< m_playerCount){
                    
                    if (i%2 == 0){
                        m_stats[i][j].setBackground(Color.white);
                    }else{
                        m_stats[i][j].setBackground(blue);
                    }
                    m_stats[i][j].setForeground(Color.black);
                    this.setName(i, m_gb.getM_players()[i].getName());
                    this.setMoney(i,Integer.toString(m_gb.getM_players()[i].getMoney()));
                 } else {
                     //m_stats[i][j].setBackground(Acquire.bgcolor);
                     m_stats[i][j].setBackground(wbg);
                     m_stats[i][j].setForeground(wbg);
                 }
            }
        }
    }
    void setStatsDuringSwap(int survivor,int defunct,int keep,
                            int swap,int sell, int index,int money)
    {
        setHotel(index,survivor+1,Integer.toString(swap));
        setHotel(index,defunct+1,Integer.toString(keep)); 
        setMoney(index,Integer.toString(money));
        int avs =  m_gb.getM_hot()[survivor].getAvailShares()-(swap - m_gb.getM_players()[index].m_hotels[survivor] );
        m_gb.getM_stockPanel().m_hotelTable.updateTable( avs,survivor);
        int avd =  m_gb.getM_hot()[defunct].getAvailShares()+ m_gb.getM_players()[index].m_hotels[defunct] - keep;
        m_gb.getM_stockPanel().m_hotelTable.updateTable( avd,defunct);
        m_gb.getM_wallet().m_hotels[defunct].mLabel.setText(keep+"/"+avd+"/$"+ m_gb.getM_hot()[defunct].price());
        m_gb.getM_wallet().m_hotels[survivor].mLabel.setText(swap+"/"+avs+"/$"+ m_gb.getM_hot()[survivor].price());
        m_gb.getM_stockPanel().m_defunct= defunct;
        m_gb.getM_stockPanel().m_survivor= survivor;
        m_gb.getM_stockPanel().m_davail= avd;
        m_gb.getM_stockPanel().m_savail= avs;
        m_gb.getM_stockPanel().m_down= keep;
        m_gb.getM_stockPanel().m_sown= swap;
    }
    void setStatsByWallet(Wallet w,int index)
    {
       for (int j=1;j<9;j++){
            if(j<8){
                this.setHotel(index, j,Integer.toString( w.m_hotels[j-1].getShares() ));
            }else{
                setMoney(index,Integer.toString(w.m_originalMoney -w.m_total));
            }
        }
    }
   
    void updateStats()
    {
        if( m_gb == null) return;
        for(int i= 0;i<m_playerCount;i++){
            for (int j=1;j<9;j++){
                if(j<8){
                    this.setHotel(i, j,Integer.toString( m_gb.getM_players()[i].m_hotels[j-1]));
                }else{
                    setMoney(i,Integer.toString(m_gb.getM_players()[i].getMoney()));
                }
            }
        }
        for(int k= 0;k<7;k++){
            //m_size[k].setText(Integer.toString(m_gb.m_hot[k].count()));
            if(m_gb.getM_hot()[k].price()==0){
                m_header[k].setFont(new Font("Dialog", 
		                                 Font.PLAIN , 12));
            }else {
                m_header[k].setFont(new Font("Dialog", 
		                                 Font.BOLD , 12));
            }
        }
        m_gb.getM_stockPanel().m_hotelTable.updateTable(m_gb);
    }
    void updateSize()
    {
        if(m_gb == null) return;
        /*for(int k= 0;k<7;k++){
            m_size[k].setText(Integer.toString(m_gb.m_hot[k].count()));
        }*/
        m_gb.getM_stockPanel().m_hotelTable.updateTable(m_gb);
    }
    GameBoard m_gb = null;
    static Color blue = new Color(110,210,255);             
    public void lostConnection(int index)
    {
        for(int m=0;m<9;m++){
            m_stats[index][m].setForeground(Color.red);
        }
    }
    
    public void ackPlayer(int index)
    {
        for(int m=0;m<9;m++){
            m_stats[index][m].setForeground(Color.black);
        }
    }
    
    public void pingPlayer(int id)
    {
        for(int m=0;m<m_playerCount;m++){
            if(m ==id)continue;
            lostConnection(m);
        }
    }
    public void hilite(int index)
    {
        
        for(int i=0;i<m_playerCount;i++){
            for(int m=0;m<9;m++){
                 m_stats[i][m].setForeground(Color.black);
            }
            if (i==index) continue;
            for (int j=0;j<9;j++){
                if (i%2 == 1){
                     m_stats[i][j].setBackground(blue);
                }else {
                    m_stats[i][j].setBackground(Color.white);
                }
                m_stats[i][j].setFont(new Font("Dialog", 
		                                 Font.PLAIN , 12));
            }
        }
        if(m_playerCount>0){
            for (int k=0;k<9;k++){
                    
                m_stats[index][k].setBackground(Color.lightGray);
                m_stats[index][k].setFont(new Font("Dialog", 
		                                    Font.BOLD , 12));  
            }
        }
    }            

      
}
