package com.example.joanabeleza.acmecafeterminal.Models;


import java.io.Serializable;
import java.util.List;

public class CheckoutDetails implements Serializable{

    private String CostumerUuid;
    private String Date;
    private List<OrderDetails> Products;
    private List<VoucherDetails> Vouchers;

    public CheckoutDetails(String costumerUuid, String date, List<OrderDetails> products, List<VoucherDetails> vouchers) {
        CostumerUuid = costumerUuid;
        Date = date;
        Products = products;
        Vouchers = vouchers;
    }

    public String getCostumerUuid() {
        return CostumerUuid;
    }

    public void setCostumerUuid(String costumerUuid) {
        CostumerUuid = costumerUuid;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public List<OrderDetails> getProducts() {
        return Products;
    }

    public void setProducts(List<OrderDetails> products) {
        Products = products;
    }

    public List<VoucherDetails> getVouchers() {
        return Vouchers;
    }

    public void setVouchers(List<VoucherDetails> vouchers) {
        Vouchers = vouchers;
    }
}
