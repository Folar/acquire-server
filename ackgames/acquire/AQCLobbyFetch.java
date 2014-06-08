package ackgames.acquire;

import java.io.Serializable;

public class AQCLobbyFetch extends AQC implements Serializable
{
    public AQCLobbyFetch()
    {
        super(LOBBY_FETCH);
    }
    public AQCLobbyFetch(String name)
    {
        super(LOBBY_FETCH);
        setName(name);
        setMessageTarget(ALL);
        setMessageType(GAME);
        setMessage(name + " has enter the lobby");
    }
}
