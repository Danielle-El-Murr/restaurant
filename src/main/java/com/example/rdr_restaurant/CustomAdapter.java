package com.example.rdr_restaurant;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CustomAdapter extends BaseAdapter {

    Context con;
    JSONArray data;
    String uid;
    LayoutInflater inflater;

    public CustomAdapter(Context c, JSONArray data, String uid){
        this.con = c;
        this.data = data;
        this.uid=uid;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public class Holder {
        TextView name,ingredients,price;
        ImageView add;
    }

    @Override
    public int getCount() {
        return data.length();
    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final Holder holder = new Holder();
        final View rowView;
        rowView = inflater.inflate(R.layout.row,null);

        holder.name = rowView.findViewById(R.id.fname);
        holder.ingredients = rowView.findViewById(R.id.ingredientsTv);
        holder.price = rowView.findViewById(R.id.priceTv);
        holder.add = rowView.findViewById(R.id.addButton);

        JSONObject obj = data.optJSONObject(i);
        try {
            String fid = obj.getString("idf");
            holder.name.setText(obj.getString("name"));
            holder.ingredients.setText(obj.getString("ingredients"));
            holder.price.setText(obj.getString("price"));

            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = "http://10.0.2.2/restaurant/addToCart.php?uid="+uid +
                            "&fid=" + fid ;


                    RequestQueue queue = Volley.newRequestQueue(con);
                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.equals("-1")) {
                                ((MenuPage) con).finish();
                            } else {
                                Toast.makeText(con, "Add failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(con,"Error:" + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    queue.add(request);
                }
            });


        }

        catch (JSONException e){
        }
        return rowView;
    }
}

