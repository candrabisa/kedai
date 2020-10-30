package com.candra.kedai.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import com.candra.kedai.R;
import com.candra.kedai.api.Api;
import com.candra.kedai.api.ApiService;
import com.candra.kedai.helper.RetrofitConfig;
import com.candra.kedai.model.address.Data;
import com.candra.kedai.model.address.Region;
import com.candra.kedai.model.address.UniqueCode;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

    SearchableSpinner sProvinsi, sKota, sKecamatan, sKelurahan;

    CircularProgressButton btn_register;
    ImageView btn_back, iv_foto;
    ImageButton btn_addFoto;
    ListView listView;
    EditText et_alamatLengkap, etFullname;

    AlertDialog dialog;
    ProgressDialog progressDialog;

    private List<String>listProvinsi = new ArrayList<>();
    List<Region> provinsiItems = new ArrayList<>();

    private List<String> listKota = new ArrayList<>();
    List<Region> kotaItems = new ArrayList<>();

    private List<String> listKecataman = new ArrayList<>();
    List<Region> kecamatanItems = new ArrayList<>();

    private List<String> listKelurahan = new ArrayList<>();
    List<Region> kelurahanItems = new ArrayList<>();

    private String alamat;

    DatabaseReference dRef;
    StorageReference sRef;

    Uri lokasi_foto;
    Integer max_foto = 1;

    String userkey_ = "userkey";
    String userkey = "";
    String userkekey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_two);
        getUserLokal();

        etFullname = findViewById(R.id.etNamaLengkap_Reg);
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
                btn_register.startAnimation();


                dRef = FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(userkekey);
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
                                    dRef.getRef().child("provinsi").setValue(sProvinsi.getSelectedItem().toString());
                                    dRef.getRef().child("kab_kota").setValue(sKota.getSelectedItem().toString());
                                    dRef.getRef().child("kecamatan").setValue(sKecamatan.getSelectedItem().toString());
                                    dRef.getRef().child("kelurahan").setValue(sKelurahan.getSelectedItem().toString());
                                    dRef.getRef().child("nama_lengkap").setValue(etFullname.getText().toString());
                                    dRef.getRef().child("alamat_lengkap").setValue(et_alamatLengkap.getText().toString());
                                }
                            }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Intent intent = new Intent(RegisterTwo.this, SuccessRegister.class);
                                    startActivity(intent);
                                    btn_register.stopAnimation();
                                }
                            });
                        }
                    });
                }


            }
        });

        btn_addFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pilihFoto();
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

                listProvinsi.add(0, "Silahkan dipilih");
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
                            long idProv = provinsiItems.get(i-1).getId();
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
                    listKota.add(kotaItems.get(kot).getName().trim());
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
                listKecataman.add(0, "Silahkan pilih");
                for (int kec = 0; kec < kecamatanItems.size(); kec++){
                    listKecataman.add(kecamatanItems.get(kec).getName());
                }
                final ArrayAdapter<String> arrayList = new ArrayAdapter<String>(RegisterTwo.this, R.layout.io_spinner, listKecataman);
                arrayList.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                sKecamatan.setAdapter(arrayList);

                sKecamatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (!sKota.getSelectedItem().toString().equals("Silahkan pilih")) {
                            long idKec = kecamatanItems.get(i - 1).getId();
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

    public void pilihFoto(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, max_foto);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == max_foto && resultCode == RESULT_OK && data != null && data.getData() !=null){
            lokasi_foto = data.getData();
            Picasso.with(this).load(lokasi_foto)
                    .centerCrop().fit().into(iv_foto);
        }

    }

    public void getUserLokal(){
        SharedPreferences sPref = getSharedPreferences(userkey_, MODE_PRIVATE);
        userkekey = sPref.getString(userkey, "");
    }
}