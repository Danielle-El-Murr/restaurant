package com.example.rdr_restaurant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.rdr_restaurant.databinding.ActivityMenuPageBinding;

import org.json.JSONArray;

public class MenuPage extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMenuPageBinding binding;

    ListView lv;
    String type;
    String uid;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMenuPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        lv = findViewById(R.id.listView);

        sharedPreferences = getSharedPreferences("login_data", MODE_PRIVATE);
        editor = sharedPreferences.edit();


        uid = sharedPreferences.getString("uid", "");


        Intent i = getIntent();
        type = i.getStringExtra("type");

    }


    @Override
    protected void onResume() {
        getMenuItems();
        super.onResume();
    }

    public void getMenuItems(){
        String url  = "http://10.0.2.2/restaurant/getMenu.php?type=" + type;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        CustomAdapter customAdapter = new CustomAdapter(MenuPage.this, response,uid);
                        lv.setAdapter(customAdapter);
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home){
            Intent i = new Intent(MenuPage.this,MainActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

}