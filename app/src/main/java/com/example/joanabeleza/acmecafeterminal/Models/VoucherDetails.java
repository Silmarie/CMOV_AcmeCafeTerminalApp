package com.example.joanabeleza.acmecafeterminal.Models;

public class VoucherDetails {
    private int Id;
    private int Type;
    private String Signature;

    public VoucherDetails(int id, int type, String signature) {
        Id = id;
        Type = type;
        Signature = signature;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getSignature() {
        return Signature;
    }

    public void setSignature(String signature) {
        Signature = signature;
    }
}
