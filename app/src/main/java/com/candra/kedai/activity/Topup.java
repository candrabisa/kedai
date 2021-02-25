package com.candra.kedai.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.util.CurrencyAmount;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.candra.kedai.R;

import java.util.Currency;

import me.abhinay.input.CurrencyEditText;
import me.abhinay.input.CurrencySymbols;

public class Topup extends AppCompatActivity {

    CurrencyEditText cet_topup;
    EditText btn_50rb, btn_100rb, btn_150rb, btn_200rb, btn_250rb, btn_300rb;
    Button btn_topup;
    ImageView btn_backSaldoDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup);

        btn_50rb = findViewById(R.id.et_50ribu);
        btn_100rb = findViewById(R.id.et_100ribu);
        btn_150rb = findViewById(R.id.et_150ribu);
        btn_200rb = findViewById(R.id.et_200ribu);
        btn_250rb = findViewById(R.id.et_250ribu);
        btn_300rb = findViewById(R.id.et_300ribu);
        btn_topup = findViewById(R.id.btn_topupSaldo);
        btn_backSaldoDetail = findViewById(R.id.btn_backSaldoDetail);

        cet_topup = findViewById(R.id.et_nominalTerserah);
        cet_topup.setSeparator(".");
        cet_topup.setCurrency(CurrencySymbols.INDONESIA);
        cet_topup.setSpacing(true);
        cet_topup.setDelimiter(true);
        cet_topup.setDecimals(false);

        btn_topup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Topup.this, PaymentMethod.class));
            }
        });

        btn_50rb.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                cet_topup.setText("50000");
            }
        });
        btn_100rb.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                cet_topup.setText("100000");
            }
        });
        btn_150rb.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                cet_topup.setText("150000");
            }
        });
        btn_200rb.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                cet_topup.setText("200000");
            }
        });
        btn_250rb.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                cet_topup.setText("250000");
            }
        });
        btn_300rb.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                cet_topup.setText("300000");
            }
        });

        btn_backSaldoDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}