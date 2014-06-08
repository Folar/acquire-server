package ackgames.acquire;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
public class AQCTimeout extends AQC implements Serializable
{
    public AQCTimeout()
    {
        super(LOBBY_TIMEOUT);
    }
    public AQCTimeout(int time)
    {
        super(LOBBY_TIMEOUT);
        mTime=time;
    }
    
    
    public int getTime()
    {
        return mTime;
    }
   
    public  void setSave(int x)
    {
        mTime=x;
    }
    
    
    public  void writeAQC(DataOutputStream dos){
        super.writeAQC(dos);
        try {
            dos.writeInt(mTime);
        }catch (IOException ioe){
        }    
    }
    public  void readAQC(DataInputStream dis)
    {
        super.readAQC(dis);
        try{
            mTime = dis.readInt();
        }catch (IOException ioe){
        }    
    }
    
    private int mTime=30;
}