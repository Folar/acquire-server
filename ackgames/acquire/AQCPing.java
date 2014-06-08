package ackgames.acquire;

import java.io.Serializable;

public class AQCPing extends AQC implements Serializable
{
    public AQCPing()
    {
        super(CLIENT_PING,-1);
    }
    public AQCPing(int gid)
    {
        super(CLIENT_PING,-1,gid);
    }
    
}