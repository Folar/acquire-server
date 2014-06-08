package ackgames.acquire;

import java.awt.*;

public class SwapFrame2 extends java.awt.Frame
{
    int m_defunct;
    int m_survivor;
    String m_bonus;
    String m_title;
    GameBoard m_gameBoard;
    Player m_player;

	public SwapFrame2(GameBoard gb)
	{
	     m_gameBoard = gb;
	    setTitle("swap stock");
		//{{INIT_CONTROLS
		setLayout(null);
		setBackground(new java.awt.Color(120,173,237));
		setSize(530,214);
		setVisible(false);
		l1.setText("t2");
		add(l1);
		l1.setBounds(24,60,504,20);
		l2.setText("t2");
		add(l2);
		l2.setBounds(24,84,504,20);
		l3.setText("t2");
		add(l3);
		l3.setBounds(24,108,504,20);
		label1.setText("Keep");
		add(label1);
		label1.setBounds(96,144,36,24);
		label2.setText("Swap");
		add(label2);
		label2.setBounds(204,144,36,24);
		add(swapChoice);
		swapChoice.setBounds(180,168,72,21);
		label3.setText("Sell");
		add(label3);
		label3.setBounds(312,144,36,24);
		add(sellChoice);
		sellChoice.setBounds(288,168,72,21);
		m_ok.setLabel("OK");
		add(m_ok);
		m_ok.setBackground(java.awt.Color.lightGray);
		m_ok.setBounds(420,144,60,24);
		m_sellAll.setLabel("Sell All");
		add(m_sellAll);
		m_sellAll.setBackground(java.awt.Color.lightGray);
		m_sellAll.setBounds(420,180,60,24);
		add(keepChoice);
		keepChoice.setBackground(java.awt.Color.white);
		keepChoice.setBounds(72,168,72,21);
		panel1.setLayout(new GridLayout(1,4,0,0));
		add(panel1);
		panel1.setBounds(55,16,420,24);
		panel1.add(g1);
		g1.setBounds(0,0,0,0);
		g2.setAlignment(java.awt.Label.CENTER);
		panel1.add(g2);
		g2.setFont(new Font("SansSerif", Font.BOLD, 14));
		g2.setBounds(0,0,0,0);
		panel1.add(g3);
		g3.setBounds(0,0,0,0);
		g4.setAlignment(java.awt.Label.CENTER);
		panel1.add(g4);
		g4.setFont(new Font("SansSerif", Font.BOLD, 14));
		g4.setBounds(0,0,0,0);
		setTitle("A Simple Frame");
		//}}

		//{{REGISTER_LISTENERS
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		SymAction lSymAction = new SymAction();
		m_ok.addActionListener(lSymAction);
		m_sellAll.addActionListener(lSymAction);
		SymItem lSymItem = new SymItem();
		swapChoice.addItemListener(lSymItem);
		sellChoice.addItemListener(lSymItem);
		//}}

		//{{INIT_MENUS
		//}}

	}

	
	public void setVisible(boolean b)
	{
		if(b)
		{
			setLocation(10, 150);
		}
	super.setVisible(b);
	}

	public void  setPlayer(Player x)
    {
        m_player = x;
    }

