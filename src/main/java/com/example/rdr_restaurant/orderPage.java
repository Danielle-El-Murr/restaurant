package com.example.rdr_restaurant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.rdr_restaurant.databinding.ActivityOrderPageBinding;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class orderPage extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityOrderPageBinding binding;

    ListView lv;
    String uid;
    TextView totalPrice;
    Button confirmB;
    ImageView refreshB;


    String dateTime;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityOrderPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setSupportActionBar(binding.toolbar);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        lv = findViewById(R.id.orderListView);
        totalPrice = findViewById(R.id.totalPriceTv);
        confirmB = findViewById(R.id.confirmButton);
        refreshB= findViewById(R.id.refreshButton);

        sharedPreferences = getSharedPreferences("login_data", MODE_PRIVATE);
        editor = sharedPreferences.edit();


        uid = sharedPreferences.getString("uid", "");


        String url = "http://10.0.2.2/restaurant/getTotal.php?uid="+uid;
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        totalPrice.setText(response);
                    }



                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) ;
        queue.add(request);



    }




    @Override
    protected void onResume() {
        getCartItems();
        super.onResume();
    }

    public void getCartItems(){
        String url  = "http://10.0.2.2/restaurant/getCart.php?uid=" + uid;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        OrderCustomAdapter orderCustomAdapter = new OrderCustomAdapter(orderPage.this, response, uid);
                        lv.setAdapter(orderCustomAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(),"Error: " + error.toString(),Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(jsonArrayRequest);
    }

    public void refreshHandler(View view){

        String url = "http://10.0.2.2/restaurant/getTotal.php?uid="+uid;
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        totalPrice.setText(response);
                    }



                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) ;
        queue.add(request);
    }

    public void confirmHandler(View view){
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD hh:mm:ss");
        dateTime = simpleDateFormat.format(calendar.getTime());

        String tp = totalPrice.getText().toString();

        String url = "http://10.0.2.2/restaurant/addOrder.php?userId="+uid+"&totalPrice="+tp+"&dateTime="+dateTime;
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }



                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) ;
        queue.add(request);
        String u = "http://10.0.2.2/restaurant/deleteCart.php?uid="+uid;
        RequestQueue q = Volley.newRequestQueue(getApplicationContext());
        StringRequest r = new StringRequest(Request.Method.GET, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }



                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) ;
        q.add(r);

        getCartItems();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home){
            Intent i = new Intent(orderPage.this,MainActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}