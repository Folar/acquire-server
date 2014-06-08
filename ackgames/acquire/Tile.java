package ackgames.acquire;
import java.io.Serializable;
public class Tile implements Serializable
{

    public static final int EMPTY = 8;
    public static final int ONBOARD = 9;
    public static final int OUTOFBOUNDRY = 10;
    public static final int START = 11;
    public static final int GROW = 12;
    public static final int MERGE = 13;
    public static final int NONPLAYBLE = 14;
    public static final int DEAD = 15;
    
    int m_state = EMPTY;
    int m_row =-1;
    public boolean m_dirty;
    public boolean m_mouseOver;
    private boolean m_inRack;
    boolean m_mergeTile;
    static Tile m_dummyTile;
    public static void setDummy( Tile t) { m_dummyTile = t;}
    int m_column =-1;
    public int getRow() { return  m_row;}

    public int getColumn() { return  m_column;}

    public int getState() { return m_state;}
    public void setState( int s)
    {
        m_state = s;
        m_dirty = true;
    }
    public Tile()
    {
        m_dirty = false;
        m_mergeTile= false;
        m_mouseOver= false;
    }
    public Tile(int row, int col)
    {
        this();
        m_row = row;
	    m_column = col;
    }
    public Tile(String s)
    {
        this();
        try {
            if(s.length()==3){
                m_column = Integer.parseInt(s.substring(0, 1))-1;
	            m_row = (int)s.charAt(2)- (int)'A';
	        }else {
	            m_column = Integer.parseInt(s.substring(0, 2))-1;
	            m_row = (int)s.charAt(3)- (int)'A';
	        }
	    }catch(Exception e){
	    }
	    //System.out.println("row = "+m_row+" col="+m_column+ " s= "+s);
    }
    
    public String getLabel()
    {
	    if (this == m_dummyTile) return "";
            String str[] = {"A","B","C","D","E","F","G","H","I"} ;
	    Integer I1;
	    int row = getRow();
	    int col  = getColumn();
	    I1 = new Integer(col + 1);
	    String tileLabel = I1.toString() + "-" + str[row];
	    return tileLabel;
    }
    public void setTile(int row, int col)
    {
        m_row = row;
	    m_column = col;
    }
    
    public int compare(Tile t)
    {
        if (t.getColumn() < getColumn()) {
	        return 1;
	        
	    } else if (t.getColumn() == getColumn()) {
	        if (t.getRow() < getRow()) {
	            return 1;    
	        }
	    }
	    return -1;
    }
    public boolean isEmpty()
    {
        if (m_row == -1){
            return true;
        }
        return false;
    }

    public boolean isM_dirty() {
        return m_dirty;
    }

    public void setM_dirty(boolean m_dirty) {
        this.m_dirty = m_dirty;
    }

    public boolean isM_mouseOver() {
        return m_mouseOver;
    }

    public void setM_mouseOver(boolean m_mouseOver) {
        this.m_mouseOver = m_mouseOver;
    }

    public boolean isM_inRack() {
        return m_inRack;
    }

    public void setM_inRack(boolean m_inRack) {
        if(m_inRack) {
            if(!this.m_inRack){
                System.out.println("Tile.setM_inRack dirty");
                m_dirty=true;
            }
        }else{
             if(this.m_inRack){
                m_dirty=true;
            }
        }
        this.m_inRack = m_inRack;
    }
}
