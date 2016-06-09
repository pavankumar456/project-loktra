package com.example.coolguy.flickr;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

public class MainActivity extends Activity {
    Context context ;
    String FlickrQuery_url = "https://api.flickr.com/services/rest/?method=flickr.photos.search";
    String FlickrQuery_per_page = "&per_page=300";
    String FlickrQuery_nojsoncallback = "&nojsoncallback=1";
    String FlickrQuery_format = "&format=json";
    String FlickrQuery_tag = "&tags=";
    String FlickrQuery_key = "&api_key=";
    String FlickrApiKey = "06cf7a7ea9307d14ddbf695638ffe94f";
    String pageno = "&page=" ;
    String qString =FlickrQuery_url + FlickrQuery_per_page+ FlickrQuery_nojsoncallback+ FlickrQuery_format+ FlickrQuery_tag + "donaldtrump" + FlickrQuery_key + FlickrApiKey+pageno;
    adapter ad;
    RequestQueue queue;
    int stopnow = 1;
    RecyclerView rv ;
    Handler  h;
    GridLayoutManager gm;
    Runnable ru;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        Runnable ru;
        final EditText et = (EditText)findViewById(R.id.ed);
        Button button = (Button)findViewById(R.id.bbb);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et.getText().toString() == null) {
                    Toast.makeText(context, "enter search query", Toast.LENGTH_LONG).show();
                } else {
                    h.post(new Runnable() {
                               @Override
                               public void run() {
                                   try {
                                       startdownloading(URLEncoder.encode(et.getText().toString(), "UTF-8"));
                                   } catch (Exception e) {
                                       e.printStackTrace();
                                   }
                               }
                           }
                    );

                }
            }
        });

        rv = (RecyclerView) findViewById(R.id.my_recycler_view);
        rv.setHasFixedSize(true);
        GridLayoutManager gm = new GridLayoutManager(this,2);
        rv.setLayoutManager(gm);
        ad = new adapter(context);
        rv.setAdapter(ad);
        h = new Handler(Looper.getMainLooper());
    }

    public void startdownloading(String s){
        stopnow = 1;
        qString = FlickrQuery_url + FlickrQuery_per_page+ FlickrQuery_nojsoncallback+ FlickrQuery_format+ FlickrQuery_tag + s + FlickrQuery_key + FlickrApiKey+pageno;
        rv.setHasFixedSize(true);
        gm = new GridLayoutManager(this,2);
        rv.setLayoutManager(gm);
        if(ad!=null){if(ad.queue != null){ad.queue.cancelAll("cancel");}}
        if(queue != null){queue.cancelAll("cancel");}
        ad = new adapter(context);
        rv.setAdapter(ad);
        queue = Volley.newRequestQueue(context);
        queue.add(adding());

    }

    public JsonObjectRequest adding(){
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET,  qString+new Integer(stopnow).toString(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // TODO Auto-generated method stub
                try{
                    ++stopnow;
                   if(response.getJSONObject("photos").getInt("pages") < response.getJSONObject("photos").getInt("page")){
                        queue.add(adding());
                    }

                    JSONArray ja = ParseJSON(response);
                    for(int i = 0;i < ja.length();i++){
                        ad.add(ja.getJSONObject(i));
                    }
                    rv.setAdapter(ad);
                }
                catch (Exception e){e.printStackTrace();}
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub

                Toast.makeText(context,"check your internet connection",Toast.LENGTH_LONG).show();
            }
        });
        jor.setTag("cancel");

        return jor;
    }

    public JSONArray ParseJSON(JSONObject JsonObject){
        JSONArray FlickrPhoto = null ;

        try {
            JSONObject Json_photos = JsonObject.getJSONObject("photos");
            FlickrPhoto = Json_photos.getJSONArray("photo");
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return FlickrPhoto;

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
