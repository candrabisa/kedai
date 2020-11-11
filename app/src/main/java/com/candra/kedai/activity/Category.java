package com.candra.kedai.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.candra.kedai.R;
import com.candra.kedai.adapter.category.FoodAdapter;
import com.candra.kedai.model.category.FoodModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class Category extends AppCompatActivity {

    TextView tv_namaKategori, tv_cat1, tv_cat2, tv_cat3;
    ImageView iv_headerKategori;

    RecyclerView rv1, rv2, rv3;
    FoodAdapter foodAdapter;
    List<FoodModel>listCategory = new ArrayList<>();

    DatabaseReference dRef, dRef1, dRef2;

    String userkey_ = "userkey";
    String userkey = "";
    String userkekey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        getUserLocal();

        tv_namaKategori = findViewById(R.id.tv_namaKategori);
        tv_cat1 = findViewById(R.id.tv_cat1);
        tv_cat2 = findViewById(R.id.tv_cat2);
        tv_cat3 = findViewById(R.id.tv_cat3);

        iv_headerKategori = findViewById(R.id.bg_category);

        rv1 = findViewById(R.id.rv_category1);
        rv1.setLayoutManager(new LinearLayoutManager(this));
        rv2 = findViewById(R.id.rv_category2);
        rv2.setLayoutManager(new LinearLayoutManager(this));
        rv3 = findViewById(R.id.rv_category3);
        rv3.setLayoutManager(new LinearLayoutManager(this));

        Bundle bundle = getIntent().getExtras();
        final String nama_kategori = bundle.getString("nama_kategori");

        dRef = FirebaseDatabase.getInstance().getReference().child("kategori").child(nama_kategori);
        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //ambil data
                final String nama_category= dataSnapshot.child("nama_kategori").getValue().toString();
                final String foto_kategori = dataSnapshot.child("url_images_kategori").getValue().toString();

                //set data
                tv_namaKategori.setText(nama_category);
                try {
                    Picasso.get().load(foto_kategori).centerCrop().fit()
                            .into(iv_headerKategori);
                } catch (Exception e){
                    Toast.makeText(Category.this, "Gagal memuat foto kategori", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dRef1 = FirebaseDatabase.getInstance().getReference().child("kategori").child(nama_kategori).child("varian");
        dRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //ambil data
                final String varian1 = dataSnapshot.child("varian_1").getValue().toString();
                final String varian2 = dataSnapshot.child("varian_2").getValue().toString();
                final String varian3 = dataSnapshot.child("varian_3").getValue().toString();

                //set data
                tv_cat1.setText(varian1);
                tv_cat2.setText(varian2);
                tv_cat3.setText(varian3);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        dRef2 = FirebaseDatabase.getInstance().getReference().child("kategori").child(nama_kategori).child("")
    }



    public void getUserLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(userkey_, MODE_PRIVATE);
        userkekey = sharedPreferences.getString(userkey, "");
    }

}