package com.candra.kedai.activity;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.candra.kedai.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    TextView btn_lupaPass;
    EditText et_Username, et_Password;
    ImageView btn_kembali;
    Button btn_login;

    ProgressDialog progressDialog;

    DatabaseReference dRef;

    FirebaseAuth fAuth;

    String userkey_ = "emailkey";
    String userkey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fAuth = FirebaseAuth.getInstance();

        btn_lupaPass = findViewById(R.id.btn_lupapass);
        btn_kembali = findViewById(R.id.iv_kembaliLog);
        btn_login = findViewById(R.id.btn_loginLog);
        et_Username = findViewById(R.id.et_usernameLog);
        et_Password = findViewById(R.id.et_passwordLog);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = et_Username.getText().toString();
                final String password = et_Password.getText().toString();

                if (username.isEmpty()){
                    et_Username.setError("Email/Username belum di isi");
                    et_Username.setFocusable(true);
                    return;
                } else if (password.isEmpty()){
                    et_Password.setError("Password belum di isi");
                    et_Password.setFocusable(true);
                    return;
                } else {
                    progressDialog = new ProgressDialog(Login.this);
                    progressDialog.setMessage("Mohon menunggu...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    fAuth.signInWithEmailAndPassword(username, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        if (fAuth.getCurrentUser().isEmailVerified()) {
                                            progressDialog.dismiss();
                                            Toast.makeText(Login.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                                            SharedPreferences sPref = getSharedPreferences(userkey_, MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sPref.edit();
                                            editor.putString(userkey, et_Username.getText().toString());
                                            editor.apply();

                                            Intent intentMain = new Intent(Login.this, MainActivity.class);
                                            startActivity(intentMain);
                                            finish();
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(Login.this, "Kamu belum melakukan verifikasi email, Silahkan cek email.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()){
                                        progressDialog.dismiss();
                                        Toast.makeText(Login.this, "Username atau email belum terdaftar", Toast.LENGTH_SHORT).show();
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(Login.this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }


            }
        });

        btn_kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_lupaPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, ForgotPassword.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Login.this, GetStarted.class);
        startActivity(intent);
        finish();
    }
}