package ackgames.acquire;

import java.io.Serializable;

class AQCPlaceState extends AQC implements Serializable
{
    AQCPlaceState()
    {
        super(CLIENT_PLACE_STATE);
    }
    AQCPlaceState(int id,String name)
    {
        super(CLIENT_PLACE_STATE,id);
        setMessageType(GAME);
        setMessage(name + "  it's your turn");
        setMessageTarget(ALL);
        setTarget(ALL);
    }
    
}