package ackgames.acquire;

import java.awt.*;

public class LoginPanel extends java.awt.Panel
{
    Acquire m_acquire = null;
   
	public LoginPanel(Acquire a,String host,String port,String name)
	{

         m_acquire = a;
		//{{INIT_CONTROLS
		setLayout(null);
		setBackground(new java.awt.Color(79,148,232));
		setSize(732,484);
		label1.setText("Name:");
		add(label1);
		label1.setForeground(java.awt.Color.white);
		label1.setFont(new Font("SansSerif", Font.BOLD|Font.ITALIC, 12));
		label1.setBounds(234,132,36,24);
		m_loginPanel.setLabel("Login");
		add(m_loginPanel);
		m_loginPanel.setBackground(java.awt.Color.lightGray);
		m_loginPanel.setBounds(276,312,60,24);
		add(m_name);
		m_name.setBounds(282,132,216,24);
		m_hostLabel.setText("Host:");
		add(m_hostLabel);
		m_hostLabel.setForeground(java.awt.Color.white);
		m_hostLabel.setFont(new Font("SansSerif", Font.BOLD|Font.ITALIC, 12));
		m_hostLabel.setBounds(234,180,36,24);
		add(m_host);
		m_host.setBounds(282,180,216,24);
		m_portLabel.setText("Port:");
		add(m_portLabel);
		m_portLabel.setForeground(java.awt.Color.white);
		m_portLabel.setFont(new Font("SansSerif", Font.BOLD|Font.ITALIC, 12));
		m_portLabel.setBounds(234,228,36,24);
		add(m_port);
		m_port.setBounds(282,228,84,24);
		label2.setText("Acquire");
		add(label2);
		label2.setForeground(java.awt.Color.white);
		label2.setFont(new Font("Serif", Font.ITALIC, 36));
		label2.setBounds(299,48,145,36);
		label3.setText("Copyright 1997-2004 AckPlay 2.0. All rights reserved.");
		add(label3);
		label3.setForeground(java.awt.Color.white);
		label3.setFont(new Font("SansSerif", Font.ITALIC, 10));
		label3.setBounds(36,408,264,24);
		add(label4);
		label4.setForeground(java.awt.Color.white);
		label4.setFont(new Font("SansSerif", Font.ITALIC, 12));
		label4.setBounds(408,336,252,24);
		error.setText("text");
		add(error);
		error.setForeground(java.awt.Color.white);
		error.setFont(new Font("SansSerif", Font.BOLD, 16));
		error.setBounds(288,276,324,24);
		error.setVisible(false);
		//}}
        m_host.setText(host);
        m_port.setText(port);
        if(name != null){
            m_name.setText(name);
            
        }
		//{{REGISTER_LISTENERS
		SymAction lSymAction = new SymAction();
		m_loginPanel.addActionListener(lSymAction);
		m_name.addActionListener(lSymAction);
		//}}
	}
    void setErrorOn(String txt)
    {
        error.setText(txt);
        error.setVisible(true);
    }
    void setErrorOff()
    {
        error.setVisible(false);
    }
	//{{DECLARE_CONTROLS
	java.awt.Label label1 = new java.awt.Label();
	java.awt.Button m_loginPanel = new java.awt.Button();
	java.awt.TextField m_name = new java.awt.TextField();
	java.awt.Label m_hostLabel = new java.awt.Label();
	java.awt.TextField m_host = new java.awt.TextField();
	java.awt.Label m_portLabel = new java.awt.Label();
	java.awt.TextField m_port = new java.awt.TextField();
	java.awt.Label label2 = new java.awt.Label();
	java.awt.Label label3 = new java.awt.Label();
	java.awt.Label label4 = new java.awt.Label();
	java.awt.Label error = new java.awt.Label();
	//}}


	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == m_loginPanel)
				mLoginPanel_ActionPerformed(event);
			else if (object == m_name)
				mName_ActionPerformed(event);
		}
	}

	void mLoginPanel_ActionPerformed(java.awt.event.ActionEvent event)
	{
	     setErrorOff();
		 m_acquire.doit(m_name.getText(),m_host.getText(),m_port.getText());
			 
	}

	void mName_ActionPerformed(java.awt.event.ActionEvent event)
	{
		mLoginPanel_ActionPerformed(null);
			 
	}

	
}