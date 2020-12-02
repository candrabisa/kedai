package com.candra.kedai.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class ProductCheckout extends AppCompatActivity {

    ImageView iv_productCheckout, btn_backCheckout, btn_editAlamatCheckout;
    TextView tv_checkoutProductNama, tv_checkoutProductQty, tv_checkoutProductHarga
            , tv_namaPembeliCheckout, tv_jenisAlamatCheckout, tv_alamatLengkapCheckout, tv_kelurahanCheckout
            , tv_kecamatanCheckout, tv_kabKotaCheckout, tv_provinsiCheckout, tv_nohpCheckout
            , tv_totalHargaCheckout, tv_ongkirCheckout, tv_totalPembayaranCheckout, tv_saldoKamuCheckout;
    Button btn_pakaiVoucher, btn_bayarCheckout;
    EditText et_pakaiVoucher;

    Integer harga_produk = 0;
    Integer saldo_kamu = 0;
    Integer total_pesanan = 0 ;
    Integer total_pembayaran = 0;
    Integer ongkos_kirim = 10000;
    Integer sisa_saldo = 0;

    DatabaseReference dRef, dRef1, dRef2;
    FirebaseUser fUser;

    ProgressDialog progressDialog = new ProgressDialog(this);

    String waktu, tanggal;
    Integer nomor_transaksi = new Random().nextInt();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_checkout);

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        iv_productCheckout = findViewById(R.id.iv_productCheckout);
        et_pakaiVoucher = findViewById(R.id.et_pakaiVoucher);
        tv_checkoutProductNama = findViewById(R.id.tv_checkoutProductNama);
        tv_checkoutProductQty = findViewById(R.id.tv_checkoutProductQty);
        tv_checkoutProductHarga = findViewById(R.id.tv_checkoutProductHarga);
        tv_namaPembeliCheckout = findViewById(R.id.tv_namaCheckout);
        tv_jenisAlamatCheckout = findViewById(R.id.tv_jenisAlamatCheckout);
        tv_alamatLengkapCheckout = findViewById(R.id.tv_alamatLengkapCheckout);
        tv_kelurahanCheckout = findViewById(R.id.tv_kelurahanCheckout);
        tv_kecamatanCheckout = findViewById(R.id.tv_kecamatanCheckout);
        tv_kabKotaCheckout = findViewById(R.id.tv_kabKotaCheckout);
        tv_provinsiCheckout = findViewById(R.id.tv_provinsiCheckout);
        tv_nohpCheckout = findViewById(R.id.tv_nohpCheckout);
        tv_totalHargaCheckout = findViewById(R.id.tv_totalHargaCheckout);
        tv_ongkirCheckout = findViewById(R.id.tv_ongkirCheckout);
        tv_totalPembayaranCheckout = findViewById(R.id.tv_totalPembayaranCheckout);
        tv_saldoKamuCheckout = findViewById(R.id.tv_saldoKamuCheckout);

        btn_backCheckout = findViewById(R.id.btn_backCheckout);
        btn_pakaiVoucher = findViewById(R.id.btn_pakaiVoucher);
        btn_bayarCheckout = findViewById(R.id.btn_bayarCheckout);
        btn_editAlamatCheckout = findViewById(R.id.iv_editAlamatCheckout);

        Bundle bundle = getIntent().getExtras();
        final String nama_kategori = bundle.getString("kategori");
        final String detail_kategori = bundle.getString("produk_detail");
        final String id_produk = bundle.getString("id_produk");
        final Integer qty_pesanan = bundle.getInt("qty_pesanan");
        final Integer saldo_saya = bundle.getInt("saldo_saya");
        final Integer total_harga_pesanan = bundle.getInt("total_harga_pesanan");

        tanggal = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        waktu = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        dRef = FirebaseDatabase.getInstance().getReference("kategori").child(nama_kategori).child(detail_kategori).child(id_produk);
        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                harga_produk = Integer.valueOf(snapshot.child("harga").getValue().toString());
                total_pesanan = harga_produk * qty_pesanan;
                total_pembayaran = total_pesanan + ongkos_kirim;

                tv_checkoutProductQty.setText(qty_pesanan+"x");
                tv_totalHargaCheckout.setText("Rp. " +total_pesanan+"");
                tv_ongkirCheckout.setText("Rp. " +ongkos_kirim.toString());
                tv_totalPembayaranCheckout.setText("Rp. " + total_pembayaran+"");

                tv_checkoutProductNama.setText(snapshot.child("nama_produk").getValue().toString());
                tv_checkoutProductHarga.setText("Rp. " + harga_produk+"");
                Glide.with(ProductCheckout.this).load(snapshot.child("url_images_produk").getValue().toString())
                        .centerCrop().fitCenter().into(iv_productCheckout);


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
                    final String namapembeli = ds.child("nama_lengkap").getValue().toString();
                    final String jenis_alamat = ds.child("jenis_alamat").getValue().toString();
                    final String alamat_lengkap = ds.child("alamat_lengkap").getValue().toString();
                    final String kelurahan = ds.child("kelurahan").getValue().toString();
                    final String kecamatan = ds.child("kecamatan").getValue().toString();
                    final String kab_kota = ds.child("kab_kota").getValue().toString();
                    final String provinsi = ds.child("provinsi").getValue().toString();
                    saldo_kamu = Integer.valueOf(saldo_saya.toString());
