package com.project.oneco.data;


public class MyTrash {
    private String date;
    private String type;
    private float amount;
    private String memo;

    public MyTrash(String date, String type, float amount, String memo) {
        this.date = date;
        this.type = type;
        this.amount = amount;
        this.memo = memo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
