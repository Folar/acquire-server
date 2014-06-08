package ackgames.acquire;
import java.awt.*;
public class Hotel
{
    public static final int LUXOR =0;
    public static final int TOWER = 1;
    public static final int AMERICAN = 2;
    public static final int WORLDWIDE = 3;
    public static final int FESTIVAL =4;
    public static final int CONTINENTAL =5;
    public static final int IMPERIAL  =6;
    int m_hotel;
    Button m_button;
    String m_name;
    int m_first;
    int m_second;
    int m_availShares = 25;
    GameBoard m_gameBoard;
    void setAvailShares( int a)
    {
	m_availShares = a ;
    }
    int getAvailShares()
    {
	    return m_availShares;
    }
    void set(int h, String name)
    {
    
        m_hotel =h;
        m_name = name;
    }
    Hotel ( GameBoard gb) {
          m_gameBoard = gb;
        m_name = "Tower";
    }
    Hotel(int h, String name,GameBoard gb)
    {
      m_gameBoard = gb;
        m_hotel =h;
        m_name = name;
    }
    int getHotel() { return m_hotel;}
    public String getName() { return m_name;}

    Button createButton()
    {
	m_button = new Button(m_name);
	m_button.setSize(25,25);
	m_button.setForeground(Color.red);
        m_button.setBackground(Color.yellow);
        m_button.repaint();
        return m_button;
    }
    void calcBonus()
    {
	m_first = price() * 10;
	m_second = price() * 5;
    }
    int secondBonus()
    {
	return m_second ;
    }
    int firstBonus ()
    {
	return m_first;
    }
    boolean isSafe()
    {
	if (count() > 10) return true;
	return false;
    }

    int count()
    {
        int cnt = 0;
        for (int i = 0; i <9; i++)
            for (int j = 0; j<12; j++)
	       if(m_gameBoard.getM_tile()[i][j].m_state == m_hotel) cnt++;
        return cnt;
    }
    int price ()
    {
	int cnt = count();
	if(cnt == 0) return 0;
        int base = 200;
        int price;
        switch (m_hotel) {
            case LUXOR:
            case TOWER:
                base = 200;
                break;
            case FESTIVAL:
            case WORLDWIDE:
            case AMERICAN:
                base = 300;
                break;
            case IMPERIAL:
            case CONTINENTAL:
                base = 400;
                break;
        }
        if (cnt < 6) {
            price = base + 100 * (cnt -2);
        } else if ( cnt < 11) {
            price = base + 400 ;
        } else if ( cnt < 21) {
            price = base + 500;
        } else if ( cnt < 31) {
            price = base + 600 ;
        } else if ( cnt < 41) {
            price = base + 700 ;
        } else {
            price =base + 800;
        }

        return price;
    }
}