//                    total_harga = harga_produk * saldo_kamu;
//                    tv_totalHargaCheckout.setText(total_harga);

                    tv_namaPembeliCheckout.setText(namapembeli);
                    tv_jenisAlamatCheckout.setText(jenis_alamat);
                    tv_alamatLengkapCheckout.setText(alamat_lengkap);
                    tv_kelurahanCheckout.setText(kelurahan);
                    tv_kecamatanCheckout.setText(kecamatan);
                    tv_kabKotaCheckout.setText(kab_kota);
                    tv_provinsiCheckout.setText(provinsi);
                    tv_saldoKamuCheckout.setText("Rp. " + saldo_kamu+"");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_bayarCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Mohon menunggu...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                dRef1 = FirebaseDatabase.getInstance().getReference().child("Pembelian").child(fUser.getUid()).child("INV" + nomor_transaksi);
                dRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        snapshot.getRef().child("invoice").setValue("INV" + nomor_transaksi.toString());
                        snapshot.getRef().child("tgl_transaksi").setValue(tanggal);
                        snapshot.getRef().child("waktu_transaksi").setValue(waktu);
                        snapshot.getRef().child("id_produk").setValue(id_produk);
                        snapshot.getRef().child("nama_produk").setValue(tv_checkoutProductNama.getText().toString());
                        snapshot.getRef().child("harga_produk").setValue(tv_checkoutProductHarga.getText().toString());
                        snapshot.getRef().child("qty").setValue(qty_pesanan);
                        snapshot.getRef().child("nama_pembeli").setValue(tv_namaPembeliCheckout.getText().toString());
                        snapshot.getRef().child("jenis_alamat").setValue(tv_jenisAlamatCheckout.getText().toString());
                        snapshot.getRef().child("alamat_lengkap").setValue(tv_alamatLengkapCheckout.getText().toString());
                        snapshot.getRef().child("kelurahan").setValue(tv_kelurahanCheckout.getText().toString().toLowerCase());
                        snapshot.getRef().child("kecamatan").setValue(tv_kecamatanCheckout.getText().toString().toLowerCase());
                        snapshot.getRef().child("kab_kota").setValue(tv_kabKotaCheckout.getText().toString().toLowerCase());
                        snapshot.getRef().child("provinsi").setValue(tv_provinsiCheckout.getText().toString().toLowerCase());
                        snapshot.getRef().child("total_pembayaran").setValue(total_pembayaran);
                        snapshot.getRef().child("metode_pembayaran").setValue("Saldo Kedai");
                        snapshot.getRef().child("status_pembayaran").setValue("Dibayar");
                        snapshot.getRef().child("status_pesanan").setValue("Diproses");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                dRef2 = FirebaseDatabase.getInstance().getReference().child("Users").child(fUser.getUid());
                dRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        sisa_saldo = saldo_kamu - total_pembayaran;
                        snapshot.getRef().child("saldo").setValue(sisa_saldo);

                        Intent intentSuccess = new Intent(ProductCheckout.this, SuccessBuy.class);
                        startActivity(intentSuccess);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        progressDialog.dismiss();

        btn_editAlamatCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEditAlamat = new Intent(ProductCheckout.this, Address.class);
                startActivity(intentEditAlamat);
            }
        });

        btn_backCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}