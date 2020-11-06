package com.candra.kedai.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.candra.kedai.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class Profile extends AppCompatActivity {

    CircularProgressButton btn_logout;
    ImageView iv_profil;
    TextView tv_namaLengkap;

    ShimmerFrameLayout shimmer1;

    FirebaseUser fUser;
    FirebaseAuth fAuth;
    DatabaseReference dRef;
    StorageReference sRef;

    String userkey_ = "userkey";
    String userkey = "";
    String userkekey = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getUserLocal();

        iv_profil = findViewById(R.id.iv_profilProf);
        tv_namaLengkap = findViewById(R.id.tv_namaProf);

        btn_logout = findViewById(R.id.btn_logout);

        shimmer1 = findViewById(R.id.shimmer_profil);

//        ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Mohon menunggu...");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//        shimmer1.startShimmerAnimation();
        dRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userkekey);
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                tv_namaLengkap.setText(dataSnapshot.child("nama_lengkap").getValue().toString());
                Picasso.with(Profile.this).load(dataSnapshot.child("url_images_profil").getValue().toString())
                        .centerCrop().fit().into(iv_profil);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences(userkey_, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(userkey, null);
                editor.apply();
                startActivity(new Intent(Profile.this, Login.class));
                finish();
            }
        });

    }


    public void getUserLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(userkey_, MODE_PRIVATE);
        userkekey = sharedPreferences.getString(userkey, "");
    }
}