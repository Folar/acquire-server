package ackgames.acquire;
import java.io.Serializable;

public class StockTransaction implements Serializable
{
    public StockTransaction() {index =0;}
    private int index;
    private int player;
    private int  survivor;
    private int defunct;
    private String title;
    private String bonusStr;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public int getSurvivor() {
        return survivor;
    }

    public void setSurvivor(int survivor) {
        this.survivor = survivor;
    }

    public int getDefunct() {
        return defunct;
    }

    public void setDefunct(int defunct) {
        this.defunct = defunct;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBonusStr() {
        return bonusStr;
    }

    public void setBonusStr(String bonusStr) {
        this.bonusStr = bonusStr;
    }
}
