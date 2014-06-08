package ackgames.acquire;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class AQCAddPlayer extends AQC implements Serializable
{
    public AQCAddPlayer()
    {
        super(CLIENT_ADD_PLAYER);
    }
    public AQCAddPlayer(int id,String names[],String msg,int gid, int version)
    {
        super(CLIENT_ADD_PLAYER,id);
        mNames = names;
        setMessageType(GAME);
        setMessage(msg);
        setGameID(gid);
        setMessageTarget(ALL);
        setTarget(ALL);
        setVersion(version);
    }

    public String[] getNames()
    {
        return mNames;
    }
     public  void writeAQC(DataOutputStream dos){
        super.writeAQC(dos);
        try {
            dos.writeInt(mNames.length);
            for(int i=0;i<mNames.length;i++){
                dos.writeUTF(mNames[i]);
            }
        }catch (IOException ioe){
        }    
    }
    public  void readAQC(DataInputStream dis){
       super.readAQC(dis);
       try{
            int cnt = dis.readInt();
            mNames = new String[cnt];
            for(int i=0;i<cnt;i++){
                mNames[i] = dis.readUTF();
            }
        }catch (IOException ioe){
        }    
    }
    private String[] mNames;
    
}