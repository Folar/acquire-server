package ackgames.acquire;

import java.io.DataInputStream;

public class AQCFactory
{
    public static AQC createObject(int code,DataInputStream dis)
    {
        switch (code)
        {
            case AQC.BROADCAST_MESSAGE:
                return getAQCBroadcast(dis);
            case AQC.LOBBY_MESSAGE:
                AQC aqc = getAQCBroadcast(dis); 
                aqc.setCode(AQC.LOBBY_MESSAGE);
                return aqc;    
            case AQC.ERROR_MESSAGE:
                AQC aqc2 = getAQCBroadcast(dis); 
                aqc2.setCode(AQC.ERROR_MESSAGE);
                return aqc2;        
            case AQC.GAMEBOARD_START:
               return getAQCStart( dis);
            case AQC.GAMEBOARD_PLAY_TILE:
               return getAQCPlayTile(dis);
            case AQC.GAMEBOARD_BUY_HOTEL:
               return getAQCBuyHotel(dis);   
            case AQC.GAMEBOARD_START_HOTEL:
               return getAQCStartHotel(dis);      
            case AQC.GAMEBOARD_SIGN_IN:
               return getAQCSignIn(dis);    
            case AQC.GAMEBOARD_NEXT_TRANSACTION:
               return getAQCNextTransaction(dis);    
            case AQC.GAMEBOARD_MERGE_HOTEL:
               return getAQCMergeHotel(dis);  
            case AQC.GAMEBOARD_SWAP_FRAME:
               return AQCFactory.getAQCSwapFrame(dis);
            case AQC.GAMEBOARD_SWAP_STOCK:
               return AQCFactory.getAQCSwapStock(dis);   
            case AQC.GAMEBOARD_END_GAME:
               return AQCFactory.getAQCBuyHotel(dis);  
            case AQC.GAMEBOARD_ACK:
               return AQCFactory.getAQCACKGameBoard(dis);  
            case AQC.GAMEBOARD_HIDE_FRAMES:
                return getAQCHideFrames(dis);
            case AQC.CLIENT_ABORT:
               return AQCFactory.getAQCGameAbort(dis);      
            case AQC.CLIENT_START_TILE:
                return AQCFactory.getAQCStartTile(dis);
            case AQC.CLIENT_REPLACE_TILE:
                return AQCFactory.getAQCReplaceTile(dis);
            case AQC.CLIENT_PING:
                return getAQCPing(dis);
            case AQC.CLIENT_ACK:
                return getAQCACKClient(dis);
            case AQC.CLIENT_INFO:
                return AQCFactory.getGameInfo(dis); 
            case AQC.CLIENT_INIT_TILES:
                return AQCFactory.getAQCInitTiles(dis); 
            case AQC.CLIENT_ADD_PLAYER:
                return AQCFactory.getAQCAddPlayer(dis);
            case AQC.CLIENT_BUY_STATE:
                return getAQCBuyState(dis);    
            case AQC.CLIENT_PLACE_STATE:    
                return getAQCPlaceState(dis); 
            case AQC.LOBBY_FETCH:    
                return getAQCLobbyFetch(dis);   
            case AQC.LOBBY_DELETE:    
                return getAQCLobbyDelete(dis); 
            case AQC.LOBBY_UPDATE:    
                return getAQCLobbyUpdate(dis);     
            case AQC.LOBBY_CREATE:    
                return getAQCLobbyCreate(dis);       
            case AQC.LOBBY_FILE:    
                return getAQCLobbyFile(dis); 
            case AQC.LOBBY_TIMEOUT:    
                return getAQCTimeout(dis);    
        }
        return null;
        
    }
    public static AQC getAQCBroadcast(DataInputStream dis)
    {
        AQC cmd = new AQC();
        cmd.readAQC(dis);
        return cmd;
    }
    public static AQCStart getAQCStart(DataInputStream dis)
    {
        AQCStart cmd = new AQCStart();
        cmd.readAQC(dis);
        return cmd;
    }
    public static AQCPlayTile getAQCPlayTile(DataInputStream dis)
    {
        AQCPlayTile cmd = new AQCPlayTile();
        cmd.readAQC(dis);
        return cmd;
    }
   
