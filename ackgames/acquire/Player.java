package ackgames.acquire;
import java.io.DataOutputStream;


public class Player
{
    String m_name;
    GameBoard m_gameBoard;
    int m_money;
    int m_hotels[] = new int[7];
    int m_tileCount = 6;
    public Tile m_tiles[] = new Tile[6];
    DataOutputStream  fOutputStream = null;
    int m_state=6;
    private int m_oldState=6;
    public int m_id;
    String m_tileStr = null;
    boolean m_lostConnection = false;
    String msgStr ="";
     String chatMsgStr ="";
    private String oldMsgStr ="";
    private String oldChatMsgStr ="";
    private boolean redrawRack=false;
    public String getMsgStr()
    {

        return msgStr;
    }
    public void setMsgStr(String m)
    {
        if(msgStr.length()==0){
            msgStr=m;
        }else{
            msgStr= msgStr+"#"+m;
        }
    }
    public void resetMsgStr()
    {
        msgStr ="";
        oldMsgStr="";
    }

    public String getChatMsgStr()
    {

        return chatMsgStr;
    }
    public void setChatMsgStr(String m)
    {
        //System.out.println("inchatm  m = " + m);
        if(chatMsgStr.length()==0){
            chatMsgStr=m;
        }else{
            chatMsgStr= chatMsgStr+"#"+m;
        }
        System.out.println("in setChatMsgStr aaa chatMsgStr = " + chatMsgStr);
    }
    public void resetChatMsgStr()
    {
        chatMsgStr ="";
        setOldChatMsgStr("");
    }


    public void setLostConnection( boolean lc) { m_lostConnection = lc;}
    public boolean isLostConnection( ) { return m_lostConnection; }
    public void setState(int s) {
        m_state = s;
    }
    public int getState() {
        return m_state;
    }
    public void setId(int id) { m_id = id;}
    public int getId() { return m_id;}
    public Player(String name,int n, GameBoard gb)
    {
        m_name = name;
         m_gameBoard = gb;
        m_id= n;
        if(Preference.VERSION.equals("CA")){
            m_money = 8000;
        }else{
            m_money = 6000;
        }
        
    }
    int mergeNum=0;
    int mergeList[];
    
    public boolean isDHTMLClient(){
        return fOutputStream==null;
    }
    public int[] getMergeList()
    {
        return mergeList;
    }
    public void setMergeList(int x[])
    {
        mergeList= x;
    }
    public int getMergeNum()
    {
        return mergeNum;
    }

    public void setMerge(int x,int y[])
    {
        mergeNum= x;
        mergeList=y;
    }
   
    public Player(String name)
    {
        m_name = name;
    }
    public Player(String name,DataOutputStream  oStream, GameBoard gb,int id )
    {
	    m_id = id;
	    fOutputStream = oStream;
	    m_gameBoard = gb;
        m_name = name;
	    if(Preference.VERSION.equals("CA")){
            m_money = 8000;
        }else{
            m_money = 6000;
        }
        for ( int i = 0; i<6;i++) {
	        m_tiles[i] = Tile.m_dummyTile;
        }
    }
    public void pickFirstTiles()
    {
       for ( int i = 0; i<6;i++) {
	        m_tiles[i] = m_gameBoard.pickATile();
       }
    }

    public void sendmsg( AQC msg )
    {
        if(isDHTMLClient()){
            return;
        }
	    synchronized(fOutputStream)
	    {
	        try {
                 msg.writeAQC(fOutputStream);
            }catch (Exception e){
            }
        }
    }
    void pp(String s) {
	System.err.println( s );
    }
    public int worth() {
	    int amt = 0;
	    for ( int i = 0; i<7;i++) {
	        if (m_hotels[i] != 0) {
		        amt = m_hotels[i] * m_gameBoard.getM_hot()[i].price();
		        m_money += amt;
	        }
        }
        return m_money;
    }
    public String getName() { return m_name; }
    public int getMoney() { return m_money; }
    public void setMoney(int m) { m_money = m; }

    public boolean bonusShare(Hotel h)
    {
	    int av = h.getAvailShares();
	    if (av == 0) return false;
	    h.setAvailShares( av - 1);
	    m_hotels[h.m_hotel]++;
        return true;
    }

    public void swapStock( Hotel h, Hotel d, int swap, int sell)
    {
	    int av = h.getAvailShares();
	    h.setAvailShares( av - swap/2);
	    m_hotels[h.m_hotel] += swap/2;

	    av = d.getAvailShares();
	    d.setAvailShares( av + swap);
	    m_hotels[d.m_hotel] -=	swap;

	    av = d.getAvailShares();
	    d.setAvailShares( av + sell);
	    m_hotels[d.m_hotel] -=	sell;
	    m_money += sell * d.firstBonus()/10;
    }

