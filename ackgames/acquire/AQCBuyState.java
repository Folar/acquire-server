package ackgames.acquire;

import java.io.Serializable;

class AQCBuyState extends AQC implements Serializable
{
    AQCBuyState()
    {
        super(CLIENT_BUY_STATE);
    }
    AQCBuyState(int id,String name)
    {
        super(CLIENT_BUY_STATE,id);
        setMessageType(GAME);
        setMessage(name + " it's your turn");
        setMessageTarget(ALL);
        setTarget(ALL);
    }
    
}