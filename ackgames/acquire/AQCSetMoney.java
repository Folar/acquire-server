package ackgames.acquire;

import java.io.Serializable;

class AQCSetMoney extends AQC implements Serializable
{
    public AQCSetMoney(int id,int amt,int pid)
    {
        super(GAMEBOARD_SET_MONEY,id);
        mAmount = amt;
        setTarget(ALL);
    }
    
    public int getAmount()
    {
        return mAmount;
    }
    public void setAmount(int x)
    {
         mAmount= x;
    }
    public int getPlayerID()
    {
        return mPlayerID;
    }
    public void setPlayerID(int x)
    {
         mPlayerID= x;
    }
    private int mAmount;
    private int mPlayerID;
}