package ackgames.acquire;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

class AQCSwapFrame extends AQC implements Serializable
{
    public AQCSwapFrame()
    {
        super(GAMEBOARD_SWAP_FRAME);
    }
    public AQCSwapFrame(int id, StockTransaction st)
    {
        super(GAMEBOARD_SWAP_FRAME,id);
        mStockTransaction =st;
        setMessageTarget(ALL);
        setMessageType(GAME);
    }
    
    public StockTransaction getStockTransaction()
    {
        return mStockTransaction;
    }
    public void setStockTransaction(StockTransaction st)
    {
         mStockTransaction =st;
    }
    public  void writeAQC(DataOutputStream dos){
        super.writeAQC(dos);
        try {
            dos.writeInt(mStockTransaction.getIndex());
            dos.writeInt(mStockTransaction.getPlayer());
            dos.writeInt(mStockTransaction.getSurvivor() );
            dos.writeInt(mStockTransaction.getDefunct() );
            dos.writeUTF(mStockTransaction.getTitle());
            dos.writeUTF(mStockTransaction.getBonusStr());
        }catch (IOException ioe){
        }    
    }
    public  void readAQC(DataInputStream dis){
        super.readAQC(dis);
       try{
            mStockTransaction = new StockTransaction();
            mStockTransaction.setIndex(dis.readInt());
            mStockTransaction.setPlayer(dis.readInt());
            mStockTransaction.setSurvivor(dis.readInt());
            mStockTransaction.setDefunct(dis.readInt());
            mStockTransaction.setTitle(dis.readUTF());
            mStockTransaction.setBonusStr(dis.readUTF());
        }catch (IOException ioe){
        }    
    }
    private StockTransaction mStockTransaction=null;
    
}