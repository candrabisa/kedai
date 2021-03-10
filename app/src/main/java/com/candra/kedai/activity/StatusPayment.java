package com.candra.kedai.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

    ImageView btn_closeWaitPayment, ic_pembayaran1, ic_pembayaran2;
    TextView tv_totalNominalTagihan, tv_metodePembayaranWaiting,
            tv_pembayaran1, tv_pembayaran2, tv_namaPembayaran1, tv_namaPembayaran2,
            tv_waktuPembelian, tv_statusPembayaran, tv_nomoTransaksi, tv_batasPembayaran;
    Button btn_konfirmasiPembayaran;

    FirebaseUser fUser;
    DatabaseReference dRef;

    Integer saldo_saya = 0;
    Integer saldo_sekarang = 0;
    Integer nominalTransaksi = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_payment);

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        btn_closeWaitPayment = findViewById(R.id.btn_closeWaitPayment);
        btn_konfirmasiPembayaran = findViewById(R.id.btn_konfirmasiPembayaran);

        ic_pembayaran1 = findViewById(R.id.ic_pembayaran1);
        ic_pembayaran2 = findViewById(R.id.ic_pembayaran2);

        tv_pembayaran1 = findViewById(R.id.tv_pembayaran1);
        tv_pembayaran2 = findViewById(R.id.tv_pembayaran2);
        tv_namaPembayaran1 = findViewById(R.id.tv_namaPembayaran1);
        tv_namaPembayaran2 = findViewById(R.id.tv_namaPembayaran2);

        tv_batasPembayaran = findViewById(R.id.tv_batasPembayaran);
        tv_totalNominalTagihan = findViewById(R.id.tv_totalNominalTagihan);
        tv_metodePembayaranWaiting = findViewById(R.id.tv_metodePembayaranWaiting);
        tv_statusPembayaran = findViewById(R.id.tv_statusPembayaran);
        tv_waktuPembelian = findViewById(R.id.tv_waktuPembelian);
        tv_nomoTransaksi = findViewById(R.id.tv_nomoTransaksi);

        Bundle bundle = getIntent().getExtras();
        final String nomorTransaksi = bundle.getString("nomorTransaksi");
        dRef = FirebaseDatabase.getInstance().getReference("TransaksiSaldo").child(fUser.getUid()).child(nomorTransaksi);
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String nomorTransaksi = snapshot.child("nomor_transaksi").getValue().toString();
                final String waktuTransaksi = snapshot.child("waktu_transaksi").getValue().toString();
                final String statusTransaksi = snapshot.child("status_transaksi").getValue().toString();
                nominalTransaksi = Integer.parseInt(snapshot.child("nominal_transaksi").getValue().toString());
                final String metodeTransaksi = snapshot.child("metode_pembayaran").getValue().toString();
                final String batas_pembayaran = snapshot.child("batas_pembayaran").getValue().toString();

                tv_nomoTransaksi.setText(nomorTransaksi);
                tv_waktuPembelian.setText(waktuTransaksi);
                tv_statusPembayaran.setText(statusTransaksi);
                tv_totalNominalTagihan.setText("Rp. "+nominalTransaksi);
                tv_metodePembayaranWaiting.setText(metodeTransaksi);
                tv_batasPembayaran.setText(batas_pembayaran);

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
                    ic_pembayaran2.setVisibility(View.GONE);
                    tv_pembayaran2.setVisibility(View.GONE);
                    tv_namaPembayaran2.setVisibility(View.GONE);
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

        btn_closeWaitPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}