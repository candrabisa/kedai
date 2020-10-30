package com.candra.kedai.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.candra.kedai.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.HashMap;
import java.util.Map;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class RegisterOne extends AppCompatActivity {

    CircularProgressButton btn_nextreg;
    ImageView iv_kembali;
    EditText etUsername, etFullname, etEmail, etHP, etPassword, etCPass;

    FirebaseAuth fAuth;
    DatabaseReference dRef;
    String userkey_ = "userkey";
    String userkey = "" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_one);

        fAuth = FirebaseAuth.getInstance();

        etUsername = findViewById(R.id.etUserName_Reg);

        etEmail = findViewById(R.id.etEmail_Reg);
        etHP = findViewById(R.id.etNoHP_Reg);
        etPassword = findViewById(R.id.etPassword_Reg);
        etCPass = findViewById(R.id.etConfirmPass_Reg);

        iv_kembali = findViewById(R.id.iv_kembaliReg1);
        btn_nextreg = findViewById(R.id.btnNext_Reg);


        btn_nextreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = etUsername.getText().toString();
                final String email = etEmail.getText().toString();
                final String no_hp = etHP.getText().toString();
                final String password = etPassword.getText().toString();
                final String cpassword = etCPass.getText().toString();


                if (username.isEmpty()) {
                    etUsername.setError("Username belum diisi");
                    etUsername.requestFocus();
                    return;
                } else if (email.isEmpty()){
                    etEmail.setError("Email belum diisi");
                    etEmail.requestFocus();
                    return;
                } else if (no_hp.isEmpty()){
                    etHP.setError("Nomer HP belum diisi");
                    etHP.requestFocus();
                    return;
                } else if (password.isEmpty()){
                    etPassword.setError("Password belum diisi");
                    etPassword.requestFocus();
                    return;
                } else if (password.length()<6) {
                    etPassword.setError("Password setidaknya memiliki 6 karakter");
                    etPassword.requestFocus();
                    return;
                } else if (!cpassword.equals(password)){
                    etCPass.setError("Password tidak cocok");
                    etCPass.requestFocus();
                    return;
                } else if (cpassword.isEmpty()){
                    etCPass.setError("Confirmasi password tidak boleh kosong");
                    etCPass.requestFocus();
                    return;
                } else {
                    btn_nextreg.startAnimation();
                    fAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegisterOne.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        (fAuth.getCurrentUser()).sendEmailVerification()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            String emailID = fAuth.getCurrentUser().getEmail();
                                                            String UserID = fAuth.getCurrentUser().getUid();
                                                            Map<String, String> user = new HashMap<>();
                                                            user.put("username", username);
                                                            user.put("email", email);
                                                            user.put("no_hp", no_hp);
                                                            user.put("password", password);
                                                            user.put("Uid", UserID);
                                                            user.put("saldo", "");
                                                            user.put("online_status", "offline");
                                                            user.put("typing", "tidak");


                                                            dRef = FirebaseDatabase.getInstance().getReference("Users");
                                                            dRef.child(username).setValue(user);

                                                            SharedPreferences sPref = getSharedPreferences(userkey_, MODE_PRIVATE);
                                                            SharedPreferences.Editor editor = sPref.edit();
                                                            editor.putString(userkey, etUsername.getText().toString());
                                                            editor.apply();
                                                            Intent intentNext = new Intent(RegisterOne.this, RegisterTwo.class);
                                                            startActivity(intentNext);
                                                        } else {
                                                            Toast.makeText(RegisterOne.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(RegisterOne.this, "Email telah digunakan", Toast.LENGTH_SHORT).show();
                                        btn_nextreg.dispose();
                                        btn_nextreg.stopAnimation();

                                    }
                                }
                            });
                }
            }
        });

        iv_kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(RegisterOne.this, GetStarted.class);
//        startActivity(intent);
//        finish();
    }
}