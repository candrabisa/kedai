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

    Boolean keluar = false;

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
                finish();
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
        if (keluar){
            finish();
        } else {
            Toast.makeText(this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show();
            keluar = true;
            new Handler().postDelayed(() -> keluar = false, 2 * 1000);
        }
    }
}