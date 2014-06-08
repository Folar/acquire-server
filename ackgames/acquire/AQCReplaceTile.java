package ackgames.acquire;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class AQCReplaceTile extends AQC implements Serializable
{
    public AQCReplaceTile()
    {
        super(CLIENT_REPLACE_TILE);
    }
    public AQCReplaceTile(int id,Tile t[])
    {
        super(CLIENT_REPLACE_TILE,id);
        mReplaceTiles = t;
    }
    public  void setReplaceTiles(Tile x[])
    {
        mReplaceTiles=x;
    }
    public Tile[] getReplaceTiles()
    {
        return mReplaceTiles;
    }
    
     public  void writeAQC(DataOutputStream dos){
        super.writeAQC(dos);
        try {
            dos.writeInt(mReplaceTiles.length);
            for(int i=0;i<mReplaceTiles.length;i++){
                dos.writeInt(mReplaceTiles[i].getRow());    
                dos.writeInt(mReplaceTiles[i].getColumn()); 
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
            mReplaceTiles = new Tile[cnt];
            for(int i=0;i<mReplaceTiles.length;i++){
                r  = dis.readInt();
                c  = dis.readInt();
                mReplaceTiles[i] = new Tile(r,c);
            }
        }catch (IOException ioe){
        }    
    }

    
    private Tile mReplaceTiles[] = null;
    
}