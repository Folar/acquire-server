package ackgames.acquire;

import java.io.Serializable;

public class AQCGameAbort extends AQC implements Serializable
{
    public AQCGameAbort()
    {
        super(CLIENT_ABORT);
    }
    public AQCGameAbort(String name,int id,int gid)
    {
        super(CLIENT_ABORT,id,gid);
        setName(name);
        setMessageTarget(ALL);
        setMessageType(GAME);
        setMessage(name + " has ended the Game prematurely");
    }
    
    public AQCGameAbort(int gid)
    {
        super(CLIENT_ABORT,-1,gid);
        setMessageTarget(ALL);
        setMessageType(GAME);
        setMessage("Game " + gid+ " has timed out" );
    }
}
