package com.example.rdr_restaurant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.rdr_restaurant.databinding.ActivityLoginPageBinding;

public class LoginPage extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityLoginPageBinding binding;
    EditText username, password;
    Button loginB;
    String u,p;
    CheckBox rememberMe;
    StringRequest request;
    RequestQueue rq;
    String url;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        username= findViewById(R.id.usernameTv);
        password= findViewById(R.id.passwordTv);
        loginB= findViewById(R.id.loginButton);
        rememberMe = findViewById(R.id.rememberMe);

        sharedPreferences = getSharedPreferences("login_data", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        String user = sharedPreferences.getString("username", "");
        String pass = sharedPreferences.getString("password", "");
        username.setText(user);


        boolean rm = sharedPreferences.getBoolean("rememberMe", false);
        if (rm){
            Intent i = new Intent(LoginPage.this, MainActivity.class);
            startActivity(i);
        }



        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                u = username.getText().toString();
                p = password.getText().toString();

                if (TextUtils.isEmpty(u)) {
                    username.setError("Please enter Name");
                } else if (TextUtils.isEmpty(p)) {
                    password.setError("Please enter Password");
                } else {

                    url = "http://10.0.2.2/restaurant/getUser.php?username="+u;
                    rq = Volley.newRequestQueue(getApplicationContext());
                    request = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.equals(p)) {

                                        String user = username.getText().toString();
                                        String pass = password.getText().toString();

                                        // saving data to the file
                                        if (rememberMe.isChecked()){
                                            editor.putString("username", user);
                                            editor.putString("password", pass);
                                            editor.putBoolean("rememberMe", true);
                                            editor.commit();
                                        }
                                        else {
                                            editor.putString("username", user);
                                            editor.putString("password", pass);
                                            editor.putBoolean("rememberMe", false);
                                            editor.commit();
                                        }

                                        Intent i = new Intent(LoginPage.this, MainActivity.class);
                                        startActivity(i);
                                    }
                                }



                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(LoginPage.this,"error",Toast.LENGTH_LONG).show();
                        }
                }) ;
                rq.add(request);
            }
        }

    });
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signup) {
            Intent i = new Intent(LoginPage.this,SignupPage.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

}