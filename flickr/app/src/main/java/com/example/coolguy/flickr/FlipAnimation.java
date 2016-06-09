package com.example.coolguy.flickr;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by coolguy on 09-06-2016.
 */
public class FlipAnimation extends Animation {

    private Camera camera;
    private LinearLayout fromView;
    private LinearLayout toView;
    private float centerX;
    private float centerY;
    private boolean forward = true;
    private TextView title ;
    private TextView desc ;

    public FlipAnimation(LinearLayout fromView, LinearLayout toView, TextView title,TextView desc,String t,String d){
        this.fromView = fromView;
        this.toView = toView;
        this.title = title;
        this.desc = desc;
        title.setText(t);
        desc.setText(d);
        setDuration(700);
        setFillAfter(false);
        setInterpolator(new AccelerateDecelerateInterpolator());
    }

    public void reverse(){
        forward = false;
        LinearLayout switchView = toView;
        toView = fromView;
        fromView = switchView;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight){
        super.initialize(width, height, parentWidth, parentHeight);
        centerX = width/2;
        centerY = height/2;
        camera = new Camera();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t){

        final double radians = Math.PI * interpolatedTime;
        float degrees = (float) (180.0 * radians / Math.PI);

        if (interpolatedTime >= 0.5f){
            degrees -= 180.f;
            fromView.setVisibility(View.GONE);
            toView.setVisibility(View.VISIBLE);
        }

        if (forward)
            degrees = -degrees;

        final Matrix matrix = t.getMatrix();
        camera.save();
        camera.rotateY(degrees);
        camera.getMatrix(matrix);
        camera.restore();
        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }}