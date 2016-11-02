package com.example.joanabeleza.acmecafeterminal.Models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Checkout implements Serializable {
    private String uuid;
    private List<Product> items = new ArrayList<>();
    private BigDecimal total;


    public Checkout(String uuid, List<Product> items, BigDecimal total) {
        this.items = items;
        this.uuid = uuid;
        this.total = total;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }


    public List<Product> getItems() {
        return items;
    }

    public void setItems(List<Product> items) {
        this.items = items;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
