package com.candra.kedai.activity;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.candra.kedai.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProductDetails extends AppCompatActivity {

    ImageView btn_back, iv_produkDetail;
    TextView tv_hargaProduk, tv_namaProduk, tv_descProduk, tv_qty, tv_saldoKamu, tv_totalHarga;
    Button btn_pesan, btn_min, btn_plus;

    Integer saldo_saya = 0;
    Integer harga_produk = 0;
    Integer total_harga = 0;
    Integer qty = 1;

    FirebaseUser fUser;

    DatabaseReference dRef;
    private static final String TAG = "ProductDetails";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        fUser = FirebaseAuth.getInstance().getCurrentUser();

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

        tv_qty.setText(qty.toString());

        btn_min.animate().alpha(0).setDuration(100).start();
        btn_min.setEnabled(false);

        Bundle bundle = getIntent().getExtras();
        final String id_produk = bundle.getString("id_produk");
        final String kategori = bundle.getString("kategori");
        final String produk_detail = bundle.getString("detail_kategori");
        Log.d(TAG, "cek nama kategori: "+kategori);

        dRef = FirebaseDatabase.getInstance().getReference().child("kategori").child(kategori).child(produk_detail).child(id_produk);
        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                harga_produk = Integer.valueOf(snapshot.child("harga").getValue().toString());
                total_harga = harga_produk * qty;
                tv_totalHarga.setText("Rp. " +total_harga+"");

                tv_namaProduk.setText(snapshot.child("nama_produk").getValue().toString());
                tv_descProduk.setText(snapshot.child("desc").getValue().toString());
                tv_hargaProduk.setText("Rp." +harga_produk+"");
                Glide.with(ProductDetails.this).load(snapshot.child("url_images_produk").getValue().toString())
                        .centerCrop().fitCenter().into(iv_produkDetail);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("Uid").equalTo(fUser.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    saldo_saya = Integer.valueOf(ds.child("saldo").getValue().toString());
                    tv_saldoKamu.setText("Rp. " + saldo_saya+"");

                    if (saldo_saya < total_harga){
                        btn_pesan.setBackgroundResource(R.drawable.bg_input_satu);
                        btn_pesan.setEnabled(false);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qty += 1;
                tv_qty.setText(qty.toString());
                if (qty > 1){
                    btn_min.animate().alpha(1).setDuration(100).start();
                    btn_min.setEnabled(true);
                }
                total_harga = harga_produk * qty;
                tv_totalHarga.setText("Rp. " +total_harga+"");
                if (saldo_saya < total_harga){
                    btn_pesan.setBackgroundResource(R.drawable.bg_input_satu);
                    btn_pesan.setEnabled(false);
                }
            }
        });
        btn_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qty -= 1;
                tv_qty.setText(qty.toString());
                if (qty < 2 ){
                    btn_min.animate().alpha(0).setDuration(100).start();
                    btn_min.setEnabled(false);
                }
                total_harga = harga_produk * qty;
                tv_totalHarga.setText("Rp. " + total_harga+"");
                if (saldo_saya > total_harga){
                    btn_pesan.setBackgroundResource(R.drawable.button_primary);
                    btn_pesan.setEnabled(true);
                } else if (saldo_saya.equals(total_harga)){
                    btn_pesan.setBackgroundResource(R.drawable.button_primary);
                    btn_pesan.setEnabled(true);
                }
            }
        });
        btn_pesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPesan = new Intent(ProductDetails.this, ProductCheckout.class);
                intentPesan.putExtra("kategori", kategori);
                intentPesan.putExtra("produk_detail", produk_detail);
                intentPesan.putExtra("id_produk", id_produk);
                intentPesan.putExtra("qty_pesanan", qty);
                intentPesan.putExtra("total_harga_pesanan", total_harga);
                intentPesan.putExtra("saldo_saya", saldo_saya);
                startActivity(intentPesan);
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}