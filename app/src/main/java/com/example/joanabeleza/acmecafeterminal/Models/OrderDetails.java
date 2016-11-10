package com.example.joanabeleza.acmecafeterminal.Models;

import java.math.BigDecimal;


public class OrderDetails {
    public int ProductId;
    public int Quantity;
    public BigDecimal Price;

    public OrderDetails(int productId, int quantity, BigDecimal price) {
        ProductId = productId;
        Quantity = quantity;
        Price = price;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public BigDecimal getPrice() {
        return Price;
    }

    public void setPrice(BigDecimal price) {
        Price = price;
    }
}
