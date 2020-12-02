package com.candra.kedai.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.candra.kedai.R;
import com.candra.kedai.api.Api;
import com.candra.kedai.api.ApiService;
import com.candra.kedai.helper.RetrofitConfig;
import com.candra.kedai.model.address.Data;
import com.candra.kedai.model.address.Region;
import com.candra.kedai.model.address.UniqueCode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.Change;
import com.google.gson.Gson;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeAddress extends AppCompatActivity {

    SearchableSpinner sp_jenisAlamat, sp_provinsi, sp_kabKota, sp_kecamatan, sp_kelurahan;
    EditText et_alamatLengkap;
    Button btn_simpanAlamat;
    ImageView btn_backUbahAlamat;

    List<String> listProvinsi = new ArrayList<>();
    List<Region> provinsiItem = new ArrayList<>();

    List<String> listKabKota = new ArrayList<>();
    List<Region> kabKotaItem = new ArrayList<>();

    List<String> listKecamatan = new ArrayList<>();
    List<Region> kecamatanItem = new ArrayList<>();

    List<String> listKelurahan = new ArrayList<>();
    List<Region> kelurahanItem = new ArrayList<>();

    DatabaseReference dRef;
    FirebaseUser fUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_address);

        ambilUniqueCode();

        sp_jenisAlamat = findViewById(R.id.sp_jenisAlamat);
        sp_provinsi = findViewById(R.id.sp_provinsi);
        sp_kabKota = findViewById(R.id.sp_kabKota);
        sp_kecamatan = findViewById(R.id.sp_kecamatan);
        sp_kelurahan = findViewById(R.id.sp_kelurahan);

        et_alamatLengkap = findViewById(R.id.et_alamatLengkap);

        btn_backUbahAlamat = findViewById(R.id.btn_backUbahAlamat);
        btn_simpanAlamat = findViewById(R.id.btn_simpanAlamat);

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        btn_simpanAlamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String jenis_alamat = sp_jenisAlamat.getSelectedItem().toString();
                    String provinsi = sp_provinsi.getSelectedItem().toString();
                    String kabKota = sp_kabKota.getSelectedItem().toString();
                    String kecamatan = sp_kecamatan.getSelectedItem().toString();
                    String kelurahan = sp_kelurahan.getSelectedItem().toString();
                    String alamat_lengkap = et_alamatLengkap.getText().toString();

                    if (alamat_lengkap.isEmpty()){
                        et_alamatLengkap.setError("Alamat lengkap belum diisi");
                    } else {
                        ProgressDialog progressDialog = new ProgressDialog(ChangeAddress.this);
                        progressDialog.setMessage("Mohon menunggu...");
                        progressDialog.setCancelable(true);
                        progressDialog.show();

                        dRef = FirebaseDatabase.getInstance().getReference("Users").child(fUser.getUid());
                        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                snapshot.getRef().child("jenis_alamat").setValue(jenis_alamat);
                                snapshot.getRef().child("provinsi").setValue(provinsi);
                                snapshot.getRef().child("kab_kota").setValue(kabKota);
                                snapshot.getRef().child("kecamatan").setValue(kecamatan);
                                snapshot.getRef().child("kelurahan").setValue(kelurahan);
                                snapshot.getRef().child("alamat_lengkap").setValue(alamat_lengkap);

                                progressDialog.dismiss();
                                Intent intent = new Intent(ChangeAddress.this, Address.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(ChangeAddress.this, "Gagal memperbarui alamat, coba lagi", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e){

                }

            }
        });
    }

    private void ambilUniqueCode(){
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

    private void ambilSpinnerProvinsi(String code) {
        listProvinsi.clear();
        ApiService apiService = RetrofitConfig.getRetrofitService().create(ApiService.class);
        Call<Data> tampilData = apiService.getListProvinsi(code);
        tampilData.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                Log.v("Mantap", "Json : " + new Gson().toJson(response));
                provinsiItem = response.body().getData();
                for (int p = 0; p < provinsiItem.size(); p++){
                    listProvinsi.add(provinsiItem.get(p).getName().trim());
                }
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ChangeAddress.this, R.layout.io_spinner, listProvinsi);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_provinsi.setAdapter(arrayAdapter);
                sp_provinsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (!sp_provinsi.getSelectedItem().toString().equals("Dipilih dulu gan")){
                            long idProv = provinsiItem.get(position).getId();
                            ambilSpinnerKabKota(code, idProv);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {

            }
        });
    }

    private void ambilSpinnerKabKota(String code, long idProv) {
        listKabKota.clear();
        sp_kecamatan.setAdapter(null);
        sp_kelurahan.setAdapter(null);
        ApiService apiService = RetrofitConfig.getRetrofitService().create(ApiService.class);
        Call<Data> tampilData = apiService.getListKabupaten(code, idProv);
        tampilData.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                kabKotaItem = response.body().getData();
                for (int p = 0; p < kabKotaItem.size(); p++){
                    listKabKota.add(kabKotaItem.get(p).getName().trim());
                }
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ChangeAddress.this, R.layout.io_spinner, listKabKota);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_kabKota.setAdapter(arrayAdapter);
                sp_kabKota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (!sp_kabKota.getSelectedItem().toString().equals("Dipilih dulu gan")){
                            long idKabKota = kabKotaItem.get(position).getId();
                            ambilSpinnerKecamatan(code, idKabKota);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {

            }
        });
    }

    private void ambilSpinnerKecamatan(String code, long idKabKota) {
        listKecamatan.clear();
        sp_kelurahan.setAdapter(null);
        ApiService apiService = RetrofitConfig.getRetrofitService().create(ApiService.class);
        Call<Data> tampilData = apiService.getListKecamatan(code, idKabKota);
        tampilData.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                kecamatanItem = response.body().getData();
                for (int p = 0; p < kecamatanItem.size(); p++){
                    listKecamatan.add(kecamatanItem.get(p).getName().trim());
                }
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ChangeAddress.this, R.layout.io_spinner, listKecamatan);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_kecamatan.setAdapter(arrayAdapter);
                sp_kecamatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (!sp_kecamatan.getSelectedItem().toString().equals("Dipilih dulu gan")){
                            long idKec = kecamatanItem.get(position).getId();
                            ambilSpinnerkelurahan(code, idKec);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {

            }
        });
    }

    private void ambilSpinnerkelurahan(String code, long idKec) {
        listKelurahan.clear();
        ApiService apiService = RetrofitConfig.getRetrofitService().create(ApiService.class);
        Call<Data> tampilData = apiService.getListKelurahan(code, idKec);
        tampilData.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                kelurahanItem = response.body().getData();
                for (int p = 0; p < kelurahanItem.size(); p++){
                    listKelurahan.add(kelurahanItem.get(p).getName().trim());
                }
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ChangeAddress.this, R.layout.io_spinner, listKelurahan);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_kelurahan.setAdapter(arrayAdapter);
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {

            }
        });
    }


}