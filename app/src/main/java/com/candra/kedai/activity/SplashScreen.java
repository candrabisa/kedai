package com.candra.kedai.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.candra.kedai.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SplashScreen extends AppCompatActivity {

    private static final String TAG = "cek";
    String userkey_ = "userkey";
    String userkey = "";
    String userkekey = "";

    String smartLogin_ = "smartlogin";
    String smartlogin = "";
    String smartLog = "";

    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private AppUpdateManager mAppUpdateManager;
    private static final int RC_APP_UPDATE = 11;


    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getUserLocal();

    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void getUserLocal() {
        SharedPreferences preferences = getSharedPreferences(userkey_, MODE_PRIVATE);
        userkekey = preferences.getString(userkey, "");
        final String uid = fAuth.getUid();
        if (uid == null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreen.this, OnBoard.class));
                    finish();
                }
            }, 2000);
        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences sharedPreferences = getSharedPreferences(smartLogin_, MODE_PRIVATE);
                    smartLog = sharedPreferences.getString(smartlogin, "");
                    if (smartLog.equals("nyala")){
                        fingerPrint();
                    } else {
                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }, 2000);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.P)
    public void fingerPrint(){
        Executor executor = Executors.newSingleThreadExecutor();
        BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(SplashScreen.this)
                .setTitle("Smart Login")
                .setDescription("Kedai mendeteksi kamu mengaktifkan Smart Login, Silahkan melakukan sidik jari")
                .setNegativeButton("Batal", executor, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).build();
        SplashScreen aThis = this;
        biometricPrompt.authenticate(new CancellationSignal(), executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                aThis.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SplashScreen.this, "Gagal Memuat...", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                aThis.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SplashScreen.this, "Berhasil Masuk", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SplashScreen.this, MainActivity.class));
                        finish();
                    }
                });
            }

        });

    }

    private final InstallStateUpdatedListener installStateUpdatedListener = new
    InstallStateUpdatedListener() {
        @Override
        public void onStateUpdate(@NonNull InstallState state) {
            if (state.installStatus() == InstallStatus.DOWNLOADED){
                //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
                popupSnackbarForCompleteUpdate();
            } else if (state.installStatus() == InstallStatus.INSTALLED){
                if (mAppUpdateManager != null){
                    mAppUpdateManager.unregisterListener(installStateUpdatedListener);
                }

            } else {
                Log.i(TAG, "InstallStateUpdatedListener: state: " + state.installStatus());
            }
        }
    };
    private void popupSnackbarForCompleteUpdate() {
        Snackbar snackbar =
                Snackbar.make(
                        findViewById(R.id.content),
                        "New app is ready!",
                        Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction("Install", view -> {
            if (mAppUpdateManager != null){
                mAppUpdateManager.completeUpdate();
            }
        });


        snackbar.setActionTextColor(getResources().getColor(R.color.greenPrimary));
        snackbar.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_APP_UPDATE) {
            if (resultCode != RESULT_OK) {
                Log.e(TAG, "onActivityResult: app download failed");
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAppUpdateManager = AppUpdateManagerFactory.create(this);

        mAppUpdateManager.registerListener(installStateUpdatedListener);

        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE /*AppUpdateType.IMMEDIATE*/)){

                try {
                    mAppUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo, AppUpdateType.FLEXIBLE /*AppUpdateType.IMMEDIATE*/, SplashScreen.this, RC_APP_UPDATE);

                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }

            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED){
                //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
                popupSnackbarForCompleteUpdate();
            } else {
                Log.e(TAG, "checkForAppUpdateAvailability: something else");
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAppUpdateManager != null) {
            mAppUpdateManager.unregisterListener(installStateUpdatedListener);
        }
    }
}