package com.candra.kedai.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.candra.kedai.R;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;

public class Settings extends AppCompatActivity {

    ConstraintLayout btn_setPassword, btn_setVerifDiri;
    Button btn_logout;
    Switch switch_smartLogin;
    ImageView btn_backSetting;

    String userkey_ = "userkey";
    String userkey = "";
    String userkekey = "";

    String smartLogin_ = "smartlogin";
    String smartlogin = "";
    String smartLog = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getUserLocal();

        btn_logout = findViewById(R.id.btn_logout);
        btn_backSetting = findViewById(R.id.btn_backSetting);
        btn_setPassword = findViewById(R.id.btn_setPassword);
        btn_setVerifDiri = findViewById(R.id.btn_setVerifDiri);
        switch_smartLogin = findViewById(R.id.switch_smartLogin);


        SharedPreferences sharedPreferences = getSharedPreferences(smartLogin_, MODE_PRIVATE);
        smartLog = sharedPreferences.getString(smartlogin, "");
        if (smartLog.equals("nyala")){
            switch_smartLogin.setChecked(true);
        } else {
            switch_smartLogin.setChecked(false);
        }


        switch_smartLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switch_smartLogin.isChecked()){
                    SharedPreferences preferences = getSharedPreferences(smartLogin_, MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(smartlogin, "nyala");
                    editor.apply();
                    Toast.makeText(Settings.this, "Smartlogin aktif", Toast.LENGTH_SHORT).show();

                } else {
                    SharedPreferences preferences = getSharedPreferences(smartLogin_, MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(smartlogin, "");
                    editor.apply();
                    Toast.makeText(Settings.this, "Smartlogin nonaktif", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_setPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, ChangePassword.class));
            }
        });

        btn_setVerifDiri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, PersonalDataVerification.class));
            }
        });

        btn_backSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                builder.setMessage("Apa yakin ingin keluar?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPreferences = getSharedPreferences(userkey_, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(userkey, null);
                        editor.clear().apply();

                        FirebaseAuth.getInstance().signOut();

                        Intent intent = new Intent(Settings.this, GetStarted.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }

    public void getUserLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(userkey_, MODE_PRIVATE);
        userkekey = sharedPreferences.getString(userkey, "");
    }

}