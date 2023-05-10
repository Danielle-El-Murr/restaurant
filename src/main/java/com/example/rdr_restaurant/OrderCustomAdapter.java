package com.example.rdr_restaurant;

import android.content.Context;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderCustomAdapter  extends BaseAdapter {
    Context con;
    JSONArray data;
    LayoutInflater inflater;
    String userid;
    int totalPrice;

    public OrderCustomAdapter(Context c, JSONArray data ,String userid){
        this.con = c;
        this.data = data;
        this.userid = userid;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public class Holder {
        TextView fname,fprice, fqty;
        ImageView qadd, qsub, delete;
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

        final OrderCustomAdapter.Holder holder = new OrderCustomAdapter.Holder();
        final View rowView;
        rowView = inflater.inflate(R.layout.order_row,null);

        holder.fname = rowView.findViewById(R.id.foodNameTv);
        holder.fqty = rowView.findViewById(R.id.quantityTv);
        holder.fprice = rowView.findViewById(R.id.foodPriceTv);
        holder.qadd = rowView.findViewById(R.id.addQty);
        holder.qsub = rowView.findViewById(R.id.subQty);
        holder.delete = rowView.findViewById(R.id.deleteButton);


        JSONObject obj = data.optJSONObject(i);
        try {

            holder.fname.setText(obj.getString("name"));
            String quantity = obj.getString("qty");
            holder.fqty.setText(quantity);
            String price = obj.getString("price");
            holder.fprice.setText(price);


            holder.delete.setTag(obj.getInt("idf"));
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = "http://10.0.2.2/restaurant/deleteFromCart.php?uid=" + userid +"&fid=" + holder.delete.getTag();
                    RequestQueue queue = Volley.newRequestQueue(con);
                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            ((orderPage) con).onResume();
                            Toast.makeText(con, "Deleted", Toast.LENGTH_SHORT).show();

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

            holder.qadd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   int qty = Integer.parseInt(holder.fqty.getText().toString());
                   if (qty<10) {
                       String q = String.valueOf(qty + 1);
                       holder.fqty.setText(q);

                       String url  = "http://10.0.2.2/restaurant/updateCart.php?uid=" +  userid +"&fid=" + holder.delete.getTag() +"&qty="+q;
                       RequestQueue queue = Volley.newRequestQueue(con);
                       JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                               new Response.Listener<JSONArray>() {
                                   @Override
                                   public void onResponse(JSONArray response) {

                                   }
                               },
                               new Response.ErrorListener() {
                                   @Override
                                   public void onErrorResponse(VolleyError error) {

                                   }
                               });
                       queue.add(jsonArrayRequest);


                   }

                }

        });
            holder.qsub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int qty = Integer.parseInt(holder.fqty.getText().toString());
                    if (qty>1) {
                        String q = String.valueOf(qty - 1);
                        holder.fqty.setText(q);


                        String url  = "http://10.0.2.2/restaurant/updateCart.php?uid=" +  userid +"&fid=" + holder.delete.getTag() +"&qty="+q;
                        RequestQueue queue = Volley.newRequestQueue(con);
                        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                                new Response.Listener<JSONArray>() {
                                    @Override
                                    public void onResponse(JSONArray response) {

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                });
                        queue.add(jsonArrayRequest);


                    }

                    }



            });



        }catch (JSONException e){
        }
        return rowView;
    }
}


