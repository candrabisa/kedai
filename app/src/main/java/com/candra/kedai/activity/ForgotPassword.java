package com.candra.kedai.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.candra.kedai.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    ImageView btn_kembali;
    EditText et_lupaPass;
    Button btn_kirim;

    ProgressDialog progressDialog;
    AlertDialog.Builder alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        btn_kembali = findViewById(R.id.iv_kembaliPass);
        et_lupaPass = findViewById(R.id.et_lupaPass);
        btn_kirim = findViewById(R.id.btn_resetPassword);

        btn_kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = et_lupaPass.getText().toString();
                if (email.isEmpty()){
                    et_lupaPass.setError("Email belum diisi");
                    et_lupaPass.setFocusable(true);
                } else {
                    progressDialog = new ProgressDialog(ForgotPassword.this);
                    progressDialog.setMessage("Mohon menunggu...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        alertDialog = new AlertDialog.Builder(ForgotPassword.this);
                                        alertDialog.setTitle("Berhasil");
                                        alertDialog.setMessage("Silahkan cek email untuk mereset password kamu");
                                        alertDialog.setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                progressDialog.dismiss();
                                                startActivity(new Intent(ForgotPassword.this, Login.class));
                                                finish();
                                            }
                                        });
                                        AlertDialog dialog = alertDialog.create();
                                        dialog.show();
                                    } else {
                                        alertDialog = new AlertDialog.Builder(ForgotPassword.this);
                                        alertDialog.setTitle("Gagal");
                                        alertDialog.setMessage("Email anda belum terdaftar");
                                        alertDialog.setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                progressDialog.dismiss();
                                            }
                                        });
                                        AlertDialog dialog = alertDialog.create();
                                        dialog.show();
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
    }
}