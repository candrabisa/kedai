package com.candra.kedai.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.icu.util.CurrencyAmount;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.candra.kedai.R;
import com.candra.kedai.adapter.BalanceTransactionAdapter;
import com.candra.kedai.model.BalanceTransactionModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class Topup extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "Hahaha";
    EditText cet_topup;
    EditText btn_50rb, btn_100rb, btn_150rb, btn_200rb, btn_250rb, btn_300rb;
    TextView tv_nominalSaldoKamu, tv_belumPernahTopup;
    Button btn_topup;
    ImageView btn_backSaldoDetail;
    RecyclerView rv_riwayatTransaksiSaldo;
    ProgressBar progressBar;

    SwipeRefreshLayout refreshLayout;
    BalanceTransactionAdapter transactionAdapter;
    List<BalanceTransactionModel> transactionModels = new ArrayList<>();

    FirebaseUser fUser;
    DatabaseReference dRef;

    Integer nominal_topup = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup);

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        btn_50rb = findViewById(R.id.et_50ribu);
        btn_100rb = findViewById(R.id.et_100ribu);
        btn_150rb = findViewById(R.id.et_150ribu);
        btn_200rb = findViewById(R.id.et_200ribu);
        btn_250rb = findViewById(R.id.et_250ribu);
        btn_300rb = findViewById(R.id.et_300ribu);
        btn_topup = findViewById(R.id.btn_topupSaldo);
        btn_backSaldoDetail = findViewById(R.id.btn_backSaldoDetail);

        tv_belumPernahTopup = findViewById(R.id.tv_belumPernahTopup);
        tv_nominalSaldoKamu = findViewById(R.id.tv_nominalSaldoKamu);
        cet_topup = findViewById(R.id.et_nominalTerserah);
        progressBar = findViewById(R.id.pb_riwayatTransaksiTopup);

        rv_riwayatTransaksiSaldo = findViewById(R.id.rv_riwayatTransaksiSaldo);
        rv_riwayatTransaksiSaldo.setHasFixedSize(true);
        rv_riwayatTransaksiSaldo.setLayoutManager(new LinearLayoutManager(this));

        refreshLayout = findViewById(R.id.sw_topup);
        refreshLayout.setOnRefreshListener(this);
        loadDataTransaksi();

        dRef = FirebaseDatabase.getInstance().getReference("Users").child(fUser.getUid());
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final int saldo_kamu = Integer.parseInt(snapshot.child("saldo").getValue().toString());
                tv_nominalSaldoKamu.setText("Rp. "+saldo_kamu);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_topup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    nominal_topup = Integer.parseInt(cet_topup.getText().toString());
                    if (nominal_topup.equals(0)) {
                        Toast.makeText(Topup.this, "Kamu belum memasukkan nominal", Toast.LENGTH_SHORT).show();
                    } else if (nominal_topup <10000){
                        Toast.makeText(Topup.this, "Minimal topup adalah Rp. 10.000", Toast.LENGTH_SHORT).show();
                    } else if (nominal_topup >2000000){
                        Toast.makeText(Topup.this, "Maksimal topup adalah Rp. 2.000.000", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(Topup.this, PaymentMethod.class);
                        intent.putExtra("nominal_topup", nominal_topup );
                        startActivity(intent);
                    }
                } catch (Exception e){
                    Toast.makeText(Topup.this, "Kamu belum memasukkan nominal topup", Toast.LENGTH_SHORT).show();
                }
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

    private void loadDataTransaksi(){
        transactionModels.clear();
        Query query = FirebaseDatabase.getInstance().getReference().child("TransaksiSaldo").child(fUser.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    BalanceTransactionModel balanceTransactionModel = ds.getValue(BalanceTransactionModel.class);
                    transactionModels.add(balanceTransactionModel);

                    Log.d(TAG, "datafromFirebase: " +transactionModels);
                    transactionAdapter = new BalanceTransactionAdapter(Topup.this, transactionModels);
                    transactionAdapter.notifyDataSetChanged();
                    rv_riwayatTransaksiSaldo.setAdapter(transactionAdapter);
                    refreshing(false);
                }

                if (transactionAdapter == null){
                    rv_riwayatTransaksiSaldo.setVisibility(View.GONE);
                    tv_belumPernahTopup.setVisibility(View.VISIBLE);
                } else{

                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void refreshing(boolean b){
        if (b){
            refreshLayout.setRefreshing(true);
        } else {
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        loadDataTransaksi();
    }
}