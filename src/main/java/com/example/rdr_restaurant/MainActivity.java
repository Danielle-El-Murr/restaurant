package com.example.rdr_restaurant;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.rdr_restaurant.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    ImageButton pizzaB, burgerB, saladB, drinkB;
    Button callB;
    ImageView delivery;
    String type="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        pizzaB = findViewById(R.id.pizzaButton);
        burgerB = findViewById(R.id.burgerButton);
        saladB = findViewById(R.id.saladButton);
        drinkB = findViewById(R.id.drinksButton);
        callB = findViewById(R.id.callButton);
        delivery = findViewById(R.id.deliveryImage);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mainactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_profile) {
            Intent i = new Intent(MainActivity.this,profile.class);
            startActivity(i);
        }
        if (id == R.id.action_order) {
            Intent i = new Intent(MainActivity.this,orderPage.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    public void pizzaClick(View view){
        type="pizza";
        goToMenu();
    }
    public void burgerClick(View view){
        type="burger";
        goToMenu();
    }
    public void saladClick(View view){
        type="salad";
        goToMenu();
    }
    public void drinksClick(View view){
        type="drink";
        goToMenu();
    }

    public void goToMenu(){
        Intent i = new Intent(MainActivity.this , MenuPage.class);
        Toast.makeText(MainActivity.this,type,Toast.LENGTH_LONG).show();
        i.putExtra("type",type);
        startActivity(i);
    }

    public void dialHandler(View v){
        String number = "96155555555";
        number = "tel:" + number;
        Uri u = Uri.parse(number);
        Intent i = new Intent(Intent.ACTION_DIAL, u);
        startActivity(i);
    }


}
