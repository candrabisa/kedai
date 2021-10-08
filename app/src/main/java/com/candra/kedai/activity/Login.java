package com.candra.kedai.activity;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.candra.kedai.R;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.signin.internal.SignInClientImpl;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import br.com.simplepass.loadingbutton.customViews.OnAnimationEndListener;

public class Login extends AppCompatActivity {

    TextView btn_lupaPass;
    EditText et_Username, et_Password;
    ImageView btn_kembali;
    CircularProgressButton btn_login;
    SignInButton btn_loginGoogleAcc;

    ProgressDialog progressDialog;

    DatabaseReference dRef;
    FirebaseAuth fAuth;

    private SignInClient signInClient;
    private BeginSignInRequest beginSignInRequest;
    private boolean showOneTapUI = true;
    private static final int masukgoogle = 100;

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
        btn_loginGoogleAcc = findViewById(R.id.btn_loginGoogleAcc);
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
                    btn_login.startAnimation();
                    AuthCredential credential = EmailAuthProvider.getCredential(username, password);
                    fAuth.signInWithCredential(credential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        if (fAuth.getCurrentUser().isEmailVerified()) {
                                            Toast.makeText(Login.this, "Login Berhasil", Toast.LENGTH_LONG).show();
                                            SharedPreferences sPref = getSharedPreferences(userkey_, MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sPref.edit();
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

        btn_loginGoogleAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInClient = Identity.getSignInClient(Login.this);
                beginSignInRequest = BeginSignInRequest.builder().
                        setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                                .setSupported(true).build())
                        .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                                .setSupported(true).setServerClientId(getString(R.string.web_client_service_google_account))
                                .setFilterByAuthorizedAccounts(false).build())
                        .setAutoSelectEnabled(true).build();

                signInClient.beginSignIn(beginSignInRequest)
                        .addOnSuccessListener(new OnSuccessListener<BeginSignInResult>() {
                    @Override
                    public void onSuccess(BeginSignInResult beginSignInResult) {
                        try {
                            startIntentSenderForResult(beginSignInResult.getPendingIntent()
                                            .getIntentSender(), masukgoogle, null, 0,0,0);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                    }
                });
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == masukgoogle){
            try {
                SignInCredential credential = signInClient.getSignInCredentialFromIntent(data);
                final String idToken = credential.getGoogleIdToken();
                String username = credential.getId();
                String password = credential.getPassword();

                firebaseWithGoogleAccount(idToken);
            } catch (ApiException e) {
                switch (e.getStatusCode()) {
                    case CommonStatusCodes.CANCELED:
                        Log.d("batal_masuk", "One-tap dialog was closed.");
                        // Don't re-prompt the user.
                        showOneTapUI = false;
                        break;
                    case CommonStatusCodes.NETWORK_ERROR:
                        Log.d("jaringan_eror", "One-tap encountered a network error.");
                        // Try again or just ignore.
                        break;
                    default:
                        Log.d("gagal Login", "Couldn't get credential from result."
                                + e.getLocalizedMessage());
                        break;
                }
            }
        }
    }

    private void firebaseWithGoogleAccount(String idToken){
        try {
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            fAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        FirebaseUser firebaseUser = fAuth.getCurrentUser();
                        assert firebaseUser != null;
                        if (!firebaseUser.isEmailVerified()){
                            firebaseUser.sendEmailVerification();
                        }
                        final String Uid =firebaseUser.getUid();
                        HashMap<String, String> input = new HashMap<>();
                        input.put("Uid", Uid);
                        input.put("alamat_lengkap", "");
                        input.put("email", firebaseUser.getEmail());
                        input.put("jenis_alamat", "");
                        input.put("jenis_kelamin", "");
                        input.put("kab_kota", "");
                        input.put("kecamatan", "");
                        input.put("kelurahan", "");
                        input.put("provinsi", "");
                        input.put("nama_lengkap", firebaseUser.getDisplayName());
                        input.put("no_hp", "");
                        input.put("online_status", "");
                        input.put("password", "");
                        input.put("tgl_lahir", "");
                        input.put("typing", "no");

                        dRef = FirebaseDatabase.getInstance().getReference("Users").child(Uid);
                        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (!snapshot.exists()){
                                    dRef.setValue(input);
                                    snapshot.getRef().child("voucher").setValue(0);
                                    snapshot.getRef().child("saldo").setValue(60000);
                                }
                                Toast.makeText(Login.this, "Login dengan google berhasil", Toast.LENGTH_SHORT).show();
//                                Snackbar.make(getWindow().getDecorView().getRootView(), "Login dengan google berhasil", BaseTransientBottomBar.LENGTH_SHORT).show();
                                startActivity(new Intent(Login.this, MainActivity.class));
                                finish();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        Log.w("cekCredential", "signInWithCredential:failure", task.getException());
                        Snackbar.make(getWindow().getDecorView().getRootView(), "Login dengan google gagal", BaseTransientBottomBar.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e){
            Log.d("cekLoginGoogle", "firebaseAuthWithGoogle: " + e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}