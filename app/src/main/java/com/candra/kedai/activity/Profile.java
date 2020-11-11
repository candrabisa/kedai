package com.candra.kedai.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    CircularProgressButton btn_logout;
    ImageView iv_profil, ic_saldo, ic_voucher;
    TextView tv_namaLengkap, tv_saldo, tv_voucher, tulisan_saldo_kamu, tulisan_voucher_kamu;

    RelativeLayout btn_editProfil;

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

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();

        iv_profil = findViewById(R.id.iv_profilProf);
        ic_saldo = findViewById(R.id.ic_saldoProfil);
        ic_voucher = findViewById(R.id.ic_voucherProf);
        tv_namaLengkap = findViewById(R.id.tv_namaProf);
        tv_saldo = findViewById(R.id.saldo_profil);
        tv_voucher = findViewById(R.id.tv_voucherProf);
        tulisan_saldo_kamu = findViewById(R.id.tulisan_saldo_kamu);
        tulisan_voucher_kamu = findViewById(R.id.tulisan_voucher_kamu);

        btn_editProfil = findViewById(R.id.rl_editProfil);

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
                    Picasso.get().load(iconSaldo).centerCrop()
                            .fit().into(ic_saldo);
                    Picasso.get().load(iconVoucher).centerCrop()
                            .fit().into(ic_voucher);
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
                for (DataSnapshot ds : dataSnapshot.getChildren()){

                    //ambildata
                    final String namalengkap = "" + ds.child("nama_lengkap").getValue();
                    final String saldo = "Rp. " + ds.child("saldo").getValue();
                    final String voucher = ds.child("voucher").getValue() + " Voucher";
                    final String foto_profil = "" +ds.child("url_images_profil").getValue();

                    //setdata
                    try {
                        Picasso.get().load(foto_profil).centerCrop()
                                .fit().into(iv_profil);
                        tv_namaLengkap.setText(namalengkap);
                        tv_saldo.setText(saldo);
                        tv_voucher.setText(voucher);
                    } catch (Exception e){
                        Picasso.get().load(R.drawable.none_image_profile).into(iv_profil);
                    }

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

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_logout.startAnimation();
                SharedPreferences sharedPreferences = getSharedPreferences(userkey_, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(userkey, null);
                editor.apply();
                btn_logout.stopProgressAnimation();
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