    int m_defunctPrice=0;
    int m_keepNumber = 0;
	int m_swapNumber =0;
	int m_sellNumber=0;
    void setFrame(StockTransaction trade )
    {
	    m_ok.requestFocus();
	    swapChoice.removeAll();
	    sellChoice.removeAll();
	    setTitle(trade.getTitle());
	    m_title = trade.getTitle();
	    m_bonus = trade.getBonusStr();
	    int cur = 0;
	    m_survivor = trade.getSurvivor();
	    m_defunct = trade.getDefunct();
	    l1.setText(m_bonus);
	   
	    g2.setText(m_gameBoard.getM_hot()[m_survivor].getName());
	    g4.setText(m_gameBoard.getM_hot()[m_defunct].getName());
	    g4.setBackground(Preference.m_color[m_defunct]);
	    g3.setBackground(Preference.m_color[m_survivor]);
	    g2.setBackground(Preference.m_color[m_survivor]);
	    g1.setBackground(Preference.m_color[m_survivor]);
	    keepChoice.setEditable(false);
	    swapChoice.setBackground(Preference.m_color[m_survivor]);
	    keepChoice.setBackground(Preference.m_color[m_defunct]);
	    Integer I1 = new Integer(m_gameBoard.getM_hot()[m_survivor].price());
	    Integer I2 = new Integer(m_gameBoard.getM_hot()[m_defunct].firstBonus()/10);
	    Integer A1 = new Integer(m_gameBoard.getM_hot()[m_survivor].getAvailShares());
	    Integer A2 = new Integer(m_player.m_hotels[m_defunct]);
	    int cntSurvivor = m_gameBoard.getM_hot()[m_survivor].getAvailShares();
	    int cntDefunct = m_gameBoard.getM_hot()[m_defunct].getAvailShares();
	    String stockWorth = "a share of " +
				m_gameBoard.getM_hot()[m_survivor].getName() +
				"(survivor) is now worth " + I1.toString()+
				". There are now " + A1.toString() +
				" shares available. ";
	    l2.setText(stockWorth);
	    m_defunctPrice = I2.intValue();
	    stockWorth = "a share of " +
			   m_gameBoard.getM_hot()[m_defunct].getName() +
				"(defunct) was worth " + I2.toString() +
				". " + m_player.getName() +
				" you have " + A2.toString() + " shares";
	    l3.setText(stockWorth);
	    
	    int cnt = m_player.m_hotels[m_defunct];
	    m_keepNumber = cnt;
	    m_swapNumber =0;
	    m_sellNumber=0;
	    keepChoice.setText(Integer.toString(cnt));
	    int n = cnt;
	    if (cnt/2 > cntSurvivor)
	    {
		    n = 2* cntSurvivor;
	    }
	    for (int i = 0; i<= n; i++){
		    I1 = new Integer(i);
		    if (i%2 == 0)
		        swapChoice.addItem(I1.toString());
	    }
	    for (int i = 0; i<=cnt; i++){
	        I1 = new Integer(i);
		    sellChoice.addItem(I1.toString());
	    }
	}
	public void addNotify()
	{
		// Record the size of the window prior to calling parents addNotify.
		Dimension d = getSize();

		super.addNotify();

		if (fComponentsAdjusted)
			return;

		// Adjust components according to the insets
		Insets ins = getInsets();
		setSize(ins.left + ins.right + d.width, ins.top + ins.bottom + d.height);
		Component components[] = getComponents();
		for (int i = 0; i < components.length; i++)
			{
			Point p = components[i].getLocation();
			p.translate(ins.left, ins.top);
			components[i].setLocation(p);
		}
		fComponentsAdjusted = true;
	}

	// Used for addNotify check.
	boolean fComponentsAdjusted = false;

	class SymWindow extends java.awt.event.WindowAdapter
	{
		public void windowClosing(java.awt.event.WindowEvent event)
		{
		Object object = event.getSource();
		if (object == SwapFrame2.this)
			SwapFrame2_WindowClosing(event);
		}
	}

	void SwapFrame2_WindowClosing(java.awt.event.WindowEvent event)
	{
		mOk_ActionPerformed(null);
	}
	//{{DECLARE_CONTROLS
	java.awt.Label l1 = new java.awt.Label();
	java.awt.Label l2 = new java.awt.Label();
	java.awt.Label l3 = new java.awt.Label();
	java.awt.Label label1 = new java.awt.Label();
	java.awt.Label label2 = new java.awt.Label();
	java.awt.Choice swapChoice = new java.awt.Choice();
	java.awt.Label label3 = new java.awt.Label();
	java.awt.Choice sellChoice = new java.awt.Choice();
	java.awt.Button m_ok = new java.awt.Button();
	java.awt.Button m_sellAll = new java.awt.Button();
	java.awt.TextField keepChoice = new java.awt.TextField();
	java.awt.Panel panel1 = new java.awt.Panel();
	java.awt.Label g1 = new java.awt.Label();
	java.awt.Label g2 = new java.awt.Label();
	java.awt.Label g3 = new java.awt.Label();
	java.awt.Label g4 = new java.awt.Label();
	//}}

