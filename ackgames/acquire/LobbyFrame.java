package ackgames.acquire;



import java.awt.*;

public class LobbyFrame extends java.awt.Frame
{
    LobbyFrame(Acquire a)
    {
        setSize(530,500);
        setLocation(600, 150);
        mLobbyPanel = new LobbyPanel(a);
        setLayout(new BorderLayout());
        add(mLobbyPanel,BorderLayout.CENTER);
        SymWindow aSymWindow = new SymWindow();
		addWindowListener(aSymWindow);
    }
    LobbyPanel mLobbyPanel = null;
    
    class SymWindow extends java.awt.event.WindowAdapter
	{
		public void windowClosing(java.awt.event.WindowEvent event)
		{
		    show(false);
		}
	}
}