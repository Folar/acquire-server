package ackgames.acquire;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DistributionPanel extends Panel
{

   Label outstandingLabel= new Label("Outstanding:   ");
   Label chainSizeLabel= new Label("Chain size: ");
   Label priceLabel= new Label("Price of stock: ");
   Label outstandingCntLabel= new Label();
   Label priceAmtLabel= new Label();
   Label sizeLabel= new Label();
   Label playerLabel[]= new Label[6];
   Label playerCntLabel[]= new Label[6];
   Player m_players[]= new Player[6];
   Player m_players2[]= new Player[6];
   HotelTable m_hotelTable = new HotelTable();
   int m_playerNum = 0;
   GameBoard m_gameBoard;
   boolean m_bShow = false;
   int m_currentHotel = -1;
    void initPlayer2()
    {
        m_players2= new Player[6];
    }
    void pp(String s) {
	    //System.err.println( s );
    }

    public int viewing()
    {
	
	    return m_currentHotel;
    }
    public void add (int i, String n)
    {
	    if (i < m_playerNum) return;
	    playerCntLabel[m_playerNum]= new Label();
	    playerLabel[m_playerNum]= new Label();
	    m_players[m_playerNum++] = new Player(n);
    }

    public void updateTable(int av,int k)
    {
        m_hotelTable.updateTable( av,k);
    }
    
    public synchronized void setFrame(int h, int holdings[] )
    {
        
        if(m_gameBoard.getGameState() == m_gameBoard.BUYSTOCK){
            holdings[m_gameBoard.getM_currentPlayer()]=
                 m_gameBoard.getM_wallet().m_hotels[h].getShares();
        }else if(m_gameBoard.isM_swappingStock()){
            if(m_survivor == h){
                holdings[m_gameBoard.getM_currentPlayer()] = m_sown;
            }else if(m_defunct == h){
                holdings[m_gameBoard.getM_currentPlayer()] = m_down;
            }
                
        }
        
        
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

	    setTitleLabel(h);
	    for ( int i =0;i<m_playerNum;i++) {
	        playerLabel[i].setText(p[i].getName());
	        I1 = new Integer(p[i].m_hotels[h]);
	        playerCntLabel[i].setText( I1.toString());
	    }
	    Integer I2 = new Integer(m_gameBoard.getM_hot()[h].price());
	    Integer I3=new Integer(m_gameBoard.getM_hot()[h].getAvailShares());;
	    priceAmtLabel.setText( "$"+I2.toString());
	    if(m_gameBoard.getGameState() == m_gameBoard.BUYSTOCK){
            I3 = new Integer( m_gameBoard.getM_wallet().m_hotels[h].getAvailShares());
        }else if(m_gameBoard.isM_swappingStock()){
            if(m_survivor == m_currentHotel){
                I3 = new Integer(m_savail);
            }else if(m_defunct == m_currentHotel){
                I3 = new Integer(m_davail);
            }
        } 
	    outstandingCntLabel.setText( I3.toString());
	    I3 = new Integer(m_gameBoard.getM_hot()[h].count());
	    sizeLabel.setText( I3.toString());
	   
    }
    
    public Dimension getPreferredSize()
    {
        return new Dimension(258,75);
    }
    
    void setTitleLabel(int i)
    {
        m_distLabel.setLabel("Stock distribution for " + m_gameBoard.getM_hot()[i].getName());
        m_distLabel.setBackground(Preference.m_color[i]);
        //((CardLayout)getLayout()).show(this,"detail");
    }
    
    Button m_distLabel = new Button();
    Panel m_statPanel = null;
    Panel m_detailPanel = new Panel(new BorderLayout());
    DistributionPanel(GameBoard gb)
    {
	    m_gameBoard =gb;
	    setLayout(new CardLayout(0,0));
	    m_detailPanel.add("North",m_distLabel);
	    m_distLabel.addActionListener(new StockActionListener());
	    
    }
    int m_defunct=0;
    int m_survivor=0;
    int m_davail=0;
    int m_down=0;
    int m_savail=0;
    int m_sown=0;
    int m_avail=0;
    int m_own=0;
    boolean m_switchToDetailsDuringSwap=false;
    public void showDetail()
    {
        /*
        if(m_gameBoard.m_swappingStock){
            if(m_survivor == m_currentHotel){
                m_switchToDetailsDuringSwap=true;
                m_own = m_sown;
                m_avail=m_savail;
                 m_gameBoard.showStock(m_currentHotel);
            }else if(m_defunct == m_currentHotel){
                m_switchToDetailsDuringSwap=true;
                m_own = m_down;
                m_avail=m_davail;
                m_gameBoard.showStock(m_currentHotel);
            }
            
        }
        */
        m_switchToDetailsDuringSwap=false;
        ((CardLayout)getLayout()).show(this,"detail");
    }
    class StockActionListener implements ActionListener
   {
       public void actionPerformed(ActionEvent e)
       {  
	       ((CardLayout)getLayout()).show(DistributionPanel.this,"table");
       }	    
    }
    void resetDistributionPanel()
    {
        m_playerNum =0;
        //((CardLayout)getLayout()).show(DistributionPanel.this,"table");
    }
    
    void buildFrame(GameBoard gb)
    {
        
        m_gameBoard = gb;
        int x[]={0,0,0,0,0,0};
        String str;
        if(m_statPanel != null){
             m_detailPanel.remove(m_statPanel);
        }
        m_statPanel= new Panel();
	    m_statPanel.setLayout(new GridLayout(m_playerNum + 3,2,2,2));
	   
	    add("table", m_hotelTable);
	    add("detail", m_detailPanel);
	    ((CardLayout)getLayout()).show(this,"table");
	    m_detailPanel.add("Center",m_statPanel);
	    int i = 0;
	    for ( i =0;i<m_playerNum;i++) {
	        pp("buildframe " + "in loop");
	        str = m_gameBoard.getM_players()[i].getName();
		    playerLabel[i] = new Label(str);
		    playerCntLabel[i] = new Label();
		    m_statPanel.add(playerLabel[i]);
		    m_statPanel.add(playerCntLabel[i]);
	    }
	    
	    m_statPanel.add(chainSizeLabel);
	    m_statPanel.add(sizeLabel);
	    m_statPanel.add(priceLabel);
	    m_statPanel.add(priceAmtLabel);
	    m_statPanel.add(outstandingLabel);
	    m_statPanel.add(outstandingCntLabel);
	    setFrame(0,x);
	    this.validate();
    }
    
}
