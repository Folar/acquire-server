package ackgames.acquire;

import java.io.Serializable;

public class AQCNextTransaction extends AQC implements Serializable
{
    public AQCNextTransaction(int id,int gid)
    {
        super(GAMEBOARD_NEXT_TRANSACTION,id,gid);
    }
    public AQCNextTransaction()
    {
        super(GAMEBOARD_NEXT_TRANSACTION);
    }
    
}