package ackgames.acquire;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class AQC implements Serializable
{
    public final static int BROADCAST_MESSAGE = 0;
    public final static int LOBBY_MESSAGE = 1;
    public final static int ERROR_MESSAGE = 2;
    
    public final static int GAMEBOARD_MESSAGES = 100;
    public final static int GAMEBOARD_START =  GAMEBOARD_MESSAGES;
    public final static int GAMEBOARD_PLAY_TILE =  GAMEBOARD_MESSAGES+2;
    public final static int GAMEBOARD_BUY_HOTEL =  GAMEBOARD_MESSAGES+3;
    public final static int GAMEBOARD_MERGE_HOTEL =  GAMEBOARD_MESSAGES+4;
    public final static int GAMEBOARD_SET_MONEY =  GAMEBOARD_MESSAGES+5;
    public final static int GAMEBOARD_SWAP_STOCK =  GAMEBOARD_MESSAGES+7;
    public final static int GAMEBOARD_SWAP_FRAME =  GAMEBOARD_MESSAGES+8;
    public final static int GAMEBOARD_NEXT_TRANSACTION =  GAMEBOARD_MESSAGES+9;
    public final static int GAMEBOARD_START_HOTEL =  GAMEBOARD_MESSAGES+10;
    public final static int GAMEBOARD_SIGN_IN =  GAMEBOARD_MESSAGES+11;
    public final static int GAMEBOARD_ACK =  GAMEBOARD_MESSAGES+12;
    public final static int GAMEBOARD_HIDE_FRAMES =  GAMEBOARD_MESSAGES+13;
    public final static int GAMEBOARD_END_GAME =  GAMEBOARD_MESSAGES+14;
    
    
    
    public final static int CLIENT_MESSAGES = 200;
    public final static int CLIENT_ADD_PLAYER =  CLIENT_MESSAGES;
    public final static int CLIENT_ACK =  CLIENT_MESSAGES+1;
    public final static int CLIENT_START_TILE =  CLIENT_MESSAGES+2; //formery btile
    public final static int CLIENT_REPLACE_TILE =  CLIENT_MESSAGES+3;
    public final static int CLIENT_INIT_TILES =  CLIENT_MESSAGES+4;
    public final static int CLIENT_PING =  CLIENT_MESSAGES+5;
    public final static int CLIENT_INFO =  CLIENT_MESSAGES+6;
    public final static int CLIENT_BUY_STATE =  CLIENT_MESSAGES+7;
    public final static int CLIENT_PLACE_STATE =  CLIENT_MESSAGES+8;
    public final static int CLIENT_ABORT =  CLIENT_MESSAGES+9;
    
    public final static int LOBBY_MESSAGES = 300;
    public final static int LOBBY_FETCH = LOBBY_MESSAGES ;
    public final static int LOBBY_DELETE = LOBBY_MESSAGES+1;
    public final static int LOBBY_UPDATE = LOBBY_MESSAGES+2;
    public final static int LOBBY_CREATE = LOBBY_MESSAGES+3;
    public final static int LOBBY_FILE = LOBBY_MESSAGES+4;
    public final static int LOBBY_TIMEOUT = LOBBY_MESSAGES+5;
    
    public final static int NO_MESSAGE =0;
    public final static int CHAT =1;
    public final static int GAME = 2;
    public final static int LOBBY =3;
    public final static int ERROR =4;
    
    public final static int CURRENT =2;
    public final static int ALL =1;

    public final static int CENTRAL_AMERICA =1;
    
    public AQC()
    {
        setCode(BROADCAST_MESSAGE);
    }
    public AQC(String str)
    {
        setCode(BROADCAST_MESSAGE);
        setMessageType(GAME);
        setMessage(str);
    } 
    public AQC(int id,String str,int mt)
    {
        setCode(BROADCAST_MESSAGE);
        if(mt ==LOBBY){
            setCode(LOBBY_MESSAGE);
        }else if(mt ==ERROR){
            setCode(ERROR_MESSAGE);
        }
        setMessageType(mt);
        setMessage(str);
        setCurrentPlayerID(id);
    } 
    public AQC(int code)
    {
        setCode(code);
    }
    
    public AQC(int code,int id)
    {
        setCode(code);
        setCurrentPlayerID(id);
    }
    
    public AQC(int code,int id,int gid)
    {
        setCode(code);
        setCurrentPlayerID(id);
        setGameID(gid);
    }
   
    public int getCode()
    {
        return mCode;
    }
    public void setCode(int x)
    {
        mCode =x;
    }
    public int getGameID()
    {
        return mGameID;
    }
    public void setGameID(int x)
    {
        mGameID =x;
    }
    public int getCurrentPlayerID()
    {
        return mCurrentPlayerID;
    }
    public void setCurrentPlayerID(int x)
    {
        mCurrentPlayerID =x;
    }
    public void setTurnMergeTileOff(boolean x)
    {
        mTurnMergeTileOff =x;
    }
    public boolean getTurnMergeTileOff()
    {
        return mTurnMergeTileOff;
    }
    
    public void setLostConnection(boolean x)
    {
        mLostConnection =x;
    }
    public boolean getLostConnection()
    {
        return mLostConnection;
    }
    public  void setMessage(String x)
    {
        mMessage =x;
    }
    public  void appendMessage(String x)
    {
        if (mMessage == null || mMessage.length()==0){
            mMessage =x;
        }else{
            mMessage = mMessage+"\n"+x;
        }
    }
    public String getMessage()
    {
        return mMessage;
    }
   
    public int getMessageType()
    {
        return mMessageType;
    }
    public void setMessageType(int x)
    {
        mMessageType =x;
    }
    public int getMessageTarget()
    {
        return mMessageTarget;
    }
    public void setMessageTarget(int x)
    {
        mMessageTarget =x;
    }
    public void setTarget(int x)
    {
        mTarget =x;
    }
    public int getTarget()
    {
        return mTarget;
    }
  
    public void setObserver(boolean x)
    {
        mObserver =x;
    }
    public boolean getObserver()
    {
        return mObserver;
    }
    public String getName()
    {
        return mName;
    }
    public  void setName(String x)
    {
        mName =x;
    }
    public int getVersion()
    {
        return mVersion;
    }
    public void setVersion(int x)
    {
        mVersion =x;
    }
    public void readAQC(DataInputStream dis)
    {
        try {
            mGameID = dis.readInt();
            mCurrentPlayerID = dis.readInt();
            mTurnMergeTileOff = dis.readBoolean();
            mLostConnection = dis.readBoolean();
            mMessage = dis.readUTF();
            mMessageType = dis.readInt();
            mMessageTarget  = dis.readInt(); 
            mTarget  = dis.readInt();
            mObserver= dis.readBoolean();
            mName = dis.readUTF();
            mVersion = dis.readInt();
        }catch (IOException ioe){
        }
    }
     public void writeAQC(DataOutputStream dos)
    {
        try {
            dos.writeInt(mCode);
            dos.writeInt(mGameID);
            dos.writeInt(mCurrentPlayerID);
            dos.writeBoolean(mTurnMergeTileOff);
            dos.writeBoolean(mLostConnection);
            if(mMessage == null){
                mMessage = "";    
            }
            dos.writeUTF(mMessage);
            dos.writeInt(mMessageType );
            dos.writeInt(mMessageTarget); 
            dos.writeInt(mTarget);
            dos.writeBoolean(mObserver);
            if(mName == null){
                mName = "";    
            }
            dos.writeUTF(mName);
            dos.writeInt(mVersion);
            
        }catch (IOException ioe){
        }
    }
    
    private int mCode;    
    private int mGameID;
    private int mCurrentPlayerID;
    private boolean mTurnMergeTileOff;
    private boolean mLostConnection;
    private String mMessage;
    private int mMessageType;
    private int mMessageTarget; 
    private int mTarget;
    private boolean mObserver;
    private String mName;
    private int mVersion; 
    
}