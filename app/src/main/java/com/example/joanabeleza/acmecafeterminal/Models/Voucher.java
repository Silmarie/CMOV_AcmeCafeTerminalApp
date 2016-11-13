package com.example.joanabeleza.acmecafeterminal.Models;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.example.joanabeleza.acmecafeterminal.Utils.TinyDB;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

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

    public boolean aa(Context context, String costumerUuid) {

        boolean res = false;

        try {

            TinyDB tinydb = new TinyDB(context);
            String pkm = tinydb.getString("PublicModulus");
            String pke = tinydb.getString("PublicExp");

            //RSAKeyParameters key = new RSAKeyParameters(false, new BigInteger(pkm), new BigInteger(pke));
            /*KeyFactory kf = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec rsaSpec = new RSAPublicKeySpec(new BigInteger(pkm), new BigInteger(pke));
            RSAPublicKey rsaPub = (RSAPublicKey) kf.generatePublic(rsaSpec);

            RSAPrivateKeySpec rsaSpec2 = new RSAPrivateKeySpec(new BigInteger("333151075106565666361519246408201610097813137602880872903791370534449487027578078285357700671458000105693518763"), new BigInteger("64360983290724749405727988445828167072774343274639893973979761340836360592365636781653932010514851136334964017"));
            RSAPrivateKey rsaPriv = (RSAPrivateKey) kf.generatePrivate(rsaSpec2);

            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initVerify(rsaPub);

            String msg = this.id + costumerUuid + this.type;
            byte[] msgBytes = msg.getBytes();
            signature.initSign(rsaPriv);
            res =  signature.verify(getSignature().getBytes());*/

            /*KeyFactory kf = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec rsaSpec = new RSAPublicKeySpec(new BigInteger(pkm), new BigInteger(pke));
            RSAPublicKey rsaPub = (RSAPublicKey) kf.generatePublic(rsaSpec);


            byte[] byteKey = Base64.decode(pkm.getBytes());
            X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
            PublicKey pub = kf.generatePublic(X509publicKey);
            byte[] publicBytes = Base64.decode(pub);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = keyFactory.generatePublic(keySpec);

            Signature sg = Signature.getInstance("SHA1WithRSA");
            sg.initVerify(pub);                                          // supply the public key

            String msg = this.id + costumerUuid + this.type;
            byte[] msgBytes = msg.getBytes();
            sg.update(msgBytes);

            return sg.verify(getSignature().getBytes());*/

        }catch (Exception e){
            Log.e("ERROR", e.toString());
        }

        Log.e("TT", ""+res);
        return res;
    }

    public  boolean validateVoucher(Context context, String costumerUuid){

        boolean res = false;

        try {
            TinyDB tinydb = new TinyDB(context);
            String pkm = tinydb.getString("PublicModulus");
            String pke = tinydb.getString("PublicExp");

            KeyFactory kf = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec rsaSpec = new RSAPublicKeySpec(new BigInteger(pkm), new BigInteger(pke));
            RSAPublicKey pub = (RSAPublicKey) kf.generatePublic(rsaSpec);

            Signature sg = Signature.getInstance("SHA1WithRSA");

            String msg = this.id + costumerUuid + this.type;
            byte[] input = msg.getBytes();

            sg.initVerify(pub);                                          // supply the public key
            sg.update(input);                                            // supply the data to verify
            res = sg.verify(Base64.decode(this.getSignature(), Base64.DEFAULT));                          // verify the signature (output) using the original data
        }catch (Exception e){
            Log.e("ERROR", e.toString());
        }

        return res;
    }
}
