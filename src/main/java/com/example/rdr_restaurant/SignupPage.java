package com.example.rdr_restaurant;

import android.content.Intent;
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

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.rdr_restaurant.databinding.ActivitySignupPageBinding;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class SignupPage extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivitySignupPageBinding binding;
    EditText fname, lname, phonetv, emailtv, passtv, username;
    Button signupButton;
    String first, last, phone,user, pass, email;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignupPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        fname = findViewById(R.id.fnameTv);
        lname = findViewById(R.id.lnameTv);
        phonetv = findViewById(R.id.phoneTv);
        emailtv = findViewById(R.id.emailTv);
        passtv = findViewById(R.id.passTv);
        username = findViewById(R.id.usernameTv);
        signupButton = findViewById(R.id.signupButton);


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                first = fname.getText().toString();
                last = lname.getText().toString();
                phone = phonetv.getText().toString();
                user = username.getText().toString();
                pass = passtv.getText().toString();
                email = emailtv.getText().toString();

                if (TextUtils.isEmpty(first)) {
                    fname.setError("Please enter First Name");
                } else if (TextUtils.isEmpty(last)) {
                    lname.setError("Please enter Last Name");
                } else if (TextUtils.isEmpty(user)) {
                    username.setError("Please enter Username");
                } else if (TextUtils.isEmpty(pass)) {
                    passtv.setError("Please enter Password");
                } else if (TextUtils.isEmpty(phone)) {
                    phonetv.setError("Please enter Phone");
                } else if (TextUtils.isEmpty(email)) {
                    emailtv.setError("Please enter Email");
                } else {
                    url = "http://10.0.2.2/restaurant/addUser.php?fname=" + first + "&lname=" + last +
                            "&username=" + user + "&password=" + pass + "&phone=" + phone + "&email=" + email;
                    saveDataToDb();
                }

            }
        });
    }
        public void saveDataToDb(){

            RequestQueue queue = Volley.newRequestQueue(this);
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                    (Request.Method.GET, url, null, new
                            Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                   // Toast.makeText(getApplicationContext(),"User Added",Toast.LENGTH_LONG).show();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                           // Toast.makeText(getApplicationContext(),"Error:",Toast.LENGTH_LONG).show();
                        }
                    });
            queue.add(jsonArrayRequest);
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home){
            Intent i = new Intent(SignupPage.this,LoginPage.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }


}




