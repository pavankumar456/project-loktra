package com.example.coolguy.loktra.test;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;

import com.example.coolguy.loktra.MainActivity;
import com.example.coolguy.loktra.R;

/**
 * Created by coolguy on 07-06-2016.
 */
public class testcase extends ActivityInstrumentationTestCase2<MainActivity> {

    RecyclerView rv = null;
    FloatingActionButton fab = null;
    Activity a = null;

    public testcase(){
        super(MainActivity.class);
    }

    public void setUp() throws Exception{
        super.setUp();
        a = this.getActivity();
        rv = (RecyclerView) a.findViewById(R.id.my_recycler_view);
        fab = (FloatingActionButton) a.findViewById(R.id.fab);
    }

    //checks whether gui is instatiated or not
    public void testguicheck(){
        assertNotNull(rv);
        assertNotNull(fab);
    }

    //checks functions regarding internet connectivity
    public void testcodecheck(){
        a.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fab.performClick();
            }
        });
    }

    public  void tearDown() throws Exception{
        super.tearDown();
    }
}
