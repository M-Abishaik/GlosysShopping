package com.extremeplayer.shopme.UserActivities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.extremeplayer.shopme.App.AppConfig;
import com.extremeplayer.shopme.App.AppController;
import com.extremeplayer.shopme.Product;
import com.extremeplayer.shopme.ProductListAdapter;
import com.extremeplayer.shopme.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class GadgetActivity extends Activity implements
        ProductListAdapter.ProductListAdapterListener{

    private static final String TAG = GadgetActivity.class.getSimpleName();

    private ListView listView;
    private Button btnCheckout;
    private String mail = "null";
    private String category = "null";

    private ProductListAdapter adapter;

    // Progress dialog
    private ProgressDialog pDialog;

    // To store all the products
    private List<Product> productsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_gadget);

        Bundle userData = getIntent().getExtras();
        if (userData != null) {
            mail = userData.getString("mail");
            category = userData.getString("type");
            Log.d("TAG", mail);
        }

        listView = (ListView) findViewById(R.id.list);
        btnCheckout = (Button) findViewById(R.id.btnCheckout);

        productsList = new ArrayList<Product>();
        adapter = new ProductListAdapter(this, productsList, this);

        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        //Fetching products from server
        fetchProducts();
    }

    /**
     * Fetching the products from our server
     * */
    private void fetchProducts() {
        // Showing progress dialog before making request

        pDialog.setMessage("Fetching products...");

        showpDialog();

        // Making json object request
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                AppConfig.URL_PRODUCTS, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    JSONArray products = response
                            .getJSONArray("products");

                    // looping through all product nodes and storing
                    // them in array list
                    for (int i = 0; i < products.length(); i++) {

                        JSONObject product = (JSONObject) products
                                .get(i);

                        String id = product.getString("id");
                        String name = product.getString("name");
                        String description = product
                                .getString("description");
                        String image = product.getString("image");
                        BigDecimal price = new BigDecimal(product
                                .getString("price"));
                        String sku = product.getString("sku");

                        Product p = new Product(id, name, description,
                                image, price, sku);

                        productsList.add(p);
                    }

                    // notifying adapter about data changes, so that the
                    // list renders with new data
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

                // hiding the progress dialog
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onAddToCartPressed(Product product) {

    }
}
