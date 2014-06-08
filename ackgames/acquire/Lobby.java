package ackgames.acquire;
import ackgames.acquire.*;
import java.awt.*;
import java.beans.*;

public class Lobby 
{
    Lobby(Acquire a)
    {
        mAquire = a;
        mLobbyFrame = new LobbyFrame(a);
        mLobbyPanel =  a.m_lobbyPanel;
        mLobbyPanel2 = mLobbyFrame.mLobbyPanel; 
    }
    
    boolean isLobbyFull()
    {
        if(mRooms[Preference.MAX_GAMES-1]== null){
            return false;        
        }else{
            return true;
        }
    }
    void setVisible()
    {
        mLobbyFrame.show();
    }
    void processMsg(AQC arg)
    {
        switch(arg.getCode())
        {
            case AQC.LOBBY_UPDATE:
                update(AQCFactory.getAQCLobbyUpdate(arg));
                break;
            case AQC.LOBBY_DELETE:
                delete(AQCFactory.getAQCLobbyDelete(arg));
                break;    
        }
        if(mAquire.m_id == -1){
            if(isLobbyFull()){
                enableCreateButton(false);
            }else{
                enableCreateButton(true);
            }
        }
    }
    
    void update(AQCLobbyUpdate alu)
    {
        AQCRoom r[] = alu.getRooms();
        if(alu.getStateChange()){
            if(alu.getSwitchToLobby()){
                mAquire.showState("lobby");  
            }else{
                mAquire.showState("main");  
            }
        }
        for(int i=0;i<r.length;i++){
            updateRoom(r[i]);    
        }
    }
   void resetLobby()
   {
        mLobbyPanel.resetLobby();
        mLobbyPanel2.resetLobby();
        for (int i=0;i<Preference.MAX_GAMES;i++){
            mRooms[i]=null;
        }
   } 
   void enableCreateButton(boolean b)
   {
        mLobbyPanel.enableCreateButton(b);
        mLobbyPanel2.enableCreateButton(b);
   }
   void enableCreate(boolean b)
   {
        mLobbyPanel.enableCreate(b);
        mLobbyPanel2.enableCreate(b);
   }
   void disableKibitz(int i)
   {
        mLobbyPanel.disableKibitz(i);
        mLobbyPanel2.disableKibitz(i);
   } 
    private void updateRoom(AQCRoom r)
    {
        for (int i=0;i<Preference.MAX_GAMES;i++){
            if(mRooms[i] != null){
                if(r.getGame() == mRooms[i].getGame()){
                    mRooms[i] = r;
                    mLobbyPanel.rooms[i].setRoom(r);
                    mLobbyPanel2.rooms[i].setRoom(r);
                    break;
                }else if(r.getGame() < mRooms[i].getGame()){
                    shiftRoomsDown(i);
                    mRooms[i] = r;
                    mLobbyPanel.rooms[i].setRoom(r);
                    mLobbyPanel2.rooms[i].setRoom(r);
                    break;
                }
            }else {
                mRooms[i] = r;
                mLobbyPanel.rooms[i].setRoom(r);
                mLobbyPanel2.rooms[i].setRoom(r);
                break;
            }
        }    
    }
    
    private void shiftRoomsDown(int index)
    {
        int lim =0;
        for(int i = index+1; i<Preference.MAX_GAMES ; i++){
            if(mRooms[i] == null){
                lim = i;
                break;
            }
        }
        for(int i = lim; i>index; i--){
            mRooms[i] = mRooms[i-1];
            mLobbyPanel.rooms[i].setRoom(mRooms[i]);
            mLobbyPanel2.rooms[i].setRoom(mRooms[i]);
        }
    }
    
    
    void  delete(AQCLobbyDelete ald)
    {
        int id = ald.getGameID();
        int index =0;
        for (int i=0;i<Preference.MAX_GAMES;i++){
            if(mRooms[i].getGame() == id){
                index = i;
                break;
            }
        }
        int last = Preference.MAX_GAMES -1;
        for (int i=index+1;i<Preference.MAX_GAMES;i++){
            if(mRooms[i]==null){
                last = i-1;
                break;
            }else{
                 mRooms[i-1] = mRooms[i];    
                 mLobbyPanel.rooms[i-1].setRoom(mRooms[i-1]);
                 mLobbyPanel2.rooms[i-1].setRoom(mRooms[i-1]);
            }
        }
        mLobbyPanel.rooms[last].removeRoom();
        mLobbyPanel2.rooms[last].removeRoom();
        mRooms[last]=null;
    }
    void populateTextAreas(String text,String name)
    {
        mLobbyPanel.populateTextArea(text,name);
        mLobbyPanel2.populateTextArea(text,name);
    }
    AQCRoom mRooms[] = new AQCRoom[Preference.MAX_GAMES];
    LobbyPanel mLobbyPanel = null;
    LobbyPanel mLobbyPanel2 = null;
    LobbyFrame mLobbyFrame = null;
    private  Acquire mAquire=null;
}