package com.candra.kedai.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.candra.kedai.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Address extends AppCompatActivity {

    TextView tv_jenisAlamat, tv_alamatLengkap, tv_kelurahan, tv_kecamatan
            ,tv_kabKota, tv_provinsi;
    Button btn_ubahAlamat;
    ImageView btn_back;

    FirebaseUser fUser;
    DatabaseReference dRef;

    String userkey_ = "userkey";
    String userkey = "";
    String userkekey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        getUserLocal();

        tv_jenisAlamat = findViewById(R.id.tv_jenisAlamat);
        tv_alamatLengkap = findViewById(R.id.tv_alamatLengkap);
        tv_kelurahan = findViewById(R.id.tv_kelurahan);
        tv_kecamatan = findViewById(R.id.tv_kecamatan);
        tv_kabKota = findViewById(R.id.tv_kabKota);
        tv_provinsi = findViewById(R.id.tv_provinsi);

        btn_back = findViewById(R.id.btn_back_address);
        btn_ubahAlamat = findViewById(R.id.btn_ubahAlamat);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("Uid").equalTo(fUser.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    final String jenis_alamat = "Alamat " + ds.child("jenis_alamat").getValue().toString();
                    final String alamat_lengkap = ds.child("alamat_lengkap").getValue().toString();
                    final String kelurahan = ds.child("kelurahan").getValue().toString().toLowerCase() + ", ";
                    final String kecamatan = ds.child("kecamatan").getValue().toString().toLowerCase();
                    final String kab_kota = ds.child("kab_kota").getValue().toString().toLowerCase() + ", ";
                    final String provinsi = ds.child("provinsi").getValue().toString().toLowerCase();

                    //ambil data
                    tv_jenisAlamat.setText(jenis_alamat);
                    tv_alamatLengkap.setText(alamat_lengkap);
                    tv_kelurahan.setText(kelurahan);
                    tv_kecamatan.setText(kecamatan);
                    tv_kabKota.setText(kab_kota);
                    tv_provinsi.setText(provinsi);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_ubahAlamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Address.this, ChangeAddress.class);
                startActivity(intent);
            }
        });

    }

    public void getUserLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(userkey_, MODE_PRIVATE);
        userkekey = sharedPreferences.getString(userkey, "");
    }
}