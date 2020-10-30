package com.candra.kedai.api;

import com.candra.kedai.model.address.Data;
import com.candra.kedai.model.address.UniqueCode;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("poe")
    Call<UniqueCode> getUniqueCode();

    @GET("{code}/m/wilayah/provinsi")
    Call<Data> getListProvinsi(@Path("code")String code);

    @GET("{code}/m/wilayah/kabupaten")
    Call<Data> getListKabupaten(@Path("code")String code, @Query("idpropinsi") long idProv);

    @GET("{code}/m/wilayah/kecamatan")
    Call<Data>getListKecamatan(@Path("code")String code, @Query("idkabupaten") long idKab);

    @GET("{code}/m/wilayah/kelurahan")
    Call<Data>getListKelurahan(@Path("code")String code, @Query("idkecamatan")long idKec);


}
