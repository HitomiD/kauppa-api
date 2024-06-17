package com.grupo_b.kauppa_api.product;

import com.fasterxml.jackson.annotation.JsonAlias;

public class ProductWithCost extends Product {
    @JsonAlias(value = "sale_price")
    private float price;
    private float cost;

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }
}
