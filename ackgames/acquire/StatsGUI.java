package ackgames.acquire;
import java.awt.*;
public class StatsGUI
{
    public Label m_nameLabel;
    public Button m_tileButton[];
    public Label m_moneyLabel;
    public HotelRegister m_stockLabel[];
    public StatsGUI(Wallet w)
    {

        m_nameLabel  = new Label();
        m_nameLabel.setBackground(Color.white);
        m_tileButton  =  new Button[6];
        
        m_stockLabel  =  new HotelRegister[7];
        m_moneyLabel  = new Label();
        m_moneyLabel.setBackground(Color.white);
        w.setMoneyLabel(m_moneyLabel);
	    for (int i = 0; i<7;i++) {
		    m_stockLabel[i] = new HotelRegister(i,w); 
	    }
	    for (int i = 0; i<6;i++) {
		    m_tileButton[i] = new Button();
		    m_tileButton[i].setEnabled(false);
	    }
	   
    }

    public void reset()
    {
	for (int i = 0; i<7;i++) {
	    if (i<2)
		m_stockLabel[i].setText("0/25/$200");
	    else if (i<5)
		m_stockLabel[i].setText("0/25/$300");
	    else
		m_stockLabel[i].setText("0/25/$400");
	}
    }

    public Label getM_nameLabel() {
        return m_nameLabel;
    }

    public Button[] getM_tileButton() {
        return m_tileButton;
    }

    public Label getM_moneyLabel() {
        return m_moneyLabel;
    }

    public void setM_moneyLabel(Label m_moneyLabel) {
        this.m_moneyLabel = m_moneyLabel;
    }

    public HotelRegister[] getM_stockLabel() {
        return m_stockLabel;
    }

}
