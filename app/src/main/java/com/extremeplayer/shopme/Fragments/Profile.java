package com.extremeplayer.shopme.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dd.processbutton.iml.SubmitProcessButton;
import com.extremeplayer.shopme.App.AppConfig;
import com.extremeplayer.shopme.App.AppController;
import com.extremeplayer.shopme.Interface.MailInterface;
import com.extremeplayer.shopme.Interface.MyInterface;
import com.extremeplayer.shopme.R;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Profile extends Fragment implements MyInterface {

    public LinearLayout loginPage, signupPage;
    public ConstraintLayout profilePage;
    String TAG = "TRY";
    MailInterface mailInterface;
    SubmitProcessButton signupButton;
    ScrollView scrollView;
    String[] final_products = {"Bluetooth Speakers", "Home Robot", "Laptop Charger", "Signature 4K LED", "Barcode Scanner", "Sonay AR", "Apple Watch", "Xbox one", "Amazon Echo", "Samsung Galaxy S8"};
    String name, email = "null", phone, products;
    String productsText = "";
    int[] product_list;
    TextView nameView, mobileView, mailView, productView;
    TextView signupName, signupMail, signupPhone, signupPass, signupRepass;
    private EditText emailText;
    private EditText passText;
    private Button loginButton, signoutButton;
    private Button signupLink, backButton;
    ShimmerTextView shimmerTextView;
    Shimmer shimmer;

    public Profile() {
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mailInterface = (MailInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onFragmentChangeListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_signup, container, false);
        email = mailInterface.sendMail();

        if (!email.equals("null")) {
            String pass = mailInterface.getPass();
            submitLogin(email, pass);
        }

        shimmerTextView = (ShimmerTextView)rootView.findViewById(R.id.shimmer_tv);
        shimmer  = new Shimmer();
        shimmer.start(shimmerTextView);

        emailText = (EditText) rootView.findViewById(R.id.input_email);
        passText = (EditText) rootView.findViewById(R.id.input_password);

        nameView = (TextView) rootView.findViewById(R.id.nameInput);
        mailView = (TextView) rootView.findViewById(R.id.mailInput);
        mobileView = (TextView) rootView.findViewById(R.id.mobileInput);
        productView = (TextView) rootView.findViewById(R.id.productsInput);
        loginButton = (Button) rootView.findViewById(R.id.btn_login);

        signupName = (EditText) rootView.findViewById(R.id.signupname);
        signupMail = (EditText) rootView.findViewById(R.id.signupmail);
        signupPhone = (EditText) rootView.findViewById(R.id.signupphone);
        signupPass = (EditText) rootView.findViewById(R.id.signuppass);
        signupRepass = (EditText) rootView.findViewById(R.id.signuprepass);

        scrollView = (ScrollView) rootView.findViewById(R.id.scroll);

        loginPage = (LinearLayout) rootView.findViewById(R.id.login_layout);
        profilePage = (ConstraintLayout) rootView.findViewById(R.id.profile_layout);
        signupPage = (LinearLayout) rootView.findViewById(R.id.signupLayout);

        emailText.setText("");
        passText.setText("");
        emailText.setError(null);
        passText.setError(null);

        product_list = new int[11];

        productsText = "";

        loginButton = (Button) rootView.findViewById(R.id.btn_login);
        signoutButton = (Button) rootView.findViewById(R.id.signoutButton);
        signupLink = (Button) rootView.findViewById(R.id.signup_login);
        signupButton = (SubmitProcessButton) rootView.findViewById(R.id.btn_signup);
        backButton = (Button) rootView.findViewById(R.id.gotologin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!validate(0)) {
                    onLoginFailed();
                    return;
                } else {
                    submitLogin(String.valueOf(emailText.getText()), String.valueOf(passText.getText()));
                }
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                register();
            }
        });

        signoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                logout();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!validate(1)) {
                    onSignupFailed();
                    return;
                }
                signup();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                gotologin();
            }
        });

        return rootView;
    }

    public void signup() {

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        submitLogin(signupMail.getText().toString(), signupPass.getText().toString());
                        Log.d(TAG, "No error");
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Response Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> param = new HashMap<String, String>();
                param.put("name", signupName.getText().toString());
                param.put("email", signupMail.getText().toString());
                param.put("password", signupPass.getText().toString());
                param.put("phone", signupPhone.getText().toString());
                return param;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq);

    }

    public void submitLogin(final String user, final String pass) {

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        fetchDetails(jObj);
                        loginPage.setVisibility(View.GONE);
                        profilePage.setVisibility(View.VISIBLE);
                        scrollView.setVisibility(View.GONE);
                        emailText.setText("");
                        passText.setText("");
                        Log.d(TAG, "No error");
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Response Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> param = new HashMap<String, String>();
                param.put("email", user);
                param.put("password", pass);
                mailInterface.storePass(pass);
                Log.d(TAG, user + pass);
                return param;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq);
    }

    public void fetchDetails(JSONObject jsonObject) {

        try {
            name = jsonObject.getJSONObject("user").getString("name");
            email = jsonObject.getJSONObject("user").getString("email");
            phone = jsonObject.getJSONObject("user").getString("phoneNumber");
            products = jsonObject.getJSONObject("productslist").toString();

            parseProducts(jsonObject);

            Log.d(TAG, name + " " + email + " " + phone + " " + " " + products);

            mailInterface.getMail(email);
            refreshProfile();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void parseProducts(JSONObject jsonObject) {

        try {

            JSONObject object = jsonObject.getJSONObject("productslist");

            Iterator<?> iterator = object.keys();
            int i = 0;

            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                Log.d(TAG, key);
                product_list[i] = Integer.parseInt(object.getString(key));
                Log.d(TAG, object.getString(key));
                i++;
            }

            productsText = "";

            for (int j = 0; j < product_list.length; j++) {
                if (product_list[j] == 1) {
                    productsText = productsText.concat(final_products[j]);
                    productsText = productsText.concat("\n");
                }
            }
            Log.d(TAG, productsText);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void refreshProfile() {
        nameView.setText(name);
        mailView.setText(email);
        mobileView.setText(phone);
        productView.setText(productsText);
    }

    public void logout() {
        nameView.setText("");
        mailView.setText("");
        mobileView.setText("");
        productView.setText("");
        emailText.setText("");
        passText.setText("");
        email = "null";

        loginPage.setVisibility(View.VISIBLE);
        profilePage.setVisibility(View.GONE);
        scrollView.setVisibility(View.GONE);

        mailInterface.getMail(email);
    }

    public void register() {

        loginPage.setVisibility(View.GONE);
        profilePage.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);

        signupName.setText("");
        signupMail.setText("");
        signupPhone.setText("");
        signupPass.setText("");
        signupRepass.setText("");

        signupMail.setError(null);
        signupPass.setError(null);
        signupPhone.setError(null);
        signupRepass.setError(null);
        signupName.setError(null);
    }

    public void gotologin() {
        loginPage.setVisibility(View.VISIBLE);
        profilePage.setVisibility(View.GONE);
        scrollView.setVisibility(View.GONE);
        emailText.setError(null);
        passText.setError(null);
    }

    public void onLoginFailed() {
        Toast.makeText(getActivity(), "Please enter valid credentials.", Toast.LENGTH_SHORT).show();
        emailText.setText("");
        passText.setText("");
        loginButton.setEnabled(true);
    }

    public void onSignupFailed() {
        Toast.makeText(getActivity(), "Sign-up failed.", Toast.LENGTH_LONG).show();
        signupButton.setEnabled(true);
    }

    public boolean validate(int flag) {
        boolean valid = true;

        String email = emailText.getText().toString();
        String password = passText.getText().toString();

        if (flag == 0) {
            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailText.setError("enter a valid email address");
                valid = false;
            } else {
                emailText.setError(null);
            }

            if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
                passText.setError("between 4 and 10 alphanumeric characters");
                valid = false;
            } else {
                passText.setError(null);
            }
        }

        if (flag == 1) {
            String mName = signupName.getText().toString();
            String mMobile = signupPhone.getText().toString();
            String mMail = signupMail.getText().toString();
            String mPass = signupPass.getText().toString();
            String reEnterPassword = signupRepass.getText().toString();

            if (mMail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(mMail).matches()) {
                signupMail.setError("enter a valid email address");
                valid = false;
            } else {
                signupMail.setError(null);
            }

            if (mPass.isEmpty() || mPass.length() < 4 || mPass.length() > 10) {
                signupPass.setError("between 4 and 10 alphanumeric characters");
                valid = false;
            } else {
                signupPass.setError(null);
            }


            if (mName.isEmpty() || mName.length() < 3) {
                signupName.setError("at least 3 characters");
                valid = false;
            } else {
                signupName.setError(null);
            }

            if (mMobile.isEmpty() || mMobile.length() != 10) {
                signupPhone.setError("Enter Valid Mobile Number");
                valid = false;
            } else {
                signupPhone.setError(null);
            }

            if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(mPass))) {
                signupRepass.setError("Password Do not match");
                valid = false;
            } else {
                signupRepass.setError(null);
            }
        }
        return valid;
    }

    @Override
    public void fragmentNowVisible() {
        Log.d("Debug", "Profile visible");
    }
}
