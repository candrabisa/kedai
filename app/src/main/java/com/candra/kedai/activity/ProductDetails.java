package com.candra.kedai.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.candra.kedai.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProductDetails extends AppCompatActivity {

    ImageView btn_back, iv_produkDetail;
    TextView tv_hargaProduk, tv_namaProduk, tv_descProduk, tv_qty, tv_saldoKamu, tv_totalHarga;
    Button btn_pesan, btn_min, btn_plus;

    DatabaseReference dRef;

    String userkey_ = "userkey";
    String userkey = "";
    String userkekey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        getUserLocal();

        btn_back = findViewById(R.id.btn_backProdukDetail);
        btn_pesan = findViewById(R.id.btn_pesanSekarang);
        btn_min = findViewById(R.id.btn_min);
        btn_plus = findViewById(R.id.btn_tambah);
        iv_produkDetail = findViewById(R.id.iv_produkDetail);

        tv_hargaProduk = findViewById(R.id.tv_satuanHarga);
        tv_namaProduk = findViewById(R.id.tv_namaProduk);
        tv_descProduk = findViewById(R.id.desc_produk);
        tv_qty = findViewById(R.id.tv_qty);
        tv_saldoKamu = findViewById(R.id.tv_saldoKamu);
        tv_totalHarga = findViewById(R.id.tv_totalHarga);

        String bundle1 = getIntent().getStringExtra("id_produk");
        Bundle bundle = getIntent().getExtras();
        final String id_produk = bundle.getString("id_produk");
        final String nama_kategori = bundle.getString("kategori_detail");
        final String produk_detail = bundle.getString("produk_detail");

        dRef = FirebaseDatabase.getInstance().getReference().child("kategori").child(nama_kategori).child(produk_detail).child(id_produk);
        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tv_namaProduk.setText(snapshot.child("nama_produk").getValue().toString());
                tv_descProduk.setText(snapshot.child("desc").getValue().toString());
                tv_hargaProduk.setText(snapshot.child("harga").getValue().toString());
                Glide.with(ProductDetails.this).load("url_images_produk")
                        .centerCrop().fitCenter().into(iv_produkDetail);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getUserLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(userkey_, MODE_PRIVATE);
        userkekey = sharedPreferences.getString(userkey, "");
    }
}