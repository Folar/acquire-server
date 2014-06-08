package ackgames.acquire;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class AQCSwapStock extends AQC implements Serializable
{
    public AQCSwapStock()
    {
        super(GAMEBOARD_SWAP_STOCK);
    }
    public AQCSwapStock(int id,int gid,int survivor,int defunct,int swap,int sell)
    {
        super(GAMEBOARD_SWAP_STOCK,id,gid);
        setTarget(ALL);
        setMessageTarget(ALL);
        setMessageType(GAME);
        mSurvivor =survivor;
        mDefunct =defunct;
        mSwap = swap;
        mSell =sell;
    }
    
    public  void setSurvivor(int x)
    {
        mSurvivor=x;
    }
    public int getSurvivor()
    {
        return mSurvivor;
    }
    public  void setDefunct(int x)
    {
        mDefunct=x;
    }
    public int getDefunct()
    {
        return mDefunct;
    }
    public  void setSwap(int x)
    {
        mSwap=x;
    }
    public int getSwap()
    {
        return mSwap;
    }
    public  void setSell(int x)
    {
        mSell=x;
    }
    public int getSell()
    {
        return mSell;
    }
     public  void writeAQC(DataOutputStream dos){
        super.writeAQC(dos);
        try {
            dos.writeInt(mSurvivor);
            dos.writeInt(mDefunct);
            dos.writeInt(mSwap);
            dos.writeInt(mSell);
        }catch (IOException ioe){
        }    
    }
    public  void readAQC(DataInputStream dis){
        super.readAQC(dis);
       try{
            mSurvivor = dis.readInt();
            mDefunct = dis.readInt();
            mSwap = dis.readInt();
            mSell = dis.readInt();
        }catch (IOException ioe){
        }    
    }
    private int mSurvivor;
    private int mDefunct;
    private int mSwap;
    private int mSell;
}