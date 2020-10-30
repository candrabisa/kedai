package com.candra.kedai.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.candra.kedai.R;

public class SuccessRegister extends AppCompatActivity {

    TextView tv1, tv2;
    ImageView iv1;
    Button btn_login;
    Animation animAtas, animBawah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_register);

        animAtas = AnimationUtils.loadAnimation(this, R.anim.fromup);
        animBawah = AnimationUtils.loadAnimation(this, R.anim.fromdown);

        tv1 = findViewById(R.id.tv_suksesReg1);
        tv2 = findViewById(R.id.tv_suksesReg2);
        iv1 = findViewById(R.id.iv_suksesReg);
        btn_login = findViewById(R.id.btn_LoginReg);

        tv1.startAnimation(animAtas);
        tv2.startAnimation(animAtas);
        iv1.startAnimation(animBawah);
        btn_login.startAnimation(animBawah);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SuccessRegister.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}