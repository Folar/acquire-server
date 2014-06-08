package ackgames.acquire;

import java.awt.*;

public class HotelRegister extends java.awt.Panel
{
    int m_max;
    int m_cnt;
    Hotel m_hot;
    Player m_player;
    GameBoard m_gameBoard;
    int m_originalShares;
    int m_originalAvailShares;
    void setValue(int cnt)
    {
        m_cnt=cnt;
        m_wallet.calc(m_hotel);
        String str = Integer.toString(getShares())+"/"+ Integer.toString(getAvailShares())+
                     "/$"+Integer.toString(getCost());
        if (m_gameBoard.getM_stockPanel().viewing() == m_hotel) {
	         m_gameBoard.showStock(m_hotel);
	    }  
	    m_gameBoard.getM_stockPanel().updateTable( getAvailShares(),m_hotel);
        mLabel2.setText(str);             
    }
    int getCost()
    {
        return m_hot.price();
    }
    int getAvailShares()
    {
        return m_originalAvailShares - m_cnt;
    }
    int getShares()
    {
        return m_originalShares + m_cnt;
    }
    void setRegValue(int cnt)
    {
        m_cnt=cnt;
        mReg.setRegValue(m_cnt);
        String str = Integer.toString(getShares())+"/"+ Integer.toString(getAvailShares())+
                     "/"+Integer.toString(getCost());
        if (m_gameBoard.getM_stockPanel().viewing() == m_hotel) {
	         m_gameBoard.showStock(m_hotel);
	    }               
        mLabel2.setText(str);
    }
    void incr()
    {
        if(m_max>m_cnt){
           m_cnt++;
           mReg.setRegValue(m_cnt);
           setValue(m_cnt);
        }
    }
   
    public Dimension getPreferredSize()
    {
        return new Dimension(40,20);
    }
    void setup()
    {
        m_player= m_gameBoard.getM_players()[m_gameBoard.getM_currentPlayer()];
        m_originalShares= m_player.m_hotels[m_hotel];
        m_originalAvailShares = m_hot.getAvailShares();
       
        if(!m_gameBoard.canBuyStock(m_hotel)){
            return;
        }
        
        ((CardLayout) getLayout()).show(this,"hotelBuy");
       
        m_max =  m_player.getMoney()/m_hot.price();
        m_max = m_max>= 3?3:m_max;
        m_max = m_max>= m_hot.getAvailShares()? m_hot.getAvailShares():m_max;
        mReg.setRegValue(0); 
        mReg.setMinimum(0);
        mReg.setMaximum(m_max);
    }
    void reset()
    {
        m_max =0;
        m_cnt =0;
        ((CardLayout) getLayout()).show(this,"hotelLabel");
    }
    
	public HotelRegister(int hotel,Wallet w)
	{
	    m_gameBoard = w.m_gameBoard;
	   
	    m_hot= m_gameBoard.getM_hot()[hotel];
        w.m_hotels[hotel]=this;
        m_hotel=hotel;
        m_wallet=w;
        if (hotel<2){
		    mLabel.setText("0/25/$200");
		    mLabel2.setText("0/25/$200");
	    }else if (hotel<5){
		    mLabel.setText("0/25/$300");
		    mLabel2.setText("0/25/$300");
	    }else{
		    mLabel.setText("0/25/$400");
		    mLabel2.setText("0/25/$400");
		}
		//{{INIT_CONTROLS
		setLayout(new CardLayout(0,0));
		Insets ins = getInsets();
		setSize(117,30);
		labelPanel.setLayout(new BorderLayout(0,0));
		add("hotelLabel", labelPanel);
		labelPanel.setBounds(0,0,117,30);
		mLabel.setText("text");
		mLabel2.setAlignment(java.awt.Label.CENTER);
		mLabel.setAlignment(java.awt.Label.CENTER);
		labelPanel.add(BorderLayout.CENTER,mLabel);
		mLabel.setBounds(0,0,117,30);
		buyPanel.setLayout(new BorderLayout(0,0));
		add("hotelBuy", buyPanel);
		buyPanel.setBounds(0,0,117,30);
		buyPanel.setVisible(false);
		mLabel2.setText("0/25/$300");
		buyPanel.add(BorderLayout.CENTER,mLabel2);
		mLabel2.setBackground(java.awt.Color.lightGray);
		mLabel2.setFont(Preference.STOCK_STATS_FONT );
		mLabel.setFont(Preference.STOCK_STATS_FONT_LARGE );
		mLabel2.setBounds(0,0,20,20);
		mReg.setLayout(new GridLayout(1,2,0,0));
		buyPanel.add(BorderLayout.EAST,mReg);
		mReg.setBounds(95,0,22,30);
		//}}

		//{{REGISTER_LISTENERS
		//}}
	    mReg.setHotelReg(this);
		reset();
	}

	//{{DECLARE_CONTROLS
	java.awt.Panel labelPanel = new java.awt.Panel();
	java.awt.Label mLabel = new java.awt.Label();
	java.awt.Panel buyPanel = new java.awt.Panel();
	java.awt.Label mLabel2 = new java.awt.Label();
	ackgames.acquire.Reg mReg = new ackgames.acquire.Reg();
	//}}
    int m_hotel;
    Wallet m_wallet;

	
	
	void  setText(String s){
	    mLabel.setText(s);
	    mLabel2.setText(s);
	}
	    

}