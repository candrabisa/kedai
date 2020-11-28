package com.candra.kedai.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.candra.kedai.R;

public class GetStarted extends AppCompatActivity {

    Button btnLogin, btnRegister;

    private Boolean keluar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        btnLogin = findViewById(R.id.btnLog_Splash);
        btnRegister = findViewById(R.id.btnReg_Splash);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GetStarted.this, Login.class));
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentReg = new Intent(GetStarted.this, RegisterOne.class);
                startActivity(intentReg);
            }
        });
    }
    @Override
    public void onBackPressed() {
        if (keluar) {
            finish(); // finish activity
            System.exit(0);
        } else {
            Toast.makeText(this, "Tekan back sekali lagi",
                    Toast.LENGTH_SHORT).show();
            keluar = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    keluar = true;
                }
            }, 3 * 1000);
        }
    }
}