package com.candra.kedai.activity;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import br.com.simplepass.loadingbutton.customViews.OnAnimationEndListener;

public class Login extends AppCompatActivity {

    TextView btn_lupaPass;
    EditText et_Username, et_Password;
    ImageView btn_kembali;
    CircularProgressButton btn_login;

    ProgressDialog progressDialog;

    DatabaseReference dRef;

    FirebaseAuth fAuth;

    String userkey_ = "userkey";
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
//                    progressDialog = new ProgressDialog(Login.this);
//                    progressDialog.setMessage("Mohon menunggu...");
//                    progressDialog.setCancelable(false);
//                    progressDialog.show();
                    btn_login.startAnimation();

                    fAuth.signInWithEmailAndPassword(username, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        if (fAuth.getCurrentUser().isEmailVerified()) {
                                            Toast.makeText(Login.this, "Login Berhasil", Toast.LENGTH_LONG).show();
                                            SharedPreferences sPref = getSharedPreferences(userkey_, MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sPref.edit();
                                            editor.putString(userkey, et_Username.getText().toString());
                                            editor.putString(userkey, et_Username.getText().toString());
                                            editor.apply();

                                            btn_login.revertAnimation();
                                            Intent intentMain = new Intent(Login.this, MainActivity.class);
                                            startActivity(intentMain);
                                            finish();
                                        } else {
                                            btn_login.revertAnimation();
                                            Toast.makeText(Login.this, "Kamu belum melakukan verifikasi, Silahkan cek email.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()){
                                        btn_login.revertAnimation();
                                        Toast.makeText(Login.this, "Email belum terdaftar", Toast.LENGTH_SHORT).show();
                                    } else {
                                        btn_login.revertAnimation();
                                        Toast.makeText(Login.this, "Email atau Password salah", Toast.LENGTH_SHORT).show();
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
    }

}