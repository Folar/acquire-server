package ackgames.acquire;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class AQCBuyHotel extends AQC implements Serializable
{
    public AQCBuyHotel()
    {
        super(GAMEBOARD_BUY_HOTEL);
    }
    public AQCBuyHotel(int id,int gid,boolean over)
    {
        super(GAMEBOARD_BUY_HOTEL,id,gid);
        setTarget(ALL);
        setMessageTarget(ALL);
        setMessageType(GAME);
        mOver = over;
        if(over){
            setCode(GAMEBOARD_END_GAME);
        }
    }
    public void setHotelPurchase(int h,int a)
    {
        mHotels[mCount]=h;
        mAmounts[mCount++]=a;
    }
    public  int getAmount(int i)
    {
        return mAmounts[i];
    }
    public int getHotel(int i)
    {
        return mHotels[i];
    }
    public int getCount()
    {
        return mCount;
    }
    public  void writeAQC(DataOutputStream dos){
        super.writeAQC(dos);
        try {
            dos.writeBoolean(mOver);
            dos.writeInt(mCount);
            for(int i=0;i<mCount;i++){
                dos.writeInt(mAmounts[i]);
            }
             for(int i=0;i<mCount;i++){
                dos.writeInt(mHotels[i]);
            }
        }catch (IOException ioe){
        }    
    }
    public  void readAQC(DataInputStream dis){
       super.readAQC(dis);
       try{
            mOver = dis.readBoolean();
            if(mOver){
                setCode(GAMEBOARD_END_GAME);
            }
            mCount = dis.readInt();
            mAmounts = new int[mCount];
            mHotels = new int[mCount];
            for(int i=0;i<mCount;i++){
                mAmounts[i] =dis.readInt();
            }
             for(int i=0;i<mCount;i++){
                mHotels[i] =dis.readInt();
            }
        }catch (IOException ioe){
        }    
    }
    private int mHotels[]= new int[6];
    private int mAmounts[]= new int[6];
    private int mCount=0;
    private boolean mOver =false;
    
}