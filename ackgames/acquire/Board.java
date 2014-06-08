package ackgames.acquire;

import java.awt.*;


public class Board extends Canvas
{
    GameBoard m_gameBoard;
    public Board(GameBoard gb)
    {
	    m_gameBoard = gb;
        setBackground(Preference.GAME_BOARD_COLOR);
	    setForeground(Color.black);
    }
    public void setPlayer (Player x)
    {
    }

    int m_gridLineWidth =2;
    void drawgrid(Graphics g)
    {



        if(Preference.THREE_D){
            for (int x = 0; x < 13; x++) {  //vertical
                g.setColor(Color.lightGray);
                g.drawLine(x*(tileWidth+ m_gridLineWidth),
                        0,
                        x*(tileWidth+ m_gridLineWidth), 
                        adjHeight);
                g.setColor(Color.gray);
                g.drawLine(x*(tileWidth+ m_gridLineWidth)+1,
                        0,
                        x*(tileWidth+ m_gridLineWidth)+1, 
                        adjHeight);
            }                
            for (int y = 0; y < 10; y++){
                g.setColor(Color.lightGray);
                g.drawLine(0,
                        y*(tileHeight+m_gridLineWidth),
                        adjWidth,
                        y*(tileHeight+m_gridLineWidth));
                g.setColor(Color.gray);           
                g.drawLine(0,
                        y*(tileHeight+m_gridLineWidth)+1,
                        adjWidth,
                        y*(tileHeight+m_gridLineWidth)+1);           
            }
        } else {
            for (int x = 0; x < 13; x++)
                g.fillRect(x*(tileWidth+ m_gridLineWidth),0,m_gridLineWidth, 
                            adjHeight);
            for (int y = 0; y < 10; y++)
                g.fillRect(0,y*(tileHeight+m_gridLineWidth),adjWidth,m_gridLineWidth);
        }    
    }
    int tileHeight;
    int tileWidth;
    int adjWidth;
    int adjHeight;
    Font fb = new Font("Serif",Font.BOLD,14);
    public void paint(Graphics g)
    {
        int h = getBounds().height;
	    int w = getBounds().width;
	    if(!Preference.THREE_D){
	        m_gridLineWidth=1;
	    }
        tileHeight= (h-10 * m_gridLineWidth)/9;
        tileWidth= (w- 13 * m_gridLineWidth)/12;
        adjWidth= 12*tileWidth+ 13*m_gridLineWidth;
        adjHeight=9*tileHeight+ 10 *m_gridLineWidth;
        upd(g);
        if(g  !=null)return;

    }

    synchronized public boolean mouseDown(Event e,int x, int y)
    {
        int w = getBounds().width;
        int h = getBounds().height;
        if (e.modifiers == Event.SHIFT_MASK){
             m_gameBoard.showLastMsg();
	    }else {
	        if ( (y/(h/9)> 8) ||( x/(w/12) > 11)) return true;
	        int ho=0;
	        if(m_gameBoard.getM_tile()[y/(h/9)][x/(w/12)].getState() != Tile.EMPTY &&
	            m_gameBoard.getM_tile()[y/(h/9)][x/(w/12)].getState() != Tile.ONBOARD){
		        if (m_gameBoard.getGameState() == GameBoard.BUYSTOCK){
		            ho = m_gameBoard.getM_tile()[y/(h/9)][x/(w/12)].getState();
		            if (m_gameBoard.m_okToBuy== false) return true;
		            if (m_gameBoard.canBuyStock(ho) == true) {
			            m_gameBoard.getM_wallet().incr(ho);
		            }
		        }else {
		            ho = m_gameBoard.getM_tile()[y/(h/9)][x/(w/12)].getState();
		            m_gameBoard.showStock(ho);
		            m_gameBoard.getM_stockPanel().showDetail();
		            
		        }
	        }
	    }
	    return true;
    }

    public void setMultiPlayer(boolean mp)
    {
    }
   
