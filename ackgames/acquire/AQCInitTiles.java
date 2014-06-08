package ackgames.acquire;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class AQCInitTiles extends AQC implements Serializable
{
    public AQCInitTiles()
    {
        super(CLIENT_INIT_TILES);
    }
    public AQCInitTiles(int id,Tile initTiles[],Tile s[])
    {
        super(CLIENT_INIT_TILES,id);
        mInitTiles = initTiles;
        int cnt = 0;
        for(int i=0;i<6;i++){
            if(s[i]!=null){
                cnt++;
            }
        }
        mStartTiles= new Tile[cnt];
        cnt=0;
        for(int i=0;i<6;i++){
            if(s[i]!=null){
                mStartTiles[cnt++]=s[i];
            }
        }
    }
    public  void writeAQC(DataOutputStream dos){
        super.writeAQC(dos);
        try {
            for(int i=0;i<6;i++){
                dos.writeInt(mInitTiles[i].getRow());    
                dos.writeInt(mInitTiles[i].getColumn()); 
            }
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
        mInitTiles = new Tile[6];
        try{
            for(int i=0;i<6;i++){
                r  = dis.readInt();
                c  = dis.readInt();
                mInitTiles[i] = new Tile(r,c);
            }
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
    public  void setInitTiles(Tile x[])
    {
        mInitTiles=x;
    }
    public Tile[] getInitTiles()
    {
        return mInitTiles;
    }
     public  void setStartTiles(Tile x[])
    {
        mStartTiles=x;
    }
    public Tile[] getStartTiles()
    {
        return mStartTiles;
    }
    private Tile mInitTiles[]= null;
    private Tile mStartTiles[]= null;
}