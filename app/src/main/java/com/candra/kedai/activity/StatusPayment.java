package com.candra.kedai.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.candra.kedai.R;
import com.candra.kedai.model.address.Data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StatusPayment extends AppCompatActivity {

    ImageView btn_closeWaitPayment, ic_pembayaran1, ic_pembayaran2, ic_menungguPembayaran;
    TextView tv_totalNominalTagihan, tv_metodePembayaranWaiting, tv_noPembayaran1, tv_noPembayaran2,
            tv_pembayaran1, tv_pembayaran2, tv_namaPembayaran1, tv_namaPembayaran2, tv_statusPembayaranStatus,
            tv_waktuPembelian, tv_statusPembayaran, tv_nomoTransaksi, tv_batasPembayaran, tv_harapLakukanPembayaran,
            tv_metodePembayaranDetail, tv_nominalPembayaranStatus, tv_totalPembayaranStatus, tv_biayaAdminStatus,
            tv_salinNominalPembayaran;
    Button btn_konfirmasiPembayaran;
    ImageButton btn_copyNoPembayaran1, btn_copyNoPembayaran2;

    LinearLayout ll_paymentMethod;

    FirebaseUser fUser;
    DatabaseReference dRef;

    Integer total_pembayaran = 0;
    Integer saldo_saya = 0;
    Integer saldo_sekarang = 0;
    Integer nominalTransaksi = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_payment);

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        ll_paymentMethod = findViewById(R.id.linearLayout7);

        btn_closeWaitPayment = findViewById(R.id.btn_closeWaitPayment);
        btn_konfirmasiPembayaran = findViewById(R.id.btn_konfirmasiPembayaran);
        btn_copyNoPembayaran2 = findViewById(R.id.btn_copyNoPembayaran2);
        btn_copyNoPembayaran1 = findViewById(R.id.btn_copyNoPembayaran1);

        ic_menungguPembayaran = findViewById(R.id.ic_menungguPembayaran);
        ic_pembayaran1 = findViewById(R.id.ic_pembayaran1);
        ic_pembayaran2 = findViewById(R.id.ic_pembayaran2);

        tv_pembayaran1 = findViewById(R.id.tv_pembayaran1);
        tv_pembayaran2 = findViewById(R.id.tv_pembayaran2);
        tv_namaPembayaran1 = findViewById(R.id.tv_namaPembayaran1);
        tv_namaPembayaran2 = findViewById(R.id.tv_namaPembayaran2);
        tv_noPembayaran1 = findViewById(R.id.tv_noPembayaran1);
        tv_noPembayaran2 = findViewById(R.id.tv_noPembayaran2);
        tv_statusPembayaranStatus = findViewById(R.id.tv_statusPembayaranStatus);
        tv_harapLakukanPembayaran = findViewById(R.id.tv_harapLakukanPembayaran);
        tv_salinNominalPembayaran = findViewById(R.id.tv_salinNominalPembayaran);

        tv_batasPembayaran = findViewById(R.id.tv_batasPembayaran);
        tv_totalNominalTagihan = findViewById(R.id.tv_totalNominalTagihan);
        tv_metodePembayaranWaiting = findViewById(R.id.tv_metodePembayaranWaiting);
        tv_statusPembayaran = findViewById(R.id.tv_statusPembayaran);
        tv_waktuPembelian = findViewById(R.id.tv_waktuPembelian);
        tv_nomoTransaksi = findViewById(R.id.tv_nomoTransaksi);
        tv_metodePembayaranDetail = findViewById(R.id.tv_metodePembayaranDetail);
        tv_nominalPembayaranStatus = findViewById(R.id.tv_nominalPembayaranStatus);
        tv_totalPembayaranStatus = findViewById(R.id.tv_totalPembayaranStatus);
        tv_biayaAdminStatus = findViewById(R.id.tv_biayaAdminStatus);

        Bundle bundle = getIntent().getExtras();
        final String nomorTransaksi = bundle.getString("nomorTransaksi");

        final String tanggalNow = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(new Date());

        dRef = FirebaseDatabase.getInstance().getReference("TransaksiSaldo").child(fUser.getUid()).child(nomorTransaksi);
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String nomorTransaksi = snapshot.child("nomor_transaksi").getValue().toString();
                final String waktuTransaksi = snapshot.child("waktu_transaksi").getValue().toString();
                final String statusTransaksi = snapshot.child("status_transaksi").getValue().toString();
                final String metodeTransaksi = snapshot.child("metode_pembayaran").getValue().toString();
                final String batas_pembayaran = snapshot.child("batas_pembayaran").getValue().toString();
                nominalTransaksi = Integer.parseInt(snapshot.child("nominal_transaksi").getValue().toString());
                total_pembayaran = Integer.parseInt(snapshot.child("total_pembayaran").getValue().toString());
                final int biaya_admin = Integer.parseInt(snapshot.child("biaya_admin").getValue().toString());

                tv_nomoTransaksi.setText(nomorTransaksi);
                tv_waktuPembelian.setText(waktuTransaksi);
                tv_statusPembayaran.setText(statusTransaksi);
                tv_totalNominalTagihan.setText("Rp. "+total_pembayaran);

                tv_metodePembayaranWaiting.setText(metodeTransaksi);
                tv_batasPembayaran.setText(batas_pembayaran);
                tv_metodePembayaranDetail.setText(metodeTransaksi);
                tv_nominalPembayaranStatus.setText("Rp. "+nominalTransaksi);
                tv_biayaAdminStatus.setText("Rp. "+biaya_admin);
                tv_totalPembayaranStatus.setText("Rp. "+total_pembayaran);

                if (metodeTransaksi.equalsIgnoreCase("Minimarket")){
                    ic_pembayaran1.setImageResource(R.drawable.indomaret);
                    tv_pembayaran1.setText("Indomaret");
                    tv_namaPembayaran1.setVisibility(View.GONE);
                    ic_pembayaran2.setImageResource(R.drawable.alfamart);
                    tv_pembayaran2.setText("Alfamart");
                    tv_namaPembayaran2.setVisibility(View.GONE);
                } else if (metodeTransaksi.equalsIgnoreCase("Google Play")){
                    ic_pembayaran1.setImageResource(R.drawable.google_play);
                    tv_pembayaran1.setText("Google Play");
                    tv_namaPembayaran1.setVisibility(View.GONE);
                    tv_noPembayaran1.setText("Silahkan dibayar");
                    ic_pembayaran2.setVisibility(View.GONE);
                    tv_pembayaran2.setVisibility(View.GONE);
                    tv_namaPembayaran2.setVisibility(View.GONE);
                    tv_noPembayaran2.setVisibility(View.GONE);
                    btn_copyNoPembayaran2.setVisibility(View.GONE);
                    btn_copyNoPembayaran1.setVisibility(View.GONE);
                }

                if (statusTransaksi.equalsIgnoreCase("Dibayar")) {
                    ic_menungguPembayaran.setImageResource(R.drawable.ic_suksesbeli);
                    tv_statusPembayaranStatus.setText("Pembayaran Selesai");
                    tv_harapLakukanPembayaran.setText("Kamu sudah melakukan pembayaran sebesar:");
                    ll_paymentMethod.setVisibility(View.GONE);
                    btn_konfirmasiPembayaran.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_konfirmasiPembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(StatusPayment.this);
                alertDialog.setTitle("Konfirmasi Pembayaran");
                alertDialog.setMessage("Pembayaran hanya fiktif belaka, silahkan pilih tombol YA untuk mengakhiri transaksi");
                alertDialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                snapshot.getRef().child("status_transaksi").setValue("Dibayar");
                                snapshot.getRef().child("waktu_transaksi").setValue(tanggalNow);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        DatabaseReference dRef1 = FirebaseDatabase.getInstance().getReference("Users").child(fUser.getUid());
                        dRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                saldo_saya = Integer.valueOf(snapshot.child("saldo").getValue().toString());
                                saldo_sekarang = saldo_saya + nominalTransaksi;
                                snapshot.getRef().child("saldo").setValue(saldo_sekarang);

                                Toast.makeText(StatusPayment.this, "Transaksi berhasil", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(StatusPayment.this, Topup.class));
                                finish();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
                alertDialog.show();
            }
        });

        tv_salinNominalPembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Nominal berhasil dicopy", String.valueOf(total_pembayaran));
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(StatusPayment.this, "Nominal pembayaran berhasil disalin", Toast.LENGTH_SHORT).show();
            }
        });
        
        btn_copyNoPembayaran2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Hahaha", tv_noPembayaran2.getText().toString());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(StatusPayment.this, "Nomor pembayaran berhasil disalin", Toast.LENGTH_SHORT).show();
            }
        });
        
        btn_copyNoPembayaran1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("wkwkwk", tv_noPembayaran1.getText().toString());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(StatusPayment.this, "Nomor pembayaran berhasil disalin", Toast.LENGTH_SHORT).show();
            }
        });

        btn_closeWaitPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}