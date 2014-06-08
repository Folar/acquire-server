package ackgames.acquire;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class AQCPlayTile extends AQC implements Serializable
{
    public AQCPlayTile()
    {
        super(GAMEBOARD_PLAY_TILE);
    }
    public AQCPlayTile(int id,int gid,Tile t,String name)
    {
        super(GAMEBOARD_PLAY_TILE ,id,gid);
        mTile=t;
        setTarget(ALL);
        setMessageTarget(ALL);
        setMessageType(GAME);
        setMessage(name + " plays Tile "+t.getLabel());
    }
    
    public Tile getTile()
    {
        return mTile;
    }
    public void setTile(Tile x)
    {
        mTile=x;
    }
    public  void writeAQC(DataOutputStream dos){
        super.writeAQC(dos);
        try {
            dos.writeInt(mTile.getRow());
            dos.writeInt(mTile.getColumn());
        }catch (IOException ioe){
        }    
    }
    public  void readAQC(DataInputStream dis){
       super.readAQC(dis);
       try{
            int r = dis.readInt();
            int c = dis.readInt();
            mTile = new Tile(r,c);
        }catch (IOException ioe){
        }    
    }
    private Tile mTile;
    
}