    public boolean purchaseStock(Hotel h)
    {
	    int av = h.getAvailShares();
	    if (av == 0) return false;
	    h.setAvailShares( av - 1);
	    m_money = m_money - h.price();
	    m_hotels[h.m_hotel]++;
	    return true;
    }
    public String colorTiles()
    {
        StringBuffer c= new StringBuffer("RRRRRR");
        String str="";
        int j=0;
        for (int i = 0; i<6; i++) {
            if(m_tiles[i] == Tile.m_dummyTile){
                continue;
            }
            
            AboutNeighbors an =
		            m_gameBoard.surroundingTiles(m_tiles[i]);     
		    switch(an.getType())
		    {
		        case Tile.GROW:
		            String s = "" +an.getGrower();
		            c.setCharAt(j, s.charAt(0));
		            break;
		        case Tile.START:
		            c.setCharAt(j,'s');
		            break;
		            case Tile.DEAD:
		            c.setCharAt(j,'d');
		            break;
		        case Tile.NONPLAYBLE:
		           c.setCharAt(j,'n');
		            break;    
		        case Tile.MERGE:
		            c.setCharAt(j,'m');
		            break;  
		       
		    }
		    j++;
		}
		for(int i=0;i<j-1;i++){
		    if(c.charAt(i)!='R'){
		        continue;
		    }
		    for(int k=i+1;k<j;k++){
		        if(c.charAt(k)!='R'){
		            continue;
		        }    
		        if(formChain(i,k)){
		             c.setCharAt(i,'w');
		             c.setCharAt(k,'w');
		        }
		    }
		}
		return c.toString();
    }	    
    
    public boolean formChain(int i,int j)
    {  
	    Tile t1 = m_tiles[i];
	    Tile t2 = m_tiles[j];
	    if (t1.getRow() == t2.getRow()){
	        if ( t1.getColumn() == (t2.getColumn()+1)  ||
	             t1.getColumn() == (t2.getColumn()-1)){
	            return true;
	            
	        }
	        return false;
	    }
	    if (t1.getColumn() == t2.getColumn()){
	        if ( t1.getRow() == (t2.getRow()+1)  ||
	             t1.getRow() == (t2.getRow()-1)){
	            return true;
	            
	        }
	        return false;
	    }
	    return false;
    }
    public String getRack()
    {
        String str=colorTiles();
        for (int i = 0; i<6; i++) {
            if(m_tiles[i] != Tile.m_dummyTile){
                str = str + " "+m_tiles[i].getLabel();
            }
	        
        }
        return str;
        
    }
    public void fillInStats(StatsGUI s)
    {
	    Integer I1 = new Integer(m_money);
	    String str;
        s.getM_nameLabel().setText(m_name + ":");
        for (int i = 0; i<6; i++) {
	        s.getM_tileButton()[i].setLabel(m_tiles[i].getLabel());
        }
    	
	    for (int i = 0; i<7; i++) {
	        Integer stockOwn = new Integer(m_hotels[i]);
	        Integer stockAvail = new Integer(m_gameBoard.getM_hot()[i].getAvailShares());
	        Integer price = new Integer(m_gameBoard.getM_hot()[i].price());
	        str = stockOwn.toString() + "/" + stockAvail.toString()  +"/$"+
		    price.toString();
	        s.getM_stockLabel()[i].setText(str);
        }
	    s.getM_moneyLabel().setText( "$"+I1.toString());
    }

    public Tile[] getM_tiles() {
        return m_tiles;
    }

    public void setM_tiles(Tile[] m_tiles) {
        this.m_tiles = m_tiles;
    }

    public int getM_id() {
        return m_id;
    }

    public void setM_id(int m_id) {
        this.m_id = m_id;
    }

    public String getOldMsgStr() {
        return oldMsgStr;
    }

    public void setOldMsgStr(String oldMsgStr) {
        this.oldMsgStr = oldMsgStr;
    }

    public String getOldChatMsgStr() {
        return oldChatMsgStr;
    }

    public void setOldChatMsgStr(String oldChatMsgStr) {
        this.oldChatMsgStr = oldChatMsgStr;
    }

    public int getOldState() {
        return m_oldState;
    }

    public void setOldState(int m_oldState) {
        this.m_oldState = m_oldState;
    }

    public boolean isRedrawRack() {
        return redrawRack;
    }

    public void setRedrawRack(boolean redrawRack) {
        this.redrawRack = redrawRack;
    }
}
