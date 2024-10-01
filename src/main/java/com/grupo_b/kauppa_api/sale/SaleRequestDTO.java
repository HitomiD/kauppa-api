package com.grupo_b.kauppa_api.sale;

//This class is only used to handle the sales on the POST requests for the web reports controller.
public class SaleRequestDTO extends BaseSale{
    private String products;

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }
}
