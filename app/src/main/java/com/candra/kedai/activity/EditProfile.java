package com.candra.kedai.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.candra.kedai.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class EditProfile extends AppCompatActivity {

    ImageView iv_profil, btn_backAppbar;
    TextView tv_Appbar;
    EditText et_nama, et_email, et_nohp, et_tgl;
    Spinner sp_jenisKel;
    Button btn_simpan;
    ImageButton btn_editFoto;

    ProgressDialog progressDialog;

    Calendar munculTgl;
    DatePickerDialog.OnDateSetListener date;

    FirebaseAuth fAuth;
    FirebaseUser fUser;

    DatabaseReference dRef;
    StorageReference sRef;

    String userkey_ = "userkey";
    String userkey = "";
    String userkekey = "";

    Uri lokasi_foto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getUserLocal();

        et_nama = findViewById(R.id.et_namaEdit);
        et_email = findViewById(R.id.et_emailEdit);
        et_nohp = findViewById(R.id.et_nohpEdit);
        et_tgl = findViewById(R.id.et_tglEdit);

        iv_profil = findViewById(R.id.iv_fotoEdit);
        sp_jenisKel = findViewById(R.id.sp_jkEdit);
        btn_editFoto = findViewById(R.id.btn_fotoEdit);
        btn_simpan = findViewById(R.id.btn_simpanEdit);
        btn_backAppbar = findViewById(R.id.btn_backEditProf);

        progressDialog = new ProgressDialog(EditProfile.this);
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();

        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("Uid").equalTo(fUser.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    //ambil data
                    final String namalengkap = "" + ds.child("nama_lengkap").getValue().toString();
                    final String email = "" + ds.child("email").getValue().toString();
                    final String nohp = "" + ds.child("no_hp").getValue().toString();
                    final String foto_profil = "" + ds.child("url_images_profil").getValue().toString();
                    final String tgl_lahir = "" + ds.child("tgl_lahir").getValue().toString();
                    final String jenis_kelamin = "" + ds.child("jenis_kelamin").getValue().toString();

                    //set data
                    et_nama.setText(namalengkap);
                    et_email.setText(email);
                    et_nohp.setText(nohp);
                    et_tgl.setText(tgl_lahir);
                    if (jenis_kelamin.equals("Laki-laki")){
                        sp_jenisKel.setSelection(0);
                    } else {
                        sp_jenisKel.setSelection(1);
                    }

                    try {
                        Picasso.with(EditProfile.this).load(foto_profil).centerCrop()
                                .fit().into(iv_profil);

                    } catch (Exception e){
                        Toast.makeText(EditProfile.this, "Error jenis kelamin", Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        munculTgl = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                munculTgl.set(Calendar.YEAR, year);
                munculTgl.set(Calendar.MONTH, month);
                munculTgl.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String formatTgl = "dd-MMMM-yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(formatTgl, Locale.US);
                et_tgl.setText(sdf.format(munculTgl.getTime()));
            }
        };
        et_tgl.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditProfile.this, date,
                        munculTgl.get(Calendar.YEAR),
                        munculTgl.get(Calendar.MONTH),
                        munculTgl.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        btn_editFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(EditProfile.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();
            }
        });

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String nama = et_nama.getText().toString();
                final String email = et_email.getText().toString();
                final String nohp = et_nohp.getText().toString();
                final String jk = sp_jenisKel.getSelectedItem().toString();
                final String tgl_lahir = et_tgl.getText().toString();

                if (nama.isEmpty()){
                    et_nama.setError("Nama tidak boleh kosong");
                    et_nama.setFocusable(true);
                } else if (email.isEmpty()){
                    et_email.setError("Email tidak boleh kosong");
                    et_email.setFocusable(true);
                } else if (nohp.isEmpty()){
                    et_nohp.setError("No hp tidak boleh kosong");
                    et_nohp.setFocusable(true);
                } else {
                    progressDialog.setMessage("Mohon menunggu...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    dRef = FirebaseDatabase.getInstance().getReference("Users").child(fUser.getUid());
                    dRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            dataSnapshot.getRef().child("nama_lengkap").setValue(nama);
                            dataSnapshot.getRef().child("email").setValue(email);
                            dataSnapshot.getRef().child("no_hp").setValue(nohp);
                            dataSnapshot.getRef().child("jenis_kelamin").setValue(jk);
                            dataSnapshot.getRef().child("tgl_lahir").setValue(tgl_lahir);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    sRef = FirebaseStorage.getInstance().getReference("PhotoUsers").child(fUser.getUid());
                    if (lokasi_foto !=null){
                        final StorageReference storageReference = sRef.child(System.currentTimeMillis() + ".kedai" +
                                getFileExtension(lokasi_foto));
                        storageReference.putFile(lokasi_foto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String foto = uri.toString();
                                        dRef.getRef().child("url_images_profil").setValue(foto);

                                        Toast.makeText(EditProfile.this, "Profil berhasil diperbaharui", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });
                            }
                        });
                    } else {
                        Picasso.with(EditProfile.this).load(R.drawable.none_image_profile)
                                .centerCrop().fit().into(iv_profil);
                    }
                }
            }
        });
    }

    String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK){
            assert data !=null;
            lokasi_foto = data.getData();
            Picasso.with(this).load(lokasi_foto).centerCrop()
                    .fit().into(iv_profil);

        } else {
            Picasso.with(this).load(R.drawable.none_image_profile).centerCrop()
                    .fit().into(iv_profil);
        }
    }


    public void getUserLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(userkey_, MODE_PRIVATE);
        userkekey = sharedPreferences.getString(userkey, "");
    }
}