    public static AQCBuyHotel getAQCBuyHotel(DataInputStream dis)
    {
        AQCBuyHotel cmd = new AQCBuyHotel();
        cmd.readAQC(dis);
        return cmd;
    }   
    public static AQCMergeHotel getAQCMergeHotel(DataInputStream dis)
    {
        AQCMergeHotel cmd = new AQCMergeHotel();
        cmd.readAQC(dis);
        return cmd;
    }
    public static AQCSwapStock getAQCSwapStock(DataInputStream dis)
    {
        AQCSwapStock cmd = new AQCSwapStock();
        cmd.readAQC(dis);
        return cmd;
    }
    public static AQCSwapFrame getAQCSwapFrame(DataInputStream dis)
    {
        AQCSwapFrame cmd = new AQCSwapFrame();
        cmd.readAQC(dis);
        return cmd;
    }
    public static AQCBuyState getAQCBuyState(DataInputStream dis)
    {
        AQCBuyState  cmd = new AQCBuyState();
        cmd.readAQC(dis);
        return cmd;
    }
    public static AQCPlaceState getAQCPlaceState(DataInputStream dis)
    {
        AQCPlaceState  cmd = new AQCPlaceState();
        cmd.readAQC(dis);
        return cmd;
    }
    public static AQCNextTransaction getAQCNextTransaction(DataInputStream dis)
    {
        AQCNextTransaction  cmd = new AQCNextTransaction();
        cmd.readAQC(dis);
        return cmd;
    }
    public static AQCStartHotel getAQCStartHotel(DataInputStream dis)
    {
        AQCStartHotel  cmd = new AQCStartHotel();
        cmd.readAQC(dis);
        return cmd;
    }
    public static AQCSignIn getAQCSignIn (DataInputStream dis)
    {
        AQCSignIn cmd = new AQCSignIn();
        cmd.readAQC(dis);
        return cmd;
    }
    public static AQCACKGameBoard getAQCACKGameBoard (DataInputStream dis)
    {
        AQCACKGameBoard cmd = new AQCACKGameBoard();
        cmd.readAQC(dis);
        return cmd;
    }
    public static AQCHideFrames getAQCHideFrames (DataInputStream dis)
    {
        AQCHideFrames  cmd = new AQCHideFrames();
        cmd.readAQC(dis);
        return cmd;
    } 
    public static AQCAddPlayer getAQCAddPlayer (DataInputStream dis)
    {
        AQCAddPlayer  cmd = new AQCAddPlayer();
        cmd.readAQC(dis);
        return cmd;
    } 
    public static AQCACKClient getAQCACKClient (DataInputStream dis)
    {
        AQCACKClient  cmd = new AQCACKClient();
        cmd.readAQC(dis);
        return cmd;
    }
    public static AQCStartTile getAQCStartTile(DataInputStream dis)
    {
        AQCStartTile  cmd = new AQCStartTile();
        cmd.readAQC(dis);
        return cmd;
    }
    public static AQCReplaceTile getAQCReplaceTile(DataInputStream dis)
    {
        AQCReplaceTile  cmd = new AQCReplaceTile();
        cmd.readAQC(dis);
        return cmd;
    }
    public static AQCInitTiles getAQCInitTiles(DataInputStream dis)
    {
        AQCInitTiles  cmd = new AQCInitTiles();
        cmd.readAQC(dis);
        return cmd;
    }
    public static AQCPing getAQCPing(DataInputStream dis)
    {
        AQCPing  cmd = new AQCPing();
        cmd.readAQC(dis);
        return cmd;
    }
    public static AQCGameInfo getGameInfo(DataInputStream dis)
    {
        AQCGameInfo  cmd = new AQCGameInfo();
        cmd.readAQC(dis);
        return cmd;
    }
    public static AQCGameAbort getAQCGameAbort(DataInputStream dis)
    {
        AQCGameAbort  cmd = new AQCGameAbort();
        cmd.readAQC(dis);
        return cmd;
    }
    public static AQCLobbyUpdate getAQCLobbyUpdate(DataInputStream dis)
    {
        AQCLobbyUpdate  cmd = new AQCLobbyUpdate();
        cmd.readAQC(dis);
        return cmd;
    }
    public static AQCLobbyDelete getAQCLobbyDelete(DataInputStream dis)
    {
        AQCLobbyDelete  cmd = new AQCLobbyDelete();
        cmd.readAQC(dis);
        return cmd;
    }
    
