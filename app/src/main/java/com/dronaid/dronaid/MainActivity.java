package com.dronaid.dronaid;

import android.animation.Animator;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout constraintLayout;
    private FragmentManager fm;
    Toolbar toolbar;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            fm = getSupportFragmentManager();

            switch (item.getItemId()) {
                case R.id.navigation_drones:
                    fm
                            .beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_from_top,R.anim.blank)
                            .replace(R.id.viewpager,DronesFragment.newInstance("",""))
                            .commit();
                    return true;
                case R.id.navigation_doctors:
                    fm
                            .beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_from_top,R.anim.blank)
                            .replace(R.id.viewpager,DoctorsFragment.newInstance("",""))
                            .commit();
                    return true;
                case R.id.navigation_profile:
                    fm.
                            beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_from_top,R.anim.blank)
                            .replace(R.id.viewpager,ProfileFragment.newInstance("",""))
                            .commit();
                    return true;
            }

            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        constraintLayout = findViewById(R.id.container);

        if (savedInstanceState == null) {
            constraintLayout.setVisibility(View.INVISIBLE);

            ViewTreeObserver viewTreeObserver = constraintLayout.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        circularRevealActivity();
                        constraintLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        }
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_doctors);

    }

    private void circularRevealActivity() {

        int cx = (int)constraintLayout.getRight() - convertDpToPixel(28,getApplicationContext());
        int cy = (int) constraintLayout.getBottom() - convertDpToPixel(28,getApplicationContext());

        float finalRadius = Math.max(constraintLayout.getWidth(), constraintLayout.getHeight());

// create the animator for this view (the start radius is zero)
        Animator circularReveal = ViewAnimationUtils.createCircularReveal(constraintLayout, cx, cy, convertDpToPixel(28,getApplicationContext()), finalRadius);
        circularReveal.setDuration(1000);

// make the view visible and start the animation
        constraintLayout.setVisibility(View.VISIBLE);
        circularReveal.start();
    }

    public static int convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int)px;
    }

}
