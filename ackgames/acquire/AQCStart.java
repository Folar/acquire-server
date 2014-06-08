package ackgames.acquire;

import java.io.Serializable;

public class AQCStart  extends AQC implements Serializable
{
    public AQCStart(int id,int gid){
        super(GAMEBOARD_START,id,gid);
    }
    public AQCStart(){
        super(GAMEBOARD_START);
    }
   
}