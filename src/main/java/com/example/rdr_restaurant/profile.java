package com.example.rdr_restaurant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.rdr_restaurant.databinding.ActivityProfileBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class profile extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityProfileBinding binding;
    EditText fnameE, lnameE, usernameE, phoneE, emailE, oldPassE, newPassE;
    Button saveB;
    ProgressBar loadingPB;
    String pass, user;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        loadingPB = findViewById(R.id.idLoadingPB);

        fnameE = findViewById(R.id.fnameEd);
        lnameE = findViewById(R.id.lnameEd);
        usernameE = findViewById(R.id.usernameEd);
        phoneE = findViewById(R.id.phoneEd);
        emailE = findViewById(R.id.emailEd);
        oldPassE = findViewById(R.id.oldPassEd);
        newPassE = findViewById(R.id.newPassEd);
        saveB = findViewById(R.id.saveButoon);

        sharedPreferences = getSharedPreferences("login_data", MODE_PRIVATE);
        editor = sharedPreferences.edit();


        user = sharedPreferences.getString("username", "");
        pass = sharedPreferences.getString("password", "");

        RequestQueue queue = Volley.newRequestQueue(profile.this);

        String url = "http://10.0.2.2/restaurant/getProfile.php?username="+user;

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                loadingPB.setVisibility(View.GONE);

                try {
                    JSONObject r = response.getJSONObject(0);

                    String id = r.getString("id");
                    editor.putString("uid", id);
                    editor.commit();


                    String fname = r.getString("fname");
                    String lname = r.getString("lname");
                    String username = r.getString("username");
                    String phone = r.getString("phone");
                    String email = r.getString("email");

                    fnameE.setText(fname);
                    lnameE.setText(lname);
                    usernameE.setText(username);
                    phoneE.setText(phone);
                    emailE.setText(email);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // below line is use to display a toast message along with our error.
                Toast.makeText(profile.this, "Fail to get data..", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_logout) {

            editor.putString("username", "");
            editor.putString("password", "");
            editor.putBoolean("rememberMe", false);
            editor.commit();

            Intent i = new Intent(profile.this,LoginPage.class);
            startActivity(i);
        } if (id == android.R.id.home){
            Intent i = new Intent(profile.this,MainActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveChanges(View view) {

        String fname = fnameE.getText().toString();
        String lname = lnameE.getText().toString();
        String username = usernameE.getText().toString();
        String phone = phoneE.getText().toString();
        String email = emailE.getText().toString();
        String oldPass = oldPassE.getText().toString();
        String newPass = newPassE.getText().toString();

        if (TextUtils.isEmpty(fname)) {
            fnameE.setError("Please enter First Name");
        } else if (TextUtils.isEmpty(lname)) {
            lnameE.setError("Please enter Last Name");
        } else if (TextUtils.isEmpty(username)) {
            usernameE.setError("Please enter Username");
        } else if (TextUtils.isEmpty(phone)) {
            phoneE.setError("Please enter Phone Number");
        } else if (TextUtils.isEmpty(email)) {
            emailE.setError("Please enter email");
        } else if (TextUtils.isEmpty(newPass)) {
            newPassE.setError("Please enter New Password");
        } else if (oldPass.equals(pass)) {
            String url = "http://10.0.2.2/restaurant/updateProfile.php?fname=" + fname + "&lname=" + lname +
                    "&username=" + username + "&password=" + newPass + "&phone=" + phone + "&email=" + email;

            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest request = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getApplicationContext(), "Account successfully updated!", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void
                        onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error:" + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
            );
            queue.add(request);
        } else {
            oldPassE.setError("Incorrect password");
        }

    }

    }


