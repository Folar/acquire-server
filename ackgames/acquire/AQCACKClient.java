package ackgames.acquire;

import java.io.Serializable;

public class AQCACKClient extends AQC implements Serializable
{
    public AQCACKClient()
    {
        super(CLIENT_ACK);
    }
    public AQCACKClient(int id,int gid)
    {
        super(CLIENT_ACK,id,gid);
        setTarget(ALL);
    }
    
}
