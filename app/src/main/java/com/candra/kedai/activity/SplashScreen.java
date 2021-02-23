package com.candra.kedai.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.widget.Toast;

import com.candra.kedai.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SplashScreen extends AppCompatActivity {

    String userkey_ = "userkey";
    String userkey = "";
    String userkekey = "";

    String smartLogin_ = "smartlogin";
    String smartlogin = "";
    String smartLog = "";

    FirebaseAuth fAuth;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getUserLocal();


    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void getUserLocal() {
        SharedPreferences preferences = getSharedPreferences(userkey_, MODE_PRIVATE);
        userkekey = preferences.getString(userkey, "");
        if (userkekey.equals("") || userkekey.isEmpty()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreen.this, OnBoard.class));
                    finish();
                }
            }, 2000);
        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences sharedPreferences = getSharedPreferences(smartLogin_, MODE_PRIVATE);
                    smartLog = sharedPreferences.getString(smartlogin, "");
                    if (smartLog.equals("nyala")){
                        fingerPrint();
                    } else {
                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }, 2000);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.P)
    public void fingerPrint(){
        Executor executor = Executors.newSingleThreadExecutor();
        BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(SplashScreen.this)
                .setTitle("Smart Login")
                .setDescription("Kedai mendeteksi kamu sudah pernah login. Secara otomatis smart login akan aktif")
                .setNegativeButton("Batal", executor, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).build();
        SplashScreen aThis = this;
        biometricPrompt.authenticate(new CancellationSignal(), executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                aThis.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SplashScreen.this, "Gagal Memuat...", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                aThis.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SplashScreen.this, "Berhasil Masuk", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SplashScreen.this, MainActivity.class));
                        finish();
                    }
                });
            }

        });

    }
}