    public static AQCLobbyFetch getAQCLobbyFetch(DataInputStream dis)
    {
        AQCLobbyFetch  cmd = new AQCLobbyFetch();
        cmd.readAQC(dis);
        return cmd;
    }
    
    public static AQCLobbyCreate getAQCLobbyCreate(DataInputStream dis)
    {
        AQCLobbyCreate  cmd = new AQCLobbyCreate();
        cmd.readAQC(dis);
        return cmd;
    }
    public static AQCLobbyFile getAQCLobbyFile(DataInputStream dis)
    {
        AQCLobbyFile  cmd = new AQCLobbyFile();
        cmd.readAQC(dis);
        return cmd;
    }
    public static AQCTimeout getAQCTimeout(DataInputStream dis)
    {
        AQCTimeout  cmd = new AQCTimeout();
        cmd.readAQC(dis);
        return cmd;
    }

    public static AQCStart getAQCStart(AQC cmd)
    {
        return (AQCStart)cmd;
    }
    public static AQCPlayTile getAQCPlayTile(AQC cmd)
    {
        return (AQCPlayTile)cmd;
    }
   
    public static AQCBuyHotel getAQCBuyHotel(AQC cmd) // nextplayer2
    {
        return (AQCBuyHotel)cmd;
    }
    public static AQCMergeHotel getAQCMergeHotel(AQC cmd)
    {
        return (AQCMergeHotel)cmd;
    }

    public static AQCSwapStock getAQCSwapStock(AQC cmd)
    {
        return (AQCSwapStock)cmd;
    }
    public static AQCSwapFrame getAQCSwapFrame(AQC cmd)
    {
        return (AQCSwapFrame)cmd;
    }
    public static AQCBuyState getAQCBuyState(AQC cmd)
    {
        return (AQCBuyState)cmd;
    }
    public static AQCPlaceState getAQCPlaceState(AQC cmd)
    {
        return (AQCPlaceState)cmd;
    }
    public static AQCNextTransaction getAQCNextTransaction(AQC cmd)
    {
        return (AQCNextTransaction)cmd;
    }
    public static AQCStartHotel getAQCStartHotel(AQC cmd)
    {
        return (AQCStartHotel)cmd;
    }
    public static AQCSignIn getAQCSignIn (AQC cmd)
    {
        return (AQCSignIn )cmd;
    }

    public static AQCHideFrames getAQCHideFrames (AQC cmd)
    {
        return (AQCHideFrames  )cmd;
    } 
    public static AQCAddPlayer getAQCAddPlayer (AQC cmd)
    {
        return (AQCAddPlayer  )cmd;
    } 
    public static AQCACKClient getAQCACKClient (AQC cmd)
    {
        return (AQCACKClient  )cmd;
    }
    public static AQCStartTile getAQCStartTile(AQC cmd)
    {
        return (AQCStartTile)cmd;
    }
    public static AQCReplaceTile getAQCReplaceTile(AQC cmd)
    {
        return (AQCReplaceTile)cmd;
    }
    public static AQCInitTiles getAQCInitTiles(AQC cmd)
    {
        return (AQCInitTiles)cmd;
    }
    public static AQCPing getAQCPing(AQC cmd)
    {
        return (AQCPing)cmd;
    }
    public static AQCGameInfo getGameInfo(AQC cmd)
    {
        return (AQCGameInfo)cmd;
    }
    public static AQCGameAbort getGameAbort(AQC cmd)
    {
        return (AQCGameAbort)cmd;
    }
    public static AQCLobbyUpdate getAQCLobbyUpdate(AQC cmd)
    {
        return (AQCLobbyUpdate)  cmd ;
    }
    public static AQCLobbyDelete getAQCLobbyDelete(AQC cmd)
    {
        return (AQCLobbyDelete)cmd;
    }

    public static AQCLobbyCreate getAQCLobbyCreate(AQC cmd)
    {
        return (AQCLobbyCreate) cmd;
    }
    public static AQCLobbyFile getAQCLobbyFile(AQC cmd)
    {
        return (AQCLobbyFile) cmd;
    }
    public static AQCTimeout getAQCTimeout(AQC cmd)
    {
        return (AQCTimeout) cmd;
    }
}
