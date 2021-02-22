package com.candra.kedai.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.candra.kedai.R;
import com.candra.kedai.model.address.Data;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class ChangePassword extends AppCompatActivity {

    private static final String TAG = "hahaha";

    CircularProgressButton btn_updatePassword;
    ImageView btn_back;
    EditText et_passwordLama, et_passwordBaru, et_passwordBaru2;
    TextView btn_resetPassword;

    DatabaseReference dRef;
    String User;
    FirebaseUser fUser, fUser2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        btn_back = findViewById(R.id.btn_backChangePW);
        btn_updatePassword = findViewById(R.id.btn_updatePassword);
        btn_resetPassword = findViewById(R.id.tv_resetPassword);

        et_passwordLama =findViewById(R.id.et_passwordLama);
        et_passwordBaru = findViewById(R.id.et_passwordBaru);
        et_passwordBaru2 = findViewById(R.id.et_passwordBaru2);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("Uid").equalTo(fUser.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    final String passwordlama = "" + ds.child("password").getValue().toString();
                    btn_updatePassword.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String passwordLama = et_passwordLama.getText().toString();
                            final String passwordBaru = et_passwordBaru.getText().toString();
                            final String passwordBaru2 = et_passwordBaru2.getText().toString();

                            if (passwordLama.isEmpty()) {
                                et_passwordLama.setError("Password lama belum diisi");
                                et_passwordLama.setFocusable(true);
                                return;
                            } else if (!passwordLama.equals(passwordlama)){
                                et_passwordLama.setError("Password lama salah");
                                et_passwordLama.setFocusable(true);
                                return;
                            } else if (passwordBaru.isEmpty()){
                                et_passwordBaru.setError("Password baru belum diisi");
                                et_passwordBaru.setFocusable(true);
                                return;
                            } else if (passwordBaru.length() < 6){
                                et_passwordBaru.setError("Minimal 6 karakter");
                                et_passwordBaru.setFocusable(true);
                                return;
                            } else if (passwordBaru2.isEmpty()){
                                et_passwordBaru2.setError("Ulangi password belum diisi");
                                et_passwordBaru2.setFocusable(true);
                                return;
                            } else if (!passwordBaru2.equals(passwordBaru)){
                                et_passwordBaru2.setError("Password tidak cocok");
                                et_passwordBaru2.setFocusable(true);
                                return;
                            } else {
                                btn_updatePassword.startAnimation();
                                final String email = fUser.getEmail();
                                AuthCredential credential = EmailAuthProvider
                                        .getCredential(email, passwordLama);
                                fUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            fUser.updatePassword(passwordBaru).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        dRef = FirebaseDatabase.getInstance().getReference("Users").child(fUser.getUid());
                                                        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                snapshot.getRef().child("password").setValue(passwordBaru);
                                                                Toast.makeText(ChangePassword.this, "Berhasil mengubah password", Toast.LENGTH_LONG).show();
                                                                btn_updatePassword.revertAnimation();
                                                                finish();
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                        } else {
                                            Log.d(TAG, "onComplete: Gagal Bossku");
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChangePassword.this, ForgotPassword.class));
            }
        });
    }
}