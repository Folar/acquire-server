package ackgames.acquire;

import java.io.Serializable;

class AQCGameBoard extends AQCGameInfo implements Serializable
{
    AQCGameBoard()
    {
        super();
    }

    Tile[][] getPlayerTiles()
    {
        return mPlayerTiles;
    }
    void  setPlayerTiles(Tile t[][] )
    {
        mPlayerTiles=t;
    }
    Tile[] getAvailableTiles()
    {
        return mAvailableTiles;
    }
    void  setAvailableTiles(Tile[] t)
    {
        mAvailableTiles = t;
    }
    private Tile mPlayerTiles[][];
    private Tile mAvailableTiles[];
}