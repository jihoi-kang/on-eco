package com.project.oneco.data;

public class TrashUsage {

    private float normal_trash;
    private float glass;
    private float can;
    private float paper;
    private float plastic;
    private float plastic_bag;

    private float trashTotal;

    public float getNormalTrash() {
        return normal_trash;
    }

    public void setNormalTrash(float normal_trash) {this.normal_trash = normal_trash;}

    public float getGlass() {
        return glass;
    }

    public void setGlass(float glass) {
        this.glass = glass;
    }

    public float getCan() {
        return can;
    }

    public void setCan(float can) { this.can = can;}

    public float getPaper() {return paper;}

    public void setPaper(float paper) {
        this.paper = paper;
    }

    public float getPlastic() {
        return plastic;
    }

    public void setPlastic(float plastic) {
        this.plastic = plastic;
    }

    public float getPlastic_bag() {
        return plastic_bag;
    }

    public void setPlastic_bag(float plastic_bag) {
        this.plastic_bag = plastic_bag;
    }

    public float getTrashTotal() {

        return trashTotal;
    }
    public void setTrashTotal(float trashTotal) {
        this.trashTotal = trashTotal;
    }
}
