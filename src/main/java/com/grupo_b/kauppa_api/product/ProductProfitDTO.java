package com.grupo_b.kauppa_api.product;

public class ProductProfitDTO extends Product {
    private float totalProfit;
    private int rank;

    public float getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(float totalProfit) {
        this.totalProfit = totalProfit;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
