package ackgames.acquire;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
public class AQCMergeHotel extends AQC implements Serializable
{
    public AQCMergeHotel()
    {
        super(GAMEBOARD_MERGE_HOTEL);
    }
    public AQCMergeHotel(int id,int gid,int cnt,int list[])
    {
        super(GAMEBOARD_MERGE_HOTEL,id,gid);
        mHotelCount=cnt;
        mMergeList=list;
        setTarget(ALL);
        setMessageTarget(ALL);
        setMessageType(GAME);
    }
    public  void setHotelCount(int x)
    {
        mHotelCount=x;
    }
    public int getHotelCount()
    {
        return mHotelCount;
    }
    public  void setMergeList(int x[])
    {
        mMergeList=x;
    }
    public int[] getMergeList()
    {
        return mMergeList;
    }
    
    public  void writeAQC(DataOutputStream dos){
        super.writeAQC(dos);
        try {
            dos.writeInt(mHotelCount);
            for(int i=0;i<mHotelCount;i++){
                dos.writeInt(mMergeList[i]);    
            }
        }catch (IOException ioe){
        }    
    }
    public  void readAQC(DataInputStream dis)
    {
        super.readAQC(dis);
        try{
            mHotelCount  = dis.readInt();
            mMergeList = new int[mHotelCount];
            for(int i=0;i<mHotelCount;i++){
                mMergeList[i] = dis.readInt();
            }
        }catch (IOException ioe){
        }    
    }
    
    private int mMergeList[];
    private int mHotelCount;
}