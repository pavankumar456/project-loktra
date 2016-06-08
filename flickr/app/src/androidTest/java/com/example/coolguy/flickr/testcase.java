package com.example.coolguy.flickr;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by coolguy on 08-06-2016.
 */

public class testcase extends ActivityInstrumentationTestCase2<MainActivity> {

    RecyclerView rv = null;
    EditText et = null ;
    Activity a = null;
    Button b = null;
    public testcase(){
        super(MainActivity.class);
    }

    public void setUp() throws Exception{
        super.setUp();
        a = this.getActivity();
        rv = (RecyclerView) a.findViewById(R.id.my_recycler_view);
        et = (EditText)a.findViewById(R.id.ed);
        b = (Button)a.findViewById(R.id.bbb);
    }

    //checks whether gui is instatiated or not
    public void testguicheck(){
        assertNotNull(rv);
        assertNotNull(et);
    }

    //checks functions regarding internet connectivity
    public void testcodecheck(){
        a.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                et.setText("donald trump");
                b.performClick();
            }
        });
    }

    public  void tearDown() throws Exception{
        super.tearDown();
    }
}