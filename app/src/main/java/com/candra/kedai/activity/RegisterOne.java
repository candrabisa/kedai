package com.candra.kedai.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

    Button btn_nextreg;
    ImageView iv_kembali;
    EditText etNama, etEmail, etHP, etPassword, etCPass;
    Spinner sp_jenisKel;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_one);

        etNama = findViewById(R.id.etUserName_Reg);
        etEmail = findViewById(R.id.etEmail_Reg);
        etHP = findViewById(R.id.etNoHP_Reg);
        etPassword = findViewById(R.id.etPassword_Reg);
        etCPass = findViewById(R.id.etConfirmPass_Reg);

        sp_jenisKel = findViewById(R.id.sp_jkRegister);

        iv_kembali = findViewById(R.id.iv_kembaliReg1);
        btn_nextreg = findViewById(R.id.btnNext_Reg);


        btn_nextreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String nama_lengkap = etNama.getText().toString();
                final String email = etEmail.getText().toString();
                final String no_hp = etHP.getText().toString();
                final String password = etPassword.getText().toString();
                final String cpassword = etCPass.getText().toString();
                final String jenis_kelamin = sp_jenisKel.getSelectedItem().toString();

                if (nama_lengkap.isEmpty()) {
                    etNama.setError("Username belum diisi");
                    etNama.requestFocus();
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
                    Intent intentNext = new Intent(RegisterOne.this, RegisterTwo.class);
                    intentNext.putExtra("nama_lengkap", nama_lengkap);
                    intentNext.putExtra("email", email);
                    intentNext.putExtra("no_hp", no_hp);
                    intentNext.putExtra("password", password);
                    intentNext.putExtra("jenis_kelamin", jenis_kelamin);
                    startActivity(intentNext);

//                    progressDialog = new ProgressDialog(RegisterOne.this);
//                    progressDialog.setMessage("Mohon menunggu...");
//                    progressDialog.setCancelable(false);
//                    progressDialog.show();
//
//                    fAuth.createUserWithEmailAndPassword(email, password)
//                            .addOnCompleteListener(RegisterOne.this, new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    if (task.isSuccessful()){
//                                        (fAuth.getCurrentUser()).sendEmailVerification()
//                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                    @Override
//                                                    public void onComplete(@NonNull Task<Void> task) {
//                                                        if (task.isSuccessful()){
//
//                                                            String emailID = fAuth.getCurrentUser().getEmail();
//                                                            String UserID = fAuth.getCurrentUser().getUid();
//
//                                                            Map<String, String> user = new HashMap<>();
//                                                            user.put("nama_lengkap", nama_lengkap);
//                                                            user.put("email", email);
//                                                            user.put("no_hp", no_hp);
//                                                            user.put("password", password);
//                                                            user.put("Uid", UserID);
//                                                            user.put("online_status", "offline");
//                                                            user.put("typing", "tidak");
//                                                            user.put("jenis_kelamin", jenis_kelamin);
//                                                            user.put("tgl_lahir", "Belum diisi");
//
//                                                            dRef = FirebaseDatabase.getInstance().getReference("Users");
//                                                            dRef.child(UserID).setValue(user);
//
//                                                            SharedPreferences sPref = getSharedPreferences(userkey_, MODE_PRIVATE);
//                                                            SharedPreferences.Editor editor = sPref.edit();
//                                                            editor.putString(userkey, UserID);
//                                                            editor.apply();
//                                                            Intent intentNext = new Intent(RegisterOne.this, RegisterTwo.class);
//                                                            startActivity(intentNext);
//                                                        } else {
//                                                            Toast.makeText(RegisterOne.this,"Email telah digunakan", Toast.LENGTH_SHORT).show();
//                                                            progressDialog.dismiss();
//                                                        }
//                                                    }
//                                                });
//                                    } else {
//                                        Toast.makeText(RegisterOne.this, "Email tidak valid atau telah digunakan", Toast.LENGTH_SHORT).show();
//                                        etEmail.setFocusable(true);
//                                        progressDialog.dismiss();
//                                    }
//                                }
//                            });
                }
            }
        });

        iv_kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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