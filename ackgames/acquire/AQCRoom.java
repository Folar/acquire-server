package ackgames.acquire;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
public class AQCRoom
{
    
    public AQCRoom()
    {
        mGame = -1;
    }
    public AQCRoom(int game ,boolean playing,String players,String kibitzers,int version)
    {
        mPlaying = playing;
        mPlayers = players;
        mKibitzers = kibitzers;
        mGame = game;
        mVersion = version;
    }
    public AQCRoom(AQCRoom r)
    {
        mPlaying = r.mPlaying;
        mPlayers = r.mPlayers;
        mKibitzers = r.mKibitzers;
        mGame = r.mGame;
        mVersion = mVersion;
    }
    
    public  void setPlaying(boolean x)
    {
        mPlaying=x;
    }
    public boolean getPlaying()
    {
        return mPlaying;
    }
    public  void setPlayers(String x)
    {
        mPlayers=x;
    }
    public String getPlayers()
    {
        return mPlayers;
    }
    public  void setKibitzers(String x)
    {
        mKibitzers=x;
    }
    public String getKibitzers()
    {
        return mKibitzers;
    }
    public  void setGame(int x)
    {
        mGame=x;
    }
    public int getGame()
    {
        return mGame;
    }
    public boolean isEmpty()
    {
        if(mGame == -1){
            return true;
        }
        return false;
    }
    public int getVersion()
    {
        return mVersion;
    }
    public void setVersion(int x)
    {
        mVersion =x;
    }
    public  void writeRoom(DataOutputStream dos){
        try {
            dos.writeBoolean(mPlaying);
            dos.writeUTF(mPlayers);
            dos.writeUTF(mKibitzers);
            dos.writeInt(mGame);
            dos.writeInt(mVersion);
        }catch (IOException ioe){
        }    
    }
    public  void readRoom(DataInputStream dis)
    {
        try{
            mPlaying  = dis.readBoolean();
            mPlayers = dis.readUTF();
            mKibitzers = dis.readUTF();
            mGame = dis.readInt();
            mVersion = dis.readInt();
        }catch (IOException ioe){
        }    
    }
    
    private String mKibitzers;
    private String mPlayers;
    private boolean mPlaying;
    private int mVersion =0;
    private int mGame;
}