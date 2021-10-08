package com.candra.kedai.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.candra.kedai.R;
import com.candra.kedai.adapter.CategoryAdapter;
import com.candra.kedai.adapter.LastViewSearchAdapter;
import com.candra.kedai.adapter.SearchAdapter;
import com.candra.kedai.model.CategoryModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {

    EditText et_cariDetail;
    RecyclerView rv_hasilSearch, rv_recentView;
    TextView tv_hasilTidakDitemukan;

    ConstraintLayout constraint_hasilPencarian;
    List<CategoryModel> listProduk = new ArrayList<>();
    SearchAdapter adapter;
    LastViewSearchAdapter adapterLast;

    FirebaseUser fUSer = FirebaseAuth.getInstance().getCurrentUser();

    DatabaseReference dRef = FirebaseDatabase.getInstance().getReference("produk");
    DatabaseReference dRef1 = FirebaseDatabase.getInstance().getReference("terakhirDiCari");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        terakhirDilihat();

        tv_hasilTidakDitemukan = findViewById(R.id.tv_hasilTidakDitemukan);
        constraint_hasilPencarian = findViewById(R.id.constraint_hasilPencarian);

        findViewById(R.id.btn_backSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rv_hasilSearch = findViewById(R.id.rv_hasilSearch);
        rv_hasilSearch.setLayoutManager(new LinearLayoutManager(this));

        rv_recentView = findViewById(R.id.rv_recentView);

        et_cariDetail = findViewById(R.id.et_cariDetail);
        et_cariDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())){
                    cariProduk(s.toString());
                    constraint_hasilPencarian.setVisibility(View.VISIBLE);
                } else {
                    constraint_hasilPencarian.setVisibility(View.GONE);
                }
            }
        });
    }

    private void cariProduk (String s){
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listProduk.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    CategoryModel categoryModel = ds.getValue(CategoryModel.class);
                    if (categoryModel.getNama_produk().toLowerCase().contains(s.toLowerCase())){
                        listProduk.add(categoryModel);
                        tv_hasilTidakDitemukan.setVisibility(View.GONE);
                    }
                    adapter = new SearchAdapter(Search.this, listProduk);
                    rv_hasilSearch.setAdapter(adapter);
                    constraint_hasilPencarian.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void terakhirDilihat(){
        dRef1.child(fUSer.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    CategoryModel categoryModel = ds.getValue(CategoryModel.class);
                    listProduk.add(categoryModel);

                    adapterLast = new LastViewSearchAdapter(Search.this, listProduk);
                    adapterLast.notifyDataSetChanged();
                    rv_recentView.setAdapter(adapterLast);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}