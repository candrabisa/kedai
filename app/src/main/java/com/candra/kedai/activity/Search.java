package com.candra.kedai.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.candra.kedai.R;
import com.candra.kedai.adapter.CategoryAdapter;
import com.candra.kedai.model.CategoryModel;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {

    EditText et_cariDetail;
    RecyclerView rv_hasilSearch;
    ImageView btn_backSearch;

    List<CategoryModel> catModel = new ArrayList<>();
    CategoryAdapter adapter;

    DatabaseReference dRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        btn_backSearch = findViewById(R.id.btn_backSearch);
        btn_backSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter = new CategoryAdapter(this, catModel);

//        dRef = FirebaseDatabase.getInstance().getReference().child("kategori");
//        dRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot ds : snapshot.getChildren()){
//                    CategoryModel categoryModel = ds.getValue(CategoryModel.class);
//                    catModel.add(categoryModel);
//                    adapter = new CategoryAdapter(Search.this, catModel);
//                    adapter.notifyDataSetChanged();
//                    rv_hasilSearch.setAdapter(adapter);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        et_cariDetail = findViewById(R.id.et_cariDetail);
        et_cariDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilterProduk().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}