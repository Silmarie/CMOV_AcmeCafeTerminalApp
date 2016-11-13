package com.example.joanabeleza.acmecafeterminal.Models;

import java.io.Serializable;

public class Voucher implements Serializable {
    int id, type;
    String title, description, signature;

    public Voucher(){}

    public Voucher(int id, int type, String title, String description, String signature) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.description = description;
        this.signature = signature;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public boolean validateVoucher(){
        boolean res = false;



        return res;
    }
}
