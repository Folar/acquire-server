package ackgames.acquire;

import java.io.Serializable;

public class AQCSignIn extends AQC implements Serializable
{
    public AQCSignIn()
    {
        super(GAMEBOARD_SIGN_IN);
    }
    public AQCSignIn(String name,int gid,boolean k)
    {
        super(GAMEBOARD_SIGN_IN,-1,gid);
        setObserver(k);
        setName(name);
        setMessage(name + " has signed on");
    }
    public AQCSignIn(String name,int gid,boolean k,int ver)
    {
        this(name,gid,k);
        setVersion(ver);
    }
}
