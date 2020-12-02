package com.candra.kedai.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.candra.kedai.R;

public class SuccessBuy extends AppCompatActivity {

    Button btn_menuUtama, btn_lihatPesanan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_buy);

        btn_lihatPesanan = findViewById(R.id.btn_lihatPesanan);
        btn_menuUtama = findViewById(R.id.btn_menuUtama);

        btn_menuUtama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentUtama = new Intent(SuccessBuy.this, MainActivity.class);
                startActivity(intentUtama);
                finish();
            }
        });

        btn_lihatPesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPesanan = new Intent(SuccessBuy.this, ListOrder.class);
                startActivity(intentPesanan);
            }
        });
    }
}