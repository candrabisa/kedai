package com.candra.kedai.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.candra.kedai.R;
import com.candra.kedai.api.Api;
import com.candra.kedai.api.ApiService;
import com.candra.kedai.helper.RetrofitConfig;
import com.candra.kedai.model.address.Data;
import com.candra.kedai.model.address.Region;
import com.candra.kedai.model.address.UniqueCode;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterTwo extends AppCompatActivity {

    SearchableSpinner sJenisAlamat, sProvinsi, sKota, sKecamatan, sKelurahan;

    CircularProgressButton btn_register;
    ImageView btn_back, iv_foto;
    ImageButton btn_addFoto;
    EditText et_alamatLengkap;

    ProgressDialog progressDialog;

    private List<String>listProvinsi = new ArrayList<>();
    List<Region> provinsiItems = new ArrayList<>();

    private List<String> listKota = new ArrayList<>();
    List<Region> kotaItems = new ArrayList<>();

    private List<String> listKecataman = new ArrayList<>();
    List<Region> kecamatanItems = new ArrayList<>();

    private List<String> listKelurahan = new ArrayList<>();
    List<Region> kelurahanItems = new ArrayList<>();

    DatabaseReference dRef;
    StorageReference sRef;

    Uri lokasi_foto;

    String userkey_ = "userkey";
    String userkey = "";
    String userkekey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_two);
        getUserLokal();

        sJenisAlamat = findViewById(R.id.etJenisAlamat_Reg);
        et_alamatLengkap = findViewById(R.id.et_alamatReg);
        sProvinsi = findViewById(R.id.et_province);
        sKota = findViewById(R.id.et_kota);
        sKecamatan = findViewById(R.id.et_kecamatan);
        sKelurahan = findViewById(R.id.et_kelurahan);
        iv_foto = findViewById(R.id.iv_fotoReg);
        btn_register = findViewById(R.id.btnReg2);
        btn_back = findViewById(R.id.iv_kembaliReg2);
        btn_addFoto = findViewById(R.id.btn_addPhotoReg);

        ambilUniqueCode();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    String jenis_alamat = sJenisAlamat.getSelectedItem().toString();
                    String alamat = et_alamatLengkap.getText().toString();
                    String provinsi = sProvinsi.getSelectedItem().toString();
                    String kota = sKota.getSelectedItem().toString();
                    String kecamatan = sKecamatan.getSelectedItem().toString();
                    String kelurahan = sKelurahan.getSelectedItem().toString();


                    if (jenis_alamat.isEmpty()){
                        Toast.makeText(RegisterTwo.this, "Jenis alamat belum dipilih", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (sProvinsi.equals("")){
                        Toast.makeText(RegisterTwo.this, "Provinsi belum dipilih", Toast.LENGTH_SHORT).show();
                        sProvinsi.setFocusable(true);
                        sProvinsi.setTitle("Provinsi belum dipilih");
                        return;
                    } else if (sKota.equals("")){
                        sKota.setFocusable(true);
                        sKota.setTitle("Kota belum diisi");
                        return;
                    } else if (sKecamatan.equals("")){
                        sKecamatan.setTitle("Kecamatan belum diisi");
                        sKecamatan.setFocusable(true);
                        return;
                    } else if (sKelurahan.equals("")){
                        sKelurahan.setTitle("Kelurahan belum diisi");
                        sKelurahan.setFocusable(true);
                        return;
                    } else if (alamat.isEmpty()) {
                        et_alamatLengkap.setError("Alamat belum diisi");
                        et_alamatLengkap.setFocusable(true);
                        return;

                    } else {
                        progressDialog = new ProgressDialog(RegisterTwo.this);
                        progressDialog.setMessage("Mohon menunggu...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        dRef = FirebaseDatabase.getInstance().getReference().child("Users")
                                .child(userkekey);
                        dRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                dataSnapshot.getRef().child("provinsi").setValue(provinsi);
                                dataSnapshot.getRef().child("kab_kota").setValue(kota);
                                dataSnapshot.getRef().child("kecamatan").setValue(kecamatan);
                                dataSnapshot.getRef().child("kelurahan").setValue(kelurahan);
                                dataSnapshot.getRef().child("jenis_alamat").setValue(jenis_alamat);
                                dataSnapshot.getRef().child("alamat_lengkap").setValue(alamat);
                                dataSnapshot.getRef().child("saldo").setValue(60000);
                                dataSnapshot.getRef().child("voucher").setValue(0);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        sRef = FirebaseStorage.getInstance().getReference().child("PhotoUsers").child(userkekey);
                        if (lokasi_foto !=null){
                            final StorageReference storageReference = sRef.child(System.currentTimeMillis() + "." +
                                    getFileExtension(lokasi_foto));
                            storageReference.putFile(lokasi_foto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String uri_photo = uri.toString();
                                            dRef.getRef().child("url_images_profil").setValue(uri_photo);

                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Intent intent = new Intent(RegisterTwo.this, SuccessRegister.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                }

                            });
                        }
                    }
                } catch (Exception e){
                    Toast.makeText(RegisterTwo.this, "Data regiter belum lengkap", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn_addFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(RegisterTwo.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void ambilUniqueCode(){
        ApiService apiService = RetrofitConfig.getRetrofitService().create(ApiService.class);
        Call<UniqueCode> ambil = apiService.getUniqueCode();
        ambil.enqueue(new Callback<UniqueCode>() {
            @Override
            public void onResponse(Call<UniqueCode> call, Response<UniqueCode> response) {
                String code = "MeP7c5ne" + response.body().getUniqueCode();
                ambilSpinnerProvinsi(code);
            }

            @Override
            public void onFailure(Call<UniqueCode> call, Throwable t) {

            }
        });
    }

    private void ambilSpinnerProvinsi(final String code) {
        listProvinsi.clear();
        ApiService apiService = RetrofitConfig.getRetrofitService().create(ApiService.class);
        Call<Data> tampilData = apiService.getListProvinsi(code);
        tampilData.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                Log.v("Mantap", "Json : " + new Gson().toJson(response));

                provinsiItems = response.body().getData();

//                listProvinsi.add(0, "Silahkan dipilih");
                for (int p = 0; p < provinsiItems.size(); p++){
                    listProvinsi.add(provinsiItems.get(p).getName().trim());
                }
                final ArrayAdapter<String> arrayList = new ArrayAdapter<>(RegisterTwo.this, R.layout.io_spinner, listProvinsi);
                arrayList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sProvinsi.setAdapter(arrayList);
                sProvinsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (!sProvinsi.getSelectedItem().toString().equals("Dipilih dulu gan")){
                            long idProv = provinsiItems.get(i).getId();
                            ambilSpinnerKota(code, idProv);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {

            }

        });
    }

    private void ambilSpinnerKota(final String code, final long idProv){
        listKota.clear();
        sKecamatan.setAdapter(null);
        sKelurahan.setAdapter(null);
        ApiService apiService = RetrofitConfig.getRetrofitService().create(ApiService.class);
        Call<Data> tampilData = apiService.getListKabupaten(code, idProv);
        tampilData.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                kotaItems = response.body().getData();
                for (int kot = 0; kot < kotaItems.size(); kot++){
                    listKota.add(kotaItems.get(kot).getName());
                }
                final ArrayAdapter<String> arrayList = new ArrayAdapter<String>(RegisterTwo.this, R.layout.io_spinner, listKota);
                arrayList.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                sKota.setAdapter(arrayList);

                sKota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (!sKota.getSelectedItem().toString().equals("Silahkan pilih")) {
                            long idKota = kotaItems.get(i).getId();
                            tampilListKecamatan(code, idKota);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {

            }
        });
    }

    private void tampilListKecamatan( final String code, final long idKab) {
        listKecataman.clear();
        sKelurahan.setAdapter(null);
        ApiService apiService = RetrofitConfig.getRetrofitService().create(ApiService.class);
        Call<Data> tampilData = apiService.getListKecamatan(code, idKab);
        tampilData.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                kecamatanItems = response.body().getData();
//                listKecataman.add(0, "Silahkan pilih");
                for (int kec = 0; kec < kecamatanItems.size(); kec++){
                    listKecataman.add(kecamatanItems.get(kec).getName().trim());
                }
                final ArrayAdapter<String> arrayList = new ArrayAdapter<String>(RegisterTwo.this, R.layout.io_spinner, listKecataman);
                arrayList.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                sKecamatan.setAdapter(arrayList);

                sKecamatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (!sKecamatan.getSelectedItem().toString().equals("Silahkan pilih")) {
                            long idKec = kecamatanItems.get(i).getId();
                            tampilListKelurahan(code, idKec);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {

            }
        });
    }

    private void tampilListKelurahan(String code, long idKec) {
        listKelurahan.clear();
        ApiService apiService = RetrofitConfig.getRetrofitService().create(ApiService.class);
        Call<Data> tampilData = apiService.getListKelurahan(code, idKec);
        tampilData.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                kelurahanItems = response.body().getData();
                for (int kel = 0; kel < kelurahanItems.size(); kel++){
                    listKelurahan.add(kelurahanItems.get(kel).getName());
                }
                final ArrayAdapter<String> arrayList = new ArrayAdapter<String>(RegisterTwo.this, R.layout.io_spinner, listKelurahan);
                arrayList.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                sKelurahan.setAdapter(arrayList);


            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {

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
            Picasso.get().load(lokasi_foto)
                    .centerCrop().fit().into(iv_foto);
        } else {
            Picasso.get().load(R.drawable.none_image_profile).centerCrop()
                    .fit().into(iv_foto);
        }

    }



    public void getUserLokal(){
        SharedPreferences sPref = getSharedPreferences(userkey_, MODE_PRIVATE);
        userkekey = sPref.getString(userkey, "");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegisterTwo.this, GetStarted.class);
        startActivity(intent);
        finish();
    }
}