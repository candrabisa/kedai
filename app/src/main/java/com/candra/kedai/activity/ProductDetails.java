package com.candra.kedai.activity;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class ProductDetails extends AppCompatActivity {

    ShimmerFrameLayout shimmerProduk;

    ImageView btn_back, iv_produkDetail, btn_wishlist;
    TextView tv_hargaProduk, tv_namaProduk, tv_descProduk, tv_qty, tv_saldoKamu, tv_totalHarga;
    Button btn_pesan, btn_min, btn_plus;

    Integer saldo_saya = 0;
    Integer harga_produk = 0;
    Integer total_harga = 0;
    Integer qty = 1;
    Integer wishlist = new Random().nextInt();

    FirebaseUser fUser;

    DatabaseReference dRef, dRef1, dRef2, dRef3;
    private static final String TAG = "ProductDetails";

    String url_images_produk;
    String id_produk;
    String kategori;
    String produk_detail;
    String idproduk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        shimmerProduk = findViewById(R.id.shimmer_produkDetail);

        btn_back = findViewById(R.id.btn_backProdukDetail);
        btn_pesan = findViewById(R.id.btn_pesanSekarang);
        btn_min = findViewById(R.id.btn_min);
        btn_plus = findViewById(R.id.btn_tambah);
        btn_wishlist = findViewById(R.id.btn_favProdukDetail);

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
        id_produk = bundle.getString("id_produk");
        kategori = bundle.getString("kategori");
        produk_detail = bundle.getString("detail_kategori");
        Log.d(TAG, "cek nama kategori: "+kategori);

        dRef = FirebaseDatabase.getInstance().getReference().child("kategori").child(kategori).child(produk_detail).child(id_produk);
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                harga_produk = Integer.valueOf(snapshot.child("harga").getValue().toString());
                total_harga = harga_produk * qty;
                tv_totalHarga.setText("Rp. " +total_harga+"");
                url_images_produk = snapshot.child("url_images_produk").getValue().toString();

                if (saldo_saya < total_harga) {
                    btn_pesan.setBackgroundResource(R.drawable.button_false);
                    btn_pesan.setEnabled(false);
                }

                tv_namaProduk.setText(snapshot.child("nama_produk").getValue().toString());
                tv_descProduk.setText(snapshot.child("desc").getValue().toString());
                tv_hargaProduk.setText("Rp. " +harga_produk+"");
                Glide.with(ProductDetails.this).load(url_images_produk)
                        .centerCrop().fitCenter().into(iv_produkDetail);

                shimmerProduk.stopShimmer();
                shimmerProduk.setVisibility(View.GONE);

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

                    if (saldo_saya > total_harga){
                        btn_pesan.setEnabled(true);
                        btn_pesan.setBackgroundResource(R.drawable.button_primary);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dRef1 = FirebaseDatabase.getInstance().getReference("Wishlist").child(fUser.getUid()).child(id_produk);
        dRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    idproduk = snapshot.child("id_produk").getValue().toString();
                    final String detail_kat = snapshot.child("detail_kategori").getValue().toString();
                    final String kategori = snapshot.child("kategori").getValue().toString();
                    final String url_images = snapshot.child("url_images_produk").getValue().toString();
                    final String harga = snapshot.child("harga").getValue().toString();
                    final String nama_produk = snapshot.child("nama_produk").getValue().toString();

                    if (idproduk.equals(id_produk)){
                        btn_wishlist.setImageResource(R.drawable.ic_baseline_favorite_24);
                    } else {

                    }

                } catch (Exception e){

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
                    btn_pesan.setBackgroundResource(R.drawable.button_false);
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

        btn_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idproduk == null){
                    addFavorit();
                } else {
                   removeFavorit();
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void removeFavorit(){
        dRef1.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(ProductDetails.this, "Berhasil dihapus dari Wishlist", Toast.LENGTH_SHORT).show();
                btn_wishlist.setImageResource(R.drawable.ic_baseline_favorite_border_24);
            }
        });
    }

    public void addFavorit(){
//        dRef2 = FirebaseDatabase.getInstance().getReference().child("Wishlist").child(fUser.getUid()).child(id_produk);
        dRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().child("id_produk").setValue(id_produk);
                snapshot.getRef().child("detail_kategori").setValue(produk_detail);
                snapshot.getRef().child("kategori").setValue(kategori);
                snapshot.getRef().child("url_images_produk").setValue(url_images_produk);
                snapshot.getRef().child("harga").setValue(harga_produk);
                snapshot.getRef().child("nama_produk").setValue(tv_namaProduk.getText().toString());

                Toast.makeText(ProductDetails.this, "Berhasil menambahkan ke Wishlist", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}