package com.candra.kedai.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.candra.kedai.R;
import com.candra.kedai.adapter.category.CategoryAdapter;
import com.candra.kedai.model.address.Data;
import com.candra.kedai.model.category.CategoryModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Category extends AppCompatActivity {

    TextView tv_namaKategori, tv_cat1, tv_cat2, tv_cat3;
    ImageView iv_headerKategori, btn_backProfile;

    RecyclerView rv1, rv2, rv3;
    CategoryAdapter catAdapter1, catAdapter2, catAdapter3;
    List<CategoryModel>listkesatu = new ArrayList<>();
    List<CategoryModel>listkedua = new ArrayList<>();
    List<CategoryModel>listketiga = new ArrayList<>();

    FirebaseUser fUser;
    DatabaseReference dRef;
    private static final String TAG = "Category";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        btn_backProfile = findViewById(R.id.btn_backProfile);

        tv_namaKategori = findViewById(R.id.tv_namaKategori);
        tv_cat1 = findViewById(R.id.tv_cat1);
        tv_cat2 = findViewById(R.id.tv_cat2);
        tv_cat3 = findViewById(R.id.tv_cat3);

        iv_headerKategori = findViewById(R.id.bg_category);

        rv1 = findViewById(R.id.rv_category1);
        rv1.setHasFixedSize(true);
        rv1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rv2 = findViewById(R.id.rv_category2);
        rv2.setHasFixedSize(true);
        rv2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rv3 = findViewById(R.id.rv_category3);
        rv3.setHasFixedSize(true);
        rv3.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        Bundle bundle = getIntent().getExtras();
        final String nama_kategori = bundle.getString("nama_kategori");
        final String list1 = bundle.getString("list_produk1");
        final String list2 = bundle.getString("list_produk2");
        final String list3 = bundle.getString("list_produk3");

        final String id_produk = bundle.getString("id_produk");

        dRef = FirebaseDatabase.getInstance().getReference().child("kategori").child(nama_kategori);
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //ambil data
                //categorynya
                final String nama_category= dataSnapshot.child("nama_kategori").getValue().toString();
                final String foto_kategori = dataSnapshot.child("url_images_kategori").getValue().toString();
                tv_namaKategori.setText(nama_category);
                try {
                    Picasso.get().load(foto_kategori).centerCrop().fit()
                            .into(iv_headerKategori);
                } catch (Exception e){
                    Toast.makeText(Category.this, "Gagal memuat foto kategori", Toast.LENGTH_SHORT).show();
                }

                //macem-macem nama variannya
                final String varian1 = dataSnapshot.child("varian").child("varian_1").getValue().toString();
                final String varian2 = dataSnapshot.child("varian").child("varian_2").getValue().toString();
                final String varian3 = dataSnapshot.child("varian").child("varian_3").getValue().toString();
                tv_cat1.setText(varian1);
                tv_cat2.setText(varian2);
                tv_cat3.setText(varian3);

                //list variannya
                try {
                    for (DataSnapshot ds : dataSnapshot.child(list1).getChildren()){
                        CategoryModel categoryModel = ds.getValue(CategoryModel.class);
                        listkesatu.add(categoryModel);

                        catAdapter1 = new CategoryAdapter(Category.this, listkesatu);
                        catAdapter1.notifyDataSetChanged();
                        rv1.setAdapter(catAdapter1);
                    }
                } catch (Exception e){

                }

                try {
                    for (DataSnapshot ds2 : dataSnapshot.child(list2).getChildren()) {
                        CategoryModel categoryModel = ds2.getValue(CategoryModel.class);
                        listkedua.add(categoryModel);

                        catAdapter2 = new CategoryAdapter(Category.this, listkedua);
                        catAdapter2.notifyDataSetChanged();
                        rv2.setAdapter(catAdapter2);
                    }
                } catch (Exception e){

                }
                
                try {
                    for (DataSnapshot ds3 : dataSnapshot.child(list3).getChildren()){
                        CategoryModel categoryModel = ds3.getValue(CategoryModel.class);
                        listketiga.add(categoryModel);

                        catAdapter3 = new CategoryAdapter(Category.this, listketiga);
                        catAdapter3.notifyDataSetChanged();
                        rv3.setAdapter(catAdapter3);
                    }   
                } catch (Exception e){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                
            }
        });
        btn_backProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }



}