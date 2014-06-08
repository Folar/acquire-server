package ackgames.acquire;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class AQCStartHotel extends AQC implements Serializable
{
    public AQCStartHotel()
    {
        super(GAMEBOARD_START_HOTEL);
    }
    public AQCStartHotel(int id,int gid,int hotel,String hName,String pName)
    {
        super(GAMEBOARD_START_HOTEL,id,gid);
        setTarget(ALL);
        setMessageTarget(ALL);
        setMessageType(GAME);
        setMessage(pName + " starts "+hName);
        setHotelIndex(hotel);
    }
    
    public  void setHotelIndex(int x)
    {
        mHotelIndex=x;
    }
    public int getHotelIndex()
    {
        return mHotelIndex;
    }
    public  void writeAQC(DataOutputStream dos){
        super.writeAQC(dos);
        try {
            dos.writeInt(mHotelIndex);
        }catch (IOException ioe){
        }    
    }
    public  void readAQC(DataInputStream dis){
        super.readAQC(dis);
       try{
            mHotelIndex = dis.readInt();
        }catch (IOException ioe){
        }    
    }
    private int mHotelIndex;
    
}