    public void drawBorder(Graphics g,int x,int y)
    {
        drawBorder(g, x, y,Color.white,Color.black);
    }
    public void drawBorder(Graphics g,int x,int y,Color top,Color bottom)
    {
        g.setColor(bottom);
		int sx = x;
		int ex = x+tileWidth-1;
		int sy = y;
		int ey = y+tileHeight-1;
		// vertical
		g.drawLine(ex, sy, ex, ey);
		// horizontal
		g.drawLine(sx, ey, ex, ey);        
        g.setColor(top);
	    	// vertical
    	g.drawLine(sx, sy, sx, ey);
		// horizontal
		g.drawLine(sx, sy, ex, sy);
	}
    public void upd(Graphics g) 
    {
        boolean all = true;
        if(g== null)
        {
            g= this.getGraphics();
            all=false;   
        }
	    String tileLabel;
	    Integer I1;
	    int col;
	    int row;
	    String str[] = {"A","B","C","D","E","F","G","H","I"} ;
	    int h = getBounds().height;
	    int w = getBounds().width;
	    
	   
	    FontMetrics fm = getFontMetrics(fb);
	    int sh = fm.getHeight();
	    for (int i = 0; i <9; i++) {
	        for (int j = 0; j < 12; j++) {
	            if ( !all && !m_gameBoard.getM_tile()[i][j].isM_dirty())
	            {
	                continue;
	            }
	    	    row = m_gameBoard.getM_tile()[i][j].getRow();
		        col  = m_gameBoard.getM_tile()[i][j].getColumn();
                //System.out.println("in paint Row() = " + row+" col = " + col);
                //System.out.println("xxxxxxxxx row ="+row +"yyyyy col= "+col);
		        Color c = g.getColor();
		        Color brown =  new Color(0xa0a033);
                m_gameBoard.getM_tile()[i][j].setM_dirty(false);
		        int x= m_gridLineWidth + (tileWidth + m_gridLineWidth ) * j;
                int y= m_gridLineWidth  + (tileHeight + m_gridLineWidth ) * i;
			    switch (m_gameBoard.getM_tile()[i][j].getState()) {
				        case Tile.ONBOARD:
				            drawBorder(g,x,y);
					        g.setColor(Color.black);
					        if(Preference.THREE_D){
					            g.fillRect(x + 1 ,y+1 ,tileWidth-2 ,tileHeight -2);
					            drawBorder(g,x,y);
					        } else{
					            g.fillRect(x ,y ,tileWidth ,tileHeight );
					        }   
					        break;
				        case Hotel.LUXOR:
					    case Hotel.TOWER:
					    case Hotel.AMERICAN:
					    case Hotel.FESTIVAL:
					    case Hotel.WORLDWIDE:
					    case Hotel.IMPERIAL:
					    case Hotel.CONTINENTAL:
					       
                            if (m_gameBoard.getM_tile()[i][j].m_mergeTile){
					            g.setColor(Preference.MERGE_TILE_BACKGROUND);
					        } else {
					            g.setColor(Preference.m_color[m_gameBoard.getM_tile()[i][j].getState()]);
					        }					       
                            		 
					        if(Preference.THREE_D){
					            g.fillRect(x + 1 ,y+1 ,tileWidth-2 ,tileHeight -2);
					            drawBorder(g,x,y);
					        } else{
					            g.fillRect(x ,y ,tileWidth ,tileHeight );
					        }     
					             
					        break;
				         case Tile.EMPTY:
				            if (m_gameBoard.getM_tile()[i][j].isM_mouseOver())
				            {
				                drawBorder(g, x, y,Color.magenta,Color.magenta);
				                m_gameBoard.getM_tile()[i][j].setM_mouseOver(false);
					            g.setColor(Color.lightGray);
					            g.fillRect(x + 1 ,y+1 ,tileWidth-2 ,tileHeight -2);
				            } else{
                                if(m_gameBoard.getM_tile()[i][j].isM_inRack()){
                                     g.setColor(Preference.TILE_IN_RACK);
                                }else{
				                    g.setColor(Preference.GAME_BOARD_COLOR);
                                }
				                g.fillRect(x  ,y ,tileWidth ,tileHeight);
				            }
			     }
			  
			    
			  
		        g.setColor(c);


		        I1 = new Integer(col + 1);
		        tileLabel = m_gameBoard.getM_tile()[i][j].getLabel();
		
		        int sw = fm.stringWidth(tileLabel);
	    	    //int xpos = j * w/12 + w/24 - sw;
		        //int ypos = i * h/9 + h/18 - sh/2 + sh;
		        int xpos = x + tileWidth/2 - sw/2;
		        int ypos = y +( tileHeight/2 +sh/2 -2);
		        if ( m_gameBoard.getM_tile()[i][j].getState() != Tile.EMPTY &&
		             m_gameBoard.getM_tile()[i][j].getState() == Tile.ONBOARD ) {
			        g.setColor(Color.white);
		        } else if ( m_gameBoard.getM_tile()[i][j].getState() != Tile.EMPTY &&
		             m_gameBoard.getM_tile()[i][j].getState() != Tile.ONBOARD ) {
			        if (m_gameBoard.getM_tile()[i][j].m_mergeTile){
					    g.setColor(Preference.MERGE_TILE_FOREGROUND);
					} else {        
			            g.setColor(Preference.TILE_FOREGROUND);
			        }
		        }
		        g.setFont(fb);
		        ///g.setColor(Color.black);
		        g.drawString(tileLabel,xpos,ypos);
		        if ( m_gameBoard.getM_tile()[i][j].getState() != Tile.EMPTY) {
			        g.setColor(c);
		        }
	        }
	    }
	    
	    if(true)
	    {
	        drawgrid(g);
	        
	    }    
	    if(!all){
	        g.dispose();
	    }
    

	}

}
