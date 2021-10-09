package com.candra.kedai.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
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
    EditText et_passwordBaru, et_passwordBaru2;
    TextView btn_resetPassword;
    FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference dRef = FirebaseDatabase.getInstance().getReference("Users").child(fUser.getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        btn_back = findViewById(R.id.btn_backChangePW);
        btn_updatePassword = findViewById(R.id.btn_updatePassword);
        btn_resetPassword = findViewById(R.id.tv_resetPassword);

        et_passwordBaru = findViewById(R.id.et_passwordBaru);
        et_passwordBaru2 = findViewById(R.id.et_passwordBaru2);

        btn_updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String passwordBaru = et_passwordBaru.getText().toString();
                final String passwordBaru2 = et_passwordBaru2.getText().toString();

                if (passwordBaru.isEmpty()){
                    et_passwordBaru.setError("Password lama belum diisi");
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
                    fUser.updatePassword(passwordBaru2).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                                dRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        snapshot.getRef().child("password").setValue(passwordBaru2);
                                        btn_updatePassword.revertAnimation();
                                        Snackbar.make(v, "Berhasil mengubah password", BaseTransientBottomBar.LENGTH_SHORT).setAction("OKE", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                et_passwordBaru.setText("");
                                                et_passwordBaru2.setText("");
                                                finish();
                                            }
                                        }).show();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            } else {
                                AuthCredential credential = EmailAuthProvider.getCredential(fUser.getEmail(), passwordBaru2);
                                fUser.linkWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()){
                                            Snackbar.make(v, "Password berhasil diubah", BaseTransientBottomBar.LENGTH_SHORT).setAction("OKE", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    et_passwordBaru.setText("");
                                                    et_passwordBaru2.setText("");
                                                    finish();
                                                }
                                            }).show();

                                        } else {
                                            Snackbar.make(v, "Password gagal diubah", BaseTransientBottomBar.LENGTH_SHORT).setAction("OKE", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    finish();
                                                }
                                            }).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
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