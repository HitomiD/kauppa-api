package com.grupo_b.kauppa_api.sale;

import com.grupo_b.kauppa_api.product.Product;

import java.util.List;

public class SaleWithProducts extends BaseSale{
    private List<Product> products;

    public List<Product> getProducts() {
        return products;
    }
    public void setProducts(List<Product> products) {
        this.products = products;
    }

}
