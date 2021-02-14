package com.candra.kedai.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.candra.kedai.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class Profile extends AppCompatActivity {

    Button btn_logout;
    ImageView iv_profil, ic_saldo, ic_voucher, btn_backProfile, btn_settings;
    TextView tv_namaLengkap, tv_saldo, tv_voucher, tulisan_saldo_kamu, tulisan_voucher_kamu;

    RelativeLayout btn_editProfil, btn_alamatPengiriman, btn_riwayatPesanan, btn_wishlist;

    ShimmerFrameLayout shimmer1;

    FirebaseUser fUser;
    DatabaseReference dRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        iv_profil = findViewById(R.id.iv_profilProf);
        ic_saldo = findViewById(R.id.ic_saldoProfil);
        ic_voucher = findViewById(R.id.ic_voucherProf);
        tv_namaLengkap = findViewById(R.id.tv_namaProf);
        tv_saldo = findViewById(R.id.saldo_profil);
        tv_voucher = findViewById(R.id.tv_voucherProf);
        tulisan_saldo_kamu = findViewById(R.id.tulisan_saldo_kamu);
        tulisan_voucher_kamu = findViewById(R.id.tulisan_voucher_kamu);

        btn_backProfile = findViewById(R.id.btn_backProfile);
        btn_editProfil = findViewById(R.id.rl_editProfil);
        btn_alamatPengiriman = findViewById(R.id.rl_alamatPengiriman);
        btn_riwayatPesanan = findViewById(R.id.rl_riwayatPesanan);
        btn_wishlist = findViewById(R.id.rl_favorit);
        btn_settings = findViewById(R.id.imageView3);

        btn_logout = findViewById(R.id.btn_logout);
        shimmer1 = findViewById(R.id.shimmer_profil);

        dRef = FirebaseDatabase.getInstance().getReference().child("Icon");
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //ambil data
                final String iconSaldo = dataSnapshot.child("ic_dompet").getValue().toString();
                final String iconVoucher = dataSnapshot.child("ic_voucher").getValue().toString();

                //set data
                tulisan_saldo_kamu.setText("Saldo Kamu");
                tulisan_voucher_kamu.setText("Voucher Kamu");
                try {
                    Glide.with(Profile.this).load(iconSaldo)
                            .centerCrop().fitCenter().into(ic_saldo);
                    Glide.with(Profile.this).load(iconVoucher)
                            .centerCrop().fitCenter().into(ic_voucher);
                } catch (Exception e){
                    Toast.makeText(Profile.this, "Icon gagal memuat", Toast.LENGTH_SHORT).show();
                }
                shimmer1.stopShimmer();
                shimmer1.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("Uid").equalTo(fUser.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        //ambildata
                        final String namalengkap = "" + ds.child("nama_lengkap").getValue();
                        final String saldo = "Rp. " + ds.child("saldo").getValue();
                        final String voucher = ds.child("voucher").getValue() + " Voucher";

                        //setdata
                        tv_namaLengkap.setText(namalengkap);
                        tv_saldo.setText(saldo);
                        tv_voucher.setText(voucher);

                        try {
                            final String fotoprofil = "" +ds.child("url_images_profil").getValue().toString();
                            Glide.with(Profile.this).load(fotoprofil)
                                    .centerCrop().fitCenter().into(iv_profil);
                        } catch (Exception e){
                            Glide.with(Profile.this).load(R.drawable.none_image_profile)
                                    .centerCrop().fitCenter().into(iv_profil);
                        }

                    }
                } catch (Exception e){

                }

                shimmer1.stopShimmer();
                shimmer1.setVisibility(View.GONE);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_editProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editProf = new Intent(Profile.this, EditProfile.class);
                startActivity(editProf);
            }
        });

        btn_alamatPengiriman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddress = new Intent(Profile.this, Address.class);
                startActivity(intentAddress);
            }
        });
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, Settings.class));
            }
        });

        btn_riwayatPesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRiwayat = new Intent(Profile.this, OrderHistory.class);
                startActivity(intentRiwayat);
            }
        });

        btn_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentWishlist = new Intent(Profile.this, Favorite.class);
                startActivity(intentWishlist);
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