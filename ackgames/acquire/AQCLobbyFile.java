package ackgames.acquire;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
public class AQCLobbyFile extends AQC implements Serializable
{
    public AQCLobbyFile()
    {
        super(LOBBY_FILE);
    }
    public AQCLobbyFile(int gid,String fileName)
    {
        super(LOBBY_FILE,-1,gid);
        mFileName = fileName;
        mSave = true;
    }
    public AQCLobbyFile(String name,String fileName)
    {
        super(LOBBY_FILE);
        mFileName = fileName;
        setName(name);
        mSave = false;
        setMessage(name + " has loaded a Game from file "+fileName+".aq");
    }
    
    public boolean getSave()
    {
        return mSave;
    }
   
    public  void setSave(boolean x)
    {
        mSave=x;
    }
    public String getFileName()
    {
        return mFileName;
    }
    
    public  void writeAQC(DataOutputStream dos){
        super.writeAQC(dos);
        try {
            dos.writeBoolean(mSave);
            dos.writeUTF(mFileName);
        }catch (IOException ioe){
        }    
    }
    public  void readAQC(DataInputStream dis)
    {
        super.readAQC(dis);
        try{
            mSave = dis.readBoolean();
            mFileName  = dis.readUTF();
        }catch (IOException ioe){
        }    
    }
    
    private boolean mSave =false;
    private String mFileName = null;
}