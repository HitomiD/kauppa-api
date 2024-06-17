package com.grupo_b.kauppa_api.sale;

public class TopSalesDTO {
    private String date;
    private String product;
    private Long quantity;
    private int rank;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Long getQuantity() {return quantity;}

    public void setQuantity(Long quantity) {this.quantity = quantity;}

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
