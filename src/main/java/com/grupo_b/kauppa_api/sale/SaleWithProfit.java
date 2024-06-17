package com.grupo_b.kauppa_api.sale;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.grupo_b.kauppa_api.product.ProductWithCost;

import java.util.List;

public class SaleWithProfit extends BaseSale {
    private List<ProductWithCost> products;


    public List<ProductWithCost> getProducts() {
        return products;
    }
    public void setProducts(List<ProductWithCost> products) {
        this.products = products;
    }
}
