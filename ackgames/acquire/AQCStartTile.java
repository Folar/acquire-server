package ackgames.acquire;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class AQCStartTile extends AQC implements Serializable
{
  
    public AQCStartTile()
    {
        super(CLIENT_START_TILE);
    }
    public AQCStartTile(int id, Tile s[],String name)
    {
        super(CLIENT_START_TILE,id);
        mStartTiles =s;
        setMessageTarget(ALL);
        setTarget(ALL);
        setMessageType(GAME);
        int cnt =0;
        setMessage(name + " goes first");
        for(int i=0;i<6;i++){
            if(s[i] ==null){
                break;
            }
            cnt++;
        }
        mStartTiles= new Tile[cnt];
        for(int i=0;i<cnt;i++){
            mStartTiles[i]=s[i];
        }
    }
    public  void setStartTiles(Tile x[])
    {
        mStartTiles=x;
    }
    public Tile[] getStartTiles()
    {
        return mStartTiles;
    }
    
    public  void writeAQC(DataOutputStream dos){
        super.writeAQC(dos);
        try {
            dos.writeInt(mStartTiles.length);
            for(int i=0;i<mStartTiles.length;i++){
                dos.writeInt(mStartTiles[i].getRow());    
                dos.writeInt(mStartTiles[i].getColumn()); 
            }
        }catch (IOException ioe){
        }    
    }
    public  void readAQC(DataInputStream dis){
        super.readAQC(dis);
        int r;
        int c;
        try{
            int cnt = dis.readInt();
            mStartTiles = new Tile[cnt];
            for(int i=0;i<mStartTiles.length;i++){
                r  = dis.readInt();
                c  = dis.readInt();
                mStartTiles[i] = new Tile(r,c);
            }
        }catch (IOException ioe){
        }    
    }

    
    private Tile mStartTiles[]= null;
}
    