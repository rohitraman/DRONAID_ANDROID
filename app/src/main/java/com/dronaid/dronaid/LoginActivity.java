package com.dronaid.dronaid;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.transition.Transition;
import android.support.transition.TransitionInflater;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    FloatingActionButton floatingActionButton2;
    ConstraintLayout constraintLayout;
    ProgressBar progressBar;
    TextInputEditText usernameInput;
    TextInputEditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_login);
        constraintLayout = findViewById(R.id.base_layout);
        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
              finish();
            }
        });



        floatingActionButton2 = findViewById(R.id.floatingActionButton2);





        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG",usernameInput.getText().toString());
                Log.d("TAG",passwordInput.getText().toString());
                progressBar.setVisibility(View.VISIBLE);
                APIClient
                        .getAPIInterface()
                        .signin(usernameInput.getText().toString(),passwordInput.getText().toString())
                        .enqueue(new Callback<SigninResponseModel>() {
                    @Override
                    public void onResponse(Call<SigninResponseModel> call, Response<SigninResponseModel> response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if(response.body().isSuccess()){
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = prefs.edit();
                            String tok = response.body().getToken();
                            Log.d("Token",tok);
                            editor.putString("token",tok);
                            editor.putString("username",usernameInput.getText().toString());

                            editor.commit();


                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();

                        }
                    }

                    @Override
                    public void onFailure(Call<SigninResponseModel> call, Throwable t) {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
    }





}

