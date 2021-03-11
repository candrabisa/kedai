package com.candra.kedai.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.candra.kedai.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class PaymentMethod extends AppCompatActivity implements BillingProcessor.IBillingHandler {

    RadioButton rb_transferBank, rb_minimarket, rb_gplay;
    ImageView btn_back, iv_expandTransfer, iv_expandMinimarket, iv_expandGPlay;
    TextView tv_TagihanPayment,tv_biayaAdmin, tv_totalTagihan, tv_kodeVoucherNoValid;
    EditText et_paymentPakaiVoucher;
    RelativeLayout rl_transferBank ,rl_minimarket, rl_googlePlay;
    ConstraintLayout constraint_transferBank, constraint_minimarket, constraint_gPlay;
    Button btn_bayarTagihan, btn_paymentPakaiVoucher;

    Integer nomorTransaksi = new Random().nextInt();
    String tanggalNow, batas_pembayaran;

    BillingProcessor bp;

    FirebaseUser fUser;
    DatabaseReference dRef;

    Integer nominal_topup = 0;
    Integer biaya_admin = 0;
    Integer total_pembayaran = 0;

    Animation animRotateMore, animRorateLess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        fUser = FirebaseAuth.getInstance().getCurrentUser();

//        bp = new BillingProcessor(this, ,this);
//        bp.initialize();

        animRotateMore = AnimationUtils.loadAnimation(this, R.anim.rotate_expand_down);
        animRorateLess = AnimationUtils.loadAnimation(this, R.anim.rotate_expand_up);

        rl_transferBank = findViewById(R.id.rl_transferBank);
        rl_minimarket = findViewById(R.id.rl_minimarket);
        rl_googlePlay = findViewById(R.id.rl_googlePlay);

        iv_expandTransfer = findViewById(R.id.iv_expandTransferBank);
        iv_expandMinimarket = findViewById(R.id.iv_expandMinimarket);
        iv_expandGPlay = findViewById(R.id.iv_expandGPlay);

        tv_TagihanPayment = findViewById(R.id.tv_TagihanPayment);
        tv_biayaAdmin = findViewById(R.id.tv_biayaAdmin);
        tv_totalTagihan = findViewById(R.id.tv_totalTagihan);
        tv_kodeVoucherNoValid = findViewById(R.id.tv_kodeVoucherNoValid);

        et_paymentPakaiVoucher = findViewById(R.id.et_paymentPakaiVoucher);

        btn_back = findViewById(R.id.btn_backPaymentMethod);
        btn_bayarTagihan = findViewById(R.id.btn_bayarTagihanPayment);
        btn_paymentPakaiVoucher = findViewById(R.id.btn_paymentPakaiVoucher);

        rb_transferBank = findViewById(R.id.rb_tranferBank);
        rb_minimarket = findViewById(R.id.rb_minimarket);
        rb_gplay = findViewById(R.id.rb_googlePlay);

        constraint_transferBank = findViewById(R.id.constraint_transferBank);
        constraint_minimarket = findViewById(R.id.constraint_minimarket);
        constraint_gPlay = findViewById(R.id.constraint_googlePlay);

        rl_transferBank.setVisibility(View.GONE);
        rl_minimarket.setVisibility(View.GONE);
        rl_googlePlay.setVisibility(View.GONE);

        Bundle bundle = getIntent().getExtras();
        nominal_topup = bundle.getInt("nominal_topup");
        tv_TagihanPayment.setText("Rp. "+ nominal_topup);
        tv_totalTagihan.setText("Rp. "+nominal_topup);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date besok = calendar.getTime();
        tanggalNow = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(new Date());
        batas_pembayaran = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(besok);

        tv_kodeVoucherNoValid.setVisibility(View.GONE);

        btn_paymentPakaiVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String kode_voucher = et_paymentPakaiVoucher.getText().toString();

                if (kode_voucher.isEmpty()){
                    Toast.makeText(PaymentMethod.this, "Kode voucher belum diisi", Toast.LENGTH_SHORT).show();
                } else {
                    tv_kodeVoucherNoValid.setVisibility(View.VISIBLE);
                    tv_kodeVoucherNoValid.setError("Error");
                    tv_kodeVoucherNoValid.setText("Kode voucher tidak valid");
                }
            }
        });

        btn_bayarTagihan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dRef = FirebaseDatabase.getInstance().getReference("TransaksiSaldo").child(fUser.getUid()).child("TSKD" + nomorTransaksi);
                dRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentMethod.this);
                        builder.setTitle("Lanjutkan Pembayaran");
                        builder.setMessage("Apakah kamu yakin ingin melanjutkan pembayaran?");
                        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.setPositiveButton("Lanjutkan", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(PaymentMethod.this, StatusPayment.class);
                                if (rb_transferBank.isChecked()){
                                    snapshot.getRef().child("nomor_transaksi").setValue("TSKD"+nomorTransaksi.toString());
                                    snapshot.getRef().child("waktu_transaksi").setValue(tanggalNow);
                                    snapshot.getRef().child("batas_pembayaran").setValue(batas_pembayaran);
                                    snapshot.getRef().child("nominal_transaksi").setValue(nominal_topup);
                                    snapshot.getRef().child("biaya_admin").setValue(biaya_admin);
                                    snapshot.getRef().child("total_pembayaran").setValue(total_pembayaran);
                                    snapshot.getRef().child("status_transaksi").setValue("Belum Dibayar");
                                    snapshot.getRef().child("metode_pembayaran").setValue("Transfer Bank");

                                    intent.putExtra("nomorTransaksi", "TSKD" +nomorTransaksi);
                                    startActivity(intent);
                                    finish();
                                } else if (rb_minimarket.isChecked()){
                                    snapshot.getRef().child("nomor_transaksi").setValue("TSKD"+nomorTransaksi.toString());
                                    snapshot.getRef().child("waktu_transaksi").setValue(tanggalNow);
                                    snapshot.getRef().child("batas_pembayaran").setValue(batas_pembayaran);
                                    snapshot.getRef().child("nominal_transaksi").setValue(nominal_topup);
                                    snapshot.getRef().child("biaya_admin").setValue(biaya_admin);
                                    snapshot.getRef().child("total_pembayaran").setValue(total_pembayaran);
                                    snapshot.getRef().child("status_transaksi").setValue("Belum Dibayar");
                                    snapshot.getRef().child("metode_pembayaran").setValue("Minimarket");

                                    intent.putExtra("nomorTransaksi", "TSKD" +nomorTransaksi);
                                    startActivity(intent);
                                    finish();
                                } else if (rb_gplay.isChecked()){
                                    snapshot.getRef().child("nomor_transaksi").setValue("TSKD"+nomorTransaksi.toString());
                                    snapshot.getRef().child("waktu_transaksi").setValue(tanggalNow);
                                    snapshot.getRef().child("batas_pembayaran").setValue(batas_pembayaran);
                                    snapshot.getRef().child("nominal_transaksi").setValue(nominal_topup);
                                    snapshot.getRef().child("biaya_admin").setValue(biaya_admin);
                                    snapshot.getRef().child("total_pembayaran").setValue(total_pembayaran);
                                    snapshot.getRef().child("status_transaksi").setValue("Belum Dibayar");
                                    snapshot.getRef().child("metode_pembayaran").setValue("Google Play");

                                    intent.putExtra("nomorTransaksi", "TSKD" +nomorTransaksi);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(PaymentMethod.this, "Kamu belum memilih metode pembayaran", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder.show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        rb_transferBank.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rb_transferBank.isChecked()){
                    expandTransferBank();
                } else {
                    collapseTransferBank();
                }
            }
        });

        rb_minimarket.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rb_minimarket.isChecked()){
                    expandMinimarket();
                } else {
                    collapseMinimarket();
                }
            }
        });

        rb_gplay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rb_gplay.isChecked()){
                    expandGooglePlay();
                } else {
                    collapseGooglePlay();
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void expandGooglePlay() {
        rl_googlePlay.setVisibility(View.VISIBLE);
        rb_transferBank.setChecked(false);
        rb_minimarket.setChecked(false);
        constraint_gPlay.setBackgroundResource(R.color.whitePrimary);

        final int finalHeight = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int finalWidth = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        rl_googlePlay.measure(finalWidth, finalHeight);

        ValueAnimator valueAnimator = slideAnimatorGPlay(0, rl_googlePlay.getMeasuredHeight());
        valueAnimator.start();

        iv_expandGPlay.startAnimation(animRotateMore);
        iv_expandGPlay.setImageResource(R.drawable.ic_baseline_expand_less_24);

        biaya_admin = 0;
        total_pembayaran = nominal_topup+biaya_admin;
        tv_biayaAdmin.setText("Rp. " + biaya_admin);
        tv_totalTagihan.setText("Rp. "+total_pembayaran);

    }

    private void collapseGooglePlay() {
        final int finalHeight = rl_googlePlay.getHeight();
        ValueAnimator valueAnimator = slideAnimatorGPlay(finalHeight, 0);
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                constraint_gPlay.setBackgroundResource(R.color.grayPrimary);
                iv_expandGPlay.startAnimation(animRorateLess);
                iv_expandGPlay.setImageResource(R.drawable.ic_baseline_expand_more_24);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                rl_googlePlay.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        valueAnimator.start();
    }

    private ValueAnimator slideAnimatorGPlay(int start, int end){
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = rl_googlePlay.getLayoutParams();
                layoutParams.height = value;
                rl_googlePlay.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    private void expandTransferBank() {
        rl_transferBank.setVisibility(View.VISIBLE);
        rb_minimarket.setChecked(false);
        rb_gplay.setChecked(false);
        constraint_transferBank.setBackgroundResource(R.color.whitePrimary);

        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        rl_transferBank.measure(widthSpec, heightSpec);

        ValueAnimator valueAnimator = slideAnimatorTransferBank(0, rl_transferBank.getMeasuredHeight());
        valueAnimator.start();

        iv_expandTransfer.startAnimation(animRotateMore);
        iv_expandTransfer.setImageResource(R.drawable.ic_baseline_expand_less_24);

        biaya_admin = 0;
        total_pembayaran = nominal_topup+biaya_admin;
        tv_biayaAdmin.setText("Rp. " + biaya_admin);
        tv_totalTagihan.setText("Rp. "+total_pembayaran);

    }

    private void collapseTransferBank(){
        final int finalHeight = rl_transferBank.getHeight();
        ValueAnimator valueAnimator = slideAnimatorTransferBank(finalHeight, 0);
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                constraint_transferBank.setBackgroundResource(R.color.grayPrimary);
                iv_expandTransfer.startAnimation(animRorateLess);
                iv_expandTransfer.setImageResource(R.drawable.ic_baseline_expand_more_24);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                rl_transferBank.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();
    }

    private ValueAnimator slideAnimatorTransferBank(int start, int end){
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = rl_transferBank.getLayoutParams();
                layoutParams.height = value;
                rl_transferBank.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    private void expandMinimarket() {
        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        rl_minimarket.measure(widthSpec, heightSpec);

        ValueAnimator valueAnimator = slideAnimatorMinimarket(0, rl_minimarket.getMeasuredHeight());
        valueAnimator.start();

        rl_minimarket.setVisibility(View.VISIBLE);
        rb_transferBank.setChecked(false);
        rb_gplay.setChecked(false);
        constraint_minimarket.setBackgroundResource(R.color.whitePrimary);

        iv_expandMinimarket.startAnimation(animRotateMore);
        iv_expandMinimarket.setImageResource(R.drawable.ic_baseline_expand_less_24);

        biaya_admin = 2500;
        total_pembayaran = nominal_topup+biaya_admin;
        tv_biayaAdmin.setText("Rp. " + biaya_admin);
        tv_totalTagihan.setText("Rp. "+total_pembayaran);

    }

    private void collapseMinimarket(){
        final int finalHeight = rl_minimarket.getHeight();
        ValueAnimator valueAnimator = slideAnimatorMinimarket(finalHeight, 0);
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                constraint_minimarket.setBackgroundResource(R.color.grayPrimary);
                iv_expandMinimarket.startAnimation(animRorateLess);
                iv_expandMinimarket.setImageResource(R.drawable.ic_baseline_expand_more_24);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                rl_minimarket.setVisibility(View.GONE);
                tv_biayaAdmin.setText("Rp. -");
                tv_totalTagihan.setText("Rp. " + nominal_topup);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();
    }

    private ValueAnimator slideAnimatorMinimarket(int start, int end){
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = rl_minimarket.getLayoutParams();
                layoutParams.height = value;
                rl_minimarket.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        if (bp != null){
            bp.release();
        }
        super.onDestroy();
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        Toast.makeText(this, "Top-up Berhasil", Toast.LENGTH_SHORT).show();
        //update ke firebase

    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        Toast.makeText(this, "Gagal top-up", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBillingInitialized() {

    }
}