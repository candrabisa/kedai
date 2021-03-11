package com.candra.kedai.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.database.ValueEventListener;

public class OrderDetails extends AppCompatActivity {

    TextView tv_statusPesanan, tv_waktuPemesanan, tv_invoicePemesanan, tv_namaPenerima, tv_jenisAlamatPenerima
            , tv_alamatLengkapPenerima, tv_kelPenerima, tv_kecPenerima, tv_kabKotaPenerima, tv_provinsiPenerima
            , tv_namaProduk, tv_qty, tv_hargaProduk, tv_totalBelanja, tv_ongkir, tv_potonganVoucher
            , tv_totalPembayaran, tv_statusPembayaran, tv_metodePembayaran, tv_noHpPenerima;

    ImageView iv_fotoProduk, btn_backOrderDetail;
    Button btn_komplain, btn_selesai, btn_beriUlasan;

    AlertDialog.Builder alertDialog;

    FirebaseUser fUser;
    DatabaseReference dRef, dRef1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        tv_statusPesanan = findViewById(R.id.tv_statusPesananOrderDetail);
        tv_waktuPemesanan = findViewById(R.id.tv_waktuPemesananOrderDetail);
        tv_invoicePemesanan = findViewById(R.id.tv_invOrderDetail);
        tv_namaPenerima = findViewById(R.id.tv_namaPenerimaOrderDetail);
        tv_jenisAlamatPenerima = findViewById(R.id.tv_jenisAlamatOrderDetail);
        tv_alamatLengkapPenerima = findViewById(R.id.tv_alamatLengkapOrderDetail);
        tv_kelPenerima = findViewById(R.id.tv_kelurahanOrderDetail);
        tv_kecPenerima = findViewById(R.id.tv_kecamatanOrderDetail);
        tv_kabKotaPenerima = findViewById(R.id.tv_kabKotaOrderDetail);
        tv_provinsiPenerima = findViewById(R.id.tv_provinsiOrderDetail);
        tv_noHpPenerima = findViewById(R.id.tv_noHPOrderDetail);
        tv_namaProduk = findViewById(R.id.tv_namaProdukOrderDetail);
        tv_qty = findViewById(R.id.tv_qtyOrderDetail);
        tv_hargaProduk = findViewById(R.id.tv_hargaProdukOrderDetail);
        tv_totalBelanja = findViewById(R.id.tv_totalBelanjaOrderDetail);
        tv_ongkir = findViewById(R.id.tv_ongkirOrderDetail);
        tv_potonganVoucher = findViewById(R.id.tv_potVoucherOrderDetail);
        tv_totalPembayaran = findViewById(R.id.tv_totalPembayaranOrderDetail);
        tv_statusPembayaran = findViewById(R.id.tv_statusPembayaranOrderDetail);
        tv_metodePembayaran = findViewById(R.id.tv_metodePembayaranOrderDetail);

        iv_fotoProduk = findViewById(R.id.iv_detailPesananOrderDetail);

        btn_backOrderDetail = findViewById(R.id.btn_backOrderDetail);
        btn_komplain = findViewById(R.id.btn_komplainOrder);
        btn_selesai = findViewById(R.id.btn_selesaiOrder);
        btn_beriUlasan = findViewById(R.id.btn_beriUlasanOrder);

        Bundle bundle = getIntent().getExtras();
        final String invoice = bundle.getString("invoice");

        dRef = FirebaseDatabase.getInstance().getReference("Pesanan").child(fUser.getUid()).child(invoice);
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String status_pesanan = snapshot.child("status_pesanan").getValue().toString();
                final String tgl_pemesanan = snapshot.child("tgl_transaksi").getValue().toString();
                final String jam_pemesanan = snapshot.child("waktu_transaksi").getValue().toString();
                final String invoice = snapshot.child("invoice").getValue().toString();
                final String nama_penerima = snapshot.child("nama_pemesan").getValue().toString();
                final String jenis_alamat = snapshot.child("jenis_alamat").getValue().toString();
                final String alamat_lengkap = snapshot.child("alamat_lengkap").getValue().toString();
                final String kelurahan = snapshot.child("kelurahan").getValue().toString();
                final String kecamatan = snapshot.child("kecamatan").getValue().toString();
                final String kabKota = snapshot.child("kab_kota").getValue().toString();
                final String provinsi = snapshot.child("provinsi").getValue().toString();
                final String no_hp = snapshot.child("no_hp").getValue().toString();
                final String nama_produk = snapshot.child("nama_produk").getValue().toString();
                final String qty = String.valueOf(snapshot.child("qty").getValue());
                final String harga_produk = String.valueOf(snapshot.child("harga_produk").getValue());
                final String total_belanja = String.valueOf(snapshot.child("total_harga_pesanan").getValue());
                final String ongkir = String.valueOf(snapshot.child("harga_ongkir").getValue());
                final String total_pembayaran = String.valueOf(snapshot.child("total_pembayaran").getValue());
                final String status_pembayaran = snapshot.child("status_pembayaran").getValue().toString();
                final String metode_pembayaran = snapshot.child("metode_pembayaran").getValue().toString();
                final String foto_produk = snapshot.child("url_images_produk").getValue().toString();

                tv_statusPesanan.setText(status_pesanan);
                tv_waktuPemesanan.setText(tgl_pemesanan +" "+ jam_pemesanan);
                tv_invoicePemesanan.setText(invoice);
                tv_namaPenerima.setText(nama_penerima);
                tv_jenisAlamatPenerima.setText(jenis_alamat);
                tv_alamatLengkapPenerima.setText(alamat_lengkap);
                tv_kelPenerima.setText(kelurahan);
                tv_kecPenerima.setText(kecamatan);
                tv_kabKotaPenerima.setText(kabKota);
                tv_provinsiPenerima.setText(provinsi);
                tv_namaProduk.setText(nama_produk);
                tv_qty.setText(qty + "x");
                tv_hargaProduk.setText(harga_produk);
                tv_totalBelanja.setText(total_belanja);
                tv_ongkir.setText(ongkir);
                tv_totalPembayaran.setText(total_pembayaran);
                tv_statusPembayaran.setText(status_pembayaran);
                tv_metodePembayaran.setText(metode_pembayaran);
                tv_noHpPenerima.setText(no_hp);

                Glide.with(OrderDetails.this).load(foto_produk).centerCrop().into(iv_fotoProduk);

                if (status_pesanan.equalsIgnoreCase("Selesai")){
                    btn_selesai.setVisibility(View.GONE);
                    btn_komplain.setVisibility(View.GONE);
                    btn_beriUlasan.setVisibility(View.VISIBLE);
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_backOrderDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(OrderDetails.this);
                alertDialog.setTitle("Pesanan");
                alertDialog.setMessage("Apakah anda ingin menyelesaikan pesanan?");
                alertDialog.setPositiveButton("Selesai", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dRef1 = FirebaseDatabase.getInstance().getReference("Pesanan").child(fUser.getUid()).child(invoice);
                        dRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                snapshot.getRef().child("status_pesanan").setValue("Selesai");

//                                Intent intent = new Intent(OrderDetails.this, OrderHistory.class);
//                                startActivity(intent);
//                                finish();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
                alertDialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        });
    }

}