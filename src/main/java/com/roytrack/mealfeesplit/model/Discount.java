package com.roytrack.mealfeesplit.model;

/**
 * Created by ruanchangming on 2015/7/1.
 */
public class Discount {
    private int id;
    private String discountName;
    private double discountNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDiscountName() {
        return discountName;
    }

    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }

    public double getDiscountNum() {
        return discountNum;
    }

    public void setDiscountNum(double discountNum) {
        this.discountNum = discountNum;
    }
}
