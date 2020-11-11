package com.candra.kedai.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    ImageView iv_profil, ic_saldo, ic_voucher, iv_content1, iv_content2, iv_content3, ic_content1, ic_content2, ic_content3;
    TextView tv_saldoHome, tv_saldoKamu, tv_voucherHome, tv_voucherKamu,
                tv_judulContent1, tv_judulContent2, tv_judulContent3,
                tv_isiContent1, tv_isiContent2, tv_isiContent3,
                tv_catContent1, tv_catContent2, tv_catContent3;
    ConstraintLayout btn_catFood, btn_drinkHome, btn_paketHome, btn_cemilanHome;

    FirebaseUser fUser;
    FirebaseAuth fAuth;
    DatabaseReference dRef, dRef1, dRef2, dRef3;

    String userkey_ = "userkey";
    String userkey = "";
    String userkekey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getUserLocal();

        ic_saldo = findViewById(R.id.a1);
        ic_voucher = findViewById(R.id.a2);
        tv_saldoKamu = findViewById(R.id.t1);
        tv_saldoHome = findViewById(R.id.t2);
        tv_voucherKamu = findViewById(R.id.t3);
        tv_voucherHome = findViewById(R.id.t4);

        tv_judulContent1 = findViewById(R.id.tv_judulContentHome);
        tv_judulContent2 = findViewById(R.id.tv2_judulContentHome);
        tv_judulContent3 = findViewById(R.id.tv3_judulContentHome);

        tv_isiContent1 = findViewById(R.id.tv_isiContentHome);
        tv_isiContent2 = findViewById(R.id.tv2_isiContentHome);
        tv_isiContent3 = findViewById(R.id.tv3_isiContentHome);

        tv_catContent1 = findViewById(R.id.tv_kategoriContentHome);
        tv_catContent2 = findViewById(R.id.tv2_kategoriContentHome);
        tv_catContent3 = findViewById(R.id.tv3_kategoriContentHome);

        ic_content1 = findViewById(R.id.iv_kategoriContentHome);
        ic_content2 = findViewById(R.id.iv2_kategoriContentHome);
        ic_content3 = findViewById(R.id.iv3_kategoriContentHome);

        iv_profil = findViewById(R.id.iv_profilHome);
        iv_content1 = findViewById(R.id.iv1_isiContent);
        iv_content2 = findViewById(R.id.iv2_isiContent);
        iv_content3 = findViewById(R.id.iv3_isiContent);

        btn_catFood = findViewById(R.id.btn_foodHome);
        btn_drinkHome = findViewById(R.id.btn_drinkHome);
        btn_paketHome = findViewById(R.id.btn_paketHome);
        btn_cemilanHome = findViewById(R.id.btn_cemilanHome);

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();

        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("Uid").equalTo(fUser.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    //ambildata
                    final String total_saldo = "Rp. " + ds.child("saldo").getValue();
                    final String total_voucher = "" + ds.child("voucher").getValue();
                    final String fotoprofil = "" + ds.child("url_images_profil").getValue();

                    //setdata
                    tv_saldoHome.setText(total_saldo);
                    tv_voucherHome.setText(total_voucher);
                    tv_saldoKamu.setText("Saldo Kamu");
                    tv_voucherKamu.setText("Voucher Kamu");
                    Picasso.get().load(fotoprofil)
                            .centerCrop().fit().into(iv_profil);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dRef = FirebaseDatabase.getInstance().getReference().child("Icon");
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //ambil data
                final String iconSaldo = dataSnapshot.child("ic_dompet").getValue().toString();
                final String iconVoucher = dataSnapshot.child("ic_voucher").getValue().toString();

                //set data
                Picasso.get().load(iconSaldo).centerCrop().fit().into(ic_saldo);
                Picasso.get().load(iconVoucher).centerCrop().fit().into(ic_voucher);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dRef1 = FirebaseDatabase.getInstance().getReference().child("landingPage").child("Content").child("content1");
        dRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //ambil data
                final String catContent = dataSnapshot.child("kategori").getValue().toString();
                final String catDesc = dataSnapshot.child("desc").getValue().toString();
                final String catJudul = dataSnapshot.child("judul_desc").getValue().toString();
                final String catIcon = dataSnapshot.child("ic_kategori").getValue().toString();
                final String catImages = dataSnapshot.child("url_images_content").getValue().toString();

                //set data
                tv_catContent1.setText(catContent);
                tv_isiContent1.setText(catDesc);
                tv_judulContent1.setText(catJudul);
                Picasso.get().load(catIcon).centerCrop().fit().into(ic_content1);
                try {
                    Glide.with(MainActivity.this).load(catImages).centerCrop().fitCenter().into(iv_content1);
                } catch (Exception e){
                    Toast.makeText(MainActivity.this, "Gagal memuat gambar 1", Toast.LENGTH_SHORT).show();
                }
               
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dRef2 = FirebaseDatabase.getInstance().getReference().child("landingPage").child("Content").child("content2");
        dRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //ambil data
                final String catContent = dataSnapshot.child("kategori").getValue().toString();
                final String catDesc = dataSnapshot.child("desc").getValue().toString();
                final String catJudul = dataSnapshot.child("judul_desc").getValue().toString();
                final String catIcon = dataSnapshot.child("ic_kategori").getValue().toString();
                final String catImages = dataSnapshot.child("url_images_content").getValue().toString();

                //set data
                tv_catContent2.setText(catContent);
                tv_isiContent2.setText(catDesc);
                tv_judulContent2.setText(catJudul);
                Picasso.get().load(catIcon).centerCrop().fit().into(ic_content2);
                try {
                    Picasso.get().load(catImages).centerCrop().fit().into(iv_content2);
                } catch (Exception e){
                    Toast.makeText(MainActivity.this, "Gagal memuat gambar 2", Toast.LENGTH_SHORT).show();
                }
                
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dRef3 = FirebaseDatabase.getInstance().getReference().child("landingPage").child("Content").child("content3");
        dRef3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //ambil data
                final String catContent = dataSnapshot.child("kategori").getValue().toString();
                final String catDesc = dataSnapshot.child("desc").getValue().toString();
                final String catJudul = dataSnapshot.child("judul_desc").getValue().toString();
                final String catIcon = dataSnapshot.child("ic_kategori").getValue().toString();
                final String catImages = dataSnapshot.child("url_images_content").getValue().toString();

                //set data
                tv_catContent3.setText(catContent);
                tv_isiContent3.setText(catDesc);
                tv_judulContent3.setText(catJudul);
                Picasso.get().load(catIcon).centerCrop().fit().into(ic_content3);
                try {
                    Glide.with(MainActivity.this).load(catImages)
                            .centerCrop().fitCenter().into(iv_content3);
                } catch (Exception e){
                    Toast.makeText(MainActivity.this, "Gagal memuat gambar 3", Toast.LENGTH_SHORT).show();
                }
                
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        iv_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentprofil = new Intent(MainActivity.this, Profile.class);
                startActivity(intentprofil);
            }
        });
        btn_catFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentFood = new Intent(MainActivity.this, Category.class);
                intentFood.putExtra("nama_kategori", "makanan");
                startActivity(intentFood);
            }
        });
        btn_drinkHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDrink = new Intent(MainActivity.this, Category.class);
                intentDrink.putExtra("nama_kategori", "minuman");
                startActivity(intentDrink);
            }
        });
        btn_paketHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPaket = new Intent(MainActivity.this, Category.class);
                intentPaket.putExtra("nama_kategori", "paketcombo");
                startActivity(intentPaket);
            }
        });
        btn_cemilanHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCemilan = new Intent(MainActivity.this, Category.class);
                intentCemilan.putExtra("nama_kategori", "cemilan");
                intentCemilan.putExtra("list_produk1", "anekabolu");
                intentCemilan.putExtra("list_produk2", "anekabutter");
                intentCemilan.putExtra("list_produk3", "anekadonat");
                startActivity(intentCemilan);
            }
        });
    }

    public void getUserLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(userkey_, MODE_PRIVATE);
        userkekey = sharedPreferences.getString(userkey, "");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        System.exit(0);
    }
}