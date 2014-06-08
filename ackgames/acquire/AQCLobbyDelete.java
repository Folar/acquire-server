package ackgames.acquire;

import java.io.Serializable;

public class AQCLobbyDelete extends AQC implements Serializable
{
    public AQCLobbyDelete()
    {
        super(LOBBY_DELETE);
    }
    public AQCLobbyDelete(int gid)
    {
        super(LOBBY_DELETE,-1,gid);
    }
}
