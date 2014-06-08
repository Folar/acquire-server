package ackgames.acquire;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

class StockFrame extends Frame
{

   Label outstandingLabel= new Label("Outstanding stock: ");
   Label chainSizeLabel= new Label("Chain size: ");
   Label priceLabel= new Label("Price of stock: ");
   Label outstandingCntLabel= new Label();
   Label priceAmtLabel= new Label();
   Label sizeLabel= new Label();
    Label playerLabel[]= new Label[6];
   Label playerCntLabel[]= new Label[6];
   Player m_players[]= new Player[6];
   int m_playerNum = 0;
   GameBoard m_gameBoard;
   boolean m_bShow = false;
   int m_currentHotel = -1;

    void pp(String s) {
	    //System.err.println( s );
    }

    int viewing()
    {
	if (!m_bShow)
	{
	    return -1;
	}
	return m_currentHotel;
    }
    void add (int i, String n)
    {
	    Integer I1	= new Integer(m_playerNum);
	    Integer I2	= new Integer(i);
	    pp("add start : "+n +" i= " +I2.toString()+ " pnum = "+ I1.toString());
	    if (i < m_playerNum) return;
	    playerCntLabel[m_playerNum]= new Label();
	    playerLabel[m_playerNum]= new Label();
	    m_players[m_playerNum++] = new Player(n);
	    I1	= new Integer(m_playerNum);
	    pp("add end :buildframe " + I1.toString());
    }

    synchronized void setFrame(int h, int holdings[] )
    {
	m_bShow = true;
	m_currentHotel = h;

	pp("in set frame");
	Player p[] = new Player[6];
	for ( int i =0;i<m_playerNum;i++) {
	    pp("in set frame:first loop");
	    p[i]= m_players[i];
	}
	for ( int i =0;i<m_playerNum;i++) {
	    pp("in set frame:second loop");
	     m_players[i].m_hotels[h] = holdings[i];
	}
	Player temp;
	Integer I1  = new Integer(m_playerNum);
	for (int i = 0; i <m_playerNum - 1;i++) {
	    for ( int j = i+1; j< m_playerNum ;j++) {
		    if ( p[i].m_hotels[h] <
			 p[j].m_hotels[h] ) {
			temp = p[i];
			p[i] = p[j];
			p[j] = temp;
		    }
	    }
	}

	    pp("in set frame:after sort");

	setTitle("Stock distribution for " + m_gameBoard.getM_hot()[h].getName());
	for ( int i =0;i<m_playerNum;i++) {
	    playerLabel[i].setText(p[i].getName());
	    I1 = new Integer(p[i].m_hotels[h]);
	    playerCntLabel[i].setText( I1.toString());
	}
	Integer I2 = new Integer(m_gameBoard.getM_hot()[h].price());
	priceAmtLabel.setText( "$"+I2.toString());
	Integer I3 = new Integer(m_gameBoard.getM_hot()[h].getAvailShares());
	outstandingCntLabel.setText( I3.toString());
	I3 = new Integer(m_gameBoard.getM_hot()[h].count());
	sizeLabel.setText( I3.toString());
    }

    public void start()
    {
	setVisible(true);
    }

    StockFrame(GameBoard gb)
    {
	    super("stats");
	    setSize(275,200);
	    setLocation(600, 50);
	    m_gameBoard =gb;
    		
		
	    //setSize(getInsets().left + getInsets().right + 430,
		//	 getInsets().top + getInsets().bottom + 270);
	    setTitle("stock portfolio");
	    this.addWindowListener( new My2WindowListener());
		
		//{{INIT_MENUS
		//}}
}
    void buildFrame()
    {
	    Integer I1	= new Integer(m_playerNum);
	    pp("buildframe " + I1.toString());
	    setLayout(new GridLayout(m_playerNum + 3,2,2,2));
	    int i = 0;
	    for ( i =0;i<m_playerNum;i++) {
	    pp("buildframe " + "in loop");
		playerLabel[i] = new Label();
		playerCntLabel[i] = new Label();
		add(playerLabel[i]);
		add(playerCntLabel[i]);
	    }
	    //add(ph1);
	    //add(ph2);
	    add(chainSizeLabel);
	    add(sizeLabel);
	    add(priceLabel);
	    add(priceAmtLabel);
	    add(outstandingLabel);
	    add(outstandingCntLabel);
    }
    /*************************************************
    public boolean handleEvent( Event e)
    {
	if (e.id == Event.WINDOW_DESTROY) {
	    m_bShow = false;
	    setVisible(false);
	    return true;
	} else
	    return super.handleEvent(e);
    }
    ************************************/



    class My2WindowListener implements WindowListener
    {
	public	void windowActivated(WindowEvent e)
	{
	}
	public	void windowClosed(WindowEvent e)
	{
	}

	public  void windowClosing(WindowEvent e)
	{
	    m_bShow = false;
	    setVisible(false);
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


	//{{DECLARE_CONTROLS
	//}}
	//{{DECLARE_MENUS
	//}}
}
