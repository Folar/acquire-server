package ackgames.acquire;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class AQCGameInfo extends AQC implements Serializable
{
    public AQCGameInfo()
    {
        super(CLIENT_INFO);
    }
    public AQCGameInfo(int id,int currentPlayer,Tile tileState[][],int availableShares[],String names[],int money[],
                int ownShares[][])
    {
        super(CLIENT_INFO,id);
        mCurrentPlayer = currentPlayer;
        mTileState =tileState;
        mAvailableShares = availableShares;
        mNames = names;
        mMoney = money;
        mOwnShares = ownShares;
    }
    public AQCGameInfo(AQCGameInfo agi)
    {
        super(CLIENT_INFO,agi.getCurrentPlayerID());
        setVersion(agi.getVersion());
        setGameStarted(agi.getGameStarted());
        setGameID(agi.getGameID());
        mCurrentPlayer = agi.mCurrentPlayer;
        mTileState =agi.mTileState;
        mAvailableShares = agi.mAvailableShares;
        mNames = agi.mNames;
        mMoney = agi.mMoney;
        mOwnShares = agi.mOwnShares;
        if (agi.getMessageType() ==GAME){
            setMessageType (GAME);
            setMessage( agi.getMessage());
            setMessageTarget(CURRENT);
        }
    }
    
    public void setCurrentPlayer(int x)
    {
        mCurrentPlayer =x;
    }
    public int getCurrentPlayer()
    {
        return mCurrentPlayer;
    }
    public  void setTileState(Tile t[][])
    {
        mTileState = new Tile[9][12];
        for (int i = 0; i <9; i++) {
	        for (int j = 0; j < 12; j++) {
		        mTileState[i][j]= new Tile(i,j);
		        mTileState[i][j].setState(t[i][j].getState());
	        }
	    } 
    }
    public Tile[][] getTileState()
    {
        return mTileState;
    }
    public  void setAvailabeShares(int x[])
    {
        mAvailableShares=x;
    }
    public int[] getAvailabeShares()
    {
        return mAvailableShares;
    }
    public  void setNames(String x[])
    {
        mNames=x;
    }
    public String[] getNames()
    {
        return mNames;
    }
    public  void setMoney(int x[])
    {
        mMoney=x;
    }
    public int[] getMoney()
    {
        return mMoney;
    }
    public  void setOwnShares(int x[][])
    {
        mOwnShares=x;
    }
    public int[][] getOwnShares()
    {
        return mOwnShares;
    }
    
    public Tile[] getRackTiles()
    {
        return mRackTiles;
    }
    public void  setRackTiles(Tile[] t)
    {
        int cnt =0;
        for(int i=0;i<6;i++){
            if(!t[i].isEmpty()){
                cnt++;
            }
        }
        mRackTiles= new Tile[cnt];
        cnt =0;
        for(int i=0;i<6;i++){
            if(!t[i].isEmpty()){
                mRackTiles[cnt]= new Tile(t[i].getRow(),t[i].getColumn());
                cnt++;
            }
        }
    }
    
    public  void setGameStarted(boolean x)
    {
         mGameStarted=x;
    }
    public boolean getGameStarted()
    {
        return mGameStarted;
    }
    
    public  void writeAQC(DataOutputStream dos){
        super.writeAQC(dos);
        try {
            for (int i = 0; i <9; i++) {  
	            for (int j = 0; j < 12; j++) {
		            dos.writeInt(mTileState[i][j].getState());
	            }
	        }
	        for (int i = 0; i <7; i++) {
	            dos.writeInt(mAvailableShares[i]);
	        }
	        int playerCount = getPlayerCount();
	        dos.writeInt(playerCount);
	        for (int i = 0; i <playerCount; i++) {
	            dos.writeUTF(mNames[i]);
	        }
	        for (int i = 0; i <playerCount; i++) {
	            dos.writeInt(mMoney[i]);
	        }
	        for (int i = 0; i <playerCount; i++) {
	            for (int j = 0; j <7; j++) {
	                dos.writeInt(mOwnShares[i][j]);
	            }
	        }
	        if(mRackTiles == null){
	            dos.writeInt(0);
	        } else {
	            dos.writeInt(mRackTiles.length);
	            for (int i = 0; i <mRackTiles.length; i++) {
	                dos.writeInt(mRackTiles[i].getRow());
	                dos.writeInt(mRackTiles[i].getColumn());
	            }
	        }
            dos.writeInt(mCurrentPlayer);
            dos.writeBoolean(mGameStarted);
          }catch (IOException ioe){
        }    
    }
    public  void readAQC(DataInputStream dis){
        super.readAQC(dis);
        int playerCount=0;
       try{
            mTileState = new Tile[9][12];
            for (int i = 0; i <9; i++) {  
	            for (int j = 0; j < 12; j++) {
		            mTileState[i][j] = new Tile(i,j);
		            mTileState[i][j].setState( dis.readInt());
	            }
	        }
	        mAvailableShares = new int[7];
	        for (int i = 0; i <7; i++) {
	            mAvailableShares[i]= dis.readInt();
	        }
	        playerCount = dis.readInt();
	        mNames = new String[playerCount];
	        mMoney = new int[playerCount];
	        mOwnShares = new int[playerCount][7];
	        for (int i = 0; i <playerCount; i++) {
	            mNames[i]= dis.readUTF();
	        }
	        for (int i = 0; i <playerCount; i++) {
	            mMoney[i]= dis.readInt();
	        }
	        for (int i = 0; i <playerCount; i++) {
	            for (int j = 0; j <7; j++) {
	                mOwnShares[i][j]= dis.readInt();
	            }
	        }
	        int tileCount = dis.readInt();
	        if(tileCount != 0){
	            mRackTiles = new Tile[tileCount];
	            for (int i = 0; i <tileCount; i++) {
	                mRackTiles[i]= new Tile(dis.readInt(),dis.readInt());
	            }
	        }
            mCurrentPlayer = dis.readInt();
            mGameStarted = dis.readBoolean();
        }catch (IOException ioe){
        }    
    }
    
    public int getPlayerCount()
    {
        if(mMoney == null){
            return 0;
        }
        return mMoney.length;
    }
    private Tile mTileState[][];
    private int mAvailableShares[];
    private String mNames[];
    private int mMoney[];
    private int mOwnShares[][];
    private Tile mRackTiles[];
    private int mCurrentPlayer;
    private boolean mGameStarted;
}
