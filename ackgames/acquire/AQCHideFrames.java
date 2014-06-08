package ackgames.acquire;

import java.io.Serializable;

public class AQCHideFrames extends AQC implements Serializable
{
    public AQCHideFrames()
    {
        super(GAMEBOARD_HIDE_FRAMES);
    }
    public AQCHideFrames(int id,String name)
    {
        super(GAMEBOARD_HIDE_FRAMES,id);
        setMessage(name+" has lost a connection");
        setTarget(ALL);
        setMessageTarget(ALL);
        setMessageType(GAME);
        setName(name);
        setLostConnection(true);
    }
   
    
}
