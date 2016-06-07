package com.example.coolguy.loktra;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.alorma.github.sdk.services.client.GithubClient;
import com.alorma.github.sdk.services.repos.GithubReposClient;
import com.alorma.github.sdk.services.repos.ReposService;
import com.alorma.github.sdk.services.repos.UserReposClient;
import com.alorma.gitskarios.core.client.BaseListClient;

import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterator;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit.RestAdapter;
import rx.Observable;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    RecyclerView rv;Thread downoading;
    LinearLayoutManager lm;
    adapter ad;Context con;
    ArrayList<content> data = new ArrayList<content>();
    Handler h ;
    SharedPreferences sh ; long timestart = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rv = (RecyclerView) findViewById(R.id.my_recycler_view);
        rv.setHasFixedSize(true);con = this;
        rv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        sh = getApplicationContext().getSharedPreferences("mp", Context.MODE_PRIVATE);


         final Runnable r = new Runnable(){
            @Override
            public void run() {
                ad = new adapter(data);
                rv.setAdapter(ad);
            }
        };



        final Runnable tr = new Runnable(){
            @Override
            public void run() {
                List<GHCommit> l = new ArrayList<GHCommit>();
                ArrayList<content> ac = new ArrayList<content>();
                timestart = System.currentTimeMillis();
                try{

                    GitHub github = GitHub.connectUsingPassword("pavan.bhavirisetty@gmail.com", "bits@2345");
                    GHRepository gr = github.getRepository("rails/rails");
                    //here 13 1
                    PagedIterator<GHCommit> it = gr.listCommits().withPageSize(13).iterator();

                    //here 26 2
                    while(it.hasNext() & l.size() < 26){
                        l.addAll(it.nextPage());
                    }

                    for(GHCommit i : l){
                        ac.add(new content(i.getCommitShortInfo().getCommitter().getName(),i.getSHA1(),i.getCommitShortInfo().getMessage()));
                        System.out.println(i.getSHA1()+"ssssssssssss");
                        System.out.println(i.getCommitShortInfo().getMessage() + "mmmmmmmmmmm");
                        System.out.println(i.getCommitShortInfo().getCommitter().getName()+"nnnnnnnnnn");
                    }
                    // here 24 2
                    if(ac.size() > 24){
                        data = ac ;
                        SharedPreferences.Editor editor = getSharedPreferences("mp", Context.MODE_PRIVATE).edit();
                        editor.putInt("size",ac.size()*3);
                        int kki = 1;
                        for(int i = 1;i < (ac.size()+1);i++){
                            editor.putString(new Integer(kki).toString(),ac.get(i-1).person);
                            ++kki;
                            editor.putString(new Integer(kki).toString(),ac.get(i-1).messageid);
                            ++kki;
                            editor.putString(new Integer(kki).toString(),ac.get(i-1).message);
                            ++kki;
                        }
                        editor.commit();
                    }
                }
                catch(Exception e){e.printStackTrace();}
                h.post(r);
                System.out.println("stopped finally *************");
            }
        };
        downoading = new Thread(tr);

        final Runnable checker = new Runnable() {
            @Override
            public void run() {
                if(hasActiveInternetConnection(con)){downoading.start();}
                else{Toast.makeText(con,"conntect to internet first",Toast.LENGTH_LONG).show();}
            }
        };

        int size = sh.getInt("size",0);
        //here 75 6  25 1
        if(size < 75){new Thread(checker).start();}
        else{
            data.clear();
            Integer ii = new Integer(1);
            while(ii < (size+1)){
                data.add(new content(sh.getString(ii.toString(), "null"), sh.getString((new Integer(ii.intValue()+1)).toString(), "null"), sh.getString((new Integer(ii.intValue()+2)).toString(), "null")));
                ii = (new Integer(ii.intValue()+4));
            }
        }
        lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
       // data.add(new content("hello", "new", "world"));
        ad = new adapter(data);
        rv.setAdapter(ad);
        h = new Handler();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               System.out.println(downoading.isAlive() + " *****");
              //  downoading.stop();
              //  System.out.println(downoading.isAlive() + " *****");
              //  downoading.start();
              //  System.out.println(downoading.isAlive() + " *****");
                if(downoading.isInterrupted()){
                    downoading = new Thread(tr);
                    new Thread(checker).start();
                }
                else if((!downoading.isInterrupted()) & (System.currentTimeMillis()-timestart) > 60000){
                    downoading.interrupt();
                    downoading = new Thread(tr);
                    new Thread(checker).start();}
            }
        });
    }


    public static boolean isNetworkAvailable(Context co) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) co.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public static boolean hasActiveInternetConnection(Context context) {
        if (isNetworkAvailable(context)) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                Log.e("", "Error checking internet connection", e);
            }
        } else {
            Log.d("", "No network available!");
        }
        return false;
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
