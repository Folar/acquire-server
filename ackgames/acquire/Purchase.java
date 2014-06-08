package ackgames.acquire;

import java.awt.*;

public class Purchase extends java.awt.Panel
{
	public Purchase()
	{
	   
		setLayout(new BorderLayout(0,1));
		setBackground(new java.awt.Color(79,148,233));
		setForeground(java.awt.Color.white);
		setSize(249,119);
		title.setText("Stock Purchases");
		title.setAlignment(java.awt.Label.CENTER);
		add(BorderLayout.NORTH, title);
		title.setForeground(new java.awt.Color(79,148,233));
		title.setFont(new Font("Serif", Font.BOLD, 13));
		title.setBounds(0,0,0,0);
		panel1.setLayout(new GridLayout(5,2,0,0));
		add(BorderLayout.CENTER, panel1);
		panel1.setFont(new Font("Serif", Font.ITALIC, 12));
		panel1.setBounds(0,0,0,0);
		n1.setText("text");
		panel1.add(n1);
		n1.setForeground(new java.awt.Color(79,148,233));
		n1.setFont(new Font("Serif", Font.ITALIC, 12));
		n1.setBounds(0,0,0,0);
		h1.setText("text");
		panel1.add(h1);
		h1.setForeground(new java.awt.Color(79,148,233));
		h1.setFont(new Font("Serif", Font.PLAIN, 12));
		h1.setBounds(0,0,0,0);
		n2.setText("text");
		panel1.add(n2);
		n2.setForeground(new java.awt.Color(79,148,233));
		n2.setBounds(0,0,0,0);
		h2.setText("text");
		panel1.add(h2);
		h2.setForeground(new java.awt.Color(79,148,233));
		h2.setFont(new Font("Serif", Font.PLAIN, 12));
		h2.setBounds(0,0,0,0);
		n3.setText("text");
		panel1.add(n3);
		n3.setForeground(new java.awt.Color(79,148,233));
		n3.setFont(new Font("Serif", Font.ITALIC, 12));
		n3.setBounds(0,0,0,0);
		h3.setText("text");
		panel1.add(h3);
		h3.setForeground(new java.awt.Color(79,148,233));
		h3.setFont(new Font("Serif", Font.PLAIN, 12));
		h3.setBounds(0,0,0,0);
		totalab.setText("Total:");
		panel1.add(totalab);
		totalab.setForeground(new java.awt.Color(79,148,233));
		totalab.setFont(new Font("Serif", Font.BOLD, 12));
		totalab.setBounds(0,0,0,0);
		panel1.add(total);
		total.setFont(new Font("Serif", Font.BOLD, 12));
		total.setBounds(0,0,0,0);
		
		panel1.add(vvv);
		vvv.setBounds(0,0,0,0);
		panel1.add(label2);

		name[0]=n1;
		name[1]=n2;
		name[2]=n3;
		hotelab[0]=h1;
		hotelab[1]=h2;
		hotelab[2]=h3;
		title.setForeground(Color.white);
	    total.setForeground(Color.white);
	    totalab.setForeground(Color.white);
	    for(int i=0;i<3;i++){
	        hotelab[i].setForeground(Color.white);
	        name[i].setForeground(Color.white);
	    }
	    reset();
	    
	}

	void reset()
	{
	    title.setText("");
	    total.setText("");
	    totalab.setText("");
	    for(int i=0;i<3;i++){
	        hotelab[i].setText("");
	        name[i].setText("");
	    }
	    
	    
	}

    void setFrame(Wallet w)
    {
        String str;
        int cnt =0;
        for(int i=0;i<7;i++){
            if(w.m_hotels[i].m_cnt>0){
                cnt++;
            }
        }    
        int ind=0;
        for(int i=0;i<7;i++){
            if(w.m_hotels[i].m_cnt>0){
                name[ind].setText(Preference.m_hotelStr[i]);
                hotelab[ind].setText( Integer.toString(w.m_hotels[i].m_cnt));
                ind++;
            }
        }
        for(int i=ind;i<3;i++){
            name[i].setText("");
	        hotelab[i].setText("");
        }
	    total.setText("$"+Integer.toString(w.m_total));
	    title.setText("Stock Purchases");
	    totalab.setText("Total:");
    }
    java.awt.Label name[]= new Label[3];
    java.awt.Label hotelab[]= new Label[3];

	java.awt.Label title = new java.awt.Label();
	java.awt.Panel panel1 = new java.awt.Panel();
	java.awt.Label n1 = new java.awt.Label();
	java.awt.Label h1 = new java.awt.Label();
	java.awt.Label n2 = new java.awt.Label();
	java.awt.Label h2 = new java.awt.Label();
	java.awt.Label n3 = new java.awt.Label();
	java.awt.Label h3 = new java.awt.Label();
	java.awt.Label totalab = new java.awt.Label();
	java.awt.Label total = new java.awt.Label();
	java.awt.Label vvv = new java.awt.Label();
	java.awt.Label label2 = new java.awt.Label();
	java.awt.Label label4 = new java.awt.Label();


}