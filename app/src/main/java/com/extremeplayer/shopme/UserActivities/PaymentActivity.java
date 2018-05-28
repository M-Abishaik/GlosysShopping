package com.extremeplayer.shopme.UserActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dd.processbutton.iml.SubmitProcessButton;
import com.extremeplayer.shopme.App.AppConfig;
import com.extremeplayer.shopme.App.AppController;
import com.extremeplayer.shopme.R;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class PaymentActivity extends AppCompatActivity {

    private TextView productsCost;
    private EditText accNumber,pinNum;
    private Spinner paymentType;
    private int cost, paymentMode = -1;
    private String mail,products,accNum,pinNo;
    private SubmitProcessButton submitProcessButton;
    ShimmerTextView shimmerTextView;
    Shimmer shimmer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_payment);

        Bundle costData = getIntent().getExtras();
        if (costData != null) {
            cost = costData.getInt("cost");
            mail = costData.getString("mail");
            products = costData.getString("productslist");
        }

        productsCost = (TextView)findViewById(R.id.productsCost);
        accNumber = (EditText)findViewById(R.id.accNumber);
        pinNum = (EditText)findViewById(R.id.pinNum);
        paymentType = (Spinner)findViewById(R.id.paymentMode);
        submitProcessButton = (SubmitProcessButton)findViewById(R.id.btn_purchase);
        shimmerTextView = (ShimmerTextView)findViewById(R.id.shimmer_tv);
        shimmer  = new Shimmer();
        shimmer.start(shimmerTextView);

        productsCost.setText(cost + "/-");

        setupSpinner();

        submitProcessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validate()){
                    onSubmitFailed();
                    return;
                }
                sendProducts();
            }
        });
    }

    public void sendProducts() {

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_PRODUCTS_PAYMENT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        displaySuccess();
                        Log.d("TAG", "No error");
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TRY", "Response Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("email", mail);
                param.put("productslist", products);
                param.put("accno",accNum);
                param.put("cost",String.valueOf(cost));
                Log.d("TRIAL", mail + " " + products);
                return param;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq);

    }

    public void displaySuccess() {
        new AlertDialog.Builder(this)
                .setTitle("Success!")
                .setMessage("Your purchase was successful.")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(PaymentActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                })
                .show();
    }

    public void onSubmitFailed() {
        Toast.makeText(PaymentActivity.this, "Transaction failed.", Toast.LENGTH_LONG).show();
        accNumber.setText("");
        pinNum.setText("");
        submitProcessButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        accNum = accNumber.getText().toString();
        pinNo = pinNum.getText().toString();

        if (accNum.isEmpty() || accNum.length() < 9 || accNum.length() > 9) {
                accNumber.setError("enter a valid A/c number");
                valid = false;
            } else {
                accNumber.setError(null);
            }

        if (pinNo.isEmpty() || pinNo.length() < 3 || pinNo.length() > 3) {
                pinNum.setError("enter a valid pin number");
                valid = false;
            } else {
                pinNum.setError(null);
            }

        if(paymentMode==(-1))
            valid = false;

        return valid;
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(PaymentActivity.this,GadgetActivity.class);
        intent.putExtra("mail",mail);
        startActivity(intent);
        finish();
    }

    private void setupSpinner() {
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_payment_options, android.R.layout.simple_spinner_item);

        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        paymentType.setAdapter(genderSpinnerAdapter);

        paymentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.payment_visa))) {
                        paymentMode = 0;
                    } else if (selection.equals(getString(R.string.payment_unionpay))) {
                        paymentMode = 1;
                    } else if(selection.equals(getString(R.string.payment_discover))){
                        paymentMode = 2;
                    } else if(selection.equals(getString(R.string.payment_amexpress))){
                        paymentMode = 3;
                    } else {
                        paymentMode = -1;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                paymentMode = -1;
            }
        });
    }
}
