package com.project.oneco.data;

public class WaterUsage {

    private float tooth;
    private float hand;
    private float face;
    private float shower;
    private float dish;
    private float etc_water;
    private float waterTotal;

    public float getTooth() {
        return tooth;
    }
    public void setTooth(float tooth) {
        this.tooth = tooth;
    }

    public float getHand() {
        return hand;
    }
    public void setHand(float hand) {
        this.hand = hand;
    }

    public float getFace() {
        return face;
    }
    public void setFace(float face) { this.face = face; }

    public float getShower() {
        return shower;
    }
    public void setShower(float shower) {
        this.shower = shower;
    }

    public float getDish() {
        return dish;
    }
    public void setDish(float dish) {
        this.dish = dish;
    }

    public float getEtcWater() {
        return etc_water;
    }
    public void setEtcWater(float etc_water) {
        this.etc_water = etc_water;
    }

    public float getWaterTotal() { return waterTotal; }
    public void setWaterTotal(float waterTotal) {
        this.waterTotal = waterTotal;
    }
}
