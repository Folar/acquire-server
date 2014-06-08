package ackgames.acquire;

import java.awt.*;

public class OrderFrame2 extends java.awt.Frame
{
    GameBoard m_gameBoard;
    int mergeNum ;
    Tile order[] = new Tile[4];

    
	public OrderFrame2(GameBoard gb)
	{
	    m_gameBoard=gb;
		//{{INIT_CONTROLS
		setLayout(null);
		setBackground(new java.awt.Color(120,173,237));
		setSize(543,128);
		setVisible(false);
		l1.setText("Survivor");
		add(l1);
		l1.setBounds(48,24,60,24);
		c1.setBackground(java.awt.Color.white);
		add(c1);
		c1.setBounds(24,48,100,21);
		label1.setText("Defunct 1");
		add(label1);
		label1.setBounds(180,24,60,24);
		add(c2);
		c2.setBounds(156,48,100,21);
		label2.setText("Defunct 2");
		add(label2);
		label2.setBounds(312,24,60,24);
		add(c3);
		c3.setBounds(288,48,100,21);
		label3.setText("Defunct 3");
		add(label3);
		label3.setBounds(444,24,60,24);
		add(c4);
		c4.setBounds(420,48,100,21);
		m_ok.setLabel("OK");
		add(m_ok);
		m_ok.setBackground(java.awt.Color.lightGray);
		m_ok.setBounds(241,84,60,24);
		setTitle("A Simple Frame");
		//}}

		//{{REGISTER_LISTENERS
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		SymAction lSymAction = new SymAction();
		m_ok.addActionListener(lSymAction);
		//}}

		//{{INIT_MENUS
		//}}

	}

	
	void setFrame(String s,int split[][], int mrg)
    {
	    mergeNum = mrg;
	    c1.removeAll();
	    c2.removeAll();
	    c3.removeAll();
	    c4.removeAll();
	    Choice ch[] = new Choice[4];
	    ch[0]=c1;ch[1]=c2;ch[2]=c3; ch[3]=c4;
	    int pos = 0;
	    int nParts = split.length;
	    for ( int i = 0;i<nParts ; i++) {
		    for (int j =0;j<split[i].length;j++)
		    {
		        for (int m =0;m<split[i].length;m++)
		        {
			        ch[j + pos].addItem(Preference.m_hotelStr[split[i][m]]);
		        }
		    }
		    pos += split[i].length;
	    }
    }


	public void start()
    {
	    setVisible(true);
    }
	public void setVisible(boolean b)
	{
		if(b)
		{
			setLocation(50, 50);
		}
	    super.setVisible(b);
	}


	public void addNotify()
	{
		// Record the size of the window prior to calling parents addNotify.
		Dimension d = getSize();

		super.addNotify();

		if (fComponentsAdjusted)
			return;

		// Adjust components according to the insets
		Insets ins = getInsets();
		setSize(ins.left + ins.right + d.width, ins.top + ins.bottom + d.height);
		Component components[] = getComponents();
		for (int i = 0; i < components.length; i++)
			{
			Point p = components[i].getLocation();
			p.translate(ins.left, ins.top);
			components[i].setLocation(p);
		}
		fComponentsAdjusted = true;
	}

	// Used for addNotify check.
	boolean fComponentsAdjusted = false;

	class SymWindow extends java.awt.event.WindowAdapter
	{
		public void windowClosing(java.awt.event.WindowEvent event)
		{
		Object object = event.getSource();
		if (object == OrderFrame2.this)
			OrderFrame2_WindowClosing(event);
		}
	}

	void OrderFrame2_WindowClosing(java.awt.event.WindowEvent event)
	{
		mOk_ActionPerformed( null);
	}
	//{{DECLARE_CONTROLS
	java.awt.Label l1 = new java.awt.Label();
	java.awt.Choice c1 = new java.awt.Choice();
	java.awt.Label label1 = new java.awt.Label();
	java.awt.Choice c2 = new java.awt.Choice();
	java.awt.Label label2 = new java.awt.Label();
	java.awt.Choice c3 = new java.awt.Choice();
	java.awt.Label label3 = new java.awt.Label();
	java.awt.Choice c4 = new java.awt.Choice();
	java.awt.Button m_ok = new java.awt.Button();
	//}}

	//{{DECLARE_MENUS
	//}}


	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == m_ok)
				mOk_ActionPerformed(event);
		}
	}

	void mOk_ActionPerformed(java.awt.event.ActionEvent event)
	{
	    String str;
		int mergeList[] = new int[4];
	    int j;
	    Choice ch[] = new Choice[4];
	    ch[0]=c1;ch[1]=c2;ch[2]=c3; ch[3]=c4;
	    for ( int i = 0; i<mergeNum;i++)
	    {
		    for (j = 0; j<7; j++) {
		        str =ch[i].getSelectedItem();
		        str = m_gameBoard.getM_hot()[j].getName();
		        if (m_gameBoard.getM_hot()[j].getName().equals(ch[i].getSelectedItem()))
			        break;
		    }
		    for(int k=0;k<i;k++)
		    {
		        if(mergeList[k] == j){
		            return;
		        }
		    }
		    mergeList[i] = j;
	    }
	    setVisible(false);
	    m_gameBoard.setMerge(mergeNum,mergeList);
			 
	}
}