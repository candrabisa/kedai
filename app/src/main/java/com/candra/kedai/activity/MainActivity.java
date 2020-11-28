package com.candra.kedai.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.candra.kedai.R;
import com.candra.kedai.adapter.category.CategoryAdapter;
import com.candra.kedai.model.category.CategoryModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    ImageView iv_profil, ic_saldo, ic_voucher, iv_content1, iv_content2, iv_content3, ic_content1, ic_content2, ic_content3,
                ic_catMakanan, ic_catMinuman, ic_catPaket, ic_catCemilan;
    TextView tv_saldoHome, tv_saldoKamu, tv_voucherHome, tv_voucherKamu,
                tv_judulContent1, tv_judulContent2, tv_judulContent3,
                tv_isiContent1, tv_isiContent2, tv_isiContent3,
                tv_catContent1, tv_catContent2, tv_catContent3,
                tvcatHomeCemilan, tvcatHomePaket, tvcatHomeMinuman, tvcatHomeMakanan,
                tv_lagipromo, tv_buatkamu, tv_palinglaris;
    ConstraintLayout btn_catFood, btn_drinkHome, btn_paketHome, btn_cemilanHome;

    FirebaseUser fUser;
    FirebaseAuth fAuth;
    DatabaseReference dRef, dRef1, dRef2;

    RecyclerView rv1, rv2, rv3;
    SwipeRefreshLayout swipeRefreshLayout;
    CategoryAdapter categoryAdapter;
    List<CategoryModel>list1 = new ArrayList<>();
    List<CategoryModel>list2 = new ArrayList<>();
    List<CategoryModel>list3 = new ArrayList<>();

    Boolean keluar;
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
        tv_buatkamu = findViewById(R.id.textView4);
        tv_palinglaris = findViewById(R.id.textView5);
        tv_lagipromo = findViewById(R.id.textView6);

        ic_catMakanan = findViewById(R.id.ic_catMakanan);
        ic_catMinuman = findViewById(R.id.ic_catMinuman);
        ic_catPaket = findViewById(R.id.ic_catPaket);
        ic_catCemilan = findViewById(R.id.ic_catCemilan);

        tvcatHomeMakanan = findViewById(R.id.tvcatHomeMakanan);
        tvcatHomeMinuman = findViewById(R.id.tvcatHomeMinuman);
        tvcatHomePaket = findViewById(R.id.tvcatHomePaket);
        tvcatHomeCemilan = findViewById(R.id.tvcatHomeCemilan);

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

        rv1 = findViewById(R.id.rv_buatkamu);
        rv2 = findViewById(R.id.rv_palingLaris);
        rv3 = findViewById(R.id.rv_lagiPromo);

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();

        swipeRefreshLayout = findViewById(R.id.sw_home);
        swipeRefreshLayout.setOnRefreshListener(this);
        loadRekomendasiHome();
        loadContentHome();
        loadUserFromDatabase();

        dRef = FirebaseDatabase.getInstance().getReference().child("Icon");
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //ambil data
                final String iconSaldo = dataSnapshot.child("ic_dompet").getValue().toString();
                final String iconVoucher = dataSnapshot.child("ic_voucher").getValue().toString();

                final String iconMakanan = dataSnapshot.child("ic_makanan").child("url_images_icon").getValue().toString();
                final String namaMakanan = dataSnapshot.child("ic_makanan").child("nama_icon").getValue().toString();

                final String iconMinuman = dataSnapshot.child("ic_minuman").child("url_images_icon").getValue().toString();
                final String namaMinuman = dataSnapshot.child("ic_minuman").child("nama_icon").getValue().toString();

                final String iconPaket = dataSnapshot.child("ic_paket").child("url_images_icon").getValue().toString();
                final String namaPaket = dataSnapshot.child("ic_paket").child("nama_icon").getValue().toString();

                final String iconCemilan = dataSnapshot.child("ic_cemilan").child("url_images_icon").getValue().toString();
                final String namaCemilan = dataSnapshot.child("ic_cemilan").child("nama_icon").getValue().toString();

                //set data
                tvcatHomeMakanan.setText(namaMakanan);
                tvcatHomeMinuman.setText(namaMinuman);
                tvcatHomePaket.setText(namaPaket);
                tvcatHomeCemilan.setText(namaCemilan);
                tv_buatkamu.setText("BUAT KAMU");
                tv_palinglaris.setText("PALING LARIS");
                tv_lagipromo.setText("LAGI PROMO");

                try {
                    //saldo&voucher
                    Glide.with(MainActivity.this).load(iconSaldo).centerCrop().fitCenter().into(ic_saldo);
                    Glide.with(MainActivity.this).load(iconVoucher).centerCrop().fitCenter().into(ic_voucher);

                    //kategori
                    Glide.with(MainActivity.this).load(iconMakanan).centerCrop().fitCenter().into(ic_catMakanan);
                    Glide.with(MainActivity.this).load(iconMinuman).centerCrop().fitCenter().into(ic_catMinuman);
                    Glide.with(MainActivity.this).load(iconPaket).centerCrop().fitCenter().into(ic_catPaket);
                    Glide.with(MainActivity.this).load(iconCemilan).centerCrop().fitCenter().into(ic_catCemilan);
                } catch (Exception e){
                    Toast.makeText(MainActivity.this, "Coba lagi", Toast.LENGTH_SHORT).show();
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
                intentFood.putExtra("list_produk1", "kuahseger");
                intentFood.putExtra("list_produk2", "anekaburger");
                intentFood.putExtra("list_produk3", "anekamie");
                startActivity(intentFood);
            }
        });
        btn_drinkHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDrink = new Intent(MainActivity.this, Category.class);
                intentDrink.putExtra("nama_kategori", "minuman");
                intentDrink.putExtra("list_produk1", "dingin");
                intentDrink.putExtra("list_produk2", "panas");
                intentDrink.putExtra("list_produk3", "jus");
                startActivity(intentDrink);
            }
        });
        btn_paketHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPaket = new Intent(MainActivity.this, Category.class);
                intentPaket.putExtra("nama_kategori", "paketcombo");
                intentPaket.putExtra("list_produk1", "paketsarapan");
                intentPaket.putExtra("list_produk2", "paketmakansiang");
                intentPaket.putExtra("list_produk3", "paketkeluarga");
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

    private void refreshing(boolean b) {
        if (b){
            swipeRefreshLayout.setRefreshing(true);
        } else {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void loadUserFromDatabase(){
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
                    try {
                        Glide.with(MainActivity.this).load(fotoprofil)
                                .centerCrop().fitCenter().into(iv_profil);
                    } catch (Exception e){
                        Picasso.get().load(R.drawable.none_image_profile)
                                .centerCrop().fit().into(iv_profil);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadRekomendasiHome(){
        dRef2 = FirebaseDatabase.getInstance().getReference().child("landingPage");
        dRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.child("recomend1").getChildren()){
                    CategoryModel categoryModel = ds.getValue(CategoryModel.class);
                    list1.add(categoryModel);

                    categoryAdapter = new CategoryAdapter(MainActivity.this, list1);
                    rv1.setAdapter(categoryAdapter);
                }

                for (DataSnapshot ds1 : dataSnapshot.child("recomend2").getChildren()){
                    CategoryModel categoryModel = ds1.getValue(CategoryModel.class);
                    list2.add(categoryModel);

                    categoryAdapter = new CategoryAdapter(MainActivity.this, list2);
                    rv2.setAdapter(categoryAdapter);
                }

                for (DataSnapshot ds2 : dataSnapshot.child("recomend3").getChildren()) {
                    CategoryModel categoryModel = ds2.getValue(CategoryModel.class);
                    list3.add(categoryModel);

                    categoryAdapter = new CategoryAdapter(MainActivity.this, list3);
                    rv3.setAdapter(categoryAdapter);
                }
                refreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void loadContentHome(){
        dRef1 = FirebaseDatabase.getInstance().getReference().child("landingPage").child("Content");
        dRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //ambil content ke 1
                final String catContent1 = dataSnapshot.child("content1").child("kategori").getValue().toString();
                final String catDesc1 = dataSnapshot.child("content1").child("desc").getValue().toString();
                final String catJudul1 = dataSnapshot.child("content1").child("judul_desc").getValue().toString();
                final String catIcon1 = dataSnapshot.child("content1").child("ic_kategori").getValue().toString();
                final String catImages1 = dataSnapshot.child("content1").child("url_images_content").getValue().toString();

                //set data
                tv_catContent1.setText(catContent1);
                tv_isiContent1.setText(catDesc1);
                tv_judulContent1.setText(catJudul1);
                try {
                    Glide.with(MainActivity.this).load(catIcon1).centerCrop().fitCenter().into(ic_content1);
                    Glide.with(MainActivity.this).load(catImages1).centerCrop().fitCenter().into(iv_content1);
                } catch (Exception e){
                    Toast.makeText(MainActivity.this, "Gagal memuat gambar 1", Toast.LENGTH_SHORT).show();
                }

                //ambil content ke 2
                final String catContent2 = dataSnapshot.child("content2").child("kategori").getValue().toString();
                final String catDesc2 = dataSnapshot.child("content2").child("desc").getValue().toString();
                final String catJudul2 = dataSnapshot.child("content2").child("judul_desc").getValue().toString();
                final String catIcon2 = dataSnapshot.child("content2").child("ic_kategori").getValue().toString();
                final String catImages2 = dataSnapshot.child("content2").child("url_images_content").getValue().toString();

                //set data
                tv_catContent2.setText(catContent2);
                tv_isiContent2.setText(catDesc2);
                tv_judulContent2.setText(catJudul2);
                try {
                    Glide.with(MainActivity.this).load(catIcon2).centerCrop().fitCenter().into(ic_content2);
                    Glide.with(MainActivity.this).load(catImages2).centerCrop().fitCenter().into(iv_content2);
                } catch (Exception e){
                    Toast.makeText(MainActivity.this, "Gagal memuat gambar 2", Toast.LENGTH_SHORT).show();
                }

                //ambil content ke 3
                final String catContent3 = dataSnapshot.child("content3").child("kategori").getValue().toString();
                final String catDesc3 = dataSnapshot.child("content3").child("desc").getValue().toString();
                final String catJudul3 = dataSnapshot.child("content3").child("judul_desc").getValue().toString();
                final String catIcon3 = dataSnapshot.child("content3").child("ic_kategori").getValue().toString();
                final String catImages3 = dataSnapshot.child("content3").child("url_images_content").getValue().toString();

                //set data
                tv_catContent3.setText(catContent3);
                tv_isiContent3.setText(catDesc3);
                tv_judulContent3.setText(catJudul3);

                try {
                    Glide.with(MainActivity.this).load(catIcon3).centerCrop().fitCenter().into(ic_content3);
                    Glide.with(MainActivity.this).load(catImages3).centerCrop().fitCenter().into(iv_content3);
                } catch (Exception e){
                    Toast.makeText(MainActivity.this, "Gagal memuat gambar 3", Toast.LENGTH_SHORT).show();
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getUserLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(userkey_, MODE_PRIVATE);
        userkekey = sharedPreferences.getString(userkey, "");
    }


    @Override
    public void onRefresh() {
        loadContentHome();
        loadUserFromDatabase();
    }
}