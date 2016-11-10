package com.example.joanabeleza.acmecafeterminal;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.joanabeleza.acmecafeterminal.Models.Checkout;
import com.example.joanabeleza.acmecafeterminal.Models.OrderDetails;
import com.example.joanabeleza.acmecafeterminal.Models.Product;
import com.example.joanabeleza.acmecafeterminal.Models.Voucher;
import com.example.joanabeleza.acmecafeterminal.Models.VoucherDetails;
import com.example.joanabeleza.acmecafeterminal.Utils.AppProperties;
import com.example.joanabeleza.acmecafeterminal.Utils.TinyDB;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private List<Product> checkoutItems = new ArrayList<>();
    RequestQueue requestQueue;

    View mProgressView;
    View mMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue=Volley.newRequestQueue(this);
        mMain = findViewById(R.id.main);
        mProgressView = findViewById(R.id.login_progress);

        updateBlackListFromServer();
    }

    public void qrCodeScanner(View view){

        mScannerView = new ZXingScannerView(this); // Programmatically initialize the scanner view
        setContentView(mScannerView);

         mScannerView.setAutoFocus(true);
        List<BarcodeFormat> formats = new ArrayList<>();
        formats.add(BarcodeFormat.QR_CODE);
        mScannerView.setFormats(formats);

        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera(); // Start camera
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mScannerView != null){
            mScannerView.stopCamera(); // Stop camera on pause
        }
    }

    @Override
    public void handleResult(Result rawResult) {
        //Log.e("handler", rawResult.getText()); // Prints scan results
        //Log.e("handler", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode)

        setContentView(R.layout.activity_main);

        // show the scanner result into dialog box.
        /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setMessage(rawResult.getText());
        AlertDialog alert1 = builder.create();
        alert1.show();*/

        // Get data
        Gson gson = new Gson();
        Checkout checkout = gson.fromJson(rawResult.getText(), Checkout.class);

        // Create order
        postOrder(checkout.getUuid(), checkout);

        //If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);
    }

    /*
        Update blacklist from server
     */
    public void updateBlackListFromServer(){
        String jsonURL = (AppProperties.getInstance()).hostName + "api/costumers/GetBlackListedCustumers/0/";
        JsonArrayRequest arrayreq = new JsonArrayRequest(jsonURL,
                // The second parameter Listener overrides the method onResponse() and passes
                //JSONArray as a parameter
                new Response.Listener<JSONArray>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONArray response) {
                        List<String> costumerBlackList = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject jsonObject = response.getJSONObject(i);
                                String id = jsonObject.getString("Uuid");
                                costumerBlackList.add(id);
                            }

                            // Try and catch are included to handle any errors due to JSON
                            catch (JSONException e) {
                                // If an error occurs, this prints the error to the log
                                e.printStackTrace();
                            }
                        }
                        Toast.makeText(mMain.getContext(), "Costumer blacklist updated from the server.", Toast.LENGTH_SHORT).show();

                        // save the blacklist list to preferences
                        ArrayList<Object> listOfStrings = new ArrayList<>(costumerBlackList.size());
                        listOfStrings.addAll(costumerBlackList);
                        TinyDB tinydb = new TinyDB(getApplicationContext());
                        tinydb.putListObject("CostumerBlackList", listOfStrings);
                    }

                },
                // The final parameter overrides the method onErrorResponse() and passes VolleyError
                //as a parameter
                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                        Toast.makeText(mMain.getContext(), "Cannot update costumer blacklist from the server.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        // Adds the JSON array request "arrayreq" to the request queue
        requestQueue.add(arrayreq);
    }

    /*
        Creates the order
     */
    protected void postOrder(final String uuid, final Checkout order){
        showProgress(true);

        //Verifica se está na blacklist local
        TinyDB tinydb = new TinyDB(getApplicationContext());
        ArrayList<Object> arl = tinydb.getListObject("CostumerBlackList", String.class);
        for (Object e : arl) {
            if(((String) e).matches(uuid)){
                Toast.makeText(mMain.getContext(), "This costumer is blacklisted.", Toast.LENGTH_SHORT).show();
                showProgress(false);
                return;
            }
        }

        // Criar a order
        try {
            String url = (AppProperties.getInstance()).hostName + "api/orders/";
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            // response
                            try {
                                JSONObject obj = new JSONObject(response);
                                String title = obj.getString("Title");
                                String message = obj.getString("Message");
                                Boolean putBlacklist = obj.getBoolean("PutBlacklist");

                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle(title);
                                builder.setMessage(message);
                                AlertDialog alert1 = builder.create();
                                alert1.show();

                                if(putBlacklist){
                                    //Adiciona na blacklist
                                    TinyDB tinydb = new TinyDB(getApplicationContext());
                                    List<String> costumerBlackList = new ArrayList<>();
                                    ArrayList<Object> arl = tinydb.getListObject("CostumerBlackList", String.class);
                                    for (Object e : arl) {
                                        costumerBlackList.add((String) e);
                                    }

                                    costumerBlackList.add(uuid);

                                    ArrayList<Object> listOfStrings = new ArrayList<>(costumerBlackList.size());
                                    listOfStrings.addAll(costumerBlackList);
                                    tinydb.putListObject("CostumerBlackList", listOfStrings);
                                }

                            } catch (Throwable t) {
                                Log.e("App", t.getMessage());
                            }
                            //Log.d("Response", response);
                            showProgress(false);
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.e("Volley", error.toString());
                            Toast.makeText(mMain.getContext(), "Couldn't connect to the server.", Toast.LENGTH_SHORT).show();
                            //TODO fazer a validação offline e guardar a order para posteriormente mandar para o sv
                            showProgress(false);
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Gson gson = new Gson();
                    Map<String, String>  params = new HashMap<>();

                    params.put("CostumerUuid", uuid);
                    params.put("Date", (new Date()).toString());

                    List<OrderDetails> od = new ArrayList<>();
                    for (Product p : order.getItems()) { od.add(new OrderDetails(p.getId(),p.getQuantity(),p.getPrice())); }
                    params.put("Products", gson.toJson(od).replace("\"", ""));

                    List<VoucherDetails> vd = new ArrayList<>();
                    for (Voucher p : order.getVouchers()) { vd.add(new VoucherDetails(p.getId(),p.getType(), "")); } //TODO corrigir signature
                    params.put("Vouchers", gson.toJson(vd).replace("\"", ""));

                    //Log.e("PostParams", params.toString());
                    return params;
                }
            };
            requestQueue.add(postRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the progress UI and hides the main form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mMain.setVisibility(show ? View.GONE : View.VISIBLE);
            mMain.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mMain.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mMain.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}