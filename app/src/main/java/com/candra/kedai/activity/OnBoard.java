package com.candra.kedai.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.candra.kedai.R;
import com.hololo.tutorial.library.Step;
import com.hololo.tutorial.library.TutorialActivity;

public class OnBoard extends TutorialActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFinishText("Get Started"); // Finish button text
        setCancelText("Keluar"); // Cancel button text

        //slide 1
        addFragment(new Step.Builder().setTitle("ES nya seger-seger lhoo!")
                .setContent("Banyak jenis dan varian rasa yang bisa dipilih sesuai MOOD kamu. Kalo lagi bete, cobain deh. Hemppp")
                .setBackgroundColor(Color.parseColor("#983257")) // int background color
                .setDrawable(R.drawable.onboard1) // int top drawable
//                .setSummary("Next ke makanan")
                .build());

        //slide 2
        addFragment(new Step.Builder().setTitle("Pesen makan juga oke")
                .setContent("Eits.. kalo laper jangan ditahan ya. nanti kalo kamu sakit, siapa yang nyakitin aku. Eeaaaaa tarik sis")
                .setBackgroundColor(Color.parseColor("#8D198F")) // int background color
                .setDrawable(R.drawable.onboard2) // int top drawable
//                .setSummary("Lanjut kamu akan terkejut")
                .build());

        //slide 3
        addFragment(new Step.Builder().setTitle("Beli pake combo lebih hemat")
                .setContent("Mau lebih hemat? tenang ya, kami udah siapin paket combo biar dompet kamu tetap tebel sampe gajian. Josss")
                .setBackgroundColor(Color.parseColor("#182657")) // int background color
                .setDrawable(R.drawable.onboard3) // int top drawable
//                .setSummary("This is summary")
                .build());

        //slide 4
        addFragment(new Step.Builder().setTitle("Jajan banyak, dapet promo")
                .setContent("Makin kamu sering jajan di Kedai. Maka akan dikasih voucher promo juga. Wah Baik banget kan kedai. Uwwu (~,~)")
                .setBackgroundColor(Color.parseColor("#692B2B")) // int background color
                .setDrawable(R.drawable.onboard4) // int top drawable
//                .setSummary("This is summary")
                .build());
    }

    @Override
    public void finishTutorial() {
        startActivity(new Intent(OnBoard.this, GetStarted.class));
        finish();
    }

    @Override
    public void cancelTutorial() {
        finish();
    }
}