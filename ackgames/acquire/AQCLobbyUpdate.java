package ackgames.acquire;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
public class AQCLobbyUpdate extends AQC implements Serializable
{
    public AQCLobbyUpdate()
    {
        super(LOBBY_UPDATE);
    }
    public AQCLobbyUpdate(AQCRoom list[])
    {
        super(LOBBY_UPDATE);
        mRooms=list;
       
    }
    public AQCLobbyUpdate(AQCRoom list[],boolean b)
    {
        this(list);
        mSwitchToLobby = b;
        mStateChange = true;
       
    }
    public boolean getStateChange()
    {
        return mStateChange;
    }
    public boolean getSwitchToLobby()
    {
        return mSwitchToLobby;
    }
   
    public  void setRooms(AQCRoom x[])
    {
        mRooms=x;
    }
    public AQCRoom[] getRooms()
    {
        return mRooms;
    }
    
    public  void writeAQC(DataOutputStream dos){
        super.writeAQC(dos);
        try {
            dos.writeBoolean(mSwitchToLobby);
            dos.writeBoolean(mStateChange);
            dos.writeInt(mRooms.length);
            for(int i=0;i<mRooms.length;i++){
                mRooms[i].writeRoom(dos);    
            }
        }catch (IOException ioe){
        }    
    }
    public  void readAQC(DataInputStream dis)
    {
        super.readAQC(dis);
        try{
            mSwitchToLobby = dis.readBoolean();
            mStateChange = dis.readBoolean();
            int cnt  = dis.readInt();
            mRooms = new AQCRoom[cnt];
            for(int i=0;i<cnt;i++){
                mRooms[i] = new AQCRoom();
                mRooms[i].readRoom(dis);
            }
        }catch (IOException ioe){
        }    
    }
    private boolean mStateChange=false;
    private boolean mSwitchToLobby =false;
    private AQCRoom mRooms[] = null;
}