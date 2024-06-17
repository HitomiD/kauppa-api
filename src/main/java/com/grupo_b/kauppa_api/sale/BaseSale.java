package com.grupo_b.kauppa_api.sale;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.grupo_b.kauppa_api.product.Product;

import java.time.LocalDate;

//This class will be used as base for all subsequent Sale variations
//depending on the format of the entities in the POST body.
public abstract class BaseSale {
    private long id;
    private LocalDate date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


}
