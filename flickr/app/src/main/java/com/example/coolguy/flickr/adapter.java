package com.example.coolguy.flickr;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by coolguy on 02-06-2016.
 */
public class adapter extends RecyclerView.Adapter<adapter.ViewHolder> {
    public ArrayList<JSONObject> data = new ArrayList<JSONObject>();
    Context co;
    RequestQueue queue;




    public class ViewHolder extends RecyclerView.ViewHolder  {

        LinearLayout root;
       LinearLayout front ;
        LinearLayout back ;
        ImageView iv ;
        TextView title ;
        boolean b ;
        FlipAnimation flipAnimation ;
        TextView location ;
        String tit = "not known";
        String tt = "not known";
        public ViewHolder(View v) {
            super(v);
            root = (LinearLayout)v.findViewById(R.id.root);
            front = (LinearLayout)v.findViewById(R.id.imageviewlayout);
            back = (LinearLayout)v.findViewById(R.id.detailslayout);
            title = (TextView)v.findViewById(R.id.title);
            b = true ;
            location = (TextView)v.findViewById(R.id.location);
            iv = (ImageView)v.findViewById(R.id.textView);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                  //  System.err.println(tit+"4444444444"+tt);
                    flipAnimation = new FlipAnimation(front, back,title,location,tt,tit);

                    if (front.getVisibility() == View.GONE)
                    {
                        flipAnimation.reverse();
                    }
                    root.startAnimation(flipAnimation);

                }
            });
            }

    }

    public adapter(Context c) {
        queue= Volley.newRequestQueue(c);
    }

    public void add(JSONObject j){
        data.add(j);
        this.notifyDataSetChanged();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell, parent, false);

        co = parent.getContext();
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public int getItemCount() {
        return data.size();
    }



    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try{

          JSONObject response = data.get(position);
         String urlphoto = "https://farm"+response.get("farm").toString()+".staticflickr.com/"+response.get("server").toString()+"/"+response.get("id").toString()+"_"+response.get("secret")+"_q.jpg";

            StringRequest sr =new StringRequest(Request.Method.GET, "https://api.flickr.com/services/rest/?method=flickr.photos.getInfo&photo_id=" + response.get("id").toString() + "&api_key=06cf7a7ea9307d14ddbf695638ffe94f",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                                factory.setNamespaceAware(true);
                                XmlPullParser xpp = factory.newPullParser();

                                xpp.setInput(new StringReader(response));
                                int eventType = xpp.getEventType();
                                while (eventType != XmlPullParser.END_DOCUMENT) {
                                    if (eventType == XmlPullParser.START_TAG) {
                                        if (xpp.getName().equals("owner")) {
                                            holder.tit = xpp.getAttributeValue(xpp.getNamespace(), "location");

                                        } else if (xpp.getName().equals("title")) {
                                            holder.tt = xpp.nextText();

                                            break;
                                        }
                                    }
                                    eventType = xpp.next();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }
            );
            sr.setTag("cancel");
            queue.add(sr);
            holder.location.setText(holder.tit);
            holder.title.setText(holder.tt);
           // System.err.println(holder.iv.getWidth() + "***********"+position);
            Picasso.with(co).load(urlphoto).fit().into(holder.iv);
    }
    catch (Exception e){e.printStackTrace();}}
}


