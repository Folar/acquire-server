package ackgames.acquire;

import java.awt.*;

public class HotelTable extends java.awt.Panel
{
	public HotelTable()
	{

		setLayout(new GridLayout(8,4,0,0));
		Insets ins = getInsets();
		setSize(256,131);

		add(Hotel);
		Hotel.setBackground(java.awt.Color.lightGray);
		availability.setAlignment(Label.CENTER);
		add(availability);
        availability.setBackground(java.awt.Color.lightGray);
		size.setAlignment(Label.CENTER);
		add(size);
		size.setBackground(java.awt.Color.lightGray);
		price.setAlignment(Label.CENTER);
		add(price);
		price.setBackground(java.awt.Color.lightGray);
        for(int i=0;i<7;i++){
            h[i]=new Label(Preference.m_hotelStr[i]);
            add(h[i]);
            h[i].setBackground(Preference.m_color[i]);
            av[i]=new Label();
            add(av[i]);
            av[i].setBackground(new Color(241,233,220));
            si[i]=new Label();
            add(si[i]);
            si[i].setBackground(new Color(241,233,220));
            pi[i]=new Label();
            add(pi[i]);
            pi[i].setBackground(new Color(241,233,220));
        }

		for(int i=0;i<7;i++){
		    av[i].setAlignment(Label.CENTER);
		    pi[i].setAlignment(Label.CENTER);
		    si[i].setAlignment(Label.CENTER);
		    stripeIt(i);
		}
	}
	void stripeIt(int i)
	{
	    if (i%2 == 0){
            av[i].setBackground(Color.white);
            si[i].setBackground(Color.white);
            pi[i].setBackground(Color.white);
        }else{
            av[i].setBackground(AcquireStats.blue);
            si[i].setBackground(AcquireStats.blue);
            pi[i].setBackground(AcquireStats.blue);
        }
	}
	
    void updateTable(int a,int k)
    {
        av[k].setText(Integer.toString(a));
    }
    void updateTable(HotelRegister h,int k)
    {
        
        av[k].setText(Integer.toString(h.getAvailShares()));
       
    }
    void updateTable(GameBoard gb)
    {
	    for(int k= 0;k<7;k++){
	        if (gb.getM_hot()[k].count()>10){
	            si[k].setBackground(Color.orange);
                pi[k].setBackground(Color.orange);
                av[k].setBackground(Color.orange);
	        }else {
	            stripeIt(k);
	        }
            si[k].setText(Integer.toString(gb.getM_hot()[k].count()));
            pi[k].setText(Integer.toString(gb.getM_hot()[k].price()));
            av[k].setText(Integer.toString(gb.getM_hot()[k].getAvailShares()));
        }
    }
	java.awt.Label Hotel = new java.awt.Label("Hotel");
	java.awt.Label availability = new java.awt.Label("Available");
	java.awt.Label size = new java.awt.Label("Size");
	java.awt.Label price = new java.awt.Label("Price");
    java.awt.Label av[] = new java.awt.Label[7];
    java.awt.Label si[] = new java.awt.Label[7];
    java.awt.Label pi[] = new java.awt.Label[7];
    java.awt.Label h[] = new java.awt.Label[7];

}
