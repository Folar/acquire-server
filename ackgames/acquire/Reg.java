package ackgames.acquire;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
public class Reg extends java.awt.Panel
{
    int m_max;
	
	public Reg()
	{

		//{{INIT_CONTROLS
		setLayout(new GridLayout(1,2,0,0));
		setSize(430,270);
		m_combo.setText("0");
		add(m_combo);
		m_combo.setBackground(java.awt.Color.white);
		m_combo.setBounds(0,0,0,0);
		mScrollBar.setBlockIncrement(1);
		add(mScrollBar);
		mScrollBar.setBounds(0,0,0,0);
		//}}

		//{{REGISTER_LISTENERS
		//SymAdjustment lSymAdjustment = new SymAdjustment();
		//mScrollBar.addAdjustmentListener(lSymAdjustment);
		//}}
	
		mScrollBar.addMouseListener(new MouseListener() {
		    public void mousePressed(MouseEvent e)
		    {
		        if (e.getY()>10){
		           if(m_value >0) m_value--;
		        } else {
		            if(m_value< m_max) m_value++;
		        }
		         m_hotelRegister.setValue(m_value);
		         m_combo.setText(Integer.toString(m_hotelRegister.m_cnt));
		    }
		    public void mouseClicked(MouseEvent e)
		    {
		      
		    }
		    public void mouseReleased(MouseEvent e)
		    {
		    }
		    public void mouseEntered(MouseEvent e)
		    {
		    }
		    public void mouseExited(MouseEvent e)
		    {
		       
		    }
		    
		    });
	}

	//{{DECLARE_CONTROLS
	java.awt.Label m_combo = new java.awt.Label();
	java.awt.Scrollbar mScrollBar = new java.awt.Scrollbar(Scrollbar.VERTICAL,0,1,0,4);
	//}}

    int m_value=0;
    
    public Dimension getPreferredSize()
    {
        return new Dimension(22,20);
    }
    
    HotelRegister m_hotelRegister;
    void setMinimum(int x){
        m_value=x;
    }
    void setMaximum(int x){
        m_max =x;
    }
    
    void setRegValue(int cnt)
    {
         m_combo.setText(Integer.toString(cnt));
         m_value=cnt;
    }
    
    
    void setsRegText(String s)
    {
         m_combo.setText(s);
        
    }
    void setHotelReg(HotelRegister h)
    {
        m_hotelRegister=h;
    }
	class SymAdjustment implements java.awt.event.AdjustmentListener
	{
		public void adjustmentValueChanged(java.awt.event.AdjustmentEvent event)
		{
		    //System.err.println("JJJJJJJJJJJ "+ event.getValue());
			Object object = event.getSource();
			if (object == mScrollBar)
				mScrollBar_AdjustmentValueChanged(event);
		}
	}

	void mScrollBar_AdjustmentValueChanged(java.awt.event.AdjustmentEvent event)
	{	    
		m_hotelRegister.setValue( m_hotelRegister.m_max- event.getValue());
		m_combo.setText(Integer.toString(m_hotelRegister.m_cnt));
	}

	
}