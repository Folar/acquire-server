package ackgames.acquire;

public class AboutNeighbors
    {
        private int neighors[]=new int[4];
        private int hotels[]=new int[7];
        private int type;
        private int grower;

    public int[] getNeighors() {
        return neighors;
    }

    public int[] getHotels() {
        return hotels;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getGrower() {
        return grower;
    }

    public void setGrower(int grower) {
        this.grower = grower;
    }
}
