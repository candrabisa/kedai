package com.candra.kedai.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.candra.kedai.R;
import com.candra.kedai.adapter.ListOrderAdapter;
import com.candra.kedai.model.ListOrderModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderHistory extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "Asu";
    ImageView btn_back;
    TextView tv_belumPernahBeli;
    RecyclerView rv_listOrder;
    SwipeRefreshLayout swipeRefreshLayout;

    ProgressBar pb_orderHistory;

    ListOrderAdapter listOrderAdapter;
    List<ListOrderModel> listRiwayatPesanan = new ArrayList<>();

    FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        pb_orderHistory = findViewById(R.id.pb_orderHistory);

        btn_back = findViewById(R.id.btn_backRiwayatPesanan);
        tv_belumPernahBeli = findViewById(R.id.tv_belumPernahBeli);
        rv_listOrder = findViewById(R.id.rv_riwayatPesanan);

        rv_listOrder.setHasFixedSize(true);
        rv_listOrder.setLayoutManager(new LinearLayoutManager(this));

        swipeRefreshLayout = findViewById(R.id.refresh_orderHistory);
        swipeRefreshLayout.setOnRefreshListener(this);
        loadDataPesanan();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void loadDataPesanan(){
        tv_belumPernahBeli.setVisibility(View.INVISIBLE);
        listRiwayatPesanan.clear();
        Query query = FirebaseDatabase.getInstance().getReference().child("Pesanan").child(fUser.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    ListOrderModel listOrderModel = ds.getValue(ListOrderModel.class);
                    listRiwayatPesanan.add(listOrderModel);

                    Log.d(TAG, "datafrom Firebase: " + listRiwayatPesanan);
                    listOrderAdapter = new ListOrderAdapter(OrderHistory.this, listRiwayatPesanan);
                    listOrderAdapter.notifyDataSetChanged();
                    rv_listOrder.setAdapter(listOrderAdapter);

                }
                if (listOrderAdapter ==null ){
                    rv_listOrder.setVisibility(View.GONE);
                    tv_belumPernahBeli.setText("Kamu belum pernah belanja");
                    tv_belumPernahBeli.setVisibility(View.VISIBLE);
                } else {
                    tv_belumPernahBeli.setVisibility(View.GONE);
                    rv_listOrder.setVisibility(View.VISIBLE);
                }
                pb_orderHistory.setVisibility(View.GONE);
                refreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void refreshing(boolean b) {
        if (b){
            swipeRefreshLayout.setRefreshing(true);
        } else {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        loadDataPesanan();
    }
}