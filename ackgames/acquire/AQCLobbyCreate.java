package ackgames.acquire;

import java.io.Serializable;

public class AQCLobbyCreate extends AQC implements Serializable
{
    public AQCLobbyCreate()
    {
        super(LOBBY_CREATE);
    }
    public AQCLobbyCreate(String name, boolean version)
    {
        super(LOBBY_CREATE);
        setName(name);
        setMessageTarget(ALL);
        setMessageType(GAME);
        setMessage(name + " has created a new Game");
        if(version){
            setVersion(CENTRAL_AMERICA);
        }
    }
}
