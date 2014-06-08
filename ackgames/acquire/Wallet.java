package ackgames.acquire;
import java.awt.*;

public class Wallet
{
    GameBoard m_gameBoard;
    int m_originalMoney;
    int m_money;
    HotelRegister m_hotels[]= new HotelRegister[7];
    Purchase m_purchase;
    public Wallet(GameBoard gb)
    {
        m_gameBoard= gb;
        m_purchase = new Purchase();
    }
    public AQC setAQCBuyHotel(AQCBuyHotel abh,String name )
    {
        String str = null;
        for(int i=0;i<7;i++){
            if(m_hotels[i].m_cnt>0){
                abh.setHotelPurchase(i, m_hotels[i].m_cnt);
                if(str == null){
                    str = m_hotels[i].m_cnt + " "+ m_gameBoard.getM_hot()[i].getName();
                }else{
                    str = str +", "+m_hotels[i].m_cnt + " "+ m_gameBoard.getM_hot()[i].getName();
                }
            }

        }
        if (str!=null){
            str = name +" buys "+str;
        }else {
            str = name +" buys no stock";
        }
        abh.setMessage(str);      
        return abh;
        
    }
    
    
    public void reset()
    {
        for(int i=0;i<7;i++){
            m_hotels[i].reset();
        }
        m_purchase.reset();
    }
    int m_total=0;
    public void calc(int h)
    {
        int cnt =0;
        m_total=0;
        for(int i=0;i<7;i++){
            cnt +=m_hotels[i].m_cnt;          
        }
        if(cnt ==4){
            for(int i=0;i<7;i++){
                if( m_hotels[i].m_cnt>0 && i!=h){
                    m_hotels[i].setRegValue(m_hotels[i].m_cnt-1);
                    break;
                }
            }
        }
        for(int i=0;i<7;i++){
            m_total +=m_hotels[i].getCost() * m_hotels[i].m_cnt;          
        }
        while(m_total> m_originalMoney){
            for(int i=0;i<7;i++){
                if( m_hotels[i].m_cnt>0 && i!=h){
                    m_hotels[i].setRegValue(m_hotels[i].m_cnt-1);
                    break;
                }
            }
            m_total=0;
            for(int i=0;i<7;i++){
                m_total +=m_hotels[i].getCost() * m_hotels[i].m_cnt;          
            }
            
        }
        m_purchase.setFrame(this);
        m_moneyLabel.setText("$"+Integer.toString(m_originalMoney- m_total));
        m_gameBoard.getM_statPanel().setStatsByWallet(this,m_gameBoard.getM_currentPlayer());
        
    }
   
    public Purchase getPurchase()
    {
        return m_purchase;
    }
    Label m_moneyLabel;
    void setMoneyLabel( Label ml)
    {
        m_moneyLabel = ml;
    }
    
    void setup()
    {
        m_originalMoney= m_gameBoard.getM_players()[m_gameBoard.getM_currentPlayer()].getMoney();
        for(int i=0;i<7;i++){
            m_hotels[i].setup();
        }
    }
    void incr(int h)
    {
        m_hotels[h].incr();
    }
}