	//{{DECLARE_MENUS
	//}}


	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == m_ok)
				mOk_ActionPerformed(event);
			else if (object == m_sellAll)
				mSellAll_ActionPerformed(event);
		}
	}

	void mOk_ActionPerformed(java.awt.event.ActionEvent event)
	{
	    System.out.println("in swwapframe2 ok");
		Integer Keep = new Integer(keepChoice.getText()  );
	    int keep = Keep.intValue();
	    Integer Swap = new Integer(swapChoice.getSelectedItem());
	    int swap = Swap.intValue();
	    Integer Sell = new Integer(sellChoice.getSelectedItem());
	    int sell = Sell.intValue();
	    if (m_player.m_hotels[m_defunct] ==( sell +swap+keep)){
		    m_gameBoard.swapper(m_player.getM_id(),m_survivor,m_defunct,swap,sell);
		    setVisible(false);
            System.out.println("out swwapframe2 hide");
	    }
	     System.out.println("out swwapframe2 ok");
			 
	}

	void mSellAll_ActionPerformed(java.awt.event.ActionEvent event)
	{
		int cnt = m_player.m_hotels[m_defunct];
		m_gameBoard.swapper(m_player.getM_id(),m_survivor,m_defunct,0,cnt);
		setVisible(false);
			 
	}

	class SymItem implements java.awt.event.ItemListener
	{
		public void itemStateChanged(java.awt.event.ItemEvent event)
		{
			Object object = event.getSource();
			if (object == swapChoice)
				swapChoice_ItemStateChanged(event);
			else if (object == sellChoice)
				sellChoice_ItemStateChanged(event);
		}
	}

	void swapChoice_ItemStateChanged(java.awt.event.ItemEvent event)
	{
		Integer Swap = new Integer(swapChoice.getSelectedItem());
	    int swap = Swap.intValue();
	    Integer Sell = new Integer(sellChoice.getSelectedItem());
	    int sell = Sell.intValue();
		Integer Keep = new Integer(keepChoice.getText()  );
	    int keep = Keep.intValue();
	    if (swap > m_swapNumber){
	        int need =  swap - m_swapNumber; 
	        if (keep >= swap){
	            m_keepNumber = keep - need;
	            m_swapNumber = swap;
	            keepChoice.setText(Integer.toString(m_keepNumber));
	        } else {
	            sell = sell - (need - keep);
	            m_sellNumber = sell;
	            m_keepNumber = 0;
	            keepChoice.setText("0");
	            m_swapNumber = swap;
	            sellChoice.select(Integer.toString(m_sellNumber));
	        }
	    }else if (swap < m_swapNumber){
	        m_keepNumber += (m_swapNumber - swap);
	        keepChoice.setText(Integer.toString(m_keepNumber));
	        m_swapNumber = swap;
	    }
	    m_gameBoard.setStatsDuringSwap(m_survivor,m_defunct,m_keepNumber,
	                                   m_swapNumber,m_sellNumber, m_defunctPrice);
			 
	}

	void sellChoice_ItemStateChanged(java.awt.event.ItemEvent event)
	{
		Integer Swap = new Integer(swapChoice.getSelectedItem());
	    int swap = Swap.intValue();
	    Integer Sell = new Integer(sellChoice.getSelectedItem());
	    int sell = Sell.intValue();
		Integer Keep = new Integer(keepChoice.getText()  );
	    int keep = Keep.intValue();
	    if (sell > m_sellNumber){
	        int need =  sell - m_sellNumber; 
	        if (keep >= sell){
	            m_keepNumber = keep - need;
	            m_sellNumber = sell;
	            keepChoice.setText(Integer.toString(m_keepNumber));
	        } else {
	            swap = swap - (need - keep);
	            if (swap%2 == 1){
	                m_keepNumber = 1;
	                swap--;
	            } else {
	                m_keepNumber = 0;
	            }
	            m_swapNumber = swap;
	            
	            keepChoice.setText(Integer.toString(m_keepNumber));
	            m_sellNumber = sell;
	            swapChoice.select(Integer.toString(m_swapNumber));
	        }
	    }else if (sell < m_sellNumber){
	        m_keepNumber += (m_sellNumber - sell);
	        keepChoice.setText(Integer.toString(m_keepNumber));
	        m_sellNumber = sell;
	    }
	    
	    m_gameBoard.setStatsDuringSwap(m_survivor,m_defunct,m_keepNumber,
	                                   m_swapNumber,m_sellNumber,m_defunctPrice);
			 
			 
	}
}