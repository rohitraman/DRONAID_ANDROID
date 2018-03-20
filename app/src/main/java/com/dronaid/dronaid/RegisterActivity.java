package com.dronaid.dronaid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private FloatingActionButton fab;
    private TextInputEditText username;
    private TextInputEditText password;
    private TextInputEditText confpass;
    private RadioButton patient;
    private RadioButton doctor;


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
        setContentView(R.layout.activity_register);

        progressBar = findViewById(R.id.progressBar34);
        progressBar.setVisibility(View.INVISIBLE);
        fab = findViewById(R.id.floatingActionButton34);
        username = findViewById(R.id.register_username);
        password  = findViewById(R.id.register_password);
        confpass = findViewById(R.id.register_confirm_password);
        patient = findViewById(R.id.patient);
        doctor = findViewById(R.id.doctor);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(password.getText().toString().equals(confpass.getText().toString())){
                    progressBar.setVisibility(View.VISIBLE);

                    APIClient.getAPIInterface().register(username.getText().toString(),password.getText().toString(),patient.isChecked()?"patient":"doctor").enqueue(new Callback<RegisterResponseModel>() {
                        @Override
                        public void onResponse(Call<RegisterResponseModel> call, Response<RegisterResponseModel> response) {
                            if(response.body().isSuccess()){
                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editor = prefs.edit();
                                String tok = response.body().getToken();
                                Log.d("Token",tok);
                                editor.putString("token",tok);
                                editor.putString("username",username.getText().toString());

                                editor.commit();
                                progressBar.setVisibility(View.INVISIBLE);



                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<RegisterResponseModel> call, Throwable t) {

                        }
                    });
                }
            }
        });

    }
}
